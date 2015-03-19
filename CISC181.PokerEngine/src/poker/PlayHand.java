package poker;

import java.util.ArrayList;
import java.util.Collections;

public class PlayHand {

	public static void main(String[] args) {



		for (int gCount = 0; gCount <= 0; gCount++) {
			ArrayList<Hand> Hands = new ArrayList<Hand>();
			Deck d = new Deck(1);
			
			for (int hCnt = 0; hCnt <= 1; hCnt++) {
				Hand h = new Hand(d);
				Collections.sort(h.getCards(),Card.CardRank);
				for(Card c : h.getCards()){
					System.out.println(c.toString());
				}
				System.out.println("");
				h.EvalHand();
				Hands.add(h);
			}
			Collections.sort(Hands, Hand.HandRank);
			for(Hand h : Hands){
				for(Card c : h.getCards()){
					System.out.println(c.toString());
				}
				System.out.println("");
			}
			System.out
					.print("Hand Strength: " + Hands.get(0).getHandStrength());
			System.out.print(" Hi Hand: " + Hands.get(0).getHighPairStrength());
			System.out.print(" Lo Hand: " + Hands.get(0).getLowPairStrength());
			System.out.print(" Kicker: " + Hands.get(0).getKicker());

			System.out.print(" beats ");

			System.out.print(" Hand Strength: "
					+ Hands.get(1).getHandStrength());
			System.out.print(" Hi Hand: " + Hands.get(1).getHighPairStrength());
			System.out.print(" Lo Hand: " + Hands.get(1).getLowPairStrength());
			System.out.print(" Kicker: " + Hands.get(1).getKicker());

			System.out.print("\n");
			System.out.print("Winning hand: ");
			for(Card c : Hands.get(0).getCards()){
				System.out.print(c.toString()+';');
			}
		}

	}

}
