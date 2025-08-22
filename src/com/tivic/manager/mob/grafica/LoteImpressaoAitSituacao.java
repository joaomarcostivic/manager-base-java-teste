package com.tivic.manager.mob.grafica;

public class LoteImpressaoAitSituacao {
	public static final short AGUARDANDO_GERACAO=-1,
    				   		  AGUARDANDO_IMPRESSAO=0,
    				   		  EM_IMPRESSAO=1,
    				   		  IMPRESSO=2,
    				   		  ENVELOPADO=3,
    				   		  EMBALADO=4,
    				   		  ARQUIVO_CONSISTENTE=9,
    				   		  ARQUIVO_INCONSISTENTE=10,
    				   		  ECARTAS_ARQUIVO_AUTORIZADO=11,
    				   		  ECARTAS_ARQUIVO_NEGADO=12,
							  REGISTRO_CANCELADO=13;
}
