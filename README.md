## Simple REST API using Spring

Deployed to Amazon AWS. 
For the project to work, you'll need to rename **example-application.properties** , located at src/main/resources, to **application.properties**.  


* Include your `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` to your tomcat8.conf file on your EC2 instance.

* Change the `max-file-size` and `max-request-size` on your local Tomcat or AWS EC2 Tomcat at tomcat\webapps\manager\WEB-INF\web.xml or you won't be able to deploy your app:

		<multipart-config>
	    	<!– 50MB max –>
	    	<max-file-size>92428800</max-file-size>
	    	<max-request-size>92428800</max-request-size>
	    	<file-size-threshold>0</file-size-threshold>
		</multipart-config>

* If you're not using H2 database, comment this line at SecurityConstants.java:

		http.headers().frameOptions().disable();
 




------------------------------------------------------------
# Features

*   Spring Boot
*   Spring JPA
*	Spring Security
*	BCrypt
*	JSON Web Token
*	HATEOAS
*	Unit and integration tests (JUnit)
*	H2 database


------------------------------------------------------------

Amazon AWS
*	Amazon Simple Email Service
*	EC2 Instance