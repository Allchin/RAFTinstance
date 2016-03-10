package cn.allchin.raft.role;

import java.util.Random;

import cn.allchin.raft.log.Logger;
import cn.allchin.raft.log.LoggerFactory;
import cn.allchin.raft.pojo.CommonConstance;
import cn.allchin.raft.rpc.MessageResult;
import cn.allchin.raft.rpc.Network;
import cn.allchin.raft.rpc.VoteReqMsg;

public class Candidate implements State {
	private Logger logger=LoggerFactory.getLogger(Candidate.class);
	//
	private Random random=new Random();
	private CommonConstance cc;
	 
	/**
	 * 被leader的通知打断
	 */
	private volatile boolean isBreak=false;;
	public Candidate(CommonConstance cc) {
	 this.cc=cc;
	 
	 logger.info("candidate|构造方法"+cc);
	}

	@Override
	public State work(CommonConstance cc) {
		this.cc=cc;
	 
		boolean voteResult=requestAll(); 
		if(voteResult){
			
			if(isBreak){
				//如果自己的竞选被打断了，则认为自己输了
				logger.info("candidate|cow1|竞选被打断了"+cc);
				new Follwer(cc);
			}
			logger.info("candidate|cow2|竞选成功，变成leader"+cc);
			return new Leader(cc );
		}
		/**
		 * 第三种可能的结果是候选人既没有赢得选举也没有输：
		 * 如果有多个跟随者同时成为候选人，
		 * 那么选票可能会被瓜分以至于没有候选人可以赢得大多数人的支持。
		 * 当这种情况发生的时候，每一个候选人都会超时，然后通过增加当前任期号来开始一轮新的选举。
		 * 然而，没有其他机制的话，选票可能会被无限的重复瓜分。
		 * 
		 * 输了和超时都认为自己是超时，然后准备下次选举，直到被leader通知输了，才认为自己输了，变成follower
		 * */
		logger.info("candidate|cow3|竞选失败，不变"+cc);
		return this;
	}

	/**
	 * <pre>
	 * 请求所有机器投票给自己
	 * 
	 * 
	 * 为了阻止选票起初就被瓜分，选举超时时间是从一个固定的区间（例如 150-300毫秒）随机选择。 
	 * 这样可以把服务器都分散开以至于在大多数情况下只有一个服务器会选举超时；
	 * 然后他赢得选举并在其他服务器超时之前发送心跳包。同样的机制被用在选票瓜分的情况下。
	 * 每一个候选人在开始一次选举的时候会重置一个随机的选举超时时间，然后在下次选举之前一直等待；
	 * 这样减少了在新的选举中另外的选票瓜分的可能性。
	 * </pre>
	 * 
	 * @return
	 */
	private boolean requestAll() {
		
		try {
			Thread.sleep(random.nextInt(150)+150);
		} catch (InterruptedException e) { }
		logger.info("candidate|ra1|请求大家投票给我"+cc);
		 
		int votes=0;
		for(String node:cc.getAddr()){
			Boolean result=requestVoteMe(node); 
			if(result == null ){
				continue;
			}
			if(result){
				votes++;
			}
		}
		if(votes>=(cc.getAddr().size()/2+1)){
			logger.info("candidate|ra2|竞选成功，收到大家的投票|"+votes+cc);
			return true;
		}
		logger.info("candidate|ra3|竞选失败，收到大家的投票|"+votes+cc);
		return false;
	}
	
	/**
	 * true  对方答应投票给自己
	 * false 对方拒绝
	 * null 无应答，we lost him
	 * @param address
	 * @return
	 */
	private Boolean requestVoteMe(String address){
		VoteReqMsg msg=new VoteReqMsg();
		msg.setDestAddress(address);
		msg.setCandidate(cc.getCurrentNodeAddress());
		msg.setTerm(cc.getCurrentTerm());
		MessageResult result=Network.sendMsg(msg); 
		return result.isSuccess(); 
	}

	/* (non-Javadoc)
	 * @see cn.allchin.raft.role.State#onHeartbeat(int)
	 * 在等待投票的时候，候选人可能会从其他的服务器接收到声明它是领导人的附加日志项 RPC。
	 * 如果这个领导人的任期号（包含在此次的 RPC中）不小于候选人当前的任期号，
	 * 那么候选人会承认领导人合法并回到跟随者状态。 如果此次 RPC 中的任期号比自己小，
	 * 那么候选人就会拒绝这次的 RPC 并且继续保持候选人状态。
	 */
	@Override
	public State onHeartbeat(int term) {
		if(term>=cc.getCurrentTerm()){
			isBreak=true;
			logger.info("candidate|cohb1|收到心跳，停止竞选，准备成为follwer"+cc);
			return new Follwer(cc);
		}
		logger.info("candidate|cohb1|收到心跳并忽略，继续竞选|收到的term|"+term+cc);
		return this;
	}

 
	
}
