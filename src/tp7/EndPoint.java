package tp7;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface EndPoint {
	public static final int BUFF_SIZE = 1024;
	
	EndPoint getOther();
	
	ByteBuffer getBuffer();
	
	SelectionKey getKey();
	
	void setKey(SelectionKey register);
	
	void setState(SOCKSV5State state);
	
	SOCKSV5State getState();
	
	
}
