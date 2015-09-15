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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    private TextView fncStep1, fncStep2, fncStep3, fncStep4, fncStep5, fncStep6;
    private LinearLayout fncLayout1, fncLayout2, fncLayout3, fncLayout4, fncLayout5, fncLayout6;
    private boolean controlFNC1, controlFNC2, controlFNC3, controlFNC4, controlFNC5, controlFNC6;
    private LinearLayout fngLayout1, fngLayout2;
    private TextView fngStep1, fngStep2;
    private boolean controlFNG1, controlFNG2;

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

                TextView inputGrammar = new TextView(this);
                inputGrammar = (TextView) findViewById(R.id.GramaticaVerde);
                AcademicSupport academic = new AcademicSupport();
                academic.setResult(g);
                inputGrammar.setText(academic.getResult());
                inputGrammar.setTextColor(getResources().getColor(R.color.MediumSpringGreen));

                grammarType(g);
                removingInitialRecursiveSymbol(g);
                removingEmptyProductions(g);
                removingChainRules(g);
                removingNotTerminalsSymbols(g);
                removingNotReachableSymbols(g);
                fnc(g);
                removingTheImmediateLeftRecursion(g);
                removingLeftRecursion(g);
                fng(g);
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


        //Controla menu 1 de FNC (Remoção de recursão no símbolo inicial)
        this.fncStep1 = (TextView) findViewById(R.id.FncStep1);
        this.fncLayout1 = (LinearLayout) findViewById(R.id.LayoutFNC1);
        this.fncLayout1.setVisibility(View.GONE);

        controlFNC1 = true;
        this.fncStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNC1) {
                    fncLayout1.setVisibility(View.GONE);
                    controlFNC1 = false;
                } else {
                    fncLayout1.setVisibility(View.VISIBLE);
                    controlFNC1 = true;
                }
            }
        });

        //Controla menu 2 de FNC (Remoção de produções vazias)
        this.fncStep2 = (TextView) findViewById(R.id.FncStep2);
        this.fncLayout2 = (LinearLayout) findViewById(R.id.LayoutFNC2);
        this.fncLayout2.setVisibility(View.GONE);

        controlFNC2 = true;
        this.fncStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNC2) {
                    fncLayout2.setVisibility(View.GONE);
                    controlFNC2 = false;
                } else {
                    fncLayout2.setVisibility(View.VISIBLE);
                    controlFNC2 = true;
                }
            }
        });

        //Controla menu 3 de FNC (Regras da cadeia)
        this.fncStep3 = (TextView) findViewById(R.id.FncStep3);
        this.fncLayout3 = (LinearLayout) findViewById(R.id.LayoutFNC3);
        this.fncLayout3.setVisibility(View.GONE);

        controlFNC3 = true;
        this.fncStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNC3) {
                    fncLayout3.setVisibility(View.GONE);
                    controlFNC3 = false;
                } else {
                    fncLayout3.setVisibility(View.VISIBLE);
                    controlFNC3 = true;
                }
            }
        });

        //Controla menu 4 de FNC (Regras não terminais)
        this.fncStep4 = (TextView) findViewById(R.id.FncStep4);
        this.fncLayout4 = (LinearLayout) findViewById(R.id.LayoutFNC4);
        this.fncLayout4.setVisibility(View.GONE);

        controlFNC4 = true;
        this.fncStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNC4) {
                    fncLayout4.setVisibility(View.GONE);
                    controlFNC4 = false;
                } else {
                    fncLayout4.setVisibility(View.VISIBLE);
                    controlFNC4 = true;
                }
            }
        });

        //Controla menu 5 de FNC (Regras não alcançáveis)
        this.fncStep5 = (TextView) findViewById(R.id.FncStep5);
        this.fncLayout5 = (LinearLayout) findViewById(R.id.LayoutFNC5);
        this.fncLayout5.setVisibility(View.GONE);

        controlFNC5 = true;
        this.fncStep5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNC5) {
                    fncLayout5.setVisibility(View.GONE);
                    controlFNC5 = false;
                } else {
                    fncLayout5.setVisibility(View.VISIBLE);
                    controlFNC5 = true;
                }
            }
        });

        //Controla menu 6 de FNC (Regras não alcançáveis)
        this.fncStep6 = (TextView) findViewById(R.id.FncStep6);
        this.fncLayout6 = (LinearLayout) findViewById(R.id.LayoutFNC6);
        this.fncLayout6.setVisibility(View.GONE);

        controlFNC6 = true;
        this.fncStep6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNC6) {
                    fncLayout6.setVisibility(View.GONE);
                    controlFNC6 = false;
                } else {
                    fncLayout6.setVisibility(View.VISIBLE);
                    controlFNC6 = true;
                }
            }
        });

        //Controla menu 1 de FNG (Remoção de recursão à esquerda)
        this.fngStep1 = (TextView) findViewById(R.id.FngStep1);
        this.fngLayout1 = (LinearLayout) findViewById(R.id.LayoutFNG1);
        this.fngLayout1.setVisibility(View.GONE);

        controlFNG1 = true;
        this.fngStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNG1) {
                    fngLayout1.setVisibility(View.GONE);
                    controlFNG1 = false;
                } else {
                    fngLayout1.setVisibility(View.VISIBLE);
                    controlFNG1 = true;
                }
            }
        });


        //Controla menu 2 de FNG (Forma normal de Greibach)
        this.fngStep2 = (TextView) findViewById(R.id.FngStep2);
        this.fngLayout2 = (LinearLayout) findViewById(R.id.LayoutFNG2);
        this.fngLayout2.setVisibility(View.GONE);

        controlFNG2 = true;
        this.fngStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlFNG2) {
                    fngLayout2.setVisibility(View.GONE);
                    controlFNG2 = false;
                } else {
                    fngLayout2.setVisibility(View.VISIBLE);
                    controlFNG2 = true;
                }
            }
        });



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
        academicInfoComments.append("\t\tQuando alguma regra não contribuir para o processo de derivação, aplica-se o algoritmo λ-rules, para" +
                "remover as produção vazias.\n");
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
            if (academicSupport.getInsertedRules().size() != 0) {
                TextView explanation2 = new TextView(this);
                explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText("(2) A adição de regras em que as ocorrências de variáveis nulas são omitidas;");
                TableLayout grammarWithNewRules = new TableLayout(this);
                grammarWithNewRules = (TableLayout) findViewById(R.id.AddingRulesTable);
                grammarWithNewRules.setShrinkAllColumns(true);
                Grammar blueGrammar = new Grammar(joinGrammars(gc, g));
                printGrammarWithNewRules(blueGrammar, grammarWithNewRules, academicSupport);
            } else {
                TextView explanation2 = new TextView(this);
                explanation2 = (TextView) findViewById(R.id.ExplanationEmptyRules2);
                explanation2.setText("(2) Não há regras a serem inseridas.");
            }

            //Configuração do passo 3 (Gramática com as regras irregulares removida)
            if (academicSupport.getIrregularRules().size() != 0) {
                TextView explanation3 = new TextView(this);
                explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText("(3) Remover as regras λ.");
                TableLayout grammarWithoutOldRules = new TableLayout(this);
                grammarWithoutOldRules = (TableLayout) findViewById(R.id.RemovingRulesTable);
                grammarWithoutOldRules.setShrinkAllColumns(true);
                Grammar redGrammar = new Grammar(joinGrammars(gc, g));
                printGrammarWithoutOldRules(redGrammar, grammarWithoutOldRules, academicSupport);
            } else {
                TextView explanation3 = new TextView(this);
                explanation3 = (TextView) findViewById(R.id.ExplanationEmptyRules3);
                explanation3.setText("(3) Não há regras a serem removidas.");
            }

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
        comments.append("\t\tA remoção de regras da cadeia consiste em um processo de renomeação.\n");
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
        if (academic.getInsertedRules().size() != 0) {
            TextView creatingGrammarWithoutChains = new TextView(this);
            creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep2);
            creatingGrammarWithoutChains.setText("(2) Subistituir as cadeias encontradas.");
            TableLayout tableWithoutChains = new TableLayout(this);
            tableWithoutChains = (TableLayout) findViewById(R.id.GrammarWithoutChains);
            tableWithoutChains.setShrinkAllColumns(true);
            printGrammarWithNewRules(gc, tableWithoutChains, academic);
        } else {
            TextView creatingGrammarWithoutChains = new TextView(this);
            creatingGrammarWithoutChains = (TextView) findViewById(R.id.RemovingChainRulesStep2);
            creatingGrammarWithoutChains.setText("(2) Não há cadeias na gramática inserida.");
        }

    }

    public void removingNotTerminalsSymbols(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academicSupport = new AcademicSupport();
        //Realiza processo
        gc = g.getGrammarWithoutNoTerm(g, academicSupport);
        academicSupport.setResult(gc);

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append("\t\tEsta transformação tem o objetivo de remover as regras que não são terminais, ou seja, todo o símbolo que não for capaz " +
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
            creatingSetOfChains.setText("(2) O primeiro passo do algoritmo é remover os símbolos não terminais.");
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
        comments.append("\t\tEsta transformação tem o objetivo de remover os símbolos que não alcançáveis no processo de derivação de uma palavra.\n");

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
            if (academic.getIrregularRules().size() != 0) {
                TextView step2 = new TextView(this);
                step2 = (TextView) findViewById(R.id.NoReachStep2);
                step2.setText("(2) O segundo passo do algoritmo é remover os símbolos que não são alcançáveis.");
                TableLayout tableResult = new TableLayout(this);
                tableResult = (TableLayout) findViewById(R.id.tableOfIrregularRulesReach);
                tableResult.setShrinkAllColumns(true);
                printOldGrammarOfTermAndReach(g, tableResult, academic);
            } else {
                TextView step2 = new TextView(this);
                step2 = (TextView) findViewById(R.id.NoReachStep2);
                step2.setText("(2) Todos os símbolos são alcançáveis.");
            }

        } else {
            TextView commentsOfNoReach = new TextView(this);
            commentsOfNoReach = (TextView) findViewById(R.id.CommentsNoReach);
            commentsOfNoReach.setText("\t\tNão há símbolos alcançáveis na gramática inserida.");
        }

    }

    public void fnc(final Grammar g) {
        Grammar gc = (Grammar) g.clone();
        AcademicSupport academic = new AcademicSupport();

        //Realiza processo para a primeira transformação (Remoção de recursão no símbolo inicial)
        gc = g.getGrammarWithInitialSymbolNotRecursive(gc, academic);
        academic.setResult(gc);
        TextView result = new TextView(this);
        result = (TextView) findViewById(R.id.GramaticaSemRecursividadeNoSimboloInicialFNC);
        result.setText(academic.getResult());

        TextView explanationStep1 = new TextView(this);
        explanationStep1 = (TextView) findViewById(R.id.ExplicacaoFNC1);
        if (academic.getSituation()) {
            StringBuilder academicInfo = new StringBuilder(academic.getComments());
            for (int i = 1; i < academic.getFoundProblems().size(); i++) {
                academicInfo.append(academic.getFoundProblems().get(i));
            }
            academicInfo.append(academic.getSolutionDescription());
            explanationStep1.setText(academicInfo);
        } else {
            explanationStep1.setText("A gramática inserida não possui regras do tipo " + g.getInitialSymbol() + " ⇒*αSβ. Logo, nenhuma alteração foi realizada.");
        }

        academic = new AcademicSupport();

        //Realiza processo para a segunda transformação (Remoção de produções vazias)
        gc = gc.getGrammarEssentiallyNoncontracting(gc, academic);

        StringBuilder academicInfoComments = new StringBuilder();

        //Configura comentários sobre o processo
        academicInfoComments.append("\t\tQuando alguma regra não contribuir para o processo de derivação, aplica-se o algoritmo λ-rules, para" +
                "remover as produção vazias.\n");
        academicInfoComments.append("\t\tO objetivo desta transformação é garantir que toda variável na forma sentencial possa contribuir para a formação " +
                "da palavra que está sendo derivada.\n\n");
        academicInfoComments.append("\t\tO algoritmo para remoção de regras λ, consiste em 3 passos:");
        academic.setComments(academicInfoComments.toString());
        academic.setComments(academicInfoComments.toString());

        academic.setResult(gc);

        //configura a gramática de resultado
        result = new TextView(this);
        result = (TextView) findViewById(R.id.GramaticaSemProducoesVaziasFNC);
        result.setText(academic.getResult());

        if (academic.getSituation()) {
            //Configuração do passo 1 (Tabela dos conjuntos)
            TextView explanation1 = new TextView(this);
            explanation1 = (TextView) findViewById(R.id.ComentariosFNC2);
            explanation1.setText("(1) Determinar o conjunto das variáveis anuláveis;");
            TableLayout tableOfSets = new TableLayout(this);
            tableOfSets = (TableLayout) findViewById(R.id.TabelaConjuntosFNC2);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfSets(tableOfSets, academic, "NULL", "PREV");

            //Configuração do passo 2 (Gramática com as regras criadas no processo)
            if (academic.getInsertedRules().size() != 0) {
                TextView explanation2 = new TextView(this);
                explanation2 = (TextView) findViewById(R.id.ExplicacaoGramaticaSemProducoesVaziasFNC);
                explanation2.setText("(2) A adição de regras em que as ocorrências de variáveis nulas são omitidas;");
                TableLayout grammarWithNewRules = new TableLayout(this);
                grammarWithNewRules = (TableLayout) findViewById(R.id.GramaticaAzul2FNC);
                grammarWithNewRules.setShrinkAllColumns(true);
                Grammar blueGrammar = new Grammar(joinGrammars(gc, g));
                printGrammarWithNewRules(blueGrammar, grammarWithNewRules, academic);
            } else {
                TextView explanation2 = new TextView(this);
                explanation2 = (TextView) findViewById(R.id.ExplicacaoGramaticaSemProducoesVaziasFNC);
                explanation2.setText("(2) Não há regras a serem inseridas.");
            }

            //Configuração do passo 3 (Gramática com as regras irregulares removida)
            if (academic.getIrregularRules().size() != 0) {
                TextView explanation3 = new TextView(this);
                explanation3 = (TextView) findViewById(R.id.ExplicacaoGramaticaComNovasProducoesFNC);
                explanation3.setText("(3) Remover as regras λ.");
                TableLayout grammarWithoutOldRules = new TableLayout(this);
                grammarWithoutOldRules = (TableLayout) findViewById(R.id.GramaticaVermelha2FNC);
                grammarWithoutOldRules.setShrinkAllColumns(true);
                Grammar redGrammar = new Grammar(joinGrammars(gc, g));
                printGrammarWithoutOldRules(redGrammar, grammarWithoutOldRules, academic);
            } else {
                TextView explanation3 = new TextView(this);
                explanation3 = (TextView) findViewById(R.id.ExplicacaoGramaticaComNovasProducoesFNC);
                explanation3.setText("(3) Não há regras a serem removidas.");
            }

        } else {
            TextView comment = new TextView(this);
            comment = (TextView) findViewById(R.id.Explicacao1ProducoesVaziasFNC);
            comment.setText("A gramática inserida não possui produções vazias.");
        }

        //Realiza processo para a terceira transformação (Remoção de regras da cadeia)
        academic = new AcademicSupport();

        //Realiza comentários sobre o processo
        StringBuilder comments = new StringBuilder();
        comments.append("\t\tA remoção de regras da cadeia consiste em um processo de renomeação.\n");
        comments.append("\t\tO algoritmo para a remoção de regras da cadeia realiza a análise da cadeia de cada variável e logo em seguida subistitui as ocorrências " +
                "de cadeia, quando for o caso, adicionando diretamente as regras da variável que criou a cadeia.\n");

        //Realiza processo
        gc = gc.getGrammarWithoutChainRules(gc, academic);
        academic.setResult(gc);

        //Configura a gramática de resultado
        result = new TextView(this);
        result = (TextView) findViewById(R.id.GramaticaSemRegrasDaCadeiaFNC);
        result.setText(academic.getResult());

        //Configura os comentários do processo
        academic.setComments(comments.toString());
        TextView commentsOfProcces = new TextView(this);
        commentsOfProcces = (TextView) findViewById(R.id.ComentarioFNC3);
        commentsOfProcces.setText(academic.getComments());

        //Configura o primeiro passo do processo
        TextView creatingSetOfChains = new TextView(this);
        creatingSetOfChains = (TextView) findViewById(R.id.Passo2RegrasDaCadeiaFNC);
        creatingSetOfChains.setText("(1) O primeiro passo do algoritmo é montar as cadeias de cada variável.");
        TableLayout tableOfChains = new TableLayout(this);
        tableOfChains = (TableLayout) findViewById(R.id.Passo2RegrasDaCadeiaFNC_Resultado);
        tableOfChains.setShrinkAllColumns(true);
        printTableOfChains(tableOfChains, academic, "Variável", "Cadeia");

        //Configura o segundo passo do processo
        if (academic.getInsertedRules().size() != 0) {
            TextView creatingGrammarWithoutChains = new TextView(this);
            creatingGrammarWithoutChains = (TextView) findViewById(R.id.Passo3RegrasDaCadeiaFNC);
            creatingGrammarWithoutChains.setText("(2) Subistituir as cadeias encontradas.");
            TableLayout tableWithoutChains = new TableLayout(this);
            tableWithoutChains = (TableLayout) findViewById(R.id.Passo3RegrasDaCadeiaFNC_Resultado);
            tableWithoutChains.setShrinkAllColumns(true);
            printGrammarWithNewRules(gc, tableWithoutChains, academic);
        } else {
            TextView creatingGrammarWithoutChains = new TextView(this);
            creatingGrammarWithoutChains = (TextView) findViewById(R.id.Passo3RegrasDaCadeiaFNC);
            creatingGrammarWithoutChains.setText("(2) Não há cadeias na gramática inserida.");
        }


        //Realiza o processo número 4 (Remoção de símbolos não terminais)
        academic = new AcademicSupport();

        Grammar gAux = (Grammar) gc.clone();
        gc = gc.getGrammarWithoutNoTerm(gc, academic);
        academic.setResult(gc);


        //Realiza comentários sobre o processo
        comments = new StringBuilder();
        comments.append("\t\tEsta transformação tem o objetivo de remover as regras que não são terminais, ou seja, todo o símbolo que não for capaz " +
                "de terminar uma forma sentencial, será removido.");

        //Configura a gramática de resultado
        TextView resultGrammar = new TextView(this);
        resultGrammar = (TextView) findViewById(R.id.GramaticaSemSimbolosNaoTerminaisFNC);
        resultGrammar.setText(academic.getResult());

        if (academic.getSituation()) {
            //Configura os comentários
            academic.setComments(comments.toString());
            result = new TextView(this);
            result = (TextView) findViewById(R.id.ComentariosNaoTerminaisFNC);
            result.setText(academic.getComments());

            //Configura o primeiro passo (Montar conjuntos)
             creatingSetOfChains = new TextView(this);
            creatingSetOfChains = (TextView) findViewById(R.id.Passo2NaoTerminaisFNC);
            creatingSetOfChains.setText("(1) O primeiro passo do algoritmo é determinar quais símbolos são terminais.");
            TableLayout tableOfSets = new TableLayout(this);
            tableOfSets = (TableLayout) findViewById(R.id.Passo2NaoTerminaisFNC_Resultado);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfSets(tableOfSets, academic, "TERM", "PREV");

            //Configura o segundo passo (Regras que foram removidas)
            TextView eliminatingNoTermRules = new TextView(this);
            eliminatingNoTermRules = (TextView) findViewById(R.id.Passo3NaoTerminaisFNC);
            eliminatingNoTermRules.setText("(2) O segundo passo do algoritmo é remover os símbolos não terminais.");
            TableLayout grammarWithoutOldRules = new TableLayout(this);
            grammarWithoutOldRules = (TableLayout) findViewById(R.id.Passo3NaoTerminaisFNC_Resultado);
            grammarWithoutOldRules.setShrinkAllColumns(true);
        } else {
            result = new TextView(this);
            result = (TextView) findViewById(R.id.ComentariosNaoTerminaisFNC);
            result.setText("A gramática inserida não possui símbolos não terminais.");
        }


        //Realiza o processo número 5 (Remoção de símbolos não alcançáveis)
        academic = new AcademicSupport();

        gAux = (Grammar) gc.clone();
        gc = gc.getGrammarWithoutNoReach(gc, academic);
        academic.setResult(gc);

        comments = new StringBuilder();

        //Realiza comentários sobre o processo
        comments.append("\t\tEsta transformação tem o objetivo de remover os símbolos que não alcançáveis no processo de derivação de uma palavra.\n");

        //Mostra o resultado do processo
        TextView tableOfResult = new TextView(this);
        tableOfResult = (TextView) findViewById(R.id.GramaticaSemSimbolosNaoAlcancaveisFNC);
        tableOfResult.setText(academic.getResult());

        if (academic.getSituation()) {
            //Configura comentários
            academic.setComments(comments.toString());
            TextView commentsOfNoReach = new TextView(this);
            commentsOfNoReach = (TextView) findViewById(R.id.ComentariosFNC5);
            commentsOfNoReach.setText(academic.getComments());

            //Primeiro passo do processo (Construção dos Conjuntos)
            TextView step1 = new TextView(this);
            step1 = (TextView) findViewById(R.id.Passo2RemocaoDeRegrasNaoAlcancaveisFNC);
            step1.setText("(1) O primeiro passo do algoritmo é determinar quais símbolos são alcançáveis.");
            TableLayout tableOfSets = new TableLayout(this);
            tableOfSets = (TableLayout) findViewById(R.id.Passo2RemocaoDeSimbolosNaoAlcancaveisFNC_Resultado);
            tableOfSets.setShrinkAllColumns(true);
            tableOfSets.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
            printTableOfReachSets(tableOfSets, academic, "REACH", "PREV", "NEW");

            //Segundo passo do processo (Eliminar os símbolos não alcançáveis)
            if (academic.getIrregularRules().size() != 0) {
                TextView step2 = new TextView(this);
                step2 = (TextView) findViewById(R.id.Passo3RemocaoDeSimbolosNaoAlcancaveisFNC);
                step2.setText("(2) O segundo passo do algoritmo é remover os símbolos que não são alcançáveis.");
                TableLayout tableResult = new TableLayout(this);
                tableResult = (TableLayout) findViewById(R.id.Passo3RemocaoDeSimbolosNaoAlcancaveisFNC_Resultado);
                tableResult.setShrinkAllColumns(true);
                printOldGrammarOfTermAndReach(gAux, tableResult, academic);
            } else {
                TextView step2 = new TextView(this);
                step2 = (TextView) findViewById(R.id.Passo3RemocaoDeSimbolosNaoAlcancaveisFNC);
                step2.setText("(2) Todos os símbolos são alcançáveis.");
            }

        } else {
            TextView commentsOfNoReach = new TextView(this);
            commentsOfNoReach = (TextView) findViewById(R.id.Passo3RemocaoDeSimbolosNaoAlcancaveisFNC);
            commentsOfNoReach.setText("\t\tNão há símbolos alcançáveis na gramática inserida.");
        }

        //Realiza o passo número 5 no processo (Forma Normal de Chomsky)
        academic = new AcademicSupport();
        gAux = (Grammar) gc.clone();
        gc = gc.FNC(gc, academic);
        academic.setResult(gc);

        comments = new StringBuilder();

        //Realiza comentários sobre o processo
        comments.append("\t\tUma GLC G = (V,Σ,P,S) está na Forma Normal de Chomsky se suas regras tem uma das seguinte propriedades:\n" +
                        "\t\t- A -> BC\t onde B, C ∈ V − {S}\n" +
                        "\t\t- A -> a\t onde a ∈ Σ\n" +
                        "\t\t- S → λ");

        //Coloca resultado de FNC na tela
        TextView chomskyResult = new TextView(this);
        chomskyResult = (TextView) findViewById(R.id.FNCResultado);
        chomskyResult.setText(academic.getResult());

        if (academic.getSituation()) {
            //Coloca comentários do processo na tela
            academic.setComments(comments.toString());
            TextView commentsOfFNC = new TextView(this);
            commentsOfFNC = (TextView) findViewById(R.id.ComentariosFNC6);
            commentsOfFNC.setText(academic.getComments());

            //Realiza segundo passo do processo (Mostra gramática com destaques e sem estar em FNC)
            TextView step1FNC = new TextView(this);
            step1FNC = (TextView) findViewById(R.id.Passo2FNC);
            step1FNC.setText("(1) Identificar as regras que não estão na Forma Normal de Chomsky.");
            TableLayout redGrammar = new TableLayout(this);
            redGrammar = (TableLayout) findViewById(R.id.Passo2FNC_Resultado);
            redGrammar.setShrinkAllColumns(true);
            printGrammarWithoutOldRules(gAux, redGrammar, academic);

            //Realiza o terceiro passo do processo (Mostra gramática com destaques em FNC)
            TextView step2FNC = new TextView(this);
            step2FNC = (TextView) findViewById(R.id.Passo3FNC);
            step2FNC.setText("(2) Deixar as regras em um dos formatos aceitos pela formalização.");
            TableLayout blueGrammar  = new TableLayout(this);
            blueGrammar = (TableLayout) findViewById(R.id.Passo3FNC_Resultado);
            blueGrammar.setShrinkAllColumns(true);
            printGrammarWithNewRules(gc, blueGrammar, academic);
        } else {
            TextView commentsOfFNC = new TextView(this);
            commentsOfFNC = (TextView) findViewById(R.id.ComentariosFNC6);
            commentsOfFNC.setText("A gramática inserida já está na Forma Normal de Chomsky.");
        }
    }

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

            //Primeiro passo do processo
            TextView step1 = new TextView(this);
            step1 = (TextView) findViewById(R.id.Step1DirectLeftRecursion);
            step1.setText("(1) O primeiro passo é identificar a recursão.");
            TableLayout tableOfRecursion = new TableLayout(this);
            tableOfRecursion = (TableLayout) findViewById(R.id.tableWithDirectLeftRecursion);
            tableOfRecursion.setShrinkAllColumns(true);
            printGrammarWithoutOldRules(g, tableOfRecursion, academicSupport);

            //Segundo passo do processo
            TextView step2 = new TextView(this);
            step2 = (TextView) findViewById(R.id.Step2DirectLeftRecursion);
            step2.setText("(2) O segundo passo é resolver a recursão.");
            TableLayout tableOfResult = new TableLayout(this);
            tableOfResult = (TableLayout) findViewById(R.id.tableWithoutDirectLeftRecursion);
            tableOfResult.setShrinkAllColumns(true);
            printGrammarWithNewRules(gc, tableOfResult, academicSupport);
        } else {
            TextView commentsOfProcess = new TextView(this);
            commentsOfProcess = (TextView) findViewById(R.id.CommentsDirectLeftRecursion);
            commentsOfProcess.setText("A gramática inserida não possui recursão direta à esquerda.");
        }

    }

    public void removingLeftRecursion(final Grammar g) {
        Grammar gc = (Grammar) g.clone();

        //Realiza processo
        AcademicSupport academicSupport = new AcademicSupport();
        Map<String, String> sortedVariables = new HashMap<>();
        gc = gc.removingLeftRecursion(gc, academicSupport, sortedVariables);
        academicSupport.setResult(gc);
        TextView resultGrammar = new TextView(this);
        resultGrammar = (TextView) findViewById(R.id.RemovalLeftRecursion);
        resultGrammar.setText(academicSupport.getResult());

       if (academicSupport.getSituation()) {
           //Realiza comentários sobre o processo
           TextView text = new TextView(this);
           text = (TextView) findViewById(R.id.commentsOfRemovalLeftRecursion);
           text.setText("A remoção de recursão à esquerda consiste em ordenar as variáveis da gramática e organizar as regras da forma que a variável do lado" +
                   "esquerdo sempre possua valor menor do que a variável do lado direito.");
           academicSupport.setComments(text.toString());

           //Realiza o primeiro passo do processo (Ordenação das variáveis)
           TextView step1 = new TextView(this);
           step1 = (TextView) findViewById(R.id.step1RemovalLeftRecursion);
           step1.setText("(1) O primeiro passo é ordenar as variáveis da gramática.");
           TableLayout tableOfSortedVariables = new TableLayout(this);
           tableOfSortedVariables = (TableLayout) findViewById(R.id.tableOfSortedVariables);
           tableOfSortedVariables.setShrinkAllColumns(true);
           printMap(tableOfSortedVariables, gc, sortedVariables, "Variável", "Valor");

           //Realiza segundo passo do processo (Destaca recursões encontradas)
           TextView step2 = new TextView(this);
           step1 = (TextView) findViewById(R.id.step2RemovalLeftRecursion);
           step1.setText("(2) O segundo passo é localizar as recursões.");
           TableLayout tableOfIrregularRules = new TableLayout(this);
           tableOfIrregularRules = (TableLayout) findViewById(R.id.tableOfLeftRecursion);
           tableOfIrregularRules.setShrinkAllColumns(true);
           printGrammarWithoutOldRules(academicSupport.getGrammar(), tableOfIrregularRules, academicSupport);

           //Realiza terceiro passo do processo (Destaca as mudanças finais)
           TextView step3 = new TextView(this);
           step3 = (TextView) findViewById(R.id.step3RemovalLeftRecursion);
           step3.setText("(3) Alterar as regras");
           TableLayout tableOfNewRules = new TableLayout(this);
           tableOfNewRules = (TableLayout) findViewById(R.id.tableWithoutLeftRecursion);
           tableOfNewRules.setShrinkAllColumns(true);
           printGrammarWithNewRules(gc, tableOfNewRules, academicSupport);


       } else {
           TextView text = new TextView(this);
           text = (TextView) findViewById(R.id.commentsOfRemovalLeftRecursion);
           if (academicSupport.getSolutionDescription().isEmpty()) {
               text.setText("A gramática inserida não possui recursão à esquerda.");
           } else {
               text.setText(academicSupport.getSolutionDescription());
           }

       }



    }

    public void fng(final Grammar g) {
        Grammar gc = (Grammar) g.clone();

        //Realiza todos os processos anteriores a Greibach
        AcademicSupport academic = new AcademicSupport();
        gc = gc.getGrammarWithInitialSymbolNotRecursive(gc, academic);

        academic = new AcademicSupport();
        gc = gc.getGrammarEssentiallyNoncontracting(gc, academic);

        academic = new AcademicSupport();
        gc = gc.getGrammarWithoutChainRules(gc, academic);

        academic = new AcademicSupport();
        gc = gc.getGrammarWithoutNoTerm(gc, academic);

        academic = new AcademicSupport();
        gc = gc.getGrammarWithoutNoReach(gc, academic);

        academic = new AcademicSupport();
        gc = gc.FNC(gc, academic);

        academic = new AcademicSupport();
        gc = gc.removingTheImmediateLeftRecursion(gc, academic);

        //Realiza processo de remoção de recursão à esquerda
        academic = new AcademicSupport();
        Map<String, String> sortedVariables = new HashMap<>();
        gc = gc.removingLeftRecursion(gc, academic, sortedVariables);

        academic.setResult(gc);
        TextView resultGrammar = new TextView(this);
        resultGrammar = (TextView) findViewById(R.id.ResultadoFNG1_1);
        resultGrammar.setText(academic.getResult());

        if (academic.getSituation()) {
            //Realiza comentários sobre o processo
            TextView text = new TextView(this);
            text = (TextView) findViewById(R.id.FNGComentarios1);
            text.setText("A remoção de recursão à esquerda consiste em ordenar as variáveis da gramática e organizar as regras da forma que a variável do lado" +
                    "esquerdo sempre possua valor menor do que a variável do lado direito.");
            academic.setComments(text.toString());

            //Realiza o primeiro passo do processo (Ordenação das variáveis)
            TextView step1 = new TextView(this);
            step1 = (TextView) findViewById(R.id.FNGPasso1_1);
            step1.setText("(1) O primeiro passo é ordenar as variáveis da gramática.");
            TableLayout tableOfSortedVariables = new TableLayout(this);
            tableOfSortedVariables = (TableLayout) findViewById(R.id.FNGStep1_1_Resultado);
            tableOfSortedVariables.setShrinkAllColumns(true);
            printMap(tableOfSortedVariables, gc, sortedVariables, "Variável", "Valor");

            //Realiza segundo passo do processo (Destaca recursões encontradas)
            TextView step2 = new TextView(this);
            step1 = (TextView) findViewById(R.id.FNGPasso1_2);
            step1.setText("(2) O segundo passo é localizar as recursões.");
            TableLayout tableOfIrregularRules = new TableLayout(this);
            tableOfIrregularRules = (TableLayout) findViewById(R.id.FNGPasso1_2_Resultado);
            tableOfIrregularRules.setShrinkAllColumns(true);
            printGrammarWithoutOldRules(academic.getGrammar(), tableOfIrregularRules, academic);

            //Realiza terceiro passo do processo (Destaca as mudanças finais)
            TextView step3 = new TextView(this);
            step3 = (TextView) findViewById(R.id.FNGPasso1_3);
            step3.setText("(3) Alterar as regras");
            TableLayout tableOfNewRules = new TableLayout(this);
            tableOfNewRules = (TableLayout) findViewById(R.id.FNGPasso1_3_Resultado);
            tableOfNewRules.setShrinkAllColumns(true);
            printGrammarWithNewRules(gc, tableOfNewRules, academic);

        } else {
            TextView text = new TextView(this);
            text = (TextView) findViewById(R.id.FNGComentarios1);
            if (academic.getSolutionDescription().isEmpty()) {
                text.setText("A gramática inserida não possui recursão à esquerda.");
            } else {
                text.setText(academic.getSolutionDescription());
            }
        }

        //Coloca a gramatica na Forma Normal de Greibach
        academic = new AcademicSupport();
        gc = gc.FNG(gc, academic);
        academic.setResult(gc);

        //Coloca resultado na tela
        TextView resultFNG = new TextView(this);
        resultFNG = (TextView) findViewById(R.id.ResultadoFNG2_1);
        resultFNG.setText(academic.getResult());

        //Realiza comentários do processo
        StringBuilder comments = new StringBuilder();
        comments.append("\t\tUma GLC G = (V , Σ, P, S) está na FN de Greibach se suas\n" +
                "regras têm uma das seguintes formas:\n" +
                "- A -> A1A2A3...An\t\t onde a ∈ Σ e A1..n ∈ V − {S}]\n" +
                "- A -> a\t\t onde a ∈ Σ\n" +
                "- A -> λ");

        if (academic.getSituation()) {
            //Insere os comentários na tela
            TextView commentsOfFNG = new TextView(this);
            commentsOfFNG = (TextView) findViewById(R.id.FNGComentarios2);
            commentsOfFNG.setText(comments.toString());

            //Realiza o primeiro passo do processo (Destacar regras irregulares)
            TextView step1FNG = new TextView(this);
            step1FNG = (TextView) findViewById(R.id.FNGPasso2_1);
            step1FNG.setText("(1) Localizar as regras que não estão na Forma Normal de Greibach.");
            TableLayout redGrammar = new TableLayout(this);
            redGrammar = (TableLayout) findViewById(R.id.FNGStep2_1_Resultado);
            redGrammar.setShrinkAllColumns(true);
            printGrammarWithoutOldRules(gc, redGrammar, academic);

            //Realiza o segundo passo do processo (Destacar as novas regras inseridas no processo)
            TextView step2FNG = new TextView(this);
            step2FNG = (TextView) findViewById(R.id.FNGPasso2_2);
            step2FNG.setText("(2) Criar as regras referentes à formalização.");
            TableLayout blueGrammar = new TableLayout(this);
            redGrammar = (TableLayout) findViewById(R.id.FNGPasso2_2_Resultado);
            redGrammar.setShrinkAllColumns(true);
            printGrammarWithNewRules(gc, blueGrammar, academic);

        } else {
            TextView commentsOfFNG = new TextView(this);
            commentsOfFNG = (TextView) findViewById(R.id.FNGComentarios2);
            commentsOfFNG.setText("A gramática inserida já está na Forma Normal de Greibach.");
        }
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
        String teste = grammar.getVariables().toString();
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

    /**
     * Método que imprime a tabela de variáveis ordenadas
     * @param table
     * @param g
     * @param map
     * @param firstValue
     * @param secondValue
     */
    private void printMap(TableLayout table, final Grammar g, final Map<String, String> map, String firstValue, String secondValue) {
        //Configura cabeçalho
        TableRow header = new TableRow(this);
        header.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        TextView htv0 = new TextView(this);
        htv0.setPadding(10, 10, 10, 10);
        htv0.setText(firstValue);
        TextView htv1 = new TextView(this);
        htv1.setPadding(10, 10, 10, 10);
        htv1.setText(secondValue);
        header.addView(htv0);
        header.addView(htv1);
        table.addView(header);

        int i = 1;
        while (i - 1 != map.size()) {
            for (String variable : g.getVariables()) {
                if ((map.get(variable) != null) && (Integer.parseInt(map.get(variable)) == i)) {
                    TableRow row = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setPadding(10, 10, 10, 10);
                    tv0.setText(variable);
                    TextView tv1 = new TextView(this);
                    tv1.setPadding(10, 10, 10, 10);
                    tv1.setText(map.get(variable));
                    if (i % 2 == 0) {
                        row.setBackgroundColor(getResources().getColor(R.color.DarkGray));
                    } else {
                        row.setBackgroundColor(getResources().getColor(R.color.Gainsboro));
                    }
                    row.addView(tv0);
                    row.addView(tv1);
                    table.addView(row);
                    i++;
                }
            }
        }
    }

}
