package poker;

public class exHand extends Exception {
	Hand hand;
	public exHand(Hand h1){
		this.hand = h1;
	}
}
