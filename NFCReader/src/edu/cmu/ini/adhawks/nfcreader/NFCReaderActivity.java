package edu.cmu.ini.adhawks.nfcreader;

import java.io.IOException;
import java.util.List;

import edu.cmu.ini.adhawks.nfcreader.*;
import edu.cmu.ini.adhawks.nfcreader.parser.*;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.os.Parcelable;
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
        dataText = (TextView) findViewById(R.id.dataText);
        testText = (TextView) findViewById(R.id.testText);
        
        //NFC stuff
        Intent intent = getIntent();
        resolveIntent(intent);
    }
    
    public void resolveIntent(Intent intent)
    {
    	String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            discoveryText.setText("NDEF Tag read!!");
        }
        // this is where we will be doing most of our work. Tech_Discovered is where all the other NFC formats are defined - GO
        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
        	discoveryText.setText("TECH Tag read!! Need to figure out what kind of tag it is");

            //Get tech list (tag type) from intent
            String[] techList = tag.getTechList();
            
            //Print out tech list to screen
            String techListString = "";
            for(String tech : techList)
            {
            	techListString += tech;
            	techListString += ", ";
            	
            
            	if(tech.equals("android.nfc.tech.MifareUltralight"))
            	{
            		testText.setText("mul");
            		MifareUltralightParser mup = new MifareUltralightParser();
            		String tagData = mup.readMifareUltralight(tag);
            		dataText.setText(tagData);
            		testText.setText("Made it here");
            	} 
            	else if(tech.equals("android.nfc.tech.MifareClassic"))
            	{
            		testText.setText("mul");
            		MifareClassicParser mcp = new MifareClassicParser();
            		String tagData = mcp.readMifareClassic(tag);
            		dataText.setText(tagData);
            		testText.setText("Made it here");            		
            	}
            	else if(tech.equals("android.nfc.tech.Ndef"))
            	{
            		//testText.setText("ndef");
            	}
            	else if(tech.equals("android.nfc.tech.NfcA"))
            	{
            		//testText.setText("nfca");
            	}            	
            }
            
            intentInfo.setText(techListString);
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
}