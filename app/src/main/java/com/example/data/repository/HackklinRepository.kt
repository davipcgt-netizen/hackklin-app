package com.example.data.repository

import android.content.Context
import com.example.data.curriculum.HackklinCurriculumCatalog
import com.example.data.local.ActivityLogEntity
import com.example.data.local.ChallengeProgressEntity
import com.example.data.local.HackklinDao
import com.example.data.local.TopicProgressEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.CliLabScenario
import com.example.data.model.CurriculumLevel
import com.example.data.model.HACKKLIN_RANKS
import com.example.data.model.TerminalLine
import com.example.data.model.TerminalLineType
import com.example.data.model.UserRankInfo
import com.example.data.remote.UserCloudProfile
import kotlinx.coroutines.flow.Flow
import kotlin.math.max

data class CliExecutionResult(
    val newLines: List<TerminalLine>,
    val isClearCommand: Boolean = false,
    val missionAccomplished: Boolean = false
)

class HackklinRepository(
    private val dao: HackklinDao,
    context: Context
) {
    private val localPrefs = context.applicationContext.getSharedPreferences(
        "hackklin_local_storage_prefs",
        Context.MODE_PRIVATE
    )

    val topicProgressFlow: Flow<List<TopicProgressEntity>> = dao.observeAllTopicProgress()
    val challengeProgressFlow: Flow<List<ChallengeProgressEntity>> = dao.observeAllChallengeProgress()
    val userStatsFlow: Flow<UserStatsEntity?> = dao.observeUserStats()
    val recentActivityFlow: Flow<List<ActivityLogEntity>> = dao.observeRecentActivity()

    companion object {
        const val TWENTY_FOUR_HOURS_MS = 24L * 60L * 60L * 1000L
        private const val KEY_THEME_MODE = "preferred_theme_mode"
        private const val KEY_LANGUAGE = "preferred_language"
        private const val KEY_ONBOARDING_DONE = "onboarding_completed"
        private const val KEY_INITIAL_LEVEL = "selected_initial_level"
    }

    fun getSavedThemeMode(): AppThemeMode =
        AppThemeMode.fromCode(localPrefs.getString(KEY_THEME_MODE, AppThemeMode.DARK.code))

    fun saveThemeMode(mode: AppThemeMode) {
        localPrefs.edit().putString(KEY_THEME_MODE, mode.code).apply()
    }

    fun getSavedLanguage(): AppLanguage =
        AppLanguage.fromCode(localPrefs.getString(KEY_LANGUAGE, AppLanguage.ES.code))

    fun saveLanguage(language: AppLanguage) {
        localPrefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }

    fun isOnboardingCompleted(): Boolean =
        localPrefs.getBoolean(KEY_ONBOARDING_DONE, false)

    fun setOnboardingCompleted(completed: Boolean, selectedInitialLevel: Int) {
        localPrefs.edit()
            .putBoolean(KEY_ONBOARDING_DONE, completed)
            .putInt(KEY_INITIAL_LEVEL, selectedInitialLevel.coerceIn(1, 8))
            .apply()
    }

    fun getSelectedInitialLevel(): Int =
        localPrefs.getInt(KEY_INITIAL_LEVEL, 1).coerceIn(1, 8)

    suspend fun mergeCloudProfileIntoLocal(cloud: UserCloudProfile) {
        val current = dao.getUserStatsOnce() ?: UserStatsEntity()
        val merged = current.copy(
            totalXp = max(current.totalXp, cloud.totalXp.toInt()),
            currentStreakDays = max(current.currentStreakDays, cloud.currentStreakDays.toInt()),
            longestStreakDays = max(current.longestStreakDays, cloud.longestStreakDays.toInt()),
            totalCommandsExecuted = max(current.totalCommandsExecuted, cloud.totalCommandsExecuted.toInt())
        )
        dao.upsertUserStats(merged)
        syncPrefs(merged)
    }

    suspend fun initializeAndVerifyStreak(nowMs: Long = System.currentTimeMillis()): Boolean {
        val current = dao.getUserStatsOnce()
        if (current == null) {
            val initial = UserStatsEntity(
                id = 1,
                totalXp = localPrefs.getInt("total_xp", 0),
                currentStreakDays = localPrefs.getInt("streak_days", 0),
                longestStreakDays = localPrefs.getInt("longest_streak", 0),
                lastActivityTimestamp = localPrefs.getLong("last_activity_ts", 0L),
                lastStreakDayEpoch = localPrefs.getLong("last_streak_epoch", 0L)
            )
            val expired = initial.lastActivityTimestamp > 0L &&
                (nowMs - initial.lastActivityTimestamp) > TWENTY_FOUR_HOURS_MS
            val normalized = if (expired) initial.copy(currentStreakDays = 0) else initial
            dao.upsertUserStats(normalized)
            syncPrefs(normalized)
            return expired
        } else {
            val expired = current.lastActivityTimestamp > 0L &&
                (nowMs - current.lastActivityTimestamp) > TWENTY_FOUR_HOURS_MS &&
                current.currentStreakDays > 0
            if (expired) {
                val resetStats = current.copy(currentStreakDays = 0)
                dao.upsertUserStats(resetStats)
                syncPrefs(resetStats)
                return true
            }
            return false
        }
    }

    suspend fun completeTopicTheory(topicId: String, levelNumber: Int, topicTitle: String) {
        val existing = dao.getTopicProgress(topicId) ?: TopicProgressEntity(
            topicId = topicId,
            levelNumber = levelNumber
        )
        val isFirstTime = !existing.theoryCompleted
        val updated = existing.copy(
            theoryCompleted = true,
            completedAtTimestamp = if (existing.labCompleted && existing.quizCompleted) {
                System.currentTimeMillis()
            } else {
                existing.completedAtTimestamp
            }
        )
        dao.upsertTopicProgress(updated)
        if (isFirstTime) {
            recordActivity(
                xpDelta = 30,
                title = "Teoría completada: $topicTitle",
                category = "Nivel $levelNumber • Teoría",
                commandsDelta = 0,
                quizDelta = 0
            )
        }
    }

    suspend fun completeTopicCliLab(topicId: String, levelNumber: Int, topicTitle: String, xpReward: Int) {
        val existing = dao.getTopicProgress(topicId) ?: TopicProgressEntity(
            topicId = topicId,
            levelNumber = levelNumber
        )
        val isFirstTime = !existing.labCompleted
        val updated = existing.copy(
            labCompleted = true,
            completedAtTimestamp = if (existing.theoryCompleted && existing.quizCompleted) {
                System.currentTimeMillis()
            } else {
                existing.completedAtTimestamp
            }
        )
        dao.upsertTopicProgress(updated)
        if (isFirstTime) {
            recordActivity(
                xpDelta = xpReward / 2,
                title = "Lab CLI superado: $topicTitle",
                category = "Nivel $levelNumber • Consola CLI",
                commandsDelta = 1,
                quizDelta = 0
            )
        } else {
            incrementCommandCounter()
        }
    }

    suspend fun completeTopicQuiz(
        topicId: String,
        levelNumber: Int,
        topicTitle: String,
        scorePercent: Int,
        xpReward: Int
    ) {
        val existing = dao.getTopicProgress(topicId) ?: TopicProgressEntity(
            topicId = topicId,
            levelNumber = levelNumber
        )
        val isFirstTime = !existing.quizCompleted
        val updated = existing.copy(
            theoryCompleted = true,
            quizCompleted = true,
            quizScore = max(existing.quizScore, scorePercent),
            completedAtTimestamp = System.currentTimeMillis()
        )
        dao.upsertTopicProgress(updated)
        if (isFirstTime) {
            recordActivity(
                xpDelta = xpReward / 2,
                title = "Módulo dominado al 100%: $topicTitle",
                category = "Nivel $levelNumber • Quiz Final",
                commandsDelta = 0,
                quizDelta = 1
            )
        }
    }

    suspend fun completeChallenge(
        challengeId: String,
        challengeTitle: String,
        xpReward: Int,
        isCli: Boolean
    ) {
        val existing = dao.getChallengeProgress(challengeId)
        val isFirstTime = existing == null || !existing.completed
        val updated = ChallengeProgressEntity(
            challengeId = challengeId,
            completed = true,
            attempts = (existing?.attempts ?: 0) + 1,
            completedAtTimestamp = System.currentTimeMillis()
        )
        dao.upsertChallengeProgress(updated)
        if (isFirstTime) {
            recordActivity(
                xpDelta = xpReward,
                title = "Reto superado: $challengeTitle",
                category = if (isCli) "Reto CLI" else "Reto Táctico",
                commandsDelta = if (isCli) 1 else 0,
                quizDelta = if (!isCli) 1 else 0
            )
        }
    }

    suspend fun incrementCommandCounter() {
        val current = dao.getUserStatsOnce() ?: UserStatsEntity()
        val updated = current.copy(totalCommandsExecuted = current.totalCommandsExecuted + 1)
        dao.upsertUserStats(updated)
        syncPrefs(updated)
    }

    private suspend fun recordActivity(
        xpDelta: Int,
        title: String,
        category: String,
        commandsDelta: Int,
        quizDelta: Int,
        nowMs: Long = System.currentTimeMillis()
    ) {
        val current = dao.getUserStatsOnce() ?: UserStatsEntity()
        val currentDayEpoch = nowMs / TWENTY_FOUR_HOURS_MS
        val elapsedSinceLast = if (current.lastActivityTimestamp == 0L) {
            Long.MAX_VALUE
        } else {
            nowMs - current.lastActivityTimestamp
        }

        val newStreak = when {
            current.lastActivityTimestamp == 0L -> 1
            elapsedSinceLast > TWENTY_FOUR_HOURS_MS -> 1
            currentDayEpoch > current.lastStreakDayEpoch -> current.currentStreakDays + 1
            else -> max(1, current.currentStreakDays)
        }

        val newLongest = max(current.longestStreakDays, newStreak)
        val updated = current.copy(
            totalXp = current.totalXp + xpDelta,
            currentStreakDays = newStreak,
            longestStreakDays = newLongest,
            lastActivityTimestamp = nowMs,
            lastStreakDayEpoch = currentDayEpoch,
            totalCommandsExecuted = current.totalCommandsExecuted + commandsDelta,
            totalQuizzesPassed = current.totalQuizzesPassed + quizDelta
        )

        dao.upsertUserStats(updated)
        dao.insertActivityLog(
            ActivityLogEntity(
                title = title,
                category = category,
                xpEarned = xpDelta,
                timestamp = nowMs
            )
        )
        syncPrefs(updated)
    }

    suspend fun resetAllLocalProgress() {
        val savedTheme = getSavedThemeMode()
        val savedLang = getSavedLanguage()
        dao.clearAllTopics()
        dao.clearAllChallenges()
        dao.clearUserStats()
        dao.clearActivityLogs()
        localPrefs.edit().clear().apply()
        saveThemeMode(savedTheme)
        saveLanguage(savedLang)
        localPrefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply()
        dao.upsertUserStats(UserStatsEntity())
    }

    private fun syncPrefs(stats: UserStatsEntity) {
        localPrefs.edit()
            .putInt("total_xp", stats.totalXp)
            .putInt("streak_days", stats.currentStreakDays)
            .putInt("longest_streak", stats.longestStreakDays)
            .putLong("last_activity_ts", stats.lastActivityTimestamp)
            .putLong("last_streak_epoch", stats.lastStreakDayEpoch)
            .apply()
    }

    fun evaluateCliCommand(rawCommand: String, scenario: CliLabScenario): CliExecutionResult {
        val trimmed = rawCommand.trim()
        val normalized = trimmed.lowercase().replace(Regex("\\s+"), " ")
        val promptLine = TerminalLine(
            text = "${scenario.promptUser}:${scenario.workingDir}$ $trimmed",
            type = TerminalLineType.INPUT
        )

        if (normalized.isEmpty()) {
            return CliExecutionResult(newLines = listOf(promptLine))
        }

        if (normalized == "clear" || normalized == "cls") {
            return CliExecutionResult(newLines = emptyList(), isClearCommand = true)
        }

        val exactMatch = scenario.acceptedCommands.any { it.lowercase() == normalized }
        val tokenMatch = scenario.requiredTokens.isNotEmpty() &&
            scenario.requiredTokens.all { token -> normalized.contains(token.lowercase()) }

        if (exactMatch || tokenMatch) {
            val outputLines = buildList {
                add(promptLine)
                scenario.successOutput.forEach { line ->
                    val type = if (line.startsWith("[+]") || line.startsWith("[!]") || line.startsWith("[OK]")) {
                        TerminalLineType.SUCCESS
                    } else {
                        TerminalLineType.OUTPUT
                    }
                    add(TerminalLine(line, type))
                }
            }
            return CliExecutionResult(
                newLines = outputLines,
                missionAccomplished = true
            )
        }

        val exploratoryOutput: List<TerminalLine> = when {
            normalized == "help" || normalized == "--help" -> listOf(
                TerminalLine("Comandos disponibles en el entorno táctico Hackklin CLI:", TerminalLineType.OUTPUT),
                TerminalLine("  • Misión actual : ${scenario.missionTitle}", TerminalLineType.OUTPUT),
                TerminalLine("  • Sugerencia    : ${scenario.hint}", TerminalLineType.HINT),
                TerminalLine("  • Utilidades    : ls, pwd, whoami, id, ip a, uname -a, clear", TerminalLineType.OUTPUT)
            )
            normalized == "whoami" -> listOf(
                TerminalLine("operador", TerminalLineType.OUTPUT)
            )
            normalized == "id" -> listOf(
                TerminalLine("uid=1000(operador) gid=1000(operador) groups=1000(operador),27(sudo),118(sec-audit)", TerminalLineType.OUTPUT)
            )
            normalized == "pwd" -> listOf(
                TerminalLine("/home/operador/lab", TerminalLineType.OUTPUT)
            )
            normalized.startsWith("ls") -> {
                val files = if (scenario.virtualFiles.isNotEmpty()) {
                    scenario.virtualFiles.keys.joinToString("  ")
                } else {
                    "README.md  evidencia.log  config.yaml  target_scope.txt"
                }
                listOf(TerminalLine(files, TerminalLineType.OUTPUT))
            }
            normalized == "date" -> listOf(
                TerminalLine("Mon Nov 9 09:30:00 UTC 2026", TerminalLineType.OUTPUT)
            )
            else -> listOf(
                TerminalLine(
                    "hackklin-sh: comando '$trimmed' ejecutado, pero no completó el objetivo de la misión.",
                    TerminalLineType.ERROR
                ),
                TerminalLine(
                    "Tip: ${scenario.hint}",
                    TerminalLineType.HINT
                )
            )
        }

        return CliExecutionResult(
            newLines = listOf(promptLine) + exploratoryOutput,
            missionAccomplished = false
        )
    }

    fun calculateRank(
        levels: List<CurriculumLevel>,
        topicProgressMap: Map<String, TopicProgressEntity>,
        totalXp: Int
    ): UserRankInfo {
        var highestCompletedLevel = 0
        for (level in levels.sortedBy { it.number }) {
            val allDone = level.topics.all { topic ->
                topicProgressMap[topic.id]?.isFullyCompleted == true
            }
            if (allDone) {
                highestCompletedLevel = level.number
            } else {
                break
            }
        }
        val byLevel = HACKKLIN_RANKS.lastOrNull { it.minLevelCompleted <= highestCompletedLevel }
            ?: HACKKLIN_RANKS.first()
        val byXp = HACKKLIN_RANKS.lastOrNull { totalXp >= it.minXp }
            ?: HACKKLIN_RANKS.first()
        return if (HACKKLIN_RANKS.indexOf(byLevel) >= HACKKLIN_RANKS.indexOf(byXp)) byLevel else byXp
    }

    fun isLevelUnlocked(
        levelNumber: Int,
        topicProgressMap: Map<String, TopicProgressEntity>
    ): Boolean {
        if (levelNumber <= 1) return true
        val previousLevel = HackklinCurriculumCatalog.allLevels.find { it.number == levelNumber - 1 }
            ?: return false
        return previousLevel.topics.all { topic ->
            topicProgressMap[topic.id]?.isFullyCompleted == true
        }
    }
}
