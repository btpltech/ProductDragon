#EC2_KEYDIR=`dirname "$EC2_PRIVATE_KEY"`
#echo $EC2_KEYDIR
#KEY_NAME=hadoop
#PRIVATE_KEY_PATH=`echo "$EC2_KEYDIR"/"id_rsa-$KEY_NAME"`
#echo $PRIVATE_KEY_PATH
export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)
SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
#sudo mkdir /home/ubuntu/status

INSTANCE_ID=`ec2-describe-instances | grep $1 | awk '{print $2}'`
ec2-terminate-instances $INSTANCE_ID
