package com.tivic.manager.wsdl.detran.mg;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento.GeradorMovimentoDocumentoFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraDocumentoCondutor extends MovimentoDocumentoHandler {
	
	private List<AitMovimento> aitMovimentoList;
	private int cdAit;
	private CustomConnection customConnection;
	
	public GeraDocumentoCondutor(List<AitMovimento> aitMovimentoList, int cdAit, CustomConnection customConnection) {
		this.aitMovimentoList = aitMovimentoList;
		this.cdAit = cdAit;
		this.customConnection = customConnection;
	}

	@Override
	public void gerar() throws Exception {
		List<AitMovimento> aitMovimentoCondutorList = new ArrayList<AitMovimento>();
		
		for(AitMovimento movimento: this.aitMovimentoList) {
			if(lgStatusCondutor(movimento)) {
				aitMovimentoCondutorList.add(movimento);
			}
		}
		
		processarDocumento(aitMovimentoCondutorList, this.aitMovimentoList, this.cdAit, this.customConnection);
	}
	
	private boolean lgStatusCondutor(AitMovimento aitMovimento) {
		List<Integer> tpMovimento = new ArrayList<Integer>();
		tpMovimento.add(AitMovimentoServices.APRESENTACAO_CONDUTOR);
		return tpMovimento.contains(aitMovimento.getTpStatus()) ? true : false;
	}
	
	private void processarDocumento(List<AitMovimento> aitMovimentoCondutorList, List<AitMovimento> aitMovimentoList, 
			int cdAit, CustomConnection customConnection) throws Exception {
		
		AitMovimento aitMovimentoCondutor = new AitMovimento();

		for (AitMovimento movimentoContudor: aitMovimentoCondutorList) {
			switch(movimentoContudor.getTpStatus()) {
				case  AitMovimentoServices.APRESENTACAO_CONDUTOR:
					aitMovimentoCondutor = movimentoContudor;
					break;
			}
		}
		if (aitMovimentoCondutor != null) {
			aitMovimentoCondutor.setCdAit(cdAit);
			new GeradorMovimentoDocumentoFactory().factory(aitMovimentoCondutor, customConnection).build();
		}
	}
}
