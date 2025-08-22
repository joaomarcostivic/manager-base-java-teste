package com.tivic.manager.crm;

public class Fidelidade {

	private int cdFidelidade;
	private String nmFidelidade;
	private String txtFidelidade;
	private String nmUnidade;
	private float vlFatorConversao;

	public Fidelidade(int cdFidelidade,
			String nmFidelidade,
			String txtFidelidade,
			String nmUnidade,
			float vlFatorConversao){
		setCdFidelidade(cdFidelidade);
		setNmFidelidade(nmFidelidade);
		setTxtFidelidade(txtFidelidade);
		setNmUnidade(nmUnidade);
		setVlFatorConversao(vlFatorConversao);
	}
	public void setCdFidelidade(int cdFidelidade){
		this.cdFidelidade=cdFidelidade;
	}
	public int getCdFidelidade(){
		return this.cdFidelidade;
	}
	public void setNmFidelidade(String nmFidelidade){
		this.nmFidelidade=nmFidelidade;
	}
	public String getNmFidelidade(){
		return this.nmFidelidade;
	}
	public void setTxtFidelidade(String txtFidelidade){
		this.txtFidelidade=txtFidelidade;
	}
	public String getTxtFidelidade(){
		return this.txtFidelidade;
	}
	public void setNmUnidade(String nmUnidade){
		this.nmUnidade=nmUnidade;
	}
	public String getNmUnidade(){
		return this.nmUnidade;
	}
	public void setVlFatorConversao(float vlFatorConversao){
		this.vlFatorConversao=vlFatorConversao;
	}
	public float getVlFatorConversao(){
		return this.vlFatorConversao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFidelidade: " +  getCdFidelidade();
		valueToString += ", nmFidelidade: " +  getNmFidelidade();
		valueToString += ", txtFidelidade: " +  getTxtFidelidade();
		valueToString += ", nmUnidade: " +  getNmUnidade();
		valueToString += ", vlFatorConversao: " +  getVlFatorConversao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Fidelidade(getCdFidelidade(),
			getNmFidelidade(),
			getTxtFidelidade(),
			getNmUnidade(),
			getVlFatorConversao());
	}

}
