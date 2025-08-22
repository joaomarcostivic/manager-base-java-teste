package com.tivic.manager.mob.concessaoveiculo.validations;

import java.sql.Connection;

import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.ConcessaoVeiculoDTO;
import javax.ws.rs.BadRequestException;

import sol.dao.ResultSetMap;

public class ValidatorPrefixo implements Validator{

	public static final String[] situacaoConcessaoVeiculo = { "Não Vinculado", "Vinculado", "Desvinculado", "Lacrado"};
	
	@Override
	public void validate(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		if(concessaoVeiculoDTO.getConcessao().getTpConcessao() == ConcessaoServices.TP_COLETIVO_URBANO) {
			
			ResultSetMap rsmValidacao = new ResultSetMap(connect
			.prepareStatement("SELECT A.*, B.* FROM mob_concessao_veiculo A "
							+ " JOIN fta_veiculo              B ON ( A.cd_veiculo = B.cd_veiculo ) "
							+ " LEFT OUTER JOIN mob_concessao C ON (A.cd_concessao = C.cd_concessao) "
							+ " WHERE A.nr_prefixo           = '" + concessaoVeiculoDTO.getConcessaoVeiculo().getNrPrefixo() + "' "
							+ " AND A.cd_concessao_veiculo <> " + concessaoVeiculoDTO.getConcessaoVeiculo().getCdConcessaoVeiculo()
							+ " AND C.tp_concessao = " + ConcessaoServices.TP_COLETIVO_URBANO)
					.executeQuery());
			if (rsmValidacao.next()) {
				throw new BadRequestException("O prefixo " + concessaoVeiculoDTO.getConcessaoVeiculo().getNrPrefixo()
						+ " está sendo utilizado pelo veículo " + rsmValidacao.getString("NR_PLACA") + ", já ["
						+ situacaoConcessaoVeiculo[rsmValidacao.getInt("st_concessao_veiculo")].toUpperCase() + "].");
			}
		}	
	}
}
