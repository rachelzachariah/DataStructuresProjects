///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  (name of main application class)
// File:             (name of this class's file)
// Semester:         CS367 Spring 2015
//
// Author:           Rachel Alisha Zachariah
// Email:            rzachariah@wisc.edu
// CS Login:         zachariah
// Lecturer's Name:  Jim Skrentny
//
//////////////////////////// 80 columns wide //////////////////////////////////
@SuppressWarnings("serial")
public class InsufficientCreditException extends Exception {
	public InsufficientCreditException() {
        super();
    }
    
    public InsufficientCreditException(String message) {
        super(message);
    }
}
