package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ProcessoInstancia {

	private int cdProcesso;
	private int cdTipoProcesso;
	private int cdOrgaoJudicial;
	private int cdComarca;
	private int tpInstancia;
	private GregorianCalendar dtSentenca;
	private int stProcesso;
	private String txtSentenca;
	private String nrProcesso;

	public ProcessoInstancia(int cdProcesso,
			int cdTipoProcesso,
			int cdOrgaoJudicial,
			int cdComarca,
			int tpInstancia,
			GregorianCalendar dtSentenca,
			int stProcesso,
			String txtSentenca,
			String nrProcesso){
		setCdProcesso(cdProcesso);
		setCdTipoProcesso(cdTipoProcesso);
		setCdOrgaoJudicial(cdOrgaoJudicial);
		setCdComarca(cdComarca);
		setTpInstancia(tpInstancia);
		setDtSentenca(dtSentenca);
		setStProcesso(stProcesso);
		setTxtSentenca(txtSentenca);
		setNrProcesso(nrProcesso);
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setCdOrgaoJudicial(int cdOrgaoJudicial){
		this.cdOrgaoJudicial=cdOrgaoJudicial;
	}
	public int getCdOrgaoJudicial(){
		return this.cdOrgaoJudicial;
	}
	public void setCdComarca(int cdComarca){
		this.cdComarca=cdComarca;
	}
	public int getCdComarca(){
		return this.cdComarca;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public void setDtSentenca(GregorianCalendar dtSentenca){
		this.dtSentenca=dtSentenca;
	}
	public GregorianCalendar getDtSentenca(){
		return this.dtSentenca;
	}
	public void setStProcesso(int stProcesso){
		this.stProcesso=stProcesso;
	}
	public int getStProcesso(){
		return this.stProcesso;
	}
	public void setTxtSentenca(String txtSentenca){
		this.txtSentenca=txtSentenca;
	}
	public String getTxtSentenca(){
		return this.txtSentenca;
	}
	public void setNrProcesso(String nrProcesso){
		this.nrProcesso=nrProcesso;
	}
	public String getNrProcesso(){
		return this.nrProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", cdOrgaoJudicial: " +  getCdOrgaoJudicial();
		valueToString += ", cdComarca: " +  getCdComarca();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		valueToString += ", dtSentenca: " +  sol.util.Util.formatDateTime(getDtSentenca(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stProcesso: " +  getStProcesso();
		valueToString += ", txtSentenca: " +  getTxtSentenca();
		valueToString += ", nrProcesso: " +  getNrProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoInstancia(cdProcesso,
			cdTipoProcesso,
			cdOrgaoJudicial,
			cdComarca,
			tpInstancia,
			dtSentenca==null ? null : (GregorianCalendar)dtSentenca.clone(),
			stProcesso,
			txtSentenca,
			nrProcesso);
	}

}