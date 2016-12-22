
public class Color
{
    private String name;

    public static Color empty = null;


    public Color( String name )
    {
        this.name = name;
    }


    @Override
    public String toString()
    {
        return this.name;
    }


    public String getName()
    {
        return this.name;
    }


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
