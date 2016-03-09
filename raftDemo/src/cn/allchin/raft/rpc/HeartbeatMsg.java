package cn.allchin.raft.rpc;

public class HeartbeatMsg implements Message {
	private String destAddress;
	private int term;
	@Override
	public String getDestAddress() {
		 
		return destAddress;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}
	
}
