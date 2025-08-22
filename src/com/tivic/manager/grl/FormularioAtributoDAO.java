package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FormularioAtributoDAO{

	public static int insert(FormularioAtributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(FormularioAtributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_formulario_atributo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormularioAtributo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_formulario_atributo (cd_formulario_atributo,"+
			                                  "cd_formulario,"+
			                                  "nm_atributo,"+
			                                  "sg_atributo,"+
			                                  "lg_obrigatorio,"+
			                                  "tp_dado,"+
			                                  "nr_casas_decimais,"+
			                                  "nr_ordem,"+
			                                  "vl_maximo,"+
			                                  "vl_minimo,"+
			                                  "txt_formula,"+
			                                  "id_atributo,"+
			                                  "tp_restricao_pessoa,"+
			                                  "cd_vinculo,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			pstmt.setString(3,objeto.getNmAtributo());
			pstmt.setString(4,objeto.getSgAtributo());
			pstmt.setInt(5,objeto.getLgObrigatorio());
			pstmt.setInt(6,objeto.getTpDado());
			pstmt.setInt(7,objeto.getNrCasasDecimais());
			pstmt.setInt(8,objeto.getNrOrdem());
			pstmt.setFloat(9,objeto.getVlMaximo());
			pstmt.setFloat(10,objeto.getVlMinimo());
			pstmt.setString(11,objeto.getTxtFormula());
			pstmt.setString(12,objeto.getIdAtributo());
			pstmt.setInt(13,objeto.getTpRestricaoPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVinculo());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUnidadeMedida());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FormularioAtributo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FormularioAtributo objeto, int cdFormularioAtributoOld) {
		return update(objeto, cdFormularioAtributoOld, null);
	}

	public static int update(FormularioAtributo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FormularioAtributo objeto, int cdFormularioAtributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_formulario_atributo SET cd_formulario_atributo=?,"+
												      		   "cd_formulario=?,"+
												      		   "nm_atributo=?,"+
												      		   "sg_atributo=?,"+
												      		   "lg_obrigatorio=?,"+
												      		   "tp_dado=?,"+
												      		   "nr_casas_decimais=?,"+
												      		   "nr_ordem=?,"+
												      		   "vl_maximo=?,"+
												      		   "vl_minimo=?,"+
												      		   "txt_formula=?,"+
												      		   "id_atributo=?,"+
												      		   "tp_restricao_pessoa=?,"+
												      		   "cd_vinculo=?,"+
												      		   "cd_unidade_medida=? WHERE cd_formulario_atributo=?");
			pstmt.setInt(1,objeto.getCdFormularioAtributo());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			pstmt.setString(3,objeto.getNmAtributo());
			pstmt.setString(4,objeto.getSgAtributo());
			pstmt.setInt(5,objeto.getLgObrigatorio());
			pstmt.setInt(6,objeto.getTpDado());
			pstmt.setInt(7,objeto.getNrCasasDecimais());
			pstmt.setInt(8,objeto.getNrOrdem());
			pstmt.setFloat(9,objeto.getVlMaximo());
			pstmt.setFloat(10,objeto.getVlMinimo());
			pstmt.setString(11,objeto.getTxtFormula());
			pstmt.setString(12,objeto.getIdAtributo());
			pstmt.setInt(13,objeto.getTpRestricaoPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVinculo());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUnidadeMedida());
			pstmt.setInt(16, cdFormularioAtributoOld!=0 ? cdFormularioAtributoOld : objeto.getCdFormularioAtributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormularioAtributo) {
		return delete(cdFormularioAtributo, null);
	}

	public static int delete(int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_formulario_atributo WHERE cd_formulario_atributo=?");
			pstmt.setInt(1, cdFormularioAtributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FormularioAtributo get(int cdFormularioAtributo) {
		return get(cdFormularioAtributo, null);
	}

	public static FormularioAtributo get(int cdFormularioAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo WHERE cd_formulario_atributo=?");
			pstmt.setInt(1, cdFormularioAtributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FormularioAtributo(rs.getInt("cd_formulario_atributo"),
						rs.getInt("cd_formulario"),
						rs.getString("nm_atributo"),
						rs.getString("sg_atributo"),
						rs.getInt("lg_obrigatorio"),
						rs.getInt("tp_dado"),
						rs.getInt("nr_casas_decimais"),
						rs.getInt("nr_ordem"),
						rs.getFloat("vl_maximo"),
						rs.getFloat("vl_minimo"),
						rs.getString("txt_formula"),
						rs.getString("id_atributo"),
						rs.getInt("tp_restricao_pessoa"),
						rs.getInt("cd_vinculo"),
						rs.getInt("cd_unidade_medida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_formulario_atributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
