package com.tivic.manager.wsdl.detran.mg;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.DadosCondutorDocumentoEntrada;
import com.tivic.manager.wsdl.interfaces.DadosEntrada;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ServicoDetranObjetoMG implements ServicoDetranObjeto{

	private Ait ait;
	private AitMovimento aitMovimento;
	private AitPagamento aitPagamento;
	private DadosEntradaMG dadosEntrada;
	private DadosRetornoMG dadosRetorno;
	
	public ServicoDetranObjetoMG() {
		// TODO Auto-generated constructor stub
	}

	public ServicoDetranObjetoMG(AitDetranObject aitDetranObject) {
		this.ait = aitDetranObject.getAit();
		this.aitMovimento = aitDetranObject.getAitMovimento();
	}

	public ServicoDetranObjetoMG(Ait ait, AitMovimento aitMovimento, DadosEntradaMG dadosEntrada, DadosRetornoMG dadosRetorno) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.dadosEntrada = dadosEntrada;
		this.dadosRetorno = dadosRetorno;
	}
	
	public ServicoDetranObjetoMG(Ait ait, AitMovimento aitMovimento, AitPagamento aitPagamento, DadosEntradaMG dadosEntrada, DadosRetornoMG dadosRetorno) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.aitPagamento = aitPagamento;
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
	public DadosEntradaMG getDadosEntrada() {
		return dadosEntrada;
	}

	@Override
	public void setDadosEntrada(DadosEntrada dadosEntrada) {
		this.dadosEntrada = (DadosEntradaMG) dadosEntrada;
	}

	@Override
	public DadosRetornoMG getDadosRetorno() {
		return dadosRetorno;
	}

	@Override
	public void setDadosRetorno(DadosRetorno dadosRetorno) {
		this.dadosRetorno = (DadosRetornoMG) dadosRetorno;
	}

	@Override
	public void setAitPagamento(AitPagamento aitPagamento) {
		this.aitPagamento = aitPagamento;		
	}

	@Override
	public AitPagamento getAitPagamento() {
		return this.aitPagamento;
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
