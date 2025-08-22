package com.tivic.manager.mob.lotes.factory.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.movimento.GeraMovimentoNotificacaoFimPrazoDefesa;
import com.tivic.manager.mob.lotes.movimento.IGeraMovimentoNotificacao;
import com.tivic.manager.mob.lotes.movimento.lote.GeraMovimentoNotificacaoNaiLote;
import com.tivic.manager.mob.lotes.movimento.lote.GeraMovimentoNotificacaoNipLote;
import com.tivic.manager.mob.lotes.movimento.viaunica.GeraFimPrazoDefesaViaUnicaNIP;
import com.tivic.manager.mob.lotes.movimento.viaunica.GeraMovimentoNotificacaoNaiViaUnica;
import com.tivic.manager.mob.lotes.movimento.viaunica.GeraMovimentoNotificacaoNipViaUnica;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GeraMovimentoNotificacaoFactory {

	private static final Map<Integer, Class<? extends IGeraMovimentoNotificacao>> map = new HashMap<>();

	static {
		map.put(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey(), GeraMovimentoNotificacaoNaiViaUnica.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NAI.getKey(), GeraMovimentoNotificacaoNaiLote.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey(), GeraMovimentoNotificacaoNipViaUnica.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIP.getKey(), GeraMovimentoNotificacaoNipLote.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey(), GeraMovimentoNotificacaoNaiViaUnica.class);
		map.put(TipoLoteImpressaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey(), GeraMovimentoNotificacaoNipViaUnica.class);
		map.put(TipoLoteImpressaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey(), GeraMovimentoNotificacaoFimPrazoDefesa.class);
		map.put(TipoLoteImpressaoEnum.LOTE_FIM_PRAZO_DEFESA_VIA_UNICA.getKey(), GeraFimPrazoDefesaViaUnicaNIP.class);
	}

	public IGeraMovimentoNotificacao strategy(int tipoDocumento) throws Exception {
		Class<? extends IGeraMovimentoNotificacao> classe = map.get(tipoDocumento);
		if (classe == null) {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}
		return classe.newInstance();
	}
}
