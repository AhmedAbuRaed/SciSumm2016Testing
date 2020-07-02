package edu.upf.taln.scisumm2016.baseline;

import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;
import gate.*;
import gate.util.GateException;
import weka.core.Instances;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ahmed on 8/20/16.
 */
public class BaseLineOutput {
    public static void main(String args[]) {
        System.out.println("Started");
        try {
            Gate.init();
        } catch (GateException e) {
            e.printStackTrace();
        }
        String rfolderName = args[1];
        String baseFolder = args[2] + File.separator + rfolderName;
        String outputFolder = baseFolder + "/Output/";
        String inputFolder = outputFolder + rfolderName;
        String modelsFolder = baseFolder + "/Models/";
        String dataStructuresFolder = baseFolder + "/ARFFs/";
        HashMap<String, Document> RCDocuments;

        System.out.println(rfolderName + " Start extracting documents from the input Folder ...");
        RCDocuments = Utilities.extractDocumentsFromBaseFolder(new File(inputFolder));
        Document rp = RCDocuments.get(rfolderName);
        AnnotationSet rpMarkups = rp.getAnnotations("Original markups");
        AnnotationSet rpAnalysis = rp.getAnnotations("Analysis");
        AnnotationSet rpReferences = rp.getAnnotations("REFERENCES");
        AnnotationSet rpSimilarities = rp.getAnnotations("Similarities");
        AnnotationSet rpSentences = rpMarkups.get("S");
        for (String key : RCDocuments.keySet()) {
            if (!key.equals(rfolderName)) {
                Document cp = RCDocuments.get(key);

                AnnotationSet cpMarkups = cp.getAnnotations("Original markups");
                AnnotationSet cpCitMarkups = cp.getAnnotations("CITATIONS");
                AnnotationSet cpSentences = cpMarkups.get("S");

                Long cpStartA, cpEndA, rpStartS, rpEndS;
                Iterator cpAnnotatorsIterator = cpCitMarkups.iterator();

                while (cpAnnotatorsIterator.hasNext()) {
                    Annotation cpAnnotator = (Annotation) cpAnnotatorsIterator.next();
                    cpStartA = cpAnnotator.getStartNode().getOffset();
                    cpEndA = cpAnnotator.getEndNode().getOffset();

                    AnnotationSet cpCitationSentences = cpSentences.get(cpStartA);
                    if (cpCitationSentences.size() > 0) {
                        Annotation cpSentence = cpCitationSentences.iterator().next();

                        Iterator rpSimilaritiesIterator = rpSimilarities.iterator();
                        while (rpSimilaritiesIterator.hasNext()) {
                            Annotation rpSimilarity = (Annotation) rpSimilaritiesIterator.next();
                            FeatureMap fm = rpSimilarity.getFeatures();

                            if (cpAnnotator.getFeatures().get("id").equals(fm.get("MatchCitanceID"))) {
                                AnnotationSet rpSentencesSet = rpSentences.get(rpSimilarity.getStartNode().getOffset());
                                if (rpSentencesSet.size() > 0) {
                                    Annotation rpSentence = rpSentencesSet.iterator().next();
                                    DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                    TrainingExample te = new TrainingExample(rpSentence, cpSentence, 0);

                                    AnnotationSet rpSentencesProp = rpAnalysis.get("Sentence_LOA").get(rpSimilarity.getStartNode().getOffset());
                                    if (rpSentencesProp.size() > 0) {
                                        Annotation rpSentenceProp = rpSentencesProp.iterator().next();
                                        FeatureMap propfm = rpSentenceProp.getFeatures();
                                        double max = 0d;
                                        String propFacet = null;
                                        for (Object pkey : propfm.keySet()) {
                                            if (pkey.toString().startsWith("PROB_DRI")) {
                                                double value = Double.valueOf(propfm.get(pkey).toString());
                                                if (value > max) {
                                                    propFacet = (String) pkey;
                                                    max = value;
                                                }
                                            }
                                        }
                                        String facet = null;
                                        switch (propFacet)
                                        {
                                            case "PROB_DRI_Approach" :
                                            case "PROB_DRI_Unspecified":
                                                facet = "METHOD";
                                                break;
                                            case "PROB_DRI_Background" :
                                                facet = "HYPOTHESIS";
                                                break;
                                            case "PROB_DRI_Challenge" :
                                                facet = "AIM";
                                                break;
                                            case "PROB_DRI_FutureWork" :
                                                facet = "IMPLICATION";
                                                break;
                                            case "PROB_DRI_Outcome" :
                                                facet = "RESULT";
                                                break;
                                        }
                                        Utilities.writeSciSummOutput(te, trCtx, facet);
                                    }
                                }
                            }


                        }
                    } else {
                        System.out.println("Could not find the Citance Sentence.");
                    }
                }
            }
        }


    }
}
