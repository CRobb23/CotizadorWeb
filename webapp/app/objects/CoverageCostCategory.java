package objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoverageCostCategory {
	
	public String name;
	public String description;
	
	private List<CoverageCost> costs;
	
	public CoverageCostCategory() {
		this.costs = new ArrayList<CoverageCost>();
	}
	
	public void addCost(CoverageCost cost) {
		this.costs.add(cost);
	}
	
	public List<CoverageCost> getCosts() {
		
		Collections.sort(this.costs, new Comparator<CoverageCost>() {
	        @Override public int compare(CoverageCost c1, CoverageCost c2) {
	        	
	        	Integer orderCompare = c1.coverageOrder.compareTo(c2.coverageOrder);
	        	if (orderCompare == 0) {
	        		return c1.coverageName.compareTo(c2.coverageName);
	        	}
	        	
	            return orderCompare;
	        }
	    });
		
		return this.costs;
	}

}
