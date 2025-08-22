package com.tivic.manager.triagem.app;

import java.util.GregorianCalendar;
import java.util.List;

public class EstacionamentoDigitalAppResponse {
	
	private int cdEvento;
	private GregorianCalendar dtTimeLimitResponse;
	private GregorianCalendar dtNotificacao;
    private String dsNotificacao;
    private String nrPlaca;
    private String nmRuaNotificacao;
    private String nrImovelReferencia;
    private List<byte[]> imagens;
	
	public int getCdEvento() {
		return cdEvento;
	}

	public void setCdEvento(int cdEvento) {
		this.cdEvento = cdEvento;
	}

	public GregorianCalendar getDtTimeLimitResponse() {
		return dtTimeLimitResponse;
	}

	public void setDtTimeLimitResponse(GregorianCalendar dtTimeLimitResponse) {
		this.dtTimeLimitResponse = dtTimeLimitResponse;
	}

	public GregorianCalendar getDtNotificacao() {
		return dtNotificacao;
	}

	public void setDtNotificacao(GregorianCalendar dtNotificacao) {
		this.dtNotificacao = dtNotificacao;
	}

	public String getDsNotificacao() {
		return dsNotificacao;
	}

	public void setDsNotificacao(String dsNotificacao) {
		this.dsNotificacao = dsNotificacao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public String getNmRuaNotificacao() {
		return nmRuaNotificacao;
	}

	public void setNmRuaNotificacao(String nmRuaNotificacao) {
		this.nmRuaNotificacao = nmRuaNotificacao;
	}

	public String getNrImovelReferencia() {
		return nrImovelReferencia;
	}

	public void setNrImovelReferencia(String nrImovelReferencia) {
		this.nrImovelReferencia = nrImovelReferencia;
	}

	public List<byte[]> getImagens() {
		return imagens;
	}

	public void setImagens(List<byte[]> imagens) {
		this.imagens = imagens;
	}
	
}
