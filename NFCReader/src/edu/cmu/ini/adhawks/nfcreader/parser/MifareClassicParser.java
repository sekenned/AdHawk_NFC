package edu.cmu.ini.adhawks.nfcreader.parser;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

public class MifareClassicParser 
{
	
	public MifareClassicParser()
	{
		
		
	}
	
	//This is the code for reading MifareClassic cards, not sure if this actually works -GO 
	public void readMifareClassic(Tag tag)
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
	                authenticate = mfc.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
	                
	                if(authenticate)
	                {
	                    //get the block count in each sector
	                	numBlocks = mfc.getBlockCountInSector(sector);
	                	blockIndex = 0;
	                    for(int block = 0; block < numBlocks; block++) 
	                    {
	                    	blockIndex = mfc.sectorToBlock(sector);
	                        //read the block
	                        byteData = mfc.readBlock(blockIndex);    
	                        //Convert the data into a string from Hex format.                
	                        //Log.i(TAG, getHexString(data, data.length));
	                        blockIndex++;
	                    }
	                }
	                else // Authentication failed - Handle it
	                { 
	                   
	                }
	            }
          }
          catch(IOException e) 
          { 
              //Log.e(TAG, e.getLocalizedMessage());
              //showAlert(3);
          }    	
    }
}
