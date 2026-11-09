package com.example.data.curriculum

import com.example.data.model.ChallengeItem
import com.example.data.model.ChallengeType
import com.example.data.model.CliLabScenario
import com.example.data.model.CurriculumLevel
import com.example.data.model.QuizQuestion

object HackklinCurriculumCatalog {

    val allLevels: List<CurriculumLevel> = listOf(
        CurriculumLevels1To2.level1Aspirante,
        CurriculumLevels1To2.level2Iniciado,
        CurriculumLevels3To4.level3Operador,
        CurriculumLevels3To4.level4AnalistaJunior,
        CurriculumLevels5To6.level5Cazador,
        CurriculumLevels5To6.level6Especialista,
        CurriculumLevels7To8.level7Arquitecto,
        CurriculumLevels7To8.level8Leyenda
    )

    val allChallenges: List<ChallengeItem> = listOf(
        // Nivel 1 Challenges
        ChallengeItem(
            id = "chg_l1_cli",
            title = "Lab Rápido: Rastreo de Puertos Ocultos",
            category = "Redes & Sockets",
            requiredLevel = 1,
            difficulty = "Nivel 1 • Aspirante",
            xpReward = 140,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Un servicio no autorizado fue levantado en una interfaz local. Audita la tabla de sockets TCP/UDP en estado LISTEN para descubrir el puerto y el proceso exacto.",
            cliScenario = CliLabScenario(
                missionTitle = "Caza de Socket Oculto en el Nodo",
                briefing = "Ejecuta `ss -tulnp` o `netstat -tulnp` para identificar el proceso sospechoso que escucha en el puerto 4444.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #01 — SOCKET HUNTER",
                    "Objetivo: Identificar el demonio en escucha no autorizado."
                ),
                quickChips = listOf("ss -tulnp", "netstat -tulnp", "ip route", "ps aux"),
                hint = "Usa 'ss -tulnp' para listar todos los sockets numéricos y el PID asociado.",
                acceptedCommands = listOf("ss -tulnp", "netstat -tulnp", "ss -tuln"),
                requiredTokens = listOf("tuln"),
                successOutput = listOf(
                    "tcp   LISTEN 0   128   0.0.0.0:22     0.0.0.0:*   users:((\"sshd\",pid=510))",
                    "tcp   LISTEN 0   5     0.0.0.0:4444   0.0.0.0:*   users:((\"ncat-listener\",pid=2911))",
                    "[+] FLAG CAPTURADA: HK{SOCKET_LISTEN_4444_DETECTED}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l1_quiz",
            title = "Desafío de Subredes CIDR y Cabeceras TCP",
            category = "Arquitectura TCP/IP",
            requiredLevel = 1,
            difficulty = "Nivel 1 • Aspirante",
            xpReward = 120,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Pon a prueba tu agilidad mental calculando máscaras CIDR, direcciones de broadcast y respuestas de banderas TCP en escenarios reales de red.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q1_1",
                    question = "Necesitas segmentar una VLAN para alojar exactamente 25 servidores de producción desperdiciando el menor número posible de direcciones IPv4. ¿Qué prefijo CIDR debes elegir?",
                    options = listOf("/28 (14 hosts útiles)", "/27 (30 hosts útiles)", "/26 (62 hosts útiles)", "/30 (2 hosts útiles)"),
                    correctIndex = 1,
                    explanation = "Una subred /27 dispone de 5 bits de host (2^5 - 2 = 30 direcciones utilizables), cubriendo los 25 servidores con el mínimo desperdicio."
                ),
                QuizQuestion(
                    id = "c_q1_2",
                    question = "Si envías un paquete TCP con la bandera SYN hacia el puerto 8080 de un servidor activo, pero ningún proceso está escuchando en ese puerto (y no hay firewall filtrando), ¿con qué banderas responde la pila TCP/IP del servidor?",
                    options = listOf("SYN + ACK", "FIN + PSH", "RST + ACK", "URG + PSH"),
                    correctIndex = 2,
                    explanation = "Según el RFC 793, cuando llega un SYN a un puerto TCP cerrado, el host destino responde inmediatamente con un segmento RST, ACK."
                )
            )
        ),

