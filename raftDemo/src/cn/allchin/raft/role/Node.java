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
		logger.info("node|nr1|�ڵ��ɫ|"+state.getClass().getSimpleName()+"|׼������|"+cc);
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
	 * ���term < currentTerm���� false ��5.2 �ڣ�
��� votedFor Ϊ�ջ��߾��� candidateId�����Һ�ѡ�˵���־Ҳ�Լ�һ���£���ô��ͶƱ������5.2 �ڣ�5.4 �ڣ�
	 */
	@Override
	public boolean vote(VoteReqMsg msg) {
		String candidate=msg.getCandidate();
		if(msg.getTerm()<cc.getCurrentTerm()){
			logger.info("node|nv0|�ڵ�ͶƱ|��Ϣterm|"+msg.getTerm()+"|��ΪtermС�ڱ���term�ܾ�|"+candidate+cc);
			return false;
		}
		
		//����Ѿ�ͶƱ�Ͳ�Ͷ��
		if(cc.getVoteFor() == null){
			cc.setVoteFor(candidate);
			logger.info("node|nv1|�ڵ�ͶƱ|"+cc.getCurrentNodeAddress()+"|֧��|"+candidate);
			return true;
		}
		 
		
		logger.info("node|nv2|�ڵ�ͶƱ|"+cc.getCurrentNodeAddress()+"|�ܾ�|"+candidate);
		return false;
	}

	/* (non-Javadoc)
	 * @see cn.allchin.raft.rpc.MessageHandler#handler(cn.allchin.raft.rpc.Message)
	 * ����������Ϣ
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
