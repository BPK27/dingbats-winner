package data_structs;

import java.util.Map;
import java.util.TreeMap;

import dingbat_processing.DingbatProcessor;

public class Dingbat {
	
	private DingbatProcessor processor;
	private Map<Integer, Symbol> dingbat = new TreeMap<Integer, Symbol>();
	
	public Dingbat(TreeMap<Integer, Symbol> treeMap){
		dingbat = treeMap;
		processor = new DingbatProcessor(this);
	}
	
	public Map<Integer, Symbol> getAllSymbols(){
		return dingbat;
	}
	
	public Symbol[][] getSortedSymbols(){
		return processor.sortSymbols();
	}
	
	public String printDingbat(){
		StringBuilder clue = new StringBuilder();
		for(Symbol[] l: processor.sortSymbols()){
			 for(Symbol s :  l){
				clue.append(s.getSymbol());
			 }
			 clue.append("\n");
		}
		return clue.toString();
	}
}
