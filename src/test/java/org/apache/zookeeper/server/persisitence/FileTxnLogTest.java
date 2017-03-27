package org.apache.zookeeper.server.persisitence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.apache.jute.BinaryOutputArchive;
import org.apache.zookeeper.server.persistence.FileHeader;
import org.apache.zookeeper.server.persistence.FileTxnLog;
import org.apache.zookeeper.server.persistence.Util;
import org.junit.Test;

public class FileTxnLogTest {
	
//	String baseData = "/Users/yourmall/Documents/DataMessage/zkdata/";
	String baseData = "F:/bingOnlySpacedatatmpzookeeper/";
    public final static String version = "version-";
    static long preAllocSize =   65536 * 1024;

	
	@Test
	public void readLogFileTest()throws Exception{
		String logFileURL = "F:\\bingOnlySpace\\getHubWorkSpace\\zookeeperSrcTest\\zookeepreSrcTest\\build\\test4121201150035403600.junit.dir\\data\\version-2\\log.1";
		File logFile = new File(logFileURL);
		BufferedInputStream bufferInputStream = new BufferedInputStream(new FileInputStream(logFile));
		DataInputStream dataInputStream = new DataInputStream(bufferInputStream);
		int magic = dataInputStream.readInt();
		System.out.println(magic);
		
		int version = dataInputStream.readInt();
		System.out.println(version);
		
		long dbid = dataInputStream.readLong();
		System.out.println(dbid);
		
		long crcvalue = dataInputStream.readLong();
		System.out.println(crcvalue);
		
		int txtEntry = dataInputStream.readInt();
		System.out.println(txtEntry);
		
		 byte[] arr = new byte[txtEntry];
		 dataInputStream.readFully(arr);
		 System.out.println(arr.length);
	}
	
	@Test
	public void apendFirstAppend() throws Exception{
		File dataDir = new File(baseData,version+"test");
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}
		File logFileWrite = new File(dataDir, ("log." + Long.toHexString(55L)));
		FileOutputStream fos = new FileOutputStream(logFileWrite);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
//		byte[] one = new byte[]{1,2};
//		bos.write(1);
		
		BinaryOutputArchive oa = BinaryOutputArchive.getArchive(bos);
		FileHeader fhdr = new FileHeader(FileTxnLog.TXNLOG_MAGIC,FileTxnLog.VERSION, 3L);
        fhdr.serialize(oa, "fileheader");
		bos.flush();
		long currentSize = fos.getChannel().position();
		System.out.println(currentSize);
	}
	
 	@Test
	public void apendSecondAppend() throws Exception{
		File dataDir = new File(baseData,version+"test");
	    if (!dataDir.exists()) {
	    	dataDir.mkdirs();
        }
		File logFileWrite = new File(dataDir, ("log." + Long.toHexString(55L)));
		FileOutputStream fos = new FileOutputStream(logFileWrite);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] buf = new byte[]{1,2};
//		bos.write(1);
//		
		BinaryOutputArchive oa = BinaryOutputArchive.getArchive(bos);
		/*FileHeader fhdr = new FileHeader(FileTxnLog.TXNLOG_MAGIC,FileTxnLog.VERSION, 3L);
        fhdr.serialize(oa, "fileheader");
        bos.flush();*/
        
        Checksum crc = new Adler32();
        crc.update(buf, 0, buf.length);
        oa.writeLong(crc.getValue(), "txnEntryCRC");
        Util.writeTxnBytes(oa, buf);
//        bos.flush();
	}
	
	
	@Test
	public void append(){
		try {
			File dataDir = new File(baseData,version+"test");
		    if (!dataDir.exists()) {
	            if (!dataDir.mkdirs()) {
	            }
	        }
			
			File logFileWrite = new File(dataDir, ("log." + Long.toHexString(55L)));
			FileOutputStream fos = new FileOutputStream(logFileWrite);
			BufferedOutputStream logStream=new BufferedOutputStream(fos);
			BinaryOutputArchive oa = BinaryOutputArchive.getArchive(logStream);
	        FileHeader fhdr = new FileHeader(FileTxnLog.TXNLOG_MAGIC,FileTxnLog.VERSION, 3L);
	        fhdr.serialize(oa, "fileheader");
	        // Make sure that the magic number is written before padding.
	        logStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void padFileTest() throws Exception{
		File dataDir = new File(baseData,version+"test");
		if (!dataDir.exists()) {
			dataDir.mkdirs();
		}
		File logFileWrite = new File(dataDir, ("log." + Long.toHexString(55L)));
		FileOutputStream fos = new FileOutputStream(logFileWrite);
		byte[] one = new byte[]{1,2,3};
		fos.write(one);
		FileChannel channel = fos.getChannel();
		long possition = channel.position();
		System.out.println(possition);
		Util.padLogFile(fos, possition, preAllocSize);
		System.out.println(channel.position());
		//在次写入
		fos.write(one);
		System.out.println(channel.position());

//		 Util.padLogFile(fos, currentSize, preAllocSize);
		 
//		BufferedOutputStream bos = new BufferedOutputStream(fos);
//		
//		BinaryOutputArchive oa = BinaryOutputArchive.getArchive(bos);
	}
	
	@Test
	public void FileChannelTest() throws Exception{
		RandomAccessFile aFile = new RandomAccessFile(baseData+"/nio-data.txt", "rw");
		FileChannel inChannel = aFile.getChannel();
		String newData = "New String to write to file..." + System.currentTimeMillis();

	/*	ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();*/
		ByteBuffer fill = ByteBuffer.allocateDirect(1);
		byte b = 2;
		fill.put(b);
//		fill.flip();
		fill.position(0);
		System.out.println(fill.remaining());
		inChannel.write(fill,101);
		System.out.println(inChannel.position());
		/*while(buf.hasRemaining()) {
			inChannel.write(buf);
		}*/
		
	}
}
