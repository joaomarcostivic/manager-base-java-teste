package com.tivic.manager.grl.equipamento.validations;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

import sol.dao.ItemComparator;

public class IdEquipamentoValidation implements Validator<Equipamento> {
	private EquipamentoRepository equipamentoRepository;
	
	public IdEquipamentoValidation() throws Exception {
		equipamentoRepository = (EquipamentoRepository) BeansFactory.get(EquipamentoRepository.class);
	}
	
	@Override
	public void validate(Equipamento equipamento, CustomConnection customConnection) throws Exception {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("id_equipamento", equipamento.getIdEquipamento(), ItemComparator.EQUAL, Types.VARCHAR));
		criterios.add(new ItemComparator("cd_equipamento", String.valueOf(equipamento.getCdEquipamento()), ItemComparator.DIFFERENT, Types.INTEGER));
		List<Equipamento> equipamentos = equipamentoRepository.find(criterios, customConnection);
		if(!equipamentos.isEmpty())
			throw new BadRequestException("Equipamento já cadastrado");	
	}

}
