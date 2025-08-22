package com.tivic.manager.mob.processamento.conversao.services;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Orgao;
import com.tivic.sol.connection.CustomConnection;

public interface IConversorEventoService {
	public void convert(EventoEquipamento evento, Orgao orgao, int cdUsuario, CustomConnection customConnection) throws Exception;
}
