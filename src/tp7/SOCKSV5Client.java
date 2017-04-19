package tp7;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SOCKSV5Client {
	private static final int BUFF_SIZE = 1024;
	
	private SOCKSV5State state;
	private ByteBuffer buffer;
	
	SelectionKey key;
	
	Byte version;
	Byte selectedMethod;

	/**
	 * IF RESOLVED
	 */
	InetAddress targetAddr;
	int targetPort;

	
	byte status;
	byte addrType = SOCKSV5Protocol.IPv4;



	public SOCKSV5Client(){
		this.state = SOCKSV5State.RCV_METHODS;
		this.buffer = ByteBuffer.allocate(BUFF_SIZE);
	}
	
	void setState(SOCKSV5State state){
		this.state = state;
	}
	
	SOCKSV5State getState(){
		return state;
	}
	
	ByteBuffer getBuffer(){
		return buffer;
	}
	
	
}
