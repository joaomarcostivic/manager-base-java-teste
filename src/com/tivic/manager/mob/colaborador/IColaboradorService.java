package com.tivic.manager.mob.colaborador;

import java.util.List;
import java.util.Map;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IColaboradorService {
	ColaboradorDTO create(ColaboradorDTO colaboradorDTO) throws Exception;
	List<ColaboradorDTO> findAll(SearchCriterios searchCriterios) throws Exception;
	ColaboradorDTO findById(int cdPessoa) throws Exception;
	ColaboradorDTO update(ColaboradorDTO colaboradorDTO) throws Exception;
	PagedResponse<ColaboradorTableDTO> findColaboradores(SearchCriterios searchCriterios) throws Exception;
	Search<ColaboradorTableDTO> findColaboradores(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	ColaboradorDTO get(int cdPessoa) throws Exception;
	ColaboradorDTO get(int cdPessoa, CustomConnection customConnection) throws Exception;
	ColaboradorDTO inativar(ColaboradorDTO colaboradorDTO) throws Exception;
	ColaboradorDTO inativar(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception;
	ColaboradorDTO ativar(ColaboradorDTO colaboradorDTO) throws Exception;
	ColaboradorDTO ativar(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception;
	public Search<Pessoa> searchPessoaColaborador(SearchCriterios searchCriterios) throws Exception;
	public String buscaNomeAutoridadeTransito() throws Exception;
	public Map<String, Object> buscarAssinaturaAutoridade() throws Exception;
}