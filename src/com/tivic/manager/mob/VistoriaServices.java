package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;	
import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.msg.MensagemServices;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class VistoriaServices {
	
	public static final String[] situacaoVistoria = {"Pendente", "Aprovada", "Reprovada", "Rep. Não Comparecimento"};
	public static final int ST_PENDENTE   = 0;
	public static final int ST_APROVADA   = 1;
	public static final int ST_REPROVADA  = 2;
	public static final int ST_REP_NAO_COMPARECIMENTO  = 3;

	public static final String[] tipoVistoria = {"Vinculção","Comum"};
	public static final int TP_VINCULACAO   = 0;
	public static final int TP_COMUM        = 1;
	
	public static Result saveLote(ArrayList<Vistoria> vistoria){
		Connection connect = null;

		try {			
			connect = Conexao.conectar();
			connect.setAutoCommit(false);			
			
			int retorno = 0;
			String msgErro = "";		
			Result rsSave = new Result(0);

			for (Vistoria saveVistoria : vistoria) {
				
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria " +
											                       "WHERE cd_pessoa = " + saveVistoria.getCdPessoa() +
											                       "  AND cd_veiculo = " + saveVistoria.getCdVeiculo() +
											                       "  AND st_vistoria = " + ST_PENDENTE+
											                       "ORDER BY cd_vistoria DESC LIMIT 1");
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

				if (rsm.next()){
					saveVistoria.setCdVistoria(rsm.getInt("cd_vistoria"));
					VistoriaDAO.update(saveVistoria);
				}else{
					rsSave = save(saveVistoria);
					if(rsSave.getCode()<=0){
						msgErro = rsSave.getMessage();
						retorno += 1;
					}
				}
				
				connect.commit();
				
				rsSave = save(saveVistoria);
				if(rsSave.getCode()<=0){
					msgErro = rsSave.getMessage();
					retorno += 1;
				}
			}
			return new Result(retorno, (retorno > 0 ? msgErro : (msgErro!=""? msgErro : "Lote de Vistoria salvo com sucesso!!")));
			
		}
		catch(Exception e){
			e.printStackTrace();
			if (connect != null)
				Conexao.rollback(connect);
			return new Result(-2, "Erro ao registar vistoria!");
			
		}
		finally{
			if (connect != null)
				Conexao.desconectar(connect);			
		}
		
		
		
		
		
	}
	
	public static Result save(Vistoria vistoria){
		return save(vistoria, null, null, null);
	}
	
	public static Result save(Vistoria vistoria, Connection connect){
		return save(vistoria, null, null, connect);
	}
	public static Result save(Vistoria vistoria, ResultSetMap rsmVistoriaItem){
		return save(vistoria, rsmVistoriaItem, null, null);
	}
	public static Result save(Vistoria vistoria, ResultSetMap rsmVistoriaItem,Connection connect ){
		return save(vistoria, rsmVistoriaItem, null, connect);
	}
	public static Result save(Vistoria vistoria, ResultSetMap rsmVistoriaItem, GregorianCalendar dtRetorno ){
		return save(vistoria, rsmVistoriaItem, dtRetorno, null);
	}
	
	/**
	 * Adaptação da assinatura para compatibilidade com a chamada do methodCaller a partir do aplicativo;
	 * @author Alvaro
	 * @param vistoria
	 * @param itensVistoria
	 * @return
	 */
	public static Result save(Vistoria vistoria, ArrayList<HashMap<String, Object>> itensVistoria ){
		ResultSetMap rsm = new ResultSetMap();
		rsm.setLines(itensVistoria);
		return save(vistoria, rsm, null, null);
	}
