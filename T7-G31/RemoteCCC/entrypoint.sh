apt update
apt-get -y install software-properties-common
apt-add-repository -y universe
apt-get -y update
apt-get -y install maven git unzip
apt-get -y install wget
echo "le versioni di java e javac sono le seguenti, è necessaria la versione 1.8: "
java -version
javac -version
wget https://github.com/EvoSuite/evosuite/releases/download/v1.0.6/evosuite-1.0.6.jar
wget https://github.com/EvoSuite/evosuite/releases/download/v1.0.6/evosuite-standalone-runtime-1.0.6.jar
java -jar evosuite-1.0.6.jar
cp evosuite-1.0.6.jar /ClientProject
cp evosuite-standalone-runtime-1.0.6.jar /ClientProject