# Cinema reservation system


## Overview
Cinema reservation system (CRS) was designed and programmed from scratch as part of the Object-oriented Technology AGH UST course.
It's main purpose is to make the process of reservation/booking of cinema seats or entire halls as seamless as possible. 
Prior to using it, CRS requires registration. 

There are two types of users: 
- regular user - can browse through available movies and halls,
make a reservation for a seat for chosen movie, book an entire hall at least two weeks in advance 
(provided that he pays an advance payment).
- admin - can browse through available movies and halls, can add new movies and new screenings,
can browse through aggregated statistics regarding past reservations.

CRS consists of two separate applications: 
- [backend](api) (REST API)
- [frontend](front).

CRS requires an integration with external payments' system. To avoid integrating with the real system at this stage,
the [payments' mock](payments-mock) was created.


## Run and test manually
(Docker and npm required)

1. Run database from Docker image:
    ```shell script
    ./gradlew composeUp
    ```
1. Run backend API:
    ```shell script
    ./gradlew :api:bootRun
    ```
1. Run payments' mock:
    ```shell script
    ./gradlew :payments-mock:bootRun
    ```
1. Run frontend:
    ```shell script
    cd front
    npm start
    ```
1. Go to http://localhost:3000 in your favourite web browser.
1. Either
    - login as admin: `admin@crs.com` with password: `test` or
    - login as regular client: `bob@crs.com` with password `test` or
    - create your own regular client account.
1. Have fun.


## Tech stack
Backend:
- Java 11
- Spring (Boot, Core, Data)
- JUnit
- MySQL
- Gradle
- Docker

Frontend:
- JavaScript
- React
- HTML 5
- CSS 3


## Docs
1. [Developer's guide](docs/dev_guide.md)
1. [Building](docs/build.md)
1. [Docker/MySQL](docs/docker.md)
1. [Architecture](docs/architecture.md)


## Known issues
[Issues](https://github.com/marwlod/cinema-reservation-system/issues)