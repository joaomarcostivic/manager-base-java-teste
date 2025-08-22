package com.tivic.manager.mob.ait.builders;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;

public class AitBuilder {
	
    private Ait ait;
    
    public AitBuilder() {
        this.ait = new Ait();
    }
    
    public AitBuilder cdAit(int cdAit) {
        ait.setCdAit(cdAit);
        return this;
    }
    
    public AitBuilder cdInfracao(int cdInfracao) {
        ait.setCdInfracao(cdInfracao);
        return this;
    }
    
    public AitBuilder cdAgente(int cdAgente) {
        ait.setCdAgente(cdAgente);
        return this;
    }
    
    public AitBuilder cdUsuario(int cdUsuario) {
        ait.setCdUsuario(cdUsuario);
        return this;
    }
    
    public AitBuilder dtInfracao(GregorianCalendar dtInfracao) {
        ait.setDtInfracao(dtInfracao);
        return this;
    }
    
    public AitBuilder dsObservacao(String dsObservacao) {
        ait.setDsObservacao(dsObservacao);
        return this;
    }
    
    public AitBuilder dsLocalInfracao(String dsLocalInfracao) {
        ait.setDsLocalInfracao(dsLocalInfracao);
        return this;
    }
    
    public AitBuilder nrAit(int nrAit) {
        ait.setNrAit(nrAit);
        return this;
    }
    
    public AitBuilder vlVelocidadePermitida(Double vlVelocidadePermitida) {
        ait.setVlVelocidadePermitida(vlVelocidadePermitida);
        return this;
    }
    
    public AitBuilder vlVelocidadeAferida(Double vlVelocidadeAferida) {
        ait.setVlVelocidadeAferida(vlVelocidadeAferida);
        return this;
    }
    
    public AitBuilder vlVelocidadePenalidade(Double vlVelocidadePenalidade) {
        ait.setVlVelocidadePenalidade(vlVelocidadePenalidade);
        return this;
    }
    
    public AitBuilder nrPlaca(String nrPlaca) {
        ait.setNrPlaca(nrPlaca);
        return this;
    }
    
    public AitBuilder dsPontoReferencia(String dsPontoReferencia) {
        ait.setDsPontoReferencia(dsPontoReferencia);
        return this;
    }
    
    public AitBuilder lgAutoAssinado(int lgAutoAssinado) {
        ait.setLgAutoAssinado(lgAutoAssinado);
        return this;
    }
    
    public AitBuilder vlLatitude(Double vlLatitude) {
        ait.setVlLatitude(vlLatitude);
        return this;
    }
    
    public AitBuilder vlLongitude(Double vlLongitude) {
        ait.setVlLongitude(vlLongitude);
        return this;
    }
    
    public AitBuilder cdCidade(int cdCidade) {
        ait.setCdCidade(cdCidade);
        return this;
    }

    public AitBuilder dtDigitacao(GregorianCalendar dtDigitacao) {
        ait.setDtDigitacao(dtDigitacao);
        return this;
    }

    public AitBuilder cdEquipamento(int cdEquipamento) {
        ait.setCdEquipamento(cdEquipamento);
        return this;
    }

    public AitBuilder vlMulta(Double vlMulta) {
        ait.setVlMulta(vlMulta);
        return this;
    }

    public AitBuilder lgEnviadoDetran(int lgEnviadoDetran) {
        ait.setLgEnviadoDetran(lgEnviadoDetran);
        return this;
    }

    public AitBuilder stAit(int stAit) {
        ait.setStAit(stAit);
        return this;
    }

    public AitBuilder cdEventoEquipamento(int cdEventoEquipamento) {
        ait.setCdEventoEquipamento(cdEventoEquipamento);
        return this;
    }

    public AitBuilder dtSincronizacao(GregorianCalendar dtSincronizacao) {
        ait.setDtSincronizacao(dtSincronizacao);
        return this;
    }

    public AitBuilder nrTentativaSincronizacao(int nrTentativaSincronizacao) {
        ait.setNrTentativaSincronizacao(nrTentativaSincronizacao);
        return this;
    }

