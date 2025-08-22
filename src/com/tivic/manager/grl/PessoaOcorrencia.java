package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaOcorrencia {

	private int cdOcorrencia;
	private int cdPessoa;
	private GregorianCalendar dtOcorrencia;
	private GregorianCalendar dtLancamento;
	private String txtOcorrencia;
	private int tpVisibilidade;
	private int cdTipoOcorrencia;
	private int cdUsuario;

	public PessoaOcorrencia() { }

	public PessoaOcorrencia(int cdOcorrencia,
			int cdPessoa,
			GregorianCalendar dtOcorrencia,
			GregorianCalendar dtLancamento,
			String txtOcorrencia,
			int tpVisibilidade,
			int cdTipoOcorrencia,
			int cdUsuario) {
		setCdOcorrencia(cdOcorrencia);
		setCdPessoa(cdPessoa);
		setDtOcorrencia(dtOcorrencia);
		setDtLancamento(dtLancamento);
		setTxtOcorrencia(txtOcorrencia);
		setTpVisibilidade(tpVisibilidade);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setCdUsuario(cdUsuario);
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setTpVisibilidade(int tpVisibilidade){
		this.tpVisibilidade=tpVisibilidade;
	}
	public int getTpVisibilidade(){
		return this.tpVisibilidade;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", tpVisibilidade: " +  getTpVisibilidade();
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaOcorrencia(getCdOcorrencia(),
			getCdPessoa(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getTxtOcorrencia(),
			getTpVisibilidade(),
			getCdTipoOcorrencia(),
			getCdUsuario());
	}

}