package juanf846.JavaUDPBeacon.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class BeaconThread extends Thread{
	private boolean continuar = true;
	private DatagramPacket dp;
	
	private int delay = 1000;
	
	BeaconThread() {}
	
	void setData(byte[] data, int puerto) throws UnknownHostException {
		InetAddress broadcast = InetAddress.getByAddress(new byte[] {(byte) 255,(byte) 255,(byte) 255,(byte) 255});
		this.dp = new DatagramPacket(data, data.length, broadcast, puerto);
	}
	
	void setDelay(int delay) {
		this.delay = delay;
	}
	
	void stopThread() {
		continuar = false;
	}
	
	@Override
	public void run() {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			while(continuar) {
				ds.send(dp);
				Thread.sleep(delay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ds != null) ds.close();
		}
		
	}
}
