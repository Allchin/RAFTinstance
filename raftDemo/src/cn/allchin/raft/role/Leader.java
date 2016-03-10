package cn.allchin.raft.role;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.allchin.raft.log.Logger;
import cn.allchin.raft.log.LoggerFactory;
import cn.allchin.raft.pojo.CommonConstance;
import cn.allchin.raft.rpc.HeartbeatMsg;
import cn.allchin.raft.rpc.Network;

/**
 * @author renxing.zhang
 *  
 */
public class Leader implements State  {
	private Logger logger=LoggerFactory.getLogger(Leader.class);
	private ExecutorService es=Executors.newCachedThreadPool();
	private CommonConstance cc;
 
	
	public Leader(CommonConstance cc ) {
		this.cc=cc; 
		heartbeat(cc.getAddr());
	}
	 

	private void heartbeat(List<String> addr ) {
		 for(final String add:addr){
			 es.submit(new Runnable(){
				 @Override
				public void run() {
					 doHeartbeat(add); 
				}
			 });
			
		 } 
	}
	private void doHeartbeat(String address){
		HeartbeatMsg hbMsg=new HeartbeatMsg();
		hbMsg.setDestAddress(address);
		hbMsg.setTerm(cc.getCurrentTerm());
		Network.sendMsg(hbMsg); 
	}



	@Override
	public State work(CommonConstance cc) {
		logger.info("leader|low1|当前领导是我,准备发送心跳|"+cc);
		this.cc=cc;
		heartbeat( cc.getAddr() );
		
		return this;
	}


	@Override
	public State onHeartbeat(int term) {
		return this;
	}

 
	
	
}
