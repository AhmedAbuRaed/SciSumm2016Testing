package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyDouble;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;


/**
 * Created by ahmed on 5/17/2016.
 */
public class probabilityApproach implements FeatCalculator<Double, TrainingExample, DocumentCtx> {
    @Override
    public MyDouble calculateFeature(TrainingExample obj, DocumentCtx docs, String probabilityApproach) {
        MyDouble value = new MyDouble(0d);

        try
        {
            value.setValue((Double) obj.getReferenceSentence().getFeatures().get("PROBABILITY_APPROACH"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }
}
