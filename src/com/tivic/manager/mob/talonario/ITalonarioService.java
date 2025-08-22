package com.tivic.manager.mob.talonario;

import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface ITalonarioService {
	public List<Talonario> getTalonairoByAgente(int cdAgente) throws Exception;
	public List<Talonario> getTalonairoByAgente(int cdAgente, CustomConnection customConncetion) throws Exception;
	public Talonario get(int cdTalao) throws Exception;
	public Talonario get(int cdTalao, CustomConnection customConncetion) throws Exception;
	public List<Talonario> find(SearchCriterios searchCriterios) throws Exception;
	public List<Talonario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Talonario insert(Talonario talonario) throws Exception;
	public Talonario insert(Talonario talonario, CustomConnection customConnection) throws Exception;
	public Talonario update(Talonario talonario) throws Exception;
	public Talonario update(Talonario talonario, CustomConnection customConnection) throws Exception;
	public List<Talonario> getDisponiveisByCdAgente(int cdAgente) throws Exception;
	public List<Talonario> getDisponiveisByCdAgente(int cdAgente, CustomConnection customConnection) throws Exception;
	public Talonario getByCdEquipamento(int cdAgente, int cdEquipamento) throws Exception;
	public Talonario getByCdEquipamento(int cdAgente, int cdEquipamento, CustomConnection customConnection) throws Exception;
	public TalonarioLoteDTO saveLote(TalonarioLoteDTO talonarioLote) throws Exception;
	public List<Talonario> findTaloesUsados(int cdTalao) throws Exception;
	public Search<Talonario> findTaloesUsados(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<TalonarioDTO> findTaloes(SearchCriterios searchCriterios) throws Exception;
}
