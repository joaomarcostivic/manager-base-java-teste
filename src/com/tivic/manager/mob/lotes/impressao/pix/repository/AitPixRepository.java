package com.tivic.manager.mob.lotes.impressao.pix.repository;

import java.util.List;

import com.tivic.manager.mob.lotes.impressao.pix.model.AitPix;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface AitPixRepository {

	public AitPix insert(AitPix AitPix, CustomConnection customConnection) throws Exception;
	public AitPix update(AitPix AitPix, CustomConnection customConnection) throws Exception;
	public AitPix delete(AitPix AitPix, CustomConnection customConnection) throws Exception;
	public AitPix get(int cdPix, CustomConnection customConnection) throws Exception;
	public List<AitPix> getAll(CustomConnection customConnection) throws Exception;
	public List<AitPix> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitPix> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
