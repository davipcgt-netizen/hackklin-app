package com.example

import com.example.data.curriculum.HackklinCurriculumCatalog
import com.example.data.local.ChallengeProgressEntity
import com.example.data.local.TopicProgressEntity
import com.example.ui.viewmodel.HackklinUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun curriculum_has_all_8_sequential_levels_and_intensive_topics() {
        val levels = HackklinCurriculumCatalog.allLevels
        assertEquals(8, levels.size)
        assertEquals("Aspirante", levels[0].rankName)
        assertEquals("Iniciado", levels[1].rankName)
        assertEquals("Operador", levels[2].rankName)
        assertEquals("Analista Junior", levels[3].rankName)
        assertEquals("Cazador", levels[4].rankName)
        assertEquals("Especialista", levels[5].rankName)
        assertEquals("Arquitecto", levels[6].rankName)
        assertEquals("Leyenda", levels[7].rankName)

        levels.forEach { level ->
            assertTrue("Cada nivel debe tener al menos 5 temas intensivos", level.topics.size >= 5)
            level.topics.forEach { topic ->
                assertTrue(topic.theorySections.isNotEmpty())
                assertTrue(topic.cliLab.quickChips.isNotEmpty())
                assertTrue(topic.quizQuestions.isNotEmpty())
            }
        }
    }

    @Test
    fun sequential_unlocking_requires_100_percent_of_previous_level_and_step_by_step_challenges() {
        val initialState = HackklinUiState()
        assertTrue(initialState.isLevelUnlocked(1))
        assertFalse(initialState.isLevelUnlocked(2))

        // Challenge #01 (index 0) is unlocked, Challenge #02 (index 1) is locked until #01 is completed
        assertTrue(initialState.isChallengeUnlockedByIndex(0))
        assertFalse(initialState.isChallengeUnlockedByIndex(1))

        val firstChallengeId = HackklinCurriculumCatalog.allChallenges.first().id
        val stateAfterFirstChallenge = HackklinUiState(
            challengeProgressMap = mapOf(
                firstChallengeId to ChallengeProgressEntity(
                    challengeId = firstChallengeId,
                    completed = true
                )
            )
        )
        assertTrue(stateAfterFirstChallenge.isChallengeUnlockedByIndex(1))
        assertFalse(stateAfterFirstChallenge.isChallengeUnlockedByIndex(2))

        // Complete all topics of Level 1
        val level1CompletedMap = HackklinCurriculumCatalog.allLevels.first().topics.associate { topic ->
            topic.id to TopicProgressEntity(
                topicId = topic.id,
                levelNumber = 1,
                theoryCompleted = true,
                labCompleted = true,
                quizCompleted = true
            )
        }
        val stateAfterLevel1 = HackklinUiState(topicProgressMap = level1CompletedMap)
        assertTrue(stateAfterLevel1.isLevelUnlocked(2))
        assertFalse(stateAfterLevel1.isLevelUnlocked(3))
        assertEquals(1.0f, stateAfterLevel1.getLevelProgressFraction(1), 0.001f)
    }
}
