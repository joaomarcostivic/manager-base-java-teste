package com.tivic.manager.acd;

public class ModalidadeEducarte {

	private int cdModalidade;
	private String nmModalidade;
	private String idModalidade;
	private int stModalidade;
	private String txtDescricao;

	public ModalidadeEducarte() { }

	public ModalidadeEducarte(int cdModalidade,
			String nmModalidade,
			String idModalidade,
			int stModalidade,
			String txtDescricao) {
		setCdModalidade(cdModalidade);
		setNmModalidade(nmModalidade);
		setIdModalidade(idModalidade);
		setStModalidade(stModalidade);
		setTxtDescricao(txtDescricao);
	}
	public void setCdModalidade(int cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public int getCdModalidade(){
		return this.cdModalidade;
	}
	public void setNmModalidade(String nmModalidade){
		this.nmModalidade=nmModalidade;
	}
	public String getNmModalidade(){
		return this.nmModalidade;
	}
	public void setIdModalidade(String idModalidade){
		this.idModalidade=idModalidade;
	}
	public String getIdModalidade(){
		return this.idModalidade;
	}
	public void setStModalidade(int stModalidade){
		this.stModalidade=stModalidade;
	}
	public int getStModalidade(){
		return this.stModalidade;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModalidade: " +  getCdModalidade();
		valueToString += ", nmModalidade: " +  getNmModalidade();
		valueToString += ", idModalidade: " +  getIdModalidade();
		valueToString += ", stModalidade: " +  getStModalidade();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModalidadeEducarte(getCdModalidade(),
			getNmModalidade(),
			getIdModalidade(),
			getStModalidade(),
			getTxtDescricao());
	}

}