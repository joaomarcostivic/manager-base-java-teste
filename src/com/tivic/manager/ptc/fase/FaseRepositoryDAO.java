package com.tivic.manager.ptc.fase;

import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.FaseDAO;
import com.tivic.sol.connection.CustomConnection;

public class FaseRepositoryDAO implements IFaseRepository{

	@Override
	public Fase get(int id) throws Exception {
		return get(id, new CustomConnection());
	}

	@Override
	public Fase get(int id, CustomConnection customConnection) {
		return FaseDAO.get(id, customConnection.getConnection());
	}

}
