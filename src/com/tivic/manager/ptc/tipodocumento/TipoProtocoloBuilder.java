package com.tivic.manager.ptc.tipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;

public class TipoProtocoloBuilder {
	
	private List<TipoProtocolo> tiposProtocolos;
	private List<Integer> statusDisponiveis;
	
	public TipoProtocoloBuilder(List<Integer> statusDisponiveis) {
		this.statusDisponiveis = statusDisponiveis;
	}
	
	public TipoProtocoloBuilder montarProtocolos() throws Exception{
		tiposProtocolos = new ArrayList<TipoProtocolo>();
		
		for(Integer status : statusDisponiveis) {
			TipoProtocolo tipoProtocolo = definirTipoProtocolo(status);
			
			if(tipoProtocolo != null)
				tiposProtocolos.add(tipoProtocolo);
		}
		
		return this;
	}
	
	public List<TipoProtocolo> build(){
		return tiposProtocolos;
	}
	
	private TipoProtocolo definirTipoProtocolo(int status) throws Exception {
		
		if(status == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return new TipoProtocolo("Advertência por Escrito", 
					status, 
					TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA.getKey());
		}
		
		else if(status == TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey()) {
			return new TipoProtocolo("Apresentação de Condutor", 
					status, 
					TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey());
		}
		
		else if(status == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return new TipoProtocolo("Entrada de Defesa Prévia", 
					status, 
					TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey());
		}
		
		else if(status == TipoStatusEnum.RECURSO_CETRAN.getKey()) {
			return new TipoProtocolo("Entrada Recurso CETRAN", 
					status, 
					TipoDocumentoProtocoloEnum.RECURSO_CETRAN.getKey());
		}
		
		else if(status == TipoStatusEnum.RECURSO_JARI.getKey()) {
			return new TipoProtocolo("Entrada Recurso JARI", 
					status, 
					TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey());
		}
		
		return null;
	}
	
	
}
