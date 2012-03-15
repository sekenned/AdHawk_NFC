<<<<<<< HEAD
package edu.cmu.ini.adhawks.nfcreader;

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
import android.widget.TextView;

public class NFCReaderActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //GUI setup
        TextView discoveryText = (TextView) findViewById(R.id.discoveryText);
        TextView intentInfo = (TextView) findViewById(R.id.intentInfo);
        
        //NFC stuff
        Intent intent = getIntent();
        String action = intent.getAction();

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            discoveryText.setText("NDEF Tag read!!");
        }
        // this is where we will be doing most of our work. Tech_Discovered is where all the other NFC formats are defined - GO
        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
        	discoveryText.setText("TECH Tag read!! /r/n Need to figure out what kind of tag it is");
            
            //byte[] data;
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //MifareClassic mfc = MifareClassic.get(tagFromIntent);
            //MifareUltralight mul = MifareUltralight.get(tagFromIntent); 
            
            tagFromIntent.toString();
            intentInfo.setText(tagFromIntent.toString());
            
        }
        //if the intent is a Tag Discovered, process it
        //TAG_DISCOVERED is a last resort action
        else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
        {
        	discoveryText.setText("TAG discovered!! /r/n This program really should be picking the tag up before this");
            
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
=======
package edu.cmu.ini.adhawks.nfcreader;

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
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NFCReaderActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //GUI setup
        TextView discoveryText = (TextView) findViewById(R.id.discoveryText);
        TextView intentInfo = (TextView) findViewById(R.id.intentInfo);
        
        //NFC stuff
        Intent intent = getIntent();
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
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            MifareUltralight mul = MifareUltralight.get(tagFromIntent); 
            
            tagFromIntent.toString();
            intentInfo.setText(tagFromIntent.toString());
            
        }
        //if the intent is a Tag Discovered, process it
        //TAG_DISCOVERED is a last resort action
        else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
        {
        	discoveryText.setText("TAG discovered!! /r/n This program really should be picking the tag up before this");
            
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
>>>>>>> 4f851c798974576c179d7837ca1f374e2cc9811d
}