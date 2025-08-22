package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class Atendimento {

	private int cdAtendimento;
	private int cdPessoa;
	private int cdProcesso;
	private GregorianCalendar dtAtendimento;
	private int stAtendimento;
	private int stRetorno;
	private String txtAtendimento;
	private float vlOrcamento;

	public Atendimento(int cdAtendimento,
			int cdPessoa,
			int cdProcesso,
			GregorianCalendar dtAtendimento,
			int stAtendimento,
			int stRetorno,
			String txtAtendimento,
			float vlOrcamento){
		setCdAtendimento(cdAtendimento);
		setCdPessoa(cdPessoa);
		setCdProcesso(cdProcesso);
		setDtAtendimento(dtAtendimento);
		setStAtendimento(stAtendimento);
		setStRetorno(stRetorno);
		setTxtAtendimento(txtAtendimento);
		setVlOrcamento(vlOrcamento);
	}
	public void setCdAtendimento(int cdAtendimento){
		this.cdAtendimento=cdAtendimento;
	}
	public int getCdAtendimento(){
		return this.cdAtendimento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setDtAtendimento(GregorianCalendar dtAtendimento){
		this.dtAtendimento=dtAtendimento;
	}
	public GregorianCalendar getDtAtendimento(){
		return this.dtAtendimento;
	}
	public void setStAtendimento(int stAtendimento){
		this.stAtendimento=stAtendimento;
	}
	public int getStAtendimento(){
		return this.stAtendimento;
	}
	public void setStRetorno(int stRetorno){
		this.stRetorno=stRetorno;
	}
	public int getStRetorno(){
		return this.stRetorno;
	}
	public void setTxtAtendimento(String txtAtendimento){
		this.txtAtendimento=txtAtendimento;
	}
	public String getTxtAtendimento(){
		return this.txtAtendimento;
	}
	public void setVlOrcamento(float vlOrcamento){
		this.vlOrcamento=vlOrcamento;
	}
	public float getVlOrcamento(){
		return this.vlOrcamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtendimento: " +  getCdAtendimento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", dtAtendimento: " +  sol.util.Util.formatDateTime(getDtAtendimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAtendimento: " +  getStAtendimento();
		valueToString += ", stRetorno: " +  getStRetorno();
		valueToString += ", txtAtendimento: " +  getTxtAtendimento();
		valueToString += ", vlOrcamento: " +  getVlOrcamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Atendimento(cdAtendimento,
			cdPessoa,
			cdProcesso,
			dtAtendimento==null ? null : (GregorianCalendar)dtAtendimento.clone(),
			stAtendimento,
			stRetorno,
			txtAtendimento,
			vlOrcamento);
	}

}