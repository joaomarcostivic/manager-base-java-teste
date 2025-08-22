package com.tivic.manager.mob.processamento.conversao.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;

public class AitMovimentoConversaoBuilder {

    private AitMovimento aitMovimento;

    public AitMovimentoConversaoBuilder() {
        aitMovimento = new AitMovimento();
        aitMovimento.setDtMovimento(new GregorianCalendar());
        aitMovimento.setDtDigitacao(new GregorianCalendar());
        aitMovimento.setTpStatus(TipoStatusEnum.REGISTRO_INFRACAO.getKey());
        aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey());
    }

    public AitMovimentoConversaoBuilder setCdMovimento(int cdMovimento) {
        aitMovimento.setCdMovimento(cdMovimento);
        return this;
    }

    public AitMovimentoConversaoBuilder setCdAit(int cdAit) {
        aitMovimento.setCdAit(cdAit);
        return this;
    }
    
    public AitMovimentoConversaoBuilder setCdUsuario(int cdUsuario) {
    	aitMovimento.setCdUsuario(cdUsuario);
    	return this;
    }

    public AitMovimento build() {
		return aitMovimento;
	}
}