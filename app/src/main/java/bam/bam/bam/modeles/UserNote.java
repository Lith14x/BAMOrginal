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

    public static UserNote getUserNote(int note)
    {
        switch(note)
        {
            case 0 :
                return N_0;
            case 1 :
                return N_1;
            case 2 :
                return N_2;
            case 3 :
                return N_3;
            case 4 :
                return N_4;
            case 5 :
                return N_5;
            default :
                return N_0;
        }
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
