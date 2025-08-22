package com.tivic.manager.flp;

import java.util.GregorianCalendar;

public class FolhaPagamento {

	private int cdFolhaPagamento;
	private int cdEmpresa;
	private int nrMes;
	private int nrAno;
	private int tpFolhaPagamento;
	private String idFolhaPagamento;
	private int stFolhaPagamento;
	private float vlGprs;
	private int cdIndicadorSalarioMinimo;
	private float vlDeducaoIrDependente;
	private float vlDeducaoIrIdoso;
	private float vlMinimoIr;
	private float prValeTransporte;
	private float prFgts;
	private int nrDiasUteis;
	private float vlHoraAulaP1;
	private int vlHoraAulaP2;
	private int vlHoraAulaP3;
	private int vlHoraAulaP4;
	private int vlHoraAulaP5;
	private GregorianCalendar dtFechamento;
	private String txtMensagem;

	public FolhaPagamento(int cdFolhaPagamento,
			int cdEmpresa,
			int nrMes,
			int nrAno,
			int tpFolhaPagamento,
			String idFolhaPagamento,
			int stFolhaPagamento,
			float vlGprs,
			int cdIndicadorSalarioMinimo,
			float vlDeducaoIrDependente,
			float vlDeducaoIrIdoso,
			float vlMinimoIr,
			float prValeTransporte,
			float prFgts,
			int nrDiasUteis,
			float vlHoraAulaP1,
			int vlHoraAulaP2,
			int vlHoraAulaP3,
			int vlHoraAulaP4,
			int vlHoraAulaP5,
			GregorianCalendar dtFechamento,
			String txtMensagem){
		setCdFolhaPagamento(cdFolhaPagamento);
		setCdEmpresa(cdEmpresa);
		setNrMes(nrMes);
		setNrAno(nrAno);
		setTpFolhaPagamento(tpFolhaPagamento);
		setIdFolhaPagamento(idFolhaPagamento);
		setStFolhaPagamento(stFolhaPagamento);
		setVlGprs(vlGprs);
		setCdIndicadorSalarioMinimo(cdIndicadorSalarioMinimo);
		setVlDeducaoIrDependente(vlDeducaoIrDependente);
		setVlDeducaoIrIdoso(vlDeducaoIrIdoso);
		setVlMinimoIr(vlMinimoIr);
		setPrValeTransporte(prValeTransporte);
		setPrFgts(prFgts);
		setNrDiasUteis(nrDiasUteis);
		setVlHoraAulaP1(vlHoraAulaP1);
		setVlHoraAulaP2(vlHoraAulaP2);
		setVlHoraAulaP3(vlHoraAulaP3);
		setVlHoraAulaP4(vlHoraAulaP4);
		setVlHoraAulaP5(vlHoraAulaP5);
		setDtFechamento(dtFechamento);
		setTxtMensagem(txtMensagem);
	}
	public void setCdFolhaPagamento(int cdFolhaPagamento){
		this.cdFolhaPagamento=cdFolhaPagamento;
	}
	public int getCdFolhaPagamento(){
		return this.cdFolhaPagamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNrMes(int nrMes){
		this.nrMes=nrMes;
	}
	public int getNrMes(){
		return this.nrMes;
	}
	public void setNrAno(int nrAno){
		this.nrAno=nrAno;
	}
	public int getNrAno(){
		return this.nrAno;
	}
	public void setTpFolhaPagamento(int tpFolhaPagamento){
		this.tpFolhaPagamento=tpFolhaPagamento;
	}
	public int getTpFolhaPagamento(){
		return this.tpFolhaPagamento;
	}
	public void setIdFolhaPagamento(String idFolhaPagamento){
		this.idFolhaPagamento=idFolhaPagamento;
	}
	public String getIdFolhaPagamento(){
		return this.idFolhaPagamento;
	}
	public void setStFolhaPagamento(int stFolhaPagamento){
		this.stFolhaPagamento=stFolhaPagamento;
	}
	public int getStFolhaPagamento(){
		return this.stFolhaPagamento;
	}
	public void setVlGprs(float vlGprs){
		this.vlGprs=vlGprs;
	}
	public float getVlGprs(){
		return this.vlGprs;
	}
	public void setCdIndicadorSalarioMinimo(int cdIndicadorSalarioMinimo){
		this.cdIndicadorSalarioMinimo=cdIndicadorSalarioMinimo;
	}
	public int getCdIndicadorSalarioMinimo(){
		return this.cdIndicadorSalarioMinimo;
	}
	public void setVlDeducaoIrDependente(float vlDeducaoIrDependente){
		this.vlDeducaoIrDependente=vlDeducaoIrDependente;
	}
	public float getVlDeducaoIrDependente(){
		return this.vlDeducaoIrDependente;
	}
	public void setVlDeducaoIrIdoso(float vlDeducaoIrIdoso){
		this.vlDeducaoIrIdoso=vlDeducaoIrIdoso;
	}
	public float getVlDeducaoIrIdoso(){
		return this.vlDeducaoIrIdoso;
	}
	public void setVlMinimoIr(float vlMinimoIr){
		this.vlMinimoIr=vlMinimoIr;
	}
	public float getVlMinimoIr(){
		return this.vlMinimoIr;
	}
	public void setPrValeTransporte(float prValeTransporte){
		this.prValeTransporte=prValeTransporte;
	}
	public float getPrValeTransporte(){
		return this.prValeTransporte;
	}
	public void setPrFgts(float prFgts){
		this.prFgts=prFgts;
	}
	public float getPrFgts(){
		return this.prFgts;
	}
	public void setNrDiasUteis(int nrDiasUteis){
		this.nrDiasUteis=nrDiasUteis;
	}
	public int getNrDiasUteis(){
		return this.nrDiasUteis;
	}
	public void setVlHoraAulaP1(float vlHoraAulaP1){
		this.vlHoraAulaP1=vlHoraAulaP1;
	}
	public float getVlHoraAulaP1(){
		return this.vlHoraAulaP1;
	}
	public void setVlHoraAulaP2(int vlHoraAulaP2){
		this.vlHoraAulaP2=vlHoraAulaP2;
	}
	public int getVlHoraAulaP2(){
		return this.vlHoraAulaP2;
	}
	public void setVlHoraAulaP3(int vlHoraAulaP3){
		this.vlHoraAulaP3=vlHoraAulaP3;
	}
	public int getVlHoraAulaP3(){
		return this.vlHoraAulaP3;
	}
	public void setVlHoraAulaP4(int vlHoraAulaP4){
		this.vlHoraAulaP4=vlHoraAulaP4;
	}
	public int getVlHoraAulaP4(){
		return this.vlHoraAulaP4;
	}
	public void setVlHoraAulaP5(int vlHoraAulaP5){
		this.vlHoraAulaP5=vlHoraAulaP5;
	}
	public int getVlHoraAulaP5(){
		return this.vlHoraAulaP5;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setTxtMensagem(String txtMensagem){
		this.txtMensagem=txtMensagem;
	}
	public String getTxtMensagem(){
		return this.txtMensagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFolhaPagamento: " +  getCdFolhaPagamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nrMes: " +  getNrMes();
		valueToString += ", nrAno: " +  getNrAno();
		valueToString += ", tpFolhaPagamento: " +  getTpFolhaPagamento();
		valueToString += ", idFolhaPagamento: " +  getIdFolhaPagamento();
		valueToString += ", stFolhaPagamento: " +  getStFolhaPagamento();
		valueToString += ", vlGprs: " +  getVlGprs();
		valueToString += ", cdIndicadorSalarioMinimo: " +  getCdIndicadorSalarioMinimo();
		valueToString += ", vlDeducaoIrDependente: " +  getVlDeducaoIrDependente();
		valueToString += ", vlDeducaoIrIdoso: " +  getVlDeducaoIrIdoso();
		valueToString += ", vlMinimoIr: " +  getVlMinimoIr();
		valueToString += ", prValeTransporte: " +  getPrValeTransporte();
		valueToString += ", prFgts: " +  getPrFgts();
		valueToString += ", nrDiasUteis: " +  getNrDiasUteis();
		valueToString += ", vlHoraAulaP1: " +  getVlHoraAulaP1();
		valueToString += ", vlHoraAulaP2: " +  getVlHoraAulaP2();
		valueToString += ", vlHoraAulaP3: " +  getVlHoraAulaP3();
		valueToString += ", vlHoraAulaP4: " +  getVlHoraAulaP4();
		valueToString += ", vlHoraAulaP5: " +  getVlHoraAulaP5();
		valueToString += ", dtFechamento: " +  sol.util.Util.formatDateTime(getDtFechamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtMensagem: " +  getTxtMensagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FolhaPagamento(getCdFolhaPagamento(),
			getCdEmpresa(),
			getNrMes(),
			getNrAno(),
			getTpFolhaPagamento(),
			getIdFolhaPagamento(),
			getStFolhaPagamento(),
			getVlGprs(),
			getCdIndicadorSalarioMinimo(),
			getVlDeducaoIrDependente(),
			getVlDeducaoIrIdoso(),
			getVlMinimoIr(),
			getPrValeTransporte(),
			getPrFgts(),
			getNrDiasUteis(),
			getVlHoraAulaP1(),
			getVlHoraAulaP2(),
			getVlHoraAulaP3(),
			getVlHoraAulaP4(),
			getVlHoraAulaP5(),
			getDtFechamento()==null ? null : (GregorianCalendar)getDtFechamento().clone(),
			getTxtMensagem());
	}

}
