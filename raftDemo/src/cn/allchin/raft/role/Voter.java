package cn.allchin.raft.role;

import cn.allchin.raft.rpc.VoteReqMsg;

/**
 * ѡ��
 * @author renxing.zhang
 *
 */
public interface Voter {
	/**
	 * �Ƿ�ͶƱ�������ѡ��
	 * @return
	 */
	public boolean vote(VoteReqMsg msg);
}
