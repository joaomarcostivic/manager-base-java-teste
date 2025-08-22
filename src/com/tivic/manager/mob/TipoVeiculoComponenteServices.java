package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoVeiculoComponenteServices {

	public static Result save(TipoVeiculoComponente tipoVeiculoComponente){
		return save(tipoVeiculoComponente, null);
	}

	public static Result save(TipoVeiculoComponente tipoVeiculoComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoVeiculoComponente==null)
				return new Result(-1, "Erro ao salvar. TipoVeiculoComponente é nulo");
			
			if( new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_componente "+
					" WHERE  cd_tipo_veiculo = "+tipoVeiculoComponente.getCdTipoVeiculo()+
					" AND nm_componente = '"+tipoVeiculoComponente.getNmComponente()+"' "+
					(( tipoVeiculoComponente.getCdTipoVeiculoComponente() > 0 )
							?" AND cd_tipo_veiculo_componente <>  "+tipoVeiculoComponente.getCdTipoVeiculoComponente():"")
					).executeQuery()).size() > 0 ){
				return new Result(-1, "Este componente já existe!!!");
			}
			int retorno;
			if(tipoVeiculoComponente.getCdTipoVeiculoComponente()==0){
				retorno = TipoVeiculoComponenteDAO.insert(tipoVeiculoComponente, connect);
				tipoVeiculoComponente.setCdTipoVeiculoComponente(retorno);
			}
			else {
				retorno = TipoVeiculoComponenteDAO.update(tipoVeiculoComponente, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOVEICULOCOMPONENTE", tipoVeiculoComponente);
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
	public static Result remove(int cdTipoVeiculoComponente){
		return remove(cdTipoVeiculoComponente, false, null);
	}
	public static Result remove(int cdTipoVeiculoComponente, boolean cascade){
		return remove(cdTipoVeiculoComponente, cascade, null);
	}
	public static Result remove(int cdTipoVeiculoComponente, boolean cascade, Connection connect){
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
			retorno = TipoVeiculoComponenteDAO.delete(cdTipoVeiculoComponente, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_componente");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_veiculo_componente ",
							" ORDER BY nm_componente ",criterios , connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
