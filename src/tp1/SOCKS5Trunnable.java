package tp1;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SOCKS5Trunnable implements TRunnable<Socket> {

	private volatile List<Byte> supportedValidationMethods;
	private volatile List<Byte> supportedCommands;
	
	private static final byte IPv4 = (byte)0x01;
	private static final byte DOMAINNAME = (byte)0x03;
	
	public SOCKS5Trunnable(){
		supportedValidationMethods = new ArrayList<>();
		supportedValidationMethods.add((byte)0x00);
		supportedCommands = new ArrayList<>();
		supportedCommands.add((byte)0x01);
	}
	
	@Override
	public void run(Socket s) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = s.getInputStream();
			outputStream = s.getOutputStream();
			DataInputStream daInput = new DataInputStream(inputStream);
			DataOutputStream daOutput = new DataOutputStream(outputStream);
			validateMethods(daInput,daOutput);
			validateRequest(daInput, daOutput,s);
			
			Thread.currentThread().join();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void validateMethods(DataInput dataInput, DataOutput dataOutput) throws IOException{
		byte verNmethods[] = new byte[2];
		dataInput.readFully(verNmethods);
		byte methods[] = new byte[verNmethods[1]];
		
		for(int i=0; i<verNmethods[1]; i++)
			methods[i] = dataInput.readByte();
		
		boolean containsValidMethod = false;
		byte lowestMethod = (byte)0x7F;
		
		for(int i=0; i<verNmethods[1]; i++){
			if(supportedValidationMethods.contains(methods[i])){
				containsValidMethod = true;
				if(methods[i]<lowestMethod){
					lowestMethod = methods[i];
				}
			}
		}
		
		printValidationInfo(verNmethods, methods);
		
		writeValidationResponse(dataOutput, verNmethods[0], containsValidMethod? lowestMethod: (byte)0xFF);
	}
	
	private void writeValidationResponse(DataOutput dataOutput, byte ver, byte method) throws IOException{
		byte response[] = new byte[2];
		response[0] = ver;
		response[1] = method;
		dataOutput.write(response);
	}
	
	private void printValidationInfo(byte verNmethods[], byte methods[]){
		System.out.println("Detected connection with SOCKS v." + verNmethods[0] + " able to use " + verNmethods[1] + 
				" authentication methods");
	}
	
	private void validateRequest(DataInput dataInput, DataOutput dataOutput, Socket usrSocket) throws IOException{
		
		/*
		 * VER | CMD |  RSV  | ATYP
		 */
		byte fields[] = new byte[4];
		dataInput.readFully(fields);
		
		if(!supportedCommands.contains(fields[1])){
			writeRequestResponse(dataOutput, fields[0], (byte) 0x07, fields[3], new byte[4], new byte[2]);
		}
		
		if(fields[2] != 0x00){
			writeRequestResponse(dataOutput, fields[0], (byte) 0x07, fields[3], new byte[4], new byte[2]);
		}
		
		if(!(fields[3] == IPv4 || fields[3] == DOMAINNAME)){
			writeRequestResponse(dataOutput, fields[0], (byte) 0x08, fields[3], new byte[4], new byte[2]);
		}
		
		
		byte portb[] = new byte[2];
		
		
		InetAddress iAddress = null;
		byte faddress[] = null;
		if(fields[3] == DOMAINNAME){
			byte address[] = new byte[dataInput.readByte()];
			dataInput.readFully(address);
			iAddress = InetAddress.getByName(new String(address));
			faddress = new byte[address.length +1];
			faddress[0] = (byte)address.length;
			for(int i=0; i< address.length; i++){
				faddress[i+1]=address[i];
			}
			
		} else if (fields[3] == IPv4){
			byte address[] = new byte[4];
			dataInput.readFully(address);
			iAddress = InetAddress.getByAddress(address);
			faddress = address;
		}
		
		dataInput.readFully(portb);
		int port = byte2toInt(portb);
		
		printRequestInfo(iAddress, port);
		
		Socket s = null;
		try {
			s = new Socket(iAddress, port);
		} catch (IOException e){
			writeRequestResponse(dataOutput, fields[0], (byte) 0x05, fields[3], new byte[4], new byte[2]);
			return;
		}
		
		
		writeRequestResponse(dataOutput, fields[0], (byte)0, IPv4, 
				s.getLocalAddress().getAddress(), intToByte2(s.getLocalPort()));
		
		new Thread(new InputToOutput(usrSocket.getInputStream(), s.getOutputStream(),usrSocket,s)).start();
		new Thread(new InputToOutput(s.getInputStream(), usrSocket.getOutputStream(),s,usrSocket)).start();
		
	}
	
	private void printRequestInfo(InetAddress iAddress, int port){
		System.out.println("Client requires to connect to: " + iAddress.getHostName() + " at port " + port);
	}
	
	private static int byte2toInt(byte n[]){
		int ans = 0;
		ans += ((n[0]>>4)&0x0F)*16*16*16;
		ans += ((n[0])&0x0F)*16*16;
		ans += ((n[1]>>4)&0x0F)*16;
		ans += ((n[1])&0x0F);
		return ans;
	}
	
	private static byte[] intToByte2(int n){
		byte ans[] = new byte[2];
		ans[0] = (byte)(n/(16*16*16));
		n -= ans[0]*(16*16*16);
		ans[0]<<=4;
		ans[0] += (byte)(n/(16*16));
		n -= (((byte)0x0F)&ans[0])*(16*16);
		ans[1] = (byte)(n/16);
		n-=ans[1]*16;
		ans[1] <<=4;
		ans[1] +=n;
		return ans;
	}
	
	private void writeRequestResponse(DataOutput dataOutput,byte ver, byte rep, byte atyp, byte ipv4[], byte port[]) throws IOException{
		byte init[] = new byte[4];
		init[0] = ver;
		init[1] = rep;
		init[2] = 0;
		init[3] = atyp;
		dataOutput.write(init);
		dataOutput.write(ipv4);
		dataOutput.write(port);
		
	}

}
