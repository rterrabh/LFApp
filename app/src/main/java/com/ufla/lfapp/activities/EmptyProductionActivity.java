package com.ufla.lfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.vo.AcademicSupport;
import com.ufla.lfapp.vo.Grammar;
import com.ufla.lfapp.vo.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by root on 21/07/16.
 */
public class EmptyProductionActivity extends HeaderGrammarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_empty_productions);
        super.onCreate(savedInstanceState);
        removingEmptyProductions(new Grammar(grammar));
    }

    @Override
    protected  Grammar getGrammar() {
        switch(algorithm) {
            case CHOMSKY_NORMAL_FORM:
            case GREIBACH_NORMAL_FORM:
                return new Grammar(grammar)
                        .getGrammarWithInitialSymbolNotRecursive(new Grammar
                                (grammar), new AcademicSupport());
            default: return super.getGrammar();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.next:
                Bundle params = new Bundle();
                params.putString("grammar", grammar);
                params.putString("word", word);
                params.putInt("algorithm", algorithm.getValue());
                Intent intent = new Intent(this, ChainRulesActivity.class);
                intent.putExtras(params);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Método que junta duas gramáticas em um String
     * @param grammar1
     * @param grammar2
     * @return
     */
    public String joinGrammars(final Grammar grammar1, final Grammar grammar2) {
        StringBuilder newG = new StringBuilder();
        for (Rule element : grammar1.getRules(grammar1.getInitialSymbol())) {
            newG.append(element).append("\n");
        }
        for (Rule element : grammar2.getRules()) {
            if (element.getLeftSide().equals(grammar1.getInitialSymbol()) && !grammar1.getRules().contains(element)) {
                newG.append(element);
                newG.append("\n");
            }
        }

        for (Rule element : grammar1.getRules()) {
            if (!element.getLeftSide().equals(grammar1.getInitialSymbol())) {
                newG.append(element);
                newG.append("\n");
            }
        }
        for (Rule element : grammar2.getRules()) {
            if (!element.getLeftSide().equals(grammar1.getInitialSymbol()) && !grammar1.getRules().contains(element)) {
                newG.append(element);
                newG.append("\n");
            }
        }
        return newG.toString();
    }

    /**
     * Método que imprime a gramática com as novas regras inseridas em destaque
     * @param table
     * @param academic
     */
    private void printGrammarWithNewRules(final Grammar grammar, TableLayout table,
                                          final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        ArrayList<String> orderVariables = new ArrayList<>(grammar.getVariables());
        Collections.sort(orderVariables);
        int contViews = 2;
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getInsertedRules().contains(element)) {
                    setSubscriptItem(right, element.getRightSide());
                    right.setTextColor(getResources().getColor(R.color.Blue));
                } else if (containsDigit(element.getRightSide())) {
                    setSubscriptItem(right, element.getRightSide());
                } else {
                    right.append(element.getRightSide());
                }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (int i = 0; i < orderVariables.size(); i++) {
            for (String variable : grammar.getVariables()) {
                if (!variable.equals(grammar.getInitialSymbol()) && orderVariables.get(i).equals(variable)) {
                    TableRow row1 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    setSubscriptItem(tv0, variable);
                    row1.addView(tv0);
                    TextView arrow1 = new TextView(this);
                    arrow1.setText("->");
                    row1.addView(arrow1);
                    contViews = 2;
                    for (Rule element : grammar.getRules()) {
                        if (variable.equals(element.getLeftSide())) {
                            TextView pipe = new TextView(this);
                            pipe.setText(" | ");
                            TextView tv1 = new TextView(this);
                            if (academic.getInsertedRules().contains(element)) {
                                setSubscriptItem(tv1, element.getRightSide());
                                tv1.setTextColor(getResources().getColor(R.color.Blue));
                            } else if (containsDigit(element.getRightSide())) {
                                setSubscriptItem(tv1, element.getRightSide());
                            } else {
                                tv1.append(element.getRightSide());
                            }
                            row1.addView(tv1);
                            row1.addView(pipe);
                            contViews += 2;
                        }
                    }
                    row1.removeViewAt(contViews - 1);
                    table.addView(row1);
                }
            }
        }
    }

    /**
     * Método que imprime a gramática com as regras irregulares em destaque
     * @param table
     * @param academic
     */
    private void printGrammarWithoutOldRules(final Grammar grammar, TableLayout table,
                                             final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        ArrayList<String> variables = new ArrayList<>(grammar.getVariables());
        Collections.sort(variables);
        int contViews = 2;
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getIrregularRules().contains(element)) {
                    setSubscriptItem(right, element.getRightSide());
                    right.setTextColor(getResources().getColor(R.color.Red));
                } else if (containsDigit(element.getRightSide())) {
                    setSubscriptItem(right, element.getRightSide());
                }else {
                    right.setText(element.getRightSide());
                }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (int i = 0; i < variables.size(); i++) {
            for (String variable : grammar.getVariables()) {
                if (!variable.equals(grammar.getInitialSymbol()) && variables.get(i).equals(variable)) {
                    TableRow row1 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    setSubscriptItem(tv0, variable);
                    row1.addView(tv0);
                    TextView arrow1 = new TextView(this);
                    arrow1.setText("->");
                    row1.addView(arrow1);
                    contViews = 2;
                    for (Rule element : grammar.getRules()) {
                        if (variable.equals(element.getLeftSide())) {
                            TextView pipe = new TextView(this);
                            pipe.setText(" | ");
                            TextView tv1 = new TextView(this);
                            if (academic.getIrregularRules().contains(element)) {
                                setSubscriptItem(tv1, element.getRightSide());
                                tv1.setTextColor(getResources().getColor(R.color.Red));
                            } else if(containsDigit(element.getRightSide())) {
                                setSubscriptItem(tv1, element.getRightSide());
                            } else {
                                tv1.setText(element.getRightSide());
                            }
                            row1.addView(tv1);
                            row1.addView(pipe);
                            contViews += 2;
                        }
                    }
                    row1.removeViewAt(contViews - 1);
                    table.addView(row1);
                }
            }
        }
    }

    private boolean containsDigit(String sentence) {
        boolean test = false;
        for (int i = 0; i < sentence.length() && !test; i++) {
            if (Character.isDigit(sentence.charAt(i))) {
                test = true;
            }
        }
        return test;
    }

    /**
     * Método que imprime a tabela dos conjuntos utilizados durante a execução do algoritmo
     * @param academic : informaçãos coletadas durante a execução do algoritmo
     */
    private void printTableOfSets(TableLayout table, final AcademicSupport academic,
                                  String nameOfFirstSet, String nameOfSecondSet) {
        int i = 0;
        TableRow header = new TableRow(this);
        TextView htv0 = new TextView(this);
        htv0.setText("");
        htv0.setPadding(10, 10, 10, 10);
        htv0.setTextColor(getResources().getColor(R.color.Black));
        TextView htv1 = new TextView(this);
        htv1.setText(nameOfFirstSet);
        htv1.setPadding(10, 10, 10, 10);
        htv1.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        htv1.setTextColor(getResources().getColor(R.color.Black));
        TextView htv2 = new TextView(this);
        htv2.setText(nameOfSecondSet);
        htv2.setPadding(10, 10, 10, 10);
        htv2.setTextColor(getResources().getColor(R.color.Black));
        header.addView(htv0);
        header.addView(htv1);
        header.addView(htv2);
        table.addView(header);

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();
        while (iteratorOfFirstCell.hasNext() && iteratorOfSecondCell.hasNext()) {
            Set<Rule> firstSet = iteratorOfFirstCell.next();
            Set<Rule> secondSet = iteratorOfSecondCell.next();

            String set1 = firstSet.toString();
            set1 = set1.replace("[", "{");
            set1 = set1.replace("]", "}");

            String set2 = secondSet.toString();
            set2 = set2.replace("[", "{");
            set2 = set2.replace("]", "}");

            TextView tv0 = new TextView(this);
            tv0.setText("(" + i + ")");
            tv0.setPadding(10, 10, 10, 10);
            tv0.setTextColor(getResources().getColor(R.color.Black));

            TextView tv1 = new TextView(this);
            tv1.setText(set1);
            tv1.setPadding(10, 10, 10, 10);
            tv1.setBackgroundColor(getResources().getColor(R.color.DarkGray));
            tv1.setTextColor(getResources().getColor(R.color.Black));

            TextView tv2 = new TextView(this);
            tv2.setText(set2);
            tv2.setPadding(10, 10, 10, 10);
            tv2.setTextColor(getResources().getColor(R.color.Black));

            TableRow row = new TableRow(this);
            row.addView(tv0);
            row.addView(tv1);
            row.addView(tv2);

            table.addView(row);
            i++;
        }
    }

    /**
     * Método que retorna o pseudocódigo do algoritmo nullable
     * @return
     */
    public String getNullableAlgorith() {
        StringBuilder algol = new StringBuilder();
        algol.append("NULL = {A | {A -> λ} ∈ P}<br>");
        algol.append("<b>repita</b><br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;PREV = NULL<br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;<b>para cada</b> A ∈ V <b>faça</b><br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>se</b> A → w e w ∈ PREV<sup>∗</sup> <b>faça</b><br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NULL = NULL ∪ {A}<br>");
        algol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NULL = NULL ∪ {A}<br>");
        algol.append("<b>até</b> NULL == PREV");
        return algol.toString();
    }

    /**
     * Método que conta o número de dígitos a partir do índice fornecido
     * @param index
     * @param element
     * @return
     */
    private int getNumberOfDigits(int index, String element) {
        int i = index;
        for (; i < element.length() && Character.isDigit(element.charAt(i)); i++);
        //i = (i == element.length() ? (i - 1) : (i));
        return i;
    }

    private void setSubscriptItem(TextView tv, String element) {
        for (int i = 0; i < element.length();) {
            if (Character.isDigit(element.charAt(i))) {
                int index = getNumberOfDigits(i, element);
                tv.append(Html.fromHtml("<sub><small>" + element.substring(i, index) + "</small></sub>"));
                i = index;
            } else {
                StringBuilder text = new StringBuilder();
                text.append(element.charAt(i));
                tv.append(text.toString());
                i++;
            }
        }
    }

    /**
     * Método que realiza a remoção de produções vazias e acrescenta as informações acadêmicas.
     * @param g : Gramática
     */
    public void removingEmptyProductions(final Grammar g) {
        AcademicSupport academicSupport = new AcademicSupport();
        StringBuilder academicInfoComments = new StringBuilder();

        //Configura comentários sobre o processo
        String align = "justify";
        academicInfoComments.append(Html.fromHtml("<p align=" + align +
                ">O algoritmo para remoção de regras λ consiste em 3 passos:"));
        academicSupport.setComments(academicInfoComments.toString());

        //Realiza processo
        Grammar gc = g.getGrammarEssentiallyNoncontracting(g, academicSupport);
        academicSupport.setResult(gc);

        //configura a gramática de resultado
        TextView grammarResult = (TextView) findViewById(R.id.ResultGrammarEmptyProductions);
        assert grammarResult != null;
        grammarResult.setText(Html.fromHtml(academicSupport.getResult()));

        if (academicSupport.getSituation()) {
            //Configura os comentários sobre a solução
            TextView result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText(academicSupport.getComments());

            //Configuração do passo 1 (Tabela dos conjuntos)
            TextView explanation1 = (TextView) findViewById(R.id.ExplanationEmptyRules1);
            explanation1.setText(Html.fromHtml("(1) Determinar o conjunto das variáveis anuláveis.</p>"));
            TextView pseudo =(TextView) findViewById(R.id.PseudoNullableAlgorith);
            assert pseudo != null;
            pseudo.setText(Html.fromHtml(getNullableAlgorith()));
            TableLayout tableOfSets = (TableLayout) findViewById(R.id.TableOfSets);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfSets(tableOfSets, academicSupport, "NULL", "PREV");

            //Configuração do passo 2 (Gramática com as regras criadas no processo)
            if (academicSupport.getInsertedRules().size() != 0) {
                TextView explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText("(2) Adicionar regras em que as ocorrências de variáveis nulas são omitidas. Por exemplo, assuma a regra A -> BABa e B" +
                        " é uma variável anulável. Logo, são inseridas as seguintes regras: A -> ABa, A -> BAa e A -> Aa.");
                TableLayout grammarWithNewRules = (TableLayout) findViewById(R.id.AddingRulesTable);
                Grammar blueGrammar = new Grammar(joinGrammars(gc, g));
                printGrammarWithNewRules(blueGrammar, grammarWithNewRules, academicSupport);
            } else {
                TextView explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText("(2) Não há regras a serem inseridas.");
            }

            //Configuração do passo 3 (Gramática com as regras irregulares removida)
            if (academicSupport.getIrregularRules().size() != 0) {
                TextView explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText("(3) Remover as regras λ.\n" +
                        "OBS: se símbolo inicial produz λ, não remover esta regra.");
                TableLayout grammarWithoutOldRules = (TableLayout) findViewById(R.id.RemovingRulesTable);
                Grammar redGrammar = new Grammar(joinGrammars(gc, g));
                printGrammarWithoutOldRules(redGrammar, grammarWithoutOldRules, academicSupport);
            } else {
                TextView explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText("(3) Não há regras a serem removidas.");
            }

        } else {
            TextView result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText("A gramática inserida não possui produções vazias.");
        }
    }

}