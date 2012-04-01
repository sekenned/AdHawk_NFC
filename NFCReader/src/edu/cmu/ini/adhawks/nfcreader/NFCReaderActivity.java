package edu.cmu.ini.adhawks.nfcreader;

import java.util.Arrays;
import java.util.List;

import edu.cmu.ini.adhawks.nfcreader.parser.*;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

public class NFCReaderActivity extends Activity
{
	TextView actionText, tagTypeText, dataText, otherText, decimalText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //GUI setup
        actionText = (TextView) findViewById(R.id.actionText);
        tagTypeText = (TextView) findViewById(R.id.tagTypeText);
        dataText = (TextView) findViewById(R.id.dataText);
        otherText = (TextView) findViewById(R.id.otherText);
        
        //NFC stuff
        Intent intent = getIntent();
        resolveIntent(intent);
    }
    
    public void resolveIntent(Intent intent)
    {
    	String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); //Why not getParcelableArrayExtra ??
        
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            actionText.setText("NDEF Tag! \n");
        }
        // this is where we will be doing most of our work. Tech_Discovered is where all the other NFC formats are defined - GO
        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
        	actionText.setText("TECH Tag! \n");

            //Get tech list (tag type) from intent
            String[] techList_array = tag.getTechList();
            
            //Print out tech list to screen
            String techList_string = "";
            for(String tech : techList_array)
            {
            	techList_string += tech;
            	techList_string += "\n";
            }
            tagTypeText.setText(techList_string);
            
            List<String> techList = Arrays.asList(techList_array);
            
            if(techList.contains("android.nfc.tech.MifareUltralight"))
            {
            	MifareUltralightParser mup = new MifareUltralightParser();
            	String tagData = mup.readMifareUltralight(tag);
            	tagData += "\n";
            	dataText.setText(tagData);
            	
            	otherText.setText("MifareUltraLight tag found \n");
            } 
            else if(techList.contains("android.nfc.tech.MifareClassic"))
            {
            	MifareClassicParser mcp = new MifareClassicParser();
            	String tagData = mcp.readMifareClassic(tag);
            	tagData += "\n";
            	dataText.setText(tagData);   
            	
            	otherText.setText("MifareClassic tag found! \n");
            }
            else if(techList.contains("android.nfc.tech.IsoDep") && techList.contains("android.nfc.tech.NfcB"))
            {
            	//possible credit card
            	otherText.setText("Possible credit card tag found \n");
            	CreditCardParser ccp = new CreditCardParser();
            	            	
            	String tagData = ccp.readCreditCard(tag);
            	dataText.setText(tagData); //view tag data
            	otherText.setText(FormatConverter.hexToString(tagData));            	
            
            	String cardData = ccp.getData();
            	dataText.setText(cardData); //view tag data
            	otherText.setText(FormatConverter.hexToString(cardData));
	
            	String recordData = ccp.readRecord();
            	dataText.setText("CC Number: "+ ccp.parseCCNumber(recordData) 
            			+"\nExpiry Date: "+ ccp.parseExpirationDate(recordData));
            	otherText.setText(FormatConverter.hexToString(recordData));
            }
            else
            {
            	//if here, failed to recognize tag type supported by our app
            	otherText.setText("Unknown tag type \n");
            }    
        }
    	//if the intent is a Tag Discovered, process it
        //TAG_DISCOVERED is a last resort action
        else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
        {
        	actionText.setText("Unknown TAG discovered \n");
            
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
        	actionText.setText("No tag read :( \n");
        }     
    }     
}