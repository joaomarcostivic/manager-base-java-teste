package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.dtos.RetornoTriagemEventoEstacionamentoDTO;

public class RetornoTriagemEventoEstacionamentoDTOBuilder {
    private Integer cdAit;
    private Integer cdEventoEquipamento;
    private String idAit;
    private Double vlLatitude;
    private Double vlLongitude;
    private String nrNotificacao;
    private String dsMotivoCancelamento;
    private String nmMotivoCancelamento;

    public RetornoTriagemEventoEstacionamentoDTOBuilder cdAit(Integer cdAit) {
        this.cdAit = cdAit;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder cdEventoEquipamento(Integer cdEventoEquipamento) {
        this.cdEventoEquipamento = cdEventoEquipamento;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder idAit(String idAit) {
        this.idAit = idAit;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder vlLatitude(Double vlLatitude) {
        this.vlLatitude = vlLatitude;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder vlLongitude(Double vlLongitude) {
        this.vlLongitude = vlLongitude;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder nrNotificacao(String nrNotificacao) {
        this.nrNotificacao = nrNotificacao;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder dsMotivoCancelamento(String dsMotivoCancelamento) {
        this.dsMotivoCancelamento = dsMotivoCancelamento;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTOBuilder nmMotivoCancelamento(String nmMotivoCancelamento) {
        this.nmMotivoCancelamento = nmMotivoCancelamento;
        return this;
    }

    public RetornoTriagemEventoEstacionamentoDTO build() {
        return new RetornoTriagemEventoEstacionamentoDTO(
            cdAit,
            cdEventoEquipamento,
            idAit,
            vlLatitude,
            vlLongitude,
            nrNotificacao,
            dsMotivoCancelamento,
            nmMotivoCancelamento
        );
    }
}