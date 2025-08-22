package com.tivic.manager.cae;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoDAO;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;

public class IngredienteServices {

	public static Result save(Ingrediente ingrediente){
		return save(ingrediente, 0, 0, 0, null);
	}

	public static Result save(Ingrediente ingrediente, Connection connect){
		return save(ingrediente, 0, 0, 0, connect);
	}
	
	public static Result save(Ingrediente ingrediente, int cdUnidadeMedida, int cdInstituicao, int cdLocalArmazenamento){
		return save(ingrediente, cdUnidadeMedida, cdInstituicao, cdLocalArmazenamento, null);
	}

	public static Result save(Ingrediente ingrediente, int cdUnidadeMedida, int cdInstituicao, int cdLocalArmazenamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ingrediente==null)
				return new Result(-1, "Erro ao salvar. Ingrediente é nulo");

			int retorno;
			
			if(ingrediente.getNmProdutoServico() == null || ingrediente.getNmProdutoServico().trim() == "")
				ingrediente.setNmProdutoServico(ingrediente.getNmIngrediente());
			
			if(ingrediente.getCdIngrediente()==0){
				retorno = IngredienteDAO.insert(ingrediente, connect);
				ingrediente.setCdIngrediente(retorno);
			}
			else {
				retorno = IngredienteDAO.update(ingrediente, connect);
			}

			//Produto Servico Empresa
			ProdutoServicoEmpresa produtoServicoEmpresa = ProdutoServicoEmpresaDAO.get(cdInstituicao, ingrediente.getCdIngrediente(), connect);
			if(produtoServicoEmpresa == null){
				produtoServicoEmpresa = new ProdutoServicoEmpresa();
				produtoServicoEmpresa.setCdEmpresa(cdInstituicao);
				produtoServicoEmpresa.setCdLocalArmazenamento(cdLocalArmazenamento);
				produtoServicoEmpresa.setCdProdutoServico(ingrediente.getCdIngrediente());
				produtoServicoEmpresa.setCdUnidadeMedida(cdUnidadeMedida);
				produtoServicoEmpresa.setLgEstoqueNegativo(0);
				produtoServicoEmpresa.setStProdutoEmpresa(1);
				int ret = ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connect);
				if(ret <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar produto serviço empresa");
				}
			}
			else{
				produtoServicoEmpresa.setCdUnidadeMedida(cdUnidadeMedida);
				produtoServicoEmpresa.setCdLocalArmazenamento(cdLocalArmazenamento);
				int ret = ProdutoServicoEmpresaDAO.update(produtoServicoEmpresa, connect);
				if(ret <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar produto serviço empresa");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INGREDIENTE", ingrediente);
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
	public static Result remove(int cdIngrediente){
		return remove(cdIngrediente, false, null);
	}
	public static Result remove(int cdIngrediente, boolean cascade){
		return remove(cdIngrediente, cascade, null);
	}
	public static Result remove(int cdIngrediente, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				retorno = connect.prepareStatement("DELETE FROM grl_produto_servico_empresa WHERE cd_produto_servico = " + cdIngrediente).executeUpdate();
			}
			if(!cascade || retorno>0)
				retorno = IngredienteDAO.delete(cdIngrediente, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.*, D.nm_unidade_medida AS nm_unidade_medida_produto, D.cd_unidade_medida AS cd_unidade_medida_produto, E.nm_grupo "
					+ "FROM cae_ingrediente A"
				+ "					JOIN grl_produto_servico B ON (A.cd_ingrediente    = B.cd_produto_servico)"
				+ "					JOIN grl_produto_servico_empresa C ON (A.cd_ingrediente    = C.cd_produto_servico)"
				+ "					LEFT OUTER JOIN grl_unidade_medida D ON (C.cd_unidade_medida = D.cd_unidade_medida)"
				+ "					LEFT OUTER JOIN cae_grupo_ingrediente E ON (A.cd_grupo = E.cd_grupo) "
				+ "			 WHERE C.cd_empresa        = " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)
				+ " ORDER BY A.nm_ingrediente ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para retornar o percapta do ingrediente pela modalidade OU pelo grupo de cardapio
	 * @param cdIngrediente
	 * @param cdModalidade
	 * @return
	 */
	public static Double getPerCapta(int cdIngrediente, int cdModalidade) {
		return getPerCapta(cdIngrediente, cdModalidade);
	}
	
	public static Double getPerCapta(int cdIngrediente, int cdModalidade, Connection connect) {
		return getPerCapta(cdIngrediente, cdModalidade, 0, connect);
	}
	
	public static Double getPerCapta(int cdIngrediente, int cdModalidade, int cdCardapioGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			if (cdModalidade==0 && cdCardapioGrupo!=0) {
				CardapioGrupo cardapioGrupo = CardapioGrupoDAO.get(cdCardapioGrupo);
				cdModalidade = cardapioGrupo.getCdModalidade();
			}
			
			pstmt = connect.prepareStatement("SELECT A.vl_per_capta FROM cae_ingrediente_recomendacao A " +
					" LEFT OUTER JOIN cae_modalidade B ON (A.cd_recomendacao_nutricional = B.cd_recomendacao_nutricional) " +
					" WHERE B.cd_modalidade = ? " +
					" AND A.cd_ingrediente = ?");

			pstmt.setInt(1, cdModalidade);
			pstmt.setInt(2, cdIngrediente);
			
			ResultSetMap modalidadeIngrediente = new ResultSetMap(pstmt.executeQuery());
			Double perCapta = 0.0;
			
			if(modalidadeIngrediente.next())
				perCapta = modalidadeIngrediente.getDouble("vl_per_capta");
				
			return perCapta;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeIngredienteServices.getByIngrediente: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModalidadeIngredienteServices.getByIngrediente: " + e);
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
		return Search.find("SELECT A.*, B.*, D.nm_unidade_medida AS nm_unidade_medida_produto, D.cd_unidade_medida AS cd_unidade_medida_produto "
				+ "			 FROM cae_ingrediente A"
				+ "			        JOIN grl_produto_servico B ON (A.cd_ingrediente    = B.cd_produto_servico)"
				+ "					JOIN grl_produto_servico_empresa C ON (A.cd_ingrediente    = C.cd_produto_servico)"
				+ "					LEFT OUTER JOIN grl_unidade_medida D ON (C.cd_unidade_medida = D.cd_unidade_medida)"
				+ "					LEFT OUTER JOIN cae_grupo_ingrediente E ON (A.cd_grupo = E.cd_grupo) "
				+ "			 WHERE C.cd_empresa        = " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)
				+ " ", " ORDER BY A.nm_ingrediente ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
