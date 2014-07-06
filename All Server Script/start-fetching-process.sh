sudo apt-get install -y php5-cli
echo "now creating screen i.e. php"
screen -dmS php
screen -S php -p 0 -X stuff "sudo php phpFetcher.php"
echo "done"
echo "now again creating screen i.e. java"
screen -S java -p 0 -X stuff "java -jar /home/ubuntu/fetchingManager.jar"
echo "done"

 
