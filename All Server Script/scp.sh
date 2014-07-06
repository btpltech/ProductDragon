export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)

SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo $SSH_OPTS

scp $SSH_OPTS /home/ubuntu/$1 "ubuntu@$2:/home/ubuntu/hadoop-ec2-scripts/nutch/version2/"

