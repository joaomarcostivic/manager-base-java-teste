package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.documentopessoa.DocumentoPessoaRepository;
import com.tivic.manager.ptc.portal.enums.TipoNmQualificacaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InsereDocumentoPessoa {
	
	private DocumentoPessoaRepository documentoPessoaRepository;

	public InsereDocumentoPessoa() throws Exception {
		documentoPessoaRepository = (DocumentoPessoaRepository) BeansFactory.get(DocumentoPessoaRepository.class);
	}
	
	public void inserir(Documento documento, int cdPessoa, CustomConnection customConnection) throws Exception {
		documentoPessoaRepository.insert(setDocumentoPessoa(documento, cdPessoa), customConnection);
	}
	
	private DocumentoPessoa setDocumentoPessoa(Documento documento, int cdPessoa) {
		DocumentoPessoa documentoPessoa = new DocumentoPessoa();
		documentoPessoa.setCdDocumento(documento.getCdDocumento());
		documentoPessoa.setCdPessoa(cdPessoa);
		documentoPessoa.setNmQualificacao(TipoNmQualificacaoEnum.REQUERENTE.getValue());
		return documentoPessoa;
	}
}
