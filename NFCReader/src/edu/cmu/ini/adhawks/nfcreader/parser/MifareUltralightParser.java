package edu.cmu.ini.adhawks.nfcreader.parser;

import java.io.IOException;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

public class MifareUltralightParser 
{

	public MifareUltralightParser()
	{
		
		
	}
	
    //Can successfully read tag but there is a bunch of crap in it that needs to be cleaned out - GO
    public String readMifareUltralight(Tag tag)
    {  
    	//MifareUltralight tags have 4 byte pages 
    	//MifareUltralight contains 64 bytes
    	//MifareUltralight C contains 192 bytes    	
        MifareUltralight mul = MifareUltralight.get(tag);     	
    	
        try 
        {
        	mul.connect();
        	byte[] byteData = null;
        	String mulData = null;
        	//MifareUltralight readPages() reads 4 pages at a time so I need an offset
        	final int OFFSET = 4;
        	int totalPages = 0;
        	
        	if(mul.getType() == MifareUltralight.TYPE_ULTRALIGHT)
        	{
        		totalPages = 4;
        	}
        	else if(mul.getType() == MifareUltralight.TYPE_ULTRALIGHT_C)
        	{
        		totalPages = 12;
        	}        	
        	
        	//the first four pages are for the OTP area, manufacturer data, and locking bits
        	for(int page = 0; page < totalPages; page++)
        	{
        		byteData = mul.readPages(page*OFFSET);
        		mulData += new String(byteData);
        	}
        
        	return mulData;
        }
        catch(IOException e) 
        { 
           	Log.e("Read MifareUltrlight", "IOException while reading tag", e);        	
        	return e.getLocalizedMessage();
        }
        finally 
        {
            if (mul != null) 
            {
               try 
               {
            	   mul.close();
               }
               catch(Exception e)
               {
	               	Log.e("Read MifareUltrlight", "Error closing tag...", e);
               }
            }
        }        
    } 	
}
