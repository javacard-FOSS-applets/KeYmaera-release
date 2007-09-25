package de.uka.ilkd.key.proof.init;

import de.uka.ilkd.key.casetool.ModelMethod;
import de.uka.ilkd.key.cspec.ComputeSpecification;
import de.uka.ilkd.key.java.ArrayOfExpression;
import de.uka.ilkd.key.java.Statement;
import de.uka.ilkd.key.java.StatementBlock;
import de.uka.ilkd.key.java.abstraction.KeYJavaType;
import de.uka.ilkd.key.java.declaration.Modifier;
import de.uka.ilkd.key.java.declaration.ParameterDeclaration;
import de.uka.ilkd.key.java.declaration.VariableSpecification;
import de.uka.ilkd.key.java.expression.literal.NullLiteral;
import de.uka.ilkd.key.java.expression.operator.CopyAssignment;
import de.uka.ilkd.key.java.expression.operator.New;
import de.uka.ilkd.key.java.reference.TypeReference;
import de.uka.ilkd.key.java.statement.Branch;
import de.uka.ilkd.key.java.statement.Catch;
import de.uka.ilkd.key.java.statement.MethodBodyStatement;
import de.uka.ilkd.key.java.statement.Try;
import de.uka.ilkd.key.logic.JavaBlock;
import de.uka.ilkd.key.logic.ProgramElementName;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.TermBuilder;
import de.uka.ilkd.key.logic.op.*;
import de.uka.ilkd.key.proof.mgt.Contract;
import de.uka.ilkd.key.proof.mgt.Contractable;
import de.uka.ilkd.key.proof.mgt.OldOperationContract;
import de.uka.ilkd.key.util.Debug;

public class ComputeSpecificationPONew extends AbstractPO {

    private static final TermBuilder tb = TermBuilder.DF;
    
	private ModelMethod modelMethod;
	private Modality modality;
	
	public ComputeSpecificationPONew(String name, ModelMethod modelMethod) {
		super(name, modelMethod.getContainingClass());

		this.modelMethod = modelMethod;
		this.modality = Op.DIA;
	}

	private JavaBlock buildJavaBlock(ProgramVariable[] formalParVars,
            ProgramMethod programMethod, 
            ProgramVariable selfVar, 
            ProgramVariable resultVar,
            ProgramVariable exceptionVar) {        
		//create method call
		StatementBlock sb;
		if(programMethod == null) {
		//constructor
		assert resultVar != null;
		TypeReference typeRef 
		= javaInfo.createTypeReference(resultVar.getKeYJavaType());
		New n = new New(formalParVars, typeRef, typeRef);
		CopyAssignment copy = new CopyAssignment(resultVar, n);
		sb = new StatementBlock(copy);
		} else {
		MethodBodyStatement call 
		= new MethodBodyStatement(programMethod,
			  selfVar,
			  resultVar,
			  new ArrayOfExpression(formalParVars));
		sb = new StatementBlock(call);
		}
		
		//create variables for try statement
		KeYJavaType eType = javaInfo.getTypeByClassName("java.lang.Throwable");
		TypeReference excTypeRef = javaInfo.createTypeReference(eType);
		ProgramElementName ePEN = new ProgramElementName("e");
		ProgramVariable eVar = new LocationVariable (ePEN, eType);
		
		//create try statement
		CopyAssignment nullStat = new CopyAssignment(exceptionVar, 
		                            NullLiteral.NULL);
		VariableSpecification eSpec = new VariableSpecification(eVar);
		ParameterDeclaration excDecl = new ParameterDeclaration(new Modifier[0],
		                                       excTypeRef,
		                                       eSpec,
		                                       false);
		CopyAssignment assignStat = new CopyAssignment(exceptionVar, eVar);
		Catch catchStat = new Catch(excDecl, new StatementBlock(assignStat));
		Try tryStat = new Try(sb, new Branch[]{catchStat});
		sb = new StatementBlock(new Statement[]{nullStat, tryStat});
		
		//create java block
		JavaBlock result = JavaBlock.createJavaBlock(sb);
		
		return result;
    }

    /**
     * Returns the program method associated with this proof obligation.
     */
    private ProgramMethod getProgramMethod(ListOfProgramVariable paramVars) {
        //get class type
        String className = modelClass.getFullClassName();
        KeYJavaType classType = javaInfo.getTypeByClassName(className); 
        Debug.assertTrue(classType != null);
                
        //get program method
        return javaInfo.getProgramMethod(classType, 
                                         modelMethod.getName(), 
                                         paramVars.toArray(), 
                                         classType);
    }

	public void readProblem(ModStrategy mod) throws ProofInputException {
        //make sure initConfig has been set
        if(initConfig == null) {
            throw new IllegalStateException("InitConfig not set.");
        }

        //prepare variables, program method and container for @pre-functions
        ListOfProgramVariable paramVars = buildParamVars(modelMethod);
        ProgramMethod programMethod = getProgramMethod(paramVars);
        ProgramVariable selfVar = null;
        if(programMethod != null && !programMethod.isStatic()) {
            selfVar = buildSelfVarAsProgVar();
        }
        ProgramVariable resultVar = buildResultVar(modelMethod);
        ProgramVariable exceptionVar = buildExcVar();

        //create formal parameters
        ProgramVariable[] parVars = paramVars.toArray();
        ProgramVariable[] formalParVars = new ProgramVariable[parVars.length];
        for(int i = 0; i < parVars.length; i++) {
            ProgramElementName name 
                    = new ProgramElementName("_" + parVars[i].name());
            formalParVars[i] 
                    = new LocationVariable(name, parVars[i].getKeYJavaType());
            registerInNamespaces(formalParVars[i]);
        }

        
        final JavaBlock methodCallJavaBlock;
		{
		    methodCallJavaBlock = buildJavaBlock(parVars,programMethod,selfVar,resultVar,exceptionVar);
		    assert methodCallJavaBlock != null;
		}

		// register created variables
		registerInNamespaces(selfVar);
        registerInNamespaces(paramVars);
        registerInNamespaces(resultVar);
        registerInNamespaces(exceptionVar);

        final Term resultTerm = ComputeSpecification.
		    createSpecificationComputationTerm(methodCallJavaBlock, 
						       initConfig.progVarNS());
		
		//@todo resultTerm = precondition -> resultTerm (translating away OCL)
		poTerms = new Term[1];
		poTerms[0] = resultTerm;

	}

	public Contractable[] getObjectOfContract() {
		return new Contractable[] { modelMethod };
	}

	public boolean initContract(Contract ct) {
        if(!(ct instanceof OldOperationContract)) {
            return false;
        }
        
        OldOperationContract mct = (OldOperationContract)ct;
        
        if(! (mct.getModelMethod().equals(modelMethod)
                && mct.getModality().equals(modality))) {
            return false;
        }
        
        ct.addCompoundProof(getPO());
        return true;
	}

}
