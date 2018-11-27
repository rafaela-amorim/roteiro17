package adt.btree;

import java.util.ArrayList;

public class BTreeImpl<T extends Comparable<T>> implements BTree<T> {

	protected BNode<T> root;
	protected int order;

	public BTreeImpl(int order) {
		this.order = order;
		this.root = new BNode<T>(order);
	}

	@Override
	public BNode<T> getRoot() {
		return this.root;
	}

	@Override
	public boolean isEmpty() {
		return this.root.isEmpty();
	}

	@Override
	public int height() {
		return height(this.root);
	}

	private int height(BNode<T> node) {
		int height = -1;
		
		while (!node.isLeaf()) {
			height++;
			node = node.getChildren().getFirst();
		}
		
		if (!isEmpty()) {
			height++;
		}
		
		return height;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BNode<T>[] depthLeftOrder() {
		ArrayList<BNode<T>> list = new ArrayList<>();
		depthLeftOrder(root, list);
		BNode<T>[] answer = list.toArray(new BNode[list.size()]);
		return answer;
	}
	
	private void depthLeftOrder(BNode<T> node, ArrayList<BNode<T>> list) {
		if (!node.isEmpty()) {
			list.add(node);
			
			for (BNode<T> children: node.getChildren()) {
				depthLeftOrder(children, list);
			}
		}
	}

	@Override
	public int size() {
		return size(this.root);
	}
	
	private int size(BNode<T> node) {
		int elements = 0;
		
		if (!node.isEmpty()) {
			elements = node.size();
			
			for (BNode<T> children: node.getChildren()) {
				elements += size(children);
			}
		}
		
		return elements;
	}
	
	@Override
	public BNodePosition<T> search(T element) {
		return search(root, element);
	}
	
	private BNodePosition<T> search(BNode<T> node ,T element) {
		BNodePosition<T> answer = new BNodePosition<>(null, -1);
		
		if (element != null) {
			int i = 0;
			
			while (i <= node.size() && element.compareTo(node.getElementAt(i)) > 0) {
				i++;
			}
			
			if (i <= node.size() && element.compareTo(node.getElementAt(i)) == 0) {
				answer.node = node.getChildren().get(i);
				answer.position = i;
			} else if (!node.isLeaf()) {
				answer = search(node.getChildren().get(i), element);
			}
		}
		
		return answer;
	}

	@Override
	public void insert(T element) {
		if (element != null) {
			insert(root, element);
		}
	}
	
	private void insert(BNode<T> node, T element) {
		if (node.isLeaf()) {
			node.addElement(element);
			if (node.getElements().size() > node.getMaxKeys()) {
				split(node);
			}
		} else {
			int i = 0;
			
			while (i < node.size() && node.getElementAt(i).compareTo(element) < 0) {
				i++;
			}
			
			insert(node.getChildren().get(i), element);
		}
	}
	
	private void split(BNode<T> node) {
		node.split();
	}

	private void promote(BNode<T> node) {
		node.promote();
	}

	// NAO PRECISA IMPLEMENTAR OS METODOS ABAIXO
	@Override
	public BNode<T> maximum(BNode<T> node) {
		// NAO PRECISA IMPLEMENTAR
		throw new UnsupportedOperationException("Not Implemented yet!");
	}

	@Override
	public BNode<T> minimum(BNode<T> node) {
		// NAO PRECISA IMPLEMENTAR
		throw new UnsupportedOperationException("Not Implemented yet!");
	}

	@Override
	public void remove(T element) {
		// NAO PRECISA IMPLEMENTAR
		throw new UnsupportedOperationException("Not Implemented yet!");
	}

}
