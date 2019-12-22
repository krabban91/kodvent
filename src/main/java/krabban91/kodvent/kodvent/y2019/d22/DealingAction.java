package krabban91.kodvent.kodvent.y2019.d22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DealingAction {
    static final int NEW_STACK = 0;
    static final int CUT = 1;
    static final int INCREMENT = 2;


    int type;

    int parameter;

    public DealingAction(String s) {
        if (s.equals("deal into new stack")) {
            type = NEW_STACK;
            parameter = 0;
        } else {

            String[] split = s.split(" ");
            if (split[0].equals("cut")) {
                type = CUT;
                parameter = Integer.parseInt(split[1]);
            }
            if (split[0].equals("deal")) {
                type = INCREMENT;
                parameter = Integer.parseInt(split[3]);
            }
        }

    }

    public List<Integer> handle(List<Integer> deck) {
        switch (type) {
            case NEW_STACK:
                ArrayList<Integer> integers = new ArrayList<>(deck);
                Collections.reverse(integers);
                return integers;
            case CUT:
                ArrayList<Integer> cutOut = new ArrayList<>();
                if(parameter>0){
                    cutOut.addAll(deck.subList(parameter, deck.size()));
                    cutOut.addAll(deck.subList(0, parameter));
                    return cutOut;
                } else {
                    cutOut.addAll(deck.subList(deck.size() + parameter, deck.size()));
                    cutOut.addAll(deck.subList(0,deck.size() + parameter));
                    return cutOut;
                }
            case INCREMENT:
                Map<Integer, Integer> incrementOut = new HashMap<>();
                for (int i = 0; i < deck.size(); i++) {
                    incrementOut.put((i*parameter)%deck.size(), deck.get(i));
                }
                return new ArrayList<>(incrementOut.values());
        }

        return deck;
    }
}
