package com.tivic.manager.adapter.base.antiga.agente;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.agente.AgenteBuilder;

public class AdapterAgente {
	public AgenteOld toBaseAntiga(Agente agente) {
		return new AgenteOldBuilder()
				.setCodAgente(agente.getCdAgente())
				.setNmAgente(agente.getNmAgente())
				.setDsEndereco(agente.getDsEndereco())
				.setBairro(agente.getNmBairro())
				.setNrCep(agente.getNrCep())
				.setCodMunicipio(agente.getCdCidade())
				.setNrMatricula(agente.getNrMatricula())
				.setCodUsuario(agente.getCdUsuario())
				.setTpAgente(agente.getTpAgente())
				.setStAgente(agente.getStAgente())
				.build();
	}
	
	public Agente toBaseNova(AgenteOld agenteOld) throws IOException, BadLocationException {
		return new AgenteBuilder()
				.setCdAgente(agenteOld.getCodAgente())
				.setCdUsuario(agenteOld.getCodUsuario())
				.setCdUsuario(agenteOld.getCodUsuario())
				.setNmAgente(agenteOld.getNmAgente())
				.setDsEndereco(agenteOld.getDsEndereco())
				.setNmBairro(agenteOld.getBairro())
				.setNrCep(agenteOld.getNrCep())
				.setCdCidade(agenteOld.getCodMunicipio())
				.setNrMatricula(agenteOld.getNrMatricula())
				.setTpAgente(agenteOld.getTpAgente())
				.build();
	}
}
