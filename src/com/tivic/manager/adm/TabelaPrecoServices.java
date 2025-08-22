package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.DefaultProcess;
import com.tivic.manager.util.ProcessManager;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TabelaPrecoServices {

	public static final int GN_TABELA_PRECO = 0;
	public static final int GN_PROMOCAO     = 0;

	public static final int APLIC_REMOCAO_PRECOS_ANT      = 0;
	public static final int APLIC_SOBREPOSICAO_PRECOS_ANT = 1;

	public static final String[] tpAplicacaoRegras = {"Remoção de preços anteriores", "Sobreposição de preços anteriores"};

	public static Result save(TabelaPreco tabelaPreco){
		return save(tabelaPreco, null);
	}

	public static Result save(TabelaPreco tabelaPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaPreco==null)
				return new Result(-1, "Erro ao salvar. TabelaPreco é nulo");

			//Garante que apenas um registro no sistema será o padrão
			if(tabelaPreco.getLgPadrao()==1){
				setPadrao(tabelaPreco.getCdTabelaPreco(), connect);
			}
			
			int retorno;
			if(tabelaPreco.getCdTabelaPreco()==0){
				retorno = TabelaPrecoDAO.insert(tabelaPreco, connect);
				tabelaPreco.setCdTabelaPreco(retorno);
			}
			else {
				retorno = TabelaPrecoDAO.update(tabelaPreco, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELAPRECO", tabelaPreco);
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
	
	public static ResultSetMap getAllProdutoServicos(int cdTabelaPreco, int cdDocumentoEntrada) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cdDocumentoEntrada", Integer.toString(cdDocumentoEntrada), ItemComparator.EQUAL, Types.INTEGER));
		return getAllProdutoServicos(cdTabelaPreco, criterios, null, null);
	}

	public static ResultSetMap getAllProdutoServicos(int cdTabelaPreco) {
		return getAllProdutoServicos(cdTabelaPreco, null, null, null);
	}

	public static ResultSetMap getAllProdutoServicos(int cdTabelaPreco, ArrayList<ItemComparator> criterios, ArrayList<String> fieldsOrder) {
		return getAllProdutoServicos(cdTabelaPreco, criterios, fieldsOrder, null);
	}

	public static ResultSetMap getAllProdutoServicos(int cdTabelaPreco, ArrayList<ItemComparator> criterios,
			ArrayList<String> fieldsOrder, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			int cdDocumentoEntrada = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdDocumentoEntrada")) {
					cdDocumentoEntrada = Integer.parseInt(criterios.get(i).getValue());
					break;
				}
			TabelaPreco tabPreco = TabelaPrecoDAO.get(cdTabelaPreco, connect);
			int cdEmpresa = tabPreco.getCdEmpresa();
			String statement = "SELECT A.*, B.nm_produto_servico, " +
					"B.id_produto_servico, C.id_reduzido, C.cd_unidade_medida, D.sg_unidade_medida, C.vl_ultimo_custo, " +
					"C.nr_ordem, F.nm_grupo, G.nm_grupo AS nm_grupo1, H.nm_grupo AS nm_grupo2 " +
					"FROM adm_produto_servico_preco A, grl_produto_servico B, grl_produto_servico_empresa C " +
					"LEFT OUTER JOIN grl_unidade_medida D ON (C.cd_unidade_medida = D.cd_unidade_medida) " +
					"LEFT OUTER JOIN alm_produto_grupo E ON (C.cd_produto_servico = E.cd_produto_servico AND " +
					"										 E.cd_empresa = " + cdEmpresa + " AND " +
					"										 E.lg_principal = 1) " +
					"LEFT OUTER JOIN alm_grupo F ON (E.cd_grupo = F.cd_grupo) " +
					"LEFT OUTER JOIN alm_grupo G ON (F.cd_grupo_superior = G.cd_grupo) " +
					"LEFT OUTER JOIN alm_grupo H ON (G.cd_grupo_superior = H.cd_grupo) " +
					"LEFT OUTER JOIN grl_produto_servico_parametro I ON (C.cd_produto_servico = I.cd_produto_servico " +
					"										 			   AND I.cd_empresa = " + cdEmpresa + ") " +
					"WHERE A.cd_produto_servico = B.cd_produto_servico " +
					"  AND B.cd_produto_servico = C.cd_produto_servico " +
					"  AND A.dt_termino_validade IS NULL " +
					"  AND A.cd_tabela_preco = " + cdTabelaPreco +
					"  AND C.cd_empresa = " + cdEmpresa +
					"  AND (I.lg_imprime_na_tabela_preco = 1 " + 
					"		 AND (I.lg_produto_uso_consumo = 0 OR I.lg_produto_uso_consumo IS NULL))" +
					(cdDocumentoEntrada>0 ? "  AND B.cd_produto_servico IN (SELECT I.cd_produto_servico " +
					"			   					FROM alm_documento_entrada_item I " +
					"			   					WHERE I.cd_documento_entrada = " + cdDocumentoEntrada + ")" : "") + 
					" ORDER BY B.nm_produto_servico";
			ResultSetMap rsm = Search.find(statement, "", criterios, connect, isConnectionNull);
			if (fieldsOrder!=null && fieldsOrder.size()>0) {
				ArrayList<String> fields = new ArrayList<String>();
				for (int i=0; i<fieldsOrder.size(); i++)
					fields.add(fieldsOrder.get(i));
				rsm.orderBy(fields);
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoServices.getAllProdutoServicos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllProdutoServicosOfRegra(int cdTabelaPreco, int cdRegra) {
		return getAllProdutoServicosOfRegra(cdTabelaPreco, cdRegra, null);
	}

	public static ResultSetMap getAllProdutoServicosOfRegra(int cdTabelaPreco, int cdRegra, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			TabelaPreco tabPreco = TabelaPrecoDAO.get(cdTabelaPreco, connection);
			TabelaPrecoRegra regra = TabelaPrecoRegraDAO.get(cdTabelaPreco, cdRegra, connection);
			int cdEmpresa = tabPreco.getCdEmpresa();
			int cdGrupo = regra.getCdGrupo();
			int cdProdutoServico = regra.getCdProdutoServico();
			int cdTabelaPrecoBase = regra.getCdTabelaPrecoBase();
			int cdFornecedor = regra.getCdFornecedor();

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_produto_servico, A.nm_produto_servico, " +
					"A.id_produto_servico, B.id_reduzido, B.cd_unidade_medida, C.sg_unidade_medida, B.vl_ultimo_custo, " +
					"B.nr_ordem, E.nm_grupo, F.nm_grupo AS nm_grupo1, G.nm_grupo AS nm_grupo2 " +
					"FROM grl_produto_servico A, grl_produto_servico_empresa B " +
					"LEFT OUTER JOIN grl_unidade_medida C ON (B.cd_unidade_medida = C.cd_unidade_medida) " +
					"LEFT OUTER JOIN alm_produto_grupo D ON (B.cd_produto_servico = D.cd_produto_servico AND " +
					"										 D.cd_empresa = ? AND " +
					"										 D.lg_principal = 1) " +
					"LEFT OUTER JOIN alm_grupo E ON (D.cd_grupo = E.cd_grupo) " +
					"LEFT OUTER JOIN alm_grupo F ON (E.cd_grupo_superior = F.cd_grupo) " +
					"LEFT OUTER JOIN alm_grupo G ON (F.cd_grupo_superior = G.cd_grupo) " +
					"WHERE A.cd_produto_servico = B.cd_produto_servico " +
					"  AND B.cd_empresa = ? " +
					"  AND B.st_produto_empresa = 1 " +
					(cdGrupo>0 ? "  AND E.cd_grupo = ? " : "") +
					(cdProdutoServico>0 ? "  AND A.cd_produto_servico = ? " : "") +
					(cdFornecedor>0 ? "  AND ? IN (SELECT H.cd_fornecedor " +
					"			 FROM alm_documento_entrada H, alm_documento_entrada_item I " +
					"			 WHERE H.cd_documento_entrada = I.cd_documento_entrada " +
					"			   AND H.cd_empresa = ? " +
					"			   AND H.cd_empresa = I.cd_empresa " +
					"			   AND H.st_documento_entrada = ? " +
					"			   AND B.cd_produto_servico = I.cd_produto_servico " +
					"			   AND H.dt_documento_entrada = (SELECT MAX(J.dt_documento_entrada) " +
					"											 FROM alm_documento_entrada J, alm_documento_entrada_item L " +
					"											 WHERE J.cd_documento_entrada = L.cd_documento_entrada " +
					"											   AND J.cd_empresa = ? " +
					"											   AND J.cd_empresa = L.cd_empresa " +
					"											   AND J.st_documento_entrada = ? " +
					"											   AND B.cd_produto_servico = L.cd_produto_servico))" : "") +
					(cdTabelaPrecoBase>0 ? "  AND A.cd_produto_servico IN (SELECT M.cd_produto_servico " +
					"								FROM adm_produto_servico_preco M " +
					"								WHERE A.cd_produto_servico = M.cd_produto_servico " +
					"								  AND M.cd_tabela_preco = ? " +
					"								  AND M.dt_termino_validade IS NULL)" : ""));
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdEmpresa);
			int i = 3;
			if (cdGrupo > 0)
				pstmt.setInt(i++, cdGrupo);
			if (cdProdutoServico > 0)
				pstmt.setInt(i++, cdProdutoServico);
			if (cdFornecedor > 0) {
				pstmt.setInt(i++, cdFornecedor);
				pstmt.setInt(i++, cdEmpresa);
				pstmt.setInt(i++, DocumentoEntradaServices.ST_LIBERADO);
				pstmt.setInt(i++, cdEmpresa);
				pstmt.setInt(i++, DocumentoEntradaServices.ST_LIBERADO);
			}
			if (cdTabelaPrecoBase > 0)
				pstmt.setInt(i++, cdTabelaPrecoBase);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nm_grupo2");
			fields.add("nm_grupo1");
			fields.add("nm_grupo");
			fields.add("nr_ordem");
			fields.add("nm_produto_servico");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoServices.getAllProdutoServicosOfRegra: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllOfEmpresa(int cdEmpresa) {
		return getAllOfEmpresa(cdEmpresa, null);
	}

	public static ResultSetMap getAllOfEmpresa(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					"SELECT * FROM adm_tabela_preco A " +
					"WHERE A.cd_empresa = "+cdEmpresa).executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoServices.getAllOfEmpresa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(int cdTabelaPreco, int cdEmpresa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_tabela_preco", "" + cdTabelaPreco, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
		return TabelaPrecoDAO.find(criterios);
	}

	public static ResultSetMap getAllRegras(int cdTabelaPreco) {
		return getAllRegras(cdTabelaPreco, null);
	}

	public static ResultSetMap getAllRegras(int cdTabelaPreco, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_fornecedor, " +
					 "C.nm_produto_servico, F.nm_grupo AS nm_grupo_superior, F.cd_grupo AS cd_grupo_superior, " +
					 "C.id_produto_servico, D.nm_grupo, " +
					 "E.nm_tabela_preco AS nm_tabela_preco_base, G.id_reduzido " +
					 "FROM adm_tabela_preco_regra A " +
					 "LEFT OUTER JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
					 "LEFT OUTER JOIN alm_grupo D ON (A.cd_grupo = D.cd_grupo) " +
					 "LEFT OUTER JOIN alm_grupo F ON (D.cd_grupo_superior = F.cd_grupo) " +
					 "LEFT OUTER JOIN adm_tabela_preco H ON (A.cd_tabela_preco = H.cd_tabela_preco) " +
					 "LEFT OUTER JOIN grl_produto_servico_empresa G ON (C.cd_produto_servico = G.cd_produto_servico AND G.cd_empresa = H.cd_tabela_preco) " +
					 "LEFT OUTER JOIN adm_tabela_preco E ON (A.cd_tabela_preco_base = E.cd_tabela_preco) " +
					 "WHERE A.cd_tabela_preco = ?");
			pstmt.setInt(1, cdTabelaPreco);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> criterios = new ArrayList<String>();
			criterios.add("nr_prioridade");
			rsm.orderBy(criterios);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoServices.getAllProdutosPreco: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return TabelaPrecoDAO.find(criterios, connection);
	}

	public static int getCountOfRegras(int cdTabelaPreco) {
		return getCountOfRegras(cdTabelaPreco, null);
	}

	public static int getCountOfRegras(int cdTabelaPreco, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM adm_tabela_preco_regra A " +
					"WHERE A.cd_tabela_preco = ?");
			pstmt.setInt(1, cdTabelaPreco);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int processarRegras(int cdTabelaPreco) {
		return processarRegras(cdTabelaPreco, null);
	}

	public static int processarRegras(int cdTabelaPreco, Connection connection) {

		class DefaultProcessTabPreco extends DefaultProcess {

			private int cdTabelaPreco;
			private Connection connection = null;

			public DefaultProcessTabPreco(int cdTabelaPreco, int countRegras, Connection connection) {
				super("", 0, (float)countRegras+1, 0);
				setConnection(connection);
				setCdTabelaPreco(cdTabelaPreco);
			}

			public int getCdTabelaPreco() {
				return cdTabelaPreco;
			}

			public void setCdTabelaPreco(int cdTabelaPreco) {
				this.cdTabelaPreco = cdTabelaPreco;
			}

			public Connection getConnection() {
				return connection;
			}

			public void setConnection(Connection connection) {
				this.connection = connection;
			}

			public void run() {
				boolean isConnectionNull = connection==null;
				try {
					connection = getConnection()==null ? Conexao.conectar() : getConnection();
					connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
					PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
							"FROM adm_tabela_preco_regra A " +
							"WHERE A.cd_tabela_preco = ? " +
							"ORDER BY A.nr_prioridade");
					pstmt.setInt(1, getCdTabelaPreco());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						if (aplicarRegra(cdTabelaPreco, rs.getInt("cd_regra"), connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							this.progress = this.maxProgress;
							return;
						}
						this.progress++;
					}

					/*if (isConnectionNull)
						connection.commit();*/
					this.progress = this.getMaxProgress();
				}
				catch(Exception e) {
					e.printStackTrace(System.out);
					this.progress = this.getMaxProgress();
				}
				finally {
					if (isConnectionNull)
						Conexao.desconectar(connection);
				}
			}

		}

		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) " +
					"FROM adm_tabela_preco_regra " +
					"WHERE cd_tabela_preco = ?");
			pstmt.setInt(1, cdTabelaPreco);
			ResultSet rs = pstmt.executeQuery();
			int countRegras = rs.next() ? rs.getInt(1) : 0;

			DefaultProcessTabPreco df = new DefaultProcessTabPreco(cdTabelaPreco, countRegras, Conexao.conectar());
			ProcessManager.registerProcess(df);
			ProcessManager.startProcess(df);

			return df.getCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result aplicarRegras(int cdTabelaPreco, int[] cdsRegras, int[] cdsProdutosServicos) {
		return aplicarRegras(cdTabelaPreco, cdsRegras, cdsProdutosServicos, null);
	}

	public static Result aplicarRegras(int cdTabelaPreco, int[] cdsRegras, int[] cdsProdutosServicos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ResultSetMap rsmPrecos = new ResultSetMap();

			for (int i=0; cdsRegras!=null && i<cdsRegras.length; i++) {
				Result resultApply = aplicarRegra(cdTabelaPreco, cdsRegras[i], cdsProdutosServicos, connection);
				if (resultApply.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return resultApply;
				}

				ResultSetMap rsmPrecosRegra = (ResultSetMap)resultApply.getObjects().get("rsmPrecos");
				while (rsmPrecosRegra.next()) {
					if (rsmPrecos.locate("cd_produto_servico", rsmPrecosRegra.getInt("cd_produto_servico"), false)) {
						rsmPrecos.getRegister().put("VL_PRECO", rsmPrecosRegra.getFloat("VL_PRECO"));
					}
					else
						rsmPrecos.addRegister(rsmPrecosRegra.getRegister());
				}
			}

			if (isConnectionNull)
				connection.commit();

			rsmPrecos.beforeFirst();
			return new Result(1, "Regras aplicadas com sucesso!", "rsmPrecos", rsmPrecos);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar aplicar regras de preço!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int aplicarRegra(int cdTabelaPreco, int cdRegra, Connection connection) {
		return aplicarRegra(cdTabelaPreco, cdRegra, null, connection).getCode();
	}

	public static Result aplicarRegra(int cdTabelaPreco, int cdRegra, int[] cdsProdutosServicos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			ResultSetMap rsmPrecos = new ResultSetMap();
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			TabelaPreco tabPreco = TabelaPrecoDAO.get(cdTabelaPreco, connection);
			TabelaPrecoRegra regra = TabelaPrecoRegraDAO.get(cdTabelaPreco, cdRegra, connection);
			int cdEmpresa = tabPreco.getCdEmpresa();
			int cdGrupo = regra.getCdGrupo();
			int cdProdutoServico = regra.getCdProdutoServico();
			int cdFornecedor = regra.getCdFornecedor();
			int cdTabelaPrecoBase = regra.getCdTabelaPrecoBase();
			int tpValorBase = regra.getTpValorBase();
			float prMargemLucro = regra.getPrMargemLucro();

			if (!(cdGrupo==0 && cdProdutoServico==0 && cdFornecedor==0 && cdTabelaPrecoBase==0) && prMargemLucro>0) {
				String sql = "SELECT A.cd_produto_servico, A.vl_ultimo_custo, " + (cdTabelaPrecoBase>0 ? " H.vl_preco, " : "") +
							 "C.nm_produto_servico, A.vl_preco_medio, A.vl_custo_medio " +
							 "FROM grl_produto_servico_empresa A, " + (cdTabelaPrecoBase>0 ? "adm_produto_servico_preco H, " : "") +
							 "	   grl_produto_servico C " +
							 "WHERE A.cd_empresa = ? " +
							 "  AND A.cd_produto_servico = C.cd_produto_servico " +
							 (cdProdutoServico>0 ? "  AND A.cd_produto_servico = ? " : "")+
							 (cdGrupo>0 ? "  AND A.cd_produto_servico IN (SELECT B.cd_produto_servico " +
							 "								FROM alm_produto_grupo B " +
							 "								WHERE B.cd_empresa = ? " +
							 "								  AND B.lg_principal = 1 " +
							 "								  AND B.cd_grupo = ?)" : "") +
							 (cdFornecedor>0 ? "  AND ? IN (SELECT D.cd_fornecedor " +
							 "			 FROM alm_documento_entrada D, alm_documento_entrada_item E " +
							 "			 WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
							 "			   AND D.cd_empresa = ? " +
							 " 			   AND (D.tp_entrada = ? OR D.tp_entrada = ?) " +
							 "			   AND D.cd_empresa = E.cd_empresa " +
							 "			   AND D.st_documento_entrada = ? " +
							 "			   AND C.cd_produto_servico = E.cd_produto_servico " +
							 "			   AND D.dt_documento_entrada = (SELECT MAX(F.dt_documento_entrada) " +
							 "											 FROM alm_documento_entrada F, alm_documento_entrada_item G " +
							 "											 WHERE F.cd_documento_entrada = G.cd_documento_entrada " +
							 "											   AND F.cd_empresa = ? " +
							 "											   AND (F.tp_entrada = ? OR F.tp_entrada = ?) " +
							 "											   AND F.cd_empresa = G.cd_empresa " +
							 "											   AND F.st_documento_entrada = ? " +
							 "											   AND C.cd_produto_servico = G.cd_produto_servico))" : "") +
							 (cdTabelaPrecoBase>0 ? "  AND A.cd_produto_servico = H.cd_produto_servico " +
							 "  AND H.cd_tabela_preco = ? " +
							 "  AND NOT H.dt_termino_validade IS NULL" : "");
				for (int i=0; cdsProdutosServicos!=null && i<cdsProdutosServicos.length; i++) {
					sql += (i==0 ? " AND (" : "") + (i>0 ? " OR " : "") + "A.cd_produto_servico = " + cdsProdutosServicos[i] + (i==cdsProdutosServicos.length-1 ? ")" : "");
				}
				PreparedStatement pstmt = connection.prepareStatement(sql);
				int i = 1;
				pstmt.setInt(i++, cdEmpresa);
				if (cdProdutoServico > 0)
					pstmt.setInt(i++, cdProdutoServico);
				if (cdGrupo > 0) {
					pstmt.setInt(i++, cdEmpresa);
					pstmt.setInt(i++, cdGrupo);
				}
				if (cdFornecedor > 0) {
					pstmt.setInt(i++, cdFornecedor);
					pstmt.setInt(i++, cdEmpresa);
					pstmt.setInt(i++, DocumentoEntradaServices.ENT_COMPRA);
					pstmt.setInt(i++, DocumentoEntradaServices.ENT_CONSIGNACAO);
					pstmt.setInt(i++, DocumentoEntradaServices.ST_LIBERADO);
					pstmt.setInt(i++, cdEmpresa);
					pstmt.setInt(i++, DocumentoEntradaServices.ENT_COMPRA);
					pstmt.setInt(i++, DocumentoEntradaServices.ENT_CONSIGNACAO);
					pstmt.setInt(i++, DocumentoEntradaServices.ST_LIBERADO);
				}
				if (cdTabelaPrecoBase>0)
					pstmt.setInt(i++, cdTabelaPrecoBase);
				ResultSet rsItens = pstmt.executeQuery();
				while (rsItens!=null && rsItens.next()) {
					int cdProdutoServicoTemp = rsItens.getInt("CD_PRODUTO_SERVICO");

					float vlPreco = (tpValorBase==TabelaPrecoRegraServices.TP_BASE_TABELA_PRECO ? rsItens.getFloat("vl_preco") :
						tpValorBase==TabelaPrecoRegraServices.TP_BASE_ULTIMO_CUSTO ? rsItens.getFloat("vl_ultimo_custo") :
							rsItens.getFloat("vl_preco_medio")) * (1 + (prMargemLucro/(float)100));

					pstmt = connection.prepareStatement("SELECT vl_preco " +
							"FROM adm_produto_servico_preco " +
							"WHERE cd_tabela_preco = ? " +
							"  AND dt_termino_validade IS NULL " +
							"  AND cd_produto_servico = ?");
					pstmt.setInt(1, cdTabelaPreco);
					pstmt.setInt(2, cdProdutoServicoTemp);
					ResultSet rs = pstmt.executeQuery();
					float vlPrecoOld = rs.next() ? rs.getFloat("vl_preco") : 0;

					if (tabPreco.getTpAplicacaoRegras() == APLIC_REMOCAO_PRECOS_ANT) {
						pstmt = connection.prepareStatement("UPDATE adm_produto_servico_preco " +
								"SET dt_termino_validade = ? " +
								"WHERE cd_tabela_preco = ? " +
								"  AND dt_termino_validade IS NULL " +
								"  AND cd_produto_servico = ?");
						pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar()));
						pstmt.setInt(2, cdTabelaPreco);
						pstmt.setInt(3, cdProdutoServicoTemp);
						pstmt.execute();
					}

					if (vlPreco > 0) {
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("CD_PRODUTO_SERVICO", cdProdutoServicoTemp);
						register.put("VL_PRECO", vlPreco);
						register.put("VL_PRECO_OLD", vlPrecoOld);
						rsmPrecos.addRegister(register);

						if (ProdutoServicoPrecoServices.setPrecoProdutoServico(cdProdutoServicoTemp, cdTabelaPreco, vlPreco, regra.getPrDesconto(), false, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao tentar atribuir preço ao produto! [cdProdutoServico:"+cdProdutoServicoTemp+
									              ",cdTabelaPreco:"+cdTabelaPreco+",vlPreco:"+vlPreco+",prDesconto:"+regra.getPrDesconto()+"]");
						}
					}
				}

				pstmt.close();

				if (isConnectionNull)
					connection.commit();
			}

			rsmPrecos.beforeFirst();
			Result result = new Result(1);
			result.addObject("rsmPrecos", rsmPrecos);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar aplicar regras de preço!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * Metodo que busca o registro de tabela de preco padrão do sistema
	 * @since 15/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static TabelaPreco getPadrao(){
		return getPadrao(null);
	}
	
	public static TabelaPreco getPadrao(Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = TabelaPrecoDAO.find(criterios, connection);
			while(rsm.next()){
				return TabelaPrecoDAO.get(rsm.getInt("cd_tabela_preco"), connection);
			}
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Metodo que vai garantir que apenas um registro de tabela de preco do sistema será a padrão
	 * @since 15/08/2014
	 * @author Gabriel
	 * @param cdTabelaPreco
	 */
	public static void setPadrao(int cdTabelaPreco){
		setPadrao(cdTabelaPreco, null);
	}
	
	public static void setPadrao(int cdTabelaPreco, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = TabelaPrecoDAO.find(criterios, connection);
			while(rsm.next()){
				TabelaPreco tabelaPreco = TabelaPrecoDAO.get(rsm.getInt("cd_tabela_preco"), connection);
				tabelaPreco.setLgPadrao(0);
				if(TabelaPrecoDAO.update(tabelaPreco, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
				}
			}
			
			TabelaPreco tabelaPreco = TabelaPrecoDAO.get(cdTabelaPreco, connection);
			tabelaPreco.setLgPadrao(1);
			if(TabelaPrecoDAO.update(tabelaPreco, connection) < 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
			}
			
			
			if(isConnectionNull)
				connection.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
