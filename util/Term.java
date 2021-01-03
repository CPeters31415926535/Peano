package util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class Term extends Element {

	protected List<Term> cache;
	
	public Term(Term... terms) {
		cache = Arrays.asList(terms);
	}
	
	@Override
	public Collection<Term> getChildren() {
		return cache;
	}
	
	protected abstract BigInteger compute();
	
	public static final class Zero extends Term {
		
		@Override
		protected BigInteger compute() {
			return BigInteger.ZERO;
		}
		
		public String toString() {
			return "0";
		}
	}
	
	//Speeds the fuck out of writing big expressions (i.e. 5 instead of S(S(S(S(S(0))))))
	public static final class Exact extends Term {
		
		private BigInteger val;
		
		public Exact(BigInteger val) {
			super();
			this.val = val;
		}
		
		@Override
		public BigInteger compute() {
			return val;
		}
		
		public String toString() {
			return val.toString();
		}
	}
	
	public static final class Succ extends Term {
		
		public Succ(Term t) {
			super(t);
		}
		
		@Override
		protected BigInteger compute() {
			return cache.get(0).compute().add(BigInteger.ONE);
		}
		
		public String toString() {
			return "S(" + cache.get(0) + ")";
		}
	}
	
	public static final class Add extends Term {
		
		public Add(Term t1, Term t2) {
			super(t1, t2);
		}
		
		@Override
		protected BigInteger compute() {
			return cache.get(0).compute().add(cache.get(1).compute());
		}
		
		public String toString() {
			return "(" + cache.get(0) + ")+(" + cache.get(1) + ")";  
		}
	}
	
	public static final class Mul extends Term {
		
		public Mul(Term t1, Term t2) {
			super(t1, t2);
		}
		
		@Override
		protected BigInteger compute() {
			return cache.get(0).compute().multiply(cache.get(1).compute());
		}
		
		public String toString() {
			return "(" + cache.get(0) + ")*(" + cache.get(1) + ")";  
		}
	}
	
	public static final class Var extends Term {
		
		public final int id;
		
		protected Optional<BigInteger> value = Optional.empty();
		
		public Var(int varnum) {
			id = varnum;
		}
		
		public void quantify(BigInteger num) {
			value = Optional.of(num);
		}
		
		public void forget() {
			value = Optional.empty();
		}
		
		@Override
		protected BigInteger compute() {
			if(value.isEmpty()) {
				throw new IllegalStateException("Instantiate Var before evaluating it");
			}
			return value.get();
		}
		
		public String toString() {
			return "x" + id;
		}
	}
}
