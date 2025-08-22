package com.tivic.manager.mob.lote.impressao.publicacao.builder;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.LotePublicacaoNotificacaoDto;

public class LotePublicacaoNotificacaoDtoBuilder {

	private LotePublicacaoNotificacaoDto lotePublicacaoNotificacaoDto;
	
	public LotePublicacaoNotificacaoDtoBuilder(LoteImpressao loteImpressao) {
		this.lotePublicacaoNotificacaoDto = new LotePublicacaoNotificacaoDto();
		this.lotePublicacaoNotificacaoDto.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
		this.lotePublicacaoNotificacaoDto.setDtCriacao(loteImpressao.getDtCriacao());
		this.lotePublicacaoNotificacaoDto.setDtPublicacao(loteImpressao.getDtEnvio());
		this.lotePublicacaoNotificacaoDto.setTpDocumento(loteImpressao.getTpDocumento());
	}
	
	public LotePublicacaoNotificacaoDto build() {
		return this.lotePublicacaoNotificacaoDto;
	}
	
}
