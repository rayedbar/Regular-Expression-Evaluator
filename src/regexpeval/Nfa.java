/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexpeval;

/**
 * @author moham
 */
public class Nfa {

    public int size, start_state, accept_state;
    public char[][] transition_table;
    public static char[] symbols;
    
    static {
        symbols = new char[2];
        symbols[0] = 'a';
        symbols[1] = 'b';
    }

    public Nfa(int size, int start_state, int accept_state) {
        this.size = size;
        this.start_state = start_state;
        this.accept_state = accept_state;
        transition_table = new char[size][size];
        initialize(transition_table);
    }

    public Nfa(Nfa nfa) {
        size = nfa.size;
        start_state = nfa.start_state;
        accept_state = nfa.accept_state;
        transition_table = new char[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(nfa.transition_table[i], 0, transition_table[i], 0, size);
        }
    }

    private void initialize(char[][] table) {
        for (char[] table1 : table) {
            for (int i = 0; i < table.length; i++) {
                table1[i] = RegexConstants.PHI;
            }
        }
    }

    public void add_transition(int from, int to, char input_symbol) {
        transition_table[from][to] = input_symbol;
    }

    public void show() {
        System.out.println("There are a total of " + size + " states: " + 0 + " - " + (size - 1));
        System.out.println("Start state is: " + start_state);
        System.out.println("Accept state is: " + accept_state);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char symbol = transition_table[i][j];
                if (symbol != RegexConstants.PHI) {
                    System.out.println("Transition from " + i + " to " + j + " on " + "'" + symbol + "'");
                }
            }
        }
        System.out.println("");
    }
    public void showMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char symbol = transition_table[i][j];
                System.out.print(symbol + " ");
            }
            System.out.println("");
        }
    }
    public void append_empty_state() {
        char[][] new_transition_table = new char[size + 1][size + 1];
        initialize(new_transition_table);
        for (int i = 0; i < size; i++) {
            System.arraycopy(transition_table[i], 0, new_transition_table[i], 0, size);
        }
        transition_table = new_transition_table;
        size++;
    }

    public void shift_states(int shift) {
        int new_size = size + shift;
        //System.out.println("new size = " + new_size);
        char[][] new_transition_table = new char[new_size][new_size];
        initialize(new_transition_table);
        for (int i = 0; i < size; i++) {
            System.arraycopy(transition_table[i], 0, new_transition_table[i + shift], shift, size);
        }
        transition_table = new_transition_table;
        start_state += shift;
        accept_state += shift;
        size = new_size;
    }

    public void fill_states(Nfa nfa) {
        for (int i = 0; i < nfa.size; i++) {
            System.arraycopy(nfa.transition_table[i], 0, transition_table[i], 0, nfa.size);
        }
    }
}
