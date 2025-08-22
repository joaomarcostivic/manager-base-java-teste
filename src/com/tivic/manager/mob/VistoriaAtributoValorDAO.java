package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VistoriaAtributoValorDAO{

	public static int insert(VistoriaAtributoValor objeto) {
		return insert(objeto, null);
	}

	public static int insert(VistoriaAtributoValor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria_atributo_valor (cd_vistoria,"+
			                                  "cd_formulario_atributo,"+
			                                  "txt_valor,"+
			                                  "blb_valor,"+
			                                  "cd_opcao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdVistoria()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdVistoria());
			if(objeto.getCdFormularioAtributo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormularioAtributo());
			pstmt.setString(3,objeto.getTxtValor());
			if(objeto.getBlbValor()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbValor());
			if(objeto.getCdOpcao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdOpcao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VistoriaAtributoValor objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(VistoriaAtributoValor objeto, int cdVistoriaOld, int cdFormularioAtributoOld) {
		return update(objeto, cdVistoriaOld, cdFormularioAtributoOld, null);
	}

	public static int update(VistoriaAtributoValor objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(VistoriaAtributoValor objeto, int cdVistoriaOld, int cdFormularioAtributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria_atributo_valor SET cd_vistoria=?,"+
												      		   "cd_formulario_atributo=?,"+
												      		   "txt_valor=?,"+
												      		   "blb_valor=?,"+
												      		   "cd_opcao=? WHERE cd_vistoria=? AND cd_formulario_atributo=?");
			pstmt.setInt(1,objeto.getCdVistoria());
			pstmt.setInt(2,objeto.getCdFormularioAtributo());
			pstmt.setString(3,objeto.getTxtValor());
			if(objeto.getBlbValor()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbValor());
			if(objeto.getCdOpcao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdOpcao());
			pstmt.setInt(6, cdVistoriaOld!=0 ? cdVistoriaOld : objeto.getCdVistoria());
			pstmt.setInt(7, cdFormularioAtributoOld!=0 ? cdFormularioAtributoOld : objeto.getCdFormularioAtributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoria, int cdFormularioAtributo) {
		return delete(cdVistoria, cdFormularioAtributo, null);
	}

	public static int delete(int cdVistoria, int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria_atributo_valor WHERE cd_vistoria=? AND cd_formulario_atributo=?");
			pstmt.setInt(1, cdVistoria);
			pstmt.setInt(2, cdFormularioAtributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VistoriaAtributoValor get(int cdVistoria, int cdFormularioAtributo) {
		return get(cdVistoria, cdFormularioAtributo, null);
	}

	public static VistoriaAtributoValor get(int cdVistoria, int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_atributo_valor WHERE cd_vistoria=? AND cd_formulario_atributo=?");
			pstmt.setInt(1, cdVistoria);
			pstmt.setInt(2, cdFormularioAtributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VistoriaAtributoValor(rs.getInt("cd_vistoria"),
						rs.getInt("cd_formulario_atributo"),
						rs.getString("txt_valor"),
						rs.getBytes("blb_valor")==null?null:rs.getBytes("blb_valor"),
						rs.getInt("cd_opcao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_atributo_valor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VistoriaAtributoValor> getList() {
		return getList(null);
	}

	public static ArrayList<VistoriaAtributoValor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VistoriaAtributoValor> list = new ArrayList<VistoriaAtributoValor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VistoriaAtributoValor obj = VistoriaAtributoValorDAO.get(rsm.getInt("cd_vistoria"), rsm.getInt("cd_formulario_atributo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaAtributoValorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_atributo_valor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
