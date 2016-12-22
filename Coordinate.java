
public class Coordinate implements Comparable<Coordinate>
{
    int i, j;


    public Coordinate( int i, int j )
    {
        this.i = i;
        this.j = j;
    }


    @Override
    public int compareTo( Coordinate other )
    {
        if ( this.i < other.i || ( this.i == other.i && this.j < other.j ) )
        {
            return -1;
        }

        if ( this.i > other.i || ( this.i == other.i && this.j > other.j ) )
        {
            return 1;
        }

        return 0;
    }


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


    public Coordinate offset( int i_off, int j_off )
    {
        return new Coordinate( this.i + i_off, this.j + j_off );
    }
}
