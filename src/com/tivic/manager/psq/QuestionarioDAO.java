package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class QuestionarioDAO{

	public static int insert(Questionario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Questionario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("psq_questionario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdQuestionario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_questionario (cd_questionario,"+
			                                  "cd_grupo_questionario,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_pessoa,"+
			                                  "cd_empresa_pessoa,"+
			                                  "cd_vinculo,"+
			                                  "cd_usuario,"+
			                                  "cd_conteudo,"+
			                                  "nm_questionario,"+
			                                  "txt_questionario,"+
			                                  "nm_local,"+
			                                  "dt_inicio,"+
			                                  "dt_termino,"+
			                                  "lg_randomico,"+
			                                  "nr_randomico,"+
			                                  "lg_ajuda,"+
			                                  "txt_ajuda,"+
			                                  "ds_dica,"+
			                                  "lg_cabecalho,"+
			                                  "lg_rodape,"+
			                                  "txt_cabecalho,"+
			                                  "img_cabecalho,"+
			                                  "txt_rodape,"+
			                                  "txt_introducao,"+
			                                  "txt_agradecimento,"+
			                                  "qt_tempo_resposta,"+
			                                  "id_questionario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdGrupoQuestionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoQuestionario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdEmpresaPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresaPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdVinculo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			if(objeto.getCdConteudo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdConteudo());
			pstmt.setString(10,objeto.getNmQuestionario());
			pstmt.setString(11,objeto.getTxtQuestionario());
			pstmt.setString(12,objeto.getNmLocal());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(15,objeto.getLgRandomico());
			pstmt.setInt(16,objeto.getNrRandomico());
			pstmt.setInt(17,objeto.getLgAjuda());
			pstmt.setString(18,objeto.getTxtAjuda());
			pstmt.setString(19,objeto.getDsDica());
			pstmt.setInt(20,objeto.getLgCabecalho());
			pstmt.setInt(21,objeto.getLgRodape());
			pstmt.setString(22,objeto.getTxtCabecalho());
			if(objeto.getImgCabecalho()==null)
				pstmt.setNull(23, Types.BINARY);
			else
				pstmt.setBytes(23,objeto.getImgCabecalho());
			pstmt.setString(24,objeto.getTxtRodape());
			pstmt.setString(25,objeto.getTxtIntroducao());
			pstmt.setString(26,objeto.getTxtAgradecimento());
			pstmt.setInt(27,objeto.getQtTempoResposta());
			pstmt.setString(28,objeto.getIdQuestionario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Questionario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Questionario objeto, int cdQuestionarioOld) {
		return update(objeto, cdQuestionarioOld, null);
	}

	public static int update(Questionario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Questionario objeto, int cdQuestionarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_questionario SET cd_questionario=?,"+
												      		   "cd_grupo_questionario=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_empresa_pessoa=?,"+
												      		   "cd_vinculo=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_conteudo=?,"+
												      		   "nm_questionario=?,"+
												      		   "txt_questionario=?,"+
												      		   "nm_local=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_termino=?,"+
												      		   "lg_randomico=?,"+
												      		   "nr_randomico=?,"+
												      		   "lg_ajuda=?,"+
												      		   "txt_ajuda=?,"+
												      		   "ds_dica=?,"+
												      		   "lg_cabecalho=?,"+
												      		   "lg_rodape=?,"+
												      		   "txt_cabecalho=?,"+
												      		   "img_cabecalho=?,"+
												      		   "txt_rodape=?,"+
												      		   "txt_introducao=?,"+
												      		   "txt_agradecimento=?,"+
												      		   "qt_tempo_resposta=?,"+
												      		   "id_questionario=? WHERE cd_questionario=?");
			pstmt.setInt(1,objeto.getCdQuestionario());
			if(objeto.getCdGrupoQuestionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoQuestionario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdEmpresaPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresaPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdVinculo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			if(objeto.getCdConteudo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdConteudo());
			pstmt.setString(10,objeto.getNmQuestionario());
			pstmt.setString(11,objeto.getTxtQuestionario());
			pstmt.setString(12,objeto.getNmLocal());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(15,objeto.getLgRandomico());
			pstmt.setInt(16,objeto.getNrRandomico());
			pstmt.setInt(17,objeto.getLgAjuda());
			pstmt.setString(18,objeto.getTxtAjuda());
			pstmt.setString(19,objeto.getDsDica());
			pstmt.setInt(20,objeto.getLgCabecalho());
			pstmt.setInt(21,objeto.getLgRodape());
			pstmt.setString(22,objeto.getTxtCabecalho());
			if(objeto.getImgCabecalho()==null)
				pstmt.setNull(23, Types.BINARY);
			else
				pstmt.setBytes(23,objeto.getImgCabecalho());
			pstmt.setString(24,objeto.getTxtRodape());
			pstmt.setString(25,objeto.getTxtIntroducao());
			pstmt.setString(26,objeto.getTxtAgradecimento());
			pstmt.setInt(27,objeto.getQtTempoResposta());
			pstmt.setString(28,objeto.getIdQuestionario());
			pstmt.setInt(29, cdQuestionarioOld!=0 ? cdQuestionarioOld : objeto.getCdQuestionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuestionario) {
		return delete(cdQuestionario, null);
	}

	public static int delete(int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_questionario WHERE cd_questionario=?");
			pstmt.setInt(1, cdQuestionario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Questionario get(int cdQuestionario) {
		return get(cdQuestionario, null);
	}

	public static Questionario get(int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_questionario WHERE cd_questionario=?");
			pstmt.setInt(1, cdQuestionario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Questionario(rs.getInt("cd_questionario"),
						rs.getInt("cd_grupo_questionario"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_empresa_pessoa"),
						rs.getInt("cd_vinculo"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_conteudo"),
						rs.getString("nm_questionario"),
						rs.getString("txt_questionario"),
						rs.getString("nm_local"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino").getTime()),
						rs.getInt("lg_randomico"),
						rs.getInt("nr_randomico"),
						rs.getInt("lg_ajuda"),
						rs.getString("txt_ajuda"),
						rs.getString("ds_dica"),
						rs.getInt("lg_cabecalho"),
						rs.getInt("lg_rodape"),
						rs.getString("txt_cabecalho"),
						rs.getBytes("img_cabecalho")==null?null:rs.getBytes("img_cabecalho"),
						rs.getString("txt_rodape"),
						rs.getString("txt_introducao"),
						rs.getString("txt_agradecimento"),
						rs.getInt("qt_tempo_resposta"),
						rs.getString("id_questionario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_questionario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_questionario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
