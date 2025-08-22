package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class IndeferimentoDocumento implements IUpdateSituacaoDocumento {
	private DocumentoRepository documentoRepository;
	
	public IndeferimentoDocumento () throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	public Documento setSituacaoDocumento(int cdDocumento, String dsAssunto, CustomConnection customConnection) throws Exception {
		Documento documento = documentoRepository.get(cdDocumento);
		documento.setDsAssunto(dsAssunto);
		documento.setCdSituacaoDocumento(getCdSituacaoDocumento(customConnection));
		documento.setCdFase(ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0, 0, customConnection.getConnection()));
		return documentoRepository.update(documento, customConnection);
	}
	
	public int getCdSituacaoDocumento( CustomConnection customConnection) throws Exception{
		int cdSituacao = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0, 0, customConnection.getConnection());
		if(cdSituacao > 0)
			return cdSituacao;
		
		throw new Exception("Valor para o Parâmetro CD_SITUACAO_DOCUMENTO_INDEFERIDO não encontrado.");
	}
}
