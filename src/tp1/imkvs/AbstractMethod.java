package tp1.imkvs;

public abstract class AbstractMethod implements Method {
	protected IMKVSServer server;
	protected String protocol;
	protected String version;
	
	public AbstractMethod(IMKVSServer server, String protocol, String version){
		this.server = server;
		this.protocol = protocol;
		this.version = version;
	}
	

}
