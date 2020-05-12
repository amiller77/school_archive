package model;

/**
 * MODEL
 * class that stores a representation of the hotel and handles logic of processing booking requests
 * @author Alexander Miller
 */
public class Model {
	
	// ************* INSTANCE FIELDS *************
	private int size;
	private RoomDataStructure<Integer,Integer>[] vacancies;
	
	/**
	 * CONSTRUCTOR
	 * create vacancy tree of rooms associated with each day, get size of hotel
	 * all rooms start vacant
	 * @param size of hotel
	 */
	public Model(int size) {
		this.size = size;
		vacancies = new RoomDataStructure[365];
		for (int i = 0; i<365; i++) {
			vacancies[i] = new AVLTree<Integer,Integer>();
			for (int k = 0; k < size; k++) {
				vacancies[i].insert(k,k);
			}
		}
	}
	
	/**
	 * PROCESS BOOKING
	 * get tree for first day, iterate over it to see if the rooms are also vacant on other days
	 * preconditions: booking is valid request
	 * @param booking
	 * @return room number if booking successful, null otherwise
	 */
	public Integer processBooking(int[] booking) {
		// get firstDayVacancies in order:
		int firstDay = booking[0];
		int lastDay = booking[1];
		RoomDataStructure<Integer,Integer> firstDayVacancies = vacancies[firstDay];
		Integer[] orderedVacancies = new Integer[firstDayVacancies.getSize()];
		firstDayVacancies.inOrderTraversal(orderedVacancies);
		
		// iterate over the vacancies for the first day
		Integer solution = null;
		for (int i=0; i<firstDayVacancies.getSize(); i++) {
			solution = orderedVacancies[i];
			boolean roomAvailable = true;
			
			// iterate over days in the range, try to find a match for all days
			for (int day=firstDay+1; day<=lastDay; day++) {
				RoomDataStructure<Integer,Integer> dayVacancies = vacancies[day];
				// try to get the same room for that day
				Integer room = dayVacancies.get(solution);
				if (room == null) {
					roomAvailable = false;
					break;
				}
			}
			
			// if room available for all days, then it's a solution 
			if (roomAvailable) {
				// book the room (remove it from list of vacancies for each day)
				bookRoom(firstDay,lastDay,solution);
				return solution;
			}
		}
		// if we check all vacancies for day 1 and didn't find a solution, there isn't one
		return null;
	}
	
	/**
	 * BOOK ROOM
	 * removes the room as a vacancy from the given days, since it is about to be filled
	 * @param startDay
	 * @param endDay
	 * @param solution
	 */
	private void bookRoom(int startDay, int endDay, Integer solution) {
		for (int day=startDay; day<= endDay; day++) {
			vacancies[day].remove(solution);
		}
	}
	
	
}
