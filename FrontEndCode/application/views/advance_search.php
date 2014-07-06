<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Ample Find Listing</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<!-- Le styles -->
<link href="<?php echo base_url();?>assets/css/bootstrap.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/icomoon.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-select.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-responsive.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/docs.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/jquery-ui.css" rel="stylesheet">

<script src="<?php echo base_url();?>assets/js/jquery-10.js"></script>
<script src="<?php echo base_url();?>assets/js/jquery-ui.js"></script>
<script type="text/javascript">
    $(document).ready(function() {

    $('#listimg').attr("src","<?php echo base_url();?>assets/ico/list_acrion.png");
    });
    </script>
<style type="text/css">
  
  .scroll-btn a{display: none;}

</style>

</head>
<body>
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
    <div class="btn btn-social btn-twtr btn-alt"><i class="icon-twitter pull-left"></i>&nbsp; &nbsp;Login With Twitter</div>
    </a></div>
    </div>
    </div>
    
    </div>
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
           
          </select>&nbsp;&nbsp;Theme</a></li>
          </ul>
        </div>
      </div>
    </div>
<div class=" page-header ">
  <div class="container">
    <div class="row">
    
      <form action="<?php echo base_url();?>index.php/front/advance_search"> 
<div class="span3 pad-top"><a href="<?php echo base_url();?>index.php" class="head-logo"><img src="<?php echo base_url();?>assets/img/base/logo-orange.png"/></a></div>
            <div class="span9 input-append search pull-right" id="MySearch" >
	
          <input type="search" placeholder="Search Products..." class="span8" name="search" id="home" style="background-color:#fdfdfd;">
          <script>document.getElementById('home').focus()</script>
          <input type="hidden" name="page" value="0">
	  <button class="btn2 search"><img src="<?php echo base_url();?>assets/ico/search-icon.png"></button>
	  <div class="clearfix"></div>
        <a href="#" class="btn btn-small btn-primary pull-right search-btn" id="adv-btn">Advance Search</a> </div>
</form>
</div>
    </div>
  </div>
  <div class="adv-search" id="adv-search" style="display:none;">
  <div class="container">
  <pre><div class="pull-left">Advance Product Search</div><div class="pull-right"><button class="btn btn-mini" id="close">&times;</button></div></pre>
  <p></p>
  <div class="row">
  <div class="span9">
<form class=" form-horizontal search ">
<div class="control-group right-arrow" >
<label class="control-label" >Products</label>
<div class="controls">
<input  type="text" class="span6 simple-text"> 
        </div>
        </div>
        <div class="control-group" >
       <label class="control-label">Price</label>
       <div class="controls" style="position:relative;" >
<input type="text" id="amount" class="slide-result"  />
      <div id="slider-range"></div>
       </div>
       </div>
        <div class="control-group">

       <label class="control-label">Country</label>
       <div class="controls">
       <select class="selectpicker span7">
          <option>India</option>
          <option>Africa</option>
          <option>Canada</option>
        </select>
        </div>
        
       </div>
       <div class="control-group">
<label class="control-label" >Brand</label>
<div class="controls">
<input  type="text" class="span6 simple-text"> 
        </div>
        </div>
        <div class="control-group overflow">
<label class="control-label">Color</label>
<div class=" controls  ">

<input type="checkbox" id="checkbox-1-3" class="regular-checkbox" /><label for="checkbox-1-3"></label><span>any color</span>
          </div>
<div class="span2">
<input type="checkbox" id="checkbox-1-2" class="regular-checkbox" /><label for="checkbox-1-2"></label><span>Black & White</span>
</label>
</div>
<div class=" span2">
<select name="colorpicker-picker">
            <option value="#7bd148">Green</option>
            <option value="#5484ed">Bold blue</option>
            <option value="#a4bdfc" >Blue</option>
            <option value="#46d6db">Turquoise</option>
            <option value="#7ae7bf">Light green</option>
            <option value="#51b749">Bold green</option>
            <option value="#fbd75b">Yellow</option>
            <option value="#ffb878">Orange</option>
            <option value="#ff887c" >Red</option>
            <option value="#dc2127">Bold red</option>
            <option value="#dbadff">Purple</option>
            <option value="#e1e1e1">Gray</option>
          </select>&nbsp <i class="bun" >This Color</i>
