package edu.upf.taln.scisumm2016.evaluation;

import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.reader.AnnotationV3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ahmed on 12/4/16.
 */
public class kokilEvaluation3 {
    public static void main(String args[]) {
        String rfolderName = args[1];
        String baseFolder = args[2] + File.separator + rfolderName;
        String outputFolder = baseFolder + "/Output/";
        String inputFolder = baseFolder + "/input/";
        String modelsFolder = baseFolder + "/Models/";
        String dataStructuresFolder = baseFolder + "/ARFFs/";

        Double matchProbability = Double.parseDouble(args[3]);
        Integer maxMatch = Integer.parseInt(args[4]);

        HashMap<String, Set<String>> outputMatches = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> outputFacets = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> goldMatches = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> goldFacets = new HashMap<String, Set<String>>();

        ArrayList<AnnotationV3> outputAnnotationV3List;
        ArrayList<AnnotationV3> goldStandardAnnotationV3List;

        outputAnnotationV3List = Utilities.extractOutputAnnotationsV3FromBaseFolder(new File(baseFolder), maxMatch, matchProbability.toString(), true);
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

        int matchTP = truePositives(goldMatches, outputMatches);
        int matchFP = falsePositives(goldMatches, outputMatches);
        int matchFN = falseNegatives(goldMatches, outputMatches);
        double matchPrecision = (double) matchTP / (double) (matchTP + matchFP);
        double matchRecall = (double) matchTP / (double) (matchTP + matchFN);
        double matchFmeasure = 0d;
        if (matchPrecision + matchRecall != 0d) {
            matchFmeasure = ((2d * matchPrecision * matchRecall) / (matchPrecision + matchRecall));
        }

        int facetTP = truePositivesFacets(goldFacets, outputFacets, goldMatches, outputMatches);
        int facetFP = falsePositivesFacets(goldFacets, outputFacets, goldMatches, outputMatches);
        int facetFN = falseNegativesFacets(goldFacets, outputFacets, goldMatches, outputMatches); // this one doesn't care for matches, because we consider only false results
        double facetPrecision = (double) facetTP / (double) (facetTP + facetFP);
        double facetRecall = (double) facetTP / (double) (facetTP + facetFN);
        double facetFmeasure = 0d;
        if (facetPrecision + facetRecall != 0d) {
            facetFmeasure = ((2d * facetPrecision * facetRecall) / (facetPrecision + facetRecall));
        }

        BufferedWriter writer = null;
        File file = new File(outputFolder + "evaluation_M" + maxMatch + "_P" + matchProbability + ".txt");
        try {
            writer = new BufferedWriter(new FileWriter(file));

            writer.write("outputMatches: " + outputMatches);
            writer.newLine();
            writer.write("goldMatches: " + goldMatches);
            writer.newLine();
            writer.write("Matches True Positive: " + matchTP);
            writer.newLine();
            writer.write("Matches Precision ( TP: " + matchTP + " / (TP + FP): "
                    + (matchTP + matchFP) + ")= " + matchPrecision);
            writer.newLine();
            writer.write("Matches Recall: ( TP: " + matchTP + " / (TP + FN): "
                    + (matchTP + matchFN) + ")= " + matchRecall);
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
            writer.write("Facets Precision ( TP: " + facetTP + " / (TP + FP): "
                    + (facetTP + facetFP) + ")= " + facetPrecision);
            writer.newLine();
            writer.write("Facets Recall: ( TP: " + facetTP + " / (TP + FN): "
                    + (facetTP + facetFN) + ")= " + facetRecall);
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

        System.out.println("outputMatches " + outputMatches);
        System.out.println("outputFacets " + outputFacets);
        System.out.println("matchTP " + matchTP);
        System.out.println("matchFP " + matchFP);
        System.out.println("matchFN " + matchFN);
        System.out.println("facetTP " + facetTP);
        System.out.println("facetFP " + facetFP);
        System.out.println("facetFN " + facetFN);
        System.out.println("goldMatches " + goldMatches);
        System.out.println("goldFacets " + goldFacets);
    }

    private static int truePositives(HashMap<String, Set<String>> gold, HashMap<String, Set<String>> output) {
        int value = 0;
        for (String citance : gold.keySet()) {
            if (gold.containsKey(citance)) {
                Set<String> goldSet = gold.get(citance);
                Set<String> outputSet = output.get(citance);
                if (outputSet != null) {
                    Set<String> intersectionSet = intersect(goldSet, outputSet);
                    value += intersectionSet.size();
                }
            }

        }

        return value;
    }

    private static int falsePositives(HashMap<String, Set<String>> gold, HashMap<String, Set<String>> output) {
        int value = 0;
        for (String citance : gold.keySet()) {
            if (gold.containsKey(citance)) {
                Set<String> goldSet = gold.get(citance);
                Set<String> outputSet = output.get(citance);
                if (outputSet != null) {
                    Set<String> differenceSet = subtract(outputSet, goldSet);
                    value += differenceSet.size();
                }
            }

        }

        return value;
    }

    private static int falseNegatives(HashMap<String, Set<String>> gold, HashMap<String, Set<String>> output) {
        int value = 0;
        for (String citance : gold.keySet()) {
            if (gold.containsKey(citance)) {
                Set<String> goldSet = gold.get(citance);
                Set<String> outputSet = output.get(citance);
                if (outputSet != null) {
                    Set<String> differenceSet = subtract(goldSet, outputSet);
                    value += differenceSet.size();
                }
                else
                {
                    value += goldSet.size();
                }
            }

        }

        return value;
    }


    private static int truePositivesFacets(HashMap<String, Set<String>> goldFacets, HashMap<String, Set<String>> outputFacets, HashMap<String, Set<String>> goldMatches, HashMap<String, Set<String>> outputMatches) {
        int value = 0;
        for (String citance : goldFacets.keySet()) {
            Set<String> goldFacetSet = goldFacets.get(citance);
            Set<String> outputFacetSet = outputFacets.get(citance);
            Set<String> goldMatchesSet = goldMatches.get(citance);
            Set<String> outputMatchesSet = outputMatches.get(citance);
            if (outputMatchesSet != null) {
                // but we need to remove the items that were not a match
                Set<String> intersectionMatchesSet = intersect(goldMatchesSet, outputMatchesSet); // correct matches
                if(intersectionMatchesSet.size() > 0)
                {
                    Set<String> intersectionFacetSet = intersect(goldFacetSet, outputFacetSet); // correct facets
                    value += intersectionFacetSet.size();
                }
            }
        }

        return value;
    }

    private static int falsePositivesFacets(HashMap<String, Set<String>> goldFacets, HashMap<String, Set<String>> outputFacets, HashMap<String, Set<String>> goldMatches, HashMap<String, Set<String>> outputMatches) {
        int value = 0;
        for (String citance : goldFacets.keySet()) {
            Set<String> goldFacetsSet = goldFacets.get(citance);
            Set<String> outputFacetsSet = outputFacets.get(citance);
            Set<String> goldMatchesSet = goldMatches.get(citance);
            Set<String> outputMatchesSet = outputMatches.get(citance);

            if (outputMatchesSet != null) {
                Set<String> intersectionMatchesSet = intersect(outputMatchesSet, goldMatchesSet); // correct matches
                if(intersectionMatchesSet.size() > 0)
                {
                    Set<String> falsePositiveBecauseOfFacet = subtract(outputFacetsSet, goldFacetsSet);
                    value += falsePositiveBecauseOfFacet.size();
                }
                else
                {
                    Set<String> intersectionFacetsSet = intersect(outputFacetsSet, goldFacetsSet);
                    value += intersectionFacetsSet.size();
                }
            }
            else
            {
                value++;
            }

        }

        return value;
    }

    private static int falseNegativesFacets(HashMap<String, Set<String>> goldFacets, HashMap<String, Set<String>> outputFacets, HashMap<String, Set<String>> goldMatches, HashMap<String, Set<String>> outputMatches) {
        int value = 0;
        for (String citance : goldFacets.keySet()) {
            if (goldFacets.containsKey(citance)) {
                Set<String> goldSet = goldFacets.get(citance);
                Set<String> outputSet = outputFacets.get(citance);
                Set<String> goldMatchesSet = goldMatches.get(citance);
                Set<String> outputMatchesSet = outputMatches.get(citance);

                if (outputMatchesSet != null) {
                    Set<String> intersectionMatchesSet = intersect(goldMatchesSet, outputMatchesSet); // correct matches
                    if(intersectionMatchesSet.size() == 0)
                    {
                        value++;
                    }
                    else
                    {
                        Set<String> intersectionFacetsSet = intersect(goldSet, outputSet); // correct matches
                        if(intersectionFacetsSet.size() == 0)
                        {
                            value++;
                        }
                    }
                }
                else
                {
                    value ++;
                }
            }

        }

        return value;
    }

    private static Set<String> union(Set<String> set1, Set<String> set2) {
        Set<String> union = new HashSet<String>(set1);
        union.addAll(set2);
        return union;
    }

    private static Set<String> intersect(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<String>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    private static Set<String> subtract(Set<String> set1, Set<String> set2) {
        Set<String> result = new HashSet<String>(set1);
        result.removeAll(set2);
        return result;
    }
}
