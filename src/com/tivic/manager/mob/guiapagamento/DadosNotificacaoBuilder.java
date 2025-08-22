package com.tivic.manager.mob.guiapagamento;

import java.awt.image.BufferedImage;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;

public class DadosNotificacaoBuilder {

	private DadosNotificacao dadosNotificacao;

	public DadosNotificacaoBuilder(DadosGuiaPagamento dadosGuiaPagamento) {
		this.dadosNotificacao = new DadosNotificacao();
		addCdAit(dadosGuiaPagamento.getCdAit());
		addIdAit(dadosGuiaPagamento.getIdAit());
		addNrRenainf(dadosGuiaPagamento.getNrRenainf());
		addNrControle(dadosGuiaPagamento.getNrControle());
		addDtEmissao(dadosGuiaPagamento.getDtEmissao());
		addNrPlaca(dadosGuiaPagamento.getNrPlaca());
		addSgUfVeiculo(dadosGuiaPagamento.getSgUfVeiculo());
		addNmMunicipio(dadosGuiaPagamento.getNmMunicipio());
		addNrRenavan(dadosGuiaPagamento.getNrRenavan());
		addNmModelo(dadosGuiaPagamento.getNmModelo());
		addDsEspecie(dadosGuiaPagamento.getDsEspecie());
		addNmProprietario(dadosGuiaPagamento.getNmProprietario());
		addNrCpfCnpjProprietario(dadosGuiaPagamento.getNrCpfCnpjProprietario());
		addDsLocalInfracao(dadosGuiaPagamento.getDsLocalInfracao());
		addDsPontoReferencia(dadosGuiaPagamento.getDsPontoReferencia());
		addDtInfracao(dadosGuiaPagamento.getDtInfracao());
		addNrCodDetran(dadosGuiaPagamento.getNrCodDetran());
		addDsInfracao(dadosGuiaPagamento.getDsInfracao());
		addNrArtigo(dadosGuiaPagamento.getNrArtigo());
		addNrInciso(dadosGuiaPagamento.getNrInciso());
		addNrParagrafo(dadosGuiaPagamento.getNrParagrafo());
		addNrAlinea(dadosGuiaPagamento.getNrAlinea());
		addNmNatureza(dadosGuiaPagamento.getNmNatureza());
		addNrPontuacao(dadosGuiaPagamento.getNrPontuacao());
		addVlMulta(dadosGuiaPagamento.getVlMulta());
		addVlVelocidadePermitida(dadosGuiaPagamento.getVlVelocidadePermitida());
		addVlVelocidadeAferida(dadosGuiaPagamento.getVlVelocidadeAferida());
		addVlVelocidadePenalidade(dadosGuiaPagamento.getVlVelocidadePenalidade());
		addNrMatricula(dadosGuiaPagamento.getNrMatricula());
		addCdEquipamento(dadosGuiaPagamento.getCdEquipamento());
		addMarcaEquipamento(dadosGuiaPagamento.getMarcaEquipamento());
		addModeloEquipamento(dadosGuiaPagamento.getModeloEquipamento());
		addDsLogradouro(dadosGuiaPagamento.getDsLogradouro());
		addNmBairro(dadosGuiaPagamento.getNmBairro());
		addNrCep(dadosGuiaPagamento.getNrCep());
		addTpEquipamento(dadosGuiaPagamento.getTpEquipamento());
		addDsObservacao(dadosGuiaPagamento.getDsObservacao());
		addDtAfericao(dadosGuiaPagamento.getDtAfericao());
		addNrLacre(dadosGuiaPagamento.getNrLacre());
		addTxtCondutor(dadosGuiaPagamento.getTxtCondutor());
		addDtVencimento(dadosGuiaPagamento.getDtVencimento());
		addVlMultaComDesconto(dadosGuiaPagamento.getVlMultaComDesconto());
		addLinhaDigitavel(dadosGuiaPagamento.getLinhaDigitavel());
		addBarCode(dadosGuiaPagamento.getBarCode());
		addNmUf(dadosGuiaPagamento.getNmUf());
		addNrInventarioInmetro(dadosGuiaPagamento.getNrInventarioInmetro());
		addCdLoteImpressao(dadosGuiaPagamento.getCdLoteImpressao());
		addLgAutoAssinado(dadosGuiaPagamento.getLgAutoAssinado());
		addCidadeProprietario(dadosGuiaPagamento.getCidadeProprietario());
		addUfProprietario(dadosGuiaPagamento.getUfProprietario());
		addEstadoProprietario(dadosGuiaPagamento.getEstadoProprietario());
		addDtMovimento(dadosGuiaPagamento.getDtMovimento());
		addBarCodeImage(dadosGuiaPagamento.getBarCodeImage());
		addTpStatus(dadosGuiaPagamento.getTpStatus());
		addTpResponsabilidade(dadosGuiaPagamento.getTpResponsabilidade());
		addIdLoteImpressao(dadosGuiaPagamento.getIdLoteImpressao());
		addNmEquipamento(dadosGuiaPagamento.getNmEquipamento());
		addNmMarcaEquipamento(dadosGuiaPagamento.getNmMarcaEquipamento());
		addNmModeloEquipamento(dadosGuiaPagamento.getNmModeloEquipamento());
	}

