package com.tivic.manager.srh;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.InstituicaoHorarioServices;
import com.tivic.manager.acd.InstituicaoPeriodo;
import com.tivic.manager.acd.InstituicaoPeriodoDAO;
import com.tivic.manager.acd.InstituicaoServices;
import com.tivic.manager.acd.ProfessorHorarioServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;

public class LotacaoServices {

	public static Result save(Lotacao lotacao){
		return save(lotacao, null);
	}

	public static Result save(Lotacao lotacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(lotacao==null)
				return new Result(-1, "Erro ao salvar. Lotacao é nulo");

			int retorno;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_setor", "" + lotacao.getCdSetor(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_matricula", "" + lotacao.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmLotacaoJaExiste = LotacaoDAO.find(criterios, connect);
			if(LotacaoDAO.get(lotacao.getCdLotacao(), lotacao.getCdMatricula(), connect)==null){
				if(!rsmLotacaoJaExiste.next()){
					retorno = LotacaoDAO.insert(lotacao, connect);
					lotacao.setCdLotacao(retorno);
				}
				else{
					lotacao.setCdLotacao(rsmLotacaoJaExiste.getInt("cd_lotacao"));
					retorno = LotacaoDAO.update(lotacao, connect);
				}
			}
			else {
				retorno = LotacaoDAO.update(lotacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOTACAO", lotacao);
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
	public static Result remove(int cdLotacao, int cdMatricula){
		return remove(cdLotacao, cdMatricula, false, null);
	}
	public static Result remove(int cdLotacao, int cdMatricula, boolean cascade){
		return remove(cdLotacao, cdMatricula, cascade, null);
	}
	public static Result remove(int cdLotacao, int cdMatricula, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 1;
			if(cascade){
				Lotacao lotacao = LotacaoDAO.get(cdLotacao, cdMatricula, connect);
				DadosFuncionais dadosFuncionais = DadosFuncionaisDAO.get(lotacao.getCdMatricula(), connect);
				Setor setor = SetorDAO.get(lotacao.getCdSetor(), connect);
				Pessoa instituicao = PessoaDAO.get(setor.getCdEmpresa(), connect);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", "" + setor.getCdEmpresa() + ", " + instituicao.getCdPessoaSuperior(), ItemComparator.IN, Types.INTEGER));
				ResultSetMap rsmInstituicaoHorario = InstituicaoHorarioServices.find(criterios, connect);
				while(rsmInstituicaoHorario.next()){
					retorno = ProfessorHorarioServices.remove(rsmInstituicaoHorario.getInt("cd_horario"), dadosFuncionais.getCdPessoa(), true, connect).getCode();
					if(retorno <= 0)
						break;
				}
			}
			
			if(!cascade || retorno>0)
				retorno = LotacaoDAO.delete(cdLotacao, cdMatricula, connect);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_lotacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca todos os cargos ocupados por uma pessoa
	 * 
	 * @param cdPessoa
	 * @return
	 */
	public static ResultSetMap findAllByPessoa(ArrayList<ItemComparator> criterios) {
		return findAllByPessoa(criterios, null);
	}
	public static ResultSetMap findAllByPessoa(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPessoa = 0;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdPessoa")) {
					cdPessoa = Integer.parseInt(criterios.get(i).getValue());
				}
			}
			
			ResultSetMap rsm = getAllByPessoa(cdPessoa, connect);
			
			while(rsm.next()){
				rsm.setValueToField("NM_INSTITUICAO", rsm.getString("nm_fantasia"));
				ResultSetMap rsmPeriodo = InstituicaoServices.getPeriodoLetivoVigente(rsm.getInt("CD_INSTITUICAO"), connect);
				if(rsmPeriodo.next())
					rsm.setValueToField("CD_PERIODO_LETIVO_ATUAL", rsmPeriodo.getInt("cd_periodo_letivo"));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			return rsm;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByPessoa(cdPessoa, 0, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		return getAllByPessoa(cdPessoa, 0, connect);
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa, int cdEmpresa) {
		return getAllByPessoa(cdPessoa, cdEmpresa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT A.*,"
					+ " B.nr_matricula, B.dt_matricula,"
					+ " C.cd_setor, C.nm_setor, C.sg_setor,"
					+ " D.cd_funcao, D.nm_funcao, D.id_funcao,"
					+ " E.cd_empresa, E.id_empresa, E1.nm_razao_social, E2.nm_pessoa AS nm_fantasia, E.cd_empresa AS cd_instituicao"
					+ " FROM srh_lotacao A"
					+ " JOIN srh_dados_funcionais B on (A.cd_matricula = B.cd_matricula AND "
					+ "									B.cd_pessoa = "+cdPessoa+")"
					+ " LEFT OUTER JOIN grl_setor C on (A.cd_setor = C.cd_setor)"
					+ " LEFT OUTER JOIN srh_funcao D on (A.cd_funcao = D.cd_funcao)"
					+ " LEFT OUTER JOIN grl_empresa E on (C.cd_empresa = E.cd_empresa)"
					+ " LEFT OUTER JOIN grl_pessoa_juridica E1 on (E.cd_empresa = E1.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_pessoa E2 on (E1.cd_pessoa = E2.cd_pessoa)"
					+ (cdEmpresa > 0 ? " WHERE C.cd_empresa = " + cdEmpresa : "")
					+ " ORDER BY nm_fantasia";
			
			pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoServices.getAllByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoServices.getAllByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca todas as empresas onde a pessoa está lotada
	 * @param cdPessoa
	 * @return
	 */
	public static ResultSetMap getAllEmpresasByPessoa(int cdPessoa) {
		return getAllEmpresasByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllEmpresasByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT DISTINCT(E.cd_empresa), E.id_empresa, "
					+ " E2.cd_pessoa, E2.nm_pessoa,"
					+ " E1.nm_razao_social, E2.nm_pessoa AS nm_fantasia, E.cd_empresa AS cd_instituicao"
					+ " FROM srh_lotacao A"
					+ " JOIN srh_dados_funcionais B on (A.cd_matricula = B.cd_matricula AND B.cd_pessoa ="+cdPessoa+")"
					+ " LEFT OUTER JOIN grl_setor C on (A.cd_setor = C.cd_setor)"
					+ " LEFT OUTER JOIN grl_empresa E on (C.cd_empresa = E.cd_empresa)"
					+ " LEFT OUTER JOIN grl_pessoa_juridica E1 on (E.cd_empresa = E1.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_pessoa E2 on (E1.cd_pessoa = E2.cd_pessoa)"
					+ " ORDER BY nm_fantasia";
			
			pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotacaoServices.getAllEmpresasByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotacaoServices.getAllEmpresasByPessoa: " + e);
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
		return Search.find("SELECT * FROM srh_lotacao A, srh_dados_funcionais B, grl_setor C WHERE A.cd_matricula = B.cd_matricula AND A.cd_setor = B.cd_setor ", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
	}

}
