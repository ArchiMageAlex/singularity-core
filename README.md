# Singularity Core
Main project ideas:

1. Entities are accessible through UI by @Entity and @RolesAllowed annotations without additional configurations
2. Entities has default CRUD with rich intelligent UI
3. Entity class must be placed at the special git repository with UI for version management
4. Core classes can be accessible to modification through UI. OSGi as main idea to implement dynamical reload. 
5. Business services/activities accessible through UI by @BusinessService and @RolesAllowed annotations without additional configurations

## Start Singularity Core by maven

__mvn spring-boot:run__
 
and access it at http://localhost:8081
