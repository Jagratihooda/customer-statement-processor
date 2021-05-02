# Customer Statement Processor #

### Assignment ###

* Quick summary

  Rabobank receives monthly deliveries of customer statement records. This information is delivered in JSON format. 
  These records need to be validated based on below conditions
  
     * all transaction references should be unique
     * end balance needs to be validated 
       
### Tech Stack

* Java 1.8
* Spring Boot
* Project Lombok 
* JUnit/Mockito

### Tools:
 * Intellij
 * Maven

## How to run

 * Run `mvn clean install`
 * Run `mvn spring-boot:run`
 * Hit below URL in browser to run the application 
   http://localhost:8081/processor/v1/customer-statement
 * Use below json for making the post request:
     *  `{
   "input": [
   {
   "reference": "1",
   "accountNumber": "IBAN1",
   "description": "First",
   "startBalance": "10",
   "mutation": "-5",
   "endBalance": "5"
   },
   {
   "reference": "2",
   "accountNumber": "IBAN2",
   "description": "Second",
   "startBalance": "20",
   "mutation": "-10",
   "endBalance": "10"
   }
   ]
   }
   `
## Scope for improvement

* Frontend implementation
* Database Addition
* CICD
* More?