// Main File for Testing and Implementation of Compiler

import java.util.Scanner;

public class Compiler {

    public static void main(String args[]) {

        // Instance Variables
        boolean fileRead;
        Scanner input = new Scanner(System.in);
        String fileName;

        // Create Lexer Instance
        Lexer myLexer = new Lexer();

        //! Get File Input From User
        System.out.println("Enter File Input for Testing Lexer");
        fileName = input.next();

        //! Interpret User Input File
        fileRead = myLexer.readFile(fileName);

        //! Run Lexical Analysis on Interpretted results (if possible)
        if(fileRead) {
            myLexer.lexicalAnalysis();
        } else {
            System.out.println("File Failed to be Read - Lexical Anaylsis will not proceed.");
        }

    }
}