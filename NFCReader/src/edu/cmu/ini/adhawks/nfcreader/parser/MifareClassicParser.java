package edu.cmu.ini.adhawks.nfcreader.parser;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;

public class MifareClassicParser 
{
	
	public MifareClassicParser()
	{
		
		
	}
	
	//This is the code for reading MifareClassic cards, not sure if this actually works -GO 
	public String readMifareClassic(Tag tag)
    {    
    	  MifareClassic mfc = MifareClassic.get(tag);
          
          try 
          {
	            mfc.connect();
	            boolean authenticate = false;
	            byte[] byteData = null;
	            String mfcData = null;
	            
	            //get the number of sectors the card has and loop through them
	            int numSectors = mfc.getSectorCount();
	            int numBlocks = 0;
	            int blockIndex = 0;
	            
	            for(int sector = 0; sector < numSectors; sector++) 
	            {
	                //authenticate the sector
	                authenticate = mfc.authenticateSectorWithKeyB(sector, MifareClassic.KEY_DEFAULT);	                
	                
	                if(authenticate)
	                {
	                    //get the block count in each sector
	                	numBlocks = mfc.getBlockCountInSector(sector);
	                	blockIndex = mfc.sectorToBlock(sector);
	                    for(int block = 0; block < numBlocks; block++) 
	                    {
	                        //read the block
	                        byteData = mfc.readBlock(blockIndex);    
	                        blockIndex++;
	                        mfcData += new String(byteData);
	                    }
	                }
	                else // Authentication failed - Handle it
	                { 
	                	return "Failed to Authenticate";
	                }
	            }
	            
	            return mfcData;
          }
          catch(IOException e) 
          { 
             	Log.e("Read MifareClassic", "IOException while reading tag", e);        	
            	return e.getLocalizedMessage();
          }
          finally 
          {
              if (mfc != null) 
              {
                 try 
                 {
                	 mfc.close();
                 }
                 catch(Exception e)
                 {
  	               	Log.e("Read MifareClassic", "Error closing tag...", e);
                 }
              }
          }          
    }
}
