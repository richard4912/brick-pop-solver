import java.util.*;
import java.util.concurrent.*;


public class Runner
{
    public static void main( String[] args ) throws InterruptedException, ExecutionException
    {
        Color red = new Color( "red" );
        Color blue = new Color( "blue" );
        Color green = new Color( "green" );

        Color[][] test = new Color[10][10];
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                test[x][y] = red;
            }
        }

        // Coordinate tester = new Coordinate( 0, 0 );
        // System.out.println( tester );
        // System.out.println( tester.offset( -1, 1 ) );
        //
        // int[][] offset = { { -1, 0, 1, 0 }, { 0, 1, 0, -1 } };
        // for ( int i = 0; i < offset[0].length; i++ )
        // {
        // Coordinate p = tester.offset( offset[0][i], offset[1][i] );
        // System.out.println( "gn " + tester + " " + p );
        //
        // }
        //
        // System.exit( 0 );

        Board test1 = new Board( test );
//        // System.out.println( test1.availableMoves() );
//        for ( Board b : test1.availableMoves().values() )
//        {
//            System.out.println( b );
//            System.out.println( b.isSolved() );
//        }
        // System.out.println( test1.floodIndices( new Coordinate( 0, 0 ) ) );
        System.out.println( test1 );
        Solver.solve( test1 );
    }
}
