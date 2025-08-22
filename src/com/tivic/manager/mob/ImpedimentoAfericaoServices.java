package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class ImpedimentoAfericaoServices {
	
	
	public static final int TP_VEICULO    = 0;
	public static final int TP_CATRACA    = 1;
	public static final int TP_HODOMETRO  = 2;
	public static final String[] tiposReferente = {"Veículo","Catraca","Hodômetro"};
	
	public static Result save(ImpedimentoAfericao impedimentoAfericao){
		return save(impedimentoAfericao, null);
	}

	public static Result save(ImpedimentoAfericao impedimentoAfericao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(impedimentoAfericao==null)
				return new Result(-1, "Erro ao salvar. ImpedimentoAfericao é nulo");

			int retorno;
			if(impedimentoAfericao.getCdImpedimentoAfericao()==0){
				retorno = ImpedimentoAfericaoDAO.insert(impedimentoAfericao, connect);
				impedimentoAfericao.setCdImpedimentoAfericao(retorno);
			}
			else {
				retorno = ImpedimentoAfericaoDAO.update(impedimentoAfericao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "IMPEDIMENTOAFERICAO", impedimentoAfericao);
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
	public static Result remove(int cdImpedimentoAfericao){
		return remove(cdImpedimentoAfericao, false, null);
	}
	public static Result remove(int cdImpedimentoAfericao, boolean cascade){
		return remove(cdImpedimentoAfericao, cascade, null);
	}
	public static Result remove(int cdImpedimentoAfericao, boolean cascade, Connection connect){
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
			retorno = ImpedimentoAfericaoDAO.delete(cdImpedimentoAfericao, connect);
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
			
			pstmt = connect.prepareStatement("SELECT * FROM mob_impedimento_afericao" );
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find() {
		return find(null, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap getImpedimentosSelecionados(ArrayList<ItemComparator> criterios) {
		return getImpedimentosSelecionados(criterios, null);
	}	

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_impedimento_afericao ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getImpedimentosSelecionados(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_impedimento_afericao A " +
				"JOIN mob_afericao_impedimento B ON ( A.CD_IMPEDIMENTO_AFERICAO	= B.CD_IMPEDIMENTO_AFERICAO) " , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static String getListaImpedimentosByAfericao(int cdAfericao) {
		try{
			String listaMotivos = "";
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_afericao_catraca", "" + cdAfericao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = getImpedimentosSelecionados(criterios);
			
			while(rsm.next())
				listaMotivos += rsm.getString("nm_impedimento") + ", ";
			//remove a virgula e acrescenta um ponto
			if(listaMotivos.length() > 0)
				listaMotivos = listaMotivos.substring(0, listaMotivos.length() - 2)+".";
			
			return listaMotivos;
			
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoServices.getListaImpedimentosByAfericao: " + e);
			return null;			
		}		
	}

	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null);
	}
	
	public static HashMap<String, Object> getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_impedimento_afericao ORDER BY cd_impedimento_afericao");
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("ImpedimentoAfericao", Util.resultSetToArrayList(pstmt.executeQuery()));
						
			return register;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoServices.getSyncData: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoServices.getSyncData: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<Integer> getCdImpedimento(ArrayList<ImpedimentoAfericao> impedimentoAfericao ){
		ArrayList<Integer> impedimentos = new ArrayList<Integer>();
		
		if(impedimentoAfericao != null) {		
			for(int index=0; index < impedimentoAfericao.size(); index++ ) {
				impedimentos.add(impedimentoAfericao.get(index).getCdImpedimentoAfericao());			
			}
		}
		return impedimentos;
	}
}
