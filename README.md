# Ingeniería de Software para aplicaciones moviles (2023-15)

## Integrantes

- Victor Danilo Castaneda Pinzon
- Edwin David Tijaro Santos
- Juan Manuel González Garzón

## ¿Como descargar el código y construir la app de forma local?

1. Clonar el repositorio

```bash
git clone https://github.com/VCastanedap/ingenieria-de-software-para-aplicaciones-moviles-MISW4203-2023-15-proyecto.git
```

2. Abrir el proyecto en una terminal

```bash
cd ingenieria-de-software-para-aplicaciones-moviles-MISW4203-2023-15-proyecto
```

3. Compilar el proyecto

```bash
./gradlew build
```

./app/build/outputs/apk/debug/app-debug.apk

### Pre-requisitos

- Java 1.8 (JDK)
- JAVA_HOME configurado

## Entrega del artefacto (apk)

- [APK primer sprint](https://github.com/VCastanedap/ingenieria-de-software-para-aplicaciones-moviles-MISW4203-2023-15-proyecto/actions/runs/6766657620)
- [APK avance del segundo sprint](https://github.com/VCastanedap/ingenieria-de-software-para-aplicaciones-moviles-MISW4203-2023-15-proyecto/actions/runs/6845799822)
- [APK segundo sprint ... link roto](https://github.com/VCastanedap/ingenieria-de-software-para-aplicaciones-moviles-MISW4203-2023-15-proyecto/blob/main/app/build/outputs/apk/debug/app-debug.apk)
- [ApK tercer sprint](https://github.com/VCastanedap/ingenieria-de-software-para-aplicaciones-moviles-MISW4203-2023-15-proyecto/raw/main/app-release-unsigned.apk)

## Notas sobre el Back que desplegamos

Nuestro back fue desplegado en un servidor con IP publica, esto nos permite tener donde hacer pruebas con los moviles desde etapas tempranas del desarrollo.
Para eso usamos el proyecto del curso como base e hicimos los siguientes cambios

[Modificaciones para desplegar el Back](https://github.com/MISW-4104-Web/BackVynils/pull/16/files)

### Back del proyecto

https://appbajopruebas.com/

- Usuario: appuser
- Contraseña: quepasswordtansegura

Header que se debe poner en las peticiones
- Header: Authorization
- Value: Basic YXBwdXNlcjpxdWVwYXNzd29yZHRhbnNlZ3VyYQ==

> Nota: YXBwdXNlcjpxdWVwYXNzd29yZHRhbnNlZ3VyYQ== es la codificación Base64 de appuser:quepasswordtansegura

> UPDATE: 2023-11-07 Se retira la Basic Authorization de Nginx para facilitar los llamados al API.

### Microoptimizaciones
 - se realizo Coroutines y viewModelScope: Cambiamos la ejecución de operaciones en segundo plano a través de coroutines y utilizamos viewModelScope para gestionar automáticamente el ciclo de vida de las coroutines en los ViewModels. Esto ayuda a 
   evitar posibles fugas de memoria y a garantizar que las coroutines se cancelen cuando el ViewModel se borra.
 - Uso de withContext: Utilizamos withContext(Dispatchers.Main) en las funciones de ViewModel para asegurarnos de que las actualizaciones de la interfaz de usuario se realicen en el hilo principal
 - Manejo de errores en las operaciones asíncronas: Implementamos un manejo más robusto de errores, capturando posibles excepciones y manejando errores específicos, como los de Volley. Además, agregamos logs para ayudar en la depuración.
 - Optimización de la red: En el NetworkServiceAdapter, hicimos ajustes para mejorar la legibilidad y mantenibilidad del código, como el uso de constantes para la URL base y el manejo de respuestas JSON de manera más clara en Album para que este 
   llamado sea asincrono en un segundo hilo.


