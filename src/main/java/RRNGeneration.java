public class RRNGeneration
{

	public static void main(String[] args) throws Exception
	{
		RRNGenerator rrn = RRNGeneratorV2Impl.getInstance();
		String rrnNo = rrn.generate();
		
		
		for(int k=0; k<10; k++)
		{	
			if(RRNCheck.doesRRNExist(rrnNo))
			{
				rrnNo = rrn.generate();
				
			}
			else
			{
				break;
			}
		}
	
	
		RRNCheck.putRRN(rrnNo, "");
		System.out.println("RRN : "+rrnNo);
	}

}
