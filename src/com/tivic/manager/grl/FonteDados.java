package com.tivic.manager.grl;

public class FonteDados {

	private int cdFonte;
	private String nmFonte;
	private String txtFonte;
	private String txtScript;
	private String txtColumns;
	private String idFonte;
	private int tpOrigem;
	private int tpFonte;

	public FonteDados(int cdFonte,
			String nmFonte,
			String txtFonte,
			String txtScript,
			String txtColumns,
			String idFonte,
			int tpOrigem,
			int tpFonte){
		setCdFonte(cdFonte);
		setNmFonte(nmFonte);
		setTxtFonte(txtFonte);
		setTxtScript(txtScript);
		setTxtColumns(txtColumns);
		setIdFonte(idFonte);
		setTpOrigem(tpOrigem);
		setTpFonte(tpFonte);
	}
	public void setCdFonte(int cdFonte){
		this.cdFonte=cdFonte;
	}
	public int getCdFonte(){
		return this.cdFonte;
	}
	public void setNmFonte(String nmFonte){
		this.nmFonte=nmFonte;
	}
	public String getNmFonte(){
		return this.nmFonte;
	}
	public void setTxtFonte(String txtFonte){
		this.txtFonte=txtFonte;
	}
	public String getTxtFonte(){
		return this.txtFonte;
	}
	public void setTxtScript(String txtScript){
		this.txtScript=txtScript;
	}
	public String getTxtScript(){
		return this.txtScript;
	}
	public void setTxtColumns(String txtColumns){
		this.txtColumns=txtColumns;
	}
	public String getTxtColumns(){
		return this.txtColumns;
	}
	public void setIdFonte(String idFonte){
		this.idFonte=idFonte;
	}
	public String getIdFonte(){
		return this.idFonte;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setTpFonte(int tpFonte){
		this.tpFonte=tpFonte;
	}
	public int getTpFonte(){
		return this.tpFonte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFonte: " +  getCdFonte();
		valueToString += ", nmFonte: " +  getNmFonte();
		valueToString += ", txtFonte: " +  getTxtFonte();
		valueToString += ", txtScript: " +  getTxtScript();
		valueToString += ", txtColumns: " +  getTxtColumns();
		valueToString += ", idFonte: " +  getIdFonte();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", tpFonte: " +  getTpFonte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FonteDados(getCdFonte(),
			getNmFonte(),
			getTxtFonte(),
			getTxtScript(),
			getTxtColumns(),
			getIdFonte(),
			getTpOrigem(),
			getTpFonte());
	}

}
