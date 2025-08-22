package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class CorreiosEtiqueta {

	private int cdEtiqueta;
	private int cdLote;
	private int nrEtiqueta;
	private GregorianCalendar dtEnvio;
	private String sgServico;
	private int cdAit;
	private int tpStatus;
	private int nrMovimento;
	private int nrDigitoVerificador;
	private int cdLoteImpressao;
	private int stAvisoRecebimento;
	private GregorianCalendar dtAvisoRecebimento;

	public CorreiosEtiqueta() { }

	public CorreiosEtiqueta(int cdEtiqueta,
			int cdLote,
			int nrEtiqueta,
			GregorianCalendar dtEnvio,
			String sgServico,
			int cdAit,
			int tpStatus,
			int nrMovimento,
			int nrDigitoVerificador,
			int cdLoteImpressao,
			int stAvisoRecebimento,
			GregorianCalendar dtAvisoRecebimento) {
		setCdEtiqueta(cdEtiqueta);
		setCdLote(cdLote);
		setNrEtiqueta(nrEtiqueta);
		setDtEnvio(dtEnvio);
		setSgServico(sgServico);
		setCdAit(cdAit);
		setTpStatus(tpStatus);
		setNrMovimento(nrMovimento);
		setNrDigitoVerificador(nrDigitoVerificador);
		setCdLoteImpressao(cdLoteImpressao);
		setStAvisoRecebimento(stAvisoRecebimento);
		setDtAvisoRecebimento(dtAvisoRecebimento);
	}
	public void setCdEtiqueta(int cdEtiqueta){
		this.cdEtiqueta=cdEtiqueta;
	}
	public int getCdEtiqueta(){
		return this.cdEtiqueta;
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setNrEtiqueta(int nrEtiqueta){
		this.nrEtiqueta=nrEtiqueta;
	}
	public int getNrEtiqueta(){
		return this.nrEtiqueta;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setSgServico(String sgServico){
		this.sgServico=sgServico;
	}
	public String getSgServico(){
		return this.sgServico;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setTpStatus(int tpStatus){
		this.tpStatus=tpStatus;
	}
	public int getTpStatus(){
		return this.tpStatus;
	}
	public void setNrMovimento(int nrMovimento){
		this.nrMovimento=nrMovimento;
	}
	public int getNrMovimento(){
		return this.nrMovimento;
	}
	public void setNrDigitoVerificador(int nrDigitoVerificador){
		this.nrDigitoVerificador=nrDigitoVerificador;
	}
	public int getNrDigitoVerificador(){
		return this.nrDigitoVerificador;
	}
	
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getStAvisoRecebimento() {
		return stAvisoRecebimento;
	}

	public void setStAvisoRecebimento(int stAvisoRecebimento) {
		this.stAvisoRecebimento = stAvisoRecebimento;
	}

	public GregorianCalendar getDtAvisoRecebimento() {
		return dtAvisoRecebimento;
	}

	public void setDtAvisoRecebimento(GregorianCalendar dtAvisoRecebimento) {
		this.dtAvisoRecebimento = dtAvisoRecebimento;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdEtiqueta: " +  getCdEtiqueta();
		valueToString += ", cdLote: " +  getCdLote();
		valueToString += ", nrEtiqueta: " +  getNrEtiqueta();
		valueToString += ", dtEnvio: " +  sol.util.Util.formatDateTime(getDtEnvio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", sgServico: " +  getSgServico();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", tpStatus: " +  getTpStatus();
		valueToString += ", nrMovimento: " +  getNrMovimento();
		valueToString += ", nrDigitoVerificador: " +  getNrDigitoVerificador();
		valueToString += ", cdLoteImpressao: " +  getCdLoteImpressao();
		valueToString += ", stAvisoRecebimento: " + getStAvisoRecebimento();
		valueToString += ", dtAvisoRecebimento: " + getDtAvisoRecebimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CorreiosEtiqueta(getCdEtiqueta(),
			getCdLote(),
			getNrEtiqueta(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getSgServico(),
			getCdAit(),
			getTpStatus(),
			getNrMovimento(),
			getNrDigitoVerificador(),
			getCdLoteImpressao(),
			getStAvisoRecebimento(),
			getDtAvisoRecebimento());
	}

}