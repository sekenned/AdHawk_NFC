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
			
			byteStream.write(0x60);
			
			dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);
			
			dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			byteStream.write(0xaf);
			byte [] response = dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			String manufData = FormatConverter.byteArrayToHexString(response);
			
			byteStream.write(0x5a);
			byteStream.write(0x90);
			byteStream.write(0x11);
			byteStream.write(0xf2);
			
			dfc.transceive(byteStream.toByteArray());
			
			byteStream.reset();
			byteStream.flush();	

			
			byteStream.write(0x6f);
			
			dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			
			byteStream.write(0xbd);
			byteStream.write(0x01);
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
			
			byteStream.write(0xaf);
			
			byte[] buff2 = dfc.transceive(byteStream.toByteArray());
			String StringBuff2 = FormatConverter.byteArrayToHexString(buff2);
			
		
			String rawDataFile0x01 = StringBuff1 + StringBuff2;
			
			
//			byteStream.write(0xbd);
//			byteStream.write(0x02);
//			byteStream.write(0x00);
//			byteStream.write(0x00);
//			byteStream.write(0x00);
//			byteStream.write(0x00);
//			byteStream.write(0x00);
//			byteStream.write(0x00);
//			
//			byte[] response2 = dfc.transceive(byteStream.toByteArray());
//			byteStream.reset();
//			byteStream.flush();
//			
//			String rawDataFile0x02 = FormatConverter.byteArrayToHexString(response2);
			
			return ("Manufacture Data: " + manufData + "\n \n" + "Raw Data File 0x01: " + rawDataFile0x01);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
    }
}
