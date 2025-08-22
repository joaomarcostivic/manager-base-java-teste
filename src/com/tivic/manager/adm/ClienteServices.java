package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.VinculoServices;

public class ClienteServices {

	public static final int TP_CREDITO_SEM_CREDITO = 0;
	public static final int TP_CREDITO_COM_CREDITO = 1;
	public static final int TP_CREDITO_SUSPENSO    = 2;
	public static final int TP_CREDITO_CANCELADO   = 3;
	
	public static final String[] tipoCredito = {"Sem crédito", "Com crédito", "Suspenso", "Cancelado"};
	
	//Situacao de crédito do cliente.
	public static final int ST_CLIENTE_SPC        = 0; // Servico de proteção ao crédito;
	public static final int ST_CLIENTE_SERASA     = 1; // Centralização dos Serviços Bancários S/A, estão são consultados com objetivo de proteger as empresas do 
	                                                   // comércio e/ou industria de fraudadores, ou pessoas individadas sem condições de assumirem novas dívidas. 
	                                                   // Minimizando/Eliminando assim o número de inadimplentes no pagamento de dívidas.
	
	public static int updateParametrosCliente(int cdPessoa,int cdEmpresa, String nmCampo, Object valor, String type){
		return updateParametrosCliente(cdPessoa, cdEmpresa, nmCampo, valor, type, null);
	}
	
	public static int updateParametrosCliente(int cdPessoa,int cdEmpresa, String nmCampo, Object valor, String type, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "UPDATE adm_cliente SET "+nmCampo+"=? WHERE cd_pessoa = ? AND cd_empresa = ?";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			if(type.equals("INT"))
				pstmt.setInt(1, Integer.parseInt(valor.toString()));
			else if(type.equals("String"))
				pstmt.setString(1, valor.toString());
			else if(type.equals("float"))
				pstmt.setFloat(1, Float.parseFloat(valor.toString()));
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdEmpresa);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteServices.updateParametrosCliente: " + e);
			return -1;
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
		String sql = "SELECT C.*,PC.nm_pessoa as nm_convenio,P.nm_pessoa" +
					 " FROM adm_cliente C " +
					 "JOIN grl_pessoa P ON(C.cd_pessoa = P.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa PC ON(C.cd_convenio = PC.cd_pessoa) ";
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getConveniados(int cdPessoa,int cdEmpresa) {
		return getConveniados(cdPessoa,cdEmpresa, null);
	}

	public static ResultSetMap getConveniados(int cdPessoa,int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT C.*,P.nm_pessoa " +
					     " FROM adm_cliente C " +
					     " JOIN grl_pessoa P ON(C.cd_pessoa = P.cd_pessoa)" +
					     " WHERE C.cd_convenio = " + cdPessoa + 
					     "  AND C.cd_empresa = " + cdEmpresa;
					
			PreparedStatement pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteServices.updateParametrosCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByEmpresa(int cdEmpresa) {
		return getAllByEmpresa(cdEmpresa, null);
	}

	public static ResultSetMap getAllByEmpresa(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT C.*,P.nm_pessoa " +
					     " FROM adm_cliente C " +
					     " JOIN grl_pessoa P ON(C.cd_pessoa = P.cd_pessoa)" +
					     " WHERE C.cd_empresa = " + cdEmpresa + " AND C.cd_pessoa = 14";
					
			PreparedStatement pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteServices.updateParametrosCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Metodo usado no DDL para fazer com que todos que sejam cadastrados com o vinculo CLIENTE, tenham um registro na tabela adm_cliente
	 * @since 12/08/2014
	 * @author Gabriel
	 * @param cdEmpresa
	 * @return
	 */
	public static void registrarCliente() {
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			int cdCliente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
			String sql = "SELECT * FROM grl_pessoa_empresa A WHERE A.cd_vinculo = "+cdCliente+" AND  A.st_vinculo = "+VinculoServices.PARCEIRO+" AND NOT EXISTS (SELECT * FROM adm_cliente B WHERE A.cd_pessoa = B.cd_pessoa AND A.cd_empresa = B.cd_empresa)";
					
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			while(rsm.next()){
				Cliente cliente = new Cliente(rsm.getInt("cd_empresa"), rsm.getInt("cd_pessoa"));
				if(ClienteDAO.insert(cliente, connect) < 0){
					System.out.println("Erro ao registrar pessoa com codigo " + rsm.getInt("cd_pessoa"));
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteServices.updateParametrosCliente: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
