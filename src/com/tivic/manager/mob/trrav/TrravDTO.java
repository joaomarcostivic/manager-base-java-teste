package com.tivic.manager.mob.trrav;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class TrravDTO {

	private int cdTrrav;
	private int nrTrrav;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	private int cdAgente;
	private int cdCidade;
	private String dsObservacao;
	private String dsLocalOcorrencia;
	private String dsPontoReferencia;
	private int cdVeiculo;
	private String nrPlaca;
	private int tpDocumento;
	private String nrDocumento;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String nmProprietario;
	private int cdLocalRemocao;
	private int cdMotivoRemocao;
	private String txtObjetos;
	private int cdBoat;
	private int cdTipoRemocao;
	private String nmRecebedor;
	private String rgRecebedor;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtRecebimento;
	private int cdCidadeCondutor;
	private int cdCidadeProprietario;
	private String ufCondutor;
	private String ufProprietario;
	private String enderecoCondutor;
	private String enderecoProprietario;
	private String bairroCondutor;
	private String bairroProprietario;
	private String nrCnhProprietario;
	private int cdCategoriaCnhProprietario;
	private int lgCnhVencidaProprietario;
	private int cdCategoriaCnhCondutor;
	private int lgCnhVencidaCondutor;
	private String dsMotivoRecolhimentoDocumentos;
	private String nmPatioDestino;
	private int cdMeioRemocao;
	private String dsMotivoRecolhimento;
	private String nmTipoRemocao;
	private int stTipoRemocao;
	private String nmAgente;
	private String nmMeioRemocao;
	private String nmCidadeProprietario;
	private String nmCidade;
	private String nmMotivoRemocao;
	private String nmCategoria;
	private String nmLocalRemocao;
	private String nmCidadeRemocao;
	private String sgEstadoRemocao;
	private String sgEstadoProprietario;
	private String nmTipoDocumentacao;
	private byte[] trrav;
	
	public int getCdTrrav() {
		return cdTrrav;
	}
	public void setCdTrrav(int cdTrrav) {
		this.cdTrrav = cdTrrav;
	}
	public int getNrTrrav() {
		return nrTrrav;
	}
	public void setNrTrrav(int nrTrrav) {
		this.nrTrrav = nrTrrav;
	}
	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}
	public int getCdAgente() {
		return cdAgente;
	}
	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}
	public int getCdCidade() {
		return cdCidade;
	}
	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}
	public String getDsObservacao() {
		return dsObservacao;
	}
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	public String getDsLocalOcorrencia() {
		return dsLocalOcorrencia;
	}
	public void setDsLocalOcorrencia(String dsLocalOcorrencia) {
		this.dsLocalOcorrencia = dsLocalOcorrencia;
	}
	public String getDsPontoReferencia() {
		return dsPontoReferencia;
	}
	public void setDsPontoReferencia(String dsPontoReferencia) {
		this.dsPontoReferencia = dsPontoReferencia;
	}
	public int getCdVeiculo() {
		return cdVeiculo;
	}
	public void setCdVeiculo(int cdVeiculo) {
		this.cdVeiculo = cdVeiculo;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	public String getNrDocumento() {
		return nrDocumento;
	}
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	public String getNmCondutor() {
		return nmCondutor;
	}
	public void setNmCondutor(String nmCondutor) {
		this.nmCondutor = nmCondutor;
	}
	public String getNrCnhCondutor() {
		return nrCnhCondutor;
	}
	public void setNrCnhCondutor(String nrCnhCondutor) {
		this.nrCnhCondutor = nrCnhCondutor;
	}
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	public int getCdLocalRemocao() {
		return cdLocalRemocao;
	}
	public void setCdLocalRemocao(int cdLocalRemocao) {
		this.cdLocalRemocao = cdLocalRemocao;
	}
	public int getCdMotivoRemocao() {
		return cdMotivoRemocao;
	}
	public void setCdMotivoRemocao(int cdMotivoRemocao) {
		this.cdMotivoRemocao = cdMotivoRemocao;
	}
	public String getTxtObjetos() {
		return txtObjetos;
	}
	public void setTxtObjetos(String txtObjetos) {
		this.txtObjetos = txtObjetos;
	}
	public int getCdBoat() {
		return cdBoat;
	}
	public void setCdBoat(int cdBoat) {
		this.cdBoat = cdBoat;
	}
	public int getCdTipoRemocao() {
		return cdTipoRemocao;
	}
	public void setCdTipoRemocao(int cdTipoRemocao) {
		this.cdTipoRemocao = cdTipoRemocao;
	}
	public String getNmRecebedor() {
		return nmRecebedor;
	}
	public void setNmRecebedor(String nmRecebedor) {
		this.nmRecebedor = nmRecebedor;
	}
	public String getRgRecebedor() {
		return rgRecebedor;
	}
	public void setRgRecebedor(String rgRecebedor) {
		this.rgRecebedor = rgRecebedor;
	}
	public GregorianCalendar getDtRecebimento() {
		return dtRecebimento;
	}
	public void setDtRecebimento(GregorianCalendar dtRecebimento) {
		this.dtRecebimento = dtRecebimento;
	}
	public int getCdCidadeCondutor() {
		return cdCidadeCondutor;
	}
	public void setCdCidadeCondutor(int cdCidadeCondutor) {
		this.cdCidadeCondutor = cdCidadeCondutor;
	}
	public int getCdCidadeProprietario() {
		return cdCidadeProprietario;
	}
	public void setCdCidadeProprietario(int cdCidadeProprietario) {
		this.cdCidadeProprietario = cdCidadeProprietario;
	}
	public String getUfCondutor() {
		return ufCondutor;
	}
	public void setUfCondutor(String ufCondutor) {
		this.ufCondutor = ufCondutor;
	}
	public String getUfProprietario() {
		return ufProprietario;
	}
	public void setUfProprietario(String ufProprietario) {
		this.ufProprietario = ufProprietario;
	}
	public String getEnderecoCondutor() {
		return enderecoCondutor;
	}
	public void setEnderecoCondutor(String enderecoCondutor) {
		this.enderecoCondutor = enderecoCondutor;
	}
	public String getEnderecoProprietario() {
		return enderecoProprietario;
	}
	public void setEnderecoProprietario(String enderecoProprietario) {
		this.enderecoProprietario = enderecoProprietario;
	}
	public String getBairroCondutor() {
		return bairroCondutor;
	}
	public void setBairroCondutor(String bairroCondutor) {
		this.bairroCondutor = bairroCondutor;
	}
	public String getBairroProprietario() {
		return bairroProprietario;
	}
	public void setBairroProprietario(String bairroProprietario) {
		this.bairroProprietario = bairroProprietario;
	}
	public String getNrCnhProrietario() {
		return nrCnhProprietario;
	}
	public void setNrCnhProrietario(String nrCnhProrietario) {
		this.nrCnhProprietario = nrCnhProrietario;
	}
	public int getCdCategoriaCnhProprietario() {
		return cdCategoriaCnhProprietario;
	}
	public void setCdCategoriaCnhProprietario(int cdCategoriaCnhProprietario) {
		this.cdCategoriaCnhProprietario = cdCategoriaCnhProprietario;
	}
	public int getLgCnhVencidaProprietario() {
		return lgCnhVencidaProprietario;
	}
	public void setLgCnhVencidaProprietario(int lgCnhVencidaProprietario) {
		this.lgCnhVencidaProprietario = lgCnhVencidaProprietario;
	}
	public int getCdCategoriaCnhCondutor() {
		return cdCategoriaCnhCondutor;
	}
	public void setCdCategoriaCnhCondutor(int cdCategoriaCnhCondutor) {
		this.cdCategoriaCnhCondutor = cdCategoriaCnhCondutor;
	}
	public int getLgCnhVencidaCondutor() {
		return lgCnhVencidaCondutor;
	}
	public void setLgCnhVencidaCondutor(int lgCnhVencidaCondutor) {
		this.lgCnhVencidaCondutor = lgCnhVencidaCondutor;
	}
	public String getDsMotivoRecolhimentoDocumentos() {
		return dsMotivoRecolhimentoDocumentos;
	}
	public void setDsMotivoRecolhimentoDocumentos(String dsMotivoRecolhimentoDocumentos) {
		this.dsMotivoRecolhimentoDocumentos = dsMotivoRecolhimentoDocumentos;
	}
	public String getNmPatioDestino() {
		return nmPatioDestino;
	}
	public void setNmPatioDestino(String nmPatioDestino) {
		this.nmPatioDestino = nmPatioDestino;
	}
	public int getCdMeioRemocao() {
		return cdMeioRemocao;
	}
	public void setCdMeioRemocao(int cdMeioRemocao) {
		this.cdMeioRemocao = cdMeioRemocao;
	}
	public String getDsMotivoRecolhimento() {
		return dsMotivoRecolhimento;
	}
	public void setDsMotivoRecolhimento(String dsMotivoRecolhimento) {
		this.dsMotivoRecolhimento = dsMotivoRecolhimento;
	}
	public String getNmTipoRemocao() {
		return nmTipoRemocao;
	}
	public void setNmTipoRemocao(String nmTipoRemocao) {
		this.nmTipoRemocao = nmTipoRemocao;
	}
	public int getStTipoRemocao() {
		return stTipoRemocao;
	}
	public void setStTipoRemocao(int stTipoRemocao) {
		this.stTipoRemocao = stTipoRemocao;
	}
	public String getNmAgente() {
		return nmAgente;
	}
	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
	public String getNmMeioRemocao() {
		return nmMeioRemocao;
	}
	public void setNmMeioRemocao(String nmMeioRemocao) {
		this.nmMeioRemocao = nmMeioRemocao;
	}
	public TrravDTO() {}
	
	public String getNmCidadeProprietario() {
		return nmCidadeProprietario;
	}
	public void setNmCidadeProprietario(String nmCidadeProprietario) {
		this.nmCidadeProprietario = nmCidadeProprietario;
	}
	public String getNmCidade() {
		return nmCidade;
	}
	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}
	public String getNmMotivoRemocao() {
		return nmMotivoRemocao;
	}
	public void setNmMotivoRemocao(String nmMotivoRemocao) {
		this.nmMotivoRemocao = nmMotivoRemocao;
	}
	public String getNmCategoria() {
		return nmCategoria;
	}
	public void setNmCategoria(String nmCategoria) {
		this.nmCategoria = nmCategoria;
	}
	public String getNmLocalRemocao() {
		return nmLocalRemocao;
	}
	public void setNmLocalRemocao(String nmLocalRemocao) {
		this.nmLocalRemocao = nmLocalRemocao;
	}
	public String getNmCidadeRemocao() {
		return nmCidadeRemocao;
	}
	public void setNmCidadeRemocao(String nmCidadeRemocao) {
		this.nmCidadeRemocao = nmCidadeRemocao;
	}
	public String getSgEstadoRemocao() {
		return sgEstadoRemocao;
	}
	public void setSgEstadoRemocao(String sgEstadoRemocao) {
		this.sgEstadoRemocao = sgEstadoRemocao;
	}
	public byte[] getTrrav() {
		return trrav;
	}
	public void setTrrav(byte[] trrav) {
		this.trrav = trrav;
	}
	public String getSgEstadoProprietario() {
		return sgEstadoProprietario;
	}
	public void setSgEstadoProprietario(String sgEstadoProprietario) {
		this.sgEstadoProprietario = sgEstadoProprietario;
	}
	public String getNmTipoDocumentacao() {
		return nmTipoDocumentacao;
	}
	public void setNmTipoDocumentacao(String nmTipoDocumentacao) {
		this.nmTipoDocumentacao = nmTipoDocumentacao;
	}
}
