package com.tivic.manager.mob.eventoequipamento.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.EventoEquipamento;

public class EventoEquipamentoBuilder {

    private EventoEquipamento eventoEquipamento;

    public EventoEquipamentoBuilder() {
        eventoEquipamento = new EventoEquipamento();
    }

    public EventoEquipamentoBuilder cdEvento(int cdEvento) {
        eventoEquipamento.setCdEvento(cdEvento);
        return this;
    }

    public EventoEquipamentoBuilder cdEquipamento(int cdEquipamento) {
        eventoEquipamento.setCdEquipamento(cdEquipamento);
        return this;
    }

    public EventoEquipamentoBuilder cdTipoEvento(int cdTipoEvento) {
        eventoEquipamento.setCdTipoEvento(cdTipoEvento);
        return this;
    }

    public EventoEquipamentoBuilder dtEvento(GregorianCalendar dtEvento) {
        eventoEquipamento.setDtEvento(dtEvento);
        return this;
    }

    public EventoEquipamentoBuilder nmOrgaoAutuador(String nmOrgaoAutuador) {
        eventoEquipamento.setNmOrgaoAutuador(nmOrgaoAutuador);
        return this;
    }

    public EventoEquipamentoBuilder nmEquipamento(String nmEquipamento) {
        eventoEquipamento.setNmEquipamento(nmEquipamento);
        return this;
    }

    public EventoEquipamentoBuilder dsLocal(String dsLocal) {
        eventoEquipamento.setDsLocal(dsLocal);
        return this;
    }

    public EventoEquipamentoBuilder idIdentificacaoInmetro(String idIdentificacaoInmetro) {
        eventoEquipamento.setIdIdentificacaoInmetro(idIdentificacaoInmetro);
        return this;
    }

    public EventoEquipamentoBuilder dtAfericao(GregorianCalendar dtAfericao) {
        eventoEquipamento.setDtAfericao(dtAfericao);
        return this;
    }

    public EventoEquipamentoBuilder nrPista(int nrPista) {
        eventoEquipamento.setNrPista(nrPista);
        return this;
    }

    public EventoEquipamentoBuilder dtConclusao(GregorianCalendar dtConclusao) {
        eventoEquipamento.setDtConclusao(dtConclusao);
        return this;
    }

    public EventoEquipamentoBuilder vlLimite(int vlLimite) {
        eventoEquipamento.setVlLimite(vlLimite);
        return this;
    }

    public EventoEquipamentoBuilder vlVelocidadeTolerada(int vlVelocidadeTolerada) {
        eventoEquipamento.setVlVelocidadeTolerada(vlVelocidadeTolerada);
        return this;
    }

    public EventoEquipamentoBuilder vlMedida(int vlMedida) {
        eventoEquipamento.setVlMedida(vlMedida);
        return this;
    }

    public EventoEquipamentoBuilder vlConsiderada(int vlConsiderada) {
        eventoEquipamento.setVlConsiderada(vlConsiderada);
        return this;
    }

    public EventoEquipamentoBuilder nrPlaca(String nrPlaca) {
        eventoEquipamento.setNrPlaca(nrPlaca);
        return this;
    }

    public EventoEquipamentoBuilder lgTempoReal(int lgTempoReal) {
        eventoEquipamento.setLgTempoReal(lgTempoReal);
        return this;
    }

    public EventoEquipamentoBuilder tpVeiculo(int tpVeiculo) {
        eventoEquipamento.setTpVeiculo(tpVeiculo);
        return this;
    }

    public EventoEquipamentoBuilder vlComprimentoVeiculo(int vlComprimentoVeiculo) {
        eventoEquipamento.setVlComprimentoVeiculo(vlComprimentoVeiculo);
        return this;
    }

    public EventoEquipamentoBuilder idMedida(int idMedida) {
        eventoEquipamento.setIdMedida(idMedida);
        return this;
    }

    public EventoEquipamentoBuilder nrSerial(String nrSerial) {
        eventoEquipamento.setNrSerial(nrSerial);
        return this;
    }

    public EventoEquipamentoBuilder nmModeloEquipamento(String nmModeloEquipamento) {
        eventoEquipamento.setNmModeloEquipamento(nmModeloEquipamento);
        return this;
    }

    public EventoEquipamentoBuilder nmRodovia(String nmRodovia) {
        eventoEquipamento.setNmRodovia(nmRodovia);
        return this;
    }

    public EventoEquipamentoBuilder nmUfRodovia(String nmUfRodovia) {
        eventoEquipamento.setNmUfRodovia(nmUfRodovia);
        return this;
    }

