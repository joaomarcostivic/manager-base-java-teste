package com.tivic.manager.mob;

public class InfracaoMotivo {

	private int cdInfracao;
	private int cdMotivo;
	private String nrMotivo;
	private String nmMotivo;
	private int stMotivo;

	public InfracaoMotivo(){ }

	public InfracaoMotivo(int cdInfracao,
			int cdMotivo,
			String nrMotivo,
			String nmMotivo,
			int stMotivo){
		setCdInfracao(cdInfracao);
		setCdMotivo(cdMotivo);
		setNrMotivo(nrMotivo);
		setNmMotivo(nmMotivo);
		setStMotivo(stMotivo);
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setNrMotivo(String nrMotivo){
		this.nrMotivo=nrMotivo;
	}
	public String getNrMotivo(){
		return this.nrMotivo;
	}
	public void setNmMotivo(String nmMotivo){
		this.nmMotivo=nmMotivo;
	}
	public String getNmMotivo(){
		return this.nmMotivo;
	}
	public void setStMotivo(int stMotivo){
		this.stMotivo=stMotivo;
	}
	public int getStMotivo(){
		return this.stMotivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInfracao: " +  getCdInfracao();
		valueToString += ", cdMotivo: " +  getCdMotivo();
		valueToString += ", nrMotivo: " +  getNrMotivo();
		valueToString += ", nmMotivo: " +  getNmMotivo();
		valueToString += ", stMotivo: " +  getStMotivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InfracaoMotivo(getCdInfracao(),
			getCdMotivo(),
			getNrMotivo(),
			getNmMotivo(),
			getStMotivo());
	}

}