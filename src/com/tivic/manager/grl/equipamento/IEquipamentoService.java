package com.tivic.manager.grl.equipamento;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEquipamentoService {
	public Equipamento insert(Equipamento equipamento) throws BadRequestException, Exception;
	public Equipamento insert(Equipamento equipamento, CustomConnection customConnection) throws BadRequestException, Exception;
	public Equipamento update(Equipamento equipamento) throws Exception;
	public Equipamento update(Equipamento equipamento, CustomConnection customConnection) throws Exception;
	public Equipamento get(int cdEquipamento) throws Exception;
	public Equipamento get(int cdEquipamento, CustomConnection customConnection) throws Exception;
	public List<EquipamentoUsuario> getAll() throws Exception;
	public List<EquipamentoUsuario> getAll(CustomConnection customConnection) throws Exception;
	public List<Equipamento> find(EquipamentoSearch equipamentoSearch) throws Exception;
	public List<Equipamento> find(EquipamentoSearch equipamentoSearch, CustomConnection customConnection) throws Exception;
	public List<Equipamento> loadDisponiveisEmprestimo(SearchCriterios searchCriterios) throws Exception;
	public List<Equipamento> loadDisponiveisEmprestimo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Equipamento getEquipamentoById(String idEquipamento) throws Exception;
	public Equipamento getEquipamentoById(String idEquipamento, CustomConnection customConnection) throws Exception;
	public boolean verificarBloqueio(String nrSerial) throws Exception;
	public boolean verificarBloqueio(String nrSerial, CustomConnection customConnection) throws Exception;
	public Equipamento getByIdEquipamento(String nmEquipamento) throws Exception;
	public Equipamento getByIdEquipamento(String nmEquipamento, CustomConnection customConnection) throws Exception;
	public Equipamento getEquipamentoAtivo(SearchCriterios searchCriterios) throws Exception;
	public Equipamento getEquipamentoAtivo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
