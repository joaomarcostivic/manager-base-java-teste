package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class SocioDAO{

	public static int insert(Socio objeto) {
		return insert(objeto, null);
	}

	public static int insert(Socio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_socio (cd_socio,"+
			                                  "cd_pessoa,"+
			                                  "cd_responsavel,"+
			                                  "cd_pais_dipj,"+
			                                  "nm_email_relatorio,"+
			                                  "tp_representante_legal,"+
			                                  "pr_capital,"+
			                                  "txt_observacao,"+
			                                  "tp_regime_matrimonial) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdSocio()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdSocio());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdResponsavel());
			if(objeto.getCdPaisDipj()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPaisDipj());
			pstmt.setString(5,objeto.getNmEmailRelatorio());
			pstmt.setInt(6,objeto.getTpRepresentanteLegal());
			pstmt.setFloat(7,objeto.getPrCapital());
			pstmt.setString(8,objeto.getTxtObservacao());
			pstmt.setInt(9,objeto.getTpRegimeMatrimonial());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.printInFile("C:/log.log", e.toString());
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Socio objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Socio objeto, int cdSocioOld, int cdPessoaOld) {
		return update(objeto, cdSocioOld, cdPessoaOld, null);
	}

	public static int update(Socio objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Socio objeto, int cdSocioOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_socio SET cd_socio=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_pais_dipj=?,"+
												      		   "nm_email_relatorio=?,"+
												      		   "tp_representante_legal=?,"+
												      		   "pr_capital=?,"+
												      		   "txt_observacao=?,"+
												      		   "tp_regime_matrimonial=? WHERE cd_socio=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdSocio());
			pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdResponsavel());
			if(objeto.getCdPaisDipj()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPaisDipj());
			pstmt.setString(5,objeto.getNmEmailRelatorio());
			pstmt.setInt(6,objeto.getTpRepresentanteLegal());
			pstmt.setFloat(7,objeto.getPrCapital());
			pstmt.setString(8,objeto.getTxtObservacao());
			pstmt.setInt(9,objeto.getTpRegimeMatrimonial());
			pstmt.setInt(10, cdSocioOld!=0 ? cdSocioOld : objeto.getCdSocio());
			pstmt.setInt(11, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());		
			return pstmt.executeUpdate();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			Util.printInFile("C:/log.log", sqlExpt.toString());
			System.err.println("Erro! SocioDAO.update: " + sqlExpt);			
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.printInFile("C:/log.log", e.toString());
			System.err.println("Erro! SocioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSocio, int cdPessoa) {
		return delete(cdSocio, cdPessoa, null);
	}

	public static int delete(int cdSocio, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_socio WHERE cd_socio=? AND cd_pessoa=?");
			pstmt.setInt(1, cdSocio);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SocioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SocioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Socio get(int cdSocio, int cdPessoa) {
		return get(cdSocio, cdPessoa, null);
	}

	public static Socio get(int cdSocio, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_socio WHERE cd_socio=? AND cd_pessoa=?");
			pstmt.setInt(1, cdSocio);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Socio(rs.getInt("cd_socio"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_pais_dipj"),
						rs.getString("nm_email_relatorio"),
						rs.getInt("tp_representante_legal"),
						rs.getFloat("pr_capital"),
						rs.getString("txt_observacao"),
						rs.getInt("tp_regime_matrimonial"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SocioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SocioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_socio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SocioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SocioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_socio", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
