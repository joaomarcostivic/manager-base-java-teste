package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class Empresa extends com.tivic.manager.grl.Empresa {

	private String nrRegistroCartorio;
	private String nrNire;
	private String nrInscricaoSuframa;
	private GregorianCalendar dtUltimaAuditoria;
	private String nrOab;
	private String nrJuntaComercial;
	private GregorianCalendar dtJuntaComercial;

	public Empresa(int cdEmpresa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais,
			String nrCnpj,
			String nmRazaoSocial,
			String nrInscricaoEstadual,
			String nrInscricaoMunicipal,
			int nrFuncionarios,
			GregorianCalendar dtInicioAtividade,
			int cdNaturezaJuridica,
			int tpEmpresa,
			GregorianCalendar dtTerminoAtividade,
			int lgMatriz,
			byte[] imgLogomarca,
			String idEmpresa,
			int cdTabelaCatEconomica,
			String nrRegistroCartorio,
			String nrNire,
			String nrInscricaoSuframa,
			GregorianCalendar dtUltimaAuditoria,
			String nrOab,
			String nrJuntaComercial,
			GregorianCalendar dtJuntaComercial){
		super(cdEmpresa,
				cdPessoaSuperior,
				cdPais,
				nmPessoa,
				nrTelefone1,
				nrTelefone2,
				nrCelular,
				nrFax,
				nmEmail,
				dtCadastro,
				gnPessoa,
				imgFoto,
				stCadastro,
				nmUrl,
				nmApelido,
				txtObservacao,
				lgNotificacao,
				idPessoa,
				cdClassificacao,
				cdFormaDivulgacao,
				dtChegadaPais,
				nrCnpj,
				nmRazaoSocial,
				nrInscricaoEstadual,
				nrInscricaoMunicipal,
				nrFuncionarios,
				dtInicioAtividade,
				cdNaturezaJuridica,
				tpEmpresa,
				dtTerminoAtividade,
				lgMatriz,
				imgLogomarca,
				idEmpresa,
				cdTabelaCatEconomica);
		setNrRegistroCartorio(nrRegistroCartorio);
		setNrNire(nrNire);
		setNrInscricaoSuframa(nrInscricaoSuframa);
		setDtUltimaAuditoria(dtUltimaAuditoria);
		setNrOab(nrOab);
		setNrJuntaComercial(nrJuntaComercial);
		setDtJuntaComercial(dtJuntaComercial);
	}
	public void setNrRegistroCartorio(String nrRegistroCartorio){
		this.nrRegistroCartorio=nrRegistroCartorio;
	}
	public String getNrRegistroCartorio(){
		return this.nrRegistroCartorio;
	}
	public void setNrNire(String nrNire){
		this.nrNire=nrNire;
	}
	public String getNrNire(){
		return this.nrNire;
	}
	public void setNrInscricaoSuframa(String nrInscricaoSuframa){
		this.nrInscricaoSuframa=nrInscricaoSuframa;
	}
	public String getNrInscricaoSuframa(){
		return this.nrInscricaoSuframa;
	}
	public void setDtUltimaAuditoria(GregorianCalendar dtUltimaAuditoria){
		this.dtUltimaAuditoria=dtUltimaAuditoria;
	}
	public GregorianCalendar getDtUltimaAuditoria(){
		return this.dtUltimaAuditoria;
	}
	public void setNrOab(String nrOab){
		this.nrOab=nrOab;
	}
	public String getNrOab(){
		return this.nrOab;
	}
	public void setNrJuntaComercial(String nrJuntaComercial){
		this.nrJuntaComercial=nrJuntaComercial;
	}
	public String getNrJuntaComercial(){
		return this.nrJuntaComercial;
	}
	public void setDtJuntaComercial(GregorianCalendar dtJuntaComercial){
		this.dtJuntaComercial=dtJuntaComercial;
	}
	public GregorianCalendar getDtJuntaComercial(){
		return this.dtJuntaComercial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nrRegistroCartorio: " +  getNrRegistroCartorio();
		valueToString += ", nrNire: " +  getNrNire();
		valueToString += ", nrInscricaoSuframa: " +  getNrInscricaoSuframa();
		valueToString += ", dtUltimaAuditoria: " +  sol.util.Util.formatDateTime(getDtUltimaAuditoria(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrOab: " +  getNrOab();
		valueToString += ", nrJuntaComercial: " +  getNrJuntaComercial();
		valueToString += ", dtJuntaComercial: " +  sol.util.Util.formatDateTime(getDtJuntaComercial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Empresa(getCdPessoa(),
				getCdPessoaSuperior(),
				getCdPais(),
				getNmPessoa(),
				getNrTelefone1(),
				getNrTelefone2(),
				getNrCelular(),
				getNrFax(),
				getNmEmail(),
				getDtCadastro(),
				getGnPessoa(),
				getImgFoto(),
				getStCadastro(),
				getNmUrl(),
				getNmApelido(),
				getTxtObservacao(),
				getLgNotificacao(),
				getIdPessoa(),
				getCdClassificacao(),
				getCdFormaDivulgacao(),
				getDtChegadaPais(),
				getNrCnpj(),
				getNmRazaoSocial(),
				getNrInscricaoEstadual(),
				getNrInscricaoMunicipal(),
				getNrFuncionarios(),
				getDtInicioAtividade()==null ? null : (GregorianCalendar)getDtInicioAtividade().clone(),
				getCdNaturezaJuridica(),
				getTpEmpresa(),
				getDtTerminoAtividade()==null ? null : (GregorianCalendar)getDtTerminoAtividade().clone(),
				getLgMatriz(),
				getImgLogomarca(),
				getIdEmpresa(),
				getCdTabelaCatEconomica(),
				getNrRegistroCartorio(),
				getNrNire(),
				getNrInscricaoSuframa(),
				getDtUltimaAuditoria()==null ? null : (GregorianCalendar)getDtUltimaAuditoria().clone(),
				getNrOab(),
				getNrJuntaComercial(),
				getDtJuntaComercial()==null ? null : (GregorianCalendar)getDtJuntaComercial().clone());
	}

}
