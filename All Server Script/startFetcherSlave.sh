echo "startFetchingSlave.sh start......."
#sudo chmod -R 777 /home/ubuntu/.aws
#sudo chmod -R 0400 /home/ubuntu/.aws/id_rsa-hadoop
#export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
#export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)

SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo $SSH_OPTS
 
echo "copying text file into server"
scp $SSH_OPTS $2 "ubuntu@$1:/home/ubuntu/"
scp $SSH_OPTS /home/ubuntu/.aws/* "ubuntu@$1:/home/ubuntu/.aws/"
ssh $SSH_OPTS "ubuntu@$1" "sudo chmod -R 777 /home/ubuntu/.aws"
ssh $ssh_OPTS "ubuntu@$1" "sudo chmod 0400 /home/ubuntu/.aws/id_rsa-hadoop"
#scp $SSH_OPTS /home/ubuntu/.aws/* "ubuntu@$1:/home/ubuntu/.aws/"
echo "done"

echo "copying jar into server"
scp $SSH_OPTS /home/ubuntu/FetchingInSlave.jar "ubuntu@$1:/home/ubuntu/"
echo "done"

#if you want to run jar from this script than
echo "running of jar is start now"
ssh $SSH_OPTS "ubuntu@$1" "sudo java -jar /home/ubuntu/FetchingInSlave.jar $1 $2 $3 $4"
echo "done"
