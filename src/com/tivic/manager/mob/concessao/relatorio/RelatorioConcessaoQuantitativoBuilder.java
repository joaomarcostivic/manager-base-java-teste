package com.tivic.manager.mob.concessao.relatorio;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.ConcessaoVeiculoServices;
import com.tivic.manager.mob.ConcessionarioPessoaServices;

import sol.dao.ResultSetMap;

public class RelatorioConcessaoQuantitativoBuilder {

	private List<RelatorioConcessaoQuantitativoDTO> listRelatorioConcessaoQuantitativoDTO;
	
	public RelatorioConcessaoQuantitativoBuilder() {
		listRelatorioConcessaoQuantitativoDTO = new ArrayList<RelatorioConcessaoQuantitativoDTO>();
	}
	
	public List<RelatorioConcessaoQuantitativoDTO> build() throws Exception {
		for (int tpConcessao = 0; tpConcessao<ConcessaoServices.tiposConcessao.length; tpConcessao++) {
			RelatorioConcessaoQuantitativoDTO relatorioConcessaoQuantitativoDTO = new RelatorioConcessaoQuantitativoDTO();
			relatorioConcessaoQuantitativoDTO.setNmTpConcessao(ConcessaoServices.tiposConcessao[tpConcessao]);
			ResultSetMap rsmConcessoes = buscaConcessoesAtivas(tpConcessao);
			relatorioConcessaoQuantitativoDTO.setQuantidadeConcessao(rsmConcessoes.size());
			while(rsmConcessoes.next()) {
				relatorioConcessaoQuantitativoDTO.setQuantidadeRepresentantes(buscaQuantitativoRepresentantes(rsmConcessoes.getInt("CD_CONCESSIONARIO")));
				relatorioConcessaoQuantitativoDTO.setQuantidadeAuxiliares(buscaQuantitativoAuxiliares(rsmConcessoes.getInt("CD_CONCESSIONARIO")));
				relatorioConcessaoQuantitativoDTO.setQuantidadeVeiculos(buscaQuantitativoVeiculos(rsmConcessoes.getInt("CD_CONCESSAO")));
			}
			listRelatorioConcessaoQuantitativoDTO.add(relatorioConcessaoQuantitativoDTO);
		}

		return listRelatorioConcessaoQuantitativoDTO;
	}
	
	private ResultSetMap buscaConcessoesAtivas(int tpConcessao) throws Exception {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			return new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_concessao WHERE tp_concessao = " + tpConcessao + " AND st_concessao = " + ConcessaoServices.ST_ATIVO).executeQuery());
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Falha na busca de concessões");
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	private int buscaQuantitativoRepresentantes(int cdConcessionario) throws Exception {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT COUNT(*) FROM mob_concessionario_pessoa A WHERE A.CD_CONCESSIONARIO = " + cdConcessionario + " AND A.TP_VINCULO = " +
																			ConcessionarioPessoaServices.TP_REPRESENTANTE + " AND A.ST_CONCESSIONARIO_PESSOA = " + ConcessionarioPessoaServices.ST_ATIVO).executeQuery());
			rsm.next();
			return rsm.getInt("COUNT");
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Falha na busca de concessões");
		} finally {
			Conexao.desconectar(connection);
		}
	}

	private int buscaQuantitativoAuxiliares(int cdConcessionario) throws Exception {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT COUNT(*) FROM mob_concessionario_pessoa A WHERE A.CD_CONCESSIONARIO = " + cdConcessionario + " AND A.TP_VINCULO = " +
												ConcessionarioPessoaServices.TP_AUXILIAR + " AND A.ST_CONCESSIONARIO_PESSOA = " + ConcessionarioPessoaServices.ST_ATIVO).executeQuery());
			rsm.next();
			return rsm.getInt("COUNT");
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Falha na busca de concessões");
		} finally {
			Conexao.desconectar(connection);
		}
	}

	private int buscaQuantitativoVeiculos(int cdConcessao) throws Exception {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT COUNT(*) FROM mob_concessao_veiculo A "
					+ "	JOIN fta_veiculo B  ON ( A.cd_veiculo = B.cd_veiculo ) "
					+ " JOIN mob_concessao D  ON ( A.cd_concessao = D.cd_concessao ) "
					+ "WHERE A.CD_CONCESSAO = " + cdConcessao + " AND A.ST_CONCESSAO_VEICULO = " + ConcessaoVeiculoServices.ST_VINCULADO).executeQuery());
			rsm.next();
			return rsm.getInt("COUNT");
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Falha na busca de concessões");
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	
}
