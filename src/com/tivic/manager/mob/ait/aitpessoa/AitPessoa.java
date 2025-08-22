package com.tivic.manager.mob.ait.aitpessoa;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.ait.aitimagempessoa.AitImagemPessoa;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AitPessoa {
	
	private int cdInfracao;
	private int cdAitPessoa;
	private int cdAgente;
	private int cdOcorrencia;
	private int cdTalao;
	private int cdUsuario;
	private int cdEquipamento;
	private int stAit;
	private int tpPessoa;
	private int nrAit;
	private double vlLongitude;
	private double vlLatitude;
	private String nmPessoa;
	private String idAit;
	private String nrCpfCnpjPessoa;
	private String txtObservacao;
	private String dsObservacao;
	private String dsLocalInfracao;
	private String dsPontoReferencia;
	private long dtInfracao;
	private long dtDigitacao;
	private ArrayList<AitImagemPessoa> imagens;

	public int getCdAitPessoa() {
		return cdAitPessoa;
	}

	public void setCdAitPessoa(int cdAitPessoa) {
		this.cdAitPessoa = cdAitPessoa;
	}

	public int getCdAgente() {
		return cdAgente;
	}

	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public int getCdTalao() {
		return cdTalao;
	}

	public void setCdTalao(int cdTalao) {
		this.cdTalao = cdTalao;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public int getCdEquipamento() {
		return cdEquipamento;
	}

	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}

	public int getStAit() {
		return stAit;
	}

	public void setStAit(int stAit) {
		this.stAit = stAit;
	}

	public int getTpPessoa() {
		return tpPessoa;
	}

	public void setTpPessoa(int tpPessoa) {
		this.tpPessoa = tpPessoa;
	}

	public int getNrAit() {
		return nrAit;
	}

	public void setNrAit(int nrAit) {
		this.nrAit = nrAit;
	}

	public double getVlLongitude() {
		return vlLongitude;
	}

	public void setVlLongitude(double vlLongitude) {
		this.vlLongitude = vlLongitude;
	}

	public double getVlLatitude() {
		return vlLatitude;
	}

	public void setVlLatitude(double vlLatitude) {
		this.vlLatitude = vlLatitude;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getNrCpfCnpjPessoa() {
		return nrCpfCnpjPessoa;
	}

	public void setNrCpfCnpjPessoa(String nrCpfCnpjPessoa) {
		this.nrCpfCnpjPessoa = nrCpfCnpjPessoa;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public String getDsLocalInfracao() {
		return dsLocalInfracao;
	}

	public void setDsLocalInfracao(String dsLocalInfracao) {
		this.dsLocalInfracao = dsLocalInfracao;
	}

	public String getDsPontoReferencia() {
		return dsPontoReferencia;
	}

	public void setDsPontoReferencia(String dsPontoReferencia) {
		this.dsPontoReferencia = dsPontoReferencia;
	}

	public long getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(long dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public long getDtDigitacao() {
		return dtDigitacao;
	}

	public void setDtDigitacao(long dtDigitacao) {
		this.dtDigitacao = dtDigitacao;
	}

	public ArrayList<AitImagemPessoa> getImagens() {
		return imagens;
	}

	public void setImagens(ArrayList<AitImagemPessoa> imagens) {
		this.imagens = imagens;
	}

	public int getCdInfracao() {
		return cdInfracao;
	}

	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AitPessoa aitPessoa = (AitPessoa) o;
		return java.util.Objects.equals(idAit, aitPessoa.idAit);
	}

}
