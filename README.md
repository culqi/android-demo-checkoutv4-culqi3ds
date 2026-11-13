# DEMO - Culqi Android + Checkout V4 + Culqi 3DS

La demo integra Culqi Android, Checkout V4 , Culqi 3DS y es compatible con la v2.0 del Culqi API, con esta demo podrás generar cargos y ordenes.

## Requisitos

* WebHosting
* Android 4.0 +
* Backend para generar cargos y órdenes.
* Afiliate [aquí](https://afiliate.culqi.com/).
* Si vas a realizar pruebas obtén tus llaves desde [aquí](https://integ-panel.culqi.com/#/registro), si vas a realizar transacciones reales obtén tus llaves desde [aquí](https://panel.culqi.com/#/registro) (1).

> Recuerda que para obtener tus llaves debes ingresar a tu CulqiPanel > Desarrollo > ***API Keys***.

![alt tag](http://i.imgur.com/NhE6mS9.png)

> Recuerda que las credenciales son enviadas al correo que registraste en el proceso de afiliación.

## Configuración

### Configurar las pruebas y ejemplos

El código y su historial usan `INSERTE_SU_LLAVE_PUBLICA` e
`INSERTE_SU_LLAVE_PRIVADA` en lugar de credenciales de ejemplo. Obtén tus
propias llaves de prueba (`pk_test_` y `sk_test_`) desde CulqiPanel y no
hagas commit de ninguna llave.

Configura tu llave pública de prueba en `Culqi.publicKey` y
`Culqi3DS.publicKey`, dentro de `app/src/main/assets/checkoutv4.html`.
Las cabeceras con `INSERTE_SU_LLAVE_PRIVADA` en ese archivo y en
`app/src/main/java/culqi/com/democheckoutv4/MainActivity.java` son ejemplos
ilustrativos: no reemplaces ese placeholder por una llave privada en el
HTML ni en el APK, tampoco si es de prueba. Implementa los cargos y las
órdenes en tu backend, guarda allí tu llave privada y adapta la demo para
consumir ese backend.

Antes de ejecutar, configura también tu hosting, `browser.loadUrl` y
`returnUrl` con tu propia URL. La demo hace llamadas reales a la API de
Culqi; usa solo credenciales y datos de prueba. Con los placeholders no
se pueden autenticar solicitudes.

Dentro de la carpeta assets encontraras un archivo con el nombre checkoutv4.html donde modificaremos las siguientes lineas:

```html
 Culqi3DS.publicKey = "INSERTE_SU_LLAVE_PUBLICA";
 Culqi.publicKey = 'INSERTE_SU_LLAVE_PUBLICA';
```

En dichas lineas estamos asignando nuestra llave pública(pk), tanto a la configuración del checkoutv4 asi como a la del Culqi 3DS. La siguiente cabecera ilustra la autenticación para cargos y órdenes y debe implementarse únicamente en tu backend:

> Importante: No debes colocar tu llave privada(sk) dentro de tu proyecto front.

```javacript
"Authorization": "Bearer INSERTE_SU_LLAVE_PRIVADA"
```

Luego debemos cargar el checkoutv4.html y el archivo jquery.min.js a nuestro webhosting.
Subido los archivos deberemos tener una ruta parecida a la siguiente:

https://{tudominio}/checkoutv4.html

Luego en el archivo MainActivity colocamos esa ruta en la siguiente parte de código


```java
browser.loadUrl("https://{tudominio}/checkoutv4.html")
```

Tambien remplazamos esa url en el archivo checkoutv4.html, esto es necesario para una correcta configuración de Culqi 3DS.

```javascript
returnUrl: "https://{tudominio}/checkoutv4.html"
```


## Inicializar la demo

Para inicializar la demo en AndroidStudio primero debemos seleccionar el emulador o celular donde se levantará la aplicación y pulsar el botón run.


## Probar la demo

Para poder visualizar la demo debemos generar un apk desde el menu Build/Build Bundle(s)/APK(s) de AndroidStudio, luego proceder a instalarlo en algún emulador o dispositivo celular.

## Importante para producción

No debes configurar tu llave privada(sk) dentro del proyecto. Los ejemplos de autenticación en HTML y Java contienen únicamente un placeholder; tu llave privada debe permanecer protegida en el backend.
Para ello debes desarrollar un backend para el proyecto, el cual hará uso de tu sk y consumirá los servicio de [cargos](https://apidocs.culqi.com/#tag/Cargos/operation/crear-cargo) y [órdenes](https://apidocs.culqi.com/#tag/Ordenes/operation/crear-orden) de Culqi, posteriomente este bakend debe ser consumido desde tu aplicación android.
