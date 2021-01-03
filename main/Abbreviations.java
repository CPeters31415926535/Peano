package main;

import util.*;

public class Abbreviations {
	private Abbreviations() {}
	public static int free = 0;
	
	//t1 <= t2
	public static Proposition leq(Term t1, Term t2) {
		return new Quantifier.Existential(new Proposition.Eq(new Term.Add(new Term.Var(free), t1), t2), free++);
	}
	
	//t1 < t2
	public static Proposition le(Term t1, Term t2) {
		return leq(new Term.Succ(t1), t2);
	}
	
	//t1 -= t3 (Mod t2) 
	public static Proposition mod(Term t1, Term t2, Term t3) {
		Term n = new Term.Var(free);
		Term bn = new Term.Mul(t2, n);
		Proposition eq = new Proposition.Eq(t1, new Term.Add(bn, t3));
		Proposition le = le(t3, t2);
		return new AtomicProposition.And(eq, le);
	}
	
	//sequence[index]=val, and bound is an upper bound for sequence
	//takes advantage of the chinese remainder theorem
	public static Proposition seq(Term sequence, Term bound, Term index, Term val) {
		return mod(sequence, new Term.Succ(new Term.Mul(bound, new Term.Succ(index))), val);
	}
	
	public static Proposition pow(Term base, Term pow, Term res) {
		Term.Var sequence = new Term.Var(free++);
		Proposition starts1 = seq(sequence, res, new Term.Zero(), new Term.Succ(new Term.Zero()));
		Term.Var bound = new Term.Var(free++);
		Term prev = new Term.Var(free++);
		Term previndex = new Term.Var(free++);
		Proposition ante = seq(sequence, bound, previndex, prev);
		Proposition next = seq(sequence, bound, new Term.Succ(previndex), new Term.Mul(prev, new Term.Succ(new Term.Succ(new Term.Zero()))));
		Proposition valid = le(previndex, pow);
		Proposition induct = new Quantifier.Universal(new Quantifier.Universal(new AtomicProposition.Implies(new AtomicProposition.And(ante, valid), next), free-2), free-1);
		Proposition endsonres = seq(sequence, bound, pow, res);
		Proposition all = new AtomicProposition.And(endsonres, new AtomicProposition.And(starts1, induct));
		return new Quantifier.Universal(new Quantifier.Existential(all, bound.id), sequence.id);
	}
	
	public static Proposition univAll(Proposition inside, int... vars) {
		for(int i = vars.length-1; i>=0; --i) {
			inside = new Quantifier.Universal(inside, vars[i]);
		}
		return inside;
	}
}
