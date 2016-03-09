package cn.allchin.raft.pojo;

public class AttachLogResp {
	private String term;
	private boolean success;
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
