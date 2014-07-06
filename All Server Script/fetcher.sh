HADOOP_HOME=/usr/local/hadoop-1.2.1
cd $HADOOP_HOME
echo "doing">$HOME/fetching_status.txt
# set the number of slaves nodes
numSlaves=$2

# and the total number of available tasks
# sets Hadoop parameter "mapred.reduce.tasks"
numTasks=`expr $numSlaves \* 2`

# number of urls to fetch in one iteration
# 250K per task?
sizeFetchlist=`expr $numSlaves \* 5000000`

# time limit for feching
timeLimitFetch=180

# num threads for fetching
numThreads=`expr $numSlaves \* 10`

#input directory for url to be fetched
SEEDDIR=inject

#output directory for crawled data
CRAWL_PATH=product_html

echo "Deleting Existing injecting output Directory"
bin/hadoop dfs -rmr /$SEEDDIR
echo "Export product links to nutch directory"
bin/hadoop jar $HOME/fetcher.jar com.ample.fetcher.ExportHbase $1 product_page_url /$SEEDDIR
echo "Deleting Existing product html output directory"
bin/hadoop dfs -rmr /$CRAWL_PATH
echo "start nutch for getting the product links in segment content directory"

commonOptions="-D mapred.reduce.tasks=$numTasks -D mapred.child.java.opts=-Xmx3000m -D mapred.reduce.tasks.speculative.execution=false -D mapred.map.tasks.speculative.execution=false -D mapred.compress.map.output=true"

bin/hadoop jar $HOME/apache-nutch-1.7/runtime/deploy/apache*.job org.apache.nutch.crawl.Injector /$CRAWL_PATH/crawldb /$SEEDDIR

bin/hadoop jar $HOME/apache-nutch-1.7/runtime/deploy/apache*.job org.apache.nutch.crawl.Generator $commonOptions /$CRAWL_PATH/crawldb /$CRAWL_PATH/segments -topN 1 -numFetchers $numSlaves -noFilter

SEGMENT=`bin/hadoop dfs -ls /$CRAWL_PATH/segments/ | grep segments |  sed -e "s/\//\\n/g" | egrep 20[0-9]+ | sort -n | tail -n 1`
echo "Segment Path is : $SEGMENT"
bin/hadoop jar $HOME/apache-nutch-1.7/runtime/deploy/apache*.job org.apache.nutch.fetcher.Fetcher $commonOptions -D fetcher.timelimit.mins=$timeLimitFetch /$CRAWL_PATH/segments/$SEGMENT -noParsing -threads $numThreads

#$HOME/apache-nutch-1.6/runtime/deploy/bin/nutch crawl /output2 -dir /product_html -depth 1 -topN 10000000

echo "Parsing Started"
bin/hadoop jar $HOME/parser.jar com.ample.parser.ParsingHtml $1 /product_html/segments
echo "Mapping of Attributes Started"
bin/hadoop jar $HOME/mapper.jar com.ample.mapping.MappingAttributes $1
echo "done">>$HOME/fetching_status.txt
