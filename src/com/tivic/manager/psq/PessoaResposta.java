package com.tivic.manager.psq;

import java.util.GregorianCalendar;

public class PessoaResposta {

	private int cdPessoa;
	private int cdQuestionario;
	private int cdQuestaoValor;
	private int cdQuestao;
	private int cdResposta;
	private int cdDigitador;
	private int cdEmpresaDigitador;
	private int cdVinculoDigitador;
	private GregorianCalendar dtResposta;
	private byte[] blbResposta;
	private String txtObservacao;
	private int cdAplicacao;
	private int cdResultado;

	public PessoaResposta(int cdPessoa,
			int cdQuestionario,
			int cdQuestaoValor,
			int cdQuestao,
			int cdResposta,
			int cdDigitador,
			int cdEmpresaDigitador,
			int cdVinculoDigitador,
			GregorianCalendar dtResposta,
			byte[] blbResposta,
			String txtObservacao,
			int cdAplicacao,
			int cdResultado){
		setCdPessoa(cdPessoa);
		setCdQuestionario(cdQuestionario);
		setCdQuestaoValor(cdQuestaoValor);
		setCdQuestao(cdQuestao);
		setCdResposta(cdResposta);
		setCdDigitador(cdDigitador);
		setCdEmpresaDigitador(cdEmpresaDigitador);
		setCdVinculoDigitador(cdVinculoDigitador);
		setDtResposta(dtResposta);
		setBlbResposta(blbResposta);
		setTxtObservacao(txtObservacao);
		setCdAplicacao(cdAplicacao);
		setCdResultado(cdResultado);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdQuestionario(int cdQuestionario){
		this.cdQuestionario=cdQuestionario;
	}
	public int getCdQuestionario(){
		return this.cdQuestionario;
	}
	public void setCdQuestaoValor(int cdQuestaoValor){
		this.cdQuestaoValor=cdQuestaoValor;
	}
	public int getCdQuestaoValor(){
		return this.cdQuestaoValor;
	}
	public void setCdQuestao(int cdQuestao){
		this.cdQuestao=cdQuestao;
	}
	public int getCdQuestao(){
		return this.cdQuestao;
	}
	public void setCdResposta(int cdResposta){
		this.cdResposta=cdResposta;
	}
	public int getCdResposta(){
		return this.cdResposta;
	}
	public void setCdDigitador(int cdDigitador){
		this.cdDigitador=cdDigitador;
	}
	public int getCdDigitador(){
		return this.cdDigitador;
	}
	public void setCdEmpresaDigitador(int cdEmpresaDigitador){
		this.cdEmpresaDigitador=cdEmpresaDigitador;
	}
	public int getCdEmpresaDigitador(){
		return this.cdEmpresaDigitador;
	}
	public void setCdVinculoDigitador(int cdVinculoDigitador){
		this.cdVinculoDigitador=cdVinculoDigitador;
	}
	public int getCdVinculoDigitador(){
		return this.cdVinculoDigitador;
	}
	public void setDtResposta(GregorianCalendar dtResposta){
		this.dtResposta=dtResposta;
	}
	public GregorianCalendar getDtResposta(){
		return this.dtResposta;
	}
	public void setBlbResposta(byte[] blbResposta){
		this.blbResposta=blbResposta;
	}
	public byte[] getBlbResposta(){
		return this.blbResposta;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdAplicacao(int cdAplicacao){
		this.cdAplicacao=cdAplicacao;
	}
	public int getCdAplicacao(){
		return this.cdAplicacao;
	}
	public void setCdResultado(int cdResultado){
		this.cdResultado=cdResultado;
	}
	public int getCdResultado(){
		return this.cdResultado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdQuestionario: " +  getCdQuestionario();
		valueToString += ", cdQuestaoValor: " +  getCdQuestaoValor();
		valueToString += ", cdQuestao: " +  getCdQuestao();
		valueToString += ", cdResposta: " +  getCdResposta();
		valueToString += ", cdDigitador: " +  getCdDigitador();
		valueToString += ", cdEmpresaDigitador: " +  getCdEmpresaDigitador();
		valueToString += ", cdVinculoDigitador: " +  getCdVinculoDigitador();
		valueToString += ", dtResposta: " +  sol.util.Util.formatDateTime(getDtResposta(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", blbResposta: " +  getBlbResposta();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdAplicacao: " +  getCdAplicacao();
		valueToString += ", cdResultado: " +  getCdResultado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaResposta(getCdPessoa(),
			getCdQuestionario(),
			getCdQuestaoValor(),
			getCdQuestao(),
			getCdResposta(),
			getCdDigitador(),
			getCdEmpresaDigitador(),
			getCdVinculoDigitador(),
			getDtResposta()==null ? null : (GregorianCalendar)getDtResposta().clone(),
			getBlbResposta(),
			getTxtObservacao(),
			getCdAplicacao(),
			getCdResultado());
	}

}
