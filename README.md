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
