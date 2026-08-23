# 🚀 FinanceAI

**FinanceAI** es una solución inteligente para analizar la salud financiera de un usuario a partir de sus transacciones e indicadores clave.  
El proyecto clasifica gastos, evalúa perfiles financieros y genera recomendaciones personalizadas a través de una **API REST**.

---

## 🎯 Objetivo

Construir un **MVP funcional** que permita:

- ✅ Clasificar transacciones en categorías financieras mediante Machine Learning (NLP).
- 📊 Analizar el perfil financiero del usuario utilizando modelos predictivos.
- 💡 Generar recomendaciones de mejora personalizadas.
- 🌐 Exponer los resultados mediante **endpoints REST**.
- ☁️ Desplegar e integrar la solución completa en **Oracle Cloud Infrastructure (OCI)** utilizando **Oracle Autonomous Database**.

---

## 🧩 Alcance del MVP

El MVP se enfoca en un flujo simple de extremo a extremo:

1. El usuario ingresa sus datos financieros y transacciones desde el Frontend.
2. El frontend envía la información al backend (Spring Boot).
3. El backend valida, procesa y orquesta la comunicación con el módulo de **Ciencia de Datos (Python)**.
4. El servidor de Python realiza la inferencia con los modelos entrenados (`.pkl`) y devuelve la clasificación y predicción de perfil.
5. El backend almacena la información en **Oracle Database** y responde al cliente con un **JSON unificado** que incluye el análisis y las recomendaciones.

---

## 🏗️ Arquitectura general

| Capa | Tecnología | Función |
| --- | --- | --- |
| 🖥️ **Frontend** | HTML5 / CSS3 / JavaScript | Interfaz de usuario interactiva y consumo de API. |
| ⚙️ **Backend** | Java 21 / Spring Boot 3 / Spring Security (JWT) | Recepción, validación, persistencia y orquestación del flujo. |
| 🧠 **Ciencia de Datos** | Python 3.11 / FastAPI / Scikit-Learn / Pandas | Inferencia de modelos ML (NLP para transacciones y perfilamiento conductual). |
| 🛢️ **Base de Datos** | Oracle Autonomous Database (ATP - Always Free) | Almacenamiento relacional seguro en la nube. |
| ☁️ **Despliegue / OCI** | Oracle Cloud Infrastructure (Ampere A1 / NGINX / Docker) | Orquestación multi-contenedor en la nube OCI. |

---

## 📌 Endpoints (Backend)

### 🔹 Registro de Cuenta
- **Método:** `POST`
- **Ruta:** `/api/auth/registrar-cuenta`
- **Autenticación:** Pública (No requiere token)
- **Descripción:** Registra un nuevo usuario en la plataforma validando la unicidad del correo electrónico y nombre de usuario.

---

### 🔹 Iniciar Sesión
- **Método:** `POST`
- **Ruta:** `/api/auth/login`
- **Autenticación:** Pública (No requiere token)
- **Descripción:** Autentica a un usuario registrado mediante sus credenciales y genera un token JWT para sesiones protegidas.

---

### 🔹 Estado del Sistema (Health Check)
- **Método:** `GET`
- **Ruta:** `/api/health`
- **Autenticación:** Pública (No requiere token)
- **Descripción:** Devuelve el estado en tiempo real del backend y verifica la conectividad en vivo mediante un ping a Oracle Database.

---

### 🔹 Clasificación de Transacciones
- **Método:** `POST`
- **Ruta:** `/api/clasificar-transacciones`
- **Autenticación:** Pública (No requiere token)
- **Descripción:** Recibe un conjunto de transacciones, las procesa con el modelo NLP de Python y devuelve el monto total agrupado por categoría financiera.

---

### 🔹 Análisis Financiero (Puntual)
- **Método:** `POST`
- **Ruta:** `/api/analisis-financiero`
- **Autenticación:** Pública (No requiere token)
- **Descripción:** Evalúa la salud financiera del usuario según su ingreso, nivel de endeudamiento, ahorro y transacciones enviadas en la petición.

---

### 🔹 Análisis Financiero Histórico
- **Método:** `POST`
- **Ruta:** `/api/analisis-financiero/mis-transacciones`
- **Autenticación:** Protegida (`Authorization: Bearer <token>`)
- **Descripción:** Evalúa el perfil financiero utilizando las transacciones almacenadas históricamente en el perfil del usuario autenticado (soporta filtro por fechas).

---

### 🔹 Registrar Transacción
- **Método:** `POST`
- **Ruta:** `/api/transacciones`
- **Autenticación:** Protegida (`Authorization: Bearer <token>`)
- **Descripción:** Invoca automáticamente la clasificación por IA para asignar la categoría correspondiente y almacena la transacción en el historial del usuario.

---

### 🔹 Ver Transacciones (con filtro)
- **Método:** `GET`
- **Ruta:** `/api/transacciones?desde={fecha}&hasta={fecha}`
- **Autenticación:** Protegida (`Authorization: Bearer <token>`)
- **Descripción:** Devuelve el listado de transacciones registradas del usuario autenticado, con filtro opcional por rango de fechas.

---

### 🔹 Eliminar Transacción
- **Método:** `DELETE`
- **Ruta:** `/api/transacciones/{id}`
- **Autenticación:** Protegida (`Authorization: Bearer <token>`)
- **Descripción:** Elimina del sistema una transacción registrada del usuario autenticado a partir de su ID.

---

## 🧠 Servidor de Ciencia de Datos e Inteligencia Artificial (Python)

