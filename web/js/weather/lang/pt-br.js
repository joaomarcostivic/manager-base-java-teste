var translate = function(key, arrayKey) {
	var lang = {
		0 : 'Tornado',
		1 : 'Tempestade Tropical',
		2 : 'Furacão',
		3 : 'Tempestades Severas',
		4 : 'Tempestades',
		5 : 'Chuva com Neve',
		6 : 'Chuva com Granizo',
		7 : 'Chuva com Neve e Granizo',
		8 : 'Geada',
		9 : 'Garoa',
		10 : 'Geada',
		11 : 'Chuva',
		12 : 'Chuva',
		13 : 'Flocos de Neve',
		14 : 'Neve Calma',
		15 : 'Nevasca',
		16 : 'Neve',
		17 : 'Granizo',
		18 : 'Granizo',
		19 : 'Poeira',
		20 : 'Neblina',
		21 : 'Nevoeiro',
		22 : 'Nevoeiro',
		23 : 'Tempestuoso',
		24 : 'Ventoso',
		25 : 'Frio',
		26 : 'Nublado',
		27 : 'Maior parte nublado',
		28 : 'Maior parte nublado',
		29 : 'Parcialmente nublado',
		30 : 'Parcialmente nublado',
		31 : 'Céu Limpo',
		32 : 'Ensolarado',
		33 : 'Tempo Limpo',
		34 : 'Tempo Limpo',
		35 : 'Misto de Chuva e Granizo',
		36 : 'Quente',
		37 : 'Tempestades Isoladas',
		38 : 'Tempestades Intermitentes',
		39 : 'Tempestades Intermitentes',
		40 : 'Chuvas Intermitentes',
		41 : 'Bastante Neve',
		42 : 'Neve',
		43 : 'Bastante Neve',
		44 : 'Parcialmente Nublado',
		45 : 'Tempestade',
		46 : 'Neve',
		47 : 'Chuvas Isoladas',
		3200 : 'Não Disponível',
		'locations' : {
			'Brazil' : 'Brasil'
		},
		'days' : {
			'Mon' : 'Segunda-feira',
			'Tue' : 'Terça-feira',
			'Wed' : 'Quarta-feira',
			'Thu' : 'Quinta-feira',
			'Fri' : 'Sexta-feira',
			'Sat' : 'Sábado',
			'Sun' : 'Domingo'
		}
	}
	if(arrayKey == null){
		return lang[key];	
	} else {
		return lang[arrayKey][key];
	}
}
