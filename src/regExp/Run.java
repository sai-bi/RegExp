package regExp;

import java.util.ArrayList;
import java.util.Stack;


public class Run {

	public static void main(String[] args) {
		String test = "a.(b.b)+.a";
		Run myTest = new Run();
		System.out.println(myTest.re2post(test));
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
				patch(e.out,s);
				fragStack.push(new Fragment(e.start,createList(s.out1)));
			}
			else if(curr == '*'){
				Fragment e = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e.start;
				s.out1 = null;
				patch(e.out,s);
				fragStack.push(new Fragment(s,createList(s.out1)));
			}
			else if(curr == '?'){
				Fragment e = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e.start;
				s.out1 = null;
				fragStack.push(new Fragment(s,appendList(e.out, createList(s.out1))));
			}
			else if(curr == '|'){
				Fragment e2 = fragStack.pop();
				Fragment e1 = fragStack.pop();
				State s = new State();
				s.tranform = 256;
				s.out = e1.start;
				s.out1 = e2.start;
				fragStack.push(new Fragment(s,appendList(e1.out,e2.out)));
			}
			else if(curr == '.'){
				Fragment e2 = fragStack.pop();
				Fragment e1 = fragStack.pop();
				patch(e1.out,e2.start);
				Fragment e3 = new Fragment();
				e3.start = e1.start;
				e3.out = e2.out;
				fragStack.push(e3);
			}
			else{
				State s = new State();
				s.tranform = curr;
				fragStack.push(new Fragment(s,createList(s.out)));
			}
		
		}
		
		return new State();
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
	public void patch(ArrayList<State> list, State s){
		for(int i = 0;i < list.size();i++){
			State temp = list.get(i);
			temp = s;
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
