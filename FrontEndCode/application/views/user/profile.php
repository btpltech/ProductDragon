<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Profile</title>
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
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
<div id="main">
 
  <div id="conbody">
    
  <div class="full_con">
  <div id="account_side">
  <div>My Account</div>
  <ul>
   <li><a href="account.html">Personal Details</a></li>
  <li><a href="profile.html">My Profile</a></li>
  <li><a href="wishlist.html">My Wishlist</a></li>
 
  </ul>
  </div>
  <div id="detail_side">
  <div class="rate">
  <h3>Ratings by Govind (1)</h3>
  <div class="div">
  <p>Ratings Distribution</p>
  <table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th>5 Star</th>
    <th>4 Star</th>
    <th>3 Star</th>
    <th>2 Star</th>
    <th>1 Star</th>
  </tr>
  <tr>
    <td>0</td>
    <td>0</td>
    <td>0</td>
    <td>0</td>
    <td>0</td>
  </tr>
</table>
</div>
  </div>
  
  
  <div class="review">
  <h3>Reviews by Govind (1)</h3>
  <div class="div">
  <p>0 of 0 users found this user's reviews helpful</p>
  <div class="div">
  <table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="160" align="center"><img src="images/iphone3.jpg"/></td>
    <td width="440">
    <p>Samsung Galaxy Tab 2 P3110 (Titanium Silver, Wi-Fi, 16 GB)</p>
    <div class="cost">Price: <span>Rs. 12599</span></div>
    <div class="avaliable"><span>In Stock.</span> Delivered in 2-3 business days.</div>
    </td>
  </tr>
  </table>
  
  <div class="det">
  <p><b>Title</b> on 22 March 13</p>
  <p>Govind rated: <img src="images/rate.png"/><img src="images/rate.png"/><img src="images/rate.png"/><img src="images/rate.png"/><img src="images/rate1.png"/></p>
  <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap</p>
  </div>
  <div class="up"><a href="#">Edit</a> | <a href="#">Delete</a> | <a href="#">Permalink</a></div>
</div>
  
  </div>
  </div>
</div>

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
