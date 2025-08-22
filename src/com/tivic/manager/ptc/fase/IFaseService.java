package com.tivic.manager.ptc.fase;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.Fase;
import com.tivic.sol.connection.CustomConnection;

public interface IFaseService {
	Fase get(int id) throws BadRequestException, Exception;
	List<Fase> getAll() throws Exception;
	List<Fase> getAll(CustomConnection customConnection) throws Exception;
}
