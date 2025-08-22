package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaQuadroVagas extends com.tivic.manager.grl.Ocorrencia {

	private String txtResposta;
	private GregorianCalendar dtResposta;
	private int cdPessoaResposta;
	private String nmAssunto;

	public OcorrenciaQuadroVagas(){ }

	public OcorrenciaQuadroVagas(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			String txtResposta,
			GregorianCalendar dtResposta,
			int cdPessoaResposta,
			String nmAssunto){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setTxtResposta(txtResposta);
		setDtResposta(dtResposta);
		setCdPessoaResposta(cdPessoaResposta);
		setNmAssunto(nmAssunto);
	}
	public void setTxtResposta(String txtResposta){
		this.txtResposta=txtResposta;
	}
	public String getTxtResposta(){
		return this.txtResposta;
	}
	public void setDtResposta(GregorianCalendar dtResposta){
		this.dtResposta=dtResposta;
	}
	public GregorianCalendar getDtResposta(){
		return this.dtResposta;
	}
	public void setCdPessoaResposta(int cdPessoaResposta){
		this.cdPessoaResposta=cdPessoaResposta;
	}
	public int getCdPessoaResposta(){
		return this.cdPessoaResposta;
	}
	public void setNmAssunto(String nmAssunto){
		this.nmAssunto=nmAssunto;
	}
	public String getNmAssunto(){
		return this.nmAssunto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", txtResposta: " +  getTxtResposta();
		valueToString += ", dtResposta: " +  sol.util.Util.formatDateTime(getDtResposta(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdPessoaResposta: " +  getCdPessoaResposta();
		valueToString += ", nmAssunto: " +  getNmAssunto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaQuadroVagas(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getTxtResposta(),
			getDtResposta()==null ? null : (GregorianCalendar)getDtResposta().clone(),
			getCdPessoaResposta(),
			getNmAssunto());
	}

}