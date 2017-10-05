package HW2;

public class AssignmentTwo {
	/*
	 * Starts a Gym thread 
	 * @param args
	 */
	public static void main(String[] args){
		Thread thread = new Thread(new Gym());
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
