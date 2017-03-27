package client;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class test extends BaseTest {
    private static final Logger LOG = LoggerFactory.getLogger(test.class);

    public static void main(String[] args) {
//		LOG.debug("32323");
//		LOG.info("3333333333333");
//		System.out.println("333333");
    	ArrayBlockingQueue<ByteBuffer> bq = new ArrayBlockingQueue<ByteBuffer>(1);
    	Object oo = bq.poll();
    	System.out.println(oo);
    	
	}
    
    @Test
    public void test1(){
    	ByteBuffer buffer=ByteBuffer.allocate(12);
    	buffer.put((byte)2);
    	buffer.put((byte)3);
    	buffer.put((byte)4);
    	buffer.put((byte)5);
    	buffer.flip();
    	print(buffer.get());
    	buffer.mark();
    	print(buffer.get());
    	buffer.reset();
    	print(buffer.get());
    	
//    	print(buffer.remaining());
    	
    }
}
