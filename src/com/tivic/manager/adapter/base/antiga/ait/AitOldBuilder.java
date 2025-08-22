package com.tivic.manager.adapter.base.antiga.ait;

import java.util.GregorianCalendar;

public class AitOldBuilder {
	private AitOld ait;
	
	public AitOldBuilder() {
		this.ait = new AitOld();
	}
	
    public AitOldBuilder codBanco(int codBanco) {
        ait.setCodBanco(codBanco);
        return this;
    }
    
    public AitOldBuilder codigoAit(int codigoAit) {
        ait.setCodigoAit(codigoAit);
        return this;
    }
    
    public AitOldBuilder codBairro(int codBairro) {
        ait.setCodBairro(codBairro);
        return this;
    }
    
    public AitOldBuilder codInfracao(int codInfracao) {
        ait.setCodInfracao(codInfracao);
        return this;
    }
    
    public AitOldBuilder codUsuario(int codUsuario) {
        ait.setCodUsuario(codUsuario);
        return this;
    }
    
    public AitOldBuilder codEspecie(int codEspecie) {
        ait.setCodEspecie(codEspecie);
        return this;
    }
    
    public AitOldBuilder codMarca(int codMarca) {
        ait.setCodMarca(codMarca);
        return this;
    }
    
    public AitOldBuilder codAgente(int codAgente) {
        ait.setCodAgente(codAgente);
        return this;
    }
    
    public AitOldBuilder codCor(int codCor) {
        ait.setCodCor(codCor);
        return this;
    }
    
    public AitOldBuilder codTipo(int codTipo) {
        ait.setCodTipo(codTipo);
        return this;
    }
    
    public AitOldBuilder dtInfracao(GregorianCalendar dtInfracao) {
        ait.setDtInfracao(dtInfracao);
        return this;
    }
    
    public AitOldBuilder codCategoria(int codCategoria) {
        ait.setCodCategoria(codCategoria);
        return this;
    }
    
    public AitOldBuilder codMunicipio(int codMunicipio) {
        ait.setCodMunicipio(codMunicipio);
        return this;
    }
    
    public AitOldBuilder dsObservacao(String dsObservacao) {
        ait.setDsObservacao(dsObservacao);
        return this;
    }
    
    public AitOldBuilder dsLocalInfracao(String dsLocalInfracao) {
        ait.setDsLocalInfracao(dsLocalInfracao);
        return this;
    }
    
    public AitOldBuilder ufVeiculo(String ufVeiculo) {
        ait.setUfVeiculo(ufVeiculo);
        return this;
    }
    
    public AitOldBuilder cdRenavan(Long cdRenavan) {
        ait.setCdRenavan(cdRenavan);
        return this;
    }
    
    public AitOldBuilder dsAnoFabricacao(String dsAnoFabricacao) {
        ait.setDsAnoFabricacao(dsAnoFabricacao);
        return this;
    }
    
    public AitOldBuilder dsAnoModelo(String dsAnoModelo) {
        ait.setDsAnoModelo(dsAnoModelo);
        return this;
    }
    
    public AitOldBuilder nmProprietario(String nmProprietario) {
        ait.setNmProprietario(nmProprietario);
        return this;
    }
    
    public AitOldBuilder tpDocumento(int tpDocumento) {
        ait.setTpDocumento(tpDocumento);
        return this;
    }
    
    public AitOldBuilder nrDocumento(String nrDocumento) {
        ait.setNrDocumento(nrDocumento);
        return this;
    }
    
    public AitOldBuilder dsLogradouro(String dsLogradouro) {
        ait.setDsLogradouro(dsLogradouro);
        return this;
    }
    
    public AitOldBuilder dsNrImovel(String dsNrImovel) {
        ait.setDsNrImovel(dsNrImovel);
        return this;
    }
    
    public AitOldBuilder nrCep(String nrCep) {
        ait.setNrCep(nrCep);
        return this;
    }
    
    public AitOldBuilder nrDdd(String nrDdd) {
        ait.setNrDdd(nrDdd);
        return this;
    }
    
    public AitOldBuilder nrTelefone(String nrTelefone) {
        ait.setNrTelefone(nrTelefone);
        return this;
    }
    
