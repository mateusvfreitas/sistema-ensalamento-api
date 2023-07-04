// novo modelo, novo data set
// gabriel kuhnen brylkowski

tuple t_sala
{
	string nome;
	int capacidade;
}

tuple t_disciplina
{
	string codigo;
	int dia;
	int horario_inicial;
	int horario_final;
}

tuple t_sala_disciplina
{
	string nome_sala;
	string codigo_disciplina;
}

tuple t_sala_atributo
{
	string nome_sala;
	string atributo;
}

tuple t_disciplina_atributo
{
	string codigo_disciplina;
	string atributo;
}

tuple t_alocacao
{
	string codigo_disciplina;
	int dia;
	int horario_inicial;
	int horario_final;
	string nome_sala;
}

tuple t_alteracao
{
  	string codigo_disciplina;
  	string horario;
  	string nome_sala_orig;
  	string nome_sala_nova;
}

{t_sala} l_salas = ...;
{string} l_atributos = ...;
{string} l_salas_minimo_uso = ...;
{string} l_salas_exclusivas = ...;
{t_sala_atributo} l_salas_atributos = ...;

sorted {t_disciplina} l_disciplinas = ...;
{t_sala_disciplina} l_salas_especificas = ...;
{t_disciplina_atributo} l_disciplinas_atributos= ...;

int v_requisitos[l_disciplinas];
int m_candidatos[l_disciplinas][l_salas];

int v_exclusiva[l_salas];
int v_minimo_uso[l_salas];

{t_alteracao} l_alteracoes;

range slots = 1..17;
range dias = 2..7;
string nome_slot[slots];

{t_alocacao} l_alocacao_atual = ...;
int m_alocacao_atual[l_disciplinas][l_salas];

dvar int+ relax_alocacao[l_disciplinas];
dvar int+ relax_exclusiva[l_disciplinas][l_salas];
dvar int+ ocupacao_sala[l_salas][dias][slots] in 0..1;
dvar int+ taxa_ocupacao[l_salas];
dvar int+ m_alocacao[l_disciplinas][l_salas] in 0..1;
dvar int+ trocou_sala[l_disciplinas] in 0..1;
dvar int+ relax_mesma_sala[l_disciplinas];

execute
{
	for(var slot in slots)
	{
		var letra, numero;
		
		numero = (slot-1)%6 + 1;
		
		if(slot > 12)
		{
			letra = "N";	  	
		}
		else if(slot < 7)
		{
			letra = "M"	  
		}
		else
		{
			letra = "T"; 
		}
		
		nome_slot[slot] = ""+letra+numero;
	}
	
	for(var aloc in l_alocacao_atual)
	{
		for(var disc in l_disciplinas)
		{
			if((disc.codigo != aloc.codigo_disciplina) || (disc.dia != aloc.dia) || (disc.horario_inicial != aloc.horario_inicial) || (disc.horario_final != aloc.horario_final))
			{
				continue;
			}

			for(var sala in l_salas)
			{
				if(sala.nome != aloc.nome_sala)
				{
					continue;
				}

				m_alocacao_atual[disc][sala] = 1;
			}
		}
	}
	
	for(var sala in l_salas)
	{
	  	v_minimo_uso[sala] = 0;
	  	
	  	for(var smu in l_salas_minimo_uso)
	  	{
	  	  	if(smu == sala.nome)
	  	  	{
	  	  	  	v_minimo_uso[sala] = 1;
	  	  	}
	  	}
	}
	
	for(var sala in l_salas)
	{
	  	v_exclusiva[sala] = 0;
	  	
	  	for(var xclu in l_salas_exclusivas)
	  	{
	  	  	if(xclu == sala.nome)
	  	  	{
	  	  	  	v_exclusiva[sala] = 1;
	  	  	}
	  	}
	}
	
	for(var disc in l_disciplinas)
	{
	  	for (var sala in l_salas)
	  	{
	  	  	var i_disc = 0;
	  	  	m_candidatos[disc][sala] = 0;
	  	  	
	  	  	for(var disc_atr in l_disciplinas_atributos)
	  	  	{
	  	  	  	if(disc_atr.codigo_disciplina == disc.codigo)
	  	  	  	{
	  	  	  	  	i_disc++;
	  	  	  	}
	  	  	  	
	  	  	  	for (var sala_atr in l_salas_atributos)
	  	  	  	{
	  	  	  	  	if((disc_atr.codigo_disciplina != disc.codigo) || (sala_atr.nome_sala != sala.nome) || (disc_atr.atributo != sala_atr.atributo))
	  	  	  	  	{
	  	  	  	  	  	continue;
	  	  	  	  	}
	  	  	  	  	m_candidatos[disc][sala]++;
	  	  	  	}
        	}
        	
        	v_requisitos[disc] = i_disc;
     		
     		if(v_requisitos[disc] <= m_candidatos[disc][sala])
     		{
     		  	
     		  	m_candidatos[disc][sala] = 1;
     		}
     		else
     		{
     		  	m_candidatos[disc][sala] = 0;
     		}
     		
     		for(var spcf in l_salas_especificas)
     		{
     		  	if(spcf.codigo_disciplina != disc.codigo)
	  	  	  	{
	  	  	  	  	continue;
	  	  	  	}
	  	  	  	else if((spcf.codigo_disciplina == disc.codigo) && (spcf.nome_sala != sala.nome))
	  	  	  	{
	  	  	  	 	m_candidatos[disc][sala] = 0;
	  	  	  	 	continue;
	  	  	  	}
	  	  	  	m_candidatos[disc][sala] = 1;
     		}
	  	}
	}
}

