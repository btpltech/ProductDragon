<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>User Profile</title>

<link href="<?php echo base_url();?>assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Phone -->
<meta name="viewport" content="width=device-width, initial-scale=1" />

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
<script type="text/javascript">
function update()
{
alert("Details Updated Successfully");
return true;
}
</script>

</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>


<body>
<div id="main">
  <div id="conbody">
    <?php 
	$this->load->library('session');
	$this->load->database();
	$user_id=$this->session->userdata('userid');
	$result_user = mysql_query("SELECT * FROM user_details WHERE user_no=".$user_id);
	while($row_user=mysql_fetch_array($result_user)){
	$first_name=$row_user['first_name'];
	$last_name=$row_user['last_name'];
	$mobile=$row_user['mobile'];
	$land_no=$row_user['land_no'];
	$gender=$row_user['gender'];
	}
	?>
  <div class="result_side">
  <div id="account_side">
  <div>My Account</div>
  <ul>
   <li><a href="<?php echo base_url();?>index.php/register/call_account">Personal Details</a></li>
  <!--<li><a href="#">My Profile</a></li>-->
  <li><a href="<?php echo base_url();?>index.php/register/wish_list">My Wishlist</a></li>
 
  </ul>
  </div>
  <div id="detail_side">
  <h1>Personal Information</h1><div class="login">
  <div class="per">
  <form action="<?php echo base_url();?>index.php/register/save_user_details" method="post">
  <table  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" valign="middle">First Name</td>
    <td  align="left" valign="middle"><input type="text"  name="first_name" value="<?php echo $first_name; ?>"/></td>
  </tr>
  <tr>
    <td  align="left" valign="middle">Last Name</td>
    <td  align="left" valign="middle"><input type="text" name="last_name" value="<?php echo $last_name; ?>"/></td>
  </tr>
  <tr>
    <td  align="left" valign="middle">Mobile Number</td>
    <td  align="left" valign="middle"><input type="text" name="mobile_no"  value="<?php echo $mobile; ?>"/></td>
  </tr>
  <tr>
    <td  align="left" valign="middle">Landline Number</td>
    <td  align="left" valign="middle"><input type="text" name="land_line_no"  value="<?php echo $land_no; ?>"/></td>
  </tr>
  <tr>
    <td  align="left" valign="middle">Gender</td>
    <td align="left" valign="middle"><select name="gender">
    <option>Select</option>
     <option <?php if($gender=="Female"){ ?> selected="selected" <?php } ?>>Female</option>
      <option <?php if($gender=="Male"){ ?> selected="selected" <?php } ?>>Male</option>
    </select></td>
  </tr>

  <tr>
 
    <td align="left" valign="middle">&nbsp;</td>
	
    <td  align="left" valign="middle"><input type="submit" value="Save Change" class="btn_submit" name="submit" onclick="update();"/></td>
  </tr>
  </table>
  </form>
</div>
<!--Its for Alert-->
  <!--Its for Alert-->
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
