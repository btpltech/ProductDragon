<?php
class Ample_model extends CI_Model
 {
  function _construct()
  {
   parent:: _Model();
   $this->load->database();
  
  }
     function item_details($ser_detail='')
	 {
		 
		 foreach($ser_detail as $key) {
			$this->load->database();
			$this->db->where('id',$key['product_id']);
			$query = $this->db->get('data');
			$details[] = $query->result_array();
		 }
		return $details;
	  
	 }
	 
	 public function product_details($productid)
	 {
	 $this->load->database();
	 $this->db->where('id',$productid);
	 $query = $this->db->get('data');
	 $details= $query->result_array();
	 return $details;
	 }



	 public function multi_product_details($cluster_id)
	 {
		 $this->load->database();
		 $this->db->where('cluster_id',$cluster_id);
		 $this->db->group_by("host"); 
		 $query = $this->db->get('clustering');
		 $details= $query->result_array();
		 return $details;
	 }

	 
	 /*function to get descriptio*/
	 public function get_description($prod_id)
	{
	 $this->db->where('id',$prod_id);
	 $query = $this->db->get('descr');
	 $details= $query->result_array();
	 return $details;
	}
 }

?>
