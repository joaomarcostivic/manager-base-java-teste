package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class ConcessaoLoteNotaFiscalServices {

	public static Result save(ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal){
		return save(concessaoLoteNotaFiscal, null, null);
	}

	public static Result save(ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal, AuthData authData){
		return save(concessaoLoteNotaFiscal, authData, null);
	}

	public static Result save(ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessaoLoteNotaFiscal==null)
				return new Result(-1, "Erro ao salvar. ConcessaoLoteNotaFiscal é nulo");

			int retorno;
			if(concessaoLoteNotaFiscal.getCdConcessaoLote()==0){
				retorno = ConcessaoLoteNotaFiscalDAO.insert(concessaoLoteNotaFiscal, connect);
				concessaoLoteNotaFiscal.setCdConcessaoLote(retorno);
			}
			else {
				retorno = ConcessaoLoteNotaFiscalDAO.update(concessaoLoteNotaFiscal, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSAOLOTENOTAFISCAL", concessaoLoteNotaFiscal);
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
	
	public static Result saveDiasFrequencia(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, int nrDiasFrequencia){
		return saveDiasFrequencia(cdConcessaoLote, cdConcessao, cdNotaFiscal, nrDiasFrequencia, null);
	}

	public static Result saveDiasFrequencia(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, int nrDiasFrequencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal = ConcessaoLoteNotaFiscalDAO.get(cdConcessaoLote, cdConcessao, cdNotaFiscal, connect);
			concessaoLoteNotaFiscal.setNrDiasFrequencia(nrDiasFrequencia);
			
			ConcessaoLote concessaoLote = ConcessaoLoteDAO.get(cdConcessaoLote, connect);
			
			concessaoLoteNotaFiscal.setVlMensalLote((new Double(concessaoLote.getVlMensal()/25*nrDiasFrequencia)).floatValue());
			
			int ret = ConcessaoLoteNotaFiscalDAO.update(concessaoLoteNotaFiscal, connect);
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(ret, "Erro ao atualizar concessao lote nota fiscal");
			}
			
			if (isConnectionNull)
				connect.commit();

			return new Result(ret, (ret<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSAOLOTENOTAFISCAL", concessaoLoteNotaFiscal);
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
	
	
	public static Result remove(ConcessaoLoteNotaFiscal concessaoLoteNotaFiscal) {
		return remove(concessaoLoteNotaFiscal.getCdConcessaoLote(), concessaoLoteNotaFiscal.getCdConcessao(), concessaoLoteNotaFiscal.getCdNotaFiscal());
	}
	public static Result remove(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal){
		return remove(cdConcessaoLote, cdConcessao, cdNotaFiscal, false, null, null);
	}
	public static Result remove(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, boolean cascade){
		return remove(cdConcessaoLote, cdConcessao, cdNotaFiscal, cascade, null, null);
	}
	public static Result remove(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, boolean cascade, AuthData authData){
		return remove(cdConcessaoLote, cdConcessao, cdNotaFiscal, cascade, authData, null);
	}
	public static Result remove(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ConcessaoLoteNotaFiscalDAO.delete(cdConcessaoLote, cdConcessao, cdNotaFiscal, connect);
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
	
	public static Result removeAllByNotaFiscal(int cdNotaFiscal){
		return removeAllByNotaFiscal(cdNotaFiscal, null);
	}
	
	public static Result removeAllByNotaFiscal(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = connect.prepareStatement("DELETE FROM mob_concessao_lote_nota_fiscal WHERE cd_nota_fiscal = " + cdNotaFiscal).executeUpdate();
			if(retorno<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao excluir registros");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Registros excluídos com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_nota_fiscal");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByNotaFiscal(int cdNotaFiscal) {
		return getAllByNotaFiscal(cdNotaFiscal, null);
	}

	public static ResultSetMap getAllByNotaFiscal(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_nota_fiscal A, mob_concessao_lote B "
					+ "							WHERE A.cd_concessao_lote = B.cd_concessao_lote "
					+ " 						  AND A.cd_concessao = B.cd_concessao "
					+ "							  AND cd_nota_fiscal = " + cdNotaFiscal
					+ "							ORDER BY B.nr_lote");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_TP_VEICULO", ConcessaoLoteServices.tipoVeiculo[rsm.getInt("tp_veiculo")]);
				rsm.setValueToField("CL_TP_TRANSPORTADOS", ConcessaoLoteServices.tipoTransportados[rsm.getInt("tp_transportados")]);
				rsm.setValueToField("CL_VL_UNITARIO_POR_KM", Util.formatNumber(rsm.getDouble("vl_unitario_km"), 2));
				rsm.setValueToField("CL_VL_MENSAL", Util.formatNumber(rsm.getDouble("vl_mensal"), 2));
				rsm.setValueToField("CL_VL_MENSAL_LOTE", Util.formatNumber(rsm.getDouble("vl_mensal_lote"), 2));				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalServices.getAllByNotaFiscal: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalServices.getAllByNotaFiscal: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_lote_nota_fiscal", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}