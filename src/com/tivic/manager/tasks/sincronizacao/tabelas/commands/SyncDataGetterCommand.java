package com.tivic.manager.tasks.sincronizacao.tabelas.commands;

import com.tivic.manager.tasks.sincronizacao.tabelas.builders.SyncDataBuilder;
import com.tivic.manager.tasks.sincronizacao.tabelas.entities.SyncDataDTO;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorCategoriaVeiculoService;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorCidadeService;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorCorService;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorEspecieVeiculoService;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorInfracaoService;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorMarcaModeloService;
import com.tivic.manager.tasks.sincronizacao.tabelas.services.consultas.api.ConsultaSincronizadorTipoVeiculoService;

public class SyncDataGetterCommand {
	
	private ConsultaSincronizadorCorService corService;
	private ConsultaSincronizadorCategoriaVeiculoService categoriaVeiculoService;
	private ConsultaSincronizadorEspecieVeiculoService especieVeiculoService;
	private ConsultaSincronizadorCidadeService cidadeService;
	private ConsultaSincronizadorMarcaModeloService marcaModeloService;
	private ConsultaSincronizadorTipoVeiculoService tipoVeiculoService;
	private ConsultaSincronizadorInfracaoService infracaoService;
	
	public SyncDataGetterCommand() throws Exception {
		corService = new ConsultaSincronizadorCorService();
		categoriaVeiculoService = new ConsultaSincronizadorCategoriaVeiculoService();
		especieVeiculoService = new ConsultaSincronizadorEspecieVeiculoService();
		cidadeService = new ConsultaSincronizadorCidadeService();
		marcaModeloService = new ConsultaSincronizadorMarcaModeloService();
		tipoVeiculoService = new ConsultaSincronizadorTipoVeiculoService();
		infracaoService = new ConsultaSincronizadorInfracaoService();
	}
	
	public SyncDataDTO executeAll() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.cor(corService.consult())
				.categoriaVeiculo(categoriaVeiculoService.consult())
				.especieVeiculo(especieVeiculoService.consult())
				.cidade(cidadeService.consult())
				.tipoVeiculo(tipoVeiculoService.consult())
				.infracao(infracaoService.consult())
				.marcaModelo(marcaModeloService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeCores() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.cor(corService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeCategorias() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.categoriaVeiculo(categoriaVeiculoService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeEspecies() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.especieVeiculo(especieVeiculoService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeCidades() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.cidade(cidadeService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeMarcas() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.marcaModelo(marcaModeloService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeTipos() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.tipoVeiculo(tipoVeiculoService.consult())
				.build();
		return syncDataDto;
	}
	
	public SyncDataDTO executeInfracoes() throws Exception{
		SyncDataBuilder syncDataBuilder = new SyncDataBuilder();
		SyncDataDTO syncDataDto = syncDataBuilder
				.infracao(infracaoService.consult())
				.build();
		return syncDataDto;
	}
}
