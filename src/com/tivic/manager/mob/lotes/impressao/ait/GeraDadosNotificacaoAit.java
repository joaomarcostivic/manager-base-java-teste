package com.tivic.manager.mob.lotes.impressao.ait;

import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.sol.search.Search;

public class GeraDadosNotificacaoAit extends DadosNotificacaoAitBase {

	public GeraDadosNotificacaoAit() throws Exception {
		super();
	}
	
	@Override
	protected Search<Notificacao> getNotificacaoSearchByCdAit(int cdAit) throws Exception {
        return getDadosByCdAit(cdAit)
        		.addField(" B.vl_infracao, B.vl_infracao AS vl_multa ")
                .build();
    }

}
