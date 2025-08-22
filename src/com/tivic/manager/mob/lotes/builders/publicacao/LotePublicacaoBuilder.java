package com.tivic.manager.mob.lotes.builders.publicacao;

import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.enums.publicacao.TipoLotePublicacaoEnum;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class LotePublicacaoBuilder {

    private LotePublicacao lotePublicacao;

    public LotePublicacaoBuilder() {
        lotePublicacao = new LotePublicacao();
    }

    public LotePublicacaoBuilder setCdLotePublicacao(int cdLotePublicacao) {
        lotePublicacao.setCdLotePublicacao(cdLotePublicacao);
        return this;
    }

    public LotePublicacaoBuilder setCdLote(int cdLote) throws ValidacaoException {
    	if(cdLote <= 0) {
			throw new ValidacaoException("O cdLote não foi criado em grl_lote.");
    	}
        lotePublicacao.setCdLote(cdLote);
        return this;
    }

    public LotePublicacaoBuilder setStatusAguardandoGeracao() {
        lotePublicacao.setStLote(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey());
        return this;
    }

    public LotePublicacaoBuilder setTpPublicacao(int tpPublicacao) throws ValidacaoException {
        if (TipoLotePublicacaoEnum.valueOf(tpPublicacao) == null) {
            throw new ValidacaoException("Tipo de publicação inválido.");
        }
        lotePublicacao.setTpPublicacao(tpPublicacao);
        return this;
    }

    public LotePublicacao build() {
        return lotePublicacao;
    }
}
