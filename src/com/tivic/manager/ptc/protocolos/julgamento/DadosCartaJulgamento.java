package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DadosCartaJulgamento extends LoteImpressaoAit  {
	private int cdAit;
	private String idAit;
	private String nrPlaca;
	private String sgUfVeiculo;
	private String nmMunicipio;
	private String nrRenavan;
	private String nmProprietario;
	private String nrCpfCnpjProprietario;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private String dsLogradouro;
	private String dsNrImovel;
	private String nmBairro;
	private String nrCep;
	private String nmUf;
	private String cidadeProprietario;	
	private int cdLoteImpressao;
	private int tpStatus;
	private String defesaPreviaDeferida;
	private String jariDeferida;
	private String jariIndeferida;
	private String nrProcesso;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPublicacaoDo;
	private String nrDocumento;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public String getSgUfVeiculo() {
		return sgUfVeiculo;
	}
	public void setSgUfVeiculo(String sgUfVeiculo) {
		this.sgUfVeiculo = sgUfVeiculo;
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
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
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
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
	public String getNmBairro() {
		return nmBairro;
	}
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}
	public String getNmUf() {
		return nmUf;
	}
	public void setNmUf(String nmUf) {
		this.nmUf = nmUf;
	}
	public String getCidadeProprietario() {
		return cidadeProprietario;
	}
	public void setCidadeProprietario(String cidadeProprietario) {
		this.cidadeProprietario = cidadeProprietario;
	}
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}
	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	public String getDefesaPreviaDeferida() {
		return defesaPreviaDeferida;
	}
	public void setDefesaPreviaDeferida(String defesaPreviaDeferida) {
		this.defesaPreviaDeferida = defesaPreviaDeferida;
	}
	public String getJariDeferida() {
		return jariDeferida;
	}
	public void setJariDeferida(String jariDeferida) {
		this.jariDeferida = jariDeferida;
	}
	public String getJariIndeferida() {
		return jariIndeferida;
	}
	public void setJariIndeferida(String jariIndeferida) {
		this.jariIndeferida = jariIndeferida;
	}
	public String getNrProcesso() {
		return nrProcesso;
	}
	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
	}
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
	public GregorianCalendar getDtPublicacaoDo() {
		return dtPublicacaoDo;
	}
	public void setDtPublicacaoDo(GregorianCalendar dtPublicacaoDo) {
		this.dtPublicacaoDo = dtPublicacaoDo;
	}	
	public String getNrDocumento() {
		return nrDocumento;
	}
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
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