	public DadosNotificacaoBuilder addCdAit(int cdAit) {
		this.dadosNotificacao.setCdAit(cdAit);
		return this;
	}

	public DadosNotificacaoBuilder addIdAit(String idAit) {
		this.dadosNotificacao.setIdAit(idAit);
		return this;
	}

	public DadosNotificacaoBuilder addNrRenainf(String nrRenainf) {
		this.dadosNotificacao.setNrRenainf(nrRenainf);
		return this;
	}

	public DadosNotificacaoBuilder addNrControle(String nrControle) {
		this.dadosNotificacao.setNrControle(nrControle);
		return this;
	}

	public DadosNotificacaoBuilder addDtEmissao(GregorianCalendar dtEmissao) {
		this.dadosNotificacao.setDtEmissao(dtEmissao);
		return this;
	}

	public DadosNotificacaoBuilder addNrPlaca(String nrPlaca) {
		this.dadosNotificacao.setNrPlaca(nrPlaca);
		return this;
	}

	public DadosNotificacaoBuilder addSgUfVeiculo(String sgUfVeiculo) {
		this.dadosNotificacao.setSgUfVeiculo(sgUfVeiculo);
		return this;
	}

	public DadosNotificacaoBuilder addNmMunicipio(String nmMunicipio) {
		this.dadosNotificacao.setNmMunicipio(nmMunicipio);
		return this;
	}

	public DadosNotificacaoBuilder addNrRenavan(String nrRenavan) {
		this.dadosNotificacao.setNrRenavan(nrRenavan);
		return this;
	}

	public DadosNotificacaoBuilder addNmModelo(String nmModelo) {
		this.dadosNotificacao.setNmModelo(nmModelo);
		return this;
	}

	public DadosNotificacaoBuilder addDsEspecie(String dsEspecie) {
		this.dadosNotificacao.setDsEspecie(dsEspecie);
		return this;
	}

	public DadosNotificacaoBuilder addNmProprietario(String nmProprietario) {
		this.dadosNotificacao.setNmProprietario(nmProprietario);
		return this;
	}

