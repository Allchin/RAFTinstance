package cn.allchin.raft.role;

import cn.allchin.raft.pojo.AttachLogReq;
import cn.allchin.raft.pojo.CommonConstance;
import cn.allchin.raft.pojo.CommonVariables;

/**
 * 附加日志的
 * 接收者
 * @author renxing.zhang
 *
 */
public class AttachLogReceiver  {
	private CommonConstance cc=new CommonConstance();
	private CommonVariables cv=new CommonVariables();
	
	/**
	 * 
	 * 
如果 term < currentTerm 就返回 false （5.1 节）
如果日志在 prevLogIndex 位置处的日志条目的任期号和 prevLogTerm 不匹配，则返回 false （5.3 节）
如果已经已经存在的日志条目和新的产生冲突（相同偏移量但是任期号不同），删除这一条和之后所有的 （5.3 节）
附加任何在已有的日志中不存在的条目
如果 leaderCommit > commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
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
