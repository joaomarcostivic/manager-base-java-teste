package com.tivic.manager.fta;

import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoServices;

public class MarcaModeloSyncFactory {
	
	public static MarcaModeloSync gerarServico() {
		Estado estado = EstadoServices.getEstadoOrgaoAutuador();
		
		if(estado.getSgEstado().equalsIgnoreCase("MG"))
			return new MarcaModeloSyncMg();
		else if(estado.getSgEstado().equalsIgnoreCase("BA"))
			return new MarcaModeloSyncBa();
		
		return null;
	}
	
	public static MarcaModeloSync gerarServico(boolean syncStr) {
		Estado estado = EstadoServices.getEstadoOrgaoAutuador();
		
		if(estado.getSgEstado().equalsIgnoreCase("MG"))
			return new MarcaModeloSyncMg(syncStr);
		else if(estado.getSgEstado().equalsIgnoreCase("BA"))
			return new MarcaModeloSyncBa();
		
		return null;
	}

}
