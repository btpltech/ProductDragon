HADOOP_VERSION=1.2.1
HOME=/home/ubuntu
FILE=$HOME/fetching-master.txt
for i in `cat $FILE`
do
IP=$i
done
#echo "create input folder"
#sudo mkdir /mnt/input
#echo "changing mode"
#sudo chmod -R 777 /mnt/input
STATUS=$1
NEW_STATUS=$2
echo "Initializing bool variable"
bool=true
while $bool;
do
#STATUS="notDone"
#NEW_STATUS="done"
echo "Getting data from S3 Started Now..."
echo $IP
echo $HOME
echo "Removing previous input directory in local server system"
sudo rm -rf /mnt/input
echo "Making new input directory"
sudo mkdir /mnt/input
echo "changing mode of input file"
sudo chmod -R 777 /mnt/input
echo "getting data from S3 bucket"
sudo java -jar $HOME/FetchingContentDownload.jar /mnt/input $STATUS $NEW_STATUS
echo "done"
echo $IP
echo "Now getting data from dynamo from table entity_info"
sudo java -jar $HOME/GetDataBeforeParsing.jar $IP
echo "moving data for input "
#sudo mv /mnt/input/* /mnt/output/
if [ $bool=="true" ]; then
cd /usr/local/hadoop-$HADOOP_VERSION
bool=false
echo "removing input directory from hdfs"
sudo bin/hadoop dfs -rmr /input
echo "making input directory into hdfs"
sudo bin/hadoop dfs -mkdir /input
echo "putting data from local file system to hdfs"
sudo bin/hadoop dfs -put /mnt/input/* /input
echo "Parsing started"
sudo bin/hadoop jar $HOME/Parsing.jar /input $IP
echo "done"
sudo bin/hadoop jar $HOME/Mapping.jar $IP
bool=true
 fi
done
#done
#sudo bin/hadoop jar /home/ubuntu/Mapping.jar
