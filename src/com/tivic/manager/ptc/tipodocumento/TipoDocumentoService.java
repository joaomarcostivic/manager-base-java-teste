package com.tivic.manager.ptc.tipodocumento;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.circuitoait.CircuitoAit;
import com.tivic.manager.mob.ait.circuitoait.CircuitoAitItem;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class TipoDocumentoService implements ITipoDocumentoService {
	private TipoDocumentoRepository tipoDocumentoRepository;
	private AitRepository aitRepository;

	public TipoDocumentoService() throws Exception{
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public TipoDocumento get(int id) throws BadRequestException, Exception{
		return get(id, new CustomConnection());
	}

	public TipoDocumento get(int id, CustomConnection customConnection) throws BadRequestException, Exception{
		try {
			customConnection.initConnection(false);
			TipoDocumento tipoDocumento = tipoDocumentoRepository.get(id, customConnection);
			customConnection.finishConnection();
			if(tipoDocumento == null) {
				throw new BadRequestException("Nenhum tipo de documento encontrado");
			}
			return tipoDocumento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<TipoProtocolo> getTiposDisponiveis(int cdAit) throws BadRequestException, Exception {
		return getTiposDisponiveis(cdAit, new CustomConnection());
	}

	public List<TipoProtocolo> getTiposDisponiveis(int cdAit, CustomConnection customConnection) throws BadRequestException, Exception {
		try {
			List<TipoProtocolo> protocolosDisponiveis = new ArrayList<TipoProtocolo>();
			Ait ait = aitRepository.get(cdAit);		
			CircuitoAit circuitoAit = new CircuitoAit();
			CircuitoAitItem aitItens = circuitoAit.getItem(ait.getTpStatus());
			List<Integer> tpStatusProtocoloDisponiveis = new CircuitoAitProtocoloFilter(aitItens.getItensPossiveis(), cdAit).filtrar();
			protocolosDisponiveis = new TipoProtocoloBuilder(tpStatusProtocoloDisponiveis).montarProtocolos().build();
			
			if(protocolosDisponiveis.isEmpty()) {
				throw new NoContentException("Nenhum tipo de protocolo disponível para lançamento.");
			}
			return protocolosDisponiveis;
		} finally {
			customConnection.closeConnection();
		}
	}
}
