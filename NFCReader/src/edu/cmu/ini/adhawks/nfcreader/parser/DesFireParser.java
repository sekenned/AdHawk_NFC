package edu.cmu.ini.adhawks.nfcreader.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

public class DesFireParser 
{
	
	public DesFireParser()
	{
		
		
	}
	
	//This is the code for reading Clipper Card 
	public String readDesFire(Tag tag)
    {
		IsoDep dfc = IsoDep.get(tag);
//		final byte manufData = (byte) 0x60;
//		final byte selectApp = (byte) 0x5A;
//		final byte getFile = (byte) 0x6F;
//		final byte readData = (byte) 0xBD;
//		final byte readRecord = (byte) 0xBB;
//		byte[] appIdBuff = new byte[3];
//		final byte appId = (byte) 0x9011f2;
		
		
		try {
			dfc.connect();
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			
			byteStream.write(0x60);						// ---> Get Manufacturing Data
			
			dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);						// --> Get more data
			
			dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);
			byte [] response = dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			String manufData = FormatConverter.byteArrayToHexString(response);
			
			byteStream.write(0x5a);						// ---> Select Application:
			byteStream.write(0x90);						//
			byteStream.write(0x11);						//	
			byteStream.write(0xf2);						// --> Clipper card:  0x901112 
			
			dfc.transceive(byteStream.toByteArray());
			
			byteStream.reset();
			byteStream.flush();	

//Get file 0x01
			byteStream.write(0x6f);						// ---> Get File
			
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			
			byteStream.write(0xbd);						// ---> Read Data
			byteStream.write(0x01);						//      of File 0x01
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			byte[] buff1 = dfc.transceive(byteStream.toByteArray());
			String StringBuff1 = FormatConverter.byteArrayToHexString(buff1);
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);						// Get the rest of the extra data.  
			
			byte[] buff2 = dfc.transceive(byteStream.toByteArray());
			String StringBuff2 = FormatConverter.hexToDecimal(FormatConverter.byteArrayToHexString(buff2));
			
		
			String rawDataFile0x01 = StringBuff1 + StringBuff2;			
	
	//Get file 0x02			
			
			byteStream.write(0x6f);						// ---> Get File
			
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xbd);						// Read data 
			byteStream.write(0x02);						// 			of file 0x02
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			byte[] result32 = dfc.transceive(byteStream.toByteArray());
			String file02result = FormatConverter.byteArrayToHexString(result32);
	
	//Get file 0x06
			byteStream.write(0x6f);						// ---> Get File
			
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			
			byteStream.write(0xbd);						// ---> Read Data
			byteStream.write(0x06);						//      of File 0x06
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			byte[] buff6 = dfc.transceive(byteStream.toByteArray());
			String StringBuff6 = FormatConverter.byteArrayToHexString(buff6);
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);						// Get the rest of the extra data.  
			
			byte[] buff6more = dfc.transceive(byteStream.toByteArray());
			StringBuff6 = StringBuff6 + FormatConverter.byteArrayToHexString(buff6more);
			
			//Get file 0x05
			byteStream.write(0x6f);						// ---> Get File
			
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			
			byteStream.write(0xbd);						// ---> Read Data
			byteStream.write(0x05);						//      of File 0x05
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			byte[] buff5 = dfc.transceive(byteStream.toByteArray());
			String StringBuff5 = FormatConverter.byteArrayToHexString(buff5);
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);						// Get the rest of the extra data.  
			
			byte[] buff5more = dfc.transceive(byteStream.toByteArray());
			StringBuff5 = StringBuff5 + FormatConverter.byteArrayToHexString(buff5more);
			
			
	//Get file 0x08
			byteStream.write(0x6f);						// ---> Get File
				
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xbd);						// Read data 
			byteStream.write(0x08);						// 			of file 0x02
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			result32 = dfc.transceive(byteStream.toByteArray());
			String file08result = FormatConverter.byteArrayToHexString(result32);
		
			
//Get file 0x0e  >>>This file is 512 long.<<<
			byteStream.write(0x6f);						// ---> Get File
			
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xbd);						// Read data 
			byteStream.write(0x0e);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			byte[] result0e00 = dfc.transceive(byteStream.toByteArray());
			String file0x0eResultTotal = FormatConverter.byteArrayToHexString(result0e00);
			
			for (int i = 0; i < 8; i++){			//Loop through "get more data" command	
				byteStream.reset();
				byteStream.flush();
		
				byteStream.write(0xaf);						 
				
				byte[] tempresult = dfc.transceive(byteStream.toByteArray());
				String tempString = FormatConverter.byteArrayToHexString(tempresult);
				file0x0eResultTotal = file0x0eResultTotal + tempString;	
			}

//Get file 0x0f >>>This file is 1280 long<<<	
			byteStream.write(0x6f);						// ---> Get File
			
			dfc.transceive(byteStream.toByteArray());	
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xbd);						// Read data 
			byteStream.write(0x0f);						//			for file 0x0f
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			byteStream.write(0x00);
			
			byte[] result0f = dfc.transceive(byteStream.toByteArray());
			String file0x0fResultTotal = FormatConverter.byteArrayToHexString(result0f);
			
			for (int i = 0; i < 21; i++){			//Loop through "get more data" command	
				byteStream.reset();
				byteStream.flush();
		
				byteStream.write(0xaf);						 
				
				byte[] tempresult = dfc.transceive(byteStream.toByteArray());
				String tempString = FormatConverter.byteArrayToHexString(tempresult);
				file0x0fResultTotal = file0x0fResultTotal + tempString;
			}

			
			return ("Manufacture Data: " + manufData + "\n \n" + 
					"Raw Data \n\n" +
					"File 0x01: " + rawDataFile0x01 + "\n \n" +
					"File 0x02: " + file02result + "\n \n" +
					"File 0x05: " + StringBuff5 + "\n \n" +
					"File 0x06: " + StringBuff6 + "\n \n" +
					"File 0x08: " + file08result  + "\n \n" +
					"File 0x0e: " + file0x0eResultTotal + "\n \n" + 
					"File 0x0f: " + file0x0fResultTotal);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
    }
}
