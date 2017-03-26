package tp1.imkvs;

public class EXIT extends AbstractMethod {
	private String arg;
	
	public EXIT(IMKVSServer server, String protocol, String version, String arg) {
		super(server, protocol, version);
		this.arg = arg;
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
		if(arg!=""){
			server.printResponse(Method.PROTOCOL, Method.VERSION, false, Method.ILLEGAL_ARGS);
			return true;
		}
		server.printResponse(Method.PROTOCOL, Method.VERSION, true, Method.BYE);
		return false;
	}

}
