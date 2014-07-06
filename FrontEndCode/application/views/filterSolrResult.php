<div>

<ul>

<?php
//print_r($response->response);

foreach($response->response->docs as $key)
{

?>
        <li>
          <?php
          $img=$key->image;
          ?>
		  <div class="product"> <img src=<?php echo $img; ?>/> </div>
          <div class="product_det_box">
            <h4>
              <?php
print substr($key->product_title, 0,50);
if(strlen($key->product_title) >'50') { print "...."; };
//$query="select value from descr where id=".$key->id;
//echo $query;
//$get_desc = mysql_query($query) or die("not executed");
//print_r($get_desc);
?>
            </h4>
            <p>
            
              <?php 
			  print $key->description;
			  /*//print $key->id;
while($info=mysql_fetch_array($get_desc)){
     $string= $info['value'];
     $string = substr(strip_tags($string),0,130);
     //$string = substr($string,0,strrpos($string," "));
    print $string."<br>";
break;
}*/
?>
            </p>
            <a href="<?php echo base_url();?>index.php/front/pro_datail/<?php echo $key->id;?>">
            <input type="button" value="Know More"/>
            </a></div>
          <div class="product_price_rag">
            <?php
			
//$query1="select product_title from data where id=".$key->id;
//$get_desc1 = mysql_query($query1);
//$info1=mysql_fetch_array($get_desc1);

//print_r($key->cluster_id);

//$query2="select product_id from clustering where cluster_id='".$key->cluster_id."' GROUP BY host";

//$get_desc2 = mysql_query($query2);
//$num_rows = mysql_num_rows($get_desc2);

/*$pri_array=array();
while($product_id=mysql_fetch_array($get_desc2))
{
	
	$get_price = "SELECT price FROM data WHERE id='".$product_id['product_id']."'";
	$get_price  = mysql_query($get_price);
	$get_price_result = mysql_fetch_array($get_price);
		
$pri_array[]=str_replace(",","",$get_price_result[0]);
		


//print_r($price);
//$count_rating[]=$im[0][0];
?>
            <?php }
 */
//$key1=array();
//$key1=$pri_array;
//print_r($key);
?>
            <h4>$ <?php echo $key->min_price;?> to $ <?php echo $key->max_price;?></h4>
            <p>From <a href="<?php echo base_url();?>index.php/front/pro_datail/<?php print $key->id;?>"> </a>
	        <?php
			echo $key->storeNumber;
	
          ?>
          
          
          
<?php
} ?>     </p></div></li></ul></div>