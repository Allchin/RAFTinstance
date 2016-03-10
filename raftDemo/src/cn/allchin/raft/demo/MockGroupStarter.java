package cn.allchin.raft.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import cn.allchin.raft.cfg.RaftCfg;
import cn.allchin.raft.log.Logger;
import cn.allchin.raft.log.LoggerFactory;
import cn.allchin.raft.role.Follwer;
import cn.allchin.raft.role.Leader;
import cn.allchin.raft.role.Node;
import cn.allchin.raft.rpc.Network;

public class MockGroupStarter {
	private static Logger logger=LoggerFactory.getLogger(MockGroupStarter.class);
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
		List<Node> removed=new ArrayList<Node>(3);
		for(int i=0;i<3;i++){
			//每10s的时间，将leader线程池停止掉
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) { }
			for(final Node n:nodes ){
				if(n.getState() instanceof Leader){
					if(!removed.contains(n)){
						n.shutdown();
						logger.info("mockGS1|关闭leader"+n.getCc());
						removed.add(n);
					} 
				}
			}
		}
		
		
		LockSupport.park();
		
	}
	public static List<Node> getNodeList(){ 
		List<Node> result=new ArrayList<Node>();
		for(String address:getNodeAddress()){
			result.add(new Node(address,getNodeAddress(),new RaftCfg()));
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
