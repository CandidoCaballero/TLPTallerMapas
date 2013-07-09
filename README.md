# Taller TLP 2k13. 
# Android: Trabajando con APIs

## Introducción

Taller sobre algunas de las APIs más famosos para desarrollar funcionalidades externas en la plataforma Android. Algunas son APIs específicas para Android, y otras son simples APIs, utilizables mediante WebServices en cualquier plataforma, ya sea móvil, web, etc.

# Demo 2: Places API

En esta segunda demo, continuaremos trabajando con la aplicación desarrollada en la demo 1, para añadir la opción de poder buscar un lugar (con ayuda de un autocompletado que no sugerirá a medida que vayamos escribiendo) y localizarlo en el mapa.

## Descripción

La aplicación Android de demostración en cuestión lo que hace es conectarse al servicio web de Google Places a través de consulta por http, para:

* **1**: Sugerirnos a medida que vayamos escribiendo el lugar que podemos estar buscando.
* **2**: Una vez que seleccionemos el lugar que nos sugiere, recolectar toda la información adicional que Google Place nos proporciona.

## Componentes

Este ejemplo está compuesto por una única Aplicación Android que hará uso de la API para Android de Google Maps en sui versión 2, y la Place API, que no es específica de Android, sino es general ya que se utiliza a través se servicios web. Mediante consulta por http, se obtiene una respuesta en formato JSON o XML. Más info acerca de [Place API] (https://developers.google.com/places/documentation/?hl=es)

## Instrucciones

Para poder usar la aplicación Android, se deberá **setear** correctamente las **KEY** para la utilización de las **APIs** utilizadas en aquellas parte del código que procede, y que está debidamente comentadas.

## Licencia / License

Copyright 2013 Paco Martín Fernández

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.