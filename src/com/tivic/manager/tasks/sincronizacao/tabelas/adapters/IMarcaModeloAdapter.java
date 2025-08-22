package com.tivic.manager.tasks.sincronizacao.tabelas.adapters;

import java.util.GregorianCalendar;

public interface IMarcaModeloAdapter {
	
	public int getCdMarca();
    public String getNmMarca();
    public String getNmModelo();
    public GregorianCalendar getDtAtualizacao();
    public com.tivic.manager.str.MarcaModelo toMarcaModeloStr();
	public com.tivic.manager.fta.MarcaModelo toMarcaModeloFta();
 
}
