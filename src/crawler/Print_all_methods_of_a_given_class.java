package crawler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class Print_all_methods_of_a_given_class {
	
	  /**
	    * Prints all methods of a class
	    * @param cl a class
	    */
	   public static void print_all_methods_of_a_given_class(String className)
	   {
		  Class cl=null;
		  Method[] methods =null;
		try {
			cl = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			methods = cl.getDeclaredMethods();
		}
	      

	      for (Method m : methods)
	      {
	         Class retType = m.getReturnType();
	         String name = m.getName();

	         System.out.print("   ");
	         // print modifiers, return type and method name
	         String modifiers = Modifier.toString(m.getModifiers());
	         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
	         System.out.print(retType.getName() + " " + name + "(");

	         // print parameter types
	         Class[] paramTypes = m.getParameterTypes();
	         for (int j = 0; j < paramTypes.length; j++)
	         {
	            if (j > 0) System.out.print(", ");
	            System.out.print(paramTypes[j].getName());
	         }
	         System.out.println(");");
	      }
	   }
	
}