minimize
	0

	+ 0.05 * sum(sala in l_salas)
		(v_minimo_uso[sala] * taxa_ocupacao[sala])

	+ 100 * sum(disc in l_disciplinas, sala in l_salas)
		(v_exclusiva[sala] * relax_exclusiva[disc][sala])

	+ 1000 * sum(disc in l_disciplinas)
		relax_alocacao[disc]

	+ 1 * sum(disc in l_disciplinas)
		trocou_sala[disc]

	+ 5 * sum(disc in l_disciplinas)
		relax_mesma_sala[disc]
;

subject to
{
	forall(disc in l_disciplinas)
		sum(sala in l_salas)
			(m_alocacao[disc][sala]) == 1 - relax_alocacao[disc];
	
	forall(disc in l_disciplinas)
		sum(sala in l_salas)
			(m_candidatos[disc][sala] * m_alocacao[disc][sala]) >= 1 - relax_alocacao[disc];
				
	forall(disc in l_disciplinas)
	  	sum(sala in l_salas)
	  	  	(1 - m_candidatos[disc][sala]) * m_alocacao[disc][sala] == 0; 

	forall(disc in l_disciplinas)
		sum(sala in l_salas)
		  	m_alocacao[disc][sala] <= 1;

	forall(sala in l_salas, disc1 in l_disciplinas, disc2 in l_disciplinas : (disc1 != disc2) && (disc1.dia == disc2.dia) && (disc1.horario_inicial <= disc2.horario_final) && (disc1.horario_final >= disc2.horario_inicial))
			m_alocacao[disc1][sala] + m_alocacao[disc2][sala] <= 1;

	forall(disc in l_disciplinas, sala in l_salas : m_alocacao_atual[disc][sala] == 0)
		trocou_sala[disc] >= m_alocacao[disc][sala];

	forall(disc1 in l_disciplinas, disc2 in l_disciplinas, sala in l_salas : (disc1.codigo == disc2.codigo) && (disc1.dia != disc2.dia))
		m_alocacao[disc1][sala] <= m_alocacao[disc2][sala] + relax_mesma_sala[disc1];

	forall(disc in l_disciplinas, sala in l_salas)
		v_exclusiva[sala] * (m_alocacao[disc][sala] - relax_exclusiva[disc][sala]) <= 0;

    forall(sala in l_salas, dia in dias, slot in slots)
        ocupacao_sala[sala][dia][slot] == sum(disc1 in l_disciplinas : (disc1.dia == dia) && (slot >= disc1.horario_inicial) && (slot <= disc1.horario_final)) m_alocacao[disc1][sala];

    forall(sala in l_salas)
        taxa_ocupacao[sala] == sum(dia in dias, slot in slots) ocupacao_sala[sala][dia][slot];
}

execute
{
	writeln("\n\nalteracoes: ");
	for(var disc in l_disciplinas)
	{
	  	var troca = 0, sala_nova = "-", sala_original = "-";
	  	for(var sala in l_salas)
	  	{
	  	  	if(m_alocacao[disc][sala] != m_alocacao_atual[disc][sala])
	  	  	{
	  	  	  	troca = 1;
	  	  	  	if(m_alocacao[disc][sala] == 1)
	  	  	  	{
	  	  	  	  	sala_nova = sala.nome;
	  	  	  	}
	  	  	  	if(m_alocacao[disc][sala] == 0)
	  	  	  	{
	  	  	  	  	sala_original = sala.nome;
	  	  	  	}
	  	  	}
	  	}
	  	
	  	if(troca)
	  	{
	  	  	var horario = "";
	  	  	for(i = disc.horario_inicial; i <= disc.horario_final; i++)
	  	  	{
	  	  	  	horario += "" + disc.dia + nome_slot[i];

				if(i < disc.horario_final)
				{
					horario += "-";
				}
			}

			writeln(disc.codigo + ": " + horario + " (" + sala_original + ") -> " + sala_nova);
			
			l_alteracoes.add(disc.codigo, horario, sala_original, sala_nova);
	  	}
	}

	writeln("\n\ndisciplinas nao alocadas: ");
    for(var disc in l_disciplinas)
    {
        var soma_alocacao = 0, sala_nova = "-", sala_original = "-";

        for(var sala in l_salas)
        {
            soma_alocacao = soma_alocacao + m_alocacao[disc][sala];
        }

        if (soma_alocacao == 0)
        {
              var horario = "";
                for(i = disc.horario_inicial; i <= disc.horario_final; i++)
                {
                      horario += "" + disc.dia + nome_slot[i];

                if(i < disc.horario_final)
                {
                    horario += "-";
                }
            }
              l_alteracoes.add(disc.codigo, horario, sala_original, sala_nova);
            writeln(disc.codigo);
        }
    }
	
	var filePath = "alteracoes.json";
	var file = new IloOplOutputFile(filePath);
	file.writeln("[ ");
	var i = 0;
	for(var alt in l_alteracoes)
	{
		file.write("\t{\"codigo\": \"" + alt.codigo_disciplina + "\", \"horario\": \"" + alt.horario + "\", \"salaOriginal\": \"" + alt.nome_sala_orig + "\", \"salaNova\": \"" + alt.nome_sala_nova + "\"}");
		i++;
		if (i < l_alteracoes.size)
		{
			file.write(",\n");
		}
	}
	file.writeln();
	file.write("]");
	file.close();
}
