package cn.allchin.raft.log;

import java.util.Date;

public class SysoLogger implements Logger {
	public void info(String msg){
		
		System.out.println("|DATE|"+new Date()+"|"+msg);
	}
}
