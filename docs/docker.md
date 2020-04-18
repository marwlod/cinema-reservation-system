# Guide to using Docker and Docker Compose

## WHY

We have Docker Compose starting MySQL database, so we all have the same database with the same version, same
content. It would be hard to maintain it when we would all have our own local databases, without synchronization.


## Start MySQL

To start docker containers (currently only MySQL) from terminal, execute from `cinema-reservation-system` directory:
```shell script
./gradlew :composeUp
```
OR click the task in `Gradle` tab on the right side of the screen -> `cinema-reservation-system` -> `Tasks` -> `docker` -> `composeUp`.


## Stop MySQL

To stop the docker containers, execute from `cinema-reservation-system` directory:
```shell script
./gradlew :composeDown
```
OR click the task in `Gradle` tab on the right side of the screen -> `cinema-reservation-system` -> `Tasks` -> `docker` -> `composeDown`.


## Useful commands

1. Don't know if the database is running or not? List all Docker containers currently running:
    ```shell script
    docker ps
    ```
1. Maybe it stopped for some reason or never started? List all Docker containers, including stopped ones:
    ```shell script
    docker ps -a
    ```
1. Show me some logs (copy ID from output from one of previous commands):
    ```shell script
    docker logs <CONTAINER ID>
    ```


## Troubleshooting

1. `composeUp` fails -> Sometimes the `composeDown` task doesn't fully stop all the containers or leaves some garbage behind. 
    Try executing in the terminal:
    ```shell script
    docker rm -f $(docker ps -aq)
    ```
1. Very weird errors -> Might be useful to delete all Docker images:
    ```shell script
    docker rmi -f $(docker images -q)
    ```