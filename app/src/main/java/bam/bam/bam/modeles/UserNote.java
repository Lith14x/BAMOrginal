package bam.bam.bam.modeles;

/**
 * @author Mabato
 *
 * Implémente les notes utilisateur
 */
public enum UserNote {
    N_1(1),
    N_0(0),
    N_2(2),
    N_3(3),
    N_4(4),
    N_5(5);

    private float val;
    private int note;
    private int nbVotes;

    UserNote(int note)
    {
        this.val = note;
        this.note = note;
    }

    public float getVal()
    {
        return val;
    }

    public void setVal(float val)
    {
        this.val = val;
    }

    public int getNbVotes()
    {
        return nbVotes;
    }
};
