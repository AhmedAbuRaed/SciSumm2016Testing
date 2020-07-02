package edu.upf.taln.scisumm2016.classify;

import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.AnnotationV3;
import edu.upf.taln.scisumm2016.reader.FeaturesMode;
import edu.upf.taln.scisumm2016.reader.OutputAnnotation;
import edu.upf.taln.scisumm2016.reader.TrainingExample;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Gate;
import gate.util.GateException;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;

import java.io.File;
import java.util.*;

/**
 * Created by Ahmed on 2/10/17.
 */
public class FacetsSystemBasedMain {
    public static String workingDir;
    public static void FacetsSystemMainRun(String args[]) {
        if (args.length == 5) {
            System.out.println("Started");

            HashMap<String, List<OutputAnnotation>> output = new HashMap<String, List<OutputAnnotation>>();

            try {
                Gate.init();
            } catch (GateException e) {
                e.printStackTrace();
            }

            workingDir = args[3].trim();

            boolean generateTraining;
            String outputInstancesType;

            int classifiedInstances = 0;

            ArrayList<AnnotationV3> annotationV3List;
            HashMap<String, Document> ProcessedRCDocuments = null;

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

            if (!generateTraining) {
                //ANNOTATE GOLD STANDARD
                //////////////////////////////
                annotationV3List = Utilities.extractAnnotationsV3FromBaseFolder(new File(baseFolder), true);
                ProcessedRCDocuments = Utilities.applyGSAnnotations(ProcessedRCDocuments, annotationV3List);
                ///////////////////////////////

                File facetModel = new File(modelsFolder + "facetModel.model");

                File trainingFacetDatastructure = new File(dataStructuresFolder + File.separator +
                        "facetTrainingDataSet.arff");

                System.out.println("Loading InputMappedClassifiers...");
                facetInputMappedClassifier = Utilities.loadInputMappedClassifier(facetModel, trainingFacetDatastructure);
                System.out.println("InputMappedClassifiers Loaded ...");

                System.out.println(rfolderName + " Start Classification...");

                Instances trainingFacetDataset = Utilities.readDataStructure(trainingFacetDatastructure);
            }

            Document rp = ProcessedRCDocuments.get(rfolderName);
            AnnotationSet rpMarkups = rp.getAnnotations("Original markups");
            AnnotationSet rpSentences = rpMarkups.get("S");
            AnnotationSet rpSystemMatches = rp.getAnnotations("WJ_Match_2");

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

                            Iterator rpSentencesIterator = rpSentences.iterator();
                            while (rpSentencesIterator.hasNext()) {
                                Annotation rpSentence = (Annotation) rpSentencesIterator.next();
                                rpStartS = rpSentence.getStartNode().getOffset();
                                rpEndS = rpSentence.getEndNode().getOffset();

                                boolean match = false;

                                AnnotationSet rpSystemMatchesInSentence = rpSystemMatches.get(rpStartS, rpEndS);
                                if (rpSystemMatchesInSentence.size() > 0) {
                                    Iterator rpSystemMatchesIterator = rpSystemMatchesInSentence.iterator();
                                    while (rpSystemMatchesIterator.hasNext()) {
                                        Annotation rpSystemMatch = (Annotation) rpSystemMatchesIterator.next();
                                        if (rpSystemMatch.getFeatures().get("id")
                                                .equals(cpAnnotator.getFeatures().get("id"))) {
                                            match = true;
                                        }
                                    }
                                    if (match) {
                                        //Testing
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


                                                //Storing output
                                                AnnotationV3 annotationV3 = Utilities.writeSciSummOutput(te, trCtx, predictedfacet);

                                                OutputAnnotation outputAnnotation = new OutputAnnotation(annotationV3, trCtx, te, matchTestInstance, facetTestInstance, 1d, "MATCH");

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

                        } else {
                            System.out.println("Could not find the Citance Sentence.");
                        }
                    }

                }
            }
            System.gc();

            if (!generateTraining) {
                //Print Result Annotations
                ProcessedRCDocuments = Utilities.spitOutOutputAnnotations(output, ProcessedRCDocuments, rfolderName, 100, String.valueOf(1.0), outputFolder);
            }

            System.out.println("Exporting new GATE Documents ...");
            Utilities.exportGATEDocuments(ProcessedRCDocuments, rfolderName, outputFolder + File.separator + "Normalized");

            System.out.println("END PROCESSING");
        } else {
            System.out.println("Wrong Number of Arguments");
        }
    }
}