    public EventoEquipamentoBuilder nmKmRodovia(String nmKmRodovia) {
        eventoEquipamento.setNmKmRodovia(nmKmRodovia);
        return this;
    }

    public EventoEquipamentoBuilder nmMetrosRodovia(String nmMetrosRodovia) {
        eventoEquipamento.setNmMetrosRodovia(nmMetrosRodovia);
        return this;
    }

    public EventoEquipamentoBuilder nmSentidoRodovia(String nmSentidoRodovia) {
        eventoEquipamento.setNmSentidoRodovia(nmSentidoRodovia);
        return this;
    }

    public EventoEquipamentoBuilder idCidade(int idCidade) {
        eventoEquipamento.setIdCidade(idCidade);
        return this;
    }

    public EventoEquipamentoBuilder nmMarcaEquipamento(String nmMarcaEquipamento) {
        eventoEquipamento.setNmMarcaEquipamento(nmMarcaEquipamento);
        return this;
    }

    public EventoEquipamentoBuilder tpEquipamento(int tpEquipamento) {
        eventoEquipamento.setTpEquipamento(tpEquipamento);
        return this;
    }

    public EventoEquipamentoBuilder lgValidaFuncionamento(int lgValidaFuncionamento) {
        eventoEquipamento.setLgValidaFuncionamento(lgValidaFuncionamento);
        return this;
    }

    public EventoEquipamentoBuilder dtExecucaoJob(GregorianCalendar dtExecucaoJob) {
        eventoEquipamento.setDtExecucaoJob(dtExecucaoJob);
        return this;
    }

    public EventoEquipamentoBuilder idUuid(String idUuid) {
        eventoEquipamento.setIdUuid(idUuid);
        return this;
    }

    public EventoEquipamentoBuilder tpRestricao(int tpRestricao) {
        eventoEquipamento.setTpRestricao(tpRestricao);
        return this;
    }

    public EventoEquipamentoBuilder tpClassificacao(int tpClassificacao) {
        eventoEquipamento.setTpClassificacao(tpClassificacao);
        return this;
    }

    public EventoEquipamentoBuilder vlPermanencia(int vlPermanencia) {
        eventoEquipamento.setVlPermanencia(vlPermanencia);
        return this;
    }

    public EventoEquipamentoBuilder vlSemaforoVermelho(int vlSemaforoVermelho) {
        eventoEquipamento.setVlSemaforoVermelho(vlSemaforoVermelho);
        return this;
    }

    public EventoEquipamentoBuilder stEvento(int stEvento) {
        eventoEquipamento.setStEvento(stEvento);
        return this;
    }

    public EventoEquipamentoBuilder cdMotivoCancelamento(int cdMotivoCancelamento) {
        eventoEquipamento.setCdMotivoCancelamento(cdMotivoCancelamento);
        return this;
    }

    public EventoEquipamentoBuilder txtEvento(String txtEvento) {
        eventoEquipamento.setTxtEvento(txtEvento);
        return this;
    }

    public EventoEquipamentoBuilder lgOlpr(int lgOlpr) {
        eventoEquipamento.setLgOlpr(lgOlpr);
        return this;
    }

    public EventoEquipamentoBuilder dtCancelamento(GregorianCalendar dtCancelamento) {
        eventoEquipamento.setDtCancelamento(dtCancelamento);
        return this;
    }

    public EventoEquipamentoBuilder cdUsuarioCancelamento(int cdUsuarioCancelamento) {
        eventoEquipamento.setCdUsuarioCancelamento(cdUsuarioCancelamento);
        return this;
    }

    public EventoEquipamentoBuilder dtProcessamento(GregorianCalendar dtProcessamento) {
        eventoEquipamento.setDtProcessamento(dtProcessamento);
        return this;
    }

    public EventoEquipamentoBuilder cdVeiculo(int cdVeiculo) {
        eventoEquipamento.setCdVeiculo(cdVeiculo);
        return this;
    }

    public EventoEquipamentoBuilder lgEnviado(int lgEnviado) {
        eventoEquipamento.setLgEnviado(lgEnviado);
        return this;
    }

    public EventoEquipamentoBuilder cdUsuarioConfirmacao(int cdUsuarioConfirmacao) {
        eventoEquipamento.setCdUsuarioConfirmacao(cdUsuarioConfirmacao);
        return this;
    }

    public EventoEquipamentoBuilder vlTolerancia(int vlTolerancia) {
        eventoEquipamento.setVlTolerancia(vlTolerancia);
        return this;
    }

    public EventoEquipamento build() {
        return eventoEquipamento;
    }
}
