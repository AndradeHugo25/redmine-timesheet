package redmine.util;

import com.riocard.ferramentas.data.DataEHora;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import redmine.model.Atividade;
import redmine.model.Linha;
import redmine.model.Msg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Data
public class ManipuladorExcel {

    private int errosPorLinha;
    private RedmineLog log;
    private int totalErros;
    private ArrayList<String> msgsErros = new ArrayList<String>();

    private ArrayList<Linha> linhasTimeSheet = new ArrayList<Linha>();
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String pathPlanilha;

    public enum Coluna {
        I, A, B, C, D, E, F, G, H
    }

    public enum NomeCampo {
        data, projeto, atividade, tarefa, observacao, horas, situacao
    }

    public enum Extensao {
        xls, xlsx
    }

    public ManipuladorExcel(String pathPlanilha, RedmineLog log) {
        this.log = log;
        this.pathPlanilha = pathPlanilha;
    }

    public void armazenarLinhasPlanilhaTimeSheet() throws IOException {
        log.escrever(Msg.INICIOLEITURAPLANILHA.getValor());

        Extensao extensaoAtual = getExtensaoPlanilha(pathPlanilha);

        FileInputStream input = new FileInputStream(pathPlanilha);

        HSSFWorkbook workbookXls;
        HSSFSheet sheetXls = null;

        XSSFWorkbook workbookXlsx;
        XSSFSheet sheetXlsx = null;

        FormulaEvaluator formulaEvaluator = null;

        switch (extensaoAtual) {
            case xls:
                workbookXls = new HSSFWorkbook(input);
                formulaEvaluator = new HSSFFormulaEvaluator(workbookXls);
                sheetXls = workbookXls.getSheetAt(0);
                break;
            case xlsx:
                ZipSecureFile.setMinInflateRatio(0.0);
                workbookXlsx = new XSSFWorkbook(input);
                formulaEvaluator = new XSSFFormulaEvaluator(workbookXlsx);
                sheetXlsx = workbookXlsx.getSheetAt(0);
                break;
        }

        DataFormatter dataFormatter = new DataFormatter();
        totalErros = 0;
        boolean hasNext = true;
        int numLinha = 1;

        while (hasNext) {
            if (numLinha != 1) {
                Linha novaLinha = new Linha();
                novaLinha.setNumLinha(numLinha);

                errosPorLinha = 0;
                for (Coluna coluna : Coluna.values()) {
                    String referencia = coluna.name() + numLinha;
                    CellReference cellReference = new CellReference(referencia);
                    Row row = null;
                    switch (extensaoAtual) {
                        case xls:
                            row = sheetXls.getRow(cellReference.getRow());
                            break;
                        case xlsx:
                            row = sheetXlsx.getRow(cellReference.getRow());
                            break;
                    }
                    Cell cell;
                    try {
                        cell = row.getCell(cellReference.getCol());

                        String txtCelula = dataFormatter.formatCellValue(cell, formulaEvaluator);
                        if (coluna.name().equals("I")) {
                            if (txtCelula.equals("lançado")) {
                                novaLinha.setSituacao(txtCelula);
                                break;
                            }
                        }

                        switch (coluna) {
                            case A:
                                Date date = cell.getDateCellValue();
                                if (!verificarErrosData(date, numLinha)) {
                                    String dataFormatada = dateFormat.format(date);
                                    novaLinha.setData(dataFormatada);
                                }
                                break;
                            case B:
                                if (!verificarErrosGenericos(txtCelula, numLinha, NomeCampo.projeto)) {
                                    novaLinha.setProjeto(txtCelula);
                                }
                                break;
                            case E:
                                if (!verificarErrosAtividade(txtCelula, numLinha)) {
                                    novaLinha.setAtividade(txtCelula);
                                }
                                break;
                            case F:
                                novaLinha.setTarefa(txtCelula);
                                break;
                            case G:
                                novaLinha.setObservacao(txtCelula);
                                break;
                            case H:
                                if (!verificarErrosGenericos(txtCelula, numLinha, NomeCampo.horas)) {
                                    novaLinha.setHoras(txtCelula);
                                }
                                break;
                        }
                    } catch (Exception e) {
                        log.escreverMensagemExcecao(e.getMessage());
                    }
                }

                if (novaLinha.getSituacao() == null) {
                    if (verificarFimArquivo(novaLinha)) {
                        removerErrosUltimaLinha(novaLinha);
                        log.escrever(Msg.FIMPROCESSAMENTO.getValor());
                        hasNext = false;
                    } else if (errosPorLinha == 0) {
                        linhasTimeSheet.add(novaLinha);
                    }
                }

            }
            numLinha++;
        }

        input.close();
    }

