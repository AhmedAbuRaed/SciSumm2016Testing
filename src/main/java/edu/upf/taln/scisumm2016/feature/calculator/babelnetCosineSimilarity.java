package edu.upf.taln.scisumm2016.feature.calculator;

import edu.upf.taln.ml.feat.base.FeatCalculator;
import edu.upf.taln.ml.feat.base.MyDouble;
import edu.upf.taln.scisumm2016.feature.context.DocumentCtx;
import edu.upf.taln.scisumm2016.reader.TrainingExample;

/**
 * Created by ahmed on 7/27/16.
 */
public class babelnetCosineSimilarity implements FeatCalculator<Double, TrainingExample, DocumentCtx> {
    @Override
    public MyDouble calculateFeature(TrainingExample obj, DocumentCtx docs, String babelnetCosineSimilarity) {
        MyDouble value = new MyDouble(0d);

        try {
            value.setValue((Double) obj.getReferenceSentence().getFeatures().get("BABELNET_COSINE_SIMILARITY"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}

