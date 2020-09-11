# Spring boot backend template

## Config files (make it before run the application)
#### make an copy from this files:
> src/main/resources/application-example.properties
src/main/resources/application-dev-example.properties
src/test/resources/application-example.properties

#### to this destinations files:
> src/main/resources/application.properties
src/main/resources/application-dev.properties
src/test/resources/application.properties

#### NOTICE: 
The target files are ignored by git. So you can add sensitive information to these files

## Run it!

```
mvnw spring-boot:run
```

## Test it!
```
mvnw test
```

Run the application on defatult port http://localhost:8080

## Distribut it!

```
mvnw install
java -jar target/spring-jwt-backend-template-0.0.1-SNAPSHOT.jar
```

  - Install dependencies and create a jar file on the target folder.
  - Run the web application from jar file that has been created.

## Request examples

Open [http://localhost:8080](http://localhost:8080)

Sing up Example [POST]:
[http://localhost:8080/auth/sing-up](http://localhost:8080/auth/sing-up)
body:
`{
    "name":"User name",
    "email":"test@test.com",
    "password":"MyPassord"
}`

#### NOTICE:
You can disable public url sing up changing auth.public-sing-up-url-enable parameter on the application-dev.properties file

Login Example (Get JWT Token on the Headers Authorization) [POST]
[http://localhost:8080/login](http://localhost:8080/login)
Body
`{
    "email":"test@test.com",
    "password":"MyPassord"
}`

Authenticated request example [GET]
[http://localhost:8080/category/1](http://localhost:8080/category/1)
Headers
`{
    "Authorization":"<Token recived on the previous request>",
    "Content-type":"application/json"
}`