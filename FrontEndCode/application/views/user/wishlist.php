<!DOCTYPE html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Wish List</title>
<link href="<?php echo base_url();?>assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Phone -->

<link rel="shortcut icon" href="<?php echo base_url();?>assets/images/favicon.ico" type="image/x-icon" />

     <style type="text/css">
<!--


#outer_container{ width:auto; padding:0 10px; background:#fff; border:1px solid #ccc; margin:0 0 15px 0;}
#thumbScroller{position:relative; margin:auto;}
#thumbScroller{width:auto;}
#thumbScroller, #thumbScroller .container, #thumbScroller .content{height:163px;}
#thumbScroller{overflow:hidden;}
#thumbScroller .container{position:relative; left:0;}
#thumbScroller .container .content div img{border:5px solid #ccc; width:230px;}
#thumbScroller .content{width:250px; float:left; overflow:hidden;}
#thumbScroller .content div{padding:5px; height:100%; font-family:Verdana, Geneva, sans-serif; font-size:13px;}
#thumbScroller img{border:5px solid #ccc; width:100}
-->
</style>
		
		

</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>


<body>
  <div id="conbody">
    
  <div class="result_side">
  <div id="account_side">
  <div>My Account</div>
  <ul>
   <li><a href="<?php echo base_url();?>index.php/register/call_account">Personal Details</a></li>
  <!--<li><a href="<?php echo base_url();?>index.php/register/profile_show">My Profile</a></li>-->
  <li><a href="<?php echo base_url();?>index.php/register/wish_list">My Wishlist</a></li>
 
  </ul>
  </div>
  <div id="detail_side">
   <h1>Your Wishlist</h1>
   <ul class="list">
   <?php
   $i=1;
   foreach($wish as $row)
   { 
   //print $row['product_id'];
   $get = mysql_query("select * from data where id=".$row['product_id']);
   while($get1=mysql_fetch_array($get)){
   ?>
   <li><table  border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td ><?php echo $i; ?></td>
    <td  align="center"><img src="<?php echo $get1['product_image'];?>"/></td>
    <td>
    <p><?php echo $get1['product_title'];?></p>
    <div class="cost">Price: <span>$ <?php echo number_format((str_replace(",","",$get1['price']))/54.77, 2, '.', '');?></span></div>
    <div class="avaliable"><span></span> <?php echo $get1['shipping_time'];?></div>
    <div class="right"><a href="<?php echo $get1['url'];?>" target="_blank"><input type="button" value="View Site"/></a></div></td>
    
  </tr>
  </table>
  </li>
  
<?php }
$i++;
} ?> 
   </ul>
  
</div>

  </div>
</div>




</div>


<script>
	$outer_container=$("#outer_container");
	$thumbScroller=$("#thumbScroller");
	$thumbScroller_container=$("#thumbScroller .container");
	$thumbScroller_content=$("#thumbScroller .content");
	$thumbScroller_thumb=$("#thumbScroller .thumb");

	var sliderWidth=$thumbScroller.width();
	var itemWidth=$thumbScroller_content.width();

	$thumbScroller_content.each(function (i) {
		totalContent=i*itemWidth;	
		$thumbScroller_container.css("width",totalContent+itemWidth);
	});

	$thumbScroller.mousemove(function(e){
		var mouseCoords=(e.pageX - this.offsetLeft);
	  	var mousePercentY=mouseCoords/sliderWidth;
	  	var destY=-(((totalContent-(sliderWidth-itemWidth))-sliderWidth)*(mousePercentY));
	  	var thePosA=mouseCoords-destY;
	  	var thePosB=destY-mouseCoords;
	  	if(mouseCoords==destY){
			$thumbScroller_container.stop();
	  	}
	  	if(mouseCoords>destY){
			$thumbScroller_container.css("left",-thePosA);
	  	}
	  	if(mouseCoords<destY){
			$thumbScroller_container.css("left",thePosB);
	  	}
	});

	var fadeSpeed=300;
	
	$thumbScroller_thumb.each(function () {
		var $this=$(this);
		$this.fadeTo(fadeSpeed, 0.5);
	});

	$thumbScroller_thumb.hover(
		function(){ //mouse over
			var $this=$(this);
			$this.stop().fadeTo(fadeSpeed, 1);
		},
		function(){ //mouse out
			var $this=$(this);
			$this.stop().fadeTo(fadeSpeed, 0.5);
		}
	);
</script>
</body>
</html>
