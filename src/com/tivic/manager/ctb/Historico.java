package com.tivic.manager.ctb;

public class Historico {

	private int cdHistorico;
	private int cdEmpresa;
	private String nmHistorico;
	private String idHistorico;
	private int lgComplemento;

	public Historico(int cdHistorico,
			int cdEmpresa,
			String nmHistorico,
			String idHistorico,
			int lgComplemento){
		setCdHistorico(cdHistorico);
		setCdEmpresa(cdEmpresa);
		setNmHistorico(nmHistorico);
		setIdHistorico(idHistorico);
		setLgComplemento(lgComplemento);
	}
	public void setCdHistorico(int cdHistorico){
		this.cdHistorico=cdHistorico;
	}
	public int getCdHistorico(){
		return this.cdHistorico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmHistorico(String nmHistorico){
		this.nmHistorico=nmHistorico;
	}
	public String getNmHistorico(){
		return this.nmHistorico;
	}
	public void setIdHistorico(String idHistorico){
		this.idHistorico=idHistorico;
	}
	public String getIdHistorico(){
		return this.idHistorico;
	}
	public void setLgComplemento(int lgComplemento){
		this.lgComplemento=lgComplemento;
	}
	public int getLgComplemento(){
		return this.lgComplemento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHistorico: " +  getCdHistorico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmHistorico: " +  getNmHistorico();
		valueToString += ", idHistorico: " +  getIdHistorico();
		valueToString += ", lgComplemento: " +  getLgComplemento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Historico(getCdHistorico(),
			getCdEmpresa(),
			getNmHistorico(),
			getIdHistorico(),
			getLgComplemento());
	}

}
