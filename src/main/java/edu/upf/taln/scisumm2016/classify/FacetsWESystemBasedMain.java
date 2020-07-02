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
import org.apache.commons.lang3.tuple.Pair;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;

import java.io.*;
import java.util.*;

/**
 * Created by Ahmed on 2/10/17.
 */
public class FacetsWESystemBasedMain {
    public static String workingDir;

    public static void FacetsWESystemMainRun(String args[]) {
        if (args.length == 5) {
            System.out.println("Started");

            HashMap<String, List<OutputAnnotation>> output = new HashMap<String, List<OutputAnnotation>>();

            try {
                Gate.init();
            } catch (GateException e) {
                e.printStackTrace();
            }

            workingDir = args[3].trim();

            HashMap<String, Pair> references = new HashMap<String, Pair>();
            HashMap<String, List<String>> gold = new HashMap<String, List<String>>();

            boolean generateTraining;
            String outputInstancesType;

            int classifiedInstances = 0;

            ArrayList<AnnotationV3> annotationV3List;
            HashMap<String, Document> ProcessedRCDocuments = null;

            InputMappedClassifier methodfacetInputMappedClassifier = null;
            InputMappedClassifier othersfacetInputMappedClassifier = null;

            String rfolderName = args[1];
            String baseFolder = workingDir + File.separator + rfolderName;
            String outputFolder = baseFolder + "/Output/";
            String inputFolder = baseFolder + "/input/";
            String modelsFolder = baseFolder + "/Models/";
            String dataStructuresFolder = baseFolder + "/ARFFs/";

            String dataFilePath = workingDir + "/errors_test.csv";
            BufferedReader reader;
            String line;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(dataFilePath), "UTF-8"));
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    if (!line.equals("")) {
                        String[] values = line.split(",");

                        if (values[0].equals(rfolderName)) {
                            String[] CSIDs;
                            String[] RSIDs;
                            List<String> list = new ArrayList<String>();
                            if (values[3].contains("+")) {
                                CSIDs = values[3].split("\\+");
                                for (String CSID : CSIDs) {
                                    references.put(values[0] + "_" + values[1] + "_" + values[2] + "_" + CSID, Pair.of(values[5], values[6]));

                                    if (values[4].contains("+")) {
                                        RSIDs = values[4].split("\\+");
                                        for (String RSID : RSIDs) {
                                            list.add(RSID);
                                        }
                                        gold.put(values[0] + "_" + values[1] + "_" + values[2] + "_" + CSID, list);
                                    } else {
                                        list.add(values[4]);
                                        gold.put(values[0] + "_" + values[1] + "_" + values[2] + "_" + CSID, list);
                                    }
                                }
                            } else {
                                references.put(values[0] + "_" + values[1] + "_" + values[2] + "_" + values[3], Pair.of(values[5], values[6]));

                                if (values[4].contains("+")) {
                                    RSIDs = values[4].split("\\+");
                                    for (String RSID : RSIDs) {
                                        list.add(RSID);
                                    }
                                    gold.put(values[0] + "_" + values[1] + "_" + values[2] + "_" + values[3], list);
                                } else {
                                    list.add(values[4]);
                                    gold.put(values[0] + "_" + values[1] + "_" + values[2] + "_" + values[3], list);
                                }
                            }
                        }
                    }
                }

                reader.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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

                File methodfacetModel = new File(modelsFolder + "methodfacetModel.model");
                File othersfacetModel = new File(modelsFolder + "othersfacetModel.model");

                File methodtrainingFacetDatastructure = new File(dataStructuresFolder + File.separator +
                        "methodfacetTrainingDataSet.arff");
                File otherstrainingFacetDatastructure = new File(dataStructuresFolder + File.separator +
                        "othersfacetTrainingDataSet.arff");

                System.out.println("Loading InputMappedClassifiers...");
                methodfacetInputMappedClassifier = Utilities.loadInputMappedClassifier(methodfacetModel, methodtrainingFacetDatastructure);
                othersfacetInputMappedClassifier = Utilities.loadInputMappedClassifier(othersfacetModel, otherstrainingFacetDatastructure);
                System.out.println("InputMappedClassifiers Loaded ...");

                System.out.println(rfolderName + " Start Classification...");
            }

            Document rp = ProcessedRCDocuments.get(rfolderName);
            AnnotationSet rpMarkups = rp.getAnnotations("Original markups");
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

                            Iterator rpSentencesIterator = rpSentences.iterator();
                            while (rpSentencesIterator.hasNext()) {
                                Annotation rpSentence = (Annotation) rpSentencesIterator.next();
                                rpStartS = rpSentence.getStartNode().getOffset();
                                rpEndS = rpSentence.getEndNode().getOffset();

                                for (String ref : references.keySet()) {
                                    if ((cpAnnotator.getFeatures().get("id").toString().split("_")[1].equals(ref.split("_")[0]) &&
                                            cpAnnotator.getFeatures().get("id").toString().split("_")[2].equals(ref.split("_")[1]) &&
                                            cpAnnotator.getFeatures().get("id").toString().split("_")[0].equals(ref.split("_")[2]) &&
                                            cpSentence.getFeatures().get("sid").equals(ref.split("_")[3])) &&
                                            (rpSentence.getFeatures().get("sid").equals(references.get(ref).getLeft().toString()) ||
                                                    rpSentence.getFeatures().get("sid").equals(references.get(ref).getRight().toString()))) {
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

                                            Instances methodfacetTestInstance = Utilities.generateNormalizedFacetTestInstance(te, trCtx, Utilities.generateFacetAttributes("METHOD", FeaturesMode.ALL), FeaturesMode.ALL);
                                            System.out.println("Match found ... ");
                                            System.out.println("Applying String to Word Vector Filter on the facet testing Dataset...");
                                            Instances methodfacetTestInstanceSTWVRPBIL = Utilities.applyStringToWordVectorFilter(methodfacetTestInstance,
                                                    "-R 204 -P rp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVCPBIL = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVRPBIL,
                                                    "-R 204 -P cp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVRPBIP = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVCPBIL,
                                                    "-R 204 -P rp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVCPBIP = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVRPBIP,
                                                    "-R 204 -P cp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVRPP = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVCPBIP,
                                                    "-R 204 -P rp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVCPP = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVRPP,
                                                    "-R 204 -P cp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVRPL = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVCPP,
                                                    "-R 204 -P rp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances methodfacetTestInstanceSTWVCPL = Utilities.applyStringToWordVectorFilter(methodfacetTestInstanceSTWVRPL,
                                                    "-R 204 -P cp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");

                                            System.out.println("Filter Applied...");

                                            System.out.println("Applying Reorder Filter on the testing Dataset...");
                                            Instances methodfacetTestInstanceRO = Utilities.applyReorderFilter(methodfacetTestInstanceSTWVCPL, "-R first-203,205-last,204");
                                            methodfacetTestInstanceRO.setClassIndex(methodfacetTestInstanceRO.numAttributes() - 1);
                                            System.out.println("Filter Applied...");

                                            Instances methodclassifiedFacetTestInstance = Utilities.classifyInstances(methodfacetTestInstanceRO, methodfacetInputMappedClassifier);

                                            if (!methodclassifiedFacetTestInstance.instance(0).classIsMissing()) {
                                                String methodpredictedfacet = methodclassifiedFacetTestInstance.instance(0).stringValue(methodclassifiedFacetTestInstance.instance(0).numAttributes() - 1);
                                                System.out.println("Method Facet predicted ... ");

                                                if (methodpredictedfacet.equals("METHOD")) {
                                                    //Storing output
                                                    AnnotationV3 annotationV3 = Utilities.writeSciSummOutput(te, trCtx, methodpredictedfacet);

                                                    OutputAnnotation outputAnnotation = new OutputAnnotation(annotationV3, trCtx, te, matchTestInstance, methodfacetTestInstance, 1d, "MATCH");

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
                                                    Instances othersfacetTestInstance = Utilities.generateNormalizedFacetTestInstance(te, trCtx, Utilities.generateFacetAttributes("OTHERS", FeaturesMode.ALL), FeaturesMode.ALL);
                                                    System.out.println("Match found ... ");
                                                    System.out.println("Applying String to Word Vector Filter on the facet testing Dataset...");
                                                    Instances othersfacetTestInstanceSTWVRPBIL = Utilities.applyStringToWordVectorFilter(othersfacetTestInstance,
                                                            "-R 204 -P rp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVCPBIL = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVRPBIL,
                                                            "-R 204 -P cp_bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVRPBIP = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVCPBIL,
                                                            "-R 204 -P rp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVCPBIP = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVRPBIP,
                                                            "-R 204 -P cp_bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVRPP = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVCPBIP,
                                                            "-R 204 -P rp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVCPP = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVRPP,
                                                            "-R 204 -P cp_p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVRPL = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVCPP,
                                                            "-R 204 -P rp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                                    Instances othersfacetTestInstanceSTWVCPL = Utilities.applyStringToWordVectorFilter(othersfacetTestInstanceSTWVRPL,
                                                            "-R 204 -P cp_l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");

                                                    System.out.println("Filter Applied...");

                                                    System.out.println("Applying Reorder Filter on the testing Dataset...");
                                                    Instances othersfacetTestInstanceRO = Utilities.applyReorderFilter(othersfacetTestInstanceSTWVCPL, "-R first-203,205-last,204");
                                                    othersfacetTestInstanceRO.setClassIndex(othersfacetTestInstanceRO.numAttributes() - 1);
                                                    System.out.println("Filter Applied...");

                                                    Instances othersclassifiedFacetTestInstance = Utilities.classifyInstances(othersfacetTestInstanceRO, othersfacetInputMappedClassifier);

                                                    if (!othersclassifiedFacetTestInstance.instance(0).classIsMissing()) {
                                                        String otherspredictedfacet = othersclassifiedFacetTestInstance.instance(0).stringValue(othersclassifiedFacetTestInstance.instance(0).numAttributes() - 1);
                                                        System.out.println("OTHERS Facet predicted ... ");

                                                        //Storing output
                                                        AnnotationV3 annotationV3 = Utilities.writeSciSummOutput(te, trCtx, otherspredictedfacet);

                                                        OutputAnnotation outputAnnotation = new OutputAnnotation(annotationV3, trCtx, te, matchTestInstance, othersfacetTestInstance, 1d, "MATCH");

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

                        } else {
                            System.out.println("Could not find the Citance Sentence.");
                        }
                    }

                }
            }
            System.gc();

            if (!generateTraining) {
                //Print Result Annotations
                ProcessedRCDocuments = Utilities.spitOutOutputAnnotations(output, ProcessedRCDocuments, rfolderName, 70, String.valueOf(1.0), outputFolder);
            }

            System.out.println("Exporting new GATE Documents ...");
            Utilities.exportGATEDocuments(ProcessedRCDocuments, rfolderName, outputFolder + File.separator + "Normalized");

            System.out.println("END PROCESSING");
        } else {
            System.out.println("Wrong Number of Arguments");
        }
    }
}
