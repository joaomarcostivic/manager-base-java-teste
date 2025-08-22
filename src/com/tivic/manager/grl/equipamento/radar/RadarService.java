package com.tivic.manager.grl.equipamento.radar;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.EquipamentoUsuario;
import com.tivic.manager.grl.equipamento.base.EquipamentoServiceBase;
import com.tivic.manager.grl.equipamento.base.EquipamentoServiceBaseFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RadarService {

	EquipamentoServiceBase equipamentoServiceBase;
	
	public RadarService() throws Exception {
		equipamentoServiceBase = EquipamentoServiceBaseFactory.build();
	}
	
	public List<EquipamentoUsuario> getAllRadarFtp() throws Exception{
		return getAllRadarFtp(new CustomConnection());
	}

	public List<EquipamentoUsuario> getAllRadarFtp(CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.tp_equipamento", EquipamentoServices.RADAR_FIXO, true);
			searchCriterios.addCriteriosEqualInteger("A.lg_sync_ftp", 1, true);
			List<EquipamentoUsuario> equipamentos = equipamentoServiceBase.find(searchCriterios, customConnection);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum radar ftp encontrado");
			customConnection.finishConnection();
			return equipamentos;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	public List<EquipamentoUsuario> getAllRadarEstatico() throws Exception{
		return getAllRadarEstatico(new CustomConnection());
	}

	public List<EquipamentoUsuario> getAllRadarEstatico(CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.tp_equipamento", EquipamentoServices.RADAR_ESTATICO, true);
			searchCriterios.addCriteriosEqualInteger("A.st_equipamento", EquipamentoServices.ATIVO, true);
			List<EquipamentoUsuario> equipamentos = equipamentoServiceBase.find(searchCriterios, customConnection);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum radar estático encontrado");
			customConnection.finishConnection();
			return equipamentos;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	public List<EquipamentoUsuario> getAllRadarFixo() throws Exception{
		return getAllRadarFixo(new CustomConnection());
	}

	public List<EquipamentoUsuario> getAllRadarFixo(CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.tp_equipamento", EquipamentoServices.RADAR_FIXO, true);
			searchCriterios.addCriteriosEqualInteger("A.st_equipamento", EquipamentoServices.ATIVO, true);
			List<EquipamentoUsuario> equipamentos = equipamentoServiceBase.find(searchCriterios, customConnection);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum radar fixo encontrado");
			customConnection.finishConnection();
			return equipamentos;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	public List<InfoRadar> getInfoRadar(int tpEquipamento) throws Exception{
		return getInfoRadar(tpEquipamento, new CustomConnection());
	}

	public List<InfoRadar> getInfoRadar(int tpEquipamento, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("B.st_equipamento", EquipamentoServices.ATIVO, true);
			searchCriterios.addCriteriosEqualInteger("B.tp_equipamento", tpEquipamento, true);
			Search<InfoRadar> search = new SearchBuilder<InfoRadar>("mob_evento_equipamento")
					.fields("A.cd_equipamento, B.id_equipamento, B.nm_equipamento, B.ds_local, " + 
							"				C.id_orgao, D.nm_cidade, " + 
							"				MAX(A.dt_evento) AS dt_ultimo_evento, " + 
							"				round(((EXTRACT(EPOCH FROM TIMESTAMP '\"+ Util.convCalendarStringSqlCompleto(new GregorianCalendar()) +\"')) - EXTRACT(EPOCH FROM MAX(A.dt_evento)))/60/60) as qt_horas")
					.addJoinTable("JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) ")
					.addJoinTable("JOIN mob_orgao C ON (B.cd_orgao = C.cd_orgao) ")
					.addJoinTable("JOIN grl_cidade D ON (C.cd_cidade = D.cd_cidade) ")
					.searchCriterios(searchCriterios)
					.groupBy("BY A.cd_equipamento, B.cd_equipamento, C.cd_orgao, D.cd_cidade")
					.orderBy("id_equipamento, dt_ultimo_evento")
					.build();
			List<InfoRadar> infoRadares = search.getList(InfoRadar.class); 
			if(infoRadares.isEmpty())
				throw new NoContentException("Nenhum radar encontrado");
			customConnection.finishConnection();
			return infoRadares;
		} finally {
			customConnection.closeConnection();
		}
		
	}

}
