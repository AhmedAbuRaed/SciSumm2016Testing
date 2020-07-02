package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyDouble;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;

/**
 * Created by ahmed on 7/8/16.
 */
public class CP_COREFCHAINS_COUNT implements FeatCalculator<Double, TrainingExample, DocumentCtx> {
    @Override
    public MyDouble calculateFeature(TrainingExample obj, DocumentCtx docs, String CP_COREFCHAINS_COUNT) {
        MyDouble value = new MyDouble(0d);

        try
        {
            value.setValue((Double) obj.getReferenceSentence().getFeatures().get("CP_COREFCHAINS_COUNT"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }
}
