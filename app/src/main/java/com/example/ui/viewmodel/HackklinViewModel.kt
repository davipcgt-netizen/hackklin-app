package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.curriculum.HackklinCurriculumCatalog
import com.example.data.local.ActivityLogEntity
import com.example.data.local.ChallengeProgressEntity
import com.example.data.local.HackklinDatabase
import com.example.data.local.TopicProgressEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.ChallengeItem
import com.example.data.model.CurriculumLevel
import com.example.data.model.CurriculumTopic
import com.example.data.model.HACKKLIN_RANKS
import com.example.data.model.UserRankInfo
import com.example.data.remote.UserCloudProfileRepository
import com.example.data.repository.CliExecutionResult
import com.example.data.repository.HackklinRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BottomNavTab {
    RUTAS,
    RETOS,
    PERFIL
}

data class UserSessionPrefs(
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val language: AppLanguage = AppLanguage.ES,
    val onboardingCompleted: Boolean = false,
    val selectedInitialLevel: Int = 1,
    val authUid: String? = null,
    val authDisplayName: String? = null,
    val authEmail: String? = null,
    val cloudSyncStatus: String? = null
)

data class HackklinUiState(
    val activeTab: BottomNavTab = BottomNavTab.RUTAS,
    val levels: List<CurriculumLevel> = HackklinCurriculumCatalog.allLevels,
    val challenges: List<ChallengeItem> = HackklinCurriculumCatalog.allChallenges,
    val topicProgressMap: Map<String, TopicProgressEntity> = emptyMap(),
    val challengeProgressMap: Map<String, ChallengeProgressEntity> = emptyMap(),
    val userStats: UserStatsEntity = UserStatsEntity(),
    val recentActivities: List<ActivityLogEntity> = emptyList(),
    val currentRank: UserRankInfo = HACKKLIN_RANKS.first(),
    val nextRank: UserRankInfo? = HACKKLIN_RANKS.getOrNull(1),
    val selectedLevelNumber: Int? = 1,
    val activeTopic: CurriculumTopic? = null,
    val activeChallenge: ChallengeItem? = null,
    val streakWasResetBanner: Boolean = false,
    val hoursRemainingInStreakWindow: Int = 24,
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val language: AppLanguage = AppLanguage.ES,
    val onboardingCompleted: Boolean = false,
    val selectedInitialLevel: Int = 1,
    val authUid: String? = null,
    val authDisplayName: String? = null,
    val authEmail: String? = null,
    val cloudSyncStatus: String? = null
) {
    val isSignedInWithGoogle: Boolean
        get() = !authUid.isNullOrBlank()

    fun isLevelUnlocked(levelNumber: Int): Boolean {
        if (levelNumber <= 1) return true
        val prevLevel = levels.find { it.number == levelNumber - 1 } ?: return false
        return prevLevel.topics.all { topicProgressMap[it.id]?.isFullyCompleted == true }
    }

    fun isTopicUnlocked(topic: CurriculumTopic): Boolean {
        if (!isLevelUnlocked(topic.levelNumber)) return false
        if (topic.orderIndex <= 1) return true
        val level = levels.find { it.number == topic.levelNumber } ?: return false
        val previousTopic = level.topics.find { it.orderIndex == topic.orderIndex - 1 } ?: return true
        return topicProgressMap[previousTopic.id]?.isFullyCompleted == true
    }

    fun getNextActiveTopic(): CurriculumTopic {
        val highestLvlNumber = getHighestUnlockedLevel()
        val activeLevel = levels.find { it.number == highestLvlNumber } ?: levels.first()
        return activeLevel.topics.firstOrNull { topic ->
            topicProgressMap[topic.id]?.isFullyCompleted != true
        } ?: activeLevel.topics.last()
    }

    fun isChallengeUnlockedByIndex(index: Int): Boolean {
        if (index <= 0) return true
        val previousChallenge = challenges.getOrNull(index - 1) ?: return false
        return challengeProgressMap[previousChallenge.id]?.completed == true
    }

    fun getCurrentChainChallenge(): Pair<Int, ChallengeItem> {
        for (i in challenges.indices) {
            val chg = challenges[i]
            val done = challengeProgressMap[chg.id]?.completed == true
            if (!done) {
                return Pair(i + 1, chg)
            }
        }
        return Pair(challenges.size, challenges.last())
    }

    fun getHighestUnlockedLevel(): Int {
        var highest = 1
        for (lvl in levels.sortedBy { it.number }) {
            if (isLevelUnlocked(lvl.number)) {
                highest = lvl.number
            } else {
                break
            }
        }
        return highest
    }

    fun getLevelProgressFraction(levelNumber: Int): Float {
        val level = levels.find { it.number == levelNumber } ?: return 0f
        if (level.topics.isEmpty()) return 0f
        var totalSteps = 0
        var completedSteps = 0
        level.topics.forEach { topic ->
            totalSteps += 3
            val prog = topicProgressMap[topic.id]
            if (prog?.theoryCompleted == true) completedSteps++
            if (prog?.labCompleted == true) completedSteps++
            if (prog?.quizCompleted == true) completedSteps++
        }
        return completedSteps.toFloat() / totalSteps.toFloat()
    }

    fun getOverallProgressPercent(): Int {
        val totalTopics = levels.sumOf { it.topics.size }
        if (totalTopics == 0) return 0
        val completedTopics = levels.sumOf { level ->
            level.topics.count { topicProgressMap[it.id]?.isFullyCompleted == true }
        }
        return ((completedTopics.toFloat() / totalTopics.toFloat()) * 100).toInt()
    }
}

class HackklinViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HackklinDatabase.getInstance(application)
    val repository = HackklinRepository(database.hackklinDao(), application)
    private val cloudRepository: UserCloudProfileRepository? = try {
        UserCloudProfileRepository(application)
    } catch (e: Exception) {
        null
    }

    private val auth: FirebaseAuth? = try {
        Firebase.auth
    } catch (e: Exception) {
        null
    }

    private val _activeTab = MutableStateFlow(BottomNavTab.RUTAS)
    private val _selectedLevelNumber = MutableStateFlow<Int?>(repository.getSelectedInitialLevel())
    private val _activeTopic = MutableStateFlow<CurriculumTopic?>(null)
    private val _activeChallenge = MutableStateFlow<ChallengeItem?>(null)
    private val _streakResetBanner = MutableStateFlow(false)

    private val _sessionPrefs = MutableStateFlow(
        UserSessionPrefs(
            themeMode = repository.getSavedThemeMode(),
            language = repository.getSavedLanguage(),
            onboardingCompleted = false,
            selectedInitialLevel = repository.getSelectedInitialLevel(),
            authUid = auth?.currentUser?.uid,
            authDisplayName = auth?.currentUser?.displayName,
            authEmail = auth?.currentUser?.email
        )
    )

    private var cloudObserveJob: Job? = null

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        _sessionPrefs.value = _sessionPrefs.value.copy(
            authUid = user?.uid,
            authDisplayName = user?.displayName,
            authEmail = user?.email
        )
        if (user != null) {
            startObservingAndSyncingCloudProfile(user)
        } else {
            cloudObserveJob?.cancel()
            cloudObserveJob = null
        }
    }

    init {
        viewModelScope.launch {
            val wasReset = repository.initializeAndVerifyStreak()
            _streakResetBanner.value = wasReset
        }
        auth?.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth?.removeAuthStateListener(authStateListener)
        cloudObserveJob?.cancel()
    }

    private fun startObservingAndSyncingCloudProfile(user: FirebaseUser) {
        val cloudRepo = cloudRepository ?: return
        cloudObserveJob?.cancel()
        cloudObserveJob = viewModelScope.launch {
            // First push/create profile if needed, then observe updates
            pushLocalStateToCloud(user)
            cloudRepo.observeMyProfile()
                .catch { /* logged by handleFirestoreError in repository */ }
                .collect { cloudProfile ->
                    if (cloudProfile != null) {
                        repository.mergeCloudProfileIntoLocal(cloudProfile)
                    }
                }
        }
    }

    private suspend fun pushLocalStateToCloud(user: FirebaseUser) {
        val cloudRepo = cloudRepository ?: return
        val currentState = uiState.value
        val prefs = _sessionPrefs.value
        val result = cloudRepo.saveOrUpdateMyProfile(
            displayName = user.displayName ?: "Agente Hackklin",
            email = user.email ?: "",
            totalXp = currentState.userStats.totalXp,
            currentStreakDays = currentState.userStats.currentStreakDays,
            longestStreakDays = currentState.userStats.longestStreakDays,
            totalCommandsExecuted = currentState.userStats.totalCommandsExecuted,
            selectedInitialLevel = prefs.selectedInitialLevel,
            preferredTheme = prefs.themeMode.code,
            preferredLanguage = prefs.language.code
        )
        if (result.isSuccess) {
            _sessionPrefs.value = _sessionPrefs.value.copy(
                cloudSyncStatus = if (prefs.language == AppLanguage.EN) {
                    "Synced with Google Cloud"
                } else {
                    "Sincronizado con Google Cloud"
                }
            )
        }
    }

    val uiState: StateFlow<HackklinUiState> = combine(
        _activeTab,
        repository.topicProgressFlow,
        repository.challengeProgressFlow,
        repository.userStatsFlow,
        repository.recentActivityFlow
    ) { tab, topicsList, challengesList, stats, activities ->
        val topicMap = topicsList.associateBy { it.topicId }
        val challengeMap = challengesList.associateBy { it.challengeId }
        val safeStats = stats ?: UserStatsEntity()
        val levels = HackklinCurriculumCatalog.allLevels
        val rank = repository.calculateRank(levels, topicMap, safeStats.totalXp)
        val rankIndex = HACKKLIN_RANKS.indexOf(rank)
        val nextRank = HACKKLIN_RANKS.getOrNull(rankIndex + 1)

        val now = System.currentTimeMillis()
        val elapsedMs = if (safeStats.lastActivityTimestamp > 0L) {
            now - safeStats.lastActivityTimestamp
        } else {
            HackklinRepository.TWENTY_FOUR_HOURS_MS
        }
        val remainingMs = (HackklinRepository.TWENTY_FOUR_HOURS_MS - elapsedMs).coerceAtLeast(0L)
        val hoursLeft = (remainingMs / (1000L * 60L * 60L)).toInt()

        HackklinUiState(
            activeTab = tab,
            levels = levels,
            challenges = HackklinCurriculumCatalog.allChallenges,
            topicProgressMap = topicMap,
            challengeProgressMap = challengeMap,
            userStats = safeStats,
            recentActivities = activities,
            currentRank = rank,
            nextRank = nextRank,
            hoursRemainingInStreakWindow = hoursLeft
        )
    }.combine(
        combine(
            _selectedLevelNumber,
            _activeTopic,
            _activeChallenge,
            _streakResetBanner,
            _sessionPrefs
        ) { selectedLvl, topic, challenge, resetBanner, prefs ->
            Quintuple(selectedLvl, topic, challenge, resetBanner, prefs)
        }
    ) { baseState, navExtras ->
        val effectiveLevel = navExtras.first ?: baseState.getHighestUnlockedLevel()
        val prefs = navExtras.fifth
        baseState.copy(
            selectedLevelNumber = effectiveLevel,
            activeTopic = navExtras.second,
            activeChallenge = navExtras.third,
            streakWasResetBanner = navExtras.fourth,
            themeMode = prefs.themeMode,
            language = prefs.language,
            onboardingCompleted = prefs.onboardingCompleted,
            selectedInitialLevel = prefs.selectedInitialLevel,
            authUid = prefs.authUid,
            authDisplayName = prefs.authDisplayName,
            authEmail = prefs.authEmail,
            cloudSyncStatus = prefs.cloudSyncStatus
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HackklinUiState(
            themeMode = repository.getSavedThemeMode(),
            language = repository.getSavedLanguage(),
            onboardingCompleted = false,
            selectedInitialLevel = repository.getSelectedInitialLevel()
        )
    )

    fun setThemeMode(mode: AppThemeMode) {
        repository.saveThemeMode(mode)
        _sessionPrefs.value = _sessionPrefs.value.copy(themeMode = mode)
        syncToCloudIfAuthenticated()
    }

    fun setLanguage(language: AppLanguage) {
        repository.saveLanguage(language)
        _sessionPrefs.value = _sessionPrefs.value.copy(language = language)
        syncToCloudIfAuthenticated()
    }

    fun completeOnboarding(selectedExperienceLevel: Int) {
        val safeLevel = selectedExperienceLevel.coerceIn(1, 8)
        repository.setOnboardingCompleted(completed = true, selectedInitialLevel = safeLevel)
        _sessionPrefs.value = _sessionPrefs.value.copy(
            onboardingCompleted = true,
            selectedInitialLevel = safeLevel
        )
        _selectedLevelNumber.value = 1
        syncToCloudIfAuthenticated()
    }

    fun reopenOnboardingTour() {
        _sessionPrefs.value = _sessionPrefs.value.copy(onboardingCompleted = false)
    }

    fun onGoogleAuthSuccess(user: FirebaseUser) {
        _sessionPrefs.value = _sessionPrefs.value.copy(
            authUid = user.uid,
            authDisplayName = user.displayName,
            authEmail = user.email
        )
        startObservingAndSyncingCloudProfile(user)
    }

    fun onGoogleSignOutComplete() {
        _sessionPrefs.value = _sessionPrefs.value.copy(
            authUid = null,
            authDisplayName = null,
            authEmail = null,
            cloudSyncStatus = null
        )
    }

    fun syncToCloudIfAuthenticated() {
        val user = auth?.currentUser ?: return
        viewModelScope.launch {
            pushLocalStateToCloud(user)
        }
    }

    fun selectTab(tab: BottomNavTab) {
        _activeTab.value = tab
        _activeTopic.value = null
        _activeChallenge.value = null
    }

    fun selectLevel(levelNumber: Int?) {
        _selectedLevelNumber.value = levelNumber
        _activeTopic.value = null
    }

    fun openTopic(topic: CurriculumTopic) {
        _activeTopic.value = topic
    }

    fun closeTopic() {
        _activeTopic.value = null
    }

    fun openChallenge(challenge: ChallengeItem) {
        _activeChallenge.value = challenge
    }

    fun closeChallenge() {
        _activeChallenge.value = null
    }

    fun dismissStreakResetBanner() {
        _streakResetBanner.value = false
    }

    fun markTopicTheoryCompleted(topic: CurriculumTopic) {
        viewModelScope.launch {
            repository.completeTopicTheory(topic.id, topic.levelNumber, topic.title)
            syncToCloudIfAuthenticated()
        }
    }

    fun executeTopicCliCommand(topic: CurriculumTopic, command: String): CliExecutionResult {
        val result = repository.evaluateCliCommand(command, topic.cliLab)
        viewModelScope.launch {
            if (result.missionAccomplished) {
                repository.completeTopicCliLab(topic.id, topic.levelNumber, topic.title, topic.xpReward)
                syncToCloudIfAuthenticated()
            } else if (command.isNotBlank() && !result.isClearCommand) {
                repository.incrementCommandCounter()
            }
        }
        return result
    }

    fun markTopicQuizCompleted(topic: CurriculumTopic, scorePercent: Int) {
        viewModelScope.launch {
            repository.completeTopicQuiz(topic.id, topic.levelNumber, topic.title, scorePercent, topic.xpReward)
            syncToCloudIfAuthenticated()
        }
    }

    fun executeChallengeCliCommand(challenge: ChallengeItem, command: String): CliExecutionResult {
        val scenario = challenge.cliScenario ?: return CliExecutionResult(emptyList())
        val result = repository.evaluateCliCommand(command, scenario)
        viewModelScope.launch {
            if (result.missionAccomplished) {
                repository.completeChallenge(challenge.id, challenge.title, challenge.xpReward, isCli = true)
                syncToCloudIfAuthenticated()
            } else if (command.isNotBlank() && !result.isClearCommand) {
                repository.incrementCommandCounter()
            }
        }
        return result
    }

    fun markChallengeQuizCompleted(challenge: ChallengeItem) {
        viewModelScope.launch {
            repository.completeChallenge(challenge.id, challenge.title, challenge.xpReward, isCli = false)
            syncToCloudIfAuthenticated()
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllLocalProgress()
            _selectedLevelNumber.value = 1
            _activeTopic.value = null
            _activeChallenge.value = null
        }
    }
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
