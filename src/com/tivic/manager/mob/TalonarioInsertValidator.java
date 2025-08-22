package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import com.tivic.manager.validation.Validator;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

@SuppressWarnings("unused")
public class TalonarioInsertValidator implements Validator<Talonario> {
	
private Talonario talonario = null;
	
	@Override
	public Optional<String> validate(Talonario object) {
		this.talonario = object;
		
		try {
			Method[] methods = this.getClass().getDeclaredMethods();
			
			for(Method method : methods) {
				if(method.getName().endsWith("validate"))
					continue;
				
				@SuppressWarnings("unchecked")
				Optional<String> op = ((Optional<String>) method.invoke(this));
				if(op.isPresent())
					return op;
			}
			
			return Optional.empty();
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
	}
	
	private Optional<String> validateTalao() {
		Connection connect = null;
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
			
		try {
			int[] gpTalao = TalonarioServices.getGrupoTalao(talonario.getTpTalao());
			String tipos = "(";
			for(int i = 0; i < gpTalao.length ; i++) {
				tipos += "\'" + gpTalao[i] + "\'";
				
				if(gpTalao.length - i != 1)
					tipos += ", ";
			}
			tipos += ")";
			
			String sql = "SELECT * FROM MOB_TALONARIO WHERE TP_TALAO IN " + tipos + " AND (NR_INICIAL BETWEEN ? AND ? OR NR_FINAL BETWEEN ? AND ? )";		
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			
			pstmt.setInt(1, talonario.getNrInicial());
			pstmt.setInt(2, talonario.getNrFinal());
			pstmt.setInt(3, talonario.getNrInicial());
			pstmt.setInt(4, talonario.getNrFinal());

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				return Optional.of("Já existe um talonário cadastrado desse mesmo tipo com numeração inicial ou final similar");
			} else {
				return Optional.empty();
			}
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}