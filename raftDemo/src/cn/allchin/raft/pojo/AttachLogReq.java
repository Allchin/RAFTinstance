package cn.allchin.raft.pojo;

public class AttachLogReq {
	private int term;
	private String leaderId;
	private Integer prevLogIndex;
	private String[] entries;
	private Integer leaderCommit;
	
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public Integer getPrevLogIndex() {
		return prevLogIndex;
	}
	public void setPrevLogIndex(Integer prevLogIndex) {
		this.prevLogIndex = prevLogIndex;
	}
	public String[] getEntries() {
		return entries;
	}
	public void setEntries(String[] entries) {
		this.entries = entries;
	}
	public Integer getLeaderCommit() {
		return leaderCommit;
	}
	public void setLeaderCommit(Integer leaderCommit) {
		this.leaderCommit = leaderCommit;
	}
	
}
