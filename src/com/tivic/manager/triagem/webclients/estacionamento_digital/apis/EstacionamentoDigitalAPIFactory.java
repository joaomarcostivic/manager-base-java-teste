package com.tivic.manager.triagem.webclients.estacionamento_digital.apis;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.triagem.webclients.estacionamento_digital.enums.ZonaAzulAPIEnum;
import com.tivic.sol.cdi.BeansFactory;

public class EstacionamentoDigitalAPIFactory {
	
	private int tpZonaAzul = 0;
	private IParametroRepository parametroRepository;
	private static final Map<Integer, Class<? extends IEstacionamentoDigitalAPI>> map = new HashMap<>();
	static { map.put(ZonaAzulAPIEnum.PARE_AZUL.getKey(), PareAzulAPI.class); }
	
	public EstacionamentoDigitalAPIFactory() throws Exception {
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		tpZonaAzul = parametroRepository.getValorOfParametroAsInt("MOB_TP_ZONA_AZUL");
	}

	public IEstacionamentoDigitalAPI getStrategy() throws Exception {
		Class<? extends IEstacionamentoDigitalAPI> estacionamentoClass = map.get(tpZonaAzul);
		if (estacionamentoClass == null) {
			throw new BadRequestException("Tipo de estacionamento digital não encontrado.");
		}
		return estacionamentoClass.newInstance();
	}
	
}
