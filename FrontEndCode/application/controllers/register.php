<?php 
 class Register extends CI_controller
 {
 
  public function __construct()
  {
   		parent::__construct();
		$this->load->helper('url');
		$this->load->helper('form');
		$this->load->model('user_register');
		$this->load->library('session'); 
  }
  
  /*function for user login*/
  
  public function login()
  {
		$this->load->library('session'); 
		$this->load->helper('url');
		$this->load->helper('form');
		$this->load->library('solrphp/Apache/Solr/Apache_Solr_Service.php');
		//$this->load->view('header');
		$this->load->view('user/sign_in');
		$this->load->view('footer');
  }
  
  /*function to show logged user profile*/
  
  public function show_profile()
  {
		$email=$this->session->userdata('email');
		if($email)
		{ 
		$this->load->helper('url');
		$this->load->helper('form');
		$this->load->library('solrphp/Apache/Solr/Apache_Solr_Service.php');
		$this->load->view('header');
		$this->load->view('user/account');
		$this->load->view('footer');
		}
		else
		{
		//$this->load->view('header');
		$this->load->view('user/sign_in');
		$this->load->view('footer');
		}
  }
  
  /*function to create user */
  
  public function create_user()
  {
  		$this->load->helper('url');
		$this->load->helper('form');
		//$this->session->set_userdata('userid',$user_id);
		$this->load->library('session');
		$user_name=$this->input->post('name_user');
		$password = $this->input->post('log_password');
		$first_name = $this->input->post('first_name');
		$last_name = $this->input->post('last_name');
		$email = $this->input->post('log_email');
		//print $user_name;
		$this->session->set_userdata('username',$user_name);
		$this->session->set_userdata('email',$email);
		
		if($this->input->post('submit'))
		{
		 $user_id=$this->user_register->insert_record($user_name,$password,$email,$first_name,$last_name);
		 $this->session->set_userdata('userid',$user_id);
		 $this->load->view('header');
		 $this->load->view('user/account');
		 $this->load->view('footer');
		}
		else{
		  $this->load->view('user/error');
		  }
  }
  
    /*function for login of user*/
	
	public function check_user($product_id="")
	{
		 
	    $this->load->helper('url');
		$this->load->helper('form');
		$this->load->library('session');
		$email = $this->input->post('email_log');
		$password = $this->input->post('password_log');
				if($this->input->post('submit'))
				{
				$log_values=$this->user_register->get_detail($email,$password);
				//print_r($log_values);
				$user_id=($log_values['0']['user_no']);
				$user_name=($log_values['0']['user_name']);
							if($log_values)
							{
							$this->session->set_userdata('userid',$user_id);
							$this->session->set_userdata('username',$user_name);
							$this->session->set_userdata('email',$email);
									
								$email=$this->session->userdata('email');
								if($email)
								{ 	
									if($product_id==""){
									$this->load->view('header');
									$this->load->view('user/account');
									$this->load->view('footer');
									}
									else
									$this->adding_wish($product_id);
								}
							  else
							  {
							  //$this->load->view('header');
							  $this->load->view('user/sign_in');
							  $this->load->view('footer');
							  }
							}
							else
							{
							//$this->load->view('header');
							$this->load->view('user/sign_in');
							$this->load->view('footer');
							}
				}
				else{
					$email=$this->session->userdata('email');
					if($email)
					{ 
				
						if($product_id==""){
						$this->load->view('header');
						$this->load->view('user/account');
						$this->load->view('footer');
						}
						else
						$this->adding_wish($product_id);
					}
				  else
				  {
				  //$this->load->view('header');
				  $this->load->view('user/sign_in');
				  $this->load->view('footer');
				  }
				}
	}
	
	/*function for sign_up*/
	
	public function sign_up()
	{
		//$this->load->view('header');
	   $this->load->view('user/sign_up');
	   $this->load->view('footer');
	}
	
	/*function for save user_details*/
	
	 public function save_user_details()
	{
		$email=$this->session->userdata('email');
		if($email)
		{ 
	    $this->load->helper('url');
		$this->load->helper('form');
		$id=$this->session->userdata('userid');
		$f_name=$this->input->post('first_name');
		$l_name=$this->input->post('last_name');
		$mobile_no=$this->input->post('mobile_no');
		$land_line_no=$this->input->post('land_line_no');
		$gender=$this->input->post('gender');
		if($this->input->post('submit'))
		{
		$this->user_register->user_details($f_name,$l_name,$mobile_no,$land_line_no,$gender,$id);
		}
		$this->load->view('header');
		$this->load->view('user/account');
		$this->load->view('footer');
		}
	  else
	  {
	  //$this->load->view('header');
	  $this->load->view('user/sign_in');
	  $this->load->view('footer');
	  }
	  }
	   
	
	  /*function for wishlist*/
	  
	  public function wish_list()
	  {
	  $this->load->helper('url');
	  $this->load->helper('form');
	  $this->load->library('session');
	  $user_name=$this->session->userdata('username');
	  $email=$this->session->userdata('email');
	  if($user_name)
	  {  
	  $this->load->model('user_register');
	  $data['wish']=$this->user_register->get_wish($email);
	  $this->load->view('header');
	  $this->load->view('user/wishlist',$data);
	  $this->load->view('footer');
	  }
	  else
	  {
	  //$this->load->view('header');
	  $this->load->view('user/sign_in');
	  $this->load->view('footer');
	  }
	  }
       
	   /*function for profile*/
	   
	   public function profile_show()
	   {
	   $this->load->view('header');
	   $this->load->view('user/profile');
	   $this->load->view('footer');
	   }
         
		 
		 /*function for adding wish */
		 
		 public function adding_wish($product_id)
		 {
		 $this->load->helper('url');
		 $this->load->helper('form');
		 $this->load->library('session');
	     $email=$this->session->userdata('email');
		 $user_name=$this->session->userdata('username');
		 if($email)
		 {
		 $this->user_register->add_wish($product_id,$email);
		 
		 $this->load->model('user_register');
	     $data['wish']=$this->user_register->get_wish($email);
		 $this->load->view('header');
		 $this->load->view('user/wishlist',$data);
		 $this->load->view('footer');
		 }
		 else{
		 $data['product_id']=$product_id;
		 //$this->load->view('header');
		 $this->load->view('user/sign_in',$data);
		 $this->load->view('footer');
		 }
		 }
		 
		 /*function for personal details*/
		 
		 public function show_info_form()
		 {
		 $this->load->view('header');
		 $this->load->view('user/account');
		 $this->load->view('footer');
		 }
		 /*function to call account page*/
		 
		 public function call_account()
		 {
		 $this->load->library('session'); 
		 if($this->session->userdata('email')){
		 $this->load->view('header');
		 $this->load->view('user/account');
		 $this->load->view('footer');
		 }
		 else{
		 //$this->load->view('header');
		 $this->load->view('user/sign_in');
		 $this->load->view('footer');
		 }
		 }
		 
	 
  
 }
?>