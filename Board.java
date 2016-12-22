import java.util.*;


/**
 * Class represents a game board state
 *
 * @author Richard
 * @version Dec 19, 2016
 * @author Project: Brick-Pop-Solver
 *
 */
public class Board
{
    private Color[][] board;

    private static Color[] emptyColumn;


    /**
     * @param input
     *            array of Colors representing Board data
     */
    public Board( Color[][] input )
    {
        this.board = input;
        for ( int i = 0; i < input[0].length; i++ )
        {
            emptyColumn[i] = Color.empty;
        }
    }


    /**
     * Determines if the board is fully solved
     * 
     * @return if board represents a fully solved one
     */
    public boolean isSolved()
    {
        for ( int i = 0; i < board.length; i++ )
        {
            for ( int j = 0; j < board[i].length; j++ )
            {
                if ( this.board[i][j] == Color.empty )
                    return false;
            }
        }
        return true;
    }


    public Color get( Coordinate loc )
    {
        return this.board[loc.i][loc.j];
    }


    public boolean isValid( Coordinate loc )
    {
        return loc.i >= 0 && loc.j >= 0 && loc.i < this.board.length && loc.j < this.board[0].length;
    }


    public Color[] extractColumn( int columnNumber )
    {
        ArrayList<Color> col = new ArrayList<Color>();
        for ( Color[] i : this.board )
        {
            col.add( i[columnNumber] );
        }
        return col.toArray( new Color[col.size()] );
    }


    public Color[] processColumn( Color[] column )
    {
        ArrayList<Color> temp = new ArrayList<Color>( column.length );
        int emptyCount = 0;
        for ( Color c : column )
        {
            if ( c != Color.empty )
                temp.add( c );
            else
                emptyCount++;
        }
        for ( int i = 0; i < emptyCount; i++ )
        {
            temp.add( Color.empty );
        }
        return temp.toArray( new Color[column.length] );
    }


    public Color[][] processColumns( Color[][] column )
    {
        ArrayList<Color[]> temp = new ArrayList<Color[]>( column[0].length );
        int emptyCount = 0;
        for ( Color[] c : column )
        {
            boolean emptyColumn = true;
            for ( Color k : c )
            {
                if ( k != Color.empty )
                    emptyColumn = false;
            }

            if ( !emptyColumn )
                temp.add( c );
            else
                emptyCount++;
        }
        for ( int i = 0; i < emptyCount; i++ )
        {
            temp.add( emptyColumn );
        }

        Color[][] result = new Color[column.length][column[0].length];
        for ( int i = 0; i < board.length; i++ )
        {
            for ( int j = 0; j < board[i].length; j++ )
            {
                result[i][j] = temp.get( j )[i];
            }
        }

        return result;
    }


    public TreeMap<Coordinate, Board> availableMoves()
    {
        TreeMap<Coordinate, Board> results = new TreeMap<Coordinate, Board>();

        for ( int i = 0; i < board.length; i++ )
        {
            for ( int j = 0; j < board[0].length; j++ )
            {
                Coordinate currentLocation = new Coordinate( i, j );
                Color element = get( currentLocation );
                if ( element != Color.empty )
                {
                    Board temp = this.pop_from( currentLocation );
                    if(!results.containsValue( temp ))
                    {
                        results.put( currentLocation, temp );
                    }
                }
            }
        }
        return results;
    }


    public Board pop_from( Coordinate location )
    {
        TreeSet<Coordinate> toRemove = this.floodIndices( location );
        if ( toRemove.size() < 2 )
            return null;

        Color[][] result = new Color[this.board.length][this.board[0].length];
        for ( int i = 0; i < board.length; i++ )
        {
            for ( int j = 0; j < board[0].length; j++ )
            {
                Coordinate temp = new Coordinate( i, j );
                if ( toRemove.contains( temp ) )
                {
                    result[i][j] = Color.empty;
                }
                else
                {
                    result[i][j] = get( temp );
                }
            }
        }
        Board rv = new Board( result );
        rv.contract();
        return rv;
    }


    /**
     * Reduces this board as if after a move
     */
    public void contract()
    {
        Color[][] columns = new Color[this.board[0].length][];
        for ( int i = 0; i < this.board[0].length; i++ )
        {
            columns[i] = processColumn( extractColumn( i ) );
        }
        this.board = processColumns( columns );
    }


    /**
     * Returns a list of valid neighbors to location loc
     * 
     * @param loc
     *            query location
     * @return a list of valid neighbors to location loc
     */
    public Coordinate[] getNeighbors( Coordinate loc )
    {
        ArrayList<Coordinate> possibilities = new ArrayList<Coordinate>();
        int[][] offset = { { -1, 0, 1, 0 }, { 0, 1, 0, -1 } };
        for ( int i = 0; i < offset.length; i++ )
        {
            Coordinate p = loc.offset( offset[i][0], offset[i][1] );
            if ( isValid( p ) )
                possibilities.add( p );
        }
        return possibilities.toArray( new Coordinate[possibilities.size()] );
    }


    /**
     * Returns a list of Coordinates in the same flood zone as the input
     * 
     * @param loc
     *            starting location
     * @return list of Coordinates in the same flood zone as loc
     */
    public TreeSet<Coordinate> floodIndices( Coordinate loc )
    {
        Queue<Coordinate> pq = new LinkedList<Coordinate>();
        pq.add( loc );

        TreeSet<Coordinate> indices = new TreeSet<Coordinate>();
        Color current = this.get( loc );

        while ( !pq.isEmpty() )
        {
            Coordinate c = pq.poll();
            indices.add( c );
            for ( Coordinate n : getNeighbors( c ) )
            {
                if ( get( n ) == current && !indices.contains( n ) )
                {
                    pq.add( n );
                }
            }
        }
        return indices;

    }


    @Override
    public String toString()
    {
        StringBuffer res = new StringBuffer( "{" );
        for ( int i = 0; i < board.length; i++ )
        {
            res.append( "{ " );
            for ( int j = 0; j < board[0].length; j++ )
            {
                res.append( this.board[i][j] + ", " );
            }
            res.append( "}, " );
        }
        res.append( "}" );
        return res.toString();
    }


    @Override
    public boolean equals( Object other )
    {
        if (!( other instanceof Board ))
        {
            return false;
        }
        return this.toString().equals(other.toString());
    }
}
