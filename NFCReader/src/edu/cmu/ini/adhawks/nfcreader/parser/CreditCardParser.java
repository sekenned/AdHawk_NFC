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
	
	//This is the code for reading Credit Cards.  SK
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
			
			String responseText = this.byteArrayToHexString(response);
			return responseText;					
		}
		catch(IOException e)
		{
			return e.getLocalizedMessage();
		}	
	}
	
	public String byteArrayToHexString(byte[] byteArray) 
	{
		StringBuffer buffer = new StringBuffer(byteArray.length * 2);
		
		for (int i = 0; i < byteArray.length; i++)
		{
			//& is a bitwise AND operation 
			int value = byteArray[i] & 0xff;
			if (value < 16) 
			{
				buffer.append('0');
			}
			
			buffer.append(Integer.toHexString(value));
		}
		
		return buffer.toString().toUpperCase();
	}
	
	public String hexToString(String hex)
	{	  
		StringBuilder buffer = new StringBuilder();
		StringBuilder tempBuffer = new StringBuilder();
		 
		for( int i=0; i<hex.length()-1; i+=2 )
		{
			//grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			//convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			//convert the decimal to character
			buffer.append((char)decimal);
			 
			tempBuffer.append(decimal);
		}
		System.out.println("Decimal : " + tempBuffer.toString());
		 
		return buffer.toString();
	}	
	
	//this currently does nothing - GO
	public String tryAID(byte[] paramArrayOfByte) throws IOException
	{
		int length = (byte)paramArrayOfByte.length;
		ByteArrayOutputStream byteSteam = new ByteArrayOutputStream();
		byteSteam.write(0);
		byteSteam.write(-92);
		byteSteam.write(4);
		byteSteam.write(0);
		byteSteam.write(length);
		byteSteam.write(paramArrayOfByte);
		byteSteam.write(0);
		
		byte[] byteArray1 = byteSteam.toByteArray();
		byte[] response = localIsoDep.transceive(byteArray1);
		byteSteam.reset();
		byteSteam.flush();
		
		String responseText = byteArrayToHexString(response);
		return responseText;	
	}
	
	//this currently does nothing - GO	
	public byte[] getCardType() throws IOException 
	{	
		byte[] visaDebitAID = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)3};
		//byte[] unknownAID = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)37};
		//byte[] unknownAID2 = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)3,(byte)16,(byte)16};
		
		return visaDebitAID;
	}	
}