    public AitOldBuilder nrRemessa(int nrRemessa) {
        ait.setNrRemessa(nrRemessa);
        return this;
    }
    
    public AitOldBuilder nrAit(String nrAit) {
        ait.setNrAit(nrAit);
        return this;
    }
    
    public AitOldBuilder nrCodigoBarras(String nrCodigoBarras) {
        ait.setNrCodigoBarras(nrCodigoBarras);
        return this;
    }
    
    public AitOldBuilder nmCondutor(String nmCondutor) {
        ait.setNmCondutor(nmCondutor);
        return this;
    }
    
    public AitOldBuilder nrCnhCondutor(String nrCnhCondutor) {
        ait.setNrCnhCondutor(nrCnhCondutor);
        return this;
    }
    
    public AitOldBuilder ufCnhCondutor(String ufCnhCondutor) {
        ait.setUfCnhCondutor(ufCnhCondutor);
        return this;
    }
    
    public AitOldBuilder dsEnderecoCondutor(String dsEnderecoCondutor) {
        ait.setDsEnderecoCondutor(dsEnderecoCondutor);
        return this;
    }
    
    public AitOldBuilder nrCpfProprietario(String nrCpfProprietario) {
        ait.setNrCpfProprietario(nrCpfProprietario);
        return this;
    }
    
    public AitOldBuilder vlVelocidadePermitida(Double vlVelocidadePermitida) {
        ait.setVlVelocidadePermitida(vlVelocidadePermitida);
        return this;
    }
    
    public AitOldBuilder vlVelocidadeAferida(Double vlVelocidadeAferida) {
        ait.setVlVelocidadeAferida(vlVelocidadeAferida);
        return this;
    }
    
    public AitOldBuilder vlVelocidadePenalidade(Double vlVelocidadePenalidade) {
        ait.setVlVelocidadePenalidade(vlVelocidadePenalidade);
        return this;
    }
    
    public AitOldBuilder nrPlaca(String nrPlaca) {
        ait.setNrPlaca(nrPlaca);
        return this;
    }
    
    public AitOldBuilder cdUsuarioExclusao(int cdUsuarioExclusao) {
        ait.setCdUsuarioExclusao(cdUsuarioExclusao);
        return this;
    }
    
    public AitOldBuilder dtVencimento(GregorianCalendar dtVencimento) {
        ait.setDtVencimento(dtVencimento);
        return this;
    }
    
    public AitOldBuilder dsPontoReferencia(String dsPontoReferencia) {
        ait.setDsPontoReferencia(dsPontoReferencia);
        return this;
    }
    
    public AitOldBuilder lgAutoAssinado(int lgAutoAssinado) {
        ait.setLgAutoAssinado(lgAutoAssinado);
        return this;
    }
    
    public AitOldBuilder dtDigitacao(GregorianCalendar dtDigitacao) {
        ait.setDtDigitacao(dtDigitacao);
        return this;
    }
    
    public AitOldBuilder tpNaturezaAutuacao(int tpNaturezaAutuacao) {
        ait.setTpNaturezaAutuacao(tpNaturezaAutuacao);
        return this;
    }
    
    public AitOldBuilder tpCnhCondutor(Integer tpCnhCondutor) {
        ait.setTpCnhCondutor(tpCnhCondutor);
        return this;
    }
    
    public AitOldBuilder tpPessoaProprietario(int tpPessoaProprietario) {
        ait.setTpPessoaProprietario(tpPessoaProprietario);
        return this;
    }
    
