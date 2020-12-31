package juanf846.JavaUDPBeacon.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

public class BeaconData {
	private long uuidHigh;
	private long uuidLow;
	private int version;
	private short puerto;
	private byte descripcionLenght = 0;
	private byte[] descripcion;
	private short extraLength = 0;
	private byte[] extra;
	private InetAddress sender;
	
	public long getUuidHigh() {
		return uuidHigh;
	}
	public long getUuidLow() {
		return uuidLow;
	}
	public int getVersion() {
		return version;
	}
	public int getPuerto() {
		return Short.toUnsignedInt(puerto);
	}
	public byte[] getDescripcion() {
		return descripcion;
	}
	public byte[] getExtra() {
		return extra;
	}
	public InetAddress getSender() {
		return sender;
	}
	
	boolean compareUuid(long uuidHigh, long uuidLow){
		return uuidHigh == this.uuidHigh && uuidLow == this.uuidLow;
	}
	
	boolean compareVersion(int versionMin, int versionMax) {
		return this.version >= versionMin && this.version <= versionMax;
	}
	
	BeaconData(byte[] data, InetAddress sender) throws IOException{
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		
		this.uuidHigh = dis.readLong();
		this.uuidLow = dis.readLong();
		this.version = dis.readInt();
		this.puerto = dis.readShort();
		
		this.descripcionLenght = dis.readByte();
		this.descripcion = new byte[this.descripcionLenght];
		if(this.descripcionLenght != 0) dis.read(descripcion);
		
		this.extraLength = dis.readShort();
		this.extra = new byte[this.extraLength];
		if(this.extraLength != 0) dis.read(extra);
		
		this.sender = sender;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BeaconData) {
			BeaconData bd = (BeaconData) obj;
			
			return sender.equals(bd.getSender()) && 
					bd.compareUuid(uuidHigh, uuidLow) && 
					bd.compareVersion(version, version) &&
					bd.getPuerto() == puerto;
		}
		return super.equals(obj);
	}
}
