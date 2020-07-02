package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyDouble;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;

/**
 * Created by Ahmed on 10/21/16.
 */
public class RPGAZSIMILARMT_PROP implements FeatCalculator<Double, TrainingExample, DocumentCtx> {
    @Override
    public MyDouble calculateFeature(TrainingExample obj, DocumentCtx docs, String RPGAZSIMILARMT_PROP) {
        MyDouble value = new MyDouble(0d);

        try
        {
            value.setValue((Double) obj.getReferenceSentence().getFeatures().get("RPGAZSIMILARMT_PROP"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }
}
