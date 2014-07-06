#!/usr/bin/env bash
#if [ $# = 0 ]; then
#  echo "Usage: copy_nutch_master STORE_HOST(without http://) THREADS LEVEL TOPN MASTER_HOST"
#  exit 1
#fi

#Adding the path variables
export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)
export AWS_CREDENTIAL_FILE=/home/ubuntu/.aws/aws-credential-file.txt
export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
export HBASE_VERSION=0.94.11
#JAVA_VERSION=1.7.0_17
export HADOOP_VERSION=1.2.1

FILENAME=/home/ubuntu/fetching-master.txt
declare -x MASTER
for i in `cat $FILENAME`
do
MASTER=$i
done

#bash /home/ubuntu/hadoop-ec2-scripts/ample-env.sh
#export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
#echo "inside copy script"> $AMPLE_HOME/logs/copy_nutch.txt
#echo $AMPLE_HOME > $AMPLE_HOME/logs/ample_home.txt
#Setting up the SSH_OPTS

EC2_KEYDIR=`dirname "$EC2_PRIVATE_KEY"`
echo $EC2_KEYDIR
KEY_NAME=hadoop
PRIVATE_KEY_PATH=`echo "$EC2_KEYDIR"/"id_rsa-$KEY_NAME"`
echo $PRIVATE_KEY_PATH

# SSH options used when connecting to EC2 instances.
export SSH_OPTS=`echo -i "$PRIVATE_KEY_PATH" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo $SSH_OPTS

#echo "$SSH_OPTS ssh parameter $MASTER"> $AMPLE_HOME/logs/ample_home1.txt

#Checking ssh logging
#ssh $SSH_OPTS "ubuntu@$5" "mkdir cool"

echo "running hbase script......................"

#copy slaves ip to region server
sudo /home/ubuntu/copy-region-server.sh

#echo "running thrift server"
#$AMPLE_HOME/nutch/launch_thrift_server.sh $MASTER

#echo "Printing the home $AMPLE_HOME   $SSH_OPTS"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version2/fetching_slave_status.txt "ubuntu@$MASTER:/home/ubuntu/"
echo "copying slaves ip into regionservers file"
ssh $SSH_OPTS "ubuntu@$MASTER" "sudo cp /home/ubuntu/fetching_slave_status.txt /usr/local/hbase-$HBASE_VERSION/conf/regionservers"
echo "running nutch script...................."

#sudo rm -rf $AMPLE_HOME/nutch/crawl_status.txt
#echo "crawling started" >> $AMPLE_HOME/nutch/crawl_status.txt
#scp $SSH_OPTS $AMPLE_HOME/nutch/version2/launch_nutch.sh "ubuntu@$MASTER:/home/ubuntu/"
ssh $SSH_OPTS "ubuntu@$MASTER" "sudo /home/ubuntu/launch_nutch.sh"
#echo "crawling ended" >> $AMPLE_HOME/nutch/crawl_status.txt
