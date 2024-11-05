package graph.db.app;

import edu.princeton.cs.introcs.StdDraw;

public class DrawPaper {

    public static void main(String[] args) {
        System.out.println("Hi.");
        StdDraw.square(0.5, 0.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLUE);

        StdDraw.line(0.5, 0.5, 0.9, 0.5);
        StdDraw.line(0.9, 0.5, 0.5, 0.8);
        StdDraw.line(0.5, 0.8, 0.5, 0.5);
        StdDraw.circle(0.7, 0.65, 0.25);

        StdDraw.textLeft(0.01, 0.95, "Hello, World.");

        double paddingLeft = 0.01;
        double radius = 0.01;
        int numCircleRow = (int)(1/(paddingLeft+2*radius));
        for (int i = 0; i < numCircleRow; i++){
            StdDraw.circle(i*(paddingLeft + 2*radius) + paddingLeft + radius, 0.92, radius);
        }

    }
}