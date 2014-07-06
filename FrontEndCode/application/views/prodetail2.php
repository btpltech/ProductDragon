<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Details</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link href="<?php echo base_url();?>assets/css/bootstrap.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-responsive.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/bootstrap-select.css" rel="stylesheet">

<link href="<?php echo base_url();?>assets/css/docs.css" rel="stylesheet">
    <link href="<?php echo base_url();?>assets/css/perfect-scrollbar.css" rel="stylesheet">
<link href="<?php echo base_url();?>assets/css/jquery-ui.css" rel="stylesheet">

	<link rel="stylesheet" type="text/css" href="<?php echo base_url();?>assets/source/jquery.fancybox.css?v=2.1.5" media="screen" />

<script type="text/javascript" charset="utf-8" language="javascript" src="<?php echo base_url();?>assets/js/DT_bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="<?php echo base_url();?>assets/css/DT_bootstrap.css">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<script src="<?php echo base_url();?>assets/js/jquery-10.js"></script>
  <script src="<?php echo base_url();?>assets/js/jquery.mousewheel.js"></script>
    <script src="<?php echo base_url();?>assets/js/perfect-scrollbar.js"></script>


<style>
table.table thead .sorting,
table.table thead .sorting_asc,
table.table thead .sorting_desc,
table.table thead .sorting_asc_disabled,
table.table thead .sorting_desc_disabled {
    cursor: pointer;
    *cursor: hand;
}
 
table.table thead .sorting { background: url('images/sort_both.png') no-repeat center right; }
table.table thead .sorting_asc { background: url('images/sort_asc.png') no-repeat center right; }
table.table thead .sorting_desc { background: url('images/sort_desc.png') no-repeat center right; }
 
table.table thead .sorting_asc_disabled { background: url('images/sort_asc_disabled.png') no-repeat center right; }
table.table thead .sorting_desc_disabled { background: url('images/sort_desc_disabled.png') no-repeat center right; }

</style>
<script src="<?php echo base_url();?>assets/js/jquery-ui.js"></script>
<script type="text/javascript" src="<?php echo base_url();?>assets/source/jquery.fancybox.js?v=2.1.5"></script>


<script type="text/javascript">
$(document).ready(function() {
$('.fancybox').fancybox();
//$("#pro").mCustomScrollbar({setHeight: "100px",mouseWheel:false});
});
function change(id)
{
      var src=$('#'+id+'').attr('src');
      //alert(src);
      $('#zoom1').attr('src',src);
      $('#zoomfancy').attr('href',src);

      $('.fancybox').fancybox();
 }
      
	</script>

     <script type="text/javascript">
      $(document).ready(function () {
        $('#pro').perfectScrollbar();
       
      });
    </script>
</head>


	
	
<body style="color:#666666; ">
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
        <li><a href="#">
          <select name="colorpicker-picker">
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
          </select>
          &nbsp;&nbsp;&nbsp;&nbsp;Theme</a></li>
      </ul>
    </div>
  </div>
</div>
<div class=" page-header ">
  <div class="container">
    <div class="row">
      <form action="<?php echo base_url();?>index.php/front/advance_search"> 
<div class="span3 pad-top"><a href="<?php echo base_url();?>index.php" class="head-logo"><img src="<?php echo base_url();?>assets/img/base/logo-orange.png"/></a></div>
      
	<div class="src-bar" align="center">
        <div id="MySearch" class="input-append search">
          <input type="search" placeholder="Search Products..." class="span6" name="search" id="home">
          <script>document.getElementById('home').focus()</script>
          <input type="hidden" name="page" value="0">
	  <button class="btn2 search"><img src="<?php echo base_url();?>assets/ico/search-icon.png"></button>
	  <div class="clearfix"></div>
        <a href="#" class="btn btn-small btn-primary pull-right search-btn" id="adv-btn">Advance Search</a> </div>
