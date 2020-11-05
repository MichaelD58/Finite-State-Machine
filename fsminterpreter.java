import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Scanner;

public class fsminterpreter {

	public static void main(String[] args) {

		//Four array lists are used to store the four seperate elements found in a transition table.
		ArrayList<String> state = new ArrayList<String>();
		ArrayList<String> input = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<String> nextState = new ArrayList<String>();

		try {
			//A bufferedReader reads in the description from the file declared in the command line
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));

			String line;
			
			//This loop continues until the buffered reader has nothing else to read.
			while ((line = reader.readLine()) != null) {

				//This falls out of the while loop if the line read by the reader is empty
				//which is the case for a few of the stacscheck descriptions.
				if (line.isEmpty()) {
					break;
				}

				//This splits the line read by the reader at the spaces, seperating the four
				//elements that make up a transition table.
				String[] element = line.split(" ");

				//This addds the elements to their specific arrayLists.
				state.add(element[0]);
				input.add(element[1]);
				output.add(element[2]);
				nextState.add(element[3]);
			}

			reader.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		}

		/**
		 * @param validDescription is used to check if the desctiption read in is valid.
		*/
		boolean validDescription = true;

		//This for loop checks every element in the 'nextState' column of the
		//transition table and makes sure that the next state appears in the 
		//'states' column as if it doesn't, then the descsription contains an
		//error.
		
		/**
		 * @param validNextState is used to track if the currently tracked item in the 
		 *        next state' column is valid. 
		*/
		for (int n = 0; n <nextState.size(); n++){
			boolean validNextState = false;
			for (int s = 0; s <state.size(); s++){
				if (nextState.get(n).equals(state.get(s))) {
					validNextState = true;
				}
			}
			//This if statement checks to see if the tracked item in the 'next
			//state' column passed its validation. If not, then the entire
			//description fails its validation.
			if (validNextState == false){
				validDescription = false;
			}
		}
			
		//This for loop checks to see if every state in the 'states' column of
		//the transition table appears at least twice, since if it only appears once
		//then that means there is a missing input in the description. This was
		//put in assuming all descriptions will deal with two items in the
		//inputStream based on the descriptions given by stacscheck. Look at report
		//for more detail on this assumption.
		/**
		 * @param validState is used to track if the currently tracked item in the 
		 *        'state' column is valid. 
		*/
		for (int x = 0; x <state.size(); x++){
			boolean validState = false;
			for (int z = 0; z <state.size(); z++){
				//This if statement checks to see if the state appears again in the
				//state ArrayList, but is not the exact same string. If so, then the
				//boolean assesing the state is set to true.
				if (state.get(x).equals(state.get(z)) && z != x) {
					validState = true;
				}
			}
			//This if statement checks to see if the tracked item in the 
			//'state' column passed its validation. If not, then the entire
			//description fails its validation.
			if (validState == false){
				validDescription = false;
			}
		}
		
		//If the description failed its validation then the specific error
		//message is sent to the user and the execution of the program is
		//stopped.
		if (validDescription == false) {
			System.out.println("Bad description");
			System.exit(0);
		}

		/**
		 * @param currentState is used to track what the current state of the
		 *        FSM is, starting with the inital state; the first state seen.
		 *        seen in the description 
		 * @param inputStream holds all of the inputs read in by the scanner.
		 * @param currentInput is used to select only a single character from the
		 *        inputStream string
		*/        
		
		String currentState = state.get(0);
		String inputStream;
		String currentInput;

		//A new scanner is created that used standard input to read in
		//the inputs. The inputs are then stored in the specific variable.
		Scanner sc = new Scanner(System.in);
		inputStream = sc.next().toString();
		sc.close();

		//The inputs are then split up into the seperate inputs (individual
		//characters) and are stored in an array.
		String[] inputs = inputStream.split("");

		//This for loop goes through every input in the input stream and 
		//deals with them seperately.
		for (String a : inputs) {
			/**
			 * @param eventualState is used to store the nextState that the FSM will
			 *        change to. The states cannot change straight away as the loop
			 *        is needed to finish first. 
			 */
			String eventualState = null;
			currentInput = a;
			//This for loop goes through every item in the 'states' column
			//of the transition table to check if it matches the current state.
			//If so, it then checks to see if that row in the transition table's
			//input matches the current input. If so, the row's output and next 
			//state are acted on. If not, the loop moves onto the next item in
			//the 'states' column.
			for (int i = 0; i <state.size(); i++){
				if (currentState.equals(state.get(i))) {
					if (currentInput.equals(input.get(i))) {
						System.out.print(output.get(i));
						eventualState = nextState.get(i);
					}
				}
			}	
			//If eventualState has been left null then that means the input
			//isn't found on the transition table and thus is classes as a
			//bad input. The bad input message is then sent to the user and
			//execution of the program is stopped.
			if(eventualState != null) {
				//Only here, outside of the nested for loop, is the state changed.
				//This means that the change of state only affects the next and not
				//current input.
				currentState = eventualState;
			}else {
				System.out.println("Bad input");
				System.exit(0);
			}
		}
	}
}
