package base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.jute.BinaryInputArchive;
import org.apache.jute.BinaryOutputArchive;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.proto.ExistsRequest;
import org.apache.zookeeper.proto.RequestHeader;
import org.junit.Test;

public class ByteOutPutStreamTest {
	
	@Test
	public void byteOutPutStream() throws Exception{
		ByteArrayOutputStream boutPut = new ByteArrayOutputStream();
		boutPut.write(97);
		boutPut.write(65);
		byte b []  = boutPut.toByteArray();
		System.out.println("Print the content:"+b.length);
		
		DataOutputStream dos=new DataOutputStream(boutPut);
		dos.writeUTF("aaa");
		byte bb [] = boutPut.toByteArray();
		System.out.println("Print the content:"+bb.length);
	}
	
	@Test
	/**
	 * Description:
	 * 1、最终都是ByteBuffer来进行字节流数据传输
	 * 2、对象里面变量只是为了方便查看存在，最终还是通过流吧属性发送出去
	 * 3、对象的写入和读取必须按照顺序，否则会无法读取到数据
	 * @throws Exception
	 * @author liliangbing
	 * @date  2017年2月15日
	 */
	public void BinaryOutputArchiveTest() throws Exception{
		ByteArrayOutputStream byteoutput = new ByteArrayOutputStream();
		BinaryOutputArchive output = BinaryOutputArchive.getArchive(byteoutput);
		
		RequestHeader h = new RequestHeader();
        h.setType(ZooDefs.OpCode.exists);
        
        ExistsRequest request = new ExistsRequest();
        request.setPath("/zk_test");
        request.setWatch(true);
        
        h.serialize(output, "header");
        request.serialize(output, "request");
        ByteBuffer buffer = ByteBuffer.wrap(byteoutput.toByteArray());
        System.out.println(buffer);
        
        ByteArrayInputStream byteInput = new ByteArrayInputStream(buffer.array());
        BinaryInputArchive input = BinaryInputArchive.getArchive(byteInput);
        
        //必须按照写入顺序依次读取
        RequestHeader h2 = new RequestHeader();
		h2.deserialize(input, "header");
		System.out.println(h2.getType());
		
        ExistsRequest request2 = new ExistsRequest();
        request2.deserialize(input, "request");
        System.out.println(request2.getPath());
        
		
	}

}
