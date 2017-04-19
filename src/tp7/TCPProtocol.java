package tp7;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface TCPProtocol {
	
	default void handleAccept(SelectionKey key) throws IOException{
		SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
		clientChannel.configureBlocking(false);
		
		SOCKSV5Client client = new SOCKSV5Client();
		client.key = clientChannel.register(key.selector(), SelectionKey.OP_READ, client);
		
	}
	
	boolean handleConnect(SelectionKey key) throws IOException;
	
	boolean handleRead(SelectionKey key) throws IOException;
	
	boolean handleWrite(SelectionKey key) throws IOException;
	
}
