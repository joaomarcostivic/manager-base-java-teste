package com.tivic.manager.mob.agente;

import com.tivic.manager.mob.Agente;

public class AgenteBuilder {

    private Agente agente;

    public AgenteBuilder() {
    	agente = new Agente();
    }
    
    public AgenteBuilder(Agente agente) {
    	this.agente = agente;
    }

    public AgenteBuilder setCdAgente(int cdAgente) {
        agente.setCdAgente(cdAgente);
        return this;
    }

    public AgenteBuilder setCdUsuario(int cdUsuario) {
    	agente.setCdUsuario(cdUsuario);
        return this;
    }

    public AgenteBuilder setNmAgente(String nmAgente) {
    	agente.setNmAgente(nmAgente);
        return this;
    }
    
    public AgenteBuilder setDsEndereco(String dsEndereco) {
    	agente.setDsEndereco(dsEndereco);
        return this;
    }
    
    public AgenteBuilder setNmBairro(String nmBairro) {
    	agente.setNmBairro(nmBairro);
		return this;
	}
    
    public AgenteBuilder setNrCep(String nrCep) {
    	agente.setNrCep(nrCep);
        return this;
    }

    public AgenteBuilder setCdCidade(int cdCidade) {
    	agente.setCdCidade(cdCidade);
        return this;
    }

    public AgenteBuilder setNrMatricula(String nrMatricula) {
    	agente.setNrMatricula(nrMatricula);
        return this;
    }

    public AgenteBuilder setTpAgente(int tpAgente) {
    	agente.setTpAgente(tpAgente);
        return this;
    }


    public Agente build() {
		return agente;
	}
}