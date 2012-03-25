package edu.cmu.ini.adhawks.nfcreader.parser;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;

public class CreditCardParser {
	
	public CreditCardParser()
	{
		// empty
	}
	
	//This is the code for reading Credit Cards.  SK
	public String readCreditCard(Tag tag)
	{
			IsoDep localIsoDep = IsoDep.get(tag);
			NfcB localNfcB = NfcB.get(tag);
			try 
			{
				localIsoDep.connect(); 													// Connect to IsoDep
				byte[] cmd = {(byte)0x00,(byte)0xB2,(byte)0x01,(byte)0x0C,(byte)0x00};	// Command dealing with Visa PayWave from Stack Overflow
				byte[] response = localIsoDep.transceive(cmd);							// get response
				
				return new String(response);											//Return response as string
				
				
				
			}
			catch(IOException e)
			{
				
			}
			return null;
			
				
			
	}

}
