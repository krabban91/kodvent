package krabban91.kodvent.kodvent.y2015.d16;

public class Aunt {
    private final int number;
    private  Integer children;
    private Integer cats;
    private Integer samoyeds  ;
    private Integer pomeranians;
    private Integer akitas;
    private Integer vizslas;
    private Integer goldfish;
    private Integer trees;
    private Integer cars;
    private Integer perfumes;

    public Aunt(String intel) {
        String[] s = intel.split(" ");
        number = Integer.parseInt(s[1].replace(":", ""));
        String[] children = intel.split("children:");
        if(children.length>1){
            this.children = Integer.parseInt(children[1].split(",")[0].trim());
        }
        String[] cats = intel.split("cats:");
        if(cats.length>1){
            this.cats = Integer.parseInt(cats[1].split(",")[0].trim());
        }
        String[] samoyeds = intel.split("samoyeds:");
        if(samoyeds.length>1){
            this.samoyeds = Integer.parseInt(samoyeds[1].split(",")[0].trim());
        }
        String[] pomeranians = intel.split("pomeranians:");
        if(pomeranians.length>1){
            this.pomeranians = Integer.parseInt(pomeranians[1].split(",")[0].trim());
        }
        String[] akitas = intel.split("akitas:");
        if(akitas.length>1){
            this.akitas = Integer.parseInt(akitas[1].split(",")[0].trim());
        }
        String[] vizslas = intel.split("vizslas:");
        if(vizslas.length>1){
            this.vizslas = Integer.parseInt(vizslas[1].split(",")[0].trim());
        }
        String[] goldfish = intel.split("goldfish:");
        if(goldfish.length>1){
            this.goldfish = Integer.parseInt(goldfish[1].split(",")[0].trim());
        }
        String[] trees = intel.split("trees:");
        if(trees.length>1){
            this.trees = Integer.parseInt(trees[1].split(",")[0].trim());
        }
        String[] cars = intel.split("cars:");
        if(cars.length>1){
            this.cars = Integer.parseInt(cars[1].split(",")[0].trim());
        }
        String[] perfumes = intel.split("perfumes:");
        if(perfumes.length>1){
            this.perfumes = Integer.parseInt(perfumes[1].split(",")[0].trim());
        }
    }

    public boolean matchChildren(int in){
        return this.children == null || this.children == in;
    }

    public long getNumber() {
        return this.number;
    }

    public boolean matchCats(int in){
        return this.cats == null || this.cats == in;
    }
    public boolean greaterThanCats(int in) {
        return this.cats == null || this.cats > in;
    }

    public boolean matchSamoyeds(int in){
        return this.samoyeds == null || this.samoyeds == in;
    }

    public boolean matchPomeranians(int in){
        return this.pomeranians == null || this.pomeranians == in;
    }

    public boolean lessThanPomeranians(int in) {
        return this.pomeranians == null || this.pomeranians < in;
    }


    public boolean matchAkitas(int in){
        return this.akitas == null || this.akitas== in;
    }

    public boolean matchVizslas(int in){
        return this.vizslas == null || this.vizslas == in;
    }

    public boolean matchGoldfish(int in){
        return this.goldfish == null || this.goldfish == in;
    }

    public boolean lessThanGoldfish(int in) {
        return this.goldfish == null || this.goldfish < in;
    }

    public boolean matchTrees(int in){
        return this.trees == null || this.trees == in;
    }
    public boolean greaterThanTrees(int in) {
        return this.trees == null || this.trees > in;
    }


    public boolean matchCars(int in){
        return this.cars == null || this.cars == in;
    }

    public boolean matchPerfumes(int in){
        return this.perfumes == null || this.perfumes == in;
    }

}
