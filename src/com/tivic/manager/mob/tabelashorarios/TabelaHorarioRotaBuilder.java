package com.tivic.manager.mob.tabelashorarios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.tivic.manager.mob.TabelaHorarioRota;

import sol.dao.ResultSetMap;

public class TabelaHorarioRotaBuilder extends TabelaHorarioRota {

	public List<TabelaHorarioRotaDTO> builder(ResultSetMap rsm){
		List<TabelaHorarioRotaDTO> listTabelaHorarioRotaDTO = new ArrayList<TabelaHorarioRotaDTO>();
		rsm.beforeFirst();
		
		while(rsm.next()) {
			TabelaHorarioRotaDTO tabelaHorarioRotaDTO = new TabelaHorarioRotaDTO();
			tabelaHorarioRotaDTO.setNmRota(rsm.getString("NM_ROTA"));
			tabelaHorarioRotaDTO.setTpRota(rsm.getInt("TP_ROTA"));
			tabelaHorarioRotaDTO.setCdLinha(rsm.getInt("CD_LINHA"));
			tabelaHorarioRotaDTO.setCdRota(rsm.getInt("CD_ROTA"));
			tabelaHorarioRotaDTO.setCdTabelaHorario(rsm.getInt("CD_TABELA_HORARIO"));
			tabelaHorarioRotaDTO.setParadas(getParadas((ResultSetMap)rsm.getObject("RSM_PARADAS")));
			tabelaHorarioRotaDTO.setHorarios(getHorarios((ResultSetMap)rsm.getObject("RSM_HORARIOS"), tabelaHorarioRotaDTO.getParadas()));
			listTabelaHorarioRotaDTO.add(tabelaHorarioRotaDTO);
		}
		
		return listTabelaHorarioRotaDTO;
	}

	private List<ParadaDTO> getParadas(ResultSetMap rsmParadas) {
		ArrayList<ParadaDTO> listParadas = new ArrayList<ParadaDTO>();
		while(rsmParadas.next()) {
			ParadaDTO paradaDto = new ParadaDTO();
			paradaDto.setCdTrecho(rsmParadas.getInt("CD_TRECHO"));
			paradaDto.setNmGrupoParadaSuperior(rsmParadas.getString("NM_GRUPO_PARADA_SUPERIOR"));
			paradaDto.setCdTrechoAnterior(rsmParadas.getInt("CD_TRECHO_ANTERIOR"));
	
			listParadas.add(paradaDto);
		}
		return listParadas;
	}
	
	private List<List<HorarioDTO>> getHorarios(ResultSetMap rsmHorarios, List<ParadaDTO> paradas) {
		List<List<HorarioDTO>> listHorarios = new ArrayList<List<HorarioDTO>>();
		rsmHorarios.beforeFirst();
		while(rsmHorarios.next()){
			
			List<HorarioDTO> horarios = new ArrayList<HorarioDTO>();
			for(ParadaDTO parada : paradas) {
				HorarioDTO horarioDTO = new HorarioDTO();
				horarioDTO.setCdHorario(rsmHorarios.getInt("CD_HORARIO"));
				horarioDTO.setHrTrecho(rsmHorarios.getString("HR_TRECHO_"+parada.getCdTrecho()));
				horarios.add(horarioDTO);
			}
			
			listHorarios.add(horarios);
	    }
		return listHorarios;
	}
	
}
