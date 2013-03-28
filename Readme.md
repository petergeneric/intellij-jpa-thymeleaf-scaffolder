IntelliJ JPA Thymeleaf Scaffolding Plugin
=========================================
Generates a Thymeleaf template for a JPA Entity

Motivation
----------
JAX-RS and Thymeleaf are great together but it can be a bit of a pain to generate the CRUD .html for JPA entities.
This plugin aims to take most of the hard work out of this process by generating decent-looking interfaces for viewing,
editing and listing entities.

Building
--------
This code needs to be built with IntelliJ IDEA 12.

Using
-----
Bring up the Generate menu in a class with a ```@javax.persistence.Entity``` annotation and click generate.
A file called ```(entity)_template.html``` will be generated in the same package.

For viewing convenience the template .html references bootstrap.min.js

Now simply reference one of the defined fragments in your main thymeleaf templates and your entity should be rendered nicely.
