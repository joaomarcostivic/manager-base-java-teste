package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class Balanco {

	private int cdBalanco;
	private int cdEmpresa;
	private GregorianCalendar dtBalanco;
	private String txtBalanco;
	private int stBalanco;
	private int cdUsuario;
	private GregorianCalendar dtFechamento;
	private String nrBalanco;
	private int cdPessoa;
	private int tpBalanco;
	private int cdLocalArmazenamento;
	private int cdGrupo;
	
	public Balanco() {}
	public Balanco(int cdBalanco,
			int cdEmpresa,
			GregorianCalendar dtBalanco,
			String txtBalanco,
			int stBalanco,
			int cdUsuario,
			GregorianCalendar dtFechamento,
			String nrBalanco,
			int cdPessoa,
			int tpBalanco,
			int cdLocalArmazenamento,
			int cdGrupo){
		setCdBalanco(cdBalanco);
		setCdEmpresa(cdEmpresa);
		setDtBalanco(dtBalanco);
		setTxtBalanco(txtBalanco);
		setStBalanco(stBalanco);
		setCdUsuario(cdUsuario);
		setDtFechamento(dtFechamento);
		setNrBalanco(nrBalanco);
		setCdPessoa(cdPessoa);
		setTpBalanco(tpBalanco);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdGrupo(cdGrupo);
	}
	public void setCdBalanco(int cdBalanco){
		this.cdBalanco=cdBalanco;
	}
	public int getCdBalanco(){
		return this.cdBalanco;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setDtBalanco(GregorianCalendar dtBalanco){
		this.dtBalanco=dtBalanco;
	}
	public GregorianCalendar getDtBalanco(){
		return this.dtBalanco;
	}
	public void setTxtBalanco(String txtBalanco){
		this.txtBalanco=txtBalanco;
	}
	public String getTxtBalanco(){
		return this.txtBalanco;
	}
	public void setStBalanco(int stBalanco){
		this.stBalanco=stBalanco;
	}
	public int getStBalanco(){
		return this.stBalanco;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setNrBalanco(String nrBalanco){
		this.nrBalanco=nrBalanco;
	}
	public String getNrBalanco(){
		return this.nrBalanco;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpBalanco(int tpBalanco){
		this.tpBalanco=tpBalanco;
	}
	public int getTpBalanco(){
		return this.tpBalanco;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBalanco: " +  getCdBalanco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", dtBalanco: " +  sol.util.Util.formatDateTime(getDtBalanco(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtBalanco: " +  getTxtBalanco();
		valueToString += ", stBalanco: " +  getStBalanco();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtFechamento: " +  sol.util.Util.formatDateTime(getDtFechamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrBalanco: " +  getNrBalanco();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpBalanco: " +  getTpBalanco();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Balanco(getCdBalanco(),
			getCdEmpresa(),
			getDtBalanco()==null ? null : (GregorianCalendar)getDtBalanco().clone(),
			getTxtBalanco(),
			getStBalanco(),
			getCdUsuario(),
			getDtFechamento()==null ? null : (GregorianCalendar)getDtFechamento().clone(),
			getNrBalanco(),
			getCdPessoa(),
			getTpBalanco(),
			getCdLocalArmazenamento(),
			getCdGrupo());
	}

}