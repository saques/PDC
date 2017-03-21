package tp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In Memory Key Value Store Saques 1.0
 * Protocolo en TEXTO PLANO
 * Cada comando se escribe en una linea
 * FORMATO:
 * 
 * Request:
 * 
 * 	Saques/1.0/[INC|DEC|GET|SET|EXIT]/key/{value}
 * 
 * Response:
 * 
 * 	Saques/1.0/[OK|ERROR]/message
 * 
 * @author msaques
 *
 */
public class IMKVSServer implements TRunnable<Socket> {

	private volatile Map<String, Integer> map;
	private static final String SAQUES = "Saques";
	private static final String VERSION = "1.0";
	private static final String NOT_SAQUES = "Not using Saques IMKVS protocol";
	private static final String INCORRECT_VERSION = "Version not supported";
	private static final String NO_ARGS_METHOD = "This method takes no arguments";
	private static final String ILLEGAL_METHOD = "Illegal method";
	private static final String NO_SUCH_KEY = "No such key";
	private static final String SUCCESS = "Success";
	
	public IMKVSServer(){
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void run(Socket t) {
		// TODO Auto-generated method stub
		try{
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(t.getInputStream()));
			PrintWriter printWriter = new PrintWriter(t.getOutputStream());
			
			String line = null;
			boolean run = true;
			while(run && (line=bufferedReader.readLine())!=null){
				String [] tokens = line.split("/");
			
				if(!protocolValidator(tokens, printWriter)){
					run = false;
					break;
				}
				
				run = methodSelector(tokens, printWriter);
				
				
			}

			t.close();
			
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return;
	}
	
	private boolean protocolValidator(String[] tokens, PrintWriter printWriter){
		if(!tokens[0].equals(SAQUES)){
			printResponse(printWriter, false, NOT_SAQUES);
			return false;
		}
		if(!tokens[1].equals(VERSION)){
			printResponse(printWriter, false, INCORRECT_VERSION);
			return false;
		}
		return true;
	}
	
	private boolean methodSelector(String[] tokens, PrintWriter printWriter){
		boolean retval = true;
		switch(tokens[2]){
		
			case "INC":
				if(!increment(tokens[3])){
					printResponse(printWriter, false, NO_SUCH_KEY);
				} else {
					printResponse(printWriter, true, SUCCESS);
				}
				break;
				
			case "DEC":
				if(!decrement(tokens[3])){
					printResponse(printWriter, false, NO_SUCH_KEY);
				} else {
					printResponse(printWriter, true, SUCCESS);
				}
				break;
				
				
			case "GET":
				Integer value = get(tokens[3]);
				if(value == null) {
					printResponse(printWriter, false, NO_SUCH_KEY);
				} else {
					printResponse(printWriter, true, value.toString());
				}
				break;
				
			case "SET":
				set(tokens[3],Integer.valueOf(tokens[4]));
				printResponse(printWriter, true, SUCCESS);
				break;
			case "EXIT":
				retval = false;
				break;
			default:
				printResponse(printWriter, false, ILLEGAL_METHOD);
				break;
		
		}
		return retval;
		
	}
	
	public boolean increment(String key){
		Integer value = map.get(key);
		if(value == null) return false;
		map.put(key, value+1);
		return true;
	}
	
	public boolean decrement(String key){
		Integer value = map.get(key);
		if (value == null) return false;
		map.put(key, value-1);
		return true;
	}
	
	public Integer get(String key){
		return map.get(key);
	}
	
	public void set(String key, Integer value){
		map.put(key,value);
	}
	
	
	private void printResponse(PrintWriter printWriter, boolean success, String message){
		
		printWriter.println(SAQUES+"/"+VERSION+"/"+(success?"OK/":"ERROR/")+message);
		printWriter.flush();
	}

}
