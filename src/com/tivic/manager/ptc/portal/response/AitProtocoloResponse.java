package com.tivic.manager.ptc.portal.response;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitProtocoloResponse {
	private int cdAit;
	private int cdUsuario;
	private String idAit;
	private int tpStatus;
	private String nrPlaca;
	private String nrRenavan;
	private String nmProprietario;
	private String nrCpfCnpjProprietario;
	private String nrTelefone;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String nrCpfCnpjCondutor;
	private String tpCategoriaCnh;
	private String nrTelefone1;
	private String nrCep;
	private String dsLogradouro;
	private String dsNrImovel;
	private String nmComplemento;
	private String nmBairro;
	private String nmCidade;
	private String ufCidade;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazoDefesa;
	private boolean solicitacaoFiciDisponivel;
	private boolean solicitacaoDefesaPreviaDisponivel;
	private boolean solicitacaoJariDisponivel;
	private boolean solicitacaoCetranDisponivel;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public String getNrRenavan() {
		return nrRenavan;
	}
	public void setNrRenavan(String nrRenavan) {
		this.nrRenavan = nrRenavan;
	}
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	public String getNrCpfCnpjProprietario() {
		return nrCpfCnpjProprietario;
	}
	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.nrCpfCnpjProprietario = nrCpfCnpjProprietario;
	}
	public String getNrTelefone() {
		return nrTelefone;
	}
	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
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
	public String getNrCpfCnpjCondutor() {
		return nrCpfCnpjCondutor;
	}
	public void setNrCpfCnpjCondutor(String nrCpfCnpjCondutor) {
		this.nrCpfCnpjCondutor = nrCpfCnpjCondutor;
	}
	public String getTpCategoriaCnh() {
		return tpCategoriaCnh;
	}
	public void setTpCategoriaCnh(String tpCategoriaCnh) {
		this.tpCategoriaCnh = tpCategoriaCnh;
	}
	public String getNrTelefone1() {
		return nrTelefone1;
	}
	public void setNrTelefone1(String nrTelefone1) {
		this.nrTelefone1 = nrTelefone1;
	}
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}
	public String getDsLogradouro() {
		return dsLogradouro;
	}
	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}
	public String getDsNrImovel() {
		return dsNrImovel;
	}
	public void setDsNrImovel(String dsNrImovel) {
		this.dsNrImovel = dsNrImovel;
	}
	public String getNmComplemento() {
		return nmComplemento;
	}
	public void setNmComplemento(String nmComplemento) {
		this.nmComplemento = nmComplemento;
	}
	public String getNmBairro() {
		return nmBairro;
	}
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}
	public String getNmCidade() {
		return nmCidade;
	}
	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}
	public String getUfCidade() {
		return ufCidade;
	}
	public void setUfCidade(String ufCidade) {
		this.ufCidade = ufCidade;
	}
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	public boolean isSolicitacaoFiciDisponivel() {
		return solicitacaoFiciDisponivel;
	}
	public void setSolicitacaoFiciDisponivel(boolean solicitacaoFiciDisponivel) {
		this.solicitacaoFiciDisponivel = solicitacaoFiciDisponivel;
	}
	public boolean isSolicitacaoDefesaPreviaDisponivel() {
		return solicitacaoDefesaPreviaDisponivel;
	}
	public void setSolicitacaoDefesaPreviaDisponivel(boolean solicitacaoDefesaPreviaDisponivel) {
		this.solicitacaoDefesaPreviaDisponivel = solicitacaoDefesaPreviaDisponivel;
	}
	public boolean isSolicitacaoJariDisponivel() {
		return solicitacaoJariDisponivel;
	}
	public void setSolicitacaoJariDisponivel(boolean solicitacaoJariDisponivel) {
		this.solicitacaoJariDisponivel = solicitacaoJariDisponivel;
	}
	public boolean isSolicitacaoCetranDisponivel() {
		return solicitacaoCetranDisponivel;
	}
	public void setSolicitacaoCetranDisponivel(boolean solicitacaoCetranDisponivel) {
		this.solicitacaoCetranDisponivel = solicitacaoCetranDisponivel;
	}
	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}
	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	
}
