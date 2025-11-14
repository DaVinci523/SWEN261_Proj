# U-Fund:  myResearch: Student Research Funding
# Modify this document to expand any and all sections that are applicable for a better understanding from your users/testers/collaborators (remove this comment and other instructions areas for your FINAL release)

An online U-Fund system built in Java **24**
  
## Team

- Anthony Rosati Jr. 
- Anthony Pelto
- Addison Asuncion
- Seth Taylor
- Nicholas Li


## Prerequisites

- Java **24** (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
-  _add any other tech stack requirements_


## How to run it

1. Clone the repository and go to the ufund-api directory.
2. Execute `mvn compile exec:java`
3. Open a seperate terminal
4. Go to the ufund-ui directory
5. Execute `ng serve -o`
6. Open in your browser `http://localhost:8080/`

## Known bugs and disclaimers

- For the application to run the funding_basket.json should have at Least the following data: (copy & paste)
{"users": [{"userName":"helper123","basket":[]},{"userName":"helper456","basket":[]}]}

- For the application to run the needs.json should have at Least the following data: (copy & paste)
[]

- For the application to run the donations.json should have at Least the following data: (copy & paste)
{"totalDonated":0,"donationGoal":100}

- For the application to run the reviews.json should have at Least the following data: (copy & paste)
[]

- To ensure the application runs as intended the responses.json MUST be Empty: (copy & paste)
[]

- For the application to run the users.json should have at Least the following data: (copy & paste)
{"users":[{"username":"helper456","password":"456","role":"helper"},{"username":"helper123","password":"123","role":"helper"},{"username":"admin","password":"admin","role":"admin"}]}


2. Disclaimers & Unresolved features: The following are features that have not been implemented for the sake of focusing on other areas of the application
- Designated types for the needs, so that admins can only create needs that belong to the designated type
- Replace the intervals with observables / more dynamic methods of transfering data.
- Moving some logic such as checkout to the backend.
- Our AI is Not connected to our site's backend. Functionally it does not interact with our site, it only answers user's questions regarding what an item does.
- - Our AI currently is setup for 250 prompts per day, a limit-reached message will display indicating this limit has been reached. 

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `ufund-api/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `ufund-api/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `ufund-api/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `ufund-api/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `ufund-api/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `docs/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `docs/` directory


## How to setup/run/test program 
1. Tester, first obtain the Acceptance Test plan
2. IP address of target machine running the app

3. Execute `mvn compile exec:java`
4. Open a seperate terminal
5. Go to the ufund-ui directory
6. Execute `ng serve -o`
7. Open in your browser `http://localhost:4200/`

## User Authentication
1. Ufund Manager => 
username: admin, password: 'admin'
2. Create a Helper => 
username: (can be anything), password: (can be anything)
3. Default Helpers => 
username: helper123, password: '123'
username: helper456, password: '456'


## License

MIT License

See LICENSE for details.
