package com.tivic.manager.mob.processamento;

public class ProcessamentoStatusBuilder {
	private ProcessamentoStatusDTO processamentoStatus = new ProcessamentoStatusDTO();
	
	public ProcessamentoStatusBuilder setIsProcessing(boolean isProcessing) {
		processamentoStatus.setIsProcessing(isProcessing);
		return this;
	}
	
	public ProcessamentoStatusBuilder setNrPendentesProcessamento(int nrPendentesProcessamento) {
		processamentoStatus.setNrPendentesProcessamento(nrPendentesProcessamento);
		return this;
	}
	
	public ProcessamentoStatusBuilder setProcessamentoResult(ProcessamentoResultDTO processamentoResult) {
		processamentoStatus.setProcessamentoResult(processamentoResult);
		return this;
	}
	
	public ProcessamentoStatusDTO build() {
		return processamentoStatus;
	}
}
