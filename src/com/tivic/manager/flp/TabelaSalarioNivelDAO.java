package com.tivic.manager.flp;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class TabelaSalarioNivelDAO{

	public static int insert(TabelaSalarioNivel objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(TabelaSalarioNivel objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_salario");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTabelaSalario()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_nivel_salario");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("flp_tabela_salario_nivel", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNivelSalario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_tabela_salario_nivel (cd_tabela_salario,"+
			                                  "cd_nivel_salario,"+
			                                  "nm_nivel_salario,"+
			                                  "id_nivel_salario,"+
			                                  "tp_calculo,"+
			                                  "vl_aplicacao) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaSalario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaSalario());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNmNivelSalario());
			pstmt.setString(4,objeto.getIdNivelSalario());
			pstmt.setInt(5,objeto.getTpCalculo());
			pstmt.setFloat(6,objeto.getVlAplicacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaSalarioNivel objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaSalarioNivel objeto, int cdTabelaSalarioOld, int cdNivelSalarioOld) {
		return update(objeto, cdTabelaSalarioOld, cdNivelSalarioOld, null);
	}

	public static int update(TabelaSalarioNivel objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaSalarioNivel objeto, int cdTabelaSalarioOld, int cdNivelSalarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_tabela_salario_nivel SET cd_tabela_salario=?,"+
												      		   "cd_nivel_salario=?,"+
												      		   "nm_nivel_salario=?,"+
												      		   "id_nivel_salario=?,"+
												      		   "tp_calculo=?,"+
												      		   "vl_aplicacao=? WHERE cd_tabela_salario=? AND cd_nivel_salario=?");
			pstmt.setInt(1,objeto.getCdTabelaSalario());
			pstmt.setInt(2,objeto.getCdNivelSalario());
			pstmt.setString(3,objeto.getNmNivelSalario());
			pstmt.setString(4,objeto.getIdNivelSalario());
			pstmt.setInt(5,objeto.getTpCalculo());
			pstmt.setFloat(6,objeto.getVlAplicacao());
			pstmt.setInt(7, cdTabelaSalarioOld!=0 ? cdTabelaSalarioOld : objeto.getCdTabelaSalario());
			pstmt.setInt(8, cdNivelSalarioOld!=0 ? cdNivelSalarioOld : objeto.getCdNivelSalario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaSalario, int cdNivelSalario) {
		return delete(cdTabelaSalario, cdNivelSalario, null);
	}

	public static int delete(int cdTabelaSalario, int cdNivelSalario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_tabela_salario_nivel WHERE cd_tabela_salario=? AND cd_nivel_salario=?");
			pstmt.setInt(1, cdTabelaSalario);
			pstmt.setInt(2, cdNivelSalario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaSalarioNivel get(int cdTabelaSalario, int cdNivelSalario) {
		return get(cdTabelaSalario, cdNivelSalario, null);
	}

	public static TabelaSalarioNivel get(int cdTabelaSalario, int cdNivelSalario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_salario_nivel WHERE cd_tabela_salario=? AND cd_nivel_salario=?");
			pstmt.setInt(1, cdTabelaSalario);
			pstmt.setInt(2, cdNivelSalario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaSalarioNivel(rs.getInt("cd_tabela_salario"),
						rs.getInt("cd_nivel_salario"),
						rs.getString("nm_nivel_salario"),
						rs.getString("id_nivel_salario"),
						rs.getInt("tp_calculo"),
						rs.getFloat("vl_aplicacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_salario_nivel");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioNivelDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_tabela_salario_nivel", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
