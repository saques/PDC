package tp0;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import tp0.Log.Message;

public class LogClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		Socket s = new Socket("localhost", 20008);
		InputStream inputStream = s.getInputStream();
		CSVLog log = new CSVLog();
		Consumer<Log.Message> consumer = new Consumer<Log.Message>() {
			@Override
			public void accept(Message t) {
				System.out.println(t.toString());
			}
		};
		log.read(inputStream, consumer);
		s.close();
	}

}
