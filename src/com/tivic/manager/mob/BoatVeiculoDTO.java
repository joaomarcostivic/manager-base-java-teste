package com.tivic.manager.mob;

import java.sql.Connection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoServices;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorServices;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoServices;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloServices;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoatVeiculoDTO extends BoatVeiculo {

	private MarcaModelo modelo;
	private Cidade cidade;
	private TipoVeiculo tipoVeiculo;
	private Cor cor;
	private CategoriaVeiculo categoriaVeiculo;
	private MarcaModelo marca;
	private EspecieVeiculo especieVeiculo;
	private Pessoa proprietario;
		
	public MarcaModelo getModelo() {
		return modelo;
	}

	public void setModelo(MarcaModelo modelo) {
		this.modelo = modelo;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}

	public CategoriaVeiculo getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
	}

	public MarcaModelo getMarca() {
		return marca;
	}

	public void setMarca(MarcaModelo marca) {
		this.marca = marca;
	}

	public EspecieVeiculo getEspecieVeiculo() {
		return especieVeiculo;
	}

	public void setEspecieVeiculo(EspecieVeiculo especieVeiculo) {
		this.especieVeiculo = especieVeiculo;
	}

	public Pessoa getProprietario() {
		return proprietario;
	}

	public void setProprietario(Pessoa proprietario) {
		this.proprietario = proprietario;
	}
	
	/* **************** */
	/* Busca por id */
	/* **************** */
	
	public void setMarca(int cdMarca, Connection connect) {
		this.setModelo(MarcaModeloServices.get(cdMarca, connect));
	}

	public void setCidade(int cdCidade, Connection connect) {
		this.setCidade(CidadeDAO.get(cdCidade, connect));
	}

	public void setTipoVeiculo(int cdTipoVeiculo, Connection connect) {
		this.setTipoVeiculo(TipoVeiculoServices.get(cdTipoVeiculo, connect));
	}

	public void setCor(int cdCor, Connection connect) {
		this.setCor(CorServices.get(cdCor, connect));
	}

	public void setCategoriaVeiculo(int cdCategoria, Connection connect) {
		this.setCategoriaVeiculo(CategoriaVeiculoServices.get(cdCategoria, connect));
	}

	public void setEspecieVeiculo(int cdEspecieVeiculo, Connection connect) {
		this.setEspecieVeiculo(EspecieVeiculoServices.get(cdEspecieVeiculo, connect));
	}

	public void setProprietario(int cdProprietario, Connection connect) {
		this.setProprietario(PessoaDAO.get(cdProprietario, connect));
	}

}
