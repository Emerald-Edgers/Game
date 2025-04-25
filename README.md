# Module Based Game

## About

This project aims to deliver a module based game, which is implemented using [libGDX](https://libgdx.com/).

### Authors
* Magnus Ahrens Thomsen (magth23@student.sdu.dk)
* Oliver Ahrens (olahr23@student.sdu.dk)
* Victor Bøgesvang Pedersen (vpede23@student.sdu.dk)
* Victor Kahl Petersen (vipet23@student.sdu.dk)

## Installing

### Prerequisites

Before building the game, you'll have to first clone the source code:

```sh
git clone https://github.com/Emerald-Edgers/Game.git
cd Game
```

Before building the game ensure you have the following installed:

* [Maven](https://maven.apache.org/download.cgi) (>= 3.9.0)
* [Java](https://adoptium.net/) (>= 21)


### Building
Build the project by running `mvn clean install` from the project root.


### Running
Run the project with `java -cp "mods-mvn/*" dk.ee.zg.Game` From the project root.
Alternatively use the provided script: [run.sh](run.sh)

### Development
For development purposes use the script: [install-run.sh](install-run.sh)

The script executes the following commands:

```sh
mvn clean install
java -cp "mods-mvn/*" dk.ee.zg.Game
```

#### Intellij IDEA
If using Intellij IDEA it is possible to setup a [Run Configuration](https://www.jetbrains.com/help/idea/run-debug-configuration.html) which automatically runs a shell script.
For convenience this project provides a preconfigured configuration at [.intellij-game-run](.intellij-game-run).

### Defining interface implementations
If a module implements an interface from another module it should be defined under META-INF/services/_path-to-interface-from-module-root_
Inside the file, define which class implements said interface in the same manner.

## Testing

### Running unit \& Integration tests with Maven
To run all unit tests in the project using Maven:
```sh
mvn test
```

### Module Combination Testing with Python
This project includes a Python-based testing tool that automatically runs various combinations of game modules to detect potential runtime issues.

#### Requirements
Make sure [Python 3.10+ is installed](https://www.python.org/downloads/). Then install the required Python packages using:
```sh
pip install -r requirements.txt
```

#### Running the Tester
> [!WARNING]
> This script will take control of you machine, and execute keyboard inputs. Close and save anything important before using.

From the root of the project, run the script:
```sh
python3 module_tester.py
```
This script will:
* Attempt to launch the game with different combinations of modules.
* Automatically press ENTER to bypass the start screen.
* Automatically hold LEFT \& UP to move the player.
* Log success or crash output for each test run into a timestamped log file located in [mods-log/](mods-log/).

By default, it starts testing from a standard amount of modules and up. You can modify the start point or behavior in the tester.py file.

### Generating Dependcy graphs
```sh
mvn depgraph:graph -DoutputDirectory=target/dependency-graphs
``` 
using this command can build .cdot dependency graphs for all modules
e.g
![img.png](img.png)
