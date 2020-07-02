package edu.upf.taln.scisumm2016.reader;

import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import weka.core.Instances;

/**
 * Created by Ahmed on 11/4/16.
 */
public class OutputAnnotation {
    private AnnotationV3 annotationV3 = new AnnotationV3(true);
    private Double annotationProbability;
    private String annotationClass = null;
    private DocumentCtx trCtx = null;
    private TrainingExample te = null;
    private Instances matchTestInstance = null;
    private Instances facetTestInstance = null;

    public OutputAnnotation(AnnotationV3 annotationV3, DocumentCtx trCtx, TrainingExample te, Instances matchTestInstance, Instances facetTestInstance,Double annotationProbability, String annotationClass){
        this.annotationV3 = annotationV3;
        this.trCtx = trCtx;
        this.te = te;
        this.matchTestInstance = matchTestInstance;
        this.facetTestInstance = facetTestInstance;
        this.annotationProbability = annotationProbability;
        this.annotationClass = annotationClass;
    }

    public OutputAnnotation(AnnotationV3 annotationV3, DocumentCtx trCtx, TrainingExample te, Instances facetTestInstance, Double annotationProbability, String annotationClass){
        this.annotationV3 = annotationV3;
        this.trCtx = trCtx;
        this.te = te;
        this.facetTestInstance = facetTestInstance;
        this.annotationProbability = annotationProbability;
        this.annotationClass = annotationClass;
    }


    public AnnotationV3 getAnnotationV3() {
        return annotationV3;
    }

    public void setAnnotationV3(AnnotationV3 annotationV3) {
        this.annotationV3 = annotationV3;
    }

    public Double getAnnotationProbability() {
        return annotationProbability;
    }

    public void setAnnotationProbability(Double annotationProbability) {
        this.annotationProbability = annotationProbability;
    }

    public String getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(String annotationClass) {
        this.annotationClass = annotationClass;
    }

    public DocumentCtx getTrCtx() {
        return trCtx;
    }

    public void setTrCtx(DocumentCtx trCtx) {
        this.trCtx = trCtx;
    }

    public TrainingExample getTe() {
        return te;
    }

    public void setTe(TrainingExample te) {
        this.te = te;
    }

    public Instances getMatchTestInstance() {
        return matchTestInstance;
    }

    public void setMatchTestInstance(Instances matchTestInstance) {
        this.matchTestInstance = matchTestInstance;
    }

    public Instances getFacetTestInstance() {
        return facetTestInstance;
    }

    public void setFacetTestInstance(Instances facetTestInstance) {
        this.facetTestInstance = facetTestInstance;
    }
}
