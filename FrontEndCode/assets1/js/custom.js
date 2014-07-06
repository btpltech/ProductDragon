// JavaScript Document



			
			
	    	$(document).ready(function() {

            	$('.slider').slider({
            		min: 5,
            		max: 1000,
            		step: 5,
            		value: [5,200]
            	});

            	$('.tooool').tooltip();

	    	});
			
			
			 window.onload=function()
       {
      $('.selectpicker').selectpicker();

         prettyPrint();
      };
      
			

//             color palate js   ///
/**
 * bootstrap-colorpalette.js
 * (c) 2013~ Jiung Kang
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

	$(' .nav-search-list dd').hide();
	$(' .mar-l-0 dt').hide();
 $("#grid").click(function(){
	 $("#gridimg").attr("src","../../assets/ico/tab_acrion.png");
	 $('#listimg').attr("src","../../assets/ico/list.png");
 $('#list').css('color','#ffffff');
  $('#grid').css('color','#555555');
 	$('.br-0').hide();
	$(' .mar-l-0 dt').show();
	$(' .nav-search-list dd').show();
	$('.nav-search-list li').addClass('span2-5');
  $(' .nav-search-list li').css("overflow","visible");
	$('.nav-search-list li h4').css("fontWeight","lighter");
    $('.nav-search-list li h4').css("fontSize","18.18px");
     $('.mar-l-0').css("width","190px" );
      $('.mar-l-0').css("height","207px" );
       $('.mar-l-0').css("float","none" );
        $('.mar-l-0').css("margin","0 auto" );


});


 $("#list").click(function(){
$('#grid').css('color','#ffffff');
$('#listimg').attr("src","../../assets/ico/list_acrion.png");
 $("#gridimg").attr("src","../../assets/ico/tab.png");
  $('#list').css('color','#555555');
 	$('.br-0').show();
	$(' .mar-l-0 dt').hide();
	$(' .nav-search-list dd').hide();
	$('.nav-search-list li').removeClass('span2-5');
	$('.nav-search-list li h4').css("fontWeight","bold");
  $('.nav-search-list li h4').css("fontSize","22px");
    $('.mar-l-0').css("float","left" );
      $(' .nav-search-list li').css("overflow","hidden");
      $('.mar-l-0').css("margin","0" );
       $('.mar-l-0').css("width","190px" );
      $('.mar-l-0').css("height","188px" );
});



    $("#adv-btn").click(function(){
    	$("#adv-search").slideToggle();
  });


     $("#close").click(function(){
    	$("#adv-search").slideToggle();
  });
       $("#btn-hide").click(function(){
      $("#sh-hd").slideToggle();
  });

   
   $("a").tooltip({
                  'selector': '',
                  'placement': 'top'
                });



   
            $(window).scroll(function(){
            if ($(this).scrollTop() > 100) {
                $('.scroll-btn a').fadeIn();
            } else {
                $('.scroll-btn a').fadeOut();
            }
        });
 
        $('.scroll-btn a').click(function(){
            $("html, body").animate({ scrollTop: 0 }, 600);
            return false;
        });     

        


$( "#slider-range" ).slider({
range: true,
min: 0,
max: 500,
values: [ 75, 300 ],
slide: function( event, ui ) {
$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
}
});
$( "#amount" ).val( "$" + $( "#slider-range" ).slider( "values", 0 ) +
" - $" + $( "#slider-range" ).slider( "values", 1 ) );


