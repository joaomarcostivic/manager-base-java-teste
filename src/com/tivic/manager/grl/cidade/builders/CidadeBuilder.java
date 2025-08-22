package com.tivic.manager.grl.cidade.builders;

import com.tivic.manager.grl.Cidade;

public class CidadeBuilder {
	
	private Cidade cidade;
	
	public CidadeBuilder() {
		this.cidade = new Cidade();
	}
	
	public CidadeBuilder cdCidade(int cdCidade) {
		this.cidade.setCdCidade(cdCidade);
		return this;
	}

	public CidadeBuilder nmCidade(String nmCidade) {
		this.cidade.setNmCidade(nmCidade);
		return this;
	}

	public CidadeBuilder nrCep(String nrCep) {
		this.cidade.setNrCep(nrCep);
		return this;
	}

	public CidadeBuilder vlAltitude(float vlAltitude) {
		this.cidade.setVlAltitude(vlAltitude);
		return this;
	}

	public CidadeBuilder vlLatitude(float vlLatitude) {
		this.cidade.setVlLatitude(vlLatitude);
		return this;
	}

	public CidadeBuilder vlLongitude(float vlLongitude) {
		this.cidade.setVlLongitude(vlLongitude);
		return this;
	}

	public CidadeBuilder cdEstado(int cdEstado) {
		this.cidade.setCdEstado(cdEstado);
		return this;
	}

	public CidadeBuilder idCidade(String idCidade) {
		this.cidade.setIdCidade(idCidade);
		return this;
	}

	public CidadeBuilder cdRegiao(int cdRegiao) {
		this.cidade.setCdRegiao(cdRegiao);
		return this;
	}

	public CidadeBuilder idIbge(String idIbge) {
		this.cidade.setIdIbge(idIbge);
		return this;
	}

	public CidadeBuilder sgCidade(String sgCidade) {
		this.cidade.setSgCidade(sgCidade);
		return this;
	}

	public CidadeBuilder qtDistanciaCapital(int qtDistanciaCapital) {
		this.cidade.setQtDistanciaCapital(qtDistanciaCapital);
		return this;
	}
	
	public CidadeBuilder qtDistanciaBase(int qtDistanciaBase) {
		this.cidade.setQtDistanciaBase(qtDistanciaBase);
		return this;
	}
	
	public Cidade build() {
		return this.cidade;
	}
}
