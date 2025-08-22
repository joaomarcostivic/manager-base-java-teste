package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CidadeDAO{

	public static int insert(Cidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("GRL_CIDADE", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdCidade()<=0)	{
				objeto.setCdCidade(code);
				//
				ResultSet rs = connect.prepareStatement("SELECT * FROM grl_cidade " +
						                                "WHERE nm_cidade = \'"+objeto.getNmCidade()+"\' " +
						                                (objeto.getCdEstado()>0 ? " AND cd_estado = "+objeto.getCdEstado():"")).executeQuery();
				if(rs.next())
					return rs.getInt("CD_CIDADE");
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO GRL_CIDADE (CD_CIDADE,"+
			                                  "NM_CIDADE,"+
			                                  "NR_CEP,"+
			                                  "VL_ALTITUDE,"+
			                                  "VL_LATITUDE,"+
			                                  "VL_LONGITUDE,"+
			                                  "CD_ESTADO,"+
			                                  "ID_CIDADE,"+
			                                  "CD_REGIAO,"+
			                                  "ID_IBGE,"+
			                                  "SG_CIDADE,"+
			                                  "QT_DISTANCIA_CAPITAL,"+
			                                  "QT_DISTANCIA_BASE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdCidade());
			pstmt.setString(2,objeto.getNmCidade());
			pstmt.setString(3,objeto.getNrCep());
			pstmt.setFloat(4,objeto.getVlAltitude());
			pstmt.setFloat(5,objeto.getVlLatitude());
			pstmt.setFloat(6,objeto.getVlLongitude());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEstado());
			pstmt.setString(8,objeto.getIdCidade());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRegiao());
			pstmt.setString(10,objeto.getIdIbge());
			pstmt.setString(11,objeto.getSgCidade());
			pstmt.setInt(12,objeto.getQtDistanciaCapital());
			pstmt.setInt(13,objeto.getQtDistanciaBase());
			pstmt.executeUpdate();
			return objeto.getCdCidade();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cidade objeto, int cdCidadeOld) {
		return update(objeto, cdCidadeOld, null);
	}

	public static int update(Cidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cidade objeto, int cdCidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE GRL_CIDADE SET CD_CIDADE=?,"+
												      		   "NM_CIDADE=?,"+
												      		   "NR_CEP=?,"+
												      		   "VL_ALTITUDE=?,"+
												      		   "VL_LATITUDE=?,"+
												      		   "VL_LONGITUDE=?,"+
												      		   "CD_ESTADO=?,"+
												      		   "ID_CIDADE=?,"+
												      		   "CD_REGIAO=?,"+
												      		   "ID_IBGE=?,"+
												      		   "SG_CIDADE=?,"+
												      		   "QT_DISTANCIA_CAPITAL=?,"+
												      		   "QT_DISTANCIA_BASE=? WHERE CD_CIDADE=?");
			pstmt.setInt(1,objeto.getCdCidade());
			pstmt.setString(2,objeto.getNmCidade());
			pstmt.setString(3,objeto.getNrCep());
			pstmt.setFloat(4,objeto.getVlAltitude());
			pstmt.setFloat(5,objeto.getVlLatitude());
			pstmt.setFloat(6,objeto.getVlLongitude());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEstado());
			pstmt.setString(8,objeto.getIdCidade());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRegiao());
			pstmt.setString(10,objeto.getIdIbge());
			pstmt.setString(11,objeto.getSgCidade());
			pstmt.setInt(12,objeto.getQtDistanciaCapital());
			pstmt.setInt(13,objeto.getQtDistanciaBase());
			pstmt.setInt(14, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCidade) {
		return delete(cdCidade, null);
	}

	public static int delete(int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM GRL_CIDADE WHERE CD_CIDADE=?");
			pstmt.setInt(1, cdCidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cidade get(int cdCidade) {
		return get(cdCidade, null);
	}

	public static Cidade get(int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_CIDADE WHERE CD_CIDADE=?");
			pstmt.setInt(1, cdCidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cidade(rs.getInt("CD_CIDADE"),
						rs.getString("NM_CIDADE"),
						rs.getString("NR_CEP"),
						rs.getFloat("VL_ALTITUDE"),
						rs.getFloat("VL_LATITUDE"),
						rs.getFloat("VL_LONGITUDE"),
						rs.getInt("CD_ESTADO"),
						rs.getString("ID_CIDADE"),
						rs.getInt("CD_REGIAO"),
						rs.getString("ID_IBGE"),
						rs.getString("SG_CIDADE"),
						rs.getInt("QT_DISTANCIA_CAPITAL"),
						rs.getInt("QT_DISTANCIA_BASE"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM GRL_CIDADE");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Cidade> getList() {
		return getList(null);
	}

	public static ArrayList<Cidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Cidade> list = new ArrayList<Cidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Cidade obj = CidadeDAO.get(rsm.getInt("CD_CIDADE"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM GRL_CIDADE", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
