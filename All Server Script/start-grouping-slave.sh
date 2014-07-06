export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts

echo "launching Grouping slaves"
/home/ubuntu/launch-hadoop-slaves grouping $1
echo "done"

echo "all slaves of group 'grouping' are started.............."
