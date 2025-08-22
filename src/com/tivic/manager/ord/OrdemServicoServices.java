package com.tivic.manager.ord;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.ptc.TipoDocumentoServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

public class OrdemServicoServices {

	public static Result save(OrdemServico ordemServico){
		return save(ordemServico, null, null, null);
	}
	
	public static Result save(OrdemServico ordemServico, ArrayList<OrdemServicoItem> itens){
		return save(ordemServico, itens, null, null);
	}
	
	public static Result save(OrdemServico ordemServico, ArrayList<OrdemServicoItem> itens, Laudo laudo){
		return save(ordemServico, itens, laudo, null);
	}
	
	public static Result save(OrdemServico ordemServico, ArrayList<OrdemServicoItem> itens, Laudo laudo, 
			ArrayList<OrdemServicoTecnico> tecnicos) {
		return save(ordemServico, itens, laudo, tecnicos, null);
	}

	public static Result save(OrdemServico ordemServico, ArrayList<OrdemServicoItem> itens, Laudo laudo, 
			ArrayList<OrdemServicoTecnico> tecnicos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ordemServico==null)
				return new Result(-1, "Erro ao salvar. OrdemServico é nulo");
			
			Result result = null;
			int retorno;
			
			//numeracao
			if((ordemServico.getNrOrdemServico()==null || ordemServico.getNrOrdemServico().equals(""))) { 
				int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_ORDEM_SERVICO", 0, 0, connect);
				
				String nrOrdemServico = TipoDocumentoServices.getNextNumeracao(cdTipoDocumento, ordemServico.getCdEmpresa(), true, connect);
				ordemServico.setNrOrdemServico(nrOrdemServico);
			}
			
			if(ordemServico.getCdOrdemServico()==0){
				retorno = OrdemServicoDAO.insert(ordemServico, connect);
				ordemServico.setCdOrdemServico(retorno);
			}
			else {
				retorno = OrdemServicoDAO.update(ordemServico, connect);
			}
			
			if(retorno<=0) {
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao salvar Ordem de Serviço");
			}
			
			// ITENS
			if(itens!=null) {
				for (OrdemServicoItem item : itens) {
					item.setCdOrdemServico(ordemServico.getCdOrdemServico());
					
					result = OrdemServicoItemServices.save(item, connect);
					if(result.getCode()<=0) {
						Conexao.rollback(connect);
						return result;
					}
					
					item = (OrdemServicoItem)result.getObjects().get("ORDEMSERVICOITEM");
				}
			}
			
			// LAUDO
			if(laudo!=null) {
				laudo.setCdOrdemServico(ordemServico.getCdOrdemServico());

				result = LaudoServices.save(laudo, connect);
				if(result.getCode()<=0) {
					Conexao.rollback(connect);
					return result;
				}
				
				laudo = (Laudo)result.getObjects().get("LAUDO");
			}
			
