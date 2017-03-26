package tp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import tp1.imkvs.IMKVSServer;

public class Test {
	
	public static void main(String ... args) throws UnknownHostException, IOException{
		TCPServer tcpServer = new TCPServer(1080, InetAddress.getByName("localhost"),10, new IMKVSServer());
		tcpServer.run();
	}
}
