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
		EndPoint client = (EndPoint)key.attachment();
		
		if(client.getState() == null)
			return true;
		
		
		client.getBuffer().clear();
		long bytesRead = channel.read(client.getBuffer());
		if(bytesRead == -1) {
			return false;
		}

		return client.getState().doOp(key,this);

	}

	@Override
	public boolean handleWrite(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		EndPoint client = (EndPoint)key.attachment();
		
		if(client.getState() == null)
			return true;
		
		boolean ret = client.getState().doOp(key,this);
		
		client.getBuffer().flip();
		channel.write(client.getBuffer());
		
		return ret;
	}

	@Override
	public void handleConnect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		EndPoint client = (EndPoint)key.attachment();
		if(channel.finishConnect()){
			/**
			 * I want to read from the key
			 * I must write to the client that the
			 * connection has been successful.
			 */
			client.getKey().interestOps(SelectionKey.OP_READ);
			client.setState(SOCKSV5State.READ_CONN);
			
			client.getOther().getKey().interestOps(SelectionKey.OP_WRITE);
			client.getOther().setState(SOCKSV5State.FINISH_SUCCESS);
		} else {
			key.interestOps(SelectionKey.OP_CONNECT);
		}
	}

}
