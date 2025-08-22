package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;

public interface ISincronizacaoInsert<T>  {
	public void insert(List<T> objects, CustomConnection connection) throws Exception;
}
