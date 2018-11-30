package helpers;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	
	public enum Operator {
		OR, AND, NONE
	}
	
	private String query;
	private List<Object> parameters;
	
	public Filter() {
		this.parameters = new ArrayList<Object>();
		this.query = "";
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public List<Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}
	
	public void addQuery(String query, Object parameter) {
		addQuery(query, parameter, Operator.NONE);
	}
	
	public void addGroupStart(Operator operator) {
		
		if (this.query.length()>0) {
			switch (operator) {
				case OR: 
					this.query += " OR ";
					break;
				case AND: 
					this.query += " AND ";
					break;
				case NONE:
					break;
			}
		}
		
		this.query += "(";
	}
	
	public void addGroupEnd() {
		this.query += ")";
	}
	
	public boolean addQuery(String queryString, Object parameter, Operator operator) {
		
		if (queryString==null || parameter==null || queryString.length()==0) {
			return false;
		}
		if (this.query.length()>0 && !this.query.substring(query.length()-1, query.length()).equals("(")) {
			switch (operator) {
				case OR: 
					this.query += " OR ";
					break;
				case AND: 
					this.query += " AND ";
					break;
				case NONE:
					break;
			}
		}
		
		this.query += queryString;
		this.parameters.add(parameter);
		
		return true;
	}
	
	public Object[] getParametersArray() {
		return this.parameters.toArray(new Object[this.parameters.size()]);
	}
	
	public boolean isValidFilter() {
		
		return (this.query.length()>0 && !this.parameters.isEmpty());
	}
}
