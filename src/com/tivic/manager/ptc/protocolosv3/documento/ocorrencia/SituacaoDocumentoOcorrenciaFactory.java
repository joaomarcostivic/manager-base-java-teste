package com.tivic.manager.ptc.protocolosv3.documento.ocorrencia;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class SituacaoDocumentoOcorrenciaFactory {
	public ISituacaoDocumentoOcorrencia getStrategy(int tpStatus) throws Exception {
		if(tpStatus == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()) {
			return new SituacaoDeferido();
		} else if(tpStatus == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()) {
			return new SituacaoIndeferido();
		} else {
			throw new ValidacaoException("Todos os resultados devem ser preenchidos.");
		}
	}
}
