package tp1.imkvs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tp1.TRunnable;

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
	private PrintWriter printWriter;
	
	
	
	public IMKVSServer(){
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void run(Socket t) {
		try{
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(t.getInputStream()));
			this.printWriter = new PrintWriter(t.getOutputStream());
			
			String line = null;
			boolean run = true;
			while(run && (line=bufferedReader.readLine())!=null){
				String [] tokens = line.split("/");
		
				Method m = MethodFactory.buildMethod(tokens, this);
				run = m.execute();
				
				
			}

			t.close();
			
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return;
	}
	
	boolean increment(String key){
		Integer value = map.get(key);
		if(value == null) return false;
		map.put(key, value+1);
		return true;
	}
	
	boolean decrement(String key){
		Integer value = map.get(key);
		if (value == null) return false;
		map.put(key, value-1);
		return true;
	}
	
	Integer get(String key){
		return map.get(key);
	}
	
	void set(String key, Integer value){
		map.put(key,value);
	}
	
	
	void printResponse(String protocol, String version,boolean success, String message){
		
		printWriter.println(protocol+"/"+version+"/"+(success?"OK/":"ERROR/")+message);
		printWriter.flush();
	}

}
