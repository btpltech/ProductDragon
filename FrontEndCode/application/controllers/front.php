<?php
 class Front extends CI_Controller
 {
   public function _construct()
   {
    parent:: _Controller();
	$this->load->model('ample_model');
	$this->load->library('session'); 
	
   }
     function display()
	 { 
	   $this->load->library('session');
	   $this->load->helper('url');
	   $this->load->helper('form');
	   $this->load->view('front_page');
	   //$this->load->view('footer');
	 }
	 function advance_search()
		{
 
			$this->load->library('session'); 
			$this->load->library('solrphp/Apache/Solr/Apache_Solr_Service.php');
			$key=$_REQUEST['search'];

			$page=$_REQUEST['page'];
			$start =$_REQUEST['page']*24;
			$end = 24;
			//$solr = new Apache_Solr_Service('ec2-54-234-2-40.compute-1.amazonaws.com', 8983, 'solr/');
			$solr = new Apache_Solr_Service('54.225.231.9', 8985, 'solr/');
			 
			/*if(@$_REQUEST['color_val']) {
				//$_REQUEST['color_val'];
					$additionalParameters['fq'] = 'color:'.@rtrim($_REQUEST['color_val'], ',');
			}
 */
                        $additionalParameters['fl'] = '*,score';
			$data['response'] = $solr->search($key, $start, $end, $additionalParameters);
			//print_r($data['response']);
//			print_r($data['response']->response->numFound);
			$data['keyword']= $key;
			$this->load->helper('url');
			$this->load->helper('form');
			//$this->load->view('header');
			$this->load->view('advance_search',$data);
			//$this->load->view('footer');
		}
	 
	 function pro_detail($cluster_id='')
	 {
		$this->load->library('session');
		$this->load->library('solrphp/Apache/Solr/Apache_Solr_Service.php');
		$solr = new Apache_Solr_Service('54.225.231.9', 8985, 'solr/');
	    	$additionalParameters['qf'] = 'cluster_id';
	    		    	$additionalParameters['fl'] = '*';
		$data['response']= $solr->search($cluster_id, 0, 1, $additionalParameters);
       		$this->load->helper('url');
		$this->load->helper('form');
		 
		//print_r($data['response']);
		 
	 
		//$this->load->view('header');
		$this->load->view('prodetail',$data);
		//$this->load->view('footer');
	 }
	 function filter_result()
	 { 
	    	$fq=$_GET['fq'];
		
		$this->load->library('session');
		$this->load->library('solrphp/Apache/Solr/Apache_Solr_Service.php');
		$solr = new Apache_Solr_Service('54.225.231.9', 8985, 'solr/');
		$additionalParameters = $fq;
		$key = $_GET['search'];
		$page=$_REQUEST['page'];
		$start =$_REQUEST['page']*24;
		$end = 24;
			
	    	$data['response']= $solr->search($key, $start, $end,$additionalParameters);
        	//print_r($data['response']);
        	$this->load->helper('url');
		//$this->load->helper('form');
		//$this->load->view('header');
		$this->load->view('filterSolrResult',$data);
		//$this->load->view('footer');
	 }

public function unsets(){
$this->load->library('session'); 
$this->load->helper('url');
$this->session->sess_destroy();
redirect('front/display');
//$this->display();
}
	
 }    
?>
