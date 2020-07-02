package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyDouble;
import edu.upf.taln.ml.feat.base.MyString;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;

/**
 * Created by ahmed on 7/8/16.
 */
public class RP_SENTENCELEMMAS_STRING implements FeatCalculator<String, TrainingExample, DocumentCtx> {
    @Override
    public MyString calculateFeature(TrainingExample obj, DocumentCtx docs, String RP_SENTENCELEMMAS_STRING) {
        MyString value = new MyString("");

        try
        {
            value.setValue(obj.getReferenceSentence().getFeatures().get("RP_SENTENCELEMMAS_STRING").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }
}
