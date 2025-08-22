package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.validation.Validators;

public class AtaServices {
	
	public AtaServices() {}
	
	public DocumentoOcorrencia publicarDocumento(DocumentoOcorrencia _documento, Validators<?> validators, Connection connect) throws ValidationException, SQLException {
		boolean isConnNull = (connect == null);
		
		try {
			if(isConnNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Optional<String> error = validators.validateAll();
			
			if(error.isPresent())
				throw new ValidationException(error.get());
			
			DocumentoOcorrencia publicado = this.publicar(_documento);
			
			return publicado;
			
		} catch(ValidationException ex) {
			if(isConnNull) {
				Conexao.rollback(connect);
			}
			System.out.println("Erro! AitMovimentoDocumentoServices.insert");
			ex.printStackTrace(System.out);
			throw ex;
		} catch(SQLException sqlex) {
			if(isConnNull) {
				Conexao.rollback(connect);
			}
			System.out.println("Erro! AitMovimentoDocumentoServices.insert");
			sqlex.printStackTrace(System.out);
			throw sqlex;
		} finally {
			if(isConnNull) 
				Conexao.desconectar(connect);
		}
		
	}
	
	private DocumentoOcorrencia publicar(DocumentoOcorrencia _documento) {
		_documento.setCdTipoOcorrencia(4);		
		_documento.setCdOcorrencia(DocumentoOcorrenciaDAO.insert(_documento));		

		if(_documento.getCdOcorrencia() <= 0)
			return null;
		
		return _documento;
	}

}
