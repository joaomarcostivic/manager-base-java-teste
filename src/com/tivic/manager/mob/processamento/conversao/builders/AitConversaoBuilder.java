package com.tivic.manager.mob.processamento.conversao.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitDtInfracaoDeserialize;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.EnviadoDetranEnum;
import com.tivic.manager.mob.ait.SituacaoAitEnum;

public class AitConversaoBuilder {
	
    private AitDtInfracaoDeserialize ait;
    
    public AitConversaoBuilder() {
        ait = new AitDtInfracaoDeserialize();
        ait.setStAit(SituacaoAitEnum.ST_CONFIRMADO.getKey());
        ait.setLgEnviadoDetran(EnviadoDetranEnum.LG_DETRAN_NAO_ENVIADA.getKey());
        ait.setDtDigitacao(new GregorianCalendar());
    }
    
    public AitConversaoBuilder cdAit(int cdAit) {
        ait.setCdAit(cdAit);
        return this;
    }
    
    public AitConversaoBuilder cdInfracao(int cdInfracao) {
        ait.setCdInfracao(cdInfracao);
        return this;
    }
    
    public AitConversaoBuilder cdAgente(int cdAgente) {
        ait.setCdAgente(cdAgente);
        return this;
    }
    
    public AitConversaoBuilder cdUsuario(int cdUsuario) {
        ait.setCdUsuario(cdUsuario);
        return this;
    }
    
    public AitConversaoBuilder dtInfracao(GregorianCalendar dtInfracao) {
        ait.setDtInfracao(dtInfracao);
        return this;
    }
    
    public AitConversaoBuilder dsLocalInfracao(String dsLocalInfracao) {
        ait.setDsLocalInfracao(dsLocalInfracao);
        return this;
    }
    
    public AitConversaoBuilder nrAit(Talonario talao) {
        ait.setNrAit(talao.getNrUltimoAit() + 1);
        return this;
    }
    
    public AitConversaoBuilder vlVelocidadePermitida(Double vlVelocidadePermitida) {
        ait.setVlVelocidadePermitida(vlVelocidadePermitida);
        return this;
    }
    
    public AitConversaoBuilder vlVelocidadeAferida(Double vlVelocidadeAferida) {
        ait.setVlVelocidadeAferida(vlVelocidadeAferida);
        return this;
    }
    
    public AitConversaoBuilder vlVelocidadePenalidade(Double vlVelocidadePenalidade) {
        ait.setVlVelocidadePenalidade(vlVelocidadePenalidade);
        return this;
    }
    
    public AitConversaoBuilder nrPlaca(String nrPlaca) {
        ait.setNrPlaca(nrPlaca);
        return this;
    }
    
    public AitConversaoBuilder vlLatitude(Double vlLatitude) {
        ait.setVlLatitude(vlLatitude);
        return this;
    }
    
    public AitConversaoBuilder vlLongitude(Double vlLongitude) {
        ait.setVlLongitude(vlLongitude);
        return this;
    }
    
    public AitConversaoBuilder cdCidade(int cdCidade) {
        ait.setCdCidade(cdCidade);
        return this;
    }

    public AitConversaoBuilder cdEquipamento(int cdEquipamento) {
        ait.setCdEquipamento(cdEquipamento);
        return this;
    }

    public AitConversaoBuilder vlMulta(Double vlMulta) {
        ait.setVlMulta(vlMulta);
        return this;
    }

    public AitConversaoBuilder dtAfericao(GregorianCalendar dtAfericao) {
        ait.setDtAfericao(dtAfericao);
        return this;
    }

    public AitConversaoBuilder nrLacre(String nrLacre) {
        ait.setNrLacre(nrLacre);
        return this;
    }

    public AitConversaoBuilder nrInventarioInmetro(String nrInventarioInmetro) {
        ait.setNrInventarioInmetro(nrInventarioInmetro);
        return this;
    }

    public AitConversaoBuilder cdMarcaAutuacao(int cdMarcaAutuacao) {
        ait.setCdMarcaAutuacao(cdMarcaAutuacao);
        return this;
    }

    public AitConversaoBuilder nmProprietario(String nmProprietario) {
        ait.setNmProprietario(nmProprietario);
        return this;
    }

    public AitConversaoBuilder idAit(Talonario talao) {
        ait.setIdAit(String.format("%s%08d", talao.getSgTalao(), talao.getNrUltimoAit() + 1));
        return this;
    }

    public AitConversaoBuilder cdEventoEquipamento(int cdEventoEquipamento) {
        ait.setCdEventoEquipamento(cdEventoEquipamento);
        return this;
    }

    public AitConversaoBuilder cdEspecieVeiculo(int cdEspecieVeiculo) {
        ait.setCdEspecieVeiculo(cdEspecieVeiculo);
        return this;
    }

    public AitConversaoBuilder cdMarcaVeiculo(int cdMarcaVeiculo) {
        ait.setCdMarcaVeiculo(cdMarcaVeiculo);
        return this;
    }

    public AitConversaoBuilder cdCorVeiculo(int cdCorVeiculo) {
        ait.setCdCorVeiculo(cdCorVeiculo);
        return this;
    }
    
    public AitConversaoBuilder cdTalao(int cdTalao) {
        ait.setCdTalao(cdTalao);
        return this;
    }
    
    public AitConversaoBuilder tpConvenio(int tpConvenio) {
    	ait.setTpConvenio(tpConvenio);
    	return this;
    }

    public AitDtInfracaoDeserialize build() {
        return ait;
    }
    
}
