echo "script for  creating instances"

#FILE="/home/ubuntu/server.txt"

#declare -x INSTANCE
#export AWS_CREDENTIAL_FILE=/home/ubuntu/.aws/aws-credential-file.txt
export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)
#export AWS_CREDENTIAL_FILE=/home/ubuntu/.aws/aws-credential-file.txt
ARCH='x86_64'
AMI_IMAGE='ami-b3044fda'
AMI_KERNEL='aki-88aa75e1'
#EC2_KEYDIR=`dirname "$EC2_PRIVATE_KEY"`
echo $EC2_KEYDIR
KEY_NAME=hadoop
#PRIVATE_KEY_PATH=`echo "$EC2_KEYDIR"/"id_rsa-$KEY_NAME"`
INSTANCE_TYPE=m1.small
if [ "$AMI_KERNEL" != "" ]; then
  KERNEL_ARG="--kernel ${AMI_KERNEL}"
fi
#sudo rm -rf /home/ubuntu/slave.txt
#sudo rm -rf /home/ubuntu/slave_status.txt
#declare -i no
#no=$4
echo "creating instances "
SLAVE=`ec2-run-instances $AMI_IMAGE -n 1 -g "fetcher" -k "$KEY_NAME" -t "$INSTANCE_TYPE" $KERNEL_ARG | grep INSTANCE | awk '{print $2}'`
#>> /home/ubuntu/$2_slave.txt
echo "Done"
echo "now_polling of slaves started"
#FILE=/home/ubuntu/$2_slave.txt
#for i in `cat $FILE`
#do
while true; do
   echo "polling of slaves please wait"
  printf "."
  # get private dns
  SLAVE_HOST=`ec2-describe-instances $SLAVE | grep running | awk '{print $4}'`
  echo $SLAVE_HOST
#sudo rm -f $AMPLE_HOME/nutch/slave_status.txt
sleep 60
if [ ! -z $SLAVE_HOST ]; then
    echo "Started"
    echo "$SLAVE_HOST"  
 #>>/home/ubuntu/$2_slave_status.txt
    echo "run_product_link.sh is running"
    sudo bash /home/ubuntu/run_product_link.sh $1 $2 $3 $4 $SLAVE_HOST
    echo "Done"
    break;
  fi
  sleep 3
done
#done
#echo "/home/ubuntu/$2_slave_status.txt" >>/home/ubuntu/slave_status.txt
echo "$1"

#FILE_NAME=/home/ubuntu/$1.txt
#for j in `cat $FILE_NAME`
#do
#while true; do
  # echo "checking slave is running or not "
 # printf "."
 #SLAVE=`ec2-describe-instances $j| grep running | awk '{print $5}'`
 # echo $SLAVE_HOST
#sudo rm -f $AMPLE_HOME/nutch/slave_status.txt

#if [ ! -z $SLAVE ]; then
    #echo "Started"
   # echo "$SLAVE_HOST"  >>/home/ubuntu/slave_status.txt
   # break;
  #fi
 # sleep 2
#done
#done
#echo "run_product_link.sh is running"
#sudo bash /home/ubuntu/run_product_link.sh $j $1 $2
#echo "Done"
#done
#SLAVE_HOST=`ec2-describe-instances $i| grep running | awk '{print $5}'`
#File=$AMPLE_HOME/nutch/version2/selenium_instance_status.txt
#SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
#for i in `cat $File` 
#do
#echo "changing mode of status.txt file">>$AMPLE_HOME/nutch/version2/file.txt
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/status.txt"
#echo "changing mode of run_product_link.sh">>$AMPLE_HOME/nutch/version2/file.txt
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /run_product_link.sh"
#echo "running run_product_link.sh script">>$AMPLE_HOME/nutch/version2/file.txt
#ssh $SSH_OPTS "ubuntu@$i" "sudo bash ./run_product_link.sh $2"
#done
