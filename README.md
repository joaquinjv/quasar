# Quasar fire operation

## Project goals

The main idea is to have a small demo project, which contains the main ideas of the development of a project. That it is scalable and that it maintains the solid ideas of why we do it the way we do it.

Objectives:
* Decode messages from different satellites that send the same message but can get different snippets from each one. Reset it if possible.
* From the coordinates of each satellite and certain distances, obtain the position of the satellite that sends the message.

solution:
* The assembly of the message is simply to verify the messages little by little and when it is complete, respond. If we went through all the messages and could not reassemble the message, it is already an unfinished response.
* Obtaining the coordinate of the satellite that sends the information is not something trivial. Doing some research is done through trilateration. Mathematically we find information and we must transcribe the same in the form of code to obtain an answer. At the same time there was a Java library that I used to check the results and also advanced websites to do the calculation. 
   * Sources: https://en.wikipedia.org/wiki/True-range_multilateration
   * https://www.edb.gov.hk/attachment/en/curriculum-development/kla/ma/res/STEM%20example_sec_trilateration%20GPS_eng.pdf
   * Nota: Note: In fact, the coordinate test was made from an example of the pdf.

## Technologies

For the present project I used Java and Spring Boot as a framework. I used "properties" files about configuration of the system variables because in this occasion we haven't used any database to get or store different kind of information, so, instead of hardcode data, I include all of these in an . properties file. I used i18n for the translation of the system. I worked with slfj through log4j for the logging system. JUnit and JaCoCo were the tool used for all in relation with test in the project. Gradle was chosen to automate the building of the code. Finally I used github as a repository of the code and gcloud or appengine to have an environment in the cloud to test the project in "production" mode.

## Object Model

For the data transfer from the http request to the different layers, I use the value object model (or VO). In this way, we have all the properties that we need to be able to use all the information through the project. Also if for example we add entities, through the VO, we could make a mapping something like "VO <-> Entity" to get some information from the database or transform of an inverse way the entity to vo. We can use for example, dozer to make this job. The database could be anyone it works, for example, PostgreSQL or Mysql.

## Architecture

The first thing to analyze is the structure of the project. This one was done following the multitier architecture, where each one of them has an specific responsability and between these, they work in a complement way in order to attend every request in the system, in consequence achieving a decoupling of the jobs. 

At the same time, the project we divided in different packages, these are in group that belong in a particular area, for example, entities, controllers, services.

* Controller: There are the classes that define the endpoints in order to process every request of the application.
* Exception: There are the personalized exception classes in our system.
* Services: There are the classes with the bussiness logic to solve the main issues in our application from the information we get in the controller layer.
* Message: There are the classe that handle i18n in the system.
* Model.Vo: The are the Value Object classes that we use to transfer information throug the layers and in case we have entities, these could be use to map entity/object. 

The code of the project follow the SOLID principles.

The following graphic respresents one HTTP request, and how it works in each layer until it get one response.

### ![alt text](https://user-images.githubusercontent.com/10168644/155060110-d6acfdab-b544-4e71-8b6b-5df1decf38bf.png)

The model continues a clear guideline, but if we want to expan the application, we could develop this one for example as the next example.

### ![alt text](https://user-images.githubusercontent.com/10168644/155060106-193e423e-626d-4413-979c-df30dd64d51e.png)

We see more complexity in the structure of the project and we will briefly explain what each item is about.

On the client side we have an HTTP request from some random client. This request will be attended by the Controller layer, after assembling the necessary information, it transfers this information to the Service layer, who will be in charge of validating different types of information and carrying out the corresponding logic to finally access the last layer before to get to the database. Then it is simply the decision we make, we can have an intermediate DAO layer and then have it call the repository or have a repository directly, who will be in charge of communicating with the database. If we have a lot of complexity in our system we can recommend having a kind of DAO layer in it.
Almost finishing, we can have a cache to access the elements that are accessed very frequently in the system, in order to lower the read/write load on the database, in this case we chose redis for such a function. Finally we would have a relational database to store system data, again this is a decision, using different technology we could have chosen a non-sql database, recommended for when we need "real time" readings, that is, It depends on our project and objectives, we will opt for one in particular.
Once we have the information we need from the database, we return the request to the client.

## Project Test

We use JUnit to have tests of the project and we use JaCoCo as a tool to have reports of the tests of the same, with this tool we can review the code sweep that we have covered in the project. Access to results. We believe that it is of great importance to have these tests.

### [Link al reporte de JaCoCo](https://github.com/joaquinjv/quasar/blob/main/coverageJaCoCo.html)

## Run the project

You must clone the project from the following link. Once raised in our local environment, a POST must be made through any client, such as curl, postman, to the following url.

* http://localhost:8080/core/topsecret

The information to send must be in a JSON format, for example:

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
The response will be something as the next format.
```
{
    "position": {
        "x": 21.0,
        "y": 20.0
    },
    "message": "este es un mensaje secreto"
}
```
Status 200 OK.

In case we couldn't encode the message or we couln't get the coordinates, the response will be Status 404 NOT FOUND.

### Cloud Project

The project was uploaded to appengine to perform tests in "production", the request may be the same but the URL to perform the POST is as follows:

https://quasarml-340722.lm.r.appspot.com/core/top_secret

A possible graph of how the cloud is structured can be the following.

## ![alt text](https://user-images.githubusercontent.com/10168644/155060111-4fcfa041-60d3-4049-af9f-0b6e2b940db6.png)

The project was built with gradle and deployed in the gcloud environment through a jar.



---


# Operación Fuego de Quasar

## Objetivo del proyecto

La idea es tener un proyecto demo pequeño, que contenga las ideas principales del desarrollo de un proyecto. Que sea escalable y que mantenga las ideas sólidas de por qué lo hacemos como lo hacemos.

Objetivos:
* Decodificar mensajes de diferentes satélites que envian el mismo mensaje pero puede llegar diferentes retazos de cada uno. Rearmarlo si es posible.
* A partir de las coordenadas de cada satélite y ciertas distancias, obtener la posición del satélite que envía el mensaje.

Solución:
* El armado del mensaje es simplemente verificar de a poco los mensajes y cuando esté completo responder. Si pasamos por todos los mensajes y no pudimos rearmar el mensaje ya es una respuesta inconclusa.
* La obtención de la coordenada del satélite que envía la información, no es algo trivial. Investigando un poco se hace mediante trilateración. Matemáticamente encontramos información y eso mismo debemos transcribir en forma de código para obtener una respuesta. Al mismo tiempo había una librería en Java que usé para comprobar los resultados y también sitios web de matemática avanzada para hacer el cálculo.
   * Fuentes: https://en.wikipedia.org/wiki/True-range_multilateration
   * https://www.edb.gov.hk/attachment/en/curriculum-development/kla/ma/res/STEM%20example_sec_trilateration%20GPS_eng.pdf
   * Nota: De hecho el test de coordenadas se hizo a partir de un ejemplo del pdf.

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





