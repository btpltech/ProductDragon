export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
export HOME=/home/ubuntu

echo "launching Grouping-master"
$AMPLE_HOME/nutch/version2/launch-hadoop-master grouping
echo "done"
FILE_NAME=$AMPLE_HOME/nutch/version2/grouping-master.txt

for i in `cat $FILE_NAME`
do
echo "copying grouping-master.txt"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/grouping-master.txt "ubuntu@$i:/home/ubuntu"
echo "copying .aws to group master"
ssh $SSH_OPTS "ubuntu@$i" "sudo mkdir /home/ubuntu/imp"
ssh $SSH_OPTS "ubuntu@$i" "sudo chmod -R 777 /home/ubuntu/imp"
scp $SSH_OPTS $HOME/.aws/* "ubuntu@$i:/home/ubuntu/imp"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/launch-hadoop-slaves "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/hadoop-ec2-init-remote.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/hadoop-ec2-env.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $HOME/.hadooop-grouping-master "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $HOME/.hadooop-private-grouping-master "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $HOME/.hadooop-zone-grouping-master "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/ec2-api-tools.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/groupingManager.jar "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/jars/grouping.jar "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/phpGrouping.sh "ubuntu@$i:/home/ubuntu"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/grouping.sh "ubuntu@$i:/home/ubuntu"
ssh $SSH_OPTS "ubuntu@$i" "sudo bash /home/ubuntu/ec2-api-tools.sh"
done

echo "grouping master are started.............."
