package com.tivic.manager.ptc.portal.builders;

import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;

public class CartaoEstacionamentoRequestBuilder {
	
	private CartaoEstacionamentoRequest cartaoEstacionamentoRequest;

	public CartaoEstacionamentoRequestBuilder() {
		this.cartaoEstacionamentoRequest = new CartaoEstacionamentoRequest();
	}
	
	public CartaoEstacionamentoRequestBuilder setTpDocumento(int tpDocumento) {
		this.cartaoEstacionamentoRequest.setTpDocumento(tpDocumento);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setOrigemDocumento(int cdTipoSistema) {
		this.cartaoEstacionamentoRequest.setTpOrigemDocumento(cdTipoSistema);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmRequerente(String nmRequerente) {
		this.cartaoEstacionamentoRequest.setNmRequerente(nmRequerente);
		return this;
	}

	public CartaoEstacionamentoRequestBuilder setNrCpfRequerente(String nrCpfRequerente) {
		this.cartaoEstacionamentoRequest.setNrCpfRequerente(nrCpfRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNrTelefoneRequerente(String nrTelefoneRequerente) {
		this.cartaoEstacionamentoRequest.setNrTelefoneRequerente(nrTelefoneRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNrCelularRequerente(String nrCelularRequerente) {
		this.cartaoEstacionamentoRequest.setNrCelularRequerente(nrCelularRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNrCepRequerente(String nrCepRequerente) {
		this.cartaoEstacionamentoRequest.setNrCepRequerente(nrCepRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmLogradouroRequerente(String nmLogradouroRequerente) {
		this.cartaoEstacionamentoRequest.setNmLogradouroRequerente(nmLogradouroRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmBairroRequerente(String nmBairroRequerente) {
		this.cartaoEstacionamentoRequest.setNmBairroRequerente(nmBairroRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNrEnderecoRequerente(String nrEnderecoRequerente) {
		this.cartaoEstacionamentoRequest.setNrEnderecoRequerente(nrEnderecoRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmComplementoRequerente(String nmComplementoRequerente) {
		this.cartaoEstacionamentoRequest.setNmComplementoRequerente(nmComplementoRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmCidadeRequerente(String nmCidadeRequerente) {
		this.cartaoEstacionamentoRequest.setNmCidadeRequerente(nmCidadeRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmUfRequerente(String nmUfRequerente) {
		this.cartaoEstacionamentoRequest.setNmUfRequerente(nmUfRequerente);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setCdCidade(int cdCidade) {
		this.cartaoEstacionamentoRequest.setCdCidade(cdCidade);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setArquivo(List<Arquivo> arquivo) {
		this.cartaoEstacionamentoRequest.setArquivo(arquivo);
		return this;
	}
	
	public CartaoEstacionamentoRequestBuilder setNmEmail(String nmEmail) {
		this.cartaoEstacionamentoRequest.setNmEmail(nmEmail);
		return this;
	}
	
	public CartaoEstacionamentoRequest build() {
		return this.cartaoEstacionamentoRequest;
	}
}
