package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class OfertaAvaliacaoDAO{

	public static int insert(OfertaAvaliacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(OfertaAvaliacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_oferta_avaliacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_oferta");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdOferta()));
			int code = Conexao.getSequenceCode("acd_oferta_avaliacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOfertaAvaliacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_oferta_avaliacao (cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "cd_tipo_avaliacao,"+
			                                  "cd_curso,"+
			                                  "cd_unidade,"+
			                                  "cd_disciplina_avaliacao,"+
			                                  "cd_curso_periodo,"+
			                                  "cd_disciplina,"+
			                                  "cd_matriz,"+
			                                  "cd_formulario,"+
			                                  "dt_avaliacao,"+
			                                  "nm_avaliacao,"+
			                                  "txt_observacao,"+
			                                  "vl_peso,"+
			                                  "id_oferta_avaliacao,"+
			                                  "st_oferta_avaliacao,"+
			                                  "cd_aula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdOferta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOferta());
			if(objeto.getCdTipoAvaliacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAvaliacao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCurso());
			if(objeto.getCdUnidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUnidade());
			if(objeto.getCdDisciplinaAvaliacao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDisciplinaAvaliacao());
			if(objeto.getCdCursoPeriodo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCursoPeriodo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDisciplina());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMatriz());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdFormulario());
			if(objeto.getDtAvaliacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAvaliacao().getTimeInMillis()));
			pstmt.setString(12,objeto.getNmAvaliacao());
			pstmt.setString(13,objeto.getTxtObservacao());
			pstmt.setFloat(14,objeto.getVlPeso());
			pstmt.setString(15,objeto.getIdOfertaAvaliacao());
			pstmt.setInt(16,objeto.getStOfertaAvaliacao());
			if(objeto.getCdAula()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdAula());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OfertaAvaliacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(OfertaAvaliacao objeto, int cdOfertaAvaliacaoOld, int cdOfertaOld) {
		return update(objeto, cdOfertaAvaliacaoOld, cdOfertaOld, null);
	}

	public static int update(OfertaAvaliacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(OfertaAvaliacao objeto, int cdOfertaAvaliacaoOld, int cdOfertaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_oferta_avaliacao SET cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "cd_tipo_avaliacao=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_unidade=?,"+
												      		   "cd_disciplina_avaliacao=?,"+
												      		   "cd_curso_periodo=?,"+
												      		   "cd_disciplina=?,"+
												      		   "cd_matriz=?,"+
												      		   "cd_formulario=?,"+
												      		   "dt_avaliacao=?,"+
												      		   "nm_avaliacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "vl_peso=?,"+
												      		   "id_oferta_avaliacao=?,"+
												      		   "st_oferta_avaliacao=?,"+
												      		   "cd_aula=? WHERE cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1,objeto.getCdOfertaAvaliacao());
			pstmt.setInt(2,objeto.getCdOferta());
			if(objeto.getCdTipoAvaliacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAvaliacao());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCurso());
			if(objeto.getCdUnidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUnidade());
			if(objeto.getCdDisciplinaAvaliacao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDisciplinaAvaliacao());
			if(objeto.getCdCursoPeriodo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCursoPeriodo());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDisciplina());
			if(objeto.getCdMatriz()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMatriz());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdFormulario());
			if(objeto.getDtAvaliacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAvaliacao().getTimeInMillis()));
			pstmt.setString(12,objeto.getNmAvaliacao());
			pstmt.setString(13,objeto.getTxtObservacao());
			pstmt.setFloat(14,objeto.getVlPeso());
			pstmt.setString(15,objeto.getIdOfertaAvaliacao());
			pstmt.setInt(16,objeto.getStOfertaAvaliacao());
			if(objeto.getCdAula()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdAula());
			pstmt.setInt(18, cdOfertaAvaliacaoOld!=0 ? cdOfertaAvaliacaoOld : objeto.getCdOfertaAvaliacao());
			pstmt.setInt(19, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOfertaAvaliacao, int cdOferta) {
		return delete(cdOfertaAvaliacao, cdOferta, null);
	}

	public static int delete(int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_oferta_avaliacao WHERE cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdOfertaAvaliacao);
			pstmt.setInt(2, cdOferta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OfertaAvaliacao get(int cdOfertaAvaliacao, int cdOferta) {
		return get(cdOfertaAvaliacao, cdOferta, null);
	}

	public static OfertaAvaliacao get(int cdOfertaAvaliacao, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao WHERE cd_oferta_avaliacao=? AND cd_oferta=?");
			pstmt.setInt(1, cdOfertaAvaliacao);
			pstmt.setInt(2, cdOferta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OfertaAvaliacao(rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getInt("cd_tipo_avaliacao"),
						rs.getInt("cd_curso"),
						rs.getInt("cd_unidade"),
						rs.getInt("cd_disciplina_avaliacao"),
						rs.getInt("cd_curso_periodo"),
						rs.getInt("cd_disciplina"),
						rs.getInt("cd_matriz"),
						rs.getInt("cd_formulario"),
						(rs.getTimestamp("dt_avaliacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_avaliacao").getTime()),
						rs.getString("nm_avaliacao"),
						rs.getString("txt_observacao"),
						rs.getFloat("vl_peso"),
						rs.getString("id_oferta_avaliacao"),
						rs.getInt("st_oferta_avaliacao"),
						rs.getInt("cd_aula"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OfertaAvaliacao> getList() {
		return getList(null);
	}

	public static ArrayList<OfertaAvaliacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OfertaAvaliacao> list = new ArrayList<OfertaAvaliacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OfertaAvaliacao obj = OfertaAvaliacaoDAO.get(rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
