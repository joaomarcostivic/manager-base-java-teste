package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaOferta extends com.tivic.manager.grl.Ocorrencia {

	private int cdOferta;
	private GregorianCalendar dtUltimaModificacao;
	private int cdUsuarioModificador;
	private int stOfertaAnterior;
	private int stOfertaPosterior;

	public OcorrenciaOferta(){ }

	public OcorrenciaOferta(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdOferta,
			GregorianCalendar dtUltimaModificacao,
			int cdUsuarioModificador,
			int stOfertaAnterior,
			int stOfertaPosterior){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdOferta(cdOferta);
		setDtUltimaModificacao(dtUltimaModificacao);
		setCdUsuarioModificador(cdUsuarioModificador);
		setStOfertaAnterior(stOfertaAnterior);
		setStOfertaPosterior(stOfertaPosterior);
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
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
	public void setStOfertaAnterior(int stOfertaAnterior){
		this.stOfertaAnterior=stOfertaAnterior;
	}
	public int getStOfertaAnterior(){
		return this.stOfertaAnterior;
	}
	public void setStOfertaPosterior(int stOfertaPosterior){
		this.stOfertaPosterior=stOfertaPosterior;
	}
	public int getStOfertaPosterior(){
		return this.stOfertaPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", dtUltimaModificacao: " +  sol.util.Util.formatDateTime(getDtUltimaModificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioModificador: " +  getCdUsuarioModificador();
		valueToString += ", stOfertaAnterior: " +  getStOfertaAnterior();
		valueToString += ", stOfertaPosterior: " +  getStOfertaPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaOferta(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdOferta(),
			getDtUltimaModificacao()==null ? null : (GregorianCalendar)getDtUltimaModificacao().clone(),
			getCdUsuarioModificador(),
			getStOfertaAnterior(),
			getStOfertaPosterior());
	}

}