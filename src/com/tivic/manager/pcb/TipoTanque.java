package com.tivic.manager.pcb;

public class TipoTanque {

	private int cdTipoTanque;
	private String idTipoTanque;
	private String nmTipoTanque;
	private float qtMedidaCm;
	private float qtLitros;
	private float qtLitrosMilimetros;
	private int stTipoTanque;
	private float qtCapacidade;

	public TipoTanque(int cdTipoTanque,
			String idTipoTanque,
			String nmTipoTanque,
			float qtMedidaCm,
			float qtLitros,
			float qtLitrosMilimetros,
			int stTipoTanque,float qtCapacidade){
		setCdTipoTanque(cdTipoTanque);
		setIdTipoTanque(idTipoTanque);
		setNmTipoTanque(nmTipoTanque);
		setQtMedidaCm(qtMedidaCm);
		setQtLitros(qtLitros);
		setQtLitrosMilimetros(qtLitrosMilimetros);
		setStTipoTanque(stTipoTanque);
		setQtCapacidade(qtCapacidade);
	}
	public void setCdTipoTanque(int cdTipoTanque){
		this.cdTipoTanque=cdTipoTanque;
	}
	public int getCdTipoTanque(){
		return this.cdTipoTanque;
	}
	public void setIdTipoTanque(String idTipoTanque){
		this.idTipoTanque=idTipoTanque;
	}
	public String getIdTipoTanque(){
		return this.idTipoTanque;
	}
	public void setNmTipoTanque(String nmTipoTanque){
		this.nmTipoTanque=nmTipoTanque;
	}
	public String getNmTipoTanque(){
		return this.nmTipoTanque;
	}
	public void setQtMedidaCm(float qtMedidaCm){
		this.qtMedidaCm=qtMedidaCm;
	}
	public float getQtMedidaCm(){
		return this.qtMedidaCm;
	}
	public void setQtLitros(float qtLitros){
		this.qtLitros=qtLitros;
	}
	public float getQtLitros(){
		return this.qtLitros;
	}
	public void setQtLitrosMilimetros(float qtLitrosMilimetros){
		this.qtLitrosMilimetros=qtLitrosMilimetros;
	}
	public float getQtLitrosMilimetros(){
		return this.qtLitrosMilimetros;
	}
	public int getStTipoTanque() {
		return stTipoTanque;
	}
	public void setStTipoTanque(int stTipoTanque) {
		this.stTipoTanque = stTipoTanque;
	}
	public float getQtCapacidade() {
		return qtCapacidade;
	}
	public void setQtCapacidade(float qtCapacidade) {
		this.qtCapacidade = qtCapacidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoTanque: " +  getCdTipoTanque();
		valueToString += ", idTipoTanque: " +  getIdTipoTanque();
		valueToString += ", nmTipoTanque: " +  getNmTipoTanque();
		valueToString += ", qtMedidaCm: " +  getQtMedidaCm();
		valueToString += ", qtLitros: " +  getQtLitros();
		valueToString += ", qtLitrosMilimetros: " +  getQtLitrosMilimetros();
		valueToString += ", qtCapacidade: " +  getQtCapacidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoTanque(getCdTipoTanque(),
			getIdTipoTanque(),
			getNmTipoTanque(),
			getQtMedidaCm(),
			getQtLitros(),
			getQtLitrosMilimetros(),
			getStTipoTanque(),
			getQtCapacidade());
	}

}