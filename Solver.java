import java.util.*;
import java.util.concurrent.*;


/**
 * We use ExecutorService and Callable to make Parallelism easy; this solution
 * is inherently parallel
 *
 * @author Richard
 * @version Dec 30, 2016
 * @author Project: Brick-Pop-Solver
 *
 */
class ParallelSolver implements Callable<ArrayList<Coordinate>>
{
    private Board moves;

    private ArrayList<Coordinate> f;


    /**
     * Constructs a new instance of ParallelSolver with specified first step
     * (already taken), and a game state
     * 
     * @param firstStep
     *            first step to reach this state
     * @param b
     *            current game state
     */
    public ParallelSolver( Coordinate firstStep, Board b )
    {
        f = new ArrayList<Coordinate>();
        f.add( firstStep );
        moves = b;
    }


    /**
     * Parallel-friendly DFS method for brick pop
     * 
     * @param available_moves
     *            Map representing list of available moves
     * @param steps
     *            past steps, used to track solution
     * @return an ArrayList of coordinates representing a solution if one
     *         exists, otherwise null
     */
    public ArrayList<Coordinate> parallel_search(
        TreeMap<Coordinate, Board> available_moves,
        ArrayList<Coordinate> steps )
    {
        // System.out.println( available_moves );
        for ( Coordinate move : available_moves.keySet() )
        {
            ArrayList<Coordinate> clonedSteps = cloneList( steps );
            clonedSteps.add( move );

            Board current = available_moves.get( move );

            if ( current.isSolved() )
                return clonedSteps;

            ArrayList<Coordinate> potentialSolution = parallel_search( current.availableMoves(), clonedSteps );
            if ( potentialSolution != null )
            {
                return potentialSolution;
            }
        }

        return null;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public ArrayList<Coordinate> call() throws Exception
    {
        if ( moves.isSolved() )
        {
            return f;
        }

        ArrayList<Coordinate> result = parallel_search( moves.availableMoves(), f );

        if ( result == null ) // sketch, but so is invokeAny
        {
            Thread.sleep( Long.MAX_VALUE );
        }

        return result;
    }


    /**
     * Private helper method, makes a deep copy of an ArrayList
     * 
     * @param input
     *            an ArrayList
     * @return deep copy of input
     */
    private <E> ArrayList<E> cloneList( ArrayList<E> input )
    {
        ArrayList<E> res = new ArrayList<E>( input.size() );
        for ( E element : input )
        {
            res.add( element );
        }
        return res;
    }

}


/**
 * Wrapper class for ParallelSolver. This is the single point of entry for the
 * computational meat of this project
 *
 * @author Richard
 * @version Dec 30, 2016
 * @author Project: Brick-Pop-Solver
 *
 */
public class Solver
{
    public static ArrayList<Coordinate> solve( Board b ) throws InterruptedException, ExecutionException
    {
        TreeMap<Coordinate, Board> available_moves = b.availableMoves();

        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<ArrayList<Coordinate>>> callables = new LinkedList<Callable<ArrayList<Coordinate>>>();
        for ( Coordinate initial : available_moves.keySet() )
        {
            Board temp = available_moves.get( initial );

            callables.add( new ParallelSolver( initial, temp ) );
        }

        return executor.invokeAny( callables );
    }

}
