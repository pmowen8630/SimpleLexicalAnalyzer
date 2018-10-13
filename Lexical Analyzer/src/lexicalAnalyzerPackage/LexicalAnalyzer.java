package lexicalAnalyzerPackage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LexicalAnalyzer {
	
	/* Variables */
	private static int charClass;
	private static char lexeme[] = new char[100];
	private static char nextChar;
	private static int  lexLen;
	private static String  nextToken;

	// character classes
	final static int LETTER = 0;
	final static int DIGIT = 1;
	final static int UNKNOWN = 99;
	final static int EOF = -1;

	private static FileReader file;
	private static BufferedReader reader;
	
	//for formatting the output
	private static String format = "%-40s%s%n";
	private static String line;

	public static void main(String[] args) {
		
		
		System.out.println("Patrick Owen, CSCI4200-DA, Fall 2018, Lexical Analyzer");
		System.out.println("***********************************************************************");

		try{
			file = new FileReader("src/lexicalAnalyzerPackage/lexInput.txt");
			reader = new BufferedReader(file);
			
 
			reader.mark(100); // marks current place to reset back to
			if((line = reader.readLine()) != null)
				System.out.println("Input: " + line);
			reader.reset();	 //resets back to the mark to continue reading
			
			getChar();
			do{
				lex();
			}while (nextToken != "END_OF_FILE");
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// lookup() - sets the appropriate token to the character being evaluated
	private static String lookup(char ch){
		switch (ch){
		case '=':
			addChar();
			nextToken = "ASSIGN_OP";
			break;
		case '(':
			addChar();
			nextToken = "LEFT_PAREN";
			break;
		case ')':
			addChar();
			nextToken = "RIGHT_PAREN";
			break;
		case '+':
			addChar();
			nextToken = "ADD_OP";
			break;
		case '-':
			addChar();
			nextToken = "SUB_OP";
			break;
		case '*':
			addChar();
			nextToken = "MULT_OP";
			break;
		case '/':
			addChar();
			nextToken = "DIV_OP";
			break;
		default:
			addChar();
			nextToken = "END_OF_FILE";
			break;
		}
		
		return nextToken;
	}
	
	// addChar() - adds the character to the lexeme array
	
	private static void addChar(){
		if (lexLen <= 98) {
			    lexeme[lexLen++] = nextChar;
			    lexeme[lexLen] = 0;
			}
		else
			System.out.println("Error - lexeme is too long \n");

	}
	
	// getChar() - reads the next character in the file and determines
	//             if it is a letter, digit, unknown, or the end of the file.
	
	private static void getChar() throws IOException{
		int character;
		if ((character = reader.read()) != EOF) {
			nextChar = (char) character;
			if (Character.isLetter(nextChar))
				charClass = LETTER;
			else if (Character.isDigit(nextChar))
			    charClass = DIGIT;
			else 
				charClass = UNKNOWN;
			   }
		else{
			charClass = EOF;
		}

	}
	
	// getNonBlank() - skips non-blank characters. If the character is '\n' (meaning a new formula) then
	//                 it does some formating, and reads the entire line to show the input
	
	private static void getNonBlank() throws IOException{
			
			while (Character.isWhitespace(nextChar)){
				if (nextChar == '\n'){
					System.out.println();
					System.out.println("***********************************************************************");
					reader.mark(100); // marks current place to reset back to
					if((line = reader.readLine()) != null)
						System.out.println("Input: " + line);
					reader.reset();	 //resets back to the mark to continue reading
				}
				
				getChar();
				
			}
		
	}
	
	private static String lexToString(){
		String result = "";
		for(int i = 0; i < lexeme.length; i++){
			if(lexeme[i] == 0)
				break;
			result+= lexeme[i];
		}
		return result;
		}
	
	// lex() - creates the lexemes character by character
	
	private static void lex() throws IOException{
		lexLen = 0;
		getNonBlank();
		
		switch (charClass){
		
		/*
		 * if the first character is a letter, keep adding the next characters to the 
		 * lexeme while the next characters are letters or digits
		 */
		case LETTER:
			addChar();
			getChar();
			while (charClass == LETTER || charClass == DIGIT){
				addChar();
				getChar();
			}
			nextToken = "IDENT";
			break;
		/*
		 * if the first character is a digit, keep adding the next characters to the 
		 * lexeme while the next characters are digits
		 */
		case  DIGIT:
			addChar();
			getChar();
			while (charClass == DIGIT){
				addChar();
				getChar();
			}
			nextToken = "INT_LIT";
			break;
			
		// if character is not a letter or digit, lookup its token
		case UNKNOWN: 
			lookup(nextChar);
			getChar();
			break;
		case EOF:
			System.out.println("***********************************************************************");
			nextToken = "END_OF_FILE";
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			break;
		}
		
		System.out.format(format, "Next token is: " + nextToken, "Next lexeme is: " + lexToString());
	}
	
	

}
