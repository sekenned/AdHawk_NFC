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
        TextView tv = new TextView(this);
        
        //NFC stuff
        Intent intent = getIntent();
        String action = intent.getAction();
        
        //if the intent is a Tag Discovered, process it
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
        {

            tv.setText("Tag read!!");
            setContentView(tv);
            
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
            
            // print the payloads -CD
            
            buildTagViews(msgs);
            
        }
        else //if the intent wasn't a Tag Discovered, print that...
        {
            
            tv.setText("No tag read :(");
            setContentView(tv);
        }
        
        
    }

    void buildTagViews(NdefMessage[] msgs)
    {
        if (msgs == null || msgs.length == 0)
        {
            return;
        }
       
        //Pulling out GUI stuff - CD
        // LayoutInflater inflater = LayoutInflater.from(this);
       // LinearLayout content = mTagContent;
       // content.removeAllViews();
       
        
        // Parse the first message in the list
        // Build views for all of the sub records
        
        //it looks like they're only ever parsing 1 message here... -CD
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        
        String s = "";
        for (int i = 0; i < size; i++)
        {
            ParsedNdefRecord record = records.get(i);
            
            s += record.getText();
                        
            //again, pulling out GUI stuff; we want to display this our own way -CD
            //content.addView(record.getView(this, inflater, content, i));
            //inflater.inflate(R.layout.tag_divider, content, true);
            
            //contents of getView():
            //TextView text = (TextView) inflater.inflate(R.layout.tag_text, parent, false);
            //text.setText(mText);
            //return text;
        }

        TextView tv = new TextView(this);
        tv.setText(s);
        setContentView(tv);
        
        
    }//end of buildTagViews()


}