package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class OrgaoDAO{

	public static int insert(Orgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Orgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_orgao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdOrgao()<=0)
				objeto.setCdOrgao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_ORGAO (CD_ORGAO,"+
                    "CD_TIPO_ORGAO,"+
                    "NM_ORGAO,"+
                    "ID_ORGAO,"+
                    "CD_RESPONSAVEL,"+
                    "CD_PESSOA,"+
                    "TP_CONTRATACAO) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdOrgao());
			if(objeto.getCdTipoOrgao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoOrgao());
			pstmt.setString(3,objeto.getNmOrgao());
			pstmt.setString(4,objeto.getIdOrgao());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoa());
			pstmt.setInt(7, objeto.getTpContratacao());
			pstmt.executeUpdate();
			return objeto.getCdOrgao();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Orgao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Orgao objeto, int cdOrgaoOld) {
		return update(objeto, cdOrgaoOld, null);
	}

	public static int update(Orgao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Orgao objeto, int cdOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_ORGAO SET CD_ORGAO=?,"+
		      		   "CD_TIPO_ORGAO=?,"+
		      		   "NM_ORGAO=?,"+
		      		   "ID_ORGAO=?,"+
		      		   "CD_RESPONSAVEL=?,"+
		      		   "CD_PESSOA=?,"+
		      		   "TP_CONTRATACAO=? WHERE CD_ORGAO=?");
			pstmt.setInt(1,objeto.getCdOrgao());
			if(objeto.getCdTipoOrgao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoOrgao());
			pstmt.setString(3,objeto.getNmOrgao());
			pstmt.setString(4,objeto.getIdOrgao());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoa());
			pstmt.setInt(7,objeto.getTpContratacao());
			pstmt.setInt(8, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgao) {
		return delete(cdOrgao, null);
	}

	public static int delete(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_orgao WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Orgao get(int cdOrgao) {
		return get(cdOrgao, null);
	}

	public static Orgao get(int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_orgao WHERE cd_orgao=?");
			pstmt.setInt(1, cdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Orgao(rs.getInt("CD_ORGAO"),
						rs.getInt("CD_TIPO_ORGAO"),
						rs.getString("NM_ORGAO"),
						rs.getString("ID_ORGAO"),
						rs.getInt("CD_RESPONSAVEL"),
						rs.getInt("CD_PESSOA"),
						rs.getInt("TP_CONTRATACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_orgao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_orgao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
