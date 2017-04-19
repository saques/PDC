package tp7;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class SOCKSV5Protocol implements TCPProtocol {

	volatile List<Byte> supportedValidationMethods;
	volatile List<Byte> supportedCommands;
	
	static final byte IPv4 = (byte)0x01;
	static final byte DOMAINNAME = (byte)0x03;
	static final byte INVALID_METHOD = (byte)0xFF;
	
	public SOCKSV5Protocol(){
		supportedValidationMethods = new ArrayList<>();
		supportedValidationMethods.add((byte)0x00);
		supportedCommands = new ArrayList<>();
		supportedCommands.add((byte)0x01);
	}
	
	@Override
	public boolean handleRead(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		SOCKSV5Client client = (SOCKSV5Client)key.attachment();
		
		client.getBuffer().clear();
		long bytesRead = channel.read(client.getBuffer());
		if(bytesRead == -1) {
			return false;
		}
		if(client.getState().doOp(key,this)){
			key.interestOps(SelectionKey.OP_WRITE);
			return true;
		}
		return false;

	}

	@Override
	public boolean handleWrite(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		SOCKSV5Client client = (SOCKSV5Client)key.attachment();
		
		boolean ret = client.getState().doOp(key,this);
		
		client.getBuffer().flip();
		channel.write(client.getBuffer());
		
		if(ret){
			key.interestOps(SelectionKey.OP_READ);
			return true;
		}
		return false;
	}

	@Override
	public boolean handleConnect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		if(channel.finishConnect()){
			/**
			 * I want to read from the key
			 * I must write to the client that the
			 * connection has been successful.
			 */
			((SOCKSV5Remote)key.attachment()).client.key.interestOps(SelectionKey.OP_READ);
		} else {
			key.interestOps(SelectionKey.OP_CONNECT);
		}
		return false;
	}

}
