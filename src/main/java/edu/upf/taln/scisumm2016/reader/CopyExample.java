/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upf.taln.scisumm2016.reader;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Horacio
 */
public class CopyExample {


    public static void main(String[] args) {
        String refPaper;
        String summPaper;
        File inDir;
        String srcLoc = "D:\\Research\\UPF\\Projects\\SciSumm2016Testing\\Scenarios\\SciSUMM-BEST-MATCH-BASELINE";
        String tgtLoc = "D:\\Research\\UPF\\Projects\\SciSumm2016Testing\\Scenarios\\Facets\\allFacets\\SMO\\W03-0410\\Output\\W03-0410";

        Document src;
        Document trg;
        AnnotationSet annSet;
        AnnotationSet origSents;
        AnnotationSet aux;
        Annotation auxSent;
        Annotation sentence;
        Long start, end;
        ArrayList<Annotation> list;
        String cluster = "W03-0410";
        try {
            Gate.init();

            src = Factory.newDocument(new URL("file:///" + srcLoc + File.separator + cluster + "_all_matched.xml"));
            trg = Factory.newDocument(new URL("file:///" + tgtLoc + File.separator + cluster + ".xml"));
            origSents = trg.getAnnotations("Analysis").get("Sentence");
            annSet = src.getAnnotations("WJ_Match_2");
            list = new ArrayList(annSet);
            for (int s = 0; s < list.size(); s++) {
                sentence = list.get(s);
                start = sentence.getStartNode().getOffset();
                end = sentence.getEndNode().getOffset();
                aux = origSents.get(start, end);
                if (aux.size() > 0) {
                    auxSent = aux.iterator().next();
                    trg.getAnnotations("WJ_Match_2").add(auxSent.getStartNode().getOffset(),
                            auxSent.getEndNode().getOffset(), "Sentence", sentence.getFeatures());
                }

            }

            PrintWriter pw = new PrintWriter(tgtLoc + File.separator + cluster + "_test_copy.xml");
            pw.println(trg.toXml());
            pw.flush();
            pw.close();


            Factory.deleteResource(trg);
            Factory.deleteResource(src);


        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(CopyExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(CopyExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CopyExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CopyExample.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


}
