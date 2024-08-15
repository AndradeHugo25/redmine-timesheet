# TIMESHEET AUTOMATION

## Projeto

Este projeto foi criado com o obejtivo de automatizar o lançamento de
horas no redmine.
<br />Ao executar o programa, o procedimento se dará da seguinte forma:

### Suas ações:

1. Preencher os dados iniciais
2. Observar a execução do programa
3. Conferir o log gerado

### Ações do sistema:
1. Exibirá a janela para capturar os dados iniciais
2. Realizará uma validação inicial dos dados da planilha
3. Inicializará o google chrome 
4. Lançará as horas no redmine
5. Fechará o google chrome, no fim do último lançamento
6. Atualizará o campo SIT da planilha para as linhas que forem lançadas com sucesso
   <br />(facilitando a identificação das linhas com problemas)
7. Exibirá uma mensagem com o resultado
8. Gerará um arquivo logTimesheet.txt com os detalhes na mesma 
   pasta da planilha

## Instruções

- Execute o programa
- Preencha os dados requisitados 
- Clique em Executar
- Verifique o log gerado
  
❗ **ATENÇÃO**

- Para linux, utilize o jar disponibilizado
- Para windows, utilize o exe disponibilizado
- É necessário que a planilha de timesheet esteja em algum diretório do
  seu computador.
- A planilha deve seguir o formato atual utilizado pelos colaboradores da empresa.
- O sistema só lançará as horas da primeira aba da planilha.  
- Caso você não possua a planilha base, baixar em: 
  - https://docs.google.com/spreadsheets/d/1wjjv7WFjeZ8lToe8GqBTXOG40N-txgsnXFPHf8hQTPY/edit#gid=1503438772
- As regras para preenchimento da planilha são as mesmas do preenchimento que já fazemos manualmente:

|CAMPO|FORMATO|REGRAS|
| ------ | ------ | ------ | 
|DATA|Texto: dd/MM/yyyy|Obrigatório; Data de execução da tarefa|
|PROJETO|Texto: mesmo nome do projeto no redmine|Obrigatório; Símbolos e números não necessários|		
|INI|Hora: HH:mm|Obrigatório; Horário de início da tarefa|
|FIM|Hora: HH:mm|Obrigatório; Horário de fim da tarefa|
|ATIVIDADE|mesmo nome da atividade no redmine|Obrigatório|
|REDMINE|Texto: número da tarefa no redmine|Opcional|
|OBSERVAÇÃO|Texto: livre|Opcional|
|DIFF|Hora: HH:mm|**Não preencher**; Campo automaticamente preenchido pela planilha|
|SIT|Texto: vazio|**Não preencher**; Campo automaticamente preenchido pela automação|

## Considerações

Caso haja algum erro, enviar email para: _qualidade@riocardmais.com.br_


