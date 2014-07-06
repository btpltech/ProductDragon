export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
echo "launching fetching slaves"
#SSH_OPTS=`echo -i "/home/ubuntu/.aws/id_rsa-hadoop" -o StrictHostKeyChecking=no -o ServerAliveInterval=30`
$AMPLE_HOME/nutch/version2/launch-hadoop-slaves fetching $1
File=$AMPLE_HOME/nutch/version2/fetching-master.txt
for i in `cat $File`
do
echo "copy slave status file into master"
scp $SSH_OPTS $AMPLE_HOME/nutch/version2/fetching-slave_status.txt "ubuntu@$i:/home/ubuntu"
#echo "change mode of launch_nutch.sh"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/launch_nutch.sh"
#echo "change mode of copy-region-server"
#ssh $SSH_OPTS "ubuntu@$i" "sudo chmod 777 /home/ubuntu/copy-region-server.sh"
#echo "run copy-regionserver.sh script"
#ssh $SSH_OPTS "ubuntu@$i" "sudo /home/ubuntu/copy-region-server.sh"
#echo "done"
#echo "run launch_nutch.sh script"
#ssh $SSH_OPTS "ubuntu@$i" "sudo /home/ubuntu/launch_nutch.sh"
#echo "done"
done

echo "all slaves of group fetching are started.............."
