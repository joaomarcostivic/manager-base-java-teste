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

public class MotoristaFolgaServices {

	public static Result save(MotoristaFolga motoristaFolga, int cdUsuario){
		return save(motoristaFolga, cdUsuario, null, null);
	}

	public static Result save(MotoristaFolga motoristaFolga, int cdUsuario, AuthData authData){
		return save(motoristaFolga, cdUsuario, authData, null);
	}

	public static Result save(MotoristaFolga motoristaFolga, int cdUsuario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(motoristaFolga==null)
				return new Result(-1, "Erro ao salvar. MotoristaFolga é nulo");

			Pessoa motorista = PessoaDAO.get(motoristaFolga.getCdMotorista(), connect);
			
			int retorno;
			if(motoristaFolga.getCdFolga()==0){
				retorno = MotoristaFolgaDAO.insert(motoristaFolga, connect);
				motoristaFolga.setCdFolga(retorno);
				
				int cdTipoOcorrenciaMotoristaFolga = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_MOTORISTA_FOLGA, connect).getCdTipoOcorrencia();
				Ocorrencia ocorrencia = new Ocorrencia(0, 0, "Adicionada folga para o motorista " + motorista.getNmPessoa() + " de " + motoristaFolga.getQtDias() + " dia(s). Motivo: " + motoristaFolga.getTxtMotivo(), Util.getDataAtual(), cdTipoOcorrenciaMotoristaFolga, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
				OcorrenciaServices.save(ocorrencia, connect);
				
			}
			else {
				retorno = MotoristaFolgaDAO.update(motoristaFolga, connect);
				
				int cdTipoOcorrenciaMotoristaFolga = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ATUALIZAR_MOTORISTA_FOLGA, connect).getCdTipoOcorrencia();
				Ocorrencia ocorrencia = new Ocorrencia(0, 0, "Atualizada folga para o motorista " + motorista.getNmPessoa() + " de " + motoristaFolga.getQtDias() + " dia(s). Motivo: " + motoristaFolga.getTxtMotivo(), Util.getDataAtual(), cdTipoOcorrenciaMotoristaFolga, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
				OcorrenciaServices.save(ocorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOTORISTAFOLGA", motoristaFolga);
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
	public static Result remove(MotoristaFolga motoristaFolga) {
		return remove(motoristaFolga.getCdFolga(), motoristaFolga.getCdMotorista());
	}
	public static Result remove(int cdFolga, int cdMotorista){
		return remove(cdFolga, cdMotorista, false, null, null);
	}
	public static Result remove(int cdFolga, int cdMotorista, boolean cascade){
		return remove(cdFolga, cdMotorista, cascade, null, null);
	}
	public static Result remove(int cdFolga, int cdMotorista, boolean cascade, AuthData authData){
		return remove(cdFolga, cdMotorista, cascade, authData, null);
	}
	public static Result remove(int cdFolga, int cdMotorista, boolean cascade, AuthData authData, Connection connect){
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
			retorno = MotoristaFolgaDAO.delete(cdFolga, cdMotorista, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_motorista_folga");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaFolgaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_motorista_folga", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);	
	}
	
	public static ResultSetMap getAllByMotorista(int cdMotorista) {
		return getAllByMotorista(cdMotorista, false, null);
	}
	
	public static ResultSetMap getAllByMotorista(int cdMotorista, Connection connection) {
		return getAllByMotorista(cdMotorista, false, connection);
	}
	
	public static ResultSetMap getAllByMotorista(int cdMotorista, boolean apenasLivres) {
		return getAllByMotorista(cdMotorista, apenasLivres, null);
	}

	public static ResultSetMap getAllByMotorista(int cdMotorista, boolean apenasLivres, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM mob_motorista_folga A " +
																"WHERE A.cd_motorista  = " +cdMotorista+
																"ORDER BY dt_registro DESC").executeQuery());
			int x = 0;
			while(rsm.next()){
				rsm.setValueToField("CL_DT_REGISTRO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_registro")));
				
				if(apenasLivres){
					ResultSetMap rsmMotoristaFolgaAgendamento = MotoristaFolgaAgendamentoServices.getAllByFolga(rsm.getInt("cd_folga"), rsm.getInt("cd_motorista"), connection);
					if(rsm.getInt("qt_dias") == rsmMotoristaFolgaAgendamento.size()){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
					else{
						rsm.setValueToField("QT_DIAS_LIVRES", (rsm.getInt("qt_dias") - rsmMotoristaFolgaAgendamento.size()));
						rsm.setValueToField("QT_DIAS_USADOS", rsmMotoristaFolgaAgendamento.size());
					}
				}
				x++;
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getMotoristas: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}