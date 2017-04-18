package tp7;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SOCKSV5Client {
	private static final int BUFF_SIZE = 1024;
	
	/**
	 * To establish two way connection
	 */
	private Socket socket;
	
	private SOCKSV5State state;
	private ByteBuffer buffer;

	Byte version;
	Byte selectedMethod;

	/**
	 * IF RESOLVED
	 */
	InetAddress targetAddr;
	int targetPort;

	
	byte status;

	byte addrType = SOCKSV5Protocol.IPv4;

	byte[] port = new byte[2];

	byte[] address = new byte[4];


	public SOCKSV5Client(Socket s){
		this.state = SOCKSV5State.RCV_METHODS;
		this.buffer = ByteBuffer.allocate(BUFF_SIZE);
		socket = s;
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
	
	Socket getSocket(){
		return socket;
	}
	
}
