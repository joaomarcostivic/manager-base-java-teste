package com.tivic.manager.acd;

public class PlanoSecao {

	private int cdSecao;
	private String nmSecao;
	private String txtSecao;
	private String idSecao;
	private int tpPlano;

	public PlanoSecao(){ }

	public PlanoSecao(int cdSecao,
			String nmSecao,
			String txtSecao,
			String idSecao,
			int tpPlano){
		setCdSecao(cdSecao);
		setNmSecao(nmSecao);
		setTxtSecao(txtSecao);
		setIdSecao(idSecao);
		setTpPlano(tpPlano);
	}
	public void setCdSecao(int cdSecao){
		this.cdSecao=cdSecao;
	}
	public int getCdSecao(){
		return this.cdSecao;
	}
	public void setNmSecao(String nmSecao){
		this.nmSecao=nmSecao;
	}
	public String getNmSecao(){
		return this.nmSecao;
	}
	public void setTxtSecao(String txtSecao){
		this.txtSecao=txtSecao;
	}
	public String getTxtSecao(){
		return this.txtSecao;
	}
	public void setIdSecao(String idSecao){
		this.idSecao=idSecao;
	}
	public String getIdSecao(){
		return this.idSecao;
	}
	public void setTpPlano(int tpPlano){
		this.tpPlano=tpPlano;
	}
	public int getTpPlano(){
		return this.tpPlano;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSecao: " +  getCdSecao();
		valueToString += ", nmSecao: " +  getNmSecao();
		valueToString += ", txtSecao: " +  getTxtSecao();
		valueToString += ", idSecao: " +  getIdSecao();
		valueToString += ", tpPlano: " +  getTpPlano();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoSecao(getCdSecao(),
			getNmSecao(),
			getTxtSecao(),
			getIdSecao(),
			getTpPlano());
	}

}