    public AitBuilder cdBairro(int cdBairro) {
        ait.setCdBairro(cdBairro);
        return this;
    }

    public AitBuilder cdUsuarioExclusao(int cdUsuarioExclusao) {
        ait.setCdUsuarioExclusao(cdUsuarioExclusao);
        return this;
    }

    public AitBuilder dtVencimento(GregorianCalendar dtVencimento) {
        ait.setDtVencimento(dtVencimento);
        return this;
    }

    public AitBuilder tpNaturezaAutuacao(int tpNaturezaAutuacao) {
        ait.setTpNaturezaAutuacao(tpNaturezaAutuacao);
        return this;
    }

    public AitBuilder dtMovimento(GregorianCalendar dtMovimento) {
        ait.setDtMovimento(dtMovimento);
        return this;
    }

    public AitBuilder tpStatus(int tpStatus) {
        ait.setTpStatus(tpStatus);
        return this;
    }

    public AitBuilder tpArquivo(int tpArquivo) {
        ait.setTpArquivo(tpArquivo);
        return this;
    }

    public AitBuilder cdOcorrencia(int cdOcorrencia) {
        ait.setCdOcorrencia(cdOcorrencia);
        return this;
    }

    public AitBuilder dsParecer(String dsParecer) {
        ait.setDsParecer(dsParecer);
        return this;
    }

    public AitBuilder nrErro(String nrErro) {
        ait.setNrErro(nrErro);
        return this;
    }

    public AitBuilder stEntrega(int stEntrega) {
        ait.setStEntrega(stEntrega);
        return this;
    }

    public AitBuilder nrProcesso(String nrProcesso) {
        ait.setNrProcesso(nrProcesso);
        return this;
    }

    public AitBuilder dtRegistroDetran(GregorianCalendar dtRegistroDetran) {
        ait.setDtRegistroDetran(dtRegistroDetran);
        return this;
    }

    public AitBuilder stRecurso(int stRecurso) {
        ait.setStRecurso(stRecurso);
        return this;
    }

    public AitBuilder lgAdvertencia(int lgAdvertencia) {
        ait.setLgAdvertencia(lgAdvertencia);
        return this;
    }

    public AitBuilder nrControle(String nrControle) {
        ait.setNrControle(nrControle);
        return this;
    }

    public AitBuilder nrRenainf(String nrRenainf) {
        ait.setNrRenainf(nrRenainf);
        return this;
    }

    public AitBuilder nrSequencial(int nrSequencial) {
        ait.setNrSequencial(nrSequencial);
        return this;
    }

    public AitBuilder dtArNai(GregorianCalendar dtArNai) {
        ait.setDtArNai(dtArNai);
        return this;
    }
    
    public AitBuilder nrNotificacaoNai(int nrNotificacaoNai) {
        ait.setNrNotificacaoNai(nrNotificacaoNai);
        return this;
    }

    public AitBuilder nrNotificacaoNip(int nrNotificacaoNip) {
        ait.setNrNotificacaoNip(nrNotificacaoNip);
        return this;
    }

    public AitBuilder tpCancelamento(int tpCancelamento) {
        ait.setTpCancelamento(tpCancelamento);
        return this;
    }

    public AitBuilder lgCancelaMovimento(int lgCancelaMovimento) {
        ait.setLgCancelaMovimento(lgCancelaMovimento);
        return this;
    }

    public AitBuilder dtCancelamentoMovimento(GregorianCalendar dtCancelamentoMovimento) {
        ait.setDtCancelamentoMovimento(dtCancelamentoMovimento);
        return this;
    }

    public AitBuilder nrRemessa(int nrRemessa) {
        ait.setNrRemessa(nrRemessa);
        return this;
    }

    public AitBuilder nrCodigoBarras(String nrCodigoBarras) {
        ait.setNrCodigoBarras(nrCodigoBarras);
        return this;
    }

