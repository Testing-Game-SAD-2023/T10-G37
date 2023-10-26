#!/bin/bash
echo "$(pwd) inizio sh"
mvn clean install
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=LINE
