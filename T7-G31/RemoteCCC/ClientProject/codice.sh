#!/bin/bash
echo "$pwd inizio sh"
mvn clean compile
java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=LINE
