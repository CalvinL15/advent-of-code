import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Hailstone {
    long[] position;
    long[] velocity;

    public Hailstone(long x, long y, long z, long vx, long vy, long vz) {
        this.position = new long[]{x, y, z};
        this.velocity = new long[]{vx, vy, vz};
    }

    public void adjustVelocity(long ax, long ay, long az) {
        this.velocity[0] -= ax;
        this.velocity[1] -= ay;
        this.velocity[2] -= az;
    }

    public static long[] findIntersection(Hailstone h1, Hailstone h2) {
        // division by zero
        if (h1.velocity[0] == 0 || h2.velocity[0] == 0) {
            return null; // Division by zero
        }
        long det = h1.velocity[0] * h2.velocity[1] - h1.velocity[1] * h2.velocity[0];
        // Check for parallel lines
        if ((h1.velocity[0] == h2.velocity[0] && h1.velocity[1] == h2.velocity[1]) || det == 0) {
            return null;
        }

        long dx = h2.position[0] - h1.position[0];
        long dy = h2.position[1] - h1.position[1];

        // Handling vertical lines
        if (h1.velocity[0] == 0 || h2.velocity[0] == 0) {
            long intX = (h1.velocity[0] == 0) ? h1.position[0] : h2.position[0];
            long intY = (h1.velocity[0] == 0) ?
                    h2.position[1] + (intX - h2.position[0]) * h2.velocity[1] / h2.velocity[0] :
                    h1.position[1] + (intX - h1.position[0]) * h1.velocity[1] / h1.velocity[0];

            return new long[]{intX, intY};
        }

        long tNumerator = dx * h2.velocity[1] - dy * h2.velocity[0];
        long sNumerator = dx * h1.velocity[1] - dy * h1.velocity[0];

        long t = tNumerator / det;
        long s = sNumerator / det;

        if (t < 0 || s < 0) {
            // Intersection happens in the past
            return null;
        }

        return new long[]{h1.position[0] + t * h1.velocity[0], h1.position[1] + t * h1.velocity[1]};
    }

    public static Long getZVelocityForIntersection(Hailstone h1, Hailstone h2, long[] intersectionPoint) {
        if (h1.velocity[0] == 0 || h2.velocity[0] == 0) {
            return null; // Division by zero
        }

        long t1 = (intersectionPoint[0] - h1.position[0]) / h1.velocity[0];
        long t2 = (intersectionPoint[0] - h2.position[0]) / h2.velocity[0];

        if (t1 == t2) {
            return null; // No unique solution for Z velocity
        }

        return (h1.position[2] - h2.position[2] + t1 * h1.velocity[2] - t2 * h2.velocity[2]) / (t1 - t2);
    }
}

public class Task24 {
    public static int countValidIntersections(ArrayList<Hailstone> hailstones, long rangeMin, long rangeMax) {
        int validIntersections = 0;

        for (int i = 0; i < hailstones.size() - 1; i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                long[] intersection = Hailstone.findIntersection(hailstones.get(i), hailstones.get(j));
                if (intersection != null &&
                        intersection[0] >= rangeMin && intersection[0] <= rangeMax &&
                        intersection[1] >= rangeMin && intersection[1] <= rangeMax) {
                    validIntersections++;
                }
            }
        }
        return validIntersections;
    }

    public static void main(String[] args) {
        File input = new File("2023/input.txt");
        String line;
        ArrayList<Hailstone> hailstones = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            while ((line = reader.readLine()) != null) {
                String[] coordAndVelo = line.split("@");
                String[] coordinates = coordAndVelo[0].split(", ");
                String[] velocities = coordAndVelo[1].split(", ");
                hailstones.add(new Hailstone(
                        Long.parseLong(coordinates[0]),
                        Long.parseLong(coordinates[1]),
                        Long.parseLong(coordinates[2].trim()),
                        Long.parseLong(velocities[0].trim()),
                        Long.parseLong(velocities[1]),
                        Long.parseLong(velocities[2])
                ));
            }
            long RANGE_MIN = 200_000_000_000_000L;
            long RANGE_MAX = 400_000_000_000_000L;
            System.out.println("task A: " + countValidIntersections(hailstones, RANGE_MIN, RANGE_MAX));
            // task B
            int N = 0;
            boolean foundSolution = false;
            long answer = 0;

            // iterate over all possible velocities, starting from 1 and -1
            while (!foundSolution) {
                for (int X = 0; X <= N; X++) {
                    int Y = N - X;
                    for (int negX : new int[]{-1, 1}) {
                        for (int negY : new int[]{-1, 1}) {
                            int aX = X * negX;
                            int aY = Y * negY;
                            long[] intersectionPoint = null;
                            Long commonZVelocity = null;

                            for (Hailstone hailstone : hailstones) {
                                hailstone.adjustVelocity(aX, aY, 0);
                            }

                            Hailstone h1 = hailstones.get(0);
                            for (int i = 1; i < hailstones.size(); i++) {
                                Hailstone h2 = hailstones.get(i);
                                long[] currentIntersection = Hailstone.findIntersection(h1, h2);
                                if (currentIntersection == null) {
                                    intersectionPoint = null;
                                    break;
                                }

                                if (intersectionPoint == null) {
                                    intersectionPoint = currentIntersection;
                                } else if (intersectionPoint[0] != currentIntersection[0] || intersectionPoint[1] != currentIntersection[1]) {
                                    intersectionPoint = null;
                                    break;
                                }
                            }

                            if (intersectionPoint != null) {
                                for (int i = 1; i < hailstones.size(); i++) {
                                    Hailstone h2 = hailstones.get(i);
                                    Long zVelocity = Hailstone.getZVelocityForIntersection(h1, h2, intersectionPoint);
                                    if (commonZVelocity == null) {
                                        commonZVelocity = zVelocity;
                                    } else if (zVelocity != null && !commonZVelocity.equals(zVelocity)) {
                                        commonZVelocity = null;
                                        break;
                                    }
                                }

                                if (commonZVelocity != null) {
                                    foundSolution = true;
                                    long Z = h1.position[2] + (intersectionPoint[0] - h1.position[0]) / h1.velocity[0] * (h1.velocity[2] - commonZVelocity);
                                    answer = intersectionPoint[0] + intersectionPoint[1] + Z;
                                    System.out.println("Velocity of the rock: " + aX + "," + aY + "," + commonZVelocity);
                                    // velocity for x,y,z = aX, aY, commonZVelocity
                                    break;
                                }
                            }

                            // Reset velocities
                            for (Hailstone hailstone : hailstones) {
                                hailstone.adjustVelocity(-aX, -aY, 0);
                            }
                        }
                        if (foundSolution) {
                            break;
                        }
                    }
                    if (foundSolution) {
                        break;
                    }
                }
                N++;
            }
            System.out.println("task B: " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
