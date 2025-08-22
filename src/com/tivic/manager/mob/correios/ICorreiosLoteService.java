package com.tivic.manager.mob.correios;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ICorreiosLoteService {
	public CorreiosLote create(CorreiosLote correiosLote) throws Exception;
	public CorreiosLote update(CorreiosLote correiosLote) throws Exception;
	public List<CorreiosLote> find(SearchCriterios searchCriterios) throws Exception;
	public List<CorreiosLoteDTO> findDTO(SearchCriterios searchCriterios) throws Exception, NoContentException;
	public List<CorreiosLoteDTO> findDTO(SearchCriterios searchCriterios, String nrEtiqueta) throws Exception, NoContentException;
	public PagedResponse<CorreiosLoteDTO> findTable(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<CorreiosLoteDTO> findTable(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public CorreiosLote get(int id) throws Exception;
}
