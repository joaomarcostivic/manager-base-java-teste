package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;

public class VistoriaPlanoItemArquivoServices {

	public static Result save(VistoriaPlanoItemArquivo vistoriaPlanoItemArquivo, Arquivo arquivo){
		return save(vistoriaPlanoItemArquivo, arquivo, null);
	}

	public static Result save(VistoriaPlanoItemArquivo vistoriaPlanoItemArquivo, Arquivo arquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(vistoriaPlanoItemArquivo==null)
				return new Result(-1, "Erro ao salvar. VistoriaPlanoItemArquivo é nulo");
			
			Result resultArquivo = ArquivoServices.save(arquivo, null, connect);
			if( resultArquivo.getCode() <= 0 )
				return new Result(-1, "Erro ao salvar Arquivo");
			
			int retorno = 1;
//			
			arquivo = (Arquivo) resultArquivo.getObjects().get("ARQUIVO");
			VistoriaPlanoItemArquivo VistoriaPlanoItemArquivoTmp = VistoriaPlanoItemArquivoDAO.get( 
																		vistoriaPlanoItemArquivo.getCdVistoriaPlanoItem(),
																		arquivo.getCdArquivo());
			if(VistoriaPlanoItemArquivoTmp==null){
				vistoriaPlanoItemArquivo.setCdArquivo(arquivo.getCdArquivo());
				retorno = VistoriaPlanoItemArquivoDAO.insert(vistoriaPlanoItemArquivo, connect);
			}
			else {
				retorno = VistoriaPlanoItemArquivoDAO.update(vistoriaPlanoItemArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VISTORIAPLANOITEMARQUIVO", vistoriaPlanoItemArquivo);
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
	public static Result remove(int cdVistoriaPlanoItem, int cdArquivo){
		return remove(cdVistoriaPlanoItem, cdArquivo, false, null);
	}
	public static Result remove(int cdVistoriaPlanoItem, int cdArquivo, boolean cascade){
		return remove(cdVistoriaPlanoItem, cdArquivo, cascade, null);
	}
	public static Result remove(int cdVistoriaPlanoItem, int cdArquivo, boolean cascade, Connection connect){
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
			retorno = VistoriaPlanoItemArquivoDAO.delete(cdVistoriaPlanoItem, cdArquivo, connect);
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
	public static Result removeAll(int cdVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = connect.prepareStatement(
					" DELETE FROM grl_arquivo WHERE cd_arquivo IN "+
					" (  SELECT cd_arquivo FROM mob_vistoria_plano_item_arquivo  A"+
					"    JOIN mob_vistoria_plano_item B on ( A.cd_vistoria_plano_item = B.cd_vistoria_plano_item "+
					"										 AND  B.cd_vistoria =  "+cdVistoria+" ) )"
					).executeUpdate();
			
			retorno = connect.prepareStatement(
					" DELETE FROM mob_vistoria_plano_item_arquivo "+
					" WHERE cd_vistoria_plano_item IN "+
					"(  SELECT cd_vistoria_plano_item FROM "+
					"          mob_vistoria_plano_item "+
					"          WHERE cd_vistoria = "+cdVistoria+" ) "
					).executeUpdate();
			
			
			if(retorno<0){
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemArquivoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_plano_item_arquivo A "+
						   " JOIN grl_arquivo B on (A.cd_arquivo = B.cd_arquivo) "
							, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
