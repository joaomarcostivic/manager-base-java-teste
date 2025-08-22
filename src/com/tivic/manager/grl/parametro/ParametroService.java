package com.tivic.manager.grl.parametro;

import com.tivic.manager.grl.parametro.dtos.ValorParametroDTO;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAOFactory;
import com.tivic.sol.connection.CustomConnection;

public class ParametroService implements IParametroService{
	private CustomConnection customConnection;
	private ParametroRepositoryDAOFactory parametroRepository;

	public ParametroService() throws Exception {
		this.parametroRepository = new ParametroRepositoryDAOFactory();
	}
	
	@Override
	public ValorParametroDTO getValorOfParametroAsInt(String nmParametro) throws Exception {
		this.customConnection = new CustomConnection();
		try {
			this.customConnection.initConnection(false);
			int nrValorParametro = parametroRepository.getStrategy().getValorOfParametroAsInt(nmParametro, this.customConnection);
			ValorParametroDTO vlParametro = new ValorParametroDTO();
			vlParametro.setNmParametro(nmParametro);
			vlParametro.setNrValorParametro(nrValorParametro);
			this.customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public ValorParametroDTO getValorOfParametroAsString(String nmParametro) throws Exception {
		this.customConnection = new CustomConnection();
		try {
			this.customConnection.initConnection(false);
			String txtValorParametro = parametroRepository.getStrategy().getValorOfParametroAsString(nmParametro);
			ValorParametroDTO vlParametro = new ValorParametroDTO();
			vlParametro.setNmParametro(nmParametro);
			vlParametro.setTxtValorParametro(txtValorParametro);
			this.customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}
}