    public AitOldBuilder nrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
        ait.setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
        return this;
    }
    
    public AitOldBuilder nmComplemento(String nmComplemento) {
        ait.setNmComplemento(nmComplemento);
        return this;
    }
    
    public AitOldBuilder dtMovimento(GregorianCalendar dtMovimento) {
        ait.setDtMovimento(dtMovimento);
        return this;
    }
    
    public AitOldBuilder tpStatus(int tpStatus) {
        ait.setTpStatus(tpStatus);
        return this;
    }
    
    public AitOldBuilder tpArquivo(int tpArquivo) {
        ait.setTpArquivo(tpArquivo);
        return this;
    }
    
    public AitOldBuilder codOcorrencia(int codOcorrencia) {
        ait.setCodOcorrencia(codOcorrencia);
        return this;
    }
    
    public AitOldBuilder dsParecer(String dsParecer) {
        ait.setDsParecer(dsParecer);
        return this;
    }
    
    public AitOldBuilder nrErro(String nrErro) {
        ait.setNrErro(nrErro);
        return this;
    }
    
    public AitOldBuilder lgEnviadoDetran(int lgEnviadoDetran) {
        ait.setLgEnviadoDetran(lgEnviadoDetran);
        return this;
    }
    
    public AitOldBuilder stEntrega(int stEntrega) {
        ait.setStEntrega(stEntrega);
        return this;
    }
    
    public AitOldBuilder nrProcesso(String nrProcesso) {
        ait.setNrProcesso(nrProcesso);
        return this;
    }
    
    public AitOldBuilder dtRegistroDetran(GregorianCalendar dtRegistroDetran) {
        ait.setDtRegistroDetran(dtRegistroDetran);
        return this;
    }
    
    public AitOldBuilder stRecurso(int stRecurso) {
        ait.setStRecurso(stRecurso);
        return this;
    }
    
    public AitOldBuilder lgAdvertencia(int lgAdvertencia) {
        ait.setLgAdvertencia(lgAdvertencia);
        return this;
    }
    
    public AitOldBuilder nrControle(int nrControle) {
        ait.setNrControle(nrControle);
        return this;
    }
    
    public AitOldBuilder nrRenainf(Long nrRenainf) {
        ait.setNrRenainf(nrRenainf);
        return this;
    }
    
    public AitOldBuilder nrSequencial(int nrSequencial) {
        ait.setNrSequencial(nrSequencial);
        return this;
    }
    
    public AitOldBuilder dtArNai(GregorianCalendar dtArNai) {
        ait.setDtArNai(dtArNai);
        return this;
    }
    
    public AitOldBuilder nrNotificacaoNai(int nrNotificacaoNai) {
        ait.setNrNotificacaoNai(nrNotificacaoNai);
        return this;
    }
    
    public AitOldBuilder dsBairroCondutor(String dsBairroCondutor) {
        ait.setDsBairroCondutor(dsBairroCondutor);
        return this;
    }
    
    public AitOldBuilder nrImovelCondutor(String nrImovelCondutor) {
        ait.setNrImovelCondutor(nrImovelCondutor);
        return this;
    }
    
    public AitOldBuilder dsComplementoCondutor(String dsComplementoCondutor) {
        ait.setDsComplementoCondutor(dsComplementoCondutor);
        return this;
    }
    
    public AitOldBuilder cdMunicipioCondutor(int cdMunicipioCondutor) {
        ait.setCdMunicipioCondutor(cdMunicipioCondutor);
        return this;
    }
    
    public AitOldBuilder nrCepCondutor(String nrCepCondutor) {
        ait.setNrCepCondutor(nrCepCondutor);
        return this;
    }
    
    public AitOldBuilder nrNotificacaoNip(int nrNotificacaoNip) {
        ait.setNrNotificacaoNip(nrNotificacaoNip);
        return this;
    }
    
    public AitOldBuilder tpCancelamento(int tpCancelamento) {
        ait.setTpCancelamento(tpCancelamento);
        return this;
    }
    
    public AitOldBuilder lgCancelaMovimento(int lgCancelaMovimento) {
        ait.setLgCancelaMovimento(lgCancelaMovimento);
        return this;
    }
    
    public AitOldBuilder dtCancelamentoMovimento(GregorianCalendar dtCancelamentoMovimento) {
        ait.setDtCancelamentoMovimento(dtCancelamentoMovimento);
        return this;
    }
    
    public AitOldBuilder nrRemessaRegistro(int nrRemessaRegistro) {
        ait.setNrRemessaRegistro(nrRemessaRegistro);
        return this;
    }
    
    public AitOldBuilder dtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro) {
        ait.setDtPrimeiroRegistro(dtPrimeiroRegistro);
        return this;
    }
    
    public AitOldBuilder dtEmissaoNip(GregorianCalendar dtEmissaoNip) {
        ait.setDtEmissaoNip(dtEmissaoNip);
        return this;
    }
    
    public AitOldBuilder dtResultadoDefesa(GregorianCalendar dtResultadoDefesa) {
        ait.setDtResultadoDefesa(dtResultadoDefesa);
        return this;
    }
    
    public AitOldBuilder dtAtualizacao(GregorianCalendar dtAtualizacao) {
        ait.setDtAtualizacao(dtAtualizacao);
        return this;
    }
    
    public AitOldBuilder dtResultadoJari(GregorianCalendar dtResultadoJari) {
        ait.setDtResultadoJari(dtResultadoJari);
        return this;
    }
    
    public AitOldBuilder dtResultadoCetran(GregorianCalendar dtResultadoCetran) {
        ait.setDtResultadoCetran(dtResultadoCetran);
        return this;
    }
    
    public AitOldBuilder blbFoto(byte[] blbFoto) {
        ait.setBlbFoto(blbFoto);
        return this;
    }
    
    public AitOldBuilder lgAdvertenciaJari(int lgAdvertenciaJari) {
        ait.setLgAdvertenciaJari(lgAdvertenciaJari);
        return this;
    }
    
    public AitOldBuilder lgAdvertenciaCetran(int lgAdvertenciaCetran) {
        ait.setLgAdvertenciaCetran(lgAdvertenciaCetran);
        return this;
    }
    
    public AitOldBuilder lgNotrigger(int lgNotrigger) {
        ait.setLgNotrigger(lgNotrigger);
        return this;
    }
    
    public AitOldBuilder stDetran(String stDetran) {
        ait.setStDetran(stDetran);
        return this;
    }
    
    public AitOldBuilder lgErro(int lgErro) {
        ait.setLgErro(lgErro);
        return this;
    }
    
    public AitOldBuilder nrCpfCondutor(String nrCpfCondutor) {
        ait.setNrCpfCondutor(nrCpfCondutor);
        return this;
    }
    
    public AitOldBuilder cdVeiculo(int cdVeiculo) {
        ait.setCdVeiculo(cdVeiculo);
        return this;
    }
    
    public AitOldBuilder cdEndereco(int cdEndereco) {
        ait.setCdEndereco(cdEndereco);
        return this;
    }
    
    public AitOldBuilder cdProprietario(int cdProprietario) {
        ait.setCdProprietario(cdProprietario);
        return this;
    }
    
    public AitOldBuilder cdCondutor(int cdCondutor) {
        ait.setCdCondutor(cdCondutor);
        return this;
    }
    
    public AitOldBuilder cdEnderecoCondutor(int cdEnderecoCondutor) {
        ait.setCdEnderecoCondutor(cdEnderecoCondutor);
        return this;
    }
    
    public AitOldBuilder txtObservacao(String txtObservacao) {
        ait.setTxtObservacao(txtObservacao);
        return this;
    }
    
    public AitOldBuilder cdMovimentoAtual(int cdMovimentoAtual) {
        ait.setCdMovimentoAtual(cdMovimentoAtual);
        return this;
    }
    
    public AitOldBuilder cdEquipamento(int cdEquipamento) {
        ait.setCdEquipamento(cdEquipamento);
        return this;
    }
    
    public AitOldBuilder vlLatitude(Double vlLatitude) {
        ait.setVlLatitude(vlLatitude);
        return this;
    }
    
    public AitOldBuilder vlLongitude(Double vlLongitude) {
        ait.setVlLongitude(vlLongitude);
        return this;
    }
    
    public AitOldBuilder codMarcaAutuacao(int codMarcaAutuacao) {
        ait.setCodMarcaAutuacao(codMarcaAutuacao);
        return this;
    }
    
    public AitOldBuilder nmCondutorAutuacao(String nmCondutorAutuacao) {
        ait.setNmCondutorAutuacao(nmCondutorAutuacao);
        return this;
    }
    
    public AitOldBuilder nmProprietarioAutuacao(String nmProprietarioAutuacao) {
        ait.setNmProprietarioAutuacao(nmProprietarioAutuacao);
        return this;
    }
    
    public AitOldBuilder nrCnhAutuacao(String nrCnhAutuacao) {
        ait.setNrCnhAutuacao(nrCnhAutuacao);
        return this;
    }
    
    public AitOldBuilder ufCnhAutuacao(String ufCnhAutuacao) {
        ait.setUfCnhAutuacao(ufCnhAutuacao);
        return this;
    }
    
    public AitOldBuilder nrDocumentoAutuacao(String nrDocumentoAutuacao) {
        ait.setNrDocumentoAutuacao(nrDocumentoAutuacao);
        return this;
    }
    
    public AitOldBuilder cdAitOrigem(int cdAitOrigem) {
        ait.setCdAitOrigem(cdAitOrigem);
        return this;
    }
    
    public AitOldBuilder vlMulta(Double vlMulta) {
        ait.setVlMulta(vlMulta);
        return this;
    }
    
    public AitOldBuilder nrFatorNic(int nrFatorNic) {
        ait.setNrFatorNic(nrFatorNic);
        return this;
    }
    
    public AitOldBuilder lgDetranFebraban(int lgDetranFebraban) {
        ait.setLgDetranFebraban(lgDetranFebraban);
        return this;
    }
    
    public AitOldBuilder tpOrigem(int tpOrigem) {
        ait.setTpOrigem(tpOrigem);
        return this;
    }
    
    public AitOldBuilder nrPlacaAntiga(String nrPlacaAntiga) {
        ait.setNrPlacaAntiga(nrPlacaAntiga);
        return this;
    }
    
    public AitOldBuilder stAit(int stAit) {
        ait.setStAit(stAit);
        return this;
    }
    
    public AitOldBuilder dtSincronizacao(GregorianCalendar dtSincronizacao) {
        ait.setDtSincronizacao(dtSincronizacao);
        return this;
    }
    
    public AitOldBuilder cdEventoEquipamento(int cdEventoEquipamento) {
        ait.setCdEventoEquipamento(cdEventoEquipamento);
        return this;
    }
    
    public AitOldBuilder tpConvenio(int tpConvenio) {
        ait.setTpConvenio(tpConvenio);
        return this;
    }
    
    public AitOldBuilder dtAfericao(GregorianCalendar dtAfericao) {
        ait.setDtAfericao(dtAfericao);
        return this;
    }
    
    public AitOldBuilder nrLacre(String nrLacre) {
        ait.setNrLacre(nrLacre);
        return this;
    }
    
    public AitOldBuilder nrInventarioInmetro(String nrInventarioInmetro) {
        ait.setNrInventarioInmetro(nrInventarioInmetro);
        return this;
    }
    
    public AitOldBuilder dtAdesaoSne(GregorianCalendar dtAdesaoSne) {
        ait.setDtAdesaoSne(dtAdesaoSne);
        return this;
    }
    
    public AitOldBuilder stOptanteSne(int stOptanteSne) {
        ait.setStOptanteSne(stOptanteSne);
        return this;
    }
    
    public AitOldBuilder lgPenalidadeAdvertenciaNip(int lgPenalidadeAdvertenciaNip) {
        ait.setLgPenalidadeAdvertenciaNip(lgPenalidadeAdvertenciaNip);
        return this;
    }
    
    public AitOldBuilder dtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
        ait.setDtPrazoDefesa(dtPrazoDefesa);
        return this;
    }
    
    public AitOldBuilder codEspecieAutuacao(int codEspecieAutuacao) {
        ait.setCodEspecieAutuacao(codEspecieAutuacao);
        return this;
    }
    
    public AitOldBuilder dtNotificacaoDevolucao(GregorianCalendar dtNotificacaoDevolucao) {
        ait.setDtNotificacaoDevolucao(dtNotificacaoDevolucao);
        return this;
    }
    
    public AitOldBuilder codTalao(int codTalao) {
        ait.setCodTalao(codTalao);
        return this;
    }
    
    public AitOldBuilder txtCancelamento(String txtCancelamento) {
    	ait.setTxtCancelamento(txtCancelamento);
    	return this;
    }
    
    public AitOld build() {
        return this.ait;
    }
	
}