</div>
</div>
        <div class="control-group">
<label class=" control-label">Rating</label>
<div class=" controls">
<ul class="nav stars ">
          <li><a class="icon active" href="#"><img src="<?php echo base_url();?>assets/ico/star.png"> </a></li>
          <li><a  class="icon  active" href="#"><img src="<?php echo base_url();?>assets/ico/star.png"> </a></li>
          <li><a  class="icon " href="#"><img src="<?php echo base_url();?>assets/ico/star.png"> </a></li>
          <li><a  class="icon " href="#"><img src="<?php echo base_url();?>assets/ico/star.png"> </a></li>
          <li><a  class="icon " href="#"><img src="<?php echo base_url();?>assets/ico/star.png"> </a></li>
        </ul>
</div>

        </div>
        <div class="control-group">

       <label class="control-label">Popularity</label> 
       <div class="controls">
       <select class="selectpicker span6">
          <option>Most Wished</option>
         
        </select>
        
        
       </div>
       </div>
       <div class="control-group">

       <label class="control-label">Size</label>
       <div class="controls">
       <select class="selectpicker span7">
          <option>Normal</option>
         
        </select>
        
        </div>
       </div>
       <div class="control-group">

       <label class=" control-label"></label>
       <div class="controls">
       		<a href="#" class="btn btn-large btn-primary">Advance Search</a>
            <a href="#" class="btn btn-large ">Clear Filters</a>
       
       </div>
        
        
       </div>
        
  </form>
  </div>
  <div class="span3 pull-right add-filters">
  <div class="control-group">
  <select class="selectpicker pull-right span3">
          <option>Size</option>
          <option>Ketchup</option>
          <option>Relish</option>
        </select>
        </div> 
         <div class="control-group"><select class="selectpicker  pull-right span3">
          <option>Speakers</option>
          <option>Ketchup</option>
          <option>Relish</option>
        </select> 
       </div>
         <div class="control-group"><select class="selectpicker pull-right span3">
         <option>Surround</option>
          <option>Africa</option>
          <option>Canada</option>
        </select>
        </div>
          <div class="control-group"><select class="selectpicker pull-right span3">
         <option>Screen</option>
          <option>Screen Size</option>
          <option>Screen Size 2</option>
        </select>
      </div>
      <div class="control-group row ">
      <a href="#" class="btn-large  span3" style="margin-left: -14px;
text-decoration: underline;">Add another filter</a>
      </div>
</div></div>
</div>
  </div>
</div>

<?php 
//..............................................................................
 	$color_val = rtrim(@$_REQUEST['color_val'], ',');
	$color_val = explode(',', $color_val);
?>


<div class="container">
 <pre><div class="pull-left">Search Result</div><div class="pull-right"><a href="#" id="grid"><img id="gridimg" src="<?php echo base_url();?>assets/ico/tab.png"></a><a href="#" 
 data-role="button"  id="list" ><img src="<?php echo base_url();?>assets/ico/list.png" id="listimg">
