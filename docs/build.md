# Building the project

## WHY
Building consists of compiling source files, compiling test files, executing tests and 
additional steps like static code analysis to find typical bugs (SpotBugs), and you can add anything you want. 
Green build => good code :)

## Let's build some code
Execute the `build` Gradle task:
- go to the `Gradle` tab (upper-right side of the screen) -> `cinema-reservation-system` -> 
        `api` -> `Tasks` -> `build` -> `build`
- alternatively use Gradle wrapper by executing `./gradlew clean build` from directory where your 
        `cinema-reservation-system` project resides in