package cn.allchin.raft.rpc;

public class VoteReqMsg implements Message {
	private String destAddress;
	private String candidate ;
	private int term;
	public String getCandidate() { 
		return candidate;
	}

	@Override
	public String getDestAddress() { 
		return destAddress;
	}

	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}

	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}
	
	
}
