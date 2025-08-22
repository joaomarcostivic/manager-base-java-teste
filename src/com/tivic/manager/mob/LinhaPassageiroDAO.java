package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LinhaPassageiroDAO{

	public static int insert(LinhaPassageiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(LinhaPassageiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_linha_passageiro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLinhaPassageiro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_linha_passageiro (cd_linha_passageiro,"+
			                                  "cd_linha,"+
			                                  "cd_pessoa,"+
			                                  "cd_matricula,"+
			                                  "dt_inicio,"+
			                                  "dt_fim) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLinha()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLinha());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMatricula());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtFim()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFim().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LinhaPassageiro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LinhaPassageiro objeto, int cdLinhaPassageiroOld) {
		return update(objeto, cdLinhaPassageiroOld, null);
	}

	public static int update(LinhaPassageiro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LinhaPassageiro objeto, int cdLinhaPassageiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_linha_passageiro SET cd_linha_passageiro=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_matricula=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_fim=? WHERE cd_linha_passageiro=?");
			pstmt.setInt(1,objeto.getCdLinhaPassageiro());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLinha());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMatricula());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtFim()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFim().getTimeInMillis()));
			pstmt.setInt(7, cdLinhaPassageiroOld!=0 ? cdLinhaPassageiroOld : objeto.getCdLinhaPassageiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLinhaPassageiro) {
		return delete(cdLinhaPassageiro, null);
	}

	public static int delete(int cdLinhaPassageiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_linha_passageiro WHERE cd_linha_passageiro=?");
			pstmt.setInt(1, cdLinhaPassageiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LinhaPassageiro get(int cdLinhaPassageiro) {
		return get(cdLinhaPassageiro, null);
	}

	public static LinhaPassageiro get(int cdLinhaPassageiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_passageiro WHERE cd_linha_passageiro=?");
			pstmt.setInt(1, cdLinhaPassageiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LinhaPassageiro(rs.getInt("cd_linha_passageiro"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_matricula"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_fim")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fim").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_passageiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LinhaPassageiro> getList() {
		return getList(null);
	}

	public static ArrayList<LinhaPassageiro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LinhaPassageiro> list = new ArrayList<LinhaPassageiro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LinhaPassageiro obj = LinhaPassageiroDAO.get(rsm.getInt("cd_linha_passageiro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_linha_passageiro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}