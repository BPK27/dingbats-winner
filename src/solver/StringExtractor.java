package solver;

import java.util.ArrayList;
import java.util.List;

import data_structs.Symbol;

/*
 * The class that does the heavy lifting in regards
 * to the gathering of groups of symbols
 * each method returns a list containg a list of symbols
 * only later is this converted to a string
 */


public class StringExtractor {
	
	public List<List<Symbol>> getHorizontal(Symbol[][] dingbat){
		List<List<Symbol>> clues = new ArrayList<List<Symbol>>();
		for(Symbol[] line: dingbat){
			List<Symbol> temp = new ArrayList<Symbol>();
			 for(Symbol s :  line){ 
				if(!s.getSymbol().equals(" ")){
					temp.add(s);
				}
				else{
					if(!(temp.size() <= 1)){
						List<Symbol> clue = new ArrayList<Symbol>();
						for(Symbol sym:temp){
							clue.add(sym);
						}
						clues.add(clue);
						temp.clear();
					}
				}
			 }
			 
			
		}
		return clues;
	}
	
	public List<List<Symbol>> getVertical(Symbol[][] dingbat){
		List<List<Symbol>> clues = new ArrayList<List<Symbol>>();
		//arbitrary size of the array known and used
		for(int i = 0; i < 50; i++){
			List<Symbol> clue = new ArrayList<Symbol>();
			for(int j = 0; j < 25; j++){
				if(!(dingbat[j][i].getSymbol().equals(" "))){
					clue.add(dingbat[j][i]);
				}
			}
			if(!(clue.size() <= 1)){
				 clues.add(clue);
			 }
		}
		return clues;
	}
	
	public List<List<Symbol>> getDiagonal(Symbol[][] dingbat){
		List<List<Symbol>> clues = new ArrayList<List<Symbol>>();
		//arbitrary size of the array known and used
		for(int i = 0; i < 49; i++){
			List<Symbol> clue = new ArrayList<Symbol>();
			for(int j = 0; j < 24; j++){
				//if its not a " " add it to clue
				//iterate diagonaly either up or down
				//if the size of the list is 1 discard
				if(!(dingbat[j][i].getSymbol().equals(" "))){
					clue.add(dingbat[j][i]);
					if(!dingbat[j+1][i+1].getSymbol().equals(" ")){
						while(!dingbat[++j][++i].getSymbol().equals(" ")){
							clue.add(dingbat[j][i]);
						}
					}
					else if(!dingbat[j-1][i+1].getSymbol().equals(" ")){
						while(!dingbat[--j][++i].getSymbol().equals(" ")){
							clue.add(dingbat[j][i]);
						}
					}
					else{
						clue.clear();
					}
				}
			}
			if(!(clue.size() <= 1)){
				 clues.add(clue);
			 }
		}
		return clues;
	}
}
