package cn.allchin.raft.role;

import cn.allchin.raft.log.Logger;
import cn.allchin.raft.log.LoggerFactory;
import cn.allchin.raft.pojo.CommonConstance;

public class Follwer implements State {
	//
	private Logger logger=LoggerFactory.getLogger(Follwer.class);
	//
	private CommonConstance cc;
	private long timeout=3000;
	private long lastLeaderHBtime=0;
	public Follwer(CommonConstance cc ) {
		logger.info("follower|fc1|构造follower");
		this.cc=cc;
	}

	@Override
	public State work(CommonConstance cc) {
		this.cc=cc;
		//是否选举超时
		if(isVoteTimeout()){
			logger.info("follower|fw1|选举超时,转为候选人");
			cc.increaseTerm();
			return new Candidate(cc);
		} 
		logger.info("follower|fw2|选举没有超时,依然是follower");
		return this;
	}

	private boolean isVoteTimeout() {
		boolean isTimeout=System.currentTimeMillis()-timeout>lastLeaderHBtime;
		if(isTimeout){
			//一旦超时，就清理自己的投票状态，准备选举或者给别人投票
			cc.setVoteFor(null);
			return true;
		}
		return false;
	}

	@Override
	public State onHeartbeat(int term) {
		//更新最后一次heartbeat时间
		long current=System.currentTimeMillis();
		lastLeaderHBtime=current;
		logger.info("follower|onhb1|收到心跳|"+term);
		return this;
	}
	
	

}
