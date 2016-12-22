import java.util.*;
import java.util.concurrent.*;


class ParallelSolver implements Callable
{
    private TreeMap<Coordinate, Board> moves;


    public ParallelSolver( TreeMap<Coordinate, Board> b )
    {
        moves = b;
    }


    public ArrayList<Coordinate> parallel_search(
        TreeMap<Coordinate, Board> available_moves,
        ArrayList<Coordinate> steps )
    {

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


    @Override
    public ArrayList<Coordinate> call() throws Exception
    {
        ArrayList<Coordinate> result = parallel_search( moves, new ArrayList<Coordinate>() );
        while ( result == null ) // sketch, but so is invokeAny
        {
            int a = 1 + 1;
        }
        return result;
    }


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


public class Solver
{
    // The pixel offset distance between any two color blocks
    final int IMAGE_BLOCK_OFFSET = 48;

    // The vertical pixel offset from the top of the screen of the first color
    // block
    final int IMAGE_BLOCK_START_I = 213;

    // The horizontal pixel offset from the left of the screen of the first
    // color block
    final int IMAGE_BLOCK_START_J = 25;


    public static ArrayList<Coordinate> solve( Board b ) throws InterruptedException, ExecutionException
    {
        TreeMap<Coordinate, Board> available_moves = b.availableMoves();

        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<ArrayList<Coordinate>>> callables = new LinkedList<Callable<ArrayList<Coordinate>>>();
        for ( Coordinate initial : available_moves.keySet() )
        {
            Board temp = available_moves.get( initial );
//            System.out.println( initial + " " + temp);
            temp = temp.pop_from( initial );
//            System.out.println( initial);
            temp.contract();
            callables.add( new ParallelSolver( temp.availableMoves() ) );
        }

        ArrayList<Coordinate> result = executor.invokeAny( callables );
        System.out.println( result );

        return result;
    }

}
