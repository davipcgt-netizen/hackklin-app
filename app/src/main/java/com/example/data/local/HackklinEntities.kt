package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_progress")
data class TopicProgressEntity(
    @PrimaryKey val topicId: String,
    val levelNumber: Int,
    val theoryCompleted: Boolean = false,
    val labCompleted: Boolean = false,
    val quizCompleted: Boolean = false,
    val quizScore: Int = 0,
    val completedAtTimestamp: Long = 0L
) {
    val isFullyCompleted: Boolean
        get() = theoryCompleted && labCompleted && quizCompleted
}

@Entity(tableName = "challenge_progress")
data class ChallengeProgressEntity(
    @PrimaryKey val challengeId: String,
    val completed: Boolean = false,
    val attempts: Int = 0,
    val completedAtTimestamp: Long = 0L
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalXp: Int = 0,
    val currentStreakDays: Int = 0,
    val longestStreakDays: Int = 0,
    val lastActivityTimestamp: Long = 0L,
    val lastStreakDayEpoch: Long = 0L,
    val totalCommandsExecuted: Int = 0,
    val totalQuizzesPassed: Int = 0
)

@Entity(tableName = "activity_log")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val xpEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)
