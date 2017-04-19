package tp7;

import java.nio.channels.SelectionKey;

public interface SOCKSV5Action {

	/**
	 * 
	 * @param client
	 * @return false if the connection must be terminated
	 */
	boolean doOp(SelectionKey key, SOCKSV5Protocol protocol);
	
}
