 if(isset($response->response->docs->storeName))
       {
       $storeNameData = $response->response->docs->storeName;
       
       
	    foreach($storeNameData as $key=>$Value)
	    {
	       
	       ?>
		  <tr>
		    <td><a href="#">
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
		  for($Value as $key1=>$val1)
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
		  </tr>
	       <?php
	       }// for close
	 }// if close
