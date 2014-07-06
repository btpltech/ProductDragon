<!DOCTYPE html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Connect with AmpleFind</title>
<link href="<?php echo base_url();?>assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Phone -->

<link rel="shortcut icon" href="<?php echo base_url();?>assets/images/favicon.ico" type="image/x-icon" />

  <script src="http://cdn.jquerytools.org/1.2.7/full/jquery.tools.min.js"></script>
<!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>-->

</head>

<body>
<div id="conbody">
  <!--<div id="top">
    <div class="colorbox">
      <ul>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
    <div class="color-change">Change Theme</div>
    <a href="sign_up.html">
      <input type="button" id="abut" value="Sign up"/>
    </a> </div>-->
  <!--<div id="sign">
  <div class="div">Sign in</div>
<div class="div2"><table border="0" width="350px" cellspacing="0" cellpadding="0">
  <tr>
    <td width="26%" height="50" align="left" valign="middle">Email Id</td>
    <td width="74%" height="50" align="left" valign="middle"><input type="text"  /></td>
  </tr>
  <tr>
    <td height="50" align="left" valign="middle">Password</td>
    <td height="50" align="left" valign="middle"><input type="password"  /></td>
  </tr>
  <tr>
    <td height="50" align="left" valign="middle">&nbsp;</td>
    <td height="50" align="left" valign="middle"><input type="button" value="Submit" id="button" /></td>
  </tr>
</table>
</div>
</div>-->
<div class="signin_header" align="center"><a href="<?php echo base_url();?>"><img src="<?php echo base_url();?>assets/images/logo.png"/></a>
<!--<h2>Welcome to AmpleFind</h2>-->
</div>
<div class="heading">Connect With AmpleFind</div>
<div class="line"></div>

<div id="signinbx">
<div class="div1">

<h2>Sign In</h2>
<h3 id="why"><a href="#">?</a></h3>
<div class="tooltip">
<p>Find out how to resolve problems signing in, or learn about easy ways to protect your account on AmpleFind.</p>
<div class="arrow"><img src="<?php echo base_url();?>assets/images/d_arrow.png"/></div>
</div>
<!--<div class="pop"></div>-->


<hr />
<form action="<?php if(!isset($product_id)){echo base_url();?>index.php/register/check_user <?php } else{ echo base_url();?>index.php/register/check_user/<?php echo $product_id; }?>" method="post">


<input type="text" name="email_log" placeholder="Username" />

<br />
<input type="password" name="password_log" placeholder="Password "  />

<input type="checkbox" id="checkbox" /><p>Stay signed in</p>
<div align="left">
<input type="submit" value="Sign In" name="submit" />
</div>
</form>

</div>
<div class="div2">
<h2>Join AmpleFind here</h2>
<hr />

<p align="center">Get Connected </p>
<div align="center"><form action="<?php echo base_url();?>index.php/register/sign_up">
<input type="submit" value="Sign Up"/>
</form></div>

</div>
<div align="center" class="banner_div"><img src="<?php echo base_url();?>assets/images/banner.png" /></div>
</div>

</div>
<script>
  $(document).ready(function() {
      $("#why").tooltip({ effect: 'slide'});
    });
</script>

</body>
</html>
