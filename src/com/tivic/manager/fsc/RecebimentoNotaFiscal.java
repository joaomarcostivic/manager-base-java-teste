package com.tivic.manager.fsc;

import java.util.GregorianCalendar;

public class RecebimentoNotaFiscal {

	private int cdRecebimentoNotaFiscal;
	private int cdPessoa;
	private int nrMes;
	private GregorianCalendar dtRecebimento;
	private int stRecebimento;
	private int nrAno;
	private int cdNotaFiscal;
	private int cdConcessao;

	public RecebimentoNotaFiscal() { }

	public RecebimentoNotaFiscal(int cdRecebimentoNotaFiscal,
			int cdPessoa,
			int nrMes,
			GregorianCalendar dtRecebimento,
			int stRecebimento,
			int nrAno,
			int cdNotaFiscal,
			int cdConcessao) {
		setCdRecebimentoNotaFiscal(cdRecebimentoNotaFiscal);
		setCdPessoa(cdPessoa);
		setNrMes(nrMes);
		setDtRecebimento(dtRecebimento);
		setStRecebimento(stRecebimento);
		setNrAno(nrAno);
		setCdNotaFiscal(cdNotaFiscal);
		setCdConcessao(cdConcessao);
	}
	public void setCdRecebimentoNotaFiscal(int cdRecebimentoNotaFiscal){
		this.cdRecebimentoNotaFiscal=cdRecebimentoNotaFiscal;
	}
	public int getCdRecebimentoNotaFiscal(){
		return this.cdRecebimentoNotaFiscal;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNrMes(int nrMes){
		this.nrMes=nrMes;
	}
	public int getNrMes(){
		return this.nrMes;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setStRecebimento(int stRecebimento){
		this.stRecebimento=stRecebimento;
	}
	public int getStRecebimento(){
		return this.stRecebimento;
	}
	public void setNrAno(int nrAno) {
		this.nrAno = nrAno;
	}
	public int getNrAno() {
		return nrAno;
	}
	public void setCdNotaFiscal(int cdNotaFiscal) {
		this.cdNotaFiscal = cdNotaFiscal;
	}
	public int getCdNotaFiscal() {
		return cdNotaFiscal;
	}
	public void setCdConcessao(int cdConcessao) {
		this.cdConcessao = cdConcessao;
	}
	public int getCdConcessao() {
		return cdConcessao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecebimentoNotaFiscal: " +  getCdRecebimentoNotaFiscal();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nrMes: " +  getNrMes();
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stRecebimento: " +  getStRecebimento();
		valueToString += ", nrAno: " +  getNrAno();
		valueToString += ", cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RecebimentoNotaFiscal(getCdRecebimentoNotaFiscal(),
			getCdPessoa(),
			getNrMes(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getStRecebimento(),
			getNrAno(),
			getCdNotaFiscal(),
			getCdConcessao());
	}

}