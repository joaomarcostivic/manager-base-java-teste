package com.tivic.manager.tasks.sincronizacao.tabelas.adapters;

import java.util.GregorianCalendar;

public class MarcaModeloFtaAdapter implements IMarcaModeloAdapter  {

    private com.tivic.manager.str.MarcaModelo strMarcaModelo;
	
	public MarcaModeloFtaAdapter(com.tivic.manager.str.MarcaModelo strMarcaModelo) {
	    this.strMarcaModelo = strMarcaModelo;
	}
	
	@Override
	public int getCdMarca() {
	    return strMarcaModelo.getCdMarca();
	}
	
	@Override
	public String getNmMarca() {
	    return strMarcaModelo.getNmMarca();
	}
	
	@Override
	public String getNmModelo() {
	    return strMarcaModelo.getNmModelo();
	}
	
	@Override
	public GregorianCalendar getDtAtualizacao() {
	    return strMarcaModelo.getDtAtualizacao();
	}
	
	@Override
	public com.tivic.manager.fta.MarcaModelo toMarcaModeloFta() {
	    com.tivic.manager.fta.MarcaModelo ftaMarcaModelo = new com.tivic.manager.fta.MarcaModelo();
	    ftaMarcaModelo.setCdMarca(getCdMarca());
	    ftaMarcaModelo.setNmMarca(getNmMarca());
	    ftaMarcaModelo.setNmModelo(getNmModelo());
	    ftaMarcaModelo.setDtAtualizacao(getDtAtualizacao());
	    return ftaMarcaModelo;
	}

	@Override
	public com.tivic.manager.str.MarcaModelo toMarcaModeloStr() {
		// TODO Auto-generated method stub
		return null;
	}
		
}
