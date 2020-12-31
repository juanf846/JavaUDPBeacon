package juanf846.javaUDPBeacon.test;

import static org.junit.Assert.*;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.junit.Test;

import juanf846.JavaUDPBeacon.client.BeaconData;
import juanf846.JavaUDPBeacon.client.Receiver;
import juanf846.JavaUDPBeacon.client.Receiver.DataChangeListener;
import juanf846.JavaUDPBeacon.server.Beacon;

public class TestServerClient {

	private Receiver receiver;
	private Beacon beacon;
	
	private boolean funciono = false ;
	
	@Test
	public void test() throws SocketException, InterruptedException {
		DatagramSocket ds = new DatagramSocket();
		
		beacon = new Beacon(1l, 1l, 1, ds.getLocalPort(), 10846);
		beacon.setDescripcion("Test exitoso");
		beacon.start();
		
		receiver = new Receiver(1l, 1l, 1, 100, (short)10846);
		receiver.setListener(new DataChangeListener() {
			
			@Override
			public void change(BeaconData[] data) {
				System.out.println("Nueva data");
				for(BeaconData bd : data) {
					System.out.println(new String(bd.getDescripcion()));
					System.out.println(bd.getPuerto());
				}
				funciono = true;
			}
		});
		receiver.start();
		Thread.sleep(5000);
		
		receiver.stop();
		beacon.stop();
		ds.close();
		if(!funciono) fail("No se recibió ningun paquete");
	}

}
