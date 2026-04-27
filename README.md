# Vinilos - Aplicación Móvil Android

Aplicación móvil Android desarrollada en Kotlin para la plataforma Vinilos. La app consume una API REST desarrollada en NestJS y permite consultar el catálogo de álbumes y el detalle de cada álbum.

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- Android Studio
- Android SDK
- Emulador Android o dispositivo físico
- Git
- Node.js
- npm
- Docker Desktop
- Postman, opcional
- JDK 17 o superior, preferiblemente el JBR incluido con Android Studio

## 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DEL_PROYECTO>
````

---

## 2. Configurar Java para Android

Si Gradle falla por problemas de Java, usar temporalmente el JBR de Android Studio:

```powershell
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version
```

Esta configuración es temporal y solo afecta la terminal actual.

También se puede configurar desde Android Studio:

```txt
File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK
```

Seleccionar:

```txt
Embedded JDK / jbr
```

---

## 3. Ejecutar el backend NestJS

El backend de la aplicación Android está desarrollado en NestJS.

Entrar a la carpeta del backend:

```bash
cd BackVynils
```

Instalar dependencias:

```bash
npm install
```

Crear o revisar el archivo `.env`:

```env
DB_HOST=localhost
DB_USER=postgres
DB_PASS=postgres
DB_NAME=vinyls
USE_SSL=false
```

Levantar PostgreSQL con Docker:

```bash
docker run --name postgres-vinyls -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=vinyls -p 5432:5432 -d postgres:16
```

Si ya existe un contenedor con ese nombre:

```bash
docker start postgres-vinyls
```

Ejecutar el backend:

```bash
npm run start:dev
```

Probar en Postman o navegador:

```txt
http://localhost:3000/albums
```

---

## 4. Configurar URL del backend en Android

En la app Android, la URL base debe apuntar al backend.

Para emulador Android:

```kotlin
private const val BASE_URL = "http://10.0.2.2:3000/"
```

Para Postman desde el computador:

```txt
http://localhost:3000/
```

No usar `localhost` dentro de la app Android cuando se ejecuta en emulador, porque `localhost` apunta al emulador y no al computador.

---

## 5. Ejecutar la app Android

Abrir el proyecto Android en Android Studio:

```txt
File > Open > carpeta del proyecto Android
```

Esperar a que termine el Gradle Sync.

Verificar que haya un emulador encendido:

```powershell
adb devices
```

Debe aparecer algo como:

```txt
emulator-5554    device
```

Instalar la app desde terminal:

```powershell
.\gradlew.bat installDebug
```

O ejecutarla desde Android Studio usando el botón verde Run.

---

## 6. Probar la conexión desde el emulador

Abrir Chrome en el emulador y probar:

```txt
http://10.0.2.2:3000/albums
```

Si aparece el JSON de álbumes, la app Android puede comunicarse con el backend.

---

## 7. Ejecutar pruebas unitarias Android

Las pruebas unitarias locales están en:

```txt
app/src/test/java
```

Para ejecutarlas:

```powershell
.\gradlew.bat testDebugUnitTest
```

---

## 8. Ejecutar pruebas instrumentadas / Espresso

Las pruebas Espresso están en:

```txt
app/src/androidTest/java
```

Con el emulador encendido, ejecutar:

```powershell
.\gradlew.bat app:connectedDebugAndroidTest
```

Si hay problemas de Java, usar primero:

```powershell
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

---

## 9. Problemas comunes

### Error: `nest is not recognized`

Instalar dependencias:

```bash
npm install
```

O instalar Nest CLI:

```bash
npm install --save-dev @nestjs/cli
```

### Error de conexión a PostgreSQL

Verificar que el contenedor esté corriendo:

```bash
docker ps
```

Si el puerto 5432 está ocupado, revisar:

```bash
docker ps --filter "publish=5432"
```

### Error de Java o Gradle

Configurar temporalmente:

```powershell
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

### Error de Android con HTTP

Revisar `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Dentro de `<application>`:

```xml
android:usesCleartextTraffic="true"
```

---

## 10. Flujo recomendado de ejecución

1. Levantar PostgreSQL.
2. Ejecutar backend NestJS.
3. Verificar endpoint en Postman.
4. Encender emulador Android.
5. Ejecutar app Android.
6. Probar pruebas unitarias o Espresso si aplica.

```bash
docker start postgres-vinyls
cd BackVynils
npm run start:dev
```

Luego en Android:

```powershell
.\gradlew.bat installDebug
```

---

## Notas importantes

* No subir `local.properties` al repositorio.
* No modificar `gradlew.bat` para rutas locales de Java.
* Usar `10.0.2.2` para que el emulador Android pueda consumir servicios locales.
* Mantener el backend corriendo antes de probar la app móvil.

---
## APK hubicacion y Release

```
- El APK de la aplicacion se encuentra en la rama principal que es la rama main.
      - La ruta exacta del APK esta en: deliverables/apk/app-v0.apk
- El release Sprint 1, incluye una actalizacion de .gitignore para que incluya los archivos APK files, el app-v0.apk en deliverables y tambien el source code.

```

