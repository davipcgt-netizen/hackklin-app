package com.example.data.curriculum

import com.example.data.model.CliLabScenario
import com.example.data.model.CurriculumLevel
import com.example.data.model.CurriculumTopic
import com.example.data.model.QuizQuestion
import com.example.data.model.TheorySection

object CurriculumLevels1To2 {

    val level1Aspirante = CurriculumLevel(
        number = 1,
        rankName = "Aspirante",
        title = "Nivel 1: Aspirante",
        subtitle = "Ciberseguridad desde Cero, Redes, IPs y Puertos",
        badgeCode = "NIVEL 1",
        description = "Empieza desde lo más básico: qué es un Hacker Ético, cómo viajan los datos por Internet, qué es una dirección IP y cómo proteger las puertas de entrada de un sistema.",
        syllabusHighlights = listOf(
            "¿Qué es la Ciberseguridad y el Hacking Ético? (White Hat vs Black Hat)",
            "Cómo funciona Internet: Direcciones IP públicas y privadas",
            "Puertos de red: Las puertas digitales de toda computadora",
            "Protocolos TCP/IP y cómo se saludan dos equipos en la red",
            "Redes locales, Wi-Fi, subredes y tu primera auditoría en terminal"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l1_t1",
                levelNumber = 1,
                orderIndex = 1,
                code = "CLASE 1.1",
                title = "El Hacker Ético",
                durationMinutes = 5,
                xpReward = 120,
                summary = "Descubre qué es la ciberseguridad desde cero, la diferencia entre un ciberdelincuente y un hacker ético, y ejecuta tu primer comando real.",
                subtopicsChecklist = listOf(
                    "¿Qué es realmente hackear? Curiosidad y defensa digital",
                    "Sombrero Blanco (Defensor) vs Sombrero Negro (Atacante)",
                    "Qué es una Vulnerabilidad (el punto débil) y un Exploit (la llave)",
                    "Tu primera consola: Identificarte en el sistema con whoami"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Bienvenido al Mundo del Hacking Ético",
                        body = "La ciberseguridad es el arte de proteger computadoras, redes y datos frente a ataques digitales.\n\nUn Hacker Ético (Sombrero Blanco) usa exactamente las mismas herramientas que un atacante, pero con permiso legal para encontrar fallos antes que los criminales y cerrarlos.",
                        codeSnippet = " Sombrero Blanco (White Hat) ──► Protege y reporta fallos\n Sombrero Negro  (Black Hat) ──► Roba datos o causa daño\n Sombrero Gris   (Grey Hat)  ──► Actúa sin permiso previo",
                        keyTakeaway = "Regla #1 del Hacker Ético: Nunca audites un sistema sin autorización expresa."
                    ),
                    TheorySection(
                        heading = "Vulnerabilidad vs Exploit",
                        body = "Imagina que tu casa tiene una ventana rota que no cierra bien: ese descuido es una VULNERABILIDAD.\n\nSi alguien usa un gancho para abrir esa ventana y entrar, ese gancho es el EXPLOIT. Nuestro trabajo es encontrar la ventana abierta primero.",
                        codeSnippet = "[ Sistema ] ──► Tiene una Vulnerabilidad (Fallo)\n[ Hacker ]  ──► Usa la Terminal CLI para detectarla",
                        keyTakeaway = "En ciberseguridad usamos la terminal de comandos porque nos da control directo y rápido sobre el sistema."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Tu Primer Comando en la Terminal",
                    briefing = "Acabas de entrar a tu primera consola segura. Pregúntale al sistema qué usuario eres escribiendo o tocando el chip `whoami`.",
                    initialBanner = listOf(
                        "CONSOLA HACKKLIN v3.0 — MODO ENTRENAMIENTO",
                        "Misión: Descubre tu identidad dentro del sistema."
                    ),
                    quickChips = listOf("whoami", "id", "pwd", "help"),
                    hint = "Toca el chip 'whoami' y presiona el botón verde Ejecutar.",
                    acceptedCommands = listOf("whoami", "id"),
                    requiredTokens = listOf("whoami"),
                    successOutput = listOf(
                        "operador_aspirante",
                        "[+] ¡Excelente! Acabas de ejecutar tu primer comando real en Linux."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l1_t1_1",
                        question = "¿Cómo se le llama al profesional de ciberseguridad que busca fallos con autorización para proteger a las personas y empresas?",
                        options = listOf(
                            "Hacker de Sombrero Blanco (White Hat)",
                            "Hacker de Sombrero Negro (Black Hat)",
                            "Ciberdelincuente anónimo",
                            "Virus informático"
                        ),
                        correctIndex = 0,
                        explanation = "Los Hackers de Sombrero Blanco (White Hat) trabajan de forma ética y legal para fortalecer la seguridad."
                    ),
                    QuizQuestion(
                        id = "q_l1_t1_2",
                        question = "En ciberseguridad, ¿qué es una 'Vulnerabilidad'?",
                        options = listOf(
                            "Un programa antivirus de pago",
                            "Una debilidad o fallo en un sistema que podría ser aprovechado por un atacante",
                            "Un cable de red desconectado",
                            "La pantalla de inicio del teléfono"
                        ),
                        correctIndex = 1,
                        explanation = "Una vulnerabilidad es el hueco o debilidad; la herramienta que lo aprovecha se llama exploit."
                    )
                )
            ),
            CurriculumTopic(
                id = "l1_t2",
                levelNumber = 1,
                orderIndex = 2,
                code = "CLASE 1.2",
                title = "Direcciones IP",
                durationMinutes = 6,
                xpReward = 130,
                summary = "Aprende cómo se localizan las computadoras en Internet mediante direcciones IP y cómo comprobar si un servidor está encendido.",
                subtopicsChecklist = listOf(
                    "¿Qué es una Dirección IP? El número de identificación de cada equipo",
                    "IP Privada (dentro de tu casa/oficina) vs IP Pública (en Internet)",
                    "Cómo saber si otro equipo responde usando el comando ping"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El DNI de cada Computadora: La Dirección IP",
                        body = "Para que un mensaje llegue a tu teléfono y no al de tu vecino, cada dispositivo conectado a una red recibe una etiqueta numérica única llamada Dirección IP.\n\nEn su formato clásico (IPv4), son 4 números del 0 al 255 separados por puntos, como `192.168.1.10`.",
                        codeSnippet = "Tu Dispositivo: 192.168.1.15  (IP Privada en tu Wi-Fi)\nTu Router:      192.168.1.1   (Puerta de salida)\nServidor Web:   10.10.0.1     (Objetivo en el laboratorio)",
                        keyTakeaway = "Ningún ataque ni defensa puede ocurrir sin conocer primero la dirección IP del objetivo."
                    ),
                    TheorySection(
                        heading = "El Sonar del Hacker: El Comando PING",
                        body = "Igual que un submarino lanza un pulso de sonido para saber si hay algo cerca, en redes usamos el comando `ping`.\n\n`ping` envía un pequeño paquete al servidor y mide cuántos milisegundos tarda en regresar la respuesta.",
                        codeSnippet = "$ ping -c 4 10.10.0.1\n64 bytes from 10.10.0.1: time=0.4 ms  ◄── ¡El servidor está vivo!",
                        keyTakeaway = "Si el comando ping recibe respuesta, sabemos que el equipo objetivo está encendido y conectado."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Detecta si el Servidor está Activo",
                    briefing = "Comprueba si el servidor central `10.10.0.1` está encendido enviándole 4 pulsos con el comando `ping -c 4 10.10.0.1`.",
                    initialBanner = listOf(
                        "RADAR DE RED HACKKLIN",
                        "Objetivo: Verificar conexión con el servidor 10.10.0.1"
                    ),
                    quickChips = listOf("ping -c 4 10.10.0.1", "ip addr", "whoami"),
                    hint = "Toca el chip 'ping -c 4 10.10.0.1' y pulsa Ejecutar.",
                    acceptedCommands = listOf("ping -c 4 10.10.0.1", "ping 10.10.0.1"),
                    requiredTokens = listOf("10.10.0.1"),
                    successOutput = listOf(
                        "PING 10.10.0.1 (10.10.0.1) 56(84) bytes of data.",
                        "64 bytes from 10.10.0.1: icmp_seq=1 ttl=64 time=0.39 ms",
                        "64 bytes from 10.10.0.1: icmp_seq=2 ttl=64 time=0.35 ms",
                        "[+] ¡Servidor localizado! Responde en menos de 1 milisegundo."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l1_t2_1",
                        question = "¿Para qué sirve una Dirección IP en una red informática?",
                        options = listOf(
                            "Para identificar y localizar de forma única a un dispositivo en la red",
                            "Para aumentar el brillo de la pantalla",
                            "Para guardar fotos en la galería",
                            "Como contraseña del correo electrónico"
                        ),
                        correctIndex = 0,
                        explanation = "La dirección IP actúa como la dirección postal exacta para enviar y recibir paquetes de datos."
                    ),
                    QuizQuestion(
                        id = "q_l1_t2_2",
                        question = "¿Qué comando utilizamos en la terminal para comprobar rápidamente si un servidor o dirección IP responde en la red?",
                        options = listOf("mkdir", "ping", "clear", "exit"),
                        correctIndex = 1,
                        explanation = "El comando ping envía paquetes ICMP Echo para verificar si el host destino está activo y medir su tiempo de respuesta."
                    )
                )
            ),
            CurriculumTopic(
                id = "l1_t3",
                levelNumber = 1,
                orderIndex = 3,
                code = "CLASE 1.3",
                title = "Puertos Abiertos",
                durationMinutes = 7,
                xpReward = 140,
                summary = "Comprende qué son los puertos de una computadora, cuáles son los más importantes y cómo descubrir si hay una puerta abierta peligrosa.",
                subtopicsChecklist = listOf(
                    "Analogía del edificio: La IP es la dirección y los Puertos son las puertas",
                    "Puertos clave que todo hacker conoce: 22 (SSH), 80/443 (Web) y 23 (Telnet)",
                    "Cómo inspeccionar las puertas abiertas de un servidor con ss -tulnp"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "La IP es el Edificio, los Puertos son las Puertas",
                        body = "Un servidor tiene una sola IP, pero puede ofrecer páginas web, correo y acceso remoto al mismo tiempo. ¿Cómo no se mezclan?\n\nCada computadora tiene 65,535 puertas numeradas llamadas PUERTOS. Cada servicio escucha detrás de su propio número de puerto.",
                        codeSnippet = "Puerto 80  ──► Páginas Web sin cifrar (HTTP)\nPuerto 443 ──► Páginas Web seguras con candado (HTTPS)\nPuerto 22  ──► Administración remota segura (SSH)\nPuerto 23  ──► Telnet (¡Peligroso! Viaja sin cifrar)",
                        keyTakeaway = "Mientras menos puertos innecesarios tenga abiertos un servidor, más difícil será atacarlo."
                    ),
                    TheorySection(
                        heading = "Vigilando las Puertas con ss -tulnp",
                        body = "Un buen defensor revisa qué puertas están abiertas en su máquina. El comando `ss -tulnp` muestra al instante todos los puertos en espera de conexión (`LISTEN`) y qué programa los abrió.",
                        codeSnippet = "$ ss -tulnp\nLISTEN  0.0.0.0:22   users:((\"sshd\"))\nLISTEN  0.0.0.0:23   users:((\"telnetd\"))  ◄── ¡Puerta insegura!",
                        keyTakeaway = "Si encuentras el puerto 23 (Telnet) o 21 (FTP) abierto sin necesidad, debes cerrarlo de inmediato."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Descubre la Puerta Insegura",
                    briefing = "Revisa qué puertos están abiertos en este servidor ejecutando `ss -tulnp` para descubrir el servicio antiguo que alguien dejó expuesto.",
                    initialBanner = listOf(
                        "ESCÁNER DE PUERTOS LOCALES",
                        "Misión: Listar todos los puertos en estado LISTEN."
                    ),
                    quickChips = listOf("ss -tulnp", "netstat -tulnp", "help"),
                    hint = "Toca el chip 'ss -tulnp' y presiona Ejecutar.",
                    acceptedCommands = listOf("ss -tulnp", "ss -tuln", "netstat -tulnp"),
                    requiredTokens = listOf("tuln"),
                    successOutput = listOf(
                        "tcp  LISTEN  0.0.0.0:22   users:((\"sshd\",pid=642))",
                        "tcp  LISTEN  0.0.0.0:443  users:((\"nginx\",pid=918))",
                        "tcp  LISTEN  0.0.0.0:23   users:((\"telnetd\",pid=1102))",
                        "[!] ¡Hallazgo! El puerto 23 (telnetd) está abierto y expone contraseñas sin cifrar."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l1_t3_1",
                        question = "¿Qué número de puerto utilizan por defecto las páginas web seguras que cuentan con cifrado HTTPS?",
                        options = listOf("Puerto 21", "Puerto 23", "Puerto 443", "Puerto 3389"),
                        correctIndex = 2,
                        explanation = "HTTPS utiliza de forma estándar el puerto 443, mientras que HTTP sin cifrar usa el puerto 80."
                    ),
                    QuizQuestion(
                        id = "q_l1_t3_2",
                        question = "¿Por qué es peligroso dejar abierto el puerto 23 (Telnet) en un servidor?",
                        options = listOf(
                            "Porque transmite usuarios y contraseñas en texto plano sin ningún cifrado",
                            "Porque gasta mucha batería",
                            "Porque apaga el monitor",
                            "Porque bloquea el reloj del sistema"
                        ),
                        correctIndex = 0,
                        explanation = "Telnet no cifra la comunicación; hoy en día se reemplaza siempre por SSH (puerto 22)."
                    )
                )
            ),
            CurriculumTopic(
                id = "l1_t4",
                levelNumber = 1,
                orderIndex = 4,
                code = "CLASE 1.4",
                title = "El Saludo TCP y DNS",
                durationMinutes = 7,
                xpReward = 145,
                summary = "Aprende cómo se conectan dos equipos con el saludo de 3 pasos (TCP Handshake) y cómo el DNS traduce nombres como google.com a direcciones IP.",
                subtopicsChecklist = listOf(
                    "El Saludo de 3 Pasos de TCP: SYN -> SYN-ACK -> ACK",
                    "¿Qué es el DNS? La agenda telefónica de Internet",
                    "Cómo investigar un dominio usando el comando dig"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Saludo de 3 Pasos (Three-Way Handshake)",
                        body = "Antes de enviarte una página web, tu teléfono y el servidor se saludan en 3 pasos rápidos usando el protocolo TCP:\n\n1. Tu equipo dice: `SYN` (¿Hola, estás ahí?)\n2. El servidor responde: `SYN-ACK` (¡Sí, te escucho!)\n3. Tu equipo confirma: `ACK` (¡Perfecto, hablemos!)",
                        codeSnippet = "Tu Equipo                         Servidor\n   │ ──────── 1. SYN ───────────────► │\n   │ ◄─────── 2. SYN + ACK ────────── │\n   │ ──────── 3. ACK ───────────────► │",
                        keyTakeaway = "Los escáneres profesionales como Nmap usan este saludo para saber en milisegundos qué puertos responden."
                    ),
                    TheorySection(
                        heading = "DNS: El Traductor de Nombres a IPs",
                        body = "Los humanos recordamos nombres como `hackklin.lan`, pero las redes solo entienden números IP.\n\nEl sistema DNS funciona como la agenda de contactos de Internet: le das un nombre de dominio y te devuelve su IP y sus notas secretas (registros TXT).",
                        codeSnippet = "$ dig corp.hackklin.lan TXT\n\"sec-gateway=gw-alpha.corp.hackklin.lan\"",
                        keyTakeaway = "Investigar los registros DNS con el comando dig es uno de los primeros pasos de todo reconocimiento ético."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Investiga un Dominio con DIG",
                    briefing = "Usa la herramienta `dig` para consultar las notas de texto (`TXT`) del dominio `corp.hackklin.lan` ejecutando `dig corp.hackklin.lan TXT`.",
                    initialBanner = listOf(
                        "INVESTIGADOR DNS HACKKLIN",
                        "Dominio objetivo: corp.hackklin.lan"
                    ),
                    quickChips = listOf("dig corp.hackklin.lan TXT", "ping -c 2 corp.hackklin.lan", "help"),
                    hint = "Toca el chip 'dig corp.hackklin.lan TXT' y pulsa Ejecutar.",
                    acceptedCommands = listOf("dig corp.hackklin.lan txt", "dig txt corp.hackklin.lan"),
                    requiredTokens = listOf("corp.hackklin.lan", "txt"),
                    successOutput = listOf(
                        ";; ANSWER SECTION:",
                        "corp.hackklin.lan. 300 IN TXT \"v=spf1 include:_spf.hackklin.lan -all\"",
                        "corp.hackklin.lan. 300 IN TXT \"servidor-secreto=vault.corp.hackklin.lan\"",
                        "[+] ¡Descubriste el servidor oculto 'vault.corp.hackklin.lan' en los registros DNS!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l1_t4_1",
                        question = "¿Cuál es el orden correcto del Saludo de 3 Pasos (Three-Way Handshake) de TCP?",
                        options = listOf(
                            "SYN ➔ SYN-ACK ➔ ACK",
                            "ACK ➔ FIN ➔ RESET",
                            "PING ➔ PONG ➔ STOP",
                            "HTTP ➔ DNS ➔ IP"
                        ),
                        correctIndex = 0,
                        explanation = "Primero se envía SYN, el servidor responde SYN-ACK y el cliente confirma con ACK."
                    )
                )
            ),
            CurriculumTopic(
                id = "l1_t5",
                levelNumber = 1,
                orderIndex = 5,
                code = "CLASE 1.5",
                title = "Mapa de Red y Subredes",
                durationMinutes = 8,
                xpReward = 150,
                summary = "Domina cómo se organiza una red local, qué significa el /24 al final de una IP y gradúate del Nivel 1 inspeccionando tus tarjetas de red.",
                subtopicsChecklist = listOf(
                    "¿Qué significa /24 al final de una IP como 192.168.1.10/24?",
                    "Cuántos equipos caben en una subred estándar (254 dispositivos)",
                    "Inspección completa de tu tarjeta de red con ip addr"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "¿Qué significa el /24 en una Red?",
                        body = "Seguro has visto direcciones escritas como `192.168.1.15/24`. Ese `/24` indica el tamaño del vecindario (la subred).\n\nEn una red `/24`, los primeros tres bloques (`192.168.1`) son fijos para todos tus vecinos, y el último número va del 1 al 254 para cada dispositivo conectado.",
                        codeSnippet = "Subred /24  ──► Caben 254 equipos (ej. 192.168.1.1 al .254)\nSubred /26  ──► Caben 62 equipos  (más pequeña y segmentada)",
                        keyTakeaway = "Dividir una empresa en subredes pequeñas evita que un virus en recepción llegue a los servidores de finanzas."
                    ),
                    TheorySection(
                        heading = "Tu Radiografía de Red: ip addr",
                        body = "Para saber en qué red estás parado al entrar a un sistema Linux, ejecutas `ip addr`.\n\nEste comando te muestra tu tarjeta de red (`eth0` o `wlan0`), tu dirección IP exacta y el tamaño de la subred.",
                        codeSnippet = "$ ip addr\n2: eth0: inet 172.16.40.18/26 brd 172.16.40.63",
                        keyTakeaway = "¡Al completar este laboratorio tendrás el 100% del Nivel 1 y desbloquearás el Nivel 2: Iniciado!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Graduación Nivel 1: Analiza tu Interfaz de Red",
                    briefing = "Descubre qué dirección IP y qué subred tiene asignada tu máquina ejecutando el comando `ip addr`.",
                    initialBanner = listOf(
                        "PRUEBA FINAL DE NIVEL 1 — ASPIRANTE",
                        "Misión: Inspeccionar la configuración de red del sistema."
                    ),
                    quickChips = listOf("ip addr", "ip route", "whoami"),
                    hint = "Toca el chip 'ip addr' y presiona Ejecutar.",
                    acceptedCommands = listOf("ip addr", "ip a", "ip route", "ifconfig"),
                    requiredTokens = listOf("ip"),
                    successOutput = listOf(
                        "1: lo: inet 127.0.0.1/8 scope host lo",
                        "2: eth0: inet 172.16.40.18/26 brd 172.16.40.63 scope global eth0",
                        "[+] ¡Red identificada con éxito! Estás listo para el Quiz de graduación del Nivel 1."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l1_t5_1",
                        question = "En una red doméstica o de oficina típica con máscara `/24` (como `192.168.1.0/24`), ¿cuántos dispositivos útiles pueden conectarse como máximo?",
                        options = listOf("Solo 2 equipos", "62 equipos", "254 equipos", "1 millón de equipos"),
                        correctIndex = 2,
                        explanation = "Una red /24 tiene 256 direcciones totales; restando la dirección de red (.0) y la de broadcast (.255), quedan 254 direcciones útiles."
                    )
                )
            )
        )
    )

    val level2Iniciado = CurriculumLevel(
        number = 2,
        rankName = "Iniciado",
        title = "Nivel 2: Iniciado",
        subtitle = "Tríada CIA, Anti-Phishing, Criptografía y Contraseñas",
        badgeCode = "NIVEL 2",
        description = "Domina los 3 pilares de la seguridad de la información, cómo cazar correos falsos (Phishing), cómo funciona el cifrado AES/RSA y cómo se protegen las contraseñas con Hashes.",
        syllabusHighlights = listOf(
            "Los 3 Pilares: Confidencialidad, Integridad y Disponibilidad (CIA)",
            "Ingeniería Social y caza de correos de Phishing (SPF, DKIM, DMARC)",
            "Criptografía Simétrica (AES) y Asimétrica (Llave Pública y Privada)",
            "Hashes de contraseñas, Salt y por qué nunca se guardan en texto plano",
            "Certificados digitales HTTPS y el candado verde TLS 1.3"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l2_t1",
                levelNumber = 2,
                orderIndex = 1,
                code = "CLASE 2.1",
                title = "La Tríada CIA",
                durationMinutes = 6,
                xpReward = 150,
                summary = "Aprende las tres reglas de oro de la seguridad: Confidencialidad, Integridad y Disponibilidad, y comprueba si un archivo fue alterado con SHA-256.",
                subtopicsChecklist = listOf(
                    "Confidencialidad: Que solo quien tiene permiso pueda leer los datos",
                    "Integridad: Que nadie modifique los datos en el camino",
                    "Disponibilidad: Que el sistema esté siempre funcionando cuando se necesita",
                    "Huella digital de archivos con sha256sum"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Los 3 Pilares: Confidencialidad, Integridad y Disponibilidad",
                        body = "Toda defensa en ciberseguridad protege tres cosas (la Tríada CIA):\n\n• Confidencialidad: Que nadie espíe tus secretos.\n• Integridad: Que nadie altere tus archivos o transferencias.\n• Disponibilidad: Que el servidor no se caiga cuando los usuarios entran.",
                        codeSnippet = "Robo de base de datos      ──► Rompe la Confidencialidad\nArchivo modificado con virus ──► Rompe la Integridad\nAtaque que tumba una web   ──► Rompe la Disponibilidad",
                        keyTakeaway = "Para garantizar la Integridad de un archivo usamos una huella digital matemática llamada Hash SHA-256."
                    ),
                    TheorySection(
                        heading = "Detectando Archivos Manipulados con SHA-256",
                        body = "Si cambias aunque sea una sola coma dentro de un programa, su código SHA-256 cambia por completo.\n\nCon el comando `sha256sum` podemos verificar en un segundo si un instalador es el original o si fue modificado por un atacante.",
                        codeSnippet = "$ sha256sum agente_seguridad.bin\n4a8c91d2b305f11e...  agente_seguridad.bin",
                        keyTakeaway = "Siempre compara el hash SHA-256 de una descarga crítica contra el publicado por el fabricante."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Verifica la Huella Digital del Archivo",
                    briefing = "Comprueba si el archivo `agente_seguridad.bin` fue modificado ejecutando `sha256sum agente_seguridad.bin`.",
                    initialBanner = listOf(
                        "VERIFICADOR DE INTEGRIDAD SHA-256",
                        "Hash original esperado: e3b0c44298fc1c14..."
                    ),
                    quickChips = listOf("sha256sum agente_seguridad.bin", "ls -la", "help"),
                    hint = "Toca el chip 'sha256sum agente_seguridad.bin' y pulsa Ejecutar.",
                    acceptedCommands = listOf("sha256sum agente_seguridad.bin", "sha256sum *"),
                    requiredTokens = listOf("sha256sum", "agente_seguridad.bin"),
                    successOutput = listOf(
                        "4a8c91d2b305f11e8902a44c7718293b001928374655aabbccddeeff00112233  agente_seguridad.bin",
                        "[!] ¡Alerta! El hash no coincide con el original: el archivo fue alterado."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l2_t1_1",
                        question = "Si un atacante modifica en secreto el número de cuenta bancaria en una factura sin que el sistema se apague, ¿qué pilar de la Tríada CIA fue vulnerado?",
                        options = listOf("Disponibilidad", "Integridad", "Velocidad", "Batería"),
                        correctIndex = 1,
                        explanation = "La Integridad garantiza que la información sea exacta y no haya sido alterada sin autorización."
                    )
                )
            ),
            CurriculumTopic(
                id = "l2_t2",
                levelNumber = 2,
                orderIndex = 2,
                code = "CLASE 2.2",
                title = "Cazando Phishing",
                durationMinutes = 7,
                xpReward = 155,
                summary = "Descubre cómo los atacantes engañan a las personas con correos falsos y cómo desenmascararlos leyendo las cabeceras técnicas.",
                subtopicsChecklist = listOf(
                    "Ingeniería Social: Hackear a la persona en lugar de la máquina",
                    "Señales técnicas de un correo falso (SPF y DMARC)",
                    "Inspección forense de un correo sospechoso en consola"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Arte del Engaño: Phishing e Ingeniería Social",
                        body = "A menudo es más fácil pedirle la contraseña a un usuario mediante un engaño que romper un servidor blindado. Eso es la Ingeniería Social.\n\nEl Phishing usa correos o mensajes urgentes que imitan a tu banco o jefe para que entregues tus claves.",
                        codeSnippet = "Enlace engañoso: https://banco-seguro.com.login-falso.ru/entrar\nDominio real:    login-falso.ru  ◄── ¡Trampa!",
                        keyTakeaway = "Fíjate siempre en el dominio justo antes de la primera barra simple '/'."
                    ),
                    TheorySection(
                        heading = "El Detector de Mentiras del Correo: SPF y DMARC",
                        body = "Aunque el nombre visible diga `ceo@empresa.com`, las cabeceras internas del correo revelan la verdad.\n\nSi la etiqueta `Authentication-Results` muestra `spf=fail` o `dmarc=fail`, significa que el correo vino desde un servidor impostor.",
                        codeSnippet = "Authentication-Results: spf=softfail; dkim=fail; dmarc=fail\nReceived: from [198.51.100.77] (Servidor impostor)",
                        keyTakeaway = "Los filtros SPF, DKIM y DMARC son el escudo principal de una empresa contra la suplantación de correo."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Analiza un Correo Sospechoso",
                    briefing = "Examina las cabeceras ocultas del archivo `correo_urgente.eml` ejecutando `cat correo_urgente.eml`.",
                    initialBanner = listOf(
                        "LABORATORIO ANTI-PHISHING",
                        "Archivo en cuarentena: correo_urgente.eml"
                    ),
                    quickChips = listOf("cat correo_urgente.eml", "grep -i spf correo_urgente.eml", "help"),
                    hint = "Toca el chip 'cat correo_urgente.eml' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat correo_urgente.eml", "grep -i spf correo_urgente.eml"),
                    requiredTokens = listOf("correo_urgente.eml"),
                    successOutput = listOf(
                        "From: \"Dirección General\" <ceo@hackklin.internal>",
                        "Return-Path: <soporte@security-alert-mailer.xyz>",
                        "Authentication-Results: spf=softfail (IP 198.51.100.77); dmarc=fail",
                        "[+] ¡Phishing cazado! El remitente real era 'security-alert-mailer.xyz'."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l2_t2_1",
                        question = "Al analizar un enlace sospechoso como `https://mi-banco.com.verificar-cuenta.net/login`, ¿a qué sitio web real te llevará si haces clic?",
                        options = listOf(
                            "A mi-banco.com",
                            "A verificar-cuenta.net (el dominio impostor)",
                            "A Google",
                            "A ningún sitio porque no tiene puerto"
                        ),
                        correctIndex = 1,
                        explanation = "El dominio real es siempre el que está inmediatamente antes del primer '/' de la ruta (verificar-cuenta.net)."
                    )
                )
            ),
            CurriculumTopic(
                id = "l2_t3",
                levelNumber = 2,
                orderIndex = 3,
                code = "CLASE 2.3",
                title = "Cifrado Secreto AES",
                durationMinutes = 8,
                xpReward = 165,
                summary = "Aprende cómo se bloquea la información con llave simétrica (AES-256) y asimétrica (Pública/Privada) y descifra un reporte real.",
                subtopicsChecklist = listOf(
                    "Cifrado Simétrico (AES-256): Una misma llave para cerrar y abrir",
                    "Cifrado Asimétrico (RSA/ECC): Llave Pública para enviar, Llave Privada para abrir",
                    "Descifrado práctico con OpenSSL en la terminal"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cifrado Simétrico vs Asimétrico",
                        body = "Cifrar convierte un texto legible en un código indescifrable para cualquiera que no tenga la llave.\n\n• Simétrico (AES-256): Usa 1 sola clave secreta ultrarrápida para cifrar y descifrar.\n• Asimétrico (RSA): Usa 2 llaves: una Pública que compartes con todos y una Privada que guardas bajo llave.",
                        codeSnippet = "AES-256 (Simétrico)  ──► Ideal para cifrar discos y archivos grandes\nRSA/ECC (Asimétrico) ──► Ideal para acordar claves por Internet y firmar",
                        keyTakeaway = "AES-256 es el estándar mundial utilizado por bancos y gobiernos para proteger datos confidenciales."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Descifra el Archivo Confidencial",
                    briefing = "Usa OpenSSL para abrir el archivo cifrado `reporte_auditoria.enc` con la contraseña `Hackklin2026` tocando el primer chip.",
                    initialBanner = listOf(
                        "MOTOR CRIPTOGRÁFICO OPENSSL",
                        "Archivo bloqueado: reporte_auditoria.enc (AES-256)"
                    ),
                    quickChips = listOf(
                        "openssl enc -d -aes-256-cbc -pbkdf2 -in reporte_auditoria.enc -k Hackklin2026",
                        "ls -lh"
                    ),
                    hint = "Toca el chip con el comando 'openssl enc -d ...' y pulsa Ejecutar.",
                    acceptedCommands = listOf(
                        "openssl enc -d -aes-256-cbc -pbkdf2 -in reporte_auditoria.enc -k hackklin2026"
                    ),
                    requiredTokens = listOf("openssl", "-d", "reporte_auditoria.enc"),
                    successOutput = listOf(
                        "=== REPORTE CONFIDENCIAL DESCIFRADO ===",
                        "Estado: BÓVEDA CENTRAL ACTIVA | Clave verificada: HK-AES256-OK",
                        "[+] ¡Archivo descifrado con éxito usando AES-256!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l2_t3_1",
                        question = "¿Cuál es la característica principal del cifrado asimétrico (como RSA o Curvas Elípticas)?",
                        options = listOf(
                            "Utiliza un par de llaves matemáticas: una Llave Pública y una Llave Privada",
                            "No utiliza ninguna clave",
                            "Solo funciona en teléfonos antiguos",
                            "Borra el archivo después de leerlo"
                        ),
                        correctIndex = 0,
                        explanation = "El cifrado asimétrico emplea una clave pública (compartible) y una clave privada (secreta)."
                    )
                )
            ),
            CurriculumTopic(
                id = "l2_t4",
                levelNumber = 2,
                orderIndex = 4,
                code = "CLASE 2.4",
                title = "Hashes y Contraseñas",
                durationMinutes = 7,
                xpReward = 160,
                summary = "Entiende cómo las plataformas serias guardan tus contraseñas usando Hashes irreversibles y Salt para que nadie pueda leerlas.",
                subtopicsChecklist = listOf(
                    "Por qué jamás se deben guardar contraseñas en texto normal",
                    "Qué es un Hash con 'Salt' (condimento aleatorio único)",
                    "Algoritmos seguros (Argon2 / yescrypt / bcrypt) frente a MD5 obsoleto"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cómo se Guardan las Contraseñas de Verdad",
                        body = "Un sistema seguro NUNCA guarda tu contraseña real. En su lugar, la pasa por una trituradora matemática de una sola dirección llamada Función Hash junto con un código aleatorio único llamado Salt.\n\nAsí, aunque un hacker robe la base de datos, solo verá hashes complejos y no tu clave.",
                        codeSnippet = "MD5 (\$1\$)       ──► ¡Peligroso! Se rompe en segundos con una tarjeta gráfica\nArgon2 / yescrypt ──► ¡Seguro! Lento a propósito para frenar ataques",
                        keyTakeaway = "Para proteger contraseñas se usan algoritmos lentos y exigentes en memoria como Argon2id, bcrypt o yescrypt."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita el Archivo de Contraseñas",
                    briefing = "Inspecciona el archivo `shadow_sample.txt` ejecutando `cat shadow_sample.txt` para detectar qué cuenta usa un hash débil.",
                    initialBanner = listOf(
                        "AUDITOR DE CREDENCIALES LINUX",
                        "Prefijos: \$y\$ = yescrypt (Seguro) | \$1\$ = MD5 (Débil)"
                    ),
                    quickChips = listOf("cat shadow_sample.txt", "help"),
                    hint = "Toca el chip 'cat shadow_sample.txt' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat shadow_sample.txt", "grep admin_sec shadow_sample.txt"),
                    requiredTokens = listOf("shadow_sample.txt"),
                    successOutput = listOf(
                        "admin_sec:\$y\$j9T\$K8mP2qW9xL1zR4vN\$9bH3mLp0qRsTuVwXyZ:19780:0:99999:7:::",
                        "legacy_app:\$1\$xyz123\$ab8912cd34ef5601928374:19100:0:99999:7:::",
                        "[+] Descubierto: 'legacy_app' sigue usando MD5 (\$1\$) vulnerable."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l2_t4_1",
                        question = "¿Para qué se le añade un 'Salt' aleatorio a cada contraseña antes de calcular su hash?",
                        options = listOf(
                            "Para que dos usuarios con la misma contraseña tengan hashes totalmente distintos y no sirvan tablas precalculadas",
                            "Para que la contraseña sea más corta",
                            "Para enviar la contraseña por correo",
                            "Para desactivar el cifrado"
                        ),
                        correctIndex = 0,
                        explanation = "El Salt garantiza que cada hash sea único e invalida los ataques con Rainbow Tables."
                    )
                )
            ),
            CurriculumTopic(
                id = "l2_t5",
                levelNumber = 2,
                orderIndex = 5,
                code = "CLASE 2.5",
                title = "El Candado TLS 1.3",
                durationMinutes = 7,
                xpReward = 170,
                summary = "Descubre cómo funciona el candado de seguridad de los sitios web (Certificados X.509 y TLS 1.3) para graduarte del Nivel 2.",
                subtopicsChecklist = listOf(
                    "¿Qué hay detrás del candado de una web HTTPS?",
                    "Certificados Digitales X.509 y Autoridades de Certificación (CA)",
                    "Inspección de un certificado real con OpenSSL"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "El Pasaporte Digital de una Web: Certificado X.509",
                        body = "Cuando entras a tu banco por HTTPS, ¿cómo sabe tu navegador que no es un impostor?\n\nEl servidor presenta un Certificado Digital X.509 firmado por una Autoridad de Certificación (CA) oficial, e inicia un túnel cifrado con TLS 1.3.",
                        codeSnippet = "Autoridad Raíz (Root CA)\n   └──► Firma el Certificado de 'api.hackklin.internal'\n           └──► Activa túnel seguro TLS 1.3",
                        keyTakeaway = "¡Supera este laboratorio para completar el Nivel 2 al 100% y ascender a Nivel 3: Operador!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Inspecciona el Certificado del Servidor",
                    briefing = "Revisa quién emitió el certificado `servidor_tls.crt` ejecutando el comando OpenSSL del primer chip.",
                    initialBanner = listOf(
                        "INSPECTOR DE CERTIFICADOS PKI",
                        "Certificado bajo análisis: servidor_tls.crt"
                    ),
                    quickChips = listOf(
                        "openssl x509 -in servidor_tls.crt -noout -text",
                        "ls -l"
                    ),
                    hint = "Toca el chip 'openssl x509 -in servidor_tls.crt -noout -text' y presiona Ejecutar.",
                    acceptedCommands = listOf(
                        "openssl x509 -in servidor_tls.crt -noout -text",
                        "openssl x509 -in servidor_tls.crt -noout -dates"
                    ),
                    requiredTokens = listOf("openssl", "x509", "servidor_tls.crt"),
                    successOutput = listOf(
                        "Issuer: O = Hackklin Security Authority, CN = Hackklin Root CA",
                        "Subject: CN = api.hackklin.internal",
                        "Validity: Not After: Dec 31 23:59:59 2027 GMT",
                        "[+] ¡Certificado válido verificado! Estás listo para graduarte del Nivel 2."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l2_t5_1",
                        question = "¿Quién firma digitalmente el certificado de un sitio web para que tu navegador confíe en que es auténtico?",
                        options = listOf(
                            "Una Autoridad de Certificación (CA) de confianza",
                            "El proveedor del teclado",
                            "Cualquier usuario anónimo",
                            "El cable HDMI"
                        ),
                        correctIndex = 0,
                        explanation = "Las Autoridades de Certificación (CA) verifican la propiedad del dominio y firman criptográficamente el certificado X.509."
                    )
                )
            )
        )
    )
}
