package com.tivic.manager.mob.processamento.sincronizacao.builders;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.eventoequipamento.enums.LgEnviadoEnum;

public class EventoSyncBuilder {
	
    private EventoEquipamento evento;
    
    public EventoSyncBuilder(EventoEquipamento eventoRadar, Equipamento equipamento, TipoEvento tipoEvento) {
        this.evento = new EventoEquipamento();
        this.evento.setLgEnviado(LgEnviadoEnum.ENVIADO.getKey());
		this.evento.setCdEquipamento(equipamento.getCdEquipamento());
		this.evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());
		this.evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());
		this.evento.setCdEquipamento(equipamento.getCdEquipamento());
		this.evento.setDtEvento(eventoRadar.getDtEvento());
		this.evento.setNmOrgaoAutuador(eventoRadar.getNmOrgaoAutuador());
		this.evento.setNmEquipamento(eventoRadar.getNmEquipamento());
		this.evento.setDsLocal(eventoRadar.getDsLocal());
		this.evento.setIdIdentificacaoInmetro(eventoRadar.getIdIdentificacaoInmetro());
		this.evento.setDtAfericao(eventoRadar.getDtAfericao());
		this.evento.setNrPista(eventoRadar.getNrPista());
		this.evento.setDtConclusao(eventoRadar.getDtConclusao());
		this.evento.setVlLimite(eventoRadar.getVlLimite());
		this.evento.setVlVelocidadeTolerada(eventoRadar.getVlVelocidadeTolerada());
		this.evento.setVlMedida(eventoRadar.getVlMedida());
		this.evento.setVlConsiderada(eventoRadar.getVlConsiderada());
		this.evento.setNrPlaca(eventoRadar.getNrPlaca());
		this.evento.setLgTempoReal(eventoRadar.getLgTempoReal());
		this.evento.setTpVeiculo(eventoRadar.getTpVeiculo());
		this.evento.setVlComprimentoVeiculo(eventoRadar.getVlComprimentoVeiculo());
		this.evento.setIdMedida(eventoRadar.getIdMedida());
		this.evento.setNrSerial(eventoRadar.getNrSerial());
		this.evento.setNmModeloEquipamento(eventoRadar.getNmModeloEquipamento());
		this.evento.setNmRodovia(eventoRadar.getNmRodovia());
		this.evento.setNmUfRodovia(eventoRadar.getNmUfRodovia());
		this.evento.setNmKmRodovia(eventoRadar.getNmKmRodovia());
		this.evento.setNmMetrosRodovia(eventoRadar.getNmMetrosRodovia());
		this.evento.setNmSentidoRodovia(eventoRadar.getNmSentidoRodovia());
		this.evento.setIdCidade(eventoRadar.getIdCidade());
		this.evento.setNmMarcaEquipamento(eventoRadar.getNmMarcaEquipamento());
		this.evento.setTpEquipamento(eventoRadar.getTpEquipamento());
		this.evento.setNrPista(eventoRadar.getNrPista());
		this.evento.setDtExecucaoJob(eventoRadar.getDtExecucaoJob());
		this.evento.setIdUuid(eventoRadar.getIdUuid());
		this.evento.setTpRestricao(eventoRadar.getTpRestricao());
		this.evento.setTpClassificacao(eventoRadar.getTpClassificacao());
		this.evento.setVlPermanencia(eventoRadar.getVlPermanencia());
		this.evento.setVlSemaforoVermelho(eventoRadar.getVlSemaforoVermelho());
		this.evento.setStEvento(eventoRadar.getStEvento());
		this.evento.setCdMotivoCancelamento(eventoRadar.getCdMotivoCancelamento());
		this.evento.setTxtEvento(eventoRadar.getTxtEvento());
		this.evento.setLgOlpr(eventoRadar.getLgOlpr());
		this.evento.setDtCancelamento(eventoRadar.getDtCancelamento());
		this.evento.setDtProcessamento(eventoRadar.getDtProcessamento());
    }
    
    public EventoEquipamento build() {
        return evento;
    }

}