			//TECNICOS
			if(tecnicos!=null) {
				result = OrdemServicoTecnicoServices.removeAll(ordemServico.getCdOrdemServico(), connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				
				for (OrdemServicoTecnico tecnico : tecnicos) {
					tecnico.setCdOrdemServico(ordemServico.getCdOrdemServico());
					
					result = OrdemServicoTecnicoServices.save(tecnico, null, connect);
					if(result.getCode()<=0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
					
					tecnico = (OrdemServicoTecnico)result.getObjects().get("ORDEMSERVICOTECNICO");
				}
			}
			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORDEMSERVICO", ordemServico);
			result.addObject("ITENS", itens);
			result.addObject("LAUDO", laudo);
			result.addObject("TECNICOS", tecnicos);

			return result;
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
	public static Result remove(int cdOrdemServico){
		return remove(cdOrdemServico, false, null);
	}
	public static Result remove(int cdOrdemServico, boolean cascade){
		return remove(cdOrdemServico, cascade, null);
	}
	public static Result remove(int cdOrdemServico, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				//TODO: capturar error
				connect.prepareStatement("DELETE FROM ord_ordem_servico_item WHERE cd_ordem_servico = "+cdOrdemServico).execute();
				connect.prepareStatement("DELETE FROM ord_laudo WHERE cd_ordem_servico = "+cdOrdemServico).execute();
				connect.prepareStatement("DELETE FROM ord_ordem_servico_tecnico WHERE cd_ordem_servico = "+cdOrdemServico).execute();
				
				OrdemServicoArquivoServices.remove(cdOrdemServico);
				
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = OrdemServicoDAO.delete(cdOrdemServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_ordem_servico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		return getAllByDocumento(cdDocumento, null);
	}

	public static ResultSetMap getAllByDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_DOCUMENTO", Integer.toString(cdDocumento), ItemComparator.EQUAL, Types.INTEGER));
			
			return find(criterios, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.getAllByDocumento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllTecnicos(int cdOrdemServico) {
		return getAllTecnicos(cdOrdemServico, null);
	}

	public static ResultSetMap getAllTecnicos(int cdOrdemServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_ordem_servico", Integer.toString(cdOrdemServico), ItemComparator.EQUAL, Types.VARCHAR));
			
			return OrdemServicoTecnicoServices.find(criterios, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.getAllTecnicos: " + e);
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
		
		int nrRegistros = 0;
		int tpLocalizacao = -1;
		int tpGrupo = -1;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
				nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("TP_LOCALIZACAO")) {
				tpLocalizacao = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("tpGrupo")) {
				tpGrupo = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("dtInicial")) {
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("dtFinal")) {
				criterios.remove(i);
				i--;
			}
		}
		
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
		//considera apenas um item por OS e o laudo pertence a os
		String sql = " SELECT " + sqlLimit[0]
					+ " DISTINCT(A.cd_ordem_servico), "
					+ " A.*,"
					+ " B1.nm_tipo_atendimento, "
					+ " C.nm_pessoa, "
					+ "		C1.tp_localizacao, "
					+ " D.nm_situacao_servico, "
					+ " E.nm_modalidade, "
					+ " F.*, "
					+ " 	F1.nm_pessoa as nm_empresa, F1.nr_telefone1 as nr_telefone1_empresa, "
					+ "		F1.nr_telefone2 as nr_telefone2_empresa, F1.nr_celular as nr_celular_empresa, "
					+ "		F2.tp_localizacao, "
					//+ " G.nm_pessoa as nm_tecnico_responsavel,"
					+ " H.*, H1.nm_tipo_documento, H2.nm_setor as nm_setor_documento, "
					//+ " I.*, "
					//+ " J.*, "
					+ " K.*,"
					+ " 	L1.nm_pessoa as nm_usuario,"
					+ " M.nm_setor,"
					+ " N.nm_pessoa AS nm_fornecedor "
					+ " FROM ord_ordem_servico A"
					+ " LEFT OUTER JOIN ord_tipo_atendimento B ON (A.cd_tipo_atendimento=B.cd_tipo_atendimento)"
					+ " LEFT OUTER JOIN crm_tipo_atendimento B1 ON (B.cd_tipo_atendimento=B1.cd_tipo_atendimento)"
					+ " LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa=C.cd_pessoa)"
					+ " LEFT OUTER JOIN acd_instituicao_educacenso C1 ON (C.cd_pessoa=C1.cd_instituicao)"
					+ " LEFT OUTER JOIN ord_situacao_servico D ON (A.cd_situacao_servico=D.cd_situacao_servico)"
					+ " LEFT OUTER JOIN ord_modalidade E ON (A.cd_modalidade=E.cd_modalidade)"
					+ " LEFT OUTER JOIN grl_empresa F ON (A.cd_empresa=F.cd_empresa)"
					+ " LEFT OUTER JOIN grl_pessoa F1 ON (F.cd_empresa=F1.cd_pessoa)"
					+ " LEFT OUTER JOIN acd_instituicao_educacenso F2 ON (F.cd_empresa=F2.cd_instituicao)"
					+ " LEFT OUTER JOIN acd_instituicao_periodo F3 ON (C1.cd_instituicao=F3.cd_instituicao)"
					//+ " LEFT OUTER JOIN grl_pessoa G ON (A.cd_tecnico_responsavel=G.cd_pessoa)"
					+ " LEFT OUTER JOIN ptc_documento H ON (A.cd_documento=H.cd_documento)"
					+ " LEFT OUTER JOIN gpn_tipo_documento H1 ON (H.cd_tipo_documento=H1.cd_tipo_documento)"
					+ " LEFT OUTER JOIN grl_setor H2 ON (H.cd_setor=H2.cd_setor)"
					//+ " LEFT OUTER JOIN ord_ordem_servico_item I ON (A.cd_ordem_servico=I.cd_ordem_servico)"
					//+ " LEFT OUTER JOIN ord_tipo_produto_servico J ON (I.cd_tipo_produto_servico=J.cd_tipo_produto_servico)"
					+ " LEFT OUTER JOIN ord_laudo K ON (A.cd_ordem_servico = K.cd_ordem_servico)"
					+ " LEFT OUTER JOIN seg_usuario L ON (A.cd_usuario=L.cd_usuario)"
					+ " LEFT OUTER JOIN grl_pessoa L1 ON (L.cd_pessoa=L1.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_setor M ON (A.cd_setor=M.cd_setor)"
					+ " LEFT OUTER JOIN grl_pessoa N ON (A.cd_fornecedor=N.cd_pessoa)"
					+ " WHERE F3.st_periodo_letivo=0 AND C1.cd_periodo_letivo = F3.cd_periodo_letivo ";
		
		if(tpLocalizacao>=0) {
			sql += " AND (C1.tp_localizacao="+tpLocalizacao+" OR F2.tp_localizacao="+tpLocalizacao+")";
		}

		ResultSetMap rsm = Search.find(sql, " ORDER BY A.dt_entrada, C.nm_pessoa, K.dt_laudo "+sqlLimit[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
//		ResultSetMap rsm = Search.findAndLog(sql, " ORDER BY C.nm_pessoa, A.dt_entrada "+sqlLimit[1], criterios, connect, connect==null);
		
		LogUtils.debug(sql);
		
		while(rsm.next()) {
			rsm.setValueToField("RSMITENS", OrdemServicoItemServices.getAllByOrdemServico(rsm.getInt("cd_ordem_servico"), connect));
			ResultSetMap rsmTecnicos = getAllTecnicos(rsm.getInt("cd_ordem_servico"), connect);
			rsm.setValueToField("RSMTECNICOS", rsmTecnicos);
			rsm.setValueToField("nm_tecnico_responsavel", Util.join(rsmTecnicos, "nm_pessoa"));
			
			GregorianCalendar dtEntrada = rsm.getGregorianCalendar("dt_entrada");
			rsm.setValueToField("ds_mes", sol.util.Util.formatDateTime(dtEntrada, "MMM/yyyy"));
			rsm.setValueToField("nr_mes", sol.util.Util.formatDateTime(dtEntrada, "MM/yyyy"));
			
		}
		rsm.beforeFirst();
		
		return rsm;
	}
	
	public static ResultSetMap getAllItens(int cdOrdemServico) {
		return getAllItens(cdOrdemServico, 0, null);
	}

	public static ResultSetMap getAllItens(int cdOrdemServico, int cdProdutoServico) {
		return getAllItens(cdOrdemServico, cdProdutoServico, null);
	}

	public static ResultSetMap getAllItens(int cdOrdemServico, int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, "
											+ " B.nm_tipo_produto_servico, "
											+ " C.nm_pessoa as nm_tecnico_responsavel "
											+ " FROM ord_ordem_servico_item A"
											+ " LEFT OUTER JOIN ord_tipo_produto_servico B ON (A.cd_tipo_produto_servico=B.cd_tipo_produto_servico)"
											+ " LEFT OUTER JOIN grl_pessoa C ON (A.cd_tecnico_responsavel=C.cd_pessoa)"											 
											+ " WHERE A.cd_ordem_servico = ? " +
											 (cdProdutoServico > 0 ? " AND A.cd_produto_servico = " + cdProdutoServico : ""));
			pstmt.setInt(1, cdOrdemServico);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getArquivos(int cdOrdemServico) {
		return getArquivos(cdOrdemServico,null);
	}

	public static ResultSetMap getArquivos(int cdOrdemServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, "
											+ " B.nm_arquivo, dt_arquivamento, nm_documento, cd_tipo_arquivo,"
											+ " cd_usuario, st_arquivo "
											+ " FROM ord_ordem_servico_arquivo A"
											+ " JOIN grl_arquivo B ON (A.cd_arquivo=B.cd_arquivo)"
											+ " WHERE A.cd_ordem_servico = ? ");
			pstmt.setInt(1, cdOrdemServico);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.getArquivos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Relatorio: OS quantitativo
	 * 
	 * @param criterios critérios de busca
	 * @return 
	 */
	public static ResultSetMap findOrdemServicoQuantitativo(ArrayList<ItemComparator> criterios) {
		return findOrdemServicoQuantitativo(criterios, null);
	}

	public static ResultSetMap findOrdemServicoQuantitativo(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			/*
			 * 0 - tecnico
			 * 1 - solicitante
			 * 2 - localidade
			 * 3 - tipo atendimento
			 */
			int tpGrupo = -1;
			int nrRegistros = 0;
			String dtInicial = null;
			String dtFinal = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpGrupo")) {
					tpGrupo = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtInicial")) {
					dtInicial = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtFinal")) {
					dtFinal = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (tpGrupo==3 && criterios.get(i).getColumn().equalsIgnoreCase("A.dt_entrada")) {
					criterios.remove(i);
					i--;
				}
			}
			ResultSetMap rsm = new ResultSetMap();
			PreparedStatement pstmt = null;
			
			/*
			 * TECNICO
			 */
			if(tpGrupo==0) { 
				/*
				 * LISTA DE TÉCNICOS
				 * pessoas ativas com vínculo 'TÉCNICO'
				 */
				int cdEmpresa = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connect);
				if(cdEmpresa<=0) {
					System.err.println("Erro! OrdemServicoServices.findOrdemServicoPorTecnico:\nVínculo CD_INSTITUICAO_SECRETARIA_MUNICIPAL não encontrado.");
					return null;
				}
					
				int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_TECNICO", 0, 0, connect);
				if(cdVinculo<=0) {
					System.err.println("Erro! OrdemServicoServices.findOrdemServicoPorTecnico:\nVínculo CD_VINCULO_TECNICO não encontrado.");
					return null;
				}
				
				ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("cd_empresa", cdEmpresa+"", ItemComparator.EQUAL, Types.INTEGER));
				crt.add(new ItemComparator("cd_vinculo", cdVinculo+"", ItemComparator.EQUAL, Types.INTEGER));
				crt.add(new ItemComparator("A.st_cadastro", PessoaServices.ST_ATIVO+"", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTecnicos = PessoaServices.find(crt, connect);
				while(rsmTecnicos.next()) {
					String sql = "SELECT COUNT(A.*) as qtd "
							+ " FROM ord_ordem_servico A "
							+ " WHERE A.cd_tecnico_responsavel="+rsmTecnicos.getInt("cd_pessoa");
					
					ResultSetMap rsmAux = Search.find(sql, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
					
					HashMap<String, Object> register = new HashMap<>();
					register.put("CD_TECNICO_RESPONSAVEL", rsmTecnicos.getString("cd_pessoa"));
					register.put("NM_TECNICO_RESPONSAVEL", rsmTecnicos.getString("nm_pessoa"));
					register.put("NM_GRUPO", rsmTecnicos.getString("nm_pessoa"));
					if(rsmAux.next()) {
						register.put("QTD_ORDEM_SERVICO", rsmAux.getInt("qtd"));
					}
					
					rsm.addRegister(register);
				}
			}
			/*
			 * SOLICITANTES
			 */
			else if(tpGrupo==1) {
				/*
				 * LISTA DE SOLICITANTES
				 */
				ResultSetMap rsmEmpresas = new ResultSetMap(connect.prepareStatement("select distinct(cd_empresa) from ord_ordem_servico").executeQuery());
				String[] empresas = new String[rsmEmpresas.size()];
				while(rsmEmpresas.next()) {
					if(rsmEmpresas.getObject("cd_empresa")!=null) {
						empresas[rsmEmpresas.getPointer()] = rsmEmpresas.getInt("cd_empresa")+"";
					}
				}
				
				ResultSetMap rsmPessoas = new ResultSetMap(connect.prepareStatement("select distinct(cd_pessoa) from ord_ordem_servico").executeQuery());
				String[] pessoas = new String[rsmPessoas.size()];
				while(rsmPessoas.next()) {
					if(rsmPessoas.getObject("cd_pessoa")!=null) {
						pessoas[rsmPessoas.getPointer()] = rsmPessoas.getInt("cd_pessoa")+"";
					}
				}

				String[] solicitantes = Util.concat(empresas, pessoas, true);				
				
				for (String cdSolicitante : solicitantes) {
					ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();
					crt.add(new ItemComparator("A.cd_pessoa", cdSolicitante, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAux = find(crt, connect);//Search.find(sql, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
					
					HashMap<String, Object> register = new HashMap<>();
					if(rsmAux.next()) {
						register.put("CD_PESSOA", rsmAux.getInt("cd_pessoa"));
						register.put("NM_PESSOA", rsmAux.getString("nm_pessoa"));
						register.put("NM_GRUPO", rsmAux.getString("nm_pessoa"));
						register.put("QTD_ORDEM_SERVICO", rsmAux.getLines().size());
						
						if(rsmAux.getLines().size()>0) {
							rsm.addRegister(register);
						}
					}
					
					
				}
			}
			else if(tpGrupo==2) {
				ResultSetMap rsmTipoAtendimento = new ResultSetMap(connect.prepareStatement("select distinct(cd_tipo_atendimento) from ord_ordem_servico").executeQuery());
				
				while(rsmTipoAtendimento.next()) {
					ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();
					crt.add(new ItemComparator("A.cd_tipo_atendimento", rsmTipoAtendimento.getInt("cd_tipo_atendimento")+"", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAux = find(crt, connect);
					
					HashMap<String, Object> register = new HashMap<>();
					if(rsmAux.next()) {
						register.put("CD_TIPO_ATENDIMENTO", rsmAux.getInt("cd_tipo_atendimento"));
						register.put("NM_TIPO_ATENDIMENTO", rsmAux.getString("nm_tipo_atendimento"));
						register.put("NM_GRUPO", rsmAux.getString("nm_tipo_atendimento"));
						register.put("QTD_ORDEM_SERVICO", rsmAux.getLines().size());
						
						if(rsmAux.getLines().size()>0) {
							rsm.addRegister(register);
						}
					}
				}
			}
			else if(tpGrupo==3) {
				GregorianCalendar dtI = Util.convStringToCalendar(dtInicial, null);
				if(dtI==null)
					dtI = Util.getPrimeiroDiaMes(Calendar.JANUARY, new GregorianCalendar().get(Calendar.YEAR));
				
				GregorianCalendar dtF = Util.convStringToCalendar(dtFinal, null);
				if(dtF==null)
					dtF = Util.getUltimoDiaMes(new GregorianCalendar().get(Calendar.MONTH), new GregorianCalendar().get(Calendar.YEAR));
				
				do {
					GregorianCalendar inicioMes = Util.getPrimeiroDiaMes(dtI.get(Calendar.MONTH), dtI.get(Calendar.YEAR));
					GregorianCalendar fimMes = Util.getUltimoDiaMes(dtI.get(Calendar.MONTH), dtI.get(Calendar.YEAR));
					fimMes.set(Calendar.HOUR, 23); fimMes.set(Calendar.MINUTE, 59); fimMes.set(Calendar.SECOND, 59);
					
					ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();//new ArrayList<>();
					crt.add(new ItemComparator("A.dt_entrada", Util.formatDate(inicioMes, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
					crt.add(new ItemComparator("A.dt_entrada", Util.formatDate(fimMes, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
					
					ResultSetMap rsmAux = find(crt, connect);
					HashMap<String, Object> register = new HashMap<>();
					if(rsmAux.next()) {
						register.put("NR_MES", Util.formatDate(rsmAux.getGregorianCalendar("dt_entrada"), "MM/yyyy"));
						register.put("DS_MES", Util.formatDate(rsmAux.getGregorianCalendar("dt_entrada"), "MMM/yyyy"));
						register.put("NM_GRUPO", Util.formatDate(rsmAux.getGregorianCalendar("dt_entrada"), "MMM/yyyy"));
						register.put("QTD_ORDEM_SERVICO", rsmAux.getLines().size());
						
						if(rsmAux.getLines().size()>0) {
							rsm.addRegister(register);
						}
					}
					
					dtI.add(Calendar.MONTH, 1);
					
				} while(Util.compareDates(dtI, dtF) <= 0);
			}
			
			ArrayList<String> columns = new ArrayList<String>();
			columns.add(tpGrupo==3 ? "NR_MES" : "QTD_ORDEM_SERVICO DESC");
			rsm.orderBy(columns);
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.findOrdemServicoQuantitativo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Quantidade de serviços por instituição
	 * 
	 * Provisório.
	 * 
	 * @param criterios
	 * @return
	 * 
	 * 
	 */
	public static ResultSetMap findQuantidadeSolicitanteServico(ArrayList<ItemComparator> criterios) {
		return findQuantidadeSolicitanteServico(criterios, null);
	}

	public static ResultSetMap findQuantidadeSolicitanteServico(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = new ResultSetMap();
			
			int[] cdAtendimentos = {94, 101, 104, 77, 31, 57, 95, 83, 107, 100, 98, 28, 96, 29, 111, 112, 113};
			String[] nmAtendimentos = {"QT_HIDRAULICO", "QT_PEDREIRO", "QT_MARCENARIA", "QT_VISITA", "QT_ELETRICA", 
									   "QT_SERRALHEIRO", "QT_BOMBA", "QT_TRANSPORTE", "QT_VIDROS", "QT_PINTURA", 
									   "QT_MATERIAL", "QT_REFORMA", "QT_TELHADO", "QT_CAPINAGEM", "QT_EXTINTOR", 
									   "QT_FOGAO", "QT_CARRO_PIPA"};
			
			/*
			 * LISTA DE SOLICITANTES
			 */
			ResultSetMap rsmEmpresas = new ResultSetMap(connect.prepareStatement("select distinct(cd_empresa) from ord_ordem_servico").executeQuery());
			String[] empresas = new String[rsmEmpresas.size()];
			while(rsmEmpresas.next()) 
				if(rsmEmpresas.getObject("cd_empresa")!=null) 
					empresas[rsmEmpresas.getPointer()] = rsmEmpresas.getInt("cd_empresa")+"";
			
			ResultSetMap rsmPessoas = new ResultSetMap(connect.prepareStatement("select distinct(cd_pessoa) from ord_ordem_servico").executeQuery());
			String[] pessoas = new String[rsmPessoas.size()];
			while(rsmPessoas.next()) 
				if(rsmPessoas.getObject("cd_pessoa")!=null) 
					pessoas[rsmPessoas.getPointer()] = rsmPessoas.getInt("cd_pessoa")+"";

			String[] solicitantes = Util.concat(empresas, pessoas, true);	
			
			for (String cdSolicitante : solicitantes) {
				HashMap<String, Object> register = new HashMap<>();
				
				Pessoa pessoa = PessoaDAO.get(Integer.parseInt(cdSolicitante), connect);
				if(pessoa==null)
					continue;
				
				int qtAtendimento = 0;
				for(int i=0; i<cdAtendimentos.length; i++) {
					
					ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();
					crt.add(new ItemComparator("A.cd_tipo_atendimento", cdAtendimentos[i]+"", ItemComparator.EQUAL, Types.INTEGER));
					crt.add(new ItemComparator("A.cd_pessoa", cdSolicitante, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAux = find(crt, connect);
					
					register.put(nmAtendimentos[i], rsmAux.getLines().size());
					
					qtAtendimento += rsmAux.getLines().size();
				}
				
				register.put("QT_TOTAL", qtAtendimento);
				register.put("NM_PESSOA", pessoa.getNmPessoa());
				
				if(qtAtendimento>0)
					rsm.addRegister(register);
			} 
			rsm.beforeFirst();
			
			ArrayList<String> columns = new ArrayList<String>();
			columns.add("NM_PESSOA");
			rsm.orderBy(columns);
						
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.findQuantidadeSolicitanteServico: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAgrupadoNF(ArrayList<ItemComparator> criterios) {
		return findAgrupadoNF(criterios, null);
	}

	public static ResultSetMap findAgrupadoNF(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			String nrNotaFiscal = null;
			int cdFornecedor = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.nr_nota_fiscal")) {
					nrNotaFiscal = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_fornecedor")) {
					cdFornecedor = Integer.parseInt(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
			}
			
			
			ResultSetMap rsm = new ResultSetMap();
			
			//busca as NF existentes nas OS
			ResultSetMap rsmNF = new ResultSetMap(connect.prepareStatement(
					"SELECT COUNT(*) AS qt_os, nr_nota_fiscal "
					+ " FROM ord_ordem_servico "
					+ " WHERE 1=1"
					+ " AND nr_nota_fiscal IS NOT NULL AND nr_nota_fiscal <> \'\'"
					+ (nrNotaFiscal!=null ? " AND nr_nota_fiscal LIKE \'"+nrNotaFiscal + "\'" : "")
					+ (cdFornecedor>0 ? " AND cd_fornecedor="+cdFornecedor : "") 
					+ " GROUP BY nr_nota_fiscal"
					+ " ORDER BY nr_nota_fiscal DESC").executeQuery());
			
			/*
			 * Um registro por NF.
			 * As informações das OS de cada NF são inseridas como segue:
			 */			
			while(rsmNF.next()) {
				HashMap<String, Object> register = new HashMap<>();
				register.put("NR_NOTA_FISCAL", rsmNF.getString("nr_nota_fiscal"));
				register.put("QT_OS", rsmNF.getInt("qt_os"));
				
				ArrayList<ItemComparator> crtAux = (ArrayList<ItemComparator>)criterios.clone();
				
				crtAux.add(new ItemComparator("A.NR_NOTA_FISCAL", rsmNF.getString("nr_nota_fiscal"), ItemComparator.EQUAL, Types.VARCHAR));
				ResultSetMap rsmOS = find(crtAux, connect);
				
				if(rsmOS.next()) {
					register.put("CD_FORNECEDOR", rsmOS.getString("cd_fornecedor"));
					register.put("NM_FORNECEDOR", rsmOS.getString("nm_fornecedor"));
					
					register.put("DT_ENTRADA", rsmOS.getGregorianCalendar("dt_entrada"));
					register.put("DS_DT_ENTRADA", Util.formatDate(rsmOS.getGregorianCalendar("dt_entrada"), "dd/MM/yyyy"));
				}
				rsmOS.beforeFirst();
				
				String dsEscolas = "";
				String dsItens = "";
				Double vlTotal = new Double(0);
				while(rsmOS.next()) {
					
					vlTotal += rsmOS.getDouble("vl_ordem_servico");
					
					dsEscolas += rsmOS.getString("nm_empresa")!=null ? rsmOS.getString("nm_empresa") : "";
					if(rsmOS.hasMore() && rsmOS.getString("nm_empresa")!=null)
						dsEscolas += ", ";
					
					dsItens = Util.join((ResultSetMap)rsmOS.getObject("RSMITENS"), "nm_tipo_produto_servico", true);
				}
				register.put("DS_ESCOLAS", dsEscolas);
				register.put("DS_ITENS", dsItens);
				register.put("VL_TOTAL", vlTotal);
				register.put("DS_VL_TOTAL", Util.formatNumber(vlTotal, 2));
				
				rsm.addRegister(register);
			}
						
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemServicoServices.findAgrupadoNF: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
