echo "script for running instances"
export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
#SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)
#export AWS_CREDENTIAL_FILE=/home/ubuntu/.aws/aws-credential-file.txt
ARCH='x86_64'
AMI_IMAGE='ami-b3044fda'
AMI_KERNEL='aki-88aa75e1'
KEY_NAME=hadoop
INSTANCE_TYPE=m1.medium
if [ "$AMI_KERNEL" != "" ]; then
  KERNEL_ARG="--kernel ${AMI_KERNEL}"
fi
AWS_ACCOUNT_ID=8829-7477-1649
#ec2-describe-group | egrep "[[:space:]]$2[[:space:]]" > /dev/null
#if [ ! $? -eq 0 ]; then
 # echo "Creating group $2"
  #ec2-add-group $2 -d "Group for Hadoop Master."
  #ec2-authorize $2 -o $2 -u $AWS_ACCOUNT_ID
  #ec2-authorize $2 -p 22    # ssh
#fi
#sudo rm -rf $AMPLE_HOME/nutch/version2/selenium.txt
#sudo rm -rf $AMPLE_HOME/nutch/version2/selenium.txt
echo "creating selenium master server"
#  ec2-add-group $1 -d "Group for Hadoop Master."
 # ec2-authorize $1 -o $1 -u $AWS_ACCOUNT_ID
  #ec2-authorize $1 -p 22    # ssh
   #ec2-authorize $1 -p 60000
#ec2-authorize $1 -p 60010
#ec2-authorize $1 -p 60020
#ec2-authorize $1 -p 60030
#ec2-authorize $1 -p 2181

ec2-run-instances $AMI_IMAGE -n "$1" -g "$2" -k "$KEY_NAME" -t "$INSTANCE_TYPE" $KERNEL_ARG | grep INSTANCE | awk '{print $2}'>> $AMPLE_HOME/nutch/version2/$1.txt
echo "Done"

FILE=$AMPLE_HOME/nutch/version2/$1.txt

for i in `cat $FILE`
do
 while true; do
 echo "polling of server"
 printf "."
 #get private dns
SLAVE_HOST=`ec2-describe-instances $i| grep running | awk '{print $4}'`
sudo rm -f $AMPLE_HOME/nutch/version2/$1_master.txt

if [ ! -z $SLAVE_HOST ]; then
   echo "Started"
  echo "$SLAVE_HOST"  >>$AMPLE_HOME/nutch/version2/$1_master.txt
 break;
fi
sleep 1
done
#bash $AMPLE_HOME/nutch/version2/copy_all_into_selenium_master.sh
done
#sudo ./copy_all_into_selenium_master.sh

#$AMPLE_HOME/nutch/version1/ample/status/copy_all_into_selenium_master.sh status.txt
#SSH_OPT=S`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
#export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
#export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)

#FILENAME=$AMPLE_HOME/nutch/version1/ample/status/selenium_instance_status.txt
#SSH_OPTS=`echo -i '/home/ubuntu/.aws/id_rsa-hadoop' -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
#echo "$SSH_OPTS .........."
#for i in `cat $FILENAME`
#do
#echo "removing previous imp directory"
#ssh $SSH_OPTS "ubuntu@$i" "sudo rm -rf /home/ubuntu/imp"
#echo "creating imp directory"
#ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/imp"
#echo "creating input directory"
#ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/input"
#echo "creating output directory"
#ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/output"
#echo "changing mode of input directory"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/input"
#echo "changing mode of output directory"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/output"
#echo "changing mode"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/imp"
#echo "copying .aws files into selenium master"
#scp $SSH_OPTS /home/ubuntu/.aws/* "ubuntu@$i:/home/ubuntu/imp"
#echo "done"
#echo "copying ec2-api-tools script in selenium server"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version1/ample/status/ec2-api-tools.sh "ubuntu@$i:/home/ubuntu/"
#echo "done"

#echo "copying create_selenium_slaves script in selenium server"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version1/ample/status/create_selenium_slave.sh "ubuntu@$i:/home/ubuntu/"
#echo "done"

#echo "copying run_product_link script in selenium server"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version1/ample/status/run_product_link.sh "ubuntu@$i:/home/ubuntu/"
#echo "done"

#echo "copying running selenium script in selenium server"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version1/ample/status/get_product_link.sh "ubuntu@$i:/home/ubuntu/"
#echo "done"

#echo "copying running seleniumServer jar in selenium server"
#scp $SSH_OPTS $AMPLE_HOME/nutch/jars/SeleniumServer.jar "ubuntu@$i:/home/ubuntu/"
#echo "done"

#echo "copying running seleniumSlave jar in selenium server"
#scp $SSH_OPTS $AMPLE_HOME/nutch/jars/SeleniumSlave.jar "ubuntu@$i:/home/ubuntu/"
#echo "done"

#echo "running ec2-scripts started"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/ec2-api-tools.sh"
#ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/ec2-api-tools.sh"
#echo "done"

#echo "running to get the data from dynamo and generate txt file for selenium is started"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/SeleniumSlave.jar"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/SeleniumServer.jar"
#ssh $SSH_OPTS "ubuntu@$i" "sudo java -jar /home/ubuntu/SeleniumServer.jar $1"
#echo "done"

#echo "running script for running script"
#ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/run_product_link.sh"
#done

