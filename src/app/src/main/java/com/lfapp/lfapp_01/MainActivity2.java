package com.lfapp.lfapp_01;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.Set;

import vo.*;



public class MainActivity2 extends ActionBarActivity {

    private TableLayout tableGrammarType;
    private TextView titleGrammarType, titleLeftDerivation, titleRightDerivation, titleRemoveInitialSymbolRecursive;
    private TextView titleRemoveEmptyProductions, titleChainRules, titleNoTermSymbols, titleNoReachSymbols, titleFnc, titleRemoveLeftDirectRecursion;
    private TextView titleRemoveLeftRecursion, titleFng, titleCyk;
    private boolean controlGrammarTypeMenu, controlLeftDerivationMenu, controlRightDerivationMenu, controlInitialSymbolRecursiveMenu, controlEmptyProductionsMenu;
    private boolean controlChainRulesMenu, controlNoTermSymbolsMenu, controlNoReachSymbolsMenu, controlFncMenu, controlLeftDirectRecursionMenu, controlLeftRecursionMenu;
    private boolean controlFngMenu, controlCykMenu;
    private LinearLayout layoutGrammarType, layoutDerivationMoreLeft, layoutDerivationMoreRight, layoutInitialSymbolRecursive, layoutEmptyProductions;
    private LinearLayout layoutChainRules, layoutNoTermSymbols, layoutNoReachSymbols, layoutFnc, layoutLeftDirectRecursion;
    private LinearLayout layoutLeftRecursion, layoutFng, layoutCyk;
    private Button button_Voltar;
    private TextView step1_1, step1_2, step2_1, step2_2, step2_3, step3_1, step3_2, step3_3, step4_1, step4_2, step4_3, step5_1, step5_2, step5_3, step7_1;
    private TextView step7_2, step7_3, step8_1, step8_2, step8_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out);

        //chama método que gerencia menu
        acordionMenu();

        String mensagem = new String();

        //pegando Intent para retirar o que foi enviado da tela anterior
        Intent intent = getIntent();
        if (intent != null) {
            Bundle dados = new Bundle();
            dados = intent.getExtras();
            if (dados != null) {
                mensagem = dados.getString("msg");
                Grammar g = new Grammar(mensagem);
                grammarType(g);
                removingInitialRecursiveSymbol(g);
                removingEmptyProductions(g);
                removingChainRules(g);
                removingNotTerminalsSymbols(g);
                removingNotReachableSymbols(g);
                //fnc(g);
                removingTheImmediateLeftRecursion(g);
                //removingLeftRecursion(g);

                Grammar gc = (Grammar) g.clone();
            }
        }

        this.button_Voltar = (Button) findViewById(R.id.button_Voltar);
        this.button_Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TrocaTela = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(TrocaTela);
                finish();
            }
        });
    }

    /**
     * Método que controla os views do menu
     */
    public void acordionMenu() {
        this.layoutGrammarType = (LinearLayout) findViewById(R.id.identifyGrammar);
        this.titleGrammarType = (TextView) findViewById(R.id.layouttext13);
        this.layoutGrammarType.setVisibility(View.GONE);

        this.layoutDerivationMoreLeft = (LinearLayout) findViewById(R.id.layout11);
        this.titleLeftDerivation = (TextView) findViewById(R.id.titleDerivationMoreLeft);
        this.layoutDerivationMoreLeft.setVisibility(View.GONE);

        this.titleRightDerivation = (TextView) findViewById(R.id.titleDerivationMoreRight);
        this.layoutDerivationMoreRight = (LinearLayout) findViewById(R.id.layout12);
        this.layoutDerivationMoreRight.setVisibility(View.GONE);

        this.titleRemoveInitialSymbolRecursive = (TextView) findViewById(R.id.titleRemoveInitialSymbolRecursive);
        this.layoutInitialSymbolRecursive = (LinearLayout) findViewById(R.id.layout1);
        this.layoutInitialSymbolRecursive.setVisibility(View.GONE);

        this.titleRemoveEmptyProductions = (TextView) findViewById(R.id.titleEmptyProductions);
        this.layoutEmptyProductions = (LinearLayout) findViewById(R.id.layout2);
        this.layoutEmptyProductions.setVisibility(View.GONE);

        this.titleChainRules = (TextView) findViewById(R.id.titleChainRules);
        this.layoutChainRules = (LinearLayout) findViewById(R.id.layout3);
        this.layoutChainRules.setVisibility(View.GONE);

        this.titleNoTermSymbols = (TextView) findViewById(R.id.titleNoTermSymbols);
        this.layoutNoTermSymbols = (LinearLayout) findViewById(R.id.layout4);
        this.layoutNoTermSymbols.setVisibility(View.GONE);

        this.titleNoReachSymbols = (TextView) findViewById(R.id.titleNoReachSymbols);
        this.layoutNoReachSymbols = (LinearLayout) findViewById(R.id.layout5);
        this.layoutNoReachSymbols.setVisibility(View.GONE);

        this.titleFnc = (TextView) findViewById(R.id.titleFNC);
        this.layoutFnc = (LinearLayout) findViewById(R.id.layout6);
        this.layoutFnc.setVisibility(View.GONE);

        this.titleRemoveLeftDirectRecursion = (TextView) findViewById(R.id.titleRemoveLeftDirectRecursion);
        this.layoutLeftDirectRecursion = (LinearLayout) findViewById(R.id.layout7);
        this.layoutLeftDirectRecursion.setVisibility(View.GONE);

        this.titleRemoveLeftRecursion = (TextView) findViewById(R.id.titleRemoveLeftRecursion);
        this.layoutLeftRecursion = (LinearLayout) findViewById(R.id.layout8);
        this.layoutLeftRecursion.setVisibility(View.GONE);

        this.titleFng = (TextView) findViewById(R.id.titleFNG);
        this.layoutFng = (LinearLayout) findViewById(R.id.layout9);
        this.layoutFng.setVisibility(View.GONE);

        this.titleCyk = (TextView) findViewById(R.id.titleCYK);
        this.layoutCyk = (LinearLayout) findViewById(R.id.layout10);
        this.layoutCyk.setVisibility(View.GONE);


        controlGrammarTypeMenu = true;
        this.titleGrammarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlGrammarTypeMenu) {
                    layoutGrammarType.setVisibility(View.GONE);
                    controlGrammarTypeMenu = false;
                } else {
                    layoutGrammarType.setVisibility(View.VISIBLE);
                    controlGrammarTypeMenu = true;
                }
            }
        });

        controlLeftDerivationMenu = true;
        this.titleLeftDerivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLeftDerivationMenu) {
                    layoutDerivationMoreLeft.setVisibility(View.GONE);
                    controlLeftDerivationMenu = false;
                } else {
                    layoutDerivationMoreLeft.setVisibility(View.VISIBLE);
                    controlLeftDerivationMenu = true;
                }
            }
        });

        controlRightDerivationMenu = true;
        this.titleRightDerivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlRightDerivationMenu) {
                    layoutDerivationMoreRight.setVisibility(View.GONE);
                    controlRightDerivationMenu = false;
                } else {
                    layoutDerivationMoreRight.setVisibility(View.VISIBLE);
                    controlRightDerivationMenu = true;
                }
            }
        });

        controlInitialSymbolRecursiveMenu = true;
        this.titleRemoveInitialSymbolRecursive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlInitialSymbolRecursiveMenu) {
                    layoutInitialSymbolRecursive.setVisibility(View.GONE);
                    controlInitialSymbolRecursiveMenu = false;
                } else {
                    layoutInitialSymbolRecursive.setVisibility(View.VISIBLE);
                    controlInitialSymbolRecursiveMenu = true;
                }
            }
        });

        controlEmptyProductionsMenu = true;
        this.titleRemoveEmptyProductions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlEmptyProductionsMenu) {
                    layoutEmptyProductions.setVisibility(View.GONE);
                    controlEmptyProductionsMenu = false;
                } else {
                    layoutEmptyProductions.setVisibility(View.VISIBLE);
                    controlEmptyProductionsMenu = true;
                }
            }
        });

        controlChainRulesMenu = true;
        this.titleChainRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlChainRulesMenu) {
                    layoutChainRules.setVisibility(View.GONE);
                    controlChainRulesMenu = false;
                } else {
                    layoutChainRules.setVisibility(View.VISIBLE);
                    controlChainRulesMenu = true;
                }
            }
        });

        controlNoTermSymbolsMenu = true;
        this.titleNoTermSymbols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlNoTermSymbolsMenu) {
                    layoutNoTermSymbols.setVisibility(View.GONE);
                    controlNoTermSymbolsMenu = false;
                } else {
                    layoutNoTermSymbols.setVisibility(View.VISIBLE);
                    controlNoTermSymbolsMenu = true;
                }
            }
        });

        controlNoReachSymbolsMenu = true;
        this.titleNoReachSymbols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlNoReachSymbolsMenu) {
                    layoutNoReachSymbols.setVisibility(View.GONE);
                    controlNoReachSymbolsMenu = false;
                } else {
                    layoutNoReachSymbols.setVisibility(View.VISIBLE);
                    controlNoReachSymbolsMenu = true;
                }
            }
        });

        controlFncMenu = true;
        this.titleFnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFncMenu) {
                    layoutFnc.setVisibility(View.GONE);
                    controlFncMenu = false;
                } else {
                    layoutFnc.setVisibility(View.VISIBLE);
                    controlFncMenu = true;
                }
            }
        });

        controlLeftDirectRecursionMenu = true;
        this.titleRemoveLeftDirectRecursion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLeftDirectRecursionMenu) {
                    layoutLeftDirectRecursion.setVisibility(View.GONE);
                    controlLeftDirectRecursionMenu = false;
                } else {
                    layoutLeftDirectRecursion.setVisibility(View.VISIBLE);
                    controlLeftDirectRecursionMenu = true;
                }
            }
        });

        controlLeftRecursionMenu = true;
        this.titleRemoveLeftRecursion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLeftRecursionMenu) {
                    layoutLeftRecursion.setVisibility(View.GONE);
                    controlLeftRecursionMenu = false;
                } else {
                    layoutLeftRecursion.setVisibility(View.VISIBLE);
                    controlLeftRecursionMenu = true;
                }
            }
        });

        controlFngMenu = true;
        this.titleFng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFngMenu) {
                    layoutFng.setVisibility(View.GONE);
                    controlFngMenu = false;
                } else {
                    layoutFng.setVisibility(View.VISIBLE);
                    controlFngMenu = true;
                }
            }
        });

        controlCykMenu = true;
        this.titleCyk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlCykMenu) {
                    layoutCyk.setVisibility(View.GONE);
                    controlCykMenu = false;
                } else {
                    layoutCyk.setVisibility(View.VISIBLE);
                    controlCykMenu = true;
                }
            }
        });
    }


    /**
     * Método que classifica a gramática passada como argumento em GR, GLC, GSC ou GI
     * @param g : gramática de entrada
     */
    public void grammarType(final Grammar g) {

        this.tableGrammarType = new TableLayout(this);
        this.tableGrammarType = (TableLayout) findViewById(R.id.tableGrammarType);
        this.tableGrammarType.setStretchAllColumns(true);

        //LINHA 1
        TableRow row0 = new TableRow(this);
        TextView tv0_0 = new TextView(this);
        tv0_0.setText("GR");
        TextView tv0_1 = new TextView(this);
        tv0_1.setText("u ∈ V");
        TextView tv0_2 = new TextView(this);
        tv0_2.setText("v ∈ λ | Σ | ΣV");
        row0.addView(tv0_0);
        row0.addView(tv0_1);
        row0.addView(tv0_2);
        row0.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        this.tableGrammarType.addView(row0);

        //LINHA 2
        TableRow row1 = new TableRow(this);
        TextView tv1_0 = new TextView(this);
        tv1_0.setText("GLC");
        TextView tv1_1 = new TextView(this);
        tv1_1.setText("u ∈ V");
        TextView tv1_2 = new TextView(this);
        tv1_2.setText("ν ∈ (V ∪ Σ)∗");
        row1.addView(tv1_0);
        row1.addView(tv1_1);
        row1.addView(tv1_2);
        row1.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
        tableGrammarType.addView(row1);

        //LINHA 3
        TableRow row2 = new TableRow(this);
        TextView tv2_0 = new TextView(this);
        tv2_0.setText("GSC");
        TextView tv2_1 = new TextView(this);
        tv2_1.setText("u ∈ (V U Σ)+");
        TextView tv2_2 = new TextView(this);
        tv2_2.setText("v ∈ (V ∪ Σ) +");
        row2.addView(tv2_0);
        row2.addView(tv2_1);
        row2.addView(tv2_2);
        row2.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        this.tableGrammarType.addView(row2);

        //LINHA 4
        TableRow row3 = new TableRow(this);
        TextView tv3_0 = new TextView(this);
        tv3_0.setText("GI");
        TextView tv3_1 = new TextView(this);
        tv3_1.setText("u ∈ (V U Σ)+");
        TextView tv3_2 = new TextView(this);
        tv3_2.setText("v ∈ (V ∪ Σ)*");
        row3.addView(tv3_0);
        row3.addView(tv3_1);
        row3.addView(tv3_2);
        row3.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
        this.tableGrammarType.addView(row3);

        AcademicSupport academic = new AcademicSupport();
        academic.setComments("A classificação de uma gramática é feita pelo tipo de suas regras (u → v). " +
                "A tabela abaixo mostra o formato de regras características de cada gramática: \n");

        StringBuilder comments = new StringBuilder();
        academic.setSolutionDescription(comments.toString() + GrammarParser.classifiesGrammar(g, comments));

        TextView explanation = new TextView(this);
        explanation = (TextView) findViewById(R.id.explanationGrammarType);
        explanation.setText(academic.getComments());

        TextView commentsOfSolution = new TextView(this);
        commentsOfSolution = (TextView) findViewById(R.id.comments);
        commentsOfSolution.setText(comments + academic.getSolutionDescription());
    }

    /**
     * Método que coloca a gramática passada como argumento no TableLayout destacando as alterações
     * realizadas.
     * @param table : TableLayout
     * @param academic : Objeto que armazena as informações acadêmicas
     * @param g : gramática passada
     */
    private void setContentInTable(TableLayout table, AcademicSupport academic, final Grammar g) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(g.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getInsertedRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Blue));
                    right.setText(element.getRightSide());
                } else if (academic.getIrregularRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Red));
                    right.setText(element.getRightSide());
                } else {
                        right.setText(element.getRightSide());
                    }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : g.getVariables()) {
            if (!variable.equals(g.getInitialSymbol())) {
                TableRow row1 = new TableRow(this);
                TextView tv0 = new TextView(this);
                tv0.setText(variable);
                row1.addView(tv0);
                TextView arrow1 = new TextView(this);
                arrow1.setText("->");
                row1.addView(arrow1);
                contViews = 2;
                for (Rule element : g.getRules()) {
                    if (variable.equals(element.getLeftSide())) {
                        TextView pipe = new TextView(this);
                        pipe.setText(" | ");
                        TextView tv1 = new TextView(this);
                        if (academic.getInsertedRules().contains(element)) {
                            tv1.setTextColor(getResources().getColor(R.color.Blue));
                            tv1.setText(element.getRightSide());
                        } else if (academic.getIrregularRules().contains(element)) {
                            tv1.setTextColor(getResources().getColor(R.color.Red));
                            tv1.setText(element.getRightSide());
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


    /**
     * Método que realiza a etapa de remoção do símbolo recursivo inicial e acrescenta as informações
     * acadêmicas.
     * @param g : gramática
     */
    public void removingInitialRecursiveSymbol(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();
        gc = g.getGrammarWithInitialSymbolNotRecursive(g, academicSupport);
        String txtGrammar = printRules(gc);

        /*step1_1 = (TextView) findViewById(R.id.DescricaoAlgoritmo1);
        step1_1.setText(academicSupport.getResult());*/

        TableLayout table = new TableLayout(this);
        table = (TableLayout) findViewById(R.id.TableRecursiveInitialSymbol);
        table.setShrinkAllColumns(true);
        setContentInTable(table, academicSupport, gc);


        step1_2 = (TextView) findViewById(R.id.Algoritmo1);
        if (academicSupport.getSituation()) {
            StringBuilder academicInfo = new StringBuilder(academicSupport.getComments());
            for (int i = 1; i < academicSupport.getFoundProblems().size(); i++) {
                academicInfo.append(academicSupport.getFoundProblems().get(i));
            }
            academicInfo.append(academicSupport.getSolutionDescription());
            step1_2.setText(academicInfo);
        } else {
            step1_2.setText("A gramática inserida não possui regras do tipo " + g.getInitialSymbol() + " ⇒*αSβ. Logo, nenhuma alteração foi realizada.");
        }
    }


    /**
     * Método que realiza a remoção de produções vazias e acrescenta as informações acadêmicas.
     * @param g : Gramática
     */
    public void removingEmptyProductions(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();
        StringBuilder academicInfoComments = new StringBuilder();

        //Configura comentários sobre o processo
        academicInfoComments.append("\t\tNa derivação de uma palavra, as formas sentenciais intermediárias podem conter variáveis que não geram " +
                "símbolos terminais. Estas variáveis são removidas a partir da aplicação do algoritmo λ-rules.\n");
        academicInfoComments.append("\t\tO objetivo desta transformação é garantir que toda variável na forma sentencial possa contribuir para a formação " +
                "da palavra que está sendo derivada.\n\n");
        academicInfoComments.append("\t\tO algoritmo para remoção de regras λ, consiste em 3 passos:");
        academicSupport.setComments(academicInfoComments.toString());

        //Realiza processo
        gc = g.getGrammarEssentiallyNoncontracting(g, academicSupport);
        academicSupport.setResult(gc);

        //configura a gramática de resultado
        TextView grammarResult = new TextView(this);
        grammarResult = (TextView) findViewById(R.id.ResultGrammarEmptyProductions);
        grammarResult.setText(academicSupport.getResult());

        if (academicSupport.getSituation()) {
            //Configura os comentários sobre a solução
            TextView result = new TextView(this);
            result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText(academicSupport.getComments());

            //Configuração do passo 1 (Tabela dos conjuntos)
            TextView explanation1 = new TextView(this);
            explanation1 = (TextView) findViewById(R.id.ExplanationEmptyRules1);
            explanation1.setText("(1) Determinar o conjunto das variáveis anuláveis;");
            TableLayout tableOfSets = new TableLayout(this);
            tableOfSets = (TableLayout) findViewById(R.id.TableOfSets);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfSets(tableOfSets, academicSupport, "NULL", "PREV");

            //Configuração do passo 2 (Gramática com as regras criadas no processo)
            TextView explanation2 = new TextView(this);
            explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
            explanation2.setText("(2) A adição de regras em que as ocorrências de variáveis nulas são omitidas;");
            TableLayout grammarWithNewRules = new TableLayout(this);
            grammarWithNewRules = (TableLayout) findViewById(R.id.AddingRulesTable);
            grammarWithNewRules.setShrinkAllColumns(true);
            Grammar blueGrammar = new Grammar(joinGrammars(gc, g));
            printGrammarWithNewRules(blueGrammar, grammarWithNewRules, academicSupport);

            //Configuração do passo 3 (Gramática com as regras irregulares removida)
            TextView explanation3 = new TextView(this);
            explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
            explanation3.setText("(3) Remover as regras λ.");
            TableLayout grammarWithoutOldRules = new TableLayout(this);
            grammarWithoutOldRules = (TableLayout) findViewById(R.id.RemovingRulesTable);
            grammarWithoutOldRules.setShrinkAllColumns(true);
            Grammar redGrammar = new Grammar(joinGrammars(gc, g));
            printGrammarWithoutOldRules(redGrammar, grammarWithoutOldRules, academicSupport);
        } else {
            TextView result = new TextView(this);
            result = (TextView) findViewById(R.id.AnswerOfEmptyProductions);
            result.setText("A gramática inserida não possui produções vazias.");
        }
    }



    public void removingChainRules(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append("\t\tA aplicação de uma regra do tipo A -> B não acrescenta nada para a formação de uma palavra. Regras dessa forma são conhecidas como " +
                "regras da cadeia. A remoção de regras da cadeia consiste em um processo de renomeação.\n");
        comments.append("\t\tO algoritmo para a remoção de regras da cadeia realiza a análise da cadeia de cada variável e logo em seguida subistitui as ocorrências " +
                "de cadeia, quando for o caso, adicionando diretamente as regras da variável que criou a cadeia.\n");

        //Realiza processo
        gc = g.getGrammarWithoutChainRules(g, academic);
        academic.setResult(gc);

        //Configura a gramática de resultado
        TextView grammarResult = new TextView(this);
        grammarResult = (TextView) findViewById(R.id.ResultGrammarWithoutChainRules);
        grammarResult.setText(academic.getResult());

        //Configura os comentários do processo
        academic.setComments(comments.toString());
        TextView commentsOfProcces = new TextView(this);
        commentsOfProcces = (TextView) findViewById(R.id.CommentsChainRules);
        commentsOfProcces.setText(academic.getComments());

        //Configura o primeiro passo do processo
        TextView creatingSetOfChains = new TextView(this);
        creatingSetOfChains = (TextView) findViewById(R.id.RemovingChainRulesStep1);
        creatingSetOfChains.setText("(1) O primeiro passo do algoritmo é montar as cadeias de cada variável.");
        TableLayout tableOfChains = new TableLayout(this);
        tableOfChains = (TableLayout) findViewById(R.id.TableOfChains);
        tableOfChains.setShrinkAllColumns(true);
        printTableOfChains(tableOfChains, academic, "Variável", "Cadeia");

        //Configura o segundo passo do processo
        TextView creatingGrammarWithoutChains = new TextView(this);
        creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep2);
        creatingGrammarWithoutChains.setText("(2) Subistituir as cadeias encontradas.");
        TableLayout tableWithoutChains = new TableLayout(this);
        tableWithoutChains = (TableLayout) findViewById(R.id.GrammarWithoutChains);
        tableWithoutChains.setShrinkAllColumns(true);
        printGrammarWithNewRules(gc, tableWithoutChains, academic);
    }

    public void removingNotTerminalsSymbols(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();
        //Realiza processo
        gc = g.getGrammarWithoutNoTerm(g, academicSupport);
        academicSupport.setResult(gc);

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append("\t\tUm símbolo inútil, é aquele que não contribui para a formação das palavras pertencentes à gramática, prodendo ser não terminais ou " +
                "não alcançáveis. Esta transformação tem o objetivo de remover as regras que não são terminais, ou seja, todo o símbolo que não for capaz " +
                "de terminar uma forma sentencial, será removido.");

        //Configura a gramática de resultado
        TextView resultGrammar = new TextView(this);
        resultGrammar = (TextView) findViewById(R.id.ResultNoTerm);
        resultGrammar.setText(academicSupport.getResult());

        if (academicSupport.getSituation()) {
            //Configura os comentários
            academicSupport.setComments(comments.toString());
            TextView result = new TextView(this);
            result = (TextView) findViewById(R.id.CommentsNoTerms);
            result.setText(academicSupport.getComments());

            //Configura o primeiro passo (Montar conjuntos)
            TextView creatingSetOfChains = new TextView(this);
            creatingSetOfChains = (TextView) findViewById(R.id.NoTermStep1);
            creatingSetOfChains.setText("(1) O primeiro passo do algoritmo é determinar quais símbolos são terminais.");
            TableLayout tableOfSets = new TableLayout(this);
            tableOfSets = (TableLayout) findViewById(R.id.TableOfSetsNoTerm);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfSets(tableOfSets, academicSupport, "TERM", "PREV");

            //Configura o segundo passo (Regras que foram removidas)
            TextView eliminatingNoTermRules = new TextView(this);
            creatingSetOfChains = (TextView) findViewById(R.id.NoTermStep2);
            creatingSetOfChains.setText("(2) O primeiro passo do algoritmo é remover os símbolos não terminais (não pertencentes ao conjunto TERM).");
            TableLayout grammarWithoutOldRules = new TableLayout(this);
            grammarWithoutOldRules = (TableLayout) findViewById(R.id.GrammarWithNoTerm);
            grammarWithoutOldRules.setShrinkAllColumns(true);
            printOldGrammarOfTermAndReach(g, grammarWithoutOldRules, academicSupport);
        } else {
            TextView result = new TextView(this);
            result = (TextView) findViewById(R.id.CommentsNoTerms);
            result.setText("A gramática inserida não possui símbolos não terminais.");
        }
    }

    public void removingNotReachableSymbols(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();
        StringBuilder comments = new StringBuilder();

        //Realiza processo
        gc = gc.getGrammarWithoutNoReach(gc, academic);
        academic.setResult(gc);

        //Realiza comentários sobre o processo
        comments.append("\t\tNá construção de uma gramática, podem existir símbolos que não são alcançados durante o processo de derivação, não contribuindo " +
                "para a formação de uma palavra. Esses símbolos são conhecidos como símbolos não alcançáveis e podem ser removidos pelo algorimo REACH.\n");
        comments.append("\t\tO algoritmo REACH tem o objetivo de montar um conjunto com todos os símbolos que contribuem para a formação de palavras em " +
                "uma dada gramática. Ao término do preenchimento do conjunto, o algoritmo remove todos os símbolos da gramática que não estão presentes no " +
                "conjunto, pois os mesmos não contribuem para a formação de nenhuma palavra na gramática inserida.");

        //Mostra o resultado do processo
        TextView tableOfResult = new TextView(this);
        tableOfResult = (TextView) findViewById(R.id.ResultNoReach);
        tableOfResult.setText(academic.getResult());

        if (academic.getSituation()) {
            //Configura comentários
            academic.setComments(comments.toString());
            TextView commentsOfNoReach = new TextView(this);
            commentsOfNoReach = (TextView) findViewById(R.id.CommentsNoReach);
            commentsOfNoReach.setText(academic.getComments());

            //Primeiro passo do processo (Construção dos Conjuntos)
            TextView step1 = new TextView(this);
            step1 = (TextView) findViewById(R.id.NoReachStep1);
            step1.setText("(1) O primeiro passo do algoritmo é determinar quais símbolos são alcançáveis.");
            TableLayout tableOfSets = new TableLayout(this);
            tableOfSets = (TableLayout) findViewById(R.id.tableOfSetsNoReach);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfReachSets(tableOfSets, academic, "REACH", "PREV", "NEW");

            //Segundo passo do processo (Eliminar os símbolos não terminais)
            TextView step2 = new TextView(this);
            step2 = (TextView) findViewById(R.id.NoReachStep2);
            step2.setText("(2) O segundo passo do algoritmo é remover os símbolos que não são alcançáveis (não estão no conjunto REACH).");
            TableLayout tableResult = new TableLayout(this);
            tableResult = (TableLayout) findViewById(R.id.tableOfIrregularRulesReach);
            tableResult.setShrinkAllColumns(true);
            printOldGrammarOfTermAndReach(g, tableResult, academic);

        } else {
            TextView commentsOfNoReach = new TextView(this);
            commentsOfNoReach = (TextView) findViewById(R.id.CommentsNoReach);
            commentsOfNoReach.setText("\t\tNão há símbolos alcançáveis na gramática inserida.");
        }

    }

    public void fnc(final Grammar g) {}

    public void removingTheImmediateLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append("\t\tRecursividade direta à esquerda pode produzir “loops infinitos” em analisadores sintáticos descendentes (top-down).");

        //Realiza processo
        gc = gc.removingTheImmediateLeftRecursion(gc, academicSupport);
        academicSupport.setResult(gc);

        //Insere o resultado do processo
        TextView resultOfProcess = new TextView(this);
        resultOfProcess = (TextView) findViewById(R.id.ResultGrammarWithoutLeftDirectRecursion);
        resultOfProcess.setText(academicSupport.getResult());

        if (academicSupport.getSituation()) {
            //Insere comentários
            academicSupport.setComments(comments.toString());
            TextView commentsOfProcess = new TextView(this);
            commentsOfProcess = (TextView) findViewById(R.id.CommentsDirectLeftRecursion);
            commentsOfProcess.setText(academicSupport.getComments());

            //Explicação do processo
            TextView step1 = new TextView(this);
            step1 = (TextView) findViewById(R.id.Step1DirectLeftRecursion);
            step1.setText("Para remover uma recursão direta à esquerda");


        } else {
            TextView commentsOfProcess = new TextView(this);
            commentsOfProcess = (TextView) findViewById(R.id.CommentsDirectLeftRecursion);
            commentsOfProcess.setText("A gramática inserida não possui recursão direta à esquerda.");
        }

    }

    public void removingLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        StringBuilder academicSupport = new StringBuilder();
        StringBuilder algorithm = new StringBuilder();

        algorithm.append("\t\t\tenumerar todas as variáveis\n");
        algorithm.append("\t\t\tpara cada A ∈ V faça\n");
        algorithm.append("\t\t\t\t\tse a variável à direita possui valor menor que a variável do lado direito então\n");
        algorithm.append("\t\t\t\t\t\t\t\t\t copia todas as produções do lado direito da variável maior na menor\n");

        step8_1 = (TextView) findViewById(R.id.PseudocódigoAlgoritmo8);
        step8_1.setText(algorithm);

        gc = g.removingLeftRecursion(g, academicSupport);

        step8_2 = (TextView) findViewById(R.id.DescricaoAlgoritmo8);
        step8_2.setText(academicSupport);

        String txtGrammar = printRules(gc);
        step8_3 = (TextView) findViewById(R.id.Algoritmo8);
        step8_3.setText(txtGrammar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String printRules(final Grammar g) {
        String out = new String();
        for (Rule element : g.getRules()) {
            if (element.getLeftSide().equals(g.getInitialSymbol())) {
                out += element.toString() + "\n";
            }
        }

        for (String variable : g.getVariables()) {
            for (Rule element : g.getRules()) {
                if (!variable.equals(g.getInitialSymbol()) && variable.equals(element.getLeftSide())) {
                    out += element.toString() + "\n";
                }
            }
        }
        return out;
    }

    /**
     * Mètodo que junta duas gramáticas em um String
     * @param grammar1
     * @param grammar2
     * @return
     */
    public String joinGrammars(final Grammar grammar1, final Grammar grammar2) {
        StringBuilder newG = new StringBuilder();
        for (Rule element : grammar1.getRules()) {
            newG.append(element);
            newG.append("\n");
        }
        for (Rule element : grammar2.getRules()) {
            if (!grammar1.getRules().contains(element)) {
                newG.append(element);
                newG.append("\n");
            }
        }
        return newG.toString();
    }

    /**
     * Método que imprime a gramática com as regras irregulares em destaque
     * @param table
     * @param academic
     */
    private void printGrammarWithoutOldRules(final Grammar grammar, TableLayout table, final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getIrregularRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Red));
                    right.setText(element.getRightSide());
                } else {
                    right.setText(element.getRightSide());
                }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : grammar.getVariables()) {
            if (!variable.equals(grammar.getInitialSymbol())) {
                TableRow row1 = new TableRow(this);
                TextView tv0 = new TextView(this);
                tv0.setText(variable);
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
                            tv1.setTextColor(getResources().getColor(R.color.Red));
                            tv1.setText(element.getRightSide());
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

    /**
     * Método que imprime a tabela dos conjuntos utilizados durante a execução do algoritmo
     * @param academic : informaçãos coletadas durante a execução do algoritmo
     */
    private void printTableOfSets(TableLayout table, final AcademicSupport academic, String nameOfFirstSet, String nameOfSecondSet) {
        int i = 0;
        TableRow header = new TableRow(this);
        TextView htv0 = new TextView(this);
        htv0.setText("(" + i + ")");
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
        i++;

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();
        while (iteratorOfSecondCell.hasNext()) {
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
     * Método que imprime a gramática com as regras irregulares em destaque
     * @param grammar
     * @param table
     * @param academic
     */
    private void printOldGrammarOfTermAndReach(final Grammar grammar, TableLayout table, final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        int contViews = 0;
        boolean initialSymbolIrregular = false;
        for (Rule element : grammar.getRules()) {
            if (element.equals(grammar.getInitialSymbol())) {
                if (academic.getIrregularRules().contains(element)) {
                    initialSymbolIrregular = true;
                }
            }
        }
        arrow0.setText(" -> ");
        left.setText(grammar.getInitialSymbol());
        if (initialSymbolIrregular) {
            left.setTextColor(getResources().getColor(R.color.Red));
        }
        row0.addView(left);
        row0.addView(arrow0);
        contViews = 2;
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getIrregularRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Red));
                    right.setText(element.getRightSide());
                } else {
                    right.setText(element.getRightSide());
                }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : grammar.getVariables()) {
            contViews = 0;
            if (!variable.equals(grammar.getInitialSymbol())) {
                TableRow row1 = new TableRow(this);
                TextView tv0 = new TextView(this);
                tv0.setText(variable);
                TextView arrow1 = new TextView(this);
                arrow1.setText("->");
                boolean irregularVariable = false;
                for (Rule element : grammar.getRules()) {
                    if (element.getLeftSide().equals(variable)) {
                        if (academic.getIrregularRules().contains(element) && !academic.getFirstSet().get(academic.getFirstSet().size() - 1).contains(variable)) {
                            irregularVariable = true;
                        }
                    }
                }

                if (irregularVariable) {
                    tv0.setTextColor(getResources().getColor(R.color.Red));
                }
                row1.addView(tv0);
                row1.addView(arrow1);
                contViews += 2;

                for (Rule element : grammar.getRules()) {
                    if (variable.equals(element.getLeftSide())) {
                        TextView pipe = new TextView(this);
                        pipe.setText(" | ");
                        TextView tv1 = new TextView(this);
                        if (academic.getIrregularRules().contains(element)) {
                            tv1.setTextColor(getResources().getColor(R.color.Red));
                            tv1.setText(element.getRightSide());
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

    /**
     * Método que imprime a gramática com as novas regras inseridas em destaque
     * @param table
     * @param academic
     */
    private void printGrammarWithNewRules(final Grammar grammar, TableLayout table, final AcademicSupport academic) {
        TableRow row0 = new TableRow(this);
        TextView left = new TextView(this);
        TextView arrow0 = new TextView(this);
        left.setText(grammar.getInitialSymbol());
        arrow0.setText(" -> ");
        row0.addView(left);
        row0.addView(arrow0);
        int contViews = 2;
        for (Rule element : grammar.getRules()) {
            if (element.getLeftSide().equals(grammar.getInitialSymbol())) {
                TextView right = new TextView(this);
                TextView pipe = new TextView(this);
                pipe.setText(" | ");
                if (academic.getInsertedRules().contains(element)) {
                    right.setTextColor(getResources().getColor(R.color.Blue));
                    right.setText(element.getRightSide());
                } else {
                    right.setText(element.getRightSide());
                }
                row0.addView(right);
                row0.addView(pipe);
                contViews += 2;
            }
        }
        row0.removeViewAt(contViews - 1);
        table.addView(row0);

        for (String variable : grammar.getVariables()) {
            if (!variable.equals(grammar.getInitialSymbol())) {
                TableRow row1 = new TableRow(this);
                TextView tv0 = new TextView(this);
                tv0.setText(variable);
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
                            tv1.setTextColor(getResources().getColor(R.color.Blue));
                            tv1.setText(element.getRightSide());
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

    /**
     * Método que imprime a tabela de chains
     * @param table
     * @param academic
     * @param nameOfFisrtCell
     * @param nameOfSecondCell
     */
    private void printTableOfChains(TableLayout table, final AcademicSupport academic, String nameOfFisrtCell, String nameOfSecondCell) {
        //Configura o cabeçalho da tabela
        TableRow header = new TableRow(this);
        TextView htv0 = new TextView(this);
        htv0.setText(nameOfFisrtCell);
        htv0.setTextColor(getResources().getColor(R.color.Black));
        htv0.setPadding(10, 10, 10, 10);

        TextView htv1 = new TextView(this);
        htv1.setText(nameOfSecondCell);
        htv1.setTextColor(getResources().getColor(R.color.Black));
        htv1.setPadding(10, 10, 10, 10);

        header.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        header.addView(htv0);
        header.addView(htv1);
        table.addView(header);
        int i = 1;

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();


        while (iteratorOfFirstCell.hasNext()) {
            Set<Rule> firstSet = iteratorOfFirstCell.next();
            Set<Rule> secondSet = iteratorOfSecondCell.next();

            String teste = firstSet.toString();
            String teste2 = secondSet.toString();

            String set1 = firstSet.toString();
            set1 = set1.replace("[", "(");
            set1 = set1.replace("]", ")");

            String set2 = secondSet.toString();
            set2 = set2.replace("[", "{");
            set2 = set2.replace("]", "}");

            TextView tv1 = new TextView(this);
            tv1.setText(set1);
            tv1.setPadding(10, 10, 10, 10);
            tv1.setTextColor(getResources().getColor(R.color.Black));

            TextView tv2 = new TextView(this);
            tv2.setText(set2);
            tv2.setPadding(10, 10, 10, 10);
            tv2.setTextColor(getResources().getColor(R.color.Black));

            TableRow row = new TableRow(this);
            if (i % 2 != 0) {
                row.setBackgroundColor(getResources().getColor(R.color.LightGrey));
            } else {
                row.setBackgroundColor(getResources().getColor(R.color.DarkGray));
            }

            row.addView(tv1);
            row.addView(tv2);
            i++;
            table.addView(row);
        }
    }

    /**
     * Método que imprime a tabela dos conjuntos utilizados durante a execução do algoritmo REACH
     * @param academic : informaçãos coletadas durante a execução do algoritmo
     */
    private void printTableOfReachSets(TableLayout table, final AcademicSupport academic, String nameOfFirstSet, String nameOfSecondSet, String nameOfThirdSet) {
        int i = 0;
        TableRow header = new TableRow(this);
        TextView htv0 = new TextView(this);
        htv0.setText("(" + i + ")");
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
        TextView htv3 = new TextView(this);
        htv3.setText(nameOfThirdSet);
        htv3.setPadding(10, 10, 10, 10);
        htv3.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        htv3.setTextColor(getResources().getColor(R.color.Black));
        header.addView(htv0);
        header.addView(htv1);
        header.addView(htv2);
        header.addView(htv3);
        table.addView(header);
        i++;

        Iterator<Set<Rule>> iteratorOfFirstCell = (Iterator) academic.getFirstSet().iterator();
        Iterator<Set<Rule>> iteratorOfSecondCell = (Iterator) academic.getSecondSet().iterator();
        Iterator<Set<Rule>> iteratorOfThirdCell = (Iterator) academic.getThirdSet().iterator();

        while (iteratorOfSecondCell.hasNext()) {
            Set<Rule> firstSet = iteratorOfFirstCell.next();
            Set<Rule> secondSet = iteratorOfSecondCell.next();
            Set<Rule> thirdSet = iteratorOfThirdCell.next();

            String set1 = firstSet.toString();
            set1 = set1.replace("[", "{");
            set1 = set1.replace("]", "}");

            String set2 = secondSet.toString();
            set2 = set2.replace("[", "{");
            set2 = set2.replace("]", "}");

            String set3 = thirdSet.toString();
            set3 = set3.replace("[", "{");
            set3 = set3.replace("]", "}");

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

            TextView tv3 = new TextView(this);
            tv3.setText(set3);
            tv3.setPadding(10, 10, 10, 10);
            tv3.setTextColor(getResources().getColor(R.color.Black));
            tv3.setBackgroundColor(getResources().getColor(R.color.DarkGray));

            TableRow row = new TableRow(this);
            row.addView(tv0);
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);

            table.addView(row);
            i++;
        }
    }
}
