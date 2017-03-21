package tp0;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Stream;

/**
 * La clase {@link CSVLog} recibe en su constructor un {@link OutputStream}.
 * Quien determina a que direcciona dicho {@link Stream} es el usuario de la
 * clase. La clase {@code LogServer} crea un socket donde escucha clientes del
 * servicio de logging. Al recibir una conexion, escribe en el outputStream obtenido
 * del socket.
 * @author ALEJO.NOTEBOOK
 *
 */
public class LogServer {

	public static void main(String[] args) throws UnknownHostException, IOException {
		ServerSocket serverSocket = new ServerSocket(20008, 10, InetAddress.getByName("localhost"));
		Socket s = serverSocket.accept();
		OutputStream outputStream = s.getOutputStream();
		
		CSVLog l = new CSVLog(outputStream);
		l.warning("hola");
		l.warning("sjadhjas");
		l.warning("asdkasjfhaskjf");
		l.warning("daksjdasjldajsldjas");
		l.warning("hola");
		l.warning("sjadhjas");
		l.warning("asdkasjfhaskjf");
		l.warning("daksjdasjldajsldjas");
		serverSocket.close();

	}

}
