package juanf846.JavaUDPBeacon.client;

import java.util.ArrayList;

/**
 * Esta clase se encarga de recibir los paquetes y devuelve un vector con los servicios encontrados.
 * 
 * @author JuanFDiz
 *
 */
public class Receiver {
	private long uuidHighFilter = 0;
	private long uuidLowFilter = 0;
	private int versionMin = 0;
	private int versionMax = 0;
	private short puerto = 0;
	
	private ArrayList<BeaconData> beacons = new ArrayList<BeaconData>();
	private ReceiverThread thread;
	private DataChangeListener listener;
	private boolean iniciado = false;
	
	/**
	 * Devuelve un vector con todos los servicios encontrados.
	 */
	public BeaconData[] getData() {
		return beacons.toArray(new BeaconData[0]);
	}
	
	/**
	 * Agrega un servicio encontrado al vector de servicios encontrados.
	 */
	void addData(BeaconData beaconData) {
		if(beacons.contains(beaconData)) beacons.remove(beaconData);
		beacons.add(beaconData);
		listener.change(getData());
	}

	/**
	 * Agrega un listener para cuando se encuentre un nuevo servicio.
	 */
	public void setListener(DataChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * Construye un nuevo receptor.
	 * 
	 * @param uuidHighFilter Primeros 64 bits del UUID.
	 * @param uuidLowFilter Ultimos 64 bits del UUID.
	 * @param versionMin Para que un servicio sea considerado compatible, debe tener una version igual o mayor a este parametro.
	 * @param versionMax Para que un servicio sea considerado compatible, debe tener una version igual o menos a este parametro.
	 * @param puerto Puerto en el que se van a recibir paquetes.
	 */
	public Receiver(long uuidHighFilter, long uuidLowFilter, int versionMin, int versionMax, short puerto) {
		this.uuidHighFilter = uuidHighFilter;
		this.uuidLowFilter = uuidLowFilter;
		this.versionMin = versionMin;
		this.versionMax = versionMax;
		this.puerto = puerto;
	}
	
	/**
	 * Inicia el receptor. Solo se puede iniciar el receptor una vez, si ya está iniciado este metodo no hará nada.
	 */
	public void start() {
		if(iniciado) return;
		
		thread = new ReceiverThread(uuidHighFilter, uuidLowFilter, versionMin, versionMax, this, puerto);
		thread.start();
		iniciado = true;
	}
	
	/**
	 * Detiene el receptor. Solo se puede detener el receptor una vez, si ya está detenido este metodo no hará nada.
	 */
	public void stop() {
		if(!iniciado) throw new IllegalStateException("Este Receiver no está iniciado");
		thread.stopThread();
		iniciado = false;
		thread = null;
	}
	
	/**
	 * Listener que se ejecuta cada vez que se encuentra un nuevo servicio.
	 * 
	 * @author JuanFDiz
	 */
	public interface DataChangeListener{
		/**
		 * Este metodo se llamará cada vez que se encuentre un nuevo servicio. El vector que se envia por parametros 
		 * es el mismo que devuelve {@link #getData()}
		 * @param data
		 */
		public void change(BeaconData[] data);
	}
	
}