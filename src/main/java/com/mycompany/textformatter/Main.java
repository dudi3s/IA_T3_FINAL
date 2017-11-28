/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textformatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;

/**
 *
 * @author Eduardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
        Analyzer cogroo = factory.createPipe();
        Document document = document = new DocumentImpl();

        String line = "";
        String input = "";
        String encode = "UTF-8";
        String c1 = "src/corpus/input/esporte.txt";
        String c2 = "src/corpus/input/policia.txt";
        String c3 = "src/corpus/input/problema.txt";
        String c4 = "src/corpus/input/trabalhador.txt";

        int qtd1 = 95;
        int qtd2 = 89;
        int qtd3 = 74;
        int qtd4 = 78;

        int contadorTexto = 0;

        List<String> verbosLig = new ArrayList<>();
        verbosLig.add("ser");
        verbosLig.add("estar");
        verbosLig.add("parecer");
        verbosLig.add("permanecer");
        verbosLig.add("ficar");
        verbosLig.add("continuar");
        verbosLig.add("andar");
        verbosLig.add("tornar");
        verbosLig.add("ano");
        verbosLig.add("nao");
        verbosLig.add("ter");
        verbosLig.add("mais");
        verbosLig.add("ir");
        verbosLig.add("ja");
        verbosLig.add("como");
        verbosLig.add("fazer");
        verbosLig.add("haver");
        verbosLig.add("poder");
        verbosLig.add("muito");
        verbosLig.add("so");

        int classe = 1;

        StringBuilder texto;

        List<TextoFinal> textosFinalTreino = new ArrayList<>();
        List<TextoFinal> textosFinalTeste = new ArrayList<>();
        List<Palavra> bowUnigrama = new ArrayList<>();
        List<Palavra> bowBigrama = new ArrayList<>();
        List<Palavra> bowTrigrama = new ArrayList<>();

        while (classe < 5) {
            switch (classe) {
                case 1:
                    input = c1;
                    contadorTexto = (int) (0.8 * qtd1);
                    break;
                case 2:
                    input = c2;
                    contadorTexto = (int) (0.8 * qtd2);
                    break;
                case 3:
                    input = c3;
                    contadorTexto = (int) (0.8 * qtd3);
                    break;
                case 4:
                    input = c4;
                    contadorTexto = (int) (0.8 * qtd4);
                    break;
                default:
                    break;
            }

            String e = input.split("/")[3];
            String nomeClasse = e.substring(0, e.length() - 4);

            List<Palavra> unigramaClasse = new ArrayList<>();
            List<Palavra> bigramaClasse = new ArrayList<>();
            List<Palavra> trigramaClasse = new ArrayList<>();

            System.out.println("");
            System.out.println("CLASSE: " + nomeClasse + " == Analisando input ==");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), encode));
                line = reader.readLine();

                while (line != null) {
                    if (line.startsWith("TEXTO")) {
                        List<Palavra> palavrasTexto = new ArrayList<>();
                        List<Palavra> unigrama = new ArrayList<>();
                        List<Palavra> bigrama = new ArrayList<>();
                        List<Palavra> trigrama = new ArrayList<>();

                        String textoAtual = line;

                        line = reader.readLine();
                        texto = new StringBuilder();
                        texto.append(line);

                        //System.out.println("Analisando " + textoAtual + "...");
                        document.setText(texto.toString());
                        cogroo.analyze(document);

                        //System.out.println("Processando palavras...");
                        for (Sentence sentence : document.getSentences()) {
                            for (Token token : sentence.getTokens()) {
                                String tag = token.getPOSTag();

                                Pattern p = Pattern.compile("^[A-z]");
                                Matcher m = p.matcher(tag);
                                boolean ce = m.find();
                                m.reset();

                                if (tag.contains("conj") || tag.contains("art") || tag.contains("pron") || tag.contains("num") || !ce) {
                                } else {
                                    for (String l : token.getLemmas()) {
                                        String lsemAcento = removerAcentos(l);
                                        if (!verbosLig.contains(lsemAcento)) {
                                            boolean found = false;

                                            for (int i = 0; i < palavrasTexto.size(); i++) {
                                                Palavra auxPal = palavrasTexto.get(i);

                                                if (auxPal.getPalavra().equals(lsemAcento)) {
                                                    auxPal.setFrequencia(auxPal.getFrequencia() + 1);
                                                    found = true;
                                                    break;
                                                }
                                            }

                                            if (!found) {
                                                Palavra pal = new Palavra(lsemAcento, tag, 1);
                                                palavrasTexto.add(pal);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for (Palavra pAux : palavrasTexto) {
                            if (!pAux.getClasse().equals("prp")) {
                                unigrama.add(pAux);

                                if (contadorTexto > 0) {
                                    if (unigramaClasse.contains(pAux)) {
                                        int index = unigramaClasse.indexOf(pAux);
                                        Palavra p = unigramaClasse.get(index);

                                        p.setFrequencia(p.getFrequencia() + pAux.getFrequencia());
                                    } else {
                                        unigramaClasse.add(pAux);
                                    }
                                }
                            }
                        }

                        //System.out.println("Processando Bigrama & Trigrama...");
                        int i;
                        for (i = 0; i < palavrasTexto.size() - 1; i++) {
                            String pA = palavrasTexto.get(i).getPalavra();
                            String pB = palavrasTexto.get(i + 1).getPalavra();

                            Palavra bi = new Palavra(pA + "_" + pB);
                            bigrama.add(bi);

                            if (bigramaClasse.contains(bi)) {
                                int index = bigramaClasse.indexOf(bi);
                                Palavra p = bigramaClasse.get(index);

                                p.setFrequencia(p.getFrequencia() + bi.getFrequencia());
                            } else {
                                bigramaClasse.add(bi);
                            }
                        }

                        for (i = 0; i < palavrasTexto.size() - 2; i++) {
                            String pA = palavrasTexto.get(i).getPalavra();
                            String pB = palavrasTexto.get(i + 1).getPalavra();
                            String pC = palavrasTexto.get(i + 2).getPalavra();

                            Palavra tri = new Palavra(pA + "_" + pB + "_" + pC);
                            trigrama.add(tri);

                            if (trigramaClasse.contains(tri)) {
                                int index = trigramaClasse.indexOf(tri);
                                Palavra p = trigramaClasse.get(index);

                                p.setFrequencia(p.getFrequencia() + tri.getFrequencia());
                            } else {
                                trigramaClasse.add(tri);
                            }
                        }
//
//                        //System.out.println("Ordenando Coleções...");
//                        Collections.sort(palavrasTextoSemPrep);

                        TextoFinal tAux = new TextoFinal(textoAtual, unigrama, bigrama, trigrama, nomeClasse);

                        if (contadorTexto > 0) {
                            textosFinalTreino.add(tAux);
                            contadorTexto--;
                        } else {
                            textosFinalTeste.add(tAux);
                        }
                    }

                    line = reader.readLine();
                }

                System.out.println("CLASSE: " + nomeClasse + " == Processando Unigrama ==");
                Collections.sort(unigramaClasse);
                Collections.sort(bigramaClasse);
                Collections.sort(trigramaClasse);

                int k = 3;
                int i = 0;

                while (k > 0 && i < unigramaClasse.size()) {
                    Palavra auxFreq = unigramaClasse.get(i);

                    if (bowUnigrama.contains(auxFreq)) {
                        int indexUni = bowUnigrama.indexOf(auxFreq);
                        Palavra bowUni = bowUnigrama.get(indexUni);

                        bowUni.setFrequencia(bowUni.getFrequencia() + auxFreq.getFrequencia());
                    } else {
                        bowUnigrama.add(auxFreq);
                        k--;
                    }

                    i++;
                }

                System.out.println("CLASSE: " + nomeClasse + " == Processando Bigrama ==");

                k = 3;
                i = 0;

                while (k > 0 && i < bigramaClasse.size()) {
                    Palavra biFreq = bigramaClasse.get(i);

                    if (bowBigrama.contains(biFreq)) {
                        int indexBi = bowBigrama.indexOf(biFreq);
                        Palavra bowBi = bowBigrama.get(indexBi);

                        bowBi.setFrequencia(bowBi.getFrequencia() + biFreq.getFrequencia());
                    } else {
                        bowBigrama.add(biFreq);
                        k--;
                    }

                    i++;
                }

                System.out.println("CLASSE: " + nomeClasse + " == Processando Trigrama ==");

                k = 3;
                i = 0;

                while (k > 0 && i < trigramaClasse.size()) {
                    Palavra triFreq = trigramaClasse.get(i);

                    if (bowTrigrama.contains(triFreq)) {
                        int indexTri = bowTrigrama.indexOf(triFreq);
                        Palavra bowTri = bowTrigrama.get(indexTri);

                        bowTri.setFrequencia(bowTri.getFrequencia() + triFreq.getFrequencia());
                    } else {
                        bowTrigrama.add(triFreq);
                        k--;
                    }

                    i++;
                }

                reader.close();

            } catch (FileNotFoundException ex) {
                System.out.println("Unable to open file '" + input + "'");
            } catch (IOException ex) {
                System.out.println("Error reading file '" + input + "'");
            }

            classe++;
        }

        System.out.println("== Iniciando geração arquivos de treino e teste ==");
        List<Integer> vetorAux;
        String saidaTreinoUnigrama = "src/corpus/uniGramaTreino.txt";
        String saidaTesteUnigrama = "src/corpus/uniGramaTeste.txt";

        String saidaTreinoBigrama = "src/corpus/biGramaTreino.txt";
        String saidaTesteBigrama = "src/corpus/biGramaTeste.txt";

        String saidaTreinoTrigrama = "src/corpus/triGramaTreino.txt";
        String saidaTesteTrigrama = "src/corpus/triGramaTeste.txt";

        BufferedWriter arquivoTreinoUnigrama = new BufferedWriter(new FileWriter(saidaTreinoUnigrama));
        BufferedWriter arquivoTesteUnigrama = new BufferedWriter(new FileWriter(saidaTesteUnigrama));

        BufferedWriter arquivoTreinoBigrama = new BufferedWriter(new FileWriter(saidaTreinoBigrama));
        BufferedWriter arquivoTesteBigrama = new BufferedWriter(new FileWriter(saidaTesteBigrama));

        BufferedWriter arquivoTreinoTrigrama = new BufferedWriter(new FileWriter(saidaTreinoTrigrama));
        BufferedWriter arquivoTesteTrigrama = new BufferedWriter(new FileWriter(saidaTesteTrigrama));

        arquivoTreinoUnigrama.write("@relation uniGramaTeste\n");
        arquivoTesteUnigrama.write("@relation uniGramaTeste\n");

        arquivoTreinoBigrama.write("@relation biGramaTeste\n");
        arquivoTesteBigrama.write("@relation biGramaTeste\n");

        arquivoTreinoTrigrama.write("@relation triGramaTeste\n");
        arquivoTesteTrigrama.write("@relation triGramaTeste\n");

        System.out.println("== Gravando atributos ==");
        int p;
        for (p = 0; p < bowUnigrama.size(); p++) {
            Palavra attUni = bowUnigrama.get(p);
            arquivoTreinoUnigrama.write("@attribute " + attUni.getPalavra() + " numeric\n");
            arquivoTesteUnigrama.write("@attribute " + attUni.getPalavra() + " numeric\n");

            Palavra attBi = bowBigrama.get(p);
            arquivoTreinoBigrama.write("@attribute " + attBi.getPalavra() + " numeric\n");
            arquivoTesteBigrama.write("@attribute " + attBi.getPalavra() + " numeric\n");

            Palavra attTri = bowTrigrama.get(p);
            arquivoTreinoTrigrama.write("@attribute " + attTri.getPalavra() + " numeric\n");
            arquivoTesteTrigrama.write("@attribute " + attTri.getPalavra() + " numeric\n");
        }

        arquivoTreinoUnigrama.write("@attribute class {esporte,policia,problema,trabalhador}\n");
        arquivoTesteUnigrama.write("@attribute class {esporte,policia,problema,trabalhador}\n");

        arquivoTreinoBigrama.write("@attribute class {esporte,policia,problema,trabalhador}\n");
        arquivoTesteBigrama.write("@attribute class {esporte,policia,problema,trabalhador}\n");

        arquivoTreinoTrigrama.write("@attribute class {esporte,policia,problema,trabalhador}\n");
        arquivoTesteTrigrama.write("@attribute class {esporte,policia,problema,trabalhador}\n");

        arquivoTreinoUnigrama.write("@data\n");
        arquivoTesteUnigrama.write("@data\n");

        arquivoTreinoBigrama.write("@data\n");
        arquivoTesteBigrama.write("@data\n");

        arquivoTreinoTrigrama.write("@data\n");
        arquivoTesteTrigrama.write("@data\n");

        System.out.println("== Gravando dados de TREINO ==");
        for (int h = 0; h < textosFinalTreino.size(); h++) {
            TextoFinal tF = textosFinalTreino.get(h);
            vetorAux = new ArrayList<>();
            Palavra pGen;

            for (p = 0; p < bowUnigrama.size(); p++) {
                pGen = bowUnigrama.get(p);

                if (tF.getUni_grama().contains(pGen)) {
                    vetorAux.add(1);
                } else {
                    vetorAux.add(0);
                }
            }

            String uni2s = printaVetor(vetorAux) + " " + tF.getClasse() + '\n';
            arquivoTreinoUnigrama.write(uni2s);

            vetorAux.clear();
            for (p = 0; p < bowBigrama.size(); p++) {
                pGen = bowBigrama.get(p);

                if (tF.getBi_grama().contains(pGen)) {
                    vetorAux.add(1);
                } else {
                    vetorAux.add(0);
                }
            }

            String bi2s = printaVetor(vetorAux) + " " + tF.getClasse() + '\n';
            arquivoTreinoBigrama.write(bi2s);

            vetorAux.clear();
            for (p = 0; p < bowTrigrama.size(); p++) {
                pGen = bowTrigrama.get(p);

                if (tF.getTri_grama().contains(pGen)) {
                    vetorAux.add(1);
                } else {
                    vetorAux.add(0);
                }
            }

            String tri2s = printaVetor(vetorAux) + " " + tF.getClasse() + '\n';
            arquivoTreinoTrigrama.write(tri2s);

        }

        System.out.println("== Gravando dados de TESTE ==");
        for (int h = 0; h < textosFinalTeste.size(); h++) {
            TextoFinal tF = textosFinalTeste.get(h);
            vetorAux = new ArrayList<>();
            Palavra pGen;

            for (p = 0; p < bowUnigrama.size(); p++) {
                pGen = bowUnigrama.get(p);

                if (tF.getUni_grama().contains(pGen)) {
                    vetorAux.add(1);
                } else {
                    vetorAux.add(0);
                }
            }

            String v2s = printaVetor(vetorAux) + " " + tF.getClasse() + '\n';
            arquivoTesteUnigrama.write(v2s);

            vetorAux.clear();
            for (p = 0; p < bowBigrama.size(); p++) {
                pGen = bowBigrama.get(p);

                if (tF.getBi_grama().contains(pGen)) {
                    vetorAux.add(1);
                } else {
                    vetorAux.add(0);
                }
            }

            String bi2s = printaVetor(vetorAux) + " " + tF.getClasse() + '\n';
            arquivoTesteBigrama.write(bi2s);

            vetorAux.clear();
            for (p = 0; p < bowTrigrama.size(); p++) {
                pGen = bowTrigrama.get(p);

                if (tF.getTri_grama().contains(pGen)) {
                    vetorAux.add(1);
                } else {
                    vetorAux.add(0);
                }
            }

            String tri2s = printaVetor(vetorAux) + " " + tF.getClasse() + '\n';
            arquivoTesteTrigrama.write(tri2s);
        }

        System.out.println("== Finalizando ==");
        arquivoTreinoUnigrama.close();
        arquivoTesteUnigrama.close();

        arquivoTreinoBigrama.close();
        arquivoTesteBigrama.close();

        arquivoTreinoTrigrama.close();
        arquivoTesteTrigrama.close();
    }

    private static String printaVetor(List<Integer> vetor) {
        StringBuilder retorno = new StringBuilder();
        for (int i = 0; i < vetor.size(); i++) {
            if (i < vetor.size() - 1) {
                retorno.append(vetor.get(i));
                retorno.append(",");
            } else {
                retorno.append(vetor.get(i));
            }
        }

        return retorno.toString();
    }

    private static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
