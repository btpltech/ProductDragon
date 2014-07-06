EC2_KEYDIR=`dirname "$EC2_PRIVATE_KEY"`
echo $EC2_KEYDIR
KEY_NAME=hadoop
PRIVATE_KEY_PATH=`echo "$EC2_KEYDIR"/"id_rsa-$KEY_NAME"`
echo $PRIVATE_KEY_PATH
SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
#sudo mkdir /home/ubuntu/status
ssh $SSH_OPTS "ubuntu@$1" "sudo /usr/local/hadoop-$HADOOP_VERSION/bin/hadoop-daemon.sh stop datanode"
ssh $SSH_OPTS "ubuntu@$1" "sudo /usr/local/hadoop-$HADOOP_VERSION/bin/hadoop-daemon.sh stop tasktracker"
INSTANCE_ID=`ec2-describe-instances | grep $1 | awk '{print $2}'`
ec2-terminate-instances $INSTANCE_ID
