package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.ConcessaoVeiculo;
import com.tivic.manager.mob.ConcessaoVeiculoDAO;
import com.tivic.manager.mob.Linha;
import com.tivic.manager.mob.LinhaDAO;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class HorarioAfericaoServices {
	
	

	public static Result save(HorarioAfericao horarioAfericao){
		return save(horarioAfericao, null, null);
	}

	public static Result save(HorarioAfericao horarioAfericao, AuthData authData){
		return save(horarioAfericao, authData, null);
	}
	
	public static Result sync(ArrayList<HorarioAfericao> horarioAfericao) {
		return sync(horarioAfericao, null);
	}
	
	public static Result sync(ArrayList<HorarioAfericao> horarioAfericoes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			ArrayList<HorarioAfericao> horarioAfericoesRetorno = new ArrayList<HorarioAfericao>();
			ArrayList<HorarioAfericao> horarioAfericoesDuplicadas = new ArrayList<HorarioAfericao>();
			ArrayList<HorarioAfericao> horarioAfericoesErro = new ArrayList<HorarioAfericao>();
			
			int retorno = 0;
			for (HorarioAfericao horarioAfericao: horarioAfericoes) {
				
				Result r = sync(horarioAfericao, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					horarioAfericoesErro.add(horarioAfericao);
					continue;
				}
				else if(r.getCode()==2) {
					horarioAfericoesDuplicadas.add(horarioAfericao);
					continue;
				}
				else {
					horarioAfericao.setCdControle(r.getCode());
					horarioAfericoesRetorno.add(horarioAfericao);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (horarioAfericoes.size() == horarioAfericoesRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar Horario de Aferições.");			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar RRDs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(HorarioAfericao horarioAfericao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo Horario Afericao...");
			System.out.println("\tAgente: "+com.tivic.manager.mob.AgenteDAO.get(horarioAfericao.getCdAgente(), connect).getNmAgente());
			System.out.println("\tLinha: ["+LinhaDAO.get(horarioAfericao.getCdLinha()).getNrLinha()+"]");
			System.out.println("\tRota: ["+LinhaRotaDAO.get(horarioAfericao.getCdLinha(),horarioAfericao.getCdRota()).getNmRota()+"]");
			if (horarioAfericao.getCdConcessaoVeiculo()>0)
				System.out.println("\tPrefixo: ["+ConcessaoVeiculoDAO.get(horarioAfericao.getCdConcessaoVeiculo()).getNrPrefixo()+"]");
			
			int retorno = 0;
			
			String sqlVerificaDuplicaidade;
			
			sqlVerificaDuplicaidade = "SELECT * FROM MOB_HORARIO_AFERICAO WHERE " +
										"dt_lancamento = '" + Util.convCalendarStringSqlCompleto(horarioAfericao.getDtLancamento()) + "'" +
										(horarioAfericao.getHrChegada() == null ? " AND hr_chegada is null" : " AND hr_chegada = '" + Util.convCalendarStringSqlCompleto(horarioAfericao.getHrChegada()) + "'") +
										(horarioAfericao.getHrPartida() == null ? " AND hr_partida is null" : " AND hr_partida = '" + Util.convCalendarStringSqlCompleto(horarioAfericao.getHrPartida()) + "'") +
										" AND cd_horario =" + horarioAfericao.getCdHorario() +
										" AND cd_tabela_horario =" + horarioAfericao.getCdTabelaHorario() +
										" AND cd_linha =" + horarioAfericao.getCdLinha() +
										" AND cd_rota =" + horarioAfericao.getCdRota() +
										" AND cd_agente =" + horarioAfericao.getCdAgente() +
										" AND cd_concessao_veiculo =" + horarioAfericao.getCdConcessaoVeiculo();
			
			ResultSet rs = connect.prepareStatement(sqlVerificaDuplicaidade).executeQuery();
			
				
			if(rs.next()) {
				retorno = 2;
				System.out.println("Diagnostico: Horario Afericao Duplicada...");
			}
			else {
				retorno = insert(horarioAfericao, connect);
				
				if(retorno > 0) {
					System.out.println("Diagnostico: Horario Afericao Recebida...");
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar Horario Aferições.");
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar Horario Afericoes ");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(HorarioAfericao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			int code = 1;
			ResultSet rs = connect.prepareStatement(lgBaseAntiga ? "SELECT MAX(cd_controle) as maxCode FROM mob_horario_afericao" : 
				"SELECT MAX(cd_controle) as maxCode FROM mob_horario_afericao").executeQuery();
			if(rs.next())
				code = rs.getInt("maxCode") + 1;
			objeto.setCdControle(code);
			
			String sql = "INSERT INTO mob_horario_afericao (cd_controle,"+
						 "dt_lancamento,"+
						 "hr_chegada,"+
						 "hr_partida,"+
						 "hr_previsto,"+
						 "cd_horario,"+
						 "cd_tabela_horario,"+
						 "cd_linha,"+
						 "cd_rota,"+
						 "cd_trecho,"+
						 "cd_agente,"+
						 "cd_concessao_veiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			if(lgBaseAntiga) {
				sql =  "INSERT INTO mob_horario_afericao (cd_controle,"+
						 "dt_lancamento,"+
						 "hr_chegada,"+
						 "hr_partida,"+
						 "hr_previsto,"+
						 "cd_horario,"+
						 "cd_tabela_horario,"+
						 "cd_linha,"+
						 "cd_rota,"+
						 "cd_trecho,"+
						 "cd_agente,"+
						 "cd_concessao_veiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			}
						
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, code);
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			if(objeto.getHrPartida()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrPrevisto()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrPrevisto().getTimeInMillis()));
			if(objeto.getCdHorario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdHorario());
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTabelaHorario());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLinha());
			if(objeto.getCdRota()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRota());
			if(objeto.getCdTrecho()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTrecho());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAgente());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdConcessaoVeiculo());
		
			code = pstmt.executeUpdate();							
						
			if(code<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return code;
				
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(HorarioAfericao horarioAfericao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(horarioAfericao==null)
				return new Result(-1, "Erro ao salvar. HorarioAfericao é nulo");

			int retorno;
			if(horarioAfericao.getCdControle()==0){
				retorno = HorarioAfericaoDAO.insert(horarioAfericao, connect);
				horarioAfericao.setCdControle(retorno);
			}
			else {
				retorno = HorarioAfericaoDAO.update(horarioAfericao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "HORARIOAFERICAO", horarioAfericao);
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
	public static Result remove(HorarioAfericao horarioAfericao) {
		return remove(horarioAfericao.getCdControle());
	}
	public static Result remove(int cdControle){
		return remove(cdControle, false, null, null);
	}
	public static Result remove(int cdControle, boolean cascade){
		return remove(cdControle, cascade, null, null);
	}
	public static Result remove(int cdControle, boolean cascade, AuthData authData){
		return remove(cdControle, cascade, authData, null);
	}
	public static Result remove(int cdControle, boolean cascade, AuthData authData, Connection connect){
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
			retorno = HorarioAfericaoDAO.delete(cdControle, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_horario_afericao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoServices.getAll: " + e);
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
		int qtRegistros = 0;
		String orderBy = "";	
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
				orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtRegistros = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}
		 
		return Search.find("SELECT A.*, B.nr_prefixo, C.nm_agente, G.nm_pessoa as nm_concessionario, E.nr_linha, F.nm_rota, D.cd_concessionario, H.* FROM mob_horario_afericao A " +
				"LEFT OUTER JOIN mob_concessao_veiculo B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) " +
				"LEFT OUTER JOIN mob_linha E ON (A.cd_linha = E.cd_linha) " +
				"LEFT OUTER JOIN mob_linha_rota F ON (A.cd_linha = F.cd_linha AND A.cd_rota = F.cd_rota) " +
				"LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente) " +
				"LEFT OUTER JOIN mob_concessao D ON (B.cd_concessao = D.cd_concessao) " +
				"LEFT OUTER JOIN grl_pessoa G ON (D.cd_concessionario = G.cd_pessoa)" +
				"LEFT OUTER JOIN grl_empresa H ON (H.id_empresa = C.nm_agente )", (orderBy != "" ? orderBy : " ORDER BY A.DT_LANCAMENTO ASC ") + (qtRegistros > 0? " LIMIT " + qtRegistros : ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static List<HorarioAfericaoDTO> findDTO(ArrayList<ItemComparator> criterios) throws Exception {
		return findDTO(criterios, null);
	}

	public static List<HorarioAfericaoDTO> findDTO(ArrayList<ItemComparator> criterios, Connection connect) {

		ResultSetMap rsm = find(criterios);

		List<HorarioAfericaoDTO> list = new ArrayList<HorarioAfericaoDTO>();

		while (rsm.next()) {
			HorarioAfericaoDTO dto = new HorarioAfericaoDTO();

			HorarioAfericao horarioAfericao = HorarioAfericaoDAO.get(rsm.getInt("CD_CONTROLE"));
			dto.setHorarioAfericao(horarioAfericao);

			Pessoa concessionario = PessoaDAO.get(rsm.getInt("CD_CONCESSIONARIO"));
			dto.setConcessionario(concessionario);

			Agente agente = AgenteDAO.get(rsm.getInt("CD_AGENTE"));
			dto.setAgente(agente);

			Linha linha = LinhaDAO.get(rsm.getInt("CD_LINHA"));
			dto.setLinha(linha);

			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo"));
			dto.setConcessaoVeiculo(concessaoVeiculo);

			Empresa empresa = EmpresaDAO.get(rsm.getInt("CD_EMPRESA"));
			dto.setEmpresa(empresa);
			list.add(dto);

		}
		return list;
	}
	
	
	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<HorarioAfericao> horarioAfericao) {
		return getSyncData(horarioAfericao, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<HorarioAfericao> horarioAfericao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(horarioAfericao != null && horarioAfericao.size() > 0) {
				for(HorarioAfericao hA : horarioAfericao) {
					
					
					hA.setCdControle(0);
					Result save = save(hA, null, connect);
					
					if(save.getCode() <= 0) {
						throw new Exception("Não foi possível completar a sincronização");
					}
				}
				
				connect.commit();
			}
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("HorarioAfericao", new ArrayList());
						
			return register;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}