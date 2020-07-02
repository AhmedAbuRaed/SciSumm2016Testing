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
 * Created by Ahmed on 5/3/17.
 */
public class FacetsWEallMergedSystemBasedMain {
    public static String workingDir;

    public static void FacetsWEallMergedSystemMainRun(String args[]) {
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

            InputMappedClassifier facetInputMappedClassifier = null;

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

                File facetModel = new File(modelsFolder + "facetModel.model");

                File trainingFacetDatastructure = new File(dataStructuresFolder + File.separator +
                        "facetTrainingDataSet.arff");

                System.out.println("Loading InputMappedClassifiers...");
                facetInputMappedClassifier = Utilities.loadInputMappedClassifier(facetModel, trainingFacetDatastructure);

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

                                            Instances facetTestInstance = Utilities.generateNormalizedFacetTestInstance(te, trCtx, Utilities.generateFacetAttributes("ALL", FeaturesMode.MERGED), FeaturesMode.MERGED);
                                            System.out.println("Match found ... ");
                                            System.out.println("Applying String to Word Vector Filter on the facet testing Dataset...");
                                            Instances facetTestInstanceSTWVBIL = Utilities.applyStringToWordVectorFilter(facetTestInstance,
                                                    "-R 204 -P bil_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances facetTestInstanceSTWVBIP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVBIL,
                                                    "-R 204 -P bip_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances facetTestInstanceSTWVP = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVBIP,
                                                    "-R 204 -P p_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            Instances facetTestInstanceSTWVL = Utilities.applyStringToWordVectorFilter(facetTestInstanceSTWVP,
                                                    "-R 204 -P l_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\'\\\\\\\"()?!\\\"\"");
                                            System.out.println("Filter Applied...");

                                            System.out.println("Applying Reorder Filter on the testing Dataset...");
                                            Instances facetTestInstanceRO = Utilities.applyReorderFilter(facetTestInstanceSTWVL, "-R first-203,205-last,204");

                                            facetTestInstanceRO.setClassIndex(facetTestInstanceRO.numAttributes() - 1);
                                            System.out.println("Filter Applied...");

                                            Instances classifiedFacetTestInstance = Utilities.classifyInstances(facetTestInstanceRO, facetInputMappedClassifier);

                                            if (!classifiedFacetTestInstance.instance(0).classIsMissing()) {
                                                String predictedfacet = classifiedFacetTestInstance.instance(0).stringValue(classifiedFacetTestInstance.instance(0).numAttributes() - 1);
                                                System.out.println("Method Facet predicted ... ");

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
                ProcessedRCDocuments = Utilities.spitOutOutputAnnotations(output, ProcessedRCDocuments, rfolderName, 2, String.valueOf(0.44), outputFolder);
            }

            System.out.println("Exporting new GATE Documents ...");
            Utilities.exportGATEDocuments(ProcessedRCDocuments, rfolderName, outputFolder + File.separator + "Normalized");

            System.out.println("END PROCESSING");
        } else {
            System.out.println("Wrong Number of Arguments");
        }
    }
}
