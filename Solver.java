import java.util.*;
import java.util.concurrent.*;


class ParallelSolver implements Callable
{
    private TreeMap<Coordinate, Board> board;


    public ParallelSolver( TreeMap<Coordinate, Board> b, Coordinate sloc )
    {
        board = b;
    }


    public ArrayList<Coordinate> parallel_search(
        TreeMap<Coordinate, Board> available_moves,
        ArrayList<Coordinate> steps )
    {
        
        
        for(Coordinate move: available_moves.keySet())
        {
            ArrayList<Coordinate> clonedSteps = cloneList(steps);
        }
        
        return null;
    }


    @Override
    public ArrayList<Coordinate> call() throws Exception
    {
        return parallel_search( board, new ArrayList<Coordinate>() );
    }


    private <E> ArrayList<E> cloneList( ArrayList<E> input )
    {
        ArrayList<E> res = new ArrayList<E>(input.size());
        for(E element: input)
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

}
