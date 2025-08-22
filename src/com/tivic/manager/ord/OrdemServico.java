package com.tivic.manager.ord;

import java.util.GregorianCalendar;

public class OrdemServico {

	private int cdOrdemServico;
	private int cdPessoa;
	private int cdTipoAtendimento;
	private GregorianCalendar dtEntrada;
	private GregorianCalendar dtSaida;
	private String txtSolicitacao;
	private String txtObservacao;
	private int cdSituacaoServico;
	private int cdModalidade;
	private String nrOrdemServico;
	private int cdEmpresa;
	private int cdTecnicoResponsavel;
	private int cdDocumento;
	private int cdSetor;
	private int cdUsuario;
	private Double vlOrdemServico;
	private int cdPlanoTrabalho;
	private String nrNotaFiscal;
	private int cdFornecedor;

	public OrdemServico() { }

	public OrdemServico(int cdOrdemServico,
			int cdPessoa,
			int cdTipoAtendimento,
			GregorianCalendar dtEntrada,
			GregorianCalendar dtSaida,
			String txtSolicitacao,
			String txtObservacao,
			int cdSituacaoServico,
			int cdModalidade,
			String nrOrdemServico,
			int cdEmpresa,
			int cdTecnicoResponsavel,
			int cdDocumento,
			int cdSetor,
			int cdUsuario,
			Double vlOrdemServico,
			int cdPlanoTrabalho,
			String nrNotaFiscal,
			int cdFornecedor) {
		setCdOrdemServico(cdOrdemServico);
		setCdPessoa(cdPessoa);
		setCdTipoAtendimento(cdTipoAtendimento);
		setDtEntrada(dtEntrada);
		setDtSaida(dtSaida);
		setTxtSolicitacao(txtSolicitacao);
		setTxtObservacao(txtObservacao);
		setCdSituacaoServico(cdSituacaoServico);
		setCdModalidade(cdModalidade);
		setNrOrdemServico(nrOrdemServico);
		setCdEmpresa(cdEmpresa);
		setCdTecnicoResponsavel(cdTecnicoResponsavel);
		setCdDocumento(cdDocumento);
		setCdSetor(cdSetor);
		setCdUsuario(cdUsuario);
		setVlOrdemServico(vlOrdemServico);
		setCdPlanoTrabalho(cdPlanoTrabalho);
		setNrNotaFiscal(nrNotaFiscal);
		setCdFornecedor(cdFornecedor);
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdTipoAtendimento(int cdTipoAtendimento){
		this.cdTipoAtendimento=cdTipoAtendimento;
	}
	public int getCdTipoAtendimento(){
		return this.cdTipoAtendimento;
	}
	public void setDtEntrada(GregorianCalendar dtEntrada){
		this.dtEntrada=dtEntrada;
	}
	public GregorianCalendar getDtEntrada(){
		return this.dtEntrada;
	}
	public void setDtSaida(GregorianCalendar dtSaida){
		this.dtSaida=dtSaida;
	}
	public GregorianCalendar getDtSaida(){
		return this.dtSaida;
	}
	public void setTxtSolicitacao(String txtSolicitacao){
		this.txtSolicitacao=txtSolicitacao;
	}
	public String getTxtSolicitacao(){
		return this.txtSolicitacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdSituacaoServico(int cdSituacaoServico){
		this.cdSituacaoServico=cdSituacaoServico;
	}
	public int getCdSituacaoServico(){
		return this.cdSituacaoServico;
	}
	public void setCdModalidade(int cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public int getCdModalidade(){
		return this.cdModalidade;
	}
	public void setNrOrdemServico(String nrOrdemServico){
		this.nrOrdemServico=nrOrdemServico;
	}
	public String getNrOrdemServico(){
		return this.nrOrdemServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdTecnicoResponsavel(int cdTecnicoResponsavel){
		this.cdTecnicoResponsavel=cdTecnicoResponsavel;
	}
	public int getCdTecnicoResponsavel(){
		return this.cdTecnicoResponsavel;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setVlOrdemServico(Double vlOrdemServico){
		this.vlOrdemServico=vlOrdemServico;
	}
	public Double getVlOrdemServico(){
		return this.vlOrdemServico;
	}
	public void setCdPlanoTrabalho(int cdPlanoTrabalho){
		this.cdPlanoTrabalho=cdPlanoTrabalho;
	}
	public int getCdPlanoTrabalho(){
		return this.cdPlanoTrabalho;
	}
	public void setNrNotaFiscal(String nrNotaFiscal){
		this.nrNotaFiscal=nrNotaFiscal;
	}
	public String getNrNotaFiscal(){
		return this.nrNotaFiscal;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdTipoAtendimento: " +  getCdTipoAtendimento();
		valueToString += ", dtEntrada: " +  sol.util.Util.formatDateTime(getDtEntrada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtSaida: " +  sol.util.Util.formatDateTime(getDtSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtSolicitacao: " +  getTxtSolicitacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdSituacaoServico: " +  getCdSituacaoServico();
		valueToString += ", cdModalidade: " +  getCdModalidade();
		valueToString += ", nrOrdemServico: " +  getNrOrdemServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdTecnicoResponsavel: " +  getCdTecnicoResponsavel();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", vlOrdemServico: " +  getVlOrdemServico();
		valueToString += ", cdPlanoTrabalho: " +  getCdPlanoTrabalho();
		valueToString += ", nrNotaFiscal: " +  getNrNotaFiscal();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemServico(getCdOrdemServico(),
			getCdPessoa(),
			getCdTipoAtendimento(),
			getDtEntrada()==null ? null : (GregorianCalendar)getDtEntrada().clone(),
			getDtSaida()==null ? null : (GregorianCalendar)getDtSaida().clone(),
			getTxtSolicitacao(),
			getTxtObservacao(),
			getCdSituacaoServico(),
			getCdModalidade(),
			getNrOrdemServico(),
			getCdEmpresa(),
			getCdTecnicoResponsavel(),
			getCdDocumento(),
			getCdSetor(),
			getCdUsuario(),
			getVlOrdemServico(),
			getCdPlanoTrabalho(),
			getNrNotaFiscal(),
			getCdFornecedor());
	}

}