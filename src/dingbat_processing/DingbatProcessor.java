package dingbat_processing;

import java.util.LinkedList;
import java.util.List;

import data_structs.Dingbat;
import data_structs.Symbol;

/* Processes the dingbat and does some of the sloppy work */

public class DingbatProcessor {
	private Dingbat dingbat;
	private int rows = 25;  //arbitrary row size for dingbat storage
	private int cols = 50;  //arbitrary column size for dingbat storage
	private List<Symbol> symbols = new LinkedList<Symbol>();
	private Symbol[][] dingbatRep = new Symbol[rows][cols];  //dingbat symbol layout
	
	public DingbatProcessor(Dingbat d){
		dingbat = d;
	}
	
	/*go through all symbols as anchor
	 * go through all symbols as child
	 * if the reference is same as anchor ID assign child
	 */
	private void processSymbols(){
		for(Symbol anchor : dingbat.getAllSymbols().values()){  
			for(Symbol child : dingbat.getAllSymbols().values()){
				if(child.getReferenceSymbol() == anchor.getID()){
					anchor.addChildSymbol(child);
				}
			}
			symbols.add(anchor);
		}
	}
	
	//method for assigning coords to symbols based on spatial data 
	//spacing implemented for regular dist on horizontal
	//#needs refining#
	private int[] checkDir(Symbol child, int row, int column){
		int[] nextIndex = {0,0};
		if(child.getLocation().equals("EAST")){
			nextIndex[0] = row; 
			nextIndex[1] = column+1 ;
			if(child.getSpace().equals("regular")){ 
				nextIndex[1]++;
			}
		}
		else if(child.getLocation().equals("WEST")){
			nextIndex[0] = row; 
			nextIndex[1] = column-1 ;
			if(child.getSpace().equals("regular")){
				nextIndex[1]--;
			}
		}
		else if(child.getLocation().equals("SOUTH")){
			nextIndex[0] = row+1; 
			nextIndex[1] = column ;
		}
		else if(child.getLocation().equals("NORTH")){
			nextIndex[0] = row-1; 
			nextIndex[1] = column ;
		}
		else if(child.getLocation().equals("NW") || child.getLocation().equals("WNW") || child.getLocation().equals("NNW")){
			nextIndex[0] = row-1; 
			nextIndex[1] = column-1 ;
		}
		else if(child.getLocation().equals("SW") || child.getLocation().equals("WSW") || child.getLocation().equals("SSW")){
			nextIndex[0] = row+1; 
			nextIndex[1] = column-1 ;
		}
		else if(child.getLocation().equals("NE") || child.getLocation().equals("ENE") || child.getLocation().equals("NNE")){
			nextIndex[0] = row-1; 
			nextIndex[1] = column+1 ;
		}
		else if(child.getLocation().equals("SE") || child.getLocation().equals("ESE") || child.getLocation().equals("SSE")){
			nextIndex[0] = row+1; 
			nextIndex[1] = column+1 ;
		}
	    return nextIndex;

	}
	
	//gives inital coords for the inital anchor
	private int[] startPos(Symbol s){
		int[] startIndex = {0,0};
		String loc = s.getLocation();
		if(loc.equals("CENTRE")){
			startIndex[0] = (rows/2)-1;
			startIndex[1] = (cols/2)-1;
		}
		else if(loc.equals("LEFT")){
			startIndex[0] = (rows/2)-1;
			startIndex[1] = 0;
		}
		else if(loc.equals("RIGHT")){
			startIndex[0] = (rows/2)-1;
			startIndex[1] = cols-1;
		}
		else if(loc.equals("TOP")){
			startIndex[0] = 0;
			startIndex[1] = (cols/2)-1;
		}
		else if(loc.equals("BOTTOM")){
			startIndex[0] = rows-1;
			startIndex[1] = (cols/2)-1;
		}
		return startIndex;
	}
	
	public Symbol[][] sortSymbols(){
		processSymbols();
		Symbol anchor = symbols.get(0); //gets initial anchor from symbols list
		//startPos returns array of coordinates for row and column of the inital anchor
		int row = startPos(anchor)[0];
		int column = startPos(anchor)[1];
		initArray(); //initialise storage array
	    dingbatRep[row][column] = anchor; //place the anchor
	    searchSymbols(anchor, row, column); //search through anchors children
	
		return dingbatRep;
	}

	private void searchSymbols(Symbol s, int row, int column){
	    int parRow = row; //parent Row
	    int parCol = column; //parent Column
		
		if(s.getChildSymbols().size() > 0){  //if not end symbol
			
			for(Symbol child : s.getChildSymbols()){ //for each child of current anchor
				
				int[] direction = checkDir(child, parRow, parCol);  //returns index for child
				int childRow = direction[0]; 
				int childCol = direction[1];
				dingbatRep[childRow][childCol] = child;
				searchSymbols(child, childRow, childCol);
			}
	
		}
	}
	
	private void initArray(){
		Symbol blank = new Symbol();
		blank.setSym(" ");
		for(int i=0; i<rows;i++){
			for(int j=0; j<cols; j++){
				dingbatRep[i][j] = blank;
			}
		}
		
	}
	
}
