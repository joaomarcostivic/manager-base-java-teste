package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class EventoArquivo {

	private int cdEvento;
	private int cdArquivo;
	private int tpArquivo;
	private GregorianCalendar dtArquivo;
	private String idArquivo;
	private int tpEventoFoto;
	private int tpFoto;
	private int lgImpressao;

	public EventoArquivo() { }

	public EventoArquivo(int cdEvento,
			int cdArquivo,
			int tpArquivo,
			GregorianCalendar dtArquivo,
			String idArquivo,
			int tpEventoFoto,
			int tpFoto,
			int lgImpressao) {
		setCdEvento(cdEvento);
		setCdArquivo(cdArquivo);
		setTpArquivo(tpArquivo);
		setDtArquivo(dtArquivo);
		setIdArquivo(idArquivo);
		setTpEventoFoto(tpEventoFoto);
		setTpFoto(tpFoto);
		setLgImpressao(lgImpressao);
	}
	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	public int getCdEvento(){
		return this.cdEvento;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setTpArquivo(int tpArquivo){
		this.tpArquivo=tpArquivo;
	}
	public int getTpArquivo(){
		return this.tpArquivo;
	}
	public void setDtArquivo(GregorianCalendar dtArquivo){
		this.dtArquivo=dtArquivo;
	}
	public GregorianCalendar getDtArquivo(){
		return this.dtArquivo;
	}
	public void setIdArquivo(String idArquivo){
		this.idArquivo=idArquivo;
	}
	public String getIdArquivo(){
		return this.idArquivo;
	}
	public void setTpEventoFoto(int tpEventoFoto){
		this.tpEventoFoto=tpEventoFoto;
	}
	public int getTpEventoFoto(){
		return this.tpEventoFoto;
	}
	public void setTpFoto(int tpFoto){
		this.tpFoto=tpFoto;
	}
	public int getTpFoto(){
		return this.tpFoto;
	}

	public int getLgImpressao() {
		return lgImpressao;
	}

	public void setLgImpressao(int lgImpressao) {
		this.lgImpressao = lgImpressao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdEvento: " +  getCdEvento();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", tpArquivo: " +  getTpArquivo();
		valueToString += ", dtArquivo: " +  sol.util.Util.formatDateTime(getDtArquivo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idArquivo: " +  getIdArquivo();
		valueToString += ", tpEventoFoto: " +  getTpEventoFoto();
		valueToString += ", tpFoto: " +  getTpFoto();
		valueToString += ", lgImpressao: " + getLgImpressao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EventoArquivo(getCdEvento(),
			getCdArquivo(),
			getTpArquivo(),
			getDtArquivo()==null ? null : (GregorianCalendar)getDtArquivo().clone(),
			getIdArquivo(),
			getTpEventoFoto(),
			getTpFoto(),
			getLgImpressao());
	}

}