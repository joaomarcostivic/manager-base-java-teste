package com.tivic.manager.tasks.sincronizacao.tabelas.entities;

import java.util.List;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.mob.Infracao;

public class SyncDataDTO {
	private List<EspecieVeiculo> especiesVeiculos;
	private List<Cor> cores;
	private List<CategoriaVeiculo> categoriasVeiculos;
	private List<MarcaModelo> marcasModelos;
	private List<Cidade> cidades;
	private List<TipoVeiculo> tipoVeiculos;
	private List<Infracao> infracoes;
	
	public List<EspecieVeiculo> getEspeciesVeiculos() {
		return especiesVeiculos;
	}
	public void setEspeciesVeiculos(List<EspecieVeiculo> especiesVeiculos) {
		this.especiesVeiculos = especiesVeiculos;
	}
	public List<Cor> getCores() {
		return cores;
	}
	public void setCores(List<Cor> cores) {
		this.cores = cores;
	}
	public List<CategoriaVeiculo> getCategoriasVeiculos() {
		return categoriasVeiculos;
	}
	public void setCategoriasVeiculos(List<CategoriaVeiculo> categoriasVeiculos) {
		this.categoriasVeiculos = categoriasVeiculos;
	}
	public List<MarcaModelo> getMarcasModelos() {
		return marcasModelos;
	}
	public void setMarcasModelos(List<MarcaModelo> marcasModelos) {
		this.marcasModelos = marcasModelos;
	}
	public List<Cidade> getCidades() {
		return cidades;
	}
	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}
	public List<TipoVeiculo> getTiposVeiculos() {
		return tipoVeiculos;
	}
	public void setTiposVeiculos(List<TipoVeiculo> tipos) {
		this.tipoVeiculos = tipos;
	}
	public List<Infracao> getInfracoes() {
		return infracoes;
	}
	public void setInfracoes(List<Infracao> infracoes) {
		this.infracoes = infracoes;
	}	
	
	
}
