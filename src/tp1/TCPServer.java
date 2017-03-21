package tp1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer implements Runnable {
	
	private ServerSocket serverSocket;
	private ExecutorService pool;
	private TRunnable<Socket> tRunnable;
	private static final int BACKLOG = 50;
	
	public TCPServer(int port, InetAddress iAddress, int poolSize, TRunnable<Socket> tRunnable) throws IOException{
		serverSocket = new ServerSocket(port, BACKLOG, iAddress);
		pool = Executors.newFixedThreadPool(poolSize);
		this.tRunnable = tRunnable;
	}
	
	public void run(){
		try {
			for(;;){
				pool.execute(new Handler(serverSocket.accept(),tRunnable));
			}			
		} catch (IOException e){
			pool.shutdown();
		}
	}

}