</a></div>
</pre>
  <div class="clearfix"></div>
  <ul class="nav nav-search-list ">
  <?php
  
  // code for adding search results
  
 foreach($response->response->docs as $key)
  {
	
  ?>
    
    <li>
  
          <?php
          $img=$key->image;
		  
		  if(isset($img))
		  {
          if(is_array($img))      	  
		  $img1 = $img[0]; 
		  else
		  $img1 = $img;
		  }
		  else
		  $img1 = "";
		  //print_r($img1);
 		  //$img2 = rtrim($img1[0],'/'); 
		  //print_r($img1);
          
		  ?>
  
	<h4>
	  <?php
	  // this code is for adding Product_title
		print substr($key->product_title, 0,50);
		if(strlen($key->product_title) >'50') { print "...."; };
		?>

	</h4>
	
      <div class="well span2 mar-l-0"><img src="<?php echo $img1?>" class="pro-list-img">
	      <span class="active-like">
	      <a href="wishlist.html" rel="tooltip" title="add to wishlist" >
	      <img src="<?php echo base_url();?>assets/ico/wishlist.png"></a>
	      </span>
	      	<dt>
	      		<?php
	      			// code for counts(from how many stores)
	      			if($key->counts == 1)
	      			{
	      				echo "From ".$key->counts." store";
	      			}// if close
	      			if($key->counts > 1)
	      			{
	      				echo "From ".$key->counts." stores";
	      			}// if close
	      			
	      		?>
	      	</dt>
	      	
     </div> 
      
      <dd class="text-center">
      	<div class="clearfix"></div>
            <div class="clearfix"></div>
            
            <p>
            	<?php
            	   // code for fetching min_price and max_price
            	   $min_price = $key->min_price;
            	   $max_price = $key->max_price;
            	   if($min_price != 0 && $max_price != 0)
            	   {
            	   	echo "$ ".$min_price." - "."$ ".$max_price;	
            	   }// if close
            	?>
	   </p>
	   <p>
	     <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php print $key->cluster_id;?>" class="btn btn-large btn-hover btn-alt">Details</a>
	   </p>
      </dd>
      <div class="span9 well pull-right br-0">
        <div class="span6">
          <h3> 	<?php
            	   // code for fetching min_price and max_price
            	   $min_price = $key->min_price;
            	   $max_price = $key->max_price;
            	   if($min_price != 0 && $max_price != 0)
            	   {
            	   	echo "$ ".$min_price." - "."$ ".$max_price;	
            	   }// if close
            	?>
	   </h3>
          <dt>Market Price:</dt>
          <dl>
          	<?php
          		// code for printing market price
          		if($key->price != 0)
          		{
          		    echo "$ ".$key->price;		
          		}// if close
          	?>
          </dl>
          <div class="clearfix"></div>
          <p>
            <?php
            	//code for description
            	$description = $key->description; 
		if(strlen($description) > 100 ) 
		{ 
			print "...."; 
		};
		            	
            ?>
          <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php print $key->cluster_id;?>" class="btn btn-mini btn-primary btn-readmore"  rel="tooltip" title="View Details">Read More</a>
          </p>
          
        </div>
        <span class="span2"> 
        <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php print $key->cluster_id;?>" class="btn btn-mini pull-right store-count">
      		<?php

	      			// code for counts(from how many stores)
	      			if($key->counts == 1)
	      			{
	      			        $countArray = explode(".",$key->counts) ;
	      			        $count = $countArray[0];   
	      				echo "From ".$count." store";
	      			}// if close
	      			if($key->counts > 1)
	      			{
	      				$countArray = explode(".",$key->counts) ;
	      			        $count = $countArray[0];   
	      				echo "From ".$count." stores";
	      			}// if close
	      			
	      		?>
	</a>
        
        <div class="clearfix"></div>
        <ul class="nav stars pull-right">
		
		<?php
		// code for rating
			$count = 0;
				if(isset($rating))
				{
			if($key->rating > 0)
			{
				$r = $key->rating;
				for($i = 0 ; $i < $r && $count!=5; $i++)
				{?>
				  <li><a href="#"  class="active"  ><img src="<?php echo base_url();?>assets/ico/star.png"></a></li>
				<?php	
				$count++;
				}// for close
			}// if close
	}
			if($count <= 5)
				{
					for($i = $count ; $i < 5; $i++)
					{?>
				          <li><a href="#" ><img src="<?php echo base_url();?>assets/ico/star.png"></a></li>
					<?php
					}// for close
				}// if close
	
		?>
        
        </ul>
        </span> 
        </div>
    </li>
  <?php 
  } // for for product search results close
  ?>  
  </ul>
</div>

<?php
// add here pagination script
?>
<div class="container text-center">
<div class="pagination">
  
     <div  class="search_res"><?php print $response->response->numFound; ?> Results for <span><?php echo $keyword?> </span>
        <div style="float:right;"><a href="<?= base_url() ?>index.php/front/advance_search?search=<?= $_REQUEST['search'] ?>&page=<?=$_REQUEST['page']-1?>">
          <?php if(0 < $_REQUEST['page'] ){
          ?>
          Previous&nbsp;
          <?php } ?>
          </a><?php print "Total Page:&nbsp;".ceil($response->response->numFound/24) ?><a href="<?= base_url() ?>index.php/front/advance_search?search=<?= $_REQUEST['search'] ?>&page=<?=$_REQUEST['page']+1?>">
          <?php if(ceil($response->response->numFound/24) >$_REQUEST['page']+1 ){?>
          &nbsp;Next
          <?php }?>
          </a></div>
      </div>

