package com.tivic.manager.mob;

public class PlanoVistoria {

	private int cdPlanoVistoria;
	private String nmPlanoVistoria;
	private int cdFormulario;
	private int tpConcessao;

	public PlanoVistoria(){ }

	public PlanoVistoria(int cdPlanoVistoria,
			String nmPlanoVistoria,
			int cdFormulario,
			int tpConcessao){
		setCdPlanoVistoria(cdPlanoVistoria);
		setNmPlanoVistoria(nmPlanoVistoria);
		setCdFormulario(cdFormulario);
		setTpConcessao(tpConcessao);
	}
	public void setCdPlanoVistoria(int cdPlanoVistoria){
		this.cdPlanoVistoria=cdPlanoVistoria;
	}
	public int getCdPlanoVistoria(){
		return this.cdPlanoVistoria;
	}
	public void setNmPlanoVistoria(String nmPlanoVistoria){
		this.nmPlanoVistoria=nmPlanoVistoria;
	}
	public String getNmPlanoVistoria(){
		return this.nmPlanoVistoria;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setTpConcessao(int tpConcessao){
		this.tpConcessao=tpConcessao;
	}
	public int getTpConcessao(){
		return this.tpConcessao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", nmPlanoVistoria: " +  getNmPlanoVistoria();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", tpConcessao: " +  getTpConcessao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoVistoria(getCdPlanoVistoria(),
			getNmPlanoVistoria(),
			getCdFormulario(),
			getTpConcessao());
	}

}