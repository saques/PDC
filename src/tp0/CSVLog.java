package tp0;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.function.Consumer;

public class CSVLog implements Log {

	
	private class CSVMessage implements Message {
		
		String[] data;
		
		public CSVMessage(String msg,Level l) {
			data = new String[3];
			data[0] = Long.toString(new Date().getTime());
			data[1] = msg;
			data[2] = l.toString();
		}
		
		public CSVMessage(String time,String msg,String level) {
			data = new String[3];
			data[0] = time;
			data[1] = msg;
			data[2] = level;
		}
		@Override
		public Date when() {
			return new Date(new Long(data[0]));
		}

		@Override
		public String getMessage() {
			return data[1];
		}

		@Override
		public Level getLevel() {
			return Enum.valueOf(Level.class, data[2]);
		}
		
		public String toString(){
			String s = "";
			s+=data[0]+",";
			s+=data[1]+",";
			s+=data[2];
			return s;
		}
		
	}
	private OutputStream f;
	private PrintWriter p;
	
	
	
	public CSVLog(OutputStream output){
		this.f = output;
		this.p = new PrintWriter(f);
	}
	
	public CSVLog() {
	}
	
	@Override
	public void warning(String msg) {
		if(f == null) throw new NullStreamException();
		if(!checkValidString(msg)) throw new IllegalArgumentException();
		Message m = new CSVMessage(msg, Level.warning);
		p.println(m);
		p.flush();
	}

	@Override
	public void info(String msg) {
		if(f == null) throw new NullStreamException();
		if(!checkValidString(msg)) throw new IllegalArgumentException();
		Message m = new CSVMessage(msg, Level.info);
		p.println(m);
		p.flush();
	}
	
	@Override
	public void setOutputStream(OutputStream outputStream){
		this.f = outputStream;
	}
	
	private boolean checkValidString(String s){
		for(char b : s.toCharArray()){
			if(b == ',') return false;
		}
		return true;
	}

	@Override
	public void read(InputStream input, Consumer<Message> consumer) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		try {
			while((line = reader.readLine())!= null){
				String[] fields = line.split(",");
				Message m = new CSVMessage(fields[0],fields[1],fields[2]);
				consumer.accept(m);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
	}
}
