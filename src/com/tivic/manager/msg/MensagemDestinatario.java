package com.tivic.manager.msg;

import java.util.GregorianCalendar;

public class MensagemDestinatario {

	private int cdPessoa;
	private int cdMensagem;
	private GregorianCalendar dtLeitura;
	private int lgConfirmada;
	private int stMensagemDestinatario;

	public MensagemDestinatario(){ }

	public MensagemDestinatario(int cdPessoa,
			int cdMensagem,
			GregorianCalendar dtLeitura,
			int lgConfirmada,
			int stMensagemDestinatario){
		setCdPessoa(cdPessoa);
		setCdMensagem(cdMensagem);
		setDtLeitura(dtLeitura);
		setLgConfirmada(lgConfirmada);
		setStMensagemDestinatario(stMensagemDestinatario);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdMensagem(int cdMensagem){
		this.cdMensagem=cdMensagem;
	}
	public int getCdMensagem(){
		return this.cdMensagem;
	}
	public void setDtLeitura(GregorianCalendar dtLeitura){
		this.dtLeitura=dtLeitura;
	}
	public GregorianCalendar getDtLeitura(){
		return this.dtLeitura;
	}
	public void setLgConfirmada(int lgConfirmada){
		this.lgConfirmada=lgConfirmada;
	}
	public int getLgConfirmada(){
		return this.lgConfirmada;
	}
	public int getStMensagemDestinatario() {
		return stMensagemDestinatario;
	}

	public void setStMensagemDestinatario(int stMensagemDestinatario) {
		this.stMensagemDestinatario = stMensagemDestinatario;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdMensagem: " +  getCdMensagem();
		valueToString += ", dtLeitura: " +  sol.util.Util.formatDateTime(getDtLeitura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgConfirmada: " +  getLgConfirmada();
		valueToString += ", stMensagemDestinatario: " +  getStMensagemDestinatario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MensagemDestinatario(getCdPessoa(),
			getCdMensagem(),
			getDtLeitura()==null ? null : (GregorianCalendar)getDtLeitura().clone(),
			getLgConfirmada(),
			getStMensagemDestinatario());
	}

}
