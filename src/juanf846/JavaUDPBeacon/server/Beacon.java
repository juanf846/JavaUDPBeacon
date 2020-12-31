package juanf846.JavaUDPBeacon.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Beacon {
	private long uuidHigh;
	private long uuidLow;
	private int version;
	private short puerto;
	private byte descripcionLenght = 0;
	private byte[] descripcion;
	private short extraLength = 0;
	private byte[] extra;
	
	private int puertoClientes;
	private BeaconThread thread = new BeaconThread();
	private boolean iniciado = false;
	
	/**
	 * Crea un nuevo Beacon 
	 * @param uuidHigh Primeros 64 bits del UUID (parte alta)
	 * @param uuidLow Ultimos 64 bits del UUID (parte baja)
	 * @param version Version del servicio
	 * @param puertoServicio Puerto donde deben comunicarse los clientes que quieran usar el servicio
	 * @param puertoClientes Puerto donde se enviaran los paquetes UDP broadcast
	 */
	public Beacon(long uuidHigh, long uuidLow, int version, int puertoServicio, int puertoClientes) {
		super();
		this.uuidHigh = uuidHigh;
		this.uuidLow = uuidLow;
		this.version = version;
		this.puerto = (short) puertoServicio;
		this.puertoClientes = puertoClientes;
	}

	public String getDescripcion() {
		return new String(descripcion);
	}

	public void setDescripcion(String descripcion) {
		byte[] newDescripcion = descripcion.getBytes();
		if(newDescripcion.length > 0xFF) throw new IllegalArgumentException("El parametro no puede superar los 255 bytes");
		
		this.descripcion = newDescripcion;
		this.descripcionLenght = (byte) newDescripcion.length;
	}

	public byte[] getExtra() {
		return extra;
	}

	public void setExtra(byte[] extra) {
		if(extra.length > 0xFFFF) throw new IllegalArgumentException("El parametro no puede superar los 65 535 bytes");
		this.extra = extra;
		this.extraLength = (short) extra.length;
	}
	
	public void setDelay(int delay) {
		thread.setDelay(delay);
	}
	
	public void start() {
		if(iniciado) throw new IllegalStateException("Este Beacon ya está iniciado");
		
		try {
			thread.setData(serializar(),puertoClientes);
			thread.start();
			iniciado = true;
		} catch (IOException e) {
			throw new RuntimeException("No se pudo iniciar el servidor por un error interno. ("+e+")");
		}
	}
	
	public void stop() {
		if(!iniciado) throw new IllegalStateException("Este Beacon no está iniciado");
		thread.stopThread();
		iniciado = false;
		thread = new BeaconThread();
	}
	
	private byte[] serializar() throws IOException {
		ByteArrayOutputStream bf = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bf);
		dos.writeLong(uuidHigh);
		dos.writeLong(uuidLow);
		dos.writeInt(version);
		dos.writeShort(puerto);
		dos.writeByte(descripcionLenght);
		if(descripcion != null) dos.write(descripcion);
		dos.writeShort(extraLength);
		if(extra != null)dos.write(extra);
		return bf.toByteArray();
	}
	
}
