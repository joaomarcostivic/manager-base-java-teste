package com.tivic.manager.mob.guiapagamento;

import java.awt.image.BufferedImage;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DadosGuiaPagamento {

	private int cdAit;
	private String idAit;
	private String nrRenainf;
	private String nrControle;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEmissao; 
	private String nrPlaca;
	private String sgUfVeiculo;
	private String nmMunicipio;
	private String nrRenavan;
	private String nmModelo;
	private String dsEspecie;
	private String nmProprietario;
	private String nrCpfCnpjProprietario;
	private String dsLocalInfracao;
	private String dsPontoReferencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private int nrCodDetran;
	private String dsInfracao;
	private String nrArtigo;
	private String nrInciso;
	private String nrParagrafo;
	private String nrAlinea;
	private String nmNatureza;
	private int nrPontuacao;
	private Double vlMulta;
	private Double vlVelocidadePermitida;
	private Double vlVelocidadeAferida;
	private Double vlVelocidadePenalidade;
	private String nrMatricula;
	private int cdEquipamento;
	private String marcaEquipamento;
	private String modeloEquipamento;
	private String dsLogradouro;
	private String nmBairro;
	private String nrCep;
	private int tpEquipamento;
	private String dsObservacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAfericao;
	private String nrLacre;
	private String txtCondutor;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	private Double vlMultaComDesconto;
	private String linhaDigitavel;
	private String barCode;
	private String nmUf;
	private String nrInventarioInmetro;
	private int cdLoteImpressao;
	private int lgAutoAssinado; 
	private String cidadeProprietario;
	private String ufProprietario;
	private String estadoProprietario;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	private BufferedImage barCodeImage;
	private int tpStatus;
    private int tpResponsabilidade;
    private String idLoteImpressao;
    private String nmAgente;
    private String nmCategoria;
    private String nmEquipamento;
    private String nmTipoVeiculo;
    private String nmMarcaEquipamento;
    private String nmModeloEquipamento;
    @JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtPrazoDefesa; 
	
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
	public String getNrRenainf() {
		return nrRenainf;
	}
	public void setNrRenainf(String nrRenainf) {
		this.nrRenainf = nrRenainf;
	}
	public String getNrControle() {
		return nrControle;
	}
	public void setNrControle(String nrControle) {
		this.nrControle = nrControle;
	}
	public GregorianCalendar getDtEmissao() {
		return dtEmissao;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao) {
		this.dtEmissao = dtEmissao;
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
	public String getNmModelo() {
		return nmModelo;
	}
	public void setNmModelo(String nmModelo) {
		this.nmModelo = nmModelo;
	}
	public String getDsEspecie() {
		return dsEspecie;
	}
	public void setDsEspecie(String dsEspecie) {
		this.dsEspecie = dsEspecie;
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
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	public int getNrCodDetran() {
		return nrCodDetran;
	}
	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}
	public String getDsInfracao() {
		return dsInfracao;
	}
	public void setDsInfracao(String dsInfracao) {
		this.dsInfracao = dsInfracao;
	}
	public String getNrArtigo() {
		return nrArtigo;
	}
	public void setNrArtigo(String nrArtigo) {
		this.nrArtigo = nrArtigo;
	}
	public String getNrInciso() {
		return nrInciso;
	}
	public void setNrInciso(String nrInciso) {
		this.nrInciso = nrInciso;
	}
	public String getNrParagrafo() {
		return nrParagrafo;
	}
	public void setNrParagrafo(String nrParagrafo) {
		this.nrParagrafo = nrParagrafo;
	}
	public String getNrAlinea() {
		return nrAlinea;
	}
	public void setNrAlinea(String nrAlinea) {
		this.nrAlinea = nrAlinea;
	}
	public String getNmNatureza() {
		return nmNatureza;
	}
	public void setNmNatureza(String nmNatureza) {
		this.nmNatureza = nmNatureza;
	}
	public int getNrPontuacao() {
		return nrPontuacao;
	}
	public void setNrPontuacao(int nrPontuacao) {
		this.nrPontuacao = nrPontuacao;
	}
	public Double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public Double getVlVelocidadePermitida() {
		return vlVelocidadePermitida;
	}
	public void setVlVelocidadePermitida(Double vlVelocidadePermitida) {
		this.vlVelocidadePermitida = vlVelocidadePermitida;
	}
	public Double getVlVelocidadeAferida() {
		return vlVelocidadeAferida;
	}
	public void setVlVelocidadeAferida(Double vlVelocidadeAferida) {
		this.vlVelocidadeAferida = vlVelocidadeAferida;
	}
	public Double getVlVelocidadePenalidade() {
		return vlVelocidadePenalidade;
	}
	public void setVlVelocidadePenalidade(Double vlVelocidadePenalidade) {
		this.vlVelocidadePenalidade = vlVelocidadePenalidade;
	}
	public String getNrMatricula() {
		return nrMatricula;
	}
	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}
	public int getCdEquipamento() {
		return cdEquipamento;
	}
	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}
	public String getMarcaEquipamento() {
		return marcaEquipamento;
	}
	public void setMarcaEquipamento(String marcaEquipamento) {
		this.marcaEquipamento = marcaEquipamento;
	}
	public String getModeloEquipamento() {
		return modeloEquipamento;
	}
	public void setModeloEquipamento(String modeloEquipamento) {
		this.modeloEquipamento = modeloEquipamento;
	}
	public String getDsLogradouro() {
		return dsLogradouro;
	}
	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
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
	public int getTpEquipamento() {
		return tpEquipamento;
	}
	public void setTpEquipamento(int tpEquipamento) {
		this.tpEquipamento = tpEquipamento;
	}
	public String getDsObservacao() {
		return dsObservacao;
	}
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	public GregorianCalendar getDtAfericao() {
		return dtAfericao;
	}
	public void setDtAfericao(GregorianCalendar dtAfericao) {
		this.dtAfericao = dtAfericao;
	}
	public String getNrLacre() {
		return nrLacre;
	}
	public void setNrLacre(String nrLacre) {
		this.nrLacre = nrLacre;
	}
	public String getTxtCondutor() {
		return txtCondutor;
	}
	public void setTxtCondutor(String txtCondutor) {
		this.txtCondutor = txtCondutor;
	}
	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	public Double getVlMultaComDesconto() {
		return vlMultaComDesconto;
	}
	public void setVlMultaComDesconto(Double vlMultaComDesconto) {
		this.vlMultaComDesconto = vlMultaComDesconto;
	}
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getNmUf() {
		return nmUf;
	}
	public void setNmUf(String nmUf) {
		this.nmUf = nmUf;
	}
	public String getNrInventarioInmetro() {
		return nrInventarioInmetro;
	}
	public void setNrInventarioInmetro(String nrInventarioInmetro) {
		this.nrInventarioInmetro = nrInventarioInmetro;
	}
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}
	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}
	public int getLgAutoAssinado() {
		return lgAutoAssinado;
	}
	public void setLgAutoAssinado(int lgAutoAssinado) {
		this.lgAutoAssinado = lgAutoAssinado;
	}
	public String getCidadeProprietario() {
		return cidadeProprietario;
	}
	public void setCidadeProprietario(String cidadeProprietario) {
		this.cidadeProprietario = cidadeProprietario;
	}
	public String getUfProprietario() {
		return ufProprietario;
	}
	public void setUfProprietario(String ufProprietario) {
		this.ufProprietario = ufProprietario;
	}
	public String getEstadoProprietario() {
		return estadoProprietario;
	}
	public void setEstadoProprietario(String estadoProprietario) {
		this.estadoProprietario = estadoProprietario;
	}
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
	public BufferedImage getBarCodeImage() {
		return barCodeImage;
	}
	public void setBarCodeImage(BufferedImage barCodeImage) {
		this.barCodeImage = barCodeImage;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	public int getTpResponsabilidade() {
		return tpResponsabilidade;
	}
	public void setTpResponsabilidade(int tpResponsabilidade) {
		this.tpResponsabilidade = tpResponsabilidade;
	}
	public String getIdLoteImpressao() {
		return idLoteImpressao;
	}
	public void setIdLoteImpressao(String idLoteImpressao) {
		this.idLoteImpressao = idLoteImpressao;
	}
	public String getNmAgente() {
		return nmAgente;
	}
	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
	public String getNmCategoria() {
		return nmCategoria;
	}
	public void setNmCategoria(String nmCategoria) {
		this.nmCategoria = nmCategoria;
	}
	public String getNmEquipamento() {
		return nmEquipamento;
	}
	public void setNmEquipamento(String nmEquipamento) {
		this.nmEquipamento = nmEquipamento;
	}
	public String getNmTipoVeiculo() {
		return nmTipoVeiculo;
	}
	public void setNmTipoVeiculo(String nmTipoVeiculo) {
		this.nmTipoVeiculo = nmTipoVeiculo;
	}
	public String getNmMarcaEquipamento() {
		return nmMarcaEquipamento;
	}
	public void setNmMarcaEquipamento(String nmMarcaEquipamento) {
		this.nmMarcaEquipamento = nmMarcaEquipamento;
	}
	public String getNmModeloEquipamento() {
		return nmModeloEquipamento;
	}
	public void setNmModeloEquipamento(String nmModeloEquipamento) {
		this.nmModeloEquipamento = nmModeloEquipamento;
	}
	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}
	
	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
}
