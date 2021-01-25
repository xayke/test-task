Test Task
=========

Prerequisites
-------------

* [Java Development Kit (JDK) 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/download.cgi)

Build and Run
-------------

1. Run in the command line:
	```
	mvn package
	mvn jetty:run
	```

2. Open `http://localhost:8080` in a web browser.

3. Use application :)  
     

   **(Note: when you launch it and press the button for the 
   first time it's may be lagged for a sec. The reason is that 
   *database/DatabaseService* creating *INSTANCE*, 
   checking tables on existence, create them (if not exists) and fill them (if they are empty))**
