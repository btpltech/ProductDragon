<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>:: Advance Search ::</title>
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>

</head>

<body>
<div id="main">
  <div id="header">
    <div class="con">
      <div class="logo"><a href="<?php echo base_url(); ?>"><img src="<?php echo base_url();?>assets/images/logo.png"></a></div>
      <div class="adv">
     <!-- <p><a href="#">Contact Us</a> |  <a href="#">Home</a> |  <a href="#">Account</a> |  <a href="#">Wishlist</a> |  <a href="#">Login</a> |  <a href="#">Signup</a></p>-->
       <!-- <h2>Advance Search</h2>-->
        <div style="overflow:hidden;">
		<form action="<?php echo base_url();?>index.php/front/advance_search">
          <div class="input_box">
            <input type="text" value="" placeholder="Search" id="home" name="search"/>
          </div>
          <div class="ser"><input type="submit" id="search" value="Search"/>
		  </form>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div id="conbody">
<h2 align="center" style="color:#FF6600">Data Not Submitted Successfully </h2> 
<a href="<?php echo base_url();?>index.php/register/sign_up"><h4 align="center" style="color:#FF9933">Try Again</h4>/>
</div>
</div>
</div>
<div class="other_footer">
<div class="footerr">
<div class="footer_logo"><a href="<?php echo base_url();?>"><img src="<?php echo base_url();?>assets/images/footer.png"></a></div>
<div class="footer_text">All right reserved /<b>Our Privacy Policy</b> /<b>Our term of use</b></div>
<div class="footer_menu"><p><a href="#">Contact Us</a> |  <a href="<?php  echo base_url();?>">Home</a> |  <a href="account.html">Account</a> |  <a href="wishlist.html">Wishlist</a> |  <a href="sign_in.html">Login</a> |  <a href="sign_up.html">Signup</a></p>
</div>
<div></div>
</div></div>

</div>

<script>
$("#accordion > li > div").click(function(){
 
    if(false == $(this).next().is(':visible')) {
        $('#accordion ul').slideUp(300);
    }
    $(this).next().slideToggle(300);
});
 
$('#accordion ul:eq(0)').show();

</script>  
</body>
</html>
