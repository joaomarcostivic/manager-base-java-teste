package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class UsuarioPenalidadeDAO{

	public static int insert(UsuarioPenalidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioPenalidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_usuario_penalidade", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setNrPenalidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_usuario_penalidade (nr_penalidade,"+
			                                  "cd_penalidade,"+
			                                  "cd_publicacao,"+
			                                  "cd_exemplar,"+
			                                  "nr_ocorrencia,"+
			                                  "cd_pessoa,"+
			                                  "dt_penalidade,"+
			                                  "tp_penalidade,"+
			                                  "nr_dias,"+
			                                  "vl_penalidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPenalidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPenalidade());
			if(objeto.getCdPublicacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPublicacao());
			if(objeto.getCdExemplar()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdExemplar());
			if(objeto.getNrOcorrencia()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getNrOcorrencia());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoa());
			if(objeto.getDtPenalidade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtPenalidade().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpPenalidade());
			pstmt.setInt(9,objeto.getNrDias());
			pstmt.setFloat(10,objeto.getVlPenalidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioPenalidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(UsuarioPenalidade objeto, int nrPenalidadeOld) {
		return update(objeto, nrPenalidadeOld, null);
	}

	public static int update(UsuarioPenalidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(UsuarioPenalidade objeto, int nrPenalidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_usuario_penalidade SET nr_penalidade=?,"+
												      		   "cd_penalidade=?,"+
												      		   "cd_publicacao=?,"+
												      		   "cd_exemplar=?,"+
												      		   "nr_ocorrencia=?,"+
												      		   "cd_pessoa=?,"+
												      		   "dt_penalidade=?,"+
												      		   "tp_penalidade=?,"+
												      		   "nr_dias=?,"+
												      		   "vl_penalidade=? WHERE nr_penalidade=?");
			pstmt.setInt(1,objeto.getNrPenalidade());
			if(objeto.getCdPenalidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPenalidade());
			if(objeto.getCdPublicacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPublicacao());
			if(objeto.getCdExemplar()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdExemplar());
			if(objeto.getNrOcorrencia()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getNrOcorrencia());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoa());
			if(objeto.getDtPenalidade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtPenalidade().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpPenalidade());
			pstmt.setInt(9,objeto.getNrDias());
			pstmt.setFloat(10,objeto.getVlPenalidade());
			pstmt.setInt(11, nrPenalidadeOld!=0 ? nrPenalidadeOld : objeto.getNrPenalidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int nrPenalidade) {
		return delete(nrPenalidade, null);
	}

	public static int delete(int nrPenalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_usuario_penalidade WHERE nr_penalidade=?");
			pstmt.setInt(1, nrPenalidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioPenalidade get(int nrPenalidade) {
		return get(nrPenalidade, null);
	}

	public static UsuarioPenalidade get(int nrPenalidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_usuario_penalidade WHERE nr_penalidade=?");
			pstmt.setInt(1, nrPenalidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioPenalidade(rs.getInt("nr_penalidade"),
						rs.getInt("cd_penalidade"),
						rs.getInt("cd_publicacao"),
						rs.getInt("cd_exemplar"),
						rs.getInt("nr_ocorrencia"),
						rs.getInt("cd_pessoa"),
						(rs.getTimestamp("dt_penalidade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_penalidade").getTime()),
						rs.getInt("tp_penalidade"),
						rs.getInt("nr_dias"),
						rs.getFloat("vl_penalidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_usuario_penalidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioPenalidade> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioPenalidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioPenalidade> list = new ArrayList<UsuarioPenalidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioPenalidade obj = UsuarioPenalidadeDAO.get(rsm.getInt("nr_penalidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPenalidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_usuario_penalidade", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
