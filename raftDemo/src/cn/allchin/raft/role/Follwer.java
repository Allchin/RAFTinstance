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
		logger.info("follower|fc1|����follower");
		this.cc=cc;
	}

	@Override
	public State work(CommonConstance cc) {
		this.cc=cc;
		//�Ƿ�ѡ�ٳ�ʱ
		if(isVoteTimeout()){
			logger.info("follower|fw1|ѡ�ٳ�ʱ,תΪ��ѡ��");
			cc.increaseTerm();
			return new Candidate(cc);
		} 
		logger.info("follower|fw2|ѡ��û�г�ʱ,��Ȼ��follower");
		return this;
	}

	private boolean isVoteTimeout() {
		boolean isTimeout=System.currentTimeMillis()-timeout>lastLeaderHBtime;
		if(isTimeout){
			//һ����ʱ���������Լ���ͶƱ״̬��׼��ѡ�ٻ��߸�����ͶƱ
			cc.setVoteFor(null);
			return true;
		}
		return false;
	}

	@Override
	public State onHeartbeat(int term) {
		//�������һ��heartbeatʱ��
		long current=System.currentTimeMillis();
		lastLeaderHBtime=current;
		logger.info("follower|onhb1|�յ�����|"+term);
		return this;
	}
	
	

}
