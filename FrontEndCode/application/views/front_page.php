<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>..:: Ample Find ::..</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<!-- Le styles -->
<link href="<?php echo base_url();?>assets/css/bootstrap.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-slider.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/icomoon.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-select.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-responsive.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/docs.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/jquery-ui.css" rel="stylesheet">
<script src="<?php echo base_url();?>assets/js/jquery-ui.js"></script>

<script src="<?php echo base_url();?>assets/js/jquery-10.js"></script>


<body style="background:#fafafa;">
<!-- navvbar -->
<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner " >
        <div class="container">
        <ul class="nav ">
        <li><a href="#myModal"  data-toggle="modal" role="button"><img  src="<?php echo base_url();?>assets/ico/key.png"> Sign In</a></li>
       
         <li><a href="#myModal2"  data-toggle="modal"  role="button"><img src="<?php echo base_url();?>assets/ico/user.png"> Sign Up</a></li>
         
          <li><a href="#"><select name="colorpicker-picker">
          <option value="#f39c12">orange</option>
            <option value="#1abc9c">blueGreen</option>
            <option value="#3498db">Blue</option>
            <option value="#9b59b6" >Purple</option>
            <option value="#836710">wood</option>
            <option value="#34495e">grayblue</option>
            <option value="#f1c40f">yellow</option>
            <option value="#e74c3c">red</option>
            <option value="#f10fe9">pink</option>
            <option value="#95a5a6" >cyangray</option>
            <option value="#b5b659">greenlight</option>
           
          </select>&nbsp;&nbsp;&nbsp;Theme</a></li>
          </ul>
        </div>
      </div>
    </div>
    
<!-- navbar ends -->
<div class="container">
<div class="srhbr-hold">
<div align="center">
<a href="#" class="logo">Ample Find <img src="<?php echo base_url();?>assets/img/base/bag.png"/></a></div>
<div class="clearfix"></div>
<form action="<?php echo base_url();?>index.php/front/advance_search"> 
<div class="src-bar" align="center">

        <div id="MySearch" class="input-append search">
          <input type="search" placeholder="Search Products..." class="span6" name="search" id="home">
          <script>document.getElementById('home').focus()</script>
          <input type="hidden" name="page" value="0">
