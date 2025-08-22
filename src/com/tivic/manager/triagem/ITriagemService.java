package com.tivic.manager.triagem;

import java.util.List;

import com.tivic.manager.triagem.dtos.GrupoEventoTriagemDTO;
import com.tivic.sol.connection.CustomConnection;

public interface ITriagemService {
	public List<GrupoEventoTriagemDTO> findGrupos() throws Exception;
	public List<GrupoEventoTriagemDTO> findGrupos(CustomConnection customConnection) throws Exception;
}
