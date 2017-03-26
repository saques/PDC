package tp1.imkvs;

public class DEC extends AbstractMethod {
	private String key;
	private String arg2;

	public DEC(IMKVSServer server, String protocol, String version, String key, String arg2) {
		super(server, protocol, version);
		this.key = key;
		this.arg2 = arg2;
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
		if(arg2!="" || key == ""){
			server.printResponse(Method.PROTOCOL, Method.VERSION, false, Method.ILLEGAL_ARGS);
			return true;
		}
		boolean ret = server.decrement(key);
		server.printResponse(Method.PROTOCOL, Method.VERSION, ret, ret ? Method.SUCCESS : Method.NO_SUCH_KEY);
		return true;
	}

}
