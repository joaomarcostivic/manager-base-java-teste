package com.tivic.manager.triagem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessamentoEventoDTO {
	private int cdEvento;
	private int cdUsuarioCancelamento;
    private int cdMelhorImagem;
	private int stEvento;
	private int cdMotivoCancelamento;
	private int cdUsuarioConfirmacao;
	private String nrPlaca;
	private String dsObservacaoCancelamento;
	private String txtObservacaoInfracao;
    private String nmRuaNotificacao;
    private String nrImovelReferencia;
	
	public int getCdEvento() {
		return cdEvento;
	}
	
	public void setCdEvento(int cdEvento) {
		this.cdEvento = cdEvento;
	}
	
	public int getCdUsuarioCancelamento() {
		return cdUsuarioCancelamento;
	}
	
	public void setCdUsuarioCancelamento(int cdUsuarioCancelamento) {
		this.cdUsuarioCancelamento = cdUsuarioCancelamento;
	}
	
	public int getCdMelhorImagem() {
		return cdMelhorImagem;
	}
	
	public void setCdMelhorImagem(int cdMelhorImagem) {
		this.cdMelhorImagem = cdMelhorImagem;
	}
	
	public int getStEvento() {
		return stEvento;
	}
	
	public void setStEvento(int stEvento) {
		this.stEvento = stEvento;
	}
	
	public int getCdMotivoCancelamento() {
		return cdMotivoCancelamento;
	}
	
	public void setCdMotivoCancelamento(int cdMotivoCancelamento) {
		this.cdMotivoCancelamento = cdMotivoCancelamento;
	}
	
	public int getCdUsuarioConfirmacao() {
		return cdUsuarioConfirmacao;
	}

	public void setCdUsuarioConfirmacao(int cdUsuarioConfirmacao) {
		this.cdUsuarioConfirmacao = cdUsuarioConfirmacao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	public String getDsObservacaoCancelamento() {
		return dsObservacaoCancelamento;
	}
	
	public void setDsObservacaoCancelamento(String dsObservacaoCancelamento) {
		this.dsObservacaoCancelamento = dsObservacaoCancelamento;
	}

	public String getTxtObservacaoInfracao() {
		return txtObservacaoInfracao;
	}
	
	public void setTxtObservacaoInfracao(String txtObservacaoInfracao) {
		this.txtObservacaoInfracao = txtObservacaoInfracao;
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
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
