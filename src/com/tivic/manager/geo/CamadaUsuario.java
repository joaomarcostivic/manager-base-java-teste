package com.tivic.manager.geo;

public class CamadaUsuario {

	private int cdCamada;
	private int cdUsuario;

	public CamadaUsuario(){ }

	public CamadaUsuario(int cdCamada,
			int cdUsuario){
		setCdCamada(cdCamada);
		setCdUsuario(cdUsuario);
	}
	public void setCdCamada(int cdCamada){
		this.cdCamada=cdCamada;
	}
	public int getCdCamada(){
		return this.cdCamada;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCamada: " +  getCdCamada();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CamadaUsuario(getCdCamada(),
			getCdUsuario());
	}

}
