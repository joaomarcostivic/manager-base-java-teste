package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class FidelidadeMovimento {

	private int cdMovimento;
	private int cdPessoa;
	private int cdFidelidade;
	private GregorianCalendar dtMovimento;
	private String txtHistorico;
	private int cdTipoMovimento;
	private int cdOcorrencia;
	private GregorianCalendar dtValidade;
	private float vlMovimento;

	public FidelidadeMovimento(int cdMovimento,
			int cdPessoa,
			int cdFidelidade,
			GregorianCalendar dtMovimento,
			String txtHistorico,
			int cdTipoMovimento,
			int cdOcorrencia,
			GregorianCalendar dtValidade,
			float vlMovimento){
		setCdMovimento(cdMovimento);
		setCdPessoa(cdPessoa);
		setCdFidelidade(cdFidelidade);
		setDtMovimento(dtMovimento);
		setTxtHistorico(txtHistorico);
		setCdTipoMovimento(cdTipoMovimento);
		setCdOcorrencia(cdOcorrencia);
		setDtValidade(dtValidade);
		setVlMovimento(vlMovimento);
	}
	public void setCdMovimento(int cdMovimento){
		this.cdMovimento=cdMovimento;
	}
	public int getCdMovimento(){
		return this.cdMovimento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdFidelidade(int cdFidelidade){
		this.cdFidelidade=cdFidelidade;
	}
	public int getCdFidelidade(){
		return this.cdFidelidade;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento){
		this.dtMovimento=dtMovimento;
	}
	public GregorianCalendar getDtMovimento(){
		return this.dtMovimento;
	}
	public void setTxtHistorico(String txtHistorico){
		this.txtHistorico=txtHistorico;
	}
	public String getTxtHistorico(){
		return this.txtHistorico;
	}
	public void setCdTipoMovimento(int cdTipoMovimento){
		this.cdTipoMovimento=cdTipoMovimento;
	}
	public int getCdTipoMovimento(){
		return this.cdTipoMovimento;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setVlMovimento(float vlMovimento){
		this.vlMovimento=vlMovimento;
	}
	public float getVlMovimento(){
		return this.vlMovimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMovimento: " +  getCdMovimento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdFidelidade: " +  getCdFidelidade();
		valueToString += ", dtMovimento: " +  sol.util.Util.formatDateTime(getDtMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtHistorico: " +  getTxtHistorico();
		valueToString += ", cdTipoMovimento: " +  getCdTipoMovimento();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlMovimento: " +  getVlMovimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FidelidadeMovimento(getCdMovimento(),
			getCdPessoa(),
			getCdFidelidade(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getTxtHistorico(),
			getCdTipoMovimento(),
			getCdOcorrencia(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getVlMovimento());
	}

}
