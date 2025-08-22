package com.tivic.manager.mob.lote.impressao;

import java.awt.image.BufferedImage;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DadosNotificacao extends LoteImpressaoAit{
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
	private String nmCondutor;
	private String nrCnhCondutor;
	private String urCnhCondutor;
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
	private String nomeEquipamento;
	private String idEquipamento;
	private String marcaEquipamento;
	private String modeloEquipamento;
	private String dsLogradouro;
	private String dsNrImovel;
	private String nmBairro;
	private String nrCep;
	private String ufCnhCondutor;
	private int tpEquipamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazoDefesa;
	private String dsObservacao;
	private String nrCpfCondutor;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAfericao;
	private String nrLacre;
	private String txtCondutor;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	private Double vlMultaComDesconto;
	private String bancosConveniados;
	private String defesaPreviaIndeferida;
	private String linhaDigitavel;
	private CorreiosEtiqueta correiosEtiqueta;
	private String barCode;
	private String codeSG;
	private String sgServico;
	private String qrCode;
	private String barCode2D;
	private String nmUF;
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
    private BufferedImage qrCodeImage;
    private BufferedImage codeSgImage;
    private BufferedImage barCode2DImage;
    private int tpStatus;
    private int tpResponsabilidade;
    private Double vlMultaGeradora;
	private Double vlVelocidadePermitidaGeradora;
	private Double vlVelocidadeAferidaGeradora;
	private Double vlVelocidadePenalidadeGeradora;
	private int nrCodDetranGeradora;
	private String dsInfracaoGeradora;
	private String nrArtigoGeradora;
	private String nrIncisoGeradora;
	private String nrParagrafoGeradora;
	private String nrAlineaGeradora;
	private String nmNaturezaGeradora;
	private int nrPontuacaoGeradora;
	private String idAitGeradora;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracaoGeradora;
	private String dsLocalInfracaoGeradora;
    private BufferedImage barCodeImageCep;

	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public void setNmUF(String nmUF) {
		this.nmUF = nmUF;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getSgServico() {
		return sgServico;
	}

	public void setSgServico(String sgServico) {
		this.sgServico = sgServico;
	}

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
	
	public String getUrCnhCondutor() {
		return urCnhCondutor;
	}
	
	public void setUrCnhCondutor(String urCnhCondutor) {
		this.urCnhCondutor = urCnhCondutor;
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
	
	public String getNomeEquipamento() {
		return nomeEquipamento;
	}
	
	public void setNomeEquipamento(String nomeEquipamento) {
		this.nomeEquipamento = nomeEquipamento;
	}
	
	public String getIdEquipamento() {
		return idEquipamento;
	}
	
	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
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
	
	public String getUfCnhCondutor() {
		return ufCnhCondutor;
	}
	
	public void setUfCnhCondutor(String ufCnhCondutor) {
		this.ufCnhCondutor = ufCnhCondutor;
	}
	
	public int getTpEquipamento() {
		return tpEquipamento;
	}
	
	public void setTpEquipamento(int tpEquipamento) {
		this.tpEquipamento = tpEquipamento;
	}
	
	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}
	
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}
	
	public String getDsObservacao() {
		return dsObservacao;
	}
	
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
	public String getNrCpfCondutor() {
		return nrCpfCondutor;
	}
	
	public void setNrCpfCondutor(String nrCpfCondutor) {
		this.nrCpfCondutor = nrCpfCondutor;
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
	
	public String getBancosConveniados() {
		return bancosConveniados;
	}
	
	public void setBancosConveniados(String bancosConveniados) {
		this.bancosConveniados = bancosConveniados;
	}
	
	public String getDefesaPreviaIndeferida() {
		return defesaPreviaIndeferida;
	}
	
	public void setDefesaPreviaIndeferida(String defesaPreviaIndeferida) {
		this.defesaPreviaIndeferida = defesaPreviaIndeferida;
	}
	
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}
	
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	
	public CorreiosEtiqueta getCorreiosEtiqueta() {
		return correiosEtiqueta;
	}

	public void setCorreiosEtiqueta(CorreiosEtiqueta correiosEtiqueta) {
		this.correiosEtiqueta = correiosEtiqueta;
	}
		
	public int getLgAutoAssinado() {
		return lgAutoAssinado;
	}

	public void setLgAutoAssinado(int lgAutoAssinado) {
		this.lgAutoAssinado = lgAutoAssinado;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getCodeSG() {
		return codeSG;
	}

	public void setCodeSG(String codeSG) {
		this.codeSG = codeSG;
	}

	public String getBarCode2D() {
		return barCode2D;
	}

	public void setBarCode2D(String barCode2D) {
		this.barCode2D = barCode2D;
	}

	public String getNmUF() {
		return nmUF;
	}

	public void setNmUf(String nmUf) {
		this.nmUF = nmUf;
	}

	public String getNrInventarioInmetro() {
		return nrInventarioInmetro;
	}

	public void setNrInventarioInmetro(String nrInventarioInmetro) {
		this.nrInventarioInmetro = nrInventarioInmetro;
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
	
	public BufferedImage getQrCodeImage() {
		return qrCodeImage;
	}

	public void setQrCodeImage(BufferedImage qrCodeImage) {
		this.qrCodeImage = qrCodeImage;
	}

	public BufferedImage getCodeSgImage() {
		return codeSgImage;
	}

	public void setCodeSgImage(BufferedImage codeSgImage) {
		this.codeSgImage = codeSgImage;
	}

	public BufferedImage getBarCode2DImage() {
		return barCode2DImage;
	}

	public void setBarCode2DImage(BufferedImage barCode2DImage) {
		this.barCode2DImage = barCode2DImage;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}
	
	public int getCdEquipamento() {
		return cdEquipamento;
	}

	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}
	
	public int getTpResponsabilidade() {
		return tpResponsabilidade;
	}

	public void setTpResponsabilidade(int tpResponsabilidade) {
		this.tpResponsabilidade = tpResponsabilidade;
	}
	
	public Double getVlMultaGeradora() {
		return vlMultaGeradora;
	}

	public void setVlMultaGeradora(Double vlMultaGeradora) {
		this.vlMultaGeradora = vlMultaGeradora;
	}

	public Double getVlVelocidadePermitidaGeradora() {
		return vlVelocidadePermitidaGeradora;
	}

	public void setVlVelocidadePermitidaGeradora(Double vlVelocidadePermitidaGeradora) {
		this.vlVelocidadePermitidaGeradora = vlVelocidadePermitidaGeradora;
	}

	public Double getVlVelocidadeAferidaGeradora() {
		return vlVelocidadeAferidaGeradora;
	}

	public void setVlVelocidadeAferidaGeradora(Double vlVelocidadeAferidaGeradora) {
		this.vlVelocidadeAferidaGeradora = vlVelocidadeAferidaGeradora;
	}

	public Double getVlVelocidadePenalidadeGeradora() {
		return vlVelocidadePenalidadeGeradora;
	}

	public void setVlVelocidadePenalidadeGeradora(Double vlVelocidadePenalidadeGeradora) {
		this.vlVelocidadePenalidadeGeradora = vlVelocidadePenalidadeGeradora;
	}

	public int getNrCodDetranGeradora() {
		return nrCodDetranGeradora;
	}

	public void setNrCodDetranGeradora(int nrCodDetranGeradora) {
		this.nrCodDetranGeradora = nrCodDetranGeradora;
	}

	public String getDsInfracaoGeradora() {
		return dsInfracaoGeradora;
	}

	public void setDsInfracaoGeradora(String dsInfracaoGeradora) {
		this.dsInfracaoGeradora = dsInfracaoGeradora;
	}

	public String getNrArtigoGeradora() {
		return nrArtigoGeradora;
	}

	public void setNrArtigoGeradora(String nrArtigoGeradora) {
		this.nrArtigoGeradora = nrArtigoGeradora;
	}

	public String getNrIncisoGeradora() {
		return nrIncisoGeradora;
	}

	public void setNrIncisoGeradora(String nrIncisoGeradora) {
		this.nrIncisoGeradora = nrIncisoGeradora;
	}

	public String getNrParagrafoGeradora() {
		return nrParagrafoGeradora;
	}

	public void setNrParagrafoGeradora(String nrParagrafoGeradora) {
		this.nrParagrafoGeradora = nrParagrafoGeradora;
	}

	public String getNrAlineaGeradora() {
		return nrAlineaGeradora;
	}

	public void setNrAlineaGeradora(String nrAlineaGeradora) {
		this.nrAlineaGeradora = nrAlineaGeradora;
	}

	public String getNmNaturezaGeradora() {
		return nmNaturezaGeradora;
	}

	public void setNmNaturezaGeradora(String nmNaturezaGeradora) {
		this.nmNaturezaGeradora = nmNaturezaGeradora;
	}

	public int getNrPontuacaoGeradora() {
		return nrPontuacaoGeradora;
	}

	public void setNrPontuacaoGeradora(int nrPontuacaoGeradora) {
		this.nrPontuacaoGeradora = nrPontuacaoGeradora;
	}

	public String getIdAitGeradora() {
		return idAitGeradora;
	}

	public void setIdAitGeradora(String idAitGeradora) {
		this.idAitGeradora = idAitGeradora;
	}

	public GregorianCalendar getDtInfracaoGeradora() {
		return dtInfracaoGeradora;
	}

	public void setDtInfracaoGeradora(GregorianCalendar dtInfracaoGeradora) {
		this.dtInfracaoGeradora = dtInfracaoGeradora;
	}

	public String getDsLocalInfracaoGeradora() {
		return dsLocalInfracaoGeradora;
	}

	public void setDsLocalInfracaoGeradora(String dsLocalInfracaoGeradora) {
		this.dsLocalInfracaoGeradora = dsLocalInfracaoGeradora;
	}

	public BufferedImage getBarCodeImageCep() {
		return barCodeImageCep;
	}

	public void setBarCodeImageCep(BufferedImage barCodeImageCep) {
		this.barCodeImageCep = barCodeImageCep;
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
