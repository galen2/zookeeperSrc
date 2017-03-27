package base;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import org.junit.Test;

public class ByteBufferTest {
	
	@Test
	public void createAlloCateTest(){
		ByteBuffer buffer = ByteBuffer.allocate(1024);  
		System.out.println(buffer);
	}
	
	@Test
	public void createByteBuffer(){
		ByteArrayOutputStream byteoutput = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(byteoutput);
		ByteBuffer buffer = ByteBuffer.wrap(byteoutput.toByteArray());
		
	}
	@Test
	public void marketAndFlipTest(){
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putLong(1);
		buffer.putLong(2);
		buffer.putLong(3);
		buffer.putLong(4);
		buffer.putLong(5);
		buffer.flip();
		System.out.println(buffer);
		System.out.println(buffer.getInt());
		System.out.println(buffer.remaining());

		System.out.println(buffer);
		System.out.println(buffer);
		System.out.println(buffer.getLong());
		System.out.println(buffer);
		buffer.mark();
		System.out.println(buffer.getLong());
		buffer.reset();
//		buffer.flip();
		System.out.println(buffer.getLong());
		System.out.println(buffer.remaining());
	}

}
