package com.tivic.manager.grl.equipamento.repository;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class EquipamentoRepositoryDAO implements EquipamentoRepository {

	@Override
	public Equipamento insert(Equipamento equipamento, CustomConnection customConnection) throws Exception {
		int cdEquipamento = EquipamentoDAO.insert(equipamento, customConnection.getConnection());
		if(cdEquipamento < 0)
			throw new Exception("Erro ao inserir equipamento");
		equipamento.setCdEquipamento(cdEquipamento);
		return equipamento;
	}

	@Override
	public Equipamento update(Equipamento equipamento, CustomConnection customConnection) throws Exception {
		EquipamentoDAO.update(equipamento, customConnection.getConnection());
		return equipamento;
	}

	@Override
	public Equipamento delete(int cdEquipamento, CustomConnection customConnection) throws Exception {
		Equipamento equipamento = get(cdEquipamento, customConnection);
		EquipamentoDAO.delete(cdEquipamento, customConnection.getConnection());
		return equipamento;
	}

	@Override
	public Equipamento get(int cdEquipamento, CustomConnection customConnection) throws Exception {
		return EquipamentoDAO.get(cdEquipamento, customConnection.getConnection());
	}

    @Override
    public List<Equipamento> getAll() throws Exception {
    	return getAll(new CustomConnection());
    }
	
	@Override
	public List<Equipamento> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Equipamento.class);
	}

	@Override
	public List<Equipamento> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.setCriterios(criterios);
		Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento").searchCriterios(searchCriterios).build();
		return search.getList(Equipamento.class);
	}

}
