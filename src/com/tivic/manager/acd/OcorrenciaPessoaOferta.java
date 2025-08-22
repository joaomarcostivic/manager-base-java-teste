package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaPessoaOferta extends com.tivic.manager.grl.Ocorrencia {

	private int cdOferta;
	private int cdFuncao;
	private GregorianCalendar dtUltimaModificacao;
	private int cdUsuarioModificador;
	private int stPessoaOfertaAnterior;
	private int stPessoaOfertaPosterior;

	public OcorrenciaPessoaOferta(){ }

	public OcorrenciaPessoaOferta(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdOferta,
			int cdFuncao,
			GregorianCalendar dtUltimaModificacao,
			int cdUsuarioModificador,
			int stPessoaOfertaAnterior,
			int stPessoaOfertaPosterior){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdOferta(cdOferta);
		setCdFuncao(cdFuncao);
		setDtUltimaModificacao(dtUltimaModificacao);
		setCdUsuarioModificador(cdUsuarioModificador);
		setStPessoaOfertaAnterior(stPessoaOfertaAnterior);
		setStPessoaOfertaPosterior(stPessoaOfertaPosterior);
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setCdFuncao(int cdFuncao){
		this.cdFuncao=cdFuncao;
	}
	public int getCdFuncao(){
		return this.cdFuncao;
	}
	public void setDtUltimaModificacao(GregorianCalendar dtUltimaModificacao){
		this.dtUltimaModificacao=dtUltimaModificacao;
	}
	public GregorianCalendar getDtUltimaModificacao(){
		return this.dtUltimaModificacao;
	}
	public void setCdUsuarioModificador(int cdUsuarioModificador){
		this.cdUsuarioModificador=cdUsuarioModificador;
	}
	public int getCdUsuarioModificador(){
		return this.cdUsuarioModificador;
	}
	public void setStPessoaOfertaAnterior(int stPessoaOfertaAnterior){
		this.stPessoaOfertaAnterior=stPessoaOfertaAnterior;
	}
	public int getStPessoaOfertaAnterior(){
		return this.stPessoaOfertaAnterior;
	}
	public void setStPessoaOfertaPosterior(int stPessoaOfertaPosterior){
		this.stPessoaOfertaPosterior=stPessoaOfertaPosterior;
	}
	public int getStPessoaOfertaPosterior(){
		return this.stPessoaOfertaPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", cdFuncao: " +  getCdFuncao();
		valueToString += ", dtUltimaModificacao: " +  sol.util.Util.formatDateTime(getDtUltimaModificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioModificador: " +  getCdUsuarioModificador();
		valueToString += ", stPessoaOfertaAnterior: " +  getStPessoaOfertaAnterior();
		valueToString += ", stPessoaOfertaPosterior: " +  getStPessoaOfertaPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaPessoaOferta(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdOferta(),
			getCdFuncao(),
			getDtUltimaModificacao()==null ? null : (GregorianCalendar)getDtUltimaModificacao().clone(),
			getCdUsuarioModificador(),
			getStPessoaOfertaAnterior(),
			getStPessoaOfertaPosterior());
	}

}