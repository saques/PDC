package tp1.imkvs;

public class SET extends AbstractMethod {
	private String key;
	private String val;
	private String arg3;

	
	public SET(IMKVSServer server, String protocol, String version, String key, String val, String arg3) {
		super(server, protocol, version);
		this.key = key;
		this.val = val;
		this.arg3 = arg3;
	}

	@Override
	public boolean execute() {
		if(!Method.isValidProtocol(protocol)){
			server.printResponse(Method.PROTOCOL, Method.VERSION, false, Method.ILLEGAL_PROTOCOL);
			return true;
		}
		if(!Method.isValidVersion(version)){
			server.printResponse(Method.PROTOCOL, Method.VERSION, false, Method.ILLEGAL_VERSION);
			return true;
		}
		if(key=="" || val=="" || arg3!=""){
			server.printResponse(Method.PROTOCOL, Method.VERSION, false, Method.ILLEGAL_ARGS);
			return true;
		}
		server.set(key,Integer.valueOf(val));
		server.printResponse(Method.PROTOCOL, Method.VERSION, true, Method.SUCCESS);
		return true;
	}

}
