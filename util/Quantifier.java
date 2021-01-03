package util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class Quantifier extends Proposition {
	
	protected List<Proposition> cache;
	public final int bound;
	
	public Optional<Integer> witness = Optional.empty();
	
	protected static int depth;
	protected static boolean isComputing = false; //This is an ugly solution, preferable would likely be to search this down the prop tree
	
	public Quantifier(Proposition over, int bound) {
		cache = new ArrayList<Proposition>(1);
		cache.add(over);
		this.bound = bound;
	}
	
	@Override
	public Collection<Proposition> getChildren() {
		return cache;
	}
	
	public boolean tentativeEval(int depth) {
		Quantifier.depth = depth;
		Quantifier.isComputing = true;
		boolean res = this.compute();
		Quantifier.isComputing = false;
		return res;
	}
	
	protected static void instantiate(Element e, int id, BigInteger value) {
		if(e instanceof Term.Var) {
			Term.Var e2 = (Term.Var) e;
			if(e2.id == id) {
				e2.quantify(value);
			}
		}
		e.getChildren().forEach(child->instantiate(child, id, value));
	}
	
	protected static void uninstantiate(Element e, int id) {
		if(e instanceof Term.Var) {
			Term.Var e2 = (Term.Var) e;
			if(e2.id == id) {
				e2.forget();
			}
		}
		e.getChildren().forEach(child->uninstantiate(child, id));
	}
	
	public static final class Universal extends Quantifier {
		
		public Universal(Proposition p, int bound) {
			super(p, bound);
		}

		@Override
		public boolean compute() {
			if(!Quantifier.isComputing) {
				throw new IllegalStateException("Need to call tentative compute");
			}
			witness = Optional.empty();
			for(int i = 0; i < Quantifier.depth; ++i) {
				Quantifier.instantiate(this, bound, BigInteger.valueOf(i));
				if(!cache.get(0).compute()) {
					witness = Optional.of(i);
					Quantifier.uninstantiate(this, bound);
					return false;
				}
			}
			Quantifier.uninstantiate(this, bound);
			return true;
		}
		
		public String toString() {
			return "forall x"+bound + ", " + cache.get(0);  
		}
		
	}
	
	public static final class Existential extends Quantifier {

		public Existential(Proposition over, int bound) {
			super(over, bound);
		}

		@Override
		public boolean compute() {
			if(!Quantifier.isComputing) {
				throw new IllegalStateException("Need to call tentative compute");
			}
			witness = Optional.empty();
			for(int i = 0; i < Quantifier.depth; ++i) {
				Quantifier.instantiate(this, bound, BigInteger.valueOf(i));
				if(cache.get(0).compute()) {
					witness = Optional.of(i);
					Quantifier.uninstantiate(this, bound);
					return true;
				}
			}
			Quantifier.uninstantiate(this, bound);
			return false;
		}
		
		public String toString() {
			return "exists x"+bound + ", " + cache.get(0);  
		}
		
	}
	
}
