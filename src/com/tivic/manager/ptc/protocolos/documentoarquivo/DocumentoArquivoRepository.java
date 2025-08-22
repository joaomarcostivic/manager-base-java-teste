package com.tivic.manager.ptc.protocolos.documentoarquivo;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.sol.connection.CustomConnection;

public interface DocumentoArquivoRepository {
	public DocumentoArquivo insert(DocumentoArquivo objeto) throws BadRequestException, Exception;
	public DocumentoArquivo insert(DocumentoArquivo objeto, CustomConnection connection) throws BadRequestException, Exception;
	public void delete(Integer cdArquivo, Integer cdDocumento, CustomConnection connection) throws BadRequestException, Exception;
}
