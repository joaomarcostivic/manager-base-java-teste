package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class RecolhimentoDocumentacao {

	private int cdRecolhimentoDocumentacao;
	private int cdRrd;
	private int cdTipoDocumentacao;
	private GregorianCalendar dtDevolucao;
	private int cdUsuario;
	private int cdAgente;
	private String nrDocumento;
	private int cdTrrav;

	public RecolhimentoDocumentacao() { }

	public RecolhimentoDocumentacao(int cdRecolhimentoDocumentacao,
			int cdRrd,
			int cdTipoDocumentacao,
			GregorianCalendar dtDevolucao,
			int cdUsuario,
			int cdAgente,
			String nrDocumento,
			int cdTrrav) {
		setCdRecolhimentoDocumentacao(cdRecolhimentoDocumentacao);
		setCdRrd(cdRrd);
		setCdTipoDocumentacao(cdTipoDocumentacao);
		setDtDevolucao(dtDevolucao);
		setCdUsuario(cdUsuario);
		setCdAgente(cdAgente);
		setNrDocumento(nrDocumento);
		setCdTrrav(cdTrrav);
	}
	public void setCdRecolhimentoDocumentacao(int cdRecolhimentoDocumentacao){
		this.cdRecolhimentoDocumentacao=cdRecolhimentoDocumentacao;
	}
	public int getCdRecolhimentoDocumentacao(){
		return this.cdRecolhimentoDocumentacao;
	}
	public void setCdRrd(int cdRrd){
		this.cdRrd=cdRrd;
	}
	public int getCdRrd(){
		return this.cdRrd;
	}
	public void setCdTipoDocumentacao(int cdTipoDocumentacao){
		this.cdTipoDocumentacao=cdTipoDocumentacao;
	}
	public int getCdTipoDocumentacao(){
		return this.cdTipoDocumentacao;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao){
		this.dtDevolucao=dtDevolucao;
	}
	public GregorianCalendar getDtDevolucao(){
		return this.dtDevolucao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setCdTrrav(int cdTrrav){
		this.cdTrrav=cdTrrav;
	}
	public int getCdTrrav(){
		return this.cdTrrav;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecolhimentoDocumentacao: " +  getCdRecolhimentoDocumentacao();
		valueToString += ", cdRrd: " +  getCdRrd();
		valueToString += ", cdTipoDocumentacao: " +  getCdTipoDocumentacao();
		valueToString += ", dtDevolucao: " +  sol.util.Util.formatDateTime(getDtDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", cdTrrav: " +  getCdTrrav();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RecolhimentoDocumentacao(getCdRecolhimentoDocumentacao(),
			getCdRrd(),
			getCdTipoDocumentacao(),
			getDtDevolucao()==null ? null : (GregorianCalendar)getDtDevolucao().clone(),
			getCdUsuario(),
			getCdAgente(),
			getNrDocumento(),
			getCdTrrav());
	}
}