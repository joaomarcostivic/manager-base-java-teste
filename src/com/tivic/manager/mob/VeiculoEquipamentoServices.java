package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.rest.request.filter.Criterios;

public class VeiculoEquipamentoServices {
	
	public static final int DESVINCULADO = 0;
	public static final int VINCULADO   = 1;

	public static String[] situacaoVeiculo   = {"Desvinculado", "Vinculado"};
	
	public static Result save(VeiculoEquipamento veiculoEquipamento){
		return save(veiculoEquipamento, null);
	}
	
	public static Result save(VeiculoEquipamento veiculoEquipamento, Equipamento equipamento){
		return save(veiculoEquipamento, equipamento, null);
	}

	public static Result save(VeiculoEquipamento veiculoEquipamento, Equipamento equipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(veiculoEquipamento==null)
				return new Result(-1, "Erro ao salvar. VeiculoEquipamento é nulo");
		
			//Verifica se o mesmo equipamento está como VINCULADO para outro veículo
			PreparedStatement pstmt = connect.prepareStatement( "SELECT * FROM mob_veiculo_equipamento "+
																"WHERE cd_equipamento = " + veiculoEquipamento.getCdEquipamento() + 
																"  AND st_instalacao = " + VINCULADO + 
																"  AND cd_veiculo <> " + veiculoEquipamento.getCdVeiculo());
			ResultSetMap rsmEstaVinculado = new ResultSetMap(pstmt.executeQuery());
			
			if(rsmEstaVinculado.next()){
				
				ResultSetMap rsmVeiculo = VeiculoServices.getCompleto(rsmEstaVinculado.getInt("cd_veiculo"));
				rsmVeiculo.next();
				return new Result(-2, "Este equipamento está VINCULADO ao veículo placa " + rsmVeiculo.getString("nr_placa"));
			}
							
			int retorno;
			if (equipamento !=null) {
				Result r = EquipamentoServices.save(equipamento, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return r;
				}
				if (equipamento.getCdEquipamento() > 0) {
					veiculoEquipamento.setCdEquipamento(equipamento.getCdEquipamento());
				} else {
					veiculoEquipamento.setCdEquipamento(r.getCode());
				}
			}
			
			if(veiculoEquipamento.getCdInstalacao() ==0){
				retorno = VeiculoEquipamentoDAO.insert(veiculoEquipamento, connect);
				veiculoEquipamento.setCdInstalacao(retorno);
			}
			else {
				retorno = VeiculoEquipamentoDAO.update(veiculoEquipamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VEICULOEQUIPAMENTO", veiculoEquipamento);
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
	public static Result remove(int cdEquipamento, int cdVeiculo, int cdInstalacao){
		return remove(cdEquipamento, cdVeiculo, cdInstalacao,  false, null);
	}
	public static Result remove(int cdEquipamento, int cdVeiculo, int cdInstalacao, boolean cascade){
		return remove(cdEquipamento, cdVeiculo, cdInstalacao, cascade, null);
	}
	public static Result remove(int cdEquipamento, int cdVeiculo, int cdInstalacao,  boolean cascade, Connection connect){
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
				retorno = VeiculoEquipamentoDAO.delete(cdEquipamento, cdVeiculo, cdInstalacao, connect);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_equipamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByIdOrgaoAndColetivoUrbano(String idOrgao){
		
		return getAllByIdOrgaoAndTpConcessao(idOrgao, ConcessaoServices.TP_COLETIVO_URBANO);
	}
	
	public static ResultSetMap getAllByIdOrgaoAndTpConcessao(String idOrgao, int tpConcessao) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>(); 
		criterios.add(new ItemComparator("E.id_orgao", idOrgao, ItemComparator.EQUAL, Types.VARCHAR));
		criterios.add(new ItemComparator("A.st_instalacao", String.valueOf(VINCULADO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("F.tp_concessao", String.valueOf(tpConcessao), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("ORDERBY", "B.nr_prefixo", ItemComparator.LIKE, Types.VARCHAR));
		
		return getAllByIdOrgao(criterios, null);
	}
	
	public static ResultSetMap getAllByIdOrgao(ArrayList<ItemComparator> criterios, Connection connect) {
		
		try {
			String orderBy = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			String sql = "SELECT A.*, B.nr_prefixo, C.nr_placa, C.cd_tipo_veiculo, D.id_equipamento, E.* "+
						 "FROM mob_veiculo_equipamento A " +
						 "LEFT OUTER JOIN mob_concessao_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " + 
						 "LEFT OUTER JOIN fta_veiculo           C ON (A.cd_veiculo = C.cd_veiculo) "+
						 "LEFT OUTER JOIN grl_equipamento       D ON (A.cd_equipamento = D.cd_equipamento) "+						  
						 "LEFT OUTER JOIN mob_orgao             E ON (D.cd_orgao = E.cd_orgao) " + 
						 "LEFT OUTER JOIN mob_concessao         F ON (B.cd_concessao = F.cd_concessao)";
			
			return Search.find(sql, orderBy ,criterios, connect!=null? connect : Conexao.conectar(), connect==null);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoServices.getAllByIdOrgao: " + e);
			return null;
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
	
			String sql = "SELECT B.* FROM mob_veiculo_equipamento A " +
						 "LEFT JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) " +
						 "LEFT JOIN fta_veiculo     C ON (A.cd_veiculo = C.cd_veiculo) " +
						 "LEFT OUTER JOIN mob_concessao_veiculo D ON (A.cd_veiculo = D.cd_veiculo)";

			return Search.find(sql, criterios, connect!=null? connect : Conexao.conectar(), connect==null);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoServices.find: " + e);
			return null;
		}
	}
	
	public static VeiculoEquipamentoDTO get(int cdEquipamento, int cdVeiculo) {
		return get(cdEquipamento, cdVeiculo, null);
	}

	public static VeiculoEquipamentoDTO get(int cdEquipamento, int cdVeiculo, Connection connect) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_equipamento", Integer.toString(cdEquipamento), ItemComparator.EQUAL, Types.INTEGER);
			crt.add("A.cd_veiculo", Integer.toString(cdVeiculo), ItemComparator.EQUAL, Types.INTEGER);

			ResultSetMap rsm = find(crt);
			VeiculoEquipamentoDTO dto = new VeiculoEquipamentoDTO();
			
			if (rsm!=null && rsm.next()) {
				VeiculoEquipamento veiculoEquipamento = VeiculoEquipamentoDAO.get(rsm.getInt("CD_EQUIPAMENTO"), cdVeiculo);
				dto.setVeiculoEquipamento(veiculoEquipamento);
				
				Equipamento equipamento = EquipamentoDAO.get(cdEquipamento);
				dto.setEquipamento(equipamento);
			}
			
			return dto;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoServices.find: " + e);
			return null;
		}
	}
	
	public static int isValidTacografo(int cdConcessao) {
		Criterios criterios = new Criterios();
		criterios.add("D.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
		criterios.add("B.tp_equipamento", Integer.toString(EquipamentoServices.TACOGRAFO), ItemComparator.EQUAL, Types.INTEGER);
		criterios.add("A.st_instalacao", Integer.toString(VINCULADO), ItemComparator.EQUAL, Types.INTEGER);
		
		int aux = -1; // PENDENCIA DE CADASTRO
		ResultSetMap rsmEquipamentos = find(criterios);
		
		while (rsmEquipamentos!=null && rsmEquipamentos.getLines().size()>0 && rsmEquipamentos.next()) {
			GregorianCalendar dtValidade = rsmEquipamentos.getGregorianCalendar("DT_FINAL");
			
			if (dtValidade==null) {
				aux = -1;
				break;
			}
			
			if (dtValidade.after(new GregorianCalendar())) {
				aux = 1; // TACOGRAFO VALIDO
			} else {
				aux = 0; // TACOGRAFO VENCIDO
				break;
			}
		}
		
		return aux;
	}
}
