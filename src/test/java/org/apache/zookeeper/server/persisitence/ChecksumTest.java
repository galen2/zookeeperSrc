package org.apache.zookeeper.server.persisitence;

import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumTest {
	
	@Test
	public void Adler32Test(){
		Checksum crc = new Adler32();
		long ee = 72623859790382858L;
		Long ewr = new Long(ee);
		
		byte[] buf = new byte[]{1,2};
		crc.update(buf, 0, buf.length);
		System.out.println(crc.getValue());
	}

}
