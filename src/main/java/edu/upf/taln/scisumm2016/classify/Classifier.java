package edu.upf.taln.scisumm2016.classify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.upf.taln.dri.lib.Factory;
import edu.upf.taln.dri.lib.exception.DRIexception;
import edu.upf.taln.scisumm2016.Utilities;
import edu.upf.taln.scisumm2016.reader.AnnotationV3;
import gate.Document;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Created by ahmed on 5/19/2016.
 */
public class Classifier {
    private static Logger logger = Logger.getLogger(Classifier.class);

    public static void main(String args[]) {
        /*
        if (args.length == 3) {
            HashMap<String, Document> RCDocuments;
            HashMap<String, Document> annotatedRCDocuments = null;
            HashMap<String, Document> drInventerProcessedRCDocuments;
            HashMap<String, Document> tokenLemmasFilledRCDocuments;

            ArrayList<AnnotationV3> annotationV3List;

            String rfolderName = args[1];

            boolean generateTraining;
            String outputInstancesType;
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
            System.out.println("Started");
            System.out.println("Initializing Dr. Inventor Framework ...");
            try {
                // A) IMPORTANT: Initialize Dr. Inventor Framework
                // A.1) set the local path to the config_file previously downloaded
                Factory.setDRIPropertyFilePath(Main.workingDir + "/DRIconfig.properties");
                // A.2) Initialize the Dr. Inventor Text Mining Framework
                Factory.initFramework();
            } catch (DRIexception drIexception) {
                drIexception.printStackTrace();
            }
            System.out.println("Dr. Inventor Framework Initialized");


            String baseFolder = Main.workingDir + File.separator + rfolderName;
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

            System.out.println(rfolderName + " Start extracting documents from the input Folder ...");
            RCDocuments = Utilities.extractDocumentsFromBaseFolder(new File(inputFolder));
            System.out.println(rfolderName + " Extracting done...");
            System.out.println(rfolderName + " Start applying annotations on the documents ...");

            if(generateTraining)
            {
                annotationV3List = Utilities.extractAnnotationsV3FromBaseFolder(new File(baseFolder), generateTraining);
                annotatedRCDocuments = Utilities.applyAnnotations(RCDocuments, annotationV3List, generateTraining);
            }


            System.out.println(rfolderName + " Annotations applied ...");
            System.out.println(rfolderName + " Start applying DR. Inventer annotations on the documents ...");
            drInventerProcessedRCDocuments = Utilities.applyDRInventer(annotatedRCDocuments);
            System.out.println(rfolderName + " DR. Inventer annotations applied ...");
            System.out.println(rfolderName + " Start applying Token Lemmas Filling on the documents ...");
            tokenLemmasFilledRCDocuments = Utilities.applyTokenLemmasFilling(drInventerProcessedRCDocuments);
            System.out.println(rfolderName + " Token Lemmas Filled ...");

            System.out.println("Exporting new GATE Documents ...");
            Utilities.exportGATEDocuments(tokenLemmasFilledRCDocuments, rfolderName, outputFolder);





        }*/
/*
        if (args.length == 2) {
            String rfolderName = args[1];
            String baseFolder = Main.workingDir + File.separator + rfolderName;
            String outputFolder = baseFolder + "/Output/";
            String modelsFolder = baseFolder + "/Models/";
            String dataStructuresFolder = baseFolder + "/ARFFs/";

            File matchModel = new File(modelsFolder + "matchModel.model");
            File facetModel = new File(modelsFolder + "facetModel.model");

            File trainingMatchDatastructure = new File(dataStructuresFolder + File.separator +
                    "matchTrainingDataSet.arff");
            File trainingFacetDatastructure = new File(dataStructuresFolder + File.separator +
                    "facetTrainingDataSet.arff");
            File testingMatchDatastructure = new File(dataStructuresFolder + File.separator + rfolderName +
                    "_scisumm2016_Testing_v_1.arff");
            File testingFacetDatastructure = new File(dataStructuresFolder + File.separator + rfolderName +
                    "_scisumm2016_FacetTesting_v_1.arff");

            Logger.getRootLogger().setLevel(Level.DEBUG);
            logger.debug(rfolderName + " Start Classification...");

            Instances trainingMatchDataset = Utilities.readDataStructure(trainingMatchDatastructure);
            Instances trainingFacetDataset = Utilities.readDataStructure(trainingFacetDatastructure);
            Instances testingMatchDataset = Utilities.readDataStructure(testingMatchDatastructure);
            Instances testingFacetDataset = Utilities.readDataStructure(testingFacetDatastructure);

            logger.debug("Applying String to Word Vector Filter on the testing Dataset...");

            Instances testingFacetDatasetSTWVRP = Utilities.applyStringToWordVectorFilter(testingFacetDataset,
                    "-R 14 -P rp_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\\'\\\\\\\"()?!\\\"\"");
            Instances testingFacetDatasetSTWVRPCP = Utilities.applyStringToWordVectorFilter(testingFacetDatasetSTWVRP,
                    "-R 14 -P cp_ -W 1000 -prune-rate -1.0 -N 0 -stemmer weka.core.stemmers.NullStemmer -stopwords-handler weka.core.stopwords.Null -M 1 -tokenizer \"weka.core.tokenizers.WordTokenizer -delimiters \\\" \\\\r\\\\n\\\\t.,;:\\\\\\\'\\\\\\\"()?!\\\"\"");

            logger.debug("Filter Applied...");

            logger.debug("Applying Reorder Filter on the testing Dataset...");

            Instances testingFacetDatasetRO = Utilities.applyReorderFilter(testingFacetDatasetSTWVRPCP, "-R first-13,15-last,14");
            testingFacetDatasetRO.setClassIndex(testingFacetDatasetRO.numAttributes() - 1);

            logger.debug("Filter Applied...");

            logger.debug("Loading InputMappedClassifiers...");
            InputMappedClassifier matchInputMappedClassifier = Utilities.loadInputMappedClassifier(matchModel, trainingMatchDatastructure);
            InputMappedClassifier facetInputMappedClassifier = Utilities.loadInputMappedClassifier(facetModel, trainingFacetDatastructure);
            logger.debug("InputMappedClassifiers Loaded ...");

            logger.debug("Classify testing Instances...");
            Instances classifiedMatchInstances = Utilities.classifyInstances(testingMatchDataset, matchInputMappedClassifier);
            Instances classifiedFacetInstances = Utilities.classifyInstances(testingFacetDatasetRO, facetInputMappedClassifier);
            logger.debug("Classification Done...");

            logger.debug("Store Output ARFFs...");
            Utilities.saveResultsFile(classifiedMatchInstances, outputFolder + "match.arff");
            Utilities.saveResultsFile(classifiedFacetInstances, outputFolder + "facet.arff");
            logger.debug("ARFFs Stored...");

            logger.info("END PROCESSING");

        }*/
    }
}
