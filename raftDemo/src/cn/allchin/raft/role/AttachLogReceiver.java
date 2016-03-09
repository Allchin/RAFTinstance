package cn.allchin.raft.role;

import cn.allchin.raft.pojo.AttachLogReq;
import cn.allchin.raft.pojo.CommonConstance;
import cn.allchin.raft.pojo.CommonVariables;

/**
 * ������־��
 * ������
 * @author renxing.zhang
 *
 */
public class AttachLogReceiver  {
	private CommonConstance cc=new CommonConstance();
	private CommonVariables cv=new CommonVariables();
	
	/**
	 * 
	 * 
��� term < currentTerm �ͷ��� false ��5.1 �ڣ�
�����־�� prevLogIndex λ�ô�����־��Ŀ�����ںź� prevLogTerm ��ƥ�䣬�򷵻� false ��5.3 �ڣ�
����Ѿ��Ѿ����ڵ���־��Ŀ���µĲ�����ͻ����ͬƫ�����������ںŲ�ͬ����ɾ����һ����֮�����е� ��5.3 �ڣ�
�����κ������е���־�в����ڵ���Ŀ
��� leaderCommit > commitIndex���� commitIndex ���� leaderCommit �� ����־��Ŀ����ֵ�н�С��һ��
	 * @param req
	 */
	public boolean handle(AttachLogReq req){
		if(req.getTerm()<cc.getCurrentTerm()){
			return false;
		}
		
		
		return false;
	}

	public CommonConstance getCc() {
		return cc;
	}

	public void setCc(CommonConstance cc) {
		this.cc = cc;
	}

	public CommonVariables getCv() {
		return cv;
	}

	public void setCv(CommonVariables cv) {
		this.cv = cv;
	}
	
	
}
