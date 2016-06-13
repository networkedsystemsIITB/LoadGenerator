#LoadGen : A LightWeight HTTP Load Generator

LoadGen is an HTTP load generator built based on user level threads. It can generate high load and consumes fewer resources compared to many existing testing tools.

##Overview

LoadGen is a load testing tool, developed as a web application. It uses Java fibers, 
a user-level thread implementation in Java. It supports basic HTTP testing, database testing and web 
service testing. The tool is useful in scenarios where the requests have high response times. Many existing tools are developed for scenarios with high request rate and quick response times, and fail to keep up with load generation when server response times are high, as the number of outstanding requests becomes very large. Our tool also has advanced features like regular expression extractor and delay timer to create complex test scenarios.

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
