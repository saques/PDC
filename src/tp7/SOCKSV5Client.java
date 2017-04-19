package tp7;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SOCKSV5Client implements EndPoint {
	
	private SOCKSV5State state;
	private ByteBuffer buffer;
    private SelectionKey key;
	
	Byte version;
	Byte selectedMethod;
	/**
	 * IF RESOLVED
	 */
	InetAddress targetAddr;
	int targetPort;
	byte status;
	byte addrType = SOCKSV5Protocol.IPv4;

	
	private EndPoint other;
	public SOCKSV5Client(){
		this.state = SOCKSV5State.RCV_METHODS;
		this.buffer = ByteBuffer.allocate(BUFF_SIZE);
	}
	
	public SOCKSV5Client(EndPoint endPoint){
		this();
		this.other = endPoint;
	}
	
	public void setState(SOCKSV5State state){
		this.state = state;
	}
	
	void setEndPoint(EndPoint other){
		this.other = other;
	}
	
	public SOCKSV5State getState(){
		return state;
	}
	
	public ByteBuffer getBuffer(){
		return buffer;
	}

	@Override
	public EndPoint getOther() {
		return other;
	}

	@Override
	public SelectionKey getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	public void setKey(SelectionKey register) {
		this.key = register;
		
	}
	
	
}
