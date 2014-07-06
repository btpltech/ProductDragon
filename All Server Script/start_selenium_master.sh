export AMPLE_HOME=/home/ubuntu/hadoop-ec2-scripts
echo "launching Single instance for getmenulinks process"
bash $AMPLE_HOME/nutch/version2/start_daemon.sh
bash $AMPLE_HOME/nutch/version2/copy_all_into_selenium_master.sh
echo "done"
