package com.tivic.manager.seg;

import sol.security.*;

public class EstruturaOfSeguranca implements StructureSecurity {
	
	public SystemServices getSystemServices() {
		return new SistemaServices();
	}
	
	public ActionServices getActionServices() {
		return null;// new AcaoServices();
	}
	
	public FormServices getFormServices() {
		return new FormularioServices();
	}
	
	public LogServices getLogServices() {
		return new com.tivic.manager.log.LogServices();
	}
	
	public ModuleServices getModuleServices() {
		return new ModuloServices();
	}
	
	public ObjectServices getObjectServices() {
		return new ObjetoServices();
	}

	public String getDescription() {
		return "Estrutura de Segurança :: officePronto";
	}

	public GroupServices getGroupServices() {
		return new GrupoServices();
	}

	public UserServices getUserServices() {
		return new UsuarioServices();
	}

	public GroupActionsServices getGroupActionsServices() {
		return null;// new AgrupamentoAcaoServices();
	}
	
}
