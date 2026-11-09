package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface HackklinDao {
    @Query("SELECT * FROM topic_progress")
    fun observeAllTopicProgress(): Flow<List<TopicProgressEntity>>

    @Query("SELECT * FROM topic_progress WHERE topicId = :topicId LIMIT 1")
    suspend fun getTopicProgress(topicId: String): TopicProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTopicProgress(progress: TopicProgressEntity)

    @Query("SELECT * FROM challenge_progress")
    fun observeAllChallengeProgress(): Flow<List<ChallengeProgressEntity>>

    @Query("SELECT * FROM challenge_progress WHERE challengeId = :challengeId LIMIT 1")
    suspend fun getChallengeProgress(challengeId: String): ChallengeProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChallengeProgress(progress: ChallengeProgressEntity)

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun observeUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStatsOnce(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserStats(stats: UserStatsEntity)

    @Query("SELECT * FROM activity_log ORDER BY timestamp DESC LIMIT 30")
    fun observeRecentActivity(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogEntity)

    @Query("DELETE FROM topic_progress")
    suspend fun clearAllTopics()

    @Query("DELETE FROM challenge_progress")
    suspend fun clearAllChallenges()

    @Query("DELETE FROM user_stats")
    suspend fun clearUserStats()

    @Query("DELETE FROM activity_log")
    suspend fun clearActivityLogs()
}

@Database(
    entities = [
        TopicProgressEntity::class,
        ChallengeProgressEntity::class,
        UserStatsEntity::class,
        ActivityLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HackklinDatabase : RoomDatabase() {
    abstract fun hackklinDao(): HackklinDao

    companion object {
        @Volatile
        private var INSTANCE: HackklinDatabase? = null

        fun getInstance(context: Context): HackklinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HackklinDatabase::class.java,
                    "hackklin_local_storage.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
