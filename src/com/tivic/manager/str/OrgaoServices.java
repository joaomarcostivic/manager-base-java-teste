package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.equipamento.EquipamentoServices;

public class OrgaoServices {
	
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
											 "FROM str_orgao " +
											 (cdCidade > 0 ? "WHERE cd_cidade = "+cdCidade : ""));
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
	
	public static ResultSetMap getAll() {
		return getAllByCidade(0, null);
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
											 "FROM str_orgao " +
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
}
