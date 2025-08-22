package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class ProcessoItem {

	private int cdProcessoItem;
	private int cdProcesso;
	private GregorianCalendar dtProcesso;
	private String nrProcesso;
	private int stProcesso;
	private int cdCliente;
	private int cdResponsavel;
	private int cdProdutoServico;

	public ProcessoItem(int cdProcessoItem,
			int cdProcesso,
			GregorianCalendar dtProcesso,
			String nrProcesso,
			int stProcesso,
			int cdCliente,
			int cdResponsavel,
			int cdProdutoServico){
		setCdProcessoItem(cdProcessoItem);
		setCdProcesso(cdProcesso);
		setDtProcesso(dtProcesso);
		setNrProcesso(nrProcesso);
		setStProcesso(stProcesso);
		setCdCliente(cdCliente);
		setCdResponsavel(cdResponsavel);
		setCdProdutoServico(cdProdutoServico);
	}
	public void setCdProcessoItem(int cdProcessoItem){
		this.cdProcessoItem=cdProcessoItem;
	}
	public int getCdProcessoItem(){
		return this.cdProcessoItem;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setDtProcesso(GregorianCalendar dtProcesso){
		this.dtProcesso=dtProcesso;
	}
	public GregorianCalendar getDtProcesso(){
		return this.dtProcesso;
	}
	public void setNrProcesso(String nrProcesso){
		this.nrProcesso=nrProcesso;
	}
	public String getNrProcesso(){
		return this.nrProcesso;
	}
	public void setStProcesso(int stProcesso){
		this.stProcesso=stProcesso;
	}
	public int getStProcesso(){
		return this.stProcesso;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcessoItem: " +  getCdProcessoItem();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", dtProcesso: " +  sol.util.Util.formatDateTime(getDtProcesso(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrProcesso: " +  getNrProcesso();
		valueToString += ", stProcesso: " +  getStProcesso();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoItem(getCdProcessoItem(),
			getCdProcesso(),
			getDtProcesso()==null ? null : (GregorianCalendar)getDtProcesso().clone(),
			getNrProcesso(),
			getStProcesso(),
			getCdCliente(),
			getCdResponsavel(),
			getCdProdutoServico());
	}

}
