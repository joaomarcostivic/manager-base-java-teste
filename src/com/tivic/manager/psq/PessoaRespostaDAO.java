package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PessoaRespostaDAO{

	public static int insert(PessoaResposta objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(PessoaResposta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[5];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_pessoa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_questionario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdQuestionario()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_questao_valor");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdQuestaoValor()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_questao");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdQuestao()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_resposta");
			keys[4].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("psq_pessoa_resposta", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdResposta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_pessoa_resposta (cd_pessoa,"+
			                                  "cd_questionario,"+
			                                  "cd_questao_valor,"+
			                                  "cd_questao,"+
			                                  "cd_resposta,"+
			                                  "cd_digitador,"+
			                                  "cd_empresa_digitador,"+
			                                  "cd_vinculo_digitador,"+
			                                  "dt_resposta,"+
			                                  "blb_resposta,"+
			                                  "txt_observacao,"+
			                                  "cd_aplicacao,"+
			                                  "cd_resultado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdQuestionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdQuestionario());
			if(objeto.getCdQuestaoValor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdQuestaoValor());
			if(objeto.getCdQuestao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdQuestao());
			pstmt.setInt(5, code);
			if(objeto.getCdDigitador()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDigitador());
			if(objeto.getCdEmpresaDigitador()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEmpresaDigitador());
			if(objeto.getCdVinculoDigitador()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdVinculoDigitador());
			if(objeto.getDtResposta()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtResposta().getTimeInMillis()));
			if(objeto.getBlbResposta()==null)
				pstmt.setNull(10, Types.BINARY);
			else
				pstmt.setBytes(10,objeto.getBlbResposta());
			pstmt.setString(11,objeto.getTxtObservacao());
			if(objeto.getCdAplicacao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdAplicacao());
			if(objeto.getCdResultado()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdResultado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaResposta objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(PessoaResposta objeto, int cdPessoaOld, int cdQuestionarioOld, int cdQuestaoValorOld, int cdQuestaoOld, int cdRespostaOld) {
		return update(objeto, cdPessoaOld, cdQuestionarioOld, cdQuestaoValorOld, cdQuestaoOld, cdRespostaOld, null);
	}

	public static int update(PessoaResposta objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(PessoaResposta objeto, int cdPessoaOld, int cdQuestionarioOld, int cdQuestaoValorOld, int cdQuestaoOld, int cdRespostaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_pessoa_resposta SET cd_pessoa=?,"+
												      		   "cd_questionario=?,"+
												      		   "cd_questao_valor=?,"+
												      		   "cd_questao=?,"+
												      		   "cd_resposta=?,"+
												      		   "cd_digitador=?,"+
												      		   "cd_empresa_digitador=?,"+
												      		   "cd_vinculo_digitador=?,"+
												      		   "dt_resposta=?,"+
												      		   "blb_resposta=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_aplicacao=?,"+
												      		   "cd_resultado=? WHERE cd_pessoa=? AND cd_questionario=? AND cd_questao_valor=? AND cd_questao=? AND cd_resposta=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdQuestionario());
			pstmt.setInt(3,objeto.getCdQuestaoValor());
			pstmt.setInt(4,objeto.getCdQuestao());
			pstmt.setInt(5,objeto.getCdResposta());
			if(objeto.getCdDigitador()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDigitador());
			if(objeto.getCdEmpresaDigitador()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEmpresaDigitador());
			if(objeto.getCdVinculoDigitador()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdVinculoDigitador());
			if(objeto.getDtResposta()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtResposta().getTimeInMillis()));
			if(objeto.getBlbResposta()==null)
				pstmt.setNull(10, Types.BINARY);
			else
				pstmt.setBytes(10,objeto.getBlbResposta());
			pstmt.setString(11,objeto.getTxtObservacao());
			if(objeto.getCdAplicacao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdAplicacao());
			if(objeto.getCdResultado()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdResultado());
			pstmt.setInt(14, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(15, cdQuestionarioOld!=0 ? cdQuestionarioOld : objeto.getCdQuestionario());
			pstmt.setInt(16, cdQuestaoValorOld!=0 ? cdQuestaoValorOld : objeto.getCdQuestaoValor());
			pstmt.setInt(17, cdQuestaoOld!=0 ? cdQuestaoOld : objeto.getCdQuestao());
			pstmt.setInt(18, cdRespostaOld!=0 ? cdRespostaOld : objeto.getCdResposta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdQuestionario, int cdQuestaoValor, int cdQuestao, int cdResposta) {
		return delete(cdPessoa, cdQuestionario, cdQuestaoValor, cdQuestao, cdResposta, null);
	}

	public static int delete(int cdPessoa, int cdQuestionario, int cdQuestaoValor, int cdQuestao, int cdResposta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_pessoa_resposta WHERE cd_pessoa=? AND cd_questionario=? AND cd_questao_valor=? AND cd_questao=? AND cd_resposta=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdQuestionario);
			pstmt.setInt(3, cdQuestaoValor);
			pstmt.setInt(4, cdQuestao);
			pstmt.setInt(5, cdResposta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaResposta get(int cdPessoa, int cdQuestionario, int cdQuestaoValor, int cdQuestao, int cdResposta) {
		return get(cdPessoa, cdQuestionario, cdQuestaoValor, cdQuestao, cdResposta, null);
	}

	public static PessoaResposta get(int cdPessoa, int cdQuestionario, int cdQuestaoValor, int cdQuestao, int cdResposta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_pessoa_resposta WHERE cd_pessoa=? AND cd_questionario=? AND cd_questao_valor=? AND cd_questao=? AND cd_resposta=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdQuestionario);
			pstmt.setInt(3, cdQuestaoValor);
			pstmt.setInt(4, cdQuestao);
			pstmt.setInt(5, cdResposta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaResposta(rs.getInt("cd_pessoa"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_questao_valor"),
						rs.getInt("cd_questao"),
						rs.getInt("cd_resposta"),
						rs.getInt("cd_digitador"),
						rs.getInt("cd_empresa_digitador"),
						rs.getInt("cd_vinculo_digitador"),
						(rs.getTimestamp("dt_resposta")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resposta").getTime()),
						rs.getBytes("blb_resposta")==null?null:rs.getBytes("blb_resposta"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_aplicacao"),
						rs.getInt("cd_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_pessoa_resposta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_pessoa_resposta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
