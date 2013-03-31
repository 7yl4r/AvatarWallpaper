package edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite;

//package weka.classifiers;

/*import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.CapabilitiesHandler;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.classifiers.Classifier;*/

class Classifier {

	  public static double classify(Object[] i)
	    throws Exception {

	    double p = Double.NaN;
	    p = Classifier.N13e9bb0(i);
	    return p;
	  }
	  static double N13e9bb0(Object []i) {
	    double p = Double.NaN;
	    if (i[0] == null) {
	    	p = 0;
	    } else if (((Double) i[0]).doubleValue() <= 70.588999) {
	    	p = 0;
	    } else if (((Double) i[0]).doubleValue() > 70.588999) {
	    	p = Classifier.N15607841(i);
	    } 
	    return p;
	  }
	  static double N15607841(Object []i) {
	    double p = Double.NaN;
	    if (i[0] == null) {
	      p = 1;
	    } else if (((Double) i[0]).doubleValue() <= 239.986935) {
	      p = 1;
	    } else if (((Double) i[0]).doubleValue() > 239.986935) {
	      p = 2;
	    } 
	    return p;
	  }
	}
