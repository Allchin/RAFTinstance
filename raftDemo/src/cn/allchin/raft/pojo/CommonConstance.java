package cn.allchin.raft.pojo;

import java.util.ArrayList;
import java.util.List;

import cn.allchin.raft.cfg.RaftCfg;

/**
 * 所有服务器上持久存在的
 * 
 * currentTerm	服务器最后一次知道的任期号（初始化为 0，持续递增）
votedFor	在当前获得选票的候选人的 Id
log[]	日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号

 * @author renxing.zhang
 *
 */
public class CommonConstance {
	private String currentNodeAddress;
	//
	private int currentTerm;
	private String voteFor;
	private String log[];
	//服务器列表
	private List<String> addr=new ArrayList<String>();
	//运行配置
	private RaftCfg cfg=null;// 
	
	public int getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}
	public String getVoteFor() {
		return voteFor;
	}
	public void setVoteFor(String voteFor) {
		this.voteFor = voteFor;
	}
	public String[] getLog() {
		return log;
	}
	public void setLog(String[] log) {
		this.log = log;
	}
	
	public int increaseTerm(){
		currentTerm=currentTerm+1;
		return currentTerm;
	}
	public String getCurrentNodeAddress() {
		return currentNodeAddress;
	}
	public void setCurrentNodeAddress(String currentNodeAddress) {
		this.currentNodeAddress = currentNodeAddress;
	}
	public List<String> getAddr() {
		return addr;
	}
	public void setAddr(List<String> addr) {
		this.addr = addr;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "|本宝宝上次投票|"+voteFor+"|最后任期号|"+currentTerm+"|@|"+this.currentNodeAddress;
	}
	public RaftCfg getCfg() {
		return cfg;
	}
	public void setCfg(RaftCfg cfg) {
		this.cfg = cfg;
	}
	
}
