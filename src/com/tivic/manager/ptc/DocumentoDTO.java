package com.tivic.manager.ptc;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.mob.Cartao;
import com.tivic.manager.mob.Concessao;

public class DocumentoDTO {
	private Documento documento;
	private Pessoa pessoa;
	private Cartao cartao;
	private DocumentoTramitacao documentoTramitacao;
	private SituacaoDocumento situacaoDocumento;
	
	public DocumentoDTO() {
		super();
	}

	public DocumentoDTO(Documento documento, Pessoa pessoa, Cartao cartao,
			DocumentoTramitacao documentoTramitacao, SituacaoDocumento situacaoDocumento) {
		super();
		this.documento = documento;
		this.pessoa = pessoa;
		this.cartao = cartao;
		this.documentoTramitacao = documentoTramitacao;
		this.situacaoDocumento = situacaoDocumento;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public DocumentoTramitacao getDocumentoTramitacao() {
		return documentoTramitacao;
	}

	public void setDocumentoTramitacao(DocumentoTramitacao documentoTramitacao) {
		this.documentoTramitacao = documentoTramitacao;
	}

	public SituacaoDocumento getSituacaoDocumento() {
		return situacaoDocumento;
	}

	public void setSituacaoDocumento(SituacaoDocumento situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
	}
	
}
