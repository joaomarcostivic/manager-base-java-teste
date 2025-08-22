package com.tivic.manager.tasks.sincronizacao.tabelas.adapters;

import java.util.GregorianCalendar;

import com.tivic.manager.fta.MarcaModelo;

public class MarcaModeloStrAdapter implements IMarcaModeloAdapter {

	private MarcaModelo ftaMarcaModelo;

	public MarcaModeloStrAdapter(com.tivic.manager.fta.MarcaModelo ftaMarcaModelo) {
	    this.ftaMarcaModelo = ftaMarcaModelo;
	}
	
	@Override
	public int getCdMarca() {
	    return ftaMarcaModelo.getCdMarca();
	}
	
	@Override
	public String getNmMarca() {
	    return ftaMarcaModelo.getNmMarca();
	}
	
	@Override
	public String getNmModelo() {
	    return ftaMarcaModelo.getNmModelo();
	}
	
	@Override
	public GregorianCalendar getDtAtualizacao() {
	    return ftaMarcaModelo.getDtAtualizacao();
	}
	
	@Override
	public com.tivic.manager.str.MarcaModelo toMarcaModeloStr() {
	    com.tivic.manager.str.MarcaModelo strMarcaModelo = new com.tivic.manager.str.MarcaModelo();
	    strMarcaModelo.setCdMarca(getCdMarca());
	    strMarcaModelo.setNmMarca(getNmMarca());
	    strMarcaModelo.setNmModelo(getNmModelo());
	    strMarcaModelo.setDtAtualizacao(getDtAtualizacao());
	    return strMarcaModelo;
	}

	@Override
	public MarcaModelo toMarcaModeloFta() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
