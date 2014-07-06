package GroupingScript;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
public class TaskStatus {
	
	
public static void WriteInAmpleClusterTaskStatus(String tableName,String family,ArrayList<String> listOfClusterNo,String qualifier,String taskValue,String masterIp)
{

	Configuration conf = new Configuration();
	conf = HBaseConfiguration.create();
	conf.set("hbase.zookeeper.quorum", masterIp);
	conf.set("hbase.zookeeper.property.clientPort", "2181");
	conf.set("hbase.master", masterIp + ":60000");

	HTable table = null;
	try {
		table = new HTable(conf, tableName);
	} catch (IOException e1) {
		 
		e1.printStackTrace();
	}
	try {
		List<Put> listOfPut = new ArrayList<Put>();
        for (int i = 0;i<listOfClusterNo.size(); i++) {
            String hash = listOfClusterNo.get(i);
			Put put = new Put(Bytes.toBytes(hash));
			put.add(Bytes.toBytes("cf"), Bytes.toBytes("task"),
					Bytes.toBytes("notDone"));
			listOfPut.add(put);
		}// for close
		table.put(listOfPut);
		table.close();
	} catch (IOException e) {
		e.printStackTrace();
	}

}// fn close

public static void WriteInHashContentTaskStatus(String tableName,String family,String rowKey,String qualifier,String taskValue,String arg) {
	Configuration conf = null;
	conf = HBaseConfiguration.create();
	conf.set("hbase.zookeeper.quorum", arg);
	conf.set("hbase.zookeeper.property.clientPort", "2181");
	conf.set("hbase.master", arg + ":60000");
	try {
		HTable table = new HTable(conf, tableName);
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
				Bytes.toBytes(taskValue));
		table.put(put);
		table.close();
		// System.out.println("Record Added to hbase");
	} catch (IOException e) {
		e.printStackTrace();
	}
}

public static Boolean FindStatusOfHashContentTaskStatus(String tableName,String family,String rowKey,String qualifier,String taskValue,String arg) throws IOException {
	
	Boolean status = false;
	Configuration conf = null;
	conf = HBaseConfiguration.create();
	conf.set("hbase.zookeeper.quorum", arg);
	conf.set("hbase.zookeeper.property.clientPort", "2181");
	conf.set("hbase.master", arg + ":60000");
	HTable table = new HTable(conf, tableName);
	Get get = new Get(rowKey.getBytes());
	//get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
	Result rs = table.get(get);
	
	for (KeyValue kv : rs.raw()) {
		// System.out.print(new String(kv.getRow()) + " " );
		// System.out.print(new String(kv.getFamily()) + ":" );
		// System.out.print(new String(kv.getQualifier()) + " " );
		// System.out.print(kv.getTimestamp() + " " );
		
		
		String temp = new String(kv.getValue());
		if(temp.equals(taskValue))
			status = true;
		
	}// for close
	
	table.close();
	return status;
}

public static Boolean FindStatusOfAmpleClusterStatus(String tableName,String arg) {
	Map<String,String> listMap = new HashMap<String,String>();

	Boolean status = true;
	Configuration conf = null;
	conf = HBaseConfiguration.create();
	conf.set("hbase.zookeeper.quorum", arg);
	conf.set("hbase.zookeeper.property.clientPort", "2181");
	conf.set("hbase.master", arg + ":60000");
	try {
		HTable table = new HTable(conf, tableName);
		Scan s = new Scan();
		ResultScanner ss = table.getScanner(s);
		for (Result r : ss) {
			for (KeyValue kv : r.raw()) {
				String row;
				row = new String(kv.getRow());
				String data = new String(kv.getValue());
	
				listMap.put(row, data);
				break;
			}
			// System.out.println("---------------------");
		}
		table.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
	for(Map.Entry<String, String> item : listMap.entrySet())
	{
		String check = item.getValue();
		if(!check.equals("done"))
		{
			status = false;
			break;
		}
	}// for close

	return status;
}// fn close
}
