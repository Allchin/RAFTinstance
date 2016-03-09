package cn.allchin.raft.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
	private static Map<String ,MessageHandler> nodeCacheMap=new HashMap<String,MessageHandler>();
	public static List<MessageResult> sendGroupMsg(Message msg){
		List<MessageResult>  resultList=new ArrayList<MessageResult>();
		for(String node:nodeCacheMap.keySet()){
			MessageHandler handler=nodeCacheMap.get(node);
			resultList.add( handler.handler(msg));
		}
		
		return resultList; 
	}
	public static MessageResult sendMsg(Message msg){
		MessageResult result=new MessageResult();
		MessageHandler handler=nodeCacheMap.get(msg.getDestAddress());
		result= handler.handler(msg) ;
		return result;
	}
	
	public static void addHandler(String key,MessageHandler handler){
		nodeCacheMap.put(key, handler);
	}
}
