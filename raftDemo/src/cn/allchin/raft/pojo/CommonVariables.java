package cn.allchin.raft.pojo;

/**
 * ״̬	���з������Ͼ������
commitIndex	��֪�������Ѿ����ύ����־��Ŀ������ֵ
lastApplied	���Ӧ�õ�״̬������־��Ŀ����ֵ����ʼ��Ϊ 0������������
 * @author renxing.zhang
 *
 */
public class CommonVariables {
	private Integer commitIndex;
	private int lastApplied;
	public Integer getCommitIndex() {
		return commitIndex;
	}
	public void setCommitIndex(Integer commitIndex) {
		this.commitIndex = commitIndex;
	}
	public int getLastApplied() {
		return lastApplied;
	}
	public void setLastApplied(int lastApplied) {
		this.lastApplied = lastApplied;
	}
	
}
