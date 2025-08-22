package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class Atendimento {

	private int cdAtendimento;
	private int cdCentral;
	private int cdPessoa;
	private int stAtendimento;
	private GregorianCalendar dtPrevisaoResposta;
	private int tpRelevancia;
	private String txtRelevancia;
	private int tpAvaliacao;
	private String txtAvaliacao;
	private String idAtendimento;
	private String dsSenha;
	private GregorianCalendar dtAdmissao;
	private int tpUsuario;
	private int cdCentralResponsavel;
	private int cdAtendenteResponsavel;
	private int tpClassificacao;
	private int cdFormaDivulgacao;
	private int cdTipoAtendimento;
	private int cdFormaContato;
	private int cdAtendimentoSuperior;
	private int cdProdutoServico;

	public Atendimento(int cdAtendimento,
			int cdCentral,
			int cdPessoa,
			int stAtendimento,
			GregorianCalendar dtPrevisaoResposta,
			int tpRelevancia,
			String txtRelevancia,
			int tpAvaliacao,
			String txtAvaliacao,
			String idAtendimento,
			String dsSenha,
			GregorianCalendar dtAdmissao,
			int tpUsuario,
			int cdCentralResponsavel,
			int cdAtendenteResponsavel,
			int tpClassificacao,
			int cdFormaDivulgacao,
			int cdTipoAtendimento,
			int cdFormaContato,
			int cdAtendimentoSuperior,
			int cdProdutoServico){
		setCdAtendimento(cdAtendimento);
		setCdCentral(cdCentral);
		setCdPessoa(cdPessoa);
		setStAtendimento(stAtendimento);
		setDtPrevisaoResposta(dtPrevisaoResposta);
		setTpRelevancia(tpRelevancia);
		setTxtRelevancia(txtRelevancia);
		setTpAvaliacao(tpAvaliacao);
		setTxtAvaliacao(txtAvaliacao);
		setIdAtendimento(idAtendimento);
		setDsSenha(dsSenha);
		setDtAdmissao(dtAdmissao);
		setTpUsuario(tpUsuario);
		setCdCentralResponsavel(cdCentralResponsavel);
		setCdAtendenteResponsavel(cdAtendenteResponsavel);
		setTpClassificacao(tpClassificacao);
		setCdFormaDivulgacao(cdFormaDivulgacao);
		setCdTipoAtendimento(cdTipoAtendimento);
		setCdFormaContato(cdFormaContato);
		setCdAtendimentoSuperior(cdAtendimentoSuperior);
		setCdProdutoServico(cdProdutoServico);
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setCdCentral(int cdCentral){
		this.cdCentral=cdCentral;
	}
	public int getCdCentral(){
		return this.cdCentral;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setStAtendimento(int stAtendimento){
		this.stAtendimento=stAtendimento;
	}
	public int getStAtendimento(){
		return this.stAtendimento;
	}
	public void setDtPrevisaoResposta(GregorianCalendar dtPrevisaoResposta){
		this.dtPrevisaoResposta=dtPrevisaoResposta;
	}
	public GregorianCalendar getDtPrevisaoResposta(){
		return this.dtPrevisaoResposta;
	}
	public void setTpRelevancia(int tpRelevancia){
		this.tpRelevancia=tpRelevancia;
	}
	public int getTpRelevancia(){
		return this.tpRelevancia;
	}
	public void setTxtRelevancia(String txtRelevancia){
		this.txtRelevancia=txtRelevancia;
	}
	public String getTxtRelevancia(){
		return this.txtRelevancia;
	}
	public void setTpAvaliacao(int tpAvaliacao){
		this.tpAvaliacao=tpAvaliacao;
	}
	public int getTpAvaliacao(){
		return this.tpAvaliacao;
	}
	public void setTxtAvaliacao(String txtAvaliacao){
		this.txtAvaliacao=txtAvaliacao;
	}
	public String getTxtAvaliacao(){
		return this.txtAvaliacao;
	}
	public void setIdAtendimento(String idAtendimento){
		this.idAtendimento=idAtendimento;
	}
	public String getIdAtendimento(){
		return this.idAtendimento;
	}
	public void setDsSenha(String dsSenha){
		this.dsSenha=dsSenha;
	}
	public String getDsSenha(){
		return this.dsSenha;
	}
	public void setDtAdmissao(GregorianCalendar dtAdmissao){
		this.dtAdmissao=dtAdmissao;
	}
	public GregorianCalendar getDtAdmissao(){
		return this.dtAdmissao;
	}
	public void setTpUsuario(int tpUsuario){
		this.tpUsuario=tpUsuario;
	}
	public int getTpUsuario(){
		return this.tpUsuario;
	}
	public void setCdCentralResponsavel(int cdCentralResponsavel){
		this.cdCentralResponsavel=cdCentralResponsavel;
	}
	public int getCdCentralResponsavel(){
		return this.cdCentralResponsavel;
	}
	public void setCdAtendenteResponsavel(int cdAtendenteResponsavel){
		this.cdAtendenteResponsavel=cdAtendenteResponsavel;
	}
	public int getCdAtendenteResponsavel(){
		return this.cdAtendenteResponsavel;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public void setCdFormaDivulgacao(int cdFormaDivulgacao){
		this.cdFormaDivulgacao=cdFormaDivulgacao;
	}
	public int getCdFormaDivulgacao(){
		return this.cdFormaDivulgacao;
	}
	public void setCdTipoAtendimento(int cdTipoAtendimento){
		this.cdTipoAtendimento=cdTipoAtendimento;
	}
	public int getCdTipoAtendimento(){
		return this.cdTipoAtendimento;
	}
	public void setCdFormaContato(int cdFormaContato){
		this.cdFormaContato=cdFormaContato;
	}
	public int getCdFormaContato(){
		return this.cdFormaContato;
	}
	public void setCdAtendimentoSuperior(int cdAtendimentoSuperior){
		this.cdAtendimentoSuperior=cdAtendimentoSuperior;
	}
	public int getCdAtendimentoSuperior(){
		return this.cdAtendimentoSuperior;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtendimento: " +  getCdAtendimento();
		valueToString += ", cdCentral: " +  getCdCentral();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", stAtendimento: " +  getStAtendimento();
		valueToString += ", dtPrevisaoResposta: " +  sol.util.Util.formatDateTime(getDtPrevisaoResposta(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpRelevancia: " +  getTpRelevancia();
		valueToString += ", txtRelevancia: " +  getTxtRelevancia();
		valueToString += ", tpAvaliacao: " +  getTpAvaliacao();
		valueToString += ", txtAvaliacao: " +  getTxtAvaliacao();
		valueToString += ", idAtendimento: " +  getIdAtendimento();
		valueToString += ", dsSenha: " +  getDsSenha();
		valueToString += ", dtAdmissao: " +  sol.util.Util.formatDateTime(getDtAdmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpUsuario: " +  getTpUsuario();
		valueToString += ", cdCentralResponsavel: " +  getCdCentralResponsavel();
		valueToString += ", cdAtendenteResponsavel: " +  getCdAtendenteResponsavel();
		valueToString += ", tpClassificacao: " +  getTpClassificacao();
		valueToString += ", cdFormaDivulgacao: " +  getCdFormaDivulgacao();
		valueToString += ", cdTipoAtendimento: " +  getCdTipoAtendimento();
		valueToString += ", cdFormaContato: " +  getCdFormaContato();
		valueToString += ", cdAtendimentoSuperior: " +  getCdAtendimentoSuperior();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Atendimento(getCdAtendimento(),
			getCdCentral(),
			getCdPessoa(),
			getStAtendimento(),
			getDtPrevisaoResposta()==null ? null : (GregorianCalendar)getDtPrevisaoResposta().clone(),
			getTpRelevancia(),
			getTxtRelevancia(),
			getTpAvaliacao(),
			getTxtAvaliacao(),
			getIdAtendimento(),
			getDsSenha(),
			getDtAdmissao()==null ? null : (GregorianCalendar)getDtAdmissao().clone(),
			getTpUsuario(),
			getCdCentralResponsavel(),
			getCdAtendenteResponsavel(),
			getTpClassificacao(),
			getCdFormaDivulgacao(),
			getCdTipoAtendimento(),
			getCdFormaContato(),
			getCdAtendimentoSuperior(),
			getCdProdutoServico());
	}

}
