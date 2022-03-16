import java.sql.SQLOutput;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.util.Scanner;

public class MapDataDrawer
{

    private int[][] grid;

    public MapDataDrawer(String filename, int rows, int cols) throws FileNotFoundException {
        grid = new int[rows][cols];
        Scanner input = new Scanner(new File(filename));
        for (int ii = 0; ii<rows; ii++)
        {
            for (int jj = 0; jj<cols;jj++)
            {
                grid[ii][jj] = input.nextInt();
            }
        }
    }
    public int findMinValue()
    {
        int min = grid[0][0];
        for (int ii = 0; ii< grid.length; ii++)
        {
            for (int jj = 0; jj<grid[0].length;jj++)
            {
                if (grid[ii][jj]<min)
                {
                    min = grid[ii][jj];
                }
            }
        }
        return min;
    }
    public int findMaxValue(){
        int max = grid[0][0];
        for (int ii = 0; ii< grid.length; ii++)
        {
            for (int jj = 0; jj<grid[0].length;jj++)
            {
                if (grid[ii][jj]>max)
                {
                    max = grid[ii][jj];
                }
            }
        }
        return max;
    }
    public  int indexOfMinInCol(int col)
    {
        int minRow = 0;
        int minVal = grid[0][col];
        for (int ii = 0; ii< grid.length; ii++)
        {
            if (grid[ii][col]<minVal)
            {
                minVal = grid[ii][col];
                minRow = ii;
            }
        }
        return minRow;
    }

    /**
     * Draws the grid using the given Graphics object.
     * Colors should be grayscale values 0-255, scaled based on min/max values in grid
     */
    public void drawMap(Graphics g)
    {
        int max = findMaxValue();
        int min = findMinValue();
        int c;
        for (int ii = 0; ii< grid.length; ii++)
        {
            for (int jj = 0; jj<grid[0].length;jj++)
            {
                c = 255*(grid[ii][jj]-min)/(max-min);
                g.setColor(new Color(c, c, c));
                g.fillRect(jj,ii,1,1);
            }
        }
    }

    /**
     * Find a path from West-to-East starting at given row.
     * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
     * @return the total change in elevation traveled from West-to-East
     */
    public int drawLowestElevPath(Graphics g, int row)
    {
        g.setColor(new Color(0, 255, 0));
        g.fillRect(0,row,1,1);
        int total=0;
        int up;
        int down;
        for (int ii = 1; ii< grid[0].length; ii++)
        {
            if (row!=0)
            {
                up = Math.abs(grid[row-1][ii]-grid[row][ii-1]);
            }
            else
            {
                up = Integer.MAX_VALUE;
            }
            int next = Math.abs(grid[row][ii]-grid[row][ii-1]);
            if (row!=479)
            {
                down = Math.abs(grid[row+1][ii]-grid[row][ii-1]);
            }
            else
            {
                down = Integer.MAX_VALUE;
            }
            if (up<next && up<down)
            {
                row-=1;
                total+=up;
            }
            else if (next<up && next<down)
            {
                total+=next;
            }
            else if (down<next && down<up)
            {
                row+=1;
                total+=down;
            }
            else if (up == next)
            {
                total+=up;
            }
            else if(up == down)
            {
                if (Math.random()<.5)
                {
                    row-=1;
                }
                else
                {
                    row+=1;
                }
                total+=up;
            }
            else {
                total += next;
            }
            g.fillRect(ii,row,1,1);
        }
        return total;
    }

    /**
     * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
     */
    public int indexOfLowestElevPath(Graphics g)
    {
        int temp = Integer.MAX_VALUE;
        int row = 0;
        for (int ii = 0; ii<480; ii++)
        {
            int total = this.drawLowestElevPath(g, ii);

            if (total<temp)
            {
                temp = total;
                row = ii;
            }
        }
        return row;
    }
}