package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Simplify {

	private List<Character> variables;
	private Map<List<Boolean>, Boolean> table;
	
	private String message;

	public static Simplify withVariables(List<Character> variables) {
		Simplify simplify = new Simplify();
		simplify.variables = variables;
		return simplify;
	}

	public Simplify ofTable(Map<List<Boolean>, Boolean> table) {
		this.table = table;
		return this;
	}

	public String getExpression() {
		if (variables == null || table == null)
			return null;
		generateMinTerms();
		compare();
		
		return getMinimised();
	}
	
	private String getMinimised(){
		removeDuplicates();
		StringBuilder sb = new StringBuilder();
		for (List<Integer> minterm : minterms) {
			for (int i = 0; i < minterm.size(); i++) {
				if (minterm.get(i) == -1)
					continue;

				sb.append(variables.get(i));
				if (minterm.get(i) == 0)
					sb.append('\'');
			}
			sb.append('+');
		}

		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			if (sb.length() == 0)
				sb.append(1);
		} else {
			sb.append(0);
		}
		
		return sb.toString();
	}

	private List<List<Integer>> minterms;

	private void generateMinTerms() {
		minterms = new ArrayList<>();
		for (List<Boolean> minterm : table.keySet()) {
			if (table.get(minterm))
				minterms.add(toIntList(minterm));
			
		}
	}

	private void compare() {
		List<Integer> tempSave = new ArrayList<>();
		List<List<Integer>> saver = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(getMinimised()+"\n");
		
		for (int i = 0; i < variables.size() && minterms.size() > 1; i++) {
			List<Boolean> compared = new ArrayList<>(Collections.nCopies(minterms.size(), false));

			for (int j = 0; j < minterms.size() - 1; j++) {
				for (int k = j + 1; k < minterms.size(); k++) {
					tempSave.clear();
					for (int l = 0; l < variables.size(); l++) {
						if (minterms.get(j).get(l) == minterms.get(k).get(l))
							tempSave.add(l);
					}
					if (tempSave.size() == variables.size() - 1)
						saveValue(tempSave, saver, compared, j, k);

				}
			}
			addOther(saver, compared);
			
			if (saver.size() > 0) {
				minterms.clear();
				minterms.addAll(saver);
			}
			saver.clear();
			
			sb.append(getMinimised()+"\n");
		}
		
		message = sb.toString();

	}

	private void saveValue(List<Integer> tempSave, List<List<Integer>> saver,
			List<Boolean> compared, int start, int end) {

		if (tempSave.size() == variables.size() - 1) {
			saver.add(new ArrayList<Integer>());
			for (int i = 0; i < minterms.get(start).size(); i++) {
				if (i == tempSave.size())
					tempSave.add(-1);
				else if (i != tempSave.get(i))
					tempSave.add(i, -1);
			}

			for (int i = 0; i < tempSave.size(); i++) {
				if (tempSave.get(i) == -1)
					saver.get(saver.size() - 1).add(-1);
				else
					saver.get(saver.size() - 1).add(minterms.get(start).get(i));
			}
			compared.set(start, true);
			compared.set(end, true);
		}
	}

	private void addOther(List<List<Integer>> saver, List<Boolean> compared) {
		for (int i = 0; i < minterms.size(); i++) {
			if (compared.size() > 0 && !compared.get(i)) {
				saver.add(new ArrayList<Integer>());
				for (int j = 0; j < minterms.get(i).size(); j++)
					saver.get(saver.size()-1).add(minterms.get(i).get(j));
			}
		}
	}

	private void removeDuplicates() {
		for (int i = 0; i < minterms.size(); i++)
			for (int j = i + 1; j < minterms.size(); j++)
				if (minterms.get(i).equals(minterms.get(j)))
					minterms.remove(j--);
	}

	private List<Integer> toIntList(List<Boolean> list) {
		List<Integer> intList = new ArrayList<>();
		for (boolean bool : list)
			intList.add(bool ? 1 : 0);
		return intList;
	}
	
	@Override
	public String toString() {
		return message;
	}

}
