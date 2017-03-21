package tp0;

import java.io.OutputStream;

/** Registra mensajes */

public interface Log {

	/** registra un mensaje con el nivel warning */
	void warning(String msg);
	
	/** registra un mensaje con el nivel info */
	void info(String msg);
	
	
	
	/** dado un input stream consume uno a uno los mensajes */
	void read(java.io.InputStream input, final java.util.function.Consumer<Message> consumer);
	
	void setOutputStream(OutputStream outputStream);
	
	enum Level {
		
		warning,
		
		info,
	
	}
	
	
	
	interface Message {
	
		java.util.Date when();
		
		String getMessage();
		
		Level getLevel();
	
	}

}



