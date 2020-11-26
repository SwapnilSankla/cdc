****CDC****

**Sample ecosystem**
![Alt text](doc/image/cdc.jpg?raw=true "Ecosystem")


This could be the simplest loan granting system. However the focus is on consumer driven contract tests and hence kept the business usecase fairly simple. The ecosystem works as below.
1. User applies for the loan by calling gateway API.
2. Gateway API fetches fraud status over Http.
3. Once fraud status is received then it emits loan creation event.
4. Loan fulfilment service is expected to listen to the event and does further processing.

The repository contains contract tests between
1. Loan gateway and Fraud service
2. Loan gateway and Loan Fulfilment service

![Alt text](doc/image/pacts.png?raw=true "Sample pacts")
