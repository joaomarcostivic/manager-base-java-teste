package com.tivic.manager.prc;

public class ProcessoArquivoAgenda {

	private int cdArquivo;
	private int cdProcesso;
	private int cdAgendaItem;

	public ProcessoArquivoAgenda(){ }

	public ProcessoArquivoAgenda(int cdArquivo,
			int cdProcesso,
			int cdAgendaItem){
		setCdArquivo(cdArquivo);
		setCdProcesso(cdProcesso);
		setCdAgendaItem(cdAgendaItem);
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdArquivo: " +  getCdArquivo();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdAgendaItem: " +  getCdAgendaItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoArquivoAgenda(getCdArquivo(),
			getCdProcesso(),
			getCdAgendaItem());
	}

}
