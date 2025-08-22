package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AtendimentoDAO{

	public static int insert(Atendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Atendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_atendimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtendimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_atendimento (cd_atendimento,"+
			                                  "cd_central,"+
			                                  "cd_pessoa,"+
			                                  "st_atendimento,"+
			                                  "dt_previsao_resposta,"+
			                                  "tp_relevancia,"+
			                                  "txt_relevancia,"+
			                                  "tp_avaliacao,"+
			                                  "txt_avaliacao,"+
			                                  "id_atendimento,"+
			                                  "ds_senha,"+
			                                  "dt_admissao,"+
			                                  "tp_usuario,"+
			                                  "cd_central_responsavel,"+
			                                  "cd_atendente_responsavel,"+
			                                  "tp_classificacao,"+
			                                  "cd_forma_divulgacao,"+
			                                  "cd_tipo_atendimento,"+
			                                  "cd_forma_contato,"+
			                                  "cd_atendimento_superior,"+
			                                  "cd_produto_servico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCentral()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCentral());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setInt(4,objeto.getStAtendimento());
			if(objeto.getDtPrevisaoResposta()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtPrevisaoResposta().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpRelevancia());
			pstmt.setString(7,objeto.getTxtRelevancia());
			pstmt.setInt(8,objeto.getTpAvaliacao());
			pstmt.setString(9,objeto.getTxtAvaliacao());
			pstmt.setString(10,objeto.getIdAtendimento());
			pstmt.setString(11,objeto.getDsSenha());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			pstmt.setInt(13,objeto.getTpUsuario());
			if(objeto.getCdCentralResponsavel()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCentralResponsavel());
			if(objeto.getCdAtendenteResponsavel()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdAtendenteResponsavel());
			pstmt.setInt(16,objeto.getTpClassificacao());
			if(objeto.getCdFormaDivulgacao()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdFormaDivulgacao());
			if(objeto.getCdTipoAtendimento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTipoAtendimento());
			if(objeto.getCdFormaContato()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdFormaContato());
			if(objeto.getCdAtendimentoSuperior()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdAtendimentoSuperior());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Atendimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Atendimento objeto, int cdAtendimentoOld) {
		return update(objeto, cdAtendimentoOld, null);
	}

	public static int update(Atendimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Atendimento objeto, int cdAtendimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_atendimento SET cd_atendimento=?,"+
												      		   "cd_central=?,"+
												      		   "cd_pessoa=?,"+
												      		   "st_atendimento=?,"+
												      		   "dt_previsao_resposta=?,"+
												      		   "tp_relevancia=?,"+
												      		   "txt_relevancia=?,"+
												      		   "tp_avaliacao=?,"+
												      		   "txt_avaliacao=?,"+
												      		   "id_atendimento=?,"+
												      		   "ds_senha=?,"+
												      		   "dt_admissao=?,"+
												      		   "tp_usuario=?,"+
												      		   "cd_central_responsavel=?,"+
												      		   "cd_atendente_responsavel=?,"+
												      		   "tp_classificacao=?,"+
												      		   "cd_forma_divulgacao=?,"+
												      		   "cd_tipo_atendimento=?,"+
												      		   "cd_forma_contato=?,"+
												      		   "cd_atendimento_superior=?,"+
												      		   "cd_produto_servico=? WHERE cd_atendimento=?");
			pstmt.setInt(1,objeto.getCdAtendimento());
			if(objeto.getCdCentral()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCentral());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setInt(4,objeto.getStAtendimento());
			if(objeto.getDtPrevisaoResposta()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtPrevisaoResposta().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpRelevancia());
			pstmt.setString(7,objeto.getTxtRelevancia());
			pstmt.setInt(8,objeto.getTpAvaliacao());
			pstmt.setString(9,objeto.getTxtAvaliacao());
			pstmt.setString(10,objeto.getIdAtendimento());
			pstmt.setString(11,objeto.getDsSenha());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			pstmt.setInt(13,objeto.getTpUsuario());
			if(objeto.getCdCentralResponsavel()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCentralResponsavel());
			if(objeto.getCdAtendenteResponsavel()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdAtendenteResponsavel());
			pstmt.setInt(16,objeto.getTpClassificacao());
			if(objeto.getCdFormaDivulgacao()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdFormaDivulgacao());
			if(objeto.getCdTipoAtendimento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTipoAtendimento());
			if(objeto.getCdFormaContato()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdFormaContato());
			if(objeto.getCdAtendimentoSuperior()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdAtendimentoSuperior());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdProdutoServico());
			pstmt.setInt(22, cdAtendimentoOld!=0 ? cdAtendimentoOld : objeto.getCdAtendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtendimento) {
		return delete(cdAtendimento, null);
	}

	public static int delete(int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_atendimento WHERE cd_atendimento=?");
			pstmt.setInt(1, cdAtendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Atendimento get(int cdAtendimento) {
		return get(cdAtendimento, null);
	}

	public static Atendimento get(int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_atendimento WHERE cd_atendimento=?");
			pstmt.setInt(1, cdAtendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Atendimento(rs.getInt("cd_atendimento"),
						rs.getInt("cd_central"),
						rs.getInt("cd_pessoa"),
						rs.getInt("st_atendimento"),
						(rs.getTimestamp("dt_previsao_resposta")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_previsao_resposta").getTime()),
						rs.getInt("tp_relevancia"),
						rs.getString("txt_relevancia"),
						rs.getInt("tp_avaliacao"),
						rs.getString("txt_avaliacao"),
						rs.getString("id_atendimento"),
						rs.getString("ds_senha"),
						(rs.getTimestamp("dt_admissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_admissao").getTime()),
						rs.getInt("tp_usuario"),
						rs.getInt("cd_central_responsavel"),
						rs.getInt("cd_atendente_responsavel"),
						rs.getInt("tp_classificacao"),
						rs.getInt("cd_forma_divulgacao"),
						rs.getInt("cd_tipo_atendimento"),
						rs.getInt("cd_forma_contato"),
						rs.getInt("cd_atendimento_superior"),
						rs.getInt("cd_produto_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_atendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_atendimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
