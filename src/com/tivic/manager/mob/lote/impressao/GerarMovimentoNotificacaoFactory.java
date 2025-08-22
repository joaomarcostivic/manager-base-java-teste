package com.tivic.manager.mob.lote.impressao;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.lote.impressao.viaunica.nip.GeraFimPrazoDefesaViaUnicaNIP;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarMovimentoNotificacaoFactory {

	private static final Map<Integer, Class<? extends IGerarMovimentoNotificacao>> map = new HashMap<>();

	static {
		map.put(TipoLoteNotificacaoEnum.LOTE_NAI_VIA_UNICA.getKey(), GerarMovimentoNotificacaoNaiViaUnica.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), GerarMovimentoNotificacaoNaiLote.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIP_VIA_UNICA.getKey(), GerarMovimentoNotificacaoNipViaUnica.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), GerarMovimentoNotificacaoNipLote.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NAI_VIA_UNICA.getKey(), GeraMovimentoNotificacaoNicNaiViaUnica.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_NIC_NIP_VIA_UNICA.getKey(), GeraMovimentoNotificacaoNicNipViaUnica.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey(), GerarMovimentoNotificacaoFimPrazoDefesa.class);
		map.put(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA_VIA_UNICA.getKey(), GeraFimPrazoDefesaViaUnicaNIP.class);
	}

	public IGerarMovimentoNotificacao strategy(int tipoDocumento) throws Exception {
		Class<? extends IGerarMovimentoNotificacao> classe = map.get(tipoDocumento);
		if (classe == null) {
			throw new ValidacaoException("Tipo de lote não encontrado.");
		}
		return classe.newInstance();
	}
}
