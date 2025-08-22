package com.tivic.manager.triagem.services;

import com.tivic.manager.triagem.dtos.UsuarioTriagemDTO;
import com.tivic.sol.connection.CustomConnection;

public interface IUsuarioTriagem {
	UsuarioTriagemDTO getByEmail(String nmEmail) throws Exception;
	UsuarioTriagemDTO getByEmail(String nmEmail, CustomConnection customConnection) throws Exception;
}
