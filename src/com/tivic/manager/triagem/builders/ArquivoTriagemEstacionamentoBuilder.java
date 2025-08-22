package com.tivic.manager.triagem.builders;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public class ArquivoTriagemEstacionamentoBuilder {

	private Arquivo arquivo;
	
	public ArquivoTriagemEstacionamentoBuilder() {
		arquivo = new Arquivo();
	}
	
	public ArquivoTriagemEstacionamentoBuilder notificacao(NotificacaoEstacionamentoDigitalDTO notificacao) {
		arquivo.setNmArquivo(notificacao.getNrNotificacao());
		arquivo.setDtCriacao(notificacao.getDtNotificacao());
		return this;
	}
	
	public ArquivoTriagemEstacionamentoBuilder arquivo(byte[] blbArquivo) {
		arquivo.setBlbArquivo(blbArquivo);
		return this;
	}
	
	public Arquivo build() {
		return arquivo;
	}
	
}
