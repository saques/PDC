package tp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
	
	public static void main(String ... args) throws UnknownHostException, IOException{
		TCPServer tcpServer = new TCPServer(2500, InetAddress.getByName("localhost"),10, new IMKVSServer());
		tcpServer.run();
	}
}
