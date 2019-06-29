package utils;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CRC32Calculator {
	public static long getChecksum(String input) {
				 
		// get bytes from string
		byte bytes[] = input.getBytes();
		 
		Checksum checksum = new CRC32();
		
		// update the current checksum with the specified array of bytes
		checksum.update(bytes, 0, bytes.length);
		 
		// return the current checksum value
		return checksum.getValue();
	}
}