	public DadosNotificacaoBuilder addNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.dadosNotificacao.setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		return this;
	}

	public DadosNotificacaoBuilder addDsLocalInfracao(String dsLocalInfracao) {
		this.dadosNotificacao.setDsLocalInfracao(dsLocalInfracao);
		return this;
	}

	public DadosNotificacaoBuilder addDsPontoReferencia(String dsPontoReferencia) {
		this.dadosNotificacao.setDsPontoReferencia(dsPontoReferencia);
		return this;
	}

	public DadosNotificacaoBuilder addDtInfracao(GregorianCalendar dtInfracao) {
		this.dadosNotificacao.setDtInfracao(dtInfracao);
		return this;
	}

	public DadosNotificacaoBuilder addNrCodDetran(int nrCodDetran) {
		this.dadosNotificacao.setNrCodDetran(nrCodDetran);
		return this;
	}

	public DadosNotificacaoBuilder addDsInfracao(String dsInfracao) {
		this.dadosNotificacao.setDsInfracao(dsInfracao);
		return this;
	}

	public DadosNotificacaoBuilder addNrArtigo(String nrArtigo) {
		this.dadosNotificacao.setNrArtigo(nrArtigo);
		return this;
	}

	public DadosNotificacaoBuilder addNrInciso(String nrInciso) {
		this.dadosNotificacao.setNrInciso(nrInciso);
		return this;
	}

	public DadosNotificacaoBuilder addNrParagrafo(String nrParagrafo) {
		this.dadosNotificacao.setNrParagrafo(nrParagrafo);
		return this;
	}

	public DadosNotificacaoBuilder addNrAlinea(String nrAlinea) {
		this.dadosNotificacao.setNrAlinea(nrAlinea);
		return this;
	}

	public DadosNotificacaoBuilder addNmNatureza(String nmNatureza) {
		this.dadosNotificacao.setNmNatureza(nmNatureza);
		return this;
	}

	public DadosNotificacaoBuilder addNrPontuacao(int nrPontuacao) {
		this.dadosNotificacao.setNrPontuacao(nrPontuacao);
		return this;
	}

	public DadosNotificacaoBuilder addVlMulta(Double vlMulta) {
		this.dadosNotificacao.setVlMulta(vlMulta);
		return this;
	}

	public DadosNotificacaoBuilder addVlVelocidadePermitida(Double vlVelocidadePermitida) {
		this.dadosNotificacao.setVlVelocidadePermitida(vlVelocidadePermitida);
		return this;
	}

	public DadosNotificacaoBuilder addVlVelocidadeAferida(Double vlVelocidadeAferida) {
		this.dadosNotificacao.setVlVelocidadeAferida(vlVelocidadeAferida);
		return this;
	}

	public DadosNotificacaoBuilder addVlVelocidadePenalidade(Double vlVelocidadePenalidade) {
		this.dadosNotificacao.setVlVelocidadePenalidade(vlVelocidadePenalidade);
		return this;
	}

	public DadosNotificacaoBuilder addNrMatricula(String nrMatricula) {
		this.dadosNotificacao.setNrMatricula(nrMatricula);
		return this;
	}

	public DadosNotificacaoBuilder addCdEquipamento(int cdEquipamento) {
		this.dadosNotificacao.setCdEquipamento(cdEquipamento);
		return this;
	}

	public DadosNotificacaoBuilder addMarcaEquipamento(String marcaEquipamento) {
		this.dadosNotificacao.setMarcaEquipamento(marcaEquipamento);
		return this;
	}

	public DadosNotificacaoBuilder addModeloEquipamento(String modeloEquipamento) {
		this.dadosNotificacao.setModeloEquipamento(modeloEquipamento);
		return this;
	}

	public DadosNotificacaoBuilder addDsLogradouro(String dsLogradouro) {
		this.dadosNotificacao.setDsLogradouro(dsLogradouro);
		return this;
	}

	public DadosNotificacaoBuilder addNmBairro(String nmBairro) {
		this.dadosNotificacao.setNmBairro(nmBairro);
		return this;
	}

	public DadosNotificacaoBuilder addNrCep(String nrCep) {
		this.dadosNotificacao.setNrCep(nrCep);
		return this;
	}

	public DadosNotificacaoBuilder addTpEquipamento(int tpEquipamento) {
		this.dadosNotificacao.setTpEquipamento(tpEquipamento);
		return this;
	}

	public DadosNotificacaoBuilder addDsObservacao(String dsObservacao) {
		this.dadosNotificacao.setDsObservacao(dsObservacao);
		return this;
	}

	public DadosNotificacaoBuilder addDtAfericao(GregorianCalendar dtAfericao) {
		this.dadosNotificacao.setDtAfericao(dtAfericao);
		return this;
	}

	public DadosNotificacaoBuilder addNrLacre(String nrLacre) {
		this.dadosNotificacao.setNrLacre(nrLacre);
		return this;
	}

	public DadosNotificacaoBuilder addTxtCondutor(String txtCondutor) {
		this.dadosNotificacao.setTxtCondutor(txtCondutor);
		return this;
	}

	public DadosNotificacaoBuilder addDtVencimento(GregorianCalendar dtVencimento) {
		this.dadosNotificacao.setDtVencimento(dtVencimento);
		return this;
	}

	public DadosNotificacaoBuilder addVlMultaComDesconto(Double vlMultaComDesconto) {
		this.dadosNotificacao.setVlMultaComDesconto(vlMultaComDesconto);
		return this;
	}

	public DadosNotificacaoBuilder addLinhaDigitavel(String linhaDigitavel) {
		this.dadosNotificacao.setLinhaDigitavel(linhaDigitavel);
		return this;
	}

	public DadosNotificacaoBuilder addBarCode(String barCode) {
		this.dadosNotificacao.setBarCode(barCode);
		return this;
	}

	public DadosNotificacaoBuilder addNmUf(String nmUf) {
		this.dadosNotificacao.setNmUf(nmUf);
		return this;
	}

	public DadosNotificacaoBuilder addNrInventarioInmetro(String nrInventarioInmetro) {
		this.dadosNotificacao.setNrInventarioInmetro(nrInventarioInmetro);
		return this;
	}

	public DadosNotificacaoBuilder addCdLoteImpressao(int cdLoteImpressao) {
		this.dadosNotificacao.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}

	public DadosNotificacaoBuilder addLgAutoAssinado(int lgAutoAssinado) {
		this.dadosNotificacao.setLgAutoAssinado(lgAutoAssinado);
		return this;
	}

	public DadosNotificacaoBuilder addCidadeProprietario(String cidadeProprietario) {
		this.dadosNotificacao.setCidadeProprietario(cidadeProprietario);
		return this;
	}

	public DadosNotificacaoBuilder addUfProprietario(String ufProprietario) {
		this.dadosNotificacao.setUfProprietario(ufProprietario);
		return this;
	}

	public DadosNotificacaoBuilder addEstadoProprietario(String estadoProprietario) {
		this.dadosNotificacao.setEstadoProprietario(estadoProprietario);
		return this;
	}

	public DadosNotificacaoBuilder addDtMovimento(GregorianCalendar dtMovimento) {
		this.dadosNotificacao.setDtMovimento(dtMovimento);
		return this;
	}

	public DadosNotificacaoBuilder addBarCodeImage(BufferedImage barCodeImage) {
		this.dadosNotificacao.setBarCodeImage(barCodeImage);
		return this;
	}

	public DadosNotificacaoBuilder addTpStatus(int tpStatus) {
		this.dadosNotificacao.setTpStatus(tpStatus);
		return this;
	}

	public DadosNotificacaoBuilder addTpResponsabilidade(int tpResponsabilidade) {
		this.dadosNotificacao.setTpResponsabilidade(tpResponsabilidade);
		return this;
	}

	public DadosNotificacaoBuilder addIdLoteImpressao(String idLoteImpressao) {
		this.dadosNotificacao.setIdLoteImpressaoAit(idLoteImpressao);
		return this;
	}

	public DadosNotificacaoBuilder addNmEquipamento(String nmEquipamento) {
		this.dadosNotificacao.setNomeEquipamento(nmEquipamento);
		return this;
	}

	public DadosNotificacaoBuilder addNmMarcaEquipamento(String nmMarcaEquipamento) {
		this.dadosNotificacao.setMarcaEquipamento(nmMarcaEquipamento);
		return this;
	}

	public DadosNotificacaoBuilder addNmModeloEquipamento(String nmModeloEquipamento) {
		this.dadosNotificacao.setModeloEquipamento(nmModeloEquipamento);
		return this;
	}

	public DadosNotificacao build() {
		return this.dadosNotificacao;
	}

}
