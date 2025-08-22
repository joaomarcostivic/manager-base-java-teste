package com.tivic.manager.grl.equipamento.repository;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.sol.connection.CustomConnection;

import sol.dao.ItemComparator;

public interface EquipamentoRepository {
	public Equipamento insert(Equipamento equipamento, CustomConnection customConnection) throws Exception;
	public Equipamento update(Equipamento equipamento, CustomConnection customConnection) throws Exception;
	public Equipamento delete(int cdEquipamento, CustomConnection customConnection) throws Exception;
	public Equipamento get(int cdEquipamento, CustomConnection customConnection) throws Exception;
	public List<Equipamento> getAll() throws Exception;
	public List<Equipamento> getAll(CustomConnection customConnection) throws Exception;
	public List<Equipamento> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection) throws Exception;
}
