 package com.tivic.manager.mob.concessaoveiculo.validations;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.ConcessaoVeiculoDAO;
import com.tivic.manager.mob.ConcessaoVeiculoDTO;
import com.tivic.manager.mob.ConcessaoVeiculoServices;
import javax.ws.rs.BadRequestException;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class ValidatorSituacao implements Validator {

	@Override
	public void validate(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		if(concessaoVeiculoDTO.getConcessaoVeiculo().getStConcessaoVeiculo() != ConcessaoVeiculoServices.ST_DESVINCULADO) {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_VEICULO", String.valueOf(concessaoVeiculoDTO.getVeiculo().getCdVeiculo()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(concessaoVeiculoDTO.getConcessao().getCdConcessao()), ItemComparator.DIFFERENT, Types.INTEGER));
			criterios.add(new ItemComparator("A.ST_CONCESSAO_VEICULO", Integer.toString(ConcessaoVeiculoServices.ST_VINCULADO)+ "," + Integer.toString(ConcessaoVeiculoServices.ST_LACRADO), ItemComparator.IN, Types.INTEGER));
			ResultSetMap rsm = ConcessaoVeiculoDAO.find(criterios, connect);		
			if(rsm.next()) {
				throw new BadRequestException("Este veículo já está vinculado a seguinte concessão: " + rsm.getString("nr_concessao"));
			}
		}
	}
}