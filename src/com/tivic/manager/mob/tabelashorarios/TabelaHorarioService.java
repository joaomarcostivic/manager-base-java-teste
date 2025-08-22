package com.tivic.manager.mob.tabelashorarios;

import java.sql.Connection;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.HorarioServices;
import com.tivic.manager.mob.LinhaRota;
import com.tivic.manager.mob.LinhaRotaServices;
import com.tivic.manager.mob.TabelaHorario;
import com.tivic.manager.mob.TabelaHorarioDAO;
import com.tivic.manager.mob.TabelaHorarioDTO;
import com.tivic.manager.mob.TabelaHorarioRota;
import com.tivic.manager.mob.TabelaHorarioServices;
import com.tivic.manager.mob.tabelahorariorota.TabelaHorarioRotaService;
import com.tivic.manager.util.Util;

public class TabelaHorarioService {

	TabelaHorarioRotaService tabelaHorarioRotaService;
	
	public TabelaHorarioService() {
		tabelaHorarioRotaService = new TabelaHorarioRotaService();
	}
	
	
	public void saveComRotas(TabelaHorarioDTO tabelaHorarioDTO) throws Exception {
		saveComRotas(tabelaHorarioDTO, null);
	}
	
	public void saveComRotas(TabelaHorarioDTO tabelaHorarioDTO, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if(tabelaHorarioDTO.getCdTabelaHorario()==0){
				insertComRotas(tabelaHorarioDTO, connection);
			}
			else {
				updateComRotas(tabelaHorarioDTO, connection);
			}
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	private void insertComRotas(TabelaHorarioDTO tabelaHorarioDTO, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int code = TabelaHorarioDAO.insert(tabelaHorarioDTO, connection);
			if(code <= 0)
				throw new Exception("Erro ao inserir tabela horário ");
			
			insertRotas(tabelaHorarioDTO, connection);
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


	private void insertRotas(TabelaHorarioDTO tabelaHorarioDTO, Connection connection) throws Exception {
		TabelaHorarioRota tabelaHorarioRotaIda = new TabelaHorarioRota(tabelaHorarioDTO.getCdLinha(),tabelaHorarioDTO.getCdTabelaHorario(),tabelaHorarioDTO.getLinhaRotaIda().getCdRota(), 0);
		tabelaHorarioRotaService.save(tabelaHorarioRotaIda, connection);
		TabelaHorarioRota tabelaHorarioRotaVolta = new TabelaHorarioRota(tabelaHorarioDTO.getCdLinha(),tabelaHorarioDTO.getCdTabelaHorario(),tabelaHorarioDTO.getLinhaRotaVolta().getCdRota(), 0);
		tabelaHorarioRotaService.save(tabelaHorarioRotaVolta, connection);
	}
	
	private void updateComRotas(TabelaHorarioDTO tabelaHorarioDTO, Connection connection) throws Exception {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int code = TabelaHorarioDAO.update(tabelaHorarioDTO, connection);
			if(code <= 0)
				throw new Exception("Erro ao atualizar tabela horário ");
			
			updateRotas(tabelaHorarioDTO, connection);
			
			if (isConnectionNull)
				connection.commit();
			
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	private void updateRotas(TabelaHorarioDTO tabelaHorarioDTO, Connection connection) throws Exception {
		TabelaHorarioRota tabelaHorarioRotaIda = new TabelaHorarioRota(tabelaHorarioDTO.getCdLinha(),tabelaHorarioDTO.getCdTabelaHorario(),tabelaHorarioDTO.getLinhaRotaIda().getCdRota(), 0);
		substituirRotas(tabelaHorarioDTO, tabelaHorarioRotaIda, LinhaRotaServices.TP_IDA, connection);
		TabelaHorarioRota tabelaHorarioRotaVolta = new TabelaHorarioRota(tabelaHorarioDTO.getCdLinha(),tabelaHorarioDTO.getCdTabelaHorario(),tabelaHorarioDTO.getLinhaRotaVolta().getCdRota(), 0);
		substituirRotas(tabelaHorarioDTO, tabelaHorarioRotaVolta, LinhaRotaServices.TP_VOLTA, connection);
	}


	private void substituirRotas(TabelaHorarioDTO tabelaHorarioDTO, TabelaHorarioRota tabelaHorarioRota, int tpRota, Connection connection) throws Exception {
		TabelaHorarioRota tabelaHorarioRotaExistente = getTabelaHorarioRotaExistente(tabelaHorarioDTO, tabelaHorarioRota, tpRota, connection);
		if(tabelaHorarioRotaExistente != null) {
			if(HorarioServices.validaExistenciaHorarios(tabelaHorarioRotaExistente.getCdTabelaHorario(), tabelaHorarioRotaExistente.getCdLinha(), tabelaHorarioRotaExistente.getCdRota()))
				throw new Exception("Não é possível alterar as rotas com horários cadastrados");
			tabelaHorarioRotaService.delete(tabelaHorarioRotaExistente, connection);
		}
		tabelaHorarioRotaService.save(tabelaHorarioRota, connection);
	}


	private TabelaHorarioRota getTabelaHorarioRotaExistente(TabelaHorarioDTO tabelaHorarioDTO, TabelaHorarioRota tabelaHorarioRota, int tpRota, Connection connection) throws Exception {
		List<TabelaHorarioRota> listTabelaHorarioRotaExistente = tabelaHorarioRotaService.findByHorarioLinha(tabelaHorarioRota.getCdTabelaHorario(), tabelaHorarioRota.getCdLinha(), tpRota, connection);
		if(listTabelaHorarioRotaExistente.isEmpty())
			return null;
		return listTabelaHorarioRotaExistente.get(0);
	}

	public TabelaHorario inactivate(TabelaHorario tabelaHorario ) throws Exception {
		return inactivate(tabelaHorario , null);
	}

	public TabelaHorario inactivate(TabelaHorario tabelaHorario , Connection connection) throws Exception {
		boolean isConnectionNull = connection == null;
		try {
			
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			tabelaHorario.setStTabelaHorario(TabelaHorarioServices.ST_INATIVA);
			tabelaHorario.setDtFinalVigencia(Util.getDataAtual());
			int result = TabelaHorarioDAO.update(tabelaHorario, connection);
			if(result <= 0)
				throw new Exception("Erro ao inativar tabela horário ");
			
			if(isConnectionNull)
				connection.commit();
			
			return tabelaHorario;
			
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public TabelaHorario activate(TabelaHorario tabelaHorario ) throws Exception {
		return activate(tabelaHorario , null);
	}

	public TabelaHorario activate(TabelaHorario tabelaHorario , Connection connection) throws Exception {
		boolean isConnectionNull = connection == null;
		try {
			
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			tabelaHorario.setStTabelaHorario(TabelaHorarioServices.ST_ATIVA);
			int result = TabelaHorarioDAO.update(tabelaHorario, connection);
			if(result <= 0)
				throw new Exception("Erro ao ativar tabela horário ");
			
			if(isConnectionNull)
				connection.commit();
			
			return tabelaHorario;
			
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
