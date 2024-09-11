# MédiasAPI

Durante a disciplina de SGBD na universidade, a média final é calculada de uma maneira um pouco trabalhosa, isso pela quantidade de atividades
semanais mais algumas provas aplicadas ao longo de todo o semestre, onde 25% da piores notas eram descartadas. Diante disso, resolvi então 
escrever um algoritmo que automatizasse esse cálculo e armazenasse as notas atribuídas conforme as avaliações eram aplicadas.
Bom, com meus estudos de API's decidi então desenvolver uma sobre esse assunto, com o objetivo de também escalar para novas features, nesse 
caso adicionar mais plano de ensinos à API sobre futuras matérias a serem cursadas. 

Calculo da Disciplina de Sistemas Gerenciadores de Banco de Dados definada pelo plano de ensino: 
- NP = (prova1 + prova2 )/2;
- NA = Media aritmética das 75% melhores notas das atividades semanais que podem chegar até 16 atividades;
- MF = 0,6 * NP + NA * 0,4

- todas as provas e atividades limitadas em 10 pontos.
