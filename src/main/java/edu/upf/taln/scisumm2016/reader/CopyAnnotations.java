package edu.upf.taln.scisumm2016.reader;

import gate.*;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;


/**
 * Created by Ahmed on 2/10/17.
 */
public class CopyAnnotations {
    public static void main(String args[]) {
        String sourceDocPath = args[1];
        String targetDocPath = args[2];
        String asSourceName = args[3];
        String asTargetName = args[4];

        try {
            Gate.init();
        } catch (GateException e) {
            e.printStackTrace();
        }

        File sourceFile = new File(sourceDocPath);
        File targetFile = new File(targetDocPath);
        Document sourceDoc = null;
        Document targetDoc = null;

        try {
            sourceDoc = Factory.newDocument(new URL("file:///" + sourceFile.getPath()), "UTF-8");
            targetDoc = Factory.newDocument(new URL("file:///" + targetFile.getPath()), "UTF-8");
        } catch (ResourceInstantiationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        AnnotationSet origSents;
        AnnotationSet aux;
        Annotation auxSent;

        origSents=targetDoc.getAnnotations("Analysis").get("Sentence");

        AnnotationSet sourceAnnotationSet = sourceDoc.getAnnotations(asSourceName);
        Iterator sourceAnnotationSetIterator = sourceAnnotationSet.iterator();

        AnnotationSet targetAnnotationSet = targetDoc.getAnnotations(asTargetName);

        while (sourceAnnotationSetIterator.hasNext())
        {
            Annotation annotation = (Annotation) sourceAnnotationSetIterator.next();
            aux=origSents.get(annotation.getStartNode().getOffset(), annotation.getEndNode().getOffset());
            if(aux.size() > 0)
            {
                auxSent=aux.iterator().next();
                try {
                    targetAnnotationSet.add(auxSent.getStartNode().getOffset(), auxSent.getEndNode().getOffset(),
                            annotation.getType(),annotation.getFeatures());
                } catch (InvalidOffsetException e) {
                    e.printStackTrace();
                }
            }
        }

        PrintWriter pw = null;

        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(targetDocPath.replaceAll(".xml","a.xml")), "UTF-8"));
            //pw = new PrintWriter(targetDocPath.replaceAll(".xml","a.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        pw.println(targetDoc.toXml());
        pw.flush();
        pw.close();
        System.out.println("Done");
    }
}
