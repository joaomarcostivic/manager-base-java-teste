package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class ProcessoAtividadeItem {

	private int cdProcessoItem;
	private int cdAtividadeItem;
	private int cdAtividade;
	private int cdProcesso;
	private int cdAgendamento;
	private GregorianCalendar dtRecebimento;
	private GregorianCalendar dtLancamento;

	public ProcessoAtividadeItem(int cdProcessoItem,
			int cdAtividadeItem,
			int cdAtividade,
			int cdProcesso,
			int cdAgendamento,
			GregorianCalendar dtRecebimento,
			GregorianCalendar dtLancamento){
		setCdProcessoItem(cdProcessoItem);
		setCdAtividadeItem(cdAtividadeItem);
		setCdAtividade(cdAtividade);
		setCdProcesso(cdProcesso);
		setCdAgendamento(cdAgendamento);
		setDtRecebimento(dtRecebimento);
		setDtLancamento(dtLancamento);
	}
	public void setCdProcessoItem(int cdProcessoItem){
		this.cdProcessoItem=cdProcessoItem;
	}
	public int getCdProcessoItem(){
		return this.cdProcessoItem;
	}
	public void setCdAtividadeItem(int cdAtividadeItem){
		this.cdAtividadeItem=cdAtividadeItem;
	}
	public int getCdAtividadeItem(){
		return this.cdAtividadeItem;
	}
	public void setCdAtividade(int cdAtividade){
		this.cdAtividade=cdAtividade;
	}
	public int getCdAtividade(){
		return this.cdAtividade;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento){
		this.dtRecebimento=dtRecebimento;
	}
	public GregorianCalendar getDtRecebimento(){
		return this.dtRecebimento;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcessoItem: " +  getCdProcessoItem();
		valueToString += ", cdAtividadeItem: " +  getCdAtividadeItem();
		valueToString += ", cdAtividade: " +  getCdAtividade();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", dtRecebimento: " +  sol.util.Util.formatDateTime(getDtRecebimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoAtividadeItem(getCdProcessoItem(),
			getCdAtividadeItem(),
			getCdAtividade(),
			getCdProcesso(),
			getCdAgendamento(),
			getDtRecebimento()==null ? null : (GregorianCalendar)getDtRecebimento().clone(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone());
	}

}
