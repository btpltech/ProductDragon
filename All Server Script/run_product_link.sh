#sudo chmod -R 777 /home/ubuntu/.aws
#sudo chmod -R 0400 /home/ubuntu/.aws/id_rsa-hadoop
export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)

SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo $SSH_OPTS
#FILE_NAME=/home/ubuntu/$2_slave_status.txt

#for i in `cat $FILE_NAME`
#do
#echo "check is slave is started"
#while true; do
 # printf "."
  # get private dns
 # SLAVE_HOST=`ec2-describe-instances $i | grep running | awk '{print $5}'`
  #echo $SLAVE_HOST
#sudo rm -f /home/ubuntu/$1-slave_status.txt

#if [ ! -z $SLAVE_HOST ]; then
 #   echo "Started"
    #echo "$SLAVE_HOST"  >>/home/ubuntu/$1-slave_status.txt
  #  break;
  #fi
  #sleep 1
#done
echo "now doing ssh & scp"
# printf "."
#SLAVE=`ec2-describe-instances $j | grep running`
#sudo rm -rf /home/ubuntu/$1-status.txt
#echo "$4 $1">/home/ubuntu/$1_IP.txt
echo "copying hostname txt file"
echo "$5 $1" >>/home/ubuntu/status.txt
scp $SSH_OPTS /home/ubuntu/input/$1 "ubuntu@$5:/home/ubuntu/"
echo "done"
echo "moving hostname text file from input folder to output folder"
sudo mv /home/ubuntu/input/$1 output/
echo "done"
echo "copying jar"
scp $SSH_OPTS /home/ubuntu/SeleniumSlave.jar "ubuntu@$5:/home/ubuntu/"
scp $SSH_OPTS /home/ubuntu/UpdateStatusToDoing.jar "ubuntu@$5:/home/ubuntu/"
scp $SSH_OPTS /home/ubuntu/UpdateStatusToDone.jar "ubuntu@$5:/home/ubuntu/"
#scp $SSH_OPTS /home/ubuntu/SeleniumSlaveThread.jar "ubuntu@$5:/home/ubuntu/"
echo "done"
echo "copying script"
scp $SSH_OPTS /home/ubuntu/selenium_master.txt "ubuntu@$5:/home/ubuntu/"
#scp $SSH_OPTS /home/ubuntu/$1_IP.txt "ubuntu@$4:/home/ubuntu/"
scp $SSH_OPTS /home/ubuntu/get_product_link.sh "ubuntu@$5:/home/ubuntu/"
#scp $SSH_OPTS /home/ubuntu/CallPagination.sh "ubuntu@$4:/home/ubuntu/"
echo "done"
#echo "create dir threadfile"
#ssh $SSH_OPTS "ubuntu@$5" "sudo mkdir /home/ubuntu/threadfile"
#echo "chmod"
#ssh $SSH_OPTS "ubuntu@$5" "sudo chmod -R 777 /home/ubuntu/threadfile"
#echo "create log dir"
#ssh $SSH_OPTS "ubuntu@$5" "sudo mkdir /home/ubuntu/log"
#echo "chmod"
#ssh $SSH_OPTS "ubuntu@$5" "sudo chmod -R 777 /home/ubuntu/log"
#echo "running jar"
#ssh $SSH_OPTS "ubuntu@$5" "sudo chmod 777 SeleniumSlaveThread.jar"
ssh $SSH_OPTS "ubuntu@$5" "sudo bash /home/ubuntu/get_product_link.sh $1 $2 $3 $4 $5"
#ssh $SSH_OPTS "ubuntu@$4" "sudo chmod 777 CallPagination.sh"
#ssh $SSH_OPTS "ubuntu@$4" "sudo sudo apt-get install -y  xvfb firefox"
#ssh $SSH_OPTS "ubuntu@$4"  "sudo apt-get install -y xfonts-100dpi xfonts-75dpi xfonts-scalable xfonts-cyrillic"
#ssh $SSH_OPTS "ubuntu@$4" "sudo apt-get install  -y x11-xserver-utils"
#"
echo "jar running sucessfully"
#done
