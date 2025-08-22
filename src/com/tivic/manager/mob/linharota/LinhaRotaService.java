package com.tivic.manager.mob.linharota ;

import java.sql.Connection;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.LinhaRota;
import com.tivic.manager.mob.LinhaRotaServices;

public class LinhaRotaService implements ILinhaRotaService{

	ILinhaRotaRepository linhaRotaRepository = new LinhaRotaRepositoryDAO(); 
	
	@Override
	public LinhaRota remove(int cdLinha, int cdRota) throws Exception {
		return remove(cdLinha, cdRota , null);
	}

	@Override
	public LinhaRota remove(int cdLinha, int cdRota, Connection connection) throws Exception {
		boolean isConnectionNull = connection == null;
		try {
			
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			LinhaRota linhaRota = linhaRotaRepository.delete(cdLinha, cdRota, connection);
			
			if(isConnectionNull)
				connection.commit();
			
			return linhaRota;
			
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	
	}

	@Override
	public LinhaRota inactivate(LinhaRota linhaRota ) throws Exception {
		return inactivate(linhaRota , null);
	}

	@Override
	public LinhaRota inactivate(LinhaRota linhaRota , Connection connection) throws Exception {
		boolean isConnectionNull = connection == null;
		try {
			
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			linhaRota.setStRota(LinhaRotaServices.ST_INATIVA);
			linhaRota = linhaRotaRepository.update(linhaRota, connection);
			
			if(isConnectionNull)
				connection.commit();
			
			return linhaRota;
			
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Override
	public LinhaRota activate(LinhaRota linhaRota ) throws Exception {
		return activate(linhaRota , null);
	}

	@Override
	public LinhaRota activate(LinhaRota linhaRota , Connection connection) throws Exception {
		boolean isConnectionNull = connection == null;
		try {
			
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			linhaRota.setStRota(LinhaRotaServices.ST_ATIVA);
			linhaRota = linhaRotaRepository.update(linhaRota, connection);
			
			if(isConnectionNull)
				connection.commit();
			
			return linhaRota;
			
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
