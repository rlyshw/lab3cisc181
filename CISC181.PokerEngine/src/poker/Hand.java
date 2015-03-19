package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand {
	private ArrayList<Card> CardsInHand;

	private int HandStrength;
	private int HiHand;
	private int LoHand;
	private int Kicker;
	private boolean bScored = false;

	private boolean Flush;
	private boolean Straight;
	private boolean Ace;
	private boolean Joker;

	public Hand(Deck d) {
		ArrayList<Card> Import = new ArrayList<Card>();
		for (int x = 0; x < 5; x++) {
			Import.add(d.drawFromDeck());
		}
		CardsInHand = Import;
	}
	


	public ArrayList<Card> getCards() {
		return CardsInHand;
	}

	public int getHandStrength() {
		return HandStrength;
	}

	public int getKicker() {
		return Kicker;
	}

	public int getHighPairStrength() {
		return HiHand;
	}

	public int getLowPairStrength() {
		return LoHand;
	}

	public boolean getAce() {
		return Ace;
	}
	
	public static Hand PickBestHand(ArrayList<Hand> Hands) {
		ArrayList<Hand> evaledHands = new ArrayList<Hand>();
		for(Hand h : Hands){
			h.EvalHand();
			evaledHands.add(h);
		}
		Collections.sort(evaledHands, Hand.HandRank);
		// we do need a way to handle ties but this interferes with my joker method(throws exHand)
		//if(evaledHands.get(0)==evaledHands.get(1)) throw new exHand();
		return evaledHands.get(0);
	}

	public static Hand EvalHand(ArrayList<Card> SeededHand){		
		Deck d = new Deck();
		Hand h = new Hand(d);
		h.CardsInHand = SeededHand;
		h.EvalHand();
		
		return h;
	}	
	
	public void EvalHand(){
		// Evaluates if the hand is a flush and/or straight then figures out
		// the hand's strength attributes
		// Sort the cards!
		Collections.sort(CardsInHand, Card.CardRank);
		// Joker Evaluation
		if(CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == eRank.JOKER){
			// so this assigns the first position to every card and makes cardsinhand the highest possible value. doesnt seeem to work
			Joker = true;
			Deck plainDeck = new Deck();
			Hand h = new Hand(plainDeck);
			ArrayList<Hand> testingList = new ArrayList<Hand>();
			h.CardsInHand = CardsInHand;
			for(Card c : plainDeck.getCards()){
				h.CardsInHand.set(0, c);
				testingList.add(h);
			}
			CardsInHand = PickBestHand(testingList).CardsInHand;
		}
		// Ace Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == eRank.ACE) {
			Ace = true;
		}

		// Flush Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getSuit()) {
			Flush = true;
		} else {
			Flush = false;
		}

		// Straight Evaluation
		if (Ace) {
			// Looks for Ace, King, Queen, Jack, 10
			if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == eRank.KING
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.QUEEN
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.JACK
					&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN) {
				Straight = true;
				// Looks for Ace, 2, 3, 4, 5
			} else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TWO
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.THREE
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.FOUR
					&& CardsInHand.get(eCardNo.SecondCard.getCardNo())
							.getRank() == eRank.FIVE) {
				Straight = true;
			} else {
				Straight = false;
			}
			// Looks for straight without Ace
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
				.getRank() == CardsInHand.get(eCardNo.SecondCard.getCardNo())
				.getRank().getRank() + 1
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank().getRank() + 2
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()
						.getRank() + 3
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank().getRank() + 4) {
			Straight = true;
		} else {
			Straight = false;
		}

		// Evaluates the hand type
		if (Straight == true
				&& Flush == true
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN
				&& Ace) {
			ScoreHand(eHandStrength.RoyalFlush, 0, 0, 0);
		}

		// Straight Flush
		else if (Straight == true && Flush == true) {
			ScoreHand(eHandStrength.StraightFlush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0);
		}
		// Four of a Kind

		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank());
		}

		else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank());
		}

		// Full House
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0);
		}

		else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0);
		}

		// Flush
		else if (Flush) {
			ScoreHand(eHandStrength.Flush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0);
		}

		// Straight
		else if (Straight) {
			ScoreHand(eHandStrength.Straight,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, 0);
		}

		// Three of a Kind
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank());
		}

		else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank());
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank());
		}

		// Two Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank())) {
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank());
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank());
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank());
		}

		// Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank());
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank());
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank());
		} else if (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank());
		}

		else {
			ScoreHand(eHandStrength.HighCard,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank());
		}
	}

	private void ScoreHand(eHandStrength hST, int HiHand, int LoHand, int Kicker) {
		this.HandStrength = hST.getHandStrength();
		this.HiHand = HiHand;
		this.LoHand = LoHand;
		this.Kicker = Kicker;
		this.bScored = true;

	}

	/**
	 * Custom sort to figure the best hand in an array of hands
	 */
	
	
	public static Comparator<Hand> HandRank = new Comparator<Hand>() {

		public int compare(Hand h1, Hand h2) {

			int result = 0;

			result = h2.HandStrength - h1.HandStrength;

			if (result != 0) {
				return result;
			}
			
			result = h2.HiHand - h1.HiHand;
			if (result != 0) {
				return result;
			}
			
			result = h2.LoHand = h1.LoHand;
			if (result != 0) {
				return result;
			}

			result = h2.Kicker = h1.Kicker;
			if (result != 0) {
				return result;
			}

			return 0;
		}
	};
}
