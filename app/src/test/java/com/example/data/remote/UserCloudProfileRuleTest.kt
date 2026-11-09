package com.example.data.remote

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserCloudProfileRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveOrUpdateMyProfile_authenticatedOwner_createsAndUpdatesProfile() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = UserCloudProfileRepository(firestore)

        val saveResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.saveOrUpdateMyProfile(
                displayName = "Alice Cyber",
                email = ALICE_EMAIL,
                totalXp = 450,
                currentStreakDays = 3,
                longestStreakDays = 5,
                totalCommandsExecuted = 14,
                selectedInitialLevel = 2,
                preferredTheme = "DARK",
                preferredLanguage = "ES"
            )
        }
        assertTrue(saveResult.isSuccess)
        assertEquals(uid, saveResult.getOrThrow())

        val fetched = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.getProfileByUserId(uid).getOrThrow()
        }
        assertNotNull(fetched)
        assertEquals("Alice Cyber", fetched?.displayName)
        assertEquals(450L, fetched?.totalXp)
    }

    @Test
    fun observeMyProfile_authenticatedOwner_emitsRealtimeProfile() = runBlocking {
        signInTestUser(ALICE_EMAIL)
        val repo = UserCloudProfileRepository(firestore)

        withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.saveOrUpdateMyProfile(
                displayName = "Alice Realtime",
                email = ALICE_EMAIL,
                totalXp = 800,
                currentStreakDays = 6,
                longestStreakDays = 6,
                totalCommandsExecuted = 22,
                selectedInitialLevel = 1,
                preferredTheme = "LIGHT",
                preferredLanguage = "EN"
            ).getOrThrow()
        }

        val profile = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeMyProfile().first { it != null && it.totalXp == 800L }
        }
        assertNotNull(profile)
        assertEquals("LIGHT", profile?.preferredTheme)
        assertEquals("EN", profile?.preferredLanguage)
    }

    @Test
    fun getProfileByUserId_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = UserCloudProfileRepository(firestore)
        withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceRepo.saveOrUpdateMyProfile(
                displayName = "Alice Private",
                email = ALICE_EMAIL,
                totalXp = 250,
                currentStreakDays = 1,
                longestStreakDays = 1,
                totalCommandsExecuted = 5,
                selectedInitialLevel = 1,
                preferredTheme = "DARK",
                preferredLanguage = "ES"
            ).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        val bobRepo = UserCloudProfileRepository(firestore)
        val crossResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            bobRepo.getProfileByUserId(aliceUid)
        }
        assertTrue(crossResult.isFailure)
        val fsEx = crossResult.exceptionOrNull()?.unwrapFirestoreException()
        assertNotNull(fsEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsEx?.code)
    }

    @Test
    fun getProfileByUserId_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repo = UserCloudProfileRepository(firestore)
        withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.saveOrUpdateMyProfile(
                displayName = "Alice Private",
                email = ALICE_EMAIL,
                totalXp = 100,
                currentStreakDays = 1,
                longestStreakDays = 1,
                totalCommandsExecuted = 2,
                selectedInitialLevel = 1,
                preferredTheme = "DARK",
                preferredLanguage = "ES"
            ).getOrThrow()
        }

        auth.signOut()
        val unauthResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.getProfileByUserId(aliceUid)
        }
        assertTrue(unauthResult.isFailure)
        val fsEx = unauthResult.exceptionOrNull()?.unwrapFirestoreException()
        assertNotNull(fsEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsEx?.code)
    }

    private companion object {
        const val ALICE_EMAIL = "alice@hackklin.test"
        const val BOB_EMAIL = "bob@hackklin.test"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
