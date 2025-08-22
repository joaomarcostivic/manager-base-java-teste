package com.tivic.manager.ptc.portal.request;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Arquivo;

public class CartaoEstacionamentoRequest {

	private int tpDocumento;
	private String nmRequerente;
	private String nrCpfRequerente;
	private String nrTelefoneRequerente;
	private String nrCelularRequerente;
	private String nrCepRequerente;
	private String nmLogradouroRequerente;
	private String nmBairroRequerente;
	private String nrEnderecoRequerente;
	private String nmComplementoRequerente;
	private String nmCidadeRequerente;
	private String nmUfRequerente;
	private int cdCidade;
	private List<Arquivo> arquivo;
	private String nmEmail;
	private int tpOrigemDocumento;

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public String getNmRequerente() {
		return nmRequerente;
	}

	public void setNmRequerente(String nmRequerente) {
		this.nmRequerente = nmRequerente;
	}

	public String getNrTelefoneRequerente() {
		return nrTelefoneRequerente;
	}

	public void setNrTelefoneRequerente(String nrTelefoneRequerente) {
		this.nrTelefoneRequerente = nrTelefoneRequerente;
	}

	public String getNrCelularRequerente() {
		return nrCelularRequerente;
	}

	public void setNrCelularRequerente(String nrCelularRequerente) {
		this.nrCelularRequerente = nrCelularRequerente;
	}

	public String getNrCepRequerente() {
		return nrCepRequerente;
	}

	public void setNrCepRequerente(String nrCepRequerente) {
		this.nrCepRequerente = nrCepRequerente;
	}

	public String getNmLogradouroRequerente() {
		return nmLogradouroRequerente;
	}

	public void setNmLogradouroRequerente(String nmLogradouroRequerente) {
		this.nmLogradouroRequerente = nmLogradouroRequerente;
	}

	public String getNmBairroRequerente() {
		return nmBairroRequerente;
	}

	public void setNmBairroRequerente(String nmBairroRequerente) {
		this.nmBairroRequerente = nmBairroRequerente;
	}

	public String getNrEnderecoRequerente() {
		return nrEnderecoRequerente;
	}

	public void setNrEnderecoRequerente(String nrEnderecoRequerente) {
		this.nrEnderecoRequerente = nrEnderecoRequerente;
	}

	public String getNmComplementoRequerente() {
		return nmComplementoRequerente;
	}

	public void setNmComplementoRequerente(String nmComplementoRequerente) {
		this.nmComplementoRequerente = nmComplementoRequerente;
	}

	public String getNmCidadeRequerente() {
		return nmCidadeRequerente;
	}

	public void setNmCidadeRequerente(String nmCidadeRequerente) {
		this.nmCidadeRequerente = nmCidadeRequerente;
	}

	public String getNmUfRequerente() {
		return nmUfRequerente;
	}

	public void setNmUfRequerente(String nmUfRequerente) {
		this.nmUfRequerente = nmUfRequerente;
	}

	public List<Arquivo> getArquivo() {
		return arquivo;
	}

	public void setArquivo(List<Arquivo> arquivo) {
		this.arquivo = arquivo;
	}

	public int getCdCidade() {
		return cdCidade;
	}

	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}

	public String getNmEmail() {
		return nmEmail;
	}

	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}

	public String getNrCpfRequerente() {
		return nrCpfRequerente;
	}

	public void setNrCpfRequerente(String nrCpfRequerente) {
		this.nrCpfRequerente = nrCpfRequerente;
	}
	
	public int getTpOrigemDocumento() {
		return tpOrigemDocumento;
	}

	public void setTpOrigemDocumento(int tpOrigemDocumento) {
		this.tpOrigemDocumento = tpOrigemDocumento;
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
