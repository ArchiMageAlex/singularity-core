# Singularity Core
Is a base functionality to administer Your current universe before Big Bang and some after.

Users, their rights, production landscape - count of server instances, their monitoring, database for each module.

*Only Gods has access to administer core*

---
#Test environment config (application.properties)

*Most common cases are with Spring Cloud Config - use bootstrap.properties to address config server and place Your config into it's git.*

server.port=8081
management.endpoints.web.exposure.include=*
spring.security.user.name=admin
spring.security.user.password=admin
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto = update

*postgres can be installed with docker*

---
#Start Singularity Core by maven

__mvn spring-boot:run__

and access it by http://localhost:8081
