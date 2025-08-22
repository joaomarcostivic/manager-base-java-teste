package com.tivic.manager.wsdl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.DadosInfratorConsultaEntrada;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.DadosCondutorDocumentoEntrada;

public class AitDetranObject {

	private Ait ait;
	private AitMovimento aitMovimento;
	private ArquivoMovimento arquivoMovimento;
	private AitPagamento aitPagamento;
	private String nrPlaca;
	private int posicaoArquivo;
	private Documento documento;
	private DadosCondutorDocumentoEntrada dadosCondutorDocumentoEntrada;
	private DadosInfratorConsultaEntrada dadosInfratorConsultaEntrada;
	
	public AitDetranObject() {
		// TODO Auto-generated constructor stub
	}

	public AitDetranObject(Ait ait) {
		super();
		this.ait = ait;
	}
	
	public AitDetranObject(AitMovimento aitMovimento) {
		super();
		this.aitMovimento = aitMovimento;
	}
	
	public AitDetranObject(Ait ait, AitMovimento aitMovimento) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
	}

	public AitDetranObject(Ait ait, AitMovimento aitMovimento, int posicaoArquivo) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.posicaoArquivo = posicaoArquivo;
	}
	
	public AitDetranObject(Ait ait, AitMovimento aitMovimento, ArquivoMovimento arquivoMovimento) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.arquivoMovimento = arquivoMovimento;
	}

	public AitDetranObject(Ait ait, AitMovimento aitMovimento, ArquivoMovimento arquivoMovimento, int posicaoArquivo) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.arquivoMovimento = arquivoMovimento;
		this.posicaoArquivo = posicaoArquivo;
	}
	
	public AitDetranObject(String nrPlaca) {
		super();
		this.nrPlaca = nrPlaca;
	}
	
	public Ait getAit() {
		return ait;
	}

	public void setAit(Ait ait) {
		this.ait = ait;
	}
	
	public AitMovimento getAitMovimento() {
		return aitMovimento;
	}
	
	public void setAitMovimento(AitMovimento aitMovimento) {
		this.aitMovimento = aitMovimento;
	}
	
	public ArquivoMovimento getArquivoMovimento() {
		return arquivoMovimento;
	}
	
	public void setArquivoMovimento(ArquivoMovimento arquivoMovimento) {
		this.arquivoMovimento = arquivoMovimento;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	public void setPosicaoArquivo(int posicaoArquivo) {
		this.posicaoArquivo = posicaoArquivo;
	}
	
	public int getPosicaoArquivo() {
		return posicaoArquivo;
	}

	public AitPagamento getAitPagamento() {
		return aitPagamento;
	}

	public void setAitPagamento(AitPagamento aitPagamento) {
		this.aitPagamento = aitPagamento;
	}
	
	public Documento getDocumento() {
		return documento;
	}
	
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public DadosCondutorDocumentoEntrada getDadosCondutorDocumentoEntrada() {
		return dadosCondutorDocumentoEntrada;
	}

	public void setDadosCondutorDocumentoEntrada(DadosCondutorDocumentoEntrada dadosCondutorDocumentoEntrada) {
		this.dadosCondutorDocumentoEntrada = dadosCondutorDocumentoEntrada;
	}

	public DadosInfratorConsultaEntrada getDadosInfratorConsultaEntrada() {
		return dadosInfratorConsultaEntrada;
	}
	
	public void setDadosInfratorConsultaEntrada(DadosInfratorConsultaEntrada dadosInfratorConsultaEntrada) {
		this.dadosInfratorConsultaEntrada = dadosInfratorConsultaEntrada;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
