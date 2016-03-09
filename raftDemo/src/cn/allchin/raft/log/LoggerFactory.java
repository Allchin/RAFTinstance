package cn.allchin.raft.log;

public class LoggerFactory {
	
	public static Logger getLogger(Class clazz){
		return new Logger();
	}
}
