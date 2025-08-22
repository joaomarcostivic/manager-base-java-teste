package com.tivic.manager.wsdl.detran.ba;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.DadosCondutorDocumentoEntrada;
import com.tivic.manager.wsdl.interfaces.DadosEntrada;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ServicoDetranObjetoBA implements ServicoDetranObjeto{

	private Ait ait;
	private AitMovimento aitMovimento;
	private DadosEntradaBA dadosEntrada;
	private DadosRetornoBA dadosRetorno;
	
	public ServicoDetranObjetoBA() {
		// TODO Auto-generated constructor stub
	}

	public ServicoDetranObjetoBA(Ait ait, AitMovimento aitMovimento, DadosEntradaBA dadosEntrada, DadosRetornoBA dadosRetorno) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.dadosEntrada = dadosEntrada;
		this.dadosRetorno = dadosRetorno;
	}

	@Override
	public Ait getAit() {
		return ait;
	}

	@Override
	public void setAit(Ait ait) {
		this.ait = ait;
	}
	
	@Override
	public AitMovimento getAitMovimento() {
		return aitMovimento;
	}

	@Override
	public void setAitMovimento(AitMovimento aitMovimento) {
		this.aitMovimento = aitMovimento;
	}

	@Override
	public DadosEntradaBA getDadosEntrada() {
		return dadosEntrada;
	}

	@Override
	public void setDadosEntrada(DadosEntrada dadosEntrada) {
		this.dadosEntrada = (DadosEntradaBA) dadosEntrada;
	}

	@Override
	public DadosRetornoBA getDadosRetorno() {
		return dadosRetorno;
	}

	@Override
	public void setDadosRetorno(DadosRetorno dadosRetorno) {
		this.dadosRetorno = (DadosRetornoBA) dadosRetorno;
	}

	@Override
	public void setAitPagamento(AitPagamento aitPagamento) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AitPagamento getAitPagamento() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DadosCondutorDocumentoEntrada getDadosCondutorDocumentoEntrada() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDadosCondutorDocumentoEntrada(DadosCondutorDocumentoEntrada dadosCondutorDocumentoEntrada) {
		// TODO Auto-generated method stub
		
	}

}
