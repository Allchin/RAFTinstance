package cn.allchin.raft.role;

import java.util.Random;

import cn.allchin.raft.log.Logger;
import cn.allchin.raft.log.LoggerFactory;
import cn.allchin.raft.pojo.CommonConstance;
import cn.allchin.raft.rpc.MessageResult;
import cn.allchin.raft.rpc.Network;
import cn.allchin.raft.rpc.VoteReqMsg;

public class Candidate implements State {
	private Logger logger=LoggerFactory.getLogger(Candidate.class);
	//
	private Random random=new Random();
	private CommonConstance cc;
	 
	/**
	 * ��leader��֪ͨ���
	 */
	private volatile boolean isBreak=false;;
	public Candidate(CommonConstance cc) {
	 this.cc=cc;
	 
	 logger.info("candidate|���췽��"+cc);
	}

	@Override
	public State work(CommonConstance cc) {
		this.cc=cc;
	 
		boolean voteResult=requestAll(); 
		if(voteResult){
			
			if(isBreak){
				//����Լ��ľ�ѡ������ˣ�����Ϊ�Լ�����
				logger.info("candidate|cow1|��ѡ�������"+cc);
				new Follwer(cc);
			}
			logger.info("candidate|cow2|��ѡ�ɹ������leader"+cc);
			return new Leader(cc );
		}
		/**
		 * �����ֿ��ܵĽ���Ǻ�ѡ�˼�û��Ӯ��ѡ��Ҳû���䣺
		 * ����ж��������ͬʱ��Ϊ��ѡ�ˣ�
		 * ��ôѡƱ���ܻᱻ�Ϸ�������û�к�ѡ�˿���Ӯ�ô�����˵�֧�֡�
		 * ���������������ʱ��ÿһ����ѡ�˶��ᳬʱ��Ȼ��ͨ�����ӵ�ǰ���ں�����ʼһ���µ�ѡ�١�
		 * Ȼ����û���������ƵĻ���ѡƱ���ܻᱻ���޵��ظ��Ϸ֡�
		 * 
		 * ���˺ͳ�ʱ����Ϊ�Լ��ǳ�ʱ��Ȼ��׼���´�ѡ�٣�ֱ����leader֪ͨ���ˣ�����Ϊ�Լ����ˣ����follower
		 * */
		logger.info("candidate|cow3|��ѡʧ�ܣ�����"+cc);
		return this;
	}

	/**
	 * <pre>
	 * �������л���ͶƱ���Լ�
	 * 
	 * 
	 * Ϊ����ֹѡƱ����ͱ��Ϸ֣�ѡ�ٳ�ʱʱ���Ǵ�һ���̶������䣨���� 150-300���룩���ѡ�� 
	 * �������԰ѷ���������ɢ���������ڴ���������ֻ��һ����������ѡ�ٳ�ʱ��
	 * Ȼ����Ӯ��ѡ�ٲ���������������ʱ֮ǰ������������ͬ���Ļ��Ʊ�����ѡƱ�Ϸֵ�����¡�
	 * ÿһ����ѡ���ڿ�ʼһ��ѡ�ٵ�ʱ�������һ�������ѡ�ٳ�ʱʱ�䣬Ȼ�����´�ѡ��֮ǰһֱ�ȴ���
	 * �������������µ�ѡ���������ѡƱ�ϷֵĿ����ԡ�
	 * </pre>
	 * 
	 * @return
	 */
	private boolean requestAll() {
		
		try {
			Thread.sleep(random.nextInt(150)+150);
		} catch (InterruptedException e) { }
		logger.info("candidate|ra1|������ͶƱ����"+cc);
		 
		int votes=0;
		for(String node:cc.getAddr()){
			Boolean result=requestVoteMe(node); 
			if(result == null ){
				continue;
			}
			if(result){
				votes++;
			}
		}
		if(votes>=(cc.getAddr().size()/2+1)){
			logger.info("candidate|ra2|��ѡ�ɹ����յ���ҵ�ͶƱ|"+votes+cc);
			return true;
		}
		logger.info("candidate|ra3|��ѡʧ�ܣ��յ���ҵ�ͶƱ|"+votes+cc);
		return false;
	}
	
	/**
	 * true  �Է���ӦͶƱ���Լ�
	 * false �Է��ܾ�
	 * null ��Ӧ��we lost him
	 * @param address
	 * @return
	 */
	private Boolean requestVoteMe(String address){
		VoteReqMsg msg=new VoteReqMsg();
		msg.setDestAddress(address);
		msg.setCandidate(cc.getCurrentNodeAddress());
		msg.setTerm(cc.getCurrentTerm());
		MessageResult result=Network.sendMsg(msg); 
		return result.isSuccess(); 
	}

	/* (non-Javadoc)
	 * @see cn.allchin.raft.role.State#onHeartbeat(int)
	 * �ڵȴ�ͶƱ��ʱ�򣬺�ѡ�˿��ܻ�������ķ��������յ����������쵼�˵ĸ�����־�� RPC��
	 * �������쵼�˵����ںţ������ڴ˴ε� RPC�У���С�ں�ѡ�˵�ǰ�����ںţ�
	 * ��ô��ѡ�˻�����쵼�˺Ϸ����ص�������״̬�� ����˴� RPC �е����ںű��Լ�С��
	 * ��ô��ѡ�˾ͻ�ܾ���ε� RPC ���Ҽ������ֺ�ѡ��״̬��
	 */
	@Override
	public State onHeartbeat(int term) {
		if(term>=cc.getCurrentTerm()){
			isBreak=true;
			logger.info("candidate|cohb1|�յ�������ֹͣ��ѡ��׼����Ϊfollwer"+cc);
			return new Follwer(cc);
		}
		logger.info("candidate|cohb1|�յ����������ԣ�������ѡ|�յ���term|"+term+cc);
		return this;
	}

 
	
}
