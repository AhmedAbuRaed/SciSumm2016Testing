package edu.upf.taln.scisumm2016.feature;

import edu.upf.taln.ml.feat.*;
import edu.upf.taln.ml.feat.exception.FeatSetConsistencyException;
import edu.upf.taln.ml.feat.exception.FeatureException;
import edu.upf.taln.scisumm2016.feature.calculator.*;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;
import gate.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ahmed on 5/3/2016.
 */
public class FeatureGenerator {
    static HashMap<String, Document> RCDocuments;

    private static Set<String> matchClassValues = new HashSet<String>();
    private static Set<String> facetClassValues = new HashSet<String>();

    static {
        matchClassValues.add("MATCH");
        matchClassValues.add("NO_MATCH");
    }

    static {
        facetClassValues.add("AIM");
        facetClassValues.add("HYPOTHESIS");
        facetClassValues.add("METHOD");
        facetClassValues.add("RESULT");
        facetClassValues.add("IMPLICATION");
    }

    public static void generateMatchARFFfromFeaturesAnnotations(HashMap<String, Document> RCDocuments, String outputPath,
                                                                String rfolderName, boolean generateTraining) {

        String outputInstancesType = (generateTraining) ? "Match_Training" : "Match_Testing";
        String version = "1";

        Logger.getRootLogger().setLevel(Level.DEBUG);

        FeatureSet<TrainingExample, DocumentCtx> featSet = new FeatureSet<TrainingExample, DocumentCtx>();

        // Adding document identifier
        try {
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("SENTENCE_POSITION", new sentenceID()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("SENTENCE_SECTION_POSITION", new sentenceSectionID()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_AIM", new facetAim()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_HYPOTHESIS", new facetHypothesis()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_IMPLICATION", new facetImplication()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_METHOD", new facetMethod()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_RESULT", new facetResult()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("JIANGCONRATH_SIMILARITY", new jiangconrathSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("LCH_SIMILARITY", new lchSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("LESK_SIMILARITY", new leskSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("LIN_SIMILARITY", new linSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PATH_SIMILARITY", new pathSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RESNIK_SIMILARITY", new resnikSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("WUP_SIMILARITY", new wupSimilarity()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("COSINE_SIMILARITY", new cosineSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("BABELNET_COSINE_SIMILARITY", new babelnetCosineSimilarity()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_APPROACH", new probabilityApproach()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_BACKGROUND", new probabilityBackground()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_CHALLENGE", new probabilityChallenge()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_FUTUREWORK", new probabilityFutureWork()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_OUTCOME", new probabilityOutcome()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CP_CITMARKER_COUNT", new CP_CITMARKER_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RP_CITMARKER_COUNT", new RP_CITMARKER_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CITMARKER_COUNT", new CITMARKER_COUNT()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CP_CAUSEAFFECT_EXISTANCE", new CP_CAUSEAFFECT_EXISTANCE()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RP_CAUSEAFFECT_EXISTANCE", new RP_CAUSEAFFECT_EXISTANCE()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CP_COREFCHAINS_COUNT", new CP_COREFCHAINS_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RP_COREFCHAINS_COUNT", new RP_COREFCHAINS_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("COREFCHAINS_COUNT", new COREFCHAINS_COUNT()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZRESEARCHMT_PROP", new GAZRESEARCHMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZRESEARCHMT_PROP", new RPGAZRESEARCHMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZRESEARCHMT_PROP", new CPGAZRESEARCHMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZARGUMENTATIONMT_PROP", new GAZARGUMENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZARGUMENTATIONMT_PROP", new RPGAZARGUMENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZARGUMENTATIONMT_PROP", new CPGAZARGUMENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAWAREMT_PROP", new GAZAWAREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAWAREMT_PROP", new RPGAZAWAREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAWAREMT_PROP", new CPGAZAWAREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZUSEMT_PROP", new GAZUSEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZUSEMT_PROP", new RPGAZUSEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZUSEMT_PROP", new CPGAZUSEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROBLEMMT_PROP", new GAZPROBLEMMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROBLEMMT_PROP", new RPGAZPROBLEMMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROBLEMMT_PROP", new CPGAZPROBLEMMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSOLUTIONMT_PROP", new GAZSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSOLUTIONMT_PROP", new RPGAZSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSOLUTIONMT_PROP", new CPGAZSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZBETTERSOLUTIONMT_PROP", new GAZBETTERSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZBETTERSOLUTIONMT_PROP", new RPGAZBETTERSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZBETTERSOLUTIONMT_PROP", new CPGAZBETTERSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTEXTSTRUCTUREMT_PROP", new GAZTEXTSTRUCTUREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTEXTSTRUCTUREMT_PROP", new RPGAZTEXTSTRUCTUREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTEXTSTRUCTUREMT_PROP", new CPGAZTEXTSTRUCTUREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZINTRESTMT_PROP", new GAZINTRESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZINTRESTMT_PROP", new RPGAZINTRESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZINTRESTMT_PROP", new CPGAZINTRESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTINUEMT_PROP", new GAZCONTINUEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTINUEMT_PROP", new RPGAZCONTINUEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTINUEMT_PROP", new CPGAZCONTINUEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZFUTUREINTERESTMT_PROP", new GAZFUTUREINTERESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZFUTUREINTERESTMT_PROP", new RPGAZFUTUREINTERESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZFUTUREINTERESTMT_PROP", new CPGAZFUTUREINTERESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEEDMT_PROP", new GAZNEEDMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEEDMT_PROP", new RPGAZNEEDMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEEDMT_PROP", new CPGAZNEEDMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAFFECTMT_PROP", new GAZAFFECTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAFFECTMT_PROP", new RPGAZAFFECTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAFFECTMT_PROP", new CPGAZAFFECTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPRESENTATIONMT_PROP", new GAZPRESENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPRESENTATIONMT_PROP", new RPGAZPRESENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPRESENTATIONMT_PROP", new CPGAZPRESENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTRASTMT_PROP", new GAZCONTRASTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTRASTMT_PROP", new RPGAZCONTRASTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTRASTMT_PROP", new CPGAZCONTRASTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCHANGEMT_PROP", new GAZCHANGEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCHANGEMT_PROP", new RPGAZCHANGEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCHANGEMT_PROP", new CPGAZCHANGEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCOMPARISONMT_PROP", new GAZCOMPARISONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCOMPARISONMT_PROP", new RPGAZCOMPARISONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCOMPARISONMT_PROP", new CPGAZCOMPARISONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSIMILARMT_PROP", new GAZSIMILARMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSIMILARMT_PROP", new RPGAZSIMILARMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSIMILARMT_PROP", new CPGAZSIMILARMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCOMPARISONADJMT_PROP", new GAZCOMPARISONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCOMPARISONADJMT_PROP", new RPGAZCOMPARISONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCOMPARISONADJMT_PROP", new CPGAZCOMPARISONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZFUTUREADJMT_PROP", new GAZFUTUREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZFUTUREADJMT_PROP", new RPGAZFUTUREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZFUTUREADJMT_PROP", new CPGAZFUTUREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZINTERESTNOUNMT_PROP", new GAZINTERESTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZINTERESTNOUNMT_PROP", new RPGAZINTERESTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZINTERESTNOUNMT_PROP", new CPGAZINTERESTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZQUESTIONNOUNMT_PROP", new GAZQUESTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZQUESTIONNOUNMT_PROP", new RPGAZQUESTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZQUESTIONNOUNMT_PROP", new CPGAZQUESTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAWAREADJMT_PROP", new GAZAWAREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAWAREADJMT_PROP", new RPGAZAWAREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAWAREADJMT_PROP", new CPGAZAWAREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZARGUMENTATIONNOUNMT_PROP", new GAZARGUMENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZARGUMENTATIONNOUNMT_PROP", new RPGAZARGUMENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZARGUMENTATIONNOUNMT_PROP", new CPGAZARGUMENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSIMILARNOUNMT_PROP", new GAZSIMILARNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSIMILARNOUNMT_PROP", new RPGAZSIMILARNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSIMILARNOUNMT_PROP", new CPGAZSIMILARNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZEARLIERADJMT_PROP", new GAZEARLIERADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZEARLIERADJMT_PROP", new RPGAZEARLIERADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZEARLIERADJMT_PROP", new CPGAZEARLIERADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZRESEARCHADJMT_PROP", new GAZRESEARCHADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZRESEARCHADJMT_PROP", new RPGAZRESEARCHADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZRESEARCHADJMT_PROP", new CPGAZRESEARCHADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEEDADJMT_PROP", new GAZNEEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEEDADJMT_PROP", new RPGAZNEEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEEDADJMT_PROP", new CPGAZNEEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZREFERENTIALMT_PROP", new GAZREFERENTIALMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZREFERENTIALMT_PROP", new RPGAZREFERENTIALMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZREFERENTIALMT_PROP", new CPGAZREFERENTIALMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZQUESTIONMT_PROP", new GAZQUESTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZQUESTIONMT_PROP", new RPGAZQUESTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZQUESTIONMT_PROP", new CPGAZQUESTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZWORKNOUNMT_PROP", new GAZWORKNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZWORKNOUNMT_PROP", new RPGAZWORKNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZWORKNOUNMT_PROP", new CPGAZWORKNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCHANGEADJMT_PROP", new GAZCHANGEADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCHANGEADJMT_PROP", new RPGAZCHANGEADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCHANGEADJMT_PROP", new CPGAZCHANGEADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZDISCIPLINEMT_PROP", new GAZDISCIPLINEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZDISCIPLINEMT_PROP", new RPGAZDISCIPLINEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZDISCIPLINEMT_PROP", new CPGAZDISCIPLINEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZGIVENMT_PROP", new GAZGIVENMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZGIVENMT_PROP", new RPGAZGIVENMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZGIVENMT_PROP", new CPGAZGIVENMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZBADADJMT_PROP", new GAZBADADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZBADADJMT_PROP", new RPGAZBADADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZBADADJMT_PROP", new CPGAZBADADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTRASTNOUNMT_PROP", new GAZCONTRASTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTRASTNOUNMT_PROP", new RPGAZCONTRASTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTRASTNOUNMT_PROP", new CPGAZCONTRASTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEEDNOUNMT_PROP", new GAZNEEDNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEEDNOUNMT_PROP", new RPGAZNEEDNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEEDNOUNMT_PROP", new CPGAZNEEDNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAIMNOUNMT_PROP", new GAZAIMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAIMNOUNMT_PROP", new RPGAZAIMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAIMNOUNMT_PROP", new CPGAZAIMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTRASTADJMT_PROP", new GAZCONTRASTADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTRASTADJMT_PROP", new RPGAZCONTRASTADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTRASTADJMT_PROP", new CPGAZCONTRASTADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSOLUTIONNOUNMT_PROP", new GAZSOLUTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSOLUTIONNOUNMT_PROP", new RPGAZSOLUTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSOLUTIONNOUNMT_PROP", new CPGAZSOLUTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTRADITIONNOUNMT_PROP", new GAZTRADITIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTRADITIONNOUNMT_PROP", new RPGAZTRADITIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTRADITIONNOUNMT_PROP", new CPGAZTRADITIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZFIRSTPRONMT_PROP", new GAZFIRSTPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZFIRSTPRONMT_PROP", new RPGAZFIRSTPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZFIRSTPRONMT_PROP", new CPGAZFIRSTPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROFESSIONALSMT_PROP", new GAZPROFESSIONALSMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROFESSIONALSMT_PROP", new RPGAZPROFESSIONALSMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROFESSIONALSMT_PROP", new CPGAZPROFESSIONALSMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROBLEMNOUNMT_PROP", new GAZPROBLEMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROBLEMNOUNMT_PROP", new RPGAZPROBLEMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROBLEMNOUNMT_PROP", new CPGAZPROBLEMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEGATIONMT_PROP", new GAZNEGATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEGATIONMT_PROP", new RPGAZNEGATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEGATIONMT_PROP", new CPGAZNEGATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTEXTNOUNMT_PROP", new GAZTEXTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTEXTNOUNMT_PROP", new RPGAZTEXTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTEXTNOUNMT_PROP", new CPGAZTEXTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROBLEMADJMT_PROP", new GAZPROBLEMADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROBLEMADJMT_PROP", new RPGAZPROBLEMADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROBLEMADJMT_PROP", new CPGAZPROBLEMADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTHIRDPRONMT_PROP", new GAZTHIRDPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTHIRDPRONMT_PROP", new RPGAZTHIRDPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTHIRDPRONMT_PROP", new CPGAZTHIRDPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTRADITIONADJMT_PROP", new GAZTRADITIONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTRADITIONADJMT_PROP", new RPGAZTRADITIONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTRADITIONADJMT_PROP", new CPGAZTRADITIONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPRESENTATIONNOUNMT_PROP", new GAZPRESENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPRESENTATIONNOUNMT_PROP", new RPGAZPRESENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPRESENTATIONNOUNMT_PROP", new CPGAZPRESENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZRESEARCHNOUNMT_PROP", new GAZRESEARCHNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZRESEARCHNOUNMT_PROP", new RPGAZRESEARCHNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZRESEARCHNOUNMT_PROP", new CPGAZRESEARCHNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZMAINADJMT_PROP", new GAZMAINADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZMAINADJMT_PROP", new RPGAZMAINADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZMAINADJMT_PROP", new CPGAZMAINADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZREFLEXSIVEMT_PROP", new GAZREFLEXSIVEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZREFLEXSIVEMT_PROP", new RPGAZREFLEXSIVEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZREFLEXSIVEMT_PROP", new CPGAZREFLEXSIVEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEDADJMT_PROP", new GAZNEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEDADJMT_PROP", new RPGAZNEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEDADJMT_PROP", new CPGAZNEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZMANYMT_PROP", new GAZMANYMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZMANYMT_PROP", new RPGAZMANYMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZMANYMT_PROP", new CPGAZMANYMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCOMPARISONNOUNMT_PROP", new GAZCOMPARISONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCOMPARISONNOUNMT_PROP", new RPGAZCOMPARISONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCOMPARISONNOUNMT_PROP", new CPGAZCOMPARISONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZGOODADJMT_PROP", new GAZGOODADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZGOODADJMT_PROP", new RPGAZGOODADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZGOODADJMT_PROP", new CPGAZGOODADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCHANGENOUNMT_PROP", new GAZCHANGENOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCHANGENOUNMT_PROP", new RPGAZCHANGENOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCHANGENOUNMT_PROP", new CPGAZCHANGENOUNMT_PROP()));

            /*featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCEBIGRAMLEMMAS_STRING", new RP_SENTENCEBIGRAMLEMMAS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCEBIGRAMLEMMAS_STRING", new CP_SENTENCEBIGRAMLEMMAS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCEBIGRAMPOSS_STRING", new RP_SENTENCEBIGRAMPOSS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCEBIGRAMPOSS_STRING", new CP_SENTENCEBIGRAMPOSS_STRING()));

            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCEPOSS_STRING", new RP_SENTENCEPOSS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCEPOSS_STRING", new CP_SENTENCEPOSS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCELEMMAS_STRING", new RP_SENTENCELEMMAS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCELEMMAS_STRING", new CP_SENTENCELEMMAS_STRING()));*/

            // Class feature (lasts)
            featSet.addFeature(new NominalW<TrainingExample, DocumentCtx>("class", matchClassValues, new ClassGetter(true)));

        } catch (FeatureException e) {
            System.out.println("Error instantiating feature generation template.");
            e.printStackTrace();
            return;
        }

        System.out.println("ARFF File - " + outputInstancesType + " " + rfolderName + " instances generation...");

        Document rp = RCDocuments.get(rfolderName);
        AnnotationSet rpMatch_Features = rp.getAnnotations("Match_Features");
        AnnotationSet rpNoMatch_Features = rp.getAnnotations("NO_Match_Features");

        Iterator rpMatchIterator = rpMatch_Features.iterator();
        Iterator rpNoMatchIterator = rpNoMatch_Features.iterator();

        while (rpMatchIterator.hasNext())
        {
            Annotation rpMatch = (Annotation) rpMatchIterator.next();
            DocumentCtx trCtx = new DocumentCtx(rp, null);
            TrainingExample te = new TrainingExample(rpMatch, null, 1);
            featSet.addElement(te, trCtx);
        }

        while (rpNoMatchIterator.hasNext())
        {
            Annotation rpMatch = (Annotation) rpNoMatchIterator.next();
            DocumentCtx trCtx = new DocumentCtx(rp, null);
            TrainingExample te = new TrainingExample(rpMatch, null, 0);
            featSet.addElement(te, trCtx);
        }

        // --- STORE ARFF:
        System.out.println("STORING ARFF... " + rfolderName);
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(FeatUtil.wekaInstanceGeneration(featSet, rfolderName + " " +
                    outputInstancesType + " scisumm2016_v_" + version));
            saver.setFile(new File(outputPath + File.separator + "scisumm2016_" + outputInstancesType + "_v_" + version + ".arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeatSetConsistencyException e) {
            e.printStackTrace();
        }

    }

    public static void generateFacetARFFfromFeaturesAnnotations(HashMap<String, Document> RCDocuments, String outputPath,
                                                                String rfolderName, boolean generateTraining) {
        String outputInstancesType = (generateTraining) ? "Facet_Training" : "Facet_Testing";
        String version = "1";

        Logger.getRootLogger().setLevel(Level.DEBUG);

        FeatureSet<TrainingExample, DocumentCtx> featSet = new FeatureSet<TrainingExample, DocumentCtx>();

        // Adding document identifier
        try {
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("SENTENCE_POSITION", new sentenceID()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("SENTENCE_SECTION_POSITION", new sentenceSectionID()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_AIM", new facetAim()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_HYPOTHESIS", new facetHypothesis()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_IMPLICATION", new facetImplication()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_METHOD", new facetMethod()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("FACET_RESULT", new facetResult()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("JIANGCONRATH_SIMILARITY", new jiangconrathSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("LCH_SIMILARITY", new lchSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("LESK_SIMILARITY", new leskSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("LIN_SIMILARITY", new linSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PATH_SIMILARITY", new pathSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RESNIK_SIMILARITY", new resnikSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("WUP_SIMILARITY", new wupSimilarity()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("COSINE_SIMILARITY", new cosineSimilarity()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("BABELNET_COSINE_SIMILARITY", new babelnetCosineSimilarity()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_APPROACH", new probabilityApproach()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_BACKGROUND", new probabilityBackground()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_CHALLENGE", new probabilityChallenge()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_FUTUREWORK", new probabilityFutureWork()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("PROBABILITY_OUTCOME", new probabilityOutcome()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CP_CITMARKER_COUNT", new CP_CITMARKER_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RP_CITMARKER_COUNT", new RP_CITMARKER_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CITMARKER_COUNT", new CITMARKER_COUNT()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CP_CAUSEAFFECT_EXISTANCE", new CP_CAUSEAFFECT_EXISTANCE()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RP_CAUSEAFFECT_EXISTANCE", new RP_CAUSEAFFECT_EXISTANCE()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CP_COREFCHAINS_COUNT", new CP_COREFCHAINS_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RP_COREFCHAINS_COUNT", new RP_COREFCHAINS_COUNT()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("COREFCHAINS_COUNT", new COREFCHAINS_COUNT()));

            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZRESEARCHMT_PROP", new GAZRESEARCHMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZRESEARCHMT_PROP", new RPGAZRESEARCHMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZRESEARCHMT_PROP", new CPGAZRESEARCHMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZARGUMENTATIONMT_PROP", new GAZARGUMENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZARGUMENTATIONMT_PROP", new RPGAZARGUMENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZARGUMENTATIONMT_PROP", new CPGAZARGUMENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAWAREMT_PROP", new GAZAWAREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAWAREMT_PROP", new RPGAZAWAREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAWAREMT_PROP", new CPGAZAWAREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZUSEMT_PROP", new GAZUSEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZUSEMT_PROP", new RPGAZUSEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZUSEMT_PROP", new CPGAZUSEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROBLEMMT_PROP", new GAZPROBLEMMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROBLEMMT_PROP", new RPGAZPROBLEMMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROBLEMMT_PROP", new CPGAZPROBLEMMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSOLUTIONMT_PROP", new GAZSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSOLUTIONMT_PROP", new RPGAZSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSOLUTIONMT_PROP", new CPGAZSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZBETTERSOLUTIONMT_PROP", new GAZBETTERSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZBETTERSOLUTIONMT_PROP", new RPGAZBETTERSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZBETTERSOLUTIONMT_PROP", new CPGAZBETTERSOLUTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTEXTSTRUCTUREMT_PROP", new GAZTEXTSTRUCTUREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTEXTSTRUCTUREMT_PROP", new RPGAZTEXTSTRUCTUREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTEXTSTRUCTUREMT_PROP", new CPGAZTEXTSTRUCTUREMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZINTRESTMT_PROP", new GAZINTRESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZINTRESTMT_PROP", new RPGAZINTRESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZINTRESTMT_PROP", new CPGAZINTRESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTINUEMT_PROP", new GAZCONTINUEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTINUEMT_PROP", new RPGAZCONTINUEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTINUEMT_PROP", new CPGAZCONTINUEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZFUTUREINTERESTMT_PROP", new GAZFUTUREINTERESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZFUTUREINTERESTMT_PROP", new RPGAZFUTUREINTERESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZFUTUREINTERESTMT_PROP", new CPGAZFUTUREINTERESTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEEDMT_PROP", new GAZNEEDMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEEDMT_PROP", new RPGAZNEEDMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEEDMT_PROP", new CPGAZNEEDMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAFFECTMT_PROP", new GAZAFFECTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAFFECTMT_PROP", new RPGAZAFFECTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAFFECTMT_PROP", new CPGAZAFFECTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPRESENTATIONMT_PROP", new GAZPRESENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPRESENTATIONMT_PROP", new RPGAZPRESENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPRESENTATIONMT_PROP", new CPGAZPRESENTATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTRASTMT_PROP", new GAZCONTRASTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTRASTMT_PROP", new RPGAZCONTRASTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTRASTMT_PROP", new CPGAZCONTRASTMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCHANGEMT_PROP", new GAZCHANGEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCHANGEMT_PROP", new RPGAZCHANGEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCHANGEMT_PROP", new CPGAZCHANGEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCOMPARISONMT_PROP", new GAZCOMPARISONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCOMPARISONMT_PROP", new RPGAZCOMPARISONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCOMPARISONMT_PROP", new CPGAZCOMPARISONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSIMILARMT_PROP", new GAZSIMILARMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSIMILARMT_PROP", new RPGAZSIMILARMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSIMILARMT_PROP", new CPGAZSIMILARMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCOMPARISONADJMT_PROP", new GAZCOMPARISONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCOMPARISONADJMT_PROP", new RPGAZCOMPARISONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCOMPARISONADJMT_PROP", new CPGAZCOMPARISONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZFUTUREADJMT_PROP", new GAZFUTUREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZFUTUREADJMT_PROP", new RPGAZFUTUREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZFUTUREADJMT_PROP", new CPGAZFUTUREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZINTERESTNOUNMT_PROP", new GAZINTERESTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZINTERESTNOUNMT_PROP", new RPGAZINTERESTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZINTERESTNOUNMT_PROP", new CPGAZINTERESTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZQUESTIONNOUNMT_PROP", new GAZQUESTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZQUESTIONNOUNMT_PROP", new RPGAZQUESTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZQUESTIONNOUNMT_PROP", new CPGAZQUESTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAWAREADJMT_PROP", new GAZAWAREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAWAREADJMT_PROP", new RPGAZAWAREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAWAREADJMT_PROP", new CPGAZAWAREADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZARGUMENTATIONNOUNMT_PROP", new GAZARGUMENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZARGUMENTATIONNOUNMT_PROP", new RPGAZARGUMENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZARGUMENTATIONNOUNMT_PROP", new CPGAZARGUMENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSIMILARNOUNMT_PROP", new GAZSIMILARNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSIMILARNOUNMT_PROP", new RPGAZSIMILARNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSIMILARNOUNMT_PROP", new CPGAZSIMILARNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZEARLIERADJMT_PROP", new GAZEARLIERADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZEARLIERADJMT_PROP", new RPGAZEARLIERADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZEARLIERADJMT_PROP", new CPGAZEARLIERADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZRESEARCHADJMT_PROP", new GAZRESEARCHADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZRESEARCHADJMT_PROP", new RPGAZRESEARCHADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZRESEARCHADJMT_PROP", new CPGAZRESEARCHADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEEDADJMT_PROP", new GAZNEEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEEDADJMT_PROP", new RPGAZNEEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEEDADJMT_PROP", new CPGAZNEEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZREFERENTIALMT_PROP", new GAZREFERENTIALMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZREFERENTIALMT_PROP", new RPGAZREFERENTIALMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZREFERENTIALMT_PROP", new CPGAZREFERENTIALMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZQUESTIONMT_PROP", new GAZQUESTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZQUESTIONMT_PROP", new RPGAZQUESTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZQUESTIONMT_PROP", new CPGAZQUESTIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZWORKNOUNMT_PROP", new GAZWORKNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZWORKNOUNMT_PROP", new RPGAZWORKNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZWORKNOUNMT_PROP", new CPGAZWORKNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCHANGEADJMT_PROP", new GAZCHANGEADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCHANGEADJMT_PROP", new RPGAZCHANGEADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCHANGEADJMT_PROP", new CPGAZCHANGEADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZDISCIPLINEMT_PROP", new GAZDISCIPLINEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZDISCIPLINEMT_PROP", new RPGAZDISCIPLINEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZDISCIPLINEMT_PROP", new CPGAZDISCIPLINEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZGIVENMT_PROP", new GAZGIVENMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZGIVENMT_PROP", new RPGAZGIVENMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZGIVENMT_PROP", new CPGAZGIVENMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZBADADJMT_PROP", new GAZBADADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZBADADJMT_PROP", new RPGAZBADADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZBADADJMT_PROP", new CPGAZBADADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTRASTNOUNMT_PROP", new GAZCONTRASTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTRASTNOUNMT_PROP", new RPGAZCONTRASTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTRASTNOUNMT_PROP", new CPGAZCONTRASTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEEDNOUNMT_PROP", new GAZNEEDNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEEDNOUNMT_PROP", new RPGAZNEEDNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEEDNOUNMT_PROP", new CPGAZNEEDNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZAIMNOUNMT_PROP", new GAZAIMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZAIMNOUNMT_PROP", new RPGAZAIMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZAIMNOUNMT_PROP", new CPGAZAIMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCONTRASTADJMT_PROP", new GAZCONTRASTADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCONTRASTADJMT_PROP", new RPGAZCONTRASTADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCONTRASTADJMT_PROP", new CPGAZCONTRASTADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZSOLUTIONNOUNMT_PROP", new GAZSOLUTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZSOLUTIONNOUNMT_PROP", new RPGAZSOLUTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZSOLUTIONNOUNMT_PROP", new CPGAZSOLUTIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTRADITIONNOUNMT_PROP", new GAZTRADITIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTRADITIONNOUNMT_PROP", new RPGAZTRADITIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTRADITIONNOUNMT_PROP", new CPGAZTRADITIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZFIRSTPRONMT_PROP", new GAZFIRSTPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZFIRSTPRONMT_PROP", new RPGAZFIRSTPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZFIRSTPRONMT_PROP", new CPGAZFIRSTPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROFESSIONALSMT_PROP", new GAZPROFESSIONALSMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROFESSIONALSMT_PROP", new RPGAZPROFESSIONALSMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROFESSIONALSMT_PROP", new CPGAZPROFESSIONALSMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROBLEMNOUNMT_PROP", new GAZPROBLEMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROBLEMNOUNMT_PROP", new RPGAZPROBLEMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROBLEMNOUNMT_PROP", new CPGAZPROBLEMNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEGATIONMT_PROP", new GAZNEGATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEGATIONMT_PROP", new RPGAZNEGATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEGATIONMT_PROP", new CPGAZNEGATIONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTEXTNOUNMT_PROP", new GAZTEXTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTEXTNOUNMT_PROP", new RPGAZTEXTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTEXTNOUNMT_PROP", new CPGAZTEXTNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPROBLEMADJMT_PROP", new GAZPROBLEMADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPROBLEMADJMT_PROP", new RPGAZPROBLEMADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPROBLEMADJMT_PROP", new CPGAZPROBLEMADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTHIRDPRONMT_PROP", new GAZTHIRDPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTHIRDPRONMT_PROP", new RPGAZTHIRDPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTHIRDPRONMT_PROP", new CPGAZTHIRDPRONMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZTRADITIONADJMT_PROP", new GAZTRADITIONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZTRADITIONADJMT_PROP", new RPGAZTRADITIONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZTRADITIONADJMT_PROP", new CPGAZTRADITIONADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZPRESENTATIONNOUNMT_PROP", new GAZPRESENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZPRESENTATIONNOUNMT_PROP", new RPGAZPRESENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZPRESENTATIONNOUNMT_PROP", new CPGAZPRESENTATIONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZRESEARCHNOUNMT_PROP", new GAZRESEARCHNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZRESEARCHNOUNMT_PROP", new RPGAZRESEARCHNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZRESEARCHNOUNMT_PROP", new CPGAZRESEARCHNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZMAINADJMT_PROP", new GAZMAINADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZMAINADJMT_PROP", new RPGAZMAINADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZMAINADJMT_PROP", new CPGAZMAINADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZREFLEXSIVEMT_PROP", new GAZREFLEXSIVEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZREFLEXSIVEMT_PROP", new RPGAZREFLEXSIVEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZREFLEXSIVEMT_PROP", new CPGAZREFLEXSIVEMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZNEDADJMT_PROP", new GAZNEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZNEDADJMT_PROP", new RPGAZNEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZNEDADJMT_PROP", new CPGAZNEDADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZMANYMT_PROP", new GAZMANYMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZMANYMT_PROP", new RPGAZMANYMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZMANYMT_PROP", new CPGAZMANYMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCOMPARISONNOUNMT_PROP", new GAZCOMPARISONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCOMPARISONNOUNMT_PROP", new RPGAZCOMPARISONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCOMPARISONNOUNMT_PROP", new CPGAZCOMPARISONNOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZGOODADJMT_PROP", new GAZGOODADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZGOODADJMT_PROP", new RPGAZGOODADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZGOODADJMT_PROP", new CPGAZGOODADJMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("GAZCHANGENOUNMT_PROP", new GAZCHANGENOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("RPGAZCHANGENOUNMT_PROP", new RPGAZCHANGENOUNMT_PROP()));
            featSet.addFeature(new NumericW<TrainingExample, DocumentCtx>("CPGAZCHANGENOUNMT_PROP", new CPGAZCHANGENOUNMT_PROP()));

            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCEBIGRAMLEMMAS_STRING", new RP_SENTENCEBIGRAMLEMMAS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCEBIGRAMLEMMAS_STRING", new CP_SENTENCEBIGRAMLEMMAS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCEBIGRAMPOSS_STRING", new RP_SENTENCEBIGRAMPOSS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCEBIGRAMPOSS_STRING", new CP_SENTENCEBIGRAMPOSS_STRING()));

            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCEPOSS_STRING", new RP_SENTENCEPOSS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCEPOSS_STRING", new CP_SENTENCEPOSS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("RP_SENTENCELEMMAS_STRING", new RP_SENTENCELEMMAS_STRING()));
            featSet.addFeature(new StringW<TrainingExample, DocumentCtx>("CP_SENTENCELEMMAS_STRING", new CP_SENTENCELEMMAS_STRING()));

            // Class feature (lasts)
            featSet.addFeature(new NominalW<TrainingExample, DocumentCtx>("class", facetClassValues, new ClassGetter(false)));
        } catch (FeatureException e) {
            System.out.println("Error instantiating feature generation template.");
            e.printStackTrace();
            return;
        }

        System.out.println("ARFF File - " + outputInstancesType + " " + rfolderName + " instances generation...");

        Document rp = RCDocuments.get(rfolderName);
        AnnotationSet rpFacet_Features = rp.getAnnotations("Facet_Features");

        Iterator rpFacetIterator = rpFacet_Features.iterator();
        while (rpFacetIterator.hasNext())
        {
            Annotation rpFacet = (Annotation) rpFacetIterator.next();
            DocumentCtx trCtx = new DocumentCtx(rp, null);
            TrainingExample te = new TrainingExample(rpFacet, null, rpFacet.getFeatures().get("class").toString());
            featSet.addElement(te, trCtx);
        }

        // --- STORE ARFF:
        System.out.println("STORING ARFF... " + rfolderName);
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(FeatUtil.wekaInstanceGeneration(featSet, rfolderName + " " +
                    outputInstancesType + " scisumm2016_v_" + version));
            saver.setFile(new File(outputPath + File.separator + "scisumm2016_" + outputInstancesType + "_v_" + version + ".arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeatSetConsistencyException e) {
            e.printStackTrace();
        }


    }

}
