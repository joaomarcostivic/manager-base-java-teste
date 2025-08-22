package com.tivic.manager.grl;

public class FiltroCampo {

	private int cdCampo;
	private String nmCampo;
	private String idCampo;
	private String nmTabela;
	private int tpRelatorio;
	private int tpCampo;

	public FiltroCampo() { }

	public FiltroCampo(int cdCampo,
			String nmCampo,
			String idCampo,
			String nmTabela,
			int tpRelatorio,
			int tpCampo) {
		setCdCampo(cdCampo);
		setNmCampo(nmCampo);
		setIdCampo(idCampo);
		setNmTabela(nmTabela);
		setTpRelatorio(tpRelatorio);
		setTpCampo(tpCampo);
	}
	public void setCdCampo(int cdCampo){
		this.cdCampo=cdCampo;
	}
	public int getCdCampo(){
		return this.cdCampo;
	}
	public void setNmCampo(String nmCampo){
		this.nmCampo=nmCampo;
	}
	public String getNmCampo(){
		return this.nmCampo;
	}
	public void setIdCampo(String idCampo){
		this.idCampo=idCampo;
	}
	public String getIdCampo(){
		return this.idCampo;
	}
	public void setNmTabela(String nmTabela){
		this.nmTabela=nmTabela;
	}
	public String getNmTabela(){
		return this.nmTabela;
	}
	public void setTpRelatorio(int tpRelatorio){
		this.tpRelatorio=tpRelatorio;
	}
	public int getTpRelatorio(){
		return this.tpRelatorio;
	}
	public void setTpCampo(int tpCampo){
		this.tpCampo=tpCampo;
	}
	public int getTpCampo(){
		return this.tpCampo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCampo: " +  getCdCampo();
		valueToString += ", nmCampo: " +  getNmCampo();
		valueToString += ", idCampo: " +  getIdCampo();
		valueToString += ", nmTabela: " +  getNmTabela();
		valueToString += ", tpRelatorio: " +  getTpRelatorio();
		valueToString += ", tpCampo: " +  getTpCampo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FiltroCampo(getCdCampo(),
			getNmCampo(),
			getIdCampo(),
			getNmTabela(),
			getTpRelatorio(),
			getTpCampo());
	}

}