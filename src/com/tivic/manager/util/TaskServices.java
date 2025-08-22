package com.tivic.manager.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarArquivo;
import com.tivic.manager.adm.ContaPagarArquivoDAO;
import com.tivic.manager.adm.ContaPagarDAO;
import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AfericaoCatraca;
import com.tivic.manager.mob.AfericaoCatracaServices;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.Cartao;
import com.tivic.manager.mob.CartaoDAO;
import com.tivic.manager.mob.CartaoServices;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.ConcessaoVeiculo;
import com.tivic.manager.mob.ConcessaoVeiculoDAO;
import com.tivic.manager.mob.ConcessaoVeiculoServices;
import com.tivic.manager.mob.Lacre;
import com.tivic.manager.mob.LacreCatraca;
import com.tivic.manager.mob.LacreCatracaServices;
import com.tivic.manager.mob.LacreDAO;
import com.tivic.manager.mob.LacreServices;
import com.tivic.manager.mob.PlanoVistoriaItem;
import com.tivic.manager.mob.PlanoVistoriaItemDAO;
import com.tivic.manager.mob.PlanoVistoriaItemServices;
import com.tivic.manager.mob.PlanoVistoriaServices;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.seg.UsuarioServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TaskServices {
	
	/**
	 * @author Alvaro
	 *  @since  21/01/2015
	 *  Task para migrar a estrutura da tabela adm_conta_(PAGAR/RECEBER)_categoria
	 *  que passou a possuir um campo único como chave primária
	 */
	public static Result alterContaCategoriaPK() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			int nextValueContaPagar = new ResultSetMap( connect.prepareStatement("SELECT * FROM GRL_GENERATOR  WHERE nm_generator = 'adm_conta_pagar_categoria'").executeQuery() ).getTotal();
			int nextValueContaReceber = new ResultSetMap( connect.prepareStatement("SELECT * FROM GRL_GENERATOR  WHERE nm_generator = 'adm_conta_receber_categoria'").executeQuery()).getTotal();
			if( nextValueContaPagar == 0 && nextValueContaReceber == 0 ){
					nextValueContaPagar = 1;
					nextValueContaReceber = 1;
					System.out.println("Criando novos campos de chave primária...");
					if( Util.getCurrentDbName() == "Postgresql" ){
						connect.prepareStatement("ALTER TABLE ADM_CONTA_PAGAR_CATEGORIA ADD cd_conta_pagar_categoria INTEGER;").execute();
						connect.prepareStatement("ALTER TABLE ADM_CONTA_RECEBER_CATEGORIA ADD cd_conta_receber_categoria INTEGER;").execute();
					}
					ResultSetMap rsmContasPagarCategoria = Search.find("SELECT * FROM adm_conta_pagar_categoria", "", null, connect, false);
					ResultSetMap rsmContasReceberCategoria = Search.find("SELECT * FROM adm_conta_receber_categoria", "", null, connect, false);
					rsmContasPagarCategoria.beforeFirst();
					rsmContasReceberCategoria.beforeFirst();
					System.out.println("Populando novos campos PK com valores incrementais...");
					Statement stmt = connect.createStatement();
					while ( rsmContasPagarCategoria.next() ) {
						stmt.addBatch("UPDATE adm_conta_pagar_categoria SET cd_conta_pagar_categoria = "+nextValueContaPagar+
								" WHERE cd_conta_pagar = "+rsmContasPagarCategoria.getInt("CD_CONTA_PAGAR")+
								" AND cd_categoria_economica = "+rsmContasPagarCategoria.getInt("CD_CATEGORIA_ECONOMICA"));
						nextValueContaPagar++;
					}
					while ( rsmContasReceberCategoria.next() ) {
						stmt.addBatch("UPDATE adm_conta_receber_categoria SET cd_conta_receber_categoria = "+nextValueContaReceber+
								" WHERE cd_conta_receber = "+rsmContasReceberCategoria.getInt("CD_CONTA_RECEBER")+
								" AND cd_categoria_economica = "+rsmContasReceberCategoria.getInt("CD_CATEGORIA_ECONOMICA"));
						nextValueContaReceber++;
					}
					stmt.executeBatch();
					System.out.println("Atualizando valores no GRL_GENERATOR...");
					connect.prepareStatement("INSERT INTO GRL_GENERATOR (nm_generator, cd_generator) VALUES ( 'adm_conta_pagar_categoria', "+nextValueContaPagar+" )").execute();
					connect.prepareStatement("INSERT INTO GRL_GENERATOR (nm_generator, cd_generator) VALUES ( 'adm_conta_receber_categoria', "+nextValueContaReceber+" )").execute();
					
					if( Util.getCurrentDbName() == "Postgresql" ){
						System.out.println("Alterando Chaves primárias...");
						connect.prepareStatement("ALTER TABLE ADM_CONTA_PAGAR_CATEGORIA DROP CONSTRAINT adm_conta_pagar_categoria_pkey;").execute();
						connect.prepareStatement("ALTER TABLE ADM_CONTA_RECEBER_CATEGORIA DROP CONSTRAINT adm_conta_receber_categoria_pkey;").execute();
						connect.prepareStatement("ALTER TABLE ADM_CONTA_PAGAR_CATEGORIA  ADD CONSTRAINT adm_conta_pagar_categoria_pkey PRIMARY KEY (cd_conta_pagar_categoria);").execute();
						connect.prepareStatement("ALTER TABLE ADM_CONTA_RECEBER_CATEGORIA  ADD CONSTRAINT adm_conta_receber_categoria_pkey PRIMARY KEY (cd_conta_receber_categoria);").execute();
					}
					connect.commit();
					System.out.println("Task concluida.");
			}
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.alterContaCategoriaPK: " +  e);
			return new Result(-1,"Erro ao alterar as chaves primárias em adm_conta_(pagar/receber)_categoria");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 *  @author Alvaro
	 *  @since  27/03/2015
	 *  Cria número de documento para parcelas de contas(pagar/receber) que não possuem
	 */
	public static Result criarNumeroDocumentoEmParcelas() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			ContaPagar contaPagar;
			ContaReceber contaReceber;
			Statement stmt = connect.createStatement();
			int nrParcela = 1;
			String nrDocumento = "";
			
			
			//Processando contas a pagar
			ResultSetMap rsmContasPagar = new ResultSetMap( connect.prepareStatement(
					"SELECT * FROM adm_conta_pagar  "+
					"WHERE  cd_conta_origem IS NOT NULL "+
					"AND nr_documento is null "+
					"ORDER BY cd_conta_origem").executeQuery() );
			rsmContasPagar.beforeFirst();
			if( rsmContasPagar.next() ){
				int cdContaOrigemAtual = rsmContasPagar.getInt("CD_CONTA_ORIGEM");
				contaPagar = ContaPagarDAO.get( cdContaOrigemAtual );
				rsmContasPagar.beforeFirst();
				System.out.println("Criando número de documento para contas a Pagar.");
				while( rsmContasPagar.next() ){
					if( cdContaOrigemAtual != rsmContasPagar.getInt("CD_CONTA_ORIGEM") ){
						cdContaOrigemAtual = rsmContasPagar.getInt("CD_CONTA_ORIGEM");
						contaPagar = ContaPagarDAO.get(cdContaOrigemAtual);
						nrParcela = 1;
					}
					if( contaPagar.getNrDocumento()!=null ){
						nrDocumento = contaPagar.getNrDocumento()+"-"+(new DecimalFormat("000").format(nrParcela));
						stmt.addBatch("UPDATE adm_conta_pagar set nr_documento = '"+nrDocumento+"' "+
									  "WHERE cd_conta_pagar = "+rsmContasPagar.getString("CD_CONTA_PAGAR"));
						nrParcela++;
					}
				}
			}
			
			
			//Processando contas a receber
			ResultSetMap rsmContasReceber = new ResultSetMap( connect.prepareStatement(
					"SELECT * FROM adm_conta_receber  "+
					"WHERE  cd_conta_origem IS NOT NULL "+
					"AND nr_documento is null "+
					"ORDER BY cd_conta_origem").executeQuery() );
			rsmContasReceber.beforeFirst();
			if( rsmContasReceber.next() ){
				int cdContaOrigemAtual = rsmContasReceber.getInt("CD_CONTA_ORIGEM");
				contaReceber = ContaReceberDAO.get( cdContaOrigemAtual );
				rsmContasReceber.beforeFirst();
				System.out.println("Criando número de documento para contas a Receber.");
				while( rsmContasReceber.next() ){
					if( cdContaOrigemAtual != rsmContasReceber.getInt("CD_CONTA_ORIGEM") ){
						cdContaOrigemAtual = rsmContasReceber.getInt("CD_CONTA_ORIGEM");
						contaReceber = ContaReceberDAO.get(cdContaOrigemAtual);
						nrParcela = 1;
					}
					if( contaReceber.getNrDocumento()!=null ){
						nrDocumento = contaReceber.getNrDocumento()+"-"+(new DecimalFormat("000").format(nrParcela));
						stmt.addBatch("UPDATE adm_conta_receber set nr_documento = '"+nrDocumento+"' "+
									  "WHERE cd_conta_receber = "+rsmContasReceber.getString("CD_CONTA_RECEBER"));
						nrParcela++;
					}
				}
			}
			stmt.executeBatch();
			connect.commit();
			System.out.println("Task concluida.");
			
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.alterContaCategoriaPK: " +  e);
			return new Result(-1,"Erro ao alterar as chaves primárias em adm_conta_(pagar/receber)_categoria");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 *  @author Alvaro
	 *  @since  20/04/2015
	 *  Migra os arquivos de contas a pagar para adm_conta_pagar_arquivo
	 */
	public static Result migrarArquivoContaPagar() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			ResultSetMap rsmContasPagar = new ResultSetMap( connect.prepareStatement(
					"SELECT * FROM adm_conta_pagar  "+
					"WHERE  cd_arquivo IS NOT NULL").executeQuery() );
			rsmContasPagar.beforeFirst();
			System.out.println("Migrando arquivos para adm_conta_pagar_arquivo.");
			while( rsmContasPagar.next() ){
				ContaPagarArquivo contaPagarArquivo = new ContaPagarArquivo();
				contaPagarArquivo.setCdArquivo( rsmContasPagar.getInt("CD_ARQUIVO") );
				contaPagarArquivo.setCdContaPagar( rsmContasPagar.getInt("CD_CONTA_PAGAR") );
				ContaPagarArquivoDAO.insert(contaPagarArquivo, connect);
			}
			connect.commit();
			System.out.println("Task concluida.");
			
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.migrarArquivoContaPagar: " +  e);
			return new Result(-1,"Erro ao migrar arquivos para adm_conta_pagar_arquivo");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/**
	 *  @author Alvaro
	 *  @since  05/06/2015
	 *  Atualiza as senhas dos usuários criptografando-as
	 */
	public static Result updateSenhaToHash() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			ResultSetMap rsmUsuario = new ResultSetMap( connect.prepareStatement(
					"SELECT * FROM seg_usuario  ").executeQuery() );
			rsmUsuario.beforeFirst();
			while( rsmUsuario.next() ){
				PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario SET nm_senha = ? " +
				"WHERE cd_usuario = ?");
				pstmt.setString(1, UsuarioServices.getPasswordHash(rsmUsuario.getString("NM_SENHA")));
				pstmt.setInt(2, rsmUsuario.getInt("CD_USUARIO"));
				pstmt.executeUpdate();
				
			}
			connect.commit();
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.updateSenhatoHash: " +  e);
			return new Result(-1,"Erro ao criptografar senha dos usuários");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @author Alvaro
	 *  @since  21/01/2015
	 *  Task para corrigir a ordenação das fichas de vistoria, de modo q um grupo de itens tenha o mesmo número de ordem
	 */
	public static Result corrigirOrdenacaoFichaVistoria() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			ResultSetMap rsmFichaVistoria = PlanoVistoriaServices.getAll();
			ResultSetMap rsmVistoriaItem = new ResultSetMap();
			rsmFichaVistoria.beforeFirst();
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("CD_VISTORIA_ITEM_GRUPO");
			while( rsmFichaVistoria.next() ){
				rsmVistoriaItem = PlanoVistoriaItemServices.getAllByPlanoVistoria( rsmFichaVistoria.getInt("CD_PLANO_VISTORIA") );
				int cdGrupoAtual = 0;
				rsmVistoriaItem.orderBy(orderBy);
				rsmVistoriaItem.beforeFirst();
				int nrOrdemGrupo = 0;
				int nrOrdemItem = 1;
				while( rsmVistoriaItem.next() ){
					if( cdGrupoAtual == rsmVistoriaItem.getInt("CD_VISTORIA_ITEM_GRUPO") ){
						nrOrdemGrupo++;
					}
					PlanoVistoriaItem item = PlanoVistoriaItemDAO.get(rsmVistoriaItem.getInt("CD_PLANO_VISTORIA"), rsmVistoriaItem.getInt("CD_VISTORIA_ITEM"));
					item.setNrOrdemGrupo(nrOrdemGrupo);
					item.setNrOrdemItem(nrOrdemItem);
					int retorno = PlanoVistoriaItemDAO.update(item, connect);
					if( retorno <= 0 ){
						Conexao.rollback(connect);
						return new Result(-1,"Erro ao corrigir ordem da ficha de vistoria");
					}
					nrOrdemItem++;
				}
				
			}
			connect.commit();
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.alterContaCategoriaPK: " +  e);
			return new Result(-1,"Erro ao corrigir ordem da ficha de vistoria");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @author Alvaro
	 *  @since  01/04/2016
	 *  Task para alterar o dado de cd_responsável para cd_agente, 
	 *  ou para cd_usuario, quando a aferição a ser corrigida foi lançada pelo módulo web
	 *  
	 *  @TODO incluir esta DDL ao terminar o método
	 *  
	 *  
	   	<release id="alvaro20160401" date="01/04/2016" autor="Alvaro" description="Incluindo agente responsável pela aferição, e altrando campo responsável(antigo) para usuário">
	   		<DDL>ALTER TABLE MOB_AFERICAO_CATRACA ADD COLUMN cd_agente INTEGER;</DDL>
	   		<DDL>
	   			ALTER TABLE MOB_AFERICAO_CATRACA
					ADD  FOREIGN KEY (cd_agente) 
					REFERENCES MOB_AGENTE(cd_agente);
	   		</DDL>
	   		<DDL>ALTER TABLE MOB_AFERICAO_CATRACA ADD COLUMN cd_usuario INTEGER;</DDL>
	   		<DDL>
	   			ALTER TABLE MOB_AFERICAO_CATRACA
					ADD  FOREIGN KEY (cd_usuario) 
					REFERENCES SEG_USUARIO(cd_usuario);
	   		</DDL>
	   		<TASK>
			   <CLASSNAME>com.tivic.manager.util.TaskServices</CLASSNAME>
			   <METHOD>alterarResponsavelAfericaoCatraca()</METHOD>
		  	</TASK>
	   	</release>
	 *  
	 */
	public static Result alterarResponsavelAfericaoCatraca() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			/**
			 * Aferições efetuadas em 31/01/2016
			 *  para cidade verde 
			 * 	no nome de Ronilson Sousa Matos ??? cd_agente = 103( Banco local )
			 * 
			 * 
			 *  para viação vitória
			 * 	no nome de Adriano Silva Porto ??? cd_agente = 200( Banco local )
			 */
			ResultSetMap rsmAfericoes = AfericaoCatracaServices.find(new ArrayList<ItemComparator>());
			rsmAfericoes.beforeFirst();
			while (rsmAfericoes.next()) {
				
				
				if( Util.compareDates( rsmAfericoes.getGregorianCalendar("DT_AFERICAO"), Util.convStringToCalendar("15/03/2016") ) < 0 ){
					
					//AfericaoCatraca leitura = AfericaoCatracaDAO.get(rsmAfericoes.getInt("CD_AFERICAO_CATRACA"));
					
					//?
				}
				
			}
			
			
			
			connect.commit();
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.alterContaCategoriaPK: " +  e);
			return new Result(-1,"Erro ao corrigir ordem da ficha de vistoria");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @author alvar
	 * @since 01/06/2016
	 * @return
	 */
	public static Result geracaoLacreCatraca() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			/**
			 *  Criação de Lacres fakes para vinculação aos veículos atualmente em operação
			 */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("D.TP_CONCESSAO", String.valueOf(ConcessaoServices.TP_COLETIVO_URBANO), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmConcessaoVeiculo = ConcessaoVeiculoServices.find(criterios);
			
			rsmConcessaoVeiculo.beforeFirst();
			ConcessaoVeiculo concessaoVeiculo = null;
			Agente agente = AgenteDAO.get( ParametroServices.getValorOfParametroAsInteger("CD_AGENTE_IMPORTACAO", 0) );
			if( agente==null ){
				return new Result(-1,"Agente de importação não configurado");
			}
			while (rsmConcessaoVeiculo.next()) {
				
				if( rsmConcessaoVeiculo.getInt("CD_LACRE_CATRACA") == 0 ){
					concessaoVeiculo = ConcessaoVeiculoDAO.get( rsmConcessaoVeiculo.getInt("CD_CONCESSAO_VEICULO") );
					Lacre lacre = new Lacre( 0, "XXXX", LacreServices.ST_DISPONIVEL, "X");
					int retorno = LacreDAO.insert(lacre, connect);
					if( retorno<=0 ){
						Conexao.rollback(connect);
						return new Result(-1,"Erro cadastrar lacre");
					}
					AfericaoCatraca afericao = new AfericaoCatraca( 0,
													concessaoVeiculo.getCdConcessaoVeiculo(),
													0, 
													(concessaoVeiculo.getDtAssinatura()!=null)
														?concessaoVeiculo.getDtAssinatura()
														:rsmConcessaoVeiculo.getGregorianCalendar("DT_INICIO_OPERACAO"),
													0, 0, 0, "Leitura automática gerada pela TIVIC para vinculação inicial de lacres",
													agente.getCdAgente(), agente.getCdUsuario(), AfericaoCatracaServices.TP_APLICACAO_VINCULACAO);
					LacreCatraca lacreCatraca = new LacreCatraca(0, retorno,
												concessaoVeiculo.getCdConcessaoVeiculo(), 0, 0);
					@SuppressWarnings("unused")
					Result r = LacreCatracaServices.save(lacreCatraca, null, afericao, null, concessaoVeiculo.getStConcessaoVeiculo(), connect);
					
				}
				
			}
			connect.commit();
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.geracaoLacreCatraca: " +  e);
			return new Result(-1,"Erro ao cadastrar lacres iniciais");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @author Edgard
	 * @since 10/04/2017
	 * @return
	 */
	public static Result alteracaoSituacaoCartaoPne() {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			
			ResultSetMap rsmCartoes = CartaoServices.getAll(connect);
			
			while(rsmCartoes.next()){
				Cartao			  cartao 		= new Cartao();
				cartao.setCdCartao(rsmCartoes.getInt("CD_CARTAO"));
				cartao.setCdPessoa(rsmCartoes.getInt("CD_PESSOA"));
				cartao.setDtEmissao(rsmCartoes.getGregorianCalendar("DT_EMISSAO"));
				cartao.setDtValidade(rsmCartoes.getGregorianCalendar("DT_VALIDADE"));
				cartao.setLgAcompanhante(rsmCartoes.getInt("LG_ACOMPANHANTE"));
				cartao.setNrVia(rsmCartoes.getInt("NR_VIA"));
				cartao.setStCartao(rsmCartoes.getInt("ST_CARTAO"));
				cartao.setTpCartao(rsmCartoes.getInt("TP_CARTAO"));
				cartao.setTpVigencia(rsmCartoes.getInt("TP_VIGENCIA"));
				
				GregorianCalendar dtVencimento 	= rsmCartoes.getGregorianCalendar("DT_VALIDADE");
				GregorianCalendar dtHoje		= new GregorianCalendar();
				
				if(dtVencimento == null || dtVencimento.before(dtHoje)){
					cartao.setStCartao(0);
					CartaoDAO.update(cartao, connect);
				}
			}
			
			connect.commit();
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! TaskServices.alteracaoSituacaoCartaoPne: " +  e);
			return new Result(-1,"Erro ao alterar situação de cartões PNE");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

}

