package edu.cmu.ini.adhawks.nfcreader.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;

public class CreditCardParser {
	IsoDep localIsoDep = null;
	
	public CreditCardParser()
	{
		// empty
	}
	
	//This currently does a SELECT statement where is grabs the card type. 
	//Successfully reads the fact that the PNC card is Visa Debit 
	public String readCreditCard(Tag tag)
	{
		localIsoDep = IsoDep.get(tag);
		try 
		{
			localIsoDep.connect();
			byte[] AID = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)3};
			
			int length = (byte)AID.length;
			ByteArrayOutputStream byteSteam = new ByteArrayOutputStream();
			byteSteam.write(0);
			//INS= Select
			byteSteam.write(-92);
			//P1 = Select by DF Name
			byteSteam.write(4);
			//P2 = First or Only Occurrence??
			byteSteam.write(0);
			//Lc= Length
			byteSteam.write(length);
			//Data = AID
			byteSteam.write(AID);
			//Le = As much as possible
			byteSteam.write(0);
			
			byte[] byteArray1 = byteSteam.toByteArray();
			byte[] response = localIsoDep.transceive(byteArray1);
			byteSteam.reset();
			byteSteam.flush();
			
			String responseText = FormatConverter.byteArrayToHexString(response);
			return responseText;					
		}
		catch(IOException e)
		{
			return e.getLocalizedMessage();
		}	
	}
	
	public String getData()
	{
		try
		{
			byte[] cmd = {(byte)-97, (byte)54};
			
			ByteArrayOutputStream byteSteam = new ByteArrayOutputStream();
			byteSteam.write(-128);
			byteSteam.write(-54);
			byteSteam.write(cmd);
			//Le = As much as possible
			byteSteam.write(0);
			
			byte[] response = localIsoDep.transceive(byteSteam.toByteArray());
			byteSteam.reset();
			byteSteam.flush();
			
		    
			String responseText = FormatConverter.byteArrayToHexString(response);
			return responseText;
		}
		catch(Exception e)
		{
			return e.getLocalizedMessage();
		}
	}
	
	public String readRecord()
	{
		try
		{
			byte[] cmd = {(byte)1};
			
			ByteArrayOutputStream byteSteam = new ByteArrayOutputStream();
			byteSteam.write(0);
			byteSteam.write(-78);
			byteSteam.write(cmd);
			byteSteam.write(12);
			byteSteam.write(0);
			
			byte[] byteArray1 = byteSteam.toByteArray();
			byte[] response = localIsoDep.transceive(byteArray1);
			byteSteam.reset();
			byteSteam.flush();	
			
			String responseText = FormatConverter.byteArrayToHexString(response);
			return responseText;
		}
		catch(Exception e)
		{
			return e.getLocalizedMessage();
		}
	}	
}
