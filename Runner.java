import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;


/**
 * Responsible for running the brick pop solver - incomplete, change filename in
 * source code for now.
 *
 * @author richard
 * @version Dec 30, 2016
 * @author Project: Brick-Pop-Solver
 */
public class Runner
{
    public static void main( String[] args ) throws InterruptedException, ExecutionException, IOException
    {
    	System.out.println("hello!");
        String filename = "test7.png";
        if ( args.length > 0 ) // replaces the filename from source if one is
                               // passed in by command line argument
        {
            filename = args[0];
        }
        Board test = new Board( LoadImage.getBoard( filename ) );

        ArrayList<Coordinate> solution = Solver.solve( test );

        // Verify correctness of solution by modeling it; if this errors, you
        // know something's wrong
        for ( Coordinate loc : solution )
        {
            System.out.println( loc );
            test = test.pop_at( loc );

            if ( test.isSolved() )
            {
                return;
            }
        }
        // If the code reaches this stage, the solution was incorrect
        System.exit( 1 );
    }
}
