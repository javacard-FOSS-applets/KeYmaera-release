package de.uka.ilkd.key.proof;

import java.util.LinkedList;
import java.util.List;

import de.uka.ilkd.key.proof.mgt.ProofStatus;
import de.uka.ilkd.key.util.Debug;

public class CompoundProof extends ProofAggregate {

    private ProofAggregate[] proofs;
    
    CompoundProof(ProofAggregate[] proofs, String name) {
        super(name);
        if (proofs.length<=1) Debug.fail();
        this.proofs=proofs;
    }

    private void addProofsToList(List l) {
        for (int i=0; i<size(); i++) {
            if (proofs[i] instanceof SingleProof) {
                l.add(proofs[i].getFirstProof());
            } else {
                ((CompoundProof)proofs[i]).addProofsToList(l);
            }
        }
    }
    
    public Proof[] getProofs() {
        List l = new LinkedList();
        addProofsToList(l);
        return (Proof[])l.toArray(new Proof[0]);
    }
        
    public int size() {
        return proofs.length;
    }
    
    public ProofAggregate get(int i) {
        return proofs[i];
    }
    
    public void updateProofStatus() {
        proofs[0].updateProofStatus();
        ProofStatus ps = proofs[0].getStatus();
        for (int i=1; i<proofs.length; i++) {
            proofs[i].updateProofStatus();
            ps = ps.combineProofStatus(proofs[i].getStatus());                    
        } 
        proofStatus = ps;
    }
    
    public ProofAggregate[] getChildren() {
        return proofs;
    }


    public boolean equals (Object o) {
        if (!(o instanceof CompoundProof)) return false;
        CompoundProof cmp = (CompoundProof) o;
        for (int i=0; i<cmp.size(); i++) {
            if (!cmp.get(i).equals(get(i))) return false;
        }
        return true;
    }
 
   
    public int hashCode(){
        int result = 17;
        for (int i=0; i < size(); i++){
            result = 37 * result + get(i).hashCode();
        }
        return result;
    }    
}
