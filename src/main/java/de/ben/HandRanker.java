package de.ben;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandRanker {
    public enum HandRanking {
        HIGH_CARD(1),
        PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        STRAIGHT(5),
        FLUSH(6),
        FULL_HOUSE(7),
        FOUR_OF_A_KIND(8),
        STRAIGHT_FLUSH(9),
        ROYAL_FLUSH(10);

        private final int rank;

        HandRanking(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return this.rank;
        }
    }

    public HandRanking rankHand(List<Card> playerCards, List<Card> dealerCards) {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(playerCards);
        allCards.addAll(dealerCards);

        allCards.sort(Comparator.comparing(Card::getValue));

        if (isRoyalFlush(allCards)) {
            return HandRanking.ROYAL_FLUSH;
        } else if (isStraightFlush(allCards)) {
            return HandRanking.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(allCards)) {
            return HandRanking.FOUR_OF_A_KIND;
        } else if (isFullHouse(allCards)) {
            return HandRanking.FULL_HOUSE;
        } else if (isFlush(allCards)) {
            return HandRanking.FLUSH;
        } else if (isStraight(allCards)) {
            return HandRanking.STRAIGHT;
        } else if (isThreeOfAKind(allCards)) {
            return HandRanking.THREE_OF_A_KIND;
        } else if (isTwoPair(allCards)) {
            return HandRanking.TWO_PAIR;
        } else if (isPair(allCards)) {
            return HandRanking.PAIR;
        } else {
            return HandRanking.HIGH_CARD;
        }
    }

    private boolean isRoyalFlush(List<Card> cards) {
        if (!isFlush(cards)) {
            return false;
        }
        List<Integer> sortedValues = cards.stream()
                .map(Card::getValue)
                .sorted()
                .collect(Collectors.toList());
        return sortedValues.containsAll(Arrays.asList(10, 11, 12, 13, 14));
    }

    private boolean isStraightFlush(List<Card> cards) {
        return isStraight(cards) && isFlush(cards);
    }

    private boolean isFourOfAKind(List<Card> cards) {
        Map<Integer, Long> cardCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        return cardCounts.values().stream().anyMatch(count -> count == 4);
    }

    private boolean isFullHouse(List<Card> cards) {
        Map<Integer, Long> cardCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        return cardCounts.values().stream().anyMatch(count -> count == 3) &&
                cardCounts.values().stream().anyMatch(count -> count == 2);
    }


    private boolean isFlush(List<Card> cards) {
    Map<Card.Suit, Long> suitCounts = cards.stream()
            .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));
    return suitCounts.values().stream().anyMatch(count -> count == 5);
    }

    private boolean isStraight(List<Card> cards) {
        List<Integer> sortedValues = cards.stream()
                .map(Card::getValue)
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < sortedValues.size() - 4; i++) {
            if (sortedValues.get(i + 4) - sortedValues.get(i) == 4) {
                return true;
            }
        }
        // Check for straight with Ace as 1
        if (sortedValues.containsAll(Arrays.asList(14, 2, 3, 4, 5))) {
            return true;
        }
        return false;
    }

    private boolean isThreeOfAKind(List<Card> cards) {
        Map<Integer, Long> cardCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        return cardCounts.values().stream().anyMatch(count -> count == 3);
    }

    private boolean isTwoPair(List<Card> cards) {
        Map<Integer, Long> cardCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        return cardCounts.values().stream().filter(count -> count == 2).count() == 2;
    }

    private boolean isPair(List<Card> cards) {
        Map<Integer, Long> cardCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        return cardCounts.values().stream().anyMatch(count -> count == 2);
    }
}