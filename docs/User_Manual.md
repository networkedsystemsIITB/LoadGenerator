#LoadGen 1.0

###1.Introduction

LoadGen is a  web application which is designed to load test client/server software (such as a web application). It is developed based on fibers which is a lightweight thread implementation in Java. This implementation helps in generating very high load without consuming much resources. It can be used for HTTP, database and web service testing.

###2.Download and installation instructions

- Download the latest release of LoadGen: https://github.com/stanlyjohn2/LoadGenerator and extract it. Set environment variable LOADGEN_HOME to the LoadGenerator directory.
- Download Apache maven: https://maven.apache.org/download.cgi and extract it. Set environment variable M2_HOME to the Maven directory.
- Download Apache Tomcat 8.0.23: https://archive.apache.org/dist/tomcat/tomcat-8/v8.0.23/bin/  and extract it. Set environment variable CATALINA_HOME to the tomcat home directory.
- Download comsat-tomcat-loader-0.7.0-jdk8.jar  https://www.1maven.com/findpomandjar/co.paralleluniverse:comsat-tomcat-loader:0.5.0 and put it in $CATALINA_HOME/lib directory.
- Download Jdk 1.8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html and extract it. Set environment variable JAVA_HOME to the jdk directory.

  Example: export LOADGEN_HOME=/home/stanly/LoadGenerator
      	   export JAVA_HOME=/usr/lib/jvm/jdk1.8.0_91                                   
  	       export M2_HOME=/usr/apache/apache-maven-3.3.9                               
   	    	 export CATALINA_HOME=/usr/apache/apache-tomcat-8.0.23  

- Set the path variables 
		
	Example: export PATH=${PATH}:${M2_HOME}/bin:${CATALINA_HOME}/bin  

- Run the following command in Loadgen home directory: mvn -Ptomcat8x dependency:properties package cargo:run

- Go to http://localhost:8080/LoadGen for the homepage of the loadgen.



