package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaInstituicao extends com.tivic.manager.grl.Ocorrencia {

	private int cdInstituicao;
	private GregorianCalendar dtUltimaModificacao;
	private int cdUsuarioModificador;
	private int cdPeriodoLetivo;

	public OcorrenciaInstituicao(){ }

	public OcorrenciaInstituicao(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdInstituicao,
			GregorianCalendar dtUltimaModificacao,
			int cdUsuarioModificador,
			int cdPeriodoLetivo){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdInstituicao(cdInstituicao);
		setDtUltimaModificacao(dtUltimaModificacao);
		setCdUsuarioModificador(cdUsuarioModificador);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
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
	public void setCdPeriodoLetivo(int cdPeriodoLetivo) {
		this.cdPeriodoLetivo = cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo() {
		return cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", dtUltimaModificacao: " +  sol.util.Util.formatDateTime(getDtUltimaModificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioModificador: " +  getCdUsuarioModificador();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaInstituicao(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdInstituicao(),
			getDtUltimaModificacao()==null ? null : (GregorianCalendar)getDtUltimaModificacao().clone(),
			getCdUsuarioModificador(),
			getCdPeriodoLetivo());
	}

}