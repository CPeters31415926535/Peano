package main;

import util.*;

public class RunPeano {
	
	public static void main(String[] args) {
		Term x = new Term.Var(0);
		Term y = new Term.Var(1);
		Term z = new Term.Var(2);
		Term n = new Term.Var(3);
		Term xn = new Term.Var(4);
		Term yn = new Term.Var(5);
		Term zn = new Term.Var(6);
		Abbreviations.free = 7;
		Proposition xnprop = Abbreviations.pow(x, n, xn);
		Proposition ynprop = Abbreviations.pow(y, n, yn);
		Proposition znprop = Abbreviations.pow(z, n, zn);
		Proposition all = new AtomicProposition.And(xnprop, new AtomicProposition.And(ynprop, znprop));
		Term lhs = new Term.Add(xn, yn);
		Proposition ineq = new AtomicProposition.Negation(new AtomicProposition.Eq(lhs, zn));
		Proposition ngeq3 = Abbreviations.leq(new Term.Succ(new Term.Succ(new Term.Succ(new Term.Zero()))), n);
		Proposition inside = new AtomicProposition.Implies(ngeq3, new AtomicProposition.Implies(all, ineq));
		Proposition FLT = Abbreviations.univAll(inside, 0, 1, 2, 3, 4, 5, 6);
		System.out.println(FLT);
	}
	
	public static Proposition greaterThanEq(Term t1, Term t2, int free) {
		return new Quantifier.Existential(new Proposition.Eq(new Term.Add(t2, new Term.Var(free)), t1), free);
	}
	
	public static Proposition greaterThan(Term t1, Term t2, int free) {
		return RunPeano.greaterThanEq(t1, new Term.Succ(t2), free);
	}
	
	public static Proposition forGreaterThan1(Proposition p, int bound, int free) {
		return new Quantifier.Universal(
				new AtomicProposition.Implies(
						greaterThan(new Term.Var(bound), new Term.Succ(new Term.Zero()), free), p), 
				bound);
	}
	
	public static Proposition isPrime(Term t1, int free1, int free2, int free3, int free4) {
		Term x1 = new Term.Var(free1);
		Term x3 = new Term.Var(free3);
		return forGreaterThan1(
				forGreaterThan1(new AtomicProposition.Negation(new Proposition.Eq(t1, 
						new Term.Mul(x1, x3))),
					free3, free4), 
				free1, free2);
	}
	
	public static Proposition isTwinPrime(Term t1, int free[]) {
		return new AtomicProposition.And(RunPeano.isPrime(t1, free[0], free[1], free[2], free[3]),
				RunPeano.isPrime(new Term.Succ(new Term.Succ(t1)), free[4], free[5], free[6], free[7]));
	}
	
}
