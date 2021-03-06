package bam.bam.bam.modeles;

/**
 * @author Mabato
 *
 * Implémente les notes utilisateur
 */
public class UserNote {

    private float val;
    private int note;
    private int nbVotes;

    public UserNote()
    {
        this.note = 0;
        this.val = 0;
        this.nbVotes = 0;
    }
    public UserNote(float note)
    {
        this.val = note;
        this.note = (int)note;
    }
    public UserNote(float note, int nbVotes)
    {
        this.val = note;
        this.note = (int)note;
        this.nbVotes = nbVotes;
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

    public void setNbVotes(int nb)
    {
        this.nbVotes = nb;
    }
};
