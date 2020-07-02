package edu.upf.taln.scisumm2016.reader;

import edu.upf.taln.scisumm2016.Utilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ahmed on 5/3/17.
 */
public class ChallangeFormat {
    public static void main(String[] args) {

        String rfolderName = args[1];
        String baseFolder = args[2] + File.separator + rfolderName;
        Double matchProbability = Double.parseDouble(args[3]);
        Integer maxMatch = Integer.parseInt(args[4]);
        String outputLocation = args[5];

        ArrayList<AnnotationV3> outputAnnotationV3List;
        ArrayList<AnnotationV3> goldStandardAnnotationV3List;
        ArrayList<AnnotationV3Full> finalAnnotationVsList = new ArrayList<AnnotationV3Full>();

        outputAnnotationV3List = Utilities.extractOutputAnnotationsV3FromBaseFolder(new File(baseFolder), maxMatch, matchProbability.toString(), true);
        goldStandardAnnotationV3List = Utilities.extractAnnotationsV3FromBaseFolder(new File(baseFolder), true);

        for (AnnotationV3 goldAnnotation : goldStandardAnnotationV3List) {
            AnnotationV3Full finalAnnotation = new AnnotationV3Full(true);
            finalAnnotation.setDiscourse_Facet(new ArrayList<String>());

            finalAnnotation.setCitance_Number(goldAnnotation.getCitance_Number());
            finalAnnotation.setReference_Article(goldAnnotation.getReference_Article());
            finalAnnotation.setCiting_Article(goldAnnotation.getCiting_Article());
            finalAnnotation.setCitation_Marker_Offset(goldAnnotation.getCitation_Marker_Offset());
            finalAnnotation.setCitation_Marker(goldAnnotation.getCitation_Marker());
            finalAnnotation.setCitation_Offset(goldAnnotation.getCitation_Offset());
            finalAnnotation.setCitation_Text(goldAnnotation.getCitation_Text());
            for (String goldCitation : goldAnnotation.getCitation_Offset()) {
                for (AnnotationV3 outputAnnotation : outputAnnotationV3List) {
                    if (goldAnnotation.getCiting_Article().equals(outputAnnotation.getCiting_Article()) &&
                            goldAnnotation.getCitation_Offset().contains(outputAnnotation.getCitation_Offset().get(0))) {
                        if(!finalAnnotation.getReference_Offset().contains(outputAnnotation.getReference_Offset().get(0)))
                        finalAnnotation.getReference_Offset().add(outputAnnotation.getReference_Offset().get(0));
                        if (finalAnnotation.getReference_Text() != null) {
                            finalAnnotation.getReference_Text().concat(outputAnnotation.getReference_Text());
                        } else {
                            finalAnnotation.setReference_Text(outputAnnotation.getReference_Text());
                        }
                        switch (outputAnnotation.getDiscourse_Facet())
                        {
                            case "AIM":
                                if(!finalAnnotation.getDiscourse_Facet().contains("Aim_Citation"))
                                finalAnnotation.getDiscourse_Facet().add("Aim_Citation");
                                break;
                            case "HYPOTHESIS":
                                if(!finalAnnotation.getDiscourse_Facet().contains("Hypothesis_Citation"))
                                finalAnnotation.getDiscourse_Facet().add("Hypothesis_Citation");
                                break;
                            case "METHOD":
                                if(!finalAnnotation.getDiscourse_Facet().contains("Method_Citation"))
                                finalAnnotation.getDiscourse_Facet().add("Method_Citation");
                                break;
                            case "RESULT":
                                if(!finalAnnotation.getDiscourse_Facet().contains("Results_Citation"))
                                finalAnnotation.getDiscourse_Facet().add("Results_Citation");
                                break;
                            case "IMPLICATION":
                                if(!finalAnnotation.getDiscourse_Facet().contains("Implication_Citation"))
                                finalAnnotation.getDiscourse_Facet().add("Implication_Citation");
                        }
                        finalAnnotation.setAnnotator(outputAnnotation.getAnnotator());
                    }
                }
            }
            finalAnnotationVsList.add(finalAnnotation);
        }
        Utilities.writeSciSummFinallOutput(finalAnnotationVsList, outputLocation + File.separator + rfolderName + ".annv3.txt");
    }
}