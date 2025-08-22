package com.tivic.manager.tasks.sincronizacao.tabelas.services;

import java.util.GregorianCalendar;

import com.tivic.manager.tasks.sincronizacao.tabelas.commands.SyncDataGetterCommand;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizacaoCategoriaVeiculoInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizacaoCidadeInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizacaoCorInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizacaoInfracaoInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizacaoMarcaModeloInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizacaoTipoVeiculoInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.SincronizadorEspecieVeiculoInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.entities.SyncDataDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class TaskSincronizacaoTabelas implements ITaskSincronizacaoTabelas{
	
	private SyncDataDTO syncDataDTO;
	private SincronizacaoCorInsert corInsert;
	private SincronizacaoCategoriaVeiculoInsert categoriaVeiculoInsert;
	private SincronizacaoCidadeInsert cidadeInsert;
	private SincronizadorEspecieVeiculoInsert especieVeiculoInsert;
	private SincronizacaoMarcaModeloInsert marcaModeloInsert;
	private SincronizacaoTipoVeiculoInsert tipoVeiculoInsert;
	private SincronizacaoInfracaoInsert infracaoInsert;
	private SyncDataGetterCommand syncDataGetter;
	private ManagerLog managerLog;
	
	public TaskSincronizacaoTabelas() throws Exception {
		syncDataGetter = new SyncDataGetterCommand();
		corInsert = new SincronizacaoCorInsert();
		categoriaVeiculoInsert = new SincronizacaoCategoriaVeiculoInsert();
		cidadeInsert = new SincronizacaoCidadeInsert();
		especieVeiculoInsert = new SincronizadorEspecieVeiculoInsert();
		marcaModeloInsert = new SincronizacaoMarcaModeloInsert();
		tipoVeiculoInsert = new SincronizacaoTipoVeiculoInsert();
		infracaoInsert = new SincronizacaoInfracaoInsert();
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@Override
	public void syncAll() throws Exception {
		
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeAll();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			categoriaVeiculoInsert.insert(syncDataDTO.getCategoriasVeiculos(), connection); 
			cidadeInsert.insert(syncDataDTO.getCidades(), connection); 
			corInsert.insert(syncDataDTO.getCores(), connection);
			especieVeiculoInsert.insert(syncDataDTO.getEspeciesVeiculos(), connection);
			tipoVeiculoInsert.insert(syncDataDTO.getTiposVeiculos(), connection);
			infracaoInsert.insert(syncDataDTO.getInfracoes(), connection);
			marcaModeloInsert.insert(syncDataDTO.getMarcasModelos(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
		
	}

	@Override
	public void syncCores() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeCores();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			corInsert.insert(syncDataDTO.getCores(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
	}

	@Override
	public void syncCategorias() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeCategorias();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			categoriaVeiculoInsert.insert(syncDataDTO.getCategoriasVeiculos(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
		
	}

	@Override
	public void syncCidades() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeCidades();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			cidadeInsert.insert(syncDataDTO.getCidades(), connection); 
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
	}

	@Override
	public void syncEspecies() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeEspecies();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			especieVeiculoInsert.insert(syncDataDTO.getEspeciesVeiculos(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
		
	}

	@Override
	public void syncMarcas() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeMarcas();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			marcaModeloInsert.insert(syncDataDTO.getMarcasModelos(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
	}
	
	@Override
	public void syncTipos() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeTipos();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			tipoVeiculoInsert.insert(syncDataDTO.getTiposVeiculos(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
	}
	
	@Override
	public void syncInfracoes() throws Exception {
		CustomConnection connection = new CustomConnection();
		syncDataDTO = syncDataGetter.executeInfracoes();
		try {
			managerLog.info("INICIO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			infracaoInsert.insert(syncDataDTO.getInfracoes(), connection);
			managerLog.info("FINALIZAÇÃO DA TASK DE SINCRONIZAÇÃO DE CARACTERÍSTICAS DE VEÍCULOS: ", 
					new GregorianCalendar().getTime().toString());
			connection.finishConnection();
		} finally {
			connection.closeConnection();	
		}
	}
}
