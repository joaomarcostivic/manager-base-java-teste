package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;


public class TipoArquivoServices {
	public static Result save(TipoArquivo tipoArquivo){
		return save(tipoArquivo, null);
	}

	public static Result save(TipoArquivo tipoArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoArquivo==null)
				return new Result(-1, "Erro ao salvar. TipoArquivo é nulo");

			int retorno;
			if(tipoArquivo.getCdTipoArquivo()==0){
				retorno = TipoArquivoDAO.insert(tipoArquivo, connect);
				tipoArquivo.setCdTipoArquivo(retorno);
			}
			else {
				retorno = TipoArquivoDAO.update(tipoArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOARQUIVO", tipoArquivo);
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
	public static Result remove(int cdTipoArquivo){
		return remove(cdTipoArquivo, false, null);
	}
	public static Result remove(int cdTipoArquivo, boolean cascade){
		return remove(cdTipoArquivo, cascade, null);
	}
	public static Result remove(int cdTipoArquivo, boolean cascade, Connection connect){
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
			retorno = TipoArquivoDAO.delete(cdTipoArquivo, connect);
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
	
	public static TipoArquivo getTipoArquivoById(String idTipoArquivo, String nmTipoArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM grl_tipo_arquivo A " +
					"WHERE id_tipo_arquivo = \'"+idTipoArquivo+"\'").executeQuery();
			if(rs.next()) 
				return new TipoArquivo(rs.getInt("cd_tipo_arquivo"),
									   rs.getString("nm_tipo_arquivo"),
									   rs.getString("id_tipo_arquivo"));
			else	{
				TipoArquivo tipo = new TipoArquivo(0, nmTipoArquivo, idTipoArquivo);
				tipo.setCdTipoArquivo(TipoArquivoDAO.insert(tipo, connect));
				return tipo;
			}
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoArquivoServices.getTipoArquivoById: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static TipoArquivo get(int cdTipoArquivo) {
		return get(cdTipoArquivo, null);
	}
	
	public static TipoArquivo get(int cdTipoArquivo, Connection connect) {
		boolean isConnectionNull =  connect==null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_arquivo WHERE cd_tipo_arquivo=?");
				pstmt.setInt(1, cdTipoArquivo);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return new TipoArquivo(rs.getInt("cd_tipo_arquivo"),
							rs.getString("nm_tipo_arquivo"), rs.getString("id_tipo_arquivo"));
				} 
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM tipo_arquivo WHERE cd_tipo_arquivo=?");
				pstmt.setInt(1, cdTipoArquivo);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return new TipoArquivo(rs.getInt("cd_tipo_arquivo"),
							rs.getString("nm_tipo_arquivo"), rs.getString("id_tipo_arquivo"));
			}
		}
			return null;
	} catch(SQLException sqlExpt) {
		sqlExpt.printStackTrace(System.out);
		System.err.println("Erro! TipoArquivoDAO.get: " + sqlExpt);
		return null;
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
		System.err.println("Erro! TipoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_arquivo ORDER BY nm_tipo_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
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
	
	/**
	 * Metodo para registrar o tipo de arquivo parametrizado para emails
	 * @since 14/08/2014
	 * @author Gabriel
	 * 
	 */
	public static void registrarTipoArquivoEmail() {
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(false);
			
			int code = TipoArquivoDAO.insert(new TipoArquivo(0, "EMAIL", "0100"), connect);
			if(code <= 0){
				System.out.println("Erro ao inserir tipo arquivo");
				if(isConnectionNull)
					Conexao.rollback(connect);
				return;
			}
			
			Parametro parametroTipoArquivoEmail = ParametroServices.getByName("CD_TIPO_ARQUIVO_EMAIL", connect);
			int cdParametroTipoArquivoEmail = parametroTipoArquivoEmail.getCdParametro();
			if(cdParametroTipoArquivoEmail <= 0){
				System.out.println("Erro ao ao buscar parametro tipo arquivo email");
				if(isConnectionNull)
					Conexao.rollback(connect);
				return;
			}
			ArrayList<ParametroValor> values = new ArrayList<ParametroValor>(); 
			values.add(new ParametroValor(cdParametroTipoArquivoEmail, 0, 0, 0, 0, null, "" + code, null));
			int ret = ParametroServices.setValoresOfParametro(cdParametroTipoArquivoEmail, 0, 0, values, connect);
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				System.out.println("Erro ao adicionar tipo de arquivo email");
				return;
			}
			
			if(isConnectionNull)
				connect.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int qtRegistros = 0;
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtRegistros = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}
		
		ResultSetMap rsm = Search.find("SELECT * FROM grl_tipo_arquivo", (qtRegistros > 0? " LIMIT " + qtRegistros : ""),
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		return rsm;
	}

}
