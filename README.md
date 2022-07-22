# Singularity Core

## Purpose
To make development so simple as a simple web-site

## Ideas
1. Entities are accessible through UI by @Entity and @RolesAllowed annotations without additional configurations
2. Entities has default CRUD with rich intelligent UI, generated automatically at runtime.
3. Entity class must be placed at the special git repository using built-in UI for version management, or using regular git client.
4. Core classes are accessible to modification through built-in UI as other entities. OSGi or own classloader as main idea to implement dynamical reload. 
5. Business services/activities accessible through UI by @BusinessService and @RolesAllowed annotations without additional configurations.

## Prerequisites to develop
- git 2.35+ __https://git-scm.com/downloads__
- maven 3.3+ __https://maven.apache.org/download.cgi__
- JDK 1.7+ __https://jdk.java.net/17/__

## Start Singularity Core build with maven
1. Clone git repository:

   git clone https://github.com/ArchiMageAlex/singularity-core.git
2. Change to directory:
    
    cd singularity-core
3. Execute:

    mvn clean package spring-boot:run
4. Access url __http://localhost:8081__
5. Sign in with admin/admin

## Start Singularity Core with docker, built with maven
1. Clone git repository:

   git clone https://github.com/ArchiMageAlex/singularity-core.git
2. Change to directory:

   cd singularity-core
3. Execute:

    mvn clean package docker:build
4. Execute:

    docker run -p 8081:8081 -e spring_mail_username=_your_email@your_domain.com_ -e spring_mail_password=_your_password_ -t singularity-core:latest 
5. Access url __http://localhost:8081__
6. Sign in with admin/admin

## Start Singularity Core with docker only
1. Unpack zip archive from here __https://github.com/ArchiMageAlex/singularity-core/archive/refs/heads/master.zip__
2. cd to singularity-core-master
3. Execute:
   
    docker buildx build .
4. Execute:
   
    docker run -p 8081:8081 -e spring_mail_username=_your_email@your_domain.com_ -e spring_mail_password=_your_password_ -t singularity-core:latest
5. Access url __http://localhost:8081__
6. Sign in with admin/admin