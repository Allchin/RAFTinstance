package cn.allchin.raft.cfg;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RaftCfg {
	public static final String heartBeatDelay="heartBeatDelay";
	public static final String heartBeatTimeout="heartBeatTimeout";
	//
	private ConcurrentHashMap<String,Object> cfgMap=new ConcurrentHashMap<String,Object>();
	 
	/**
	 * ����Ĭ��ֵ
	 */
	public RaftCfg() {
		cfgMap.put(heartBeatDelay, 5000);//����
		cfgMap.put(heartBeatTimeout, 3000);//����
		//
		init();
	}
	/**
	 * �Զ����ʼ��
	 */
	private void init() {
		// TODO Auto-generated method stub
		
	}
	public String  getStringValue(String key){
		Object obj=getValue(key);
		if(obj !=null ){
			return (String)obj;
		}
		return null;
	}
	
	public List  getListValue(String key){
		Object obj=getValue(key);
		if(obj !=null ){
			return (List)obj;
		}
		return null;
	}
	
	public Integer getIntValue(String key){
		Object obj=getValue(key);
		if(obj !=null ){
			return (Integer)obj;
		}
		return null;
	}
	private Object getValue(String key){
		return cfgMap.get(key);
	}
}
