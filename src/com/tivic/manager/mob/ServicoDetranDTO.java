package com.tivic.manager.mob;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ServicoDetranDTO {
	
	private String nrAit;
	private int codigoRetorno;
	private String mensagemRetorno;
	private byte[] arquivo;
	private String tipoRetorno;
	private GregorianCalendar dataMovimento;
	private String nomeArquivo;
	
	public ServicoDetranDTO() {
		// TODO Auto-generated constructor stub
	}

	public ServicoDetranDTO(String nrAit, int codigoRetorno, String mensagemRetorno, String tipoRetorno, GregorianCalendar dataMovimento, byte[] arquivo, String nomeArquivo) {
		super();
		this.nrAit = nrAit;
		this.codigoRetorno = codigoRetorno;
		this.mensagemRetorno = mensagemRetorno;
		this.tipoRetorno = tipoRetorno;
		this.dataMovimento = dataMovimento;
		this.arquivo = arquivo;
		this.nomeArquivo = nomeArquivo;
	}

	public String getNrAit() {
		return nrAit;
	}

	public void setNrAit(String nrAit) {
		this.nrAit = nrAit;
	}

	public int getCodigoRetorno() {
		return codigoRetorno;
	}

	public void setCodigoRetorno(int codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}

	public String getMensagemRetorno() {
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

	public String getTipoRetorno() {
		return tipoRetorno;
	}
	
	public void setTipoRetorno(String tipoRetorno) {
		this.tipoRetorno = tipoRetorno;
	}
	
	public GregorianCalendar getDataMovimento() {
		return dataMovimento;
	}
	
	public void setDataMovimento(GregorianCalendar dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	
	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	@Override
	public String toString() {
		return "{\"nrAit\": \"" + nrAit + "\""
				+ ", \"codigoRetorno\": " + codigoRetorno
				+ ", \"mensagemRetorno\": \""+ mensagemRetorno + "\""
				+ ", \"tipoRetorno\": \""+ tipoRetorno + "\""
				+ ", \"dataMovimento\": \""+ Util.convCalendarStringIso(dataMovimento) + "\""
				+ ", \"arquivo\": \"" + Base64.encodeBase64String(arquivo) + "\""
				+ ", \"nomeArquivo\": \"" + nomeArquivo + "\"}";
	}
		
}
