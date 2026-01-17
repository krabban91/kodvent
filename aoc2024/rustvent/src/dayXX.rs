use ferris_says::say;
use std::io::{stdout, BufWriter};

fn main() {
    let stdo = stdout();
    let msg = String::from("Hello, world!");
    let w = msg.chars().count();
    let writer = BufWriter::new(stdo.lock());
    say(&msg, w, writer).unwrap()
}