    public AitBuilder nrRemessaRegistro(int nrRemessaRegistro) {
        ait.setNrRemessaRegistro(nrRemessaRegistro);
        return this;
    }

    public AitBuilder dtEmissaoNip(GregorianCalendar dtEmissaoNip) {
        ait.setDtEmissaoNip(dtEmissaoNip);
        return this;
    }

    public AitBuilder dtResultadoDefesa(GregorianCalendar dtResultadoDefesa) {
        ait.setDtResultadoDefesa(dtResultadoDefesa);
        return this;
    }

    public AitBuilder dtAtualizacao(GregorianCalendar dtAtualizacao) {
        ait.setDtAtualizacao(dtAtualizacao);
        return this;
    }

    public AitBuilder dtResultadoJari(GregorianCalendar dtResultadoJari) {
        ait.setDtResultadoJari(dtResultadoJari);
        return this;
    }

    public AitBuilder dtResultadoCetran(GregorianCalendar dtResultadoCetran) {
        ait.setDtResultadoCetran(dtResultadoCetran);
        return this;
    }

    public AitBuilder dtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
        ait.setDtPrazoDefesa(dtPrazoDefesa);
        return this;
    }

    public AitBuilder lgAdvertenciaJari(int lgAdvertenciaJari) {
        ait.setLgAdvertenciaJari(lgAdvertenciaJari);
        return this;
    }

    public AitBuilder lgAdvertenciaCetran(int lgAdvertenciaCetran) {
        ait.setLgAdvertenciaCetran(lgAdvertenciaCetran);
        return this;
    }

    public AitBuilder lgNotrigger(int lgNotrigger) {
        ait.setLgNotrigger(lgNotrigger);
        return this;
    }

    public AitBuilder stDetran(int stDetran) {
        ait.setStDetran(stDetran);
        return this;
    }

    public AitBuilder lgErro(int lgErro) {
        ait.setLgErro(lgErro);
        return this;
    }

    public AitBuilder txtObservacao(String txtObservacao) {
        ait.setTxtObservacao(txtObservacao);
        return this;
    }

    public AitBuilder cdMovimentoAtual(int cdMovimentoAtual) {
        ait.setCdMovimentoAtual(cdMovimentoAtual);
        return this;
    }

    public AitBuilder cdAitOrigem(int cdAitOrigem) {
        ait.setCdAitOrigem(cdAitOrigem);
        return this;
    }

    public AitBuilder nrFatorNic(int nrFatorNic) {
        ait.setNrFatorNic(nrFatorNic);
        return this;
    }

    public AitBuilder lgDetranFebraban(int lgDetranFebraban) {
        ait.setLgDetranFebraban(lgDetranFebraban);
        return this;
    }

    public AitBuilder nrPlacaAntiga(String nrPlacaAntiga) {
        ait.setNrPlacaAntiga(nrPlacaAntiga);
        return this;
    }

    public AitBuilder cdEspecieVeiculo(int cdEspecieVeiculo) {
        ait.setCdEspecieVeiculo(cdEspecieVeiculo);
        return this;
    }

    public AitBuilder cdMarcaVeiculo(int cdMarcaVeiculo) {
        ait.setCdMarcaVeiculo(cdMarcaVeiculo);
        return this;
    }

    public AitBuilder cdCorVeiculo(int cdCorVeiculo) {
        ait.setCdCorVeiculo(cdCorVeiculo);
        return this;
    }

    public AitBuilder cdTipoVeiculo(int cdTipoVeiculo) {
        ait.setCdTipoVeiculo(cdTipoVeiculo);
        return this;
    }

    public AitBuilder cdCategoriaVeiculo(int cdCategoriaVeiculo) {
        ait.setCdCategoriaVeiculo(cdCategoriaVeiculo);
        return this;
    }
    
    public AitBuilder nrRenavan(String nrRenavan) {
        ait.setNrRenavan(nrRenavan);
        return this;
    }

    public AitBuilder nrAnoFabricacao(String nrAnoFabricacao) {
        ait.setNrAnoFabricacao(nrAnoFabricacao);
        return this;
    }

    public AitBuilder nrAnoModelo(String nrAnoModelo) {
        ait.setNrAnoModelo(nrAnoModelo);
        return this;
    }

    public AitBuilder nmProprietario(String nmProprietario) {
        ait.setNmProprietario(nmProprietario);
        return this;
    }

    public AitBuilder tpDocumento(int tpDocumento) {
        ait.setTpDocumento(tpDocumento);
        return this;
    }

    public AitBuilder nrDocumento(String nrDocumento) {
        ait.setNrDocumento(nrDocumento);
        return this;
    }

    public AitBuilder nmCondutor(String nmCondutor) {
        ait.setNmCondutor(nmCondutor);
        return this;
    }

    public AitBuilder nrCnhCondutor(String nrCnhCondutor) {
        ait.setNrCnhCondutor(nrCnhCondutor);
        return this;
    }

    public AitBuilder ufCnhCondutor(String ufCnhCondutor) {
        ait.setUfCnhCondutor(ufCnhCondutor);
        return this;
    }

    public AitBuilder tpCnhCondutor(int tpCnhCondutor) {
        ait.setTpCnhCondutor(tpCnhCondutor);
        return this;
    }

    public AitBuilder cdMarcaAutuacao(int cdMarcaAutuacao) {
        ait.setCdMarcaAutuacao(cdMarcaAutuacao);
        return this;
    }

    public AitBuilder nmCondutorAutuacao(String nmCondutorAutuacao) {
        ait.setNmCondutorAutuacao(nmCondutorAutuacao);
        return this;
    }

    public AitBuilder nmProprietarioAutuacao(String nmProprietarioAutuacao) {
        ait.setNmProprietarioAutuacao(nmProprietarioAutuacao);
        return this;
    }

    public AitBuilder nrCnhAutuacao(String nrCnhAutuacao) {
        ait.setNrCnhAutuacao(nrCnhAutuacao);
        return this;
    }

    public AitBuilder ufCnhAutuacao(String ufCnhAutuacao) {
        ait.setUfCnhAutuacao(ufCnhAutuacao);
        return this;
    }

    public AitBuilder nrDocumentoAutuacao(String nrDocumentoAutuacao) {
        ait.setNrDocumentoAutuacao(nrDocumentoAutuacao);
        return this;
    }

    public AitBuilder tpPessoaProprietario(int tpPessoaProprietario) {
        ait.setTpPessoaProprietario(tpPessoaProprietario);
        return this;
    }

    public AitBuilder cdBanco(int cdBanco) {
        ait.setCdBanco(cdBanco);
        return this;
    }

    public AitBuilder sgUfVeiculo(String sgUfVeiculo) {
        ait.setSgUfVeiculo(sgUfVeiculo);
        return this;
    }

    public AitBuilder dsLogradouro(String dsLogradouro) {
        ait.setDsLogradouro(dsLogradouro);
        return this;
    }

    public AitBuilder dsNrImovel(String dsNrImovel) {
        ait.setDsNrImovel(dsNrImovel);
        return this;
    }

    public AitBuilder nrCep(String nrCep) {
        ait.setNrCep(nrCep);
        return this;
    }

    public AitBuilder nrDdd(String nrDdd) {
        ait.setNrDdd(nrDdd);
        return this;
    }

    public AitBuilder nrTelefone(String nrTelefone) {
        ait.setNrTelefone(nrTelefone);
        return this;
    }

    public AitBuilder dsEnderecoCondutor(String dsEnderecoCondutor) {
        ait.setDsEnderecoCondutor(dsEnderecoCondutor);
        return this;
    }

    public AitBuilder nrCpfProprietario(String nrCpfProprietario) {
        ait.setNrCpfProprietario(nrCpfProprietario);
        return this;
    }

    public AitBuilder nrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
        ait.setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
        return this;
    }

    public AitBuilder nmComplemento(String nmComplemento) {
        ait.setNmComplemento(nmComplemento);
        return this;
    }

    public AitBuilder dsBairroCondutor(String dsBairroCondutor) {
        ait.setDsBairroCondutor(dsBairroCondutor);
        return this;
    }

    public AitBuilder nrImovelCondutor(String nrImovelCondutor) {
        ait.setNrImovelCondutor(nrImovelCondutor);
        return this;
    }
    
    public AitBuilder dsComplementoCondutor(String dsComplementoCondutor) {
        ait.setDsComplementoCondutor(dsComplementoCondutor);
        return this;
    }

    public AitBuilder cdCidadeCondutor(int cdCidadeCondutor) {
        ait.setCdCidadeCondutor(cdCidadeCondutor);
        return this;
    }

    public AitBuilder nrCepCondutor(String nrCepCondutor) {
        ait.setNrCepCondutor(nrCepCondutor);
        return this;
    }

    public AitBuilder dtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro) {
        ait.setDtPrimeiroRegistro(dtPrimeiroRegistro);
        return this;
    }

    public AitBuilder nrCpfCondutor(String nrCpfCondutor) {
        ait.setNrCpfCondutor(nrCpfCondutor);
        return this;
    }

    public AitBuilder cdVeiculo(int cdVeiculo) {
        ait.setCdVeiculo(cdVeiculo);
        return this;
    }

    public AitBuilder cdEndereco(int cdEndereco) {
        ait.setCdEndereco(cdEndereco);
        return this;
    }

    public AitBuilder cdProprietario(int cdProprietario) {
        ait.setCdProprietario(cdProprietario);
        return this;
    }

    public AitBuilder cdCondutor(int cdCondutor) {
        ait.setCdCondutor(cdCondutor);
        return this;
    }

    public AitBuilder cdEnderecoCondutor(int cdEnderecoCondutor) {
        ait.setCdEnderecoCondutor(cdEnderecoCondutor);
        return this;
    }

    public AitBuilder dtNotificacaoDevolucao(GregorianCalendar dtNotificacaoDevolucao) {
        ait.setDtNotificacaoDevolucao(dtNotificacaoDevolucao);
        return this;
    }

    public AitBuilder tpOrigem(int tpOrigem) {
        ait.setTpOrigem(tpOrigem);
        return this;
    }

    public AitBuilder lgPublicarNaiDiarioOficial(int lgPublicarNaiDiarioOficial) {
        ait.setLgPublicarNaiDiarioOficial(lgPublicarNaiDiarioOficial);
        return this;
    }

    public AitBuilder tpConvenio(int tpConvenio) {
        ait.setTpConvenio(tpConvenio);
        return this;
    }

    public AitBuilder idAit(String idAit) {
        ait.setIdAit(idAit);
        return this;
    }

    public AitBuilder cdTalao(int cdTalao) {
        ait.setCdTalao(cdTalao);
        return this;
    }

    public AitBuilder dtAfericao(GregorianCalendar dtAfericao) {
        ait.setDtAfericao(dtAfericao);
        return this;
    }

    public AitBuilder nrLacre(String nrLacre) {
        ait.setNrLacre(nrLacre);
        return this;
    }

    public AitBuilder nrInventarioInmetro(String nrInventarioInmetro) {
        ait.setNrInventarioInmetro(nrInventarioInmetro);
        return this;
    }

    public AitBuilder cdLogradouroInfracao(int cdLogradouroInfracao) {
        ait.setCdLogradouroInfracao(cdLogradouroInfracao);
        return this;
    }

    public AitBuilder imagens(List<AitImagem> imagens) {
        ait.setImagens(imagens);
        return this;
    }

    public AitBuilder movimentos(List<AitMovimento> movimentos) {
        ait.setMovimentos(movimentos);
        return this;
    }

    public AitBuilder movimentoAtual(AitMovimento movimentoAtual) {
        ait.setMovimentoAtual(movimentoAtual);
        return this;
    }
    
    public AitBuilder txtCancelamento(String txtCancelamento) {
    	ait.setTxtCancelamento(txtCancelamento);
    	return this;
    }
    
    public Ait build() {
        return ait;
    }
    
}
