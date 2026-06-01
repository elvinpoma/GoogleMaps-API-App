# GoogleMaps-API-App

App nativa en **Kotlin** y **Jetpack Compose** con **Google Maps SDK** y **Supabase**. Permite a los usuarios registrar marcadores privados mediante un long click, asociar imágenes (cámara/galería) y gestionar sus ubicaciones (CRUD) desde un menú drawer con persistencia de sesión segura.

---

## Características principales

- **Autenticación Segura:** Login y registro con Supabase
- **Google Maps Integrado:** Visualización de mapa interactivo en tiempo real
- **Gestión de Marcadores:** Crear, editar y eliminar ubicaciones (CRUD completo)
- **Captura de Imágenes:** Fotos desde cámara o galería asociadas a cada marcador
- **Menú Drawer:** Navegación fluida entre Mapa y Lista de marcadores
- **Persistencia de Sesión:** Mantiene la sesión del usuario de forma segura
- **Interfaz Moderna:** Construida totalmente con Jetpack Compose
- **Gestos Intuitivos:** Long click en el mapa para crear marcadores

---

## Capturas de Pantalla

### Autenticación y Navegación
Acceso seguro a la aplicación y exploración del mapa principal.

| Login | Registro | Mapa Vacío |
| :---: | :---: | :---: |
| ![Login](./Fotos/FotoLogIn.jpg) | ![Registro](./Fotos/FotoRegistroCuenta.jpg) | ![Mapa](./Fotos/FotoMapaVacio.jpg) |

### Creación de Marcadores
Formulario interactivo y visualización de marcadores en el mapa.

| Drawer Menu | Formulario Creación | Vista Puntero |
| :---: | :---: | :---: |
| ![Drawer](./Fotos/FotoDrawerMenu.jpg) | ![Formulario](./Fotos/FotoFormularioCreacionMarcador.jpg) | ![Puntero](./Fotos/FotoVistaPunteroMapa.jpg) |

### Detalles y Gestión
Visualización detallada de marcadores y opciones de edición.

| Detalles Marcador | Lista de Marcadores | Vista Previa |
| :---: | :---: | :---: |
| ![Detalles](./Fotos/FotoDetallesFotoMapa.jpg) | ![Lista](./Fotos/FotoListadoMarcadores.jpg) | ![Previsualización](./Fotos/FotoVistaFotoDesdeListado.jpg) |

### Edición
Herramientas para actualizar información de marcadores.

| Cambiar Imagen |
| :---: |
| ![Editar](./Fotos/FotoCambioImagen.jpg) |

---

## Tecnologías utilizadas

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Entorno de Desarrollo:** Android Studio
* **Backend:** [Supabase](https://supabase.com/) (PostgreSQL + Auth)
* **Mapas:** [Google Maps SDK](https://developers.google.com/maps)
* **Gestión de Estado:** ViewModel + StateFlow
* **Base de Datos:** Supabase + Local Storage
* **Cámara/Galería:** ActivityResultContracts
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Networking:** Retrofit + Coroutines

---

## Flujo de la Aplicación

1. **Login/Registro** - Autenticación segura con Supabase
2. **Mapa Principal** - Visualiza marcadores existentes
3. **Long Click** - Abre formulario para crear marcador
4. **Captura de Foto** - Desde cámara o galería
5. **Guardar Marcador** - Se persiste en la base de datos
6. **Gestión desde Drawer** - Editar, ver detalles o eliminar marcadores
7. **Vista Lista** - Consulta todos tus marcadores en formato de listado

---

## Instalación y Uso

1. Clona este repositorio:
   ```bash
   git clone https://github.com/elvinpoma/GoogleMaps-API-App.git
   ```
2. Abre el proyecto en **Android Studio**
3. Configura tu API key de Google Maps en `AndroidManifest.xml`
4. Configura las credenciales de Supabase en el proyecto
5. Sincroniza con Gradle
6. Ejecuta en emulador o dispositivo físico
