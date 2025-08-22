package com.tivic.manager.mob.ait.relatorios;

import com.tivic.sol.connection.CustomConnection;

public interface IGeraSegundaViaNaiService {
	public byte [] gerarSegundaViaNai(int[] cdAits, CustomConnection customConnection) throws Exception;
	public byte [] gerarSegundaViaNai (int[] cdAits) throws Exception;

}
