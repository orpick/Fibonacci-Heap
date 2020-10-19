/**
 * 
 * אביטל חיימן
 * avitalhaiman
 * 
 * 
 * אור פיקהולץ
 * orpickholz 
 */

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{

   private HeapNode min, first;
   private int n, numRoots, numMarked;
   public static int numLinks, numCuts;
	
   /**
    * public FibonacciHeap() 
    *  
    *  Class constructor - nullify the variables
    */
   public FibonacciHeap() {
	   this.n = 0;
	   this.numRoots = 0;
	   this.numMarked = 0;
	   this.min = null;
	   this.first = null;
   }
   
   /**
    * public int getNumMarked()
    *  
    *return the number of the marked nodes in the heap
    */
   public int getNumMarked() {
	   	return this.numMarked;
	   }
   
   /**
    * public int getNumRoots()
    *  
    *return the number of the roots exists in the heap
    */
   public int getNumRoots() {
	   	return this.numRoots;
	   }
   
   /**
    * public int getNumNodes()
    *  
    *return the total number of nodes in the heap
    */
   public int getNumNodes() {
   	return this.n;
   }
   
   
   /**
    * public int getFirst()
    *  
    *returns the first tree in the heap, by returning its root
    */
   public HeapNode getFirst() {
	   return this.first;
   }
   
   /**
    *  private HeapNode getMin() 
    * 
    * @return the HeapNode with the minimal key in this heap.
    */
   public HeapNode getMin() {
   	return this.min;
   }
   
   
	/**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return n == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key)
    {   
    	HeapNode newNode = new HeapNode(key);
    	this.n++;
    	if(min == null) {
    		min = newNode;
    		first = min;
    	}
    	else {
	    	if(key < this.min.getKey())
	    		this.min = newNode;
    	}
    	
	    connectNewRoot(newNode);
    	return newNode; 
    }
    
    /**
     * 	private void connectNewRoot(HeapNode node)
     *
     * Given a node - root of an a tree, adding it to the root list of the heap,
     * updating the first pointer to be the new node.
     */
  	private void connectNewRoot(HeapNode node) {
	   if(first == null)
		   this.first = node;
	   else {
		   this.first.getPrev().setNext(node);
		   node.setPrev(this.first.getPrev());
		   node.setNext(this.first);
			this.first.setPrev(node);
			this.first = node;
	   }
		this.numRoots++;
	
}
  	 /**
     * 	private void connectNewChild(HeapNode node)
     *
     * Given two nodes - connecting them to be brothers under the same parent.
     */
   private void connectNewChild(HeapNode newChild,HeapNode oldChild) {
	   newChild.setNext(oldChild);
	   newChild.setPrev(oldChild.getPrev());
	   oldChild.getPrev().setNext(newChild);
	   oldChild.setPrev(newChild);
	   oldChild.getParent().setChild(newChild);

}

/**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */

    public void deleteMin()
    {
    	
    	HeapNode child = this.min.getChild(), curr = child;
    	
    	//Disconnecting children from parent- creating new roots
    	if(child != null){
    		do {
    			curr.setParent(null);
        		curr = curr.getNext();
    	}
    		while(curr.getNext() != child);
    		curr.setParent(null);
    	}
    	
     	
    	if(this.first == min) {
    		if(child != null)
    			this.first = child;
    		else
    			this.first = this.min.getNext();
    	}
    	
    	
    	if(this.numRoots != 1) {
    		
	    	//Connecting between the previous tree to the next one after the deleting
	    	if(child != null) {
	    		this.min.getPrev().setNext(child);
		    	child.setPrev(this.min.getPrev());
		    	curr.setNext(this.min.getNext());
		    	this.min.getNext().setPrev(curr);
	    	}
	    	else {
	    		this.min.getPrev().setNext(this.min.getNext());
	    		this.min.getNext().setPrev(this.min.getPrev());
	    	}
	  
	    	//In case of forest - completing the process of adding new trees
	    	if(curr != null) {
	    		curr.setNext(this.min.getNext());
	    		this.min.getNext().setPrev(curr);
	    	}
    	}
    	
    	
    	//updating the fields
    	 this.numRoots += min.getRank() - 1;
    	 this.n --;
    	
    	 //deleting the node
    	this.min.setNext(null);
    	this.min.setPrev(null);
    	this.min.setChild(null);
    	this.min = null;
    	
    	
    	if(n == 0) {
    		this.first = null;
    		return;
    	}
    	consolidate(this.first);
    }  	
    	
	/**
	 *private void consolidate(HeapNode child)
	 * 
	 * Inserting the trees in the heap into boxes,
	 * and linking trees with the same rank (if needed)
	 */
   private void consolidate(HeapNode node) {
	   int numOfBoxes = (int)Math.ceil((Math.log(n)/Math.log(2))) + 1;
		HeapNode [] boxes = new HeapNode[numOfBoxes];
		HeapNode curr = node, newMin = this.first;
		HeapNode keep = curr.getNext();
		int maxRunning = this.numRoots, j = 0;
		
		do {
			if(curr.getKey() < newMin.getKey())
				newMin = curr;
			
			if(boxes[curr.getRank()] == null) {
				
				boxes[curr.getRank()] = curr;
				curr = keep;
				keep = keep.getNext();
				if(j != maxRunning-1 ) {
					curr.setNext(curr);
					curr.setPrev(curr);
				}
				j++;
			}
			else {
				int currDegBefore=curr.getRank();
				curr.setNext(curr);
				curr.setPrev(curr);
				curr = boxes[curr.getRank()].link(curr); 
				boxes[currDegBefore]= null;
				this.numRoots --;
				numLinks++;
			}
			
		}
		while(j != maxRunning);
		this.min = newMin;
		keep = null;
		boolean flag = false;
		HeapNode last = null;
		
		//updating the new heap
		for(int i = 0; i < numOfBoxes; i++) {
			if(boxes[i] != null){
				if(flag == false) {
					this.first = boxes[i];
					flag = true;
				}
				if(boxes[i].getKey() < this.min.getKey())
					min = boxes[i];
				//first time initialization of keep
				if(keep == null)
					keep = boxes[i];
				else {
					keep.setNext(boxes[i]);
					boxes[i].setPrev(keep);
					last = boxes[i];
					keep = last;
				}
				}
		}
		last = keep;
		this.first.setPrev(last);
		last.setNext(this.first);
				
			}

   	/**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    

    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  
    	  this.n += heap2.getNumNodes();
    	  this.numRoots += heap2.getNumRoots();
    	  this.numMarked += heap2.getNumMarked();
    	  
    	  //creating a chain of roots
    	  HeapNode a = this.first.prev;
    	  HeapNode b = heap2.getFirst();
    	  a.setNext(b);
    	  
    	 this.first.setPrev(b.getPrev());
    	 b.getPrev().setNext(this.first);
    	 b.setPrev(a);
    	  if(heap2.getMin().getKey() < this.min.getKey())		
    		  this.min = heap2.getMin();
    	  
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return n;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    
    {
    	int maxTreeRank = (int)Math.ceil((Math.log(n)/Math.log(2))) + 1;
    	int[] arr = new int[maxTreeRank];
    	HeapNode curr = first;
    	
    	do {
    		arr[curr.getRank()]++;
    		curr = curr.getNext();
    	}
    	while(curr != this.first);
	
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	decreaseKey(x, x.getKey() +1);
    	deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {   
    	x.setKey(x.getKey() - delta);
	    if (this.min.getKey() > x.getKey())
			this.min = x;
    	
	    //x is a root in the heap
    	if(x.getParent() == null)
    		return;
    	HeapNode parent = x.getParent();
 
    	if(x.getKey() < parent.getKey()) {
    		//cutting the node
    		cut(x,parent);
			//connecting x - new root to the heap
    		connectNewRoot(x);
    		if(!parent.isMarked()&& parent.getParent() != null) {
		    			this.numMarked++;
		    			parent.setMark(true);
    		}
    			
    		else
    			if(parent.getParent() != null) //root
    				cascadingCuts(parent, parent.getParent());
    	}
    	
    	
    }	
   
    
    /**
     * private void cut(HeapNode x,HeapNode parent) 
     * 
     * @param x
     * @param parent (x's parent)
     * 
     * cuts x from its tree.
     */
    private void cut(HeapNode x,HeapNode parent) {
    	numCuts++;
    	if(x.isMarked()) {
    		numMarked--;
    		x.setMark(false);
    	}
    	
    	if(x.getNext() == x){
    		parent.setChild(null);
    	}
    	else {
	    	x.getNext().setPrev(x.getPrev());
			x.getPrev().setNext(x.getNext());
			if(parent.getChild() == x)
				parent.setChild(x.getNext());
    	}

		parent.setRank(parent.getRank()-1);
		x.setParent(null);
		
    }
    
    /**
     * private void cascadingCuts(HeapNode y1, HeapNode y2)  
     * 
     * @param y1
     * @param y2 (y1's parent)
     * 
     * cuts y1 from its tree and inserts it to the heap as a separated tree.
     * if after cutting y1, we need to cut off some of its parents as well,
     * we call this method recursively.
     */
   private void cascadingCuts(HeapNode y1, HeapNode y2) {
	   cut(y1,y2);
	   connectNewRoot(y1); //inserting y1 as a separated tree to this heap
	   if(y2.getParent() != null) 
	   {
		   if(!y2.isMarked()) 
		   {
			   y2.setMark(true);
			   numMarked++;
			   
		   }
		   else 
			   cascadingCuts(y2,y2.getParent());
	   }
}

   	/**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return this.numRoots + 2 * this.numMarked;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return numLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which disconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return numCuts;
    }

    /**
   * public static int[] kMin(FibonacciHeap H, int k) 
   *
   * This static function returns the k minimal elements in a binomial tree H.
   * 
   */

   public static int[] kMin(FibonacciHeap H, int k)
   {   
	   //if H is an empty tree of if k doesn't create a long enough array
	   if((k<1)||(H.getMin()==null))
	   {
		   int [] emptyArray = new int[0];
    	   return emptyArray;
	   }
	   
   		//building a dynamic temporary heap 
       FibonacciHeap tmpHeap = new FibonacciHeap();
       //building the array that we will return
       int [] kMinElements = new int[k];
       
       HeapNode minPointer=H.getMin();
       //inserting the first minimum to tmpHeap
       HeapNode newMin=tmpHeap.insert(minPointer.key);   
       //updating newMin's pointer to point at minPointer so we will be able to access it in O(1)
       newMin.pointer=minPointer;
       //inserting the first minimum to the returned array
       kMinElements[0]=minPointer.key;
       tmpHeap.deleteMin();
       
       //after deleting the minimum, going back to H in order insert to tmpHeap minimum's children
        if(minPointer.getChild()==null) // if H's root has no children
        	return kMinElements;
        //otherwise, inserting all of it's children to tmpHeap
        insertChildren(tmpHeap,minPointer);
        
        //repeating the algorithm k-1 more times
        for(int i=1;i<k;i++)
        {
       	 minPointer=tmpHeap.getMin().pointer;
       	 kMinElements[i]=minPointer.key;
       	 tmpHeap.deleteMin();
       	 if(minPointer.getChild()!=null)
       		 insertChildren(tmpHeap,minPointer);
        }
		return kMinElements; 
   }
    /** 
     * private static void insertChildren(FibonacciHeap tmpHeap, HeapNode parent)
     *  
     * @param tmpHeap
     * @param parent
     * 
     * this method inserts all of parent's children to tmpHeap.
     */
   private static void insertChildren(FibonacciHeap tmpHeap, HeapNode parent)
   {
   	HeapNode firstChild=parent.getChild();
   	tmpHeap.insert(firstChild.key).pointer=firstChild;
   	HeapNode child=firstChild.next;
   	while(child!=firstChild)
   	{
   		tmpHeap.insert(child.key).pointer=child;
       	child=child.next;
   	}
   }
    
   /**
    * public class HeapNode
    * 
    * creating a "HeapNode" object. 
    */
    public class HeapNode{
    	private int key, rank;
    	private boolean mark;
    	private HeapNode child, next, prev, parent, pointer;
    	
    	public HeapNode(int key) {
     	   this.key = key;
     	   this.child = null;
     	   this.next = this;
     	   this.prev = this;
     	   this.parent = null;
     	   this.mark = false;
     	   this.pointer=null;
     	}
     	public HeapNode(int key, HeapNode pointer)
     	{
     		this(key);
     		this.pointer=pointer;
     	}

	public int getKey() {
	    return this.key;
      }
  	public int getRank() {
  		return this.rank;
  	}
  	public boolean isMarked() {
  		return this.mark;
  	}
  	public HeapNode getChild() {
  		return this.child;
  	}
  	public HeapNode getPrev() {
  		return this.prev;
  	}
	public HeapNode getNext() {
  		return this.next;
  	}
	public HeapNode getParent() {
  		return this.parent;
  	}
	
	public void setKey(int key) {
		this.key = key;
      }
  	public void setRank(int rank) {
  		this.rank = rank;
  	}
  	public void setMark(boolean mark) {
  		this.mark = mark;
  	}
  	public void setChild(HeapNode node) {
  		this.child = node;
  	}
  	public void setPrev(HeapNode node) {
  		this.prev = node;
  	}
	public void setNext(HeapNode node) {
		this.next = node;
  	}
	public void setParent(HeapNode node) {
		this.parent = node;
  	}
  
	
	/**
	 * private HeapNode link(HeapNode curr)
	 * 
	 * links between two trees with the same rank
	 */
	private HeapNode link(HeapNode curr) {
			if(curr.getKey() > this.getKey()) {
				curr.setParent(this);
				if(this.child == null) {
					this.setChild(curr);
					this.setRank(this.getRank()+1);
					}
				else
				{
					connectNewChild(curr,this.child);
					this.setRank(this.getRank()+1);
				}
				return this;
			}
			
			else{
				this.setParent(curr);
				if(curr.child == null) {
					this.setNext(this);
					this.setPrev(this);
					curr.setChild(this);
					curr.setRank(curr.getRank()+1);
					}
				else
				{
					connectNewChild(this,curr.getChild());
					curr.setRank(curr.getRank()+1);
				}
				return curr;
			}	
	}		
} 	
}