        // Nivel 2 Challenges
        ChallengeItem(
            id = "chg_l2_cli",
            title = "Lab Criptográfico: Verificación de Firma y Hash",
            category = "Criptografía Aplicada",
            requiredLevel = 2,
            difficulty = "Nivel 2 • Iniciado",
            xpReward = 160,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Se han descargado dos binarios de actualización en `/lab`. Uno es legítimo y el otro fue troyanizado. Usa herramientas de hashing y OpenSSL para identificar el paquete auténtico.",
            cliScenario = CliLabScenario(
                missionTitle = "Detección de Binario Troyanizado por Hash SHA-256",
                briefing = "Calcula el hash de ambos paquetes ejecutando `sha256sum update_v1.bin update_v2.bin` y compáralo con el hash oficial `9a01b2c3...`.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #03 — CRYPTO VERIFIER",
                    "Hash Oficial Publicado: 9a01b2c3d4e5f60718293a4b5c6d7e8f9a01b2c3d4e5f60718293a4b5c6d7e8f"
                ),
                quickChips = listOf("sha256sum update_v1.bin update_v2.bin", "ls -la", "openssl version"),
                hint = "Ejecuta 'sha256sum update_v1.bin update_v2.bin' para comparar ambos archivos.",
                acceptedCommands = listOf(
                    "sha256sum update_v1.bin update_v2.bin",
                    "sha256sum update_v1.bin",
                    "sha256sum *"
                ),
                requiredTokens = listOf("sha256sum"),
                successOutput = listOf(
                    "9a01b2c3d4e5f60718293a4b5c6d7e8f9a01b2c3d4e5f60718293a4b5c6d7e8f  update_v1.bin [LEGÍTIMO]",
                    "00ff11ee22dd33cc44bb55aa6699778800ff11ee22dd33cc44bb55aa66997788  update_v2.bin [ALTERADO]",
                    "[+] FLAG CAPTURADA: HK{SHA256_INTEGRITY_GUARD}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l2_quiz",
            title = "Quiz Táctico: Auditoría de Cabeceras Anti-Phishing",
            category = "Ingeniería Social & PKI",
            requiredLevel = 2,
            difficulty = "Nivel 2 • Iniciado",
            xpReward = 150,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Analiza un escenario real de suplantación de identidad corporativa y determina qué política DNS y criptográfica bloquea la entrega en los buzones de los empleados.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q2_1",
                    question = "¿Qué configuración del registro DMARC ordena a los servidores de correo receptores rechazar por completo (bloquear la entrega) los mensajes que no superen la validación alineada de SPF o DKIM?",
                    options = listOf("v=DMARC1; p=none;", "v=DMARC1; p=reject;", "v=SPF1 -all", "TLS_VERIFY=false"),
                    correctIndex = 1,
                    explanation = "La política p=reject en DMARC instruye a las pasarelas receptoras a descartar los correos que fallen la alineación SPF/DKIM."
                )
            )
        ),

        // Nivel 3 Challenges
        ChallengeItem(
            id = "chg_l3_cli",
            title = "Lab Hardening: Neutralización de Binario SUID",
            category = "Linux Hardening",
            requiredLevel = 3,
            difficulty = "Nivel 3 • Operador",
            xpReward = 190,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Un administrador dejó accidentalmente el bit SUID root activo sobre `/usr/bin/find`. Localízalo y remueve el bit SUID con `chmod u-s /usr/bin/find`.",
            cliScenario = CliLabScenario(
                missionTitle = "Remediación de Permiso SUID Peligroso",
                briefing = "Revoca el bit SUID del binario `/usr/bin/find` ejecutando `chmod u-s /usr/bin/find`.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #05 — SUID REMEDIATION",
                    "-rwsr-xr-x 1 root root 239816 /usr/bin/find [PELIGRO DE ESCALADA]"
                ),
                quickChips = listOf("chmod u-s /usr/bin/find", "find / -perm -4000 2>/dev/null", "ls -l /usr/bin/find"),
                hint = "Usa 'chmod u-s /usr/bin/find' para quitar el bit Set-UID del propietario.",
                acceptedCommands = listOf("chmod u-s /usr/bin/find", "chmod 755 /usr/bin/find", "chmod 0755 /usr/bin/find"),
                requiredTokens = listOf("chmod", "/usr/bin/find"),
                successOutput = listOf(
                    "-rwxr-xr-x 1 root root 239816 /usr/bin/find",
                    "[+] Bit SUID eliminado con éxito. Escalada por GTFOBins bloqueada.",
                    "[+] FLAG CAPTURADA: HK{HARDENED_SUID_REMOVED}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l3_quiz",
            title = "Quiz Táctico: Defensa de Active Directory y GPO",
            category = "Windows Server Hardening",
            requiredLevel = 3,
            difficulty = "Nivel 3 • Operador",
            xpReward = 175,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Evalúa qué controles de sistema operativo Windows impiden el volcado de memoria LSASS y el envenenamiento de resolución de nombres en la LAN.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q3_1",
                    question = "¿Qué clave de registro / directiva de seguridad de Windows ejecuta el proceso `lsass.exe` como un Proceso Ligero Protegido (Protected Process Light - PPL) para impedir que herramientas no firmadas lean su memoria?",
                    options = listOf("RunAsPPL = 1 ( en HKLM\\SYSTEM\\CurrentControlSet\\Control\\Lsa )", "EnableLUA = 0", "AutoAdminLogon = 1", "DisableFirewall = 1"),
                    correctIndex = 0,
                    explanation = "Configurar RunAsPPL=1 activa la protección LSA en el kernel, impidiendo que procesos de usuario (incluso administradores sin driver firmado) abran lsass.exe con permisos de lectura de memoria."
                )
            )
        ),

        // Nivel 4 Challenges
        ChallengeItem(
            id = "chg_l4_cli",
            title = "Lab SOC: Detección de SQL Injection en Logs de Nginx",
            category = "Blue Team & SIEM",
            requiredLevel = 4,
            difficulty = "Nivel 4 • Analista Junior",
            xpReward = 210,
            type = ChallengeType.CLI_SIMULATOR,
            description = "El WAF reportó anomalías en el servidor web. Filtra el archivo `nginx_access.log` para identificar la IP que lanzó un ataque `UNION SELECT`.",
            cliScenario = CliLabScenario(
                missionTitle = "Caza de Payload SQLi en Access Logs",
                briefing = "Filtra `nginx_access.log` buscando el patrón `UNION` mediante `grep -i UNION nginx_access.log`.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #07 — SOC WEB LOG HUNTER",
                    "Archivo de registro: nginx_access.log (14,200 peticiones)"
                ),
                quickChips = listOf("grep -i UNION nginx_access.log", "tail -n 20 nginx_access.log", "awk '{print $1}' nginx_access.log"),
                hint = "Ejecuta 'grep -i UNION nginx_access.log' para aislar las peticiones con inyección SQL.",
                acceptedCommands = listOf("grep -i union nginx_access.log", "grep union nginx_access.log", "cat nginx_access.log"),
                requiredTokens = listOf("nginx_access.log"),
                successOutput = listOf(
                    "198.51.100.212 - - [09/Nov/2026:07:12:09 +0000] \"GET /productos.php?id=-1%20UNION%20SELECT%201,username,password_hash%20FROM%20users-- HTTP/1.1\" 200 1842",
                    "[+] Atacante identificado: 198.51.100.212 (Código HTTP 200 con tamaño de respuesta anómalo 1842 bytes).",
                    "[+] FLAG CAPTURADA: HK{SOC_SQLI_DETECTED_212}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l4_quiz",
            title = "Quiz Táctico: Correlación de Eventos SIEM",
            category = "Monitoreo SOC",
            requiredLevel = 4,
            difficulty = "Nivel 4 • Analista Junior",
            xpReward = 195,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Determina la secuencia exacta de Event IDs y señales de red que distinguen un Password Spraying de un bloqueo de cuenta accidental.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q4_1",
                    question = "¿En qué se diferencia un ataque de 'Password Spraying' de un ataque de fuerza bruta tradicional en los logs de autenticación?",
                    options = listOf(
                        "Password Spraying prueba una o pocas contraseñas comunes contra cientos de usuarios distintos lentamente para evitar bloquear cuentas, generando Event ID 4625 distribuidos entre muchos usuarios desde un mismo origen",
                        "Password Spraying solo ocurre por cable coaxial",
                        "Password Spraying nunca genera eventos 4625",
                        "Password Spraying borra el disco duro automáticamente"
                    ),
                    correctIndex = 0,
                    explanation = "Al probar 1 contraseña contra muchos usuarios espaciando los intentos, el atacante evita superar el umbral de bloqueo individual (Account Lockout Threshold)."
                )
            )
        ),

        // Nivel 5 Challenges
        ChallengeItem(
            id = "chg_l5_cli",
            title = "Lab Red Team: Descubrimiento de Subdominio y Puerto",
            category = "Pentesting & Recon",
            requiredLevel = 5,
            difficulty = "Nivel 5 • Cazador",
            xpReward = 240,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Durante una prueba de penetración autorizada, necesitas identificar qué servicio corre en el puerto 9000 del host `10.10.90.15` usando Nmap.",
            cliScenario = CliLabScenario(
                missionTitle = "Fingerprinting de Servicio en Puerto No Estándar",
                briefing = "Escanea el puerto 9000 del objetivo `10.10.90.15` con detección de versión usando `nmap -sV -p 9000 10.10.90.15`.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #09 — RED TEAM SERVICE RECON",
                    "Target: 10.10.90.15 | Puerto objetivo: 9000/tcp"
                ),
                quickChips = listOf("nmap -sV -p 9000 10.10.90.15", "nmap -A 10.10.90.15", "curl -I http://10.10.90.15:9000"),
                hint = "Ejecuta 'nmap -sV -p 9000 10.10.90.15' para interrogar el banner del puerto 9000.",
                acceptedCommands = listOf("nmap -sv -p 9000 10.10.90.15", "nmap -p 9000 -sv 10.10.90.15", "nmap -a 10.10.90.15"),
                requiredTokens = listOf("nmap", "10.10.90.15"),
                successOutput = listOf(
                    "PORT     STATE SERVICE VERSION",
                    "9000/tcp open  http    Portainer CE 2.19.4 (Docker Management UI)",
                    "[+] Servicio crítico descubierto: Panel de gestión de contenedores Portainer expuesto.",
                    "[+] FLAG CAPTURADA: HK{NMAP_PORTAINER_9000}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l5_quiz",
            title = "Quiz Táctico: Explotación y Remediación Web",
            category = "OWASP Top 10",
            requiredLevel = 5,
            difficulty = "Nivel 5 • Cazador",
            xpReward = 225,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Analiza vectores avanzados de Local File Inclusion (LFI), Cross-Site Scripting (XSS) y cabeceras Content-Security-Policy (CSP).",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q5_1",
                    question = "¿Qué atributo en una cookie de sesión (`Set-Cookie`) impide que un script malicioso ejecutado mediante una vulnerabilidad XSS pueda leer el valor de la cookie a través de `document.cookie`?",
                    options = listOf("Atributo HttpOnly", "Atributo Path=/", "Atributo Domain", "Atributo Max-Age=3600"),
                    correctIndex = 0,
                    explanation = "El flag HttpOnly prohíbe el acceso a la cookie desde APIs de JavaScript del lado del cliente como document.cookie."
                )
            )
        ),

        // Nivel 6 Challenges
        ChallengeItem(
            id = "chg_l6_cli",
            title = "Lab DFIR: Detección de Inyección en Memoria RAM",
            category = "Forense Digital",
            requiredLevel = 6,
            difficulty = "Nivel 6 • Especialista",
            xpReward = 265,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Analiza la imagen de memoria `workstation_04.raw` con el plugin `windows.malfind` de Volatility 3 para detectar páginas de memoria con permisos `PAGE_EXECUTE_READWRITE` inyectadas.",
            cliScenario = CliLabScenario(
                missionTitle = "Caza de Código Inyectado (Process Hollowing)",
                briefing = "Ejecuta `vol -f workstation_04.raw windows.malfind` para localizar regiones de memoria ejecutable inyectadas sin respaldo en disco.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #11 — DFIR MALFIND HUNTER",
                    "Evidencia RAM: workstation_04.raw"
                ),
                quickChips = listOf("vol -f workstation_04.raw windows.malfind", "vol -f workstation_04.raw windows.pslist", "help"),
                hint = "Ejecuta 'vol -f workstation_04.raw windows.malfind' para detectar páginas RWX con cabecera MZ.",
                acceptedCommands = listOf("vol -f workstation_04.raw windows.malfind", "vol -f workstation_04.raw windows.pslist"),
                requiredTokens = listOf("vol", "workstation_04.raw"),
                successOutput = listOf(
                    "PID     Process         Start VPN       End VPN         Tag     Protection",
                    "2184    explorer.exe    0x00000210a0000 0x00000210c8000 VadS    PAGE_EXECUTE_READWRITE",
                    "4d 5a 90 00 03 00 00 00 04 00 00 00 ff ff 00 00   MZ..............",
                    "[!] INYECCIÓN PE DETECTADA: Cabecera 'MZ' encontrada en región RWX privada dentro de explorer.exe (PID 2184).",
                    "[+] FLAG CAPTURADA: HK{VOLATILITY_MALFIND_MZ_2184}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l6_quiz",
            title = "Quiz Táctico: Respuesta a Incidentes de Ransomware",
            category = "DFIR & Contención",
            requiredLevel = 6,
            difficulty = "Nivel 6 • Especialista",
            xpReward = 250,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Toma decisiones críticas bajo presión durante los primeros 15 minutos de un incidente de cifrado masivo en la red corporativa.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q6_1",
                    question = "Durante un incidente activo de ransomware, identificas 3 servidores que están cifrando carpetas compartidas SMB, pero aún no han sido reiniciados. ¿Cuál es la primera acción correcta de contención y preservación?",
                    options = listOf(
                        "Aislar inmediatamente sus interfaces de red (en el switch/EDR) sin apagar la energía eléctrica para frenar la propagación lateral y poder capturar la memoria RAM",
                        "Formatear los 3 servidores de inmediato sin guardar logs",
                        "Conectar el servidor de respaldos offline a la misma VLAN infectada",
                        "Ignorar la alerta hasta el lunes"
                    ),
                    correctIndex = 0,
                    explanation = "El aislamiento de red detiene la propagación por SMB/RPC mientras preserva el estado volátil en la memoria RAM para el equipo forense."
                )
            )
        ),

        // Nivel 7 Challenges
        ChallengeItem(
            id = "chg_l7_cli",
            title = "Lab Cloud: Escaneo de Vulnerabilidades en Imagen Docker",
            category = "DevSecOps & Containers",
            requiredLevel = 7,
            difficulty = "Nivel 7 • Arquitecto",
            xpReward = 285,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Antes de promover la imagen `hackklin/api-gateway:1.4` al clúster de producción, ejecuta un escaneo de vulnerabilidades críticas con `trivy image hackklin/api-gateway:1.4`.",
            cliScenario = CliLabScenario(
                missionTitle = "Auditoría de Imagen de Contenedor con Trivy",
                briefing = "Ejecuta `trivy image hackklin/api-gateway:1.4` para inspeccionar paquetes del sistema operativo y librerías con CVEs críticos.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #13 — CONTAINER SUPPLY CHAIN AUDIT",
                    "Imagen candidata: hackklin/api-gateway:1.4"
                ),
                quickChips = listOf("trivy image hackklin/api-gateway:1.4", "docker history hackklin/api-gateway:1.4", "help"),
                hint = "Ejecuta 'trivy image hackklin/api-gateway:1.4' para escanear todas las capas del contenedor.",
                acceptedCommands = listOf("trivy image hackklin/api-gateway:1.4", "docker history hackklin/api-gateway:1.4"),
                requiredTokens = listOf("hackklin/api-gateway:1.4"),
                successOutput = listOf(
                    "hackklin/api-gateway:1.4 (alpine 3.18.2)",
                    "Total: 2 (HIGH: 1, CRITICAL: 1)",
                    "┌──────────────┬────────────────┬──────────┬───────────────────┬───────────────┐",
                    "│   Library    │ Vulnerability  │ Severity │ Installed Version │ Fixed Version │",
                    "├──────────────┼────────────────┼──────────┼───────────────────┼───────────────┤",
                    "│ libssl3      │ CVE-2024-0727  │ HIGH     │ 3.1.1-r0          │ 3.1.4-r5      │",
                    "│ libexpat     │ CVE-2024-45490 │ CRITICAL │ 2.5.0-r0          │ 2.6.3-r0      │",
                    "└──────────────┴────────────────┴──────────┴───────────────────┴───────────────┘",
                    "[+] FLAG CAPTURADA: HK{TRIVY_SUPPLY_CHAIN_GUARD}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l7_quiz",
            title = "Quiz Táctico: Arquitectura Zero-Trust en Kubernetes",
            category = "Cloud-Native Security",
            requiredLevel = 7,
            difficulty = "Nivel 7 • Arquitecto",
            xpReward = 275,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Diseña controles de admisión, cifrado de secretos en etcd y políticas de red para un entorno Kubernetes multi-tenant.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q7_1",
                    question = "¿Cómo se almacenan por defecto los objetos `Secret` de Kubernetes dentro de la base de datos `etcd` si no se configura un proveedor `EncryptionConfiguration` con KMS?",
                    options = listOf(
                        "Codificados únicamente en Base64 (texto plano sin cifrado criptográfico)",
                        "Cifrados con RSA-4096 por defecto",
                        "Hasheados con Argon2id irreversible",
                        "Comprimidos en formato MP3"
                    ),
                    correctIndex = 0,
                    explanation = "Por defecto, los Secrets en Kubernetes solo están codificados en Base64; se debe habilitar EncryptionConfiguration con KMS para cifrarlos en reposo dentro de etcd."
                )
            )
        ),

        // Nivel 8 Challenges
        ChallengeItem(
            id = "chg_l8_cli",
            title = "Lab Élite: Detección de Ataque DCSync en Controlador de Dominio",
            category = "Operaciones de Élite",
            requiredLevel = 8,
            difficulty = "Nivel 8 • Leyenda",
            xpReward = 330,
            type = ChallengeType.CLI_SIMULATOR,
            description = "Un adversario avanzado intenta replicar los hashes de todo el dominio (`ntds.dit`) simulando ser un controlador de dominio mediante el ataque DCSync (Event ID 4662). Caza el evento en `ad_replication.log`.",
            cliScenario = CliLabScenario(
                missionTitle = "Caza de Replicación Maliciosa DCSync (MITRE T1003.006)",
                briefing = "Filtra `ad_replication.log` buscando el GUID de replicación `1131f6ad-9c07-11d1-f79f-00c04fc2dcd2` o el Event ID `4662` ejecutando `grep 4662 ad_replication.log`.",
                initialBanner = listOf(
                    "HACKKLIN CHALLENGE #15 — ELITE DCSYNC THREAT HUNT",
                    "GUID DS-Replication-Get-Changes-All: 1131f6ad-9c07-11d1-f79f-00c04fc2dcd2"
                ),
                quickChips = listOf("grep 4662 ad_replication.log", "cat ad_replication.log", "help"),
                hint = "Ejecuta 'grep 4662 ad_replication.log' o 'cat ad_replication.log' para identificar el origen del DCSync.",
                acceptedCommands = listOf("grep 4662 ad_replication.log", "cat ad_replication.log"),
                requiredTokens = listOf("ad_replication.log"),
                successOutput = listOf(
                    "2026-11-09T08:55:12Z [EventID=4662] SubjectAccount=CORP\\adm_compromised ObjectServer=DS Properties={1131f6ad-9c07-11d1-f79f-00c04fc2dcd2} WorkstationIP=10.20.14.89",
                    "[!] ATAQUE DCSYNC CONFIRMADO: La cuenta 'CORP\\adm_compromised' solicitó replicación de secretos desde una estación de trabajo (10.20.14.89) que NO es un Domain Controller.",
                    "[+] FLAG CAPTURADA: HK{ELITE_DCSYNC_HUNTER_4662}"
                )
            )
        ),
        ChallengeItem(
            id = "chg_l8_quiz",
            title = "Quiz Maestro: Explotación de Memoria y Defensas del Kernel",
            category = "Investigación de Vulnerabilidades",
            requiredLevel = 8,
            difficulty = "Nivel 8 • Leyenda",
            xpReward = 310,
            type = ChallengeType.SCENARIO_QUIZ,
            description = "Demuestra tu dominio absoluto sobre ASLR, PIE, GOT/PLT, Use-After-Free y arquitectura de detección comportamental.",
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c_q8_1",
                    question = "¿Por qué un atacante necesita primero una vulnerabilidad de 'Information Disclosure' (fuga de un puntero de memoria) para explotar de forma fiable un binario que tiene activados tanto ASLR como PIE?",
                    options = listOf(
                        "Porque ASLR + PIE aleatorizan la dirección base del binario y de libc en cada ejecución; filtrar un puntero permite calcular la dirección base real restando su desplazamiento (offset) estático",
                        "Porque ASLR cifra el código fuente con una contraseña de usuario",
                        "Porque PIE borra los registros del procesador cada 5 segundos",
                        "Porque sin Information Disclosure el puerto TCP permanece cerrado"
                    ),
                    correctIndex = 0,
                    explanation = "Aunque ASLR cambia la dirección base en cada arranque del proceso, los desplazamientos relativos (offsets) dentro de una misma librería o binario permanecen constantes."
                )
            )
        )
    )
}
