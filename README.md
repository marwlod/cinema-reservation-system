# Cinema reservation system

## Setting up developer workstation

1. [Configure](https://help.github.com/en/github/authenticating-to-github/adding-a-new-ssh-key-to-your-github-account) 
    GitHub SSH keys.
1. Download [Java SE 11](https://jdk.java.net/java-se-ri/11)  
1. Download [IntelliJ IDEA](https://www.jetbrains.com/idea/download) 
    (Ultimate 2020.1 or newer would be best, AGH students have a [free student license](https://accounts.ki.agh.edu.pl/licenses/)).
1. Inside IntelliJ: `File` -> `New` -> `Project from Version Control...` -> select Git as version control system and
    copy `git@github.com:marwlod/cinema-reservation-system.git` to `URL` field, then click `Clone` 
    (your GitHub SSH keys need to be configured for this to work).
1. Configure Java in IntelliJ:
    - navigate to `File` -> `Project Structure` -> `Project` tab on the left
    - `Project SDK` field should point to your Java 11 SDK, `Project language level` should be 11,
        `Project compiler output` should be similar to `/your/path/to/project/cinema-reservation-system/out`
1. Install [Docker](https://docs.docker.com/engine/install/). 
1. Go to `Gradle` tab on the right side of the screen, click refresh button and all dependencies should be downloaded.
1. [Start](docs/docker.md) the database from Docker Compose.
1. Setup the database connection (if you already have MySQL installed locally on your computer, there might be issues, 
    you don't need to have MySQL installed, that's what Docker is for):
    - go to the `Database` tab on the right side of the screen, click `+` button -> `Datasource` -> `MySQL`
    - enter in URL field `jdbc:mysql://localhost:3306/api` and username and password from [here](api/src/test/resources/db/db-init.sql)


## Testing the setup:

1. [Build](docs/build.md) the project.
1. Go to [MainApplication.java](/api/src/main/java/io/github/kkw/api/MainApplication.java) and run it.


## Teamwork

1. Project management (creating new tasks, reporting bugs) will be done in 
    [Jira](https://marwlod.atlassian.net/secure/RapidBoard.jspa?rapidView=1&projectKey=CRS).
1. Google Docs for specifying system specification, architecture etc. is 
    [here](https://docs.google.com/document/d/1BDx4CPwI4I0IPwDOA703z2xR4x-2jga33cw82GyiGe8/edit).
1. Don't commit to `master` branch please. Every change to the `master` branch should be proposed by creating a 
    [Pull Request](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request).
    Before creating a Pull Request make sure the [build](docs/build.md) is passing.
    We don't have any automatic build system yet so it needs to be done manually :(


## Educational resources

1. [Git](https://www.freecodecamp.org/news/learn-the-basics-of-git-in-under-10-minutes-da548267cc91/)
    Everything can be done in GUI (`VCS` -> `Git`) or with keyboard shortcuts inside IntelliJ. 
    Most important things to master related to version control:
    - creating a new branch
    - committing
    - pushing
    - creating a Pull Request
    - merging (usually with `master` branch, working from your branch)
    - add/rollback file
    - optional but useful - shelving/unshelving changes, showing history for a file
1. [Markdown](https://guides.github.com/features/mastering-markdown/) 
    for creating documentation like this, very easy.
1. [Gradle](https://docs.gradle.org/current/userguide/getting_started.html) - the most important thing is to be able 
    to add new dependencies, e.g. if you want to add a dependency for [api](api) module, add a line to 
    [api/build.gradle](api/build.gradle). 
    Find the right dependencies with specific versions [here](https://mvnrepository.com/).
1. Spring - [tutorials](https://www.baeldung.com/spring-tutorial),
    [official guides](https://spring.io/guides),
    [docs](https://docs.spring.io/spring/docs/current/spring-framework-reference/index.html).
1. [Keyboard shortcuts](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf) 
    for IntelliJ (very handy, but not necessary).
1. [Docker](https://docs.docker.com/compose/gettingstarted/) - you don't need to know much about it. There are a few commands
    that are useful when something goes wrong, I wrote them [here](docs/docker.md).