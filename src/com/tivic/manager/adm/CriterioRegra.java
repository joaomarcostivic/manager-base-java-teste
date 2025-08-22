package com.tivic.manager.adm;

public class CriterioRegra {

	private int cdCriterio;
	private String nmCriterio;
	private String txtSql;

	public CriterioRegra(int cdCriterio,
			String nmCriterio,
			String txtSql){
		setCdCriterio(cdCriterio);
		setNmCriterio(nmCriterio);
		setTxtSql(txtSql);
	}
	public void setCdCriterio(int cdCriterio){
		this.cdCriterio=cdCriterio;
	}
	public int getCdCriterio(){
		return this.cdCriterio;
	}
	public void setNmCriterio(String nmCriterio){
		this.nmCriterio=nmCriterio;
	}
	public String getNmCriterio(){
		return this.nmCriterio;
	}
	public void setTxtSql(String txtSql){
		this.txtSql=txtSql;
	}
	public String getTxtSql(){
		return this.txtSql;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCriterio: " +  getCdCriterio();
		valueToString += ", nmCriterio: " +  getNmCriterio();
		valueToString += ", txtSql: " +  getTxtSql();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CriterioRegra(getCdCriterio(),
			getNmCriterio(),
			getTxtSql());
	}

}
