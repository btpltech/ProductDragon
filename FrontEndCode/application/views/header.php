
<header>
    <div class="con">
      <div class="logo"><a href="<?php echo base_url();?>"><img src="<?php echo base_url();?>assets/images/logo.png"></a></div>
      <div class="adv">
	  <form action="<?php echo base_url();?>index.php/front/advance_search">
	 <div class="input_box"><div class="fdbx"></div>
     <input type="hidden" name="page" value="0" />
     <input type="text" value="" placeholder= "Search Product" id="home" name="search" value="<?php if(isset($_GET['search'])) echo $_GET['search']; ?>" style="background:none; width:75%; float:left; border:none;"/><script>document.getElementById('home').focus()</script> <button class="search_button"></button></div>
	   <!-- <input type="submit" id="search" value="Search"/>-->
	</form>
          </div>
        </div>
      </div>
    </div>
</header>
