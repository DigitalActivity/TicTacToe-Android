package tictactoe.tictactoe;

import android.os.Bundle;

/**
 * Classe Partie : Gestion de la table du jeu, positions et trouver s'il y a un gagnant
 * @author Younes rabdi
 */

class Partie {
    public static final int  NB_CASES  = 9;
    public static final char CASE_VIDE = '0';
    public static final char CASE_JOUEUR1 = '1';
    public static final char CASE_JOUEUR2 = '2';
    public char prochainAJouer = CASE_JOUEUR1;
    private final char[] table;
    public  char[] getTable() { return table; } // Table getter.

    /**
     * Constructeur par defaut
     */
    public Partie(Bundle etat) {
        table = new char[NB_CASES];
        // charger la table, Si une instance est sauvegardée
        if( etat != null) {
            prochainAJouer = etat.getChar(MainActivity.EXTRA_PROCHAIN_A_JOUER, CASE_JOUEUR1);
            for (int index = 0; index < NB_CASES; index++)
                table[index] = etat.getChar(String.valueOf(index), CASE_VIDE);
        }
        else { // Table vide, Si aucune instance sauvegardée
            for (int x = 0; x < NB_CASES; x++)
                table[x] = CASE_VIDE;
        }
    }

    /**
     * Placer le joueur à une posistion et chercher un gagnant
     * @param p_indexCase index de la case à occuper
     * @return char avec l'identifiant du joueur gagnant ou identifiant case vide sinon
     */
    public char jouer(int p_indexCase) {
        if(p_indexCase >= table.length || p_indexCase < 0) {
            return CASE_VIDE;
        }
        table[p_indexCase] = prochainAJouer;
        // Definir le prochain à jouer
        prochainAJouer = (prochainAJouer == CASE_JOUEUR1) ? CASE_JOUEUR2 : CASE_JOUEUR1;
        // retourner char correspondant à une case vide ou bien à un joueur gagnant
        return chercheGagnant();
    }

    /**
     * Reset la table du jeu
     */
    public void resetTable () {
        for (int x = 0; x < NB_CASES; x++)
            table[x] = CASE_VIDE;
    }

    /**
     * Verifier s'il y a un gagnant
     * @return char correspondant au joueur gagnant
     */
    public char chercheGagnant() {
        // Horizontalement
        for (int i = 0; i <= table.length * 2 / 3; i = i + 3) {
            if (table[i] == table[i+1] && table[i+1] == table[i+2] && table[i] != CASE_VIDE) {
                return table[i];
            }
        }
        // Vertical
        for (int i = 0; i < table.length / 3; i++) {
            if (table[i] == table[i+3] && table[i+3] == table[i+6] && table[i] != CASE_VIDE) {
                return table[i];
            }
        }
        // Diagonal
        if (table[0] == table[4] && table[4] == table[8] && table[0] != CASE_VIDE) {
            return table[0];
        }
        if (table[2] == table[4] && table[4] == table[6] && table[2] != CASE_VIDE) {
            return table[2];
        }
        return CASE_VIDE;
    }
}