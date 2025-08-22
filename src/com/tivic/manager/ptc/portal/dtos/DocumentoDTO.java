package com.tivic.manager.ptc.portal.dtos;

import java.util.GregorianCalendar;

public class DocumentoDTO {
	
	private int cdDocumento;
	private int cdTipoDocumento;
	private String nrDocumento;
	private int cdSituacaoDocumento;
	private GregorianCalendar dtProtocolo;
	private String nmTipoDocumento;
	private String nmSituacaoDocumento;
	
	public int getCdDocumento() {
		return cdDocumento;
	}
	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}
	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}
	public String getNrDocumento() {
		return nrDocumento;
	}
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}
	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}
	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}
	public String getNmTipoDocumento() {
		return nmTipoDocumento;
	}
	public void setNmTipoDocumento(String nmTipoDocumento) {
		this.nmTipoDocumento = nmTipoDocumento;
	}
	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}
	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}
	
}
