package cn.allchin.raft.role;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.allchin.raft.log.Logger;
import cn.allchin.raft.log.LoggerFactory;
import cn.allchin.raft.pojo.CommonConstance;
import cn.allchin.raft.rpc.HeartbeatMsg;
import cn.allchin.raft.rpc.Message;
import cn.allchin.raft.rpc.MessageHandler;
import cn.allchin.raft.rpc.MessageResult;
import cn.allchin.raft.rpc.VoteReqMsg;

public class Node implements   Runnable,Voter,MessageHandler {
	

	private Logger logger=LoggerFactory.getLogger(Node.class);  
	//
	private CommonConstance cc=new CommonConstance();
	private State state=new Follwer(cc);
	private ScheduledExecutorService pool=Executors.newScheduledThreadPool(1);
	public Node(String localAddress,List<String> nodeList) {
		cc.setAddr(nodeList); 
		cc.setCurrentNodeAddress(localAddress);
	}
	public void run(){
		logger.info("nr1|节点状态|"+state+"|准备工作|"+cc);
		state=state.work(cc);
	}
	
	public void start(){
		state=state.work(cc);
		pool.scheduleWithFixedDelay(this, 0, 5000, TimeUnit.SECONDS);
	}
	
  
	@Override
	public boolean vote(String candidate) {
		//如果已经投票就不投了
		if(cc.getVoteFor() == null){
			cc.setVoteFor(candidate);
			logger.info("nv1|节点|"+cc.getCurrentNodeAddress()+"|投票给|"+candidate);
			return true;
		}
		logger.info("nv2|节点|"+cc.getCurrentNodeAddress()+"|拒绝投票|");
		return false;
	}

	@Override
	public MessageResult handler(Message msg) {
		MessageResult mr=new MessageResult();
		if(msg instanceof VoteReqMsg){
			VoteReqMsg voteReq=(VoteReqMsg)msg;
			boolean voted=vote(voteReq.getCandidate());
			mr.setSuccess(voted);
		}
		if(msg instanceof HeartbeatMsg){
			HeartbeatMsg heartbeat=(HeartbeatMsg)msg;
			State destState=state.onHeartbeat(heartbeat.getTerm());
			boolean roleChanged= !(destState.equals(state));
			mr.setSuccess( roleChanged);
		}
		
		return mr;
	}
	public CommonConstance getCc() {
		return cc;
	}
	public void setCc(CommonConstance cc) {
		this.cc = cc;
	}
	
 
}
