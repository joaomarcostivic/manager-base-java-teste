package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ConcessaoLoteServices {

	public static final int TP_VEICULO_ONIBUS = 0;
	public static final int TP_VEICULO_MINIMO_9_LUGARES  = 1;
	public static final int TP_VEICULO_MINIMO_12_LUGARES  = 2;
	public static final int TP_VEICULO_MINIMO_16_LUGARES   = 3;
	public static final int TP_VEICULO_MINIMO_24_LUGARES    = 4;
	
	public static final String[] tipoVeiculo = {"Ônibus", "Veículo de no mínimo 9 lugares", "Veículo de no mínimo 12 lugares", "Veículo de no mínimo 16 lugares",
													"Veículo de no mínimo 24 lugares"};
	
	public static final int TP_TRANSPORTADOS_ALUNOS = 0;
	public static final int TP_TRANSPORTADOS_PROFESSORES  = 1;
	
	public static final String[] tipoTransportados = {"Alunos", "Professores"};
	public static final String[] tiposTurno = {"Matutino", "Vespertino", "Noturno"};
	
	public static Result save(ConcessaoLote concessaoLote){
		return save(concessaoLote, 0, null, null);
	}

	public static Result save(ConcessaoLote concessaoLote, int lgLinha){
		return save(concessaoLote, 1, null, null);
	}
	
	public static Result save(ConcessaoLote concessaoLote, AuthData authData){
		return save(concessaoLote, 0, authData, null);
	}

	public static Result save(ConcessaoLote concessaoLote, int lgLinha, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessaoLote==null)
				return new Result(-1, "Erro ao salvar. ConcessaoLote é nulo");

			
			ResultSetMap rsmLotes = getAllByConcessao(concessaoLote.getCdConcessao(), connect);
			while(rsmLotes.next()){
				if(rsmLotes.getInt("cd_concessao_lote") != concessaoLote.getCdConcessaoLote() && rsmLotes.getInt("nr_lote") == concessaoLote.getNrLote()){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Já existe um lote nessa concessão com o número " + concessaoLote.getNrLote());
				}
			}
			
			int retorno;
			if(concessaoLote.getCdConcessaoLote()==0){
				retorno = ConcessaoLoteDAO.insert(concessaoLote, connect);
				concessaoLote.setCdConcessaoLote(retorno);
			}
			else {
				retorno = ConcessaoLoteDAO.update(concessaoLote, connect);
			}
			
			if (retorno==1 && lgLinha==1) {
				Linha linha = LinhaDAO.get(concessaoLote.getCdLinha(), connect);
				linha.setCdConcessao(concessaoLote.getCdConcessao());
				retorno = LinhaServices.save(linha, connect).getCode();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSAOLOTE", concessaoLote);
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
	public static Result remove(ConcessaoLote concessaoLote) {
		return remove(concessaoLote.getCdConcessaoLote());
	}
	public static Result remove(int cdConcessaoLote){
		return remove(cdConcessaoLote, false, null, null);
	}
	public static Result remove(int cdConcessaoLote, boolean cascade){
		return remove(cdConcessaoLote, cascade, null, null);
	}
	public static Result remove(int cdConcessaoLote, boolean cascade, AuthData authData){
		return remove(cdConcessaoLote, cascade, authData, null);
	}
	public static Result remove(int cdConcessaoLote, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ConcessaoLoteDAO.delete(cdConcessaoLote, connect);
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
	
	public static Result removeConcessao(int cdConcessaoLote){
		return removeConcessao(cdConcessaoLote, null, null);
	}
	public static Result removeConcessao(int cdConcessaoLote, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			// Removendo a concessao e as informacoes do lote
			ConcessaoLote concessaoLote = ConcessaoLoteDAO.get(cdConcessaoLote, connect);
			ConcessaoLote concessaoLoteNovo = new ConcessaoLote();
			
			concessaoLoteNovo.setCdConcessaoLote(cdConcessaoLote);
			concessaoLoteNovo.setTpVeiculo(concessaoLote.getTpVeiculo());
			concessaoLoteNovo.setTpTransportados(concessaoLote.getTpTransportados());
			concessaoLoteNovo.setTpTurno(concessaoLote.getTpTurno());
			concessaoLoteNovo.setCdLinha(concessaoLote.getCdLinha());
			concessaoLoteNovo.setCdDistrito(concessaoLote.getCdDistrito());
			concessaoLoteNovo.setCdCidade(concessaoLote.getCdCidade());
			
			if(retorno>0) {
				retorno = ConcessaoLoteDAO.update(concessaoLoteNovo, connect);
			}
			
			// Removendo o lote da linha
			Linha linha = LinhaDAO.get(concessaoLote.getCdLinha(), connect);
			Linha linhaNova = new Linha();
			
			linhaNova.setCdLinha(linha.getCdLinha());
			linhaNova.setNrLinha(linha.getNrLinha());
			
			if(retorno>0) {
				retorno = LinhaDAO.update(linhaNova, connect);
			}
			
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_lote", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByConcessao(int cdConcessao) {
		return getAllByConcessao(cdConcessao, null);
	}

	public static ResultSetMap getAllByConcessao(int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.NR_LINHA FROM mob_concessao_lote A"
											+ " LEFT OUTER JOIN mob_linha B ON (A.cd_linha = B.cd_linha)"
											+ " WHERE A.cd_concessao = " + cdConcessao + " ORDER BY A.nr_lote");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_TP_VEICULO", tipoVeiculo[rsm.getInt("tp_veiculo")]);
				rsm.setValueToField("CL_TP_TRANSPORTADOS", tipoTransportados[rsm.getInt("tp_transportados")]);
				rsm.setValueToField("CL_TP_TURNO", tiposTurno[rsm.getInt("tp_turno")]);
				rsm.setValueToField("CL_VL_UNITARIO_POR_KM", Util.formatNumber(rsm.getDouble("vl_unitario_km"), 2));
				rsm.setValueToField("CL_VL_MENSAL", Util.formatNumber(rsm.getDouble("vl_mensal"), 2));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}