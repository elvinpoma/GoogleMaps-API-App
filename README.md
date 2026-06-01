# GoogleMaps-API-App

Flujo principal y galería compacta.

App nativa en Kotlin y Jetpack Compose con Google Maps SDK y Supabase: autenticación (login/register), creación y gestión de marcadores con imágenes (cámara/galería) y persistencia por usuario.

## Flujo principal (resumido)

- Login / Register: acceso seguro con Supabase.
- Mapa: pantalla principal para explorar y añadir marcadores.
- Drawer: menú lateral con opciones "Mapa" y "Lista".
- Long click en el mapa: abre el formulario de creación de marcador.
- Guardar marcador: aparece un puntero en el mapa.
- Click en el puntero: abre la vista de detalles del marcador con la foto.
- Drawer → Lista: muestra todos los marcadores del usuario.
- Desde la lista: ver foto (previsualización), editar (cambiar imagen) o eliminar marcador.

## Capturas de pantalla

<table>
  <tr>
    <td align="center">
      <img src="Fotos/FotoLogIn.jpg" alt="Login" width="250" />
      <p>Login — inicio de sesión con Supabase.</p>
    </td>
    <td align="center">
      <img src="Fotos/FotoRegistroCuenta.jpg" alt="Registro" width="250" />
      <p>Registro — creación de cuenta.</p>
    </td>
    <td align="center">
      <img src="Fotos/FotoMapaVacio.jpg" alt="Mapa vacío" width="250" />
      <p>Mapa — vista principal donde se añaden marcadores.</p>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="Fotos/FotoDrawerMenu.jpg" alt="Drawer menu" width="250" />
      <p>Drawer — opciones "Mapa" y "Lista" y "Cerrar Sesión".</p>
    </td>
    <td align="center">
      <img src="Fotos/FotoFormularioCreacionMarcador.jpg" alt="Crear marcador" width="250" />
      <p>Formulario — se abre tras long click para crear marcador.</p>
    </td>
    <td align="center">
      <img src="Fotos/FotoVistaPunteroMapa.jpg" alt="Vista puntero" width="250" />
      <p>Puntero — marcador visible en el mapa tras guardar.</p>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="Fotos/FotoDetallesFotoMapa.jpg" alt="Detalles marcador" width="250" />
      <p>Detalles — vista al pulsar el puntero, incluye foto y metadatos.</p>
    </td>
    <td align="center">
      <img src="Fotos/FotoListadoMarcadores.jpg" alt="Listado marcadores" width="250" />
      <p>Lista — todos los marcadores del usuario.</p>
    </td>
    <td align="center">
      <img src="Fotos/FotoVistaFotoDesdeListado.jpg" alt="Vista foto desde listado" width="250" />
      <p>Previsualización — ver la foto desde la lista.</p>
    </td>
  </tr>
  <tr>
    <td align="center" colspan="3">
      <img src="Fotos/FotoCambioImagen.jpg" alt="Editar marcador" width="250" />
      <p>Editar — cambiar imagen o datos del marcador.</p>
    </td>
  </tr>
</table>
