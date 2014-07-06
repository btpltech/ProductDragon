export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
export HOME=/home/ubuntu
SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo "launching fetching master"
$AMPLE_HOME/nutch/version2/launch-hadoop-master fetching

FILE_NAME=$AMPLE_HOME/nutch/version2/fetching-master.txt
for i in `cat $FILE_NAME`
do
#echo "copying start_nutch.sh script"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version2/start_nutch.sh "ubuntu@$i:/home/ubuntu" 
#echo "copying regionserver.sh script"
#scp $SSH_OPTS $AMPLE_HOME/nutch/version2/copy-region-server.sh "ubuntu@$i:/home/ubuntu"

echo "making imp folder into master"
ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/imp"
echo "done"

echo "change mode of imp folder into master"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/imp"
echo "done"
echo "copying .aws folder to master"
scp $SSH_OPTS $HOME/.aws/* "ubuntu@$i:/home/ubuntu/imp"
echo "done"

echo "copying fetching-master.txt into master"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/fetching-master.txt "ubuntu@$i:/home/ubuntu"
echo "done"
echo "copying launch_nutch script into master"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/launch_nutch.sh "ubuntu@$i:/home/ubuntu"
echo "done"
echo "copying jars files"

scp $SSH_OPTS $HOME/.hadooop-fetching-master  "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $HOME/.hadooop-zone-fetching-master  "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $HOME/.hadooop-private-fetching-master  "ubuntu@$i:/home/ubuntu"

scp $SSH_OPTS $AMPLE_HOME/nutch/version2/fetcher.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/phpFetcher.php "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/fetcher.jar "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/parser.jar "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/mapper.jar "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/fetchingManager.jar "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/ec2-api-tools.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/start-fetching-slave.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/launch-hadoop-slaves "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/hadoop-ec2-env.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/hadoop-ec2-init-remote.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/start-fetching-process.sh "ubuntu@$i:/home/ubuntu"
echo "Setting up the amazon ec2 api tools"
ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/ec2-api-tools.sh"
echo "start nutch into master"
ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/launch_nutch.sh"
echo "done"
echo "start fetching process"
ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/start-fetching-process.sh"
echo "done"
done
