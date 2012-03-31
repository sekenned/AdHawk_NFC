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
			byte[] cmd = {(byte)-97, (byte)54};
			//byte[] cmd = {(byte)-97, (byte)19};
			//byte[] cmd = {(byte)-128, (byte)-54};
			
			ByteArrayOutputStream byteSteam = new ByteArrayOutputStream();
			byteSteam.write(-128);
			//INS= Select
			byteSteam.write(-54);
			//Data = AID
			byteSteam.write(cmd);
			//Le = As much as possible
			byteSteam.write(0);
			
			byte[] response = localIsoDep.transceive(byteSteam.toByteArray());
			byteSteam.reset();
			byteSteam.flush();
			
//			byte[] arrayOfByte4 = null;
//			int j;
//		    if ((response != null) && (response[0] == cmd[0]) && (response[1] == cmd[1]))
//		    {
//		      int i = response[2];
//		      arrayOfByte4 = new byte[i];
//		      j = 0;
//		      if (j < i)
//		      {
//				    for (String str = String.valueOf((arrayOfByte4[0] << 8) + arrayOfByte4[1]); ; str = "")
//				    {
//				      arrayOfByte4[j] = response[(j + 3)];
//				      j++;
//				      break;
//				    }
//		      }
//		    }
			
		    
			
			
			//write byte array to a string
			/*
			String s = "";
			for(byte b : response)
			{
				s += b;
			}
			String responseText = s;
			*/
			
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
			
			//return null;
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
