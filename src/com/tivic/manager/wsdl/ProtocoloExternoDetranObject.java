package com.tivic.manager.wsdl;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.ptc.Documento;

public class ProtocoloExternoDetranObject {
	
	private Ait ait;
	private AitMovimento aitMovimento;
	private ArquivoMovimento arquivoMovimento;
	private AitPagamento aitPagamento;
	private Documento documento;
	private String nrPlaca;
	private int posicaoArquivo;
	
	public ProtocoloExternoDetranObject() {
		// TODO Auto-generated constructor stub
	}

	public ProtocoloExternoDetranObject(Ait ait) {
		super();
		this.ait = ait;
	}
	
	public ProtocoloExternoDetranObject(AitMovimento aitMovimento) {
		super();
		this.aitMovimento = aitMovimento;
	}
	
	public ProtocoloExternoDetranObject(Ait ait, AitMovimento aitMovimento) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
	}
	

	public ProtocoloExternoDetranObject(Ait ait, AitMovimento aitMovimento, int posicaoArquivo) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.posicaoArquivo = posicaoArquivo;
	}
	
	public ProtocoloExternoDetranObject(Ait ait, AitMovimento aitMovimento, ArquivoMovimento arquivoMovimento) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.arquivoMovimento = arquivoMovimento;
	}

	public ProtocoloExternoDetranObject(Ait ait, AitMovimento aitMovimento, ArquivoMovimento arquivoMovimento, int posicaoArquivo) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.arquivoMovimento = arquivoMovimento;
		this.posicaoArquivo = posicaoArquivo;
	}
	
	public ProtocoloExternoDetranObject(Ait ait, AitMovimento aitMovimento, Documento documento) {
		super();
		this.ait = ait;
		this.aitMovimento = aitMovimento;
		this.documento = documento;
	}
	
	public ProtocoloExternoDetranObject(String nrPlaca) {
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

}
