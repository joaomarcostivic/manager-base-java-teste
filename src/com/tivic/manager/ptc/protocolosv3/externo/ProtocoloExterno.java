package com.tivic.manager.ptc.protocolosv3.externo;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ProtocoloExterno {

	private int cdDocumentoExterno;
	private int cdDocumento;
	private String idAit;
	private String nrPlaca;
	private int cdInfracao;
	private String nrRenainf;
	private GregorianCalendar dtEntrada;
	private int cdOrgaoExterno;
	private String nmCondutor;

	public ProtocoloExterno() { }

	public ProtocoloExterno(int cdDocumentoExterno,
			int cdDocumento,
			String idAit,
			String nrPlaca,
			int cdInfracao,
			String nrRenainf,
			GregorianCalendar dtEntrada,
			int cdOrgaoExterno,
			String nmCondutor) {
		setCdDocumentoExterno(cdDocumentoExterno);
		setCdDocumento(cdDocumento);
		setIdAit(idAit);
		setNrPlaca(nrPlaca);
		setCdInfracao(cdInfracao);
		setNrRenainf(nrRenainf);
		setDtEntrada(dtEntrada);
		setCdOrgaoExterno(cdOrgaoExterno);
		setNmCondutor(nmCondutor);
	}
	public void setCdDocumentoExterno(int cdDocumentoExterno){
		this.cdDocumentoExterno=cdDocumentoExterno;
	}
	public int getCdDocumentoExterno(){
		return this.cdDocumentoExterno;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setIdAit(String idAit){
		this.idAit=idAit;
	}
	public String getIdAit(){
		return this.idAit;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setNrRenainf(String nrRenainf){
		this.nrRenainf=nrRenainf;
	}
	public String getNrRenainf(){
		return this.nrRenainf;
	}
	public void setDtEntrada(GregorianCalendar dtEntrada){
		this.dtEntrada=dtEntrada;
	}
	public GregorianCalendar getDtEntrada(){
		return this.dtEntrada;
	}
	public int getCdOrgaoExterno() {
		return cdOrgaoExterno;
	}
	public void setCdOrgaoExterno(int cdOrgaoExterno) {
		this.cdOrgaoExterno = cdOrgaoExterno;
	}
	public String getNmCondutor() {
		return nmCondutor;
	}
	public void setNmCondutor(String nmCondutor) {
		this.nmCondutor = nmCondutor;
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
