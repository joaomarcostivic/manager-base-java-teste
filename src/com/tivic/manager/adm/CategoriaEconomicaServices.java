package com.tivic.manager.adm;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CategoriaEconomicaServices {

	public static final int TP_RECEITA         = 0;
	public static final int TP_DESPESA         = 1;
	public static final int TP_DEDUCAO_RECEITA = 3;
	public static final int TP_DEDUCAO_DESPESA = 4;
	public static final int TP_TRANSFERENCIA   = 5;

	public static final String idRECEITA 		 = "1";
	public static final String idDESPESA 		 = "2";
	public static final String idDEDUCAO_RECEITA = "3";
	public static final String idDEDUCAO_DESPESA = "4";
	public static final String idTRANSFERENCIA 	 = "5";

	public static final String[] tipoCategoria = {"Receita","Despesa","Dedução da Receita","Dedução de Despesa","Transferências"};
	
	public static Result save(CategoriaEconomica categoriaEconomica){
		return save(categoriaEconomica, null);
	}

	public static Result save(CategoriaEconomica categoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(categoriaEconomica==null)
				return new Result(-1, "Erro ao salvar. CategoriaEconomica é nulo");

			int retorno;
			if(categoriaEconomica.getCdCategoriaEconomica()==0){
				retorno = CategoriaEconomicaDAO.insert(categoriaEconomica, connect);
				categoriaEconomica.setCdCategoriaEconomica(retorno);
			}
			else {
				retorno = CategoriaEconomicaDAO.update(categoriaEconomica, connect);
				updateNrCategoriasInferiores( categoriaEconomica.getCdCategoriaEconomica(),
											  categoriaEconomica.getNrCategoriaEconomica(),
											  categoriaEconomica.getNrNivel(), connect);
				//Atualiza a ordenação das categorias filhas
				if( retorno > 0 && categoriaEconomica.getCdCategoriaSuperior() > 0 ){
					CategoriaEconomica catSuperior = CategoriaEconomicaDAO.get(categoriaEconomica.getCdCategoriaSuperior(), connect);
					updateNrCategoriasInferiores( catSuperior.getCdCategoriaEconomica(),
												catSuperior.getNrCategoriaEconomica(),
												catSuperior.getNrNivel(), connect);
				}else{
					//Se for categoria de nível 1 atualiza as categorias de mesmo nível
					Result r = updateNrCategoriasNivelUm( categoriaEconomica, connect);
					if( r.getCode() <= 0 ){
						if( isConnectionNull )
							Conexao.rollback(connect);
						return r;
						
					}
				}
			}
			
			alterarStatusSubCategorias(categoriaEconomica, connect);
			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CATEGORIAECONOMICA", categoriaEconomica);
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
	/***
	 * @author Alvaro
	 * @param categoriaEconomica
	 * @param connect
	 * @return
	 * @since 19/03/2015
	 *  Ativa ou inativa subcategorias
	 */
	private static Result alterarStatusSubCategorias( CategoriaEconomica categoriaEconomica, Connection connect ){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			ResultSetMap rsmSubCategorias = new ResultSetMap( connect.prepareStatement(
												"SELECT * FROM adm_categoria_economica "+
										        "WHERE cd_categoria_superior = "+categoriaEconomica.getCdCategoriaEconomica()).executeQuery());
			connect.prepareStatement(
					" UPDATE adm_categoria_economica "+
					" SET lg_ativo = "+categoriaEconomica.getLgAtivo()+
			        " WHERE cd_categoria_superior = "+categoriaEconomica.getCdCategoriaEconomica()).executeUpdate();
			
			rsmSubCategorias.beforeFirst();
			while ( rsmSubCategorias.next() ) {
				CategoriaEconomica subCategoria = CategoriaEconomicaDAO.get( rsmSubCategorias.getInt("CD_CATEGORIA_ECONOMICA"), connect );
				subCategoria.setLgAtivo( categoriaEconomica.getLgAtivo() );
				alterarStatusSubCategorias(subCategoria, connect);
			}

			return new Result(1);
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
	public static Result remove(int cdCategoriaEconomica){
		return remove(cdCategoriaEconomica, false, null);
	}
	public static Result remove(int cdCategoriaEconomica, boolean cascade){
		return remove(cdCategoriaEconomica, cascade, null);
	}
	public static Result remove(int cdCategoriaEconomica, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				connect.prepareStatement(
						"DELETE FROM adm_categoria_economica "+
		                "WHERE cd_categoria_superior = "+cdCategoriaEconomica).execute();
				retorno = 1;
			}
			
			//Verifica se há Contas/Movimentações classificadas com esta categoria
			ResultSet rs = connect.prepareStatement(
								" SELECT * FROM adm_categoria_economica  A "+
								" JOIN     adm_movimento_conta_categoria  B ON ( A.CD_CATEGORIA_ECONOMICA = B.CD_CATEGORIA_ECONOMICA ) "+
								" JOIN     adm_conta_pagar_categoria      C ON ( A.CD_CATEGORIA_ECONOMICA = C.CD_CATEGORIA_ECONOMICA  ) "+
								" JOIN     adm_conta_receber_categoria    D ON ( A.CD_CATEGORIA_ECONOMICA = D.CD_CATEGORIA_ECONOMICA ) "+
								" JOIN     adm_orcamento_categoria        E ON ( A.CD_CATEGORIA_ECONOMICA = E.CD_CATEGORIA_ECONOMICA ) "+
				                " WHERE A.cd_categoria_economica = "+cdCategoriaEconomica).executeQuery();
			if( rs.next() ){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Categoria não pôde ser excluida. Existem movimentações classificadas com esta categoria!");
			}
			
 			CategoriaEconomica catToRemove = CategoriaEconomicaDAO.get(cdCategoriaEconomica, connect);
			if(!cascade || retorno>0)
				retorno = CategoriaEconomicaDAO.delete(cdCategoriaEconomica, connect);
			
			//Atualiza a ordenação das categorias filhas
			if( retorno > 0 && catToRemove.getCdCategoriaSuperior() > 0 ){
				CategoriaEconomica catSuperior = CategoriaEconomicaDAO.get(catToRemove.getCdCategoriaSuperior(), connect);
				updateNrCategoriasInferiores( catSuperior.getCdCategoriaEconomica(),
											catSuperior.getNrCategoriaEconomica(),
											catSuperior.getNrNivel(), connect);
			}else{
				//Se for categoria de nível 1 atualiza as categorias de mesmo nível
				Result r = updateNrCategoriasNivelUm( catToRemove, connect);
				if( r.getCode() <= 0 ){
					if( isConnectionNull )
						Conexao.rollback(connect);
					return r;
					
				}
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
	
	public static void updateNrCategoriasInferiores(int cdCategoriaEconomica, String prefix, int nrNivel, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSet rs = connect.prepareStatement(
					" SELECT * FROM adm_categoria_economica "+
	                " WHERE cd_categoria_superior = "+cdCategoriaEconomica+
	                " ORDER BY nr_categoria_economica ").executeQuery();
			CategoriaEconomica categoriaSuperior = CategoriaEconomicaDAO.get(cdCategoriaEconomica, connect);
			int nrNivelCategoria = 1;
			while(rs.next()){
				CategoriaEconomica categoria = new CategoriaEconomica(rs.getInt("cd_categoria_economica"),
																	  rs.getInt("cd_categoria_superior"),
																	  rs.getString("nm_categoria_economica"),
																	  categoriaSuperior.getTpCategoriaEconomica(),
																	  prefix+"."+ Util.fillNum(nrNivelCategoria, 2),
																	  rs.getString("id_categoria_economica"),
																	  nrNivel+1,
																	  rs.getInt("cd_tabela_cat_economica"),
																	  categoriaSuperior.getLgAtivo(),
																	  rs.getDouble("vl_aliquota"),
																	  rs.getInt("lg_lancar_faturamento"));
				nrNivelCategoria++;
				CategoriaEconomicaDAO.update(categoria, connect);
				updateNrCategoriasInferiores(categoria.getCdCategoriaEconomica(), categoria.getNrCategoriaEconomica(), nrNivel+1, connect);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			System.err.println("Erro! CategoriaEconomicaServices.updateNrCategoriasInferiores: " +  e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result updateNrCategoriasNivelUm(CategoriaEconomica categoria, Connection connect)	{
 		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if( categoria == null )
				return new Result(-1, "Categoria é nula");

			if( categoria.getNrNivel() > 1 || categoria.getCdCategoriaSuperior() > 0 )
				return new Result(-1, "Categoria não é de nível 1.");
			
			
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement(
										" SELECT * FROM ADM_CATEGORIA_ECONOMICA "+
										" WHERE nr_nivel = 1 "+
										" and nr_categoria_economica > '"+categoria.getNrCategoriaEconomica()+"' ").executeQuery());
			
			int retorno = -1;
			while( rsm != null && rsm.next() ){
				CategoriaEconomica cat = CategoriaEconomicaDAO.get( rsm.getInt("CD_CATEGORIA_ECONOMICA"));
				int nrCategoria = Integer.parseInt(cat.getNrCategoriaEconomica().replace("0", ""));
				cat.setNrCategoriaEconomica( Util.fillNum(nrCategoria++, 2) );
				retorno = CategoriaEconomicaDAO.update(cat, connect);
				if( retorno <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, "Erro ao reordenar categorias de nível 1.");
				}
				
			}
			connect.commit();
			return new Result(1, "Categoria reorganizadas com sucesso!");
			
		}catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			System.err.println("Erro! CategoriaEconomicaServices.updateNrCategoriasNivelUm: " +  e);
			return null;
		}finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result permutarOrdemCategorias( CategoriaEconomica catOrigem, CategoriaEconomica catDestino){
		return permutarOrdemCategorias(catOrigem, catDestino, null);
	}
	public static Result permutarOrdemCategorias( CategoriaEconomica catOrigem, CategoriaEconomica catDestino, Connection connect ){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if( catOrigem == null || catDestino == null ){
				return new Result(-1, "Erro. Categorias são nulas.");
			}
			
			if( catOrigem.getTpCategoriaEconomica() != catDestino.getTpCategoriaEconomica() ){
				return new Result(-2, "Não é possível permutar categorias de naturaza diferente.");
			}

			if( catOrigem.getCdCategoriaSuperior() != catDestino.getCdCategoriaSuperior() 
				|| catOrigem.getNrNivel() != catDestino.getNrNivel()
			){
				return new Result(-2, "Não é possível permutar categorias de Níveis diferentes.");
			}
			
			CategoriaEconomica cloneCatOrigem = (CategoriaEconomica) catOrigem.clone();
			
			catOrigem.setNrCategoriaEconomica( catDestino.getNrCategoriaEconomica() );
			catOrigem.setIdCategoriaEconomica( catDestino.getIdCategoriaEconomica() );
			
			catDestino.setNrCategoriaEconomica( cloneCatOrigem.getNrCategoriaEconomica() );
			catDestino.setIdCategoriaEconomica( cloneCatOrigem.getIdCategoriaEconomica() );
			
			int rOrigem = CategoriaEconomicaDAO.update(catOrigem, connect);
			int rDestino = CategoriaEconomicaDAO.update(catDestino, connect);
			
			updateNrCategoriasInferiores( catOrigem.getCdCategoriaEconomica(),
					catOrigem.getNrCategoriaEconomica(),
					catOrigem.getNrNivel(), connect);

			updateNrCategoriasInferiores( catDestino.getCdCategoriaEconomica(),
					catDestino.getNrCategoriaEconomica(),
					catDestino.getNrNivel(), connect);
			
			if( rOrigem > 0 && rDestino > 0 ){
				connect.commit();
				return new Result(1, "Categorias permutadas con sucesso!");
			}else{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Falha ao permutar as categorias.");
			}
			
			
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.permutarOrdemCategorias: " +  e);
			return new Result(-1, "Falha ao permutar as categorias.");
		}finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String gerarNrCategoria(int tpCategoriaEconomica, int cdCategoriaSuperior, String idCategoria, Connection connect)	{
		try {
			String nrCategoria = idCategoria;
			ResultSet rs;
			PreparedStatement pstmt = connect.prepareStatement("SELECT id_categoria_economica, cd_categoria_superior " +
															   "FROM adm_categoria_economica "+
	                										   "WHERE cd_categoria_economica = ? ");
			while(cdCategoriaSuperior>0)	{
				pstmt.setInt(1, cdCategoriaSuperior);
				rs = pstmt.executeQuery();
				if(rs.next())	{
					nrCategoria = rs.getString("id_categoria_economica")+"."+nrCategoria;
					cdCategoriaSuperior = rs.getInt("cd_categoria_superior");
				}
			}
			return nrCategoria;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.gerarNrCategoria: " +  e);
			return null;
		}
	}

	public static int transferirClassificacao(int cdCategoriaAnterior, int cdNovaCategoria,
			boolean changeClassificacaoConta, boolean changeContasPagas,
			boolean changeClassificacaoMovimento, boolean changePeriodoConsolidado)
	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			// Susbstitui classificação de contas
			if(changeClassificacaoConta)	{
				/*
				 * CONTAS À RECEBER
				 */
				// Colocando o valor da categoria anterior na nova categoria (quando a categoria nova já existir)
				connect.prepareStatement(
						 "UPDATE adm_conta_receber_categoria " +
						 "SET vl_conta_categoria = vl_conta_categoria + (SELECT vl_conta_categoria FROM adm_conta_receber_categoria A " +
						 "                                               WHERE A.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber" +
						 "                                                 AND A.cd_categoria_economica = "+cdCategoriaAnterior+") "+
						 "WHERE cd_categoria_economica = "+cdNovaCategoria+
						 "  AND EXISTS (SELECT * FROM adm_conta_receber_categoria B " +
						 "              WHERE B.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber " +
						 "                AND B.cd_categoria_economica = "+cdCategoriaAnterior+") "+
						 (!changeContasPagas?" AND (SELECT st_conta FROM adm_conta_receber C " +
						                     "      WHERE C.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber) = "+ContaPagarServices.ST_EM_ABERTO:"")).executeUpdate();
				// Excluindo a categoria anterior onde já existir a categoria nova
				connect.prepareStatement(
						 "DELETE FROM adm_conta_receber_categoria " +
						 "WHERE cd_categoria_economica = "+cdCategoriaAnterior+
						 "  AND EXISTS (SELECT * FROM adm_conta_receber_categoria B " +
						 "              WHERE B.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber" +
						 "                AND B.cd_categoria_economica = "+cdNovaCategoria+") "+
						 (!changeContasPagas?" AND (SELECT st_conta FROM adm_conta_receber C " +
						                     "      WHERE C.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber) = "+ContaPagarServices.ST_EM_ABERTO:"")).executeUpdate();
				// Atualizando da categoria anterior para a nova categoria
				connect.prepareStatement("UPDATE adm_conta_receber_categoria SET cd_categoria_economica = "+cdNovaCategoria+
										 "WHERE cd_categoria_economica = "+cdCategoriaAnterior+
										 (!changeContasPagas?" AND (SELECT st_conta FROM adm_conta_receber B " +
										                     "      WHERE B.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber) = "+ContaPagarServices.ST_EM_ABERTO:"")).executeUpdate();


				/*
				 * CONTAS À PAGAR
				 */
				// Colocando o valor da categoria anterior na nova categoria (quando a categoria nova já existir)
				connect.prepareStatement(
						 "UPDATE adm_conta_pagar_categoria " +
						 "SET vl_conta_categoria = vl_conta_categoria + (SELECT vl_conta_categoria FROM adm_conta_pagar_categoria A " +
						 "                                               WHERE A.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar" +
						 "                                                 AND A.cd_categoria_economica = "+cdCategoriaAnterior+") "+
						 "WHERE cd_categoria_economica = "+cdNovaCategoria+
						 "  AND EXISTS (SELECT * FROM adm_conta_pagar_categoria B " +
						 "              WHERE B.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar" +
						 "                AND B.cd_categoria_economica = "+cdCategoriaAnterior+") "+
						 (!changeContasPagas?" AND (SELECT st_conta FROM adm_conta_pagar C " +
						                     "      WHERE C.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar) = "+ContaPagarServices.ST_EM_ABERTO:"")).executeUpdate();
				// Excluindo a categoria anterior onde já existir a categoria nova
				connect.prepareStatement(
						 "DELETE FROM adm_conta_pagar_categoria " +
						 "WHERE cd_categoria_economica = "+cdCategoriaAnterior+
						 "  AND EXISTS (SELECT * FROM adm_conta_pagar_categoria A " +
						 "              WHERE A.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar" +
						 "                AND A.cd_categoria_economica = "+cdNovaCategoria+") "+
						 (!changeContasPagas?" AND (SELECT st_conta FROM adm_conta_pagar B " +
						                     "      WHERE B.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar) = "+ContaPagarServices.ST_EM_ABERTO:"")).executeUpdate();
				// Atualizando da categoria anterior para a nova categoria
				connect.prepareStatement("UPDATE adm_conta_pagar_categoria SET cd_categoria_economica = "+cdNovaCategoria+
						 				 " WHERE cd_categoria_economica = "+cdCategoriaAnterior+
										 (!changeContasPagas?" AND (SELECT st_conta FROM adm_conta_pagar A " +
							                                 "      WHERE A.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar) = "+ContaPagarServices.ST_EM_ABERTO:"")).executeUpdate();
			}
			// Substitui classificação de movimentações
			if(changeClassificacaoConta)	{
				/*
				 * MOVIMENTO DE CONTA
				 */
				// Colocando o valor da categoria anterior na nova categoria (quando a categoria nova já existir)
				connect.prepareStatement(
						 "UPDATE adm_movimento_conta_categoria " +
						 "SET vl_movimento_categoria = vl_movimento_categoria + (SELECT vl_movimento_categoria FROM adm_movimento_conta_categoria A " +
						 "                                                       WHERE A.cd_conta               = adm_movimento_conta_categoria.cd_conta " +
						 "                                                         AND A.cd_movimento_conta     = adm_movimento_conta_categoria.cd_movimento_conta" +
						 "                                                         AND A.cd_categoria_economica = "+cdCategoriaAnterior+") "+
						 "WHERE cd_categoria_economica = "+cdNovaCategoria+
						 "  AND EXISTS (SELECT * FROM adm_movimento_conta_categoria B " +
						 "              WHERE B.cd_conta               = adm_movimento_conta_categoria.cd_conta " +
						 "                AND B.cd_movimento_conta     = adm_movimento_conta_categoria.cd_movimento_conta " +
						 "                AND B.cd_categoria_economica = "+cdCategoriaAnterior+") "+
						 (!changePeriodoConsolidado?
								 " AND (SELECT st_movimento FROM adm_movimento_conta C " +
						         "      WHERE C.cd_conta = adm_movimento_conta_categoria.cd_conta" +
						         "        AND C.cd_movimento_conta = adm_movimento_conta_categoria.cd_movimento_conta) = "+MovimentoContaServices.ST_CONCILIADO:"")).executeUpdate();
				// Excluindo a categoria anterior onde já existir a categoria nova
				connect.prepareStatement(
						 "DELETE FROM adm_movimento_conta_categoria " +
						 "WHERE cd_categoria_economica = "+cdCategoriaAnterior+
						 "  AND EXISTS (SELECT * FROM adm_movimento_conta_categoria A " +
						 "              WHERE A.cd_conta               = adm_movimento_conta_categoria.cd_conta " +
						 "                AND A.cd_movimento_conta     = adm_movimento_conta_categoria.cd_movimento_conta " +
						 "                AND A.cd_categoria_economica = "+cdNovaCategoria+") "+
						 (!changePeriodoConsolidado?
								 " AND (SELECT st_movimento FROM adm_movimento_conta B " +
						         "      WHERE B.cd_conta = adm_movimento_conta_categoria.cd_conta" +
						         "        AND B.cd_movimento_conta = adm_movimento_conta_categoria.cd_movimento_conta) = "+MovimentoContaServices.ST_CONCILIADO:"")).executeUpdate();
				// Atualizando da categoria anterior para a nova categoria
				connect.prepareStatement("UPDATE adm_movimento_conta_categoria SET cd_categoria_economica = "+cdNovaCategoria+
										 " WHERE cd_categoria_economica = "+cdCategoriaAnterior+
										 (!changePeriodoConsolidado?
												 " AND (SELECT st_movimento FROM adm_movimento_conta A " +
										         "      WHERE A.cd_conta = adm_movimento_conta_categoria.cd_conta" +
										         "        AND A.cd_movimento_conta = adm_movimento_conta_categoria.cd_movimento_conta) = "+MovimentoContaServices.ST_CONCILIADO:"")).executeUpdate();
			}
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.insert: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	
	public static int insert(CategoriaEconomica objeto)	{
		Connection connect = Conexao.conectar();
		try {
			if(objeto.getCdCategoriaSuperior()>0)	{
				ResultSet rs = connect.prepareStatement(
					"SELECT nr_nivel, tp_categoria_economica FROM adm_categoria_economica "+
	                "WHERE cd_categoria_economica = "+objeto.getCdCategoriaSuperior()).executeQuery();
				if(rs.next())	{
					objeto.setNrNivel(rs.getInt("nr_nivel")+1);
					objeto.setTpCategoriaEconomica(rs.getInt("tp_categoria_economica"));
				}
			}
			else	{
				objeto.setNrNivel(1);
			}
			objeto.setNrCategoriaEconomica(gerarNrCategoria(objeto.getTpCategoriaEconomica(), objeto.getCdCategoriaSuperior(), objeto.getIdCategoriaEconomica(), connect));
			return CategoriaEconomicaDAO.insert(objeto, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.insert: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int update(CategoriaEconomica objeto){
		Connection connect = Conexao.conectar();
		try {
			if(objeto.getCdCategoriaSuperior()>0)	{
				ResultSet rs = connect.prepareStatement(
					"SELECT nr_nivel FROM adm_categoria_economica "+
	                "WHERE cd_categoria_economica = "+objeto.getCdCategoriaSuperior()).executeQuery();
				if(rs.next())
					objeto.setNrNivel(rs.getInt("nr_nivel")+1);
			}
			else
				objeto.setNrNivel(0);
			objeto.setNrCategoriaEconomica(gerarNrCategoria(objeto.getTpCategoriaEconomica(), objeto.getCdCategoriaSuperior(), objeto.getIdCategoriaEconomica(), connect));
			updateNrCategoriasInferiores(objeto.getCdCategoriaEconomica(), objeto.getNrCategoriaEconomica(), objeto.getNrNivel(), connect);
			return CategoriaEconomicaDAO.update(objeto, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.update: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllHierarquia() {
		return getAllHierarquia(0, -1, null);
	}

	public static ResultSetMap getAllHierarquia(int cdEmpresa) {
		return getAllHierarquia(cdEmpresa, -1, null);
	}

	public static ResultSetMap getAllHierarquia(int cdEmpresa, int tpCreditoDebito) {
		return getAllHierarquia(cdEmpresa, tpCreditoDebito, null);
	}

	public static ResultSetMap getAllHierarquia(int cdEmpresa, int tpCreditoDebito, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();

			int catEconomicasByEmpresa = ParametroServices.getValorOfParametroAsInteger("LG_CATEGORIAS_ECONOMICAS_POR_EMPRESA", 0, 0, connect);
			if (catEconomicasByEmpresa == 1) {
				Empresa empresa = EmpresaDAO.get(cdEmpresa, connect);
				if(empresa.getCdTabelaCatEconomica() > 0)
					criterios.add(new ItemComparator("cd_tabela_cat_economica", Integer.toString(empresa.getCdTabelaCatEconomica()), ItemComparator.EQUAL, Types.INTEGER));
			}
			if (tpCreditoDebito!=-1)
				criterios.add(new ItemComparator("tpCreditoDebito", Integer.toString(tpCreditoDebito), ItemComparator.EQUAL, Types.SMALLINT));

			ResultSetMap rsm = find(criterios, connect);
			while (rsm != null && rsm.next()) {
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("NM_CATEGORIA", rsm.getString("NR_CATEGORIA_ECONOMICA") + " - " +rsm.getString("NM_CATEGORIA_ECONOMICA")+ativa);
				if (rsm.getInt("CD_CATEGORIA_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdCategoria = rsm.getInt("CD_CATEGORIA_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					/*
					 *  Procura o próximo registro que possua a mesma categoria superior
					 */
					if (rsm.locate("CD_CATEGORIA_ECONOMICA", new Integer(rsm.getInt("CD_CATEGORIA_SUPERIOR")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_CATEGORIA_ECONOMICA") == cdCategoria;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("CD_CATEGORIA_ECONOMICA")==cdCategoria;
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
	public static ResultSetMap getCategoriasSuperioresComMovimentacao(int cdEmpresa) {
		return getCategoriasSuperioresComMovimentacao(cdEmpresa, null);
	}
	
	public static ResultSetMap getCategoriasSuperioresComMovimentacao(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsmCat = getAllCategoria();
			rsmCat.beforeFirst();
			ResultSetMap rsm = new ResultSetMap();
			while( rsmCat.next() ){
				
				ResultSet rsContasPagar = connect.prepareStatement(
						" SELECT COUNT(B.cd_conta_pagar) AS QT_CONTA_PAGAR "+
						" FROM ADM_CATEGORIA_ECONOMICA A "+
						" LEFT JOIN ADM_CONTA_PAGAR_CATEGORIA B  ON   ( A.CD_CATEGORIA_ECONOMICA = B.CD_CATEGORIA_ECONOMICA) "+
						" LEFT JOIN ADM_CONTA_PAGAR           B2 ON   ( B.CD_CONTA_PAGAR = B2.CD_CONTA_PAGAR) "+
						" WHERE A.CD_CATEGORIA_ECONOMICA = "+rsmCat.getInt("CD_CATEGORIA_ECONOMICA")+
						" AND B2.CD_EMPRESA = "+cdEmpresa
					).executeQuery();
				
				ResultSet rsContasReceber = connect.prepareStatement(
						" SELECT  COUNT(C.cd_conta_receber) AS QT_CONTA_RECEBER "+
								" FROM ADM_CATEGORIA_ECONOMICA A "+
								" LEFT JOIN ADM_CONTA_RECEBER_CATEGORIA   C ON  ( A.CD_CATEGORIA_ECONOMICA = C.CD_CATEGORIA_ECONOMICA) "+
								" LEFT JOIN ADM_CONTA_RECEBER             C2 ON   ( C.CD_CONTA_RECEBER = C2.CD_CONTA_RECEBER) "+
								" WHERE A.CD_CATEGORIA_ECONOMICA = "+rsmCat.getInt("CD_CATEGORIA_ECONOMICA")+
								" AND C2.CD_EMPRESA = "+cdEmpresa
						).executeQuery();
				
				ResultSet rsMovimentacoes = connect.prepareStatement(
						" SELECT COUNT( D2.cd_movimento_conta ) AS QT_MOVIMENTACAO "+
								" FROM ADM_CATEGORIA_ECONOMICA A "+
								" LEFT JOIN ADM_MOVIMENTO_CONTA_CATEGORIA   D  ON  (A.CD_CATEGORIA_ECONOMICA = D.CD_CATEGORIA_ECONOMICA) "+
								" LEFT JOIN ADM_MOVIMENTO_CONTA             D2 ON  ( D2.CD_CONTA = D.CD_CONTA  AND D2.CD_MOVIMENTO_CONTA = D.CD_MOVIMENTO_CONTA ) "+
								" LEFT JOIN ADM_CONTA_FINANCEIRA                       D3 ON  ( D2.CD_CONTA = D3.CD_CONTA ) "+
								" WHERE A.CD_CATEGORIA_ECONOMICA = "+rsmCat.getInt("CD_CATEGORIA_ECONOMICA")+
								" AND D3.CD_EMPRESA = "+cdEmpresa
						).executeQuery();
				
				ResultSet rsHasChild = connect.prepareStatement(
						" SELECT COUNT( cd_categoria_economica ) AS QT_CHILDREN "+
								" FROM ADM_CATEGORIA_ECONOMICA A "+
								" WHERE A.CD_CATEGORIA_SUPERIOR = "+rsmCat.getInt("CD_CATEGORIA_ECONOMICA")
						).executeQuery();
				
				rsContasPagar.next();
				rsContasReceber.next();
				rsMovimentacoes.next();
				
				if( 
					( rsHasChild.next() && rsHasChild.getInt("QT_CHILDREN") > 0 ) &&
					(  rsContasPagar.getInt("QT_CONTA_PAGAR") > 0
						|| rsContasReceber.getInt("QT_CONTA_RECEBER") > 0
						|| rsMovimentacoes.getInt("QT_MOVIMENTACAO") > 0
					)
					
				){
					rsm.addRegister( rsmCat.getRegister() );
					rsm.last();
					rsm.setValueToField("QT_CONTA_PAGAR", rsContasPagar.getInt("QT_CONTA_PAGAR"));
					rsm.setValueToField("QT_CONTA_RECEBER", rsContasReceber.getInt("QT_CONTA_RECEBER"));
					rsm.setValueToField("QT_MOVIMENTACAO", rsMovimentacoes.getInt("QT_MOVIMENTACAO"));
				}
				
			}
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
	
	public static ResultSetMap getContasByCategoria(int cdCategoria, ArrayList<ItemComparator> criterios) {
		return getContasByCategoria(cdCategoria, criterios, null);
	}
	
	public static ResultSetMap getContasByCategoria(int cdCategoria, ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsmContasPagar = Search.find(
												" SELECT * FROM ADM_CONTA_PAGAR A "+
												" JOIN ADM_CONTA_PAGAR_CATEGORIA B ON ( A.CD_CONTA_PAGAR = B.CD_CONTA_PAGAR )"+
												" JOIN ADM_CONTA_FINANCEIRA      C ON ( A.CD_CONTA = C.CD_CONTA )"+
												" JOIN GRL_PESSOA                D ON ( A.CD_PESSOA = D.CD_PESSOA )"+
												" WHERE B.cd_categoria_economica = "+cdCategoria,
												"",
												criterios, connect, false); 
					
			ResultSetMap rsmContasReceber = Search.find(
										        " SELECT * FROM ADM_CONTA_RECEBER A "+
												" JOIN  ADM_CONTA_RECEBER_CATEGORIA B ON ( A.CD_CONTA_RECEBER = B.CD_CONTA_RECEBER )"+
												" JOIN ADM_CONTA_FINANCEIRA      C ON ( A.CD_CONTA = C.CD_CONTA )"+
												" JOIN GRL_PESSOA                D ON ( A.CD_PESSOA = D.CD_PESSOA )"+
												" WHERE B.cd_categoria_economica = "+cdCategoria,
												"",
												criterios, connect, false); 
			
			ResultSetMap rsm = new ResultSetMap();
			HashMap<String, Object> regConta = new HashMap<String, Object>();
			regConta.put("CONTAS_PAGAR", rsmContasPagar);
			regConta.put("CONTAS_RECEBER", rsmContasReceber);
			rsm.addRegister(regConta);
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

	public static Result reclassificarContasPagarCategorias(ArrayList<Integer> contasPagar, int cdCategoriaOld, int cdCategoriaNew) {
		return reclassificarContasPagarCategorias(contasPagar, cdCategoriaOld, cdCategoriaNew, null);
	}
	
	public static Result reclassificarContasPagarCategorias(ArrayList<Integer> contasPagar, int cdCategoriaOld, int cdCategoriaNew, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(false);
			
			ResultSetMap rsmContasCategoria = new ResultSetMap( connect.prepareStatement(
					" SELECT * FROM ADM_MOVIMENTO_CONTA_CATEGORIA"+
					" WHERE cd_categoria_economica = "+cdCategoriaOld+
					" AND cd_conta_pagar in ( "+Util.join(contasPagar)+" )  "
				).executeQuery()
			);
	
			rsmContasCategoria.beforeFirst();
			while (rsmContasCategoria.next()) {
				connect.prepareStatement(
						" UPDATE adm_movimento_conta_categoria set cd_categoria_economica =  "+cdCategoriaNew+
						" WHERE cd_categoria_economica = "+cdCategoriaOld+
						" AND cd_conta_pagar =  "+rsmContasCategoria.getInt("CD_CONTA_PAGAR")+
						" AND cd_conta =  "+rsmContasCategoria.getInt("CD_CONTA")+
						" AND cd_movimento_conta =  "+rsmContasCategoria.getInt("CD_MOVIMENTO_CONTA")+
						" AND cd_movimento_conta_categoria =  "+rsmContasCategoria.getInt("CD_MOVIMENTO_CONTA_CATEGORIA")
					).executeUpdate();
			}
			
			connect.prepareStatement(
						" UPDATE adm_conta_pagar_categoria set cd_categoria_economica =  "+cdCategoriaNew+
						" WHERE cd_categoria_economica = "+cdCategoriaOld+
						" AND cd_conta_pagar in ( "+Util.join(contasPagar)+" )  "
					).executeUpdate();
			connect.commit();
			return new Result(1, "Categorias Reclassificadas com Sucesso!");
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
	
	public static Result reclassificarContasReceberCategorias(ArrayList<Integer> contasReceber, int cdCategoriaOld, int cdCategoriaNew) {
		return reclassificarContasReceberCategorias(contasReceber, cdCategoriaOld, cdCategoriaNew, null);
	}
	
	public static Result reclassificarContasReceberCategorias(ArrayList<Integer> contasReceber,  int cdCategoriaOld, int cdCategoriaNew, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(false);
			
			ResultSetMap rsmContasCategoria = new ResultSetMap( connect.prepareStatement(
								" SELECT * FROM ADM_MOVIMENTO_CONTA_CATEGORIA "+
								" WHERE cd_categoria_economica = "+cdCategoriaOld+
								" AND cd_conta_receber in ( "+Util.join(contasReceber)+" )  "
							).executeQuery()
					);
			
			rsmContasCategoria.beforeFirst();
			while (rsmContasCategoria.next()) {
				connect.prepareStatement(
						" UPDATE adm_movimento_conta_categoria set cd_categoria_economica =  "+cdCategoriaNew+
						" WHERE cd_categoria_economica = "+cdCategoriaOld+
						" AND cd_conta_receber =  "+rsmContasCategoria.getInt("CD_CONTA_RECEBER")+
						" AND cd_conta =  "+rsmContasCategoria.getInt("CD_CONTA")+
						" AND cd_movimento_conta =  "+rsmContasCategoria.getInt("CD_MOVIMENTO_CONTA")+
						" AND cd_movimento_conta_categoria = "+rsmContasCategoria.getInt("CD_MOVIMENTO_CONTA_CATEGORIA")
					).executeUpdate();
			}
			
			connect.prepareStatement(
					" UPDATE adm_conta_receber_categoria set cd_categoria_economica =  "+cdCategoriaNew+
					" WHERE cd_categoria_economica = "+cdCategoriaOld+
					" AND cd_conta_receber in ( "+Util.join(contasReceber)+" )  "
				).executeUpdate();
			connect.commit();
			
			return new Result(1, "Categorias Reclassificadas com Sucesso!");
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int tpCreditoDebito = -1;
		boolean lgOrderByName = false; 
		for (int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("tpCreditoDebito")) {
				tpCreditoDebito = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
			if (criterios.get(i).getColumn().equalsIgnoreCase("lgOrderByName")) {
				lgOrderByName = true;
				criterios.remove(i);
				break;
			}
		}
		int tpCategoriaEconomica1 = tpCreditoDebito==MovimentoContaServices.CREDITO ? TP_RECEITA : TP_DESPESA;
		int tpCategoriaEconomica2 = tpCreditoDebito==MovimentoContaServices.CREDITO ? TP_DEDUCAO_RECEITA : TP_DEDUCAO_DESPESA;
		ResultSetMap rsm = Search.find("SELECT * " +
				"FROM adm_categoria_economica " +
				"WHERE 1 = 1 " +
				(tpCreditoDebito==-1 ? "" : ("  AND (tp_categoria_economica = " + tpCategoriaEconomica1 + " OR tp_categoria_economica = " + tpCategoriaEconomica2 +") ")),
				(lgOrderByName ? "" : "ORDER BY nr_categoria_economica"), criterios, Conexao.conectar(), true);
		while(rsm.next()){
			String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
			rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
			rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
			CategoriaEconomica categoriaSuperior = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_superior"), connect);
			if(categoriaSuperior!=null) {
				rsm.setValueToField("NM_CATEGORIA_SUPERIOR", categoriaSuperior.getNmCategoriaEconomica());
				rsm.setValueToField("CL_CATEGORIA_ECONOMICA_WITH_SUPERIOR", categoriaSuperior.getNmCategoriaEconomica() + " - " + rsm.getString("nm_categoria_economica"));
			}
		}
		rsm.beforeFirst();
		
		if(lgOrderByName){
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_CATEGORIA_SUPERIOR");
			fields.add("NM_CATEGORIA_ECONOMICA");
			rsm.orderBy(fields);
			rsm.beforeFirst();
		}
		
		
		return rsm;
	}

	public static ResultSetMap getAllCategoria() {
		String sql = "SELECT * FROM adm_categoria_economica A";
		ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", new ArrayList<ItemComparator>(), Conexao.conectar(), true);
		while(rsm.next()){
			String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
			rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
			rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
		}
		rsm.beforeFirst();
		return rsm;
	}

	public static ResultSetMap getAllCategoriaDespesa() {
		try {
			String sql = "SELECT * " +
					"FROM adm_categoria_economica A " +
					"WHERE A.tp_categoria_economica IN (1,3)";
			ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", new ArrayList<ItemComparator>(), Conexao.conectar(), true);
			Connection connAux = Conexao.conectar();
			while(rsm.next()){
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
				rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
				
				ResultSet rs = connAux.prepareStatement("SELECT COUNT(CD_CATEGORIA_ECONOMICA) AS QT_CATEGORIAS "+
						 " FROM adm_categoria_economica  "+
						 " WHERE CD_CATEGORIA_SUPERIOR =  "+rsm.getInt("CD_CATEGORIA_ECONOMICA")).executeQuery();
				if( rs.next() && rs.getInt("QT_CATEGORIAS") > 0 && ParametroServices.getValorOfParametroAsInteger("LG_DESABILITAR_SELECAO_CATEGORIA_RAIZ", 0) > 0  ){
					rsm.setValueToField("_ITEM_DISABLED", true);
				}
			}
			rsm.beforeFirst();
			return rsm;
		} 
		catch(Exception e) {
			return null;
		}
	}

	public static ResultSetMap getAllCategoriaReceita() {
		try {
			String sql = "SELECT * FROM adm_categoria_economica A WHERE A.tp_categoria_economica IN (0,1,2,3)";
			ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", new ArrayList<ItemComparator>(), Conexao.conectar(), true);
			
			Connection connAux = Conexao.conectar();
			while(rsm.next()){
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
				rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
				
				ResultSet rs = connAux.prepareStatement("SELECT COUNT(CD_CATEGORIA_ECONOMICA) AS QT_CATEGORIAS "+
						 " FROM adm_categoria_economica  "+
						 " WHERE CD_CATEGORIA_SUPERIOR =  "+rsm.getInt("CD_CATEGORIA_ECONOMICA")).executeQuery();
				if( rs.next() && rs.getInt("QT_CATEGORIAS") > 0 && ParametroServices.getValorOfParametroAsInteger("LG_DESABILITAR_SELECAO_CATEGORIA_RAIZ", 0) > 0  ){
					rsm.setValueToField("_ITEM_DISABLED", true);
				}
			}
			rsm.beforeFirst();
			return rsm;
		} catch(Exception e) {
			return null;
		}
	}

	public static ResultSetMap findCategoriaDespesa(ArrayList<ItemComparator> criterios) {
		return findCategoriaDespesa(criterios, null);
	}
	public static ResultSetMap findCategoriaDespesa(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try	{
			String sql = "SELECT * FROM adm_categoria_economica A WHERE A.tp_categoria_economica IN ("+TP_DESPESA+","+TP_DEDUCAO_RECEITA+")";
			ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", criterios, connect, false);
			ResultSet rs;
			while(rsm.next()){
				rs = connect.prepareStatement("SELECT COUNT(CD_CATEGORIA_ECONOMICA) AS QT_CATEGORIAS "+
											 " FROM adm_categoria_economica  "+
											 " WHERE CD_CATEGORIA_SUPERIOR =  "+rsm.getInt("CD_CATEGORIA_ECONOMICA")).executeQuery();
				if( rs.next() && rs.getInt("QT_CATEGORIAS") > 0 
						&& ParametroServices.getValorOfParametroAsInteger("LG_DESABILITAR_SELECAO_CATEGORIA_RAIZ", 0) > 0  ){
					rsm.setValueToField("_ITEM_DISABLED", true);
				}
				
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
						rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.findCategoriaDespesa: " + e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findCategoriaReceita(ArrayList<ItemComparator> criterios){
		return findCategoriaReceita(criterios, null);
	}
	public static ResultSetMap findCategoriaReceita(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try	{
			String sql = "SELECT * FROM adm_categoria_economica A WHERE A.tp_categoria_economica IN ("+TP_RECEITA+","+TP_DEDUCAO_DESPESA+")";
			ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", criterios, connect, false);
			ResultSet rs;
			while(rsm.next()){
				rs = connect.prepareStatement("SELECT COUNT(CD_CATEGORIA_ECONOMICA) AS QT_CATEGORIAS "+
											 " FROM adm_categoria_economica  "+
											 " WHERE CD_CATEGORIA_SUPERIOR =  "+rsm.getInt("CD_CATEGORIA_ECONOMICA")).executeQuery();
				if( rs.next() && rs.getInt("QT_CATEGORIAS") > 0 
						&& ParametroServices.getValorOfParametroAsInteger("LG_DESABILITAR_SELECAO_CATEGORIA_RAIZ", 0) > 0  ){
					rsm.setValueToField("_ITEM_DISABLED", true);
				}
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
						rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.findCategoriaReceita: " + e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findCategoriaReducaoReceita(ArrayList<ItemComparator> criterios) {
		return findCategoriaReducaoReceita(criterios, null);
	}
	
	public static ResultSetMap findCategoriaReducaoReceita(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try	{
			String sql = "SELECT * FROM adm_categoria_economica A WHERE A.tp_categoria_economica = "+TP_DEDUCAO_RECEITA;
			ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", criterios, Conexao.conectar(), true);
			while(rsm.next()){
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
				rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
			}
			rsm.beforeFirst();
			return rsm;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.findCategoriaReceita: " + e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findCategoriaReducaoDespesa(ArrayList<ItemComparator> criterios) {
		return findCategoriaReducaoDespesa(criterios, null);
	}
	public static ResultSetMap findCategoriaReducaoDespesa(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try	{
			String sql = "SELECT * FROM adm_categoria_economica A WHERE A.tp_categoria_economica = "+TP_DEDUCAO_DESPESA;
			ResultSetMap rsm = Search.find(sql, "ORDER BY tp_categoria_economica, nr_categoria_economica", criterios, Conexao.conectar(), true);
			while(rsm.next()){
				String ativa = ( rsm.getInt("LG_ATIVO") == 1 )?"":"(inativa)";
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", rsm.getInt("nr_nivel")*5, ' ', 'E')+
						rsm.getString("nr_categoria_economica")+" - "+rsm.getString("nm_categoria_economica")+ativa);
			}
			rsm.beforeFirst();
			return rsm;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.findCategoriaReceita: " + e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDetalheOfCategoriaPeriodo(int cdCategoriaEconomica, int nrMes, int nrAno, int tpData, int cdConta) {
		Connection connect = Conexao.conectar();
		try	{
			GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1, 0, 0, 0);
			GregorianCalendar dtFinal   = new GregorianCalendar(nrAno, nrMes, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			return getDetalheOfCategoriaPeriodo(cdCategoriaEconomica, dtInicial, dtFinal, tpData, cdConta, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.getDetalheOfCategoriaPeriodo: " + e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdCategoriaEconimica Categoria econômica da qual se deseja ver os detalhes
     * @param dtInicial Inicio do período do qual se deseja ver o detalhamento
     * @param dtFinal Final do período do qual se deseja ver o detalhamento
     * @return ResultSetMap Relação de contas classificadas com a categoria desejada
     */
	public static ResultSetMap getDetalheOfCategoriaPeriodo(int cdCategoriaEconomica, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpData, int cdConta, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			CategoriaEconomica cat = CategoriaEconomicaDAO.get(cdCategoriaEconomica, connect);
			if(cat==null)
				return new ResultSetMap();
			String sql;
			if (cat.getTpCategoriaEconomica()==TP_DEDUCAO_RECEITA || cat.getTpCategoriaEconomica()==TP_RECEITA)	{
				sql =  "SELECT A.cd_conta_receber, A.vl_conta_categoria, B.dt_emissao, B.dt_vencimento, B.vl_conta," +
					   "       B.vl_recebido, B.nr_documento, B.ds_historico, C.nm_pessoa "+
			           "FROM adm_conta_receber_categoria A " +
			           "JOIN adm_conta_receber           B ON (A.cd_conta_receber = B.cd_conta_receber) "+
			           "LEFT OUTER JOIN grl_pessoa       C ON (B.cd_pessoa = C.cd_pessoa) "+
			           "WHERE A.cd_categoria_economica = " +cdCategoriaEconomica+
			           (cdConta>0 ? "  AND B.cd_conta = "+cdConta : "")+
			           "  AND "+(tpData==0?"B.dt_emissao":"B.dt_vencimento")+" BETWEEN ? AND ?";
			}
			else	{
				sql =  "SELECT A.cd_conta_pagar, A.vl_conta_categoria, B.dt_emissao, B.dt_vencimento, B.vl_conta," +
					   "       B.vl_pago, B.nr_documento, B.ds_historico, C.nm_pessoa "+
			           "FROM adm_conta_pagar_categoria A " +
			           "JOIN adm_conta_pagar           B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
			           "LEFT OUTER JOIN grl_pessoa     C ON (B.cd_pessoa = C.cd_pessoa) "+
			           "WHERE A.cd_categoria_economica = " +cdCategoriaEconomica+
			           (cdConta>0 ? "  AND B.cd_conta = "+cdConta : "")+
			           "  AND "+(tpData==0?"B.dt_emissao":"B.dt_vencimento")+" BETWEEN ? AND ?";
			}
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.getDetalheOfCategoriaPeriodo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver os movimentos
     * @param nrMes Mês do qual se deseja verificar o movimento
     * @param nrAno Ano do qual se deseja verificar o movimento
     * @return Array com os totais agrupados em Receita e Despesa
     */
	public static float[] getDespesaReceita(int cdEmpresa, int nrMes, int nrAno) {
		Connection connect = Conexao.conectar();
		try	{
			GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1, 0, 0, 0);
			GregorianCalendar dtFinal   = new GregorianCalendar(nrAno, nrMes, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			return getDespesaReceita(cdEmpresa, dtInicial, dtFinal, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaServices.getDetalheOfCategoriaPeriodo: " + e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver os movimentos
     * @param dtInicial Data inicial do período desejado
     * @param dtFinal Data final do período desejado
     * @return Array com os totais agrupados em Receita e Despesa
     */
	public static float[] getDespesaReceita(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Receitas
			pstmt = connect.prepareStatement(
					   "SELECT B.tp_categoria_economica, COUNT(*) AS qt_itens, SUM(vl_conta_categoria) AS vl_total "+
					   "FROM adm_conta_receber_categoria A "+
					   "JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica) "+
					   "JOIN adm_conta_receber       C ON (A.cd_conta_receber   = C.cd_conta_receber) "+
					   "WHERE C.dt_emissao BETWEEN ? AND ? "+
					   "GROUP BY B.tp_categoria_economica");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			float vlReceita = 0, vlDespesa = 0;
			while(rs.next())
				if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_RECEITA)
					vlReceita += rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DESPESA)
					vlDespesa += rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DEDUCAO_RECEITA)
					vlReceita -= rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DEDUCAO_DESPESA)
					vlDespesa -= rs.getFloat("vl_total");
			// Despesas
			pstmt = connect.prepareStatement(
					   "SELECT B.tp_categoria_economica, COUNT(*) AS qt_itens, SUM(vl_conta_categoria) AS vl_total "+
					   "FROM adm_conta_pagar_categoria A "+
					   "JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica) "+
					   "JOIN adm_conta_pagar       C ON (A.cd_conta_pagar = C.cd_conta_pagar) "+
					   "WHERE C.dt_emissao BETWEEN ? AND ? "+
					   "GROUP BY B.tp_categoria_economica");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rs = pstmt.executeQuery();
			while(rs.next())
				if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_RECEITA)
					vlReceita += rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DESPESA)
					vlDespesa += rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DEDUCAO_RECEITA)
					vlReceita -= rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DEDUCAO_DESPESA)
					vlDespesa -= rs.getFloat("vl_total");
			return new float[] {vlReceita, vlDespesa};
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getDespesaReceita: " + e);
			return new float[] {0, 0};
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdCategoriaEconomica Código da categoria economica da qual deseja receber as categorias inferiores
     * @param connect Conexão com o banco de dados
     * @return Array de inteiros com a lista de códigos das categorias inferiores
     */
	private static ArrayList<Integer> getCategoriasInferiores(int cdCategoriaEconomica, Connection connect)	{
		boolean isConnetionNull = connect==null;
		if(isConnetionNull)
			connect = Conexao.conectar();
		ArrayList<Integer> r = new ArrayList<Integer>();
		try {
			ResultSet rs = connect.prepareStatement("SELECT cd_categoria_economica FROM adm_categoria_economica " +
					                                "WHERE cd_categoria_superior = "+cdCategoriaEconomica).executeQuery();
			while(rs.next())	{
				r.add(new Integer(rs.getInt("cd_categoria_economica")));
				r.addAll(getCategoriasInferiores(rs.getInt("cd_categoria_economica"), connect));
			}
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getCategoriasFilhas: " + e);
			return null;
		}
		finally {
			if(isConnetionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver a classificação dos movimentos
     * @param nrMes Mês do qual se deseja ver os movimentos classificados em categorias
     * @param nrAno Ano do qual se deseja ver os movimentos classificados em categorias
     * @param tpCategoriaEconomica Filtra os tipos de categorias que deseja ver
     * @param cdCategoriaReceita Indica a categoria de receita e despesa da qual deseja ver a movimentação
     * @param cdCategoriaDespesa Indica a categoria de despesa e despesa da qual deseja ver a movimentação
     * @param connect Conexão com o banco de dados
     * @return ResultSetMap com as categorias nas quais está classificado o movimento
     */
	@SuppressWarnings("unchecked")
	public static ResultSetMap getMovimentoByCategoria(int cdEmpresa, int nrMesInicial, int nrAnoInicial, int nrMesFinal, int nrAnoFinal,
			int tpCategoriaEconomica, int cdCategoriaReceita, int cdCategoriaDespesa, int nrNivelMaximo, int tpData, int cdConta,
			boolean showDetalhe, int tpDetalhe)
	{
		Connection connect = Conexao.conectar();
		try	{
			boolean lgHistorico   = (tpDetalhe >= 3);
			if(lgHistorico) tpDetalhe -= 3;
			boolean lgNrDocumento = (tpDetalhe > 0);
			//
			String dsData = tpData==0 ? "dt_emissao" : "dt_vencimento";
			ResultSetMap rsm = new ResultSetMap();
			String previousField = "";
			for(int nrAno=nrAnoInicial, nrMes=nrMesInicial; (nrMes+nrAno*12)<=(nrMesFinal+nrAnoFinal*12);)	{
				GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1, 0, 0, 0);
				GregorianCalendar dtFinal   = new GregorianCalendar(nrAno, nrMes, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
				ResultSetMap rsmTemp = getMovimentoByCategoria(cdEmpresa, dtInicial, dtFinal, tpCategoriaEconomica, cdCategoriaReceita, cdCategoriaDespesa, nrNivelMaximo, tpData, cdConta, connect);
				String fieldName 	 = Util.fillNum(nrMes+1, 2)+"_"+Util.fillNum(nrAno, 4);
				while(rsmTemp.next())	{
					if(rsm.locate("cd_categoria_economica", rsmTemp.getInt("cd_categoria_economica")))	{
						rsm.setValueToField("VL_"+fieldName, rsmTemp.getFloat("vl_total_categoria"));
						rsm.setValueToField("QT_"+fieldName, rsmTemp.getInt("qt_itens"));
						rsm.setValueToField("DS_VL_"+fieldName, rsmTemp.getString("ds_vl_total_categoria"));
						float vlPrevious = rsm.getFloat("VL_"+previousField);
						rsm.setValueToField("PR_"+fieldName, new Float(vlPrevious > 0 ? (double)Math.round((rsmTemp.getFloat("vl_total_categoria")-vlPrevious) / vlPrevious * 100) : 0));
						rsm.setValueToField("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(rsm.getFloat("vl_total_categoria")+rsmTemp.getFloat("vl_total_categoria"))+Util.fill("", (rsmTemp.getInt("nr_nivel"))*10, ' ', 'E'));
						rsm.setValueToField("VL_TOTAL_CATEGORIA", rsm.getFloat("vl_total_categoria")+rsmTemp.getFloat("vl_total_categoria"));
						rsm.setValueToField("VL_TOTAL_RECEITA", rsm.getFloat("vl_total_receita")+rsmTemp.getFloat("vl_total_receita"));
						rsm.setValueToField("VL_TOTAL_DESPESA", rsm.getFloat("vl_total_despesa")+rsmTemp.getFloat("vl_total_despesa"));
					}
					else	{
						HashMap<String,Object> reg = rsmTemp.getRegister();
						reg.put("VL_"+fieldName, rsmTemp.getFloat("vl_total_categoria"));
						reg.put("QT_"+fieldName, rsmTemp.getInt("qt_itens"));
						reg.put("DS_VL_"+fieldName, rsmTemp.getString("ds_vl_total_categoria"));
						reg.put("PR_"+fieldName, new Float(0));
						reg.put("VL_TOTAL_CATEGORIA", rsmTemp.getFloat("vl_total_categoria"));
						reg.put("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(rsmTemp.getFloat("vl_total_categoria"))+Util.fill("", (rsmTemp.getInt("nr_nivel"))*10, ' ', 'E'));
						rsm.addRegister(reg);
						if(nrMesInicial==nrMesFinal && nrAnoInicial==nrAnoFinal && showDetalhe)	{
							ResultSetMap rsmDetalhe = getDetalheOfCategoriaPeriodo(rsmTemp.getInt("cd_categoria_economica"), dtInicial, dtFinal, tpData, cdConta, connect);
							rsmDetalhe.beforeFirst();
							while(rsmDetalhe.next())	{
								HashMap<String,Object> regDetalhe = (HashMap<String,Object>)reg.clone();
								regDetalhe.put("CD_CONTA_PAGAR", rsmDetalhe.getInt("cd_conta_pagar"));
								regDetalhe.put("CD_CONTA_RECEBER", rsmDetalhe.getInt("cd_conta_receber"));
								regDetalhe.put("VL_TOTAL_CATEGORIA", null);
								regDetalhe.put("DS_VL_TOTAL_CATEGORIA", "");
								regDetalhe.put("VL_"+fieldName, null);
								regDetalhe.put("QT_"+fieldName, null);
								regDetalhe.put("DS_VL_"+fieldName, null);
								regDetalhe.put("PR_"+fieldName, null);
								regDetalhe.put("QT_ITENS", "");
								regDetalhe.put("LG_DETALHE", "1");
								regDetalhe.put("NR_CATEGORIA_ECONOMICA", regDetalhe.get("NR_CATEGORIA_ECONOMICA")+". Detalhe");
								String nome = (rsmDetalhe.getString("nm_pessoa")!=null?rsmDetalhe.getString("nm_pessoa"):"Sem Favorecido/Sacado ");
								nome = nome.length()>23 ? nome.substring(0,22)+"..." : nome;
								String dsDetalhe= Util.fill("", (rsmTemp.getInt("nr_nivel"))*10+3, ' ', 'E')+nome+" - "+
													Util.formatDateTime(rsmDetalhe.getGregorianCalendar(dsData),"dd/MM/yyyy")+" - "+
													Util.formatNumber(rsmDetalhe.getFloat("vl_pago"));
								if(lgNrDocumento)
									dsDetalhe += ", Doc: "+rsmDetalhe.getString("nr_documento");
								if(lgHistorico)	{
									String temp = rsmDetalhe.getString("ds_historico")!=null ? ", "+rsmDetalhe.getString("ds_historico") : "";
									temp = temp.length()>30 ? temp.substring(0,30)+"..." : temp;
									dsDetalhe += temp;
								}
								regDetalhe.put("DS_CATEGORIA_ECONOMICA", dsDetalhe);
								rsm.addRegister(regDetalhe);
							}
						}
					}
				}
				previousField = fieldName;
				nrMes++;
				if(nrMes>12)	{
					nrMes = 1;
					nrAno++;
				}
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NR_CATEGORIA_ECONOMICA");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver a classificação dos movimentos
     * @param dtInicial Data inicial do período desejado
     * @param dtFinal Data final do período desejado
     * @param tpCategoriaEconomica Filtra os tipos de categorias que deseja ver
     * @param cdCategoriaReceita Indica a categoria de receita e despesa da qual deseja ver a movimentação
     * @param cdCategoriaDespesa Indica a categoria de despesa e despesa da qual deseja ver a movimentação
     * @param nrNivelMaximo Limite do nível máximo até o qual deseja ver detalhado o relatório
     * @param connect Conexão com o banco de dados
     * @return ResultSetMap com as categorias nas quais está classificado o movimento
     **/
	public static ResultSetMap getMovimentoByCategoria(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpCategoriaEconomica, int cdCategoriaReceita, int cdCategoriaDespesa, int nrNivelMaximo, int tpData, int cdConta, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		connect = isConnectionNull ? Conexao.conectar() : connect;
		PreparedStatement pstmt;
		try {
			float[] vlReceitaDespesa = getDespesaReceita(cdEmpresa, dtInicial, dtFinal, connect);
			String categorias = "";
			if(cdCategoriaReceita>0)	{
				categorias = String.valueOf(cdCategoriaReceita);
				ArrayList<Integer> cats = getCategoriasInferiores(cdCategoriaReceita, connect);
				for(int i=0; i<cats.size(); i++)	{
					categorias += ","+cats.get(i).intValue();
				}
			}
			if(cdCategoriaDespesa>0)	{
				categorias += (categorias.equals("")?"":",")+String.valueOf(cdCategoriaDespesa);
				ArrayList<Integer> cats = getCategoriasInferiores(cdCategoriaDespesa, connect);
				for(int i=0; i<cats.size(); i++)	{
					categorias += ","+cats.get(i).intValue();
				}
			}
			// Conta a Receber
			pstmt = connect.prepareStatement(
					   "SELECT C.nr_nivel, C.cd_categoria_superior, C.tp_categoria_economica, "+
					   "       A.cd_categoria_economica, nr_categoria_economica, nm_categoria_economica, "+
					   "       COUNT(*) AS qt_itens, SUM(vl_conta_categoria) AS vl_total_categoria "+
					   "FROM adm_conta_receber_categoria A "+
					   "JOIN adm_conta_receber       B ON (A.cd_conta_receber = B.cd_conta_receber) "+
					   "JOIN adm_categoria_economica C ON (A.cd_categoria_economica = C.cd_categoria_economica) "+
					   "WHERE "+(tpData==0?"B.dt_emissao":"B.dt_vencimento")+" BETWEEN ? AND ? "+
					   (tpCategoriaEconomica>=0 ? " AND C.tp_categoria_economica = "+tpCategoriaEconomica : "")+
					   (!categorias.equals("")  ? " AND C.cd_categoria_economica IN ("+categorias+")": "")+
					   (cdConta>0 ? "  AND B.cd_conta = "+cdConta : "")+
					   " AND B.st_conta NOT IN ("+ContaReceberServices.ST_CANCELADA+","+ContaReceberServices.ST_PERDA+","+
					                              ContaReceberServices.ST_NEGOCIADA+")"+
					   " GROUP BY C.nr_nivel, C.cd_categoria_superior, C.tp_categoria_economica, "+
					   "          A.cd_categoria_economica, C.nr_categoria_economica, C.nm_categoria_economica " +
					   " ORDER BY C.nr_nivel DESC");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmCR = new ResultSetMap(pstmt.executeQuery());
			while(rsmCR.next())	{
				int nrPosition = rsmCR.getPosition();
		        int qtItens    = rsmCR.getInt("qt_itens");
		        float vlTotal  = rsmCR.getFloat("vl_total_categoria");
		        if (rsmCR.getInt("cd_categoria_superior", 0) > 0)	{
		        	int cdCategoriaSuperior = rsmCR.getInt("cd_categoria_superior", 0);
		            HashMap<String,Object> register = new HashMap<String,Object>();
		        	if (!rsmCR.locate("cd_categoria_economica", rsmCR.getObject("cd_categoria_superior"))) {
		        		CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoriaSuperior, connect);
			            register = new HashMap<String,Object>();
			            register.put("CD_CATEGORIA_ECONOMICA", new Integer(categoria.getCdCategoriaEconomica()));
			            register.put("NM_CATEGORIA_ECONOMICA", categoria.getNmCategoriaEconomica());
			            register.put("TP_CATEGORIA_ECONOMICA", new Integer(categoria.getTpCategoriaEconomica()));
			            register.put("CD_CATEGORIA_SUPERIOR", new Integer(categoria.getCdCategoriaSuperior()));
			            register.put("NR_CATEGORIA_ECONOMICA", categoria.getNrCategoriaEconomica());
			            register.put("NR_NIVEL", new Integer(categoria.getNrNivel()));
			            register.put("VL_TOTAL_CATEGORIA", new Double(0));
			            register.put("QT_ITENS", new Integer(0));
			            rsmCR.addRegister(register);
		        	}
		        	else
		        		register = rsmCR.getRegister();
		        	register.put("VL_TOTAL_CATEGORIA", new Double(((Double)register.get("VL_TOTAL_CATEGORIA")).floatValue() + vlTotal));
		        	register.put("QT_ITENS", new Integer(((Integer)register.get("QT_ITENS")).intValue() + qtItens));
		        };
		        rsmCR.goTo(nrPosition);
			}
			// Conta a Pagar
			pstmt = connect.prepareStatement(
					   "SELECT C.nr_nivel, C.cd_categoria_superior, C.tp_categoria_economica, "+
					   "       A.cd_categoria_economica, nr_categoria_economica, nm_categoria_economica, "+
					   "       COUNT(*) AS qt_itens, SUM(vl_conta_categoria) AS vl_total_categoria "+
					   "FROM adm_conta_pagar_categoria A "+
					   "JOIN adm_conta_pagar           B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					   "JOIN adm_categoria_economica   C ON (A.cd_categoria_economica = C.cd_categoria_economica) "+
					   "WHERE "+(tpData==0?"B.dt_emissao":"B.dt_vencimento")+" BETWEEN ? AND ? "+
					   " AND B.st_conta NOT IN ("+ContaPagarServices.ST_CANCELADA+","+ContaPagarServices.ST_NEGOCIADA+")"+
					   (tpCategoriaEconomica>=0 ? " AND C.tp_categoria_economica = "+tpCategoriaEconomica : "")+
					   (!categorias.equals("")  ? " AND C.cd_categoria_economica IN ("+categorias+")": "")+
					   (cdConta>0 ? "  AND B.cd_conta = "+cdConta : "")+
					   " GROUP BY C.nr_nivel, C.cd_categoria_superior, C.tp_categoria_economica, "+
					   "          A.cd_categoria_economica, C.nr_categoria_economica, C.nm_categoria_economica " +
					   " ORDER BY C.nr_nivel DESC");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsmCP = new ResultSetMap(pstmt.executeQuery());
			while(rsmCP.next())	{
				int nrPosition = rsmCP.getPosition();
		        int qtItens    = rsmCP.getInt("qt_itens");
		        float vlTotal  = rsmCP.getFloat("vl_total_categoria");
		        if (rsmCP.getInt("cd_categoria_superior", 0) > 0)	{
		        	int cdCategoriaSuperior = rsmCP.getInt("cd_categoria_superior", 0);
		            HashMap<String,Object> register = new HashMap<String,Object>();
		        	if (!rsmCP.locate("cd_categoria_economica", rsmCP.getObject("cd_categoria_superior"))) {
		        		CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoriaSuperior, connect);
			            register = new HashMap<String,Object>();
			            register.put("CD_CATEGORIA_ECONOMICA", new Integer(categoria.getCdCategoriaEconomica()));
			            register.put("NM_CATEGORIA_ECONOMICA", categoria.getNmCategoriaEconomica());
			            register.put("TP_CATEGORIA_ECONOMICA", new Integer(categoria.getTpCategoriaEconomica()));
			            register.put("CD_CATEGORIA_SUPERIOR", new Integer(categoria.getCdCategoriaSuperior()));
			            register.put("NR_CATEGORIA_ECONOMICA", categoria.getNrCategoriaEconomica());
			            register.put("NR_NIVEL", new Integer(categoria.getNrNivel()));
			            register.put("VL_TOTAL_CATEGORIA", new Double(0));
			            register.put("QT_ITENS", new Integer(0));
			            rsmCP.addRegister(register);
		        	}
		        	else
		        		register = rsmCP.getRegister();
		        	register.put("VL_TOTAL_CATEGORIA", new Double(((Double)register.get("VL_TOTAL_CATEGORIA")).floatValue() + vlTotal));
		        	register.put("QT_ITENS", new Integer(((Integer)register.get("QT_ITENS")).intValue() + qtItens));
		        };
		        rsmCP.goTo(nrPosition);
			}
			ResultSetMap rsmReturn = new ResultSetMap();
			rsmCR.beforeFirst();
			rsmCP.beforeFirst();
			// Receitas
			HashMap<String,Object> regReceita = new HashMap<String,Object>();
			regReceita.put("CD_CATEGORIA_ECONOMICA", -1);
			regReceita.put("NR_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.idRECEITA);
			regReceita.put("TP_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.TP_RECEITA);
			regReceita.put("NM_CATEGORIA_ECONOMICA", "RECEITAS");
			regReceita.put("DS_CATEGORIA_ECONOMICA", "RECEITAS");
			regReceita.put("VL_TOTAL_CATEGORIA", new Float(0));
			regReceita.put("QT_ITENS", new Integer(0));
			regReceita.put("VL_TOTAL_RECEITA", new Float(vlReceitaDespesa[0]));
			regReceita.put("VL_TOTAL_DESPESA", new Float(vlReceitaDespesa[1]));
			// Despesas
			HashMap<String,Object> regDespesa = new HashMap<String,Object>();
			regDespesa.put("CD_CATEGORIA_ECONOMICA", -2);
			regDespesa.put("NR_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.idDESPESA);
			regDespesa.put("TP_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.TP_DESPESA);
			regDespesa.put("NM_CATEGORIA_ECONOMICA", "DESPESAS");
			regDespesa.put("DS_CATEGORIA_ECONOMICA", "DESPESAS");
			regDespesa.put("VL_TOTAL_CATEGORIA", new Float(0));
			regDespesa.put("QT_ITENS", new Integer(0));
			regDespesa.put("VL_TOTAL_RECEITA", new Float(vlReceitaDespesa[0]));
			regDespesa.put("VL_TOTAL_DESPESA", new Float(vlReceitaDespesa[1]));
			// Dedução das Receitas
			HashMap<String,Object> regDedReceita = new HashMap<String,Object>();
			regDedReceita.put("CD_CATEGORIA_ECONOMICA", -3);
			regDedReceita.put("NR_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.idDEDUCAO_RECEITA);
			regDedReceita.put("TP_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.TP_DEDUCAO_RECEITA);
			regDedReceita.put("NM_CATEGORIA_ECONOMICA", "DEDUÇÃO DA RECEITA");
			regDedReceita.put("DS_CATEGORIA_ECONOMICA", "DEDUÇÃO DA RECEITA");
			regDedReceita.put("VL_TOTAL_CATEGORIA", new Float(0));
			regDedReceita.put("QT_ITENS", new Integer(0));
			regDedReceita.put("VL_TOTAL_RECEITA", new Float(vlReceitaDespesa[0]));
			regDedReceita.put("VL_TOTAL_DESPESA", new Float(vlReceitaDespesa[1]));
			// Dedução das Despesas
			HashMap<String,Object> regDedDespesa = new HashMap<String,Object>();
			regDedDespesa.put("CD_CATEGORIA_ECONOMICA", -4);
			regDedDespesa.put("NR_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.idDEDUCAO_DESPESA);
			regDedDespesa.put("TP_CATEGORIA_ECONOMICA", CategoriaEconomicaServices.TP_DEDUCAO_DESPESA);
			regDedDespesa.put("NM_CATEGORIA_ECONOMICA", "DEDUÇÃO DA DESPESA");
			regDedDespesa.put("DS_CATEGORIA_ECONOMICA", "DEDUÇÃO DA DESPESA");
			regDedDespesa.put("VL_TOTAL_CATEGORIA", new Float(0));
			regDedDespesa.put("QT_ITENS", new Integer(0));
			regDedDespesa.put("VL_TOTAL_RECEITA", new Float(vlReceitaDespesa[0]));
			regDedDespesa.put("VL_TOTAL_DESPESA", new Float(vlReceitaDespesa[1]));
			for(int i=0; i<2; i++)	{
				ResultSetMap rsm = (i==0) ? rsmCR : rsmCP;
				while(rsm.next())	{
					if(nrNivelMaximo>0 && rsm.getInt("nr_nivel")>nrNivelMaximo)	{
						continue;
					}
					if(rsm.getInt("CD_CATEGORIA_SUPERIOR")<=0)	{
						if(rsm.getInt("TP_CATEGORIA_ECONOMICA")==CategoriaEconomicaServices.TP_RECEITA)	{
							regReceita.put("VL_TOTAL_CATEGORIA", new Float(((Float)regReceita.get("VL_TOTAL_CATEGORIA")).floatValue()+rsm.getFloat("VL_TOTAL_CATEGORIA")));
							regReceita.put("QT_ITENS", new Integer(((Integer)regReceita.get("QT_ITENS")).intValue()+rsm.getInt("QT_ITENS")));
						}
						if(rsm.getInt("TP_CATEGORIA_ECONOMICA")==CategoriaEconomicaServices.TP_DESPESA)	{
							regDespesa.put("VL_TOTAL_CATEGORIA", new Float(((Float)regDespesa.get("VL_TOTAL_CATEGORIA")).floatValue()+rsm.getFloat("VL_TOTAL_CATEGORIA")));
							regDespesa.put("QT_ITENS", new Integer(((Integer)regDespesa.get("QT_ITENS")).intValue()+rsm.getInt("QT_ITENS")));
						}
						if(rsm.getInt("TP_CATEGORIA_ECONOMICA")==CategoriaEconomicaServices.TP_DEDUCAO_RECEITA)	{
							regDedReceita.put("VL_TOTAL_CATEGORIA", new Float(((Float)regDedReceita.get("VL_TOTAL_CATEGORIA")).floatValue()+rsm.getFloat("VL_TOTAL_CATEGORIA")));
							regDedReceita.put("QT_ITENS", new Integer(((Integer)regDedReceita.get("QT_ITENS")).intValue()+rsm.getInt("QT_ITENS")));
						}
						if(rsm.getInt("TP_CATEGORIA_ECONOMICA")==CategoriaEconomicaServices.TP_DEDUCAO_DESPESA)	{
							regDedDespesa.put("VL_TOTAL_CATEGORIA", new Float(((Float)regDedDespesa.get("VL_TOTAL_CATEGORIA")).floatValue()+rsm.getFloat("VL_TOTAL_CATEGORIA")));
							regDedDespesa.put("QT_ITENS", new Integer(((Integer)regDedDespesa.get("QT_ITENS")).intValue()+rsm.getInt("QT_ITENS")));
						}
					}
					rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", (rsm.getInt("nr_nivel"))*10, ' ', 'E')+rsm.getString("nm_categoria_economica"));
					rsm.setValueToField("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(rsm.getFloat("vl_total_categoria"))+Util.fill("", (rsm.getInt("nr_nivel"))*10, ' ', 'E'));
					rsm.setValueToField("VL_TOTAL_RECEITA", new Float(vlReceitaDespesa[0]));
					rsm.setValueToField("VL_TOTAL_DESPESA", new Float(vlReceitaDespesa[1]));
					rsmReturn.addRegister(rsm.getRegister());
				}
			}
			regReceita.put("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(((Float)regReceita.get("VL_TOTAL_CATEGORIA")).floatValue()));
			regDespesa.put("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(((Float)regDespesa.get("VL_TOTAL_CATEGORIA")).floatValue()));
			regDedReceita.put("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(((Float)regDedReceita.get("VL_TOTAL_CATEGORIA")).floatValue()));
			regDedDespesa.put("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(((Float)regDedDespesa.get("VL_TOTAL_CATEGORIA")).floatValue()));
//			rsmReturn.addRegister(regReceita);
//			rsmReturn.addRegister(regDespesa);
			rsmReturn.addRegister(regDedReceita);
			rsmReturn.addRegister(regDedDespesa);
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NR_CATEGORIA_ECONOMICA");
			rsmReturn.orderBy(orderBy);
			rsmReturn.beforeFirst();
			return rsmReturn;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getMovimentoByCategoria: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @author Álvaro
	 * @param filtros, HashMap contendo nome das contas ( Categorias Econômicas, Provisao, Recbíveis Entrada/Saída, Ativo, Passivo )
	 * 		e o nível de detalhe( Analítico=0, Sintético=1 ) a serem adicionados ao relatório.
	 * @param cdEmpresa
	 * @param nrMesInicial
	 * @param nrAnoInicial
	 * @param nrMesFinal
	 * @param nrAnoFinal
	 * @param tpCategoriaEconomica
	 * @param cdCategoriaReceita
	 * @param cdCategoriaDespesa
	 * @param nrNivelMaximo
	 * @param tpData
	 * @param cdConta
	 * @param showDetalhe
	 * @param tpDetalhe
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public static Result gerarRelatorioMovimentoByCategoria( HashMap filtros, int cdEmpresa, int nrMesInicial, int nrAnoInicial, int nrMesFinal, int nrAnoFinal,
			int tpCategoriaEconomica, int cdCategoriaReceita, int cdCategoriaDespesa, int nrNivelMaximo, int tpData, int cdConta,
			boolean showDetalhe, int tpDetalhe){
		Connection connect = Conexao.conectar();
		try	{
			HashMap<String, Object> params = new HashMap<String, Object>();
			SimpleDateFormat mesAnoFormat = new SimpleDateFormat("MMMM/yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			HashMap<String, Object> register = new HashMap<String, Object>();
			GregorianCalendar dtPeriodoInicial=null, dtPeriodoFinal=null;
			
			if( filtros.get("PERIODO_INICIAL") !=null )
				dtPeriodoInicial = Util.stringToCalendar( (String) filtros.get("PERIODO_INICIAL") ) ;
						
			if( filtros.get("PERIODO_FINAL") !=null )
				dtPeriodoFinal = Util.stringToCalendar( (String) filtros.get("PERIODO_FINAL") ) ;
			
			GregorianCalendar dtInicial = (dtPeriodoInicial !=null)? dtPeriodoInicial : new GregorianCalendar(nrAnoInicial, nrMesInicial, 1, 0, 0, 0);
			GregorianCalendar dtFinal   = (dtPeriodoFinal !=null)? dtPeriodoFinal : new GregorianCalendar(nrAnoFinal, nrMesFinal, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			
			params.put("NM_PERIODO_INICIAL", dateFormat.format( dtInicial.getTime() )  );
			params.put("NM_PERIODO_FINAL", dateFormat.format( dtFinal.getTime() ) );
			StringBuffer strBuffer = new StringBuffer( mesAnoFormat.format( dtInicial.getTime() ) );
			strBuffer.append(" a ");
			strBuffer.append( mesAnoFormat.format( dtFinal.getTime() ) );
			params.put("MES_ANO", strBuffer.toString() );
		
			Result result = new Result(1);
			ResultSetMap rsm = new ResultSetMap();
			if( filtros.get("CATEGORIA_ECONOMICA") != null ){
				Integer nivelDetalheCatEconomica = ( filtros.get("NR_DETALHE_CATEGORIA_ECONOMICA") != null )? Integer.parseInt( (String)filtros.get("NR_DETALHE_CATEGORIA_ECONOMICA") ): null ;
				ResultSetMap rsmCategoriaEconomica = getMovimentoByCategoria( cdEmpresa,  nrMesInicial,  nrAnoInicial,  nrMesFinal,  nrAnoFinal,
													 tpCategoriaEconomica,  cdCategoriaReceita,  cdCategoriaDespesa,
													 nrNivelMaximo,  tpData,  cdConta, showDetalhe,  tpDetalhe);
				ResultSetMap rsmTmpCategoriaEconomica = new ResultSetMap();
				rsmTmpCategoriaEconomica.setLines( rsmCategoriaEconomica.getLines() );
				Double resultadoOperacionalLiquido = 0.0;
				Double resultadoOperacionalBruto = 0.0;
				while( rsmTmpCategoriaEconomica.next() ){
					while( rsmCategoriaEconomica.next() ){
						if(rsmTmpCategoriaEconomica.getInt("CD_CATEGORIA_ECONOMICA") == rsmCategoriaEconomica.getInt("CD_CATEGORIA_ECONOMICA")
							 && rsmCategoriaEconomica.getInt("NR_NIVEL") == 0
							){
							rsmCategoriaEconomica.setValueToField("PORCENTAGEM", "100");
							if( rsmCategoriaEconomica.getInt("TP_CATEGORIA_ECONOMICA") == 0 ){
								resultadoOperacionalBruto += rsmCategoriaEconomica.getFloat("VL_TOTAL_CATEGORIA");
								resultadoOperacionalLiquido += rsmCategoriaEconomica.getFloat("VL_TOTAL_CATEGORIA");
							}
							else if( rsmCategoriaEconomica.getInt("TP_CATEGORIA_ECONOMICA") == 1 ){
								resultadoOperacionalLiquido -= rsmCategoriaEconomica.getFloat("VL_TOTAL_CATEGORIA");
							}
						}
						else if( rsmTmpCategoriaEconomica.getInt("CD_CATEGORIA_ECONOMICA") == rsmCategoriaEconomica.getInt("CD_CATEGORIA_SUPERIOR")){
							Float vlTotal = rsmTmpCategoriaEconomica.getFloat("VL_TOTAL_CATEGORIA");
							Float vlFilho = rsmCategoriaEconomica.getFloat("VL_TOTAL_CATEGORIA");
							Float porcentagem = (vlFilho*100/vlTotal);
							rsmCategoriaEconomica.setValueToField("PORCENTAGEM", porcentagem.toString());
						}
					}
					rsmCategoriaEconomica.goTo(0);
				}
				
				ArrayList<String> crtOrder = new ArrayList<String>();
				crtOrder.add("TP_CATEGORIA_ECONOMICA");
				rsmCategoriaEconomica.orderBy(crtOrder);
				while( rsmCategoriaEconomica.next() ){
					if( rsmCategoriaEconomica.getInt("TP_CATEGORIA_ECONOMICA") != 0 ){
						HashMap<String, Object> regResultadoOperacionalBruto= new HashMap<String, Object>();
						regResultadoOperacionalBruto.put("NR_NIVEL", 0);
						regResultadoOperacionalBruto.put("VL_TOTAL_CATEGORIA", resultadoOperacionalBruto);
						regResultadoOperacionalBruto.put("NM_CATEGORIA_ECONOMICA", "RESULTADO OPERACIONAL BRUTO");
						rsmCategoriaEconomica.getLines().add( rsmCategoriaEconomica.getPointer(), regResultadoOperacionalBruto);
						break;
					}
				}
				
				HashMap<String, Object> regResultadoOperacionalLiquido = new HashMap<String, Object>();
				regResultadoOperacionalLiquido.put("NR_NIVEL", 0);
				regResultadoOperacionalLiquido.put("VL_TOTAL_CATEGORIA", resultadoOperacionalLiquido);
				regResultadoOperacionalLiquido.put("NM_CATEGORIA_ECONOMICA", "RESULTADO OPERACIONAL LIQUÍDO");
				rsmCategoriaEconomica.addRegister(regResultadoOperacionalLiquido);
				
				if( nivelDetalheCatEconomica != null  ){
					rsmCategoriaEconomica.beforeFirst();
					while( rsmCategoriaEconomica.next() ){
						if( rsmCategoriaEconomica.getInt("NR_NIVEL") > nivelDetalheCatEconomica ){
							rsmCategoriaEconomica.deleteRow();
							rsmCategoriaEconomica.previous();
						}
					}
				}
				register.put("CATEGORIA_ECONOMICA", rsmCategoriaEconomica.getLines());
			}
			if( filtros.get("RECEBIVEIS_ENTRADAS") != null ){
				Integer nivelDetalheRecEntradas = ( filtros.get("NR_DETALHE_RECEBIVEIS_ENTRADAS") != null )? Integer.parseInt( (String)filtros.get("NR_DETALHE_RECEBIVEIS_ENTRADAS") ): null ;
				ResultSetMap rsmRecebiveisEntradas = ContaReceberServices.getDemonstrativoEntradasPorPeriodo(cdEmpresa, dtInicial, dtFinal, nivelDetalheRecEntradas, connect);
				register.put("RECEBIVEIS_ENTRADAS", rsmRecebiveisEntradas.getLines());
			}
			if( filtros.get("RECEBIVEIS_SAIDAS") != null ){
				Integer nivelDetalheRecSaidas = ( filtros.get("NR_DETALHE_RECEBIVEIS_SAIDAS") != null )? Integer.parseInt( (String)filtros.get("NR_DETALHE_RECEBIVEIS_SAIDAS") ): null ;
				ResultSetMap rsmRecebiveisSaidas = ContaReceberServices.getDemonstrativoSaidasPorPeriodo(cdEmpresa, dtInicial, dtFinal, nivelDetalheRecSaidas, connect);
				register.put("RECEBIVEIS_SAIDAS", rsmRecebiveisSaidas.getLines());
			}
			if( filtros.get("ATIVOS") != null ){}
			if( filtros.get("PASSIVOS") != null ){}
			rsm.addRegister( register );
			result.addObject("rsm", rsm);
			result.addObject("params", params);
			return result;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.gerarRelatorioMovimentoByCategoria: " + e);
			return null;
		}finally	{
			Conexao.desconectar(connect);
		}
	}
	
}