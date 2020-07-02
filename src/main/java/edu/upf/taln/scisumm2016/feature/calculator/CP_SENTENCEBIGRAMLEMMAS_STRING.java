package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyString;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;

/**
 * Created by ahmed on 7/8/16.
 */
public class CP_SENTENCEBIGRAMLEMMAS_STRING implements FeatCalculator<String, TrainingExample, DocumentCtx> {
    @Override
    public MyString calculateFeature(TrainingExample obj, DocumentCtx docs, String CP_SENTENCEBIGRAMLEMMAS_STRING) {
        MyString value = new MyString("");

        try
        {
            value.setValue(obj.getReferenceSentence().getFeatures().get("CP_SENTENCEBIGRAMLEMMAS_STRING").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }
}
