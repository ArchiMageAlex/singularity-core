# Singularity Core
Is a base functionality to administer Your current universe before Big Bang and some after.

Users, their rights, production landscape - count of server instances, their monitoring, database for each module.

*Only Gods has access to administer core*

---
#Test environment config (application.properties)

*Most common cases are with Spring Cloud Config - use bootstrap.properties to address config server and place Your config into it's git.*

<code>server.port=8081</code>

<code>management.endpoints.web.exposure.include=*</code>

<code>spring.security.user.name=admin</code>

<code>spring.security.user.password=admin</code>

<code>spring.datasource.url=jdbc:postgresql://localhost:5432/postgres</code>

<code>spring.datasource.username=postgres</code>

<code>spring.datasource.password=postgres</code>

<code>spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect</code>

<code>spring.jpa.hibernate.ddl-auto = update</code>

*postgres can be installed with docker*

---
#Start Singularity Core by maven

__mvn spring-boot:run__

and access it at http://localhost:8081
