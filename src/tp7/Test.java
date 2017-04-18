package tp7;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		TCPSelectorServer server = new TCPSelectorServer(new SOCKSV5Protocol(), "1080");
		server.run();

	}

}
