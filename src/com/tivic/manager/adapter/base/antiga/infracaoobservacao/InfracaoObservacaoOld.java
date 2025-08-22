package com.tivic.manager.adapter.base.antiga.infracaoobservacao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InfracaoObservacaoOld {

    private int codInfracao;
    private int cdObservacao;
    private String nrObservacao;
    private String nmObservacao;
    private int stObservacao;
    private String txtObservacao;
    private int tpInfracaoObservacao;

    public InfracaoObservacaoOld() {}

    public InfracaoObservacaoOld(int codInfracao,
			int cdObservacao,
			String nrObservacao,
			String nmObservacao,
			String txtObservacao,
			int stObservacao,
			int tpInfracaoObservacao){
		setCodInfracao(codInfracao);
		setCdObservacao(cdObservacao);
		setNrObservacao(nrObservacao);
		setNmObservacao(nmObservacao);
		setTxtObservacao(txtObservacao);
		setStObservacao(stObservacao);
		setTpInfracaoObservacao(tpInfracaoObservacao);
	}

    public int getCodInfracao() {
        return codInfracao;
    }

    public void setCodInfracao(int codInfracao) {
        this.codInfracao = codInfracao;
    }

    public int getCdObservacao() {
        return cdObservacao;
    }

    public void setCdObservacao(int cdObservacao) {
        this.cdObservacao = cdObservacao;
    }

    public String getNrObservacao() {
        return nrObservacao;
    }

    public void setNrObservacao(String nrObservacao) {
        this.nrObservacao = nrObservacao;
    }

    public String getNmObservacao() {
        return nmObservacao;
    }

    public void setNmObservacao(String nmObservacao) {
        this.nmObservacao = nmObservacao;
    }

    public int getStObservacao() {
        return stObservacao;
    }

    public void setStObservacao(int stObservacao) {
        this.stObservacao = stObservacao;
    }

    public String getTxtObservacao() {
        return txtObservacao;
    }

    public void setTxtObservacao(String txtObservacao) {
        this.txtObservacao = txtObservacao;
    }

    public int getTpInfracaoObservacao() {
        return tpInfracaoObservacao;
    }

    public void setTpInfracaoObservacao(int tpInfracaoObservacao) {
        this.tpInfracaoObservacao = tpInfracaoObservacao;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
}