//	public static Result save(Vistoria vistoria, ArrayList<HashMap<String, Object>> itensVistoria, GregorianCalendar dtRetorno ){
//		ResultSetMap rsm = new ResultSetMap();
//		rsm.setLines(itensVistoria);
//		return save(vistoria, rsm, dtRetorno, null);
//	}
	
	public static Result save(Vistoria vistoria, ResultSetMap rsmVistoriaItem, GregorianCalendar dtRetorno, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmDefeitos;
		ResultSetMap rsmAnexos;
		boolean heNovaVistoriaDeRetorno = false;
		@SuppressWarnings("unused")
		String defeitoObservacao = "";		
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(vistoria==null)
				return new Result(-1, "Erro ao salvar. Vistoria é nulo");
			
			// Verifica se a vistoria é de retorno
			if(vistoria.getCdVistoria() == 0 && vistoria.getCdVistoriaAnterior() != 0 && vistoria.getDtAplicacao() == null){
				heNovaVistoriaDeRetorno = true;				
			}	
			
			int retorno;
          
			if(vistoria.getCdVistoria()==0){				 
				retorno = VistoriaDAO.insert(vistoria, connect);
				vistoria.setCdVistoria(retorno);// Atribui o novo código da vistoria de retorno gerado pelo banco
			}
			else {
//             Verificar se uma vistoria pode ser editada.
//				/** 
//				 * Remove as vistorias de retorno caso um resultado anterior seja alterado de 'Reprovado' para 'Aprovado'
//				 * 
//				 */
//				Vistoria vistoriaCadastrada = VistoriaDAO.get(vistoria.getCdVistoria());
//				if( vistoriaCadastrada.getStVistoria() == ST_REPROVADA
//					&& vistoria.getStVistoria() == ST_APROVADA	){
//					removeVistoriaRetorno(vistoria.getCdVistoria(), connect);
//				}
								
				retorno = VistoriaDAO.update(vistoria, connect);
			}				
			
			
			if( rsmVistoriaItem != null ){
				rsmVistoriaItem.beforeFirst();
				while( rsmVistoriaItem.next() ){
					
					VistoriaPlanoItem vistoriaPlanoItem =  VistoriaPlanoItemDAO.get( rsmVistoriaItem.getInt("CD_VISTORIA_PLANO_ITEM"));
					if( vistoriaPlanoItem == null || heNovaVistoriaDeRetorno)
						vistoriaPlanoItem = new VistoriaPlanoItem();
					
					vistoriaPlanoItem.setCdPlanoVistoria( vistoria.getCdPlanoVistoria() );
					vistoriaPlanoItem.setCdVistoria( vistoria.getCdVistoria() );
					vistoriaPlanoItem.setCdVistoriaItem( rsmVistoriaItem.getInt("CD_VISTORIA_ITEM") );					
					vistoriaPlanoItem.setDsObservacao( rsmVistoriaItem.getString("DS_OBSERVACAO") );				

//					if(heNovaVistoriaDeRetorno){
//						vistoriaPlanoItem.setCdVistoria(vistoria.getCdVistoriaAnterior() + 1);												
//					}
					
					// Caso seja um vistoria de retorno, trata os itens reprovados setando-os para na verificado
					if(heNovaVistoriaDeRetorno && rsmVistoriaItem.getInt("ST_ITEM") == VistoriaPlanoItemServices.ST_REPROVADO){
						vistoriaPlanoItem.setStItem( VistoriaPlanoItemServices.ST_NAO_VERIFICADO );
					}
					else{
						vistoriaPlanoItem.setStItem( rsmVistoriaItem.getInt("ST_ITEM") );
					}									
											
					Result r = VistoriaPlanoItemServices.save(vistoriaPlanoItem, connect);					
					
					if( r.getCode() <= 0 ){
						Conexao.rollback(connect);
						return new Result(-2, "Erro ao registar situação do item de vistoria!");
					}					
					
					rsmDefeitos = (ResultSetMap)rsmVistoriaItem.getObject("DEFEITOS");
					if( rsmDefeitos != null && !heNovaVistoriaDeRetorno){
						rsmDefeitos.beforeFirst();
						while( rsmDefeitos.next() ){							
							VistoriaPlanoItemDefeito vistoriaPlanoItemDefeito = VistoriaPlanoItemDefeitoDAO.get( rsmDefeitos.getInt("CD_VISTORIA_PLANO_ITEM_DEFEITO") );
							if( vistoriaPlanoItemDefeito == null || heNovaVistoriaDeRetorno)
								vistoriaPlanoItemDefeito = new VistoriaPlanoItemDefeito();
							vistoriaPlanoItemDefeito.setCdVistoriaPlanoItem( vistoriaPlanoItem.getCdVistoriaPlanoItem() );
							vistoriaPlanoItemDefeito.setCdPlanoVistoria( vistoriaPlanoItem.getCdPlanoVistoria() );														
							vistoriaPlanoItemDefeito.setCdVistoriaItem( vistoriaPlanoItem.getCdVistoriaItem() );
							vistoriaPlanoItemDefeito.setCdDefeitoVistoriaItem(rsmDefeitos.getInt("CD_DEFEITO_VISTORIA_ITEM") );
							r = VistoriaPlanoItemDefeitoServices.save(vistoriaPlanoItemDefeito, connect);
							if( r.getCode() <= 0 ){
								Conexao.rollback(connect);
								return new Result(-2, "Erro ao registar defeito do item de vistoria!");
							}
						}
					}
					rsmAnexos = (ResultSetMap)rsmVistoriaItem.getObject("ANEXOS");
					if( rsmAnexos != null && !heNovaVistoriaDeRetorno){
						rsmAnexos.beforeFirst();
						while( rsmAnexos.next() ){
							VistoriaPlanoItemArquivo anexo = VistoriaPlanoItemArquivoDAO.get(vistoriaPlanoItem.getCdVistoriaPlanoItem(), rsmAnexos.getInt("CD_ARQUIVO"));
							if( anexo == null || heNovaVistoriaDeRetorno)
								anexo = new VistoriaPlanoItemArquivo();
							anexo.setCdVistoriaPlanoItem( vistoriaPlanoItem.getCdVistoriaPlanoItem() );
							Arquivo arquivo = new Arquivo();
							arquivo.setDtArquivamento( new GregorianCalendar() );
							arquivo.setNmArquivo( rsmAnexos.getString("NM_ARQUIVO") );
							arquivo.setBlbArquivo( (byte[])rsmAnexos.getObject("BLB_ARQUIVO") );
							r = VistoriaPlanoItemArquivoServices.save(anexo, arquivo, connect);
							if( r.getCode() <= 0 ){
								Conexao.rollback(connect);
								return new Result(-2, "Erro ao registar anexo do item de vistoria!");
							}
						}
					}
				}
			}
			
			if(dtRetorno!=null){
				Vistoria vistoriaRetorno = new Vistoria( 0/*cdVistoria*/, dtRetorno/*dtVistoria*/, 0/*cdAgente*/, 
												vistoria.getCdPessoa()/*cdPessoa*/, vistoria.getCdEquipamento()/*cdEquipamento*/,
												vistoria.getCdVeiculo()/*cdVeiculo*/, vistoria.getCdPlanoVistoria()/*cdPlanoVistoria*/, 
												vistoria.getCdVistoria()/*cdVistoriaAnterior*/, VistoriaServices.ST_PENDENTE/*stVistoria*/,
												null/*dtAplicacao*/, VistoriaServices.TP_COMUM/*tpVistoria*/, "", "", vistoria.getCdVistoriador(), vistoria.getCdCondutor(), vistoria.getCdConcessao());
				
				Result resultVistoria = VistoriaServices.save(vistoriaRetorno, rsmVistoriaItem ,connect);
				if(resultVistoria.getCode()<=0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao agendar vistoria de retorno!");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VISTORIA", vistoria);
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
	
	public static String verificarVistoriasVencidas(){
		Connection connect = null;

		try {			
			connect = Conexao.conectar();
			connect.setAutoCommit(false);			
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria " +
					                                            "SET st_vistoria = 3 " +       
					                                            "WHERE cd_vistoria IN (SELECT cd_vistoria FROM mob_vistoria " + 
					                                            "WHERE st_vistoria = 0 AND dt_vistoria < now())");
									
			pstmt.executeUpdate();
			
			connect.commit();
			return "[Mobilidade] Rotina de verificação de vencimento de Vistorias executada com sucesso";
		}
		catch(Exception e){
			e.printStackTrace();
			if (connect != null)
				Conexao.rollback(connect);
			return "[Mobilidade] Um erro ocorreu na rotina de verificação de vencimento de vistorias";
			
		}
		finally{
			if (connect != null)
				Conexao.desconectar(connect);			
		}
		
	}

	public static ResultSetMap copiarItensVistorias() {		
		Connection connect = Conexao.conectar();
		ResultSetMap rsmVistoriaItem;
		ResultSetMap rsmDefeitos;
		int contItens;	
		int contDefeitos;
		String itensReprovadoObservacao;
		int cdVistoria;

		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();			
			criterios.add(new ItemComparator("A.ST_VISTORIA", String.valueOf(2), ItemComparator.EQUAL, Types.INTEGER));				

			ResultSetMap rsmResultado = VistoriaServices.find(criterios);

			rsmResultado.beforeFirst();
			while( rsmResultado.next() ){
				itensReprovadoObservacao = "";
				contItens = 1;		
				cdVistoria = rsmResultado.getInt("CD_VISTORIA");
				rsmVistoriaItem = getResultadoVistoria(cdVistoria);

				if( rsmVistoriaItem != null ){
					rsmVistoriaItem.beforeFirst();
					while( rsmVistoriaItem.next() ){
						if(rsmVistoriaItem.getInt("ST_ITEM") == 2){
							
							itensReprovadoObservacao += contItens + "-" + rsmVistoriaItem.getString("NM_VISTORIA_ITEM") + " ";

							rsmDefeitos = (ResultSetMap)rsmVistoriaItem.getObject("DEFEITOS");	
							contDefeitos = 1;
							if( rsmDefeitos != null){
								rsmDefeitos.beforeFirst();
								while( rsmDefeitos.next() ){	
									if(rsmDefeitos.getInt("CD_VISTORIA_PLANO_ITEM_DEFEITO") != 0){									
										itensReprovadoObservacao += contItens + "." + contDefeitos + "-" + rsmDefeitos.getString("NM_DEFEITO") + " ";
										contDefeitos++;
									}
								}
							}
							contItens++;
						}
						else{
							continue;						
						}						
					}					
				}			
				
				if(itensReprovadoObservacao != "" && !rsmResultado.getString("DS_OBSERVACAO").contains("ITENS REPROVADOS NA VISTORIA")){					
					Vistoria vistoriaAtualizacao = VistoriaDAO.get(cdVistoria);
					String novaObservacao = vistoriaAtualizacao.getDsObservacao() + "\n\nITENS REPROVADOS NA VISTORIA: " + itensReprovadoObservacao;
					
					vistoriaAtualizacao.setDsObservacao( novaObservacao);	
					VistoriaDAO.update(vistoriaAtualizacao);					
				}				
			}

			return rsmResultado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.getResultadoVistoria: " + e);
			return null;
		}
		finally {			
			Conexao.desconectar(connect);
		}
	}
	
	@SuppressWarnings("unused")
	private static Result removeVistoriaRetorno(int cdVistoria, Connection connect ){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result r;
			if( cdVistoria == 0 )
				return new Result(-1, "Vistoria não informada!");
			ResultSetMap rsmVistoriasRetorno = new ResultSetMap( connect.prepareStatement(
									"SELECT * FROM mob_vistoria WHERE cd_vistoria_anterior = "+cdVistoria
									).executeQuery());
			
			if( rsmVistoriasRetorno != null ){
				rsmVistoriasRetorno.beforeFirst();
				while ( rsmVistoriasRetorno.next() ) {
					r = removeVistoriaRetorno( rsmVistoriasRetorno.getInt("CD_VISTORIA")  , connect);
					if( r.getCode() <= 0 ){
						if (isConnectionNull)
							Conexao.desconectar(connect);
						return r;
					}
				}
			}
			
			r = remove(cdVistoria, true, connect);
			
			if(r.getCode()<=0){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Erro ao excluir vistorias de retorno!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Vistorias de retorno excluídas com sucesso!");
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
	
	public static Result remove(int cdVistoria){
		return remove(cdVistoria, false, null);
	}
	public static Result remove(int cdVistoria, boolean cascade){
		return remove(cdVistoria, cascade, null);
	}
	public static Result remove(int cdVistoria, boolean cascade, Connection connect){
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
				
			/**
			 * Excluindo arquivos anexados
			 */
			VistoriaPlanoItemArquivoServices.removeAll(cdVistoria, connect);
			/**
			 * Excluindo Defeitos registrados
			 */
			VistoriaPlanoItemDefeitoServices.removeAll(cdVistoria, connect);
			/**
			 * Excluindo Itens avaliados
			 */
			VistoriaPlanoItemServices.removeAll(cdVistoria, connect);
			retorno = VistoriaDAO.delete(cdVistoria, connect);
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
	
	public static ResultSetMap getResultadoVistoria(int cdVistoria, boolean lgHierarquizar) {
		return getResultadoVistoria(cdVistoria, lgHierarquizar, null);
	}
	public static ResultSetMap getResultadoVistoria(int cdVistoria) {
		return getResultadoVistoria(cdVistoria, false, null);
	}
	
	public static ResultSetMap getResultadoVistoria(int cdVistoria, boolean lgHierarquizar, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
			PreparedStatement pstmt;
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_VISTORIA", String.valueOf(cdVistoria), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmResultado = VistoriaPlanoItemServices.find(criterios, connect);
			
			String sqlDefeitos = "SELECT * FROM mob_plano_vistoria_item_defeito A "+
								 " JOIN mob_defeito_vistoria_item            B ON ( A.cd_defeito_vistoria_item = B.cd_defeito_vistoria_item ) "+
								 " LEFT JOIN mob_vistoria_plano_item_defeito C ON ( A.cd_defeito_vistoria_item = C.cd_defeito_vistoria_item "+
								 " 													AND C.cd_vistoria_plano_item = ? ) "+								 
								 " WHERE A.cd_plano_vistoria = ? and A.cd_vistoria_item = ? "+
								 " ORDER BY NM_DEFEITO ASC ";
			rsmResultado.beforeFirst();
			while( rsmResultado.next() ){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_VISTORIA_PLANO_ITEM", rsmResultado.getString("CD_VISTORIA_PLANO_ITEM"), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap anexos = VistoriaPlanoItemArquivoServices.find(criterios, connect);
				if( anexos!=null )
					rsmResultado.setValueToField("ANEXOS", anexos);
				
				pstmt = connect.prepareStatement(sqlDefeitos);
				pstmt.setInt(1, rsmResultado.getInt("CD_VISTORIA_PLANO_ITEM") );
				pstmt.setInt(2, rsmResultado.getInt("CD_PLANO_VISTORIA") );
				pstmt.setInt(3, rsmResultado.getInt("CD_VISTORIA_ITEM") );
				ResultSetMap rsmDefeitos = new ResultSetMap(pstmt.executeQuery());
				rsmResultado.setValueToField("DEFEITOS", rsmDefeitos );
				
			}
			if( lgHierarquizar )
				rsmResultado = VistoriaItemServices.hierarquizar(rsmResultado);
			return rsmResultado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.getResultadoVistoria: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement(" SELECT *, G.nm_pessoa as nm_vistoriado " +
											" FROM mob_vistoria A " +
											" JOIN fta_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " + 
											" LEFT JOIN mob_concessao C ON ( A.cd_pessoa = C.cd_concessionario ) " + 
											" LEFT JOIN mob_concessao_veiculo D ON ( C.cd_concessao = D.cd_concessao AND A.cd_veiculo = D.cd_veiculo ) " + 
											" LEFT JOIN fta_marca_modelo E ON (B.cd_marca = E.cd_marca) "+ 
											" LEFT JOIN mob_agente F ON (A.cd_agente = F.cd_agente) " +
											" JOIN grl_pessoa G ON (A.cd_pessoa = G.cd_pessoa)"+ 
											" ORDER BY A.ST_VISTORIA, A.DT_VISTORIA DESC " );
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findColetivoUrbano() {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("C.TP_CONCESSAO", String.valueOf( ConcessaoServices.TP_COLETIVO_URBANO ), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			String orderBy = "";
	//		String data    = "";			
			int qtRegistros = 0;
			boolean pesquisaPonto = false;	
			boolean lgNmVeiculo = false;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim() + " DESC";
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("LIMIT")) {
					qtRegistros = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NM_GRUPO_PARADA")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_PONTO")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("DS_REFERENCIA")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_ORDEM")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaPonto")) {
					pesquisaPonto = true;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("lgNmVeiculo")) {
					lgNmVeiculo = true;
					criterios.remove(i);
					i--;
				}
			}
			
			String sql = " SELECT *, A.dt_aplicacao, C.tp_concessao, G.nm_pessoa as nm_vistoriado, I.nm_pessoa as nm_vistoriador,  " +
						" J.cd_vistoria as cd_vistoria_retorno, J.dt_vistoria as dt_retorno, J.id_selo,  "+
						" J.st_vistoria as st_vistoria_retorno, D.nr_prefixo, K.nm_pessoa as nm_condutor,"+
						" L.nm_pessoa as nm_concessionario, L.nm_email AS nm_email_concessionario "+
						(pesquisaPonto? " , M.ds_referencia AS NR_ORDEM, N.nm_grupo_parada AS NR_PONTO " : "")+
						" FROM mob_vistoria A " +
						" JOIN fta_veiculo        		   B ON (A.cd_veiculo = B.cd_veiculo) " + 
						" LEFT JOIN mob_concessao 		   C ON (A.cd_pessoa = C.cd_concessionario ) " + 
						" LEFT JOIN mob_concessao_veiculo  D ON (C.cd_concessao = D.cd_concessao AND A.cd_veiculo = D.cd_veiculo ) " + 
						" LEFT JOIN fta_marca_modelo       E ON (B.cd_marca = E.cd_marca) "+ 
						" LEFT JOIN mob_agente             F ON (A.cd_agente = F.cd_agente) " +
						" JOIN grl_pessoa 				   G ON (A.cd_pessoa = G.cd_pessoa)"+ 
						" JOIN mob_plano_vistoria 		   H ON (A.cd_plano_vistoria = H.cd_plano_vistoria)"+ 
						" LEFT JOIN grl_pessoa 			   I ON (A.cd_vistoriador = I.cd_pessoa) "+
						" LEFT JOIN mob_vistoria 		   J ON (J.cd_vistoria_anterior = A.cd_vistoria) "+
						" LEFT JOIN grl_pessoa  		   K ON (A.cd_condutor = K.cd_pessoa) "+
						" LEFT JOIN grl_pessoa  		   L ON (C.cd_concessionario = L.cd_pessoa) "+
						(pesquisaPonto ? 
						" LEFT OUTER JOIN mob_parada       M ON (M.cd_concessao = C.cd_concessao) " +
						" LEFT OUTER JOIN mob_grupo_parada N ON (N.cd_grupo_parada = M.cd_grupo_parada) " : "" );
			
			//LogUtils.info("SQL:\n"+Search.getStatementSQL(sql, (orderBy != "" ? orderBy : " ORDER BY A.ST_VISTORIA, A.DT_VISTORIA DESC, D.NR_PREFIXO "), criterios, true));
						
			ResultSetMap rsm = Search.find(sql, (orderBy != "" ? orderBy : " ORDER BY A.ST_VISTORIA, A.DT_VISTORIA ASC ") + (qtRegistros > 0? " LIMIT " + qtRegistros: ""),
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			if (lgNmVeiculo)
				ConcessaoVeiculoServices.setNmVeiculo(rsm);
			
			//LogUtils.logTimer("VISTORIA_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			//LogUtils.debug("VistoriaServices.find: Iniciando injeção de dados adicionais...");
			
			if( rsm != null ){
				
				rsm.beforeFirst();
				while(rsm.next()){
					rsm.setValueToField("NM_SITUACAO_VISTORIA", situacaoVistoria[rsm.getInt("ST_VISTORIA")]);
				}
			}
			
			//LogUtils.debug(rsm.size() + " registro(s)");
			//LogUtils.logTimer("VISTORIA_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			//LogUtils.destroyTimer("VISTORIA_FIND_TIMER");
			 
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.find: " + e);
			return null;
		}
	}
	
	public static ResultSetMap findVistoriador(ArrayList<ItemComparator> criterios) {
		return findVistoriador(criterios, null);
	}

	public static ResultSetMap findVistoriador(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			String orderBy = "";			
			int qtRegistros = 0;
			
			String sql = " SELECT A.*, A.dt_aplicacao, A.st_vistoria, A.cd_pessoa, B.nr_placa, G.nm_pessoa AS nm_vistoriado, I.nm_pessoa AS nm_vistoriador,"+
						 " J.cd_vistoria as cd_vistoria_retorno, J.dt_vistoria as dt_retorno, J.id_selo, J.st_vistoria AS st_vistoria_retorno," + 
						 " D.nr_prefixo, K.nm_pessoa AS nm_concessionario," + 
						 " L.ds_referencia AS NR_ORDEM, M.nm_grupo_parada AS NR_PONTO" + 
						 " FROM mob_vistoria 					A" + 
						 " JOIN fta_veiculo 					B ON (A.cd_veiculo = B.cd_veiculo)" + 
						 " LEFT JOIN mob_concessao 				C ON (A.cd_pessoa = C.cd_concessionario)" + 
						 " LEFT JOIN mob_concessao_veiculo 		D ON (C.cd_concessao = D.cd_concessao AND A.cd_veiculo = D.cd_veiculo)" +
						 " LEFT JOIN mob_agente		 			F ON (A.cd_agente = F.cd_agente)" + 
						 " JOIN grl_pessoa 						G ON (A.cd_pessoa = G.cd_pessoa)" + 
						 " JOIN mob_plano_vistoria 				H ON (A.cd_plano_vistoria = H.cd_plano_vistoria)" + 
						 " LEFT JOIN grl_pessoa 				I ON (A.cd_vistoriador = I.cd_pessoa)" + 
						 " LEFT JOIN mob_vistoria 				J ON (J.cd_vistoria_anterior = A.cd_vistoria)" + 
						 " LEFT JOIN grl_pessoa 				K ON (C.cd_concessionario = K.cd_pessoa)" + 
						 " LEFT OUTER JOIN mob_parada 			L ON (L.cd_concessao = C.cd_concessao)" + 
						 " LEFT OUTER JOIN mob_grupo_parada 	M ON (M.cd_grupo_parada = L.cd_grupo_parada)" +
						 " WHERE 1=1";
						
			ResultSetMap rsm = Search.find(sql, ( orderBy != "" ? orderBy + " DESC " : " ORDER BY A.DT_APLICACAO ASC ") + (qtRegistros > 0? " LIMIT " + qtRegistros: ""),
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			if( rsm != null ){
				
				rsm.beforeFirst();
				while(rsm.next()){
					rsm.setValueToField("NM_SITUACAO_VISTORIA", situacaoVistoria[rsm.getInt("ST_VISTORIA")]);
				}
			}
			 
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.findRelatorio: " + e);
			return null;
		}
	}
	
	
	public static ResultSetMap findSimples(ArrayList<ItemComparator> criterios) {
		return findSimples(criterios, null);
	}
	
	public static ResultSetMap findSimples(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			String orderBy = "";		
			int qtRegistros = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("LIMIT")) {
					qtRegistros = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}
			}
			
			String sql = "SELECT A.*, B.*, C.*, D.*, E.nm_pessoa " +
						" FROM mob_vistoria A " +
						" JOIN fta_veiculo        		   		B ON (A.cd_veiculo = B.cd_veiculo) " + 
						" LEFT JOIN mob_concessao 		   		C ON (A.cd_pessoa = C.cd_concessionario ) " + 
						" LEFT JOIN mob_concessao_veiculo  		D ON (C.cd_concessao = D.cd_concessao AND A.cd_veiculo = D.cd_veiculo ) " +
						" LEFT JOIN grl_pessoa 					E ON (C.cd_concessionario = E.cd_pessoa) " +
						" WHERE 1 = 1 ";
						
			ResultSetMap rsm = Search.findAndLog(sql, (orderBy != "" ? orderBy : " ORDER BY A.ST_VISTORIA, A.DT_VISTORIA ASC ") + (qtRegistros > 0? " LIMIT " + qtRegistros: ""),
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			 
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.find: " + e);
			return null;
		}
	}
	
	public static ResultSetMap findVistoriaByConcessao(int cdConcessao) {
		return findVistoriaByVeiculo(0,cdConcessao, null);
	}
	public static ResultSetMap findVistoriaByVeiculo (int cdVeiculo) {
		return findVistoriaByVeiculo(cdVeiculo, null);
	}
	public static ResultSetMap findVistoriaByVeiculo(int cdVeiculo, Connection connect) {
		return findVistoriaByVeiculo(cdVeiculo, 0, connect);
	}
	public static ResultSetMap findVistoriaByVeiculo(int cdVeiculo, int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, C.*, D.*, E.nm_pessoa AS NM_CONCESSIONARIO, F.cd_concessao, " +
					"G.nr_prefixo, H.nm_pessoa AS NM_VISTORIADOR " +
					"FROM mob_vistoria A " +
					"LEFT JOIN mob_agente          B ON (A.cd_agente  = B.cd_agente) " +
					"JOIN fta_veiculo              C ON (A.cd_veiculo = C.cd_veiculo) " + 
					"JOIN fta_marca_modelo         D ON (C.cd_marca   = D.cd_marca) " + 
					"JOIN grl_pessoa               E ON (A.cd_pessoa  = E.cd_pessoa) " +
					"LEFT OUTER JOIN mob_concessao F ON (E.cd_pessoa  = F.cd_concessionario) " +
					"LEFT OUTER JOIN mob_concessao_veiculo G ON (G.cd_veiculo = C.cd_veiculo) " +
					"LEFT JOIN grl_pessoa 		   H ON (A.cd_vistoriador = H.cd_pessoa) "+
					(cdConcessao > 0 ?"WHERE F.cd_concessao  = " + cdConcessao : "") +
					(cdVeiculo   > 0 ?"WHERE C.cd_veiculo    = " + cdVeiculo   : "")
				);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.findVistoriaByVeiculo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.findVistoriaByVeiculo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAllVistoriasByGroup() {
		return findAllVistoriasByGroup(null);
	}
	
	public static ResultSetMap findAllVistoriasByGroup(Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT COUNT(A.CD_VISTORIA) AS QT_VISTORIAS, B.NM_PLANO_VISTORIA FROM mob_vistoria A "+
					"JOIN mob_plano_vistoria B on ( A.cd_plano_vistoria = B.cd_plano_vistoria ) "+
					"GROUP BY B.NM_PLANO_VISTORIA ");
			
			return new ResultSetMap(pstmt.executeQuery());
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static Result enviarEmailVistoria(Vistoria vistoria, int cdUsuario, int cdEmpresa, GregorianCalendar dtRetorno) {
		return enviarEmailVistoria(vistoria, cdUsuario, cdEmpresa, dtRetorno, null);
	}
	
	public static Result enviarEmailVistoria(Vistoria vistoria, int cdUsuario, int cdEmpresa, GregorianCalendar dtRetorno, Connection connect) {
		try {			
			String nmAgente = "";
			String mailDestinatario = "";
			String msgMail = "";
			String dsAssunto = "";
			String nrPrefixo = "";
			//busca pdf
			byte[] byteArray = printVistoriaReport(vistoria, cdUsuario, cdEmpresa, dtRetorno);
			
			if(byteArray == null)
				return new Result(-1, "Erro ao gerar PDF. ");
			//	
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			HashMap<String,Object> destinatario = new HashMap<String, Object>();
			HashMap<String,Object> anexos = new HashMap<String, Object>();
			ArrayList<HashMap<String, Object>> dadosDestinatario = new ArrayList<HashMap<String,Object>>();
			ArrayList<HashMap<String, Object>> dadosAnexos = new ArrayList<HashMap<String,Object>>();
			
			Mensagem mensagem = new Mensagem();
			
			
			//buscar dados do agente
			criterios.add(new ItemComparator("cd_agente", String.valueOf(vistoria.getCdAgente()), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmAgente = AgenteServices.find(criterios);
			if(rsmAgente.next()){
				nmAgente = rsmAgente.getString("nm_agente");
				mensagem.setCdUsuarioOrigem(rsmAgente.getInt("cd_usuario"));
			}
			else
				return new Result(-1, "Não é possível enviar mensagem. Agente não vinculado à vistoria.");
			
			//buscar dados do destinatário/Veiculo
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_concessao", String.valueOf(vistoria.getCdConcessao()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_veiculo", String.valueOf(vistoria.getCdVeiculo()), ItemComparator.EQUAL, Types.INTEGER));
			
			
			ResultSetMap rsmConcessaoVeiculo = ConcessaoVeiculoServices.find(criterios);
			if(rsmConcessaoVeiculo.next()){
				
				mailDestinatario = rsmConcessaoVeiculo.getString("nm_email_concessionario");
				if(mailDestinatario == null)
					return new Result(-1, "Não é possível enviar mensagem. Destinatário não possui email cadastrado.");
				
				nrPrefixo = String.valueOf(rsmConcessaoVeiculo.getInt("nr_prefixo"));
				if(nrPrefixo.trim() == "")
					return new Result(-1, "Não é possível enviar mensagem. Prefixo não cadastrado.");
				
				//destinatario para email
				destinatario.put("CD_PESSOA", rsmConcessaoVeiculo.getInt("cd_pessoa_mail"));
				destinatario.put("NM_EMAIL", mailDestinatario);
				anexos.put("BLB_ARQUIVO", byteArray);
				anexos.put("NM_ARQUIVO", "Vistoria_Prefixo: "+nrPrefixo+"_" + com.tivic.manager.util.Util.formatDate(vistoria.getDtAplicacao(), "ddmmyyyy") +".pdf" );
				
				dadosDestinatario.add(destinatario);
				dadosAnexos.add(anexos);
				//
				dsAssunto = "[Vistoria - " + ConcessaoServices.tiposConcessao[rsmConcessaoVeiculo.getInt("tp_concessao")] + "] ("+VistoriaServices.situacaoVistoria[vistoria.getStVistoria()].toUpperCase()+ ") Prefixo: " + nrPrefixo;
				mensagem.setDsAssunto(dsAssunto);
				
				msgMail = "Vistoria realizada " + com.tivic.manager.util.Util.formatDate(vistoria.getDtAplicacao(),  "dd/MM/yyyy HH") + "h" + 
											      com.tivic.manager.util.Util.formatDate(vistoria.getDtAplicacao(),  "mm")+ "min pelo agente " + nmAgente.toUpperCase();
				mensagem.setTxtMensagem(msgMail);
			}else
				return new Result(-1, "Não é possível enviar mensagem. Concessão não vinculada ao cadastro da vistoria");
			//
			mensagem.setDtEnvio(new GregorianCalendar());
			
			Result r = MensagemServices.enviarMensagem(mensagem, dadosDestinatario, null, dadosAnexos, connect);
			if(r.getCode()<=0) {
				return new Result(-1, "Erro ao enviar mensagem. " + r.getMessage());
			}
			else
				return new Result(1, "Email enviado com sucesso");

		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-7, "Erro desconhecido ao enviar mensagem.");
		}
	}
	
	private static byte[] printVistoriaReport(Vistoria vistoria, int cdUsuario, int cdEmpresa, GregorianCalendar dtRetorno){
				
		Empresa empresa   = EmpresaDAO.get(cdEmpresa);
		Usuario usuario   = UsuarioDAO.get(cdUsuario);
		
		Pessoa pessoaUsuario = PessoaDAO.get(usuario.getCdPessoa());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		String nmReport = "resultado_vistoria";
		
		//ResultSetMap rsmVistoriaItem = PlanoVistoriaItemServices.getAllByPlanoVistoria(vistoria.getCdPlanoVistoria()); 
		ResultSetMap rsmVistoriaItem = getResultadoVistoria(vistoria.getCdVistoria());
		rsmVistoriaItem.beforeFirst();
		while(rsmVistoriaItem.next()){
			ResultSetMap rsmDefeitos = (ResultSetMap)rsmVistoriaItem.getObject("DEFEITOS"); 
			rsmVistoriaItem.setValueToField("ITENS_DEFEITOS", rsmDefeitos.getLines() );
		}
		//busca dados da concessão para aquele veículo
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_concessao", String.valueOf(vistoria.getCdConcessao()), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_veiculo", String.valueOf(vistoria.getCdVeiculo()), ItemComparator.EQUAL, Types.INTEGER));
		
		ResultSetMap rsmConcessaoVeiculo = ConcessaoVeiculoServices.find(criterios);
		if(rsmConcessaoVeiculo.next()){
			params.put("NM_USUARIO", pessoaUsuario.getNmPessoa());
		    params.put("NM_RAZAO_SOCIAL", empresa.getNmPessoa());
		    //
			params.put("LOGO", empresa.getImgLogomarca());
			params.put("LOGO_DEV", null);
			params.put("NM_CONCESSIONARIO", rsmConcessaoVeiculo.getString("nm_concessionario"));
			params.put("NR_PLACA", rsmConcessaoVeiculo.getString("nr_placa"));
			params.put("NR_PREFIXO_PONTO", rsmConcessaoVeiculo.getString("nr_prefixo"));
			params.put("NR_ANO_FABRICACAO", rsmConcessaoVeiculo.getString("nr_ano_fabricacao"));
			params.put("NR_CHASSI", rsmConcessaoVeiculo.getString("nr_ano_chassi"));
			params.put("NM_MARCA", rsmConcessaoVeiculo.getString("nm_marca_carroceria"));
			params.put("NM_MODELO", rsmConcessaoVeiculo.getString("nm_modelo_carroceria"));
			params.put("DT_APLICACAO", Util.formatDateTime(vistoria.getDtAplicacao(), "dd/MM/yyyy HH:mm"));			
			params.put("NR_SELO", vistoria.getIdSelo());
			params.put("DS_OBSERVACAO", vistoria.getDsObservacao());
			params.put("ST_VISTORIA", vistoria.getStVistoria());			
			//data de retorno
			if(dtRetorno != null)
				params.put("DT_RETORNO", Util.formatDateTime(dtRetorno, "dd/MM/yyyy HH:mm"));
			//Plano de vistoria
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_plano_vistoria", String.valueOf(vistoria.getCdPlanoVistoria()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPlanoVistoria = PlanoVistoriaServices.find(criterios);
			
			if(rsmPlanoVistoria.next())
				params.put("NM_APLICACAO", rsmPlanoVistoria.getString("nm_plano_vistoria"));//Nm_plano_vistoria
			//Vistoriador
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(vistoria.getCdVistoriador()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmVistoriador = PessoaServices.find(criterios);
			
			if(rsmVistoriador.next())
				params.put("NM_VISTORIADOR", rsmVistoriador.getString("nm_pessoa"));
			//Condutor
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(vistoria.getCdCondutor()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCondutor = PessoaServices.find(criterios);
			
			if(rsmCondutor.next())
				params.put("NM_CONDUTOR", rsmConcessaoVeiculo.getString("nm_pessoa")); 
			
			
		}
		
//		for ( Object reg : rsmVistoriaItem){
//			ResultSetMap rsmDefeitos = reg.DEFEITOS as ResultSetMap;
//			reg.ITENS_DEFEITOS = (rsmDefeitos)?rsmDefeitos.lines:null;
//		}
//			
//		if( regVistoria.CD_VISTORIA && regVistoria.ST_VISTORIA != Vistoria.ST_PENDENTE ){				
//			nmReport = "resultado_vistoria";
//			dtRetorno.text = DateUtils.format( regVistoria.DT_RETORNO, "dd/MM/yyyy");					
//		}
//		
		byte[] byteArray = ReportServices.getPdfReport("mob/"+nmReport, params, rsmVistoriaItem);
	
		return byteArray;
	}

}
