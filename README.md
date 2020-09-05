# Spring backend template

## Avaliable scripts

### `mvnw install`
Install dependencies and create jar file on the target folder

### `java -jar target/spring-jwt-backend-template-0.0.1-SNAPSHOT.jar`

Run the web application.
Open [http://localhost:8080](http://localhost:8080)

Sing up Example [POST]:
[http://localhost:8080/auth/sing-up](http://localhost:8080/auth/sing-up)
body:
`{
    "name":"User name",
    "email":"test@test.com",
    "password":"MyPassord"
}`

OBS: You can disable public url sing up changing auth.public-sing-up-url-enable parameter at the application-deve.properties file

Sing in Example (Get JWT Token on the Headers Authorization) [POST]
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

### `mvnw test`

Run the integrations tests.