<!--
  <ul>
    <li><a href="#">&lt;</a></li>
    <li><a href="#">1</a></li>
    <li class="active"><a href="#">2</a></li>
    <li><a href="#">3</a></li>
    <li><a href="#">...</a></li>
    <li><a href="#">23</a></li>
    <li><a href="#">&gt;</a></li>
  </ul>
-->

</div>
</div>

<!-- navbar ends -->

<footer class="footer">
  <div class="container">
  <hr>
  <p class="footer-links pull-left mar-btm">Related Search Results: Galaxy S3, MacBook Pro, Apple Time, FOSSIL</p>
  <div class="pull-right">
  <a href="#" class="btn-link btn-mini side-left">Return to top</a>
 <div class="scroll-btn"><a href="#" class=" btn-small  btn "><img src="<?php echo base_url();?>assets/ico/top-arrow.png"></a></div>
  </div>
  <div class="clearfix"></div>
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












































    <?php
    /*
foreach($response->response->docs as $key)
{
	
?>
        <li>
          
          <?php
          $img=$key->image;
		  
		  if(isset($img))
		  {
          if(is_array($img))      	  
		  $img1 = $img[0]; 
		  else
		  $img1 = $img;
		  }
		  else
		  $img1 = "";
		  //print_r($img1);
 		  //$img2 = rtrim($img1[0],'/'); 
		  //print_r($img1);
          
		  ?>
          
          
          
          
		  <div class="product"> <img src=<?php echo $img1; ?> ></div>
          <div class="product_det_box">
            <h4>
              <?php
print substr($key->product_title, 0,50);
if(strlen($key->product_title) >'50') { print "...."; };
?>
            </h4>
            <p>
            
              <?php 
			  print $key->description;
?>
            </p>
 
            <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php echo $key->cluster_id;?>">
            <input type="button" value="Know More"/>
            </a></div>
            <div class="product_price_rag">

            <h4>$ <?php echo $key->min_price;?> to $ <?php echo $key->max_price;?></h4>
            <p>From <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php print $key->id;?>"></a>
	        <?php
			echo $key->storeNumber;
	
} ?>









<?php

// our old code
?>
 
<?php 

//<-------------------------------------------------------------------------------------------------------------->

?>
<!--
<div id="conbody">
<div class="result_side">

<div style="overflow:scroll; height:500px; width:200px; float:left; margin:0 20px 0 0;"><h3>Filters</h3>
-->
<?php
/*
foreach($response->facet_counts as $key =>$a)
		{
			$j=1;
			$count=0;
			foreach($a as $keys=>$b)
			{
				
				$pos = strpos($keys, '[');
				if($pos!==false)
				{
				   if($count==0)
				   {
					  echo "<h5>Price</h5>";
					  
					  echo "<div id='price'>"; 
					  
					  $value=explode(':',$keys);
					  $v=$value[1];
		
					  $id="p$j"."_"."price";
					  if($b==0)
					  echo "<input type='checkbox' id='$id' value='$v' onclick='filterResult(this.id,this.value)' disabled='disabled'/>$v($b)"."<br>";   
					  else
					  echo "<input type='checkbox' id='$id' value='$v' onclick='filterResult(this.id,this.value)'/> $v($b)"."<br>";
					  $count++;
					  $j++;
				   }
				   else
				   {
					  $value=explode(':',$keys);
					  $v=$value[1];
					  $id="p$j"."_"."price";
					  if($b==0)
					  echo "<input type='checkbox' id='$id' value='$v' onclick='filterResult(this.id,this.value)' disabled='disabled'/> $v($b)"."<br>";
					  else
					  echo "<input type='checkbox' id='$id' value='$v' onclick='filterResult(this.id,this.value)'/> $v($b)"."<br>";
					  $j++;
				   }	
				}
				else
				{
				echo"<h5>$keys</h5>";
				if(is_object($b)){
				$i=1;
				foreach($b as $keys2 =>$c)
				{
					if($keys=="brand")
					{
					if($i==1)	
					echo "<div id='brand'>";
					$id="b$i"."_"."brand";
					if($c==0)
					echo "<input type='checkbox' value='$keys2' id='$id' onclick='filterResult(this.id,this.value)' disabled='disabled'/> $keys2($c)"."<br>";
					else
					echo "<input type='checkbox' value='$keys2' id='$id' onclick='filterResult(this.id,this.value)'/> $keys2($c)"."<br>";
					}
					if($keys=="color")
					{
					if($i==1)	
					echo "<div id='color'>";
					$id="c$i"."_"."color";
					if($c==0)
					echo "<input type='checkbox' value='$keys2' id='$id' onclick='filterResult(this.id,this.value)' disabled='disabled'/> $keys2($c)"."<br>";
					else
					echo "<input type='checkbox' value='$keys2' id='$id' onclick='filterResult(this.id,this.value)'/> $keys2($c)"."<br>";
					}
					if($keys=="type")
					{
					if($i==1)	
					echo "<div id='type'>";
					$id="t$i"."_"."type";
					if($c==0)
					echo "<input type='checkbox' value='$keys2' id='$id' onclick='filterResult(this.id,this.value)' disabled='disabled'/> $keys2($c)"."<br>";
					else
					echo "<input type='checkbox' value='$keys2' id='$id' onclick='filterResult(this.id,this.value)'/> $keys2($c)"."<br>";
					}
					
					
					$i++;
				}//for close
				if($i>1)
				{
				echo "<hr>";
				echo "</div>";
				}//if close
				}// if close
				//echo "</div>";
			}//else close
			  
			}
			if($count!=0)
				{
				 echo "<hr>";
				 echo "</div>";
				 $count=0;
				}//if close
			//print_r($a);
		}
//-- ---------------------------------------------------------------------------------------------------------- -->
echo "</div>";
*/
?>
<!--
    <div class="result_list">
      <div  class="search_res"><?php print $response->response->numFound; ?> Results for <span><?php echo $keyword?> </span>
        <div style="float:right;"><a href="<?= base_url() ?>index.php/front/advance_search?search=<?= $_REQUEST['search'] ?>&page=<?=$_REQUEST['page']-1?>">
          <?php if(0 < $_REQUEST['page'] ){?>
          Previous&nbsp;
          <?php } ?>
          </a><?php print "Total Page:&nbsp;".ceil($response->response->numFound/24) ?><a href="<?= base_url() ?>index.php/front/advance_search?search=<?= $_REQUEST['search'] ?>&page=<?=$_REQUEST['page']+1?>">
          <?php if(ceil($response->response->numFound/24) >$_REQUEST['page']+1 ){?>
          &nbsp;Next
          <?php }?>
          </a></div>
      </div>
      -->
      <!--
      <div id="filterResult">
      <ul>
        -->
        <?php
        /*
foreach($response->response->docs as $key)
{
	
?>
        <li>
          
          <?php
          $img=$key->image;
		  
		  if(isset($img))
		  {
          if(is_array($img))      	  
		  $img1 = $img[0]; 
		  else
		  $img1 = $img;
		  }
		  else
		  $img1 = "";
		  //print_r($img1);
 		  //$img2 = rtrim($img1[0],'/'); 
		  //print_r($img1);
          
		  ?>
          
          
          
          
		  <div class="product"> <img src=<?php echo $img1; ?> ></div>
          <div class="product_det_box">
            <h4>
              <?php
print substr($key->product_title, 0,50);
if(strlen($key->product_title) >'50') { print "...."; };
?>
            </h4>
            <p>
            
              <?php 
			  print $key->description;
?>
            </p>
 
            <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php echo $key->cluster_id;?>">
            <input type="button" value="Know More"/>
            </a></div>
            <div class="product_price_rag">

            <h4>$ <?php echo $key->min_price;?> to $ <?php echo $key->max_price;?></h4>
            <p>From <a href="<?php echo base_url();?>index.php/front/pro_detail/<?php print $key->id;?>"></a>
	        <?php
			echo $key->storeNumber;
	
} ?></p></div></li></ul>
      </div>
    </div>
    <!-- added for filter result-->
    


    <?php if(ceil($response->response->numFound/24) !=0) {?>
    <div  class="search_res"><?php print $response->response->numFound; ?> Results for <span><?php echo $keyword?></span>
      <div style="float:right;"><a href="<?= base_url() ?>index.php/advance_search?search=<?= $_REQUEST['search'] ?>&page=<?=$_REQUEST['page']+1?>">
        <?php if(0 < $_REQUEST['page'] ){?>
        Previous&nbsp;
        <?php } ?>
        </a><?php print "Total Page:&nbsp;".ceil($response->response->numFound/24) ?><a href="<?= base_url() ?>index.php/front/advance_search?search=<?= $_REQUEST['search'] ?>&page=<?=$_REQUEST['page']+1?>">
        <?php if(ceil($response->response->numFound/24) >$_REQUEST['page']+1 ){?>
        &nbsp;Next
        <?php }?>
        </a></div>
    </div>
  </div>
  <?php }
  */?>
  
  
