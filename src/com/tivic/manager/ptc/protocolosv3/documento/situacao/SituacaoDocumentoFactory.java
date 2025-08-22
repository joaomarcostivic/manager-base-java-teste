package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class SituacaoDocumentoFactory {
	public IUpdateSituacaoDocumento getStrategy(int tpStatus) throws Exception {
		if(tpStatus == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()) {
			return new DeferimentoDocumento();
		} else if(tpStatus == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()) {
			return new IndeferimentoDocumento();
		} else {
			throw new ValidacaoException("Situação do documento não registrada.");
		}
	}
}
