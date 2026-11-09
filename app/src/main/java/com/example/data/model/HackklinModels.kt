package com.example.data.model

enum class AppThemeMode(val code: String, val labelEs: String, val labelEn: String, val emoji: String) {
    DARK("DARK", "Oscuro Puro", "Pure Dark", "🌙"),
    LIGHT("LIGHT", "Blanco Táctico", "Tactical Light", "☀️");

    companion object {
        fun fromCode(code: String?): AppThemeMode =
            entries.find { it.code == code } ?: DARK
    }
}

enum class AppLanguage(val code: String, val label: String, val flagEmoji: String) {
    ES("ES", "Español", "🇪🇸"),
    EN("EN", "English", "🇺🇸");

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.find { it.code == code } ?: ES
    }
}

data class CurriculumLevel(
    val number: Int,
    val rankName: String,
    val title: String,
    val subtitle: String,
    val badgeCode: String,
    val description: String,
    val syllabusHighlights: List<String>,
    val topics: List<CurriculumTopic>
)

data class CurriculumTopic(
    val id: String,
    val levelNumber: Int,
    val orderIndex: Int,
    val code: String,
    val title: String,
    val durationMinutes: Int,
    val xpReward: Int,
    val summary: String,
    val subtopicsChecklist: List<String>,
    val theorySections: List<TheorySection>,
    val cliLab: CliLabScenario,
    val quizQuestions: List<QuizQuestion>
)

data class TheorySection(
    val heading: String,
    val body: String,
    val codeSnippet: String? = null,
    val keyTakeaway: String
)

data class CliLabScenario(
    val missionTitle: String,
    val briefing: String,
    val promptUser: String = "operador@hackklin",
    val workingDir: String = "~/lab",
    val initialBanner: List<String>,
    val quickChips: List<String>,
    val hint: String,
    val acceptedCommands: List<String>,
    val requiredTokens: List<String>,
    val successOutput: List<String>,
    val virtualFiles: Map<String, String> = emptyMap()
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

enum class ChallengeType(val label: String) {
    CLI_SIMULATOR("Laboratorio CLI"),
    SCENARIO_QUIZ("Quiz Táctico")
}

data class ChallengeItem(
    val id: String,
    val title: String,
    val category: String,
    val requiredLevel: Int,
    val difficulty: String,
    val xpReward: Int,
    val type: ChallengeType,
    val description: String,
    val cliScenario: CliLabScenario? = null,
    val quizQuestions: List<QuizQuestion> = emptyList()
)

enum class TerminalLineType {
    BANNER,
    INPUT,
    OUTPUT,
    SUCCESS,
    ERROR,
    HINT
}

data class TerminalLine(
    val text: String,
    val type: TerminalLineType
)

data class UserRankInfo(
    val title: String,
    val titleEn: String,
    val emoji: String,
    val badgeCode: String,
    val minLevelCompleted: Int,
    val minXp: Int,
    val motto: String,
    val mottoEn: String
) {
    fun localizedTitle(language: AppLanguage): String =
        if (language == AppLanguage.EN) titleEn else title

    fun localizedMotto(language: AppLanguage): String =
        if (language == AppLanguage.EN) mottoEn else motto
}

val HACKKLIN_RANKS = listOf(
    UserRankInfo(
        title = "Novato",
        titleEn = "Rookie",
        emoji = "🌱",
        badgeCode = "RNK-00",
        minLevelCompleted = 0,
        minXp = 0,
        motto = "Primeros pasos en el reconocimiento del entorno digital",
        mottoEn = "First steps into digital environment reconnaissance"
    ),
    UserRankInfo(
        title = "Aspirante",
        titleEn = "Aspirant",
        emoji = "🛰️",
        badgeCode = "RNK-01",
        minLevelCompleted = 1,
        minXp = 500,
        motto = "Dominio de direcciones IP, puertos, protocolos TCP/IP y subredes",
        mottoEn = "Mastery of IP addresses, ports, TCP/IP protocols and subnets"
    ),
    UserRankInfo(
        title = "Iniciado",
        titleEn = "Initiate",
        emoji = "🔐",
        badgeCode = "RNK-02",
        minLevelCompleted = 2,
        minXp = 1100,
        motto = "Custodio de la tríada CIA, detección de phishing y criptografía",
        mottoEn = "Guardian of the CIA triad, phishing defense and cryptography"
    ),
    UserRankInfo(
        title = "Operador",
        titleEn = "Operator",
        emoji = "🛡️",
        badgeCode = "RNK-03",
        minLevelCompleted = 3,
        minXp = 1800,
        motto = "Especialista en administración Linux/Windows, SSH y Hardening",
        mottoEn = "Specialist in Linux/Windows administration, SSH and Hardening"
    ),
    UserRankInfo(
        title = "Analista Junior",
        titleEn = "Junior Analyst",
        emoji = "📡",
        badgeCode = "RNK-04",
        minLevelCompleted = 4,
        minXp = 2600,
        motto = "Vigilante SOC, detección SIEM, tráfico y defensa activa Blue Team",
        mottoEn = "SOC defender, SIEM detection, traffic analysis and Blue Team ops"
    ),
    UserRankInfo(
        title = "Cazador",
        titleEn = "Hunter",
        emoji = "🦅",
        badgeCode = "RNK-05",
        minLevelCompleted = 5,
        minXp = 3500,
        motto = "Operador de pentesting, enumeración Nmap y explotación ética",
        mottoEn = "Pentesting operator, Nmap enumeration and ethical exploitation"
    ),
    UserRankInfo(
        title = "Especialista",
        titleEn = "Specialist",
        emoji = "🧬",
        badgeCode = "RNK-06",
        minLevelCompleted = 6,
        minXp = 4500,
        motto = "Investigador forense digital DFIR y respuesta ante incidentes",
        mottoEn = "DFIR digital forensics investigator and incident responder"
    ),
    UserRankInfo(
        title = "Arquitecto",
        titleEn = "Architect",
        emoji = "☁️",
        badgeCode = "RNK-07",
        minLevelCompleted = 7,
        minXp = 5600,
        motto = "Ingeniero de seguridad Cloud IAM, contenedores y DevSecOps",
        mottoEn = "Cloud IAM security engineer, containers and DevSecOps"
    ),
    UserRankInfo(
        title = "Hacker",
        titleEn = "Elite Hacker",
        emoji = "👑",
        badgeCode = "RNK-08",
        minLevelCompleted = 8,
        minXp = 6800,
        motto = "Leyenda de ingeniería inversa y operaciones Red/Blue de élite",
        mottoEn = "Legend of reverse engineering and elite Red/Blue operations"
    )
)

fun getLevelEmoji(levelNumber: Int): String = when (levelNumber) {
    1 -> "🛰️"
    2 -> "🔐"
    3 -> "🛡️"
    4 -> "📡"
    5 -> "🦅"
    6 -> "🧬"
    7 -> "☁️"
    8 -> "👑"
    else -> "🌱"
}
