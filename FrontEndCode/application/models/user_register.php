<?php
  class user_register extends CI_Model
  {
   public function __construct()
   {
    parent::__construct();
	$this->load->database();
   }
   
   /* function for saving login data*/
    
	public function insert_record($user_name,$password,$email,$first_name,$last_name)
	{
	
	 $data = array(
	               'user_name'=>$user_name,
				   'password'=>$password,
				    'email'=>$email,
				   );
	$this->db->insert('user_login_data',$data);		
	
	/*Getting the userid of the user*/
	
		$this->db->where('email',$email);
		$query2 = $this->db->get('user_login_data');
		$details2= $query2->result_array();
		//print_r($details2);
		$id=$details2['0']['user_no'];
	
	/*Putting the user id with required details*/
	
	$data3= array(
					'user_no'=>$id,
	              'first_name'=>$first_name,
				   'last_name'=>$last_name,
				   'mobile'=>'',
				    'land_no'=>'',
					'gender'=>'',
					);
	$this->db->insert('user_details',$data3);		
	return $id;
	}
	
	public function get_detail($email,$password)
	{
	 $this->db->where('email',$email);
	 $this->db->where('password',$password);
	 $query = $this->db->get('user_login_data');
	 $details= $query->result_array();
	 if($query->num_rows()==1)
	 {
	  return $details;
	 }
	}
	
	/*function to save user details*/
	
	public function user_details($f_name,$l_name,$mobile_no,$land_line_no,$gender,$id)
	{
	 $data= array(
	              'first_name'=>$f_name,
				   'last_name'=>$l_name,
				   'mobile'=>$mobile_no,
				    'land_no'=>$land_line_no,
					'gender'=>$gender,
					);
	$this->db->where('user_no',$id);
	$this->db->update('user_details',$data);				
	}
	
	/*function for wishlist data*/
	public function get_wish($email)
	{
	 $this->db->where('email',$email);
	 $query = $this->db->get('wish_list');
	 $details= $query->result_array();
	
	  return $details;
	 
	}
	/* function for adding wishlist*/
	public function add_wish($product_id,$email)
	{
	 $data= array(
	              'product_id'=>$product_id,
				   'email'=>$email,
					);
	$this->db->insert('wish_list',$data);				
	
	}   
  }
?>