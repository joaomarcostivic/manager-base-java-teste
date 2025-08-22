package com.tivic.manager.tasks.sincronizacao.tabelas.services;

public interface ITaskSincronizacaoTabelas {
	public void syncAll() throws Exception;
	public void syncCores() throws Exception;
	public void syncCategorias() throws Exception;
	public void syncCidades() throws Exception;
	public void syncEspecies() throws Exception;
	public void syncMarcas() throws Exception;
	public void syncTipos() throws Exception;
	public void syncInfracoes() throws Exception;
}
