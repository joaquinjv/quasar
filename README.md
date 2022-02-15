# ML - Operación Fuego de Quasar

## Arquitectura

Lo primero que vamos a exponer, es la estructura del proyecto. La misma se hizo siguiendo el lineamiento multicapa, dónde cada una tiene una responsabilidad y entre ellas se complementan para poder atender los requerimientos. Una vista de las mismas la definimos de la siguiente manera.

###<< IMAGE >>

Al mismo tiempo el proyecto lo dividimos en diferentes package para agrupar las clases que pertenecen a un área particular, ejemplo, entidades, controllers, services, etc.

* Controller: Son las clases que definen los endpoints para poder empezar a procesar los request de la aplicación.
* Exception: Son las clases personalizadas para el manejo de errores del sistema.
* Services: Son las clases con lógica de negocio para resolver los problemas principales tiene la aplicación a partir de la información que nos brinda el controller.
* Message: Son las clases que maneja i18n del sistema.
* Model.Vo: Son las clases Value Object que utilizamos en el sistema para transferencia de información a través de las capas y en caso de tener entidades, se podrían usar para mapear entidad/objeto.

En el desarrollo del sistema se siguen los principios SOLID.

## Ejemplo de flujo desde request a response

El siguiente gráfico representa una petición HTTP y como trabaja cada capa hasta obtener la respuesta.

###<< IMAGE >>

## Como ejecutar

Se debe realizar un POST mediante cualquier cliente, como curl, postman, a la siguiente url.

* http://localhost:8080/core/topsecret




 
