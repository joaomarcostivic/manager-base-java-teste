package com.tivic.manager.mcr;

import java.util.GregorianCalendar;

public class Empreendimento {

	private int cdEmpreendimento;
	private int cdPessoa;
	private int tpNegocio;
	private int tpLocal;
	private int tpPontoComercial;
	private GregorianCalendar dtContrato;
	private String nmOutroPonto;
	private int nrDiasAtividade;
	private String nmLocalAtividade;
	private int tpAtividade;
	private int tpEstruturacao;
	private int lgEmpresaFamiliar;
	private int nrTempoExperiencia;
	private int nrTempoIndependencia;
	private String txtAprendizado;
	private String txtHistorico;
	private int vlMuitoBom;
	private int vlBom;
	private int vlRegular;
	private int vlFraco;
	private String txtConcorrencia;
	private String txtFidelidade;
	private String txtCapacidadeInstalada;
	private GregorianCalendar dtInformacaoBem;
	private int nrDiaPrestacao;
	private String txtObservacao;
	private String idEmpreendimento;

	public Empreendimento(int cdEmpreendimento,
			int cdPessoa,
			int tpNegocio,
			int tpLocal,
			int tpPontoComercial,
			GregorianCalendar dtContrato,
			String nmOutroPonto,
			int nrDiasAtividade,
			String nmLocalAtividade,
			int tpAtividade,
			int tpEstruturacao,
			int lgEmpresaFamiliar,
			int nrTempoExperiencia,
			int nrTempoIndependencia,
			String txtAprendizado,
			String txtHistorico,
			int vlMuitoBom,
			int vlBom,
			int vlRegular,
			int vlFraco,
			String txtConcorrencia,
			String txtFidelidade,
			String txtCapacidadeInstalada,
			GregorianCalendar dtInformacaoBem,
			int nrDiaPrestacao,
			String txtObservacao,
			String idEmpreendimento){
		setCdEmpreendimento(cdEmpreendimento);
		setCdPessoa(cdPessoa);
		setTpNegocio(tpNegocio);
		setTpLocal(tpLocal);
		setTpPontoComercial(tpPontoComercial);
		setDtContrato(dtContrato);
		setNmOutroPonto(nmOutroPonto);
		setNrDiasAtividade(nrDiasAtividade);
		setNmLocalAtividade(nmLocalAtividade);
		setTpAtividade(tpAtividade);
		setTpEstruturacao(tpEstruturacao);
		setLgEmpresaFamiliar(lgEmpresaFamiliar);
		setNrTempoExperiencia(nrTempoExperiencia);
		setNrTempoIndependencia(nrTempoIndependencia);
		setTxtAprendizado(txtAprendizado);
		setTxtHistorico(txtHistorico);
		setVlMuitoBom(vlMuitoBom);
		setVlBom(vlBom);
		setVlRegular(vlRegular);
		setVlFraco(vlFraco);
		setTxtConcorrencia(txtConcorrencia);
		setTxtFidelidade(txtFidelidade);
		setTxtCapacidadeInstalada(txtCapacidadeInstalada);
		setDtInformacaoBem(dtInformacaoBem);
		setNrDiaPrestacao(nrDiaPrestacao);
		setTxtObservacao(txtObservacao);
		setIdEmpreendimento(idEmpreendimento);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpNegocio(int tpNegocio){
		this.tpNegocio=tpNegocio;
	}
	public int getTpNegocio(){
		return this.tpNegocio;
	}
	public void setTpLocal(int tpLocal){
		this.tpLocal=tpLocal;
	}
	public int getTpLocal(){
		return this.tpLocal;
	}
	public void setTpPontoComercial(int tpPontoComercial){
		this.tpPontoComercial=tpPontoComercial;
	}
	public int getTpPontoComercial(){
		return this.tpPontoComercial;
	}
	public void setDtContrato(GregorianCalendar dtContrato){
		this.dtContrato=dtContrato;
	}
	public GregorianCalendar getDtContrato(){
		return this.dtContrato;
	}
	public void setNmOutroPonto(String nmOutroPonto){
		this.nmOutroPonto=nmOutroPonto;
	}
	public String getNmOutroPonto(){
		return this.nmOutroPonto;
	}
	public void setNrDiasAtividade(int nrDiasAtividade){
		this.nrDiasAtividade=nrDiasAtividade;
	}
	public int getNrDiasAtividade(){
		return this.nrDiasAtividade;
	}
	public void setNmLocalAtividade(String nmLocalAtividade){
		this.nmLocalAtividade=nmLocalAtividade;
	}
	public String getNmLocalAtividade(){
		return this.nmLocalAtividade;
	}
	public void setTpAtividade(int tpAtividade){
		this.tpAtividade=tpAtividade;
	}
	public int getTpAtividade(){
		return this.tpAtividade;
	}
	public void setTpEstruturacao(int tpEstruturacao){
		this.tpEstruturacao=tpEstruturacao;
	}
	public int getTpEstruturacao(){
		return this.tpEstruturacao;
	}
	public void setLgEmpresaFamiliar(int lgEmpresaFamiliar){
		this.lgEmpresaFamiliar=lgEmpresaFamiliar;
	}
	public int getLgEmpresaFamiliar(){
		return this.lgEmpresaFamiliar;
	}
	public void setNrTempoExperiencia(int nrTempoExperiencia){
		this.nrTempoExperiencia=nrTempoExperiencia;
	}
	public int getNrTempoExperiencia(){
		return this.nrTempoExperiencia;
	}
	public void setNrTempoIndependencia(int nrTempoIndependencia){
		this.nrTempoIndependencia=nrTempoIndependencia;
	}
	public int getNrTempoIndependencia(){
		return this.nrTempoIndependencia;
	}
	public void setTxtAprendizado(String txtAprendizado){
		this.txtAprendizado=txtAprendizado;
	}
	public String getTxtAprendizado(){
		return this.txtAprendizado;
	}
	public void setTxtHistorico(String txtHistorico){
		this.txtHistorico=txtHistorico;
	}
	public String getTxtHistorico(){
		return this.txtHistorico;
	}
	public void setVlMuitoBom(int vlMuitoBom){
		this.vlMuitoBom=vlMuitoBom;
	}
	public int getVlMuitoBom(){
		return this.vlMuitoBom;
	}
	public void setVlBom(int vlBom){
		this.vlBom=vlBom;
	}
	public int getVlBom(){
		return this.vlBom;
	}
	public void setVlRegular(int vlRegular){
		this.vlRegular=vlRegular;
	}
	public int getVlRegular(){
		return this.vlRegular;
	}
	public void setVlFraco(int vlFraco){
		this.vlFraco=vlFraco;
	}
	public int getVlFraco(){
		return this.vlFraco;
	}
	public void setTxtConcorrencia(String txtConcorrencia){
		this.txtConcorrencia=txtConcorrencia;
	}
	public String getTxtConcorrencia(){
		return this.txtConcorrencia;
	}
	public void setTxtFidelidade(String txtFidelidade){
		this.txtFidelidade=txtFidelidade;
	}
	public String getTxtFidelidade(){
		return this.txtFidelidade;
	}
	public void setTxtCapacidadeInstalada(String txtCapacidadeInstalada){
		this.txtCapacidadeInstalada=txtCapacidadeInstalada;
	}
	public String getTxtCapacidadeInstalada(){
		return this.txtCapacidadeInstalada;
	}
	public void setDtInformacaoBem(GregorianCalendar dtInformacaoBem){
		this.dtInformacaoBem=dtInformacaoBem;
	}
	public GregorianCalendar getDtInformacaoBem(){
		return this.dtInformacaoBem;
	}
	public void setNrDiaPrestacao(int nrDiaPrestacao){
		this.nrDiaPrestacao=nrDiaPrestacao;
	}
	public int getNrDiaPrestacao(){
		return this.nrDiaPrestacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setIdEmpreendimento(String idEmpreendimento){
		this.idEmpreendimento=idEmpreendimento;
	}
	public String getIdEmpreendimento(){
		return this.idEmpreendimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpNegocio: " +  getTpNegocio();
		valueToString += ", tpLocal: " +  getTpLocal();
		valueToString += ", tpPontoComercial: " +  getTpPontoComercial();
		valueToString += ", dtContrato: " +  sol.util.Util.formatDateTime(getDtContrato(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmOutroPonto: " +  getNmOutroPonto();
		valueToString += ", nrDiasAtividade: " +  getNrDiasAtividade();
		valueToString += ", nmLocalAtividade: " +  getNmLocalAtividade();
		valueToString += ", tpAtividade: " +  getTpAtividade();
		valueToString += ", tpEstruturacao: " +  getTpEstruturacao();
		valueToString += ", lgEmpresaFamiliar: " +  getLgEmpresaFamiliar();
		valueToString += ", nrTempoExperiencia: " +  getNrTempoExperiencia();
		valueToString += ", nrTempoIndependencia: " +  getNrTempoIndependencia();
		valueToString += ", txtAprendizado: " +  getTxtAprendizado();
		valueToString += ", txtHistorico: " +  getTxtHistorico();
		valueToString += ", vlMuitoBom: " +  getVlMuitoBom();
		valueToString += ", vlBom: " +  getVlBom();
		valueToString += ", vlRegular: " +  getVlRegular();
		valueToString += ", vlFraco: " +  getVlFraco();
		valueToString += ", txtConcorrencia: " +  getTxtConcorrencia();
		valueToString += ", txtFidelidade: " +  getTxtFidelidade();
		valueToString += ", txtCapacidadeInstalada: " +  getTxtCapacidadeInstalada();
		valueToString += ", dtInformacaoBem: " +  sol.util.Util.formatDateTime(getDtInformacaoBem(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDiaPrestacao: " +  getNrDiaPrestacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", idEmpreendimento: " +  getIdEmpreendimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Empreendimento(cdEmpreendimento,
			cdPessoa,
			tpNegocio,
			tpLocal,
			tpPontoComercial,
			dtContrato==null ? null : (GregorianCalendar)dtContrato.clone(),
			nmOutroPonto,
			nrDiasAtividade,
			nmLocalAtividade,
			tpAtividade,
			tpEstruturacao,
			lgEmpresaFamiliar,
			nrTempoExperiencia,
			nrTempoIndependencia,
			txtAprendizado,
			txtHistorico,
			vlMuitoBom,
			vlBom,
			vlRegular,
			vlFraco,
			txtConcorrencia,
			txtFidelidade,
			txtCapacidadeInstalada,
			dtInformacaoBem==null ? null : (GregorianCalendar)dtInformacaoBem.clone(),
			nrDiaPrestacao,
			txtObservacao,
			idEmpreendimento);
	}

}