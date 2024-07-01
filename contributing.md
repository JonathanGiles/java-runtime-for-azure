# Contributing

Firstly - thanks for thinking about contributing!

## Code Quality

The build has three tools that are used to ensure code quality:

1. [Checkstyle](https://checkstyle.org/) - this is used to ensure that the code adheres to a consistent style.
2. [PMD](https://pmd.github.io/) - this is used to find common programming flaws.
3. [SpotBugs](https://spotbugs.github.io/) - this is used to find common bugs in Java code.

Before submitting a pull request, please ensure that the code passes all of these checks. You can run them locally by executing `mvn clean verify`.