#LoadGen : A LightWeight HTTP Load Generator

LoadGen is an HTTP load generator built based on user level threads. It can generate high load and consumes fewer resources compared to many existing testing tools.

##Overview

LoadGen is a load testing tool which is developed as a web application. It is developed based on Java fibers 
which is a user-level thread implementation in Java. It supports basic HTTP testing, database testing and web 
service testing. The tool is useful in scenarios where the requests are having high response time where many existing tools failed to generate load. It have advanced features like regular expression extractor and delay timer to create complex test scenarios.

##Required software packages/tools

- Apache Tomcat 8.0.23
- Apache Maven 
- Java jdk 1.8 or above

##Directory structure

- src: Contains source code files
- docs: Contains documents related to LoadGen
- pom.xml: File to install the dependencies for different libraries

######User Manual

Please find the document User_Manual.pdf under the docs folder. It contains detailed instructions for downloading, installing and running the LoadGen and use it to run various tests.

######Developer Manual

Please find the document Developer_Manual.pdf under the docs folder. It contains detailed instructions for understanding the codebase.

######Authors

- Stanly Thomas, Master's student (2014-2016), Dept. of Computer Science and Engineering, IIT Bombay.
- Prof. Mythili Vutukuru, Dept. of Computer Science and Engineering, IIT Bombay.

######Contact

- Stanly Thomas, stanlyjohn2@gmail.com
- Prof. Mythili Vutukuru, mythili@cse.iitb.ac.in
