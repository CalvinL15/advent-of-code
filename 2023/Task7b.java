import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Task7b {
    public static void main(String[] args) {
        long totalWinnings = 0;
        File input = new File("input");
        ArrayList<Cards7b> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cardAndBid = line.split(" ");
                cards.add(new Cards7b(cardAndBid[0], Integer.parseInt(cardAndBid[1])));
            }
            cards.sort((c1, c2) -> {
                if (c1.cardType != c2.cardType) {
                    return Integer.compare(c1.cardType, c2.cardType);
                }
                return compareCard(c1.cardStr, c2.cardStr);
            });
            for (int i = 0; i<cards.size(); i++) {
                totalWinnings += (long) (i + 1) *cards.get(i).bid;
            }
            System.out.println(totalWinnings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int compareCard(String card1, String card2) {
        String cardOrder = "J23456789TJQKA";
        for (int i = 0; i < card1.length(); i++) {
            int rank1 = cardOrder.indexOf(card1.charAt(i));
            int rank2 = cardOrder.indexOf(card2.charAt(i));
            if (rank1 != rank2) {
                return Integer.compare(rank1, rank2);
            }
        }
        return 0;
    }
}

/* cardTypes =
    Five of a kind = 6
    Four of a kind = 5
    Full house = 4
    Three of a kind = 3
    Two pair = 2
    One pair = 1
    High card = 0
 */

class Cards7b {
    Map<Character, Integer> card;
    String cardStr;
    int bid;
    int cardType;
    public Cards7b(String card, int bid) {
        this.cardStr = card;
        this.card = new HashMap<>();
        for (int i = 0; i<card.length(); i++){
            this.card.put(card.charAt(i), this.card.getOrDefault(card.charAt(i), 0) + 1);
        }
        this.bid = bid;
        if (this.card.get('J') == null) {
            if (this.card.containsValue(5)){
                this.cardType = 6;
            } else if (this.card.containsValue(4)) {
                this.cardType = 5;
            } else if (this.card.containsValue(3) && this.card.containsValue(2)) {
                this.cardType = 4;
            } else if (this.card.containsValue(3)) {
                this.cardType = 3;
            } else if ((this.card.values().stream().filter(count -> count == 2).count() == 2)) {
                this.cardType = 2;
            } else if(this.card.containsValue(2)) {
                this.cardType = 1;
            } else {
                this.cardType = 0;
            }
        } else {
            int howManyJokers = this.card.get('J');
            if (howManyJokers == 5 || howManyJokers == 4)
                this.cardType = 6;
            else if (howManyJokers == 3) {
                if (this.card.containsValue(2)) {
                    this.cardType = 6;
                } else {
                    this.cardType = 5;
                }
            } else if (howManyJokers == 2) {
                if (this.card.containsValue(3)) {
                    this.cardType = 6;
                } else if ((this.card.values().stream().filter(count -> count == 2).count() == 2)) {
                    this.cardType = 5;
                } else {
                    this.cardType = 3;
                }
            } else {
                if (this.card.containsValue(4)) {
                    this.cardType = 6;
                } else if (this.card.containsValue(3)) {
                    this.cardType = 5;
                } else if ((this.card.values().stream().filter(count -> count == 2).count() == 2)) {
                    this.cardType = 4;
                } else if (this.card.containsValue(2)) {
                    this.cardType = 3;
                } else {
                    this.cardType = 1;
                }
            }
        }

    }
}
