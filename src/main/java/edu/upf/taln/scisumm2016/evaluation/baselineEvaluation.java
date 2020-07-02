package edu.upf.taln.scisumm2016.evaluation;

import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.reader.AnnotationV3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by ahmed on 8/22/16.
 */
public class baselineEvaluation {
    public static void main(String args[]) {
        String rfolderName = args[1];
        String baseFolder = args[2] + File.separator + rfolderName;
        String outputFolder = baseFolder + "/Output/";
        String inputFolder = baseFolder + "/input/";
        String modelsFolder = baseFolder + "/Models/";
        String dataStructuresFolder = baseFolder + "/ARFFs/";

        HashMap<String, Set<String>> baselineMatches = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> baselineFacets = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> goldMatches = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> goldFacets = new HashMap<String, Set<String>>();

        ArrayList<AnnotationV3> baseLineAnnotationV3List;
        ArrayList<AnnotationV3> goldStandardAnnotationV3List;

        baseLineAnnotationV3List = Utilities.extractBaseLineAnnotationsV3FromBaseFolder(new File(baseFolder), true);
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

        //BaseLine Output
        for (int i = 0; i < baseLineAnnotationV3List.size(); i++) {
            String baselineCitingArticle = baseLineAnnotationV3List.get(i).getCiting_Article();
            for (String baselineCitationOffset : baseLineAnnotationV3List.get(i).getCitation_Offset()) {
                //references match
                for (String baselineReferenceOffset : baseLineAnnotationV3List.get(i).getReference_Offset()) {
                    if (baselineMatches.containsKey(baselineCitingArticle + "_" + baselineCitationOffset)) {
                        Set temp = baselineMatches.get(baselineCitingArticle + "_" + baselineCitationOffset);
                        temp.add(baselineReferenceOffset);
                        baselineMatches.put(baselineCitingArticle + "_" + baselineCitationOffset, temp);
                    } else {
                        Set temp = new HashSet<>();
                        temp.add(baselineReferenceOffset);
                        baselineMatches.put(baselineCitingArticle + "_" + baselineCitationOffset, temp);
                    }
                }
                //facet
                if (baselineFacets.containsKey(baselineCitingArticle + "_" + baselineCitationOffset)) {
                    Set temp = baselineFacets.get(baselineCitingArticle + "_" + baselineCitationOffset);
                    temp.add(baseLineAnnotationV3List.get(i).getDiscourse_Facet());
                    baselineFacets.put(baselineCitingArticle + "_" + baselineCitationOffset, temp);
                } else {
                    Set temp = new HashSet<>();
                    temp.add(baseLineAnnotationV3List.get(i).getDiscourse_Facet());
                    baselineFacets.put(baselineCitingArticle + "_" + baselineCitationOffset, temp);
                }
            }
        }

        double baselineMatchesSize = 0;
        double goldMatchesSize = 0;
        double baselineFacetsSize = 0;
        double goldFacetsSize = 0;

        for(Set set: baselineMatches.values())
        {
            baselineMatchesSize+= set.size();
        }
        for(Set set: goldMatches.values())
        {
            goldMatchesSize+= set.size();
        }
        for(Set set: baselineFacets.values())
        {
            baselineFacetsSize+= set.size();
        }
        for(Set set: goldFacets.values())
        {
            goldFacetsSize+= set.size();
        }

        int matchTP = truePositivesCount(goldMatches, baselineMatches);
        double matchPrecision = (double) matchTP / (double) baselineMatchesSize;
        double matchRecall = (double) matchTP / (double) goldMatchesSize;
        double matchFmeasure = 0d;
        if(matchPrecision + matchRecall != 0d)
        {
            matchFmeasure = ( (2d * matchPrecision * matchRecall) / (matchPrecision + matchRecall));
        }

        int facetTP = truePositivesCount(goldFacets, baselineFacets);
        double facetPrecision = (double) facetTP / (double) baselineFacetsSize;
        double facetRecall = (double) facetTP / (double) goldFacetsSize;
        double facetFmeasure = 0d;
        if(facetPrecision + facetRecall != 0d)
        {
            facetFmeasure = ((2d * facetPrecision * facetRecall) / (facetPrecision + facetRecall));
        }

        BufferedWriter writer = null;
        File file = new File(outputFolder + "baselineEvaluation.txt");
        try {
            writer = new BufferedWriter(new FileWriter(file));

            writer.write("baselineMatches: " + baselineMatches);
            writer.newLine();
            writer.write("goldMatches: " + goldMatches);
            writer.newLine();
            writer.write("Matches True Positive (!Collections.disjoint(matchGoldSet, matchBaselineSet)): " + matchTP);
            writer.newLine();
            writer.write("Matches Precision ( TP: " +  matchTP + " / baseline system size: "
                    + baselineMatchesSize + ")= " + matchPrecision);
            writer.newLine();
            writer.write("Matches Recall: ( TP: " +  matchTP + " / gold size: "
                    + goldMatchesSize + ")= " + matchRecall);
            writer.newLine();
            writer.write("Matches F-Measure( : " + "((2 * " + matchPrecision  + "*" + matchRecall +
                    ") / (" + matchPrecision + "+" + matchRecall + "))= " + matchFmeasure);
            writer.newLine();

            writer.write("baselineFacets: " + baselineFacets);
            writer.newLine();
            writer.write("goldFacets: " + goldFacets);
            writer.newLine();
            writer.write("Facets True Positive (!Collections.disjoint(facetGoldSet, facetBaselineSet)): " + facetTP);
            writer.newLine();
            writer.write("Facets Precision ( TP: " +  facetTP + " / baseline system size: "
                    + baselineFacetsSize + ")= " + facetPrecision);
            writer.newLine();
            writer.write("Facets Recall: ( TP: " +  facetTP + " / gold size: "
                    + goldFacetsSize + ")= " + facetRecall);
            writer.newLine();
            writer.write("Facets F-Measure( : " + "((2 * " + facetPrecision  + "*" + facetRecall +
                    ") / (" + facetPrecision + "+" + facetRecall + "))= " + facetFmeasure);
            writer.newLine();

            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("baselineMatches " + baselineMatches);
        System.out.println("baselineFacets " + baselineFacets);
        System.out.println(matchTP);
        System.out.println(facetTP);
        System.out.println("goldMatches " + goldMatches);
        System.out.println("goldFacets " + goldFacets);


    }


    private static int truePositivesCount(HashMap<String, Set<String>> gold, HashMap<String, Set<String>> baseline) {
        int value = 0;
        for (String citance : baseline.keySet()) {
            if (gold.containsKey(citance)) {
                Set goldSet = gold.get(citance);
                Set outputSet = baseline.get(citance);

                if (!Collections.disjoint(goldSet, outputSet)) {
                    value++;
                }
            }

        }

        return value;
    }

}
