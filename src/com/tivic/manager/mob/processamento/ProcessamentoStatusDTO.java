package com.tivic.manager.mob.processamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcessamentoStatusDTO {
	private boolean isProcessing;
	private int nrPendentesProcessamento;
	private ProcessamentoResultDTO processamentoResult;
	
	public boolean getIsProcessing() {
		return isProcessing;
	}
	
	public void setIsProcessing(boolean isSyncing) {
		this.isProcessing = isSyncing;
	}
	
	public int getNrPendentesProcessamento() {
		return nrPendentesProcessamento;
	}
	
	public void setNrPendentesProcessamento(int nrPendentesProcessamento) {
		this.nrPendentesProcessamento = nrPendentesProcessamento;
	}
	
	public ProcessamentoResultDTO getProcessamentoResult() {
		return processamentoResult;
	}

	public void setProcessamentoResult(ProcessamentoResultDTO processamentoResult) {
		this.processamentoResult = processamentoResult;
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
