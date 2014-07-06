<footer>
<div class="footer">
<div class="footer_logo"><a href="<?php echo base_url();?>"><img src="<?php echo base_url();?>assets/images/footer.png"></a></div>
<div class="footer_text">All right reserved /<b> Our Privacy Policy </b> /<b> Our term of use </b></div>
<div class="footer_menu"><p><a href="#">Contact Us</a> |  <a href="<?php echo base_url();?>">Home</a> | <a href="<?php echo base_url();?>index.php/register/call_account">Account</a>| <a href="<?php echo base_url();?>index.php/register/wish_list/wish">Wishlist</a> | 
<?php
$this->load->library('session'); 
if(!$this->session->userdata('username'))
{
?>
 <a href="<?php echo base_url();?>index.php/register/login">Sign In</a> |  <a href="<?php echo base_url();?>index.php/register/sign_up">Sign up</a></p>
<?php
 }
else
{
?>
<a href="<?php echo base_url();?>index.php/front/unsets">Logout</a>
<?php
}
 ?>
 </div>
<div></div>
</div></footer>