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
			byte[] AID = {(byte)-97, (byte)54};
			
			int length = (byte)AID.length;
			ByteArrayOutputStream byteSteam = new ByteArrayOutputStream();
			byteSteam.write(-128);
			//INS= Select
			byteSteam.write(-54);
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
		catch(Exception e)
		{
			return e.getLocalizedMessage();
		}
	}
	
	//this currently does nothing - GO	
	public byte[] getCardType() throws IOException 
	{	
		byte[] visaDebitAID = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)3};
		//byte[] unknownAID = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)37};
		//byte[] unknownAID2 = {(byte)-96, (byte)0, (byte)0, (byte)0, (byte)3,(byte)16,(byte)16};
		
		return visaDebitAID;
	}
	
	//Stu is trying to working on putting something together here so don't touch. Thanks! - GO
	public String stusReader(Tag tag)
	{
		IsoDep localIsoDep = IsoDep.get(tag);
		NfcB localNfcB = NfcB.get(tag);
		try 
		{
			localIsoDep.connect(); 													    // Connect to IsoDep
			//byte[] cmd = {(byte)0x00,(byte)0xB2,(byte)0x01,(byte)0x0C,(byte)0x00};	// Command dealing with Visa PayWave from Stack Overflow
			byte[] cmd = {(byte)0x00,(byte)0xA4,(byte)0x04,(byte)0x00,
						  (byte)0x00,(byte)0xA0,(byte)0x00,(byte)0x00,
						  (byte)0x00,(byte)0x03,(byte)0x10,(byte)0x10,
						  (byte)0x00,(byte)0x90,(byte)0x00};							//byte array derived from OpenSCDP cmd
			
			//String sData = "0xBB"; 				// "got this magic value from web" - Stack overflow
	        //byte []data = sData.getBytes();		//
			
			//new
			for (int sfi = 1; sfi <= 31; sfi++)
			{
				for (int rec = 1; rec <= 16; rec++)
				{
					//convert ints sfi and rec into type Byte to be input into transceive						
					byte recByte = (byte) rec;
					byte sfiByte = (byte) sfi;
					
					//This code is modified from http://snippets.dzone.com/posts/show/93
					//byte[] tlv = localIsoDep.transceive((byte)0x00, (byte)0xB2, recByte, (sfi << (byte)0x03 | (byte)0x04, (byte)0x00); //(ByteArrayToString(sfiByte) << (byte)0x03) | (byte)0x04, (byte)0x00);
					//card.sendApdu(0x00, 0xB2, rec, (sfi << 3) | 4, 0x00);
//					if (card.SW == 0x9000)//This is from the original 'card' format.  It needs to change to some sort of transceive output.  
//					{
//						
//					}
				}
			}

			//Old
			byte[] response = localIsoDep.transceive(cmd);							// get response
			return new String(response);											//Return response as string	
		}
		catch(IOException e)
		{
			
		}
		return null;		
	}
}
