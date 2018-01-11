package tictactoe.tictactoe;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TableRow;

/**
 * BoutonCase : Gérer l’affichage et les évènements d’une case du jeu
 * Android lint seggere d'extend v7.widget.AppCompatButton au lieu de Button. (extend button marcherai aussi)
 * @author Younes rabdi
 */
class BoutonCase extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {
    // Chaque button garde le numero de la case qu'il represente
    private final int numeroCase;
    public int getNumeroCase() {
        return numeroCase;
    }

    /**
     * Cinstructeur BoutonCase
     * @param context widget context
     * @param p_numeroCase numero de la case représentée par le bouton
     */
    public BoutonCase(Context context, int p_numeroCase) {
        super(context);
        this.numeroCase = p_numeroCase;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int height = metrics.heightPixels / 5; // screen height par 5
        this.setHeight(height);
        // Taille du boutton depond de l'ecrant. Chaque bouton prends 1 des 3 places sur chaque ligne
        setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
    }

    @Override
    public void onClick(View v) {
        this.setEnabled(false);
    }
}