</form>
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
<div class="clearfix"></div>
<div class="container">
<?php
        // foreach($response->response->docs as $key)
        {
         //print_r($key->image); 

        }
        ?>
         
  <div class="row">
    <div class="span5 ">
      <div class="thumb-cant">
        <ul class="thumb-list multizoom1 thumbs">
        <?php
           $product_title;
           $description;
           $rating;
           $counts;
           $max_price;
           $min_price;
	   $price;
	   $img;          
	   $img1; 
           //print_r($response);
           
            foreach($response->response->docs as $key)
  	    {
	

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
	   
	   
	   	if($key->product_title !='')
		$product_title = $key->product_title;
		if($key->description !='')
		$description = $key->description;	   
		if($key->rating !='')
	   	$rating = $key->rating;
	   
		if($key->counts !='')
	   	$counts = $key->counts;

	   	if($key->min_price!='')
	           $min_price = $key->min_price;
	        if($key->max_price !='')
            	   $max_price = $key->max_price;
            	if($key->price!='')
            	   $price = $key->price;
            	   
	   }// for close

	    if(isset($img))
	    {
	        
	       if(is_array($img))
	       {
	        for($i = 0;$i<sizeof($img);$i++)
		{
		?>
		<li><a href="javascript:;" ><img width="64" src="<?php echo $img[$i]; ?>" id="<?php echo $i; ?>" onclick="change(this.id)"></a></li>
		<?php
		}
	       }// if close    
	       else
	       {
	       ?>
	       <li><a href="javascript:;" ><img width="64" src="<?php echo $img1; ?>" id="1" onclick="change(this.id)"></a></li>
	       <?php
	       }// else close
	        
	    }
	    
               
           ?>
          
        </ul>
      </div>
      <div class="thumb-big well mar-l-0">
      <img id="zoom1" src="<?php  if(is_array($img))echo $img[0];else echo $img1;?>"/> 
      <span class="active-like"><a href="wishlist.html"  class="icon" rel="tooltip" title="add to wishlist"><img src="<?php echo base_url();?>assets/ico/wishlist.png"></a></span>
        <dl class="dt-opacity" style="display:block"><a href="<?php  if(is_array($img))echo $img[0];else echo $img1;?>" class="fancybox"  id="zoomfancy" data-fancybox-group="gallery">+ Zoom</a></dl>
      </div>
    </div>
    <div class="span7 det-0">
    
      <h4 class="pull-left"><?php
		// code for printing product_title
		if($product_title!='')
		echo $product_title;;	      
	      ?>
     </h4>
      <a class="btn btn-mini pull-right  btn-alt"><?php
	      			
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
      <hr>
      <section style="padding-top:0;">
        <article>
         <ul class="nav stars ">
           <?php
		// code for rating
			$count = 0;
				if(isset($rating))
				{
			if($rating > 0)
			{
				for($i = 0 ; $i < $rating && $count!=5; $i++)
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
        </article>
        <!--<article class="pull-right span2">
<div class="addthis_toolbox addthis_default_style">
<a class="addthis_button_tweet"></a>
<a class="addthis_button_facebook_like" fb:like:layout="button_count"></a>
<a class="addthis_button_pinterest_pinit"></a>
</div>
<script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=undefined"></script>
</article>--> 
      </section>
      <div class="clearfix"></div>
      <section class="br-0">
        <h3><b><?php
            	   // code for fetching min_price and max_price
            	   if(isset($min_price) && isset($max_rice))
            	   {
		    	   if($min_price != 0 && $max_price != 0)
		    	   {
		    	   	echo "$ ".$min_price." - "."$ ".$max_price;	
		    	   }// if close
		    	   
            	   }
            	?></b></h3>
        <dt>Market Price:</dt>
        <dl>
         <?php
         if(isset($price))
         {
			if($price!='')
			echo $price;
	 }
	
		?>
		
        </dl>
      </section>
      <div class="clearfix"></div>
      <p>&nbsp;</p>
      <!--<div class="tabbable">
        <ul class="nav nav-tabs" id="myTab">
          <li class="active"><a data-toggle="tab" href="#des">Description</a></li>
          <li class=""><a data-toggle="tab" href="#pro">Product Details</a></li>
        </ul>
        -->
        <!--<div class="tab-content " id="myTabContent">-->
          <div id="des" class="tab-pane fade active in ">
            <p>
            <?php
            // code for description
            if(isset($description))
            {
            if($description!='')
            echo $description;
            }
            ?>
            </p>
          </div>
          <!--
          <div id="pro" class="tab-pane fade">
          <div>
            <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. Exercitation +1 labore velit, blog sartorial PBR leggings next level wes anderson artisan four loko farm-to-table craft beer twee. Qui photo booth letterpress, commodo enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum PBR. Homo nostrud organic, assumenda labore aesthetic magna delectus mollit. Keytar helvetica VHS salvia yr, vero magna velit sapiente labore stumptown. Vegan fanny pack odio cillum wes anderson 8-bit, sustainable jean shorts beard ut DIY ethical culpa terry richardson biodiesel. Art party scenester stumptown, tumblr butcher vero sint qui sapiente accusamus tattooed echo park.</p>
            </div>
            -->
          <!--</div>-->
         
    </div>
  </div>
  <p>&nbsp;</p>
  <div class="row">
    <div class="span12 overflow">
      <table class="table table-striped table-bordered" id="example">
        <thead>
        <th>Store Name</th>
          <th>Store Rating<span><a href="#" class="icon icon-top"><img src="<?php echo base_url();?>assets/ico/to-arow.png"></a> <a href="#"class="icon icon-bottom"><img src="<?php echo base_url();?>assets/ico/btnm-arow.png"></a></span></th>
          <th>Country</th>
          <th>Condition</th>
          <th>Price<span><a href="#" class="icon icon-top"><img src="<?php echo base_url();?>assets/ico/to-arow.png"></a> <a href="#"class="icon icon-bottom"><img src="<?php echo base_url();?>assets/ico/btnm-arow.png"></a></span></th>
          <th>Shipping Charges<span><a href="#" class="icon icon-top"><img src="<?php echo base_url();?>assets/ico/to-arow.png"></a> <a href="#"class="icon icon-bottom"><img src="<?php echo base_url();?>assets/ico/btnm-arow.png"></a></span></th>
          <th>Total Price:<span><a href="#" class="icon icon-top"><img src="<?php echo base_url();?>assets/ico/to-arow.png"></a> <a href="#"class="icon icon-bottom"><img src="<?php echo base_url();?>assets/ico/btnm-arow.png"></a></span></th>
            </thead>
        <tbody>
  <?php
       
$storeNameData;

foreach($response->response->docs as $key) 
{
 $storeNameData = $key->storeName;
}

     
     $storeNameData = json_encode($storeNameData);
     //print_r($storeNameData);
     
     if(!isset($storeNameData))
      {
	    foreach($storeNameData as $key->$Value)
	    {
	       ?>
		  <tr>
		    <td>
		     <a href="#">
		        <?php
                         // code for storeName
                         echo $key;
                         ?>
                     </a>
		    </td>
		    <td><ul class="rating-star">
		  <?php
		  // code for rating
		  $rating1;
		  $shippingCharge;
		  foreach($Value as $key1->$val1)
		  {
		    if($key1 == "shipping_charges")
		    $shippingCharges = $val1;
		    if($key1 == "rating")
		    $rating1 = $val1;
		  }// for close
		   if(isset($rating1))
	           {
			if($rating1 > 0)
			{
				for($i = 0 ; $i < $rating1 && $count!=5; $i++)
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
         
		      </ul></td>
		    <td>India</td>
		    <td>New</td>
		    <td>
		     <?php
		    // code for price
		    	if($price!='')
		    	echo "$".$price;
		    	?>
		    	    
            	    </td>
		    <td>
		    <?php
		    if(isset($shippingCharges))
		    echo $shippingCharges;
		    ?>
		    </td>
		    <td>
		    <?php
		     if(isset($shippingCharges) && isset($price))
		     {
		      echo $shippingCharges+$price;
		     }// if close
		    ?>
		    </td>
		  </tr>
		   
             <?php
             }// main for close
      }// main if close
    
       ?>
        </tbody>
      </table>
    </div>
  </div>
</div>
<div class="container text-center">
  <div class="pagination">
    <ul>
    <!--
      <li><a href="#">&lt;</a></li>
      <li><a href="#">1</a></li>
      <li class="active"><a href="#">2</a></li>
      <li><a href="#">3</a></li>
      <li><a href="#">...</a></li>
      <li><a href="#">23</a></li>
      <li><a href="#">&gt;</a></li>
    -->
    </ul>
  </div>
</div>
<div class="container thumb-gallery">
  <pre><div class="pull-left">Related Product</div><div class="pull-right"><a class="btn btn-alt btn-link btn-mini" id="btn-hide">- Hide / + Show</a></div>
</pre>
  <div class="row" id="sh-hd">
    <div class="span2 text-center thumb thumb">
      <h4>Mac Book Pro 15'</h4>
      <div class="well down-link"><img src="<?php echo base_url();?>assets/img/products-img/lappy.jpg"><span class=""><a href="wishlist.html"  class="icon icon-heart"></a></span> </div>
      <a href="#" class="btn btn-primary btn-alt btn-large d-btn">Details</a> </div>
    <div class="span2 text-center thumb">
      <h4>Mac Book Pro 15'</h4>
      <div class="well down-link"><img src="<?php echo base_url();?>assets/img/products-img/lappy.jpg"><span class=""><a href="wishlist.html"  class="icon icon-heart"></a></span> </div>
      <a href="#" class="btn btn-primary btn-alt btn-large d-btn">Details</a> </div>
    <div class="span2 text-center thumb">
      <h4>Mac Book Pro 15'</h4>
      <div class="well down-link"><img src="<?php echo base_url();?>assets/img/products-img/lappy.jpg"><span class=""><a href="wishlist.html"  class="icon icon-heart"></a></span> </div>
      <a href="#" class="btn btn-primary btn-alt btn-large d-btn">Details</a> </div>
    <div class="span2 text-center thumb">
      <h4>Mac Book Pro 15'</h4>
      <div class="well down-link"><img src="<?php echo base_url();?>assets/img/products-img/lappy.jpg"><span class=""><a href="wishlist.html"  class="icon icon-heart"></a></span> </div>
      <a href="#" class="btn btn-primary btn-alt btn-large d-btn">Details</a> </div>
    <div class="span2 text-center thumb">
      <h4>Mac Book Pro 15'</h4>
      <div class="well down-link"><img src="<?php echo base_url();?>assets/img/products-img/lappy.jpg"><span class=""><a href="wishlist.html"  class="icon icon-heart"></a></span> </div>
      <a href="#" class="btn btn-primary btn-alt btn-large d-btn">Details</a> </div>
    <div class="span2 text-center thumb">
      <h4>Mac Book Pro 15'</h4>
      <div class="well down-link"><img src="<?php echo base_url();?>assets/img/products-img/lappy.jpg"><span class=""><a href="wishlist.html"  class="icon icon-heart"></a></span> </div>
      <a href="#" class="btn btn-primary btn-alt btn-large d-btn">Details</a> </div>
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
<script>

$(document).ready(function() {
 
    $('#colorpicker-inline').simplecolorpicker('selectColor', '#fbd75b');
	  $('select[name="colorpicker-picker"]').simplecolorpicker({picker: true});
	  

});

</script> 


<script src="<?php echo base_url();?>assets/js/bootstrap-transition.js"></script>
<script src="<?php echo base_url();?>assets/js/bootstrap-tooltip.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-modal.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-tab.js"></script> 
    <script src="<?php echo base_url();?>assets/js/bootstrap-dropdown.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-button.js"></script>
    <script src="<?php echo base_url();?>assets/js/bootstrap-collapse.js"></script>
  
    
<script src="<?php echo base_url();?>assets/js/jquery.simplecolorpicker.js"></script> 
 <script src="<?php echo base_url();?>assets/js/application.js"></script> 
<script src="<?php echo base_url();?>assets/js/custom.js"></script> 
<script src="<?php echo base_url();?>assets/js/bootstrap-select.js"></script>
</body>
</html>
