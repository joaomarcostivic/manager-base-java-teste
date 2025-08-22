package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelamentoDocumento {
	
	private DocumentoRepository documentoRepository;
	
	public CancelamentoDocumento () throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	public void setDocumentoFaseCancelado(ProtocoloDTO documento, CustomConnection customConnection) throws Exception {
		Documento novoDocumento = documentoRepository.get(documento.getDocumento().getCdDocumento());
		novoDocumento.setCdFase(getCdFaseCancelado());
		documentoRepository.update(novoDocumento, customConnection);
	}
	
	private int getCdFaseCancelado() throws Exception{
		int cdFase = ParametroServices.getValorOfParametroAsInteger("CD_FASE_CANCELADO", 0);
		
		if(cdFase > 0)
			return cdFase;
		
		throw new Exception("Valor para o Parâmetro CD_FASE_CANCELADO não encontrado.");
	}
}
