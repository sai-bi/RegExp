package regExp;

import java.util.ArrayList;

public class Fragment {
	public State start;
	public ArrayList<State> out;
	
	public Fragment(){
		start = null;
		out = new ArrayList<State>();
	}
	
	public Fragment(State s, ArrayList<State> out1){
		start = s;
		out = out1;
	}
	
	
}
