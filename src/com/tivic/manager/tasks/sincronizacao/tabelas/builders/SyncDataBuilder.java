package com.tivic.manager.tasks.sincronizacao.tabelas.builders;

import org.json.JSONArray;

import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.CategoriaVeiculoListBuilder;
import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.CidadeListBuild;
import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.CorListBuilder;
import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.EspecieVeiculoListBuilder;
import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.InfracaoListBuild;
import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.MarcaModeloListBuilder;
import com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas.TipoVeiculoListBuilder;
import com.tivic.manager.tasks.sincronizacao.tabelas.entities.SyncDataDTO;

public class SyncDataBuilder {
	private SyncDataDTO syncDataDTO;
	
	public SyncDataBuilder() {
		syncDataDTO = new SyncDataDTO();
	}
	
	public SyncDataBuilder cor(JSONArray data) throws Exception {
		CorListBuilder corBuilder = new CorListBuilder();
		syncDataDTO.setCores(corBuilder.process(data).build());
		return this;
	}
	
	public SyncDataBuilder categoriaVeiculo(JSONArray data) throws Exception {
		CategoriaVeiculoListBuilder categoriaVeiculoListBuilder = new CategoriaVeiculoListBuilder();
		syncDataDTO.setCategoriasVeiculos(categoriaVeiculoListBuilder.process(data).build());
		return this;
	}
	
	public SyncDataBuilder especieVeiculo(JSONArray data) throws Exception {
		EspecieVeiculoListBuilder especieVeiculoListBuilder = new EspecieVeiculoListBuilder();
		syncDataDTO.setEspeciesVeiculos(especieVeiculoListBuilder.process(data).build());
		return this;
	}
	
	public SyncDataBuilder cidade(JSONArray data) throws Exception {
		CidadeListBuild cidadeListBuild = new CidadeListBuild();
		syncDataDTO.setCidades(cidadeListBuild.process(data).build());
		return this;
	}
	
	public SyncDataBuilder marcaModelo(JSONArray data) throws Exception {
		MarcaModeloListBuilder marcaModeloListBuilder = new MarcaModeloListBuilder();
		syncDataDTO.setMarcasModelos(marcaModeloListBuilder.process(data).build());
		return this;
	} 
	
	public SyncDataBuilder tipoVeiculo(JSONArray data) throws Exception {
		TipoVeiculoListBuilder tipoListBuilder = new TipoVeiculoListBuilder();
		syncDataDTO.setTiposVeiculos(tipoListBuilder.process(data).build());
		return this;
	} 
	
	public SyncDataBuilder infracao(JSONArray data) throws Exception {
		InfracaoListBuild infracaoListBuilder = new InfracaoListBuild();
		syncDataDTO.setInfracoes(infracaoListBuilder.process(data).build());
		return this;
	} 
	
	public SyncDataDTO build() {
		return syncDataDTO;
	}
	
}
