package com.tivic.manager.mob.v2.ait;

public class InformacoesVeiculoDTO {
	private String codMunicipio;
	private String nmMunicipio;
	private String sgEstado;
	
	public InformacoesVeiculoDTO(String codMunicipio, String nmMunicipio, String sgEstado) {
		super();
		this.codMunicipio = codMunicipio;
		this.nmMunicipio = nmMunicipio;
		this.sgEstado = sgEstado;
	}

	public String getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

	public String getNmMunicipio() {
		return nmMunicipio;
	}

	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}

	public String getSgEstado() {
		return sgEstado;
	}

	public void setSgEstado(String sgEstado) {
		this.sgEstado = sgEstado;
	}
	
	
}
