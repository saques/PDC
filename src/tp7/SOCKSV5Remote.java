package tp7;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class SOCKSV5Remote implements EndPoint{

	private EndPoint other;
	private ByteBuffer buffer;
	private SelectionKey key;
	private SOCKSV5State state;
	
	public SOCKSV5Remote(EndPoint endPoint) {
		this.buffer = ByteBuffer.allocate(BUFF_SIZE);
		this.other = endPoint;
	}
	
	public void setKey(SelectionKey key){
		this.key = key;
	}
	
	@Override
	public EndPoint getOther() {
		return other;
	}

	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}

	@Override
	public SelectionKey getKey() {
		return key;
	}

	@Override
	public void setState(SOCKSV5State state) {
		this.state = state;
	}

	@Override
	public SOCKSV5State getState() {
		return state;
	}

}
