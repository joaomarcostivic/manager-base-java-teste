package com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.sol.connection.CustomConnection;

public class GeradorMovimentoDocumentoFactory {

	public GeradorMovimentoDocumento factory(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception{
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.DEFESA_PREVIA:
				return new GeradorMovimentoDocumentoDefesaPrevia(aitMovimento, customConnection);
			
			case AitMovimentoServices.DEFESA_INDEFERIDA:
				return new GeradorMovimentoDocumentoDefesaIndeferida(aitMovimento, customConnection);

			case AitMovimentoServices.DEFESA_DEFERIDA:
				return new GeradorMovimentoDocumentoDefesaDeferida(aitMovimento, customConnection);

			case AitMovimentoServices.RECURSO_JARI:
				return new GeradorMovimentoDocumentoRecursoJari(aitMovimento, customConnection);
			
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
				return new GeradorMovimentoDocumentoJariSemProvimento(aitMovimento, customConnection);

			case AitMovimentoServices.JARI_COM_PROVIMENTO:
				return new GeradorMovimentoDocumentoJariComProvimento(aitMovimento, customConnection);

			case AitMovimentoServices.RECURSO_CETRAN:
				return new GeradorMovimentoDocumentoRecursoCetran(aitMovimento, customConnection);
			
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				return new GeradorMovimentoDocumentoCetranIndeferido(aitMovimento, customConnection);

			case AitMovimentoServices.CETRAN_DEFERIDO:
				return new GeradorMovimentoDocumentoCetranDeferido(aitMovimento, customConnection);

			case AitMovimentoServices.APRESENTACAO_CONDUTOR:
				return new GeradorMovimentoDocumentoApresentacaoCondutor(aitMovimento, customConnection);
				
			default:
				return new GeradorMovimentoDocumento() {
					@Override
					public void build() throws Exception {}
				};
		}
	}
	
}
