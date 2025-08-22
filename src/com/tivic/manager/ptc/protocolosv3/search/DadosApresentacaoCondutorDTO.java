package com.tivic.manager.ptc.protocolosv3.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DadosApresentacaoCondutorDTO extends DadosProtocoloDTO{
	
	private int cdApresentacaoCondutor;
	private int cdDocumento;
	private String nrCnh;
	private String ufCnh;
	private String tpCategoriaCnh;
	private String nmCondutor;
	private String dsEndereco;
	private String dsComplementoEndereco;
	private String nmCidade;
	private String nrTelefone1;
	private String nrTelefone2;
	private String nrCpfCnpj;
	private String nrRg;
	private int tpModeloCnh;
	private String idPaisCnh;
	private String sgOrgaoRg;
	private int cdEstadoRg;
	private int tpDocumento;

	public DadosApresentacaoCondutorDTO() { }

	public DadosApresentacaoCondutorDTO(int cdApresentacaoCondutor,
			int cdDocumento,
			String nrCnh,
			String ufCnh,
			String tpCategoriaCnh,
			String nmCondutor,
			String dsEndereco,
			String dsComplementoEndereco,
			String nmCidade,
			String nrTelefone1,
			String nrTelefone2,
			String nrCpfCnpj,
			String nrRg,
			int tpModeloCnh,
			String idPaisCnh,
			String sgOrgaoRg,
			int cdEstadoRg) {
		setCdApresentacaoCondutor(cdApresentacaoCondutor);
		setCdDocumento(cdDocumento);
		setNrCnh(nrCnh);
		setUfCnh(ufCnh);
		setTpCategoriaCnh(tpCategoriaCnh);
		setNmCondutor(nmCondutor);
		setDsEndereco(dsEndereco);
		setDsComplementoEndereco(dsComplementoEndereco);
		setNmCidade(nmCidade);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCpfCnpj(nrCpfCnpj);
		setNrRg(nrRg);
		setTpModeloCnh(tpModeloCnh);
		setIdPaisCnh(idPaisCnh);
		setSgOrgaoRg(sgOrgaoRg);
		setCdEstadoRg(cdEstadoRg);
	}
	public void setCdApresentacaoCondutor(int cdApresentacaoCondutor){
		this.cdApresentacaoCondutor=cdApresentacaoCondutor;
	}
	public int getCdApresentacaoCondutor(){
		return this.cdApresentacaoCondutor;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setNrCnh(String nrCnh){
		this.nrCnh=nrCnh;
	}
	public String getNrCnh(){
		return this.nrCnh;
	}
	public void setUfCnh(String ufCnh){
		this.ufCnh=ufCnh;
	}
	public String getUfCnh(){
		return this.ufCnh;
	}
	public void setTpCategoriaCnh(String tpCategoriaCnh){
		this.tpCategoriaCnh=tpCategoriaCnh;
	}
	public String getTpCategoriaCnh(){
		return this.tpCategoriaCnh;
	}
	public void setNmCondutor(String nmCondutor){
		this.nmCondutor=nmCondutor;
	}
	public String getNmCondutor(){
		return this.nmCondutor;
	}
	public void setDsEndereco(String dsEndereco){
		this.dsEndereco=dsEndereco;
	}
	public String getDsEndereco(){
		return this.dsEndereco;
	}
	public void setDsComplementoEndereco(String dsComplementoEndereco){
		this.dsComplementoEndereco=dsComplementoEndereco;
	}
	public String getDsComplementoEndereco(){
		return this.dsComplementoEndereco;
	}
	public void setNmCidade(String nmCidade){
		this.nmCidade=nmCidade;
	}
	public String getNmCidade(){
		return this.nmCidade;
	}
	public void setNrTelefone1(String nrTelefone1){
		this.nrTelefone1=nrTelefone1;
	}
	public String getNrTelefone1(){
		return this.nrTelefone1;
	}
	public void setNrTelefone2(String nrTelefone2){
		this.nrTelefone2=nrTelefone2;
	}
	public String getNrTelefone2(){
		return this.nrTelefone2;
	}
	public void setNrCpfCnpj(String nrCpfCnpj){
		this.nrCpfCnpj=nrCpfCnpj;
	}
	public String getNrCpfCnpj(){
		return this.nrCpfCnpj;
	}
	public void setNrRg(String nrRg){
		this.nrRg=nrRg;
	}
	public String getNrRg(){
		return this.nrRg;
	}
	public void setTpModeloCnh(int tpModeloCnh){
		this.tpModeloCnh=tpModeloCnh;
	}
	public int getTpModeloCnh(){
		return this.tpModeloCnh;
	}
	public void setIdPaisCnh(String idPaisCnh){
		this.idPaisCnh=idPaisCnh;
	}
	public String getIdPaisCnh(){
		return this.idPaisCnh;
	}
	public void setSgOrgaoRg(String sgOrgaoRg){
		this.sgOrgaoRg=sgOrgaoRg;
	}
	public String getSgOrgaoRg(){
		return this.sgOrgaoRg;
	}
	public void setCdEstadoRg(int cdEstadoRg){
		this.cdEstadoRg=cdEstadoRg;
	}
	public int getCdEstadoRg(){
		return this.cdEstadoRg;
	}
	
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
}
