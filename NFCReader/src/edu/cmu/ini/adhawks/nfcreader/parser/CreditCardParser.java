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
	
	//Convert rec to binary for transceive to work
	//Based on code from http://snippets.dzone.com/posts/show/93
	public static byte[] intToByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}
	
	//Turns a byte array to string
	public static String ByteArrayToString(byte[] byteArray)
	{
	  StringBuilder hex = new StringBuilder(byteArray.length*2);
	  for(byte b : byteArray)
	  {
	    hex.append(String.format("{0:x2}",b));
	  }
	  return hex.toString();
	}

	
	//This is the code for reading Credit Cards.  SK
	//This currently does a SELECT statement where is grabs the card type. 
	//Successfully reads the fact that the PNC card is Visa Debit 
	public String readCreditCard(Tag tag)
	{
<<<<<<< HEAD
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
=======
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
					for (int rec = 0x01; rec <= 0x10; rec++)
					{
						//convert ints sfi and rec into type Byte to be input into transceive						
						byte[] recByte = intToByteArray(rec);
						byte[] sfiByte = intToByteArray(sfi);
						
						//This code is modified from http://snippets.dzone.com/posts/show/93
						byte[] tlv = localIsoDep.transceive((byte)0x00, (byte)0xB2, recByte, (sfi << (byte)0x03 | (byte)0x04, (byte)0x00); //(ByteArrayToString(sfiByte) << (byte)0x03) | (byte)0x04, (byte)0x00);
						//card.sendApdu(0x00, 0xB2, rec, (sfi << 3) | 4, 0x00);
						if (card.SW == 0x9000)//This is from the original 'card' format.  It needs to change to some sort of transceive output.  
						{
							
						}
					}
				}
				
				
				
				//Old
				byte[] response = localIsoDep.transceive(cmd);							// get response
				
				return new String(response);											//Return response as string
				
				
				
			}
			catch(IOException e)
>>>>>>> 16e9d3f08659748ef07e55da147a2be225ae0fee
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
