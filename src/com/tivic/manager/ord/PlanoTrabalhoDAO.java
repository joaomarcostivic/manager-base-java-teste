package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PlanoTrabalhoDAO{

	public static int insert(PlanoTrabalho objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoTrabalho objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_plano_trabalho", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoTrabalho(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_plano_trabalho (cd_plano_trabalho,"+
			                                  "dt_cadastro,"+
			                                  "id_plano,"+
			                                  "dt_trabalho,"+
			                                  "st_plano,"+
			                                  "txt_observacao,"+
			                                  "cd_usuario,"+
			                                  "cd_supervisor,"+
			                                  "cd_motorista,"+
			                                  "cd_veiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(3,objeto.getIdPlano());
			if(objeto.getDtTrabalho()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtTrabalho().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPlano());
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuario());
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSupervisor());
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMotorista());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoTrabalho objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoTrabalho objeto, int cdPlanoTrabalhoOld) {
		return update(objeto, cdPlanoTrabalhoOld, null);
	}

	public static int update(PlanoTrabalho objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoTrabalho objeto, int cdPlanoTrabalhoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_plano_trabalho SET cd_plano_trabalho=?,"+
												      		   "dt_cadastro=?,"+
												      		   "id_plano=?,"+
												      		   "dt_trabalho=?,"+
												      		   "st_plano=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_supervisor=?,"+
												      		   "cd_motorista=?,"+
												      		   "cd_veiculo=? WHERE cd_plano_trabalho=?");
			pstmt.setInt(1,objeto.getCdPlanoTrabalho());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(3,objeto.getIdPlano());
			if(objeto.getDtTrabalho()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtTrabalho().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPlano());
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuario());
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSupervisor());
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMotorista());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdVeiculo());
			pstmt.setInt(11, cdPlanoTrabalhoOld!=0 ? cdPlanoTrabalhoOld : objeto.getCdPlanoTrabalho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoTrabalho) {
		return delete(cdPlanoTrabalho, null);
	}

	public static int delete(int cdPlanoTrabalho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_plano_trabalho WHERE cd_plano_trabalho=?");
			pstmt.setInt(1, cdPlanoTrabalho);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoTrabalho get(int cdPlanoTrabalho) {
		return get(cdPlanoTrabalho, null);
	}

	public static PlanoTrabalho get(int cdPlanoTrabalho, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_plano_trabalho WHERE cd_plano_trabalho=?");
			pstmt.setInt(1, cdPlanoTrabalho);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoTrabalho(rs.getInt("cd_plano_trabalho"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getString("id_plano"),
						(rs.getTimestamp("dt_trabalho")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_trabalho").getTime()),
						rs.getInt("st_plano"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_supervisor"),
						rs.getInt("cd_motorista"),
						rs.getInt("cd_veiculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_plano_trabalho");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoTrabalho> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoTrabalho> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoTrabalho> list = new ArrayList<PlanoTrabalho>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoTrabalho obj = PlanoTrabalhoDAO.get(rsm.getInt("cd_plano_trabalho"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_plano_trabalho", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
