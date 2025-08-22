package com.tivic.manager.mob.veiculoequipamento;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.tivic.manager.mob.Equipamento;
import com.tivic.manager.mob.EquipamentoDAO;
import com.tivic.manager.mob.VeiculoEquipamentoDAO;
import com.tivic.manager.mob.VeiculoEquipamentoDTO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.validation.Validators;

public class VeiculoEquipamentoService {
	
	public VeiculoEquipamentoDTO insert(VeiculoEquipamentoDTO veiculoEquipamento, Validators<?> validators) throws SQLException, ValidationException, Exception{
		return insert(veiculoEquipamento, validators, null);
	}
	
	public VeiculoEquipamentoDTO insert(VeiculoEquipamentoDTO veiculoEquipamento, Validators<?> validators, Connection connection) throws SQLException, ValidationException, Exception{
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			validate(validators);
			insertEquipamento(veiculoEquipamento.getEquipamento(), connection);
			veiculoEquipamento.getVeiculoEquipamento().setCdEquipamento(veiculoEquipamento.getEquipamento().getCdEquipamento());
			int resultInsert = VeiculoEquipamentoDAO.insert(veiculoEquipamento.getVeiculoEquipamento(), connection);
			if(resultInsert <= 0) {
				if(isConnectionNull)
					Conexao.rollback(connection);
				throw new Exception("Erro ao inserir veiculo equipamento");
			}
			
			if(isConnectionNull) {
				connection.commit();
			}
			return veiculoEquipamento;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
		
		

	}

	private void insertEquipamento(Equipamento equipamento, Connection connection) throws SQLException, ValidationException, Exception{
		int resultInsert = EquipamentoDAO.insert(equipamento, connection);
		if(resultInsert <= 0) {
			throw new Exception("Erro ao inserir equipamento");
		}
	}

	private void validate(Validators<?> validators) throws ValidationException{
		Optional<String> error = validators.validateAll();
		if(error.isPresent())
			throw new ValidationException(error.get());
	}
	
}
