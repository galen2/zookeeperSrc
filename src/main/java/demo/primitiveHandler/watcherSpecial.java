package demo.primitiveHandler;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

public class watcherSpecial implements Watcher{

	
	public ZooKeeper zk  = null;
	
	public String znode = null;
	
	
	public watcherSpecial(ZooKeeper zk,String znode){
		this.zk = zk ;
		this.znode = znode;
	}
	
	
	@Override
	public void process(WatchedEvent event) {
		String path = event.getPath();
		if (event.getType() == Event.EventType.None) {
			// We are are being told that the state of the
			// connection has changed
			switch (event.getState()) {
			case SyncConnected:
				// In this particular example we don't need to do anything
				// here - watches are automatically re-registered with
				// server and any watches triggered while the client was
				// disconnected will be delivered (in order of course)
				break;
			case Expired:
				// It's all over
				break;
			}
		} else {
			if (path != null && path.equals(znode)) {
				try {
//					zk.exists(znode, true,new statCallback(),null);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	

}
