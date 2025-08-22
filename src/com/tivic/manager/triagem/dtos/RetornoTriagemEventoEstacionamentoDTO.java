package com.tivic.manager.triagem.dtos;

import com.tivic.manager.triagem.builders.RetornoTriagemEventoEstacionamentoDTOBuilder;

public final class RetornoTriagemEventoEstacionamentoDTO {
    private final Integer cdAit;
    private final Integer cdEventoEquipamento;
    private final String idAit;
    private final Double vlLatitude;
    private final Double vlLongitude;
    private final String nrNotificacao;
    private final String dsObservacao;
    private final String nmMotivoCancelamento;

    public RetornoTriagemEventoEstacionamentoDTO(
            Integer cdAit,
            Integer cdEventoEquipamento,
            String idAit,
            Double vlLatitude,
        	Double vlLongitude,
            String nrNotificacao,
            String dsObservacao,
            String nmMotivoCancelamento
        ) {
            this.cdAit = cdAit;
            this.cdEventoEquipamento = cdEventoEquipamento;
            this.idAit = idAit;
            this.vlLatitude = vlLatitude;
            this.vlLongitude = vlLongitude;
            this.nrNotificacao = nrNotificacao;
            this.dsObservacao = dsObservacao;
            this.nmMotivoCancelamento = nmMotivoCancelamento;
    }

    public Integer getCdAit() {
        return cdAit;
    }

    public Integer getCdEventoEquipamento() {
		return cdEventoEquipamento;
	}

    public String getIdAit() {
        return idAit;
    }

    public Double getVlLatitude() {
        return vlLatitude;
    }

    public Double getVlLongitude() {
        return vlLongitude;
    }

    public String getNrNotificacao() {
        return nrNotificacao;
    }
    
    public String getDsObservacao() {
		return dsObservacao;
	}
    
    public String getNmMotivoCancelamento() {
		return nmMotivoCancelamento;
	}
    
    public static RetornoTriagemEventoEstacionamentoDTOBuilder builder() {
        return new RetornoTriagemEventoEstacionamentoDTOBuilder();
    }
}
