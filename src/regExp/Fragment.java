package regExp;

import java.util.ArrayList;

public class Fragment {
	public State start;
	public ArrayList<State> out;
	public ArrayList<Integer> outChange; //0 stands for change out, 1 stands for change out1;
	public Fragment(){
		start = null;
		out = new ArrayList<State>();
		outChange = new ArrayList<Integer>();
	}
	
	public Fragment(State s, ArrayList<State> out1){
		start = s;
		out = out1;
		outChange = new ArrayList<Integer>();
	}
	
	
}
