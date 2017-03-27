package demo.watchExecutor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class Executor implements Watcher, Runnable, DataMonitorListener {
	
	
	String znode;

	DataMonitor dm;

	ZooKeeper zk;

	String filename;

	String exec[];

	Process child;

	public Executor(String hostPort, String znode, String filename, String exec[]) throws KeeperException, IOException {
		this.filename = filename;
		this.exec = exec;
		this.znode = znode;
		zk = new ZooKeeper(hostPort, 3000, this);
		dm = new DataMonitor(zk, znode, null, this);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String hostPort = "192.168.33.14:2181";
		String znode = "/zk_test";
		String filename = "consumers";
		String exec[] = { hostPort, znode, filename };
		try {
			new Executor(hostPort, znode, filename, exec).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * We do process any events ourselves, we just need to forward them on.
	 * 
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
	 * 当数据发生变化的时候，触发这个函数
	 */
	public void process(WatchedEvent event) {
		dm.process(event);
	}

	public void run() {
		try {
			synchronized (this) {
				while (!dm.dead) {
					wait();
				}
			}
		} catch (InterruptedException e) {
		}
	}

	public void closing(int rc) {
		synchronized (this) {
			notifyAll();
		}
	}

	static class StreamWriter extends Thread {
		OutputStream os;

		InputStream is;

		StreamWriter(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
			start();
		}

		public void run() {
			byte b[] = new byte[80];
			int rc;
			try {
				while ((rc = is.read(b)) > 0) {
					os.write(b, 0, rc);
				}
			} catch (IOException e) {
			}

		}
	}

	public void exists(byte[] data) {
		if (data == null) {
			if (child != null) {
				System.out.println("Killing process");
				child.destroy();
				try {
					child.waitFor();
				} catch (InterruptedException e) {
				}
			}
			child = null;
		} else {
			if (child != null) {
				System.out.println("Stopping child");
				child.destroy();
				try {
					child.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				FileOutputStream fos = new FileOutputStream(filename);
				System.out.println(new String(data));
				fos.write(data);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			 * try { System.out.println("Starting child"); child =
			 * Runtime.getRuntime().exec(exec); new
			 * StreamWriter(child.getInputStream(), System.out); new
			 * StreamWriter(child.getErrorStream(), System.err); } catch
			 * (IOException e) { e.printStackTrace(); }
			 */
		}
	}
	

	public void test() {
		try {
			byte[] data = zk.getData(znode, this, null);
			System.out.println(new String(data));
			/*List<String> children = zk.getChildren("/otter/canal/destinations", false);
			for (String aa : children) {
				
			}*/
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
