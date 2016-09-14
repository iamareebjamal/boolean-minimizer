package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Utils {

	private static List<Character> oper = new ArrayList<Character>();

	static {
		oper.add('+');
		oper.add('*');
		oper.add('\'');
	}

	private static int rank(char op) {
		// the bigger the number, the higher the rank
		switch (op) {
		case '+':
			return 1;
		case '*':
			return 2;
		case '\'':
			return 3;
		default:
			return 0; // '('
		}
	}

	private static boolean isOperator(char c) {
		return oper.contains(c);
	}

	private static boolean isOperand(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	public static List<Character> infixToPostfix(String s) {
		Stack<Character> operators = new Stack<Character>();
		List<Character> postfix = new LinkedList<Character>();

		for (char c : s.toCharArray()) {

			if (c == ' ' || c == '\t') {
				continue;
			}

			if (c == '(') {
				operators.push('(');
			} else if (c == ')') {
				while (operators.peek() != '(') {
					postfix.add(operators.pop());
				}
				operators.pop(); // popping "("
			} else if (isOperator(c)) { // operator
				while (!operators.isEmpty()
						&& rank(c) <= rank(operators.peek())) {
					postfix.add(operators.pop());
				}
				operators.push(c);
			} else {
				postfix.add(c);
			}

		}

		while (!operators.isEmpty()) {
			postfix.add(operators.pop());
		}

		return postfix;
	}

	private static List<Boolean> getRow(int num, int bits) {
		List<Boolean> row = new ArrayList<Boolean>();

		String binaryString = Integer.toBinaryString(num);
		while (binaryString.length() < bits) {
			binaryString = "0" + binaryString;
		}

		for (char c : binaryString.toCharArray()) {
			if (c == '0') {
				row.add(Boolean.FALSE);
			} else {
				row.add(Boolean.TRUE);
			}
		}

		return row;
	}

	public static List<List<Boolean>> getTable(int var) {
		List<List<Boolean>> table = new ArrayList<>();
		int max = (int) Math.pow(2, var);

		for (int i = 0; i < max; i++) {
			table.add(getRow(i, var));
		}

		return table;
	}
	
	public static List<List<Boolean>> getGrayCode(int n) {
        List<Integer> ret = new LinkedList<>();
        ret.add(0);
        for (int i = 0; i < n; i++) {
            int size = ret.size();
            for (int j = size - 1; j >= 0; j--)
                ret.add(ret.get(j) + size);
        }

        List<List<Boolean>> list = new LinkedList<>();
        for(int i : ret){
            list.add(getRow(i, n));
        }
        return list;
    }

	private static int getDecimal(List<Boolean> row) {
		int high = (int) Math.pow(2, row.size() - 2);
		int sum = 0;
		for (int i = 0; i < row.size() - 1; i++) {
			int temp = 0;
			if (row.get(i))
				temp = 1;

			sum += high * temp;
			high /= 2;
		}
		return sum;
	}

	public static List<Integer> getMinTerms(List<List<Boolean>> table) {
		List<Integer> minTerms = new ArrayList<Integer>();
		for (List<Boolean> row : table) {
			if (row.get(row.size() - 1))
				minTerms.add(getDecimal(row));
		}
		return minTerms;
	}

	public static boolean evaluatePostfix(List<Character> variables, List<Boolean> set,
			List<Character> postfix) {

		Stack<Boolean> operands = new Stack<Boolean>();
		for (Character c : postfix) {
			if (isOperand(c)) {
				operands.add(set.get(variables.indexOf(c)));
				continue;
			}

			switch (c) {
			case '\'':
				operands.push(!operands.pop());
				break;
			case '+':
				operands.push(operands.pop() | operands.pop());
				break;
			case '*':
				operands.push(operands.pop() & operands.pop());
				break;
			}
		}

		return operands.pop();
	}

	/*
	 * public int calculate(String s) { return
	 * evaluatePostfix(infixToPostfix(s)); }
	 */
}