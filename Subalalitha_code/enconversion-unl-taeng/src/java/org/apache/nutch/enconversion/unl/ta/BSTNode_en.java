//package english_unl;

package org.apache.nutch.enconversion.unl.ta;
public class BSTNode_en {

	public int el;
	public String lexeme;
	public String headword;
	public String constraint;

	BSTNode_en next;
	BSTNode_en left, right;

	public BSTNode_en() {
		next = left = right = null;
	}

	public BSTNode_en(int el, String lex, String CL) {
		this.el = el;
		lexeme = lex;
		// headword = hw;
		constraint = CL;
	}

	public BSTNode_en getNext() {
		return next;
	}

	public void setNext(BSTNode_en nxt) {
		next = nxt;
	}
}
