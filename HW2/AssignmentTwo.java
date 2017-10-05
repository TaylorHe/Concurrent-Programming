package HW2;

public class AssignmentTwo {
	/*
	 * Starts a Gym thread 
	 * @param args
	 */
	public static void main(String[] args){
		Thread t = new Thread(new Gym());
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