El módulo de Inteligencia Artificial reside en la carpeta `python/servidor_local_py` y está construido con **FastAPI** y **Scikit-Learn**. Expone servicios de inferencia consumidos por el Backend de Spring Boot.

### 🤖 Modelos y Artefactos Incluidos:
- **`modelo_clasificador_transacciones.pkl` + `tfidf_vectorizer.pkl`**: Modelo de Procesamiento de Lenguaje Natural (NLP) que analiza el texto descriptivo de las transacciones (ej: *"Starbucks Providencia"*) y asigna automáticamente la categoría correspondiente (*Alimentación, Transporte, Entretenimiento, Servicios, etc.*).
- **`modelo_clasificador_perfiles_rf_volatiles.pkl`**: Modelo de clasificación basado en **Random Forest** que analiza el comportamiento financiero a partir del ingreso mensual, nivel de endeudamiento, frecuencia de ahorro, gasto total y ratio de endeudamiento/ingreso.
- **`scaler_perfiles_volatiles.pkl` y `label_encoder_frecuencia_ahorro_volatiles.pkl`**: Transformadores de datos para normalización de características continuas y codificación de variables categóricas.

### 🔌 Endpoints del Servidor de IA (Puerto 8000):
- **`POST /api/clasificacion`**: Recibe `{"descripcion": "..."}` y retorna la categoría asignada.
- **`POST /api/analisis`**: Recibe indicadores financieros y retorna el perfil predecido (*Saludable, En observación, En riesgo*) con sus probabilidades.

### 📋 Guía de ejecución del Servidor de Python localmente:

#### 1. Prerrequisitos:
- **Python 3.10+**
- **pip**

#### 2. Pasos de ejecución:
```bash
# 1. Navegar al directorio del servidor de Python
cd python/servidor_local_py

# 2. Crear y activar entorno virtual (Recomendado)
python3 -m venv venv

# En Linux / macOS:
source venv/bin/activate

# En Windows:
venv\Scripts\activate

# 3. Instalar las dependencias requeridas
pip install -r requirements.txt

# 4. Iniciar el servidor Uvicorn
uvicorn main:app --reload --port 8000
```
> El servidor estará escuchando en: `http://127.0.0.1:8000`. Puedes explorar la documentación interactiva Swagger en `http://127.0.0.1:8000/docs`.

---

## 🚀 Guía de ejecución Backend (Spring Boot)

### 📋 Prerrequisitos

- **Java JDK 21**
- **Git**

---

### 1. Navegar al directorio del Backend

```bash
cd backend
```

---

### 2. Otorgar permisos al ejecutable de Gradle Wrapper (solo Linux/macOS)

```bash
chmod +x gradlew
```

---

### 3. Compilar y ejecutar la aplicación

- **En Linux / macOS:**

  ```bash
  ./gradlew bootRun
  ```

- **En Windows (CMD / PowerShell):**

  ```cmd
  gradlew.bat bootRun
  ```

> La API del backend estará escuchando en: `http://localhost:8080`.

---

## 🌐 Ejecutar el frontend localmente

Para abrir la interfaz web en modo desarrollo sin Docker:

### Windows

```bat
cd frontend
start_server.bat
```

### Linux / macOS

```bash
cd frontend
chmod +x start_server.sh
./start_server.sh
```

Luego abre en tu navegador:

```text
http://127.0.0.1:3000/html/index.html
```
---

## 🐳 Despliegue con Docker Compose (Entorno Completo / OCI)

El proyecto incluye la infraestructura necesaria para desplegar los 3 componentes (Frontend NGINX, Backend Spring Boot, Servidor Python IA) junto a la base de datos **Oracle Autonomous Database** mediante **Docker Compose**.

### 📋 Archivos de Configuración:

1. **[.env](.env)**: Configura la URL de conexión a Oracle DB, la contraseña del usuario `ADMIN` y el secreto JWT.
2. **`oracle_wallet/`**: Carpeta donde se descomprimen los archivos de credenciales y certificados (`tnsnames.ora`, `cwallet.sso`) descargados de OCI.
3. **[docker-compose.yml](docker-compose.yml)**: Orquestador multi-contenedor.

### 🚀 Pasos para desplegar con Docker Compose:

```bash
# 1. Colocar los archivos del Wallet de Oracle dentro de /oracle_wallet
# 2. Configurar las variables en el archivo .env

# 3. Levantar todos los servicios en segundo plano
docker compose up -d --build
```

#### Puertos expuestos:

- 🖥️ **Frontend (NGINX + Reverse Proxy)**: `http://localhost:80` (o `http://TU_IP_PUBLICA`)
- ⚙️ **Backend Spring Boot**: `http://localhost:8080` o `http://localhost:80/api` 
- 🧠 **Python IA Server**: `http://localhost:8000`

---

## 🛠️ Tecnologías utilizadas

- **Backend**: Java 21, Spring Boot 3, Spring Data JPA, Spring Security, JWT (jjwt), Hibernate.
- **Ciencia de Datos**: Python 3.11, FastAPI, Uvicorn, Scikit-Learn, Pandas, Joblib.
- **Base de Datos**: Oracle Autonomous Database (ATP), Oracle Wallet, Oracle JDBC Driver (ojdbc11).
- **Frontend**: HTML5, Vanilla CSS3, JavaScript (ES6+ Modules), Bootstrap 5.
- **Despliegue & DevOps**: Oracle Cloud Infrastructure (OCI), Docker, Docker Compose, NGINX Reverse Proxy.
