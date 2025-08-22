package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.validation.Validator;

import sol.dao.ResultSetMap;

@SuppressWarnings("unused")
public class TalonarioUpdateValidator implements Validator<Talonario> {
	
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
	
	private Optional<String> validateTalaoUpdate() {
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
			
			String sql = "SELECT * FROM MOB_TALONARIO WHERE TP_TALAO IN " + tipos + " AND (NR_INICIAL BETWEEN ? AND ? OR NR_FINAL BETWEEN ? AND ?)";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);			
			pstmt.setInt(1, talonario.getNrInicial());
			pstmt.setInt(2, talonario.getNrFinal());
			pstmt.setInt(3, talonario.getNrInicial());
			pstmt.setInt(4, talonario.getNrFinal());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				ArrayList<Talonario> conflitos = new ArrayList<Talonario>(new ResultSetMapper<Talonario>(rsm, Talonario.class).toList());
				
				if(conflitos.size() > 0)
					if(conflitos.get(0).getCdTalao() != talonario.getCdTalao() || conflitos.size() > 1)
						return Optional.of("Já existe talão cadastrado com essa numeração");

			} else {
				return Optional.empty();
			}
			
			return Optional.empty();
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
