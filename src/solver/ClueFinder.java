package solver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data_structs.FeatureDescMap;
import data_structs.FeaturesMap;
import data_structs.Symbol;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.dictionary.Dictionary;

public class ClueFinder {
	
	private Symbol[][] dingbat; //dingbat storage
	private Dictionary dict;
	private List<FeaturesMap> words = new ArrayList<FeaturesMap>();
	private StringExtractor extractor = new StringExtractor();
	private StringPatternFinder patternFinder = new StringPatternFinder();
	
	
	public ClueFinder(Symbol[][] clue) {
		dingbat = clue;
	}
	
	//files which hold the synonyms of the features retrieved
	private List<String> setFeatures(String key) throws IOException{
		String nextLine;
		String fileName = "";
		List<String> features = new ArrayList<String>();
		
		
			switch(key.toLowerCase()){
			case "up": fileName = "src/features/up";
					break;
			case "down": fileName = "src/features/down";
			        break;
			case "back": fileName = "src/features/back";
					break;
			case "in" : fileName = "src/features/between";
					break;
			case "below" : fileName = "src/features/below";
					break;
			case "above" : fileName = "src/features/above";
					break;
			case "diagonal" : fileName = "src/features/diagonal";
					break;
			case "" : fileName = "";
	
			}
		
		if(fileName.equals("")){
			return new ArrayList<String>();
		}
		
		FileReader wordFile = new FileReader(fileName);
		BufferedReader myReader = new BufferedReader(wordFile);
		
		while((nextLine = myReader.readLine()) != null){
			features.add(nextLine);
		}
		myReader.close();
		return features;
		
	}
	
	//setup the dictionary
	public void dictionarySetup() {
		try {
			JWNL.initialize(new FileInputStream("src/file_properties.xml"));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dict = Dictionary.getInstance();
		
	}

	public List<FeaturesMap> getWords(){
		return words;
	}
	
	//return all the alternative words
	public List<String> getAlternativeWords(String word) throws JWNLException, IOException{
		List<String> alts = new ArrayList<String>();
		AlternativeWordFinder aw = new AlternativeWordFinder(dict);
		for(String s:aw.getAlternateWords(word)){
			alts.add(s);
		}
	
		return alts;
	}
	
	//puts all the words, and associated features into a FeaturesMap
	public void setWords() throws IOException{
		patternFinder.setDictionary(dict);
		FeatureDescMap validVerts = new FeatureDescMap();
		
		//returns the one word descriptions of the feature "BACK", "UP"
		FeatureDescMap hor = patternFinder.findHorizontalPatterns(extractor.getHorizontal(dingbat));
		FeatureDescMap ver = patternFinder.findVerticalPatterns(extractor.getVertical(dingbat));
		FeatureDescMap diag = patternFinder.findDiagonalPatterns(extractor.getDiagonal(dingbat));
		
		
		//checking number of horizontal words
		//#clean up
		if(hor.size() >= 2){
			validVerts = validVerts(ver);
		}
		else{
			validVerts = ver;
		}
		
		
		if(hor.isEmpty() && ver.isEmpty()){
			for(String word : diag.keySet()){
				FeaturesMap temp = new FeaturesMap();
				temp.put(word, setFeatures(diag.get(word).get(0)));
				words.add(temp);
			}
		}
		else{
			for(String word : hor.keySet()){
				for(String feature : hor.get(word)){
					FeaturesMap temp = new FeaturesMap();
					temp.put(word, setFeatures(feature));
					words.add(temp);
				}
			}
			for(String word : validVerts.keySet()){
				for(String feature : validVerts.get(word)){
					FeaturesMap temp = new FeaturesMap();
					temp.put(word, setFeatures(feature));
					words.add(temp);
				}
				
			}
		}
	}
	
	//only returns vertical words if the word has an effect on the dingbat
	private FeatureDescMap validVerts(FeatureDescMap ver){
		FeatureDescMap verticals = new FeatureDescMap();
		for(String word : ver.keySet()){
			if(!(word.length() == 2)){
				verticals.put(word, ver.get(word));
			}
		}
		return verticals;
		
	}

}
