package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Processo {

	private int cdProcesso;
	private int cdAit;
	private String nrProcesso;
	private GregorianCalendar dtProcesso;
	private String dsParecer;
	private int stProcesso;
	private int tpProcesso;
	private int stImpressao;
	private int tpOrigem;
	private int cdEstadoOrigem;
	private String nrOrgaoOrigem;
	private int cdUsuario;

	public Processo() { }

	public Processo(int cdProcesso,
			int cdAit,
			String nrProcesso,
			GregorianCalendar dtProcesso,
			String dsParecer,
			int stProcesso,
			int tpProcesso,
			int stImpressao,
			int tpOrigem,
			int cdEstadoOrigem,
			String nrOrgaoOrigem,
			int cdUsuario) {
		setCdProcesso(cdProcesso);
		setCdAit(cdAit);
		setNrProcesso(nrProcesso);
		setDtProcesso(dtProcesso);
		setDsParecer(dsParecer);
		setStProcesso(stProcesso);
		setTpProcesso(tpProcesso);
		setStImpressao(stImpressao);
		setTpOrigem(tpOrigem);
		setCdEstadoOrigem(cdEstadoOrigem);
		setNrOrgaoOrigem(nrOrgaoOrigem);
		setCdUsuario(cdUsuario);
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setNrProcesso(String nrProcesso){
		this.nrProcesso=nrProcesso;
	}
	public String getNrProcesso(){
		return this.nrProcesso;
	}
	public void setDtProcesso(GregorianCalendar dtProcesso){
		this.dtProcesso=dtProcesso;
	}
	public GregorianCalendar getDtProcesso(){
		return this.dtProcesso;
	}
	public void setDsParecer(String dsParecer){
		this.dsParecer=dsParecer;
	}
	public String getDsParecer(){
		return this.dsParecer;
	}
	public void setStProcesso(int stProcesso){
		this.stProcesso=stProcesso;
	}
	public int getStProcesso(){
		return this.stProcesso;
	}
	public void setTpProcesso(int tpProcesso){
		this.tpProcesso=tpProcesso;
	}
	public int getTpProcesso(){
		return this.tpProcesso;
	}
	public void setStImpressao(int stImpressao){
		this.stImpressao=stImpressao;
	}
	public int getStImpressao(){
		return this.stImpressao;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setCdEstadoOrigem(int cdEstadoOrigem){
		this.cdEstadoOrigem=cdEstadoOrigem;
	}
	public int getCdEstadoOrigem(){
		return this.cdEstadoOrigem;
	}
	public void setNrOrgaoOrigem(String nrOrgaoOrigem){
		this.nrOrgaoOrigem=nrOrgaoOrigem;
	}
	public String getNrOrgaoOrigem(){
		return this.nrOrgaoOrigem;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", nrProcesso: " +  getNrProcesso();
		valueToString += ", dtProcesso: " +  sol.util.Util.formatDateTime(getDtProcesso(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsParecer: " +  getDsParecer();
		valueToString += ", stProcesso: " +  getStProcesso();
		valueToString += ", tpProcesso: " +  getTpProcesso();
		valueToString += ", stImpressao: " +  getStImpressao();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", cdEstadoOrigem: " +  getCdEstadoOrigem();
		valueToString += ", nrOrgaoOrigem: " +  getNrOrgaoOrigem();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Processo(getCdProcesso(),
			getCdAit(),
			getNrProcesso(),
			getDtProcesso()==null ? null : (GregorianCalendar)getDtProcesso().clone(),
			getDsParecer(),
			getStProcesso(),
			getTpProcesso(),
			getStImpressao(),
			getTpOrigem(),
			getCdEstadoOrigem(),
			getNrOrgaoOrigem(),
			getCdUsuario());
	}

}