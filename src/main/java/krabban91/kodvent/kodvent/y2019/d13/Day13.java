package krabban91.kodvent.kodvent.y2019.d13;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day13 {
    List<Long> in;

    public Day13() throws InterruptedException {
        System.out.println("::: Starting Day 13 :::");
        String inputPath = "y2019/d13/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        final LinkedBlockingDeque<Long> outputs = new LinkedBlockingDeque<>();
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), outputs);
        executor.execute(computer);
        final Map<Point, Tile> map = new HashMap<>();
        while (!computer.hasHalted() || !outputs.isEmpty()) {
            final Long x = computer.pollOutput(2);
            final Long y = computer.pollOutput(2);
            final Long type = computer.pollOutput(2);
            map.put(new Point(x.intValue(), y.intValue()), new Tile(type));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return map.values().stream()
                .filter(Tile::isBlock)
                .count();
    }


    public long getPart2() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        final LinkedBlockingDeque<Long> outputs = new LinkedBlockingDeque<>();
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), outputs);
        computer.setAddress(0, 2);
        executor.execute(computer);
        final Map<Point, Tile> map = new HashMap<>();
        Long score = null;
        Point ballHeading = new Point(1, 1);
        Point ball = null;
        Point paddle = null;
        while (!computer.hasHalted() || !outputs.isEmpty()) {

            while (map.size() < 740) {
                final Long x = computer.pollOutput(2);
                final Long y = computer.pollOutput(2);
                final Long type = computer.pollOutput(2);
                map.put(new Point(x.intValue(), y.intValue()), new Tile(type));
            }
            final Long x = computer.pollOutput(2);
            final Long y = computer.pollOutput(2);
            score = computer.pollOutput(2);
            System.out.println("X=" + x + ",Y=" + y + ", score:" + score);
            System.out.println(new LogUtils<Tile>().mapToText(map, Tile::toString));
            //keep same x as ball location
            if (ball == null) {
                ball = map.entrySet().stream().filter(e -> e.getValue().isBall()).map(value -> value.getKey()).findAny().get();
                paddle = map.entrySet().stream().filter(e -> e.getValue().isPaddle()).map(value -> value.getKey()).findAny().get();

            }

            Point newBallLocation = new Point(ball.x + ballHeading.x, ball.y + ballHeading.y);
            final Tile tile = map.get(newBallLocation);
            if (tile.isBlock()) {
                //remove block, flip y axis
                map.put(newBallLocation, new Tile(0L));
                ballHeading = new Point(ballHeading.x, -1 * ballHeading.y);
                //newBallLocation = new Point(ball.x+ballHeading.x, ball.y+ballHeading.y);

            } else if (tile.isWall()) {
                //flip x axis
                ballHeading = new Point(-1 * ballHeading.x, ballHeading.y);
                //newBallLocation = new Point(ball.x+ballHeading.x, ball.y+ballHeading.y);

            } else if (tile.isPaddle()) {
                ballHeading = new Point(ballHeading.x, -1 * ballHeading.y);
                //newBallLocation = new Point(ball.x+ballHeading.x, ball.y+ballHeading.y);
                //flip y axis

            } else {
                // air. just move
            }
            //Move ball
            map.put(ball, new Tile(0L));
            map.put(newBallLocation, new Tile(4L));
            Point futureBall = new Point(newBallLocation.x + ballHeading.x, newBallLocation.y + ballHeading.y);

            //Move paddle
            final int paddleDirection = Integer.compare(futureBall.x, paddle.x);
            Point nextPaddleLocation = new Point(paddle.x + paddleDirection, paddle.y);
            map.put(paddle, new Tile(0L));
            map.put(nextPaddleLocation, new Tile(3L));

            //give input
            ball = newBallLocation;
            paddle = nextPaddleLocation;
            computer.addInput(paddleDirection);

        }
        return score.longValue();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
