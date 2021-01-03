package util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class Proposition extends Element {
	
	public abstract boolean compute();
	
	public static final class Eq extends Proposition {

		private final List<Term> terms;
		
		public Eq(Term t1, Term t2) {
			terms = Arrays.asList(t1, t2);
		}
		
		@Override
		public boolean compute() {
			return terms.get(0).compute().equals(terms.get(1).compute());
		}

		@Override
		public Collection<Term> getChildren() {
			return terms;
		}
		
		public String toString() {
			return "(" + terms.get(0) + ")=(" + terms.get(1) + ")";  
		}
		
	}
	
}