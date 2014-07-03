package solver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import data_structs.Dingbat;

import net.didion.jwnl.JWNLException;
import parser.DingbatParser;


public class Main {

	public static void main(String[] args) throws JWNLException, IOException {
		
		SolverHelper helper = new SolverHelper();
		List<String> solution = helper.getSolutions();
		int i = -1;

		//for each clue in the file
	   for(String clue : helper.getClues()){
		    i++; //next solution
			StringReader stringReader = new StringReader(clue);
			BufferedReader clueReader = new BufferedReader(stringReader);
			DingbatParser parser = new DingbatParser(clueReader); //create parser
			Dingbat dingbatClue = new Dingbat(parser.parse()); //create dingbat
		
			//pass the spatially organised symbols to the solver tool
			DingbatSolver solver = new DingbatSolver(dingbatClue.getSortedSymbols());
			System.out.println("Solving...");
			
			System.out.println(solution.get(i));
			System.out.println(solver.findPhrase());
		}
		
		

	}

}
