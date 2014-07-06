#Set path of home
HOME=/home/ubuntu
#echo "export HOME=$(echo ">>$HOME/.bashrc
#source $HOME/.bashrc
sudo perl -pi.orig -e   'next if /-backports/; s/^# (deb .* multiverse)$/$1/'   /etc/apt/sources.list

sudo apt-add-repository -y ppa:awstools-dev/awstools
sudo apt-get update

sudo apt-get install -y ec2-api-tools ec2-ami-tools iamcli rdscli moncli ascli elasticache
sudo chmod -R 777 /home/ubuntu/.aws
sudo chmod -R 0400 /home/ubuntu/.aws/id_rsa-hadoop


#copying pk & cert from home to .aws folder
sudo cp -r $HOME/imp/* $HOME/.aws/
#sudo cp $HOME/cert-*.pem $HOME/.aws/
#sudo cp $HOME/id-rsa-hadoop $HOME/.aws/
#creating aws-credential-file.txt file
#cat > $HOME/.aws/aws-credential-file.txt <<EOF
#AWSAccessKeyId=AKIAJMAEQRZSCGU3JZTA
#AWSSecretKey=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
#EOF

# setting pk & cert into bashrc file
#echo "export EC2_PRIVATE_KEY=$(echo /home/ubuntu/.aws/pk-*.pem)">>$HOME/.bashrc
#echo "export EC2_CERT=$(echo /home/ubuntu/.aws/cert-*.pem)">>$HOME/.bashrc
#echo "export AWS_CREDENTIAL_FILE=/home/ubuntu/.aws/aws-credential-file.txt">>$HOME/.bashrc
#source $HOME/.bashrc

