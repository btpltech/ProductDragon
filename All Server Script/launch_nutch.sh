# get arguments
export JAVA_HOME=/usr/local/jdk1.7.0_17
export HADOOP_HOME=/usr/local/hadoop-1.2.1
export PATH=$JAVA_HOME/bin:$HADOOP_HOME/bin:$PATH
#download nutch

echo $HADOOP_HOME
echo $JAVA_HOME

sudo wget -nv http://www.carfab.com/apachesoftware/nutch/1.7/apache-nutch-1.7-src.tar.gz
sudo tar xzf apache-nutch-1.7-src.tar.gz
sudo rm -f apache-nutch-1.7-src.tar.gz

export NUTCH_HOME=/home/ubuntu/apache-nutch-1.7

cd $NUTCH_HOME/conf/
sudo rm -f regex-urlfilter.xml

#copying into regex-urlfilter.xml
cat > $NUTCH_HOME/conf/regex-urlfilter.txt <<EOF
# The default url filter.
# Better for whole-internet crawling.

# Each non-comment, non-blank line contains a regular expression
# prefixed by '+' or '-'.  The first matching pattern in the file
# determines whether a URL is included or ignored.  If no pattern
# matches, the URL is ignored.

# skip file: ftp: and mailto: urls
-^(file|ftp|mailto):

# skip image and other suffixes we can't yet parse
# for a more extensive coverage use the urlfilter-suffix plugin
-\.(gif|GIF|jpg|JPG|png|PNG|ico|ICO|css|CSS|sit|SIT|eps|EPS|wmf|WMF|zip|ZIP|ppt|PPT|mpg|MPG|xls|XLS|gz|GZ|rpm|RPM|tgz|TGZ|mov|MOV|exe|EXE|jpeg|JPEG|bmp|BMP|js|JS)$

# skip URLs containing certain characters as probable queries, etc.
#-[?*!@=]

# skip URLs with slash-delimited segment that repeats 3+ times, to break loops
-.*(/[^/]+)/[^/]+\1/[^/]+\1/

# accept anything else
+.
EOF

#downloading ant
#sudo apt-get install ant
#echo "ant downloaded successfully"
cd $NUTCH_HOME

#compile nutch
ant clean
ant

#sudo rm -rf $NUTCH_HOME/url
#sudo mkdir $NUTCH_HOME/url
#cat > $NUTCH_HOME/url/seed.txt <<EOF
#http://www.$1/
#EOF
#sudo cp -r $NUTCH_HOME/url $HADOOP_HOME/
#sudo $HADOOP_HOME/bin/hadoop dfs -rmr /url
#sudo $HADOOP_HOME/bin/hadoop dfs -put $NUTCH_HOME/url/ /
##NUTCH_JAVA_HOME=$JAVA_HOME

echo 'export JAVA_HOME='$JAVA_HOME | cat - $NUTCH_HOME/runtime/deploy/bin/nutch > temp && mv temp $NUTCH_HOME/runtime/deploy/bin/nutch
echo 'export HADOOP_HOME='$HADOOP_HOME | cat - $NUTCH_HOME/runtime/deploy/bin/nutch > temp && mv temp $NUTCH_HOME/runtime/deploy/bin/nutch
echo 'export PATH='$PATH | cat - $NUTCH_HOME/runtime/deploy/bin/nutch > temp && mv temp $NUTCH_HOME/runtime/deploy/bin/nutch

echo $NUTCH_HOME
sudo chmod -R 777 $NUTCH_HOME/runtime/deploy/bin/
#COM="$NUTCH_HOME/runtime/deploy/bin/nutch crawl /url -dir /$1 -threads $2 -depth $3 -topN $4"
#echo $COM
#sudo $COM

