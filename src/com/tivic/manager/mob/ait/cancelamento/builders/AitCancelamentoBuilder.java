package com.tivic.manager.mob.ait.cancelamento.builders;

import com.tivic.manager.mob.ait.cancelamento.AitCancelamento;

public class AitCancelamentoBuilder {
	private AitCancelamento aitCancelamento;
	
	public AitCancelamentoBuilder() {
		this.aitCancelamento = new AitCancelamento();
	}
	
	public AitCancelamentoBuilder setCdArquivo(int cdArquivo) {
		this.aitCancelamento.setCdArquivo(cdArquivo);
		return this;
	}
	
	public AitCancelamentoBuilder setCdMovimento(int cdMovimento) {
		this.aitCancelamento.setCdMovimento(cdMovimento);
		return this;
	}
	
	public AitCancelamentoBuilder setCdAit(int cdAit) {
		this.aitCancelamento.setCdAit(cdAit);
		return this;
	}
	
	public AitCancelamento build() {
		return this.aitCancelamento;
	}	

}
