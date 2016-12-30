
/**
 * Abstraction for a Coordinate. Why do this over using a simple pair, or even
 * coordinates encoded as integers? Good question - adding in this abstraction
 * makes the code elsewhere easier to understand
 *
 * @author Richard
 * @version Dec 30, 2016
 * @author Project: Brick-Pop-Solver
 *
 */
public class Coordinate implements Comparable<Coordinate>
{
    int i, j;


    /**
     * @param i
     *            first value in a coordinate pair
     * @param j
     *            second value in a coordinate pair
     */
    public Coordinate( int i, int j )
    {
        this.i = i;
        this.j = j;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( Coordinate other )
    {
        if ( this.i < other.i || ( this.i == other.i && this.j < other.j ) )
        {
            return 1;
        }

        if ( this.i > other.i || ( this.i == other.i && this.j > other.j ) )
        {
            return -1;
        }

        return 0;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other )
    {
        if ( !( other instanceof Coordinate ) )
        {
            return false;
        }
        Coordinate converted = (Coordinate)other;
        return converted.i == this.i && converted.j == this.j;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "(" + i + ", " + j + ")";
    }


    /**
     * Returns a new Coordinate object shifted by the supplied offsets
     * 
     * @param i_off
     *            offset to i
     * @param j_off
     *            offset to j
     * @return a new Coordinate object shifted by the supplied offsets
     */
    public Coordinate offset( int i_off, int j_off )
    {
        return new Coordinate( this.i + i_off, this.j + j_off );
    }
}
