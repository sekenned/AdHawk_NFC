package edu.cmu.ini.adhawks.nfcreader.parser;

public class FormatConverter {

	public static String hexToString(String hex)
	{	
		int decimal = 0;
		StringBuilder buffer = new StringBuilder();
		 
		for( int i=0; i<hex.length()-1; i+=2 )
		{
			//grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			try{
				//convert hex to decimal
				decimal = Integer.parseInt(output, 16);
				//convert the decimal to character
				buffer.append((char)decimal);				
			} catch(Exception nfe) { }
		}
		 
		return buffer.toString();
	}
	
	public static String hexToDecimal(String hex)
	{	  
		StringBuilder buffer = new StringBuilder();
		int decimal = 0;
		
		for( int i=0; i<hex.length()-1; i+=2 )
		{
			//grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			
			try{
				//convert hex to decimal
				decimal = Integer.parseInt(output, 16);
				//convert the decimal to character
				buffer.append(decimal);				
			} catch(Exception nfe) { }
		}
		 
		return buffer.toString();
	}	
	
	public static String byteArrayToHexString(byte[] byteArray) 
	{
		StringBuffer buffer = new StringBuffer(byteArray.length * 2);
		
		for (int i = 0; i < byteArray.length; i++)
		{
			//& is a bitwise AND operation 
			int value = byteArray[i] & 0xff;
			if (value < 16) 
			{
				buffer.append('0');
			}
			
			buffer.append(Integer.toHexString(value));
		}
		
		return buffer.toString().toUpperCase();
	}	
	
}
