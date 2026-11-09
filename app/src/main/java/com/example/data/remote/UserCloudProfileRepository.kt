package com.example.data.remote

import android.content.Context
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@IgnoreExtraProperties
data class UserCloudProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val totalXp: Long = 0L,
    val currentStreakDays: Long = 0L,
    val longestStreakDays: Long = 0L,
    val totalCommandsExecuted: Long = 0L,
    val selectedInitialLevel: Long = 1L,
    val preferredTheme: String = "DARK",
    val preferredLanguage: String = "ES",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

class UserCloudProfileRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeMyProfile(): Flow<UserCloudProfile?> = flow {
        val uid = requireUserId()
        val path = "users/$uid"
        emitAll(
            db.collection("users").document(uid)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            UserCloudProfile::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else {
                        null
                    }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: (error as? Exception)
                    if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                        handleFirestoreError(exceptionToLog, OperationType.GET, path)
                    }
                    throw error
                }
        )
    }

    suspend fun getProfileByUserId(targetUserId: String): Result<UserCloudProfile?> = runCatching {
        val docRef = db.collection("users").document(targetUserId)
        try {
            val snap = docRef.get().await()
            if (snap.exists()) {
                snap.toObject(
                    UserCloudProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            } else {
                null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            throw e
        }
    }

    suspend fun saveOrUpdateMyProfile(
        displayName: String,
        email: String,
        totalXp: Int,
        currentStreakDays: Int,
        longestStreakDays: Int,
        totalCommandsExecuted: Int,
        selectedInitialLevel: Int,
        preferredTheme: String,
        preferredLanguage: String
    ): Result<String> = runCatching {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val safeName = displayName.trim().ifBlank { "Agente Hackklin" }.take(100)
        val safeEmail = email.trim().take(160)
        val safeLevel = selectedInitialLevel.coerceIn(1, 8)
        val safeTheme = if (preferredTheme == "LIGHT") "LIGHT" else "DARK"
        val safeLang = if (preferredLanguage == "EN") "EN" else "ES"

        try {
            val existingSnap = docRef.get().await()
            if (!existingSnap.exists()) {
                val createPayload = mapOf(
                    "userId" to uid,
                    "displayName" to safeName,
                    "email" to safeEmail,
                    "totalXp" to totalXp.coerceAtLeast(0).toLong(),
                    "currentStreakDays" to currentStreakDays.coerceAtLeast(0).toLong(),
                    "longestStreakDays" to longestStreakDays.coerceAtLeast(0).toLong(),
                    "totalCommandsExecuted" to totalCommandsExecuted.coerceAtLeast(0).toLong(),
                    "selectedInitialLevel" to safeLevel.toLong(),
                    "preferredTheme" to safeTheme,
                    "preferredLanguage" to safeLang,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(createPayload).await()
            } else {
                val updatePayload = mapOf(
                    "displayName" to safeName,
                    "email" to safeEmail,
                    "totalXp" to totalXp.coerceAtLeast(0).toLong(),
                    "currentStreakDays" to currentStreakDays.coerceAtLeast(0).toLong(),
                    "longestStreakDays" to longestStreakDays.coerceAtLeast(0).toLong(),
                    "totalCommandsExecuted" to totalCommandsExecuted.coerceAtLeast(0).toLong(),
                    "selectedInitialLevel" to safeLevel.toLong(),
                    "preferredTheme" to safeTheme,
                    "preferredLanguage" to safeLang,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.update(updatePayload).await()
            }
            uid
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }
}
