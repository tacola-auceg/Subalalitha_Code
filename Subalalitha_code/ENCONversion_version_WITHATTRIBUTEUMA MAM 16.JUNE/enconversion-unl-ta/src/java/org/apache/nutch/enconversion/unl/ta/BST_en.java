package org.apache.nutch.enconversion.unl.ta;

//package org.apache.nutch.enconversion.unl.ta;
import java.io.*;

/**
 *Creating a Binary Search Tree for the Dictionary.
 */
public class BST_en {
	public BSTNode_en root = null;
	PrintStream concept;
	int ConceptCount;
	public String tamil_word = null;
	boolean uwflag;
	String tamilword;
	String multiword;

	public BST_en() {
		try {
			// concept = new PrintStream(new FileOutputStream(new
			// File("dict_out.txt"),true));
			ConceptCount = 0;
		} catch (Exception e) {
		}
	}

	public void clear() {
		root = null;
	}

	/**
	 *@return boolean value is true if the root of the tree is empty otherwise
	 *         false.
	 */
	public boolean isEmpty() {
		return root == null;
	}

	// traversing BST

	/**
	 *The Binary Search Tree is arranged in the Inorder fashion.
	 */
	public void inorder() {
		inorder(root);
	}

	/**
	 *Visiting the BST nodes
	 * 
	 * @param p
	 *            element in the node of the BST Tree.
	 */
	protected void visit(BSTNode_en p) {
		/*
		 * concept.print(p.el + " "); concept.print(p.lexeme + " ");
		 * concept.print(p.headword+ " "); concept.println(p.constraint + " ");
		 * concept.println("i am insssssssssssssssss");
		 */
		if (p.getNext() != null) {
			visit(p.getNext());
		}

	}

	/**
	 *Inorder Operation is performed.
	 */
	protected void inorder(BSTNode_en p) {
		if (p != null) {
			inorder(p.left);
			visit(p);
			inorder(p.right);
		}
	}

	public String find_tamil_word(BSTNode_en p, String UNL) {
		String tamilword1 = null;

		tamilword1 = p.lexeme;

		/*
		 * else if(p.getNext() != null) { find_tamil_word(p.getNext(),UNL); }
		 */
		return tamilword1;
	}

	public String retrive_tamilword(String UNLword) {
		uwflag = false;
		String tamil = retrive_tamilword(root, UNLword);
		return tamil;
	}

	public String retrive_tamilword(BSTNode_en p, String UNL) {

		int flag = 0;

		if (p != null) {

			retrive_tamilword(p.left, UNL);
			if ((p.headword + "(" + p.constraint + ")").equals(UNL)) {
				// //System.out.println("eeeeeeeeeeeeeeqqqqqqqqqqqqqqqqq");
				// //System.out.println("unnnnnnnnlllll"+p.headword);
				// //System.out.println("the passsssss"+UNL);
				// //System.out.println("the taaaaaaaam"+p.lexeme);
				// tamil_word= find_tamil_word(p,UNL);
				tamil_word = p.lexeme;
				flag = 1;
				uwflag = true;
				// break;
			}
			// //System.out.println("the unl"+UNL);
			// //System.out.println("the lex"+p.lexeme);
			// //System.out.println("the head"+p.headword);
			// //System.out.println("the constraint"+p.constraint);
			if (flag != 1)
				retrive_tamilword(p.right, UNL);
			// //System.out.println("the tamilllllllll"+tamil_word);
		}
		// //System.out.println("the tamilllllllll"+p.lexeme);
		return tamil_word;

	}

	public boolean get_uwflag() {
		return uwflag;
	}

	public String retrive_tamilmultiword(String UNLword) {

		String tamil = retrive_tamilmultiword(root, UNLword);
		return tamil;
	}

	public String retrive_tamilmultiword(BSTNode_en p, String UNL) {

		if (p != null) {
			int flag = 0;
			retrive_tamilmultiword(p.left, UNL);
			// //System.out.println(p.headword+ "  "+ UNL);
			if ((p.headword).equals(UNL)) {
				// //System.out.println("went inside" );
				multiword = p.lexeme;
				// //System.out.println("MultiWord "+ multiword);
				flag = 1;

			}
			if (flag != 1)
				retrive_tamilmultiword(p.right, UNL);
		}

		return multiword;

	}

	/**
	 *To search for a string.
	 * 
	 * @param el
	 *            searching for an element.
	 *@return root node in the tree.
	 */

	public BSTNode_en search(int el) {
		return search(root, el);
	}

	public BSTNode_en search(BSTNode_en p, int el) {
		// //System.out.println("Hashcode "+p.el);

		if (p == null)
			return null;
		else if (el < (p.el))
			return search(p.left, el);
		else if (el > (p.el))
			return search(p.right, el);
		else
			return p;

	}

	public BSTNode_en search_in_next(int el) {
		BSTNode_en bn1 = new BSTNode_en();
		BSTNode_en bn2 = new BSTNode_en();
		bn1 = search(el);
		if ((bn1 != null) && bn1.el != el) {
			bn2 = bn1.next;
			while (bn2 != null) {
				if (bn2.next.el == el) {
					break;
				}
				bn2 = bn2.getNext();
			}
			return bn2;
		} else
			return bn1;

	}

	/**
	 *to insert new string. to find a place for inserting new node.
	 * 
	 * @param el
	 *            element to be searched, lex lexeme. hw headword. CL Constraint
	 *            list.
	 */

	public void insert(int el, String lex, String CL) {
		BSTNode_en p = root, prev = null;
		while (p != null) {
			// find a place for inserting new node;
			prev = p;
			if (el < (p.el)) {
				p = p.left;
			}

			else if (el > (p.el)) {
				p = p.right;

			} else {
				break;
			}

		}
		if (root == null) // tree is empty;
			root = new BSTNode_en(el, lex, CL);
		else if (el < (prev.el))
			prev.left = new BSTNode_en(el, lex, CL);
		else if (el > (prev.el))
			prev.right = new BSTNode_en(el, lex, CL);
		else
			prev.next = new BSTNode_en(el, lex, CL);
		ConceptCount++;
	}

	/**
	 *to know the size of the concepts.
	 * 
	 * @return returns the no. of concepts in the given sentence.
	 */
	public int Conceptsize() {
		return ConceptCount;
	}

	/*
	 * public BST get_root() { return root; }
	 */

	/*
	 * public static void main(String args[]) { BST b =new BST(); }
	 */
}
