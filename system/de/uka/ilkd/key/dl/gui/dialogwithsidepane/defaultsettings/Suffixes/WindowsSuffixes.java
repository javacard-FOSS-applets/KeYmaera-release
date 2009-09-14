package de.uka.ilkd.key.dl.gui.dialogwithsidepane.defaultsettings.Suffixes;

import java.io.File;
import java.util.HashMap;

public class WindowsSuffixes {

    public static final WindowsSuffixes INSTANCE = new WindowsSuffixes();
    HashMap<String, String> jLinkSuffixesList; 
    HashMap<String, String> mathKernelSuffixesList; 
    
    public WindowsSuffixes(){
	setjLinkSuffixesList();
	setMathKernelSuffixesList();
	
    }
    private void setjLinkSuffixesList(){
	
	String sp = File.separator;
	jLinkSuffixesList = new HashMap<String, String>();	
	
	jLinkSuffixesList.put("SystemFiles"+sp+"Links",
				"JLink");
	jLinkSuffixesList.put("Mathematica"+sp+"SystemFiles",
		                "Links"+sp+"JLink");
	jLinkSuffixesList.put("Mathematica",
				"SystemFiles"+sp+"Links"+sp+"JLink");
	jLinkSuffixesList.put("Program Files",
				"SystemFiles"+sp+"Links"+sp+"JLink");
	jLinkSuffixesList.put("[#DEFAULT#]",
				"SystemFiles"+sp+"Links"+sp + "JLink");	
	//Suffixes for differents mathematica versions
	for (MathematicaVersions version: MathematicaVersions.values()){
	    
	    jLinkSuffixesList.put("Mathematica" + sp + version.getVersionEndString(),
		    "SystemFiles"+ sp + "Links" + sp + "JLink");	    
	}
	
    }
    public HashMap<String, String> getJLinkSuffixesList(){
	
	return jLinkSuffixesList;
    }
    private void setMathKernelSuffixesList(){
	
	String sp = File.separator;
	mathKernelSuffixesList = new HashMap<String, String>();	

	mathKernelSuffixesList.put("Mathematica",
				"Executables");
	mathKernelSuffixesList.put("Program Files",
				"Mathematica"+sp+"Executables");
	mathKernelSuffixesList.put("[#DEFAULT#]",
				"Executables"+sp+"Executables");
	//Suffixes for differents mathematica versions
	for (MathematicaVersions version: MathematicaVersions.values()){
	    
	    mathKernelSuffixesList.put("Mathematica" + sp + version.getVersionEndString(),
		    "Executables");	    
	}

    }
    public HashMap<String, String> getMathKernelSuffixesList(){
	
	return mathKernelSuffixesList;
    }

}
