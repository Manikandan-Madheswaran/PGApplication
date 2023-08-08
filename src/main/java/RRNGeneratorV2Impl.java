

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Calendar;

import com.merchant.demo.webservice.utils.ConfigFileUtil;

public class RRNGeneratorV2Impl implements RRNGenerator{

	private static RRNGenerator rrnGenerator;
	private static Object initLock = new Object();
	private File file;
	private int currentLocalVal;
	private int _5thCharVal = 0;
	private int previousDayOfyear;
	private static File directory;

	private RRNGeneratorV2Impl(String filePath) {
		try {
			this.file = new File(filePath);
			System.out.println("localhost rrnv2 :::::::::::::::"+Inet4Address.getLocalHost().getHostAddress());
			//_5thCharVal = Integer.parseInt(ConfigFileUtil.getProperty("RRN_"+Inet4Address.getLocalHost().getHostAddress()+"_"+System.getProperty("weblogic.Name")));
			//_5thCharVal = Integer.parseInt(ConfigFileUtil.getProperty("RRN_"+Inet4Address.getLocalHost().getHostAddress()+"_"+"MS1"));
			_5thCharVal = 56;
			if (file.exists()) {
				String[] val = readRRNFromFile().split(",");
				currentLocalVal = Integer.parseInt(val[1].trim());
				this.previousDayOfyear = Integer.parseInt(val[0].trim());
			} else {
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("Error in RRNGeneratorV2Impl :"+e.getMessage());
		}
	}

	public String generate() {
		StringBuilder rrn = new StringBuilder();
		Calendar current = Calendar.getInstance();
		/**Hardcoded for Testing purpose - 13 digit RRN issue -start **/
		/*String rrnDate = ConfigFileUtil.getProperty("RRN_DATE");
		Pattern pat = Pattern.compile("[-]");
		String[] strs = pat.split(rrnDate);
		current.set(Calendar.DATE, Integer.parseInt(strs[0]));
		current.set(Calendar.MONTH, Integer.parseInt(strs[1])-1);
		current.set(Calendar.YEAR, Integer.parseInt(strs[2]));*/
		/**Hardcoded for Testing purpose - 13 digit RRN issue - ends **/
		int currentDayOfYear = current.get(Calendar.DAY_OF_YEAR);
		rrn.append(current.get(Calendar.YEAR) % 10);
		rrn.append(String.format("%03d", currentDayOfYear));
		rrn.append(_5thCharVal);
		if(_5thCharVal > 9)
			rrn.append(String.format("%06d", getCurrentValue(currentDayOfYear)));
		else
			rrn.append(String.format("%07d", getCurrentValue(currentDayOfYear)));
		return rrn.toString();
	}

	private synchronized int getCurrentValue(int currentDay) {
		if(currentDay > this.previousDayOfyear) {
			this.currentLocalVal = 0;
			this.previousDayOfyear = currentDay;
		}
		/***Added for RRN Generation issue : starts ***/
		else if(currentDay < this.previousDayOfyear) {
			this.currentLocalVal = 0;
			this.previousDayOfyear = currentDay;
		}
     	/***Added for RRN Generation issue : end ***/
		int tempCurrentVal = currentLocalVal;
		try {
			addRRNInFile(currentDay+","+(++currentLocalVal));
		} catch (IOException e) {
			throw new RuntimeException("RRN Generation Erorr");
		}

		return tempCurrentVal;
	}

	private String readRRNFromFile() throws IOException {
		FileInputStream fin = null;
		DataInputStream dis = null;
		try {
			fin = new FileInputStream(file);
			dis = new DataInputStream(fin);
			return dis.readUTF();
		} finally {
			try {
				if (dis != null) 
					dis.close();
				if (fin != null)
					fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addRRNInFile(String val) throws IOException {
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try {
			fos = new FileOutputStream(file, false);
			dos = new DataOutputStream(fos);
			dos.writeUTF(val);
		} finally {
			if (dos != null)
				dos.close();
			if (fos != null)
				fos.close();
		}
	}

	public static RRNGenerator getInstance() throws Exception {
		synchronized (initLock) {
			if (null == rrnGenerator) {
				String host = Inet4Address.getLocalHost().getHostAddress();
				//StringBuffer filePath = new StringBuffer(ConfigFileUtil.getProperty("RRNFILEPATH")+File.separator+host.substring(host.lastIndexOf(".")+1)+"_"+System.getProperty("weblogic.Name"));
				StringBuffer filePath = new StringBuffer(ConfigFileUtil.getProperty("RRNFILEPATH")+File.separator+host.substring(host.lastIndexOf(".")+1)+"_"+"MS1");
				directory = new File(filePath.toString());
				if(!directory.exists())
					directory.mkdir();
				filePath.append(File.separator+"RRNGen.txt");
				rrnGenerator = new RRNGeneratorV2Impl(filePath.toString());
			}
		}
		return rrnGenerator;
	}
}