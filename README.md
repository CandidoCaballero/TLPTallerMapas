# Taller TLP 2k13. 
# Android: Trabajando con APIs

## Introducción

Taller sobre algunas de las APIs más famosos para desarrollar funcionalidades externas en la plataforma Android. Algunas son APIs específicas para Android, y otras son simples APIs, utilizables mediante WebServices en cualquier plataforma, ya sea móvil, web, etc.

# Demo 3: Google Cloud Messaging

En esta tercera demo, tras realizar en las dos primeras los trabajos con las APIs de Maps y Places de Google, utilizaremos el servicio GCM que nos brinda Google para realizar la implementación de Notificaciones Push en nuestra aplicación.

## Descripción

La aplicación de demostración en cuestión lo que hace es localizar un lugar en el Mapa y enviarlo. En este caso, para poder realizar el ejemplo autónomamente, sin necesidad de depender de otro usuario para realizar la comunicación de envío y recibo de la notificación push, la aplicación enviará la notificación al propio terminal móvil, por lo que el propio usuario que notifica su localización la recibirá. Para realizar el envío a otro usuario que no sea él mismo, simplemente bastará con modificar un par de líneas en el código del servidor y que la aplicación envíe a este, el usuario de destino, por lo que es un cambio trivial.

## Componentes

Este ejemplo se divide en dos partes:

* **El Cliente**: que será la propia **aplicación Android**.

* **El Servidor**: que será una **aplicación web** que se deberá desplegar en un servidor para que realice la comunicación entre la aplicación Android y el servicio GCM de Google. En este ejemplo se realiza la aplicación en *Node.js* + *Express.js* (javascript) ya que en la web se pueden encontrar múltiples ejemplos de uso en PHP y otras tecnologías, por lo que se prefirió elegir tecnologías emergentes para demostrar la versatilidad de uso que proporciona GCM, pudiendo utilizar la tecnología en servidor que más nos guste.

## Instrucciones

Para poder usar la aplicación Android y la aplicación Node.js en el Servidor, se deberá **setear** correctamente las **KEY** para la utilización de las **APIs** utilizadas en aquellas parte del código que procede, y que está debidamente comentadas. Además se deberá cambiar la URL donde despleguemos el servidor Node.js, en la aplicación Android, para que sepa a donde se debe conectar.

## Licencia / License

Copyright 2013 Paco Martín Fernández

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
