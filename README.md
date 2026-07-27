# 🚀 FinanceAI

**FinanceAI** es una solución inteligente para analizar la salud financiera de un usuario a partir de sus transacciones e indicadores clave.  
El proyecto clasifica gastos, evalúa perfiles financieros y genera recomendaciones personalizadas a través de una **API REST**.

---

## 🎯 Objetivo

Construir un **MVP funcional** que permita:

- ✅ Clasificar transacciones en categorías financieras.
- 📊 Analizar el perfil financiero del usuario.
- 💡 Generar recomendaciones de mejora.
- 🌐 Exponer los resultados mediante **endpoints REST**.
- ☁️ Integrar el flujo con **Ciencia de Datos** y al menos un servicio de **OCI**.

---

## 🧩 Alcance del MVP

El MVP se enfoca en un flujo simple de extremo a extremo:

1. El usuario ingresa sus datos financieros.
2. El frontend envía la información al backend.
3. El backend valida, procesa y orquesta el análisis.
4. El módulo de **Ciencia de Datos** clasifica y predice el perfil financiero.
5. El backend responde con un **JSON unificado** que incluye el análisis y las recomendaciones.

---

## 🏗️ Arquitectura general

| Capa | Tecnología | Función |
|------|------------|---------|
| 🖥️ **Frontend** | Interfaz simple | Ingreso de datos y visualización de resultados. |
| ⚙️ **Backend** | Spring Boot (API REST) | Recepción, validación y orquestación del flujo. |
| 🧠 **Ciencia de Datos** | Python | Clasificación de transacciones y predicción de perfil financiero. |
| ☁️ **OCI** | Oracle Cloud Infrastructure | Despliegue y/o almacenamiento en la nube. |

---

## 📌 Endpoints (backend)

### 🔹 Clasificación de transacciones

- **Método:** `POST`  
- **Ruta:** `/api/clasificar-transacciones`  
- **Descripción:** Recibe un conjunto de transacciones y devuelve la categoría financiera correspondiente.

---

### 🔹 Análisis financiero

- **Método:** `POST`  
- **Ruta:** `/api/analisis-financiero`  
- **Descripción:** Recibe datos financieros del usuario, procesa la información y devuelve:
  - Perfil financiero
  - Probabilidad
  - Resumen de gastos
  - Recomendaciones personalizadas

## 🚀 Guía de ejecución Backend (desde cero)

A contuación se presentan los pasos para instalar las dependencias necesarias y levantar el servidor Spring Boot desde cero.

### 📋 Prerrequisitos

- **Java JDK 21**
- **Git**

---

### 1. Instalación de Java 21 (JDK 21)

#### En Linux (Ubuntu / Debian):
```bash
sudo apt update
sudo apt install openjdk-21-jdk -y
```

#### En Windows / macOS (descarga directa):
- **Descarga manual:** Descargar e instalar JDK 21 desde [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java21).

#### Verificar la instalación de Java:
```bash
java -version
```
> Se debería ver una salida indicando `openjdk version "21.x.x"` o similar.

---

### 2. Clonar el repositorio

Abre una terminal y clona el proyecto:
```bash
git clone https://github.com/No-Country-simulation/G9-LATAM-Team-59.git
cd G9-LATAM-Team-59
```

---

### 3. Navegar al directorio del Backend

```bash
cd backend
```

---

### 4. Otorgar permisos al ejecutable de Gradle (solo Linux/macOS)

El repositorio cuenta con el **Gradle Wrapper (`gradlew`)**, por lo que **no es necesario instalar Gradle globalmente**.

En Linux o macOS, otorga permisos de ejecución al script:
```bash
chmod +x gradlew
```

---

### 5. Compilar y ejecutar la aplicación

Se debe ejecutar el servidor Spring Boot utilizando el Gradle Wrapper:

- **En Linux / macOS:**
  ```bash
  ./gradlew bootRun
  ```

- **En Windows (CMD / PowerShell):**
  ```cmd
  gradlew.bat bootRun
  ```

*Nota: La primera vez que sea ejecutado, Gradle descargará automáticamente las dependencias del proyecto.*

---

### 6. Verificación y confirmación

Una vez iniciada la aplicación:
- La API estará escuchando en: `http://localhost:8080`
- Se creará automáticamente la base de datos SQLite en `backend/bd_hackathon.db`.
- Puedes verificar el funcionamiento realizando una petición `POST` al endpoint de registro o clasificación:
  ```bash
  curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"email":"test@ejemplo.com","password":"123"}'
  ```

---

## 🛠️ Tecnologías utilizadas

- Java + Spring Boot  
- Python (pandas, scikit-learn, etc.)  
- Oracle Cloud Infrastructure (OCI)  
- HTML/CSS/JS (Frontend básico)  
- Maven / Gradle  
- Postman (pruebas de API)

---

