package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.validation.Validator;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

@SuppressWarnings("unused")
public class AtaValidator implements Validator<DocumentoOcorrencia>{

	DocumentoOcorrencia documento = null;
	
	@Override
	public Optional<String> validate(DocumentoOcorrencia _object) {
		this.documento = _object;
		
		try {
			Method[] methods = this.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().endsWith("validate"))
					continue;
				
				@SuppressWarnings("unchecked")
				Optional<String> op = ((Optional<String>) method.invoke(this));
				
				if (op.isPresent())
					return op;
			}
			
			return Optional.empty();
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
	}
	
	public Optional<String> validateAta() {
		Connection connect;
		
		try {
			connect = Conexao.conectar();
			
			Documento _ata = DocumentoDAO.get(this.documento.getCdDocumento());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO DOC JOIN PTC_DOCUMENTO ATA ON (DOC.CD_DOCUMENTO_SUPERIOR = ATA.CD_DOCUMENTO)"
					+ " WHERE ATA.NR_DOCUMENTO = ?");
			
			pstmt.setString(1, _ata.getNrDocumento());
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next())
				return Optional.empty();
			
			Conexao.desconectar(connect);
			
			return Optional.of("Não é possível publicar um boletim vazio.");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
		
	}
	
	public Optional<String> validatePublicacao() {
		Connection connect;
		
		try {
			connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO_OCORRENCIA WHERE CD_DOCUMENTO = ?");
			
			pstmt.setInt(1, this.documento.getCdDocumento());
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(!_rsm.next())
				return Optional.empty();
			
			Conexao.desconectar(connect);
			
			return Optional.of("Documento já publicado");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
	}
	
}
