package edu.upf.taln.scisumm2016.classify;

/**
 * Created by ahmed on 5/30/2016.
 */
public class Main {
    public static String workingDir;
    public static void main(String args[]) {
        workingDir = args[3].trim();
        switch (args[4])
        {
            case "RawDocumentsMain":
                RawDocumentsMain.RawDocumentsMainRun(args);
                break;
            case "EnrichedDocumentsMain":
                EnrichedDocumentsMain.EnrichedDocumentsMainRun(args);
                break;
            case "FacetsMain":
                FacetsMain.FacetsMainRun(args);
                break;
            case "FacetsSystemBasedMain":
                FacetsSystemBasedMain.FacetsSystemMainRun(args);
                break;
            case "FacetsWESystemBasedMain":
                FacetsWESystemBasedMain.FacetsWESystemMainRun(args);
                break;
            case "FacetsWEallSystemBasedMain":
                FacetsWEallSystemBasedMain.FacetsWEallSystemMainRun(args);
                break;
            case "FacetsWEallMergedSystemBasedMain":
                FacetsWEallMergedSystemBasedMain.FacetsWEallMergedSystemMainRun(args);
                break;
        }
    }
}

