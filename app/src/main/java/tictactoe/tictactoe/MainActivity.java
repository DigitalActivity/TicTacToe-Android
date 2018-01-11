package tictactoe.tictactoe;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activité principale.
 * @author Younes rabdi
 */
public class MainActivity extends AppCompatActivity {
    public  static final String EXTRA_PROCHAIN_A_JOUER = "prochain";
    private static final String EXTRA_ETAT_TABLE_JEU = "EtatTableJeu";
    private static final String EXTRA_SCORE_JOUEUR1 = "ScoreJoueur1";
    private static final String EXTRA_SCORE_JOUEUR2 = "ScoreJoueur2";
    private int scoreJoueur1 = 0;
    private int scoreJoueur2 = 0;
    private TableLayout tableJeu; // Table d'affichage du jeu
    private Partie partie;
    // Widgets, image assets
    private TextView text_score_joueur1;
    private TextView text_score_joueur2;
    private Drawable m_case_joueur1;
    private Drawable m_case_joueur2;
    private ImageView m_case_prochain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate(Bundle) called"); // Pour tests seulement

        // Créer une nouvelle partie, et charger les données si une sauvegarde existe
        partie = new Partie(savedInstanceState);
        // Restauration des données sauvegardées pour affichage
        if (savedInstanceState != null) {
            scoreJoueur1 = savedInstanceState.getInt(EXTRA_SCORE_JOUEUR1, 0);
            scoreJoueur2 = savedInstanceState.getInt(EXTRA_SCORE_JOUEUR2, 0);
            // recuperer la table du jeu
            char[] table = partie.getTable();
            if (savedInstanceState.getBundle(EXTRA_ETAT_TABLE_JEU) != null) {
                for (int index = 0; index < Partie.NB_CASES; index++) {
                    try { // Pas necessaire mais suggeré par andrdoid lint
                        table[index] = savedInstanceState.getBundle(EXTRA_ETAT_TABLE_JEU).getChar(String.valueOf(index), Partie.CASE_VIDE);
                    }
                    catch (NullPointerException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }
        // Relier les widgets du layout
        tableJeu = (TableLayout) findViewById(R.id.table_jeu);
        text_score_joueur1 = (TextView) findViewById(R.id.textView_scorejoueur1);
        text_score_joueur2 = (TextView) findViewById(R.id.textView_scorejoueur2);
        m_case_prochain = (ImageView) findViewById(R.id.imageViewProchain);
        m_case_joueur1  = ResourcesCompat.getDrawable(getResources(),R.drawable.x, null);
        m_case_joueur2  = ResourcesCompat.getDrawable(getResources(),R.drawable.o, null);
        m_case_prochain.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                (partie.prochainAJouer == Partie.CASE_JOUEUR1 ? R.drawable.x : R.drawable.o) , null));
        // Bouton reinisiliser
        Button m_button_reinitialiser = (Button) findViewById(R.id.button_reinitialiser);
        m_button_reinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //partie.prochainAJouer = Partie.CASE_JOUEUR1;
                partie.resetTable();
                m_case_prochain.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        (partie.prochainAJouer == Partie.CASE_JOUEUR1 ? R.drawable.x : R.drawable.o) , null));
                afficherTableJeu();
            }
        });
        // Adapter les drawables à l'ecrant
        Bitmap m_joueur1 = ((BitmapDrawable) m_case_joueur1).getBitmap();
        Bitmap m_joueur2 = ((BitmapDrawable) m_case_joueur2).getBitmap();
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        m_case_joueur1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(m_joueur1, (int) (50 * scale + 0.5f), (int) (50 * scale + 0.5f), false));
        m_case_joueur2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(m_joueur2, (int) (50 * scale + 0.5f), (int) (50 * scale + 0.5f), false));
        // Afficher les scores et la table du jeu
        text_score_joueur1.setText(String.valueOf(scoreJoueur1));
        text_score_joueur2.setText(String.valueOf(scoreJoueur2));
        afficherTableJeu();
    }

    // Sauvegarde données avant de restart Activité
    @Override
    public void onSaveInstanceState(Bundle SaveInstanceState) {
        super.onSaveInstanceState(SaveInstanceState);
        Log.i(TAG,"SaveInstanceState");
        SaveInstanceState.putInt(EXTRA_SCORE_JOUEUR1, scoreJoueur1);
        SaveInstanceState.putInt(EXTRA_SCORE_JOUEUR2, scoreJoueur2);
        SaveInstanceState.putChar(EXTRA_PROCHAIN_A_JOUER, partie.prochainAJouer);
        // Sauvegarder la table du jeu dans un bundle. Key c'est l'index et value c'est le contenu de la case
        char[] table = partie.getTable();
        for (int index = 0; index < table.length; index++) {
            SaveInstanceState.putChar(String.valueOf(index), table[index]);
        }
    }
    /**
     * Afficher la table du jeu
     */
    private void afficherTableJeu() {
        // Obtenir la table du jeu
        char[] table = partie.getTable();
        // vider le tableau d'Affichage actuel
        tableJeu.removeAllViews();
        TableRow ligne = new TableRow(this);
        // Mettre à jour l'image du prochain à jouer
        m_case_prochain.setImageDrawable(ResourcesCompat.getDrawable(getResources(), (partie.prochainAJouer == Partie.CASE_JOUEUR1 ? R.drawable.x : R.drawable.o) , null));
        // Afficher les cases, ligne par ligne
        for(int index = 0; index < table.length; index++) {
            // Créer ligne apres chaque 3 colonnes
            if(index % 3 == 0) {
                ligne = new TableRow(this);
                //adapter l'affichage pour 3 boutons
                ligne.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT, 3f));
                // Ajouter la ligne à la table
                tableJeu.addView(ligne);
            }
            // Buton pour chaque Case
            BoutonCase bc = new BoutonCase(this, index);

            switch(table[index]) {
                case Partie.CASE_JOUEUR1 : bc.setBackground(m_case_joueur1);
                    bc.setEnabled(false); // cases occupées sont desactivées
                    break;
                case Partie.CASE_JOUEUR2 : bc.setBackground(m_case_joueur2);
                    bc.setEnabled(false);
                    break;
                case Partie.CASE_VIDE : setOnClick(bc);
                    bc.setEnabled(partie.chercheGagnant() == Partie.CASE_VIDE); // Cases vides actives tant qu'il n'y a pas de gagnant
                    break;
            }
            // Ajouter le buton à la ligne
            ligne.addView(bc);
        }
    }

    /**
     * Definir un setOnclick pour ButtonCase
     * @param p_butonCase boutonCase à setter
     */
    private void setOnClick(final BoutonCase p_butonCase){
        p_butonCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Faire jouer
                char gagnant = partie.jouer(p_butonCase.getNumeroCase());
                afficherTableJeu();
                // désactiver le bouton
                p_butonCase.setEnabled(false);

                // Incrementer les scores au case ou il y a un gagnant
                switch(gagnant) {
                    case Partie.CASE_JOUEUR1 : scoreJoueur1++;
                        text_score_joueur1.setText(String.valueOf(scoreJoueur1));
                        Toast.makeText(MainActivity.this, "joueur #1 " + String.valueOf(getString(R.string.string_message_gagnant)), Toast.LENGTH_LONG).show();
                        break;
                    case Partie.CASE_JOUEUR2 : scoreJoueur2++;
                        Toast.makeText(MainActivity.this, "joueur #2 " + String.valueOf(getString(R.string.string_message_gagnant)), Toast.LENGTH_LONG).show();
                        text_score_joueur2.setText(String.valueOf(scoreJoueur2));
                }
            }
        });
    }

    // Sert seulement à visualiser les etats de l'application pendant les rotations/pauses...
    private static final String TAG = "MAinActivity";
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() est appelée"); }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() est appelée"); }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() est appelée"); }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() est appelée"); }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() est appelée"); }
}