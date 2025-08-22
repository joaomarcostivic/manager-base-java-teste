package com.tivic.manager.wsdl.detran.mg;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.sol.connection.CustomConnection;

public class GeraDocumentos {
	
	private GeraDocumentoDefesa documentoDefesa;
	private GeraDocumentoJari documentoJari;
	private GeraDocumentoCetran documentoCetran;
	private GeraDocumentoCondutor documentoCondutor;
	
	public GeraDocumentos(List<AitMovimento> aitMovimentoList, int cdAit, CustomConnection customConnection) throws Exception {
		this.documentoDefesa = new GeraDocumentoDefesa(aitMovimentoList, cdAit, customConnection);
		this.documentoJari = new GeraDocumentoJari(aitMovimentoList, cdAit, customConnection);
		this.documentoCetran = new GeraDocumentoCetran(aitMovimentoList, cdAit, customConnection);
		this.documentoCondutor = new GeraDocumentoCondutor(aitMovimentoList, cdAit, customConnection);

	}
	
	public void gerar() throws Exception {
		this.documentoDefesa.setNextGenerator(documentoJari);
		this.documentoJari.setNextGenerator(documentoCetran);
		this.documentoCetran.setNextGenerator(documentoCondutor);
		this.documentoDefesa.gerar();
	}
}
