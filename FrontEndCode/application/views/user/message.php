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

  <div id="conbody">
<h2 align="center" style="color:#FF6600">Record Added Successfully</h2> 
</div>
</div>
</div>

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
