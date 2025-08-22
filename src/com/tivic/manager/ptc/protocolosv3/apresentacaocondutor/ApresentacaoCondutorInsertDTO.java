package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;

public class ApresentacaoCondutorInsertDTO extends ProtocoloInsertDTO{
	private int cdDocumento;
	private String nrCnh;
	private String ufCnh;
	private Integer tpCategoriaCnh;
	private String nmCondutor;
	private String nrTelefone1;
	private String nrTelefone2;
	private String nrCpfCnpj;
	private int tpModeloCnh;
	private String idPaisCnh;
	
	public ApresentacaoCondutorInsertDTO() { }

	public ApresentacaoCondutorInsertDTO(
			int cdDocumento,
			String nrCnh,
			String ufCnh,
			int tpCategoriaCnh,
			String nmCondutor,
			String nrTelefone1,
			String nrTelefone2,
			String nrCpfCnpj,
			int tpModeloCnh,
			String idPaisCnh,
			String sgOrgaoRg) {
		setCdDocumento(cdDocumento);
		setNrCnh(nrCnh);
		setUfCnh(ufCnh);
		setTpCategoriaCnh(tpCategoriaCnh);
		setNmCondutor(nmCondutor);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCpfCnpj(nrCpfCnpj);
		setTpModeloCnh(tpModeloCnh);
		setIdPaisCnh(idPaisCnh);
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
	public String getUfCnh() {
		return ufCnh;
	}
	public void setUfCnh(String ufCnh) {
		this.ufCnh = ufCnh;
	}
	public String getNrCpfCnpj() {
		return nrCpfCnpj;
	}
	public void setNrCpfCnpj(String nrCpfCnpj) {
		this.nrCpfCnpj = nrCpfCnpj;
	}

	public void setTpCategoriaCnh(Integer tpCategoriaCnh){
		this.tpCategoriaCnh=tpCategoriaCnh;
	}
	public Integer getTpCategoriaCnh(){
		return this.tpCategoriaCnh;
	}
	public void setNmCondutor(String nmCondutor){
		this.nmCondutor=nmCondutor;
	}
	public String getNmCondutor(){
		return this.nmCondutor;
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
	
}
