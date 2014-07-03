package solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* 
 *  Processes some of the files containing info
 *  for the solver
 *  */

public class SolverHelper {
	
	public List<String> getPhrases() throws IOException{
		String nextLine = "";
		
		List<String> phrases = new ArrayList<String>(); //hold all phrases
		FileReader wordFile = new FileReader("src/solver tools/phrases"); //phrase file
		BufferedReader myReader = new BufferedReader(wordFile);
		
		while((nextLine = myReader.readLine()) != null){
			phrases.add(nextLine);
		}
		myReader.close();
		return phrases;
	}
	
	public List<String> getClues() throws IOException{
		String nextLine = "";
		
		List<String> clues = new ArrayList<String>(); //hold all clues
		FileReader wordFile = new FileReader("src/solver tools/clues"); //clue file
		BufferedReader myReader = new BufferedReader(wordFile);
		
		while((nextLine = myReader.readLine()) != null){
			clues.add(nextLine);
		}
		myReader.close();
		return clues;
	}
	
	public List<String> getSolutions() throws IOException{
		String nextLine = "";
		
		List<String> solutions = new ArrayList<String>(); //hold all solutions
		FileReader wordFile = new FileReader("src/solver tools/solutions"); //solution file
		BufferedReader myReader = new BufferedReader(wordFile);
		
		while((nextLine = myReader.readLine()) != null){
			solutions.add(nextLine);
		}
		myReader.close();
		return solutions;
}

}
