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

	
	//This is the code for reading Credit Cards.  SK
	public String readCreditCard(Tag tag)
	{
			IsoDep localIsoDep = IsoDep.get(tag);
			NfcB localNfcB = NfcB.get(tag);
			try 
			{
				localIsoDep.connect(); 													// Connect to IsoDep
				//byte[] cmd = {(byte)0x00,(byte)0xB2,(byte)0x01,(byte)0x0C,(byte)0x00};	// Command dealing with Visa PayWave from Stack Overflow
				byte[] cmd = {(byte)0x00,(byte)0xA4,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0xA0,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x10,(byte)0x10,(byte)0x00,(byte)0x90,(byte)0x00};	//OpenSCDP cmd
				
				//String sData = "0xBB"; 				// got this magic value from web
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
						byte[] tlv = localIsoDep.transceive((byte)0x00, (byte)0xB2, recByte, (sfiByte << (byte)0x03) | (byte)0x04, (byte)0x00);
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
			{
				
			}
			return null;
			
				
			
	}

}
