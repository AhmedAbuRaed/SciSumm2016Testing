package edu.upf.taln.scisumm2016.classify;

import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.feature.FeatureGenerator;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.AnnotationV3;
import edu.upf.taln.scisumm2016.reader.FeaturesMode;
import edu.upf.taln.scisumm2016.reader.OutputAnnotation;
import edu.upf.taln.scisumm2016.reader.TrainingExample;
import gate.*;
import gate.util.GateException;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;

import java.io.File;
import java.util.*;

/**
 * Created by upf on 10/3/16.
 */
public class EnrichedDocumentsMain {
    public static String workingDir;

    public static void EnrichedDocumentsMainRun(String args[]) {
        if (args.length == 7) {
            System.out.println("Started");

            HashMap<String, List<OutputAnnotation>> output = new HashMap<String, List<OutputAnnotation>>();

            Double matchProbability = Double.parseDouble(args[5]);
            Integer maxMatch = Integer.parseInt(args[6]);

            try {
                Gate.init();
            } catch (GateException e) {
                e.printStackTrace();
            }

            workingDir = args[3].trim();

            boolean generateTraining;
            String outputInstancesType;

            int matchParsedInstances = 0;
            int facetParsedInstances = 0;
            int classifiedInstances = 0;

            ArrayList<AnnotationV3> annotationV3List;
            HashMap<String, Document> ProcessedRCDocuments = null;

            InputMappedClassifier matchInputMappedClassifier = null;
            InputMappedClassifier facetInputMappedClassifier = null;

            String rfolderName = args[1];
            String baseFolder = workingDir + File.separator + rfolderName;
            String outputFolder = baseFolder + "/Output/";
            String inputFolder = baseFolder + "/input/";
            String modelsFolder = baseFolder + "/Models/";
            String dataStructuresFolder = baseFolder + "/ARFFs/";

            if (args[2].equals("Train")) {
                generateTraining = true;
                outputInstancesType = "Training";
                System.out.println(rfolderName + " Start " + outputInstancesType + "...");
                System.out.println(rfolderName + " " + outputInstancesType +
                        " instances generation...");
            } else {
                generateTraining = false;
                outputInstancesType = "Testing";
                System.out.println(rfolderName + " Start " + outputInstancesType + "...");
                System.out.println(" - " + rfolderName + " " + outputInstancesType +
                        " instances generation...");
            }

            System.out.println(rfolderName + " Start extracting documents from the output Folder ...");
            ProcessedRCDocuments = Utilities.extractDocumentsFromBaseFolder(new File(outputFolder + File.separator + rfolderName));
            System.out.println(rfolderName + " Extracting done...");
            System.out.println(rfolderName + " Start applying annotations on the documents ...");

            //ANNOTATE GOLD STANDARD
            //////////////////////////////
            //First Empty old annotations if exists the last thing we need is duplicates
            Document d = ProcessedRCDocuments.get(rfolderName);
            d.getAnnotations("GS_REFERENCES").clear();
            ProcessedRCDocuments.put(rfolderName, d);
            /////////////////////////////
            annotationV3List = Utilities.extractAnnotationsV3FromBaseFolder(new File(baseFolder), true);
            ProcessedRCDocuments = Utilities.applyGSAnnotations(ProcessedRCDocuments, annotationV3List);
            ///////////////////////////////

            if (!generateTraining) {

                File matchModel = new File(modelsFolder + "matchModel.model");
                File facetModel = new File(modelsFolder + "facetModel.model");

                File trainingMatchDatastructure = new File(dataStructuresFolder + File.separator +
                        "matchTrainingDataSet.arff");
                File trainingFacetDatastructure = new File(dataStructuresFolder + File.separator +
                        "facetTrainingDataSet.arff");

                System.out.println("Loading InputMappedClassifiers...");
                matchInputMappedClassifier = Utilities.loadInputMappedClassifier(matchModel, trainingMatchDatastructure);
                facetInputMappedClassifier = Utilities.loadInputMappedClassifier(facetModel, trainingFacetDatastructure);
                System.out.println("InputMappedClassifiers Loaded ...");

                System.out.println(rfolderName + " Start Classification...");

                Instances trainingMatchDataset = Utilities.readDataStructure(trainingMatchDatastructure);
                Instances trainingFacetDataset = Utilities.readDataStructure(trainingFacetDatastructure);
            }

            Document rp = ProcessedRCDocuments.get(rfolderName);
            AnnotationSet rpMarkups = rp.getAnnotations("Original markups");
            AnnotationSet rpReferences = rp.getAnnotations("GS_REFERENCES");
            AnnotationSet rpSentences = rpMarkups.get("S");

            //delete old features values
            AnnotationSet rpNoMatchFeatures = rp.getAnnotations("Match_Features");
            AnnotationSet rpMatchFeatures = rp.getAnnotations("NO_Match_Features");
            AnnotationSet rpFacetFeatures = rp.getAnnotations("Facet_Features");

            rpMatchFeatures.clear();
            rpNoMatchFeatures.clear();
            rpFacetFeatures.clear();

            for (String key : ProcessedRCDocuments.keySet()) {
                if (!key.equals(rfolderName)) {
                    Document cp = ProcessedRCDocuments.get(key);

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

                            //check in case there is no match set the value of the highest sim value
                            boolean testMatchFound = false;

                            Iterator rpSentencesIterator = rpSentences.iterator();
                            while (rpSentencesIterator.hasNext()) {
                                Annotation rpSentence = (Annotation) rpSentencesIterator.next();
                                rpStartS = rpSentence.getStartNode().getOffset();
                                rpEndS = rpSentence.getEndNode().getOffset();

                                boolean match = false;

                                if (generateTraining) {
                                    //Training
                                    //Match Training
                                    AnnotationSet rpReferenceCitations = rpReferences.get(rpStartS, rpEndS);
                                    if (rpReferenceCitations.size() > 0) {
                                        try {
                                            matchParsedInstances++;
                                            System.out.println("Parsing " + outputInstancesType + " match instance " + matchParsedInstances
                                                    + ": (citance: " + cp.getName() + " reference: " + rp.getName()
                                                    + " id: ref: " + rpSentence.getFeatures().get("sid")
                                                    + " cit: " + cpSentence.getFeatures().get("sid") + "):");


                                            Iterator rpMatchReferenceCitationsIterator = rpReferenceCitations.iterator();
                                            while (rpMatchReferenceCitationsIterator.hasNext()) {
                                                Annotation rpReferenceCitation = (Annotation) rpMatchReferenceCitationsIterator.next();
                                                if (rpReferenceCitation.getFeatures().get("id")
                                                        .equals(cpAnnotator.getFeatures().get("id"))) {
                                                    match = true;
                                                }
                                            }

                                            if (match) {
                                                // Set training context
                                                DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                                TrainingExample te = new TrainingExample(rpSentence, cpSentence, 1);
                                                Instances matchTrainInstance = Utilities.generateNormalizedMatchTrainingInstance(te, trCtx, Utilities.generateMatchAttributes());
                                                ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithMatchFeatures(matchTrainInstance, te, trCtx));
                                            } else {
                                                // Set training context
                                                DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                                TrainingExample te = new TrainingExample(rpSentence, cpSentence, 0);
                                                Instances matchTrainInstance = Utilities.generateNormalizedMatchTrainingInstance(te, trCtx, Utilities.generateMatchAttributes());
                                                ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithNOMatchFeatures(matchTrainInstance, te, trCtx));
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Error generating " + outputInstancesType
                                                    + " match instance features of example "
                                                    + matchParsedInstances
                                                    + ": (citance: " + cp.getName() + " reference: " + rp.getName()
                                                    + " id: ref: " + rpSentence.getFeatures().get("sid")
                                                    + " cit: " + cpSentence.getFeatures().get("sid") + "):");
                                            e.printStackTrace();
                                        }

                                        //Facet Training
                                        Iterator rpFacetReferenceCitationsIterator = rpReferenceCitations.iterator();
                                        while (rpFacetReferenceCitationsIterator.hasNext()) {
                                            Annotation rpReferenceCitation = (Annotation) rpFacetReferenceCitationsIterator.next();
                                            if (rpReferenceCitation.getFeatures().get("id")
                                                    .equals(cpAnnotator.getFeatures().get("id"))) {

                                                try {
                                                    facetParsedInstances++;
                                                    System.out.println("Parsing " + outputInstancesType + " facet instance " + facetParsedInstances
                                                            + ": (citance: " + cp.getName() + " reference: " + rp.getName()
                                                            + " id: ref: " + rpSentence.getFeatures().get("sid")
                                                            + " cit: " + cpSentence.getFeatures().get("sid") + "):");

                                                    // Set training context
                                                    String rpReferenceCitationFacets = rpReferenceCitation.getFeatures().get("Discourse_Facet").toString();
                                                    if (rpReferenceCitationFacets.contains(",")) {
                                                        for (String facet : rpReferenceCitationFacets.trim().split(",")) {
                                                            //We have tons of Method instances so we train others
                                                            if (!facet.replaceAll("[^a-zA-Z0-9_]", "").equals("Method_Citation")) {
                                                                facet = facet.replaceAll("[^a-zA-Z0-9_]", "");
                                                                DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                                                TrainingExample te = new TrainingExample(rpSentence, cpSentence, facet);

                                                                Instances facetTrainInstance = Utilities.generateNormalizedFacetTrainingInstance(te, trCtx, Utilities.generateFacetAttributes("ALL", FeaturesMode.ALL));
                                                                ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithFacetFeatures(facetTrainInstance, te, trCtx));
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                                        TrainingExample te = new TrainingExample(rpSentence, cpSentence, rpReferenceCitationFacets);

                                                        Instances facetTrainInstance = Utilities.generateNormalizedFacetTrainingInstance(te, trCtx, Utilities.generateFacetAttributes("ALL", FeaturesMode.ALL));
                                                        ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithFacetFeatures(facetTrainInstance, te, trCtx));
                                                    }

                                                } catch (Exception e) {
                                                    System.out.println("Error generating " + outputInstancesType
                                                            + " facet instance features of example "
                                                            + facetParsedInstances
                                                            + ": (citance: " + cp.getName() + " reference: " + rp.getName()
                                                            + " id: ref: " + rpSentence.getFeatures().get("sid")
                                                            + " cit: " + cpSentence.getFeatures().get("sid") + "):");
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    //Testing
                                    boolean validCosine = false;
                                    AnnotationSet rpSentencesMain = rpSentences.get(rpStartS);
                                    if (rpSentencesMain.size() > 0) {
                                        Annotation rpSentenceMain = rpSentencesMain.iterator().next();
                                        if (rpSentenceMain.getFeatures().containsKey("sim_" + cpAnnotator.getFeatures().get("id"))) {
                                            double cosine = (double) rpSentenceMain.getFeatures().get("sim_" + cpAnnotator.getFeatures().get("id"));
                                            if (cosine > 0d) {
                                                validCosine = true;
                                            }
                                        } else {
                                            System.out.println("ID DOES NOT EXIST");
                                        }
                                    }
                                    if (validCosine) {
                                        try {
                                            classifiedInstances++;
                                            System.out.println("Classifying test instance " + classifiedInstances
                                                    + ": (citance: " + cp.getName() + " reference: " + rp.getName()
                                                    + " id: ref: " + rpSentence.getFeatures().get("sid")
                                                    + " cit: " + cpSentence.getFeatures().get("sid") + "):");

                                            // Set testing context
                                            DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                            TrainingExample te;

                                            te = new TrainingExample(rpSentence, cpSentence, 0);
                                            Instances matchTestInstance = Utilities.generateNormalizedMatchTestInstance(te, trCtx, Utilities.generateMatchAttributes());

                                            /*
                                            System.out.println("Applying String to Word Vector Filter on the match testing Dataset...");
                                            Instances matchTestInstanceSTWVRPBIL = Utilities.applyStringToWordVectorFilter(matchTestInstance,
                                                    "-R 88 -P rp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVCPBIL = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVRPBIL,
                                                    "-R 88 -P cp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVRPBIP = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVCPBIL,
                                                    "-R 88 -P rp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVCPBIP = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVRPBIP,
                                                    "-R 88 -P cp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVRPP = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVCPBIP,
                                                    "-R 88 -P rp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVCPP = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVRPP,
                                                    "-R 88 -P cp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVRPL = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVCPP,
                                                    "-R 88 -P rp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances matchTestInstanceSTWVCPL = Utilities.applyStringToWordVectorFilter(matchTestInstanceSTWVRPL,
                                                    "-R 88 -P cp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");

                                            System.out.println("Filter Applied...");

                                            System.out.println("Applying Reorder Filter on the testing Dataset...");
                                            Instances matchTestInstanceRO = Utilities.applyReorderFilter(matchTestInstanceSTWVCPL, "-R first-87,89-last,88");
                                            matchTestInstanceRO.setClassIndex(matchTestInstanceRO.numAttributes() - 1);
                                            System.out.println("Filter Applied...");*/

                                            Instances classifiedMatchTestInstance = Utilities.classifyInstances(matchTestInstance/*matchTestInstanceRO*/, matchInputMappedClassifier);

                                            double[] prediction = matchInputMappedClassifier.distributionForInstance(classifiedMatchTestInstance.get(0));
                                            System.out.println("Match Pred ... " + prediction[0] + " - " + prediction[1]);

                                            if (!classifiedMatchTestInstance.instance(0).classIsMissing()) {
                                                String predictedMatch = classifiedMatchTestInstance.instance(0).stringValue(classifiedMatchTestInstance.instance(0).numAttributes() - 1);
                                                if ((predictedMatch.equals("MATCH")) && (prediction[1] >= matchProbability)) {
                                                    testMatchFound = true;

                                                    Instances facetTestInstance = Utilities.generateNormalizedFacetTestInstance(te, trCtx, Utilities.generateFacetAttributes("ALL", FeaturesMode.ALL), FeaturesMode.ALL);
                                                    System.out.println("Match found ... ");
                                                    System.out.println("Applying String to Word Vector Filter on the facet testing Dataset...");
                                                    Instances facetTestInstanceSTWVRPBIL = Utilities.applyStringToWordVectorFilter(facetTestInstance,
                                                            "-R 204 -P rp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVCPBIL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPBIL,
                                                            "-R 204 -P cp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVRPBIP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVCPBIL,
                                                            "-R 204 -P rp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVCPBIP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPBIP,
                                                            "-R 204 -P cp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVRPP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVCPBIP,
                                                            "-R 204 -P rp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVCPP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPP,
                                                            "-R 204 -P cp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVRPL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVCPP,
                                                            "-R 204 -P rp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances facetTestInstanceSTWVCPL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPL,
                                                            "-R 204 -P cp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");

                                                    System.out.println("Filter Applied...");

                                                    System.out.println("Applying Reorder Filter on the testing Dataset...");
                                                    Instances facetTestInstanceRO = Utilities.applyReorderFilter(facetTestInstanceSTWVCPL, "-R first-203,205-last,204");
                                                    facetTestInstanceRO.setClassIndex(facetTestInstanceRO.numAttributes() - 1);
                                                    System.out.println("Filter Applied...");

                                                    Instances classifiedFacetTestInstance = Utilities.classifyInstances(facetTestInstanceRO, facetInputMappedClassifier);

                                                    if (!classifiedFacetTestInstance.instance(0).classIsMissing()) {
                                                        String predictedfacet = classifiedFacetTestInstance.instance(0).stringValue(classifiedFacetTestInstance.instance(0).numAttributes() - 1);
                                                        System.out.println("Facet predicted ... ");

                                                        /*ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithReference(te, trCtx, predictedfacet));

                                                        ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithMatchFeatures(matchTestInstance, te, trCtx));

                                                        facetTestInstance.instance(0).setClassValue(predictedfacet);
                                                        ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithFacetFeatures(facetTestInstance, te, trCtx));*/

                                                        //Storing output
                                                        AnnotationV3 annotationV3 = Utilities.writeSciSummOutput(te, trCtx, predictedfacet);

                                                        OutputAnnotation outputAnnotation = new OutputAnnotation(annotationV3, trCtx, te, matchTestInstance, facetTestInstance, prediction[1], predictedMatch);

                                                        if (output.containsKey(annotationV3.getCiting_Article() + "_" + cpSentence.getFeatures().get("sid"))) {
                                                            List<OutputAnnotation> temp = output.get(annotationV3.getCiting_Article() + "_" + te.getCitanceSentence().getFeatures().get("sid"));
                                                            temp.add(outputAnnotation);
                                                            output.put(annotationV3.getCiting_Article() + "_" + te.getCitanceSentence().getFeatures().get("sid"), temp);
                                                        } else {
                                                            List<OutputAnnotation> temp = new LinkedList<>();
                                                            temp.add(outputAnnotation);
                                                            output.put(annotationV3.getCiting_Article() + "_" + te.getCitanceSentence().getFeatures().get("sid"), temp);
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Error generating test instance "
                                                    + " instance features of example "
                                                    + classifiedInstances
                                                    + ": (citance: " + cp.getName() + " reference: " + rp.getName()
                                                    + " id: ref: " + rpSentence.getFeatures().get("sid")
                                                    + " cit: " + cpSentence.getFeatures().get("sid") + "):");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            if (!generateTraining) {
                                //Handle cases in which non of the sentences in the RP match the Citance
                                if (!testMatchFound) {
                                    System.out.println("No Match found for Citance : " + cpSentence.getFeatures().get("sid") +
                                            " Applying fall back Matching based on Cosine similarity");
                                    String predictedfacet = null;
                                    DocumentCtx trCtx = new DocumentCtx(rp, cp);
                                    TrainingExample te;

                                    AnnotationSet rpSimilarity = rp.getAnnotations("Similarities");
                                    Iterator rpSimilarityIterartor = rpSimilarity.iterator();

                                    while (rpSimilarityIterartor.hasNext()) {
                                        Annotation annotation = (Annotation) rpSimilarityIterartor.next();
                                        FeatureMap fm = annotation.getFeatures();
                                        if (fm.get("MatchCitanceID").equals(cpAnnotator.getFeatures().get("id"))) {
                                            AnnotationSet rpSentenceSet = rpSentences.get(annotation.getStartNode().getOffset());
                                            if (rpSentenceSet.size() > 0) {
                                                Annotation ann = rpSentenceSet.iterator().next();
                                                te = new TrainingExample(ann, cpSentence, 0);

                                                Instances facetTestInstance = Utilities.generateNormalizedFacetTestInstance(te, trCtx, Utilities.generateFacetAttributes("ALL", FeaturesMode.ALL), FeaturesMode.ALL);
                                                System.out.println("Applying String to Word Vector Filter on the facet testing Dataset...");
                                                Instances facetTestInstanceSTWVRPBIL = Utilities.applyStringToWordVectorFilter(facetTestInstance,
                                                        "-R 204 -P rp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVCPBIL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPBIL,
                                                        "-R 204 -P cp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVRPBIP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVCPBIL,
                                                        "-R 204 -P rp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVCPBIP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPBIP,
                                                        "-R 204 -P cp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVRPP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVCPBIP,
                                                        "-R 204 -P rp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVCPP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPP,
                                                        "-R 204 -P cp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVRPL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVCPP,
                                                        "-R 204 -P rp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                Instances facetTestInstanceSTWVCPL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVRPL,
                                                        "-R 204 -P cp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");

                                                System.out.println("Filter Applied...");

                                                System.out.println("Applying Reorder Filter on the testing Dataset...");
                                                Instances facetTestInstanceRO = Utilities.applyReorderFilter(facetTestInstanceSTWVCPL, "-R first-203,205-last,204");
                                                facetTestInstanceRO.setClassIndex(facetTestInstanceRO.numAttributes() - 1);
                                                System.out.println("Filter Applied...");

                                                Instances classifiedFacetTestInstance = Utilities.classifyInstances(facetTestInstanceRO, facetInputMappedClassifier);
                                                if (!classifiedFacetTestInstance.instance(0).classIsMissing()) {
                                                    predictedfacet = classifiedFacetTestInstance.instance(0).stringValue(classifiedFacetTestInstance.instance(0).numAttributes() - 1);
                                                }

                                                //Storing output
                                                AnnotationV3 annotationV3 = Utilities.writeSciSummOutput(te, trCtx, predictedfacet);
                                                OutputAnnotation outputAnnotation = new OutputAnnotation(annotationV3, trCtx, te, facetTestInstance, -1d, "NO_MATCH");

                                                if (output.containsKey(annotationV3.getCiting_Article() + "_" + cpSentence.getFeatures().get("sid"))) {
                                                    List<OutputAnnotation> temp = output.get(annotationV3.getCiting_Article() + "_" + te.getCitanceSentence().getFeatures().get("sid"));
                                                    temp.add(outputAnnotation);
                                                    output.put(annotationV3.getCiting_Article() + "_" + te.getCitanceSentence().getFeatures().get("sid"), temp);
                                                } else {
                                                    List<OutputAnnotation> temp = new LinkedList<>();
                                                    temp.add(outputAnnotation);
                                                    output.put(annotationV3.getCiting_Article() + "_" + te.getCitanceSentence().getFeatures().get("sid"), temp);
                                                }
                                            } else {
                                                System.out.println("Could not find fallback reference sentence");
                                            }
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
            System.gc();

            if (!generateTraining) {
                //Print Result Annotations
                ProcessedRCDocuments = Utilities.spitOutOutputAnnotations(output, ProcessedRCDocuments, rfolderName, maxMatch, String.valueOf(matchProbability), outputFolder);
            }

            System.out.println("Exporting new GATE Documents ...");
            Utilities.exportGATEDocuments(ProcessedRCDocuments, rfolderName, outputFolder + File.separator + "Normalized");

            if (generateTraining) {
                FeatureGenerator.generateMatchARFFfromFeaturesAnnotations(ProcessedRCDocuments, dataStructuresFolder + File.separator + "Normalized", rfolderName, generateTraining);
                FeatureGenerator.generateFacetARFFfromFeaturesAnnotations(ProcessedRCDocuments, dataStructuresFolder + File.separator + "Normalized", rfolderName, generateTraining);
            } else {
                FeatureGenerator.generateMatchARFFfromFeaturesAnnotations(ProcessedRCDocuments, dataStructuresFolder + File.separator + "Normalized", rfolderName, generateTraining);
                FeatureGenerator.generateFacetARFFfromFeaturesAnnotations(ProcessedRCDocuments, dataStructuresFolder + File.separator + "Normalized", rfolderName, generateTraining);
            }


            System.out.println("END PROCESSING");
        } else {
            System.out.println("Wrong Number of Arguments");
        }
    }
}
