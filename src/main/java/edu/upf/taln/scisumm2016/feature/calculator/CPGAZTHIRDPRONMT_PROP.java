package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyDouble;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;

/**
 * Created by Ahmed on 10/23/16.
 */
public class CPGAZTHIRDPRONMT_PROP implements FeatCalculator<Double, TrainingExample, DocumentCtx> {
    @Override
    public MyDouble calculateFeature(TrainingExample obj, DocumentCtx docs, String CPGAZTHIRDPRONMT_PROP) {
        MyDouble value = new MyDouble(0d);

        try
        {
            value.setValue((Double) obj.getReferenceSentence().getFeatures().get("CPGAZTHIRDPRONMT_PROP"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }
}