package edu.upf.taln.scisumm2016.evaluation;

import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.reader.AnnotationV3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ahmed on 1/21/17.
 */
public class EvaluationPack {
    public static void main(String args[]) {
        String rfolderName = args[1];
        String baseFolder = args[2] + File.separator + rfolderName;
        String outputFolder = baseFolder + "/Output/";
        String inputFolder = baseFolder + "/input/";
        String modelsFolder = baseFolder + "/Models/";
        String dataStructuresFolder = baseFolder + "/ARFFs/";

        ArrayList<Double> matchProbability = new ArrayList<>(8);
        ArrayList<Integer> maxMatch = new ArrayList<>(3);

        matchProbability.add(Double.valueOf(0.40));
        matchProbability.add(Double.valueOf(0.50));
        matchProbability.add(Double.valueOf(0.60));
        matchProbability.add(Double.valueOf(0.70));
        matchProbability.add(Double.valueOf(0.80));
        matchProbability.add(Double.valueOf(0.90));
        matchProbability.add(Double.valueOf(0.95));
        matchProbability.add(Double.valueOf(0.975));

        maxMatch.add(1);
        maxMatch.add(2);
        maxMatch.add(3);

        for (int match : maxMatch) {
            for (Double prob : matchProbability) {
                HashMap<String, Set<String>> outputMatches = new HashMap<String, Set<String>>();
                HashMap<String, Set<String>> outputFacets = new HashMap<String, Set<String>>();
                HashMap<String, Set<String>> goldMatches = new HashMap<String, Set<String>>();
                HashMap<String, Set<String>> goldFacets = new HashMap<String, Set<String>>();

                ArrayList<AnnotationV3> outputAnnotationV3List;
                ArrayList<AnnotationV3> goldStandardAnnotationV3List;

                outputAnnotationV3List = Utilities.extractOutputAnnotationsV3FromBaseFolder(new File(baseFolder), match, prob.toString(), true);
                goldStandardAnnotationV3List = Utilities.extractAnnotationsV3FromBaseFolder(new File(baseFolder), true);

                //Gold Standard
                for (int i = 0; i < goldStandardAnnotationV3List.size(); i++) {
                    String goldCitingArticle = goldStandardAnnotationV3List.get(i).getCiting_Article();

                    Set<String> facets = new HashSet<>();
                    Set<String> t = new HashSet<>();
                    if (goldStandardAnnotationV3List.get(i).getDiscourse_Facet().contains(",")) {
                        for (String facet : goldStandardAnnotationV3List.get(i).getDiscourse_Facet().trim().split(",")) {
                            facets.add(facet.replaceAll("[^a-zA-Z0-9_\\s]+", ""));
                        }
                    } else {
                        facets.add(goldStandardAnnotationV3List.get(i).getDiscourse_Facet());
                    }

                    for (String facet : facets) {
                        switch (facet) {
                            case "Aim_Citation":
                            case "Aim_Facet":
                                facet = "AIM";
                                break;
                            case "Hypothesis_Citation":
                            case "Hypothesis_Facet":
                                facet = "HYPOTHESIS";
                                break;
                            case "Method_Citation":
                            case "Method_Facet":
                                facet = "METHOD";
                                break;
                            case "Results_Citation":
                            case "Results_Facet":
                                facet = "RESULT";
                                break;
                            case "Implication_Citation":
                            case "Implication_Facet":
                                facet = "IMPLICATION";
                                break;
                        }
                        t.add(facet);
                    }
                    facets.clear();
                    facets.addAll(t);


                    for (String goldCitationOffset : goldStandardAnnotationV3List.get(i).getCitation_Offset()) {
                        //references match
                        for (String goldReferenceOffset : goldStandardAnnotationV3List.get(i).getReference_Offset()) {
                            if (goldMatches.containsKey(goldCitingArticle + "_" + goldCitationOffset)) {
                                Set temp = goldMatches.get(goldCitingArticle + "_" + goldCitationOffset);
                                temp.add(goldReferenceOffset);
                                goldMatches.put(goldCitingArticle + "_" + goldCitationOffset, temp);
                            } else {
                                Set temp = new HashSet<>();
                                temp.add(goldReferenceOffset);
                                goldMatches.put(goldCitingArticle + "_" + goldCitationOffset, temp);
                            }
                        }
                        //facet
                        if (goldFacets.containsKey(goldCitingArticle + "_" + goldCitationOffset)) {
                            Set temp = goldFacets.get(goldCitingArticle + "_" + goldCitationOffset);
                            temp.addAll(facets);
                            goldFacets.put(goldCitingArticle + "_" + goldCitationOffset, temp);
                        } else {
                            Set temp = new HashSet<>();
                            temp.addAll(facets);
                            goldFacets.put(goldCitingArticle + "_" + goldCitationOffset, temp);
                        }
                    }
                }

                //System output
                for (int i = 0; i < outputAnnotationV3List.size(); i++) {
                    String outputCitingArticle = outputAnnotationV3List.get(i).getCiting_Article();
                    for (String outputCitationOffset : outputAnnotationV3List.get(i).getCitation_Offset()) {
                        //references match
                        for (String outputReferenceOffset : outputAnnotationV3List.get(i).getReference_Offset()) {
                            if (outputMatches.containsKey(outputCitingArticle + "_" + outputCitationOffset)) {
                                Set temp = outputMatches.get(outputCitingArticle + "_" + outputCitationOffset);
                                temp.add(outputReferenceOffset);
                                outputMatches.put(outputCitingArticle + "_" + outputCitationOffset, temp);
                            } else {
                                Set temp = new HashSet<>();
                                temp.add(outputReferenceOffset);
                                outputMatches.put(outputCitingArticle + "_" + outputCitationOffset, temp);
                            }
                        }
                        //facet
                        if (outputFacets.containsKey(outputCitingArticle + "_" + outputCitationOffset)) {
                            Set temp = outputFacets.get(outputCitingArticle + "_" + outputCitationOffset);
                            temp.add(outputAnnotationV3List.get(i).getDiscourse_Facet());
                            outputFacets.put(outputCitingArticle + "_" + outputCitationOffset, temp);
                        } else {
                            Set temp = new HashSet<>();
                            temp.add(outputAnnotationV3List.get(i).getDiscourse_Facet());
                            outputFacets.put(outputCitingArticle + "_" + outputCitationOffset, temp);
                        }
                    }
                }

                double outputMatchesSize = 0;
                double goldMatchesSize = 0;
                double outputFacetsSize = 0;
                double goldFacetsSize = 0;

                for (Set set : outputMatches.values()) {
                    outputMatchesSize += set.size();
                }
                for (Set set : goldMatches.values()) {
                    goldMatchesSize += set.size();
                }
                for (Set set : outputFacets.values()) {
                    outputFacetsSize += set.size();
                }
                for (Set set : goldFacets.values()) {
                    goldFacetsSize += set.size();
                }

                int matchTP = truePositivesCount(goldMatches, outputMatches);
                double matchPrecision = (double) matchTP / (double) outputMatchesSize;
                double matchRecall = (double) matchTP / (double) goldMatchesSize;
                double matchFmeasure = 0d;
                if (matchPrecision + matchRecall != 0d) {
                    matchFmeasure = ((2d * matchPrecision * matchRecall) / (matchPrecision + matchRecall));
                }

                int facetTP = truePositivesCount(goldFacets, outputFacets);
                double facetPrecision = (double) facetTP / (double) outputFacetsSize;
                double facetRecall = (double) facetTP / (double) goldFacetsSize;
                double facetFmeasure = 0d;
                if (facetPrecision + facetRecall != 0d) {
                    facetFmeasure = ((2d * facetPrecision * facetRecall) / (facetPrecision + facetRecall));
                }

                BufferedWriter writer = null;
                File file = new File(outputFolder + "evaluation_M" + match + "_P" + prob + ".txt");
                try {
                    writer = new BufferedWriter(new FileWriter(file));

                    writer.write("outputMatches: " + outputMatches);
                    writer.newLine();
                    writer.write("goldMatches: " + goldMatches);
                    writer.newLine();
                    writer.write("Matches True Positive: " + matchTP);
                    writer.newLine();
                    writer.write("Matches Precision ( TP: " + matchTP + " / system output size: "
                            + outputMatchesSize + ")= " + matchPrecision);
                    writer.newLine();
                    writer.write("Matches Recall: ( TP: " + matchTP + " / gold size: "
                            + goldMatchesSize + ")= " + matchRecall);
                    writer.newLine();
                    writer.write("Matches F-Measure( : " + "((2 * " + matchPrecision + "*" + matchRecall +
                            ") / (" + matchPrecision + "+" + matchRecall + "))= " + matchFmeasure);
                    writer.newLine();

                    writer.write("outputFacets: " + outputFacets);
                    writer.newLine();
                    writer.write("goldFacets: " + goldFacets);
                    writer.newLine();
                    writer.write("Facets True Positive: " + facetTP);
                    writer.newLine();
                    writer.write("Facets Precision ( TP: " + facetTP + " / system output size: "
                            + outputFacetsSize + ")= " + facetPrecision);
                    writer.newLine();
                    writer.write("Facets Recall: ( TP: " + facetTP + " / gold size: "
                            + goldFacetsSize + ")= " + facetRecall);
                    writer.newLine();
                    writer.write("Facets F-Measure( : " + "((2 * " + facetPrecision + "*" + facetRecall +
                            ") / (" + facetPrecision + "+" + facetRecall + "))= " + facetFmeasure);
                    writer.newLine();

                    writer.newLine();
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("maxMatch: " + match);
                System.out.println("matchProbability: " + prob);
                System.out.println("outputMatches " + outputMatches);
                System.out.println("outputFacets " + outputFacets);
                System.out.println(matchTP);
                System.out.println(facetTP);
                System.out.println("goldMatches " + goldMatches);
                System.out.println("goldFacets " + goldFacets);

            }
        }
    }

    private static int truePositivesCount(HashMap<String, Set<String>> gold, HashMap<String, Set<String>> output) {
        int value = 0;
        for (String citance : gold.keySet()) {
            if (gold.containsKey(citance)) {
                Set goldSet = gold.get(citance);
                Set outputSet = output.get(citance);

                Iterator goldIterator = goldSet.iterator();
                while (goldIterator.hasNext()) {
                    String val = goldIterator.next().toString();
                    if (outputSet.contains(val)) {
                        value++;
                    }
                }
            }

        }

        return value;
    }
}
