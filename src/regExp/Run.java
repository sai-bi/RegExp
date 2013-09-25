package regExp;

import java.util.ArrayList;
import java.util.Stack;

/*
 * a(bc + cd)
 * a * (b*c +  c * d)
 */
public class Run {
	private int listID;
	public static void main(String[] args) {
		String test = "a?.a?.a?.a?.a?.a?.a?.a?.a?.a?.a.a.a.a.a.a.a.a.a.a";
		Run myTest = new Run();
		String postfix = myTest.re2post(test);
		State start = myTest.post2dfa(postfix);
		System.out.println(postfix);
		//String temp = "aaaaaaaaaaaaaa";
		//System.out.println(myTest.match(start, temp));
		
	}
	
	public ArrayList<State> startList(State s, ArrayList<State> l){
		listID++;
		l.clear();
		addState(l,s);
		
		return l;
	}
	
	public void addState(ArrayList<State> l, State s){
		if(s == null || s.lastList == listID)
			return;
		s.lastList = listID;
		
		if(s.tranform == 256){
			addState(l,s.out);
			addState(l,s.out1);
			return;
		}
		l.add(s);
	}
	
	public int isMatch(ArrayList<State> l){
		for(int i = 0;i < l.size();i++){
			if(l.get(i).tranform == 257)
				return 1;
		}
		return 0;
	}
	
	public void step(ArrayList<State> currList, int c, ArrayList<State> nextList){
		listID++;
		nextList.clear();
		
		for(int i = 0;i < currList.size();i++){
			if(currList.get(i).tranform == c){
				addState(nextList, currList.get(i).out);
			}
		}
	}
	
	public int match(State start, String s){
		ArrayList<State> currList = new ArrayList<State>();
		ArrayList<State> nextList = new ArrayList<State>();
		
		
		startList(start, currList);
		
		for(int i = 0;i < s.length();i++){
			step(currList,s.charAt(i),nextList);
			ArrayList<State> temp = currList;
			currList = nextList;
			nextList = temp;
		}
		
		
		return isMatch(currList);
	}
	
	
	
	
	public State post2dfa(String postfix){
		Stack<Fragment> fragStack = new Stack<Fragment>();
		for(int i = 0;i < postfix.length();i++){
			char curr = postfix.charAt(i);
			
			if(curr == '+'){
				Fragment e = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e.start;
				s.out1 = null;
				patch(e,e.out,s);
				
				Fragment temp = new Fragment(e.start,createList(s)); 
				fragStack.push(temp);
				temp.outChange.add(1);
			}
			else if(curr == '*'){
				Fragment e = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e.start;
				s.out1 = null;
				patch(e,e.out,s);
				
				Fragment temp = new Fragment(s,createList(s));
				fragStack.push(temp);
				temp.outChange.add(1);
				
			}
			else if(curr == '?'){
				Fragment e = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e.start;
				s.out1 = null;
				
				Fragment temp = new Fragment(s,appendList(e.out, createList(s)));
				fragStack.push(temp);
				temp.outChange.add(1);
				
				for(int j = 0;j < e.outChange.size();j++){
					temp.outChange.add(e.outChange.get(j));
				}
				temp.outChange.add(1);
			}
			else if(curr == '|'){
				Fragment e2 = fragStack.pop();
				Fragment e1 = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e1.start;
				s.out1 = e2.start;
				
				Fragment temp = new Fragment(s,appendList(e1.out,e2.out)); 
				fragStack.push(temp);
				
				for(int j = 0;j < e1.outChange.size();j++){
					temp.outChange.add(e1.outChange.get(j));
				}
				for(int j = 0;j < e2.outChange.size();j++){
					temp.outChange.add(e2.outChange.get(j));
				}
				
				
				
				
			}
			else if(curr == '.'){
				Fragment e2 = fragStack.pop();
				Fragment e1 = fragStack.pop();
				
				patch(e1,e1.out,e2.start);
					
				Fragment e3 = new Fragment();
				e3.start = e1.start;
				e3.out = e2.out;
				e3.outChange = e2.outChange;
				fragStack.push(e3);
			}
			else{
				State s = new State();
				s.tranform = curr;
				Fragment temp = new Fragment(s,createList(s));
				temp.outChange.add(0);
				fragStack.push(temp);
			}
		
		}
		
		Fragment e = fragStack.pop();
		State matchState = new State();
		matchState.tranform = 257;
		matchState.out = null;
		matchState.out1 = null;
		
		patch(e,e.out,matchState);
		System.out.println(e.start);
		System.out.println(e.start.out);
		return e.start;
	}
	
	
	
	public ArrayList<State> createList(State out1){
		ArrayList<State> temp = new ArrayList<State>();
		temp.add(out1);
		return temp;
	}
	
	public ArrayList<State> appendList(ArrayList<State> list1, ArrayList<State> list2){
		ArrayList<State> temp = new ArrayList<State>();
		
		for(int i = 0;i < list1.size();i++){
			temp.add(list1.get(i));
		}
		
		for(int i = 0;i < list2.size();i++){
			temp.add(list2.get(i));
		}
		
		return temp;
	}
	public void patch(Fragment f, ArrayList<State> list, State s){
		for(int i = 0;i < list.size();i++){
			State temp = list.get(i);
			int flag = f.outChange.get(i);
			if(flag == 1)
				temp.out1 = s;
			else if(flag == 0)
				temp.out = s;
		}
	}
	
	String re2post(String input){
		String postFix = "";
		ArrayList<Character> stack = new ArrayList<Character>();
		
		for(int i = 0;i < input.length();i++){
			char x = input.charAt(i);
			
			
			if(stack.size() == 0 || stack.get(stack.size()-1)== '('){
				if(x == '+' || x == '*' || x == '|' || x == '?' || x=='.' || x=='(' || x==')'){
					stack.add(x);
					continue;
				}
			}
			
			switch(x){
			case '+': case '*': case '|': case '.': case '?':
				char top = stack.get(stack.size()-1);
				if(getPrecedence(x) > getPrecedence(top)){
					stack.add(x);
				}
				else if(getPrecedence(x) == getPrecedence(top)){
					postFix = postFix + top;
					stack.remove(stack.size()-1);
					stack.add(x);
				}
				else{
					stack.remove(stack.size()-1);
					postFix = postFix + top;
					i = i-1;
				}
				break;
			case '(':
				stack.add(x);	
				break;
			case ')':
				while(true){
					char temp = stack.get(stack.size()-1);
					stack.remove(stack.size()-1);
					if(temp == '(')
						break;
					postFix = postFix + temp;
				}
				break;
			default:
				postFix = postFix + x;
				break;
			}
			
			
		}
		
		for(int i = stack.size()-1;i>=0;i--){
			postFix = postFix + stack.get(i);
		}
		
		return postFix;
	}
	
	int getPrecedence(char x){
		if(x == '|')
			return 1;
		else if(x == '.')
			return 2;
		else 
			return 3;
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
