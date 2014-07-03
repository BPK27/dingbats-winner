package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.didion.jwnl.JWNLException;


import data_structs.FeaturesMap;
import data_structs.Symbol;

public class DingbatSolver {
	private Symbol[][] clue;
	private SolverHelper helper = new SolverHelper();
	private ClueFinder finder;
	
	
	public DingbatSolver(Symbol[][] sortedSymbols) {
		clue = sortedSymbols;
	}
	
	public List<String> findPhrase() throws IOException, JWNLException{
		finder = new ClueFinder(clue);
		List<String> phrases = new ArrayList<String>();
		int currentFeatures = 0; //measure similarity to phrase
		
		finder.dictionarySetup();
		finder.setWords();
		
		
		for(String phrase : helper.getPhrases()){
			int tempFeatures = 0;
			for(FeaturesMap s : finder.getWords()){
				for(String key : s.keySet()){
					if(phraseContainsWord(phrase, key)){
						if(phraseContainsFeature(phrase, s.get(key))){
							tempFeatures++;
						}
					}
				}
			}
			//replace or add to list based on the feature measure
			if(currentFeatures < tempFeatures){
				phrases.clear();
				phrases.add(phrase);
				currentFeatures = tempFeatures;
			}
			else if(currentFeatures == tempFeatures){
				phrases.add(phrase);
			}
		}
		
		return phrases;
		
	}
	
	//if any alternatives to the word are contianed in the phrase returns true
	private boolean phraseContainsWord(String phrase, String word) throws JWNLException, IOException{
		for(String alt : finder.getAlternativeWords(word)){
			if(phrase.toLowerCase().contains(alt.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	//if the phrase contains the features associated with the word just passed returns true
	private boolean phraseContainsFeature(String phrase, List<List<String>> list){
		boolean trigger = true;
		boolean temp;
		for(List<String> features : list){
			temp = false;
			if(features.size() == 0){return true;}
			for(String feature : features){
				if(phrase.toLowerCase().contains(feature.toLowerCase())){
					 temp = true;
				}
			}
			if(temp){
				trigger = temp;
			}
			else{
				return false;
			}
		}
		
		return trigger;
	}
	
	
}
