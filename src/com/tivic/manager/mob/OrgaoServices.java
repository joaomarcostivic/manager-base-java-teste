package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class OrgaoServices {
	
	public static Result save(Orgao orgao){
		return save(orgao, null, null);
	}

	public static Result save(Orgao orgao, AuthData authData){
		return save(orgao, authData, null);
	}

	public static Result save(Orgao orgao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(orgao==null)
				return new Result(-1, "Erro ao salvar. Orgao é nulo");
			
			if(getOrgaoById(orgao.getIdOrgao()) != null && getOrgaoById(orgao.getIdOrgao()).next() && orgao.getCdOrgao() <= 0) {
				return new Result(-1, "Erro ao salvar. Já existe orgão com esse Id cadastrado");
			}

			int retorno;
			if(orgao.getCdOrgao()==0){
				retorno = OrgaoDAO.insert(orgao, connect);
				orgao.setCdOrgao(retorno);
			}
			else {
				retorno = OrgaoDAO.update(orgao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORGAO", orgao);
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
	
	public static Result remove(Orgao orgao) {
		return remove(orgao.getCdOrgao());
	}
	public static Result remove(int cdOrgao){
		return remove(cdOrgao, false, null, null);
	}
	public static Result remove(int cdOrgao, boolean cascade){
		return remove(cdOrgao, cascade, null, null);
	}
	public static Result remove(int cdOrgao, boolean cascade, AuthData authData){
		return remove(cdOrgao, cascade, authData, null);
	}
	public static Result remove(int cdOrgao, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OrgaoDAO.delete(cdOrgao, connect);
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
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_cidade, C.cd_estado, C.sg_estado, A.cd_agente_responsavel AS cd_agente, D.nm_agente "
					+ " FROM mob_orgao A"
					+ " LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)"
					+ " LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)"
					+ " LEFT OUTER JOIN "
					+ (Util.isStrBaseAntiga() ? 
							" agente D ON (A.cd_agente_responsavel = D.cod_agente) " : 
							" mob_agente D ON (A.cd_agente_responsavel = D.cd_agente) "));
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAll: " + e);
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
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		
		return Search.find("SELECT A.*, B.nm_pessoa AS nm_responsavel, C.nm_funcao AS nm_funcao_responsavel, D.*, E.* "
				+ " 		FROM mob_orgao A "
				+ "			LEFT OUTER JOIN grl_pessoa B ON (A.cd_responsavel = B.cd_pessoa)"
				+ "			LEFT OUTER JOIN srh_funcao C ON (A.cd_funcao_responsavel = C.cd_funcao)"
				+ "			LEFT OUTER JOIN grl_pessoa D ON (A.cd_pessoa_orgao = D.cd_pessoa)"
				+ "			LEFT OUTER JOIN grl_pessoa_endereco E ON (D.cd_pessoa = E.cd_pessoa AND E.lg_principal = 1)", 
				limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Lista todos os Orgão de uma cidade. 
	 * Caso o código da cidade seja 0, todos os orgaos de todas as cidades serão listados.
	 * @param cdCidade
	 * @return
	 */
	public static ResultSetMap getAllByCidade(int cdCidade) {
		return getAllByCidade(cdCidade, null);
	}

	public static ResultSetMap getAllByCidade(int cdCidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " + 
											 "FROM mob_orgao A " +
											 "LEFT OUTER JOIN grl_pessoa 		  B ON (A.cd_pessoa_orgao = B.cd_pessoa) "+
											 "LEFT OUTER JOIN grl_pessoa_endereco C ON (B.cd_pessoa = C.cd_pessoa) " +
											 (cdCidade > 0 ? "WHERE C.cd_cidade = " + cdCidade + 
													 		 "  AND C.lg_principal = " + PessoaEnderecoServices.ENDERECO_PRINCIPAL: ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAllByCidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAllByCidade: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOrgaoByCodigo(int cdOrgao) {
		return getOrgaoByCodigo(cdOrgao, null);
	}
	
	public static ResultSetMap getOrgaoByCodigo(int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " + 
											 "FROM mob_orgao " +
											 (cdOrgao > 0 ? "WHERE cd_orgao = "+cdOrgao : ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getOrgaoByCodigo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getOrgaoByCodigo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap getOrgaoById(String idOrgao) {
		return getOrgaoById(idOrgao, null);
	}
	
	private static ResultSetMap getOrgaoById(String idOrgao, Connection connect) {
		boolean isConnNull = (connect == null);
		
		if(isConnNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_ORGAO WHERE ID_ORGAO = ?");
			pstmt.setString(1, idOrgao);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull) 
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOrgaoByEquipamento(String idEquipamento) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>(); 
			criterios.add(new ItemComparator("id_equipamento", idEquipamento, ItemComparator.EQUAL, java.sql.Types.VARCHAR));
			ResultSetMap rsmOrgao = null;
			ResultSetMap rsmEquipamento = EquipamentoServices.find(criterios);
			if(rsmEquipamento.next()){
				rsmOrgao = getOrgaoByCodigo(rsmEquipamento.getInt("cd_orgao"));
			}
			return rsmOrgao;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getOrgaoByEquipamento: " + e);
			return null;
		}
	}
	
	public static Orgao getOrgaoUnico(){
		try {
			Orgao orgao = null;
			ResultSetMap rsmOrgao = OrgaoServices.getAll();
			while(rsmOrgao.next()){
				if(rsmOrgao.getInt("lg_emitir_ait")==0)
					continue;
				orgao = OrgaoDAO.get(rsmOrgao.getInt("cd_orgao"));
				break;
			}
			return orgao;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getOrgaoUnico: " + e);
			return null;
		}
	}
	
	public static String getSgEstadoOrgaoAutuador(){
		Orgao orgao = getOrgaoUnico();
		Cidade cidadeOrgao = CidadeDAO.get(orgao.getCdCidade());
		Estado estadoOrgao = EstadoDAO.get(cidadeOrgao.getCdEstado());
		return estadoOrgao.getSgEstado();
	}
	
	public static Cidade getCidadeOrgaoAutuador(){
		Orgao orgao = getOrgaoUnico();
		Cidade cidadeOrgao = CidadeDAO.get(orgao.getCdCidade());
		return cidadeOrgao;
	}
}
