import java.util.*; 
  
class SortStack 
{ 
    // This function return the sorted stack 
    public static Stack<Hero> sort_stack(Stack<Hero>  
                                             input) 
    { 
        Stack<Hero> tmpStack = new Stack<Hero>(); 
        while(!input.isEmpty()) 
        { 
            // pop out the first element 
            Hero tmp = input.pop(); 
          
            // while temporary stack is not empty and 
            // top of stack is greater than temp 
            while(!tmpStack.isEmpty() && tmpStack.peek().compareTo(tmp) > 0) 
            { 
                // pop from temporary stack and  
                // push it to the input stack 
                input.push(tmpStack.pop()); 
            } 
              
            // push temp in tempory of stack 
            tmpStack.push(tmp); 
        } 
        return tmpStack; 
    } 
}