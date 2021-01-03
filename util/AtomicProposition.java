package util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class AtomicProposition extends Proposition {
	
	protected List<Proposition> cache; 
	
	public AtomicProposition(Proposition... p) {
		cache = Arrays.asList(p);
	}
	
	@Override
	public Collection<? extends Proposition> getChildren() {
		return cache;
	}
	
	
	public static final class Negation extends AtomicProposition {
		public Negation(Proposition p) {
			super(p);
		}
		
		@Override
		public boolean compute() {
			return !cache.get(0).compute();
		}
		
		public String toString() {
			return "!(" + cache.get(0) + ")";  
		}
	}
	
	public static final class And extends AtomicProposition {
		public And(Proposition p1, Proposition p2) {
			super(p1, p2);
		}
		
		@Override
		public boolean compute() {
			return cache.get(0).compute()&&cache.get(1).compute();
		}
		
		public String toString() {
			return "(" + cache.get(0) + ")&&(" + cache.get(1) + ")";  
		}
	}
	
	public static final class Or extends AtomicProposition {
		public Or(Proposition p1, Proposition p2) {
			super(p1, p2);
		}
		
		@Override
		public boolean compute() {
			return cache.get(0).compute()||cache.get(1).compute();
		}
		
		public String toString() {
			return "(" + cache.get(0) + ")||(" + cache.get(1) + ")";  
		}
	}
	
	public static final class Implies extends AtomicProposition {
		public Implies(Proposition p1, Proposition p2) {
			super(p1, p2);
		}
		
		@Override
		public boolean compute() {
			return !cache.get(0).compute()||cache.get(1).compute();
		}
		
		public String toString() {
			return "(" + cache.get(0) + ")->(" + cache.get(1) + ")";  
		}
	}
}
