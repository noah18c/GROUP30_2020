# GROUP30_2020

## Commenting & readability:

  **Ensure every method has a comment using JavaDoc commenting** 
  
  	/** method to doStuff
  	 *@param foo - int - the number of foos
  	 *@retrun bar - int - the bar level
	 */
     
  **Ensure that complex code pieces such as nested for loops are explained**
  
  	//This loops through all numbers and calls {@code: doStuff()} on each
  	for(int i: numbers)
  	{
  	  doStuff(i);
  	}
  
  **Add extra line comments for further explanation, either above or to the side (lined up)**
    Between the two examples below, what is more readable?        
            
            // Create two more elements
						L[Lpntr] = new String[2];
						L_values[Lpntr] = new Boolean[2];
            
						// Add clause (Vi + Kj + n ^ Vi + Kl + n) 
						L[Lpntr][0] = i + colours[j] + "n";
						L[Lpntr][1] = i + colours[l] + "n";
            
						// Add two new corresponding values to the L_Values 
						L_values[Lpntr][0] = null;
						L_values[Lpntr][1] = null;
            
						// Increment to the next pointer location
						Lpntr++;
  
    or:
              
						L[Lpntr] = new String[2]; // Create two more elements
						L_values[Lpntr] = new Boolean[2];
						L[Lpntr][0] = i + colours[j] + "n";// Add clause (Vi + Kj + n ^ Vi + Kl + n) 
						L[Lpntr][1] = i + colours[l] + "n";
						L_values[Lpntr][0] = null;// Add two new corresponding values to the L_Values 
						L_values[Lpntr][1] = null;
						Lpntr++;	// Increment to the next pointer location
  
  
  
