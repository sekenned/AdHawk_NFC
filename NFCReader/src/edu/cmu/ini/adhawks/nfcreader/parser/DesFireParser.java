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
	
	//This is the code for reading 
	public String readDesFire(Tag tag)
    {
		IsoDep dfc = IsoDep.get(tag);
		final byte manufData = (byte) 0x60;
		final byte selectApp = (byte) 0x5A;
		final byte getFile = (byte) 0x6F;
		final byte readData = (byte) 0xBD;
		final byte readRecord = (byte) 0xBB;
		
		
		try {
			dfc.connect();
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			
//			byteStream.write(manufData);
//			
//			byteStream.write(selectApp);
//			byteStream.write(0x9011f2);
//
//			byteStream.write(getFile);
//			byteStream.write(0x08);
//			byteStream.write(inputArray);
//			
//			byteStream.write(readData);

			byte[] response = dfc.transceive(byteStream.toByteArray());
			byteStream.reset();
			byteStream.flush();
			
			return FormatConverter.byteArrayToHexString(response);		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}
