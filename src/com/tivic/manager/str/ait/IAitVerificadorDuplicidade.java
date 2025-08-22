package com.tivic.manager.str.ait;

import java.sql.Connection;

import com.tivic.manager.mob.Ait;

public interface IAitVerificadorDuplicidade {

	public IAitVerificadorDuplicidade findByIdAit(Ait ait, Connection customConnection) throws Exception;
	public IAitVerificadorDuplicidade findByNrAit(com.tivic.manager.str.Ait ait, Connection customConnection) throws Exception;
	public <T> T get() throws Exception;

}
