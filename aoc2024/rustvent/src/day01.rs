use std::fs;

pub fn day() {
    let now = std::time::Instant::now();
    println!("Day 1");
    test_part1();
    test_part2();
    part1();
    part2();
    let elapsed = now.elapsed();
    println!("Day 1 done in: {:.2?}", elapsed);
}

fn read(path: &str) -> (Vec<(i64, i64)>, Vec<(i64, i64)>) {
    let input = fs::read_to_string(path).unwrap();
    let mut left: Vec<(i64, i64)> = Vec::new();
    let mut right: Vec<(i64, i64)> = Vec::new();
    input.split('\n').enumerate().for_each(|(index, line)| {
        if line.trim().is_empty() {
            return;
        }
        let parts: Vec<&str> = line.split_whitespace().collect();
        let l: i64 = parts[0].parse().unwrap();
        let r: i64 = parts[1].parse().unwrap();
        left.push((l, index as i64));
        right.push((r, index as i64));
    });
    left.sort();
    right.sort();
    (left, right)
}

fn test_part1() {
    let _sum = do_part1("input/test/day01.txt");
    println!("Test P1: Sum of abs differences: {}", _sum);
}

fn do_part1(path: &str) -> i64 {
    let (left, right) = read(path);
    let mut _sum = 0;
    for i in 0..left.len() {
        _sum += (left[i].0 - right[i].0).abs();
    }
    _sum
}

fn do_part2(path: &str) -> i64 {
    let mut _sum: i64 = 0;
    let (left, right) = read(path);
    for l in left.iter() {
        _sum += l.0 * right.clone().into_iter().filter(|e| e.0 == l.0).count() as i64;
    }
    _sum
}

fn test_part2() {
    let _sum = do_part2("input/test/day01.txt");
    println!("Test P2: Sum of abs differences: {}", _sum);

}
fn part1() {
    let _sum = do_part1("input/real/day01.txt");
    println!("Real P1: Sum of abs differences: {}", _sum);
}

fn part2() {
    let _sum = do_part2("input/real/day01.txt");
    println!("Real P2: Sum of abs differences: {}", _sum);

}
