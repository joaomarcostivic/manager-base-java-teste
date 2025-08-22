package com.tivic.manager.fta;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class TipoVeiculoServices {
		
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			ResultSetMap rsm = getAll(connect);			
			return toStrTipos(rsm);
			
		} catch (Exception e) {
			
			e.printStackTrace(System.out);
			return null;
			
		} finally {
			
			if(isConnectionNull)
				Conexao.desconectar(connect);
			
		}
		
	}

	public static Result save(TipoVeiculo tipoVeiculo){
		return save(tipoVeiculo, null);
	}

	public static Result save(TipoVeiculo tipoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoVeiculo==null)
				return new Result(-1, "Erro ao salvar. TipoVeiculo é nulo");

			int retorno;
			if(tipoVeiculo.getCdTipoVeiculo()==0){
				retorno = TipoVeiculoDAO.insert(tipoVeiculo, connect);
				tipoVeiculo.setCdTipoVeiculo(retorno);
			}
			else {
				retorno = TipoVeiculoDAO.update(tipoVeiculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOVEICULO", tipoVeiculo);
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
	public static Result remove(int cdTipoVeiculo){
		return remove(cdTipoVeiculo, false, null);
	}
	public static Result remove(int cdTipoVeiculo, boolean cascade){
		return remove(cdTipoVeiculo, cascade, null);
	}
	public static Result remove(int cdTipoVeiculo, boolean cascade, Connection connect){
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
			retorno = TipoVeiculoDAO.delete(cdTipoVeiculo, connect);
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
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT *, cd_tipo_veiculo as cd_tipo, nm_tipo_veiculo as nm_tipo FROM fta_tipo_veiculo ORDER BY nm_tipo_veiculo");
				return new ResultSetMap(pstmt.executeQuery());
			}
			
			pstmt = connect.prepareStatement("SELECT *, nm_tipo as nm_tipo_veiculo, cod_tipo as cd_tipo_veiculo FROM tipo_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static TipoVeiculo get(int cdTipoVeiculo) {
		return get(cdTipoVeiculo, null);
	}

	public static TipoVeiculo get(int cdTipoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_veiculo WHERE cd_tipo_veiculo=?");
				pstmt.setInt(1, cdTipoVeiculo);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new TipoVeiculo(rs.getInt("cd_tipo_veiculo"),
							rs.getString("nm_tipo_veiculo"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM tipo_veiculo WHERE cod_tipo=?");
				pstmt.setInt(1, cdTipoVeiculo);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new TipoVeiculo(rs.getInt("cod_tipo"),
							rs.getString("nm_tipo"));
				}
			}
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static TipoVeiculo getByNome(String nmTipoVeiculo) {
		return getByNome(nmTipoVeiculo, null);
	}

	public static TipoVeiculo getByNome(String nmTipoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_veiculo WHERE nm_tipo_veiculo LIKE ?");
				pstmt.setString(1, nmTipoVeiculo);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new TipoVeiculo(rs.getInt("cd_tipo_veiculo"),
							rs.getString("nm_tipo_veiculo"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM tipo_veiculo WHERE nm_tipo LIKE ?");
				pstmt.setString(1, nmTipoVeiculo);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new TipoVeiculo(rs.getInt("cod_tipo"),
							rs.getString("nm_tipo"));
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.getByNome: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoServices.getByNome: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	private static ResultSetMap toStrTipos(ResultSetMap rsm) {
		
		if(Util.isStrBaseAntiga()) {
			
			ResultSetMap rsmOut = new ResultSetMap();
			
			while(rsm.next()) {
				
				TipoVeiculo tipoFta = new TipoVeiculo(
						rsm.getInt("cd_tipo_veiculo"),
						rsm.getString("nm_tipo_veiculo")
						);

				com.tivic.manager.str.TipoVeiculo conv = toStrTipoVeiculo(tipoFta);
				
				HashMap<String, Object> hash = new HashMap<String, Object>();
				
				hash.put("cd_tipo_veiculo", conv.getCdTipo());
				hash.put("nm_tipo_veiculo", conv.getNmTipo());
				
				rsmOut.addRegister(hash);
				
			}
			
			return rsmOut;
			
		} else {
			
			return rsm;
			
		}
		
	}
	
	private static com.tivic.manager.str.TipoVeiculo toStrTipoVeiculo (TipoVeiculo tipoVeiculo) {
		return new com.tivic.manager.str.TipoVeiculo (
				tipoVeiculo.getCdTipoVeiculo(),
				tipoVeiculo.getNmTipoVeiculo()
				);
	}

}
