package de.uka.ilkd.key.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import de.uka.ilkd.key.java.SourceElement;



public class SingleRenamingTable extends RenamingTable{

    SourceElement oldVar,newVar;
    LinkedList ll= new LinkedList();

    public SingleRenamingTable(SourceElement oldVar, SourceElement newVar){
	this.oldVar = oldVar;
	this.newVar = newVar;
	ll.add(oldVar);
    }

    public SourceElement  getRenaming(SourceElement se){
	if (se.equals(oldVar)) return newVar;
	return null;
    }

    public Iterator getRenamingIterator(){
	return ll.listIterator(0);
    }
    
    public String toString(){
	return ("SingleRenamingTable: "+oldVar+" -> "+newVar);
    }
    
    public HashMap getHashMap(){
        HashMap hm = new HashMap();
        hm.put(oldVar,newVar);
        return hm;
    }

}
