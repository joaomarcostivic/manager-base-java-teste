package com.tivic.manager.acd;

public class Circulo {

	private int cdCirculo;
	private String nmCirculo;
	private int tpCirculo;
	private String txtObservacao;
	private String idCirculo;
	private int cdInstituicaoPrincipal;

	public Circulo(){ }

	public Circulo(int cdCirculo,
			String nmCirculo,
			int tpCirculo,
			String txtObservacao,
			String idCirculo,
			int cdInstituicaoPrincipal){
		setCdCirculo(cdCirculo);
		setNmCirculo(nmCirculo);
		setTpCirculo(tpCirculo);
		setTxtObservacao(txtObservacao);
		setIdCirculo(idCirculo);
		setCdInstituicaoPrincipal(cdInstituicaoPrincipal);
	}
	public void setCdCirculo(int cdCirculo){
		this.cdCirculo=cdCirculo;
	}
	public int getCdCirculo(){
		return this.cdCirculo;
	}
	public void setNmCirculo(String nmCirculo){
		this.nmCirculo=nmCirculo;
	}
	public String getNmCirculo(){
		return this.nmCirculo;
	}
	public void setTpCirculo(int tpCirculo){
		this.tpCirculo=tpCirculo;
	}
	public int getTpCirculo(){
		return this.tpCirculo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setIdCirculo(String idCirculo){
		this.idCirculo=idCirculo;
	}
	public String getIdCirculo(){
		return this.idCirculo;
	}
	public void setCdInstituicaoPrincipal(int cdInstituicaoPrincipal){
		this.cdInstituicaoPrincipal=cdInstituicaoPrincipal;
	}
	public int getCdInstituicaoPrincipal(){
		return this.cdInstituicaoPrincipal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCirculo: " +  getCdCirculo();
		valueToString += ", nmCirculo: " +  getNmCirculo();
		valueToString += ", tpCirculo: " +  getTpCirculo();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", idCirculo: " +  getIdCirculo();
		valueToString += ", cdInstituicaoPrincipal: " +  getCdInstituicaoPrincipal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Circulo(getCdCirculo(),
			getNmCirculo(),
			getTpCirculo(),
			getTxtObservacao(),
			getIdCirculo(),
			getCdInstituicaoPrincipal());
	}

}