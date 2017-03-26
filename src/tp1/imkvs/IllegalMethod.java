package tp1.imkvs;

public class IllegalMethod extends AbstractMethod {

	public IllegalMethod(IMKVSServer server, String protocol, String version) {
		super(server, protocol, version);
	}

	@Override
	public boolean execute() {
		server.printResponse(Method.PROTOCOL, Method.VERSION, false, Method.ILLEGAL_METHOD);
		return true;
	}

}
