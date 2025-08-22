package com.tivic.manager.mob.lotes.builders.impressao;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;

public class TipoimpressaoAutuacaoBuilder {
	
	private final TipoImpressaoNotificacao tipoImpressaoNotificacao = new TipoImpressaoNotificacao();

    public TipoimpressaoAutuacaoBuilder setCdAit(int cdAit) {
        tipoImpressaoNotificacao.setCdAit(cdAit);
        return this;
    }

    public TipoimpressaoAutuacaoBuilder setCdUsuario(int cdUsuario) {
        tipoImpressaoNotificacao.setCdUsuario(cdUsuario);
        return this;
    }

    public TipoimpressaoAutuacaoBuilder setMovimento(AitMovimento aitMovimento) {
        tipoImpressaoNotificacao.setContemMovimento(aitMovimento != null && aitMovimento.getCdMovimento() > 0);
        tipoImpressaoNotificacao.setMovimentoEnviado(aitMovimento != null && 
        		aitMovimento.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey());
        return this;
    }

    public TipoimpressaoAutuacaoBuilder setMovimentoCancelado(AitMovimento aitMovimento, AitMovimento aitMovimentocancelado) {
        boolean canceladoValido = aitMovimentocancelado != null 
			&& aitMovimentocancelado.getCdMovimento() > 0 
			&& aitMovimento != null 
			&& aitMovimentocancelado.getDtMovimento().after(aitMovimento.getDtMovimento());
        tipoImpressaoNotificacao.setMovimentoCancelado(canceladoValido);
        return this;
    }

    public TipoimpressaoAutuacaoBuilder setLote(List<LoteImpressao> loteImpressaoList) {
        tipoImpressaoNotificacao.setRegistradoEmLote(loteImpressaoList != null && !loteImpressaoList.isEmpty());
        return this;
    }

    public TipoimpressaoAutuacaoBuilder setAdvertencia(AitMovimento advertencia) {
        boolean advertenciaValida = advertencia != null 
        	&& advertencia.getCdMovimento() > 0 
        	&& advertencia.getLgEnviadoDetran() != TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey() 
        	&& advertencia.getLgEnviadoDetran() != TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey();
        tipoImpressaoNotificacao.setMovimentoAdvertencia(advertenciaValida);
        return this;
    }

    public TipoImpressaoNotificacao build() {
        return tipoImpressaoNotificacao;
    }

}
