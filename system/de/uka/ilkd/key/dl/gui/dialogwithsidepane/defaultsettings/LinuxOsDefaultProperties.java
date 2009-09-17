package de.uka.ilkd.key.dl.gui.dialogwithsidepane.defaultsettings;

import java.io.File;
import java.util.Properties;
/**
 *         The LinuxOsDefaultProperties class creates and instance of a Property
 *         Object containing all possible default properties for linux platform.
 *         
 *          @author zacho
 * 
 */
public class LinuxOsDefaultProperties implements IOsDefaultProperties {

    private Properties props;
    private String sp = File.separator;
   
    
    /**
     * @return the default Properties for linux Operating system
     */
    public Properties getDefaultPropertyList() {


        if (props == null) {
            props = new Properties();
            initJlinkDefault();
            initMathKernelDefault();
            initQepcadDefault();
            initSaclibDefault();
            initReduceBinaryDefault();
            initcsdpPathDefault();
            initHOLLight();
            initCheckBoxDefault();
        }
        return props;
    }

    /**
     * Initialise jlink default path
     */
    public void initJlinkDefault() {// Change name

       String jlinkDir = sp+"usr"+sp+"local"+sp+"Wolfram"+sp+"Mathematica"+sp+
        			"SystemFiles"+sp+"Links"+sp + "JLink";
        
        props.put("com.wolfram.jlink.libdir", jlinkDir);
    }

    /**
     * Initialise mathkernel default value
     */

    public void initMathKernelDefault() {
	
	props.put("[MathematicaOptions]mathematicaPath", sp+"usr"+sp+"local"+sp+"Wolfram"+sp+"Mathematica");
        props.put("[MathematicaOptions]mathKernel", sp+"usr"+sp+"local"+sp+"Wolfram"+sp+"Mathematica"+sp+"Executables");
    }

    /**
     * Initialise quepcad default path
     */

    public void initQepcadDefault() {

        String qpath = System.getenv("qe");
        if (qpath == null) {
            qpath = System.getProperty("user.home");
            if (qpath == null)
                qpath = "/";
        }
        props.put("[QepcadOptions]qepcadPath", qpath);
    }

    /**
     * Initialise saclib default path
     */

    public void initSaclibDefault() {

        String spath = System.getenv("saclib");
        if (spath == null) {
            spath = System.getProperty("user.home");
            if (spath == null)
                spath = "/";
        }
        props.put("[QepcadOptions]saclibPath", spath);
    }

    /**
     * Initialise reduce binary default value
     */
    public void initReduceBinaryDefault() {
        String rpath = System.getProperty("user.home");
        if (rpath == null)
            rpath = "/";
        props.put("[ReduceOptions]reduceBinary", rpath);
    }

    /**
     * Initialise HOL light paths default values
     */
    public void  initHOLLight(){
	String hol = System.getProperty("user.home");
        if (hol == null)
            hol = "/";
        props.put("[HOLLightOptions]harrisonqePath", hol);
        props.put("[HOLLightOptions]hollightPath", hol);
        
        File olcam = new File(sp+"usr"+sp+"bin"+sp+"ocaml");
	if(!olcam.exists())
	    olcam = new File(System.getProperty("user.home")); 
	if(!olcam.exists())
	    olcam = new File("/"); 
        props.put("[HOLLightOptions]ocamlPath", olcam.getAbsolutePath());
    }
    /**
     * Initialise csdp default value
     */
    public void  initcsdpPathDefault(){
	
	File csdp = new File(sp+"user"+sp+"bin"+sp+"csdp");	
        if (!csdp.exists())
            csdp = new File(System.getProperty("user.home"));
        if (!csdp.exists()) 
            csdp = new File("/");
        props.put("[DLOptions]csdpPath", csdp.getAbsolutePath());
    }
    /**
     * Initialise checkBox default value
     */
    public void initCheckBoxDefault() {
        props.put("[checkBox]flag", "false");
    }

}