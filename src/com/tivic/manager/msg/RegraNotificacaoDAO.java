package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraNotificacaoDAO{

	public static int insert(RegraNotificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraNotificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("msg_regra_notificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegraNotificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO msg_regra_notificacao (cd_regra_notificacao,"+
			                                  "nm_regra_notificacao,"+
			                                  "ds_regra_notificacao,"+
			                                  "tp_entidade,"+
			                                  "tp_acao,"+
			                                  "lg_ativo,"+
			                                  "cd_processo,"+
			                                  "cd_grupo_trabalho,"+
			                                  "cd_andamento,"+
			                                  "cd_arquivo,"+
			                                  "cd_evento_financeiro,"+
			                                  "cd_advogado,"+
			                                  "cd_cliente,"+
			                                  "cd_agenda_item,"+
			                                  "cd_responsavel_agenda,"+
			                                  "tp_agenda_item,"+
			                                  "cd_tipo_andamento,"+
			                                  "lg_notificar_cliente,"+
			                                  "st_agenda_item,"+
			                                  "qt_horas_final_prazo,"+
			                                  "cd_grupo_responsavel_agenda,"+
			                                  "cd_tipo_prazo,"+
			                                  "lg_notificar_responsavel,"+
			                                  "lg_notificar_advogado,"+
			                                  "lg_notificar_grupo_trabalho,"+
			                                  "lg_mudanca_prazo,"+
			                                  "lg_mudanca_responsavel,"+
			                                  "lg_notificar_autor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmRegraNotificacao());
			pstmt.setString(3,objeto.getDsRegraNotificacao());
			pstmt.setInt(4,objeto.getTpEntidade());
			pstmt.setInt(5,objeto.getTpAcao());
			pstmt.setInt(6,objeto.getLgAtivo());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProcesso());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdGrupoTrabalho());
			if(objeto.getCdAndamento()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdAndamento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdArquivo());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdEventoFinanceiro());
			if(objeto.getCdAdvogado()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdAdvogado());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdCliente());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdAgendaItem());
			if(objeto.getCdResponsavelAgenda()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdResponsavelAgenda());
			pstmt.setInt(16,objeto.getTpAgendaItem());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTipoAndamento());
			pstmt.setInt(18,objeto.getLgNotificarCliente());
			pstmt.setInt(19,objeto.getStAgendaItem());
			pstmt.setInt(20,objeto.getQtHorasFinalPrazo());
			if(objeto.getCdGrupoResponsavelAgenda()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdGrupoResponsavelAgenda());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdTipoPrazo());
			pstmt.setInt(23,objeto.getLgNotificarResponsavel());
			pstmt.setInt(24,objeto.getLgNotificarAdvogado());
			pstmt.setInt(25,objeto.getLgNotificarGrupoTrabalho());
			pstmt.setInt(26,objeto.getLgMudancaPrazo());
			pstmt.setInt(27,objeto.getLgMudancaResponsavel());
			pstmt.setInt(28,objeto.getLgNotificarAutor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraNotificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(RegraNotificacao objeto, int cdRegraNotificacaoOld) {
		return update(objeto, cdRegraNotificacaoOld, null);
	}

	public static int update(RegraNotificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(RegraNotificacao objeto, int cdRegraNotificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE msg_regra_notificacao SET cd_regra_notificacao=?,"+
												      		   "nm_regra_notificacao=?,"+
												      		   "ds_regra_notificacao=?,"+
												      		   "tp_entidade=?,"+
												      		   "tp_acao=?,"+
												      		   "lg_ativo=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_grupo_trabalho=?,"+
												      		   "cd_andamento=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "cd_advogado=?,"+
												      		   "cd_cliente=?,"+
												      		   "cd_agenda_item=?,"+
												      		   "cd_responsavel_agenda=?,"+
												      		   "tp_agenda_item=?,"+
												      		   "cd_tipo_andamento=?,"+
												      		   "lg_notificar_cliente=?,"+
												      		   "st_agenda_item=?,"+
												      		   "qt_horas_final_prazo=?,"+
												      		   "cd_grupo_responsavel_agenda=?,"+
												      		   "cd_tipo_prazo=?,"+
												      		   "lg_notificar_responsavel=?,"+
												      		   "lg_notificar_advogado=?,"+
												      		   "lg_notificar_grupo_trabalho=?,"+
												      		   "lg_mudanca_prazo=?,"+
												      		   "lg_mudanca_responsavel=?,"+
												      		   "lg_notificar_autor=? WHERE cd_regra_notificacao=?");
			pstmt.setInt(1,objeto.getCdRegraNotificacao());
			pstmt.setString(2,objeto.getNmRegraNotificacao());
			pstmt.setString(3,objeto.getDsRegraNotificacao());
			pstmt.setInt(4,objeto.getTpEntidade());
			pstmt.setInt(5,objeto.getTpAcao());
			pstmt.setInt(6,objeto.getLgAtivo());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProcesso());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdGrupoTrabalho());
			if(objeto.getCdAndamento()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdAndamento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdArquivo());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdEventoFinanceiro());
			if(objeto.getCdAdvogado()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdAdvogado());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdCliente());
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdAgendaItem());
			if(objeto.getCdResponsavelAgenda()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdResponsavelAgenda());
			pstmt.setInt(16,objeto.getTpAgendaItem());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTipoAndamento());
			pstmt.setInt(18,objeto.getLgNotificarCliente());
			pstmt.setInt(19,objeto.getStAgendaItem());
			pstmt.setInt(20,objeto.getQtHorasFinalPrazo());
			if(objeto.getCdGrupoResponsavelAgenda()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdGrupoResponsavelAgenda());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdTipoPrazo());
			pstmt.setInt(23,objeto.getLgNotificarResponsavel());
			pstmt.setInt(24,objeto.getLgNotificarAdvogado());
			pstmt.setInt(25,objeto.getLgNotificarGrupoTrabalho());
			pstmt.setInt(26,objeto.getLgMudancaPrazo());
			pstmt.setInt(27,objeto.getLgMudancaResponsavel());
			pstmt.setInt(28,objeto.getLgNotificarAutor());
			pstmt.setInt(29, cdRegraNotificacaoOld!=0 ? cdRegraNotificacaoOld : objeto.getCdRegraNotificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraNotificacao) {
		return delete(cdRegraNotificacao, null);
	}

	public static int delete(int cdRegraNotificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_regra_notificacao WHERE cd_regra_notificacao=?");
			pstmt.setInt(1, cdRegraNotificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraNotificacao get(int cdRegraNotificacao) {
		return get(cdRegraNotificacao, null);
	}

	public static RegraNotificacao get(int cdRegraNotificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM msg_regra_notificacao WHERE cd_regra_notificacao=?");
			pstmt.setInt(1, cdRegraNotificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraNotificacao(rs.getInt("cd_regra_notificacao"),
						rs.getString("nm_regra_notificacao"),
						rs.getString("ds_regra_notificacao"),
						rs.getInt("tp_entidade"),
						rs.getInt("tp_acao"),
						rs.getInt("lg_ativo"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_grupo_trabalho"),
						rs.getInt("cd_andamento"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_evento_financeiro"),
						rs.getInt("cd_advogado"),
						rs.getInt("cd_cliente"),
						rs.getInt("cd_agenda_item"),
						rs.getInt("cd_responsavel_agenda"),
						rs.getInt("tp_agenda_item"),
						rs.getInt("cd_tipo_andamento"),
						rs.getInt("lg_notificar_cliente"),
						rs.getInt("st_agenda_item"),
						rs.getInt("qt_horas_final_prazo"),
						rs.getInt("cd_grupo_responsavel_agenda"),
						rs.getInt("cd_tipo_prazo"),
						rs.getInt("lg_notificar_responsavel"),
						rs.getInt("lg_notificar_advogado"),
						rs.getInt("lg_notificar_grupo_trabalho"),
						rs.getInt("lg_mudanca_prazo"),
						rs.getInt("lg_mudanca_responsavel"),
						rs.getInt("lg_notificar_autor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_regra_notificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraNotificacao> getList() {
		return getList(null);
	}

	public static ArrayList<RegraNotificacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraNotificacao> list = new ArrayList<RegraNotificacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraNotificacao obj = RegraNotificacaoDAO.get(rsm.getInt("cd_regra_notificacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM msg_regra_notificacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}