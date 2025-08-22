package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.ResultSetMapper;

public class FormularioAtributoValorServices {

	public static Result save(FormularioAtributoValor formularioAtributoValor){
		return save(formularioAtributoValor, null);
	}

	public static Result save(FormularioAtributoValor formularioAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formularioAtributoValor==null)
				return new Result(-1, "Erro ao salvar. FormularioAtributoValor é nulo");

			int retorno;
			if(FormularioAtributoValorDAO.get(formularioAtributoValor.getCdFormularioAtributo(), formularioAtributoValor.getCdFormularioAtributoValor(), connect)==null){
				retorno = FormularioAtributoValorDAO.insert(formularioAtributoValor, connect);
				formularioAtributoValor.setCdFormularioAtributo(retorno);
			}
			else {
				retorno = FormularioAtributoValorDAO.update(formularioAtributoValor, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMULARIOATRIBUTOVALOR", formularioAtributoValor);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdFormularioAtributo, int cdFormularioAtributoValor){
		return remove(cdFormularioAtributo, cdFormularioAtributoValor, false, null);
	}
	public static Result remove(int cdFormularioAtributo, int cdFormularioAtributoValor, boolean cascade){
		return remove(cdFormularioAtributo, cdFormularioAtributoValor, cascade, null);
	}
	public static Result remove(int cdFormularioAtributo, int cdFormularioAtributoValor, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = FormularioAtributoValorDAO.delete(cdFormularioAtributo, cdFormularioAtributoValor, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo_valor");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByFormulario(int cdFormulario) {
		return getAllByFormulario(cdFormulario, null);
	}
	
	public static ResultSetMap getAllByFormulario(int cdFormulario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.* " + 
											 "FROM grl_formulario_atributo_valor A, grl_formulario_atributo B " + 
											 "WHERE A.cd_formulario_atributo = B.cd_formulario_atributo AND B.cd_formulario = ?");
			pstmt.setInt(1, cdFormulario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorServices.getAllByFormulario: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioAtributoValorServices.getAllByFormulario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAtributosFormulario(int cdDocumento) {
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.* " +
															 "FROM grl_formulario_atributo_valor A " +
															 "WHERE  A.cd_documento = "+cdDocumento).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
//	public static ResultSetMap getAllByFormulario(int cdFormulario, Connection connect) {
//		boolean isConnectionNull = connect==null;
//		if (isConnectionNull)
//			connect = Conexao.conectar();
//		PreparedStatement pstmt;
//		try {
//			pstmt = connect.prepareStatement("SELECT A.*, B.* "
//					+ " FROM grl_formulario A, grl_formulario_atributo B "
//					+ " LEFT OUTER JOIN grl_formulario_atributo_valor C ON (B.cd_formulario_atributo = C.cd_formulario_atributo)"
//					+ " LEFT OUTER JOIN ptc_documento D ON (C.cd_documento = D.cd_documento AND C.cd_documento = ?) "
//					+ " LEFT OUTER JOIN ptc_tipo_documento E ON (D.cd_tipo_documento = E.cd_tipo_documento AND E.cd_formulario = A.cd_formulario)"
//					+ " WHERE A.cd_formulario = B.cd_formulario AND B.cd_formulario = ?");
//			pstmt.setInt(1, cdFormulario);
//			return new ResultSetMap(pstmt.executeQuery());
//		}
//		catch(SQLException sqlExpt) {
//			sqlExpt.printStackTrace(System.out);
//			System.err.println("Erro! FormularioAtributoValorServices.getAllByFormulario: " + sqlExpt);
//			return null;
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			System.err.println("Erro! FormularioAtributoValorServices.getAllByFormulario: " + e);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}

	public static ResultSetMap getAllByDocumentoFormulario(int cdDocumento, int cdFormulario) {
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, C.* "
					+ " FROM grl_formulario A"
					+ " JOIN grl_formulario_atributo B ON (A.cd_formulario = B.cd_formulario) "
					+ " LEFT OUTER JOIN grl_formulario_atributo_valor C ON (B.cd_formulario_atributo = C.cd_formulario_atributo AND C.cd_documento = "+cdDocumento+") "
					+ " WHERE A.cd_formulario = "+cdFormulario 
					+ " ORDER BY B.nr_ordem").executeQuery());
			
			while(rsm.next()) {
				if(rsm.getInt("TP_DADO")==FormularioAtributoServices.TP_OPCOES){
					rsm.setValueToField("RSM_OPCOES", FormularioAtributoOpcaoServices.getAllByFormularioAtributo(rsm.getInt("CD_FORMULARIO_ATRIBUTO"), connect));
				}
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Object getValorByDocumentoAtributo(int cdDocumento, String nmAtributo) {
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, C.* "
					+ " FROM grl_formulario A"
					+ " JOIN grl_formulario_atributo B ON (A.cd_formulario = B.cd_formulario) "
					+ " JOIN grl_formulario_atributo_valor C ON (B.cd_formulario_atributo = C.cd_formulario_atributo AND C.cd_documento = "+cdDocumento+") "
					+ " WHERE B.nm_atributo = '"+nmAtributo+"'" 
					+ " ORDER BY B.nr_ordem").executeQuery());
			
			if(rsm.next())
				return rsm.getObject("txt_atributo_valor");
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByDocumentoAtributo(int cdDocumento, String nmAtributo) {
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, C.* "
					+ " FROM grl_formulario A"
					+ " JOIN grl_formulario_atributo B ON (A.cd_formulario = B.cd_formulario) "
					+ " JOIN grl_formulario_atributo_valor C ON (B.cd_formulario_atributo = C.cd_formulario_atributo AND C.cd_documento = "+cdDocumento+") "
					+ " WHERE B.nm_atributo = '"+nmAtributo+"'" 
					+ " ORDER BY B.nr_ordem").executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM grl_formulario_atributo_valor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static FormularioAtributoValor getAtributoByDocAtributo (int cdDocumento, int cdAtributo) {
		return getAtributoByDocAtributo(cdDocumento, cdAtributo, null); 
	}
	
	public static FormularioAtributoValor getAtributoByDocAtributo (int cdDocumento, int cdAtributo, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull) 
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO_VALOR WHERE CD_DOCUMENTO = ? AND CD_FORMULARIO_ATRIBUTO = ?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdAtributo);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				ResultSetMapper<FormularioAtributoValor> rsmConv = new ResultSetMapper<FormularioAtributoValor>(rsm, FormularioAtributoValor.class);
				return rsmConv.getFirst();
			}
			
			return null;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static FormularioAtributoValor getAtributoByNmAtributo (int cdDocumento, String nmAtributo, int cdFormulario) {
		return getAtributoByNmAtributo(cdDocumento, nmAtributo, cdFormulario, null); 
	}
	
	public static FormularioAtributoValor getAtributoByNmAtributo (int cdDocumento, String nmAtributo, int cdFormulario, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull) 
			connect = Conexao.conectar();		
		
		try {
			FormularioAtributo _atributo = FormularioAtributoServices.getByNmAtributo(nmAtributo, cdFormulario);
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO_VALOR WHERE CD_DOCUMENTO = ? AND CD_FORMULARIO_ATRIBUTO = ?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, _atributo.getCdFormularioAtributo());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				ResultSetMapper<FormularioAtributoValor> rsmConv = new ResultSetMapper<FormularioAtributoValor>(rsm, FormularioAtributoValor.class);
				return rsmConv.getFirst();
			}
			
			return null;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}