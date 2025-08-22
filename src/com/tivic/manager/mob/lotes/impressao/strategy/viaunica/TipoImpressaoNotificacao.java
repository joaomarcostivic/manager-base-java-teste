package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TipoImpressaoNotificacao {
	
	private int cdAit;
	private int cdUsuario;
	private boolean contemMovimento;
	private boolean movimentoEnviado;
	private boolean registradoEmLote;
	private boolean movimentoCancelado;
	private boolean movimentoAdvertencia;
	private boolean naturezaAdvertencia;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public boolean getContemMovimento() {
		return contemMovimento;
	}
	public void setContemMovimento(boolean contemMovimento) {
		this.contemMovimento = contemMovimento;
	}
	public boolean getMovimentoEnviado() {
		return movimentoEnviado;
	}
	public void setMovimentoEnviado(boolean movimentoEnviado) {
		this.movimentoEnviado = movimentoEnviado;
	}
	public boolean getRegistradoEmLote() {
		return registradoEmLote;
	}
	public void setRegistradoEmLote(boolean registradoEmLote) {
		this.registradoEmLote = registradoEmLote;
	}

	public boolean getMovimentoCancelado() {
		return movimentoCancelado;
	}
	public void setMovimentoCancelado(boolean movimentoCancelado) {
		this.movimentoCancelado = movimentoCancelado;
	}
	public boolean getMovimentoAdvertencia() {
		return movimentoAdvertencia;
	}
	public void setMovimentoAdvertencia(boolean movimentoAdvertencia) {
		this.movimentoAdvertencia = movimentoAdvertencia;
	}
	public boolean setNaturezaAdvertencia() {
		return naturezaAdvertencia;
	}
	public void setNaturezaAdvertencia(boolean naturezaAdvertencia) {
		this.naturezaAdvertencia = naturezaAdvertencia;
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