    private boolean verificarFimArquivo(Linha linha) {
        int nulosOuVazios = 0;
        for (String aux : linha.getCamposObrigatorios()) {
            if (aux == null || aux.equals("")) {
                nulosOuVazios++;
            }
        }
        return nulosOuVazios == linha.getCamposObrigatorios().size();
    }

    private void removerErrosUltimaLinha(Linha linha) {
        totalErros -= linha.getCamposObrigatorios().size();

        int numUltimaLinha = linha.getNumLinha();
        ArrayList<String> msgsErrosCopia = new ArrayList<String>(msgsErros);
        for (String msg : msgsErrosCopia) {
            if (msg.contains(String.valueOf(numUltimaLinha))) {
                msgsErros.remove(msg);
            }
        }
    }

    public Extensao getExtensaoPlanilha(String pathCompletoPlanilha) {
        int posUltimoPonto = pathCompletoPlanilha.lastIndexOf('.');
        String extensaoUsuario = pathCompletoPlanilha.substring(posUltimoPonto + 1);
        for (Extensao ext : Extensao.values()) {
            if (extensaoUsuario.equals(ext.name())) {
                return ext;
            }
        }
        return null;
    }

    private boolean verificarErrosGenericos(String campo, int linha, NomeCampo nomeCampo) {
        if (campo == null || campo.equals("")) {
            if (!nomeCampo.equals(NomeCampo.data)) {
                msgsErros.add("> Linha " + linha + "-" + Msg.CAMPONULO.getValor() + " Campo: " + nomeCampo);
            }
            errosPorLinha++;
            totalErros++;
            return true;
        }
        return false;
    }

    private boolean verificarErrosData(Date data, int linha) {
        try {
            String dataFormatada = dateFormat.format(data);
            if (verificarErrosGenericos(dataFormatada, linha, NomeCampo.data)) {
                return true;
            } else {
                String dataAtual = DataEHora.getDataAtualFormatada("dd/MM/yyyy");
                if (DataEHora.compareToParaDatas(dataFormatada, dataAtual) == 1) {
                    msgsErros.add("> Linha " + linha + "-" + Msg.DATAMAIORQUEATUAL.getValor());
                    errosPorLinha++;
                    totalErros++;
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            msgsErros.add("> Linha " + linha + "-" + Msg.DATAINVALIDA.getValor());
            errosPorLinha++;
            totalErros++;
            return true;
        }
    }

    private boolean verificarErrosAtividade(String atividade, int linha) {
        if (verificarErrosGenericos(atividade, linha, NomeCampo.atividade)) {
            return true;
        } else {
            if (!Atividade.contem(atividade)) {
                msgsErros.add("> Linha " + linha + "-" + Msg.ATIVIDADEINVALIDA.getValor());
                errosPorLinha += 1;
                totalErros++;
                return true;
            }
            return false;
        }
    }

    public void confirmarLancamento(Linha linha) throws IOException {
        Extensao extensaoAtual = getExtensaoPlanilha(pathPlanilha);

        FileInputStream input = new FileInputStream(pathPlanilha);

        HSSFWorkbook workbookXls = null;
        HSSFSheet sheetXls = null;

        XSSFWorkbook workbookXlsx = null;
        XSSFSheet sheetXlsx = null;

        switch (extensaoAtual) {
            case xls:
                workbookXls = new HSSFWorkbook(input);
                sheetXls = workbookXls.getSheetAt(0);
                break;
            case xlsx:
                ZipSecureFile.setMinInflateRatio(0.0);
                workbookXlsx = new XSSFWorkbook(input);
                sheetXlsx = workbookXlsx.getSheetAt(0);
                break;
        }

        String referencia = Coluna.I + String.valueOf(linha.getNumLinha());
        CellReference cellReference = new CellReference(referencia);
        Row row = null;
        switch (extensaoAtual) {
            case xls:
                row = sheetXls.getRow(cellReference.getRow());
                break;
            case xlsx:
                row = sheetXlsx.getRow(cellReference.getRow());
                break;
        }

        Cell cell = row.getCell(cellReference.getCol());
        cell.setCellValue("lançado");

        input.close();

        FileOutputStream outFile = new FileOutputStream(new File(pathPlanilha));
        switch (extensaoAtual) {
            case xls:
                workbookXls.write(outFile);
            case xlsx:
                workbookXlsx.write(outFile);
        }

        outFile.close();
    }

}
