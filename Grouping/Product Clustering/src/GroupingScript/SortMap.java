package GroupingScript;



import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SortMap {

	static class ValueComparator implements Comparator<String> {

		Map<String, Integer> base;

		ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		@Override
		public int compare(String a, String b) {
			Integer x = base.get(a);
			Integer y = base.get(b);
			if (x.equals(y)) {
				return a.compareTo(b);
			}
			return x.compareTo(y);
		}
	}

	public static void main(String[] args) {

		Map<String,String>  MapForSoring = new HashMap<String,String>();
		MapForSoring.put("a", "19");
		MapForSoring.put("b", "19");
		MapForSoring.put("c", "1");
		
		 Map<String,Integer>  tempMapForSoring = new HashMap<String,Integer>();
		 Map<String,String>  tempMapForSoring_1 = new HashMap<String,String>();
		 
		 // conversion
		 for(Map.Entry<String, String> item : MapForSoring.entrySet())
		 {
			 tempMapForSoring.put(item.getKey(), Integer.parseInt(item.getValue()));
		 }// for close
		 
		 
		 // call function to sort
		 
		    HashMap<String, Integer> map = new HashMap<String, Integer>();
			ValueComparator vc = new ValueComparator(map);
			TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(vc);
			
		for(Map.Entry<String, Integer> item  : tempMapForSoring.entrySet()){
			map.put(item.getKey(), item.getValue());
		}
			sorted.putAll(map);
			System.out.println(sorted);
			
			
			for(Entry<String, Integer> item: sorted.entrySet())
			{
				//System.out.println(item.getKey());
				System.out.println(item.getValue());
				
			}
			
		 
		
//System.out.println(tempMapForSoring_1);

	}
}