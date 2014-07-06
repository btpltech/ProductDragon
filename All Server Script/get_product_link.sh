sudo chmod -R 777 /home/ubuntu/.aws
sudo chmod -R 0400 /home/ubuntu/.aws/id_rsa-hadoop

export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)

SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo $SSH_OPTS

sudo apt-get install -y  xvfb firefox
sudo apt-get install -y xfonts-100dpi xfonts-75dpi xfonts-scalable xfonts-cyrillic
sudo apt-get install  -y x11-xserver-utils
Xvfb :99 -ac 2>/dev/null &
#screen -S fetch -p 0 -X stuff "exit"
#screen -dmS fetch
#screen -S fetch -p 0 -X stuff "export DISPLAY=:99 $(printf \\r)"
export DISPLAY=:99
#source /home/ubuntu/.bashrc
#screen -S fetch -p 0 -X stuff "java -jar /home/ubuntu/get_product_link.jar $1 $2 $(printf \\r)"
echo "Doing status in storelink table"
sudo java -jar UpdateStatusToDoing.jar $1 $5
echo "running seleniumslave jar"
sudo java -jar /home/ubuntu/SeleniumSlave.jar $1 $2 $3 $4 $5
echo "Done status in Slave table"
sudo java -jar /home/ubuntu/UpdateStatusToDone.jar $1 $5
#export DISPLAY=:99
file=/home/ubuntu/selenium_master.txt
for i in `cat $file`
do
#echo /home/ubuntu/IP.txt >/home/ubuntu/$2_IP.txt
#scp $SSH_OPTS /home/ubuntu/$1_IP.txt "ubuntu@$i:/home/ubuntu/single_slave/"
scp $SSH_OPTS /home/ubuntu/$1.log "ubuntu@$i:/home/ubuntu/log/"
#sudo java -jar /home/ubuntu/UpdateStatus.jar $1 $2 $5
#scp $SSH_OPTS /home/ubuntu/log/* "ubuntu@$i:/home/ubuntu/thread_log/"
#scp $SSH_OPTS /home/ubuntu/threadfile/* "ubuntu@$i:/home/ubuntu/thread_file/"
done
 
