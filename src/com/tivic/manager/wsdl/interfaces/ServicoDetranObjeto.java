package com.tivic.manager.wsdl.interfaces;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.DadosCondutorDocumentoEntrada;

public interface ServicoDetranObjeto {
	public void setAit(Ait ait);
	public Ait getAit();
	
	public void setAitMovimento(AitMovimento aitMovimento);
	public AitMovimento getAitMovimento();
	
	public void setAitPagamento(AitPagamento aitPagamento);
	public AitPagamento getAitPagamento();
	
	public DadosCondutorDocumentoEntrada getDadosCondutorDocumentoEntrada();
	public void setDadosCondutorDocumentoEntrada(DadosCondutorDocumentoEntrada dadosCondutorDocumentoEntrada);
	
	public void setDadosEntrada(DadosEntrada dadosEntrada);
	public DadosEntrada getDadosEntrada();
	public void setDadosRetorno(DadosRetorno dadosRetorno);
	public DadosRetorno getDadosRetorno();
}
