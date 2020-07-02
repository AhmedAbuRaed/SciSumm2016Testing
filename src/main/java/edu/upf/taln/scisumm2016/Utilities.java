package edu.upf.taln.scisumm2016;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.*;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.upf.taln.dri.lib.demo.Util;
import edu.upf.taln.scisumm2016.classify.Main;
import edu.upf.taln.scisumm2016.feature.calculator.RPGAZAWAREMT_PROP;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.*;
import gate.*;
import gate.Factory;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.InvalidOffsetException;
import gate.util.persistence.PersistenceManager;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by ahmed on 5/19/2016.
 */
public class Utilities {
    public static int counter = 1;
    public static int finalCounter = 1;
    public static CorpusController application;

    public static Instances readDataStructure(File dataStructure) {
        BufferedReader reader = null;
        Instances data = null;
        try {
            reader = new BufferedReader(
                    new FileReader(dataStructure));
            data = new Instances(reader);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // setting class attribute
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    public static ArrayList<Attribute> generateMatchAttributes() {
        ArrayList<Attribute> matchFeatures = new ArrayList<Attribute>();
        matchFeatures.add(new Attribute("SENTENCE_POSITION"));
        matchFeatures.add(new Attribute("SENTENCE_SECTION_POSITION"));

        matchFeatures.add(new Attribute("FACET_AIM"));
        matchFeatures.add(new Attribute("FACET_HYPOTHESIS"));
        matchFeatures.add(new Attribute("FACET_IMPLICATION"));
        matchFeatures.add(new Attribute("FACET_METHOD"));
        matchFeatures.add(new Attribute("FACET_RESULT"));

        matchFeatures.add(new Attribute("JIANGCONRATH_SIMILARITY"));
        matchFeatures.add(new Attribute("LCH_SIMILARITY"));
        matchFeatures.add(new Attribute("LESK_SIMILARITY"));
        matchFeatures.add(new Attribute("LIN_SIMILARITY"));
        matchFeatures.add(new Attribute("PATH_SIMILARITY"));
        matchFeatures.add(new Attribute("RESNIK_SIMILARITY"));
        matchFeatures.add(new Attribute("WUP_SIMILARITY"));

        matchFeatures.add(new Attribute("COSINE_SIMILARITY"));
        matchFeatures.add(new Attribute("BABELNET_COSINE_SIMILARITY"));

        matchFeatures.add(new Attribute("PROBABILITY_APPROACH"));
        matchFeatures.add(new Attribute("PROBABILITY_BACKGROUND"));
        matchFeatures.add(new Attribute("PROBABILITY_CHALLENGE"));
        matchFeatures.add(new Attribute("PROBABILITY_FUTUREWORK"));
        matchFeatures.add(new Attribute("PROBABILITY_OUTCOME"));

        matchFeatures.add(new Attribute("CP_CITMARKER_COUNT"));
        matchFeatures.add(new Attribute("RP_CITMARKER_COUNT"));
        matchFeatures.add(new Attribute("CITMARKER_COUNT"));

        matchFeatures.add(new Attribute("CP_CAUSEAFFECT_EXISTANCE"));
        matchFeatures.add(new Attribute("RP_CAUSEAFFECT_EXISTANCE"));

        matchFeatures.add(new Attribute("CP_COREFCHAINS_COUNT"));
        matchFeatures.add(new Attribute("RP_COREFCHAINS_COUNT"));
        matchFeatures.add(new Attribute("COREFCHAINS_COUNT"));

        matchFeatures.add(new Attribute("GAZRESEARCHMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZRESEARCHMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZRESEARCHMT_PROP"));
        matchFeatures.add(new Attribute("GAZARGUMENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZARGUMENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZARGUMENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZAWAREMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAWAREMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAWAREMT_PROP"));
        matchFeatures.add(new Attribute("GAZUSEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZUSEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZUSEMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROBLEMMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROBLEMMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROBLEMMT_PROP"));
        matchFeatures.add(new Attribute("GAZSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZBETTERSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZBETTERSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZBETTERSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZTEXTSTRUCTUREMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTEXTSTRUCTUREMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTEXTSTRUCTUREMT_PROP"));
        matchFeatures.add(new Attribute("GAZINTRESTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZINTRESTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZINTRESTMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTINUEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTINUEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTINUEMT_PROP"));
        matchFeatures.add(new Attribute("GAZFUTUREINTERESTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZFUTUREINTERESTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZFUTUREINTERESTMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEEDMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEEDMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEEDMT_PROP"));
        matchFeatures.add(new Attribute("GAZAFFECTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAFFECTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAFFECTMT_PROP"));
        matchFeatures.add(new Attribute("GAZPRESENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPRESENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPRESENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTRASTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTRASTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTRASTMT_PROP"));
        matchFeatures.add(new Attribute("GAZCHANGEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCHANGEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCHANGEMT_PROP"));
        matchFeatures.add(new Attribute("GAZCOMPARISONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCOMPARISONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCOMPARISONMT_PROP"));
        matchFeatures.add(new Attribute("GAZSIMILARMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSIMILARMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSIMILARMT_PROP"));
        matchFeatures.add(new Attribute("GAZCOMPARISONADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCOMPARISONADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCOMPARISONADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZFUTUREADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZFUTUREADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZFUTUREADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZINTERESTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZINTERESTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZINTERESTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZQUESTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZQUESTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZQUESTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZAWAREADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAWAREADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAWAREADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZARGUMENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZARGUMENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZARGUMENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZSIMILARNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSIMILARNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSIMILARNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZEARLIERADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZEARLIERADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZEARLIERADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZRESEARCHADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZRESEARCHADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZRESEARCHADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEEDADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEEDADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEEDADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZREFERENTIALMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZREFERENTIALMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZREFERENTIALMT_PROP"));
        matchFeatures.add(new Attribute("GAZQUESTIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZQUESTIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZQUESTIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZWORKNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZWORKNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZWORKNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZCHANGEADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCHANGEADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCHANGEADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZDISCIPLINEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZDISCIPLINEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZDISCIPLINEMT_PROP"));
        matchFeatures.add(new Attribute("GAZGIVENMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZGIVENMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZGIVENMT_PROP"));
        matchFeatures.add(new Attribute("GAZBADADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZBADADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZBADADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTRASTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTRASTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTRASTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEEDNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEEDNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEEDNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZAIMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAIMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAIMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTRASTADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTRASTADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTRASTADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZSOLUTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSOLUTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSOLUTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZTRADITIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTRADITIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTRADITIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZFIRSTPRONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZFIRSTPRONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZFIRSTPRONMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROFESSIONALSMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROFESSIONALSMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROFESSIONALSMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROBLEMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROBLEMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROBLEMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEGATIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEGATIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEGATIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZTEXTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTEXTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTEXTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROBLEMADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROBLEMADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROBLEMADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZTHIRDPRONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTHIRDPRONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTHIRDPRONMT_PROP"));
        matchFeatures.add(new Attribute("GAZTRADITIONADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTRADITIONADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTRADITIONADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZPRESENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPRESENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPRESENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZRESEARCHNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZRESEARCHNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZRESEARCHNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZMAINADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZMAINADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZMAINADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZREFLEXSIVEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZREFLEXSIVEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZREFLEXSIVEMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEDADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEDADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEDADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZMANYMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZMANYMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZMANYMT_PROP"));
        matchFeatures.add(new Attribute("GAZCOMPARISONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCOMPARISONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCOMPARISONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZGOODADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZGOODADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZGOODADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZCHANGENOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCHANGENOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCHANGENOUNMT_PROP"));

        /*
        matchFeatures.add(new Attribute("RP_SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("RP_SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));

        matchFeatures.add(new Attribute("RP_SENTENCEPOSS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCEPOSS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("RP_SENTENCELEMMAS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCELEMMAS_STRING", (ArrayList) null));*/


        // declare Class attribute
        List fvClassVal = new ArrayList(2);
        fvClassVal.add("NO_MATCH");
        fvClassVal.add("MATCH");

        matchFeatures.add(new Attribute("class", fvClassVal));

        return matchFeatures;
    }

    public static ArrayList<Attribute> generateTopMatchAttributes() {
        ArrayList<Attribute> matchFeatures = new ArrayList<Attribute>();
        matchFeatures.add(new Attribute("SENTENCE_POSITION"));
        matchFeatures.add(new Attribute("SENTENCE_SECTION_POSITION"));

        matchFeatures.add(new Attribute("FACET_AIM"));
        matchFeatures.add(new Attribute("FACET_HYPOTHESIS"));
        matchFeatures.add(new Attribute("FACET_IMPLICATION"));
        matchFeatures.add(new Attribute("FACET_METHOD"));
        matchFeatures.add(new Attribute("FACET_RESULT"));

        matchFeatures.add(new Attribute("JIANGCONRATH_SIMILARITY"));
        matchFeatures.add(new Attribute("LCH_SIMILARITY"));
        matchFeatures.add(new Attribute("LESK_SIMILARITY"));
        matchFeatures.add(new Attribute("LIN_SIMILARITY"));
        matchFeatures.add(new Attribute("PATH_SIMILARITY"));
        matchFeatures.add(new Attribute("RESNIK_SIMILARITY"));
        matchFeatures.add(new Attribute("WUP_SIMILARITY"));

        matchFeatures.add(new Attribute("COSINE_SIMILARITY"));
        matchFeatures.add(new Attribute("BABELNET_COSINE_SIMILARITY"));

        matchFeatures.add(new Attribute("PROBABILITY_APPROACH"));
        matchFeatures.add(new Attribute("PROBABILITY_BACKGROUND"));
        matchFeatures.add(new Attribute("PROBABILITY_CHALLENGE"));
        matchFeatures.add(new Attribute("PROBABILITY_FUTUREWORK"));
        matchFeatures.add(new Attribute("PROBABILITY_OUTCOME"));

        matchFeatures.add(new Attribute("CP_CITMARKER_COUNT"));
        matchFeatures.add(new Attribute("RP_CITMARKER_COUNT"));
        matchFeatures.add(new Attribute("CITMARKER_COUNT"));

        matchFeatures.add(new Attribute("CP_CAUSEAFFECT_EXISTANCE"));
        matchFeatures.add(new Attribute("RP_CAUSEAFFECT_EXISTANCE"));

        matchFeatures.add(new Attribute("CP_COREFCHAINS_COUNT"));
        matchFeatures.add(new Attribute("RP_COREFCHAINS_COUNT"));
        matchFeatures.add(new Attribute("COREFCHAINS_COUNT"));

        matchFeatures.add(new Attribute("GAZRESEARCHMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZRESEARCHMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZRESEARCHMT_PROP"));
        matchFeatures.add(new Attribute("GAZARGUMENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZARGUMENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZARGUMENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZAWAREMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAWAREMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAWAREMT_PROP"));
        matchFeatures.add(new Attribute("GAZUSEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZUSEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZUSEMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROBLEMMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROBLEMMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROBLEMMT_PROP"));
        matchFeatures.add(new Attribute("GAZSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZBETTERSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZBETTERSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZBETTERSOLUTIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZTEXTSTRUCTUREMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTEXTSTRUCTUREMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTEXTSTRUCTUREMT_PROP"));
        matchFeatures.add(new Attribute("GAZINTRESTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZINTRESTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZINTRESTMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTINUEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTINUEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTINUEMT_PROP"));
        matchFeatures.add(new Attribute("GAZFUTUREINTERESTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZFUTUREINTERESTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZFUTUREINTERESTMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEEDMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEEDMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEEDMT_PROP"));
        matchFeatures.add(new Attribute("GAZAFFECTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAFFECTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAFFECTMT_PROP"));
        matchFeatures.add(new Attribute("GAZPRESENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPRESENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPRESENTATIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTRASTMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTRASTMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTRASTMT_PROP"));
        matchFeatures.add(new Attribute("GAZCHANGEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCHANGEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCHANGEMT_PROP"));
        matchFeatures.add(new Attribute("GAZCOMPARISONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCOMPARISONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCOMPARISONMT_PROP"));
        matchFeatures.add(new Attribute("GAZSIMILARMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSIMILARMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSIMILARMT_PROP"));
        matchFeatures.add(new Attribute("GAZCOMPARISONADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCOMPARISONADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCOMPARISONADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZFUTUREADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZFUTUREADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZFUTUREADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZINTERESTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZINTERESTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZINTERESTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZQUESTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZQUESTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZQUESTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZAWAREADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAWAREADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAWAREADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZARGUMENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZARGUMENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZARGUMENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZSIMILARNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSIMILARNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSIMILARNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZEARLIERADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZEARLIERADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZEARLIERADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZRESEARCHADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZRESEARCHADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZRESEARCHADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEEDADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEEDADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEEDADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZREFERENTIALMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZREFERENTIALMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZREFERENTIALMT_PROP"));
        matchFeatures.add(new Attribute("GAZQUESTIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZQUESTIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZQUESTIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZWORKNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZWORKNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZWORKNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZCHANGEADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCHANGEADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCHANGEADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZDISCIPLINEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZDISCIPLINEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZDISCIPLINEMT_PROP"));
        matchFeatures.add(new Attribute("GAZGIVENMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZGIVENMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZGIVENMT_PROP"));
        matchFeatures.add(new Attribute("GAZBADADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZBADADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZBADADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTRASTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTRASTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTRASTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEEDNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEEDNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEEDNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZAIMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZAIMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZAIMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZCONTRASTADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCONTRASTADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCONTRASTADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZSOLUTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZSOLUTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZSOLUTIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZTRADITIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTRADITIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTRADITIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZFIRSTPRONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZFIRSTPRONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZFIRSTPRONMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROFESSIONALSMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROFESSIONALSMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROFESSIONALSMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROBLEMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROBLEMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROBLEMNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEGATIONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEGATIONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEGATIONMT_PROP"));
        matchFeatures.add(new Attribute("GAZTEXTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTEXTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTEXTNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZPROBLEMADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPROBLEMADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPROBLEMADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZTHIRDPRONMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTHIRDPRONMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTHIRDPRONMT_PROP"));
        matchFeatures.add(new Attribute("GAZTRADITIONADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZTRADITIONADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZTRADITIONADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZPRESENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZPRESENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZPRESENTATIONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZRESEARCHNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZRESEARCHNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZRESEARCHNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZMAINADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZMAINADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZMAINADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZREFLEXSIVEMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZREFLEXSIVEMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZREFLEXSIVEMT_PROP"));
        matchFeatures.add(new Attribute("GAZNEDADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZNEDADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZNEDADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZMANYMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZMANYMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZMANYMT_PROP"));
        matchFeatures.add(new Attribute("GAZCOMPARISONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCOMPARISONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCOMPARISONNOUNMT_PROP"));
        matchFeatures.add(new Attribute("GAZGOODADJMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZGOODADJMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZGOODADJMT_PROP"));
        matchFeatures.add(new Attribute("GAZCHANGENOUNMT_PROP"));
        matchFeatures.add(new Attribute("RPGAZCHANGENOUNMT_PROP"));
        matchFeatures.add(new Attribute("CPGAZCHANGENOUNMT_PROP"));

        /*matchFeatures.add(new Attribute("RP_SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("RP_SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));

        matchFeatures.add(new Attribute("RP_SENTENCEPOSS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCEPOSS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("RP_SENTENCELEMMAS_STRING", (ArrayList) null));
        matchFeatures.add(new Attribute("CP_SENTENCELEMMAS_STRING", (ArrayList) null));*/

        // declare Class attribute
        List fvClassVal = new ArrayList(2);
        fvClassVal.add("NO_MATCH");
        fvClassVal.add("MATCH");

        matchFeatures.add(new Attribute("class", fvClassVal));

        return matchFeatures;
    }

    public static ArrayList<Attribute> generateFacetAttributes(String classValues, FeaturesMode featuresMode) {
        ArrayList<Attribute> facetFeatures = new ArrayList<Attribute>();
        facetFeatures.add(new Attribute("SENTENCE_POSITION"));
        facetFeatures.add(new Attribute("SENTENCE_SECTION_POSITION"));

        facetFeatures.add(new Attribute("FACET_AIM"));
        facetFeatures.add(new Attribute("FACET_HYPOTHESIS"));
        facetFeatures.add(new Attribute("FACET_IMPLICATION"));
        facetFeatures.add(new Attribute("FACET_METHOD"));
        facetFeatures.add(new Attribute("FACET_RESULT"));

        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("JIANGCONRATH_SIMILARITY"));
            facetFeatures.add(new Attribute("LCH_SIMILARITY"));
            facetFeatures.add(new Attribute("LESK_SIMILARITY"));
            facetFeatures.add(new Attribute("LIN_SIMILARITY"));
            facetFeatures.add(new Attribute("PATH_SIMILARITY"));
            facetFeatures.add(new Attribute("RESNIK_SIMILARITY"));
            facetFeatures.add(new Attribute("WUP_SIMILARITY"));
        }

        facetFeatures.add(new Attribute("COSINE_SIMILARITY"));
        facetFeatures.add(new Attribute("BABELNET_COSINE_SIMILARITY"));

        facetFeatures.add(new Attribute("PROBABILITY_APPROACH"));
        facetFeatures.add(new Attribute("PROBABILITY_BACKGROUND"));
        facetFeatures.add(new Attribute("PROBABILITY_CHALLENGE"));
        facetFeatures.add(new Attribute("PROBABILITY_FUTUREWORK"));
        facetFeatures.add(new Attribute("PROBABILITY_OUTCOME"));

        facetFeatures.add(new Attribute("CP_CITMARKER_COUNT"));
        facetFeatures.add(new Attribute("RP_CITMARKER_COUNT"));
        facetFeatures.add(new Attribute("CITMARKER_COUNT"));

        facetFeatures.add(new Attribute("CP_CAUSEAFFECT_EXISTANCE"));
        facetFeatures.add(new Attribute("RP_CAUSEAFFECT_EXISTANCE"));

        facetFeatures.add(new Attribute("CP_COREFCHAINS_COUNT"));
        facetFeatures.add(new Attribute("RP_COREFCHAINS_COUNT"));
        facetFeatures.add(new Attribute("COREFCHAINS_COUNT"));

        facetFeatures.add(new Attribute("GAZRESEARCHMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZRESEARCHMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZRESEARCHMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZARGUMENTATIONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZARGUMENTATIONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZARGUMENTATIONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZAWAREMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZAWAREMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZAWAREMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZUSEMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZUSEMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZUSEMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZPROBLEMMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZPROBLEMMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZPROBLEMMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZSOLUTIONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZSOLUTIONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZSOLUTIONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZBETTERSOLUTIONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZBETTERSOLUTIONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZBETTERSOLUTIONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZTEXTSTRUCTUREMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZTEXTSTRUCTUREMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZTEXTSTRUCTUREMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZINTRESTMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZINTRESTMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZINTRESTMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCONTINUEMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCONTINUEMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCONTINUEMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZFUTUREINTERESTMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZFUTUREINTERESTMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZFUTUREINTERESTMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZNEEDMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZNEEDMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZNEEDMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZAFFECTMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZAFFECTMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZAFFECTMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZPRESENTATIONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZPRESENTATIONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZPRESENTATIONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCONTRASTMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCONTRASTMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCONTRASTMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCHANGEMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCHANGEMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCHANGEMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCOMPARISONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCOMPARISONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCOMPARISONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZSIMILARMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZSIMILARMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZSIMILARMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCOMPARISONADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCOMPARISONADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCOMPARISONADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZFUTUREADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZFUTUREADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZFUTUREADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZINTERESTNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZINTERESTNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZINTERESTNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZQUESTIONNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZQUESTIONNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZQUESTIONNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZAWAREADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZAWAREADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZAWAREADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZARGUMENTATIONNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZARGUMENTATIONNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZARGUMENTATIONNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZSIMILARNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZSIMILARNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZSIMILARNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZEARLIERADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZEARLIERADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZEARLIERADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZRESEARCHADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZRESEARCHADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZRESEARCHADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZNEEDADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZNEEDADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZNEEDADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZREFERENTIALMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZREFERENTIALMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZREFERENTIALMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZQUESTIONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZQUESTIONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZQUESTIONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZWORKNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZWORKNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZWORKNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCHANGEADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCHANGEADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCHANGEADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZDISCIPLINEMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZDISCIPLINEMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZDISCIPLINEMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZGIVENMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZGIVENMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZGIVENMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZBADADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZBADADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZBADADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCONTRASTNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCONTRASTNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCONTRASTNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZNEEDNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZNEEDNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZNEEDNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZAIMNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZAIMNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZAIMNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCONTRASTADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCONTRASTADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCONTRASTADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZSOLUTIONNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZSOLUTIONNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZSOLUTIONNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZTRADITIONNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZTRADITIONNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZTRADITIONNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZFIRSTPRONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZFIRSTPRONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZFIRSTPRONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZPROFESSIONALSMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZPROFESSIONALSMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZPROFESSIONALSMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZPROBLEMNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZPROBLEMNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZPROBLEMNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZNEGATIONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZNEGATIONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZNEGATIONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZTEXTNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZTEXTNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZTEXTNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZPROBLEMADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZPROBLEMADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZPROBLEMADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZTHIRDPRONMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZTHIRDPRONMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZTHIRDPRONMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZTRADITIONADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZTRADITIONADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZTRADITIONADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZPRESENTATIONNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZPRESENTATIONNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZPRESENTATIONNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZRESEARCHNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZRESEARCHNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZRESEARCHNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZMAINADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZMAINADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZMAINADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZREFLEXSIVEMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZREFLEXSIVEMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZREFLEXSIVEMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZNEDADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZNEDADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZNEDADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZMANYMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZMANYMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZMANYMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCOMPARISONNOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCOMPARISONNOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCOMPARISONNOUNMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZGOODADJMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZGOODADJMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZGOODADJMT_PROP"));
        }
        facetFeatures.add(new Attribute("GAZCHANGENOUNMT_PROP"));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("RPGAZCHANGENOUNMT_PROP"));
            facetFeatures.add(new Attribute("CPGAZCHANGENOUNMT_PROP"));
        }

        if (featuresMode.equals(FeaturesMode.MERGED) || featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            facetFeatures.add(new Attribute("SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("SENTENCEPOSS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("SENTENCELEMMAS_STRING", (ArrayList) null));
        } else if (featuresMode.equals(FeaturesMode.ALL)) {
            facetFeatures.add(new Attribute("RP_SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("CP_SENTENCEBIGRAMLEMMAS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("RP_SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("CP_SENTENCEBIGRAMPOSS_STRING", (ArrayList) null));

            facetFeatures.add(new Attribute("RP_SENTENCEPOSS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("CP_SENTENCEPOSS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("RP_SENTENCELEMMAS_STRING", (ArrayList) null));
            facetFeatures.add(new Attribute("CP_SENTENCELEMMAS_STRING", (ArrayList) null));
        }

        if (classValues.equals("METHOD")) {
            // declare Class attribute
            List fvClassVal = new ArrayList(3);
            fvClassVal.add("DUMMY");
            fvClassVal.add("NOMETHOD");
            fvClassVal.add("METHOD");

            facetFeatures.add(new Attribute("class", fvClassVal));
        } else if (classValues.equals("OTHERS")) {
            // declare Class attribute
            List fvClassVal = new ArrayList(4);
            fvClassVal.add("DUMMY");
            fvClassVal.add("AIM");
            fvClassVal.add("IMPLICATION");
            fvClassVal.add("RESULT");

            facetFeatures.add(new Attribute("class", fvClassVal));
        } else if (classValues.equals("ALL")) {
            // declare Class attribute
            List fvClassVal = new ArrayList(5);
            fvClassVal.add("AIM");
            fvClassVal.add("HYPOTHESIS");
            fvClassVal.add("METHOD");
            fvClassVal.add("IMPLICATION");
            fvClassVal.add("RESULT");

            facetFeatures.add(new Attribute("class", fvClassVal));
        }

        return facetFeatures;
    }

    public static InputMappedClassifier loadInputMappedClassifier(File classifierModel, File
            classifierDataStructure) {
        // Load classifier
        SerializedClassifier coreClassifier = new SerializedClassifier();
        coreClassifier.setModelFile(classifierModel);
        coreClassifier.setDebug(false);

        // Load InputMappedClassifier and set the just loaded model as classifier
        InputMappedClassifier inputMappedClassifier = new InputMappedClassifier();

        inputMappedClassifier.setClassifier(coreClassifier);

        ConverterUtils.DataSource source = null;

        try {
            source = new ConverterUtils.DataSource(classifierDataStructure.getAbsolutePath());

            Instances headerModel = source.getDataSet();

            headerModel.setClassIndex(headerModel.numAttributes() - 1);
            inputMappedClassifier.setModelHeader(headerModel);
            inputMappedClassifier.setModelPath(classifierModel.getAbsolutePath());

            inputMappedClassifier.setDebug(false);
            inputMappedClassifier.setSuppressMappingReport(true);
            inputMappedClassifier.setTrim(true);
            inputMappedClassifier.setIgnoreCaseForNames(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputMappedClassifier;
    }

    public static void exportGATEDocuments(HashMap<String, Document> processedRCDocuments, String
            rfolder, String outputFolder) {
        PrintWriter pw = null;
        File ref = new File(outputFolder + File.separator + rfolder);

        // attempt to create the directory here
        ref.mkdirs();

        for (String docKey : processedRCDocuments.keySet()) {
            if (ref.exists()) {
                // creating the directory succeeded
                try {
                    pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFolder + File.separator
                            + rfolder + "/" + docKey + ".xml"), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pw.println(processedRCDocuments.get(docKey).toXml());
                pw.flush();
                pw.close();
            } else {
                // creating the directory failed
                System.out.println("failed trying to create the directory");
            }
        }

    }

    public static Instances applyStringToWordVectorFilter(Instances dataSet, String options) {
        Instances instances;
        Instances filteredInstances = null;
        try {
            instances = dataSet;
            instances.setClassIndex(instances.numAttributes() - 1);

            StringToWordVector stringToWordVectorFilter = new StringToWordVector();
            stringToWordVectorFilter.setInputFormat(dataSet);
            stringToWordVectorFilter.setOptions(weka.core.Utils.splitOptions(options));
            filteredInstances = Filter.useFilter(instances, stringToWordVectorFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredInstances;
    }

    public static Instances applyReorderFilter(Instances dataSet, String options) {
        Instances instances;
        Instances filteredInstances = null;
        try {
            instances = dataSet;
            instances.setClassIndex(instances.numAttributes() - 1);

            Reorder reorderFilter = new Reorder();
            reorderFilter.setOptions(weka.core.Utils.splitOptions(options));
            reorderFilter.setInputFormat(dataSet);
            filteredInstances = Filter.useFilter(instances, reorderFilter);
            filteredInstances.setClassIndex(filteredInstances.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredInstances;
    }

    public static Instances generateMatchTrainingInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features) {
        Instances trainingDataset = new Instances("MatchTrain", features, 0);
        trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);

        Instance trainInstance = new DenseInstance(204);
        trainingDataset.add(trainInstance);
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeJiangconrathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LCH_SIMILARITY"), Utilities.computeLCHSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LESK_SIMILARITY"), Utilities.computeLeskSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LIN_SIMILARITY"), Utilities.computeLinSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PATH_SIMILARITY"), Utilities.computePathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeResnikSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("WUP_SIMILARITY"), Utilities.computeWUPSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeCPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeRPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CITMARKER_COUNT"), Utilities.computeCitMarkerCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeCPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeRPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeCorefChainsCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        /*
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));*/


        if (obj.getIsMatch().equals(1)) {
            trainingDataset.instance(0).setClassValue("MATCH");
        } else {
            trainingDataset.instance(0).setClassValue("NO_MATCH");
        }

        return trainingDataset;
    }

    public static Instances generateNormalizedMatchTrainingInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features) {
        Instances trainingDataset = new Instances("MatchTrain", features, 0);
        trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);

        Instance trainInstance = new DenseInstance(204);
        trainingDataset.add(trainInstance);
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeNormalizedJiangconrathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LCH_SIMILARITY"), Utilities.computeNormalizedLCHSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LESK_SIMILARITY"), Utilities.computeNormalizedLeskSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LIN_SIMILARITY"), Utilities.computeNormalizedLinSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PATH_SIMILARITY"), Utilities.computeNormalizedPathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeNormalizedResnikSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("WUP_SIMILARITY"), Utilities.computeNormalizedWUPSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeNormalizedCPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeNormalizedRPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CITMARKER_COUNT"), Utilities.computeNormalizedCitMarkerCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeNormalizedCPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeNormalizedRPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeNormalizedCorefChainsCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        /*
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));*/


        if (obj.getIsMatch().equals(1)) {
            trainingDataset.instance(0).setClassValue("MATCH");
        } else {
            trainingDataset.instance(0).setClassValue("NO_MATCH");
        }

        return trainingDataset;
    }

    public static Instances generateMatchTestInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features) {
        Instances testingDataset = new Instances("MatchTest", features, 0);
        testingDataset.setClassIndex(testingDataset.numAttributes() - 1);

        Instance testInstance = new DenseInstance(204);
        testingDataset.add(testInstance);
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeJiangconrathSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LCH_SIMILARITY"), Utilities.computeLCHSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LESK_SIMILARITY"), Utilities.computeLeskSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LIN_SIMILARITY"), Utilities.computeLinSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PATH_SIMILARITY"), Utilities.computePathSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeResnikSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("WUP_SIMILARITY"), Utilities.computeWUPSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeCPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeRPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CITMARKER_COUNT"), Utilities.computeCitMarkerCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeCPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeRPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeCorefChainsCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        /*
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));*/

        testingDataset.instance(0).setClassMissing();

        return testingDataset;
    }

    public static Instances generateNormalizedMatchTestInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features) {
        Instances testingDataset = new Instances("MatchTest", features, 0);
        testingDataset.setClassIndex(testingDataset.numAttributes() - 1);

        Instance testInstance = new DenseInstance(204);
        testingDataset.add(testInstance);
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeNormalizedJiangconrathSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LCH_SIMILARITY"), Utilities.computeNormalizedLCHSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LESK_SIMILARITY"), Utilities.computeNormalizedLeskSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LIN_SIMILARITY"), Utilities.computeNormalizedLinSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PATH_SIMILARITY"), Utilities.computeNormalizedPathSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeNormalizedResnikSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("WUP_SIMILARITY"), Utilities.computeNormalizedWUPSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeNormalizedCPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeNormalizedRPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CITMARKER_COUNT"), Utilities.computeNormalizedCitMarkerCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeNormalizedCPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeNormalizedRPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeNormalizedCorefChainsCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        /*testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));*/

        testingDataset.instance(0).setClassMissing();

        return testingDataset;
    }

    public static Instances generateFacetTrainingInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features) {
        Instances trainingDataset = new Instances("FacetTrain", features, 0);
        trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);

        Instance trainInstance = new DenseInstance(212);
        trainingDataset.add(trainInstance);
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeJiangconrathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LCH_SIMILARITY"), Utilities.computeLCHSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LESK_SIMILARITY"), Utilities.computeLeskSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LIN_SIMILARITY"), Utilities.computeLinSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PATH_SIMILARITY"), Utilities.computePathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeResnikSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("WUP_SIMILARITY"), Utilities.computeWUPSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeCPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeRPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CITMARKER_COUNT"), Utilities.computeCitMarkerCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeCPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeRPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeCorefChainsCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));

        String classValue = null;

        String facet = obj.getFacet();
        switch (facet) {
            case "Aim_Citation":
            case "Aim_Facet":
            case "AIM":
                classValue = "AIM";
                break;
            case "Hypothesis_Citation":
            case "Hypothesis_Facet":
            case "HYPOTHESIS":
                classValue = "HYPOTHESIS";
                break;
            case "Method_Citation":
            case "Method_Facet":
            case "METHOD":
                classValue = "METHOD";
                break;
            case "Results_Citation":
            case "Results_Facet":
            case "RESULT":
                classValue = "RESULT";
                break;
            case "Implication_Citation":
            case "Implication_Facet":
            case "IMPLICATION":
                classValue = "IMPLICATION";
                break;
        }


        trainingDataset.instance(0).setClassValue(classValue);
        return trainingDataset;
    }

    public static Instances generateNormalizedFacetTrainingInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features) {
        Instances trainingDataset = new Instances("FacetTrain", features, 0);
        trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);

        Instance trainInstance = new DenseInstance(212);
        trainingDataset.add(trainInstance);
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeNormalizedJiangconrathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LCH_SIMILARITY"), Utilities.computeNormalizedLCHSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LESK_SIMILARITY"), Utilities.computeNormalizedLeskSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("LIN_SIMILARITY"), Utilities.computeNormalizedLinSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PATH_SIMILARITY"), Utilities.computeNormalizedPathSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeNormalizedResnikSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("WUP_SIMILARITY"), Utilities.computeNormalizedWUPSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeNormalizedCPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeNormalizedRPCitMarkerCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CITMARKER_COUNT"), Utilities.computeNormalizedCitMarkerCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeNormalizedCPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeNormalizedRPCorefChainsCount(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeNormalizedCorefChainsCount(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
        trainingDataset.instance(0).setValue(trainingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));

        String classValue = null;

        String facet = obj.getFacet();
        switch (facet) {
            case "Aim_Citation":
            case "Aim_Facet":
            case "AIM":
                classValue = "AIM";
                break;
            case "Hypothesis_Citation":
            case "Hypothesis_Facet":
            case "HYPOTHESIS":
                classValue = "HYPOTHESIS";
                break;
            case "Method_Citation":
            case "Method_Facet":
            case "METHOD":
                classValue = "METHOD";
                break;
            case "Results_Citation":
            case "Results_Facet":
            case "RESULT":
                classValue = "RESULT";
                break;
            case "Implication_Citation":
            case "Implication_Facet":
            case "IMPLICATION":
                classValue = "IMPLICATION";
                break;
        }


        trainingDataset.instance(0).setClassValue(classValue);
        return trainingDataset;
    }

    public static Instances generateFacetTestInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features, FeaturesMode featuresMode) {
        Instances testingDataset = new Instances("FacetTest", features, 0);
        testingDataset.setClassIndex(testingDataset.numAttributes() - 1);

        Instance testInstance = null;
        if (featuresMode.equals(FeaturesMode.MERGED)) {
            testInstance = new DenseInstance(208);
        } else if (featuresMode.equals(FeaturesMode.ALL)) {
            testInstance = new DenseInstance(212);
        }

        testingDataset.add(testInstance);
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeJiangconrathSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LCH_SIMILARITY"), Utilities.computeLCHSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LESK_SIMILARITY"), Utilities.computeLeskSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("LIN_SIMILARITY"), Utilities.computeLinSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PATH_SIMILARITY"), Utilities.computePathSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeResnikSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("WUP_SIMILARITY"), Utilities.computeWUPSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeCPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeRPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CITMARKER_COUNT"), Utilities.computeCitMarkerCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeCPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeRPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeCorefChainsCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));

        if (featuresMode.equals(FeaturesMode.MERGED)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeSentenceBigramLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCEBIGRAMPOSS_STRING"), Utilities.computeSentenceBigramPOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCEPOSS_STRING"), Utilities.computeSentencePOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCELEMMAS_STRING"), Utilities.computeSentenceLemmasString(obj, docs));

        } else if (featuresMode.equals(FeaturesMode.ALL)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));
        }
        testingDataset.instance(0).setClassMissing();

        return testingDataset;
    }

    public static Instances generateNormalizedFacetTestInstance(TrainingExample obj, DocumentCtx
            docs, ArrayList<Attribute> features, FeaturesMode featuresMode) {
        Instances testingDataset = new Instances("FacetTest", features, 0);
        testingDataset.setClassIndex(testingDataset.numAttributes() - 1);

        Instance testInstance = null;
        if (featuresMode.equals(FeaturesMode.MERGED)) {
            testInstance = new DenseInstance(208);
        } else if (featuresMode.equals(FeaturesMode.ALL)) {
            testInstance = new DenseInstance(212);
        } else if (featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testInstance = new DenseInstance(85);
        }

        testingDataset.add(testInstance);
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_POSITION"), Utilities.computeSentenceID(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCE_SECTION_POSITION"), Utilities.computeSentenceSectionID(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_AIM"), Utilities.computeFacetAim(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_HYPOTHESIS"), Utilities.computeFacetHypothesis(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_IMPLICATION"), Utilities.computeFacetImplication(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_METHOD"), Utilities.computeFacetMethod(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("FACET_RESULT"), Utilities.computeFacetResult(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("JIANGCONRATH_SIMILARITY"), Utilities.computeNormalizedJiangconrathSimilarity(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("LCH_SIMILARITY"), Utilities.computeNormalizedLCHSimilarity(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("LESK_SIMILARITY"), Utilities.computeNormalizedLeskSimilarity(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("LIN_SIMILARITY"), Utilities.computeNormalizedLinSimilarity(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("PATH_SIMILARITY"), Utilities.computeNormalizedPathSimilarity(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("RESNIK_SIMILARITY"), Utilities.computeNormalizedResnikSimilarity(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("WUP_SIMILARITY"), Utilities.computeNormalizedWUPSimilarity(obj, docs));
        }

        testingDataset.instance(0).setValue(testingDataset.attribute("COSINE_SIMILARITY"), Utilities.computeCosineSimilarity(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("BABELNET_COSINE_SIMILARITY"), Utilities.computeBabelnetCosineSimilarity(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_APPROACH"), Utilities.computeProbabilityApproach(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_BACKGROUND"), Utilities.computeProbabilityBackground(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_CHALLENGE"), Utilities.computeProbabilityChallenge(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_FUTUREWORK"), Utilities.computeProbabilityFutureWork(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("PROBABILITY_OUTCOME"), Utilities.computeProbabilityOutcome(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CITMARKER_COUNT"), Utilities.computeNormalizedCPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CITMARKER_COUNT"), Utilities.computeNormalizedRPCitMarkerCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("CITMARKER_COUNT"), Utilities.computeNormalizedCitMarkerCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_CAUSEAFFECT_EXISTANCE"), Utilities.computeCPCauseAffectExistance(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_CAUSEAFFECT_EXISTANCE"), Utilities.computeRPCauseAffectExistance(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("CP_COREFCHAINS_COUNT"), Utilities.computeNormalizedCPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("RP_COREFCHAINS_COUNT"), Utilities.computeNormalizedRPCorefChainsCount(obj, docs));
        testingDataset.instance(0).setValue(testingDataset.attribute("COREFCHAINS_COUNT"), Utilities.computeNormalizedCorefChainsCount(obj, docs));

        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHMT_PROP"), Utilities.computeGazResearchMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHMT_PROP"), Utilities.computeRPGazResearchMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHMT_PROP"), Utilities.computeCPGazResearchMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONMT_PROP"), Utilities.computeGazArgumentationMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONMT_PROP"), Utilities.computeRPGazArgumentationMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONMT_PROP"), Utilities.computeCPGazArgumentationMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREMT_PROP"), Utilities.computeGazAwareMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREMT_PROP"), Utilities.computeRPGazAwareMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREMT_PROP"), Utilities.computeCPGazAwareMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZUSEMT_PROP"), Utilities.computeGazUseMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZUSEMT_PROP"), Utilities.computeRPGazUseMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZUSEMT_PROP"), Utilities.computeCPGazUseMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMMT_PROP"), Utilities.computeGazProblemMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMMT_PROP"), Utilities.computeRPGazProblemMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMMT_PROP"), Utilities.computeCPGazProblemMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONMT_PROP"), Utilities.computeGazSolutionMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONMT_PROP"), Utilities.computeRPGazSolutionMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONMT_PROP"), Utilities.computeCPGazSolutionMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBETTERSOLUTIONMT_PROP"), Utilities.computeGazBetterSolutionMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeRPGazBetterSolutionMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBETTERSOLUTIONMT_PROP"), Utilities.computeCPGazBetterSolutionMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTSTRUCTUREMT_PROP"), Utilities.computeGazTextstructureMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeRPGazTextstructureMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTSTRUCTUREMT_PROP"), Utilities.computeCPGazTextstructureMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTRESTMT_PROP"), Utilities.computeGazInterestMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTRESTMT_PROP"), Utilities.computeRPGazInterestMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTRESTMT_PROP"), Utilities.computeCPGazInterestMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTINUEMT_PROP"), Utilities.computeGazContinueMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTINUEMT_PROP"), Utilities.computeRPGazContinueMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTINUEMT_PROP"), Utilities.computeCPGazContinueMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREINTERESTMT_PROP"), Utilities.computeGazFutureInterestMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREINTERESTMT_PROP"), Utilities.computeRPGazFutureInterestMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREINTERESTMT_PROP"), Utilities.computeCPGazFutureInterestMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDMT_PROP"), Utilities.computeGazNeedMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDMT_PROP"), Utilities.computeRPGazNeedMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDMT_PROP"), Utilities.computeCPGazNeedMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAFFECTMT_PROP"), Utilities.computeGazAffectMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAFFECTMT_PROP"), Utilities.computeRPGazAffectMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAFFECTMT_PROP"), Utilities.computeCPGazAffectMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONMT_PROP"), Utilities.computeGazPresentationMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONMT_PROP"), Utilities.computeRPGazPresentationMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONMT_PROP"), Utilities.computeCPGazPresentationMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTMT_PROP"), Utilities.computeGazContrastMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTMT_PROP"), Utilities.computeRPGazContrastMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTMT_PROP"), Utilities.computeCPGazContrastMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEMT_PROP"), Utilities.computeGazChangeMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEMT_PROP"), Utilities.computeRPGazChangeMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEMT_PROP"), Utilities.computeCPGazChangeMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONMT_PROP"), Utilities.computeGazComparisonMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONMT_PROP"), Utilities.computeRPGazComparisonMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONMT_PROP"), Utilities.computeCPGazComparisonMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARMT_PROP"), Utilities.computeGazSimilarMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARMT_PROP"), Utilities.computeRPGazSimilarMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARMT_PROP"), Utilities.computeCPGazSimilarMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONADJMT_PROP"), Utilities.computeGazComparisonAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONADJMT_PROP"), Utilities.computeRPGazComparisonAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONADJMT_PROP"), Utilities.computeCPGazComparisonAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFUTUREADJMT_PROP"), Utilities.computeGazFutureAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFUTUREADJMT_PROP"), Utilities.computeRPGazFutureAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFUTUREADJMT_PROP"), Utilities.computeCPGazFutureAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZINTERESTNOUNMT_PROP"), Utilities.computeGazInterestNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZINTERESTNOUNMT_PROP"), Utilities.computeRPGazInterestNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZINTERESTNOUNMT_PROP"), Utilities.computeCPGazInterestNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONNOUNMT_PROP"), Utilities.computeGazQuestionNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONNOUNMT_PROP"), Utilities.computeRPGazQuestionNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONNOUNMT_PROP"), Utilities.computeCPGazQuestionNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAWAREADJMT_PROP"), Utilities.computeGazAwareAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAWAREADJMT_PROP"), Utilities.computeRPGazAwareAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAWAREADJMT_PROP"), Utilities.computeCPGazAwareAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeGazArgumentationNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeRPGazArgumentationNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZARGUMENTATIONNOUNMT_PROP"), Utilities.computeCPGazArgumentationNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSIMILARNOUNMT_PROP"), Utilities.computeGazSimilarNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSIMILARNOUNMT_PROP"), Utilities.computeRPGazSimilarNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSIMILARNOUNMT_PROP"), Utilities.computeCPGazSimilarNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZEARLIERADJMT_PROP"), Utilities.computeGazEarlierAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZEARLIERADJMT_PROP"), Utilities.computeRPGazEarlierAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZEARLIERADJMT_PROP"), Utilities.computeCPGazEarlierAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHADJMT_PROP"), Utilities.computeGazResearchAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHADJMT_PROP"), Utilities.computeRPGazResearchAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHADJMT_PROP"), Utilities.computeCPGazResearchAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDADJMT_PROP"), Utilities.computeGazNeedAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDADJMT_PROP"), Utilities.computeRPGazNeedAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDADJMT_PROP"), Utilities.computeCPGazNeedAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFERENTIALMT_PROP"), Utilities.computeGazReferentialMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFERENTIALMT_PROP"), Utilities.computeRPGazReferentialMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFERENTIALMT_PROP"), Utilities.computeCPGazReferentialMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZQUESTIONMT_PROP"), Utilities.computeGazQuestionMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZQUESTIONMT_PROP"), Utilities.computeRPGazQuestionMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZQUESTIONMT_PROP"), Utilities.computeCPGazQuestionMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZWORKNOUNMT_PROP"), Utilities.computeGazWorkNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZWORKNOUNMT_PROP"), Utilities.computeRPGazWorkNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZWORKNOUNMT_PROP"), Utilities.computeCPGazWorkNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGEADJMT_PROP"), Utilities.computeGazChangeAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGEADJMT_PROP"), Utilities.computeRPGazChangeAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGEADJMT_PROP"), Utilities.computeCPGazChangeAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZDISCIPLINEMT_PROP"), Utilities.computeGazDisciplineMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZDISCIPLINEMT_PROP"), Utilities.computeRPGazDisciplineMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZDISCIPLINEMT_PROP"), Utilities.computeCPGazDisciplineMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGIVENMT_PROP"), Utilities.computeGazGivenMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGIVENMT_PROP"), Utilities.computeRPGazGivenMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGIVENMT_PROP"), Utilities.computeCPGazGivenMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZBADADJMT_PROP"), Utilities.computeGazBadAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZBADADJMT_PROP"), Utilities.computeRPGazBadAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZBADADJMT_PROP"), Utilities.computeCPGazBadAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTNOUNMT_PROP"), Utilities.computeGazContrastNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTNOUNMT_PROP"), Utilities.computeRPGazContrastNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTNOUNMT_PROP"), Utilities.computeCPGazContrastNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEEDNOUNMT_PROP"), Utilities.computeGazNeedNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEEDNOUNMT_PROP"), Utilities.computeRPGazNeedNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEEDNOUNMT_PROP"), Utilities.computeCPGazNeedNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZAIMNOUNMT_PROP"), Utilities.computeGazAimNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZAIMNOUNMT_PROP"), Utilities.computeRPGazAimNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZAIMNOUNMT_PROP"), Utilities.computeCPGazAimNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCONTRASTADJMT_PROP"), Utilities.computeGazContrastAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCONTRASTADJMT_PROP"), Utilities.computeRPGazContrastAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCONTRASTADJMT_PROP"), Utilities.computeCPGazContrastAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZSOLUTIONNOUNMT_PROP"), Utilities.computeGazSolutionNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeRPGazSolutionNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZSOLUTIONNOUNMT_PROP"), Utilities.computeCPGazSolutionNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONNOUNMT_PROP"), Utilities.computeGazTraditionNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONNOUNMT_PROP"), Utilities.computeRPGazTraditionNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONNOUNMT_PROP"), Utilities.computeCPGazTraditionNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZFIRSTPRONMT_PROP"), Utilities.computeGazFirstPronMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZFIRSTPRONMT_PROP"), Utilities.computeRPGazFirstPronMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZFIRSTPRONMT_PROP"), Utilities.computeCPGazFirstPronMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROFESSIONALSMT_PROP"), Utilities.computeGazProfessionalsMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROFESSIONALSMT_PROP"), Utilities.computeRPGazProfessionalsMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROFESSIONALSMT_PROP"), Utilities.computeCPGazProfessionalsMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMNOUNMT_PROP"), Utilities.computeGazProblemNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMNOUNMT_PROP"), Utilities.computeRPGazProblemNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMNOUNMT_PROP"), Utilities.computeCPGazProblemNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEGATIONMT_PROP"), Utilities.computeGazNegationMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEGATIONMT_PROP"), Utilities.computeRPGazNegationMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEGATIONMT_PROP"), Utilities.computeCPGazNegationMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTEXTNOUNMT_PROP"), Utilities.computeGazTextNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTEXTNOUNMT_PROP"), Utilities.computeRPGazTextNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTEXTNOUNMT_PROP"), Utilities.computeCPGazTextNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPROBLEMADJMT_PROP"), Utilities.computeGazProblemAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPROBLEMADJMT_PROP"), Utilities.computeRPGazProblemAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPROBLEMADJMT_PROP"), Utilities.computeCPGazProblemAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTHIRDPRONMT_PROP"), Utilities.computeGazThirdPronMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTHIRDPRONMT_PROP"), Utilities.computeRPGazThirdPronMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTHIRDPRONMT_PROP"), Utilities.computeCPGazThirdPronMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZTRADITIONADJMT_PROP"), Utilities.computeGazTraditionAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZTRADITIONADJMT_PROP"), Utilities.computeRPGazTraditionAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZTRADITIONADJMT_PROP"), Utilities.computeCPGazTraditionAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZPRESENTATIONNOUNMT_PROP"), Utilities.computeGazPresentationNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeRPGazPresentationNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZPRESENTATIONNOUNMT_PROP"), Utilities.computeCPGazPresentationNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZRESEARCHNOUNMT_PROP"), Utilities.computeGazResearchNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZRESEARCHNOUNMT_PROP"), Utilities.computeRPGazResearchNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZRESEARCHNOUNMT_PROP"), Utilities.computeCPGazResearchNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMAINADJMT_PROP"), Utilities.computeGazMainAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMAINADJMT_PROP"), Utilities.computeRPGazMainAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMAINADJMT_PROP"), Utilities.computeCPGazMainAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZREFLEXSIVEMT_PROP"), Utilities.computeGazReflexiveMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZREFLEXSIVEMT_PROP"), Utilities.computeRPGazReflexiveMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZREFLEXSIVEMT_PROP"), Utilities.computeCPGazReflexiveMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZNEDADJMT_PROP"), Utilities.computeGazNedAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZNEDADJMT_PROP"), Utilities.computeRPGazNedAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZNEDADJMT_PROP"), Utilities.computeCPGazNedAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZMANYMT_PROP"), Utilities.computeGazManyMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZMANYMT_PROP"), Utilities.computeRPGazManyMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZMANYMT_PROP"), Utilities.computeCPGazManyMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCOMPARISONNOUNMT_PROP"), Utilities.computeGazComparisonNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeRPGazComparisonNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCOMPARISONNOUNMT_PROP"), Utilities.computeCPGazComparisonNounMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZGOODADJMT_PROP"), Utilities.computeGazGoodAdjMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZGOODADJMT_PROP"), Utilities.computeRPGazGoodAdjMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZGOODADJMT_PROP"), Utilities.computeCPGazGoodAdjMTProp(obj, docs));
        }
        testingDataset.instance(0).setValue(testingDataset.attribute("GAZCHANGENOUNMT_PROP"), Utilities.computeGazChangeNounMTProp(obj, docs));
        if (!featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RPGAZCHANGENOUNMT_PROP"), Utilities.computeRPGazChangeNounMTProp(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CPGAZCHANGENOUNMT_PROP"), Utilities.computeCPGazChangeNounMTProp(obj, docs));
        }
        if (featuresMode.equals(FeaturesMode.MERGED) || featuresMode.equals(FeaturesMode.MERGEDBASIC)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeSentenceBigramLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCEBIGRAMPOSS_STRING"), Utilities.computeSentenceBigramPOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCEPOSS_STRING"), Utilities.computeSentencePOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("SENTENCELEMMAS_STRING"), Utilities.computeSentenceLemmasString(obj, docs));

        } else if (featuresMode.equals(FeaturesMode.ALL)) {
            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeRPSentenceBigramLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMLEMMAS_STRING"), Utilities.computeCPSentenceBigramLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeRPSentenceBigramPOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEBIGRAMPOSS_STRING"), Utilities.computeCPSentenceBigramPOSsString(obj, docs));

            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCEPOSS_STRING"), Utilities.computeRPSentencePOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCEPOSS_STRING"), Utilities.computeCPSentencePOSsString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("RP_SENTENCELEMMAS_STRING"), Utilities.computeRPSentenceLemmasString(obj, docs));
            testingDataset.instance(0).setValue(testingDataset.attribute("CP_SENTENCELEMMAS_STRING"), Utilities.computeCPSentenceLemmasString(obj, docs));
        }
        testingDataset.instance(0).setClassMissing();

        return testingDataset;
    }

    public static Instances classifyInstances(Instances testData, InputMappedClassifier
            inputMappedClassifier) {
        try {
            //System.out.println("Total number of testing instances : " + testData.size());
            for (int i = 0; i < testData.numInstances(); i++) {
                double pred = inputMappedClassifier.classifyInstance(testData.instance(i));
                testData.instance(i).setClassValue(pred);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        testData.setClassIndex(testData.numAttributes() - 1);
        return testData;
    }

    public static Document annotateRPWithReference(TrainingExample obj, DocumentCtx docs, String
            discourse_Facet) {
        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        FeatureMap fm = Factory.newFeatureMap();

        AnnotationSet rpReferences = rp.getAnnotations("REFERENCES");
        AnnotationSet cpCitations = cp.getAnnotations("CITATIONS");

        String citance_Number = (String) cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Citance_Number");
        String reference_Article = rp.getName();
        String citing_Article = cp.getName();
        String citation_Marker = (String) cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Citation_Marker");
        String reference_Offset = obj.getReferenceSentence().getFeatures().get("sid").toString();
        String reference_Text = null;
        try {
            reference_Text = rp.getContent().getContent(obj.getReferenceSentence().getStartNode().getOffset(), obj.getReferenceSentence().getEndNode().getOffset()).toString();
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }
        String Discourse_Facet = discourse_Facet;
        String annotator = (String) cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Annotator");

        fm.put("id", (String) cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("id"));
        fm.put("Citance_Number", citance_Number);
        fm.put("Reference_Article", reference_Article);
        fm.put("Citing_Article", citing_Article);
        fm.put("Citation_Marker", citation_Marker);
        fm.put("Reference_Offset", reference_Offset);
        //fm.put("reference_Text", reference_Text);
        fm.put("Discourse_Facet", discourse_Facet);
        fm.put("Annotator", annotator);

        try {
            rpReferences.add(obj.getReferenceSentence().getStartNode().getOffset(),
                    obj.getReferenceSentence().getEndNode().getOffset(),
                    cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Annotator").toString(),
                    fm);
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }

        return rp;
    }

    public static Document annotateRPWithMatchFeatures(Instances instances, TrainingExample
            obj, DocumentCtx docs) {
        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        FeatureMap fm = Factory.newFeatureMap();
        for (int i = 0; i < instances.instance(0).numAttributes(); i++) {
            if (instances.instance(0).attribute(i).isNumeric()) {
                fm.put(instances.instance(0).attribute(i).name(), instances.instance(0).value(instances.instance(0).attribute(i)));
            } else {
                fm.put(instances.instance(0).attribute(i).name(), instances.instance(0).stringValue(instances.instance(0).attribute(i)));
            }
        }

        AnnotationSet cpCitations = cp.getAnnotations("CITATIONS");

        AnnotationSet rpFeatures = rp.getAnnotations("Match_Features");
        try {
            rpFeatures.add(obj.getReferenceSentence().getStartNode().getOffset(), obj.getReferenceSentence().getEndNode().getOffset(),
                    cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("id").toString(),
                    fm);
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }

        return rp;
    }

    public static Document annotateRPWithNOMatchFeatures(Instances instances, TrainingExample
            obj, DocumentCtx docs) {
        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        FeatureMap fm = Factory.newFeatureMap();
        for (int i = 0; i < instances.instance(0).numAttributes(); i++) {
            if (instances.instance(0).attribute(i).isNumeric()) {
                fm.put(instances.instance(0).attribute(i).name(), instances.instance(0).value(instances.instance(0).attribute(i)));
            } else {
                fm.put(instances.instance(0).attribute(i).name(), instances.instance(0).stringValue(instances.instance(0).attribute(i)));
            }
        }

        AnnotationSet cpCitations = cp.getAnnotations("CITATIONS");

        AnnotationSet rpFeatures = rp.getAnnotations("NO_Match_Features");
        try {
            rpFeatures.add(obj.getReferenceSentence().getStartNode().getOffset(), obj.getReferenceSentence().getEndNode().getOffset(),
                    cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("id").toString(),
                    fm);
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }


        return rp;
    }

    public static Document annotateRPWithFacetFeatures(Instances instances, TrainingExample
            obj, DocumentCtx docs) {
        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        FeatureMap fm = Factory.newFeatureMap();
        for (int i = 0; i < instances.instance(0).numAttributes(); i++) {
            if (instances.instance(0).attribute(i).isNumeric()) {
                fm.put(instances.instance(0).attribute(i).name(), instances.instance(0).value(instances.instance(0).attribute(i)));
            } else {
                fm.put(instances.instance(0).attribute(i).name(), instances.instance(0).stringValue(instances.instance(0).attribute(i)));
            }
        }

        AnnotationSet cpCitations = cp.getAnnotations("CITATIONS");

        AnnotationSet rpFeatures = rp.getAnnotations("Facet_Features");
        try {
            rpFeatures.add(obj.getReferenceSentence().getStartNode().getOffset(), obj.getReferenceSentence().getEndNode().getOffset(),
                    cpCitations.get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("id").toString(),
                    fm);
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }

        return rp;
    }

    public static void writeSciSummFinallOutput
            (ArrayList<AnnotationV3Full> finalAnnotationVsList, String filePath) {
        for (AnnotationV3Full annotationV3Full : finalAnnotationVsList) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Citance Number: ");
            stringBuilder.append(annotationV3Full.getCitance_Number());
            stringBuilder.append(" | Reference Article: ");
            stringBuilder.append(annotationV3Full.getReference_Article());
            stringBuilder.append(".xml | Citing Article: ");
            stringBuilder.append(annotationV3Full.getCiting_Article());
            stringBuilder.append(".xml | Citation Marker Offset:  ['");
            stringBuilder.append(annotationV3Full.getCitation_Marker_Offset());
            stringBuilder.append("'] | Citation Marker: ");
            stringBuilder.append(annotationV3Full.getCitation_Marker());
            stringBuilder.append(" | Citation Offset:  ['");
            int citationCount = 0;
            for (String citationOffset : annotationV3Full.getCitation_Offset()) {
                stringBuilder.append(citationOffset);
                citationCount++;
                if (citationCount != annotationV3Full.getCitation_Offset().size()) {
                    stringBuilder.append("','");
                }
            }
            stringBuilder.append("'] | Citation Text: ");
            stringBuilder.append(annotationV3Full.getCitation_Text());
            stringBuilder.append(" | Reference Offset:  ['");
            int referenceCount = 0;
            for (String referenceOffset : annotationV3Full.getReference_Offset()) {
                stringBuilder.append(referenceOffset);
                referenceCount++;
                if (referenceCount != annotationV3Full.getReference_Offset().size()) {
                    stringBuilder.append("','");
                }
            }
            stringBuilder.append("'] | Reference Text: ");
            stringBuilder.append(annotationV3Full.getReference_Text());
            stringBuilder.append(" | Discourse Facet: ");
            if (annotationV3Full.getDiscourse_Facet().size() > 1) {
                stringBuilder.append(" ['");
            }
            int facetCount = 0;
            for (String facet : annotationV3Full.getDiscourse_Facet()) {
                stringBuilder.append(facet);
                facetCount++;
                if (facetCount != annotationV3Full.getDiscourse_Facet().size()) {
                    stringBuilder.append("','");
                }
            }
            if (annotationV3Full.getDiscourse_Facet().size() > 1) {
                stringBuilder.append("'] ");
            }
            stringBuilder.append(" | Annotator: ");
            stringBuilder.append(annotationV3Full.getAnnotator());
            stringBuilder.append(" |");
            stringBuilder.append(System.getProperty("line.separator"));

            File file = new File(filePath);
            if (file.exists()) {
                try (FileWriter fw = new FileWriter(file, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println(stringBuilder.toString());
                    //more code

                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
            } else {
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(file));

                    writer.write(stringBuilder.toString());
                    writer.newLine();
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeSciSummOutput(OutputAnnotation outputAnnotation, String filePath) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Citance Number: ");
        stringBuilder.append(finalCounter);
        stringBuilder.append(" | Reference Article: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getReference_Article());
        stringBuilder.append(" | Citing Article: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getCiting_Article());
        stringBuilder.append(" | Citation Marker Offset:  ['");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getCitation_Marker_Offset());
        stringBuilder.append("'] | Citation Marker: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getCitation_Marker());
        stringBuilder.append(" | Citation Offset:  ['");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getCitation_Offset().get(0));
        stringBuilder.append("'] | Citation Text: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getCitation_Text());
        stringBuilder.append(" | Reference Offset:  ['");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getReference_Offset().get(0));
        stringBuilder.append("'] | Reference Text: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getReference_Text());
        stringBuilder.append(" | Discourse Facet: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getDiscourse_Facet());
        stringBuilder.append(" | Annotator: ");
        stringBuilder.append(outputAnnotation.getAnnotationV3().getAnnotator());
        stringBuilder.append(" |");
        stringBuilder.append(System.getProperty("line.separator"));

        File file = new File(filePath);
        if (file.exists()) {
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(stringBuilder.toString());
                //more code

            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        } else {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));

                writer.write(stringBuilder.toString());
                writer.newLine();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finalCounter++;
    }

    public static AnnotationV3 writeSciSummOutput(TrainingExample obj, DocumentCtx docs, String
            discourse_Facet) {
        StringBuilder stringBuilder = new StringBuilder();

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        stringBuilder.append("Citance Number: ");
        stringBuilder.append(counter);
        stringBuilder.append(" | Reference Article: ");
        stringBuilder.append(rp.getName().substring(0, rp.getName().indexOf(".")));
        stringBuilder.append(" | Citing Article: ");
        stringBuilder.append(cp.getName().substring(0, cp.getName().indexOf(".")));
        stringBuilder.append(" | Citation Marker Offset:  ['");
        stringBuilder.append(cp.getAnnotations("CITATIONS").get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Citation_Marker_Offset"));
        stringBuilder.append("'] | Citation Marker: ");
        stringBuilder.append(cp.getAnnotations("CITATIONS").get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Citation_Marker"));
        stringBuilder.append(" | Citation Offset:  ['");
        stringBuilder.append(obj.getCitanceSentence().getFeatures().get("sid"));
        stringBuilder.append("'] | Citation Text: ");
        try {
            stringBuilder.append(cp.getContent().getContent(obj.getCitanceSentence().getStartNode().getOffset(), obj.getCitanceSentence().getEndNode().getOffset()).toString().replaceAll("\\|", ""));
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }
        stringBuilder.append(" | Reference Offset:  ['");
        stringBuilder.append(obj.getReferenceSentence().getFeatures().get("sid"));
        stringBuilder.append("'] | Reference Text: ");
        try {
            stringBuilder.append(rp.getContent().getContent(obj.getReferenceSentence().getStartNode().getOffset(), obj.getReferenceSentence().getEndNode().getOffset()).toString().replaceAll("\\|", ""));
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }
        stringBuilder.append(" | Discourse Facet: ");
        stringBuilder.append(discourse_Facet);
        stringBuilder.append(" | Annotator: ");
        stringBuilder.append(cp.getAnnotations("CITATIONS").get(obj.getCitanceSentence().getStartNode().getOffset()).iterator().next().getFeatures().get("Annotator"));
        stringBuilder.append(" |");
        stringBuilder.append(System.getProperty("line.separator"));

        counter++;

        return new AnnotationV3(stringBuilder.toString(), true);
    }

    public static HashMap<String, Document> extractDocumentsFromBaseFolder(File referenceBaseFolder) {
        Document doc = null;
        HashMap<String, Document> documents = new HashMap<String, Document>();

        try {
            for (File document : referenceBaseFolder.listFiles()) {
                doc = Factory.newDocument(new URL("file:///" + document.getPath()), "UTF-8");
                documents.put(document.getName().substring(0, document.getName().indexOf(".")), doc);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ResourceInstantiationException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public static ArrayList<AnnotationV3> extractAnnotationsV3FromBaseFolder(File referenceBaseFolder,
                                                                             boolean generateTraining) {
        ArrayList<AnnotationV3> annotationV3List = new ArrayList<AnnotationV3>();

        String annotationsFilePath = referenceBaseFolder + "/annotation/" +
                referenceBaseFolder.getName()/*.substring(0, referenceBaseFolder.getName().indexOf("_"))*/ + ".annv3.txt";
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(annotationsFilePath), "UTF-8"));

            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    AnnotationV3 annotationV3 = new AnnotationV3(line, generateTraining);
                    annotationV3List.add(annotationV3);
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
        return annotationV3List;
    }

    public static ArrayList<AnnotationV3> extractOutputAnnotationsV3FromBaseFolder(File
                                                                                           referenceBaseFolder, Integer maxMatches, String matchProbability, boolean generateTraining) {
        ArrayList<AnnotationV3> annotationV3List = new ArrayList<AnnotationV3>();

        String annotationsFilePath = referenceBaseFolder + "/Output/annov3_M" + maxMatches + "_P" + matchProbability + ".txt";
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(annotationsFilePath), "UTF-8"));

            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    AnnotationV3 annotationV3 = new AnnotationV3(line, generateTraining);
                    annotationV3List.add(annotationV3);
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
        return annotationV3List;
    }

    public static ArrayList<AnnotationV3> extractBaseLineAnnotationsV3FromBaseFolder(File
                                                                                             referenceBaseFolder, boolean generateTraining) {
        ArrayList<AnnotationV3> annotationV3List = new ArrayList<AnnotationV3>();

        String annotationsFilePath = referenceBaseFolder + "/Output/baselineAnnov3.txt";
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(annotationsFilePath), "UTF-8"));

            while ((line = reader.readLine()) != null) {
                if (!line.equals("")) {
                    AnnotationV3 annotationV3 = new AnnotationV3(line, generateTraining);
                    annotationV3List.add(annotationV3);
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
        return annotationV3List;
    }

    public static HashMap<String, Document> applyAnnotations
            (HashMap<String, Document> RCDocuments, ArrayList<AnnotationV3> annotationV3List,
             boolean generateTraining) {
        HashMap<String, Document> processedDocuments = new HashMap<String, Document>();
        ArrayList<String> reference_Offset = new ArrayList<>();
        String reference_Text = null;
        String discourse_Facet = null;
        Document rp = null;
        Document cp = null;

        for (AnnotationV3 annotationV3 : annotationV3List) {
            String citance_Number = annotationV3.getCitance_Number();
            String reference_Article = annotationV3.getReference_Article();
            String citing_Article = annotationV3.getCiting_Article();
            String citation_Marker_Offset = annotationV3.getCitation_Marker_Offset();
            String citation_Marker = annotationV3.getCitation_Marker();
            ArrayList<String> citation_Offset = annotationV3.getCitation_Offset();
            String citation_Text = annotationV3.getCitation_Text();
            if (generateTraining) {
                reference_Offset = annotationV3.getReference_Offset();
                reference_Text = annotationV3.getReference_Text();
                discourse_Facet = annotationV3.getDiscourse_Facet();
            }
            String annotator = annotationV3.getAnnotator();

            rp = RCDocuments.get(reference_Article);
            cp = RCDocuments.get(citing_Article);

            AnnotationSet rpMarkups = rp.getAnnotations("Original markups");
            AnnotationSet cpMarkups = cp.getAnnotations("Original markups");

            String rpContent = rp.getContent().toString();
            String cpContent = cp.getContent().toString();

            AnnotationSet references = rp.getAnnotations("REFERENCES");
            AnnotationSet citations = cp.getAnnotations("CITATIONS");

            AnnotationSet rp_sentences = rpMarkups.get("S");
            AnnotationSet cp_sentences = cpMarkups.get("S");

            FeatureMap cfilter = Factory.newFeatureMap();
            FeatureMap rfilter = Factory.newFeatureMap();

            for (String co : citation_Offset) {
                cfilter.put("sid", co);

                AnnotationSet cselected = cp_sentences.get("S", cfilter);
                Annotation csentence;
                Long cstartS, cendS;
                FeatureMap cfm_anns;
                if (cselected.size() > 0) {
                    csentence = cselected.iterator().next();
                    cstartS = csentence.getStartNode().getOffset();
                    cendS = csentence.getEndNode().getOffset();
                    cfm_anns = Factory.newFeatureMap();
                    cfm_anns.put("id", citance_Number + "_" + reference_Article + "_" + citing_Article + "_" + annotator);
                    cfm_anns.put("Citance_Number", citance_Number);
                    cfm_anns.put("Reference_Article", reference_Article);
                    cfm_anns.put("Citing_Article", citing_Article);
                    cfm_anns.put("Citation_Marker_Offset", citation_Marker_Offset);
                    cfm_anns.put("Citation_Marker", citation_Marker);
                    cfm_anns.put("Citation_Offset", co);
                    //cfm_anns.put("Citation_Text", citation_Text);
                    if (generateTraining) {
                        cfm_anns.put("Discourse_Facet", discourse_Facet);
                    }
                    cfm_anns.put("Annotator", annotator);
                    try {
                        citations.add(cstartS, cendS, annotator, cfm_anns);
                    } catch (InvalidOffsetException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (generateTraining) {
                //Annotate The reference
                for (String ro : reference_Offset) {
                    rfilter.put("sid", ro);

                    AnnotationSet rselected = rp_sentences.get("S", rfilter);
                    Annotation rsentence;
                    Long rstartS, rendS;
                    FeatureMap rfm_anns;
                    if (rselected.size() > 0) {
                        rsentence = rselected.iterator().next();
                        rstartS = rsentence.getStartNode().getOffset();
                        rendS = rsentence.getEndNode().getOffset();

                        rfm_anns = Factory.newFeatureMap();
                        rfm_anns.put("id", citance_Number + "_" + reference_Article + "_" + citing_Article + "_" + annotator);
                        rfm_anns.put("Citance_Number", citance_Number);
                        rfm_anns.put("Reference_Article", reference_Article);
                        rfm_anns.put("Citing_Article", citing_Article);
                        rfm_anns.put("Citation_Marker", citation_Marker);
                        rfm_anns.put("Reference_Offset", ro);
                        //rfm_anns.put("reference_Text", reference_Text);
                        rfm_anns.put("Discourse_Facet", discourse_Facet);
                        rfm_anns.put("Annotator", annotator);
                        try {
                            references.add(rstartS, rendS, annotator, rfm_anns);
                        } catch (InvalidOffsetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            RCDocuments.put(reference_Article, rp);
            RCDocuments.put(citing_Article, cp);

        }

        processedDocuments = RCDocuments;

        return processedDocuments;
    }

    public static HashMap<String, Document> applyGSAnnotations
            (HashMap<String, Document> RCDocuments, ArrayList<AnnotationV3> annotationV3List) {
        HashMap<String, Document> processedDocuments = new HashMap<String, Document>();
        ArrayList<String> reference_Offset = new ArrayList<>();
        String reference_Text = null;
        String discourse_Facet = null;
        Document rp = null;

        for (AnnotationV3 annotationV3 : annotationV3List) {
            String citance_Number = annotationV3.getCitance_Number();
            String reference_Article = annotationV3.getReference_Article();
            String citing_Article = annotationV3.getCiting_Article();
            String citation_Marker_Offset = annotationV3.getCitation_Marker_Offset();
            String citation_Marker = annotationV3.getCitation_Marker();
            ArrayList<String> citation_Offset = annotationV3.getCitation_Offset();
            String citation_Text = annotationV3.getCitation_Text();

            reference_Offset = annotationV3.getReference_Offset();
            reference_Text = annotationV3.getReference_Text();
            discourse_Facet = annotationV3.getDiscourse_Facet();

            String annotator = annotationV3.getAnnotator();

            rp = RCDocuments.get(reference_Article);

            AnnotationSet rpMarkups = rp.getAnnotations("Original markups");

            String rpContent = rp.getContent().toString();

            AnnotationSet references = rp.getAnnotations("GS_REFERENCES");

            AnnotationSet rp_sentences = rpMarkups.get("S");

            FeatureMap cfilter = Factory.newFeatureMap();
            FeatureMap rfilter = Factory.newFeatureMap();

            //Annotate The reference
            for (String ro : reference_Offset) {
                rfilter.put("sid", ro);

                AnnotationSet rselected = rp_sentences.get("S", rfilter);
                Annotation rsentence;
                Long rstartS, rendS;
                FeatureMap rfm_anns;
                if (rselected.size() > 0) {
                    rsentence = rselected.iterator().next();
                    rstartS = rsentence.getStartNode().getOffset();
                    rendS = rsentence.getEndNode().getOffset();

                    rfm_anns = Factory.newFeatureMap();
                    rfm_anns.put("id", citance_Number + "_" + reference_Article + "_" + citing_Article + "_" + annotator);
                    rfm_anns.put("Citance_Number", citance_Number);
                    rfm_anns.put("Reference_Article", reference_Article);
                    rfm_anns.put("Citing_Article", citing_Article);
                    rfm_anns.put("Citation_Marker", citation_Marker);
                    rfm_anns.put("Reference_Offset", ro);
                    //rfm_anns.put("reference_Text", reference_Text);
                    rfm_anns.put("Discourse_Facet", discourse_Facet);
                    rfm_anns.put("Annotator", annotator);
                    try {
                        references.add(rstartS, rendS, annotator, rfm_anns);
                    } catch (InvalidOffsetException e) {
                        e.printStackTrace();
                    }
                }

            }

            RCDocuments.put(reference_Article, rp);
        }

        processedDocuments = RCDocuments;

        return processedDocuments;
    }

    public static HashMap<String, Document> applyCoRefChainsConnectionsonVectors
            (HashMap<String, Document> RCDocuments) {
        HashMap<String, Document> processedRCDocuments = new HashMap<String, Document>();


        //Adding CorefChains to the CP Vector_Norms.
        for (String key : RCDocuments.keySet()) {
            Document doc = RCDocuments.get(key);
            AnnotationSet analysis = doc.getAnnotations("Analysis");
            AnnotationSet vectors = analysis.get("Vector");
            AnnotationSet coRefChains = doc.getAnnotations("CorefChains");
            ArrayList checkedCoRefChainsTypes = new ArrayList();

            for (Annotation coref : coRefChains) {
                String corefType = coref.getType();

                AnnotationSet coRefChainsTypes = coRefChains.get(corefType);
                FeatureMap NOMINALTypefilter = Factory.newFeatureMap();
                FeatureMap PRONOMINALTypefilter = Factory.newFeatureMap();

                for (Annotation coRefChainsType : coRefChainsTypes) {
                    if (!checkedCoRefChainsTypes.contains(corefType)) {
                        NOMINALTypefilter.put("type", "NOMINAL");
                        PRONOMINALTypefilter.put("type", "PRONOMINAL");
                        AnnotationSet SelectedNOMINALTypes = coRefChainsTypes.get(corefType, NOMINALTypefilter);
                        AnnotationSet SelectedPRONOMINALTypes = coRefChainsTypes.get(corefType, PRONOMINALTypefilter);
                        if (SelectedNOMINALTypes.size() > 0 && SelectedPRONOMINALTypes.size() > 0) {
                            for (Annotation SelectedNOMINALType : SelectedNOMINALTypes) {
                                AnnotationSet SelectedNOMINALTypeVectors = vectors.get(SelectedNOMINALType.getStartNode().getOffset(), SelectedNOMINALType.getEndNode().getOffset());
                                if (SelectedNOMINALTypeVectors.size() > 0) {
                                    Annotation SelectedNOMINALTypeVector = SelectedNOMINALTypeVectors.iterator().next();
                                    for (Annotation SelectedPRONOMINALType : SelectedPRONOMINALTypes) {
                                        AnnotationSet SelectedPRONOMINALTypeVectors = vectors.get(SelectedPRONOMINALType.getStartNode().getOffset(), SelectedPRONOMINALType.getEndNode().getOffset());
                                        if (SelectedPRONOMINALTypeVectors.size() > 0) {
                                            Annotation cpSelectedPRONOMINALTypeVector = SelectedPRONOMINALTypeVectors.iterator().next();
                                            if (SelectedNOMINALTypeVector.getStartNode().getOffset() != cpSelectedPRONOMINALTypeVector.getStartNode().getOffset()) {
                                                AnnotationSet cpNOMINALTypeTokens = analysis.get("Token").get(SelectedNOMINALType.getStartNode().getOffset(),
                                                        SelectedNOMINALType.getEndNode().getOffset());
                                                for (Annotation token : cpNOMINALTypeTokens) {
                                                    if (SelectedNOMINALTypeVector.getFeatures().containsKey(token.getFeatures().get("string"))) {
                                                        cpSelectedPRONOMINALTypeVector.getFeatures().put(token.getFeatures().get("string"), SelectedNOMINALTypeVector.getFeatures().get(token.getFeatures().get("string")));
                                                    }
                                                }
                                            }
                                        } else {
                                            System.out.println("Could not find the  Vector of PRONOMINALType");
                                        }
                                    }
                                } else {
                                    System.out.println("Could not find the  Vector of NOMINALType");
                                }
                            }
                        }
                        checkedCoRefChainsTypes.add(corefType);
                    }
                }
            }
            processedRCDocuments.put(key, doc);
        }

        return processedRCDocuments;
    }

    public static HashMap<String, Document> applyGappVectors(HashMap<String, Document> RCDocuments) {
        HashMap<String, Document> processedRCDocuments = new HashMap<String, Document>();

        try {
            // load the GAPP
            application = (CorpusController) PersistenceManager.loadObjectFromFile(new File(Main.workingDir + File.separator + "ACLSUMM_VECTORS.gapp"));

            Corpus corpus = null;
            Document doc;
            for (String key : RCDocuments.keySet()) {
                doc = RCDocuments.get(key);
                corpus = Factory.newCorpus("");
                corpus.add(doc);
                application.setCorpus(corpus);
                application.execute();

                processedRCDocuments.put(key, doc);
                Factory.deleteResource(corpus);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ResourceInstantiationException e) {
            e.printStackTrace();
        }
        return processedRCDocuments;
    }

    public static HashMap<String, Document> applyGappNormalizedVectors
            (HashMap<String, Document> RCDocuments) {
        HashMap<String, Document> processedRCDocuments = new HashMap<String, Document>();

        try {
            // load the GAPP
            application = (CorpusController) PersistenceManager.loadObjectFromFile(new File(Main.workingDir + File.separator + "ACLSUMM_NORMALIZED_VECTORS.gapp"));

            Corpus corpus = null;
            Document doc;
            for (String key : RCDocuments.keySet()) {
                doc = RCDocuments.get(key);
                corpus = Factory.newCorpus("");
                corpus.add(doc);
                application.setCorpus(corpus);
                application.execute();

                processedRCDocuments.put(key, doc);
                Factory.deleteResource(corpus);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ResourceInstantiationException e) {
            e.printStackTrace();
        }
        return processedRCDocuments;
    }

    public static HashMap<String, Document> applyCosineSimilarities
            (HashMap<String, Document> RCDocuments, File rfolder) {
        HashMap<String, Document> processedDocuments = new HashMap<String, Document>();
        HashMap<String, FeatureMap> combinedcpAnnotatorsIDsNormalizedVectors;

        Document rp = RCDocuments.get(rfolder.getName());

        AnnotationSet rpAnalysis = rp.getAnnotations("Analysis");
        AnnotationSet rpNormalizedVectors = rpAnalysis.get("Vector_Norm");

        for (String key : RCDocuments.keySet()) {
            if (!key.equals(rfolder.getName())) {
                Document cp = RCDocuments.get(key);

                AnnotationSet rpSimilarity = rp.getAnnotations("Similarities");
                AnnotationSet cpAnalysis = cp.getAnnotations("Analysis");
                AnnotationSet cpCitMarkups = cp.getAnnotations("CITATIONS");
                AnnotationSet cpNormalizedVectors = cpAnalysis.get("Vector_Norm");
                AnnotationSet rpSentences = rpAnalysis.get("Sentence");

                AnnotationSet rpSentencesNVOffset;

                Annotation rpSentenceNVOffset;

                Long cpStartA, cpEndA, rpStartNV = null, rpEndNV = null, rpMaxStartNVS, rpMAxEndNNVS;
                Iterator cpAnnotatorsIterator = cpCitMarkups.iterator();

                double maxCosineValue = -1;
                int rpMaxNormalizedVectorID = 0;

                combinedcpAnnotatorsIDsNormalizedVectors = combinecpAnnotatorsIDsNormalizedVectors(cpCitMarkups, cpNormalizedVectors);
                for (String id : combinedcpAnnotatorsIDsNormalizedVectors.keySet()) {
                    FeatureMap rpfm_anns = Factory.newFeatureMap();
                    Iterator rpNormalizedVectorsIterator = rpNormalizedVectors.iterator();

                    while (rpNormalizedVectorsIterator.hasNext()) {
                        Annotation rpNormalizedVector = (Annotation) rpNormalizedVectorsIterator.next();
                        rpStartNV = rpNormalizedVector.getStartNode().getOffset();
                        rpEndNV = rpNormalizedVector.getEndNode().getOffset();
                        FeatureMap rpNormalizedVectorfm = rpNormalizedVector.getFeatures();

                        rpSentencesNVOffset = rpSentences.get(rpStartNV);

                        if (rpSentencesNVOffset.size() > 0) {
                            rpSentenceNVOffset = rpSentencesNVOffset.iterator().next();
                            FeatureMap rpSentencefm = rpSentenceNVOffset.getFeatures();
                            rpSentencefm.put("sim_" + id,
                                    summa.scorer.Cosine.cosine1(combinedcpAnnotatorsIDsNormalizedVectors.get(id),
                                            rpNormalizedVectorfm));
                            if (summa.scorer.Cosine.cosine1(combinedcpAnnotatorsIDsNormalizedVectors.get(id),
                                    rpNormalizedVectorfm) > maxCosineValue) {
                                maxCosineValue = summa.scorer.Cosine.cosine1(combinedcpAnnotatorsIDsNormalizedVectors.get(id),
                                        rpNormalizedVectorfm);
                                rpMaxNormalizedVectorID = rpNormalizedVector.getId();
                            }
                        } else {
                            System.out.println("Could not find the Normalized Victor Sentence.");
                        }
                    }

                    Annotation rpMaxNormalizedVector = rpNormalizedVectors.get(rpMaxNormalizedVectorID);
                    rpMaxStartNVS = rpMaxNormalizedVector.getStartNode().getOffset();
                    rpMAxEndNNVS = rpMaxNormalizedVector.getEndNode().getOffset();

                    rpfm_anns.put("MatchCitanceID", id);
                    rpfm_anns.put("MatchSimilarityValue", maxCosineValue);

                    try {
                        rpSimilarity.add(rpMaxStartNVS, rpMAxEndNNVS, "Match", rpfm_anns);
                    } catch (InvalidOffsetException e) {
                        e.printStackTrace();
                    }
                }
                maxCosineValue = -1;
                rpMaxNormalizedVectorID = 0;
            }
        }

        RCDocuments.put(rfolder.getName(), rp);

        processedDocuments = RCDocuments;

        return processedDocuments;
    }

    public static HashMap<String, Document> applyBabelnetCosineSimilarities
            (HashMap<String, Document> RCDocuments, File rfolder) {
        HashMap<String, Document> processedDocuments = new HashMap<String, Document>();
        HashMap<String, FeatureMap> combinedcpAnnotatorsIDsBabelnetNormalizedVectors;

        Document rp = RCDocuments.get(rfolder.getName());

        AnnotationSet rpBabelnet = rp.getAnnotations("Babelnet");
        AnnotationSet rpBabelnetNormalizedVectors = rpBabelnet.get("BNVector_Norm");

        for (String key : RCDocuments.keySet()) {
            if (!key.equals(rfolder.getName())) {
                Document cp = RCDocuments.get(key);

                AnnotationSet rpSimilarity = rp.getAnnotations("BabelnetSimilarities");
                AnnotationSet cpBabelnet = cp.getAnnotations("Babelnet");
                AnnotationSet cpCitMarkups = cp.getAnnotations("CITATIONS");
                AnnotationSet cpBabelnetNormalizedVectors = cpBabelnet.get("BNVector_Norm");
                AnnotationSet rpSentences = rpBabelnet.get("Sentence");

                AnnotationSet rpSentencesNVOffset;

                Annotation rpSentenceNVOffset;

                Long cpStartA, cpEndA, rpStartNV = null, rpEndNV = null, rpMaxStartNVS, rpMAxEndNNVS;
                Iterator cpAnnotatorsIterator = cpCitMarkups.iterator();

                double maxCosineValue = -1;
                int rpMaxBabelnetNormalizedVectorID = 0;

                combinedcpAnnotatorsIDsBabelnetNormalizedVectors = combinecpAnnotatorsIDsNormalizedVectors(cpCitMarkups, cpBabelnetNormalizedVectors);
                for (String id : combinedcpAnnotatorsIDsBabelnetNormalizedVectors.keySet()) {
                    FeatureMap rpfm_anns = Factory.newFeatureMap();
                    Iterator rpBabelnetNormalizedVectorsIterator = rpBabelnetNormalizedVectors.iterator();

                    while (rpBabelnetNormalizedVectorsIterator.hasNext()) {
                        Annotation rpBabelnetNormalizedVector = (Annotation) rpBabelnetNormalizedVectorsIterator.next();
                        rpStartNV = rpBabelnetNormalizedVector.getStartNode().getOffset();
                        rpEndNV = rpBabelnetNormalizedVector.getEndNode().getOffset();
                        FeatureMap rpBabelnetNormalizedVectorfm = rpBabelnetNormalizedVector.getFeatures();

                        rpSentencesNVOffset = rpSentences.get(rpStartNV);

                        if (rpSentencesNVOffset.size() > 0) {
                            rpSentenceNVOffset = rpSentencesNVOffset.iterator().next();
                            FeatureMap rpSentencefm = rpSentenceNVOffset.getFeatures();
                            FeatureMap t = combinedcpAnnotatorsIDsBabelnetNormalizedVectors.get(id);
                            FeatureMap k = rpBabelnetNormalizedVectorfm;
                            double b = summa.scorer.Cosine.cosine1(t, k);

                            rpSentencefm.put("BNsim_" + id,
                                    summa.scorer.Cosine.cosine1(combinedcpAnnotatorsIDsBabelnetNormalizedVectors.get(id),
                                            rpBabelnetNormalizedVectorfm));
                            if (summa.scorer.Cosine.cosine1(combinedcpAnnotatorsIDsBabelnetNormalizedVectors.get(id),
                                    rpBabelnetNormalizedVectorfm) > maxCosineValue) {
                                maxCosineValue = summa.scorer.Cosine.cosine1(combinedcpAnnotatorsIDsBabelnetNormalizedVectors.get(id),
                                        rpBabelnetNormalizedVectorfm);
                                rpMaxBabelnetNormalizedVectorID = rpBabelnetNormalizedVector.getId();
                            }
                        } else {
                            System.out.println("Could not find the Normalized Victor Sentence.");
                        }
                    }

                    Annotation rpMaxBabelnetNormalizedVector = rpBabelnetNormalizedVectors.get(rpMaxBabelnetNormalizedVectorID);
                    rpMaxStartNVS = rpMaxBabelnetNormalizedVector.getStartNode().getOffset();
                    rpMAxEndNNVS = rpMaxBabelnetNormalizedVector.getEndNode().getOffset();

                    rpfm_anns.put("MatchCitanceID", id);
                    rpfm_anns.put("MatchSimilarityValue", maxCosineValue);

                    try {
                        rpSimilarity.add(rpMaxStartNVS, rpMAxEndNNVS, "Match", rpfm_anns);
                    } catch (InvalidOffsetException e) {
                        e.printStackTrace();
                    }
                }
                maxCosineValue = -1;
                rpMaxBabelnetNormalizedVectorID = 0;
            }
        }

        RCDocuments.put(rfolder.getName(), rp);

        processedDocuments = RCDocuments;

        return processedDocuments;
    }

    public static HashMap<String, Document> applyDRInventer
            (HashMap<String, Document> processedDocuments) {
        for (String key : processedDocuments.keySet()) {
            PrintStream out = System.out;
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            }));
            try {
                processedDocuments.put(key, Util.enrichSentences(processedDocuments.get(key), "Original markups", "S"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.setOut(out);
            }
        }
        return processedDocuments;
    }

    public static HashMap<String, Document> applyTokenLemmasBNKindFilling
            (HashMap<String, Document> tokenLemmasBNKindsFilledRCDocuments) {
        Document doc = null;
        for (String key : tokenLemmasBNKindsFilledRCDocuments.keySet()) {
            doc = tokenLemmasBNKindsFilledRCDocuments.get(key);
            for (Annotation annotation : doc.getAnnotations("Analysis").get("Token")) {
                FeatureMap fm = annotation.getFeatures();
                if (!fm.containsKey("lemma")) {
                    if (fm.containsKey("string")) {
                        fm.put("lemma", fm.get("string"));
                    } else {
                        try {
                            String value = String.valueOf(doc.getContent().getContent(annotation.getStartNode().getOffset(), annotation.getEndNode().getOffset()));
                            fm.put("lemma", value.toLowerCase());
                        } catch (InvalidOffsetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            for (Annotation annotation : doc.getAnnotations("Babelnet").get("Entity")) {
                FeatureMap fm = annotation.getFeatures();
                if (!fm.containsKey("kind")) {
                    fm.put("kind", "entity");
                }
            }

            tokenLemmasBNKindsFilledRCDocuments.put(key, doc);
        }

        return tokenLemmasBNKindsFilledRCDocuments;
    }

    public static HashMap<String, FeatureMap> combinecpAnnotatorsIDsNormalizedVectors(AnnotationSet
                                                                                              cpAnnotators, AnnotationSet cpNormalizedVectors) {
        HashMap<String, FeatureMap> combinedcpAnnotatorsIDsNormalizedVectors = new HashMap<String, FeatureMap>();
        AnnotationSet cpAnnotatorNormalizedVectors;
        Annotation cpAnnotator;
        Annotation cpAnnotatorNormalizedVector = null;
        FeatureMap cpAnnotatorfm;
        Long cpStartA, cpEndA;

        Iterator cpAnnotatorsIterator = cpAnnotators.iterator();
        while (cpAnnotatorsIterator.hasNext()) {
            cpAnnotator = (Annotation) cpAnnotatorsIterator.next();
            cpStartA = cpAnnotator.getStartNode().getOffset();
            cpEndA = cpAnnotator.getEndNode().getOffset();

            cpAnnotatorfm = cpAnnotator.getFeatures();
            String id = (String) cpAnnotatorfm.get("id");

            if (combinedcpAnnotatorsIDsNormalizedVectors.containsKey(id)) {
                cpAnnotatorNormalizedVectors = cpNormalizedVectors.get(cpStartA);

                if (cpAnnotatorNormalizedVectors.size() > 0) {
                    cpAnnotatorNormalizedVector = cpAnnotatorNormalizedVectors.iterator().next();
                    combinedcpAnnotatorsIDsNormalizedVectors.put(id,
                            combineNormalizedVectors(combinedcpAnnotatorsIDsNormalizedVectors.get(id),
                                    cpAnnotatorNormalizedVector.getFeatures()));
                } else {
                    System.out.println("Could not find the Annotator Normalized Victor.");
                }
            } else {
                cpAnnotatorNormalizedVectors = cpNormalizedVectors.get(cpStartA);
                if (cpAnnotatorNormalizedVectors.size() > 0) {
                    cpAnnotatorNormalizedVector = cpAnnotatorNormalizedVectors.iterator().next();
                    combinedcpAnnotatorsIDsNormalizedVectors.put(id, cpAnnotatorNormalizedVector.getFeatures());
                } else {
                    System.out.println("Could not find the Annotator Normalized Victor.");
                }
            }
        }

        return combinedcpAnnotatorsIDsNormalizedVectors;
    }

    public static FeatureMap combineNormalizedVectors(FeatureMap normalizedVector1, FeatureMap
            normalizedVector2) {
        FeatureMap combineNormalizedVector = Factory.newFeatureMap();
        for (Object key : normalizedVector1.keySet()) {
            if (normalizedVector2.containsKey(key)) {
                combineNormalizedVector.put(key, String.valueOf((new Double((String) normalizedVector1.get(key)) +
                        new Double((String) normalizedVector2.get(key))) / 2.0));
            } else {
                combineNormalizedVector.put(key, String.valueOf((new Double((String) normalizedVector1.get(key)) / 2.0)));
            }
        }

        for (Object key : normalizedVector2.keySet()) {
            if (!normalizedVector1.containsKey(key)) {
                combineNormalizedVector.put(key, String.valueOf((new Double((String) normalizedVector2.get(key)) / 2.0)));
            }
        }
        return combineNormalizedVector;
    }

    public static double computeSentenceID(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Annotation refSentence = obj.getReferenceSentence();

        if (refSentence.getFeatures().containsKey("sid")) {
            if (refSentence.getFeatures().get("sid") != null && !refSentence.getFeatures().get("sid").equals("")) {
                //VALIDATE String
                final String Digits = "(\\p{Digit}+)";
                final String HexDigits = "(\\p{XDigit}+)";
                // an exponent is 'e' or 'E' followed by an optionally
                // signed decimal integer.
                final String Exp = "[eE][+-]?" + Digits;
                final String fpRegex =
                        ("[\\x00-\\x20]*" +  // Optional leading "whitespace"
                                "[+-]?(" + // Optional sign character
                                "NaN|" +           // "NaN" string
                                "Infinity|" +      // "Infinity" string

                                // A decimal floating-point string representing a finite positive
                                // number without a leading sign has at most five basic pieces:
                                // Digits . Digits ExponentPart FloatTypeSuffix
                                //
                                // Since this method allows integer-only strings as input
                                // in addition to strings of floating-point literals, the
                                // two sub-patterns below are simplifications of the grammar
                                // productions from section 3.10.2 of
                                // The Java™ Language Specification.

                                // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                                "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

                                // . Digits ExponentPart_opt FloatTypeSuffix_opt
                                "(\\.(" + Digits + ")(" + Exp + ")?)|" +

                                // Hexadecimal strings
                                "((" +
                                // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                                "(0[xX]" + HexDigits + "(\\.)?)|" +

                                // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                                "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                                ")[pP][+-]?" + Digits + "))" +
                                "[fFdD]?))" +
                                "[\\x00-\\x20]*");// Optional trailing "whitespace"


                if (Pattern.matches(fpRegex, (String) refSentence.getFeatures().get("sid"))) {
                    Double sentenceIDValue = Double.valueOf((String) refSentence.getFeatures().get("sid")) + 1;
                    if (!sentenceIDValue.isInfinite() && !sentenceIDValue.isNaN() && sentenceIDValue > 0) {
                        value = (1d / sentenceIDValue);
                    }
                }
            }
        }
        return value;
    }

    public static double computeSentenceSectionID(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Annotation refSentence = obj.getReferenceSentence();

        if (refSentence.getFeatures().containsKey("ssid")) {
            if (refSentence.getFeatures().get("ssid") != null && !refSentence.getFeatures().get("ssid").equals("")) {
                //VALIDATE String
                final String Digits = "(\\p{Digit}+)";
                final String HexDigits = "(\\p{XDigit}+)";
                // an exponent is 'e' or 'E' followed by an optionally
                // signed decimal integer.
                final String Exp = "[eE][+-]?" + Digits;
                final String fpRegex =
                        ("[\\x00-\\x20]*" +  // Optional leading "whitespace"
                                "[+-]?(" + // Optional sign character
                                "NaN|" +           // "NaN" string
                                "Infinity|" +      // "Infinity" string

                                // A decimal floating-point string representing a finite positive
                                // number without a leading sign has at most five basic pieces:
                                // Digits . Digits ExponentPart FloatTypeSuffix
                                //
                                // Since this method allows integer-only strings as input
                                // in addition to strings of floating-point literals, the
                                // two sub-patterns below are simplifications of the grammar
                                // productions from section 3.10.2 of
                                // The Java™ Language Specification.

                                // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                                "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

                                // . Digits ExponentPart_opt FloatTypeSuffix_opt
                                "(\\.(" + Digits + ")(" + Exp + ")?)|" +

                                // Hexadecimal strings
                                "((" +
                                // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                                "(0[xX]" + HexDigits + "(\\.)?)|" +

                                // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                                "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                                ")[pP][+-]?" + Digits + "))" +
                                "[fFdD]?))" +
                                "[\\x00-\\x20]*");// Optional trailing "whitespace"

                if (Pattern.matches(fpRegex, (String) refSentence.getFeatures().get("ssid"))) {
                    Double sentenceSectionIDValue = Double.valueOf((String) refSentence.getFeatures().get("ssid")) + 1;
                    if (!sentenceSectionIDValue.isInfinite() && !sentenceSectionIDValue.isNaN() && sentenceSectionIDValue > 0) {
                        value = (1d / sentenceSectionIDValue);
                    }
                }
            }
        }

        return value;
    }

    public static double computeFacetAim(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        String[] matches = new String[]{"aim", "objective", "purpose"};

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet sections = rp.getAnnotations("Original markups").get("SECTION").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (sections.size() > 0) {
            Annotation section = sections.iterator().next();

            for (String s : matches) {
                if (section.getFeatures().get("title").toString().toLowerCase().contains(s)) {
                    value = 1d;
                }
            }
        }

        return value;
    }

    public static double computeFacetHypothesis(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        String[] matches = new String[]{"hypothesis", "possibility", "theory"};

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet sections = rp.getAnnotations("Original markups").get("SECTION").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (sections.size() > 0) {
            Annotation section = sections.iterator().next();

            for (String s : matches) {
                if (section.getFeatures().get("title").toString().toLowerCase().contains(s)) {
                    value = 1d;
                }
            }
        }

        return value;
    }

    public static double computeFacetImplication(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        String[] matches = new String[]{"implication", "deduction", "entailment"};

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet sections = rp.getAnnotations("Original markups").get("SECTION").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (sections.size() > 0) {
            Annotation section = sections.iterator().next();

            for (String s : matches) {
                if (section.getFeatures().get("title").toString().toLowerCase().contains(s)) {
                    value = 1d;
                }
            }
        }

        return value;
    }

    public static double computeFacetMethod(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        String[] matches = new String[]{"method"};

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet sections = rp.getAnnotations("Original markups").get("SECTION").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (sections.size() > 0) {
            Annotation section = sections.iterator().next();

            for (String s : matches) {
                if (section.getFeatures().get("title").toString().toLowerCase().contains(s)) {
                    value = 1d;
                }
            }
        }

        return value;
    }

    public static double computeFacetResult(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        String[] matches = new String[]{"result", "solution", "outcome", "answer"};

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet sections = rp.getAnnotations("Original markups").get("SECTION").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (sections.size() > 0) {
            Annotation section = sections.iterator().next();

            for (String s : matches) {
                if (section.getFeatures().get("title").toString().toLowerCase().contains(s)) {
                    value = 1d;
                }
            }
        }

        return value;
    }

    public static double computeJiangconrathSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {

                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double val = new JiangConrath(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());

                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }

        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedJiangconrathSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {

                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {

                    double maxSim = new JiangConrath(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new JiangConrath(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;

                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }

        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeLCHSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);

        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {

                    double val = new LeacockChodorow(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }

        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedLCHSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);

        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double maxSim = new LeacockChodorow(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new LeacockChodorow(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }

        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeLeskSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double val = new Lesk(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }

        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedLeskSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double maxSim = new Lesk(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new Lesk(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }

        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeLinSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double val = new Lin(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedLinSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double maxSim = new Lin(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new Lin(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computePathSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);

        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double val = new Path(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedPathSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);

        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double maxSim = new Path(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new Path(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeResnikSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double val = new Resnik(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedResnikSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);
        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double maxSim = new Resnik(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new Resnik(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeWUPSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);

        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double val = new WuPalmer(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString());
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeNormalizedWUPSimilarity(TrainingExample obj, DocumentCtx docs) {
        ILexicalDatabase db = new NictWordNet();
        double value = 0d;
        int count = 0;
        double sum = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        WS4JConfiguration.getInstance().setMFS(true);

        for (Annotation cpToken : cpTokens) {
            for (Annotation rpToken : rpTokens) {
                if (!rpToken.getFeatures().get("string").toString().equals(cpToken.getFeatures().get("string").toString())) {
                    double maxSim = new WuPalmer(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            cpToken.getFeatures().get("string").toString());

                    double val = new WuPalmer(db).calcRelatednessOfWords(cpToken.getFeatures().get("string").toString(),
                            rpToken.getFeatures().get("string").toString()) / maxSim;
                    if (!Double.isInfinite(val)) {
                        sum += val;
                        count++;
                    }
                } else {
                    sum++;
                }
            }
        }
        if (count > 0) {
            value = (sum / count);
        } else {
            value = 0d;
        }

        return value;
    }

    public static double computeCosineSimilarity(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSimilarities = rp.getAnnotations("Analysis").get("Sentence").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpAnnotators = cp.getAnnotations("CITATIONS").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        if (rpSimilarities.size() > 0 && cpAnnotators.size() > 0) {
            Annotation rpSentence = rpSimilarities.iterator().next();
            Annotation cpAnnotator = cpAnnotators.iterator().next();

            value = (Double) rpSentence.getFeatures().get("sim_" + cpAnnotator.getFeatures().get("id"));
        }

        return value;
    }

    public static double computeBabelnetCosineSimilarity(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSimilarities = rp.getAnnotations("Babelnet").get("Sentence").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpAnnotators = cp.getAnnotations("CITATIONS").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        if (rpSimilarities.size() > 0 && cpAnnotators.size() > 0) {
            Annotation rpSentence = rpSimilarities.iterator().next();
            Annotation cpAnnotator = cpAnnotators.iterator().next();

            value = (Double) rpSentence.getFeatures().get("BNsim_" + cpAnnotator.getFeatures().get("id"));
        }

        return value;
    }

    public static double computeProbabilityApproach(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSentenceProbabilities = rp.getAnnotations("Analysis").get("Sentence_LOA").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (rpSentenceProbabilities.size() > 0) {
            Annotation rpSentence = rpSentenceProbabilities.iterator().next();

            if (rpSentence.getFeatures().get("PROB_DRI_Approach") != null)
                value = ((double) rpSentence.getFeatures().get("PROB_DRI_Approach"));
        }

        return value;
    }

    public static double computeProbabilityBackground(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSentenceProbabilities = rp.getAnnotations("Analysis").get("Sentence_LOA").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (rpSentenceProbabilities.size() > 0) {
            Annotation rpSentence = rpSentenceProbabilities.iterator().next();

            if (rpSentence.getFeatures().get("PROB_DRI_Background") != null)
                value = ((Double) rpSentence.getFeatures().get("PROB_DRI_Background"));
        }

        return value;
    }

    public static double computeProbabilityChallenge(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSentenceProbabilities = rp.getAnnotations("Analysis").get("Sentence_LOA").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (rpSentenceProbabilities.size() > 0) {
            Annotation rpSentence = rpSentenceProbabilities.iterator().next();

            if (rpSentence.getFeatures().get("PROB_DRI_Challenge") != null)
                value = ((Double) rpSentence.getFeatures().get("PROB_DRI_Challenge"));
        }

        return value;
    }

    public static double computeProbabilityFutureWork(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSentenceProbabilities = rp.getAnnotations("Analysis").get("Sentence_LOA").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (rpSentenceProbabilities.size() > 0) {
            Annotation rpSentence = rpSentenceProbabilities.iterator().next();

            if (rpSentence.getFeatures().get("PROB_DRI_FutureWork") != null)
                value = ((Double) rpSentence.getFeatures().get("PROB_DRI_FutureWork"));
        }

        return value;
    }

    public static double computeProbabilityOutcome(TrainingExample obj, DocumentCtx docs) {
        double value = 0d;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpSentenceProbabilities = rp.getAnnotations("Analysis").get("Sentence_LOA").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if (rpSentenceProbabilities.size() > 0) {
            Annotation rpSentence = rpSentenceProbabilities.iterator().next();

            if (rpSentence.getFeatures().get("PROB_DRI_Outcome") != null)
                value = ((Double) rpSentence.getFeatures().get("PROB_DRI_Outcome"));
        }

        return value;
    }

    public static double computeCPCitMarkerCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpCitMarkers = cp.getAnnotations("Analysis").get("CitMarker").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpCitMarkers) {
            value++;
        }

        return value;
    }

    public static double computeNormalizedCPCitMarkerCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        double totalCount = cp.getAnnotations("Analysis").get("CitMarker").size();

        AnnotationSet cpCitMarkers = cp.getAnnotations("Analysis").get("CitMarker").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpCitMarkers) {
            value++;
        }

        if (totalCount > 0) {
            return value / totalCount;
        }

        return 0;
    }

    public static double computeRPCitMarkerCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpCitMarkers = rp.getAnnotations("Analysis").get("CitMarker").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCitMarkers) {
            value++;
        }

        return value;
    }

    public static double computeNormalizedRPCitMarkerCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        double totalCount = rp.getAnnotations("Analysis").get("CitMarker").size();

        AnnotationSet rpCitMarkers = rp.getAnnotations("Analysis").get("CitMarker").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCitMarkers) {
            value++;
        }

        if (totalCount > 0) {
            return value / totalCount;
        } else {
            return 0;
        }

    }

    public static double computeCitMarkerCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpCitMarkers = rp.getAnnotations("Analysis").get("CitMarker").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpCitMarkers = cp.getAnnotations("Analysis").get("CitMarker").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCitMarkers) {
            value++;
        }

        for (Annotation annotation : cpCitMarkers) {
            value++;
        }

        return value;
    }

    public static double computeNormalizedCitMarkerCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        double rpTotalCount = rp.getAnnotations("Analysis").get("CitMarker").size();
        double cpTotalCount = cp.getAnnotations("Analysis").get("CitMarker").size();

        AnnotationSet rpCitMarkers = rp.getAnnotations("Analysis").get("CitMarker").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpCitMarkers = cp.getAnnotations("Analysis").get("CitMarker").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCitMarkers) {
            value++;
        }

        for (Annotation annotation : cpCitMarkers) {
            value++;
        }

        if (cpTotalCount + rpTotalCount > 0) {
            return value / (cpTotalCount + rpTotalCount);
        }

        return 0;
    }

    public static double computeCPCauseAffectExistance(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpCause = cp.getAnnotations("Causality").get("CAUSE").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        AnnotationSet cpEffect = cp.getAnnotations("Causality").get("EFFECT").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        if (cpCause.size() > 0 || cpEffect.size() > 0) {
            value = 1;
        }

        return value;
    }

    public static double computeRPCauseAffectExistance(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpCause = rp.getAnnotations("Causality").get("CAUSE").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet rpEffect = rp.getAnnotations("Causality").get("EFFECT").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        if ((rpCause.size() > 0 || rpEffect.size() > 0)) {
            value = 1;
        }

        return value;
    }

    public static double computeCPCorefChainsCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpCorefChains = cp.getAnnotations("CorefChains").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpCorefChains) {
            value++;
        }

        return value;
    }

    public static double computeNormalizedCPCorefChainsCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        double totalCount = cp.getAnnotations("CorefChains").size();

        AnnotationSet cpCorefChains = cp.getAnnotations("CorefChains").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpCorefChains) {
            value++;
        }

        if (totalCount > 0) {
            return value / totalCount;
        }

        return 0;
    }

    public static double computeRPCorefChainsCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpCorefChains = rp.getAnnotations("CorefChains").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCorefChains) {
            value++;
        }

        return value;
    }

    public static double computeNormalizedRPCorefChainsCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        double totalCount = rp.getAnnotations("CorefChains").size();

        AnnotationSet rpCorefChains = rp.getAnnotations("CorefChains").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCorefChains) {
            value++;
        }

        if (totalCount > 0) {
            return value / totalCount;
        }

        return 0;
    }

    public static double computeCorefChainsCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpCorefChains = rp.getAnnotations("CorefChains").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpCorefChains = cp.getAnnotations("CorefChains").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCorefChains) {
            value++;
        }

        for (Annotation annotation : cpCorefChains) {
            value++;
        }

        return value;
    }

    public static double computeNormalizedCorefChainsCount(TrainingExample obj, DocumentCtx docs) {
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        double rpTotalCount = rp.getAnnotations("CorefChains").size();
        double cpTotalCount = cp.getAnnotations("CorefChains").size();

        AnnotationSet rpCorefChains = rp.getAnnotations("CorefChains").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpCorefChains = cp.getAnnotations("CorefChains").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpCorefChains) {
            value++;
        }

        for (Annotation annotation : cpCorefChains) {
            value++;
        }

        if ((cpTotalCount + rpTotalCount) > 0) {
            return value / (cpTotalCount + rpTotalCount);
        }

        return 0;
    }

    public static double getPercentage(int n, int total) {
        double proportion = 0d;
        if (total != 0) {
            proportion = ((double) n) / ((double) total);
        }
        return proportion;
    }

    public static double computeGazResearchMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazResearchMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research")) {
                MTCount++;
            }
        }

        /*for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("research")) {
                totalCount++;
            }
        }*/

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazResearchMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research")) {
                MTCount++;
            }
        }

        /*for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("research")) {
                totalCount++;
            }
        }*/

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazArgumentationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazArgumentationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("argumentation")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazArgumentationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("argumentation")) {
                totalCount++;
            }
        }*/

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazAwareMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazAwareMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("aware")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazAwareMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("aware")) {
                totalCount++;
            }
        }*/

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazUseMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("use")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("use")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazUseMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("use")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("use")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazUseMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("use")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("use")) {
                totalCount++;
            }
        }*/

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazProblemMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazProblemMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("problem")) {
                totalCount++;
            }
        }*/

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazProblemMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem")) {
                MTCount++;
            }
        }
