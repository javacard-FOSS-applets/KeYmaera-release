package de.uka.ilkd.key.parser.ocl;

import de.uka.ilkd.key.speclang.SLTranslationError;

public class OCLTranslationError extends antlr.RecognitionException {

	String message = "";
	

	public OCLTranslationError(String msg) {
		super(msg);
        this.message = msg;
	}

    public OCLTranslationError(String msg, int line, int col) {
        super(msg, "", line, col);
        this.message = msg;
    }
	
    public String getMessage() {
		return this.message;
	}

    public SLTranslationError getSLTranslationError() {
	SLTranslationError result = new SLTranslationError(message, getLine(), getColumn());
    	result.setStackTrace(getStackTrace());
    	return result;
    }
}
