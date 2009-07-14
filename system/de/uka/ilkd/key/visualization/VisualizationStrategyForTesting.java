// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2009 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
package de.uka.ilkd.key.visualization;


import java.util.LinkedList;

import de.uka.ilkd.key.java.*;
import de.uka.ilkd.key.java.reference.IExecutionContext;
import de.uka.ilkd.key.java.statement.MethodFrame;
import de.uka.ilkd.key.logic.*;
import de.uka.ilkd.key.logic.ProgramPrefix;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.rule.*;
import de.uka.ilkd.key.rule.inst.ContextInstantiationEntry;
import de.uka.ilkd.key.gui.Main;
import de.uka.ilkd.key.gui.notification.events.*;

/**A visualization strategy computes execution traces. This is a useful
 * feature for test generation. In order to generate a test for a modality
 * in a node we use this strategy to compute a trace back to the beginning 
 * of the proof and determine the formula from which we extract the 
 * test code and the post condition. 
 * Compared to the parent class, this class creates traces that include 
 * one additional trace element at the end of the trace. This applies 
 * even in cases where symbolic execution has not taken place yet and
 * the trace generated by the parent class would be empty.
 * @author gladisch*/
public class VisualizationStrategyForTesting extends
        SimpleVisualizationStrategy {

    /**A visualization strategy computes execution traces. This is a useful
     * feature for test generation. In order to generate a test for a modality
     * in a node we use this strategy to compute a trace back to the beginning 
     * of the proof and determine the formula from which we extract the 
     * test code and the post condition. 
     * @author gladisch */
    public VisualizationStrategyForTesting(Services serv){
        super(serv);
        //DEBUG =true;
    }
    
    /**Computes the execution trace for the Java block occurrence occ in the
     * sequent of Node n. Compared to the overwritten method of the parent
     * class this method creates traces that include one additional trace element
     * even in cases where symbolic execution has not taken place yet.
     * @author gladisch
     */
    protected ExecutionTraceModel extractExecutionTrace(Node node, Occ occ,
            Integer type) {              
        TraceElement firstTraceElement = null;
        TraceElement lastTraceElement = TraceElement.END;        
        TraceElement lastSource = null;
        ContextTraceElement lastExecuted = TraceElement.END;
        boolean warningOccured=false;
        
        print("------------- Creating Trace ");
        print("");
        
        TraceElement te=null;
        long rating = 0;
        
        Node currentNode = node;
        Occ currentJB    = occ;
        while (!currentNode.root()) {

            Occ result = new Occ(false, -1, -1);
            /**Usually this variable tells whether to include an occurence of a modal operator
             * in the trace or not. This overwritten method also includes an occurence 
             * to the trace if the variable inTrace is falls and the variable te is null.
             * Note that even if inTrace is false the object reference by result is and
             * must be updated by occInParent. */
            boolean inTrace = occInParent(currentNode, currentJB, result);

            currentJB = result;

            if (result.cfm == -1) {
                break;
            }
            currentNode = currentNode.parent();
            if (inTrace) {
                //This branch is as originally implemented in SimpleVisualizationStrategy.extractExecutionTrace
                print("ACTIVE STATEMENT IN EXECUTION TRACE (inTrace=true)");
                final PosTacletApp pta = (PosTacletApp) currentNode.getAppliedRuleApp();                
                final ContextInstantiationEntry cie = 
                    pta.instantiations().getContextInstantiation();
                final SourceElement actStatement = getActStatement(currentNode);
                if (isContextStatement(actStatement)) {
                    rating++;
                    if (!isParentContextTE(actStatement)) {
                        te = new ContextTraceElement(actStatement, currentNode,
                                lastTraceElement, lastExecuted,
                                getExecutionContext(cie.contextProgram()));
                    } else {
                        te = new ParentContextTraceElement(actStatement,
                                currentNode, lastTraceElement, lastExecuted,
                                null, getExecutionContext(cie.contextProgram()));
                    }
                    // sets contextStatement
                    final MethodFrame frame = getMethodFrame(cie.contextProgram());
                    
                    if (frame != null && frame.getProgramMethod() != null) {
                        final StatementBlock methodBody = 
                            frame.getProgramMethod().getBody();                    
                        if (methodBody != null) {
                            StatementByPositionWalker w = new StatementByPositionWalker(
                                    methodBody, actStatement.getPositionInfo());
                            w.start();
                            if (w.getResult() != null) {
                                ((ContextTraceElement) te).
                                    setContextStatement(w.getResult());
                            }
                        }
                    }
                    
                    if (lastSource == null) {
                        lastSource = te;
                    }
                } else {
                    rating = rating<1?1:rating;//This is a modification wrt. the overwritten method of the parent class
                    te = new TraceElement(actStatement, currentNode, 
                             lastTraceElement, lastExecuted,
                             getExecutionContext(cie.contextProgram()));
                }
                if (firstTraceElement == null) {
                    firstTraceElement = te;
                }

                lastTraceElement = te;

                if (te instanceof ContextTraceElement) {
                    lastExecuted = (ContextTraceElement) te;
                }
            }else if(te==null){//&& false
                /** @author Christoph Gladisch chrisg 
                 * The purpose of this branch is to include
                 * the last occurence of a "modal operator" in a proof
                 * (when regarding the natrual order of program execution).
                 * In this way a TraceElement is created even
                 * if no symbolic execution takes place. 
                 * This is desired for test case generation. 
                 * At this point no TraceElement has been created yet and
                 * inTrace is and must be false at this point.
                 * 
                 * An alternative way could be to overwrite
                 * indexAfterAntecSuccTacletapplication. See the comments
                 * in this method.
                 */
                print("VisStrategyForTesting: Trying to add first node:"+node.serialNr());
                IExecutionContext ec = null;
                SourceElement actSt = null;
                if(occ!=null && occ.jbt!=null){
                    print("VisStrategyForTesting: occ.jbt = "+occ.jbt);
                    /*According to my understanding the expression occ.jbt.javaBlock().program() 
                     * should be sufficient to determine the program element in focus of the 
                     * occurrence occ, but it isn't. occ.jbt.javaBlock().program() may be null.
                     * Therefore findFirstJavaProgramElement tries to find the first best javablock
                     * with a program element. 'Hope this is correct.
                    */
                     JavaProgramElement contextProgram = findFirstJavaProgramElement(occ.jbt);
                     actSt = determineFirstAndActiveStatement(contextProgram);
                         if(actSt==null){
                             warningOccured = true; //this prevents from generating multiple warnings by the loop.
                             if(contextProgram!=null){
                                 actSt = contextProgram;
                                 String cpStr = contextProgram.toString();
                                 cpStr = cpStr.length()>200?cpStr.substring(0, 170)+" ...":cpStr;
                                 Main.getInstance().notify(new GeneralFailureEvent(
                                     "Warning: ExecutionTrace extraction from node "+node.serialNr()+" and JavaBlock "+occ+" (See VisualizationStrategyForTesting.java)." +
                                     "\n Could not determine the active statement in:\n"+cpStr));
                             }else{
                                 String jbtStr = occ.jbt.toString();
                                 jbtStr = jbtStr.length()>200?jbtStr.substring(0,170)+" ...":jbtStr;
                                 Main.getInstance().notify(new GeneralFailureEvent(
                                         "Warning: ExecutionTrace extraction from node "+node.serialNr()+" and JavaBlock "+occ+" (See VisualizationStrategyForTesting.java)." +
                                         "\n Term has no JavaBlock:\n"+jbtStr));
                             }
                         }
                         rating = rating<1?1:rating;//This has influence, e.g., on UnitTestBuilder.createTestForNodes()
                        
                     te = new UnexecutedTraceElement(actSt,contextProgram, occ.ant, node,
                                                 lastTraceElement, lastExecuted, ec);
                      if (firstTraceElement == null) {
                         firstTraceElement = te;
                      }

                      lastTraceElement = te;

                      if (te instanceof ContextTraceElement) {
                          /** This is copy&pasted from the previous branch.
                           * This assignement is important if we modify the code
                           * and create a ContextTraceElement above. Thus this
                           * assignment protects against a bug.
                           */
                          lastExecuted = (ContextTraceElement) te;
                      }
                }else if(!warningOccured){//occ==null || occ.jbt==null
                    warningOccured = true; //this prevents from generating multiple warnings by the loop.
                    Main.getInstance().notify(new GeneralFailureEvent(
                            "Warning: There are problems in extracting an execution trace from node "+node.serialNr() +".\n" +
                            (occ==null?"No JavaBlock occurrence selected (occ==null)":
                                (occ.jbt==null?"JavaBlock of JavaBlock occurrence cannot be determined (occ.jbt==null) for "+occ:""))+
                            "\nSee VisualizationStrategyForTesting.java"));
                }

            }
            print("---------");
        }

        if (lastTraceElement != TraceElement.END) {
            //This is modified with respect to the overwritten method of the parent class
            return new ExecutionTraceModelForTesting(lastTraceElement, firstTraceElement,
                    (ContextTraceElement) lastSource, rating, currentNode,
                    node, type);
        }
        return null;
    }

    /**Recursive extension of Term.javaBlock().program(). This method
     * performs depth first order search on the syntax tree of the term t
     * until it finds a JavaBlock that is not "empty". Some terms may have
     * a JavaBlock but with no root JavaProgramElement. In this
     * case the search continues. 
     * */
    private JavaProgramElement findFirstJavaProgramElement(Term t){
        if(t!=null){
            JavaProgramElement jpe=null;
            JavaBlock jb=t.javaBlock();
            if(jb!=null){
                jpe = jb.program();
            }
            if(jpe!=null){
                return jpe;
            }else{
                for(int i=0;i<t.arity();i++){
                    JavaProgramElement jpe2=findFirstJavaProgramElement(t.sub(i));
                    if(jpe2!=null)
                        return jpe2;
                }
            }
        }
        return null;
    }
    
    /**Computes the first active statement in the given ProgramElement. 
     * In contrast to SimpleVisualisationStrategy.getActStatement() which
     * makes indirectly use of NodeInfo.determineFirstAndActiveStatement(), this
     * method (that has been derived from NodeInfo.determineFirstAndActiveStatement())
     * does not look at the active statement that has actually been choosen
     * by a symbolic execution rule. Instead, this method computes the 
     * *potential* active statement for the given JavaBlock.
     * @author gladisch */
    private SourceElement determineFirstAndActiveStatement(ProgramElement pe) {
        SourceElement activeStatement=null;
        if(pe==null){
            return null;
        }

        if (pe != null) {
            activeStatement = pe.getFirstElement();
            while ((activeStatement instanceof ProgramPrefix)
                    && !(activeStatement instanceof StatementBlock)) {
                activeStatement = activeStatement.getFirstElement();
            }
        }
        return activeStatement;
    }

   
}
