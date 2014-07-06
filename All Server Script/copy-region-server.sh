HBASE_VERSION=0.94.11
#starting daemon on master
FILE=/home/ubuntu/$1-master.txt
for i in `cat $FILE`
do
echo "starting zookeeper on master"
echo "$i" >/usr/local/hbase-0.94.11/conf/regionservers
sudo /usr/local/hbase-$HBASE_VERSION/bin/hbase-daemon.sh start zookeeper
echo "zookeeper started successfully"
echo "starting master on $i"
sudo /usr/local/hbase-$HBASE_VERSION/bin/hbase-daemon.sh start master
echo "master started successfully"
echo "hbase daemon started on master server successfully done...."
done
#starting daemon on slaves
#FILENAME=$AMPLE_HOME/nutch/version2/fetching-slave_status.txt
#for i in `cat $FILENAME`
#do
#ssh $SSH_OPTS "ubuntu@$i" "sudo cp /home/ubuntu/fetching-slave_status.txt /usr/local/hbase-0.94.11/conf/regionservers"
#echo "starting regionserver on slave $i"
#ssh $SSH_OPTS "ubuntu@$i" "sudo /usr/local/hbase-$HBASE_VERSION/bin/hbase-daemon.sh start regionserver"
#echo "region server started successfully"
#done
#echo "hbase daemon started on slaves server successfully done ..............."

echo  "hbase is ready ...................."

