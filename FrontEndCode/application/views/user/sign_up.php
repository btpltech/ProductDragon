<!DOCTYPE html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Connect with AmpleFind</title>
<link href="<?php echo base_url();?>assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<?php echo base_url();?>assets/css/style.css" rel="stylesheet" type="text/css" />
<!-- Phone -->

<link rel="shortcut icon" href="<?php echo base_url();?>assets/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" language="javascript">
function validate()
{
	
  if(document.getElementById("first_name").value=='')
  {
  alert("Please provide First Name");
  document.getElementById("first_name").focus;
  return false;
  }
  //alert('hi');
  if(document.getElementById("last_name").value=='')
  {
  alert("Please provide Last Name");
  document.getElementById("last_name").focus;
  return false;
  }
  if(document.getElementById("log_email").value=='')
  {
  alert("Please provide an Email Address");
  document.getElementById("log_email").focus;
  return false;
  }
  if(document.getElementById("log_password").value=='')
  {
   alert("Please Provide Passsword");
   document.getElementById("log_password").focus;
   return false;
  }
  if(document.getElementById("log_password").value!=document.getElementById("c_log_password").value)
  {
  alert("Password Don't Match");
  document.getElementById("c_log_password").focus;
  return false;
  }
  if(document.getElementById("name_user").value=='')
  {
  alert("Please provide User Name");
  document.getElementById("name_user").focus;
  return false;
  }
 }
</script>
</head>

<body>
<div id="conbody2"> 
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
  <div class="signin_header" align="center"><a href="<?php echo base_url();?>"><img src="<?php echo base_url();?>assets/images/logo.png"/></a> </div>
  <div class="heading">Get Registered With AmpleFind</div>
  <div class="line"></div>
  <div id="signinbx">
    <div class="div3">
      <form action="<?php echo base_url();?>index.php/register/create_user" method="post" onsubmit="return validate();">
        <h2>Get Registered Here</h2>
        <hr />
        <ul class="ul1">
        <li>
          <div class="fieldbx">
            <input  type="text" placeholder="First Name" name="first_name" id="first_name">
          </div>
          </li>
          <li>
            <div class="fieldbx">
              <input value="" type="text" placeholder="Last Name" name="last_name" id="last_name">
            </div>
          </li>
        </ul>
        <ul class="ul2">
          <li>
            <div class="fieldbx">
              <input value="" type="text" placeholder="E-mail" name="log_email" id="log_email">
            </div>
          </li>
          <li>
               
                           <div class="fieldbx">
              <input type="text" name="name_user" placeholder="AmpleFind user ID " id="name_user" value=""/>
            </div>
          </li>
        </ul>
        <ul class="ul2">
          <li>
           
            <div class="fieldbx">
              <input value="" type="password" placeholder="Password" name="log_password" id="log_password" >
            </div>
          </li>
          <li>
           
            <br />
            <div class="fieldbx">
              <input value="" type="password" placeholder="Confirm Passowrd" name="c_log_password" id="c_log_password">
            </div>
          </li>
        </ul>
        <ul class="ul4">
          <li><input type="checkbox" id="checkbox" />I have read and accepted the <a href="#">User Agreement</a> and <a href="#">Privacy Policy</a>.</li>
        </ul>
        <input type="submit" value="Sign Up" id="sub" name="submit" />
      </form>
    </div>
    <div class="div4">
      <h2>Already have an account</h2>
      <hr />
      <form action="<?php echo base_url();?>index.php/register/login">
        <input type="submit" name="submit" id="sub" value="Sign In"/>
      </form>
      <img src="<?php echo base_url();?>assets/images/ads.png"  width="100%"/> </div>
  </div>
</div>
</body>
</html>