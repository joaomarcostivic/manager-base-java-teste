package com.tivic.manager.ptc.protocolos.mg;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolos.IProtocoloRecursoFactory;
import com.tivic.manager.ptc.protocolos.IProtocoloRecursoServices;
import com.tivic.manager.ptc.protocolos.mg.defesa.DefesaServices;
import com.tivic.manager.ptc.protocolos.mg.fici.FiciServices;

public class ProtocoloRecursoFactoryMG implements IProtocoloRecursoFactory {

	public static final int DEFAULT = -1;
	public static final int REMESSA = 0;
	public static final int CANCELAMENTO = 1;
	public static final int NOTIFICACAO = 2;
	public static final int APRESENTACAO_CONDUTOR = 3;
	public static final int DEFESA = 4;
	public static final int RECURSO_JARI = 5;
	public static final int CETRAN = 6;
	public static final int BAIXA = 7;
	public static final int REABERTURA = 8;
	public static final int ADVERTENCIA_DEFESA = 9;
	public static final int CANCELAMENTO_DEFESA = 10;
	public static final int CANCELAMENTO_ADVERTENCIA = 11;
	public static final int CANCELAMENTO_FICI = 12;
	public static final int CANCELAMENTO_JARI = 13;
	public static final int CANCELAMENTO_CETRAN = 14;
	public static final int CANCELAMENTO_NIP = 15;
	public static final int PUBLICACAO = 16;

	@Override
	public IProtocoloRecursoServices gerarServico(int tpStatus) throws Exception{
		switch (convertStatusToServico(tpStatus)) {
			case APRESENTACAO_CONDUTOR:
				return new FiciServices();
				
			case DEFESA:
				return new DefesaServices();
				
			case ADVERTENCIA_DEFESA:
				return new DefesaServices();
				
			default:
				throw new Exception("Serviço não implementado");
		}
	}
	
	private static int convertStatusToServico(int tpStatus){
        switch(tpStatus){

            case AitMovimentoServices.TRANSFERENCIA_PONTUACAO:
                return APRESENTACAO_CONDUTOR;
                
            case AitMovimentoServices.DEFESA_PREVIA:
                return DEFESA;
                
            case AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA:
                return ADVERTENCIA_DEFESA;
                
            default:
                return DEFAULT;
            
        }
    }

}
