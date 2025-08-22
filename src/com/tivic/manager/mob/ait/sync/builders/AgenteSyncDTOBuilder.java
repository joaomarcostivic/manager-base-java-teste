package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.ait.sync.entities.AgenteSyncDTO;

public class AgenteSyncDTOBuilder {

	private AgenteSyncDTO agenteSyncDTO;
	
	public AgenteSyncDTOBuilder(Agente agente) {
		this.agenteSyncDTO = new AgenteSyncDTO();
		setAgenteSync(agente);
	}
	
	private void setAgenteSync(Agente agente) {
		this.agenteSyncDTO.setCdAgente(agente.getCdAgente());
		this.agenteSyncDTO.setCdUsuario(agente.getCdUsuario());
		this.agenteSyncDTO.setNmAgente(agente.getNmAgente());
		this.agenteSyncDTO.setNrMatricula(agente.getNrMatricula());
	}
	
	public AgenteSyncDTO build() {
		return this.agenteSyncDTO;
	}
}
