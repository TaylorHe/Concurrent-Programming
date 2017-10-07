package HW2;

//Taylor He
//I pledge my honor that I have abided by the Stevens Honor System.

public class Assignment2 {
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