<button class="btn2 search"><img src="<?php echo base_url();?>assets/ico/search-icon.png"></button>
          <div class="clearfix"></div>
          <a href="listing.html" class="btn btn-small btn-primary pull-right search-btn">Advance Search</a>
        </div>
    </form>
    <!-- Button to trigger modal -->
  
    <!-- Modal -->
    <div id="myModal" class="modal hide fade span7 text-left" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>

    <div class="modal-body">
    <div class="login-form pull-left">
    <article class=" well" id="old-user">
    
    <h4 class="text-center">	Existing user? Login here.</h4> 

    <form >
        	<div class="control-group">
            <label>E-Mail</label>
            <div class="controls">
            <input type="text" name="Email" placeholder="Input Your Email here">
            </div>
            </div>
            <div class="control-group">
            <label> Password </label>
            <div class="controls">
            <input type="password" name="password" placeholder="Input Your Password here">
            </div>
            </div>
            <div class="control-group">
         
          <div class="controls">
          <input type="checkbox" id="checkbox-1-1" class="regular-checkbox" /><label for="checkbox-1-1"></label><span>Remember Me</span>
          </div>
         </div>
         <div class="clearfix"></div>
         <p>&nbsp;</p>
         <div class="control-group">
          <div class="controls text-center">
          <a href="#" class="btn btn-primary  btn-alt btn-large">Sign-Up</a>
          </div>
          </div>
           
          

    </form>
    <div class="control-group forget">
       
          <a href="#" class=" btn-link btn-mini">Forget Password?</a>
        
          </div>
    </article>
    <div class="text-center"> <a href="#">
    <div class="btn btn-social btn-fb btn-alt"><i class="icon-facebook pull-left"></i>&nbsp; &nbsp;Login With Facebook</div>
    </a></div>
 </div>
    <div class="login-form pull-right">
    <article class=" well dismis" id="new-user">
    
    <h4 class="text-center" id="new-user-ini">New? Create a user here.</h4>

    <form  class="">
        	<div class="control-group">
            <label>E-Mail</label>
            <div class="controls">
            <input type="text" name="Email" placeholder="Input Your Email here" required disabled/>
            </div>
            </div>
            <div class="control-group">
            <label> Password </label>
            <div class="controls">
            <input type="password" name="password" placeholder="Input Your Password here" required disabled/>
            </div>
            </div>
            <div class="control-group">
       <p class="pad10">&nbsp;<br>
		&nbsp;	</p>
          <div class="controls">
        
          </div>
         </div>
         <div class="control-group">
          <div class="controls text-center">
          <a href="#" class="btn btn-primary  btn-alt btn-large disabled">Sign-Up</a>
          </div>
          </div>
          
    </form>
    </article>
    <div class="text-center"> <a href="#">
    <div class="btn btn-social btn-twtr btn-alt"><i class="icon-twitter pull-left"></i>&nbsp; &nbsp;Login With Twitter</div>
    </a></div>
    </div>
    </div>
    </div>
    
    <div id="myModal2" class="modal hide fade span7 text-left" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>

    <div class="modal-body">
    <div class="login-form pull-left">
    <article class=" well dismis " id="old-user">
    
    <h4 class="text-center">	Existing user? Login here.</h4> 

    <form >
            <div class="control-group">
            <label>E-Mail</label>
            <div class="controls">
            <input type="text" name="Email" placeholder="Input Your Email here" disabled>
            </div>
            </div>
            <div class="control-group">
            <label> Password </label>
            <div class="controls">
            <input type="password" name="password" placeholder="Input Your Password here" disabled>
            </div>
            </div>
            <div class="control-group">
         <div class="controls">
         <input type="checkbox" id="checkbox-1-1" class="regular-checkbox" /><label for="checkbox-1-1"></label><span>Remember Me</span>
         </div>
         </div>
         
         <div class="clearfix"></div>
         <p>&nbsp;</p>
         <div class="control-group">
          <div class="controls text-center">
          <a href="#" class="btn btn-primary  btn-alt btn-large">Sign-Up</a>
          </div>
          </div>
           
          

    </form>
    <div class="control-group forget">
       
          <a href="#" class=" btn-link btn-mini">Forget Password?</a>
        
          </div>
    </article>
    <div class="text-center"> <a href="#">
    <div class="btn btn-social btn-fb btn-alt"><i class="icon-facebook pull-left"></i>&nbsp; &nbsp;Login With Facebook</div>
    </a></div>
 </div>
    <div class="login-form pull-right">
    <article class=" well " id="new-user">
    
    <h4 class="text-center" id="new-user-ini">New? Create a user here.</h4>

    <form  class="">
        <div class="control-group">
            <label>E-Mail</label>
            <div class="controls">
            <input type="text" name="Email" placeholder="Input Your Email here"/>
            </div>
            </div>
            <div class="control-group">
            <label> Password </label>
            <div class="controls">
            <input type="password" name="password" placeholder="Input Your Password here" />
            </div>
            </div>
            <div class="control-group">
       <p class="pad10">&nbsp;<br>
		&nbsp;	</p>
          <div class="controls">
        
          </div>
         </div>
         <div class="control-group">
          <div class="controls text-center">
          <a href="#" class="btn btn-primary  btn-alt btn-large disabled">Sign-Up</a>
          </div>
          </div>
          
          

    </form>
    </article>
     <div class="text-center"> <a href="#">
            <div class="btn btn-social btn-twtr btn-alt"><img src="assets/ico/tw.png" class="img-line">&nbsp; &nbsp;Login With Twitter</div>
            </a></div>
        </div>
  </div>
    </div>
    

<footer class="footer navbar-fixed-bottom">


<div class="container">
	<div class="footer-links pull-left">
    	<li><a href="#">Contact Us</a></li>
        <li><a href="#">Home</a></li>
        <li><a href="#">About Us</a></li>
        <li><a href="#">Wishlist</a></li>
       <li> <a href="#">Sign-up</a></li>
        <li><a href="#">Sign-in</a></li>
    </div>
    <div class="footer-links pull-right">
    	<li>&copy;2013 Ample Find. All rights reserved</li>
        <li><a href="#">Privacy Policy</a></li>
        <li><a href="#">Terms of use</a></li>
       
    </div>

</div>
</footer>
 <script>

$(document).ready(function() {
 
    $('#colorpicker-inline').simplecolorpicker('selectColor', '#fbd75b');
	  $('select[name="colorpicker-picker"]').simplecolorpicker({picker: true});

});
</script>

<script src="<?php echo base_url();?>assets/js/bootstrap-transition.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-modal.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-dropdown.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-button.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-collapse.js"></script>
<script src="<?php echo base_url();?>assets/js/jquery.simplecolorpicker.js"></script>  
<script src="<?php echo base_url();?>assets/js/custom.js"></script> 
 <script src="<?php echo base_url();?>assets/js/application.js"></script> 
<script src="<?php echo base_url();?>assets/js/bootstrap-slider.js"></script> 
<script src="<?php echo base_url();?>assets/js/custom.js"></script> 
<script src="<?php echo base_url();?>assets/js/bootstrap-select.js"></script>
</body>
</html>
