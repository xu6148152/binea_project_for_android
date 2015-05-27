package demo.binea.com.heatmapdemo.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class RacquetHeatmap {

    /**
     * Ellipse which focus is on the Y axis.
     */
    public static class Ellipse {
        private int a;
        private int b;
        private int squareA;
        private int squareB;

        public Ellipse(int a, int b) {
            this.a = a;
            this.b = b;
            this.squareA = a * a;
            this.squareB = b * b;
        }

        public boolean contain(int x, int y) {
            x -= b;
            y -= a;
            return (float) y * y / this.squareA + (float) x * x / this.squareB <= 1.0f;
        }
    }

    private static class RacquetPoint {
        public int x;
        public int y;

        public RacquetPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static Bitmap generate(Bitmap background, Rect drawArea, Ellipse keepRegionEllipse,
            float[][] points, int color, float degrees) {
        return generate(background, drawArea, keepRegionEllipse, points, color, degrees, true);
    }

    public static Bitmap generate(Bitmap background, Rect drawArea, Ellipse keepRegionEllipse,
            float[][] points, int color, float degrees, boolean groupingEnabled) {
        return generate(background, drawArea, keepRegionEllipse, points, color, degrees,
                groupingEnabled, 24);
    }

    /**
     * Generate a heatmap with specified background and render the points in the rectangle
     * specified by drawArea, rotation support is only available for square background.
     *
     * @param background the background of heatmap
     * @param drawArea rectangle for drawing points of heatmap
     * @param keepRegionEllipse the point in the ellipse will be rendered on the heatmap.
     * @param points the points for generating the heatmap
     * @param color the color used for rendering the heatmap
     * @param degrees rotate the generated heatmap with the specified rotation.
     * @param radius the radius of circle, which determines spread effect of a point in heatmap.
     */
    public static Bitmap generate(Bitmap background, Rect drawArea, Ellipse keepRegionEllipse,
            float[][] points, int color, float degrees, boolean groupingEnabled, int radius) {

        if (background == null) {
            throw new IllegalArgumentException("background cannot be null.");
        }

        if (points == null || points.length == 0) {
            return rotateAndKeepSize(background, degrees);
        }

        final int groupingThreshold = radius / 4;
        final int peaksRemovalThreshold = groupingThreshold * 2;
        final float peaksRemovalFactor = 0.4f;

        Bitmap heatmap = background.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(heatmap);

        int drawAreaWidth = drawArea.width();
        int drawAreaHeight = drawArea.height();

        // mapping the coordinate of points into draw area
        RacquetPoint[] racquetPoints = new RacquetPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            racquetPoints[i] = new RacquetPoint((int) ((points[i][0] + 1) * 0.5 * drawAreaWidth),
                    (int) (Math.abs(points[i][1] - 1) * 0.5 * drawAreaHeight));
        }

        int[] density = new int[drawAreaHeight * drawAreaWidth];
        int[] pointWeight = new int[points.length];

        // init weight to 100
        for (int i = 0; i < points.length; i++) {
            pointWeight[i] = 100;
        }

        // Grouping and filtering bunches of points in same location
        if (groupingEnabled) {
            for (int i = 0; i < points.length; i++) {
                if (pointWeight[i] > 0) {
                    for (int j = i + 1; j < points.length; j++) {
                        if (pointWeight[j] > 0) {
                            int currentDistance = isqrt((racquetPoints[i].x - racquetPoints[j].x)
                                    * (racquetPoints[i].x - racquetPoints[j].x)
                                    + (racquetPoints[i].y - racquetPoints[j].y)
                                    * (racquetPoints[i].y - racquetPoints[j].y));

                            if (currentDistance > peaksRemovalThreshold) {
                                currentDistance = peaksRemovalThreshold;
                            }

                            float K1 = 1 - peaksRemovalFactor;
                            float K2 = peaksRemovalFactor;

                            // Lowering peaks
                            pointWeight[i] = (int) (K1 * pointWeight[i]
                                    + K2 * pointWeight[i] * ((float) (currentDistance)
                                    / (float) peaksRemovalThreshold));

                            // Performing grouping if two points are closer than groupingThreshold
                            if (currentDistance <= groupingThreshold) {
                                // Merge i and j points. Store result in [i], remove [j]
                                racquetPoints[i].x = (racquetPoints[i].x + racquetPoints[j].x) / 2;
                                racquetPoints[i].y = (racquetPoints[i].y + racquetPoints[j].y) / 2;
                                pointWeight[i] = pointWeight[i] + pointWeight[j];

                                // pointWeight[j] is set negative to be avoided
                                pointWeight[j] = -10;

                                // Repeat again for new point
                                i--;
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Fill density info. Density is calculated around each point
        int maxDensity = density[0];
        int fromX, fromY, toX, toY;
        for (int i = 0; i < points.length; i++) {
            if (pointWeight[i] > 0) {
                fromX = racquetPoints[i].x - radius;
                fromY = racquetPoints[i].y - radius;
                toX = racquetPoints[i].x + radius;
                toY = racquetPoints[i].y + radius;

                if (fromX < 0) fromX = 0;
                if (fromY < 0) fromY = 0;
                if (toX > drawAreaWidth) toX = drawAreaWidth;
                if (toY > drawAreaHeight) toY = drawAreaHeight;

                for (int y = fromY; y < toY; y++) {
                    for (int x = fromX; x < toX; x++) {
                        if (keepRegionEllipse.contain(x, y)) {
                            int currentDistance =
                                    (x - racquetPoints[i].x) * (x - racquetPoints[i].x)
                                            + (y - racquetPoints[i].y) * (y - racquetPoints[i].y);

                            int currentDensity = radius - isqrt(currentDistance);
                            if (currentDensity < 0) currentDensity = 0;

                            int position = y * drawAreaWidth + x;
                            density[position] += currentDensity * pointWeight[i];
                            if (maxDensity < density[position]) maxDensity = density[position];
                        }
                    }
                }
            }
        }

        // set paint for rendering heatmap
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);

        // draw heatmap
        int i = 0;
        for (int y = 0; y < drawAreaHeight; y++) {
            for (int x = 0; x < drawAreaWidth; x++, i++) {
                if (density[i] > 0) {
                    float floatDensity = (float) density[i] / (float) maxDensity;
                    paint.setAlpha((int) (floatDensity * 255));
                    canvas.drawPoint(x + drawArea.left, y + drawArea.top, paint);
                }
            }
        }

        if (Float.compare(degrees, 0.0f) != 0) {
            Bitmap rotatedHeatmap = rotateAndKeepSize(heatmap, degrees);
            heatmap.recycle();
            return rotatedHeatmap;
        } else {
            return heatmap;
        }
    }

    private static Bitmap rotateAndKeepSize(Bitmap source, float degrees){
        if(Float.compare(degrees, 0.0f) == 0){
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight());
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        Bitmap rotatedHeatmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        Bitmap croppedHeatmap = Bitmap.createBitmap(rotatedHeatmap,
                (rotatedHeatmap.getWidth() - source.getWidth())/2,
                (rotatedHeatmap.getHeight()-source.getHeight())/2, source.getWidth(), source.getHeight());
        rotatedHeatmap.recycle();
        return croppedHeatmap;
    }
    /**
     * Get the integer sqrt of parameter x
     */
    private static int isqrt(int x) {
        int sqrttable[] = {
                0, 16, 22, 27, 32, 35, 39, 42, 45, 48, 50, 53, 55, 57, 59, 61, 64, 65, 67, 69, 71,
                73, 75, 76, 78, 80, 81, 83, 84, 86, 87, 89, 90, 91, 93, 94, 96, 97, 98, 99, 101,
                102, 103, 104, 106, 107, 108, 109, 110, 112, 113, 114, 115, 116, 117, 118, 119, 120,
                121, 122, 123, 124, 125, 126, 128, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137,
                138, 139, 140, 141, 142, 143, 144, 144, 145, 146, 147, 148, 149, 150, 150, 151, 152,
                153, 154, 155, 155, 156, 157, 158, 159, 160, 160, 161, 162, 163, 163, 164, 165, 166,
                167, 167, 168, 169, 170, 170, 171, 172, 173, 173, 174, 175, 176, 176, 177, 178, 178,
                179, 180, 181, 181, 182, 183, 183, 184, 185, 185, 186, 187, 187, 188, 189, 189, 190,
                191, 192, 192, 193, 193, 194, 195, 195, 196, 197, 197, 198, 199, 199, 200, 201, 201,
                202, 203, 203, 204, 204, 205, 206, 206, 207, 208, 208, 209, 209, 210, 211, 211, 212,
                212, 213, 214, 214, 215, 215, 216, 217, 217, 218, 218, 219, 219, 220, 221, 221, 222,
                222, 223, 224, 224, 225, 225, 226, 226, 227, 227, 228, 229, 229, 230, 230, 231, 231,
                232, 232, 233, 234, 234, 235, 235, 236, 236, 237, 237, 238, 238, 239, 240, 240, 241,
                241, 242, 242, 243, 243, 244, 244, 245, 245, 246, 246, 247, 247, 248, 248, 249, 249,
                250, 250, 251, 251, 252, 252, 253, 253, 254, 254, 255
        };

        int xn;

        if (x >= 0x10000) {
            if (x >= 0x1000000) {
                if (x >= 0x10000000) {
                    if (x >= 0x40000000) {
                        xn = sqrttable[x >> 24] << 8;
                    } else {
                        xn = sqrttable[x >> 22] << 7;
                    }
                } else {
                    if (x >= 0x4000000) {
                        xn = sqrttable[x >> 20] << 6;
                    } else {
                        xn = sqrttable[x >> 18] << 5;
                    }
                }

                xn = (xn + 1 + (x / xn)) >> 1;
                xn = (xn + 1 + (x / xn)) >> 1;

                return ((xn * xn) > x) ? --xn : xn;
            } else {
                if (x >= 0x100000) {
                    if (x >= 0x400000) {
                        xn = sqrttable[x >> 16] << 4;
                    } else {
                        xn = sqrttable[x >> 14] << 3;
                    }
                } else {
                    if (x >= 0x40000) {
                        xn = sqrttable[x >> 12] << 2;
                    } else {
                        xn = sqrttable[x >> 10] << 1;
                    }
                }

                xn = (xn + 1 + (x / xn)) >> 1;

                return ((xn * xn) > x) ? --xn : xn;
            }
        } else {
            if (x >= 0x100) {
                if (x >= 0x1000) {
                    if (x >= 0x4000) {
                        xn = (sqrttable[x >> 8]) + 1;
                    } else {
                        xn = (sqrttable[x >> 6] >> 1) + 1;
                    }
                } else {
                    if (x >= 0x400) {
                        xn = (sqrttable[x >> 4] >> 2) + 1;
                    } else {
                        xn = (sqrttable[x >> 2] >> 3) + 1;
                    }
                }

                return ((xn * xn) > x) ? --xn : xn;
            } else {
                if (x >= 0) {
                    return sqrttable[x] >> 4;
                } else {
                    return -1; // negative argument...
                }
            }
        }
    }
}