<script>

function filterResult(id,val)
 {
	 var filterSplit=id.split("_");
     //alert("reach");
	 var filter=filterSplit[1];
	 var fq=new Array();
	 var i=0;
	 var v;

	 $("#price").children("input:checkbox:checked").each(function() {
	  
	  if(i==0)
	   {	 
        v='price:('+this.value+')';
	    i++;
	   tempV=v;
	   }
	   else
	   {
		   var temp=v.split(')');
		   v=temp[0]+' OR '+this.value+')';
	   }
	   
	   
    });
	if(i>0)
	fq.push(v);
	var i=0;
	$("#color").children("input:checkbox:checked").each(function() {
       if(i==0)
	   {
	   v='color:('+this.value+')';
	   i++;
	   }
	   else
	   {
		   var temp=v.split(')');
		   v=temp[0]+' OR '+this.value+')';
	   }
    });
	if(i>0)
	fq.push(v);

	var i=0;
	$("#type").children("input:checkbox:checked").each(function() {
       if(i==0)
	   {
	   v='type:('+this.value+')';
	   i++;
	   }
	   else
	   {
		   var temp=v.split(')');
		   v=temp[0]+' OR '+this.value+')';
	   }
    });
	if(i>0)
	fq.push(v);
	
	var i=0;
	$("#brand").children("input:checkbox:checked").each(function() {
       if(i==0)
	   {
	   v='brand:('+this.value+')';
	   i++;
	   }
	   else
	   {
		   var temp=v.split(')');
		   //alert(temp[0]);
		   v=temp[0]+' OR '+this.value+')';
	   }
    });
	if(i>0)
	fq.push(v);
	
	 
	 var searchBox;
	 
	 searchBox = window.location.search;
	 var searchText=searchBox.split('search')
	 //alert(searchText[1]);
	 var data='fq='+fq+'&val'+searchText[1];
	 var searchTextStore=searchText[1].split("=");
	 //alert(data);
	//alert(searchTextStore[1]);
	//alert(fq);
	 //var url="<?php echo base_url();?>index.php/front/filter_result/fq";
		/*$.get(url,data,function(data){
			alert('ansu');
			
		alert(data);
		//$("#resultsAfter").html(data);
		});*/
		$.ajax({
		  type: "GET",
		  url: "<?php echo base_url();?>index.php/front/filter_result?fq="+fq+"&search="+searchTextStore[1],
         success: function(data)
		 {
			 //alert(data);
			 $("#filterResult").html(data);
	     }
		});
		
	  
 }
 
</script>
<script>

$(document).ready(function() {
 
    $('#colorpicker-inline').simplecolorpicker('selectColor', '#fbd75b');
	  $('select[name="colorpicker-picker"]').simplecolorpicker({picker: true});

});
</script> 

<script src="<?php echo base_url();?>assets/js/bootstrap-transition.js"></script>
<script src="<?php echo base_url();?>assets/js/bootstrap-tooltip.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-modal.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-dropdown.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-button.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-collapse.js"></script>
<script src="<?php echo base_url();?>assets/js/jquery.simplecolorpicker.js"></script> 
 <script src="<?php echo base_url();?>assets/js/application.js"></script> 
<script src="<?php echo base_url();?>assets/js/custom.js"></script> 
<script src="<?php echo base_url();?>assets/js/bootstrap-select.js"></script>
</body>
</html>
