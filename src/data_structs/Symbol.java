package data_structs;

import java.util.LinkedList;
import java.util.List;

public class Symbol {
	private int symbolID;
	private int referenceSymbol;
	private String symbol;
	private String location;
	private String rotation = "NORTH";
	private String solidity = "normal";
	private String style = "regular";
	private String space = "small";
	private String size = "regular";
	private List<Symbol> childSymbols = new LinkedList<Symbol>();
	
	public void setID(int id){
		symbolID = id;
	}	
	
	public int getID(){
		return symbolID;
	}
	
	public void setSize(String s){
		this.size = s;
	}	
	
	public String getSize(){
		return size;
	}
	
	public void setSym(String sym){
		this.symbol = sym;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setRotation(String rot) {
		this.rotation = rot;
		
	}
	
	public String getRotation() {
		return rotation;
		
	}
	
	public int getReferenceSymbol() {
		return referenceSymbol;
	}

	public void setReferenceSymbol(int referenceSymbol) {
		this.referenceSymbol = referenceSymbol;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSolidity() {
		return solidity;
	}

	public void setSolidity(String solidity) {
		this.solidity = solidity;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}
	
	public void addChildSymbol(Symbol s){
		childSymbols.add(s);
	}
	
	public List<Symbol> getChildSymbols(){
		return childSymbols;
	}
	
	public String toString() {
		return "Symbol [symbolID=" + symbolID + ", referenceSymbol="
				+ referenceSymbol + ", symbol=" + symbol + ", location="
				+ location + ", rotation=" + rotation + ", solidity="
				+ solidity + ", style=" + style + ", space=" + space 
				+ ", size=" + size + "]";
	}
	
	public void printSymbol(){
		System.out.println(symbol);
	}
	
}
