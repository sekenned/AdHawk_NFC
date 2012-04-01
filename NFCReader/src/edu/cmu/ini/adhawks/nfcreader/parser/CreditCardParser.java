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
	
	//Parses response data to get CC number (hides middle digits) 
	public String parseCCNumber(String recordData)
	{
		String ccNumber = "";
		ccNumber = recordData.substring(8, 12);
		ccNumber += " xxxx xxxx xx";
		ccNumber += recordData.substring(23,24);
		
		return ccNumber;
	}
	
	public String parseExpirationDate(String recordData)
	{
		//find the first D after the CC number and then the date will be after that
		int dLocation=0;
		for(int i=0; i<recordData.length(); i++)
		{
			if( recordData.charAt(i) == 'D')
			{
				dLocation = i;
				break;
			}
		}
		
		String year = "20" + recordData.substring(dLocation + 1, dLocation + 3);
		String month = recordData.substring(dLocation + 3, dLocation + 5);
		
		//return in month/year format
		String expirationDate = month + "/" + year;
		
		return expirationDate;
	}
	
}