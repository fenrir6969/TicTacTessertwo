import java.awt.Polygon;

public class Polygons {

    //                                   0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16
    private static final int[] pointsX = {0, 57, 208, 25, 177, 97, 151, 85, 140, 98, 154, 86, 141, 61, 213, 30, 180};
    private static final int[] pointsY = {0, 23, 25, 75, 77, 83, 85, 102, 104, 135, 137, 155, 157, 164, 170, 215, 220};
    public static final Polygon[] polygons = new Polygon[26];

    static {
        initializePolygons();
    }

    private static Polygon makePolygon(int p1, int p2, int p3, int p4) {
        int[] x = {pointsX[p1], pointsX[p2], pointsX[p3], pointsX[p4]};
        int[] y = {pointsY[p1], pointsY[p2], pointsY[p3], pointsY[p4]};
        return new Polygon(x, y, 4);
    }

    private static void initializePolygons() {
        int offsetX = 870;
        int offsetY = 50;
        for (int i = 1; i < 17; i++) {
            pointsX[i] = pointsX[i] + offsetX;
            pointsY[i] = pointsY[i] + offsetY;
            System.out.println(pointsX[i] + " " + pointsY[i]);
        }
        polygons[1] = makePolygon(1, 2, 4, 3);
        polygons[2] = makePolygon(1, 2, 6, 5);
        polygons[3] = makePolygon(2, 4, 8, 6);
        polygons[4] = makePolygon(4, 3, 7, 8);
        polygons[5] = makePolygon(3, 1, 5, 7);
        polygons[6] = makePolygon(1, 2, 14, 13);
        polygons[7] = makePolygon(2, 4, 16, 14);
        polygons[8] = makePolygon(4, 3, 15, 16);
        polygons[9] = makePolygon(3, 1, 13, 15);
        polygons[10] = makePolygon(5, 6, 8, 7);
        polygons[11] = makePolygon(5, 6, 10, 9);
        polygons[12] = makePolygon(6, 8, 12, 10);
        polygons[13] = makePolygon(8, 7, 11, 12);
        polygons[14] = makePolygon(7, 5, 9, 11);
        polygons[15] = makePolygon(9, 10, 12, 11);
        polygons[16] = makePolygon(9, 10, 14, 13);
        polygons[17] = makePolygon(10, 12, 16, 14);
        polygons[18] = makePolygon(12, 11, 15, 16);
        polygons[19] = makePolygon(11, 9, 13, 15);
        polygons[20] = makePolygon(13, 14, 16, 15);
        polygons[21] = makePolygon(1, 5, 9, 13);
        polygons[22] = makePolygon(2, 6, 10, 14);
        polygons[23] = makePolygon(4, 8, 12, 16);
        polygons[24] = makePolygon(3, 7, 11, 15);
    }
}

