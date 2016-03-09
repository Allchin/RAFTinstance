package cn.allchin.raft.pojo;

/**
 * 状态	所有服务器上经常变的
commitIndex	已知的最大的已经被提交的日志条目的索引值
lastApplied	最后被应用到状态机的日志条目索引值（初始化为 0，持续递增）
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
