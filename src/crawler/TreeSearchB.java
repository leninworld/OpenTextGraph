package crawler;

import java.io.IOException;

// interview question

class NodeC{
	NodeC right, left;
	int d;
	NodeC(int index){
		right=null;
		left=null;
	}
}

public class TreeSearchB {
	 
	NodeC g_r1, g_r2; 
	
	//
	public static void main(String[] args) throws IOException {
		
		
		TreeSearchB instance=new TreeSearchB();
		instance.g_r1=new NodeC(100);
		instance.g_r1.right=new NodeC(110);
		instance.g_r1.left=new NodeC(80);
		
		TreeSearchB instance2=instance;
		
		System.out.println(is_B_Sub_of_A(instance.g_r1,instance2.g_r2));
		//CASE 2 (needs debug)
		instance2=new TreeSearchB();
		instance2.g_r2=new NodeC(120);
		instance2.g_r1=new NodeC(120);
		System.out.println(is_B_Sub_of_A(instance.g_r1,instance2.g_r2));
		
	}
	
	static boolean is_B_Sub_of_A(NodeC a, NodeC b){
		
		try{
			if(a==null) return false;
			if(b==null) return true;
			
			// cehck on root
			if(isIdentical(a, b))
				return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// recursviely try to check on left and right tree
		return isIdentical(a.left, b) || isIdentical(a.right, b);
		
	}

	static boolean isIdentical(NodeC r1, NodeC r2){
		try{
			
			
			//one is null handled, also handles both null.. null cant be compared with another null
			if(r1==null || 
					r2==null)
				return false;

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return (r1.d== r2.d && isIdentical(r1.left, r2.left)
				&& isIdentical(r1.right, r2.right));
	}
	
}
