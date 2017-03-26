package tp1.imkvs;

public interface Method {
	
	public static final String PROTOCOL = "Saques";
	public static final String VERSION = "1.0";
	public static final String ILLEGAL_PROTOCOL = "Not using Saques IMKVS protocol";
	public static final String ILLEGAL_VERSION = "Version not supported";
	public static final String ILLEGAL_ARGS = "Too few or too many arguments";
	public static final String ILLEGAL_METHOD = "Illegal method or synthax";
	public static final String NO_SUCH_KEY = "No such key";
	public static final String SUCCESS = "Success";
	public static final String BYE = "Bye";
	
	public static boolean isValidProtocol(String protocol){
		return protocol.equals(PROTOCOL);
	}
	
	public static boolean isValidVersion(String version){
		return version.equals(VERSION);
	}
	
	boolean execute();
	
}
