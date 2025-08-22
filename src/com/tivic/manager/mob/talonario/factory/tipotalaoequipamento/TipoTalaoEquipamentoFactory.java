package com.tivic.manager.mob.talonario.factory.tipotalaoequipamento;

import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class TipoTalaoEquipamentoFactory {
	
	public TipoTalaoEquipamentoFactory() { }
	
	public TipoTalaoEquipamentoStrategy getStrategy(int tpEquipamento) throws Exception {
	    if (tpEquipamento == EquipamentoEnum.RADAR_FIXO.getKey() || tpEquipamento == EquipamentoEnum.DETECTOR.getKey()) {
	    	return new RadarFixoStrategy();
	    }
	    
	    if (tpEquipamento == EquipamentoEnum.CAMERA.getKey()) {
	    	return new VideoMonitoramentoStrategy();
	    }
	    
	    if (tpEquipamento == EquipamentoEnum.RADAR_ESTATICO.getKey()) {
	    	return new RadarEstaticoStrategy();
	    }
	    
	    if (tpEquipamento == EquipamentoEnum.ZONA_AZUL.getKey()) {
	    	return new ZonaAzulStrategy();
	    }
	    
	    throw new ValidacaoException("O tipo de equipamento " + tpEquipamento + " é invalido.");
	}

}
