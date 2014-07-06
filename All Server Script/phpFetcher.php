<?php
// code for running script 
while(true)
{
  if(!file_exists("/home/ubuntu/.lock"))
  {
	if(file_exists("/home/ubuntu/.read"))
  {
   $file = fopen("/home/ubuntu/.read","r");
 # echo "Starting php script"; 
   $line = fgets($file);
   if($line!='')
   {

   	$data = explode("#",$line);
   	
   	$arg_1 = $data[0];
   	$arg_2 = $data[1];
	$file1 = fopen("/home/ubuntu/fetching_status.txt","r");
	$line1 = fgets($file1);
   if($line1=="start")
   {   
	echo "Reading data and passing it to the shell script";
        shell_exec('sudo /home/ubuntu/fetcher.sh '.rtrim($arg_1).' '.rtrim($arg_2));
	echo "Shell script execution done";
}
fclose($file1);
   } // if close
   
  fclose($file);
}
  }// outer if close

}// while close

?>
