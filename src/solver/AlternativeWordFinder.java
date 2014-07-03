package solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

/*
 * Used to find sound alikes, hypernyms and manipulate morphology of words
 */
public class AlternativeWordFinder {

	private Dictionary d;
	public AlternativeWordFinder(Dictionary dict){
		d = dict;
	}
	
	//returns all alternate forms of word found
	public List<String> getAlternateWords(String word) throws JWNLException, IOException{
		List<String> altWords = new ArrayList<String>();
		altWords.addAll(getSoundAlikes(word));
		altWords.addAll(getHypernyms(word));
		return altWords;
	}
	
	private List<String> getSoundAlikes(String word) throws IOException{
		String nextLine;
		String nextLine1;
		List<String> soundAlike = new ArrayList<String>();
		List<String[]> original = new ArrayList<String[]>();
		String fileName = "src/solver tools/sounds";
		String[] phoneme = {"",""};
		FileReader wordFile = new FileReader(fileName);
		FileReader wordFile1 = new FileReader(fileName);
		BufferedReader myReader = new BufferedReader(wordFile);
		BufferedReader myReader1 = new BufferedReader(wordFile1);
		
		//if the word in the file is equal to the word being search hold the information
		while((nextLine = myReader.readLine()) != null){
			StringTokenizer st = new StringTokenizer(nextLine);
			String lemma = st.nextToken();
			if(lemma.equalsIgnoreCase(word)){
				phoneme[0] = lemma;
				phoneme[1] = st.nextToken();
				original.add(phoneme);  //can have multiple sounds, list of sounds stored
			}
		}
		
		
		
		//if the soundalike is found in the file take the word as alternative
		while((nextLine1 = myReader1.readLine()) != null){
			StringTokenizer st = new StringTokenizer(nextLine1);
			String lemma = st.nextToken();
			String sound = st.nextToken();
			for(String[] s : original){
				if(sound.equals(s[1])){
					soundAlike.add(lemma);
				}
			}
		}
		myReader.close();
		myReader1.close();
		return soundAlike;
	}
	
	private List<String> getHypernyms(String word) throws JWNLException, IOException{
		List<String> alternatives = new ArrayList<String> ();
		IndexWord i = null;
		
		if((i = d.getIndexWord(POS.NOUN, word)) == null){
			if((i = d.getIndexWord(POS.VERB, word)) == null){
				if((i = d.getIndexWord(POS.ADJECTIVE, word)) == null){
					if((i = d.getIndexWord(POS.ADVERB, word)) == null){
						return alternatives; //will be empty
					}
				}
			}
		}
		
		//word is contained in the WordNet dictionary
		//retrieve the hypernyms
		Synset[] synSets = i.getSenses();
	       for (Synset synset : synSets)
	       {
	    	  Pointer[] pointerArr = synset.getPointers(PointerType.HYPERNYM);
	    	  for (Pointer x : pointerArr) {
                  for (Word w:x.getTargetSynset().getWords()){
                          alternatives.add(w.getLemma());;
                  }
	    	  }
	    	  
	          Word[] words = synset.getWords();
	          for (Word w : words)
	          {
	        	  alternatives.add(w.getLemma());
	          }
	       }	
	       
		return alternatives;
	}
	
	//process morpholgy of word and returns root form
	private String wordProcessing(String w) throws IOException{
		int wordLen = w.length();
		
		if(w.endsWith("thes") || w.endsWith("zes") || w.endsWith("ses") || w.endsWith("xes")){
			return w.substring(0, wordLen-2);		//removes the "es" from the word
		}
		else if(w.endsWith("ies") && wordLen > 4){		//some words end with ies, but are too short to have it as suffix
			w = w.substring(0, wordLen-3);				// if long enough to be word, appends a "y"
			w = w.concat("y");
			return w;
		}
		else if(w.endsWith("s")){
			return w.substring(0, wordLen-1);		//removes "s"
		}
		else if(w.endsWith("ing")){
			if(w.substring(0, wordLen-3).matches(".*(.)\\1\\z")){	//removes "ing" then checks for ending in double letter at end of string using lookbehind 1 character
				return w.substring(0, wordLen-4);
			}
			else
				return w.substring(0, wordLen-3);
		}
		else if(w.endsWith("ied")){	//remove "ied", append a "y"
			w = w.substring(0, wordLen-3);
			w = w.concat("y");
			return w;
		}
		else if(w.endsWith("ed")){
			if(w.substring(0, wordLen-2).matches(".*(.)\\1\\z")){	//removes "ed" then checks for ending in double letter
				return w.substring(0, wordLen-3);
			}
			else
				return w.substring(0, wordLen-2);
		}
		else{		//uses the irregular verb list
			Map<String, Vector<String>> map = new TreeMap<String, Vector<String>>();	//uses affect map as it assigns verbs to a root
			map = setWords("src/solver tools/irregular verbs.idx");
			
			
			for(String v: map.keySet()){
				if(map.get(v).contains(w)){ //if a vector in the set contains word return the the key
					return v;
				}
			}
		}
		return w;
	}
	
	//used in processing the irregular verbs
	private Map<String, Vector<String>> setWords(String path) throws IOException{
		Map<String, Vector<String>> temp = new TreeMap<String, Vector<String>>();
		Vector<String> assocWords;		//Vector containing the associated words
		StringTokenizer st;				//tokenizer to split the lines
		String nextLine;
		FileReader wordFile = new FileReader(path);
		BufferedReader myReader = new BufferedReader(wordFile);
		
		while((nextLine = myReader.readLine()) != null){
			st = new StringTokenizer(nextLine, "\t");			//creates new tokenizer on every iteration of while loop
			String head = st.nextToken();						//separates head of list from the associated words
			assocWords = new Vector<String>();					//creates new Vector on each iteration
			while(st.hasMoreElements()){
				assocWords.add(st.nextToken());					//inserts words into vector
			}
			temp.put(head, assocWords);					//head, and associated words go into hashmap together
		}
		myReader.close();
		return temp;
	}	
	
	public String getRootForm(String w) throws IOException{
		
		if(w.length() < 4){
			return w;
		}
		else{
			String word = wordProcessing(w.toLowerCase());
			
			return word;			//returns null if the word root is not present
		}	
	}
	
	
}
