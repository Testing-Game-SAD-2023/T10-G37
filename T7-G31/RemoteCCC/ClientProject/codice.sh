#!/bin/bash
echo "$(pwd) inizio sh"
mvn clean install
# Controlla il risultato di 'mvn clean install'
if [ $? -ne 0 ]; then
    echo "Errore durante 'mvn clean install'. Uscita con exit code 1."
    exit 1
fi

/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=LINE
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=BRANCH
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=WEAKMUTATION
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=EXCEPTION
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=METHOD
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=OUTPUT
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=CBRANCH
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar evosuite-1.0.6.jar -measureCoverage -class ClientProject.$1 -Djunit=ClientProject.$2 -projectCP /ClientProject/target/classes:/ClientProject/target/test-classes -Dcriterion=METHODNOEXCEPTION








