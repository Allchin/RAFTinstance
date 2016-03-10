package cn.allchin.raft.role;

import cn.allchin.raft.rpc.VoteReqMsg;

/**
 * 选民
 * @author renxing.zhang
 *
 */
public interface Voter {
	/**
	 * 是否投票给这个候选人
	 * @return
	 */
	public boolean vote(VoteReqMsg msg);
}
