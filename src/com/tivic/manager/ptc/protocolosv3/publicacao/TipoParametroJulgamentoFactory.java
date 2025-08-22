package com.tivic.manager.ptc.protocolosv3.publicacao;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;

public class TipoParametroJulgamentoFactory {

	public String strategy(String idRecurso, int stJulgamento) {
		int stIndeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		int stDeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		if(TipoStatusEnum.DEFESA_PREVIA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stDeferido ) {
			return ParametroServices.getValorOfParametro("MOB_PUBLICAR_DEFESA_DEFERIDA");
		} 
		else if(TipoStatusEnum.DEFESA_PREVIA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stIndeferido) {
			return ParametroServices.getValorOfParametro("MOB_PUBLICAR_DEFESA_INDEFERIDA");
		} 
		else if(TipoStatusEnum.RECURSO_JARI.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stDeferido) {
			return ParametroServices.getValorOfParametro("MOB_PUBLICAR_JARI_DEFERIDA");
		}
		else if(TipoStatusEnum.RECURSO_JARI.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stIndeferido) {
			return ParametroServices.getValorOfParametro("MOB_PUBLICAR_JARI_INDEFERIDA");
		}
		else if(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stDeferido) {
			return ParametroServices.getValorOfParametro("MOB_PUBLICAR_DEFESA_ADVERTENCIA_DEFERIDA");
		}
		else if(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stIndeferido) {
			return ParametroServices.getValorOfParametro("MOB_PUBLICAR_DEFESA_ADVERTENCIA_INDEFERIDA");
		}else {
			return null;
		}
	}	
}