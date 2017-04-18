package tp7;

public interface SOCKSV5Action {

	/**
	 * 
	 * @param client
	 * @return false if the connection must be terminated
	 */
	boolean doOp(SOCKSV5Client client, SOCKSV5Protocol protocol);
	
}
