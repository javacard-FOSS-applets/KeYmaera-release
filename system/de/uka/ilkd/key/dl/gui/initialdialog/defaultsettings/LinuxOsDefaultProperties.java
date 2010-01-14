package de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings;

import java.io.File;

import de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.Suffixes.LinuxSuffixes;

/**
 * The LinuxOsDefaultProperties class creates and instance of a Property Object
 * containing all possible default properties for linux platform.
 * 
 * @author zacho
 * 
 */
public class LinuxOsDefaultProperties extends OsDefaultProperties implements
		IOsDefaultProperties {

	public LinuxOsDefaultProperties() {
		setMathematicaDefaultPath(sp + "usr" + sp + "local" + sp + "Wolfram"
				+ sp + "Mathematica");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getCSDPPathDefault()
	 */
	@Override
	protected String getCSDPPathDefault() {
		File csdp = new File(sp + "user" + sp + "bin" + sp + "csdp");
		if (!csdp.exists())
			csdp = new File(System.getProperty("user.home") + sp + "bin" + "csdp");
		if (!csdp.exists())
			csdp = new File(sp + "usr" + sp + "bin" + sp + "csdp");
		return csdp.getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getHOLLightPath()
	 */
	@Override
	public String getHOLLightPath() {
		String hol = System.getProperty("user.home");
		if (hol == null)
			hol = "/";
		return hol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getHarrisionQEPath()
	 */
	@Override
	public String getHarrisionQEPath() {
		String hol = System.getProperty("user.home");
		if (hol == null)
			hol = "/";
		return hol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getJLinkDefault()
	 */
	@Override
	public String getJLinkDefault() {
		return getMathematicaDefaultPath() + sp + "SystemFiles" + sp + "Links"
				+ sp + "JLink" + sp + "SystemFiles" + sp + "Libraries" + sp
				+ "Linux-x86-64";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getMathKernelDefault()
	 */
	@Override
	public String getMathKernelDefault() {
		return getMathematicaDefaultPath() + sp + "Executables" + sp
				+ "MathKernel";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getOCAMLPath()
	 */
	@Override
	public String getOCAMLPath() {
		File ocaml = new File(sp + "usr" + sp + "bin" + sp + "ocaml");
		if (!ocaml.exists()) {
			ocaml = new File(System.getProperty("user.home") + sp + "bin" + sp
					+ "ocaml");
		}
		if (!ocaml.exists()) {
			ocaml = new File("/usr/bin/ocaml");
		}
		return ocaml.getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getQepCadDefault()
	 */
	@Override
	public String getQepCadDefault() {
		String qpath = System.getenv("qe");
		if (qpath == null) {
			qpath = System.getProperty("user.home");
			if (qpath == null)
				qpath = "/";
		}
		return qpath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getReduceBinaryDefault()
	 */
	@Override
	public String getReduceBinaryDefault() {
		String rpath = System.getProperty("user.home");
		if (rpath == null) {
			rpath = sp + "usr" + sp + "bin" + sp + "reduce";
		} else {
			rpath += sp + "bin" + sp + "reduce";
		}
		return rpath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getSaclibDefault()
	 */
	@Override
	public String getSaclibDefault() {
		String spath = System.getenv("saclib");
		if (spath == null) {
			spath = System.getProperty("user.home");
			if (spath == null)
				spath = "/";
		}
		return spath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uka.ilkd.key.dl.gui.initialdialog.defaultsettings.OsDefaultProperties
	 * #getMathematicaCompletePath(java.lang.String)
	 */
	@Override
	protected String getMathematicaCompletePath(String mathematicaDefaultPath2) {
		return LinuxSuffixes.INSTANCE
				.getMathematicaPath(mathematicaDefaultPath2);
	}
}