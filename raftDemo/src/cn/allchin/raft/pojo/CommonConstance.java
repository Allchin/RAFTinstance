package cn.allchin.raft.pojo;

import java.util.ArrayList;
import java.util.List;

import cn.allchin.raft.cfg.RaftCfg;

/**
 * ���з������ϳ־ô��ڵ�
 * 
 * currentTerm	���������һ��֪�������ںţ���ʼ��Ϊ 0������������
votedFor	�ڵ�ǰ���ѡƱ�ĺ�ѡ�˵� Id
log[]	��־��Ŀ����ÿһ����Ŀ����һ���û�״̬��ִ�е�ָ����յ�ʱ�����ں�

 * @author renxing.zhang
 *
 */
public class CommonConstance {
	private String currentNodeAddress;
	//
	private int currentTerm;
	private String voteFor;
	private String log[];
	//�������б�
	private List<String> addr=new ArrayList<String>();
	//��������
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
		return "|�������ϴ�ͶƱ|"+voteFor+"|������ں�|"+currentTerm+"|@|"+this.currentNodeAddress;
	}
	public RaftCfg getCfg() {
		return cfg;
	}
	public void setCfg(RaftCfg cfg) {
		this.cfg = cfg;
	}
	
}
