package tp7;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public enum SOCKSV5State implements SOCKSV5Action{

	RCV_METHODS () {

		@Override
		public boolean doOp(SelectionKey key, SOCKSV5Protocol protocol) {
			SOCKSV5Client client = (SOCKSV5Client)key.attachment();
			System.out.println(this.name());
			ByteBuffer buffer = client.getBuffer();
			
			buffer.flip();
			byte version = buffer.get();
			byte nMethods = buffer.get();
			byte methods[] = new byte[nMethods];
			
			for(int i=0; i<nMethods; i++)
				methods[i] = buffer.get();
			
			boolean containsValidMethod = false;
			byte lowestMethod = (byte)0x7F;
			
			for(int i=0; i<nMethods; i++){
				if(protocol.supportedValidationMethods.contains(methods[i])){
					containsValidMethod = true;
					if(methods[i]<lowestMethod){
						lowestMethod = methods[i];
					}
				}
			}
			
			client.version = version;
			client.selectedMethod = containsValidMethod? lowestMethod: SOCKSV5Protocol.INVALID_METHOD;
			
			client.setState(ANSW_METHODS);
			return true;
			
		}
		
	},
	
	ANSW_METHODS () {

		@Override
		public boolean doOp(SelectionKey key, SOCKSV5Protocol protocol) {
			SOCKSV5Client client = (SOCKSV5Client)key.attachment();
			System.out.println(this.name());
			ByteBuffer buffer = client.getBuffer();
			
			buffer.clear();
			buffer.put((byte)client.version);
			buffer.put((byte)client.selectedMethod);
			client.setState(RCV_REQUEST);
			return client.selectedMethod != SOCKSV5Protocol.INVALID_METHOD ? true : false;
		}
		
	},
	
	RCV_REQUEST() {

		@Override
		public boolean doOp(SelectionKey key, SOCKSV5Protocol protocol)  {
			SOCKSV5Client client = (SOCKSV5Client)key.attachment();
			System.out.println(this.name());
			ByteBuffer buffer = client.getBuffer();
			/*
			 * VER | CMD |  RSV  | ATYP
			 */
			
			buffer.flip();
			byte fields[] = new byte[4];
			buffer.get(fields);
			
			if(!protocol.supportedCommands.contains(fields[1]) ||
			   fields[2] != 0x00){
				client.version = fields[0];
				client.status = (byte)0x07;
				client.setState(ANSW_REQUEST);
				return true;
			}
			
			if(!(fields[3] == SOCKSV5Protocol.IPv4 || fields[3] == SOCKSV5Protocol.DOMAINNAME)){
				client.version = fields[0];
				client.status = (byte)0x08;
				client.setState(ANSW_REQUEST);
				return true;
			}
			
			
			byte portb[] = new byte[2];
			
			
			InetAddress iAddress = null;
			byte faddress[] = null;
			try {
				if(fields[3] == SOCKSV5Protocol.DOMAINNAME){
					byte address[] = new byte[buffer.get()];
					buffer.get(address);
					iAddress = InetAddress.getByName(new String(address));
					faddress = new byte[address.length +1];
					faddress[0] = (byte)address.length;
					for(int i=0; i< address.length; i++){
						faddress[i+1]=address[i];
					}
					
				} else if (fields[3] == SOCKSV5Protocol.IPv4){
					byte address[] = new byte[4];
					buffer.get(address);
					iAddress = InetAddress.getByAddress(address);
					faddress = address;
				}
			} catch (UnknownHostException e){
				client.version = fields[0];
				client.status = (byte)0x04;
				client.setState(ANSW_REQUEST);
				return true;
			}
			
			buffer.get(portb);
			int port = byte2toInt(portb);
			
			client.status = 0x00;
			client.version = fields[0];
			client.targetAddr = iAddress;
			client.targetPort = port;
			
			client.setState(ANSW_REQUEST);
			return true;
		}
		
		
	},
	
	ANSW_REQUEST() {

		@Override
		public boolean doOp(SelectionKey key, SOCKSV5Protocol protocol) {
			SOCKSV5Client client = (SOCKSV5Client)key.attachment();
			System.out.println(this.name());
			ByteBuffer buffer = client.getBuffer();
			buffer.clear();
			if(client.status != (byte)0x00){
				fillFailure(client);
				return false;
			}
			
			try {
				SocketChannel channel = SocketChannel.open();
				channel.configureBlocking(false);
				SOCKSV5Remote remote = new SOCKSV5Remote(client);
				if(!channel.connect(new InetSocketAddress(client.targetAddr, client.targetPort))){
					channel.register(key.selector(), SelectionKey.OP_CONNECT, remote);
				} else{
					fillSuccess(client);
					key.interestOps(SelectionKey.OP_READ);
					return true;
				}
				
			} catch (IOException e){
				fillFailure(client);
				return false;
			}

			
			
			
			return true;
		}
		
	},
	
	READ_CONN(){

		@Override
		public boolean doOp(SelectionKey key, SOCKSV5Protocol protocol) {
			// TODO Auto-generated method stub
			return false;
		}
		
	},
	
	WRITE_CONN(){

		@Override
		public boolean doOp(SelectionKey key, SOCKSV5Protocol protocol) {
			// TODO Auto-generated method stub
			return false;
		}
		
	};
	
	
	private static void fillFailure(SOCKSV5Client client){
		ByteBuffer buffer = client.getBuffer();
		//PRINT FAILURE
		buffer.put(client.version);
		buffer.put(client.status);
		buffer.put((byte)0x00);
		buffer.put(client.addrType);
		buffer.put(new byte[4]);
		buffer.put(new byte[2]);
	}
	
	static void fillSuccess(SOCKSV5Client client){
		ByteBuffer buffer = client.getBuffer();
		buffer.put(client.version);
		buffer.put(client.status);
		buffer.put((byte)0x00);
		buffer.put(client.addrType);
		buffer.put(client.targetAddr.getAddress());
		buffer.put(intToByte2(client.targetPort));
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
	private static int byte2toInt(byte n[]){
		int ans = 0;
		ans += ((n[0]>>4)&0x0F)*16*16*16;
		ans += ((n[0])&0x0F)*16*16;
		ans += ((n[1]>>4)&0x0F)*16;
		ans += ((n[1])&0x0F);
		return ans;
	}
	
}
