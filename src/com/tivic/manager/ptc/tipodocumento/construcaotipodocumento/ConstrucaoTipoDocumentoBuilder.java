package com.tivic.manager.ptc.tipodocumento.construcaotipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class ConstrucaoTipoDocumentoBuilder {
	private List<TipoDocumentoStatus> tiposDocumentos;
	private List<IConstrucaoTipoDocumento> documentosContruidos;
	
	public ConstrucaoTipoDocumentoBuilder() {
		this.documentosContruidos = new ArrayList<IConstrucaoTipoDocumento>();
		tiposDocumentos = new ArrayList<TipoDocumentoStatus>();
		definirTiposConstrucao();
	}
	
	public ConstrucaoTipoDocumentoBuilder construirTipoDocumentos() {
		for(IConstrucaoTipoDocumento tipoDocumento : documentosContruidos ) {
			tiposDocumentos.add(tipoDocumento.montar());
		}
		
		return this;
	}
	
	public List<TipoDocumentoStatus> build(){
		return tiposDocumentos;
	}
	
	private void definirTiposConstrucao() {
		documentosContruidos.add(new ConstrucaoApresentacaoCondutor());
		documentosContruidos.add(new ConstrucaoDefesaPrevia());
		documentosContruidos.add(new ConstrucaoRecursoJari());
		documentosContruidos.add(new ConstrucaoRecursoCetran());
		documentosContruidos.add(new ConstrucaoDefesaAdvertencia());
		
	}
}
