<!DOCTYPE html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>AmpleFind</title>
<link href="<?php echo base_url();?>assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<?php echo base_url();?>assets/js/respond.min.js"></script>

<!-- Desktop -->

<link rel="shortcut icon" href="<?php echo base_url();?>assets/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>

<script>
$(document).ready(function() {

	
		$("#sign_button").click(function(){
		$("#sign_form").toggle();

			

	});

});
</script>

</head>

<body id="body">
<div id="main">
<div id="conbody2">
<div id="top">
<!--<div class="colorbox">
<ul>
<li></li>
<li></li>
<li></li>
<li></li>
</ul>
</div>-->
<!--<div class="color-change">Change Theme</div>-->
<div class="front_botton">
<?php 
if(!$this->session->userdata('username')){
?>
<form action="<?php if(!isset($product_id)){echo base_url();?>index.php/register/check_user <?php } else{ echo base_url();?>index.php/register/check_user/<?php echo $product_id; }?>" method="post">
<!--<input type="submit" name="submit" id="abut" value="Sign In"/>-->
<div class="sign-in">
<ul>
<li><a href="#"  id="sign_button">Sign in</a>
<ul id="sign_form">
<div class="img_arrow"><img src="<?php echo base_url();?>assets/images/d_arrow_top.png"/></div>
<input type="text"  placeholder="Email Id" name="email_log"/>
<p></p>
<input type="Password"  placeholder="Password"  name="password_log"/>
<p></p>
<input type="submit" id="abut" value="Sign in" name="submit"/>
</ul>
</li>
<li><a href="<?php echo base_url();?>index.php/register/sign_up">Sign Up</a></li>
</ul>
</div>
</form>

<?php
}
?>
</div>

</div>
<div class="search_part">
<div align="center"><img src="<?php echo base_url();?>assets/images/logo.png"></div>
<div style="overflow:hidden;">
<form action="<?php echo base_url();?>index.php/front/advance_search"> 
<!--input_box-->
<div class="input_box"><div class="fdbx"></div>
<input type="text" value="" placeholder= "Search Product" id="home" name="search" style="background:none; width:75%;float:left;border:none;"/>
<input type="hidden" name="page" value="0">
<script>document.getElementById('home').focus()</script> <button class="search_button1"></button></div>
<!--input_box-->


<!--input_box2-->


</form>
</div>
</div>

</div>
</body>
</html>
