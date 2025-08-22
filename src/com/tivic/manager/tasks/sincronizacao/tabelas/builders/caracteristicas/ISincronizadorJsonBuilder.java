package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.util.List;

import org.json.JSONArray;

public interface ISincronizadorJsonBuilder<T> {
	public ISincronizadorJsonBuilder<?> process(JSONArray data) throws Exception;
	public List<T> build();
}
