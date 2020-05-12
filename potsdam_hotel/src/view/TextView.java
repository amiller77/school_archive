package view;

import model.Model;
import java.util.Scanner;

/**
 * TEXT VIEW
 * view class to handle command-line user interface, validate user input
 * @author Alexander Miller
 *
 */
public class TextView {

	/**
	 * MAIN
	 * create a scanner to gather input, greet user, get hotel size, create model,
	 * 		fetch bookings until user tells you to quit
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		greetUser();
		int size = fetchHotelSize(input);
		Model model = new Model(size);
		// loop until the user tells you to exit [handled in fetchBooking()]
		while (true) {
			int[] booking = fetchBooking(input);
			Integer bookingResult = model.processBooking(booking);
			// if bookingResult is not null, then successful - otherwise failed
			if (bookingResult != null) {
				System.out.println("Booking successful! Your room number: "+bookingResult+".");
			} else {
				System.out.println("I'm sorry, the hotel is fully booked on those dates.");
			}
		}
	}

	/**
	 * GREET USER
	 */
	public static void greetUser() {
		System.out.println("Greetings! Welcome to the Potsdam Hotel booking service.");
		System.out.println("Type 'exit' at any time to quit.");
	}
	
	/**
	 * PROCESS EXIT INSTRUCTION
	 */
	public static void checkForExitInstruction(String instruction) {
		if (instruction.equals("exit")) {
			System.out.println("Goodbye!");
			System.exit(0);
		}
	}
	
	/**
	 * FETCH HOTEL SIZE
	 * fetches hotel size from user and validates it
	 * @return hotel size
	 */
	public static int fetchHotelSize(Scanner input) {
		System.out.println("Please enter hotel size: ");
		String response = input.nextLine().trim();
		checkForExitInstruction(response);
		// if response can't be parsed as an integer, or the integer is invalid, try again
		int size = 0;
		try {
			size = hotelSizeValidator(response);
		} catch (NumberFormatException x) {
			System.out.println("Please provide a positive integer less than or equal to 1000.");
			return fetchHotelSize(input);
		}
		return size;
	}
	
	/**
	 * HOTEL SIZE VALIDATOR
	 * performs the validation for fetchHotelSize()
	 * this is only really its own function so that one can write test cases to check validation,
	 * 		as fetchHotelSize() just loops till it gets a valid response or the user exits
	 * @return integer hotel size
	 */
	public static int hotelSizeValidator(String response) throws NumberFormatException {
		int size = Integer.parseInt(response); // throws NumberFormatException if string cannot be converted
		if (size > 1000 || size <= 0) {
			throw new NumberFormatException();
		}
		return size;
	}
	
	/**
	 * FETCH BOOKING
	 * gets the booking start and end dates, validates they are valid integers
	 * @return the dates of booking, [firstdate, lastdate]
	 */
	public static int[] fetchBooking(Scanner input) {
		String startDatePrompt = "Please enter first day of booking (between 0 and 364): ";
		String endDatePrompt = "Please enter last day of booking (between 0 and 364): ";
		int startDate = fetchBookingHelper(input,startDatePrompt);
		int endDate = fetchBookingHelper(input,endDatePrompt);
		// can't have the end date be lower than the start date - then start over
		if (endDate < startDate) {
			System.out.println("Please provide a first day of booking which precedes the last day.");
			return fetchBooking(input);
		}
		int[] booking = {startDate,endDate};
		return booking;
	}
	
	/**
	 * FETCH BOOKING HELPER
	 * handles repetitive tasks of fetchBooking(), namely:
	 * 		get input, convert to integer, validate integer in [0,364], process exit instruction
	 * @param message to write to console
	 * @return either first day or last day of booking, depending on context of call
	 */
	public static int fetchBookingHelper(Scanner input, String message) {
		System.out.println(message);
		String response = input.nextLine().trim();
		checkForExitInstruction(response);
		int booking=0;
		try {
			validateDate(response);
		} catch (NumberFormatException x) {
			System.out.println("Please provide a non-negative integer less than or equal to 364.");
			return fetchBookingHelper(input,message);
		}
		return booking;
	}
	
	/**
	 * VALIDATE DATE
	 * performs the validation for fetchBookingHelper()
	 * this is only really its own function so that one can write test cases to check validation,
	 * 		as fetchBookingHelper() just loops till it gets a valid response or the user exits
	 */
	public static int validateDate(String response) throws NumberFormatException {
		int booking = Integer.parseInt(response);
		if (booking > 364 || booking < 0) {
			throw new NumberFormatException();
		}
		return booking;
	}
	
	
}
