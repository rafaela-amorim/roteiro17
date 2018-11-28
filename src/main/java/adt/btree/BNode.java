package adt.btree;

import java.util.Collections;
import java.util.LinkedList;

public class BNode<T extends Comparable<T>> {
	protected LinkedList<T> elements; 			//PODERIA TRABALHAR COM ARRAY TAMBEM
	protected LinkedList<BNode<T>> children; 	//PODERIA TRABALHAR COM ARRAY TAMBEM
	protected BNode<T> parent;
	protected int order;
	
	public BNode(int order){
		this.order = order;
		this.elements = new LinkedList<T>();	
		this.children = new LinkedList<BNode<T>>();
	}
	@Override
	public String toString() {
		return this.elements.toString();
	}

	@Override
	public boolean equals(Object obj) {
		boolean resp = false;
		if(obj != null){
			if(obj instanceof BNode){
				if(this.size() == ((BNode<T>) obj).size()){
					resp = true;
					int i = 0;
					while(i<this.size() && resp){
						resp = resp && this.getElementAt(i).equals(((BNode<T>) obj).getElementAt(i));
						i++;
					}
				}
			}
		}
		return resp;
	}
	public boolean isEmpty(){
		return this.size() == 0;
	}
	public int size(){
		return this.elements.size();
	}
	public boolean isLeaf(){
		return this.children.size() == 0;
	}
	public boolean isFull(){
		return this.size()== order - 1;
	}
	public void addElement(T element){
		this.elements.add(element);
		Collections.sort(elements);
	}
	public void removeElement(T element){
		this.elements.remove(element);
	}
	public void removeElement(int position){
		this.elements.remove(position);
	}
	public void addChild(int position, BNode<T> child){
		this.children.add(position, child);
		child.parent = this;
	}
	public void removeChild(BNode<T> child){
		this.children.remove(child);
	}
	public int indexOfChild(BNode<T> child){
		return this.children.indexOf(child);
	}
	public T getElementAt(int index){
		return this.elements.get(index);
	}
	
	private void saveElements(T average, BNode<T> bigger, BNode<T> smaller) {
		int index = 0;

		while (index < getElements().size()) {
			if (average.compareTo(getElementAt(index)) < 0) {
				bigger.addElement(getElementAt(index));
			}

			if (average.compareTo(getElementAt(index)) > 0) {
				smaller.addElement(getElementAt(index));
			}

			index++;
		}
	}

	private void reorganizer(LinkedList<BNode<T>> children, BNode<T> node, int first, int last) {
		int pos;
		int index = first;

		while (index < last) {
			pos = position(node.getElements(), children.get(index).getElements().get(0));
			node.addChild(pos, children.get(index));
			index++;
		}
	}

	private int position(LinkedList<T> lista, T average) {
		int i = 0;
		while (i < lista.size()) {
			if (lista.get(i).compareTo(average) > 0) {
				return i;
			}

			i++;
		}

		return lista.size();
	}

	protected void split() {
		T average = getElements().get(getElements().size() / 2);
		int pos, left, right;
		BNode<T> bigger = new BNode<T>(getMaxChildren());
		BNode<T> smaller = new BNode<T>(getMaxChildren());
		LinkedList<BNode<T>> children = new LinkedList<BNode<T>>();

		saveElements(average, bigger, smaller);

		if (getParent() == null && !isLeaf()) {
			children = getChildren();

			setElements(new LinkedList<T>());
			addElement(average);
			setChildren(new LinkedList<BNode<T>>());

			addChild(0, smaller);
			addChild(1, bigger);
			reorganizer(children, smaller, 0, smaller.size() + 1);
			reorganizer(children, bigger, bigger.size() + 1, children.size());
		} else if (getParent() == null && isLeaf()) {
			setElements(new LinkedList<T>());
			addElement(average);
			addChild(0, smaller);
			addChild(1, bigger);
		} else if (isLeaf()) {
			BNode<T> node = new BNode<>(getMaxChildren());

			node.getElements().add(average);
			node.parent = getParent();

			smaller.parent = getParent();
			bigger.parent = getParent();

			pos = position(node.getParent().getElements(), average);
			left = pos;
			right = pos + 1;

			getParent().getChildren().set(left, smaller);
			getParent().getChildren().add(right, bigger);

			node.promote();		// aaa promote
		} else {
			BNode<T> node = new BNode<>(getMaxChildren());
			children = getChildren();

			node.getElements().add(average);
			node.parent = getParent();

			smaller.parent = getParent();
			bigger.parent = getParent();

			pos = position(node.getElements(), average);
			left = pos;
			right = pos + 1;

			getParent().getChildren().add(left, smaller);
			getParent().getChildren().add(right, bigger);
		}
	}

	protected void promote() {
		int pos = position(getParent().getElements(), getElementAt(0));
		getParent().getElements().add(pos, getElementAt(0));

		if (getParent().size() > getMaxKeys()) {
			getParent().split();
		}
	}
	
	public LinkedList<T> getElements() {
		return elements;
	}
	public void setElements(LinkedList<T> elements) {
		this.elements = elements;
	}
	public LinkedList<BNode<T>> getChildren() {
		return children;
	}
	public void setChildren(LinkedList<BNode<T>> children) {
		this.children = children;
	}
	public BNode<T> copy(){
		BNode<T> result = new BNode<T>(order);
		result.parent = parent;
		for (int i = 0; i < this.elements.size(); i++) {
			result.addElement(this.elements.get(i));
		}
		for (int i = 0; i < this.children.size(); i++) {
			result.addChild(i,((BNode<T>)this.children.get(i)).copy());
		}
		
		return result;
	}
	public BNode<T> getParent() {
		return parent;
	}
	public void setParent(BNode<T> parent) {
		this.parent = parent;
	}
	public int getMaxKeys() {
		return order - 1;
	}
	public int getMaxChildren() {
		return order;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	
}
