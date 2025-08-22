package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Ocorrencia;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class MotoristaFolgaAgendamentoServices {

	/* Situação dos agendamentos */
	public static final int ST_AGENDADO = 0;
	public static final int ST_EFETIVADO = 1;
	public static final int ST_EM_ATRASO = 2;
	public static final int ST_CANCELADO = 3;

	public static final String[] situacoesFolga = {"Agendado",
		"Efetivado",
		"Em Atraso",
		"Cancelado"};
	
	public static Result save(MotoristaFolgaAgendamento motoristaFolgaAgendamento, int cdUsuario){
		return save(motoristaFolgaAgendamento, cdUsuario, null, null);
	}

	public static Result save(MotoristaFolgaAgendamento motoristaFolgaAgendamento, int cdUsuario, AuthData authData){
		return save(motoristaFolgaAgendamento, cdUsuario, authData, null);
	}

	public static Result save(MotoristaFolgaAgendamento motoristaFolgaAgendamento, int cdUsuario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(motoristaFolgaAgendamento==null)
				return new Result(-1, "Erro ao salvar. MotoristaFolgaAgendamento é nulo");

			Pessoa motorista = PessoaDAO.get(motoristaFolgaAgendamento.getCdMotorista(), connect);
			
			int retorno;
			if(motoristaFolgaAgendamento.getCdFolgaAgendamento()==0){
				retorno = MotoristaFolgaAgendamentoDAO.insert(motoristaFolgaAgendamento, connect);
				motoristaFolgaAgendamento.setCdFolgaAgendamento(retorno);
				
				int cdTipoOcorrenciaMotoristaFolgaAgendamento = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_MOTORISTA_FOLGA_AGENDAMENTO, connect).getCdTipoOcorrencia();
				Ocorrencia ocorrencia = new Ocorrencia(0, 0, "Registrar folga para o motorista " + motorista.getNmPessoa() + " na data de " + Util.convCalendarString3(motoristaFolgaAgendamento.getDtAgendamento())+ ". Observação: " + motoristaFolgaAgendamento.getTxtObservacao(), Util.getDataAtual(), cdTipoOcorrenciaMotoristaFolgaAgendamento, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
				OcorrenciaServices.save(ocorrencia, connect);
			}
			else {
				retorno = MotoristaFolgaAgendamentoDAO.update(motoristaFolgaAgendamento, connect);
				
				int cdTipoOcorrenciaMotoristaFolgaAgendamento = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_MOTORISTA_FOLGA_AGENDAMENTO, connect).getCdTipoOcorrencia();
				Ocorrencia ocorrencia = new Ocorrencia(0, 0, "Alterar registro de folga para o motorista " + motorista.getNmPessoa() + " na data de " + Util.convCalendarString3(motoristaFolgaAgendamento.getDtAgendamento())+ ". Observação: " + motoristaFolgaAgendamento.getTxtObservacao(), Util.getDataAtual(), cdTipoOcorrenciaMotoristaFolgaAgendamento, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
				OcorrenciaServices.save(ocorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOTORISTAFOLGAAGENDAMENTO", motoristaFolgaAgendamento);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(MotoristaFolgaAgendamento motoristaFolgaAgendamento) {
		return remove(motoristaFolgaAgendamento.getCdFolgaAgendamento(), motoristaFolgaAgendamento.getCdMotorista());
	}
	public static Result remove(int cdFolgaAgendamento, int cdMotorista){
		return remove(cdFolgaAgendamento, cdMotorista, false, null, null);
	}
	public static Result remove(int cdFolgaAgendamento, int cdMotorista, boolean cascade){
		return remove(cdFolgaAgendamento, cdMotorista, cascade, null, null);
	}
	public static Result remove(int cdFolgaAgendamento, int cdMotorista, boolean cascade, AuthData authData){
		return remove(cdFolgaAgendamento, cdMotorista, cascade, authData, null);
	}
	public static Result remove(int cdFolgaAgendamento, int cdMotorista, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = MotoristaFolgaAgendamentoDAO.delete(cdFolgaAgendamento, cdMotorista, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_motorista_folga_agendamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_motorista_folga_agendamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByMotorista(int cdMotorista) {
		return getAllByMotorista(cdMotorista, null);
	}

	public static ResultSetMap getAllByMotorista(int cdMotorista, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga_agendamento A " +
																"WHERE A.cd_motorista  = " +cdMotorista).executeQuery());
			while(rsm.next()){
				
				
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAllByMotorista: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllByFolga(int cdFolga, int cdMotorista) {
		return getAllByFolga(cdFolga, cdMotorista, null);
	}

	public static ResultSetMap getAllByFolga(int cdFolga, int cdMotorista, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga_agendamento A " +
																"WHERE A.cd_folga  = " +cdFolga + 
																"  AND A.cd_motorista = " + cdMotorista).executeQuery());
			while(rsm.next()){
				
				
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAllByFolga: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllEfetivadoByMotorista(int cdMotorista) {
		return getAllEfetivadoByMotorista(cdMotorista, null);
	}

	public static ResultSetMap getAllEfetivadoByMotorista(int cdMotorista, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga_agendamento A " +
																"WHERE A.cd_motorista  = " +cdMotorista + 
																"  AND A.st_folga = " + ST_EFETIVADO).executeQuery());
			while(rsm.next()){
				
				
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAllEfetivadoByMotorista: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static MotoristaFolgaAgendamento getAgendadoByMotorista(int cdMotorista) {
		return getAgendadoByMotorista(cdMotorista, null);
	}

	public static MotoristaFolgaAgendamento getAgendadoByMotorista(int cdMotorista, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			MotoristaFolgaAgendamento motoristaFolgaAgendamento = null;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga_agendamento A " +
																"WHERE A.cd_motorista  = " +cdMotorista + 
																"  AND A.st_folga = " + ST_AGENDADO).executeQuery());
			while(rsm.next()){
				motoristaFolgaAgendamento = MotoristaFolgaAgendamentoDAO.get(rsm.getInt("cd_folga_agendamento"), cdMotorista, connection);
			}
			rsm.beforeFirst();
			
			return motoristaFolgaAgendamento;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAtualByMotorista: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static MotoristaFolgaAgendamento getAtualByMotorista(int cdMotorista) {
		return getAtualByMotorista(cdMotorista, null);
	}

	public static MotoristaFolgaAgendamento getAtualByMotorista(int cdMotorista, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			MotoristaFolgaAgendamento motoristaFolgaAgendamento = null;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga_agendamento A " +
																"WHERE A.cd_motorista  = " +cdMotorista + 
																"  AND A.st_folga = " + ST_EFETIVADO + 
																" ORDER BY dt_agendamento DESC").executeQuery());
			if(rsm.next()){
				motoristaFolgaAgendamento = MotoristaFolgaAgendamentoDAO.get(rsm.getInt("cd_folga_agendamento"), cdMotorista, connection);
			}
			rsm.beforeFirst();
			
			return motoristaFolgaAgendamento;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAtualByMotorista: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static MotoristaFolgaAgendamento getEmAtrasoByMotorista(int cdMotorista) {
		return getEmAtrasoByMotorista(cdMotorista, null);
	}

	public static MotoristaFolgaAgendamento getEmAtrasoByMotorista(int cdMotorista, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			MotoristaFolgaAgendamento motoristaFolgaAgendamento = null;
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga_agendamento A " +
																"WHERE A.cd_motorista  = " +cdMotorista + 
																"  AND A.st_folga = " + ST_EM_ATRASO + 
																" ORDER BY dt_agendamento DESC").executeQuery());
			
			if(rsm.next()){
				motoristaFolgaAgendamento = MotoristaFolgaAgendamentoDAO.get(rsm.getInt("cd_folga_agendamento"), cdMotorista, connection);
			}
			rsm.beforeFirst();
			
			return motoristaFolgaAgendamento;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaAgendamentoServices.getAtualByMotorista: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
		
	public static Result efetivarFolga(MotoristaFolgaAgendamento motoristaFolgaAgendamento, int cdUsuario){
		return efetivarFolga(motoristaFolgaAgendamento, cdUsuario, null);
	}

	public static Result efetivarFolga(MotoristaFolgaAgendamento motoristaFolgaAgendamento, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(motoristaFolgaAgendamento==null)
				return new Result(-1, "Erro ao salvar. MotoristaFolgaAgendamento é nulo");

			ResultSetMap rsmMotoristaFolga = MotoristaFolgaServices.getAllByMotorista(motoristaFolgaAgendamento.getCdMotorista(), connect);
			while(rsmMotoristaFolga.next()){
				ResultSetMap rsmMotoristaFolgaAgendamento = MotoristaFolgaAgendamentoServices.getAllByFolga(rsmMotoristaFolga.getInt("cd_folga"), rsmMotoristaFolga.getInt("cd_motorista"), connect);
				if(rsmMotoristaFolga.getInt("qt_dias") > rsmMotoristaFolgaAgendamento.size()){
					motoristaFolgaAgendamento.setCdFolga(rsmMotoristaFolga.getInt("cd_folga"));
					if(MotoristaFolgaAgendamentoDAO.update(motoristaFolgaAgendamento, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar folga agendamento");
					}
				}
			}
			rsmMotoristaFolga.beforeFirst();
			
			Pessoa motorista = PessoaDAO.get(motoristaFolgaAgendamento.getCdMotorista(), connect);
			
			motoristaFolgaAgendamento.setStFolga(ST_EFETIVADO);
			int retorno = MotoristaFolgaAgendamentoDAO.update(motoristaFolgaAgendamento, connect);
			
			int cdTipoOcorrenciaEfetivarFolgaMotorista = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_EFETIVAR_FOLGA_MOTORISTA, connect).getCdTipoOcorrencia();
			Ocorrencia ocorrencia = new Ocorrencia(0, 0, "Efetivada folga para o motorista " + motorista.getNmPessoa() + " na data de " + Util.convCalendarString3(Util.getDataAtual()), Util.getDataAtual(), cdTipoOcorrenciaEfetivarFolgaMotorista, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
			OcorrenciaServices.save(ocorrencia, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOTORISTAFOLGAAGENDAMENTO", motoristaFolgaAgendamento);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}