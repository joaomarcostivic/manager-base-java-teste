package com.tivic.manager.ptc.protocolos.documentoarquivo;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.DocumentoArquivoDAO;
import com.tivic.sol.connection.CustomConnection;

public class DocumentoArquivoRepositoryDAO implements DocumentoArquivoRepository{

	@Override
	public DocumentoArquivo insert(DocumentoArquivo objeto) throws BadRequestException, Exception {
		return insert(objeto, new CustomConnection());
	}

	@Override
	public DocumentoArquivo insert(DocumentoArquivo objeto, CustomConnection connection)
			throws BadRequestException, Exception {
		
		int result = DocumentoArquivoDAO.insert(objeto, connection.getConnection());

		if (result <= 0) {
			throw new BadRequestException("Erro ao inserir uma relação de Documento Arquivo.");
		}

		return objeto;
	}
	
	@Override
	public void delete(Integer cdArquivo, Integer cdDocumento, CustomConnection connection) throws BadRequestException, Exception {
		int result = DocumentoArquivoDAO.delete(cdArquivo, cdDocumento, connection.getConnection());

		if (result <= 0) {
			throw new BadRequestException("Erro ao deletar uma relação de Documento Arquivo.");
		}
	}

	
}
