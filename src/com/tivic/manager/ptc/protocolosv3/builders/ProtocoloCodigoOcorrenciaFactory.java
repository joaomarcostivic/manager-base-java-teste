package com.tivic.manager.ptc.protocolosv3.builders;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;

public class ProtocoloCodigoOcorrenciaFactory {
	
	final int CD_PEDIDO_INDENTIFICACAO_ACEITO = 52;
	final int CD_SEM_VALOR_OCORRENCIA = 0;
	
	public int strategy(ProtocoloDTO protocolo) throws Exception {
		SituacaoDocumentoResultado situacaoDocumento = new SituacaoDocumentoResultado();
		if(situacaoDocumento.isIndeferido(protocolo.getDocumento().getCdSituacaoDocumento())) {
			return protocolo.getAitMovimento().getCdOcorrencia();
		}
		
		if(situacaoDocumento.isDeferido(protocolo.getDocumento().getCdSituacaoDocumento())
				&& protocolo.getTipoDocumento().getCdTipoDocumento() == TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey()) {
			return CD_PEDIDO_INDENTIFICACAO_ACEITO;
		}
		
		return CD_SEM_VALOR_OCORRENCIA;
	}
}
