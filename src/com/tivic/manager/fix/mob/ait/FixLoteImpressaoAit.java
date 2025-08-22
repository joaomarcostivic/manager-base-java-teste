package com.tivic.manager.fix.mob.ait;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.BairroServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.cidade.cidadeproprietario.CorretorIDCidade;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAitDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.TipoAdesaoSneEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranServicesMG;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.notificacao.TipoNotificacaoEnum;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class FixLoteImpressaoAit {
	
	private static int cdLoteImpressao = 461;
	private static int cdUsuario = 1;
	
    private static List<Ait> aitList = new ArrayList<>();
    
	public static void fixLoteImpressaoAit(Integer a){
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			
			//Insere o codigoMunicipio em cd_cidade_proprietario de mob_ait, retornado da consulta ao Detran em Produção ;
	        //fixCdCidadeProprietarioLote();
	        
			//Insere o bairro, a nº e logradouro em cd_bairro, ds_nr_imovel e ds_logradouro de mob_ait, retornados da consulta ao Detran em Produção ;
	        //fixEnderecoProprietarioLote();

	        //Rinicia a geração do Lote corrigido;
	        new LoteNotificacaoService().reiniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario);  

			System.out.println("Fim.");
			connection.commit();
		} catch (Exception e) {
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	public static List<Ait> fixDeletarAitsLoteImpressaoAit() throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    List<Ait> aitsExcluidos = new ArrayList<>();

	    try {
	        customConnection.initConnection(true);
	        ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("C:\\Users\\Ricardo Almeida\\Downloads\\nip_a_serem_corrigidas.csv", ",", true);
	        while (rsm.next()) {
	            System.out.println("Lista de AIT's: \n" + rsm);
	            String idAit = rsm.getString("ID_AIT");
	            Ait ait = AitServices.getById(idAit);
	            
	            if (ait == null) {
	                System.out.println("AIT com ID " + idAit + " não encontrado ou é nulo. Ignorando...");
	                continue; 
	            }
	            
	            LoteImpressaoAitDAO.delete(429, ait.getCdAit());
	            aitsExcluidos.add(ait);
	        }
	        
	        rsm = ResultSetMap.getResultsetMapFromCSVFile("C:\\Users\\Ricardo Almeida\\Downloads\\nip_a_serem_corrigidas.csv", ",", true);
	        while (rsm.next()) {
	            System.out.println("Lista de AIT's: " + rsm);
	            String idAit = rsm.getString("ID_AIT");
	            Ait ait = AitServices.getById(idAit);
	            
	            if (ait == null) {
	                System.out.println("AIT com ID " + idAit + " não encontrado ou é nulo. Ignorando...");
	                continue; 
	            }
	            
	            LoteImpressaoAitDAO.delete(430, ait.getCdAit());
	            aitsExcluidos.add(ait);
	        }
	        
	        customConnection.finishConnection();
			System.out.println("\n----------------------------------- FINALIZADO ---------------------------------------\n");
	    } finally {
	        customConnection.closeConnection();
	    }
	    return aitsExcluidos;
	}
	
	public static void fixCdCidadeProprietarioAits() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
	        ResultSetMap rsmListaAits = ResultSetMap.getResultsetMapFromCSVFile("C:\\Users\\Ricardo Almeida\\Downloads\\nip_a_serem_corrigidas.csv", ",", true);
	        	
	        while (rsmListaAits.next()) {
	        	String idAit = rsmListaAits.getString("ID_AIT");
	        	if (idAit == null || idAit.isEmpty()) {
	                System.out.println("ID_AIT não encontrado ou é vazio. Ignorando...");
	                continue;
	            }
	            Ait ait = AitServices.getById(idAit);
	            if (ait == null) {
	                System.out.println("AIT com ID " + idAit + " não encontrado ou é nulo. Ignorando...");
	                continue;
	            }
	        	
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT ltrim(A.ds_saida ->> 'codigoMunicipioPossuidor', '0') AS id_cidade_ibge, "
						+ "ltrim(A.ds_saida ->> 'bairroImovelPossuidor', '0') AS nm_bairro_possuidor, "
						+ " A.cd_arquivo_movimento, A.cd_movimento, A.cd_ait, C.cd_bairro, D.id_cidade, D.cd_cidade "
						+ " FROM mob_arquivo_movimento A "
						+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
						+ "     JOIN grl_bairro C ON (B.cd_bairro = C.cd_bairro) "
						+ "     JOIN grl_cidade D ON (D.id_cidade = ltrim(A.ds_saida ->> 'codigoMunicipioPossuidor', '0')) "
						+ "	WHERE A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
						+ " AND B.cd_ait = " + ait.getCdAit() 
				).executeQuery());
				if (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + ait.getCdAit());
				    continue; 
				}
				while (rsm.next()) {
					Cidade cidade = getByIdCidade(rsm.getString("ID_CIDADE_IBGE"));
					Bairro bairro = BairroServices.getBairroByNomeCidade(rsm.getString("NM_BAIRRO_POSSUIDOR"), cidade.getCdCidade(), customConnection.getConnection());
					
					if(bairro != null)
						ait.setCdBairro(bairro.getCdBairro());
					else {
						bairro = new Bairro();
						bairro.setCdCidade(cidade.getCdCidade());
						bairro.setNmBairro(rsm.getString("NM_BAIRRO_POSSUIDOR"));
						Result result = BairroServices.save(bairro, customConnection.getConnection());
						if(result.getCode() > 0) {
							ait.setCdBairro(bairro.getCdBairro());
						}
					}
					
					AitDAO.update(ait, customConnection.getConnection());
					
					System.out.println("<----------- AIT: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit());
					System.out.println("CD BAIRRO: " + ait.getCdBairro());
					System.out.println("\tCD_CIDADE NOVO: " + bairro.getCdCidade());
				}
	        }
			System.out.println("\n----------------------------------- FINALIZADO ---------------------------------------\n");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixCdCidadeProprietarioBairroLote() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT CASE WHEN D.id_cidade LIKE '0%' THEN A.ds_saida ->> 'codigoMunicipioPossuidor' "
						+ "	ELSE ltrim(A.ds_saida ->> 'codigoMunicipioPossuidor', '0') "
						+ "		END AS id_cidade_ibge, "
						+ " A.ds_saida ->> 'bairroImovelPossuidor' AS nm_bairro_possuidor, "
						+ "	A.cd_arquivo_movimento, A.cd_movimento, A.cd_ait, C.cd_bairro, D.id_cidade, D.cd_cidade, F.cd_lote_impressao "
						+ " FROM mob_arquivo_movimento A "
						+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
						+ "     JOIN grl_bairro C ON (B.cd_bairro = C.cd_bairro) "
						+ "     JOIN grl_cidade D ON (CAST(D.id_cidade AS text) = A.ds_saida ->> 'codigoMunicipioPossuidor' OR D.id_cidade = ltrim(A.ds_saida ->> 'codigoMunicipioPossuidor', '0')) "
						+ "     JOIN mob_lote_impressao_ait E ON (B.cd_ait = E.cd_ait) "
						+ "     JOIN mob_lote_impressao F ON (F.cd_lote_impressao = E.cd_lote_impressao) "
						+ "	WHERE A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
						+ " AND F.cd_lote_impressao = 429 "
				).executeQuery());
				while (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + rsm.getInt("cd_ait"));
				    continue; 
				}
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					
					Cidade cidade = getByIdCidade(rsm.getString("ID_CIDADE_IBGE"));
					Bairro bairro = BairroServices.getBairroByNomeCidade(rsm.getString("NM_BAIRRO_POSSUIDOR"), cidade.getCdCidade(), customConnection.getConnection());
					
					if(bairro != null)
						ait.setCdBairro(bairro.getCdBairro());
					else {
						bairro = new Bairro();
						bairro.setCdCidade(cidade.getCdCidade());
						bairro.setNmBairro(rsm.getString("NM_BAIRRO_POSSUIDOR"));
						Result result = BairroServices.save(bairro, customConnection.getConnection());
						if(result.getCode() > 0) {
							ait.setCdBairro(bairro.getCdBairro());
						}
					}
					
					AitDAO.update(ait, customConnection.getConnection());
					
					System.out.println("<----------- AIT: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit());
					System.out.println("CD BAIRRO: " + ait.getCdBairro());
					System.out.println("\tCD_CIDADE NOVO: " + bairro.getCdCidade());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixCdCidadeProprietarioLote() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
				System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						" SELECT A.cd_ait, A.cd_cidade_proprietario, A.nr_placa FROM mob_ait A "
						+ "	JOIN mob_lote_impressao_ait B ON (B.cd_ait = A.cd_ait) "
						+ "	JOIN mob_lote_impressao C ON (C.cd_lote_impressao = B.cd_lote_impressao) " 
						+ " WHERE A.cd_cidade_proprietario IS null AND C.cd_lote_impressao = 413 " 
				).executeQuery());
				while (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + rsm.getInt("cd_ait"));
				    continue; 
				}
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					System.out.println("<----------- AIT: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit() + " ...CD CIDADE PROPRIETÁRIO: " + ait.getCdCidadeProprietario() + " ...PLACA: " + ait.getNrPlaca());
					
					ServicoDetranConsultaServices servicoDetranConsultaServices = new ServicoDetranConsultaServices();
					ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarPlaca(ait.getNrPlaca());
					ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = (ConsultarPlacaDadosRetorno) servicoDetranObjeto.getDadosRetorno();

					CorretorIDCidade cidadeProprietario = new CorretorIDCidade();
					Cidade cidade = cidadeProprietario.getCidadeById(String.valueOf(consultarPlacaDadosRetorno.getCodigoMunicipio()));
					
					ait.setCdCidadeProprietario(cidade.getCdCidade());
					AitDAO.update(ait, customConnection.getConnection());
					
					System.out.println("<----------- AIT ATUALIZADO: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit() + " ...CD CIDADE PROPRIETÁRIO: " + ait.getCdCidadeProprietario() + " ...PLACA: " + ait.getNrPlaca());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixEnderecoProprietarioLote() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						" SELECT A.cd_ait, A.cd_bairro, A.ds_logradouro, A.ds_nr_imovel FROM mob_ait A "
						+ "	JOIN mob_lote_impressao_ait B ON (B.cd_ait = A.cd_ait) "
						+ "	JOIN mob_lote_impressao C ON (C.cd_lote_impressao = B.cd_lote_impressao) " 
						+ " WHERE ("
						+ "			A.cd_bairro IS null "
						+ "			OR A.ds_logradouro IS null "
						+ "			OR A.ds_nr_imovel IS null"
						+ "	) AND C.cd_lote_impressao = " + cdLoteImpressao 
				).executeQuery());
				while (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + rsm.getInt("cd_ait"));
				    continue; 
				}
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					
					System.out.println("<----------- AIT: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit() + " ... CD Bairro: " + ait.getCdBairro() + " ...Logradouro: " + ait.getDsLogradouro() + " ...Nº: " + ait.getDsNrImovel() );
					
					ServicoDetranConsultaServices servicoDetranConsultaServices = new ServicoDetranConsultaServices();
					ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarPlaca(ait.getNrPlaca());
					ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = (ConsultarPlacaDadosRetorno) servicoDetranObjeto.getDadosRetorno();
					
					CorretorIDCidade cidadeProprietario = new CorretorIDCidade();
					Cidade cidade = cidadeProprietario.getCidadeById(String.valueOf(consultarPlacaDadosRetorno.getCodigoMunicipio()));
					Bairro bairro = BairroServices.getBairroByNomeCidade(consultarPlacaDadosRetorno.getBairro(), cidade.getCdCidade(), customConnection.getConnection());
					
					if(bairro != null)
						ait.setCdBairro(bairro.getCdBairro());
					else {
						bairro = new Bairro();
						bairro.setCdCidade(cidade.getCdCidade());
						bairro.setNmBairro(consultarPlacaDadosRetorno.getBairro());
						Result result = BairroServices.save(bairro, customConnection.getConnection());
						if(result.getCode() > 0) {
							ait.setCdBairro(bairro.getCdBairro());
						}
					}
					ait.setDsLogradouro(consultarPlacaDadosRetorno.getLogradouro());
					ait.setDsNrImovel(consultarPlacaDadosRetorno.getNumero());
					AitDAO.update(ait, customConnection.getConnection());
					
					System.out.println("<----------- AIT ATUALIZADO: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit() + " ... CD Bairro: " + ait.getCdBairro() + " ...Logradouro: " + ait.getDsLogradouro() + " ...Nº: " + ait.getDsNrImovel() );
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void alterarPrazoVencimentoAitsLote() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			PreparedStatement pst = customConnection.getConnection().prepareStatement(
					"SELECT A.cd_ait, A.id_ait, A.dt_infracao, A.dt_vencimento FROM mob_ait A "
					+ "	JOIN mob_lote_impressao_ait B ON (B.cd_ait = A.cd_ait) "
					+ "		WHERE B.cd_lote_impressao = 447 "
					+ "	ORDER BY A.cd_ait ASC");
			System.out.println("SQL : " + pst);
			ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
			System.out.println("Resultado consulta SIZE: " + rsm.getLines().size());
			while (rsm.next()) {
				try {
					System.out.println("Alterando Data CD AIT: " + rsm.getString("CD_AIT"));
					System.out.println("Alterando Data ID AIT: " + rsm.getString("ID_AIT"));
					AlteraPrazoRecursoDTO alterarPrazoRecurso = new AlteraPrazoRecursoDTO();
					alterarPrazoRecurso.setCdAit(rsm.getInt("CD_AIT"));
					alterarPrazoRecurso.setCdUsuario(39);
					alterarPrazoRecurso.setNovoPrazoRecurso(new GregorianCalendar(2023, 8, 18));
					alterarPrazoRecurso.setTipoRecurso(TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey());
					new ServicoDetranServicesMG().alterarPrazoRecurso(alterarPrazoRecurso);
				}
				catch (Exception e) {
					System.out.println("XXXXXXXXXXXXXXXXXX ERROR XXXXXXXXXXXXXXXXXXXXX");
					System.out.println("Não foi possivel atualizar o AIT.");
				}
			}
			System.out.println("\n----------------------------------- FINALIZADO ---------------------------------------\n");
			customConnection.closeConnection();
		}
		finally {
			customConnection.finishConnection();
		}
	}
	
	public static Cidade getByIdCidade(String idCidade) {
		return getByIdCidade(idCidade, null);
	}
	
	public static Cidade getByIdCidade(String idCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_cidade WHERE id_cidade=?");
			pstmt.setString(1, idCidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return CidadeDAO.get(rs.getInt("cd_cidade"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void alterarPrazoVencimentoAitLote(Integer a){
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			System.out.println("-------------------FIX INICIADO-------------------");
			alterarPrazoVencimentoAitLote();
			System.out.println("-------------------FIX FINALIZADO-------------------");
			connection.commit();
		} catch (Exception e) {
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	public static void alterarPrazoVencimentoAitLote() throws Exception {
		String dtVencimentoInicial = "2024-03-08";
		String dtVencimentoFinal = "2024-03-25";
		GregorianCalendar novoPrazo = new GregorianCalendar(2024, 3, 05);
		int cdUsuario = 39;
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			PreparedStatement pst = customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT ON (A.cd_ait) A.cd_ait, A.id_ait, A.dt_infracao, A.dt_vencimento FROM mob_ait A "
						   + " LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait) " 
						   + "	WHERE DATE(A.dt_vencimento) BETWEEN '" + dtVencimentoInicial + "' AND '" + dtVencimentoFinal + "'" 
						   + " 	AND NOT EXISTS("   
						   + " 		SELECT K.cd_ait FROM mob_ait_movimento K "
						   + " 		WHERE"
						   + "			(" 
						   + "				K.tp_status IN (" + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
						   + "				, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
						   + "			    , " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
						   + "			   	," + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
						   + "	 		    , " + TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey()
						   + "			    , " + TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey()
						   + "			    , " + TipoStatusEnum.MULTA_PAGA.getKey()
						   + "				)"
						   + "			) AND K.cd_ait = A.cd_ait"
						   + "	)"
						   + " AND "
						   + " ("
						   + " 		NOT EXISTS  "
						   + "		("
						   + " 			SELECT B.cd_ait FROM mob_ait_movimento B "
						   + "				WHERE"
						   + "				("
						   + "					B.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey() 
						   + " 				and B.cd_ait = A.cd_ait )"	
						   + "  	)"
						   + "		OR "
						   + "		( "
						   + "			K.dt_movimento > "
						   + "			("
						   + "          	SELECT B3.dt_movimento FROM mob_ait_movimento B3 WHERE B3.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey()
						   + "       			AND B3.cd_ait = A.cd_ait ORDER BY B3.dt_movimento DESC limit 1 "
						   + "			)"
						   + "      ) " 
						   + " 	)"
						   + "	AND"
						   + "	("
						   + "		NOT EXISTS "
						   + "		( "
						   + "			SELECT E.cd_ait FROM mob_lote_impressao D "
						   + "			JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao) "	
						   + " 				WHERE "
						   + "				( D.tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NIP.getKey() + " and A.cd_ait = E.cd_ait )"
						   + " 		)"
						   + "		OR EXISTS "
						   + "		( "
						   + "			SELECT E1.cd_ait FROM mob_lote_impressao_ait E1 "
						   + "				WHERE E1.cd_ait = A.cd_ait and E1.cd_lote_impressao = "
						   + "				("
						   + "					SELECT D1.cd_lote_impressao FROM mob_lote_impressao D1 "
						   + "					JOIN mob_lote_impressao_ait E2 ON (E2.cd_lote_impressao = D1.cd_lote_impressao)"
						   + "						WHERE D1.tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NIP.getKey() 
						   + "						ORDER BY D1.dt_criacao DESC LIMIT 1"
						   + "				) AND E1.cd_ait = A.cd_ait AND E1.st_impressao = " + LoteImpressaoAitSituacao.REGISTRO_CANCELADO
						   + "		)"
						   + "	)"
						   + "	AND NOT EXISTS( SELECT L.cd_ait FROM mob_ait_movimento L"
						   + " 		WHERE("
						   + " 			L.st_adesao_sne = " + TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey() 
						   + " 			AND L.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey()
						   + "		) AND L.cd_ait = A.cd_ait )"
						   + " ORDER BY A.cd_ait DESC, A.dt_vencimento DESC ");
			System.out.println("SQL : " + pst);
			ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
			System.out.println("Resultado consulta quantidade: " + rsm.getLines().size());
			while (rsm.next()) {
				try {
					System.out.println("Alterando data CD AIT: " + rsm.getString("CD_AIT"));
					System.out.println("Alterando aata ID AIT: " + rsm.getString("ID_AIT"));
					AlteraPrazoRecursoDTO alterarPrazoRecurso = new AlteraPrazoRecursoDTO();
					alterarPrazoRecurso.setCdAit(rsm.getInt("CD_AIT"));
					alterarPrazoRecurso.setCdUsuario(cdUsuario);
					alterarPrazoRecurso.setNovoPrazoRecurso(novoPrazo);
					alterarPrazoRecurso.setTipoRecurso(TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey());
					new ServicoDetranServicesMG().alterarPrazoRecurso(alterarPrazoRecurso);
				}
				catch (Exception e) {
					System.out.println("XXXXXXXXXXXXXXXXXX ERROR XXXXXXXXXXXXXXXXXXXXX");
					System.out.println("Não foi possível atualizar o AIT.");
				}
			}
			System.out.println("Total de AITs: " + rsm.size());
			customConnection.closeConnection();
		}
		finally {
			customConnection.finishConnection();
		}
	}
	
	public static void alterarTipoLoteImpressaoBaseAntiga(Integer a) {
		final int NAI_ENVIADO_BASE_ANTIGA = 0;
		final int NIP_ENVIADA_BASE_ANTIGA = 1;
		CustomConnection customConnection = null;
		System.out.println("----------------------------------- INICIADO ---------------------------------------");
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			
			PreparedStatement pst = customConnection.getConnection().prepareStatement(
					" SELECT *  FROM mob_lote_impressao "
							+ " WHERE tp_documento = " + NAI_ENVIADO_BASE_ANTIGA + " OR tp_documento = " + NIP_ENVIADA_BASE_ANTIGA ) ;
			System.out.println("SQL : " + pst);
			ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
			System.out.println("Resultado consulta quantidade: " + rsm.getLines().size());
			while (rsm.next()) {
				try {
					LoteImpressao loteImpressao = LoteImpressaoDAO.get(rsm.getInt("cd_lote_impressao"));
					
					System.out.println("<----------- LOTE IMPRESSÃO: ----------->");
					System.out.println("CD Lote Impressão: " + loteImpressao.getCdLoteImpressao() + " ...TP Documento: " + loteImpressao.getTpDocumento() );
					
			        int tpDocumento = rsm.getInt("tp_documento");
			        if (tpDocumento == NAI_ENVIADO_BASE_ANTIGA) {
			            loteImpressao.setTpDocumento(TipoStatusEnum.NAI_ENVIADO.getKey());
			        }
			        if (tpDocumento == NIP_ENVIADA_BASE_ANTIGA) {
			            loteImpressao.setTpDocumento(TipoStatusEnum.NIP_ENVIADA.getKey());
			        }		        
			        LoteImpressaoDAO.update(loteImpressao, customConnection.getConnection());
			        System.out.println("<----------- LOTE IMPRESSÃO ATUALIZADO: ----------->");
					System.out.println("CD Lote Impressão: " + loteImpressao.getCdLoteImpressao() + " ...TP Documento: " + loteImpressao.getTpDocumento() );
					
				}
				catch (Exception e) {
					System.out.println("XXXXXXXXXXXXXXXXXX ERROR XXXXXXXXXXXXXXXXXXXXX");
					System.out.println("Não foi possível atualizar o tipo de lote.");
				}
			}
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
