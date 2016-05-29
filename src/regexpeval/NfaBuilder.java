/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexpeval;

/**
 *
 * @author moham
 */
public class NfaBuilder {

    public static Nfa build_nfa_basic(char symbol) {
        Nfa nfa = new Nfa(2, 0, 1);
        nfa.add_transition(0, 1, symbol);
        return nfa;
    }

    public static Nfa build_nfa_alter(Nfa nfa1, Nfa nfa2) {
        nfa1.shift_states(1);
        nfa2.shift_states(nfa1.size);

        Nfa nfa = new Nfa(nfa2);
        nfa.fill_states(nfa1);

        nfa.add_transition(0, nfa1.start_state, RegexConstants.EPS);
        nfa.add_transition(0, nfa2.start_state, RegexConstants.EPS);
        nfa.start_state = 0;

        nfa.append_empty_state();
        nfa.accept_state = nfa.size - 1;
        nfa.add_transition(nfa1.accept_state, nfa.accept_state, RegexConstants.EPS);
        nfa.add_transition(nfa2.accept_state, nfa.accept_state, RegexConstants.EPS);

        return nfa;
    }

    public static Nfa build_nfa_star(Nfa nfa) {
        nfa.shift_states(1);
        nfa.append_empty_state();

        nfa.add_transition(nfa.accept_state, nfa.start_state, RegexConstants.EPS);
        nfa.add_transition(0, nfa.start_state, RegexConstants.EPS);
        nfa.add_transition(nfa.accept_state, nfa.size - 1, RegexConstants.EPS);
        nfa.add_transition(0, nfa.size - 1, RegexConstants.EPS);

        nfa.start_state = 0;
        nfa.accept_state = nfa.size - 1;

        return nfa;
    }

    static Nfa build_nfa_concat(Nfa nfa1, Nfa nfa2) {
        nfa2.shift_states(nfa1.size - 1);

        Nfa nfa = new Nfa(nfa2);
        nfa.fill_states(nfa1);
        nfa.start_state = nfa1.start_state;

        return nfa;
    }
}
