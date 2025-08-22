package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class FormacaoDAO{

	public static int insert(Formacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Formacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_pessoa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_formacao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("acd_formacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_formacao (cd_pessoa,"+
			                                  "cd_formacao,"+
			                                  "cd_agencia_financiadora,"+
			                                  "cd_instituicao,"+
			                                  "st_formacao,"+
			                                  "nr_ano_inicio,"+
			                                  "nr_ano_termino,"+
			                                  "tp_nivel,"+
			                                  "nm_curso,"+
			                                  "qt_carga_horaria,"+
			                                  "nm_titulo_trabalho,"+
			                                  "nm_orientador,"+
			                                  "nr_mes_obtencao_titulo,"+
			                                  "nr_ano_obtencao_titulo,"+
			                                  "tp_doutorado,"+
			                                  "tp_formacao_complementar,"+
			                                  "tp_mestrado,"+
			                                  "tp_especializacao,"+
			                                  "nm_instituicao,"+
			                                  "lg_complementacao_pedagogica,"+
			                                  "cd_formacao_curso,"+
			                                  "cd_instituicao_superior,"+
			                                  "tp_instituicao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2, code);
			if(objeto.getCdAgenciaFinanciadora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgenciaFinanciadora());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInstituicao());
			pstmt.setInt(5,objeto.getStFormacao());
			pstmt.setString(6,objeto.getNrAnoInicio());
			pstmt.setString(7,objeto.getNrAnoTermino());
			pstmt.setInt(8,objeto.getTpNivel());
			pstmt.setString(9,objeto.getNmCurso());
			pstmt.setInt(10,objeto.getQtCargaHoraria());
			pstmt.setString(11,objeto.getNmTituloTrabalho());
			pstmt.setString(12,objeto.getNmOrientador());
			pstmt.setInt(13,objeto.getNrMesObtencaoTitulo());
			pstmt.setString(14,objeto.getNrAnoObtencaoTitulo());
			pstmt.setInt(15,objeto.getTpDoutorado());
			pstmt.setInt(16,objeto.getTpFormacaoComplementar());
			pstmt.setInt(17,objeto.getTpMestrado());
			pstmt.setInt(18,objeto.getTpEspecializacao());
			pstmt.setString(19,objeto.getNmInstituicao());
			pstmt.setInt(20,objeto.getLgComplementacaoPedagogica());
			if(objeto.getCdFormacaoCurso()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdFormacaoCurso());
			pstmt.setInt(22,objeto.getCdInstituicaoSuperior());
			pstmt.setInt(23,objeto.getTpInstituicao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Formacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Formacao objeto, int cdPessoaOld, int cdFormacaoOld) {
		return update(objeto, cdPessoaOld, cdFormacaoOld, null);
	}

	public static int update(Formacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Formacao objeto, int cdPessoaOld, int cdFormacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_formacao SET cd_pessoa=?,"+
												      		   "cd_formacao=?,"+
												      		   "cd_agencia_financiadora=?,"+
												      		   "cd_instituicao=?,"+
												      		   "st_formacao=?,"+
												      		   "nr_ano_inicio=?,"+
												      		   "nr_ano_termino=?,"+
												      		   "tp_nivel=?,"+
												      		   "nm_curso=?,"+
												      		   "qt_carga_horaria=?,"+
												      		   "nm_titulo_trabalho=?,"+
												      		   "nm_orientador=?,"+
												      		   "nr_mes_obtencao_titulo=?,"+
												      		   "nr_ano_obtencao_titulo=?,"+
												      		   "tp_doutorado=?,"+
												      		   "tp_formacao_complementar=?,"+
												      		   "tp_mestrado=?,"+
												      		   "tp_especializacao=?,"+
												      		   "nm_instituicao=?,"+
												      		   "lg_complementacao_pedagogica=?,"+
												      		   "cd_formacao_curso=?,"+
												      		   "cd_instituicao_superior=?,"+
												      		   "tp_instituicao=? WHERE cd_pessoa=? AND cd_formacao=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdFormacao());
			if(objeto.getCdAgenciaFinanciadora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgenciaFinanciadora());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdInstituicao());
			pstmt.setInt(5,objeto.getStFormacao());
			pstmt.setString(6,objeto.getNrAnoInicio());
			pstmt.setString(7,objeto.getNrAnoTermino());
			pstmt.setInt(8,objeto.getTpNivel());
			pstmt.setString(9,objeto.getNmCurso());
			pstmt.setInt(10,objeto.getQtCargaHoraria());
			pstmt.setString(11,objeto.getNmTituloTrabalho());
			pstmt.setString(12,objeto.getNmOrientador());
			pstmt.setInt(13,objeto.getNrMesObtencaoTitulo());
			pstmt.setString(14,objeto.getNrAnoObtencaoTitulo());
			pstmt.setInt(15,objeto.getTpDoutorado());
			pstmt.setInt(16,objeto.getTpFormacaoComplementar());
			pstmt.setInt(17,objeto.getTpMestrado());
			pstmt.setInt(18,objeto.getTpEspecializacao());
			pstmt.setString(19,objeto.getNmInstituicao());
			pstmt.setInt(20,objeto.getLgComplementacaoPedagogica());
			if(objeto.getCdFormacaoCurso()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdFormacaoCurso());
			pstmt.setInt(22,objeto.getCdInstituicaoSuperior());
			pstmt.setInt(23,objeto.getTpInstituicao());
			pstmt.setInt(24, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(25, cdFormacaoOld!=0 ? cdFormacaoOld : objeto.getCdFormacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdFormacao) {
		return delete(cdPessoa, cdFormacao, null);
	}

	public static int delete(int cdPessoa, int cdFormacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_formacao WHERE cd_pessoa=? AND cd_formacao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdFormacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Formacao get(int cdPessoa, int cdFormacao) {
		return get(cdPessoa, cdFormacao, null);
	}

	public static Formacao get(int cdPessoa, int cdFormacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao WHERE cd_pessoa=? AND cd_formacao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdFormacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Formacao(rs.getInt("cd_pessoa"),
						rs.getInt("cd_formacao"),
						rs.getInt("cd_agencia_financiadora"),
						rs.getInt("cd_instituicao"),
						rs.getInt("st_formacao"),
						rs.getString("nr_ano_inicio"),
						rs.getString("nr_ano_termino"),
						rs.getInt("tp_nivel"),
						rs.getString("nm_curso"),
						rs.getInt("qt_carga_horaria"),
						rs.getString("nm_titulo_trabalho"),
						rs.getString("nm_orientador"),
						rs.getInt("nr_mes_obtencao_titulo"),
						rs.getString("nr_ano_obtencao_titulo"),
						rs.getInt("tp_doutorado"),
						rs.getInt("tp_formacao_complementar"),
						rs.getInt("tp_mestrado"),
						rs.getInt("tp_especializacao"),
						rs.getString("nm_instituicao"),
						rs.getInt("lg_complementacao_pedagogica"),
						rs.getInt("cd_formacao_curso"),
						rs.getInt("cd_instituicao_superior"),
						rs.getInt("tp_instituicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Formacao> getList() {
		return getList(null);
	}

	public static ArrayList<Formacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Formacao> list = new ArrayList<Formacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Formacao obj = FormacaoDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_formacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_formacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
