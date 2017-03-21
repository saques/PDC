package tp0;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UTF8Reader {
	
	private List<Integer> l = new ArrayList<>();
	private List<Byte[]> lb = new ArrayList<>();
	private InputStream io;
	
	public UTF8Reader(InputStream io){
		this.io = io;
	}
	
	
	public void read() throws IOException{
		int c = -1;
		int ans = 0;
		while((c = io.read()) != -1){
			if(((c>>7)&1) == 0) {
				ans = c;
				Byte[] n = new Byte[1];
				n[0] = (byte)ans;
				lb.add(n);
			} else {
				byte[] bs;
				if(((c>>4)&1)==1 && ((c>>5)&1)==1 && ((c>>6)&1)==1){
					bs = new byte[3];
					io.read(bs);
					ans = c;
					ans <<=8;
					ans |=(bs[0]&0x00FF);
					ans <<=8;
					ans |=(bs[1]&0x00FF);
					ans <<=8;
					ans |=(bs[2]&0x00FF);
				} else if(((c>>5)&1)==1 && ((c>>6)&1)==1){
					bs = new byte[2];
					io.read(bs);
					ans = c;
					ans <<=8;
					ans |=(bs[0]&0x00FF);
					ans <<=8;
					ans |=(bs[1]&0x00FF);
				} else {
					bs = new byte[1];
					io.read(bs);
					ans = c;
					ans <<=8;
					ans |=(bs[0]&0x00FF);
				}
				Byte[] n = new Byte[bs.length+1];
				n[0]=(byte)c;
				for(int i=0; i<bs.length; i++){
					n[i+i]=bs[i];
				}
				lb.add(n);
			}
			l.add(ans);
		}
		
	}
	
	
	public List<Integer> getRead(){
		return l;
	}
	
	public List<Byte[]> getBytesRead(){
		return lb;
	}
	
	
	
}
