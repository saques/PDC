package tp0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import tp0.Log.Message;

@SuppressWarnings("unused")
public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		Log l = new CSVLog("testfile");
//		l.warning("salksajdlkjas");
//		l.info("la puta madre");
//		l.read(new FileInputStream("testfile"), new Consumer<Log.Message>() {
//			
//			@Override
//			public void accept(Message t) {
//				System.out.println(t);
//				
//			}
//		});
		
		
		
//		UTF8Reader reader = new UTF8Reader(new FileInputStream("test"));
//		reader.read();
//		for(int i : reader.getRead()){
//			System.out.println(Character.toChars(i));
//		}
	
//		OutputStream outputStream = new FileOutputStream("test",true);
//		CSVLog l = new CSVLog(outputStream);
//		l.warning("prueba");
//		l.warning("pruebasadas");
		
		InputStream inputStream = new FileInputStream("test");
		CSVLog l = new CSVLog(new FileOutputStream("test",true));
		l.read(inputStream, new Consumer<Log.Message>() {

			@Override
			public void accept(Message t) {
				System.out.println(t.toString());				
			}
			
		});
		
	}

}
