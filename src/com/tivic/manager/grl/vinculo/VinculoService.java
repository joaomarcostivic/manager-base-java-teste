package com.tivic.manager.grl.vinculo;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Vinculo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class VinculoService implements IVinculoService {
	private VinculoRepository vinculoRepository;
	
	public VinculoService() throws Exception {
		vinculoRepository = (VinculoRepository) BeansFactory.get(VinculoRepository.class);
	}

	@Override
	public Vinculo get(int cdVinculo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Vinculo vinculo = get(cdVinculo, customConnection);
			if (vinculo == null) {
				throw new BadRequestException("Nenhum vínculo encontrado");
			}
			customConnection.finishConnection();
			return vinculo;
		} finally {
			customConnection.finishConnection();
		}
	}

	@Override
	public Vinculo get(int cdVinculo, CustomConnection customConnection) throws Exception {
		return vinculoRepository.get(cdVinculo, customConnection);
	}

	@Override
	public List<Vinculo> find() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Vinculo> vinculos = find(customConnection);
			if (vinculos.isEmpty()) {
				throw new BadRequestException("Nenhum vínculo encontrado");
			}
			customConnection.finishConnection();
			return vinculos;
		} finally {
			customConnection.finishConnection();
		}
	}

	@Override
	public List<Vinculo> find(CustomConnection customConnection) throws Exception {
		return vinculoRepository.find(customConnection);
	}

}
