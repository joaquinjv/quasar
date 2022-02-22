# ML - Operación Fuego de Quasar

## Tecnología

Para el presente proyecto se utilizó Java y Spring Boot como framework. Se utilizó archivos properties para configurar cuestiones de variables del sistema ya que esta vez no utilizamos ninguna base de datos para obtener diferente tipo de información, entonces, en vez de harcodear datos, los incluimos en un archivo .properties. Se utilizó i18n para las traducciones del sistema. Utilizamos slfj mediante log4j para logueos del sistema. Utilizamos JUnit y JaCoCo para lo que respecta a los Test del sistema. Utilizamos gradle para automatizar la construcción de nuestro sistema. Por último usamos github como repositorio de nuestro código y gcloud o appengine para tener un ambiente en la nube y probar nuestro proyecto en "producción".

## Modelo de objetos

Para hacer la transferencia de datos desde el request HTTP hasta las diferentes capas, usamos el modelo de ValueObject o VO. De esta manera tenemos todas las propiedades que necesitamos para poder utilizar la información en el proyecto. Además si por ejemplo le agregásemos entidades al sistema, por medio del VO, podemos hacer un mapeo del estilo VO <-> Entity para obtener información de la base de datos o para transformar de manera inversa la entidad a VO. Se puede utilizar por ejemplo dozer para realizar dicha tarea. La base de datos puede ser cualquiera que nos sirva, ejemplo, PostgreSql o Mysql.

## Arquitectura

Lo primero que vamos a exponer, es la estructura del proyecto. La misma se hizo siguiendo el lineamiento multicapa, dónde cada una tiene una responsabilidad y entre ellas se complementan para poder atender los requerimientos. 

Al mismo tiempo el proyecto lo dividimos en diferentes package para agrupar las clases que pertenecen a un área particular, ejemplo, entidades, controllers, services, etc.

* Controller: Son las clases que definen los endpoints para poder empezar a procesar los request de la aplicación.
* Exception: Son las clases personalizadas para el manejo de errores del sistema.
* Services: Son las clases con lógica de negocio para resolver los problemas principales tiene la aplicación a partir de la información que nos brinda el controller.
* Message: Son las clases que maneja i18n del sistema.
* Model.Vo: Son las clases Value Object que utilizamos en el sistema para transferencia de información a través de las capas y en caso de tener entidades, se podrían usar para mapear entidad/objeto.

En el desarrollo del sistema se siguen los principios SOLID.

## Ejemplo de flujo desde request a response

El siguiente gráfico representa una petición HTTP y como trabaja cada capa hasta obtener la respuesta.

### ![alt text](https://user-images.githubusercontent.com/10168644/155060110-d6acfdab-b544-4e71-8b6b-5df1decf38bf.png)

El modelo sigue un lineamiento claro, pero si nosotros ampliásemos la aplicación podríamos desarrollarla de la siguiente manera por ejemplo.

### ![alt text](https://user-images.githubusercontent.com/10168644/155060106-193e423e-626d-4413-979c-df30dd64d51e.png)

Vemos más complejidad en la esrtructura del proyecto y vamos a explicar brevemente de que trata cada ítem.

En la parte del cliente tenemos una solicitud http de algún cliente aleatorio. Esa petición va a ser atendida por la capa Controller, luego de armar la información necesaria, transfiere esta información a la capa Service, quién se encargará de validar diferente tipo de información y realizar la lógica que le corresponda para finalmente acceder a la última capa antes de llegar a la base de datos. Luego es simplemente la decisión que tomememos, podemos tener una capa Dao intermedia y luego que la misma llame al repository o bien tener un repository directamente, quién se encargará de comunicarse con la base de datos. Si tenemos mucha complejidad en nuestro sistema podemos recomendar tener una suerte de capa Dao en el mismo.
Casi terminando, podemos contar con una caché para acceder a los elementos que se acceden con mucha frecuencia al sistema, de manera de bajar la carga de lectura/escritura en la base de datos, en este caso elegimos redis para tal función. Finalmente contaríamos con una base de datos relacional para almacenar datos del sistema, de nuevo esto es una decisión, utilizando diferente tecnología podríamos haber elegido por una base de datos no-sql, recomendable para cuando necesitamos lecturas "en tiempo real", es decir, depende de nuestro proyecto y objetivos optaremos por alguna en particular.
Una vez que tenemos la información que necesitamos de la base de datos devolvemos al cliente su requerimiento.

## Test del proyecto

Utilizamos JUnit para tener test del proecto y utilizamos JaCoCo como herramienta para tener reportes de los test del mismo, con esta herramienta podemos revisar el barrido de código que tenemos cubierto en el proyecto. Acceso a los resultados. Creemos que es de gran importancia contar con estos test.

### link a jacoco reporte

## Como ejecutar

Se debe clonar el proyecto del siguiente link. Una vez levantado en nuestro ambiente local, se debe realizar un POST mediante cualquier cliente, como curl, postman, a la siguiente url.

* http://localhost:8080/core/topsecret

La información a enviar debe ser un JSON, como por ejemplo:
```
{
    "satellites": [{
        "name": "kenobi",
        "distance": 29.0,
        "message": ["este","","","mensaje","secreto"]
    },{
        "name": "skywalker",
        "distance": 25.0,
        "message": ["","es","","",""]
    },{
        "name": "sato",
        "distance": 13.0,
        "message": ["este","","un","",""]
    }]
}
```
La respuesta devuelta será del siguiente formato.
```
{
    "position": {
        "x": 21.0,
        "y": 20.0
    },
    "message": "este es un mensaje secreto"
}
```
con Status 200 OK.

En caso de no poder por ejemplo codificar el mensaje u obtener las coordenadas la respuesta será Status 404 NOT FOUND.

### Proyecto en la nube

El proyecto fue subido a appengine para realizar pruebas en "producción", el request puede ser el mismo pero la URL a realizar el POST es la siguiente:

https://quasarml-340722.lm.r.appspot.com/core/top_secret

Una posible gráfica de cómo se estructura la nube puede ser la siguiente.

## ![alt text](https://user-images.githubusercontent.com/10168644/155060111-4fcfa041-60d3-4049-af9f-0b6e2b940db6.png)

El proyecto fue armado con gradle y desplegado en el ambiente gcloud mediante un jar.



