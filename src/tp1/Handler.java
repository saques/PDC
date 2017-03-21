package tp1;

import java.net.Socket;

public class Handler implements Runnable {

	private Socket s ;
	private TRunnable<Socket> tRunnable;
	
	public Handler(Socket s, TRunnable<Socket> tRunnable){
		this.s = s;
		this.tRunnable = tRunnable;
	}

	
	@Override
	public void run() {
		tRunnable.run(s);
		
	}
	
	
}
