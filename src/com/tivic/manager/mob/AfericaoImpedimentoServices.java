package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.exception.ValidationException;

public class AfericaoImpedimentoServices {
	
	public static final int LEGIVEL  = 0;
	public static final int ILEGIVEL = 1;
	
	public static Result save(AfericaoImpedimento afericaoImpedimento){
		return save(afericaoImpedimento, null);
	}

	public static Result save(AfericaoImpedimento afericaoImpedimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(afericaoImpedimento==null)
				return new Result(-1, "Erro ao salvar. AfericaoImpedimento é nulo");

			int retorno;
			if(afericaoImpedimento.getCdAfericaoImpedimento()==0){
				retorno = AfericaoImpedimentoDAO.insert(afericaoImpedimento, connect);
				afericaoImpedimento.setCdAfericaoImpedimento(retorno);
			}
			else {
				retorno = AfericaoImpedimentoDAO.update(afericaoImpedimento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AFERICAOIMPEDIMENTO", afericaoImpedimento);
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
	
	public static int atualizarImpedimentos (int cdAfericaoCatraca, int lgHodometroIlegivel, ArrayList<Integer> impedimentos, Connection connect) throws ValidationException {
		
		AfericaoImpedimento afericaoImpedimento = null;	
		
		int retorno = 0;
		retorno = clearImpedimentos(cdAfericaoCatraca, connect).getCode();
		
		if (retorno <= 0){
			Conexao.rollback(connect);
			throw new ValidationException("Erro ao apagar impedimentos.");
		}
		
		if(impedimentos!=null && impedimentos.size()>0 && lgHodometroIlegivel == ILEGIVEL) {
			for (Integer cdImpedimentoAfericao : impedimentos) {
				afericaoImpedimento = new AfericaoImpedimento(0, cdImpedimentoAfericao, cdAfericaoCatraca);				
				retorno = AfericaoImpedimentoDAO.insert(afericaoImpedimento,connect);
			}			
		}
		
		if(retorno<=0) {
			Conexao.rollback(connect);
			throw new ValidationException("Erro ao apagar impedimentos.");
		}
		
		return retorno;
	}
	
	private static Result clearImpedimentos(int cdAfericao, Connection connect) {
		boolean isConnectionNull = connect==null;
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			criterios.add(new ItemComparator("cd_afericao_catraca", "" + cdAfericao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap resultFind = AfericaoImpedimentoDAO.find(criterios);
			
			if(resultFind.getLines().size() > 0){				
			   connect.prepareStatement("DELETE FROM mob_afericao_impedimento "+
			   						    "WHERE cd_afericao_catraca = " + cdAfericao).executeUpdate();
			   return new Result(2, "Item deletado com sucesso!");
			}
			else{
				return  new Result(1, "Nenhuma aferição encontrada para o cÃ³digo! CD_AFERICAO =" + cdAfericao );
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir os impedimentos da aferição!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdAfericaoImpedimento){
		return remove(cdAfericaoImpedimento, false, null);
	}
	public static Result remove(int cdAfericaoImpedimento, boolean cascade){
		return remove(cdAfericaoImpedimento, cascade, null);
	}
	public static Result remove(int cdAfericaoImpedimento, boolean cascade, Connection connect){
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
			retorno = AfericaoImpedimentoDAO.delete(cdAfericaoImpedimento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_impedimento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_afericao_impedimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
