package cn.allchin.raft.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import cn.allchin.raft.role.Node;
import cn.allchin.raft.rpc.Network;

public class MockGroupStarter {
	public static ExecutorService es=Executors.newFixedThreadPool(5);
	public static void main(String[] args) {
		List<Node> nodes=getNodeList();
		
		for(final Node n:nodes){ 
			Network.addHandler(n.getCc().getCurrentNodeAddress(), n);
		}
		//
		for(final Node n:nodes ){
			es.submit(new Runnable() {
				
				@Override
				public void run() {
					 n.start(); 
				}
			});
		}
		
		LockSupport.park();
		
	}
	public static List<Node> getNodeList(){ 
		List<Node> result=new ArrayList<Node>();
		for(String address:getNodeAddress()){
			result.add(new Node(address,getNodeAddress()));
		}
		
		return result;
	}
	public static List<String> getNodeAddress(){
		List<String> result=new ArrayList<String>();
		result.add("192.168.1.1:9000");
		result.add("192.168.1.2:9000");
		result.add("192.168.1.3:9000");
		result.add("192.168.1.4:9000");
		result.add("192.168.1.5:9000");
		return  result;
	}
}
