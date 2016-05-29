package regexpeval;

import java.util.*;

/**
 * Created by Rayed Bin Wahed on 19-Feb-16.
 */
public class Dfa {

    public Map<Set<Integer>, Character> DStates;
    public Map<Map<Character, Character>, Character> Dtrans;
    public char start_state;
    public ArrayList<Character> final_states;
    private final Nfa nfa;

    public Dfa(Nfa nfa) {
        this.nfa = nfa;
        DStates = new HashMap<>();
        Dtrans = new HashMap<>();
        final_states = new ArrayList<>();
    }

    public Set<Integer> e_closure(Set<Integer> states) {
        Stack stack = new Stack<>();
        states.stream().forEach(state -> {
            stack.push(state);
        });
        Set<Integer> closure_states = new TreeSet<>(states);
        while (!stack.isEmpty()) {
            int state = (int) stack.pop();
            for (int i = 0; i < nfa.size; ++i) {
                if (nfa.transition_table[state][i] == RegexConstants.EPS) {
                    if (!closure_states.contains(i)) {
                        closure_states.add(i);
                        stack.push(i);
                    }
                }
            }
        }
        return closure_states;
    }

    public void subset_construct() {
        Queue<Set<Integer>> states = new LinkedList<>();
//        Stack<Set<Integer>> unmarked_states = new Stack<>();

        Set<Integer> nfa_initial = new TreeSet<>();
        nfa_initial.add(nfa.start_state);

        Set<Integer> dfa_initial = e_closure(nfa_initial);
        //System.out.println(dfa_initial);

        char id = 65;
        start_state = id;

        states.add(dfa_initial);
        DStates.put(dfa_initial, id);

        while (!states.isEmpty()) {
            Set<Integer> s = states.remove();
            for (char symbol : Nfa.symbols) {
                Set<Integer> U = e_closure(move(s, symbol));
                if (!DStates.containsKey(U)) {
                    char c = ++id;
                    DStates.put(U, c);
                    states.add(U);
                    if (U.contains(nfa.accept_state)) {
                        final_states.add(c);
                    }
                }
                Character m = DStates.get(s);
                Character n = DStates.get(U);
                Map<Character, Character> ma = new HashMap<>();
                ma.put(m, symbol);
                Dtrans.put(ma, n);
            }
        }

//        DStates.entrySet().stream().forEach((entry) -> {
//            while (entry.getValue() == 0){
//                entry.setValue(1);
//                for (char symbol : Nfa.symbols) {
//                    Set<Integer> U = e_closure(move(entry.getKey(), symbol));
//                    if (!DStates.containsKey(U)){
//                        DStates.put(U, 0);
//                    }
//                    Map<Set<Integer>, Character> m = new HashMap<>();
//                    m.put(entry.getKey(), symbol);
//                    Dtrans.put(m, U);
//                }
//            }
//        });
    }

    private Set<Integer> move(Set<Integer> states, char symbol) {
        Set<Integer> move = new TreeSet<>();
        states.stream().forEach(state -> {
            for (int i = 0; i < nfa.size; ++i) {
                if (nfa.transition_table[state][i] == symbol) {
                    move.add(i);
                }
            }
        });
        return move;
    }

    public void verify(String input) {
        char s = start_state;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            Map<Character, Character> m = new HashMap<Character, Character>();
            m.put(s, c);
            s = Dtrans.get(m);
        }
        if (final_states.contains(s)) {
            System.out.println("Input Accepted");
        } else {
            System.out.println("Input cannot be derived from regular expression");
        }
    }

    public void print() {
        System.out.println("");
//        for (Set<Integer> s : DStates) {
//            System.out.println(s);
//        }
//        System.out.println("");
//        for (Map.Entry<Map<Set<Integer>, Character>, Set<Integer>> entry : Dtrans.entrySet()) {
//            Map<Set<Integer>, Character> key = entry.getKey();
//            Set<Integer> value = entry.getValue();
//            System.out.println(key + " : " + value);
//        }
        DStates.entrySet().stream().forEach((entry) -> {
            Set<Integer> key = entry.getKey();
            Character value = entry.getValue();
            System.out.println(key + " : " + value);
        });

        System.out.println("");

        Dtrans.entrySet().stream().forEach((entry) -> {
            Map<Character, Character> key = entry.getKey();
            Character value = entry.getValue();
            key.entrySet().stream().forEach((entry1) -> {
                Character key1 = entry1.getKey();
                Character value1 = entry1.getValue();
                System.out.println(key1 + " : " + value1 + " => " + value);
            });
        });

        System.out.println("");

        System.out.print("Final States: ");
        final_states.stream().forEach((c) -> {
            System.out.print(c + " ");
        });
        
        System.out.println("");
    }

}
