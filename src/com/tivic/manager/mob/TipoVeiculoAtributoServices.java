package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoVeiculoAtributoServices {

	public static Result save(TipoVeiculoAtributo tipoVeiculoAtributo){
		return save(tipoVeiculoAtributo, null);
	}

	public static Result save(TipoVeiculoAtributo tipoVeiculoAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoVeiculoAtributo==null)
				return new Result(-1, "Erro ao salvar. TipoVeiculoAtributo é nulo");
			
			int retorno;
			if( new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_atributo "+
					" WHERE  cd_tipo_veiculo_componente = "+tipoVeiculoAtributo.getCdTipoVeiculoComponente()+
					" AND nm_atributo = '"+tipoVeiculoAtributo.getNmAtributo()+"' "+
					( (tipoVeiculoAtributo.getCdTipoVeiculoAtributo() > 0)
							?"AND cd_tipo_veiculo_atributo <> "+tipoVeiculoAtributo.getCdTipoVeiculoAtributo():"" )
					).executeQuery()).size() > 0 ){
				return new Result(-1, "Este atributo já existe!!!");
			}
			if(tipoVeiculoAtributo.getCdTipoVeiculoAtributo()==0){
				retorno = TipoVeiculoAtributoDAO.insert(tipoVeiculoAtributo, connect);
				tipoVeiculoAtributo.setCdTipoVeiculoAtributo(retorno);
			}
			else {
				retorno = TipoVeiculoAtributoDAO.update(tipoVeiculoAtributo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOVEICULOATRIBUTO", tipoVeiculoAtributo);
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
	public static Result remove(int cdTipoVeiculoAtributo){
		return remove(cdTipoVeiculoAtributo, false, null);
	}
	public static Result remove(int cdTipoVeiculoAtributo, boolean cascade){
		return remove(cdTipoVeiculoAtributo, cascade, null);
	}
	public static Result remove(int cdTipoVeiculoAtributo, boolean cascade, Connection connect){
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
			retorno = TipoVeiculoAtributoDAO.delete(cdTipoVeiculoAtributo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_atributo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_veiculo_atributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	public static ResultSetMap findByVeiculo(ArrayList<ItemComparator> criterios, int cdVeiculo) {
		return findByVeiculo(criterios, cdVeiculo, null);
	}
	
	public static ResultSetMap findByVeiculo(ArrayList<ItemComparator> criterios, int cdVeiculo, Connection connect) {
		return Search.find("SELECT * FROM mob_tipo_veiculo_atributo A "+
							" LEFT JOIN mob_veiculo_atributo_valor B "+
							"					on ( A.cd_tipo_veiculo_atributo = B.cd_tipo_veiculo_atributo"+
							" 						AND B.cd_veiculo = "+cdVeiculo+") ",
							" ORDER BY A.nm_atributo ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
