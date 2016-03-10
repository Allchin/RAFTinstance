package cn.allchin.raft.role;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.allchin.raft.cfg.RaftCfg;
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
	public Node(String localAddress,List<String> nodeList,RaftCfg cfg) {
		cc.setAddr(nodeList); 
		cc.setCurrentNodeAddress(localAddress);
		cc.setCfg(cfg);
	}
	public void run(){
		logger.info("node|nr1|节点角色|"+state.getClass().getSimpleName()+"|准备工作|"+cc);
		state=state.work(cc);
	}
	
	public void start(){
		state=state.work(cc);
		int delay=cc.getCfg().getIntValue(RaftCfg.heartBeatDelay);
		pool.scheduleWithFixedDelay(this, 0, delay, TimeUnit.MILLISECONDS);
	}
	
  
	/* (non-Javadoc)
	 * @see cn.allchin.raft.role.Voter#vote(java.lang.String)
	 * 
	 * 如果term < currentTerm返回 false （5.2 节）
如果 votedFor 为空或者就是 candidateId，并且候选人的日志也自己一样新，那么就投票给他（5.2 节，5.4 节）
	 */
	@Override
	public boolean vote(VoteReqMsg msg) {
		String candidate=msg.getCandidate();
		if(msg.getTerm()<cc.getCurrentTerm()){
			logger.info("node|nv0|节点投票|消息term|"+msg.getTerm()+"|因为term小于本地term拒绝|"+candidate+cc);
			return false;
		}
		
		//如果已经投票就不投了
		if(cc.getVoteFor() == null){
			cc.setVoteFor(candidate);
			logger.info("node|nv1|节点投票|"+cc.getCurrentNodeAddress()+"|支持|"+candidate);
			return true;
		}
		 
		
		logger.info("node|nv2|节点投票|"+cc.getCurrentNodeAddress()+"|拒绝|"+candidate);
		return false;
	}

	/* (non-Javadoc)
	 * @see cn.allchin.raft.rpc.MessageHandler#handler(cn.allchin.raft.rpc.Message)
	 * 处理网络消息
	 */
	@Override
	public MessageResult handler(Message msg) {
		MessageResult mr=new MessageResult();
		if(msg instanceof VoteReqMsg){
			VoteReqMsg voteReq=(VoteReqMsg)msg;
			boolean voted=vote(voteReq);
			mr.setSuccess(voted);
		}
		if(msg instanceof HeartbeatMsg){
			HeartbeatMsg heartbeat=(HeartbeatMsg)msg;
			State destState=state.onHeartbeat(heartbeat.getTerm());
			boolean roleChanged= !(destState.equals(state)); 
			mr.setSuccess( roleChanged);
			if(roleChanged){
				state=destState;
			}
		}
		
		return mr;
	}
	public void shutdown(){
		pool.shutdown();
	}
	
	public CommonConstance getCc() {
		return cc;
	}
	public void setCc(CommonConstance cc) {
		this.cc = cc;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
 
}
