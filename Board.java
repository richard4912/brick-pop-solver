import java.util.*;


/**
 * Class representing a game board state
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
        emptyColumn = new Color[input[0].length];
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
                if ( this.board[i][j] != Color.empty )
                    return false;
            }
        }
        return true;
    }


    /**
     * Returns the Color at a specific coordinate from this board
     * 
     * @param loc
     *            a coordinate
     * @return the Color at a specific coordinate from this board
     */
    public Color get( Coordinate loc )
    {
        return this.board[loc.i][loc.j];
    }


    /**
     * Checks if a given coordinate is valid
     * 
     * @param loc
     *            a coordinate
     * @return true iff loc is a valid Coordinate in the context of this Board
     */
    public boolean isValid( Coordinate loc )
    {
        return loc.i >= 0 && loc.j >= 0 && loc.i < this.board.length && loc.j < this.board[0].length;
    }


    /**
     * Pulls a column from this board; is helper method
     * 
     * @param columnNumber
     *            the number of the column to extract
     * @return the specified column from this board
     */
    private Color[] extractColumn( int columnNumber )
    {
        ArrayList<Color> col = new ArrayList<Color>();
        for ( Color[] i : this.board )
        {
            col.add( i[columnNumber] );
        }
        return col.toArray( new Color[col.size()] );
    }


    /**
     * Shrinks a column down, shifting empty spaces to the top of the column as
     * necessary
     * 
     * @param column
     *            an array of Colors representing a column from this board
     * @return a compressed column
     */
    private Color[] processColumn( Color[] column )
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


    /**
     * Removes empty columns and shifts non-empty columns left as necessary
     * 
     * @param columns
     *            an array of Color arrays, each of which represent a single
     *            column
     * @return transposed result, new board
     */
    public Color[][] compressRows( Color[][] columns )
    {
        ArrayList<Color[]> temp = new ArrayList<Color[]>( columns[0].length );
        int emptyCount = 0;
        for ( Color[] c : columns )
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

        Color[][] result = new Color[columns.length][columns[0].length];
        for ( int i = 0; i < board.length; i++ )
        {
            for ( int j = 0; j < board[i].length; j++ )
            {
                result[i][j] = temp.get( j )[i];
            }
        }

        return result;
    }


    /**
     * Returns a Map of Coordinates and Boards representing all possible moves
     * from this state; each Coordinate represents a brick pop, while each
     * corresponding board reflects game state after the relevant pop
     * 
     * @return a Map of Coordinates and Boards representing all possible moves
     *         from this state
     */
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
                    Board temp = this.pop_at( currentLocation );

                    if ( temp == null )
                    {
                        continue;
                    }

                    temp.contract();
                    if ( !results.containsValue( temp ) )
                    {
                        results.put( currentLocation, temp );
                    }
                }
            }
        }
        return results;
    }


    /**
     * Models the process of popping a Brick at a specified location
     * 
     * @param location
     *            a Coordinate in this board
     * @return a new Board representing the results of popping the specified
     *         brick
     */
    public Board pop_at( Coordinate location )
    {
        // gets all locations to pop
        TreeSet<Coordinate> toRemove = this.floodIndices( location );
        if ( toRemove.size() < 2 ) // is not a valid pop; no single bricks are
                                   // untouchable
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
        this.board = compressRows( columns );
    }


    /**
     * Returns a list of valid neighbors to location loc
     * 
     * @param loc
     *            query location
     * @return a list of valid neighbors to location loc
     */
    public ArrayList<Coordinate> getNeighbors( Coordinate loc )
    {
        ArrayList<Coordinate> possibilities = new ArrayList<Coordinate>();
        int[][] offset = { { -1, 0, 1, 0 }, { 0, 1, 0, -1 } };
        for ( int i = 0; i < offset[0].length; i++ )
        {
            Coordinate p = loc.offset( offset[0][i], offset[1][i] );
            if ( isValid( p ) )
                possibilities.add( p );
        }
        return possibilities;
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
        Queue<Coordinate> pq = new PriorityQueue<Coordinate>();
        pq.add( loc );

        TreeSet<Coordinate> indices = new TreeSet<Coordinate>();
        Color current = this.get( loc );

        if ( current == Color.empty )
        {
            return indices;
        }

        while ( !pq.isEmpty() )
        {
            Coordinate c = pq.poll();
            indices.add( c );

            for ( Coordinate n : getNeighbors( c ) )
            {

                if ( get( n ) != Color.empty && get( n ).equals( current ) && !indices.contains( n )
                    && !pq.contains( n ) )
                {
                    pq.add( n );
                }
            }
        }
        return indices;

    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer res = new StringBuffer( "" );
        for ( int i = 0; i < board.length; i++ )
        {
            for ( int j = 0; j < board[0].length; j++ )
            {
                res.append( this.board[board.length - 1 - i][j] + "\t" );
            }
            res.append( System.lineSeparator() );
        }
        return res.toString();
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other )
    {
        if ( !( other instanceof Board ) )
        {
            return false;
        }
        return this.toString().equals( other.toString() );
    }
}
