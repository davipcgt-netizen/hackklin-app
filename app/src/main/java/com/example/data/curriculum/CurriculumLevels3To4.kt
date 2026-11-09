package com.example.data.curriculum

import com.example.data.model.CliLabScenario
import com.example.data.model.CurriculumLevel
import com.example.data.model.CurriculumTopic
import com.example.data.model.QuizQuestion
import com.example.data.model.TheorySection

object CurriculumLevels3To4 {

    val level3Operador = CurriculumLevel(
        number = 3,
        rankName = "Operador",
        title = "Nivel 3: Operador",
        subtitle = "Sistemas Linux, Windows y Blindaje (Hardening)",
        badgeCode = "NIVEL 3",
        description = "Aprende a administrar y blindar servidores Linux y Windows: permisos de archivos, protección de accesos SSH, cortafuegos y control de privilegios.",
        syllabusHighlights = listOf(
            "Permisos de archivos en Linux (chmod) y bits peligrosos SUID",
            "Blindaje de acceso remoto por SSH (sshd_config)",
            "Configuración de Cortafuegos (UFW) y defensas del Kernel",
            "Control de superusuario con Sudoers y cuentas de servicio",
            "Seguridad en Windows Server y Active Directory"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l3_t1",
                levelNumber = 3,
                orderIndex = 1,
                code = "CLASE 3.1",
                title = "Permisos Linux",
                durationMinutes = 7,
                xpReward = 175,
                summary = "Domina cómo funcionan los permisos de lectura, escritura y ejecución en Linux y cómo proteger tus llaves privadas.",
                subtopicsChecklist = listOf(
                    "Los 3 permisos básicos: Leer (4), Escribir (2) y Ejecutar (1)",
                    "Por qué el permiso 777 es un peligro grave de seguridad",
                    "Proteger una llave privada con chmod 600"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Código de Permisos en Linux: 4, 2 y 1",
                        body = "En Linux, cada archivo define quién puede tocarlo usando tres números: Propietario, Grupo y Otros.\n\nCada número se calcula sumando: Leer = 4, Escribir = 2 y Ejecutar = 1. Por eso, `600` significa que solo el dueño puede leer y escribir (4+2=6) y nadie más tiene acceso (0 y 0).",
                        codeSnippet = "chmod 777 archivo  ──► ¡Peligro! Todo el mundo puede modificarlo\nchmod 600 llave    ──► ¡Seguro! Solo el dueño puede leer su llave",
                        keyTakeaway = "Las claves privadas SSH deben tener siempre permiso 600 para que ningún otro usuario del sistema las robe."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Blinda la Llave Privada expuesta",
                    briefing = "La clave `id_rsa_prod` tiene permisos `777` (abierta a todos). Protégela inmediatamente ejecutando `chmod 600 id_rsa_prod`.",
                    initialBanner = listOf(
                        "LABORATORIO DE HARDENING LINUX",
                        "-rwxrwxrwx 1 operador operador id_rsa_prod [¡EXPUESTA!]"
                    ),
                    quickChips = listOf("chmod 600 id_rsa_prod", "ls -la", "help"),
                    hint = "Toca el chip 'chmod 600 id_rsa_prod' y presiona Ejecutar.",
                    acceptedCommands = listOf("chmod 600 id_rsa_prod", "chmod 0600 id_rsa_prod", "chmod 400 id_rsa_prod"),
                    requiredTokens = listOf("chmod", "id_rsa_prod"),
                    successOutput = listOf(
                        "-rw------- 1 operador operador 2602 Nov 09 id_rsa_prod",
                        "[+] ¡Llave blindada! Permisos restringidos a 600 (-rw-------)."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l3_t1_1",
                        question = "¿Qué comando asigna permisos de lectura y escritura únicamente al propietario de un archivo y bloquea por completo a los demás usuarios?",
                        options = listOf("chmod 600 archivo", "chmod 777 archivo", "ping archivo", "ls archivo"),
                        correctIndex = 0,
                        explanation = "El valor 6 (4 lectura + 2 escritura) para el dueño y 00 para grupo y otros protege archivos confidenciales."
                    )
                )
            ),
            CurriculumTopic(
                id = "l3_t2",
                levelNumber = 3,
                orderIndex = 2,
                code = "CLASE 3.2",
                title = "Blindaje de SSH",
                durationMinutes = 7,
                xpReward = 180,
                summary = "Aprende a proteger la puerta de administración remota SSH desactivando el acceso directo de root y el uso de contraseñas.",
                subtopicsChecklist = listOf(
                    "Desactivar login directo de superusuario (PermitRootLogin no)",
                    "Exigir llaves criptográficas en lugar de contraseñas (PasswordAuthentication no)",
                    "Auditar el archivo sshd_config"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cerrando la Puerta a la Fuerza Bruta en SSH",
                        body = "Miles de bots en Internet intentan adivinar la contraseña del usuario `root` por SSH cada minuto.\n\nPara blindar un servidor, editamos `/etc/ssh/sshd_config` y activamos dos escudos definitivos: prohibimos que `root` entre directo y desactivamos las contraseñas para exigir llaves digitales.",
                        codeSnippet = "PermitRootLogin no\nPasswordAuthentication no\nPubkeyAuthentication yes",
                        keyTakeaway = "Sin autenticación por contraseña, los ataques de fuerza bruta por diccionario quedan neutralizados al 100%."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita la Seguridad del Servidor SSH",
                    briefing = "Verifica que el archivo `sshd_config` tenga el blindaje activo ejecutando el comando del primer chip.",
                    initialBanner = listOf(
                        "AUDITORÍA DE DEMONIO OPENSSH",
                        "Archivo objetivo: sshd_config"
                    ),
                    quickChips = listOf(
                        "grep -E 'PermitRootLogin|PasswordAuthentication' sshd_config",
                        "cat sshd_config"
                    ),
                    hint = "Toca el primer chip y presiona Ejecutar.",
                    acceptedCommands = listOf(
                        "grep -e 'permitrootlogin|passwordauthentication' sshd_config",
                        "cat sshd_config",
                        "sshd -t"
                    ),
                    requiredTokens = listOf("sshd"),
                    successOutput = listOf(
                        "PermitRootLogin no",
                        "PasswordAuthentication no",
                        "[+] ¡SSH fortificado! Login de root y contraseñas deshabilitados."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l3_t2_1",
                        question = "¿Qué directiva en `sshd_config` desactiva el inicio de sesión con contraseña y obliga a usar llaves criptográficas?",
                        options = listOf("PasswordAuthentication no", "Port 80", "Banner yes", "AllowAll yes"),
                        correctIndex = 0,
                        explanation = "PasswordAuthentication no impide que un atacante pruebe contraseñas contra el servicio SSH."
                    )
                )
            ),
            CurriculumTopic(
                id = "l3_t3",
                levelNumber = 3,
                orderIndex = 3,
                code = "CLASE 3.3",
                title = "Firewall UFW",
                durationMinutes = 7,
                xpReward = 185,
                summary = "Configura el cortafuegos de Linux bajo la regla de oro: bloquear todo lo que entra salvo los puertos estrictamente necesarios.",
                subtopicsChecklist = listOf(
                    "Política por defecto: Denegar todo el tráfico entrante (Default Deny)",
                    "Abrir únicamente los puertos indispensables",
                    "Verificación de estado con ufw status verbose"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Escudo Perimetral: Firewall con Default Deny",
                        body = "Un cortafuegos (Firewall) es el guardia de seguridad que vigila todos los puertos del servidor.\n\nLa regla de oro es aplicar `Default Deny Incoming`: cerrar todas las puertas de entrada por defecto y abrir exclusivamente las que el servidor necesita para trabajar.",
                        codeSnippet = "$ ufw default deny incoming\n$ ufw allow 443/tcp\n$ ufw enable",
                        keyTakeaway = "Con un firewall bien configurado, aunque un programa interno abra un puerto por error, nadie desde fuera podrá alcanzarlo."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Comprueba el Estado del Cortafuegos",
                    briefing = "Revisa las reglas activas del firewall ejecutando `ufw status verbose`.",
                    initialBanner = listOf(
                        "CONTROL DE CORTAFUEGOS UFW",
                        "Misión: Confirmar que la política de entrada sea DENY."
                    ),
                    quickChips = listOf("ufw status verbose", "iptables -L -n", "help"),
                    hint = "Toca el chip 'ufw status verbose' y presiona Ejecutar.",
                    acceptedCommands = listOf("ufw status verbose", "ufw status", "iptables -l -n"),
                    requiredTokens = listOf("ufw"),
                    successOutput = listOf(
                        "Status: active",
                        "Default: deny (incoming), allow (outgoing)",
                        "22/tcp   ALLOW IN   10.10.0.0/24",
                        "443/tcp  ALLOW IN   Anywhere",
                        "[+] Firewall verificado correctamente."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l3_t3_1",
                        question = "¿Cuál es la política por defecto recomendada para las conexiones ENTRANTES en el firewall de un servidor seguro?",
                        options = listOf(
                            "Denegar todo por defecto (Default Deny) y permitir solo los puertos necesarios",
                            "Permitir todos los 65,535 puertos por defecto",
                            "Apagar el firewall por completo",
                            "Permitir solo el puerto 23 Telnet"
                        ),
                        correctIndex = 0,
                        explanation = "Bloquear todas las entradas salvo lista blanca explícita minimiza la superficie de ataque."
                    )
                )
            ),
            CurriculumTopic(
                id = "l3_t4",
                levelNumber = 3,
                orderIndex = 4,
                code = "CLASE 3.4",
                title = "Privilegios Sudo",
                durationMinutes = 7,
                xpReward = 180,
                summary = "Aprende cómo se otorgan permisos de administrador con sudo y cómo detectar configuraciones peligrosas que permiten tomar control total.",
                subtopicsChecklist = listOf(
                    "El principio de Mínimo Privilegio",
                    "Por qué dar sudo sin contraseña sobre ciertos programas es peligroso",
                    "Auditar permisos con sudo -l"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Vigilando los Permisos de Superusuario (sudo -l)",
                        body = "El comando `sudo` permite a un usuario normal ejecutar una tarea puntual como administrador (`root`).\n\nPero si un administrador configura mal el sistema y permite ejecutar herramientas como `find` o `vim` con `sudo` sin contraseña, un atacante puede usarlas para abrir una consola de control total.",
                        codeSnippet = "$ sudo -l\n(root) NOPASSWD: /usr/bin/find  ◄── ¡Fallo grave de configuración!",
                        keyTakeaway = "Ejecutar `sudo -l` es siempre una de las primeras comprobaciones al auditar la seguridad interna de un servidor Linux."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita tus Privilegios Sudo",
                    briefing = "Descubre qué comandos puedes ejecutar como superusuario corriendo `sudo -l`.",
                    initialBanner = listOf(
                        "AUDITOR DE PRIVILEGIOS SUDOERS",
                        "Misión: Detectar permisos excesivos en la cuenta actual."
                    ),
                    quickChips = listOf("sudo -l", "id", "whoami"),
                    hint = "Toca el chip 'sudo -l' y pulsa Ejecutar.",
                    acceptedCommands = listOf("sudo -l", "id"),
                    requiredTokens = listOf("sudo"),
                    successOutput = listOf(
                        "User operador may run the following commands:",
                        "    (root) /usr/bin/systemctl status nginx",
                        "    (root) NOPASSWD: /usr/bin/find",
                        "[!] ¡Alerta detectada! '/usr/bin/find' con NOPASSWD permite escalar a root."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l3_t4_1",
                        question = "¿Qué comando de Linux muestra la lista de programas que tu usuario actual tiene permiso de ejecutar con `sudo`?",
                        options = listOf("sudo -l", "ping -c 1", "clear", "mkdir"),
                        correctIndex = 0,
                        explanation = "sudo -l lista las reglas permitidas para el usuario actual en /etc/sudoers."
                    )
                )
            ),
            CurriculumTopic(
                id = "l3_t5",
                levelNumber = 3,
                orderIndex = 5,
                code = "CLASE 3.5",
                title = "Blindaje Windows",
                durationMinutes = 8,
                xpReward = 190,
                summary = "Domina cómo se protegen las redes corporativas Windows (Active Directory) con contraseñas únicas LAPS y firma SMB.",
                subtopicsChecklist = listOf(
                    "El riesgo de usar la misma clave de Administrador en todas las PCs",
                    "Windows LAPS: Una contraseña única y automática para cada equipo",
                    "Firma SMB obligatoria contra ataques en la red local"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Protegiendo Empresas con Windows LAPS y Firma SMB",
                        body = "En muchas empresas, todas las computadoras tenían la misma contraseña de Administrador Local. Si un hacker entraba a una sola PC, podía saltar a todas las demás.\n\nCon Windows LAPS, cada computadora tiene una contraseña distinta que cambia sola, y con SMB Signing evitamos que alguien intercepte conexiones en la red.",
                        codeSnippet = "Windows LAPS       ──► Contraseña única por cada PC de la empresa\nSMB Signing Activo ──► Bloquea la interceptación de sesiones en red",
                        keyTakeaway = "¡Completa este laboratorio para graduarte del Nivel 3 y ascender a Analista Junior (Blue Team)!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Verifica el Blindaje del Dominio Windows",
                    briefing = "Revisa el reporte de políticas de seguridad del servidor ejecutando `cat ad_hardening_report.txt`.",
                    initialBanner = listOf(
                        "AUDITOR DE ACTIVE DIRECTORY",
                        "Reporte exportado: ad_hardening_report.txt"
                    ),
                    quickChips = listOf("cat ad_hardening_report.txt", "ls -la"),
                    hint = "Toca el chip 'cat ad_hardening_report.txt' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat ad_hardening_report.txt", "grep -i smb ad_hardening_report.txt"),
                    requiredTokens = listOf("ad_hardening_report.txt"),
                    successOutput = listOf(
                        "[OK] SMBv1 Protocol: DISABLED",
                        "[OK] SMB Signing Required: ENABLED",
                        "[OK] Windows LAPS Rotation: ACTIVE",
                        "[+] ¡Dominio Windows verificado! Has completado el Nivel 3."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l3_t5_1",
                        question = "¿Qué herramienta de Microsoft asigna automáticamente una contraseña de administrador local diferente a cada computadora de la empresa?",
                        options = listOf("Windows LAPS", "Paint", "Bloc de notas", "Calculadora"),
                        correctIndex = 0,
                        explanation = "Windows LAPS gestiona y rota contraseñas únicas de administrador local por cada equipo del dominio."
                    )
                )
            )
        )
    )

    val level4AnalistaJunior = CurriculumLevel(
        number = 4,
        rankName = "Analista Junior",
        title = "Nivel 4: Analista Junior",
        subtitle = "Defensa Activa (Blue Team), Logs, SIEM y Tráfico",
        badgeCode = "NIVEL 4",
        description = "Entra al Centro de Operaciones de Seguridad (SOC): detecta ataques en tiempo real analizando registros (logs), eventos de Windows, paquetes de red y reglas YARA.",
        syllabusHighlights = listOf(
            "Cómo cazar ataques de fuerza bruta en los registros de Linux (auth.log)",
            "Códigos de eventos de seguridad en Windows (Event IDs 4624, 4625, 4720)",
            "Captura y análisis de paquetes de red con tcpdump",
            "Detección de malware y webshells con reglas YARA y Suricata",
            "Clasificación de vulnerabilidades críticas con CVE y CVSS"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l4_t1",
                levelNumber = 4,
                orderIndex = 1,
                code = "CLASE 4.1",
                title = "Cazando en Logs",
                durationMinutes = 7,
                xpReward = 195,
                summary = "Aprende cómo el equipo defensivo (Blue Team) descubre a un atacante rastreando las huellas que deja en el archivo auth.log.",
                subtopicsChecklist = listOf(
                    "¿Qué hace un analista SOC / Blue Team?",
                    "Rastrear intentos fallidos ('Failed password') en auth.log",
                    "Identificar la dirección IP exacta del atacante"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Las Cámaras de Seguridad del Servidor: auth.log",
                        body = "Cada vez que alguien intenta entrar a un servidor Linux, queda grabado con fecha, hora y dirección IP en el archivo `/var/log/auth.log`.\n\nSi un atacante prueba miles de claves, veremos una lluvia de líneas `Failed password`. Con el comando `grep` podemos filtrar el archivo y atrapar su IP al instante.",
                        codeSnippet = "$ grep \"Failed password\" auth.log\nFailed password for root from 203.0.113.88 port 49812",
                        keyTakeaway = "Identificar la IP atacante en los logs nos permite bloquearla en el firewall inmediatamente."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Atrapa la IP del Atacante en los Logs",
                    briefing = "Filtra el registro `auth.log` buscando los intentos fallidos con `grep \"Failed password\" auth.log`.",
                    initialBanner = listOf(
                        "CONSOLA SOC SIEM — ANÁLISIS DE LOGS",
                        "Alerta: Múltiples intentos de acceso detectados en auth.log"
                    ),
                    quickChips = listOf("grep \"Failed password\" auth.log", "cat auth.log"),
                    hint = "Toca el chip 'grep \"Failed password\" auth.log' y pulsa Ejecutar.",
                    acceptedCommands = listOf("grep \"failed password\" auth.log", "grep failed auth.log", "cat auth.log"),
                    requiredTokens = listOf("auth.log"),
                    successOutput = listOf(
                        "Nov 09 08:14:01 sshd: Failed password for root from 203.0.113.88",
                        "Nov 09 08:14:03 sshd: Failed password for admin from 203.0.113.88",
                        "[+] ¡Atacante identificado! La IP 203.0.113.88 está lanzando fuerza bruta."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l4_t1_1",
                        question = "¿Qué archivo de Linux guarda el registro de todos los intentos de inicio de sesión SSH exitosos y fallidos?",
                        options = listOf("/var/log/auth.log", "/etc/hostname", "/tmp/foto.jpg", "/bin/ls"),
                        correctIndex = 0,
                        explanation = "/var/log/auth.log registra toda la actividad de autenticación y uso de sudo en sistemas Debian/Ubuntu."
                    )
                )
            ),
            CurriculumTopic(
                id = "l4_t2",
                levelNumber = 4,
                orderIndex = 2,
                code = "CLASE 4.2",
                title = "Eventos Windows",
                durationMinutes = 7,
                xpReward = 200,
                summary = "Memoriza los códigos de alerta más importantes de Windows para detectar si un intruso creó un usuario oculto o instaló un programa.",
                subtopicsChecklist = listOf(
                    "Event ID 4624 (Entrada exitosa) vs 4625 (Contraseña fallida)",
                    "Event ID 4720: ¡Alguien acaba de crear un usuario nuevo!",
                    "Event ID 7045: ¡Se instaló un servicio nuevo en el sistema!"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Los Códigos Secretos del Visor de Eventos Windows",
                        body = "Windows numera cada suceso de seguridad con un código llamado Event ID. Cuando un atacante logra entrar, suele crear un usuario falso para no perder el acceso.\n\nSi buscas el código `4720` (Usuario creado) o `7045` (Servicio instalado), descubrirás sus puertas traseras en segundos.",
                        codeSnippet = "Event ID 4625 ──► Intento de inicio de sesión fallido\nEvent ID 4720 ──► Cuenta de usuario nueva creada\nEvent ID 7045 ──► Nuevo servicio instalado en Windows",
                        keyTakeaway = "Los sistemas SIEM alertan automáticamente cuando aparece un evento 4720 fuera de horario laboral."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Caza el Usuario Oculto Creado por el Intruso",
                    briefing = "Filtra los eventos de Windows buscando los códigos `4720` o `7045` con el primer chip.",
                    initialBanner = listOf(
                        "CAZADOR DE EVENTOS WINDOWS (SIEM)",
                        "Archivo de registro: win_security_events.log"
                    ),
                    quickChips = listOf("grep -E '4720|7045' win_security_events.log", "cat win_security_events.log"),
                    hint = "Toca el chip 'grep -E '4720|7045' win_security_events.log' y pulsa Ejecutar.",
                    acceptedCommands = listOf("grep -e '4720|7045' win_security_events.log", "cat win_security_events.log"),
                    requiredTokens = listOf("win_security_events.log"),
                    successOutput = listOf(
                        "[EventID=4720] TargetAccount=\"svc_updater_bak\" [USUARIO CREADO]",
                        "[EventID=7045] ServiceName=\"WinSyncHelper\" [SERVICIO INSTALADO]",
                        "[+] ¡Puerta trasera detectada! El intruso creó la cuenta 'svc_updater_bak'."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l4_t2_1",
                        question = "¿Qué número de Event ID en Windows se genera inmediatamente cuando alguien crea una nueva cuenta de usuario?",
                        options = listOf("Event ID 4720", "Event ID 100", "Event ID 404", "Event ID 80"),
                        correctIndex = 0,
                        explanation = "El Event ID 4720 registra la creación de cualquier cuenta de usuario en el sistema."
                    )
                )
            ),
            CurriculumTopic(
                id = "l4_t3",
                levelNumber = 4,
                orderIndex = 3,
                code = "CLASE 4.3",
                title = "Tráfico con Tcpdump",
                durationMinutes = 8,
                xpReward = 205,
                summary = "Inspecciona los paquetes que viajan por el cable de red usando tcpdump para pillar a un atacante escaneando tus puertos.",
                subtopicsChecklist = listOf(
                    "¿Qué es un archivo de captura de tráfico (.pcap)?",
                    "Cómo se ve un escaneo de puertos en vivo (ráfaga de banderas [S])",
                    "Analizar una captura con tcpdump -nn -r"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Rayos X en la Red: El Comando tcpdump",
                        body = "`tcpdump` nos permite ver cada paquete que pasa por la tarjeta de red. Si alguien está escaneando nuestro servidor para buscar puertas abiertas, veremos decenas de paquetes con la bandera `Flags [S]` (SYN) golpeando distintos puertos uno tras otro.",
                        codeSnippet = "11:20:04 IP 10.20.4.115 > 10.20.4.10.22:  Flags [S]\n11:20:04 IP 10.20.4.115 > 10.20.4.10.80:  Flags [S]\n11:20:04 IP 10.20.4.115 > 10.20.4.10.445: Flags [S]",
                        keyTakeaway = "Una ráfaga rápida de paquetes Flags [S] hacia muchos puertos distintos es la firma inequívoca de un escaneo de puertos."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Analiza la Captura de Red Sospechosa",
                    briefing = "Abre la grabación de tráfico `alerta_red.pcap` ejecutando `tcpdump -nn -r alerta_red.pcap`.",
                    initialBanner = listOf(
                        "ANALIZADOR DE PAQUETES TCPDUMP",
                        "Captura de incidente: alerta_red.pcap"
                    ),
                    quickChips = listOf("tcpdump -nn -r alerta_red.pcap", "ls -lh"),
                    hint = "Toca el chip 'tcpdump -nn -r alerta_red.pcap' y pulsa Ejecutar.",
                    acceptedCommands = listOf("tcpdump -nn -r alerta_red.pcap", "tcpdump -r alerta_red.pcap"),
                    requiredTokens = listOf("tcpdump", "alerta_red.pcap"),
                    successOutput = listOf(
                        "11:20:04.001 IP 10.20.4.115 > 10.20.4.10.22: Flags [S]",
                        "11:20:04.002 IP 10.20.4.115 > 10.20.4.10.80: Flags [S]",
                        "11:20:04.003 IP 10.20.4.115 > 10.20.4.10.445: Flags [S]",
                        "[+] ¡Escaneo detectado desde el equipo interno 10.20.4.115!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l4_t3_1",
                        question = "Al analizar tráfico con `tcpdump`, ¿qué indica ver cientos de paquetes `Flags [S]` (SYN) desde una misma IP hacia diferentes puertos en menos de un segundo?",
                        options = listOf(
                            "Que esa IP está realizando un escaneo de puertos contra nuestro servidor",
                            "Que el servidor se está actualizando normalmente",
                            "Que el cable de red está desconectado",
                            "Que alguien está viendo un video"
                        ),
                        correctIndex = 0,
                        explanation = "Enviar paquetes SYN consecutivos a múltiples puertos es el método estándar de los escáneres como Nmap."
                    )
                )
            ),
            CurriculumTopic(
                id = "l4_t4",
                levelNumber = 4,
                orderIndex = 4,
                code = "CLASE 4.4",
                title = "Reglas YARA",
                durationMinutes = 7,
                xpReward = 200,
                summary = "Usa la herramienta favorita de los cazadores de malware (YARA) para buscar archivos maliciosos camuflados dentro de un servidor web.",
                subtopicsChecklist = listOf(
                    "¿Qué es YARA? El buscador de ADN del malware",
                    "Cómo los atacantes esconden Webshells en carpetas de imágenes",
                    "Escaneo de emergencia con yara"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cazando Malware Oculto con YARA",
                        body = "A veces un atacante sube un programa espía (llamado Webshell) disfrazado con nombre de foto o caché, como `avatar_cache.php`.\n\nCon **YARA**, definimos qué palabras o fragmentos sospechosos buscar por dentro de todos los archivos y el motor los encuentra aunque tengan nombres inocentes.",
                        codeSnippet = "$ yara webshell.yar /var/www/html\nDetect_Webshell_PHP /var/www/html/uploads/avatar_cache.php",
                        keyTakeaway = "YARA analiza el contenido interno de cada archivo sin importar cómo lo haya bautizado el atacante."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Escanea el Servidor Web con YARA",
                    briefing = "Ejecuta `yara webshell.yar /var/www/html` para encontrar el archivo malicioso escondido en la web.",
                    initialBanner = listOf(
                        "MOTOR CAZADOR DE MALWARE YARA",
                        "Regla cargada: webshell.yar"
                    ),
                    quickChips = listOf("yara webshell.yar /var/www/html", "cat webshell.yar"),
                    hint = "Toca el chip 'yara webshell.yar /var/www/html' y pulsa Ejecutar.",
                    acceptedCommands = listOf("yara webshell.yar /var/www/html", "yara -r webshell.yar /var/www/html"),
                    requiredTokens = listOf("yara", "webshell.yar"),
                    successOutput = listOf(
                        "Detect_Webshell_PHP /var/www/html/uploads/avatar_cache.php",
                        "[!] ¡Amenaza localizada en '/var/www/html/uploads/avatar_cache.php'!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l4_t4_1",
                        question = "¿Para qué se utiliza principalmente la herramienta YARA en ciberseguridad?",
                        options = listOf(
                            "Para identificar y clasificar muestras de malware o archivos maliciosos mediante reglas de patrones",
                            "Para diseñar páginas web con colores",
                            "Para cambiar la contraseña del Wi-Fi",
                            "Para comprimir videos"
                        ),
                        correctIndex = 0,
                        explanation = "YARA busca patrones textuales y binarios (firmas) dentro de archivos y memoria RAM para cazar malware."
                    )
                )
            ),
            CurriculumTopic(
                id = "l4_t5",
                levelNumber = 4,
                orderIndex = 5,
                code = "CLASE 4.5",
                title = "Alertas CVE y CVSS",
                durationMinutes = 7,
                xpReward = 210,
                summary = "Aprende cómo se nombran las vulnerabilidades a nivel mundial (CVE) y cómo saber cuál es la más urgente según su puntaje CVSS del 0 al 10.",
                subtopicsChecklist = listOf(
                    "¿Qué es un código CVE (ej. CVE-2024-3400)?",
                    "La escala de gravedad CVSS del 0.0 al 10.0 (Crítica >= 9.0)",
                    "Priorizar qué servidor parchar primero"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Semáforo Mundial de Vulnerabilidades: CVE y CVSS",
                        body = "Cuando se descubre un fallo de seguridad importante en el mundo, recibe un código único llamado **CVE** (por ejemplo, `CVE-2024-3400`) y una nota de peligro del 0 al 10 llamada **CVSS**.\n\nSi un servidor expuesto a Internet tiene un fallo con nota **9.8 (CRITICAL)**, debemos actualizarlo de inmediato antes que cualquier otro.",
                        codeSnippet = "CVSS 0.1 - 3.9  ──► Baja\nCVSS 4.0 - 6.9  ──► Media\nCVSS 7.0 - 8.9  ──► Alta\nCVSS 9.0 - 10.0 ──► ¡CRÍTICA! (Prioridad #1)",
                        keyTakeaway = "¡Supera este laboratorio para graduarte de Analista Junior y desbloquear el Nivel 5: Cazador (Hacking Ético Red Team)!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Encuentra la Vulnerabilidad Crítica (9.8)",
                    briefing = "Revisa el reporte del escáner ejecutando `cat vuln_scan_results.txt` para identificar el servidor en peligro crítico.",
                    initialBanner = listOf(
                        "GESTOR DE VULNERABILIDADES CVE/CVSS",
                        "Reporte listo: vuln_scan_results.txt"
                    ),
                    quickChips = listOf("cat vuln_scan_results.txt", "grep CRITICAL vuln_scan_results.txt"),
                    hint = "Toca el chip 'cat vuln_scan_results.txt' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat vuln_scan_results.txt", "grep critical vuln_scan_results.txt"),
                    requiredTokens = listOf("vuln_scan_results.txt"),
                    successOutput = listOf(
                        "[HIGH]     HOST: 10.10.20.14 | CVE-2024-21887 | CVSS: 8.2",
                        "[CRITICAL] HOST: 203.0.113.10 | CVE-2024-3400  | CVSS: 9.8 | CISA-KEV: YES",
                        "[+] ¡Prioridad #1 localizada en el host 203.0.113.10 (CVSS 9.8)!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l4_t5_1",
                        question = "En la escala internacional CVSS (de 0 a 10), ¿qué nivel de gravedad tiene una vulnerabilidad con puntuación de 9.8?",
                        options = listOf("Crítica (Critical)", "Baja (Low)", "Informativa", "Sin riesgo"),
                        correctIndex = 0,
                        explanation = "Toda puntuación CVSS entre 9.0 y 10.0 se clasifica con severidad Crítica."
                    )
                )
            )
        )
    )
}
