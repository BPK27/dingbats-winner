package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import data_structs.*;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;


/*
 * Takes the symbol groups and trys to form words
 */

public class StringPatternFinder {
	
	private Dictionary dictionary;
	
	private AlternativeWordFinder a = new AlternativeWordFinder(dictionary);
	
	public StringPatternFinder(){
	}
	
	public void setDictionary(Dictionary d){
		dictionary = d;
	}
	
	public FeatureDescMap findVerticalPatterns(List<List<Symbol>> list) throws IOException{
		FeatureDescMap verts = new FeatureDescMap();
		
		for(List<Symbol> symbols: list){
			try {
				String vertical = convertToStrings(symbols);
				if(isWord(vertical)){
					verts.put(vertical, "DOWN");
				}
				if(checkReverse(vertical) != null){
					verts.put(checkReverse(vertical), "UP");
				}
			}catch (JWNLException e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
		}
			
		return verts;
	}
	
	public FeatureDescMap findHorizontalPatterns(List<List<Symbol>> list) throws IOException{
		FeatureDescMap horiz = new FeatureDescMap();
		
		for(List<Symbol> symbols: list){
			try {
				String hor = convertToStrings(symbols);
				if(isWord(hor)){
					horiz.put(hor,checkLayer(symbols));
				}
			
				if(checkReverse(hor) != null){
					horiz.put(checkReverse(hor), "BACK");
					horiz.put(checkReverse(hor), checkLayer(symbols));
				}
				
				if((!isWord(hor)) && (checkReverse(hor) == null)){
					List<String> w = checkMiddle(hor);
					if(!(w.isEmpty())){
						horiz.put(w.get(0), "IN"); //the middle word
						horiz.put(w.get(1), ""); //outer word
					}
				}
			}catch (JWNLException e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
		}
		return horiz;
	}
	
	public FeatureDescMap findDiagonalPatterns(List<List<Symbol>> list) throws IOException{
		FeatureDescMap diag = new FeatureDescMap();
		
		for(List<Symbol> symbols: list){
			try {
				String diagonal = convertToStrings(symbols);
				if(isWord(diagonal)){
					diag.put(diagonal, "DIAGONAL");
				}
				
			}catch (JWNLException e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
		}
		return diag;
	}
	
	private List<String> checkMiddle(String pattern) throws JWNLException, IOException{
		List<String> words = new ArrayList<String>();	
		int i=(pattern.length()/2)-1;; //starting position
		int j;
		
		if(pattern.length()%2 == 0){ //even number of characters
			j= i+2; 
		}
		else{
			j= i+3;
		}
		
		//keep making substrings until word runs out
		for(;i >= 1;i--,j++){
			String middle = pattern.substring(i, j);
			if(isWord(middle)){
				StringBuilder sb = new StringBuilder();
				sb.append(pattern.substring(0, i));
				sb.append(pattern.substring(j));
				words.add(middle);
				words.add(sb.toString());
				if(isWord(sb.toString())){
					break;
				}
				else{
					words.clear();
					continue;
				}
				
			}
			
		}
		return words;
	}
	
	//uses location information of childrne and anchors to decide if it is layerd
	//if so, top or bottom
	private String checkLayer(List<Symbol> list){
		for(Symbol s : list){
			if(s.getLocation().equals("NORTH")){
				return "ABOVE";
			}
			else if(s.getLocation().equals("SOUTH")){
				return "BELOW";
			}
			else{
				for(Symbol child : s.getChildSymbols()){
					if(child.getLocation().equals("NORTH")){
						return "BELOW";
					}
					else if(child.getLocation().equals("SOUTH")){
						return "ABOVE";
					}
				}
			}
		}
		return "";
	}
	
	private boolean isWord(String lemma) throws JWNLException, IOException{
		String word;
	    word = a.getRootForm(lemma);
		
		if(dictionary.getIndexWord(POS.NOUN, word) == null){
			if(dictionary.getIndexWord(POS.VERB, word) == null){
				if(dictionary.getIndexWord(POS.ADJECTIVE, word) == null){
					if(dictionary.getIndexWord(POS.ADVERB, word) == null){
						return false;
					}
					
				}
			}
		}
		return true;
	}
	
	
	private String checkReverse(String word) throws JWNLException, IOException{
		String reverse = new StringBuffer(word).reverse().toString();
		if(isWord(reverse)){
			return reverse;
		}
		
		return null;
		
	}
	
	private String convertToStrings(List<Symbol> list){
		StringBuilder clue = new StringBuilder();
		for(Symbol l : list){
			
			clue.append(l.getSymbol());
		}
		
		
		return clue.toString();
		
	}
}
