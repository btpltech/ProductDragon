#SSH_OPT=S`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)
export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)

FILENAME=$AMPLE_HOME/nutch/version2/selenium_instance_status.txt
SSH_OPTS=`echo -i '/home/ubuntu/.aws/id_rsa-hadoop' -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo "$SSH_OPTS .........."
FILE=$AMPLE_HOME/nutch/version2/selenium_instance.txt
for j in `cat $FILE`
do
 #while true; do
 #echo "polling of server"
 #printf "."
#MASTER=`ec2-describe-instances $i | grep running`
#sleep 1
for i in `cat $FILENAME`
do
#while true; do
 #echo "polling of server"
 printf "."
MASTER=`ec2-describe-instances $j | grep running`
#sleep 1
#done
echo "$MASTER is running successfully"
echo "removing previous imp directory"
ssh $SSH_OPTS "ubuntu@$i" "sudo rm -rf /home/ubuntu/imp"
echo "creating imp directory"
ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/imp"
echo "creating input directory"
ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/input"
echo "creating output directory"
ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/output"
echo "changing mode of input directory"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/input"
echo "changing mode of output directory"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/output"
echo "changing mode"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/imp"
echo "copying .aws files into selenium master"
scp $SSH_OPTS /home/ubuntu/.aws/* "ubuntu@$i:/home/ubuntu/imp"
echo "done"
echo "copying ec2-api-tools script in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/ec2-api-tools.sh "ubuntu@$i:/home/ubuntu/"
echo "done"

echo "copying create_selenium_slaves script in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/create_selenium_slave.sh "ubuntu@$i:/home/ubuntu/"
echo "done"

echo "copying run_product_link script in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/run_product_link.sh "ubuntu@$i:/home/ubuntu/"
echo "done"

echo "copying running selenium script in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/get_product_link.sh "ubuntu@$i:/home/ubuntu/"
echo "done"

echo "copying running seleniumServer jar in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/SeleniumServer.jar "ubuntu@$i:/home/ubuntu/"
echo "done"

echo "copying running seleniumSlave jar in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/SeleniumSlave.jar "ubuntu@$i:/home/ubuntu/"
echo "done"

echo "copying kill-slave script in selenium server"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/selenium_master.txt "ubuntu@$i:/home/ubuntu/"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/killing-selenium-slave.sh "ubuntu@$i:/home/ubuntu/"
echo "done"


echo "running ec2-scripts started"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/ec2-api-tools.sh"
#ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/ec2-api-tools.sh"
echo "done"

echo "running to get the data from dynamo and generate txt file for selenium is started"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/SeleniumSlave.jar"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/SeleniumServer.jar"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/run_product_link.sh"
echo "change mode done "
ssh $SSH_OPTS "ubuntu@$i" "sudo java -jar /home/ubuntu/SeleniumServer.jar"
#ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/run_product_link.sh"
echo "done"
done
done
