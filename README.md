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

- For the application to run the funding_basket.json should have at least the following data: 
{"users": [{"userName":"helper123","basket":[]}]}

- For the application to run the needs.json should have at least the following data: 
[]

2. Bugs & Disclaimers:  The following are features that are currently bugged, and planned to be resolved in Sprint 3: 
- Needs still remain in funding basket even after their corresponding need is removed from the needs cupboard.
- For users, there is no set password for them. The program will accept any value. This is planned to be updated in Sprint 3.
- In the admin page, if the data fields are empty when updating a need, the respective fields will be empty. The same goes for adding a need, the only requirement for adding is the need's name. (to update a need you would have to enter all the fields even if you only want to update one field.)
- Regarding quantity and surplus, our cupboard currently shows the manager's needs that are remaining, counting down for every user that supports it. Then in surplus, the visual changes to "Surplus" instead of showing the negative values. We plan to change this functionality to display, for example 8/10 when not in surplus so that later on when the requested needs increas, say from 10-20, the application will display 13/20 where the previous surplus was 13/10.

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
7. Open in your browser `http://localhost:8080/`

## User Authentication
1. Ufund Manager => 
username: admin, password: (can be anything)
2. Helper => 
username: (can be anything), password: (can be anything)
3. Default Helpers => 
username: helper123, password: (can be anything)
username: helper456, password: (can be anything)


## License

MIT License

See LICENSE for details.
