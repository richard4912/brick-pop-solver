import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


/**
 * Helper class for loading images from disk
 *
 * @author Richard
 * @version Dec 30, 2016
 * @author Project: Brick-Pop-Solver
 *
 */
public class LoadImage
{
    /**
     * Helper method for reading images from disk
     * 
     * @param fileName
     *            path to image
     * @return integer array of RGB values representing an image's pixels
     * @throws IOException
     */
    public static int[][] getImage( String fileName ) throws IOException
    {
        BufferedImage image = ImageIO.read( new File( fileName ) );
        int[][] result = new int[image.getWidth()][image.getHeight()];

        for ( int x = 0; x < image.getWidth(); x++ )
        {
            for ( int y = 0; y < image.getHeight(); y++ )
            {
                result[x][y] = image.getRGB( x, y );
            }
        }

        return result;
    }


    /**
     * Uses black magic to pick out the proper pixels to define our starting
     * Board
     * 
     * @param fileName
     *            path to input image - make sure this is properly cropped
     * @return an 10x10 array of Colors representing bricks
     * @throws IOException
     */
    public static Color[][] getBoard( String fileName ) throws IOException
    {
        int[][] data = getImage( fileName );
        Color[][] img = new Color[10][10];

        int width = data.length;
        int height = data[0].length;

        for ( int x = 5; x < 100; x += 10 )
        {
            for ( int y = 95; y >= 0; y -= 10 )
            {
                img[9 - ( y - 5 ) / 10][( ( x - 5 ) / 10 )] = new Color(
                    "" + data[(int)( ( 0. + x ) / 100 * width )][(int)( ( 0. + y ) / 100 * height )] );
            }
        }

        return img;
    }

}