/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("problem")) {
                totalCount++;
            }
        }*/


        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazSolutionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazSolutionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution")) {
                MTCount++;
            }
        }
        /*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("solution")) {
                totalCount++;
            }
        }*/

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazSolutionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution")) {
                MTCount++;
            }
        }
        /*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("solution")) {
                totalCount++;
            }
        }*/


        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazBetterSolutionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("better_solution")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("better_solution")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazBetterSolutionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("better_solution")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("better_solution")) {
                totalCount++;
            }
        }*/

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazBetterSolutionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("better_solution")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("better_solution")) {
                totalCount++;
            }
        }*/


        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazTextstructureMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("textstructure")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("textstructure")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazTextstructureMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("textstructure")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("textstructure")) {
                totalCount++;
            }
        }*/

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazTextstructureMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("textstructure")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("textstructure")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazInterestMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazInterestMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("interest")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazInterestMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("interest")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazContinueMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("continue")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("continue")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazContinueMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("continue")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("continue")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazContinueMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("continue")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("continue")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazFutureInterestMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_interest")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_interest")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazFutureInterestMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_interest")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("future_interest")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazFutureInterestMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_interest")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("future_interest")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazNeedMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazNeedMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("need")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazNeedMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("need")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazAffectMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("affect")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("affect")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazAffectMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("affect")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("affect")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazAffectMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("affect")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("affect")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazPresentationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazPresentationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("presentation")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazPresentationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("presentation")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazContrastMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazContrastMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("contrast")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazContrastMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("contrast")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazChangeMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazChangeMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("change")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazChangeMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("change")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazComparisonMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazComparisonMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("comparison")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazComparisonMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("comparison")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazSimilarMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazSimilarMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("similar")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazSimilarMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("similar")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazComparisonAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazComparisonAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("comparison_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazComparisonAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("comparison_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazFutureAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazFutureAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("future_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazFutureAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("future_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("future_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazInterestNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazInterestNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("interest_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazInterestNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("interest_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("interest_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazQuestionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazQuestionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("question_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazQuestionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("question_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazAwareAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazAwareAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("aware_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazAwareAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aware_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("aware_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazArgumentationNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazArgumentationNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("argumentation_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazArgumentationNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("argumentation_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("argumentation_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazSimilarNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazSimilarNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("similar_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazSimilarNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("similar_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("similar_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazEarlierAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("earlier_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("earlier_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazEarlierAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("earlier_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("earlier_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazEarlierAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("earlier_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("earlier_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazResearchAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazResearchAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("research_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazResearchAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("research_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazNeedAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazNeedAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("need_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazNeedAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("need_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazReferentialMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("referential")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("referential")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazReferentialMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("referential")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("referential")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazReferentialMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("referential")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("referential")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazQuestionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazQuestionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("question")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazQuestionMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("question")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("question")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazWorkNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("work_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("work_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazWorkNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("work_noun")) {
                MTCount++;
            }
        }
        /*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("work_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazWorkNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("work_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("work_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazChangeAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazChangeAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("change_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazChangeAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("change_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazDisciplineMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("discipline")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("discipline")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazDisciplineMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("discipline")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("discipline")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazDisciplineMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("discipline")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("discipline")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazGivenMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("given")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("given")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazGivenMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("given")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("given")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazGivenMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("given")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("given")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazBadAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("bad_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("bad_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazBadAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("bad_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("bad_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazBadAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("bad_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("bad_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazContrastNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazContrastNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_noun")) {
                MTCount++;
            }
        }
        /*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("contrast_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazContrastNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("contrast_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazNeedNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazNeedNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("need_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazNeedNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("need_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("need_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazAimNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aim_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aim_noun")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazAimNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aim_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("aim_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazAimNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("aim_noun")) {
                MTCount++;
            }
        }
        /*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("aim_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazContrastAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_adj")) {
                MTCount++;
            }
/*
            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazContrastAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("contrast_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazContrastAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("contrast_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("contrast_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazSolutionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazSolutionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("solution_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazSolutionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("solution_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("solution_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazTraditionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazTraditionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("tradition_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazTraditionNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("tradition_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazFirstPronMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("first_pron")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("first_pron")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazFirstPronMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet cpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("first_pron")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("first_pron")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazFirstPronMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("first_pron")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("first_pron")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazProfessionalsMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("professionals")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("professionals")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazProfessionalsMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("professionals")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("professionals")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazProfessionalsMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("professionals")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("professionals")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazProblemNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazProblemNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("problem_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazProblemNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("problem_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazNegationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("negation")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("negation")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }

        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazNegationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("negation")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("negation")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazNegationMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("negation")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("negation")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazTextNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("text_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("text_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }


        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);
        return value;
    }

    public static double computeRPGazTextNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("text_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("text_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazTextNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("text_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("text_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazProblemAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazProblemAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("problem_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazProblemAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("problem_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("problem_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazThirdPronMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("third_pron")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("third_pron")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazThirdPronMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("third_pron")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("third_pron")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazThirdPronMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("third_pron")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("third_pron")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazTraditionAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazTraditionAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("tradition_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazTraditionAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("tradition_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("tradition_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazPresentationNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazPresentationNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("presentation_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazPresentationNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("presentation_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("presentation_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazResearchNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazResearchNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("research_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazResearchNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("research_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("research_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazMainAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;


        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("main_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("main_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazMainAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("main_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("main_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazMainAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("main_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("main_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazReflexiveMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("reflexive")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("reflexive")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazReflexiveMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("reflexive")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("reflexive")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazReflexiveMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("reflexive")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("reflexive")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazNedAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("ned_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("ned_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazNedAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("ned_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("ned_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazNedAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());


        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("ned_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("ned_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazManyMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("many")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("many")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazManyMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("many")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("many")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazManyMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("many")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("many")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazComparisonNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazComparisonNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("comparison_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazComparisonNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("comparison_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("comparison_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazGoodAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("good_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("good_adj")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazGoodAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("good_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("good_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazGoodAdjMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("good_adj")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("good_adj")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeGazChangeNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();
        Document cp = docs.getCitationDoc();

        Annotation refSentence = obj.getReferenceSentence();
        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());
        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_noun")) {
                MTCount++;
            }

/*            if (fm.containsValue("action_lexicon") || fm.containsValue("concept_lexicon")) {
                totalCount++;
            }*/
        }
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = rpTokens.size() + cpTokens.size();

        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeRPGazChangeNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document rp = docs.getReferenceDoc();

        Annotation refSentence = obj.getReferenceSentence();

        AnnotationSet rpLookups = rp.getAnnotations("Analysis").get("Lookup").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        for (Annotation annotation : rpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : rp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("change_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet rpTokens = rp.getAnnotations("Analysis").get("Token").get(refSentence.getStartNode().getOffset(),
                refSentence.getEndNode().getOffset());

        totalCount = rpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static double computeCPGazChangeNounMTProp(TrainingExample obj, DocumentCtx docs) {
        int totalCount = 0;
        int MTCount = 0;
        double value = 0;

        Document cp = docs.getCitationDoc();

        Annotation citSentence = obj.getCitanceSentence();

        AnnotationSet cpLookups = cp.getAnnotations("Analysis").get("Lookup").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());

        for (Annotation annotation : cpLookups) {
            FeatureMap fm = annotation.getFeatures();
            if (fm.containsValue("change_noun")) {
                MTCount++;
            }
        }/*
        for (Annotation lookup : cp.getAnnotations("Analysis").get("Lookup")) {
            FeatureMap fm = lookup.getFeatures();
            if (fm.containsValue("change_noun")) {
                totalCount++;
            }
        }*/
        AnnotationSet cpTokens = cp.getAnnotations("Analysis").get("Token").get(citSentence.getStartNode().getOffset(),
                citSentence.getEndNode().getOffset());
        totalCount = cpTokens.size();
        value = getPercentage(MTCount, totalCount);

        return value;
    }

    public static String computeSentenceBigramLemmasString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        Document cp = null;
        try {
            rp = docs.getReferenceDoc();
            cp = docs.getCitationDoc();

            Annotation refSentence = obj.getReferenceSentence();
            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : rp.getAnnotations("LemmasNGrams").get("2-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
            for (Annotation annotation : cp.getAnnotations("LemmasNGrams").get("2-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeRPSentenceBigramLemmasString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        try {
            rp = docs.getReferenceDoc();

            Annotation refSentence = obj.getReferenceSentence();

            for (Annotation annotation : rp.getAnnotations("LemmasNGrams").get("2-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeCPSentenceBigramLemmasString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document cp = null;
        try {
            cp = docs.getCitationDoc();

            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : cp.getAnnotations("LemmasNGrams").get("2-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeSentenceBigramPOSsString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        Document cp = null;

        try {
            rp = docs.getReferenceDoc();
            cp = docs.getCitationDoc();

            Annotation refSentence = obj.getReferenceSentence();
            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : rp.getAnnotations("POSNGrams").get("2-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
            for (Annotation annotation : cp.getAnnotations("POSNGrams").get("2-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeRPSentenceBigramPOSsString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;

        try {
            rp = docs.getReferenceDoc();

            Annotation refSentence = obj.getReferenceSentence();

            for (Annotation annotation : rp.getAnnotations("POSNGrams").get("2-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeCPSentenceBigramPOSsString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document cp = null;
        try {
            cp = docs.getCitationDoc();

            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : cp.getAnnotations("POSNGrams").get("2-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string").toString().replaceAll(" ", "_");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeSentencePOSsString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        Document cp = null;
        try {
            rp = docs.getReferenceDoc();
            cp = docs.getCitationDoc();

            Annotation refSentence = obj.getReferenceSentence();
            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : rp.getAnnotations("POSNGrams").get("1-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
            for (Annotation annotation : cp.getAnnotations("POSNGrams").get("1-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeRPSentencePOSsString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        try {
            rp = docs.getReferenceDoc();

            Annotation refSentence = obj.getReferenceSentence();

            for (Annotation annotation : rp.getAnnotations("POSNGrams").get("1-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeCPSentencePOSsString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document cp = null;
        try {
            cp = docs.getCitationDoc();

            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : cp.getAnnotations("POSNGrams").get("1-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeSentenceLemmasString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        Document cp = null;

        try {
            rp = docs.getReferenceDoc();
            cp = docs.getCitationDoc();

            Annotation refSentence = obj.getReferenceSentence();
            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : rp.getAnnotations("LemmasNGrams").get("1-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
            for (Annotation annotation : cp.getAnnotations("LemmasNGrams").get("1-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeRPSentenceLemmasString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document rp = null;
        try {
            rp = docs.getReferenceDoc();

            Annotation refSentence = obj.getReferenceSentence();

            for (Annotation annotation : rp.getAnnotations("LemmasNGrams").get("1-gram").get(refSentence.getStartNode().getOffset(), refSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String computeCPSentenceLemmasString(TrainingExample obj, DocumentCtx docs) {
        String value = "";
        Document cp = null;
        try {
            cp = docs.getCitationDoc();

            Annotation citSentence = obj.getCitanceSentence();

            for (Annotation annotation : cp.getAnnotations("LemmasNGrams").get("1-gram").get(citSentence.getStartNode().getOffset(), citSentence.getEndNode().getOffset())) {
                value = value + " " + annotation.getFeatures().get("string");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static HashMap<String, Document> spitOutOutputAnnotations
            (HashMap<String, List<OutputAnnotation>> output, HashMap<String, Document> ProcessedRCDocuments, String
                    rfolderName, Integer maxMatches, String matchProbability, String outputFolder) {
        for (String citance : output.keySet()) {
            int spittedSoFar = 0;

            while (spittedSoFar < maxMatches) {
                OutputAnnotation maxAnnotation = null;
                Double maxProbability = -10d;
                Iterator iterator = output.get(citance).iterator();

                while (iterator.hasNext()) {
                    OutputAnnotation outputAnnotation = (OutputAnnotation) iterator.next();

                    if (outputAnnotation.getAnnotationProbability() >= maxProbability) {
                        maxProbability = outputAnnotation.getAnnotationProbability();
                        maxAnnotation = outputAnnotation;
                    }
                }
                if (output.get(citance).size() > 0) {
                    Utilities.writeSciSummOutput(maxAnnotation, outputFolder + File.separator + "annov3_M" + maxMatches + "_P" + matchProbability + ".txt");

                    ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithReference(maxAnnotation.getTe(), maxAnnotation.getTrCtx(), maxAnnotation.getAnnotationV3().getDiscourse_Facet()));

                    spittedSoFar++;

                    if (maxAnnotation.getMatchTestInstance() != null) {
                        ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithMatchFeatures(maxAnnotation.getMatchTestInstance(), maxAnnotation.getTe(), maxAnnotation.getTrCtx()));

                        Instances facetTestInstance = maxAnnotation.getFacetTestInstance();
                        facetTestInstance.instance(0).setClassValue(maxAnnotation.getAnnotationV3().getDiscourse_Facet());
                        maxAnnotation.setFacetTestInstance(facetTestInstance);

                        ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithFacetFeatures(maxAnnotation.getFacetTestInstance(), maxAnnotation.getTe(), maxAnnotation.getTrCtx()));
                    } else {
                        Instances facetTestInstance = maxAnnotation.getFacetTestInstance();
                        facetTestInstance.instance(0).setClassValue(maxAnnotation.getAnnotationV3().getDiscourse_Facet());
                        maxAnnotation.setFacetTestInstance(facetTestInstance);

                        ProcessedRCDocuments.put(rfolderName, Utilities.annotateRPWithFacetFeatures(maxAnnotation.getFacetTestInstance(), maxAnnotation.getTe(), maxAnnotation.getTrCtx()));
                    }
                    output.get(citance).remove(maxAnnotation);
                } else {
                    break;
                }
            }
        }
        return ProcessedRCDocuments;
    }
}
