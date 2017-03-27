package org.apache.zookeeper.server.persisitence;

import org.apache.zookeeper.server.persistence.Util;
import org.junit.Test;

import client.BaseTest;

public class UtilTest extends BaseTest{

	@Test
	public void makeSnapshotNameTest(){
		long zxid = 4232L;
		String result = Util.makeSnapshotName(zxid);
		print(result);
	}
}
