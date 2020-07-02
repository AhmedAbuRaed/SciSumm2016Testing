package edu.upf.taln.scisumm2016.reader;

import java.util.ArrayList;

/**
 * Created by ahmed on 4/24/2016.
 */
public class AnnotationV3 {
    private boolean generateTraining;
    private String citance_Number;
    private String reference_Article;
    private String citing_Article;
    private String citation_Marker_Offset;
    private String citation_Marker;
    private ArrayList<String> citation_Offset = new ArrayList<String>();
    private String citation_Text;
    private ArrayList<String> reference_Offset = new ArrayList<String>();
    private String reference_Text;
    private String discourse_Facet;
    private String annotator;

    public AnnotationV3(String annV3Line, boolean generateTraining) {
        init(annV3Line, generateTraining);
    }

    public AnnotationV3(boolean generateTraining) {
        this.generateTraining = generateTraining;
    }

    private void init(String annV3Line, boolean generateTraining) {
        this.generateTraining = generateTraining;
        String[] fields = annV3Line.split("\\|");
        this.citance_Number = fields[0].trim().split(":")[1].trim();
        if (fields[1].trim().split(":")[1].trim().contains(".")) {
            this.reference_Article = fields[1].trim().split(":")[1].trim().substring(0, fields[1].trim().split(":")[1].trim().lastIndexOf('.'));
        } else {
            this.reference_Article = fields[1].trim().split(":")[1].trim();
        }
        if (fields[2].trim().split(":")[1].trim().contains(".")) {
            this.citing_Article = fields[2].trim().split(":")[1].trim().substring(0, fields[2].trim().split(":")[1].trim().lastIndexOf('.'));
        } else {
            this.citing_Article = fields[2].trim().split(":")[1].trim();
        }
        this.citation_Marker_Offset = fields[3].trim().split(":")[1].trim().replaceAll("\\D+", "");
        this.citation_Marker = fields[4].trim().split(":")[1].trim();
        for (String co : fields[5].trim().split(":")[1].trim().split(",")) {
            getCitation_Offset().add(co.replaceAll("\\D+", ""));
        }
        this.citation_Text = fields[6].trim().split(":")[1].trim();

        if (generateTraining) {
            for (String ro : fields[7].trim().split(":")[1].trim().split(",")) {
                getReference_Offset().add(ro.replaceAll("\\D+", ""));
            }
            this.reference_Text = fields[8].trim().split(":")[1].trim();
            this.discourse_Facet = fields[9].trim().split(":")[1].trim();
            if (fields[10].trim().split(":")[1].trim().contains(",")) {
                this.annotator = fields[10].trim().split(":")[1].trim()
                        .substring(0, fields[10].trim().split(":")[1].trim().indexOf(",")).replaceAll(" ", "_");
            } else {
                this.annotator = fields[10].trim().split(":")[1].trim().replaceAll(" ", "_");
            }
        } else {
            if (fields.length == 8) {
                if (fields[7].trim().split(":")[1].trim().contains(",")) {
                    this.annotator = fields[7].trim().split(":")[1].trim()
                            .substring(0, fields[7].trim().split(":")[1].trim().indexOf(",")).replaceAll(" ", "_");
                } else {
                    this.annotator = fields[7].trim().split(":")[1].trim().replaceAll(" ", "_");
                }
            } else {
                if (fields[10].trim().split(":")[1].trim().contains(",")) {
                    this.annotator = fields[10].trim().split(":")[1].trim()
                            .substring(0, fields[10].trim().split(":")[1].trim().indexOf(",")).replaceAll(" ", "_");
                } else {
                    this.annotator = fields[10].trim().split(":")[1].trim().replaceAll(" ", "_");
                }
            }
        }
    }

    public boolean isGenerateTraining() {
        return generateTraining;
    }

    public String getCitance_Number() {
        return citance_Number;
    }

    public String getReference_Article() {
        return reference_Article;
    }

    public String getCiting_Article() {
        return citing_Article;
    }

    public String getCitation_Marker_Offset() {
        return citation_Marker_Offset;
    }

    public String getCitation_Marker() {
        return citation_Marker;
    }

    public ArrayList<String> getCitation_Offset() {
        return citation_Offset;
    }

    public String getCitation_Text() {
        return citation_Text;
    }

    public ArrayList<String> getReference_Offset() {
        return reference_Offset;
    }

    public String getReference_Text() {
        return reference_Text;
    }

    public String getDiscourse_Facet() {
        return discourse_Facet;
    }

    public String getAnnotator() {
        return annotator;
    }
}
