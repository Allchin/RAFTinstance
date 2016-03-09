package cn.allchin.raft.role;

import cn.allchin.raft.pojo.CommonConstance;

public interface State {
	 public State work(CommonConstance cc);
	 public State onHeartbeat(int term);
	 
	 
}
