package tp1.imkvs;

public class MethodFactory {

	
	static Method buildMethod(String tokens[], IMKVSServer server){
		String protocol = tokens.length >0? tokens[0] : "";
		String version = tokens.length >1? tokens[1] : "";
		String method = tokens.length >2? tokens[2] : "";
		String arg1 = tokens.length >3? tokens[3] : "";
		String arg2 = tokens.length >4? tokens[4] : "";
		/*
		 * This should be null, but passed to Methods for validation
		 * Always passing the next argument
		 */
		String arg3 = tokens.length >5? tokens[5] : "";
		
		Method retval = null;
		switch(method){
			case "INC":
				retval = new INC(server, protocol, version, arg1, arg2);
				break;
			case "DEC":
				retval = new DEC(server, protocol, version, arg1, arg2);
				break;
			case "GET":
				retval = new GET(server, protocol, version, arg1, arg2);
				break;
			case "SET":
				retval = new SET(server, protocol, version, arg1, arg2, arg3);
				break;
			case "EXIT":
				retval = new EXIT(server, protocol, version, arg1);
				break;
			default:
				retval = new IllegalMethod(server, protocol, version);
				break;
		}
		return retval;
	}
	
}
