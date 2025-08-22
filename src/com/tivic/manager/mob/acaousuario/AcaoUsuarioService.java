package com.tivic.manager.mob.acaousuario;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class AcaoUsuarioService implements IAcaoUsuarioService {
	
	private AcaoUsuarioRepository acaoUsuarioRepository;
	
	public AcaoUsuarioService() throws Exception {
		this.acaoUsuarioRepository = (AcaoUsuarioRepository) BeansFactory.get(AcaoUsuarioRepository.class);
	}

	@Override
	public void insert(AcaoUsuario acaoUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(acaoUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void insert(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception {
		this.acaoUsuarioRepository.insert(acaoUsuario, customConnection);
	}

	@Override
	public void update(AcaoUsuario acaoUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(acaoUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(AcaoUsuario acaoUsuario, CustomConnection customConnection) throws Exception {
		this.acaoUsuarioRepository.update(acaoUsuario, customConnection);
	}

	@Override
	public AcaoUsuario get(int cdAcao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AcaoUsuario acaoUsuario = get(cdAcao, customConnection);
			customConnection.finishConnection();
			return acaoUsuario;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AcaoUsuario get(int cdAcao, CustomConnection customConnection) throws Exception {
		return this.acaoUsuarioRepository.get(cdAcao, customConnection);
	}

	@Override
	public void insertAll(List<AcaoUsuarioDTO> acaoUsuarioList) throws Exception {
		for(AcaoUsuarioDTO acaoUsuario: acaoUsuarioList) {				
			insert(new AcaoUsuarioBuilder(acaoUsuario).build());
		}	
	}

	@Override
	public void insertAll(List<AcaoUsuarioDTO> acaoUsuarioList, CustomConnection customConnection) throws Exception {
		for(AcaoUsuarioDTO acaoUsuario: acaoUsuarioList) {				
			insert(new AcaoUsuarioBuilder(acaoUsuario).build(), customConnection);
		}
	}	
}
