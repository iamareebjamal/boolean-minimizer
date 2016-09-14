package parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utils.Utils;

public class Parser {

	private List<Character> variables = new ArrayList<Character>();
	private String expression;

	private List<List<Boolean>> minterms = new ArrayList<List<Boolean>>();
	private Map<List<Boolean>, Boolean> table = new LinkedHashMap<List<Boolean>, Boolean>();

	public Parser() {
	}

	public void setExpression(String expression) {
		this.expression = expression;
		variables.clear();
		minterms.clear();
		table.clear();
		normalizeExpression();
	}

	public String getExpression() {
		return expression;
	}

	private void normalizeExpression() {
		expression = expression.trim();
		expression = expression.replace(" ", "");
		expression = expression.replace(".", "*");
		expression = expression.replace("|", "+");
		expression = expression.replace("&", "*");
		expression = expression.replace("^", "*");

		StringBuilder sb = new StringBuilder(expression);
		char prev = ' ';

		for (int i = 0; i < sb.length(); i++) {

			char c = sb.charAt(i);
			if (isOperand(c)) {
				addVariable(c);

				if (isOperand(prev) || prev == ')' || prev == '\'') {
					sb.insert(i, '*');
				}

			} else if (c == '(' && (prev == ')' || prev == '\'' || isOperand(prev))) {
				sb.insert(i, '*');
			}

			prev = sb.charAt(i);
		}

		expression = sb.toString();
	}

	public void generateTable() {
		minterms = Utils.getTable(variables.size());

		List<Character> postfix = Utils.infixToPostfix(expression);

		for (List<Boolean> minterm : minterms) {
			table.put(minterm,
					Utils.evaluatePostfix(variables, minterm, postfix));
		}
	}

	public void printKMap() {
		// Rows
		int cols = minterms.get(0).size() / 2;
		int rows = minterms.get(0).size() - cols;

		System.out.print("K-Map:\n\n   ");

		List<List<Boolean>> left = Utils.getGrayCode(rows);
		for (List<Boolean> columns : left) {
			for (Boolean bool : columns)
				System.out.print(toInt(bool));
			System.out.print("  ");
		}
		System.out.println();

		for (List<Boolean> row : Utils.getGrayCode(cols)) {
			for (Boolean bool : row) {
				System.out.print(toInt(bool));
			}

			System.out.print("  ");
			for (List<Boolean> iter : left) {
				List<Boolean> key = new ArrayList<Boolean>();
				key.addAll(row);
				key.addAll(iter);

				System.out.print(toInt(table.get(key)));
				for (int i = 0; i <= left.get(0).size(); i++)
					System.out.print(" ");
			}
			System.out.println();
		}

	}

	public void printTable() {
		System.out.println("Truth Table : \n");
		
		for (char c : variables)
			System.out.print(c + " ");

		System.out.println("   F");

		for (List<Boolean> minterm : table.keySet()) {
			for (Boolean bool : minterm)
				print(bool);
			System.out.println("   " + toInt(table.get(minterm)));
		}
	}

	private void print(boolean bool) {
		System.out.print(toInt(bool) + " ");
	}

	private int toInt(boolean bool) {
		int var = 0;
		if (bool)
			var = 1;
		return var;
	}

	private boolean isOperand(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	private void addVariable(char c) {
		if (!variables.contains(c))
			variables.add(c);
	}

	public List<Character> getVariables() {
		return variables;
	}
	
	public Map<List<Boolean>, Boolean> getTruthTable(){
		return table;
	}

}
