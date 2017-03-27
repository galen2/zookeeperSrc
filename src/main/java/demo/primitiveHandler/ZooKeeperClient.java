package demo.primitiveHandler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperClient {
	
	static Logger log = Logger.getLogger(ZooKeeperClient.class);
	
	public static ZooKeeper createZK(String hostPort,String znode){
		try {
			ZooKeeper zk = new ZooKeeper(hostPort, 3000, null);
			return zk;
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
	
	
	public static String getData(ZooKeeper zk ,String znode){
		try {
			byte[] data =  zk.getData(znode, null, null);
			return new String(data);
		} catch (Exception e) {
			log.error(e);
		} 
		return null;
	}
	
}
