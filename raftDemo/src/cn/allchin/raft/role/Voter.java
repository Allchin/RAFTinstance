package cn.allchin.raft.role;

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
	public boolean vote(String candidate);
}
