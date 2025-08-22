package com.tivic.manager.mob.veiculo.validations;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.mob.veiculo.validations.Validator;
import javax.ws.rs.BadRequestException;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class ValidatorPlaca implements Validator{

	@Override
	public void validate(Veiculo veiculo, Connection connect) throws Exception {

		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("CD_VEICULO", String.valueOf(veiculo.getCdVeiculo()), ItemComparator.DIFFERENT, Types.INTEGER));
		criterios.add(new ItemComparator("NR_PLACA", String.valueOf(veiculo.getNrPlaca()), ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsm = VeiculoDAO.find(criterios, connect);		
		if(rsm.next()) {
			throw new BadRequestException("A placa " + rsm.getString("nr_placa") + " já está associada a um veículo existente.");
		}
	}

}
