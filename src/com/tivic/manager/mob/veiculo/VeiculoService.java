package com.tivic.manager.mob.veiculo;

import java.sql.Connection;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.mob.veiculo.validations.SaveValidationVeiculo;

public class VeiculoService {

	public Veiculo save(Veiculo veiculo) throws Exception {
		return save(veiculo, null);
	}

	public Veiculo save(Veiculo veiculo, Connection connect) throws Exception {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			/**
			 * Valida a existência de outro veículo com a placa, chassi ou renavam
			 * informados
			 */
			new SaveValidationVeiculo(veiculo).validate(connect);
			if (veiculo.getCdVeiculo() == 0) {
				int cdVeiculo = VeiculoDAO.insert(veiculo, connect);
				if (cdVeiculo < 0) {
					throw new Exception("Erro na inserção do veículo.");
				}
				veiculo.setCdVeiculo(cdVeiculo);
			} else {
				int retorno = VeiculoDAO.update(veiculo, connect);
				if (retorno < 0) {
					throw new Exception("Erro na atualização do veículo.");
				}
			}

			if (isConnectionNull) {
				connect.commit();
			}

			return veiculo;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
