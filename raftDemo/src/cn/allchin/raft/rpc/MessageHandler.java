package cn.allchin.raft.rpc;

public interface MessageHandler {
	public MessageResult handler(Message msg);
}
