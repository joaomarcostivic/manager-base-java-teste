package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.validation.Validator;

import sol.dao.ResultSetMap;

public class AgenteTransitoValidator  implements Validator<Talonario>  {

	private Talonario talonario;

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
	
	public Optional<String> validateAgente() {
		Agente agente = AgenteDAO.get(talonario.getCdAgente());
		
		if(agente.getTpAgente() == AgenteServices.TP_TRANSITO && agente.getStAgente() == AgenteServices.ST_ATIVO)
			return Optional.empty();
		
		return Optional.of("Agente inválido, o agente deve estar ativo e ser agente de trânsito");
	}
	
	public Optional<String> validateAlteracaoAgente() {
		Connection connect = null;
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT WHERE CD_TALAO = ?");
			pstmt.setInt(1, talonario.getCdTalao());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			boolean consumido = false;
			if(rsm.next())
				consumido = true;
			
			Talonario talaoTmp = TalonarioDAO.get(talonario.getCdTalao());
			
			if(consumido && talaoTmp.getCdAgente() != talonario.getCdAgente())
				return Optional.of("Talão já tem uma ou mais folhas consumidas, não é possível alterar o agente");
			
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
