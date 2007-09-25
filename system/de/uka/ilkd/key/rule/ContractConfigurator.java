package de.uka.ilkd.key.rule;

import de.uka.ilkd.key.logic.op.ListOfProgramMethod;
import de.uka.ilkd.key.logic.op.Modality;
import de.uka.ilkd.key.proof.mgt.OldOperationContract;
import de.uka.ilkd.key.proof.mgt.SpecificationRepository;
import de.uka.ilkd.key.speclang.ListOfClassInvariant;

public interface ContractConfigurator {

    void setSpecification(SpecificationRepository repos);
    
    void start();
    
    OldOperationContract getMethodContract();
    
    ListOfClassInvariant getPreInvariants();
    
    ListOfClassInvariant getPostInvariants();
    
    void setProgramMethods(ListOfProgramMethod pm);
    
    void setModality(Modality modality);
    
    boolean wasSuccessful();
    
    void clear();
    
    void allowConfiguration(boolean allowConfig);
}
