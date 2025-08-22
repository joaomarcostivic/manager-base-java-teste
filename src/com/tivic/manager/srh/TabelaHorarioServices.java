package com.tivic.manager.srh;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class TabelaHorarioServices {
	public static final int TP_NORMAL = 0;
	public static final int TP_RSR	   = 1;

	public static Result save(TabelaHorario tabelaHorario){
		return save(tabelaHorario, null, null);
	}

	public static Result save(TabelaHorario tabelaHorario, AuthData authData){
		return save(tabelaHorario, authData, null);
	}

	public static Result save(TabelaHorario tabelaHorario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaHorario==null)
				return new Result(-1, "Erro ao salvar. TabelaHorario é nulo");

			int retorno;
			if(tabelaHorario.getCdTabelaHorario()==0){
				retorno = TabelaHorarioDAO.insert(tabelaHorario, connect);
				tabelaHorario.setCdTabelaHorario(retorno);
			}
			else {
				retorno = TabelaHorarioDAO.update(tabelaHorario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELAHORARIO", tabelaHorario);
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
	public static Result remove(int cdTabelaHorario){
		return remove(cdTabelaHorario, false, null, null);
	}
	public static Result remove(int cdTabelaHorario, boolean cascade){
		return remove(cdTabelaHorario, cascade, null, null);
	}
	public static Result remove(int cdTabelaHorario, boolean cascade, AuthData authData){
		return remove(cdTabelaHorario, cascade, authData, null);
	}
	public static Result remove(int cdTabelaHorario, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TabelaHorarioDAO.delete(cdTabelaHorario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tabela_horario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaHorario getTabelaClick2Call(){
		Connection connect = Conexao.conectar();
		try {
			ResultSet rsTH = connect.prepareStatement("SELECT * FROM srh_tabela_horario " +
                    								   "WHERE nm_tabela_horario = \'CLICK2CALL\'").executeQuery();
			if(!rsTH.next())	{
				int cdEmpresa = 0; 
				ResultSet rsEmp = connect.prepareStatement("SELECT * FROM grl_pessoa A, grl_empresa B " +
						                                   "WHERE A.cd_pessoa = B.cd_empresa " +
						                                   "  AND nm_pessoa = \'CLICK2CALL\'").executeQuery();
				if(!rsEmp.next())	{
					com.tivic.manager.grl.Empresa empresa = new com.tivic.manager.grl.Empresa(0,0,0,"CLICK2CALL",null,null,null,null,null,
							                                                    new GregorianCalendar(),1,null,1,null,null,
							                                                    null,0,null,0,0,null,null,null,null,null,
							                                                    0,null,0,0,null,1,null,null,0);
					cdEmpresa = com.tivic.manager.grl.EmpresaDAO.insert(empresa, connect);
				}
				//
				TabelaHorario tabelaHorario = new TabelaHorario(0,"CLICK2CALL","C2C",0,0,cdEmpresa);
				TabelaHorarioDAO.insert(tabelaHorario, connect);
				return tabelaHorario;
			}
			else
				return TabelaHorarioDAO.get(rsTH.getInt("cd_tabela_horario"), connect);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM srh_tabela_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}



	
	
	
	
