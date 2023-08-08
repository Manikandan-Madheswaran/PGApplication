

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class RRNCheck {
	
	static HashMap hashmap=new HashMap();
	
	public static boolean doesRRNExist(String rrn)
	{
		if(hashmap!=null)
		{
			return hashmap.containsKey(rrn);
		}
		else
		{
			return false;
		}
	}
	
	public static void putRRN(String rrn,String val)
	{
		hashmap.put(rrn, val);
	}
	
	public static void removeRRN(String rrn)
	{
		if(hashmap!=null)
		{
			hashmap.remove(rrn);
		}
	}
	

}
