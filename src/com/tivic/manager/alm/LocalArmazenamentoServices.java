package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.SetorDAO;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LocalArmazenamentoServices {
	
	public static Result save(LocalArmazenamento localArmazenamento){
		return save(localArmazenamento, null);
	}
	
	public static Result save(LocalArmazenamento localArmazenamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(localArmazenamento==null)
				return new Result(-1, "Erro ao salvar. Setor é nulo");
			
			if(localArmazenamento.getCdLocalArmazenamento() == localArmazenamento.getCdLocalArmazenamentoSuperior())
				return new Result(-1, "O local " + localArmazenamento.getNmLocalArmazenamento() + " não pode ser vinculado a ele mesmo.");
			
			int retorno;
			if(localArmazenamento.getCdLocalArmazenamento()==0){
				retorno = LocalArmazenamentoDAO.insert(localArmazenamento, connect);
				localArmazenamento.setCdLocalArmazenamento(retorno);
			}
			else {
				retorno = LocalArmazenamentoDAO.update(localArmazenamento, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SETOR", localArmazenamento);
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
	
	public static Result remove(int cdLocalArmazenamento){
		return remove(cdLocalArmazenamento, false, null);
	}
	
	public static Result remove(int cdLocalArmazenamento, boolean cascade){
		return remove(cdLocalArmazenamento, cascade, null);
	}
	
	public static Result remove(int cdLocalArmazenamento, boolean cascade, Connection connect){
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
				retorno = LocalArmazenamentoDAO.delete(cdLocalArmazenamento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este local está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Local excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir local!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static ResultSetMap getAllHierarquia(int cdEmpresa) {
		return getAllHierarquia(cdEmpresa, null);
	}

	public static ResultSetMap getAllHierarquia(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if (cdEmpresa > 0)
				criterios.add(new ItemComparator("CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = LocalArmazenamentoDAO.find(criterios, connect);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("CD_LOCAL_ARMAZENAMENTO_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdLocalArmazenamento = rsm.getInt("CD_LOCAL_ARMAZENAMENTO_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("CD_LOCAL_ARMAZENAMENTO", new Integer(rsm.getInt("CD_LOCAL_ARMAZENAMENTO_SUPERIOR")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_LOCAL_ARMAZENAMENTO")==cdLocalArmazenamento;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm     = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound    = subRsm==null ? false : subRsm.getInt("CD_LOCAL_ARMAZENAMENTO")==cdLocalArmazenamento;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("nm_local_armazenamento");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			return rsm;
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

	public static ResultSetMap getLocalArmazenamentoOf(int cdProdutoServico, int cdEmpresa){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cdProdutoServico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
		return findCompleto(criterios);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return findCompleto(criterios, connect);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			int cdProdutoServico = 0;
			int cdEmpresa = 0;
			boolean isCombustivel = false;
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdProdutoServico")) {
					cdProdutoServico = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_empresa")) {
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				}
			}
			if(cdProdutoServico > 0 && cdEmpresa > 0){
				ArrayList<Integer> cdGrupoCombustivel = GrupoServices.getAllCombustivelAsArray(cdEmpresa, connect);
				for(Integer cdGrupo : cdGrupoCombustivel){
					if(ProdutoGrupoDAO.get(cdProdutoServico, cdGrupo.intValue(), cdEmpresa, connect) != null){
						isCombustivel = true;
						break;
					}
				}
			}
			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_nivel_local, C.nm_pessoa, D.nm_setor " +
					           "FROM alm_local_armazenamento A " +
							   "LEFT OUTER JOIN alm_nivel_local B ON (A.cd_nivel_local = B.cd_nivel_local) " +
							   "LEFT OUTER JOIN grl_pessoa C ON (A.cd_responsavel = C.cd_pessoa) " +
							   "LEFT OUTER JOIN grl_setor D ON (A.cd_setor = D.cd_setor) " + 
							   "WHERE " + (isCombustivel ? "" : "NOT") + " EXISTS (SELECT * FROM pcb_tanque TAN WHERE A.cd_local_armazenamento = TAN.cd_tanque " + (cdProdutoServico > 0 && isCombustivel ? " AND TAN.cd_produto_servico = "+cdProdutoServico : "" ) +") ", 
							   criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			return rsm;
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLocaisArmazenamentoOutrosAlter(int cdEmpresa, int cdDocumentoEntrada, int cdProdutoServico) {
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_nivel_local, E.qt_entrada, E.qt_entrada_consignada " +
												             "FROM alm_local_armazenamento A " +
														     "LEFT OUTER JOIN alm_nivel_local        B ON (A.cd_nivel_local = B.cd_nivel_local) " +
														     "LEFT OUTER JOIN alm_entrada_local_item E ON (E.cd_local_armazenamento = A.cd_local_armazenamento " +
														     "                                         AND E.cd_documento_entrada   = "+cdDocumentoEntrada+
														     "                                         AND E.cd_produto_servico     = "+cdProdutoServico+") " + 
														     "WHERE A.cd_empresa = "+cdEmpresa+
														     "  AND NOT EXISTS  (SELECT cd_tanque FROM pcb_tanque TAN WHERE A.cd_local_armazenamento = TAN.cd_tanque) ").executeQuery());
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
	
	public static ResultSetMap findLocaisArmazenamentoEmpresa(int cdEmpresa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_empresa", "cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return findCompleto(criterios);
	}

	public static ResultSetMap findLocaisArmazenamentoEmpresa(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios);
	}

	public static ResultSetMap findCompleto(int cdEmpresa, ArrayList<ItemComparator> criterios) {
		criterios.add(new ItemComparator("A.cd_empresa", "cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return findCompleto(cdEmpresa, criterios, null);
	}

	public static ResultSetMap findCompleto(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_nivel_local, C.nm_pessoa, D.nm_setor " +
				           "FROM alm_local_armazenamento A " +
						   "LEFT OUTER JOIN alm_nivel_local B ON (A.cd_nivel_local = B.cd_nivel_local) " +
						   "LEFT OUTER JOIN grl_pessoa      C ON (A.cd_responsavel = C.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_setor       D ON (A.cd_setor = D.cd_setor) ", 
						   "ORDER BY A.nm_local_armazenamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAll() {
		return getAll(0, null);
	}
	
	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}


	public static ResultSetMap getAll(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_local_armazenamento " +
					                                           (cdEmpresa > 0 ? "WHERE cd_empresa = ?" : ""));
			
			if(cdEmpresa > 0)
				pstmt.setInt(1, cdEmpresa);
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
	 * 
	 * @param cdEmpresa
	 * @return
	 * @see #getNotTanques(int, Connection)
	 */
	public static ResultSetMap getNotTanques(int cdEmpresa) {
		return getNotTanques(cdEmpresa, null);
	}
	
	/**
	 * Método que busca todos os locais de armazenamento que não sejam tanques
	 * 
	 * @param cdEmpresa
	 * @return todos os locais de armazenamento diferentes de tanque
	 * @author Luiz Romario Filho
	 * @since 10/09/2014
	 */
	public static ResultSetMap getNotTanques(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_empresa = ? AND cd_local_armazenamento NOT IN (SELECT cd_tanque FROM pcb_tanque)");
			pstmt.setInt(1, cdEmpresa);
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
	 * 
	 * @param cdEmpresa
	 * @return
	 * @see #getTanques(int, Connection)
	 */
	public static ResultSetMap getTanques(int cdEmpresa) {
		return getTanques(cdEmpresa, null);
	}
	
	/**
	 * Método que busca todos os locais de armazenamento que sejam tanques
	 * 
	 * @param cdEmpresa
	 * @return todos os locais de armazenamento diferentes de tanque
	 * @author Luiz Romario Filho
	 * @since 06/10/2014
	 */
	public static ResultSetMap getTanques(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_empresa = ? AND cd_local_armazenamento IN (SELECT cd_tanque FROM pcb_tanque)");
			pstmt.setInt(1, cdEmpresa);
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

	public static String getListaLocaisInferiores(int cdLocalArmazenamento, String txtLista, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			txtLista += (txtLista.equals("")?"":",") + cdLocalArmazenamento;
			
			ResultSet rs = connect.prepareStatement("SELECT cd_local_armazenamento FROM alm_local_armazenamento WHERE cd_local_armazenamento_superior = "+cdLocalArmazenamento).executeQuery();
			
			while(rs.next())
				txtLista = getListaLocaisInferiores(rs.getInt("cd_local_armazenamento"), txtLista, connect);
			
			return txtLista;
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

	public static ResultSetMap getLocaisQueArmazenaOf(int cdEmpresa, int cdLocalArmazenamentoBase) {
		return getLocaisQueArmazenaOf(cdEmpresa, cdLocalArmazenamentoBase, null);
	}
	public static ResultSetMap getLocaisQueArmazenaOf(int cdEmpresa, int cdLocalArmazenamentoBase, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", "cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.lg_armazena", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = findCompleto(criterios);
			ResultSetMap rsmResult = new ResultSetMap();
			while(rsm.next())	{
				boolean isOfLocalBase = false;
				int cdLocalSuperior = rsm.getInt("cd_local_armazenamento_superior");
				String nmLocalSuperior = "";
				while(cdLocalSuperior>0)	{
					isOfLocalBase = isOfLocalBase || (cdLocalSuperior == cdLocalArmazenamentoBase);
					ResultSet rs = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_local_armazenamento = "+cdLocalSuperior).executeQuery();
					if(rs.next())	{
						if(cdLocalSuperior != rs.getInt("cd_local_armazenamento_superior")){
							nmLocalSuperior = rs.getString("nm_local_armazenamento")+(nmLocalSuperior.equals("") ? "" : " -> ")+nmLocalSuperior;
							cdLocalSuperior = rs.getInt("cd_local_armazenamento_superior");
						}
						else{
							isOfLocalBase = false;
							nmLocalSuperior = "";
							cdLocalSuperior = 0;
						}
					}
				}
				rsm.setValueToField("NM_LOCAL_SUPERIOR", nmLocalSuperior);
				//
				if(isOfLocalBase)
					rsmResult.addRegister(rsm.getRegister());
			}
			rsmResult.beforeFirst();
			return rsmResult;
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

}