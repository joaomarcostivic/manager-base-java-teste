package com.tivic.manager.ptc.protocolosv3;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProtocoloInsertDTO {
	private int cdAit;
	private int cdTipoDocumento;
	private int cdOcorrencia;
	private String nrDocumento;
	private String nmRequerente;
	private String txtObservacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	private int cdUsuario;
	private List<Arquivo> arquivos;
	private boolean lgGerar;
	private int cdSituacaoDocumento;
	private int tpDocumento;

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}

	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getNmRequerente() {
		return nmRequerente;
	}

	public void setNmRequerente(String nmRequerente) {
		this.nmRequerente = nmRequerente;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}

	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public boolean isLgGerar() {
		return lgGerar;
	}

	public void setLgGerar(boolean lgGerar) {
		this.lgGerar = lgGerar;
	}

	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
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
