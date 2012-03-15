package edu.cmu.ini.adhawks.nfcreader;

import java.io.IOException;
import java.util.List;

import edu.cmu.ini.adhawks.nfcreader.NdefMessageParser;
import edu.cmu.ini.adhawks.nfcreader.R;
import edu.cmu.ini.adhawks.nfcreader.nfcrecords.ParsedNdefRecord;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NFCReaderActivity extends Activity
{
	TextView discoveryText, intentInfo, dataText, testText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //GUI setup
        discoveryText = (TextView) findViewById(R.id.discoveryText);
        intentInfo = (TextView) findViewById(R.id.intentInfo);
        
        //NFC stuff
        Intent intent = getIntent();
        resolveIntent(intent);
    }
    
    public void resolveIntent(Intent intent)
    {
    	String action = intent.getAction();
    	
        String action = intent.getAction();

        
        
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            discoveryText.setText("NDEF Tag read!!");
        }
        // this is where we will be doing most of our work. Tech_Discovered is where all the other NFC formats are defined - GO
        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
        	discoveryText.setText("TECH Tag read!! /r/n Need to figure out what kind of tag it is");
            
            byte[] data;
            
            //get tag from intent
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            
           // MifareClassic mfc = MifareClassic.get(tagFromIntent);
           // MifareUltralight mul = MifareUltralight.get(tagFromIntent); 

            //Get tech list (tag type) from intent
            String[] localTechList = tagFromIntent.getTechList();
            
            //Print out tech list to screen
            String localTechListString = "";
            for(String s : localTechList)
            {
            	localTechListString += s;
            	localTechListString += " ";
            }
            intentInfo.setText(localTechListString);
        }
    	//if the intent is a Tag Discovered, process it
        //TAG_DISCOVERED is a last resort action
        else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
        {
        	discoveryText.setText("TAG discovered!!. This program really should be picking the tag up before this");
            
            //from here on is copied from NFCDemo -CD
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null)
            {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            else
            {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }
        }
        else //if the intent wasn't a Tag Discovered, print that...
        {
        	discoveryText.setText("No tag read :(");
        }     
    }
    
    //This is the code for reading MifareClassic cards, not sure if this actually works -GO    
    public void readMiFareClassic(Tag tag)
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
 
    //Still working on reading MiFareUltralight cards -GO
    public void readMiFareUltralight(Tag tag)
    {   
        MifareUltralight mul = MifareUltralight.get(tag);     	
    	
        try 
        {
        	mul.connect();
        	byte[] byteData = null;
        	String mulData = null;
        	int totalPages = 16;
        	
        	byteData = mul.readPages(0); 
        	
//        	for(int page = 0; page < totalPages; page++)
//        	{
//        		byteData = mul.readPages(page);
//        		mulData += new String(byteData);
//        	}
        	
//        	intentInfo.setText(mulData);
        	intentInfo.setText(new String(byteData));
        }
        catch(Exception e) 
        { 
        	e.getLocalizedMessage();
        }   	
    }    
}