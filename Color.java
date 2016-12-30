
/**
 * Abstraction of a Color. This holds a String representing the color; all Color
 * objects with identical String representations are equal
 *
 * @author Richard
 * @version Dec 30, 2016
 * @author Project: Brick-Pop-Solver
 *
 */
public class Color
{
    private String name;

    public static Color empty = null;


    /**
     * @param name
     *            this Color's string representation
     */
    public Color( String name )
    {
        this.name = name;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }


    /**
     * Returns this color's name
     * 
     * @return this color's name
     */
    public String getName()
    {
        return this.name;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other )
    {
        if ( !( other instanceof Color ) )
        {
            return false;
        }
        Color converted = (Color)other;
        return converted.getName().equals( this.getName() );
    }
}
