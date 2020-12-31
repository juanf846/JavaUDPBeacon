package juanf846.JavaUDPBeacon.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

/**
 * Clase encargada de recibir los paquetes y pasarselos a Receiver.
 * 
 * @author JuanFDiz
 *
 */
class ReceiverThread extends Thread{

	private long uuidHighFilter = 0;
	private long uuidLowFilter = 0;
	private int versionMin = 0;
	private int versionMax = 0;
	private short puerto;
	
	private boolean continuar = true;
	private Receiver receiver;

	/**
	 * Detiene el hilo
	 */
	void stopThread() {
		continuar = false;
	}
	
	ReceiverThread(long uuidHighFilter, long uuidLowFilter, int versionMin, int versionMax, Receiver receiver, short puerto) {
		this.uuidHighFilter = uuidHighFilter;
		this.uuidLowFilter = uuidLowFilter;
		this.versionMin = versionMin;
		this.versionMax = versionMax;
		this.receiver = receiver;
		this.puerto = puerto;
	}

	@Override
	public void run() {
		DatagramSocket ds = null;
		DatagramPacket dp = new DatagramPacket(new byte[0xFF], 0xFF);
		try {
			ds = new DatagramSocket(puerto);
			ds.setSoTimeout(1000);
			while(continuar) {
				try {
					ds.receive(dp);
					try {
						BeaconData bd = new BeaconData(dp.getData(), dp.getAddress());
						if(!(uuidHighFilter == 0 && uuidLowFilter == 0)) 
							if(!(bd.compareUuid(uuidHighFilter, uuidLowFilter)))
								continue;
						
						if(!(bd.compareVersion(versionMin, versionMax)))
							continue;
						
						receiver.addData(bd);
					}catch(IOException e) {}
				}catch(SocketTimeoutException e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ds != null) ds.close();
		}
		
	}
}
