package com.tivic.manager.ptc.fase;

import com.tivic.manager.ptc.Fase;
import com.tivic.sol.connection.CustomConnection;

public interface IFaseRepository {
	public Fase get(int id) throws Exception;
	public Fase get(int id, CustomConnection customConnection);
}
