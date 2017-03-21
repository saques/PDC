package tp1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class InputToOutput implements Runnable {

	private InputStream input;
	private OutputStream output;
	private Socket s1,s2;
	private static final int BUF_SIZE = 512;
	
	public InputToOutput(InputStream inputStream, OutputStream outputStream,Socket s1, Socket s2){
		this.input = inputStream;
		this.output = outputStream;
		this.s1 = s1;
		this.s2 = s2;
	}
	
	
	@Override
	public void run() {
		byte buf[] = new byte[BUF_SIZE];
		boolean run = true;
		while(run){
			try {
				int read = 0;
				if(!s1.isClosed()){
					read = input.read(buf);
				} else {
					run = false;
					break;
				}
				if(read >=0 && !s2.isClosed()){
					output.write(buf, 0, read);
				} else {
					run = false;
					break;
				}
				
			} catch (IOException e) {
				run = false;
				break;
			}
			
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
