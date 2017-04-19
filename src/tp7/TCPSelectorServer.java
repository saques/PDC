package tp7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TCPSelectorServer implements Runnable {
	
	private TCPProtocol protocol;
	private Selector selector;
	
	public TCPSelectorServer(TCPProtocol protocol, String ... ports) throws IOException {
		if(ports.length < 1) 
			throw new IllegalArgumentException("At least 1 port required");
		this.protocol = protocol;
		this.selector = Selector.open();
		for(String port: ports){
			int pnum = Integer.valueOf(port);
			ServerSocketChannel listenChannel = ServerSocketChannel.open();
			listenChannel.socket().bind(new InetSocketAddress(pnum));
			listenChannel.configureBlocking(false);
			listenChannel.register(selector, SelectionKey.OP_ACCEPT);
		}	
	}

	@Override
	public void run() {
		try {
			
			for(;;) {
				selector.select();
				Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
				while(keyIter.hasNext()){
					SelectionKey key = keyIter.next();
					keyIter.remove();
					if(key.isAcceptable()){
						protocol.handleAccept(key);
					}
					
					if(key.isConnectable()){
						protocol.handleConnect(key);
					}
					
					if(key.isReadable()){
						protocol.handleRead(key);
					}
					
					if(key.isValid() && key.isWritable()){
						protocol.handleWrite(key);
					}
					
					
				}
			}
			
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	

}
